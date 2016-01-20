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

// Def structs
static struct proc_dir_entry *proc_entry; //crea entrada /proc
static struct timer_list timer;
static struct work_struct *work;
static struct list_head *lnls;
static struct cbuffer_t *cbuffer;
// Def variables
// Var de modconfig
static unsigned long timer_period=500;
static int max_random=100;
static unsigned int emergency_threshold=90;

//Lista enlazada
typedef struct {
  int data;
  struct list_head links;
}list_item_t;


////Funcionesstatic 
ssize_t modconfig_read(struct file *filp, char __user *buff, size_t len, loff_t *off){
	
  if ((*off) > 0) 
    return 0;
   
  //TIMER PERIOD EN MS
  int timer_periodms = timer_period * 10;
  int datos = snprintf(buff, len," timer_period_ms = %d\n emergency_threshold = %d\n max_random = %d\n", timer_periodms, emergency_threshold, max_random);
   
  *off+=len; 
    
  return datos;
}

static ssize_t modconfig_write(struct file *file, const char __user *buffer, unsigned long count, loff_t *data){
	
  char string[50] = "";
  int valor = 0;
	
	
  if (copy_from_user( string, buffer, count ))  
    return -EFAULT;
		
  if(sscanf(string, "timer_period_ms %d", &valor)){
    timer_period = valor;
  }
  if(sscanf(string, "emergency_threshold %d", &valor)){
    emergency_threshold = valor;
  }	
  if(sscanf(string, "max_random %d", &valor)){
    max_random = valor;
  }

  return count;
	
}


static int modtimer_open(struct inode *inode, struct file *file){
	
  down(&mtx);
  if(lector) {
    up(&mtx);
    return -EPERM;
  }

  lector = true;
  up(&mtx);
	
  timer.expires = jiffies + timer_period;
  add_timer(&timer);
	
  //Aumentar contador de referencia
  try_module_get(THIS_MODULE);

  return 0;
}


static int modtimer_release(struct inode *inode, struct file *file){
  struct list_head* cur_node=NULL;
  struct list_head* aux = NULL;
  list_item_t *item=NULL;
	
  //Desactivar temporizador
  del_timer_sync(&timer);
  //Terminar trabajo planificado
  flush_scheduled_work();
  //Vaciar buffer circular
  clear_cbuffer_t(cbuffer_t);
	
  //Vaciar lista enlazada
  list_for_each_safe(cur_node,aux, &lnls){
    item = list_entry(cur_node, list_item_t, links);
    list_del(&item->links);
  }
  //
  lector = false;
  //Decrementar contador de referencia
  module_put(THIS_MODULE);
	
  return 0;
}


static ssize_t modtimer_read(struct file *file, char *user, size_t nbytes, loff_t *offset){
  struct list_head* cur_node=NULL;
  list_item_t *item=NULL;
	
	
  unsigned char string1[MAX_SIZE_BUFF]="";
  unsigned char string2[10];
	
	
  //Borrar dato de la lista
  trace_printk("\nEntro eliminar todo\n");
  list_for_each_safe(cur_node,string1, &myList){
    item = list_entry(cur_node, list_item_t, links);
	  
    sprintf(string2, "%in",item->data);
    strcat(string1,string2);
	  
    list_del(&item->links);
    trace_printk("\nEliminado nodo\n");
  }
}


int modtimer_init(void){
	
  int ret = 0;
  //init LiNkedLiSt	
  LIST_HEAD(lnls); 
  // Create timer 
  init_timer(&timer);
  // Initialize field
  timer.data=0;
  timer.function=fire_timer;
  timer.expires=jiffies + timer_period; // Activate it one second from now
  // Activate the timer for the first time
  add_timer(&timer); 

  INIT_WORK(work,wq_function);
	
  proc_entry = proc_create( "modtimer", 0666, NULL, &proc_entry_fops_modtimer);
  if (proc_entry == NULL) {
    ret = -ENOMEM;
    printk(KERN_INFO "modtimer: Can't create /proc entry\n");
  } else {
    printk(KERN_INFO "modtimer: Module loaded\n");
  }

  proc_entry = proc_create( "modconfig", 0666, NULL, &proc_entry_fops_modconfig);
  if (proc_entry == NULL) {
    ret = -ENOMEM;
    printk(KERN_INFO "modconfig: Can't create /proc entry\n");
  } else {
    printk(KERN_INFO "modconfig: Module loaded\n");
  }
	
  cbuffer = create_cbuffer_t(MAX_SIZE_BUFF);
  INIT_WORK(&my_work, wq_function );
	
  return ret;
}

void modtimer_clean(void){
	
  remove_proc_entry("modtimer", NULL);
  printk(KERN_INFO "modtimer: Module unloaded.\n");
	
  remove_proc_entry("modconfig", NULL);
  printk(KERN_INFO "modconfig: Module unloaded.\n");
	
  destroy_cbuffer_t(cbuffer);
	
  //Vaciar lista enlazada
  list_for_each_safe(cur_node,aux, &lnls){
    item = list_entry(cur_node, list_item_t, links);
    list_del(&item->links);
  }
	
}


static const struct file_operacions proc_entry_fops_modconfig = {
  .read = modconfig_read,
  .write = modconfig_write,
};

static const struct file_operacions proc_entry_fops_modtimer = {
  .open = modtimer_open,
  .release = modtimer_release,
  .write = modtimer_write,
};

module_init(modtimer_init);
module_exit(modtimer_clean);

//###Timer###
//Function invoked when timer expires
static void fire_timer(unsigned long data){
  static int n = 0;
  unsigned int cpu=smp_processor_id();
  //spinlock_irqsave//
  // Genera num aleatorio
  n = get_random_int() % max_random;

  // Inserta el elem en cbuffer
  if(!is_full_cbuffer_t(cbuffer)){
    insert_cbuffer_t(cbuffer, n);
  }
  
  //flushworkpendingaqui//
  if(emergency_threshold<size_cbuffer_t(cbuffer)){
    //Inserta la tarea en la otra cpu
    schedule_work_on((cpu+1)%2, work);
  }
  //spinlock_irqrestore//
  /* Re-activate the timer one second from now */
  mod_timer( &(timer), jiffies + timer_period); 
}//// Tarea diferida
//Workqueue

//Mueve de cbuffer a lnls
static void wq_function(struct work_struct *work){
  int elem=0;
  unsigned long flags;
  list_item_t *tmp[size_cbuffer_t(cbuffer)];
  
  //Reserva de memoria
  while(elem<size_cbuffer_t(cbuffer)){
    tmp[elem]=(list_item_t *)vmalloc(sizeof(list_item_t));
    elem++;
  }
  
  spin_lock_irqsave(&mutex, flags);
  elem=0;
  while(!is_empty_cbuffer(cbuffer)){
    tmp[elem]->data=remove_cbuffer_t(cbuffer);
    list_add_tail(&tmp[elem]->links, &lnls);
    elem++;
  }
  spin_unlock_irqrestore(&mutex, flags);
  
  if(programaUsuarioBloqueado)
    up();

  
}



