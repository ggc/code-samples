#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/vmalloc.h>
#include <linux/init.h>
#include <asm-generic/uaccess.h>
#include <asm-generic/errno.h>
#include <linux/semaphore.h>
#include "cbuffer.h"



//MODULE_LICENSE("GPL");

static struct proc_dir_entry *proc_entry;
cbuffer_t* cbuffer;
int prod_count = 0;
int cons_count = 0;
struct semaphore mtx;
struct semaphore sem_prod;
struct semaphore sem_cons;
struct semaphore sem_prodReady;
struct semaphore sem_consReady;
int nr_prod_waiting=0;
int nr_cons_waiting=0;

/* Se invoca al hacer open() de entrada /proc */
static int fifoproc_open(struct inode * inode, struct file *fd){

  printk("fifoopen\n");  
  if(fd->f_mode & FMODE_READ){ //El consumidor abre el FIFO
    //Traduccion START
    if(down_interruptible(&mtx)) //Lock
      return -EINTR;
    while(prod_count==0 && nr_prod_waiting==0) {
      nr_cons_waiting++;
      up(&mtx);
      //Codigo de seguridad//
      if (down_interruptible(&sem_prodReady)){
	down(&mtx);
	nr_cons_waiting--;
	up(&mtx);
	return -EINTR;
      }
      if (down_interruptible(&mtx))
	return -EINTR;
      //Traduccion END
    }
    nr_cons_waiting--;
    cons_count++; 
    up(&mtx); //Unlock
  }else{ //El productor abre el FIFO
    //Traduccion START
    if(down_interruptible(&mtx)) //Lock
      return -EINTR;
    while(cons_count==0 && nr_cons_waiting==0) {
      nr_prod_waiting++;
      up(&mtx);
      //Codigo de seguridad//
      if (down_interruptible(&sem_consReady)){
	down(&mtx);
	nr_prod_waiting--;
	up(&mtx);
	return -EINTR;
      }
      if (down_interruptible(&mtx))
	return -EINTR;
      //Traduccion END
    }
    nr_prod_waiting--;
    prod_count++; 
    up(&mtx); //Unlock
  }   
  return 0;
}
/* Se invoca al hacer close() de entrada /proc */
static int fifoproc_release(struct inode * inode, struct file * fd){
  
  printk("fiforelease\n");
  if(fd->f_mode & FMODE_READ){ //El consumidor cierra el FIFO
    down_interruptible(&mtx);
    cons_count--;
    up(&mtx);
  }else{
    down_interruptible(&mtx); //El productor cierra el FIFO
    prod_count++;
    up(&mtx);
  }
  return 0;
}
/* Se invoca al hacer read() de entrada /proc */
static ssize_t fifoproc_read(struct file * fd, char * rbuffer, size_t size, loff_t * offset){
  //if(nr_gaps_cbuffer_t(cbuffer)<size)

  char kbuf[51];
  int nr_bytes=0;
  char * item;


  printk("fiforead\n");  
  item = vmalloc(sizeof(char));
  if(offset>0)
    return 0;
  if(down_interruptible(&mtx)){
    return -EINTR;
  }
  while(size_cbuffer_t(cbuffer)<size){
    nr_cons_waiting++;
    up(&mtx);
    if (down_interruptible(&sem_cons)){
      down(&mtx);
      nr_cons_waiting--;
      up(&mtx);
      return -EINTR;
    }

    if (down_interruptible(&mtx)){
      return -EINTR;
    }
  }

  
  item = head_cbuffer_t(cbuffer);
  remove_cbuffer_t(cbuffer);
  printk("printk %s",item);
  if (nr_prod_waiting>0)
    {
      up(&sem_prod);
      nr_prod_waiting--;
    }

  up(&mtx);

  nr_bytes=sprintf(kbuf, "%s",item);

  vfree(item);

  if (size < nr_bytes)
    return -ENOSPC;
  
  if (copy_to_user(rbuffer,kbuf,nr_bytes))
    return -EINVAL;
   
  (*offset)+=nr_bytes;

  return nr_bytes;
}
/* Se invoca al hacer write() de entrada /proc */
static ssize_t fifoproc_write(struct file * fd, const char * wbuffer, size_t size, loff_t * offset){
  char kbuf[51];
  char val=' ';
  int* item=NULL;


  printk("fifowrite\n");
  if (size > 50) {
    return -ENOSPC;
  }
  if (copy_from_user( kbuf, wbuffer, size )) {
    return -EFAULT;
  }
  //strcat(kbuf, '\0');
  *offset+=size;


  if (sscanf(kbuf,"%c",&val)!=1){
    return -EINVAL;
  }

  //item=vmalloc(sizeof(char));
  //item=val;

  if(down_interruptible(&mtx)){
    vfree(item);
    return -EINTR;
  }
  while(nr_gaps_cbuffer_t(cbuffer) < size && cons_count <= 0){
   
    printk("nr_prod_waiting\n"); 
    nr_prod_waiting++;
    up(&mtx);
    if(down_interruptible(&sem_prod)){
      down(&mtx);
      vfree(item);
      nr_prod_waiting--;
      up(&mtx);
      return -EINTR;
    }
    if(down_interruptible(&mtx)){
      vfree(item);
      return -EINTR;
    }
  }
  
  printk("fifo preinsercion %s\n",val);

  insert_cbuffer_t(cbuffer, val); //item

  if(nr_cons_waiting>0){
    up(&sem_cons);
    nr_cons_waiting--;
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
  sema_init(&sem_consReady,0);
  sema_init(&sem_prodReady,0);
  cbuffer = create_cbuffer_t(50);
  nr_prod_waiting=0;
  nr_cons_waiting=0;
  prod_count=0;
  cons_count=0;

  if(!cbuffer)
    return -ENOMEM;

  proc_entry = proc_create_data("modfifo",0666, NULL, &proc_entry_fops,NULL);
  
  if(proc_entry==NULL){
    destroy_cbuffer_t(cbuffer);
    printk(KERN_INFO "prodcons: Can't create /proc entry\n");
    return -ENOMEM;
  } else {
    printk(KERN_INFO "prodcons: Module loaded\n");
  }
  return 0;
}
void cleanup_fifoproc_module(void){
  remove_proc_entry("modfifo", NULL);
  destroy_cbuffer_t(cbuffer);
}

module_init( init_fifoproc_module );
module_exit( cleanup_fifoproc_module );
