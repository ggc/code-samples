#include <linux/vmalloc.h>
#include <asm-generic/uaccess.h>
#include <asm-generic/errno.h>
#include <linux/semaphore.h>
#include <linux/spinlock.h>
#include <linux/proc_fs.h>
#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/workqueue.h>
#include "cbuffer.h"
#include <linux/random.h>
#include <linux/types.h>



MODULE_AUTHOR("Miguel Angel Perez");
MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("Modtimer");

#define MAX_SIZE_BUFF 10
#define streq(a,b) (strcmp((a),(b)) == 0)
DEFINE_SPINLOCK(mutex_cbuff); // Definicion spinlock buffer
DEFINE_SEMAPHORE(mutex_list); // Semaforo para las operaciones lista
struct semaphore cola; // Semaforo de cola

int timer_period = HZ/2;
int emergency_threshold = 80;
int max_random = 400;
static cbuffer_t *cbuff; // Estructura Buffer
struct work_struct my_work; //Definicion de trabajo
struct timer_list timer;
static char usado = false;
static char bloqueo = false;
static char espera = false;






MODULE_AUTHOR("Miguel Angel Perez");
MODULE_DESCRIPTION("Implementa una lista de enteros en un módulo"\
					"de kernel administrable por una entrada en /proc");
MODULE_LICENSE("GPL");
struct proc_dir_entry *proc_entry;


LIST_HEAD(mylist); 

/*
 *  Definición de manejo de elementos de la lista
 *
 */

typedef struct {
       int data;
       struct list_head links;
}list_item_t;




/*
 *	FUNCIONES DE MANEJO DE LISTA
 *	--------------------------------------
 */





/*********************************************************/
/********************  Utilidades  ***********************/
/*********************************************************/


/*Vacía la lista de list_item_t*/
void vacia_list_item(void){
    struct list_head* pos = mylist.next;
    struct list_head* posaux;
    list_item_t* item;
    printk(KERN_INFO "Vaciando la lista\n");

    while(!list_empty(&mylist)){
        item = list_entry(pos, list_item_t, links);
        posaux = pos->next;
        list_del(pos);
        vfree(item);
        pos = posaux;
    }
    //printk(KERN_INFO "Lista vacía\n");
}

/*
 *   Manejo de la entrada en /proc
 *
 */


static ssize_t modconfig_read(struct file *filp, char __user *buff, size_t len, loff_t *off) {
  
int cadena;
  if ((*off) > 0) 
    return 0;
   
    cadena = snprintf(buff, len,"timer_period = %d\nemergency_threshold = %d\nmax_random = %d\n", timer_period, emergency_threshold, max_random);
   
  *off+=len; 
    
  return cadena;
  
}

static ssize_t modconfig_write(struct file *file, const char __user *buffer, unsigned long count, loff_t *data)
{
	char line[25] = ""; // Un entero no puede ocupar más de 10 caracteres
	char command[20] = "";//Defino la operacion de 8 caracteres
	int num = 0;
	
	
	
	copy_from_user(line, buffer,count);
	line[count] = 0;
	
	
	sscanf(line,"%20s %d", command, &num);
	
	if(streq(command,"timer_period")){
		timer_period = num;
	}else if(streq(command,"emergency_threshold")){
		emergency_threshold = num;
	}else if(streq(command,"max_random")){
		max_random = num;
	}

	
	
	return count;
}

static ssize_t modtimer_read(struct file *file, char *user, size_t nbytes, loff_t *offset)
{
struct list_head* pos; 
    struct list_head* auxpos; 
    list_item_t* item;
    unsigned char aux[MAX_SIZE_BUFF]="";
    unsigned char aux2[10];
    int total =0;
    int subt =0;

   // printk(KERN_INFO "modtimer_read abierto\n");  

	
    /* 
     * Vaciar toda la lista que sea posible, dentro del tamaño máximo (512 
     * bytes es el máximo, así que hay que controlar que al copiar en una
     * cadena auxiliar no se sobrepase el buffer.
     */ 
 if(!total){
            bloqueo = true;
            up(&mutex_list);   
            if(down_interruptible(&cola)){
                bloqueo = false; 
                return 0;
            }
        }
        
    pos=mylist.next;
    while (pos!=&mylist) {
        auxpos = pos->next;
        item = list_entry(pos, list_item_t, links);
        subt=sprintf(aux2, "%in",item->data);

        if (subt+total>nbytes)
            break;

        strcat(aux,aux2); 
        total+=subt;

        list_del(pos);
        vfree(item);
       // printk(KERN_INFO "sacado de la lista\n");     

        pos = auxpos;       

    }
up(&mutex_list);
    //printk(KERN_INFO "Fuera del bucle, cadena copiada: %s\nTotal copiado:%d\nSizeof(aux):%lu\n",aux,total,sizeof(aux));
   

    if (copy_to_user(user,aux,total))
        return -1;

    //printk(KERN_INFO "modtimer_read cerrado\n");
    return total;
}



void fire_timer(unsigned long data){ 		

    unsigned int rnd = (get_random_int() % max_random);
    int cpu_actual=smp_processor_id();
    int items_cbuf;
    printk(KERN_INFO"Generado numero: %i\n", rnd);
    
   // printk(KERN_INFO "Entrando a fire_timer\n");


    spin_lock(&mutex_cbuff); // Inicio Sección crítica
    if(!is_full_cbuffer_t(cbuff))
    {
        insert_cbuffer_t(cbuff, (unsigned char)rnd);
        
    }
    
 /* Seleccion Cpu*/
		if (cpu_actual == 0){
			schedule_work_on(1, &my_work);
		}
		else{
			schedule_work_on(0, &my_work);
		}

    items_cbuf=size_cbuffer_t(cbuff);
    spin_unlock(&mutex_cbuff); //Fin sección critica

    //printk(KERN_INFO "workqueue_pendiente = %d, items_cbuf=%d\n, MAX_SIZE_BUFF = %d\n, emergency_threshold = %d\n, umbral = %d\n", workqueue_pendiente, items_cbuf, MAX_SIZE_BUFF, emergency_threshold, (MAX_SIZE_BUFF*emergency_threshold)/100);

    //Si el trabajo no estaba planificado y hace falta planificarlo
    if(espera == false && items_cbuf >= (MAX_SIZE_BUFF*emergency_threshold)/100 )
    {
        espera=true;
      
		 
  
       // printk(KERN_INFO "Planificado\n");
    }


    //Programar el timer

   // printk(KERN_INFO "Timer Programado\n");
    mod_timer(&timer, jiffies + timer_period);
   // printk(KERN_INFO "Saliendo de Fire_timer\n");
}

static int modtimer_open(struct inode *inode, struct file *file){
	//printk(KERN_INFO "entrando en modtimer_open\n");
	
	 // Inicio sección crítica
    down(&mutex_list);
    if(usado) {
        up(&mutex_list);
        printk(KERN_INFO"[modtimer] ERROR: no puede haber 2 lectores");
        return -EPERM;
    }

    usado = true;
    up(&mutex_list);
    // Fin sección crítica
	
        timer.expires = jiffies + timer_period;
        add_timer(&timer);
		

    try_module_get(THIS_MODULE);
    
    
	//printk(KERN_INFO "saliendo de modtimer_open\n");
    return 0;
}

static int modtimer_release(struct inode *inode, struct file *file){
	//printk(KERN_INFO "modtimer_release abierto\n");
	//Eliminar temporizador
	del_timer_sync(&timer);
	//printk(KERN_INFO "timer borrado\n");
	//Esperar a que todo el trabajo planificado termine
    flush_scheduled_work();
    //printk(KERN_INFO "workqueue finalizada\n");
    // Limpiamos las Estrucutras creadas.
    /*Vacía el buffer*/
    remove_cbuffer_t (cbuff);
    /*Vacía la lista */
    vacia_list_item();
    usado = false;
    printk(KERN_INFO "lista vacia\n");
   
    module_put(THIS_MODULE);
    //printk(KERN_INFO "modtimer_release cerrado\n");

    return 0;
}

/*
 *   Funciones de carga y descarga del módulo
 *
 *
 */

static const struct file_operations proc_entry_fops = {
    .read = modconfig_read,
    .write = modconfig_write,    
};

static const struct file_operations proc_entry_modtimer = {
    .read = modtimer_read,
    .open = modtimer_open,
    .release = modtimer_release,
    
};





static void work_generate (struct work_struct *work){	
  
    unsigned long flags;
    list_item_t * items[MAX_SIZE_BUFF];
    unsigned int numbers[MAX_SIZE_BUFF];
    int i;
    int nr_elems =0;
    //printk(KERN_INFO "Entrando en Fire Timer\n");


    spin_lock_irqsave(&mutex_cbuff, flags);
    nr_elems  = size_cbuffer_t(cbuff);
    for(i =0; i< nr_elems;i++){
        numbers[i] = (unsigned char)*head_cbuffer_t(cbuff);
        remove_cbuffer_t(cbuff);
        
    }
    
   

    spin_unlock_irqrestore(&mutex_cbuff, flags);

    for (i=0;i<nr_elems;i++){
        items[i] = (list_item_t*)vmalloc(sizeof(list_item_t));
        
    }

 
//inicio sección crítica
    down(&mutex_list);
    
    /* vaciar buffer y llenar cola enlazada */
    for (i=0;i<nr_elems;i++){
		items[i]->data = numbers[i];
        list_add_tail(&items[i]->links, &mylist);
    }


    
   
    
    if(bloqueo){
        bloqueo = false;
        up(&cola);
    }
    up(&mutex_list);
    //fin sección crítica
    
    // Entra sección critica
    spin_lock_irqsave(&mutex_cbuff, flags);
    espera = false;
    spin_unlock_irqrestore(&mutex_cbuff, flags);
    // Sal sección crítica
    
    //printk(KERN_INFO "Saliendo de Fire Timer\n");
}

        
    
    




int modtimer_init(void){
		/*Inicialización del timer*/
		timer.expires = jiffies + timer_period;
		timer.data = 0;
		timer.function = fire_timer;
		init_timer(&timer);
		//printk(KERN_INFO "creado timer\n");
		
		/*Crear entradas /proc*/
		proc_entry = proc_create("modconfig",0666,NULL, &proc_entry_fops);
		printk(KERN_INFO "Entrada /proc/modconfig creada.\n");
		proc_entry = proc_create("modtimer",0666,NULL, &proc_entry_modtimer);
		printk(KERN_INFO "Entrada /proc/modtimer creada.\n");
		
		/*Crear un buffer del tamño MAX_SIZE_BUFF y asignalo a cbuff*/
		cbuff = create_cbuffer_t(MAX_SIZE_BUFF);
		
		/*Inicializacion del trabajo*/
		
		INIT_WORK(&my_work, work_generate );
		//printk(KERN_INFO "creada workqueue\n");
		
		
		
		printk(KERN_INFO "módulo instalado correctamente\n");
		
		
		return 0;
}


void modtimer_clean(void){
	printk(KERN_INFO "Desinstalando módulo\n");
	remove_proc_entry("modconfig", NULL);
	printk(KERN_INFO "Entrada /proc/modconfig eliminada.\n");
	remove_proc_entry("modtimer", NULL);
	printk(KERN_INFO "Entrada /proc/modtimer eliminada.\n");
	destroy_cbuffer_t(cbuff);
	vacia_list_item();
}
module_init(modtimer_init);
module_exit(modtimer_clean);



