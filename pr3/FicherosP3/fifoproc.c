#include "cbuffer.h"

MODULE_LICENSE("GPL");

cbuffer_t* cbuffer;
int prod_count = 0;
int cons_count = 0;
struct semaphore mtx;
struct semaphore sem_prod;
struct semaphore sem_cons;
int nr_prod_waiting=0;
int nr_cons_waiting=0;

/* Funciones de inicialización y descarga del módulo */
int init_module(void){
	cbuffer = create_cbuffer_t(50);
	sema_init(mtx,1);
	sema_init(sem_prod,X);
	sema_init(sem_cons,Y);
}
void cleanup_module(void){

}
/* Se invoca al hacer open() de entrada /proc */
static int fifoproc_open(struct inode * inode, struct file * fd){

 return 0;
}
/* Se invoca al hacer close() de entrada /proc */
static int fifoproc_release(struct inode * inode, struct file * fd){


return 0;
}
/* Se invoca al hacer read() de entrada /proc */
static ssize_t fifoproc_read(struct file * fd, char * rbuffer, size_t size, loff_t * offset){

}
/* Se invoca al hacer write() de entrada /proc */
static ssize_t fifoproc_write(struct file * fd, const char * wbuffer, size_t size,
loff_t * offset){

}