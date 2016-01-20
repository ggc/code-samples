#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/vmalloc.h>
#include <asm-generic/uaccess.h>
#include <asm-generic/errno.h>
#include <linux/semaphore.h>
#include "cbuffer.h"
#define CBUF_MAX_SIZE 50

static struct proc_dir_entry *proc_entry;

cbuffer_t* cbuffer;
int prod_count = 0;
int cons_count = 0;
int nr_waiting_prod = 0;
int nr_waiting_cons = 0;
struct semaphore mtx;
struct semaphore sem_prod;
struct semaphore sem_cons;

/* Se invoca al hacer open() de entrada /proc */
static int fifoproc_open(struct inode * inode, struct file *fd){
	
	/* "Adquiere" el mutex */
	if (down_interruptible(&mtx)) 
		return -EINTR;
  
	if(fd->f_mode & FMODE_READ){ //El consumidor abre el FIFO
		cons_count++;
		//cond_signal(prod);
		if (nr_waiting_prod>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
			up(&sem_prod); \
			nr_waiting_prod--; \
		}
	
		while(prod_count == 0){
			//cond_wait(cons, mtx);
			nr_waiting_cons++; 
			/*Libera el mutex*/
			up(&mtx); 
			/*Se bloquea en la cola*/
			  if (down_interruptible(&sem_cons)){ 
				down(&mtx); 
				nr_waiting_cons--; 
				up(&mtx); 
				return -EINTR; 
			} 
			/*Adquiere el mutex*/
			  if (down_interruptible(&mtx)){ 
				return -EINTR; 
			}		
		}
	
  }else{ //El productor abre el FIFO
		prod_count++;
		//cond_signal(cons);
		if (nr_waiting_cons>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
			up(&sem_cons); \
			nr_waiting_cons--; \
		}
		while(cons_count == 0){
			//cond_wait(prod, mtx);
			nr_waiting_prod++; 
			/*Libera el mutex*/
			up(&mtx); 
			/*Se bloquea en la cola*/
			if (down_interruptible(&sem_prod)){ 
				down(&mtx); 
				nr_waiting_prod--; 
				up(&mtx); 
				return -EINTR; 
			} 
			/*Adquiere el mutex*/
			  if (down_interruptible(&mtx)){ 
				return -EINTR; 
			}	
		}	
  }
  
  /* "Libera" el mutex */ 
  up(&mtx);
  
  return 0;
}

/* Se invoca al hacer close() de entrada /proc */
static int fifoproc_release(struct inode * inode, struct file * fd){
	
  	/* "Adquiere" el mutex */
	if (down_interruptible(&mtx)) 
			return -EINTR;
		
	if(fd->f_mode & FMODE_READ){ //El consumidor cierra el FIFO
		cons_count--;
		//cond_signal(prod);
		if (nr_waiting_prod>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
			up(&sem_prod); \
			nr_waiting_prod--; \
		}
	}else{
		prod_count--;
		//cond_signal(cons);
		if (nr_waiting_cons>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
			up(&sem_cons); \
			nr_waiting_cons--; \
		}
	}
	if (cons_count == 0 && prod_count ==0)
		clear_cbuffer_t(cbuffer);
	up(&mtx);
	
	return 0;
}

/* Se invoca al hacer read() de entrada /proc */
static ssize_t fifoproc_read(struct file * fd, char * buffer, size_t size, loff_t * offset){

  char kbuf[CBUF_MAX_SIZE];

  if(size>CBUF_MAX_SIZE)
    return -ENOSPC;

  if(down_interruptible(&mtx))
    return -EINTR;
	
	//Vacio y no productores
	if (is_empty_cbuffer_t(cbuffer) && prod_count == 0){
		up(&mtx);
		return 0;  //EOF
	}
	//No items necesarios
  while( size_cbuffer_t(cbuffer)<size){
	//cond_wait(cons, mtx);
    nr_waiting_cons++; 
	/*Libera el mutex*/
    up(&mtx); 
	/*Se bloquea en la cola*/
	if (down_interruptible(&sem_cons)){ 
		down(&mtx); 
		nr_waiting_cons--; 
		up(&mtx); 
		return -EINTR; 
    } 
	/*Adquiere el mutex*/
    if (down_interruptible(&mtx)){ 
        return -EINTR; 
    }
  }
	
	remove_items_cbuffer_t(cbuffer,kbuf,size);
		
	//cond_signal(prod);
	if (nr_waiting_prod>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
		up(&sem_prod); \
		nr_waiting_prod--; \
	}
	up(&mtx);
	
	copy_to_user(buffer, kbuf, size);
	
	return size;
}

/* Se invoca al hacer write() de entrada /proc */
static ssize_t fifoproc_write(struct file * fd, const char *buffer, size_t size, loff_t * offset){

    char kbuf[CBUF_MAX_SIZE];
  
	if (size > CBUF_MAX_SIZE) { return -EINVAL;}
	if(copy_from_user(kbuf,buffer,size)){ return -ENOMEM;}
  
  	/* "Adquiere" el mutex */
	if (down_interruptible(&mtx)) 
		return -EINTR;
	
	
	/* Esperar hasta que haya hueco para insertar (debe haber consumidores) */ 
    while(nr_gaps_cbuffer_t(cbuffer) < size && cons_count > 0){
		//cond_wait(prod,mtx);
        nr_waiting_prod++; 
		/*Libera el mutex*/
        up(&mtx); 
		/*Se bloquea en la cola*/
        if (down_interruptible(&sem_prod)){ 
			down(&mtx); 
			nr_waiting_prod--; 
			up(&mtx); 
			return -EINTR; 
        } 
		/*Adquiere el mutex*/
        if (down_interruptible(&mtx)){ 
			return -EINTR; 
        }
	}
		
	/* Detectar fin de comunicación por error (consumidor cierra FIFO antes) */ 
	if (cons_count==0) {up(&mtx); return -EPIPE;} 
	
	insert_items_cbuffer_t(cbuffer,kbuf,size); 
	
	/* Despertar a posible consumidor bloqueado */ 
	//cond_signal(cons);
	if (nr_waiting_cons>0) { \
		/* Despierta a uno de los hilos bloqueados */ \
		up(&sem_cons); \
		nr_waiting_cons--; \
	}
	up(&mtx);
	return size;
  }
 
  static const struct file_operations proc_entry_fops = {
    .open = fifoproc_open,
    .release = fifoproc_release,
    .read = fifoproc_read,
    .write = fifoproc_write,    
  };

  /* Funciones de inicialización y descarga del módulo */
  int init_fifoproc_module(void){
	  
    sema_init(&mtx,1);
    sema_init(&sem_prod,0);
    sema_init(&sem_cons,0);
    cbuffer = create_cbuffer_t(CBUF_MAX_SIZE);
	
    if (!cbuffer) return -ENOMEM;

    proc_entry = proc_create_data("modfifo",0666, NULL, &proc_entry_fops,NULL);
  
    if(proc_entry==NULL){
      destroy_cbuffer_t(cbuffer);
      printk(KERN_INFO "modfifo: Can't create /proc entry\n");
      return -ENOMEM;
    } else {
      printk(KERN_INFO "modfifo: Module loaded\n");
    }
    return 0;
  }
  
  void cleanup_fifoproc_module(void){
    remove_proc_entry("modfifo", NULL);
    destroy_cbuffer_t(cbuffer);
	printk(KERN_INFO "modfifo: Module unloaded\n");
  }

  module_init( init_fifoproc_module );
  module_exit( cleanup_fifoproc_module );
