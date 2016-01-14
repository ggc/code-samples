#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/vmalloc.h>
#include <asm-generic/uaccess.h>
#include <asm-generic/errno.h>
#include <linux/semaphore.h>
#include "cbuffer.h"
#define CBUF_SIZE 50

static char *kbuf;
static struct proc_dir_entry *proc_entry;
cbuffer_t* cbuffer;
int prod_count = 0;
int cons_count = 0;
struct semaphore mtx;
struct semaphore sem_prod;
struct semaphore sem_cons;
int nr_prod_waiting=0;
int nr_cons_waiting=0;

/* Se invoca al hacer open() de entrada /proc */
static int fifoproc_open(struct inode * inode, struct file *fd){
  down_interruptible(&mtx);
  
  if(fd->f_mode & FMODE_READ){ //El consumidor abre el FIFO
    printk("FIFO read open\n"); 
    cons_count++;
    // Signal
    if(down_interruptible(&mtx)) //Prescindible?>
      return -EINTR;
    if(nr_prod_waiting>0){
      up(&sem_prod);
      nr_prod_waiting--;
    }
    up(&mtx);//<?
    //-Signal

    // Wait
    if(down_interruptible(&mtx)) //Prescindible?>
      return -EINTR;
    while(prod_count==0 && nr_prod_waiting==0) { //Arreglar
      nr_cons_waiting++;
      up(&mtx);
      if (down_interruptible(&sem_prodReady)){
	down(&mtx);
	nr_cons_waiting--;
	up(&mtx);
	return -EINTR;
      }
      if (down_interruptible(&mtx))
	return -EINTR;
    }
    up(&mtx);//<?
    //-Wait
    
  }else{ //El productor abre el FIFO
    printk("FIFO write open\n");  

    prod_count++;
 
    // Signal
    if (down_interruptible(&mtx))//Quitar?
      return -EINTR;
    if(nr_prod_waiting>0){
      up(&sem_prodReady);  
      nr_prod_waiting--;
    }
    up(&mtx);//quitar?
    //-Signal

    // Wait
    if(down_interruptible(&mtx))//quitar?
      return -EINTR;
    while(cons_count==0 && nr_cons_waiting==0) {//arreglar?
      nr_prod_waiting++;
      up(&mtx);
      if (down_interruptible(&sem_consReady)){
	down(&mtx);
	nr_prod_waiting--;
	up(&mtx);
	return -EINTR;
      }
      if (down_interruptible(&mtx))
	return -EINTR;
    }
    up(&mtx);//quitar?
    //-Wait

  }   
  return 0;
}

/* Se invoca al hacer close() de entrada /proc */
static int fifoproc_release(struct inode * inode, struct file * fd){
  
  printk("FIFOrelease\n");
  if(fd->f_mode & FMODE_READ){ //El consumidor cierra el FIFO
    if (down_interruptible(&mtx))
      return -EINTR;
    cons_count--;
    up(&mtx);
  }else{
    if(down_interruptible(&mtx)) //El productor cierra el FIFO
      return -EINTR;
    prod_count--;
    up(&mtx);
  }
  return 0;
}

/* Se invoca al hacer read() de entrada /proc */
static ssize_t fifoproc_read(struct file * fd, char * buffer, size_t size, loff_t * offset){

  char kbuf[CBUF_SIZE+1];
  int nr_bytes=0;

  printk("FIFO read\n");  
  if((*offset)>0)
    return 0;

  // Wait
  if(down_interruptible(&mtx)){
    return -EINTR;
  }
  //Si solicia $size y hay menos elem, entra.
  while( size_cbuffer_t(cbuffer)<size ){
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
  up(&mtx);
  //-Wait
  
  nr_bytes=size;
  remove_items_cbuffer_t(cbuffer, kbuf, size);

  //copy_from_user(dest, orig, len)
  if (copy_to_user(buffer, &kbuf[0], nr_bytes))
    return -EINVAL;

  (*offset)+=nr_bytes;
  
  // Signal
  if (down_interruptible(&mtx))
    return -EINTR;
  if (nr_prod_waiting>0){
    up(&sem_prod);
    nr_prod_waiting--;
  }
  up(&mtx);
  //-Signal
  
  return nr_bytes;
}

/* Se invoca al hacer write() de entrada /proc */
static ssize_t fifoproc_write(struct file * fd, const char *buffer, size_t size, loff_t * offset){

    char kbuf[CBUF_SIZE+1];
  
    if((*offset) > 0)
      return 0;
    printk("FIFOwrite\n");
  
    // Wait
    if(down_interruptible(&mtx)){
      return -EINTR;
    }
    while(nr_gaps_cbuffer_t(cbuffer) < size && cons_count <= 0){
   
      printk("nr_prod_waiting\n"); 
      nr_prod_waiting++;
      up(&mtx);
      if(down_interruptible(&sem_prod)){
	down(&mtx);
	nr_prod_waiting--;
	up(&mtx);
	return -EINTR;
      }
      if(down_interruptible(&mtx))
	return -EINTR;
    }//-Wait

    //copy_from_user(dest, orig, len)
    if (copy_from_user( kbuf, buffer, size )) {
      return -EFAULT;
    }

    kbuf[size]='\0';
    printk(KERN_ALERT "kbuf: >%s<\n",kbuf);
    *offset+=size;
  
    insert_items_cbuffer_t(cbuffer, kbuf, size); //item

    // Signal
    if(down_interruptible(&mtx))
      return -EINTR;
    if(nr_cons_waiting>0){
      up(&sem_cons);
      nr_cons_waiting--;
    }
    up(&mtx);
    //-Signal

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
    kbuf = (char *)vmalloc( CBUF_SIZE );/////
    sema_init(&mtx,1);
    sema_init(&sem_prod,0);
    sema_init(&sem_cons,0);
    sema_init(&sem_consReady,0);
    sema_init(&sem_prodReady,0);
    cbuffer = create_cbuffer_t(CBUF_SIZE);
    nr_prod_waiting=0;
    nr_cons_waiting=0;
    prod_count=0;
    cons_count=0;

    if(!cbuffer)
      return -ENOMEM;
    memset(kbuf,0,CBUF_SIZE);//////
    proc_entry = proc_create_data("modfifo",0666, NULL, &proc_entry_fops,NULL);
  
    if(proc_entry==NULL){
      vfree(kbuf); //////
      destroy_cbuffer_t(cbuffer);
      printk(KERN_INFO "modfifo: Can't create /proc entry\n");
      return -ENOMEM;
    } else {
      printk(KERN_INFO "modfifo: Module loaded\n");
    }
    return 0;
  }
  void cleanup_fifoproc_module(void){
    vfree(kbuf); //////
    remove_proc_entry("modfifo", NULL);
    destroy_cbuffer_t(cbuffer);
  }

  module_init( init_fifoproc_module );
  module_exit( cleanup_fifoproc_module );
