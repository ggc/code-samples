#include <linux/random.h>
#include <linux/spinlock.h>
#include <linux/vmalloc.h>
#include <asm-generic/uaccess.h>
#include <asm-generic/errno.h>
#include <linux/semaphore.h>
#include <linux/proc_fs.h>
#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/workqueue.h>
#include <linux/types.h>
#include "cbuffer.h"

#define MAX_SIZE_KBUF 100

// Def structs
static struct proc_dir_entry *proc_entry; //crea entrada /proc
static struct timer_list timer;
static struct work_struct work;
static struct list_head lnls;
static cbuffer_t *cbuffer;
DEFINE_SPINLOCK(spin); // spinlock
DEFINE_SEMAPHORE(mtx); // lock-like
DEFINE_SEMAPHORE(empty); // bloquea cuando no hay numeros
DEFINE_SEMAPHORE(timerReady);

// Def variables
static char lector = false;
static int list_items = 0;
static int wq_waiting = 0;

// Var de modconfig
static unsigned long timer_period=HZ;
static unsigned int max_random=100;
static unsigned int emergency_threshold=90;

//Lista enlazada
typedef struct {
  int data;
  struct list_head links;
}list_item_t;

//PROTOTIPOS
static void fire_timer(unsigned long data);
static void wq_function(struct work_struct *work);

static ssize_t modconfig_write(struct file *file, const char __user *buffer, unsigned long count, loff_t *data);
static ssize_t modconfig_read(struct file *filp, char __user *buff, size_t len, loff_t *off);

static int modtimer_open(struct inode *inode, struct file *file);
static int modtimer_release(struct inode *inode, struct file *file);
static ssize_t modtimer_read(struct file *file, char __user *user, size_t nbytes, loff_t *offset);


////Funciones
static ssize_t modconfig_read(struct file *filp, char __user *buff, size_t len, loff_t *off){
  int timer_periodms;
  int datos;
	
  if ((*off) > 0) 
    return 0;
   
  //TIMER PERIOD EN MS
  timer_periodms = 1000/timer_period;
  datos = snprintf(buff, len," timer_period_ms = %d\n emergency_threshold = %d\n max_random = %d\n", timer_periodms, emergency_threshold, max_random); //Usar copy_to_user()
   
  *off+=len; 
    
  return datos;
}


static ssize_t modconfig_write(struct file *file, const char __user *buffer, unsigned long count, loff_t *data){
	
  char string[50] = "";
  int valor = 0;
	
  if (copy_from_user( string, buffer, count ))  
    return -EFAULT;
		
  if(sscanf(string, "timer_period_ms %d", &valor)){
    timer_period = 1000/valor;
  }
  if(sscanf(string, "emergency_threshold %d", &valor)){
    emergency_threshold = (int)valor;
  }	
  if(sscanf(string, "max_random %d", &valor)){
    max_random = (int)valor;
  }

  return count;	
}


static int modtimer_open(struct inode *inode, struct file *file){
  printk(KERN_INFO "modtimer open\n");

  down(&mtx);
  //Limita a 1 los lectores
  if(lector) {
    up(&mtx);
    return -EPERM;
  }else{
    lector = true;
  }
  up(&mtx);
 
  //Crea timer
  init_timer(&timer);
  //Inicializa los campos
  timer.data=0;
  timer.function=fire_timer;
  timer.expires=jiffies + HZ; //###timer_period
  //Activar el timer
  add_timer(&timer); 
    
  //Aumentar contador de referencia
  try_module_get(THIS_MODULE);
  
  return 0;
}


static ssize_t modtimer_read(struct file *file, char __user *buf, size_t nbytes, loff_t *offset){
  struct list_head* cur_node=NULL;
  struct list_head* aux_node=NULL;
  list_item_t *item=NULL;
  int length;
  unsigned char string[10];
  char kbuf[MAX_SIZE_KBUF]="";
  
  while(1){
    printk(KERN_INFO "modtimer read\n");
  
    if(down_interruptible(&mtx)){
      return -EINVAL;
    }//
    //Detener si esta vacia
    if(list_empty(&lnls)){
      up(&timerReady);
      if(down_interruptible(&empty)){
	return 0;
      }
    }
    
    //Borrar dato de la lista
    list_for_each_safe(cur_node, aux_node, &lnls){
      item = list_entry(cur_node, list_item_t, links);
	  
      sprintf(string, "%i\n",item->data);
      strcat(kbuf,string);

      list_del(&item->links);
      list_items = list_items - 1;
    }

    length = strlen(kbuf);//Cuidado
    printk(KERN_INFO "string=%s###len=%d\n",kbuf,length);

    if(copy_to_user(buf, kbuf, length)){
      printk(KERN_INFO "copytouser fallido\n");      
      return -EINVAL;
    }
    printk(KERN_INFO "copytouser exitoso\n");      
    up(&mtx);//
  }

  (*offset)+=nbytes;
  
  return length;
}


static int modtimer_release(struct inode *inode, struct file *file){
  struct list_head* cur_node=NULL;
  struct list_head* aux = NULL;
  list_item_t *item=NULL;
	
  printk(KERN_INFO "modtimer release\n");

  //Desactivar temporizador
  del_timer_sync(&timer);
  //Terminar trabajos planificados
  flush_scheduled_work();
  //Vaciar lista enlazada
  list_for_each_safe(cur_node,aux, &lnls){
    item = list_entry(cur_node, list_item_t, links);
    list_del(&item->links);//Libera los elem?
    list_items = list_items - 1;
  }
  
  lector = false;
  //Decrementar contador de referencia
  module_put(THIS_MODULE);
	
  return 0;
}


static const struct file_operations proc_entry_fops_modconfig = {
  .read = modconfig_read,
  .write = modconfig_write,
};

static const struct file_operations proc_entry_fops_modtimer = {
  .open = modtimer_open,
  .read = modtimer_read,
  .release = modtimer_release,
};

//###Timer###
//Funcion invocada cuando caduca el timer
static void fire_timer(unsigned long data){
  static unsigned int n = 0;
  unsigned long flags;
  unsigned int cpu=smp_processor_id();

  // Genera num aleatorio
  n = (unsigned int)get_random_int() % max_random;

  spin_lock_irqsave(&spin, flags);
  // Inserta el elem en cbuffer
  if(!is_full_cbuffer_t(cbuffer)){
    insert_cbuffer_t(cbuffer, n);
  }
  printk(KERN_INFO "Generado numero: %i\n", n);
  //  printk(KERN_INFO "thresh:%d\nsize:%d\n", emergency_threshold,size_cbuffer_t(cbuffer)*10);
  //Checkea umbral
  if(emergency_threshold<=size_cbuffer_t(cbuffer)*10 && !wq_waiting){
    //Inserta la tarea en la otra cpu
    schedule_work_on((cpu+1)%2, &work);
    wq_waiting=1;
  }
  spin_unlock_irqrestore(&spin, flags);
  
  // Reactiva el timer para timer_period ticks despues
  mod_timer( &timer, jiffies + timer_period); 
}


int modtimer_init(void){	
  int ret = 0;
  
  //Entrada modtimer
  proc_entry = proc_create( "modtimer", 0666, NULL, &proc_entry_fops_modtimer);
  if (proc_entry == NULL) {
    ret = -ENOMEM;
    printk(KERN_INFO "modtimer: Can't create /proc entry\n");
  } else {
    printk(KERN_INFO "/proc/modtimer created\n");
  }
  //Entrada modconfig
  proc_entry = proc_create( "modconfig", 0666, NULL, &proc_entry_fops_modconfig);
  if (proc_entry == NULL) {
    ret = -ENOMEM;
    printk(KERN_INFO "modconfig: Can't create /proc entry\n");
  } else {
    printk(KERN_INFO "/proc/modconfig created\n");
  }
  
  cbuffer = create_cbuffer_t(10);
  INIT_LIST_HEAD(&lnls); 
  INIT_WORK(&work, wq_function );
  
  return ret;
}


void modtimer_clean(void){
  struct list_head* cur_node=NULL;
  struct list_head* aux_node = NULL;
  list_item_t *item=NULL;
    
  remove_proc_entry("modtimer", NULL);
  printk(KERN_INFO "modtimer: Module unloaded.\n");
	
  remove_proc_entry("modconfig", NULL);
  printk(KERN_INFO "modconfig: Module unloaded.\n");
	
  destroy_cbuffer_t(cbuffer);
	
  //Vaciar lista enlazada
  list_for_each_safe(cur_node,aux_node, &lnls){
    item = list_entry(cur_node, list_item_t, links);
    list_del(&item->links);//###Libera memoria?
    list_items = list_items - 1;
  }
}


module_init(modtimer_init);
module_exit(modtimer_clean);


//###Tarea diferida###
//Workqueue: Mueve de cbuffer a lnls
static void wq_function(struct work_struct *work){
  int elem=0;
  unsigned long flags;
  list_item_t *tmp[size_cbuffer_t(cbuffer)];
  
  if(down_interruptible(&mtx)){
    return ;//Error
  }  
  //Reserva de memoria de elementos
  while(elem<size_cbuffer_t(cbuffer)){
    tmp[elem]=(list_item_t *)vmalloc(sizeof(list_item_t));
    elem++;
  }
  up(&mtx);

  down(&timerReady);
  
  spin_lock_irqsave(&spin, flags);
  elem=0;
  while(!is_empty_cbuffer_t(cbuffer)){
    tmp[elem]->data=remove_cbuffer_t(cbuffer);
    list_add_tail(&tmp[elem]->links, &lnls);
    elem++;
  }
  spin_unlock_irqrestore(&spin, flags);

  // Libera al lector
  wq_waiting=0;
  up(&empty);
  
}

MODULE_LICENSE("GPL");
