#include <linux/semaphore.h>
#include "cbuffer.h"

MODULE_LICENSE("GPL");

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

/* Funciones de inicialización y descarga del módulo */
int init_module(void){
  cbuffer = create_cbuffer_t(50);
  sem_init(mtx,1);
  sem_init(sem_prod,0);
  sem_init(sem_cons,0);
  sem_init(sem_consReady,0);
  sem_init(sem_prodReady,0);
  nr_prod_waiting=0;
  nr_cons_waiting=0;
  prod_count=0;
  cons_count=0;
}
void cleanup_module(void){
  destroy_cbuffer_t(cbuffer);
}
/* Se invoca al hacer open() de entrada /proc */
static int fifoproc_open(struct inode * inode, struct file * fd){
  
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
    while(cons==0 && nr_cons_waiting==0) {
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
  
}
/* Se invoca al hacer write() de entrada /proc */
static ssize_t fifoproc_write(struct file * fd, const char * wbuffer, size_t size, loff_t * offset){
  
}
