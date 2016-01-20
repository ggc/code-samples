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
struct semaphore sem_prodReady;
struct semaphore sem_consReady;
int nr_prod_waiting=0;
int nr_cons_waiting=0;

/* Se invoca al hacer open() de entrada /proc */
static int fifoproc_open(struct inode * inode, struct file *fd){

  
  if(fd->f_mode & FMODE_READ){ //El consumidor abre el FIFO
  printk("FIFO cons open\n");  
  /*   
    //Traduccion START
    if(down_interruptible(&mtx)) //Lock 1
      return -EINTR;
    
    printk("FIFOopen cons pasa mtx.down\n");
    
    while(prod_count==0 && nr_prod_waiting==0) {
      nr_cons_waiting++;
      up(&mtx); // Unlock 1
      //Codigo de seguridad//
      if (down_interruptible(&sem_prodReady)){
	down(&mtx);
	nr_cons_waiting--;
	up(&mtx);
	return -EINTR;
      }

      printk("FIFOopen cons pasa semprodReady.down\n");
      
      if (down_interruptible(&mtx)) //Lock 2
	return -EINTR;
      //Traduccion END
      printk("FIFOopen cons pasa mtx.down\n");
    }
    
    nr_cons_waiting--;
    cons_count++; 
    //insert_cbuffer_t(cbuffer,'W');
    up(&mtx); // Unlock 2
    up(&sem_prodReady);
    */
    cons_count++;
  }else{ //El productor abre el FIFO
    printk("FIFO prod open\n");  
    /*
    //Traduccion START
    if(down_interruptible(&mtx)) // Lock 1
      return -EINTR;
    
    printk("FIFOopen prod pasa mtx.down\n");
    
    while(cons_count==0 && nr_cons_waiting==0) {
      nr_prod_waiting++;
      up(&mtx); // Unlock 1
      //Codigo de seguridad//
      if (down_interruptible(&sem_consReady)){
	down(&mtx);
	nr_prod_waiting--;
	up(&mtx);
	return -EINTR;
      }
      
      printk("FIFOopen prod pasa sem_consReady.down\n");
      
      if (down_interruptible(&mtx)) // Lock 2
	return -EINTR;
      printk("FIFOopen prod pasa mtx.down\n");
      //Traduccion END
    }
    nr_prod_waiting--;
    prod_count++; 
    up(&mtx); //Unlock 2
    up(&sem_consReady);
    */
    prod_count++;
  }   
  return 0;
}

/* Se invoca al hacer close() de entrada /proc */
static int fifoproc_release(struct inode * inode, struct file * fd){
  
  printk("fiforelease\n");
  if(fd->f_mode & FMODE_READ){ //El consumidor cierra el FIFO
    if (down_interruptible(&mtx))
      return -EINTR;
    cons_count--;
    up(&mtx);
  }else{
    if(down_interruptible(&mtx)) //El productor cierra el FIFO
      return -EINTR;
    prod_count++;
    up(&mtx);
  }
  return 0;
}

/* Se invoca al hacer read() de entrada /proc */
static ssize_t fifoproc_read(struct file * fd, char * buffer, size_t size, loff_t * offset){

  char kbuf[CBUF_SIZE+1];
  int nr_bytes=0;
  char c;

  printk("FIFOread STARTS\n");  
  
  if((*offset)>0)
    return 0;
  
  while( is_empty_cbuffer_t(cbuffer) && prod_count<=0 ){
    if(down_interruptible(&sem_prod) )
      printk("Explosion durante while.read\n");
  }
  printk("Sale del while.read\n");  
 
  if(!is_empty_cbuffer_t(cbuffer) ){
    c = remove_cbuffer_t(cbuffer);
    printk("Elimina bien del cbuffer\n");
  }
  up(&sem_cons);

  nr_bytes=1;
    
  if (copy_to_user(buffer, &kbuf[0], nr_bytes))
    return -EINVAL;
  

  (*offset)+=nr_bytes;

  printk("FIFOread ENDS\n");  
  return nr_bytes;
}

/* Se invoca al hacer write() de entrada /proc */
static ssize_t fifoproc_write(struct file * fd, const char *buffer, size_t size, loff_t * offset){

  char kbuf[CBUF_SIZE+1];
  
  if((*offset) > 0){
    printk("FIFOwrite END offset\n");
    return 0;
  }

  printk("FIFOwrite START\n");
  if (size > nr_gaps_cbuffer_t(cbuffer) ) {
    return -ENOSPC;
  }
  // copy_from_user( destino, origen, longitud )
  if (copy_from_user( &kbuf[0], buffer, size )) {
    return -EFAULT;
  }
  kbuf[size]='\0';
  //kbuf[0]='a';
  //kbuf[1]='b';
  //kbuf[2]='\0';
  printk(KERN_ALERT "kbuf: >>>%s<<<\n",kbuf);
  *offset+=size;

  
  while( !is_full_cbuffer_t(cbuffer) && cons_count <= 0){
    if(down_interruptible(&sem_cons) )
      printk("Explosion durante while.write\n");
  }
  printk("Sale del while.write\n");  
  
  insert_cbuffer_t(cbuffer, kbuf[0]); //item
  printk("Inserta bien el cbuffer\n");  
  
  up(&sem_prod);

  printk("FIFOwrite ENDS\n");  
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
  // Inicializa y reserva memoria
  kbuf = (char *)vmalloc( CBUF_SIZE );
  sema_init(&mtx,1); 
  sema_init(&sem_prod,0);
  sema_init(&sem_cons,0);
  sema_init(&sem_prodReady,0);
  sema_init(&sem_consReady,0);
  cbuffer = create_cbuffer_t(CBUF_SIZE);
  nr_prod_waiting=0;
  nr_cons_waiting=0;
  prod_count=0;
  cons_count=0;

  if(!cbuffer)
    return -ENOMEM;
  memset(kbuf,0,CBUF_SIZE);

  // Crea la entrada /proc
  proc_entry = proc_create_data("modfifo",0666, NULL, &proc_entry_fops,NULL);
  
  if(proc_entry==NULL){
    vfree(kbuf); 
    destroy_cbuffer_t(cbuffer);
    printk(KERN_INFO "modfifo: Can't create /proc entry\n");
    return -ENOMEM;
  } else {
    printk(KERN_INFO "modfifo: Module loaded\n");
  }
  return 0;
}

void cleanup_fifoproc_module(void){
  vfree(kbuf); 
  remove_proc_entry("modfifo", NULL);
  destroy_cbuffer_t(cbuffer);
}

module_init( init_fifoproc_module );
module_exit( cleanup_fifoproc_module );
