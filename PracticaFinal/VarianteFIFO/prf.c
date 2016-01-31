#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/vmalloc.h>
#include <asm-generic/uaccess.h>
#include <asm-generic/errno.h>
#include <linux/semaphore.h>
#include "cbuffer.h"
#include "list.h"
#define CBUF_MAX_SIZE 50
#define MAX_NAME_LENGTH 30

MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("Multififo");
MODULE_AUTHOR("Alejandro Sacanell");

static struct proc_dir_entry *proc_entry;
struct proc_dir_entry *proc_parent;
struct list_head myList;

typedef struct {
	cbuffer_t* cbuffer;
	int prod_count;
	int cons_count;
	int nr_waiting_prod;
	int nr_waiting_cons;
	struct semaphore mtx;
	struct semaphore sem_prod;
	struct semaphore sem_cons;
}entryStruct;

typedef struct {
	char name[MAX_NAME_LENGTH];
	entryStruct* syncro;
	struct list_head links;
}list_item_t;

//PROTOTIPOS///////////////////
static int fifodefault_open(struct inode * inode, struct file *fd);
static int fifodefault_release(struct inode * inode, struct file * fd);
static ssize_t fifodefault_read(struct file * fd, char * buffer, size_t size, loff_t * offset);
static ssize_t fifodefault_write(struct file * fd, const char *buffer, size_t size, loff_t * offset);

static ssize_t fifocontrol_write(struct file * fd, const char *buffer, size_t size, loff_t * offset);

///////////////////////////////

static const struct file_operations proc_entry_fops_default = {
    .open = fifodefault_open,
    .release = fifodefault_release,
    .read = fifodefault_read,
    .write = fifodefault_write,    
};

static const struct file_operations proc_entry_fops_control = {
    .write = fifocontrol_write,  
};




/* Se invoca al hacer open() de entrada /proc/default */
static int fifodefault_open(struct inode * inode, struct file *fd){
	entryStruct* private_data=(entryStruct*)PDE_DATA(fd->f_inode);
	
	/* "Adquiere" el mutex */
	if (down_interruptible(&(private_data->mtx))) 
	return -EINTR;
	
	if(fd->f_mode & FMODE_READ){
		
		private_data->cons_count++;
		//cond_signal(prod);
		if (private_data->nr_waiting_prod>0) { \
			/* Despierta a uno de los hilos bloqueados */ \
			up(&(private_data->sem_prod)); \
			private_data->nr_waiting_prod--; \
		}
		
		while(private_data->prod_count == 0){
			//cond_wait(cons, mtx);
			private_data->nr_waiting_cons++; 
			/*Libera el mutex*/
			up(&(private_data->mtx)); 
			/*Se bloquea en la cola*/
			if (down_interruptible(&(private_data->sem_cons))){ 
				down(&(private_data->mtx)); 
				private_data->nr_waiting_cons--; 
				up(&(private_data->mtx)); 
				return -EINTR; 
			} 
			/*Adquiere el mutex*/
			if (down_interruptible(&(private_data->mtx))){ 
				return -EINTR; 
			}		
		}	
		
	}else{ //El productor abre el FIFO
		private_data->prod_count++;
		//cond_signal(cons);
		if (private_data->nr_waiting_cons>0) { \
			/* Despierta a uno de los hilos bloqueados */ \
			up(&(private_data->sem_cons)); \
			private_data->nr_waiting_cons--; \
		}
		while(private_data->cons_count == 0){
			//cond_wait(prod, mtx);
			private_data->nr_waiting_prod++; 
			/*Libera el mutex*/
			up(&(private_data->mtx)); 
			/*Se bloquea en la cola*/
			if (down_interruptible(&(private_data->sem_prod))){ 
				down(&(private_data->mtx)); 
				private_data->nr_waiting_prod--; 
				up(&(private_data->mtx)); 
				return -EINTR; 
			} 
			/*Adquiere el mutex*/
			if (down_interruptible(&(private_data->mtx))){ 
				return -EINTR; 
			}	
		}	
	}
	
	/* "Libera" el mutex */ 
	up(&(private_data->mtx));
	
	return 0;
}

/* Se invoca al hacer close() de entrada /proc/default */
static int fifodefault_release(struct inode * inode, struct file * fd){
	entryStruct* private_data=(entryStruct*)PDE_DATA(fd->f_inode);
	
	
  	/* "Adquiere" el mutex */
	if (down_interruptible(&(private_data->mtx))) 
	return -EINTR;
	
	if(fd->f_mode & FMODE_READ){ //El consumidor cierra el FIFO
		private_data->cons_count--;
		//cond_signal(prod);
		if (private_data->nr_waiting_prod>0) { 
			/* Despierta a uno de los hilos bloqueados */ 
			up(&(private_data->sem_prod)); 
			private_data->nr_waiting_prod--; 
		}
	}else{
		private_data->prod_count--;
		//cond_signal(cons);
		if (private_data->nr_waiting_cons>0) { \
			/* Despierta a uno de los hilos bloqueados */ \
			up(&(private_data->sem_cons)); \
			private_data->nr_waiting_cons--; \
		}
	}
	if (private_data->cons_count == 0 && private_data->prod_count ==0)
	clear_cbuffer_t(private_data->cbuffer);
	up(&(private_data->mtx));
	
	return 0;
}

/* Se invoca al hacer read() de entrada /proc/default */
static ssize_t fifodefault_read(struct file * fd, char * buffer, size_t size, loff_t * offset){
	
	entryStruct* private_data=(entryStruct*)PDE_DATA(fd->f_inode);
	
	char kbuf[CBUF_MAX_SIZE];
	
	if(size>CBUF_MAX_SIZE)
    {return -ENOSPC;}
	
	if(down_interruptible(&(private_data->mtx)))
    {return -EINTR;}
	
	//No items necesarios
	while( (size_cbuffer_t(private_data->cbuffer)<size) && (private_data->prod_count>0)){
		//cond_wait(cons, mtx);
		private_data->nr_waiting_cons++; 
		/*Libera el mutex*/
		up(&(private_data->mtx)); 
		/*Se bloquea en la cola*/
		if (down_interruptible(&(private_data->sem_cons))){ 
			down(&(private_data->mtx)); 
			private_data->nr_waiting_cons--; 
			up(&(private_data->mtx)); 
			return -EINTR; 
		} 
		/*Adquiere el mutex*/
		if (down_interruptible(&(private_data->mtx))){ 
			return -EINTR; 
		}
	}
	
	//Vacio y no productores
	if ((is_empty_cbuffer_t(private_data->cbuffer)) && (private_data->prod_count == 0)){
		up(&(private_data->mtx));
		return 0;  //EOF
	}
	
	remove_items_cbuffer_t(private_data->cbuffer,kbuf,size);
	
	//cond_signal(prod);
	if (private_data->nr_waiting_prod>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
		up(&(private_data->sem_prod)); \
		private_data->nr_waiting_prod--; \
	}
	up(&(private_data->mtx));
	
	copy_to_user(buffer, kbuf, size);
	
	return size;
}

/* Se invoca al hacer write() de entrada /proc/default */
static ssize_t fifodefault_write(struct file * fd, const char *buffer, size_t size, loff_t * offset){
	entryStruct* private_data=(entryStruct*)PDE_DATA(fd->f_inode);
	
	char kbuf[CBUF_MAX_SIZE];
	
	if (size > CBUF_MAX_SIZE) { return -EINVAL;}
	if(copy_from_user(kbuf,buffer,size)){ return -ENOMEM;}
	
  	/* "Adquiere" el mutex */
	if (down_interruptible(&(private_data->mtx))) 
	return -EINTR;
	
	
	/* Esperar hasta que haya hueco para insertar (debe haber consumidores) */ 
    while(nr_gaps_cbuffer_t(private_data->cbuffer) < size && private_data->cons_count > 0){
		//cond_wait(prod,mtx);
        private_data->nr_waiting_prod++; 
		/*Libera el mutex*/
        up(&(private_data->mtx)); 
		/*Se bloquea en la cola*/
        if (down_interruptible(&(private_data->sem_prod))){ 
			down(&(private_data->mtx)); 
			private_data->nr_waiting_prod--; 
			up(&(private_data->mtx)); 
			return -EINTR; 
		} 
		/*Adquiere el mutex*/
        if (down_interruptible(&(private_data->mtx))){ 
			return -EINTR; 
		}
	}
	
	/* Detectar fin de comunicación por error (consumidor cierra FIFO antes) */ 
	if (private_data->cons_count==0) {up(&(private_data->mtx)); return -EPIPE;} 
	
	insert_items_cbuffer_t(private_data->cbuffer,kbuf,size); 
	
	/* Despertar a posible consumidor bloqueado */ 
	//cond_signal(cons);
	if (private_data->nr_waiting_cons>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
		up(&(private_data->sem_cons)); \
		private_data->nr_waiting_cons--; \
	}
	up(&(private_data->mtx));
	return size;
}













/* Se invoca al hacer write() de entrada /proc/control */
static ssize_t fifocontrol_write(struct file * fd, const char *buffer, size_t size, loff_t * offset){
	char kbuf[CBUF_MAX_SIZE]="";
	char name[MAX_NAME_LENGTH]="";
	list_item_t *tmp;
	int encontrado = 0;
	
	
	struct list_head* cur_node=NULL;
	list_item_t *item=NULL;
	struct list_head* aux = NULL;
	
	if(copy_from_user(kbuf,buffer,size)){
		return -ENOMEM;
	}
	
	if(sscanf(kbuf, "create %s", name)){ 
		tmp = (list_item_t*)vmalloc(sizeof(list_item_t));
		tmp->syncro = (entryStruct*)vmalloc(sizeof(entryStruct));
		tmp->syncro->cbuffer = create_cbuffer_t(CBUF_MAX_SIZE);
		tmp->syncro->prod_count = 0;
		tmp->syncro->cons_count = 0;
		tmp->syncro->nr_waiting_prod=0;
		tmp->syncro->nr_waiting_cons=0;
		sema_init(&(tmp->syncro->mtx),1);
		sema_init(&(tmp->syncro->sem_prod),0);
		sema_init(&(tmp->syncro->sem_cons),0);
		if(!(tmp->syncro->cbuffer)){
			return -ENOMEM;
		}
		
		proc_entry = proc_create_data(name,0666, proc_parent, &proc_entry_fops_default, tmp->syncro);
		if(proc_entry==NULL){
			destroy_cbuffer_t(tmp->syncro->cbuffer);
			vfree(tmp->syncro);
			vfree(tmp);
			printk(KERN_INFO "multififo/%s: Can't create /proc entry\n", name);
			return -ENOMEM;
		}else{
			strcpy(tmp->name, name);
			list_add_tail(&tmp->links, &myList);
			printk(KERN_INFO "Entry %s created.\n", name);
		}   
	}
	
	if(sscanf(kbuf, "delete %s", name)){ 	
		list_for_each_safe(cur_node,aux, &myList){
			item = list_entry(cur_node, list_item_t, links);
			if(strcmp(item->name, name) == 0){
				encontrado = 1;
				vfree(item->syncro);
				remove_proc_entry(name, proc_parent);
				printk(KERN_INFO "Entry %s deleted.\n", name);
				list_del(&item->links);
			}
		}
		if(encontrado == 0){
			printk(KERN_INFO "The entry %s doesnt exist.\n", name);
		}
	}
	
	return size;
}













/* Funciones de inicialización y descarga del módulo */
static int init_multififo_module(void){
	
	list_item_t *tmp;
	list_item_t *tmp2;
	proc_parent = proc_mkdir("multififo",NULL);
	INIT_LIST_HEAD(&myList);
	
	if(proc_parent==NULL){
		printk(KERN_INFO "multififo: Can't create /proc parent\n");
		return -ENOMEM;
	}
	
	tmp = (list_item_t*)vmalloc(sizeof(list_item_t));
	tmp->syncro = (entryStruct*)vmalloc(sizeof(entryStruct));
	tmp->syncro->cbuffer = create_cbuffer_t(CBUF_MAX_SIZE);
	tmp->syncro->prod_count = 0;
	tmp->syncro->cons_count = 0;
	tmp->syncro->nr_waiting_prod=0;
	tmp->syncro->nr_waiting_cons=0;
	sema_init(&(tmp->syncro->mtx),1);
	sema_init(&(tmp->syncro->sem_prod),0);
	sema_init(&(tmp->syncro->sem_cons),0);
	if(!(tmp->syncro->cbuffer))
	{return -ENOMEM;}
	
    proc_entry = proc_create_data("default",0666, proc_parent, &proc_entry_fops_default,tmp->syncro);
	
    if(proc_entry==NULL){
		destroy_cbuffer_t(tmp->syncro->cbuffer);
		vfree(tmp->syncro);
		vfree(tmp);
		printk(KERN_INFO "multififo/default: Can't create /proc entry\n");
		return -ENOMEM;
	}else{
		strcpy(tmp->name, "default");
		list_add_tail(&tmp->links, &myList);
		printk(KERN_INFO "multififo/default: Module loaded\n");
	}
	
	proc_entry = proc_create_data("control",0666, proc_parent, &proc_entry_fops_control,NULL);
    if(proc_entry==NULL){
		printk(KERN_INFO "multififo/control: Can't create /proc entry\n");
		return -ENOMEM;
	}else{
		tmp2 = (list_item_t*)vmalloc(sizeof(list_item_t));
		tmp2->syncro = NULL;
		strcpy(tmp2->name, "control");
		list_add_tail(&tmp2->links, &myList);
		printk(KERN_INFO "multififo/control: Module loaded\n");
	}
	
    return 0;
}

static void cleanup_multififo_module(void){
	struct list_head* cur_node=NULL;
    list_item_t *item=NULL;
    struct list_head* aux = NULL;
	
	list_for_each_safe(cur_node,aux, &myList){
		item = list_entry(cur_node, list_item_t, links);
		vfree(item->syncro);
		remove_proc_entry(item->name, proc_parent);
		printk(KERN_INFO "multififo/%s: Entry unloaded\n",item->name);
		list_del(&item->links);
	} 
	
	remove_proc_entry("multififo", NULL);
	printk(KERN_INFO "multififo: Module unloaded\n");
}

module_init( init_multififo_module );
module_exit( cleanup_multififo_module );
