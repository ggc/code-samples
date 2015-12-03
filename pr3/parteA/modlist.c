#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/vmalloc.h>
#include <asm-generic/uaccess.h>
#include <linux/ftrace.h>
#include <linux/list.h>
#include <linux/list_sort.h>
#include <linux/spinlock.h>

MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("Kernel Module - FDI-UCM");
MODULE_AUTHOR("ASN and GGC");

#define BUFFER_LENGTH       PAGE_SIZE


struct list_head myList;
struct list_head newList;
typedef struct {
  int data;
  struct list_head links;
}list_item_t;
DEFINE_SPINLOCK(sp);

char funcion[10];
int valor;
static char *modlist;
static struct proc_dir_entry *proc_entry;

static ssize_t modlist_read(struct file *filp, char __user *buf, size_t len, loff_t *off) {
  
  int nr_bytes; //Bytes a leer
  list_item_t *item=NULL; //item aux
  struct list_head* cur_node=NULL; //nodo auxiliar para el recorrido
  char *end = "\0"; //Final de cadena. Se anademanualmente
  char *strValue = vmalloc(5*sizeof(char)); //Semicadenas de "_valor"
  modlist = vmalloc(BUFFER_LENGTH); //cadena a volcar en buf tamano BUFFER_LENGTH

  if ((*off) > 0) 
    return 0;

  list_for_each(cur_node, &myList){
    item = list_entry(cur_node, list_item_t, links);
    //trace_printk("%d\n", item->data);
    sprintf(strValue, " %d", item->data); //en arg1 meter ar2 donde lo de arg2 es ar3
    strcat(modlist, strValue); //Concatenar el subsegmento a modlist
    strcat(modlist, "\n"); //Concatena salto de linea
  }
  strcat(modlist, end); //anadir EOF

  nr_bytes=strlen(modlist); //Formada la cadena se mide
    
  if (len<nr_bytes)
    return -ENOSPC;

  if (copy_to_user( buf, &modlist[0], nr_bytes))
    return -EINVAL;
  
  (*off)+=len;
  
  //Liberar memoria
  vfree(strValue);
  vfree(modlist);
  
  return nr_bytes; 
}



static ssize_t modlist_write(struct file *filp, const char __user *buf, size_t len, loff_t *off) {

  list_item_t *tmp;
  list_item_t *tmp2;
  struct list_head* cur_node=NULL;
  list_item_t *item=NULL;
  struct list_head* aux = NULL;
  struct list_head *first;
  struct list_head *last;

  int menor;
  int apariciones = 0;
  int i, count=100;
  modlist = vmalloc(BUFFER_LENGTH);

  if (copy_from_user( &modlist[0], buf, len ))  
    return -EFAULT;


  if(sscanf(modlist, "add %d", &valor)){ 
    trace_printk("\nEntro añadir dato %d\n", valor);
    tmp = (list_item_t*)vmalloc(sizeof(list_item_t));
    tmp->data=valor;
    list_add_tail(&tmp->links, &myList);
    trace_printk("\nValue %d added.\n", tmp->data);
  }


  if(strcmp(modlist, "cleanup\n")==0){
    trace_printk("\nEntro eliminar todo\n");
    list_for_each_safe(cur_node,aux, &myList){
      item = list_entry(cur_node, list_item_t, links);
      list_del(&item->links);
      trace_printk("\nEliminado nodo\n");
    }
   
  }

  if(sscanf(modlist, "remove %d", &valor)){
    trace_printk("\nEntro eliminar valor %d\n", valor);
    list_for_each_safe(cur_node,aux, &myList){
      item = list_entry(cur_node, list_item_t, links);
      if(item->data == valor){
	list_del(&item->links);
	trace_printk("\nEliminado nodo\n");
      }
    }
  }
    
  if(strcmp(modlist, "count\n")==0){
    int count = 0;
    trace_printk("\nEntro count\n");
    list_for_each_safe(cur_node,aux, &myList){
      item = list_entry(cur_node, list_item_t, links);
      count++;
    }
    trace_printk("\n Count=%d\n",count);
  }
   
  if(strcmp(modlist, "sort\n")==0){

    trace_printk("\nEntro ordenar\n");
    INIT_LIST_HEAD(&newList);      
    while(!list_empty(&myList) && count>0){    //MIENTRAS LISTA NO VACIA
      count--;
  	  
      //CALCULO EL MENOR DE LA LISTA
      trace_printk("Calculando \n");//######
      menor=999;
      trace_printk("%d \n", menor);//######
      cur_node=NULL;
      list_for_each(cur_node, &myList){
	item = list_entry(cur_node, list_item_t, links);
	if(item->data < menor){
	  menor = item->data;
	}
      }
      trace_printk("menor: %d\n", menor);//######
      
      //CALCULO EL NUMERO DE APARICIONES
      trace_printk("Calculo apariciones: \n");//######
      apariciones = 0; 
      cur_node=NULL;
      list_for_each(cur_node, &myList){
	item = list_entry(cur_node, list_item_t, links);
	if(item->data == menor){
	  apariciones = apariciones+1;
	}
      }
      trace_printk("apariciones: %d\n", apariciones);//######
	 
     
      //AÑADO EL NUMERO X CANTIDAD DE VECES EN LA LISTA NUEVA
      trace_printk("Añado en lisa nueva: \n");//######
      for(i=0;i<apariciones;i++){
	tmp2 = (list_item_t*)vmalloc(sizeof(list_item_t));
	tmp2->data=menor;
	list_add_tail(&tmp2->links, &newList); 
      }
	 
      trace_printk("apariciones añadidas: %d\n", apariciones);//######
	  
	  
      //ELIMINO TODOS LOS "MENORES" DE LA LISTA ORIGINAL
      trace_printk("Elimino menores de la lista original: \n");//######
      cur_node=NULL;
      aux = NULL;
      list_for_each_safe(cur_node, aux, &myList){
	item = list_entry(cur_node, list_item_t, links);
	if(item->data == menor){
          list_del(&item->links);
	}
      }
      trace_printk("Eliminados de la lista original\n");//######
      trace_printk("Iteracion anadida\n");//######
    }
    cur_node=NULL;
    list_for_each(cur_node, &newList){
      item = list_entry(cur_node, list_item_t, links);
      trace_printk("itemdata: %d\n", item->data);
    }

    first = newList.next;
    last = newList.prev;
    myList.next=first; //1
    myList.prev=last; //4
    last->next=&myList; //3
    first->prev=&myList; //2
    
  }


  (*off)+=len;
	
  return len;
}

struct file_operations fops = {
  .read = modlist_read,	
  .write = modlist_write
};




int init_modlist_module( void )
{  
  
  int ret = 0;
  modlist = (char *)vmalloc( BUFFER_LENGTH );

  INIT_LIST_HEAD(&myList);
  

  if (!modlist) {
    ret = -ENOMEM;
  } else {

    memset( modlist, 0, BUFFER_LENGTH );
    proc_entry = proc_create( "modlist", 0666, NULL, &fops);
    if (proc_entry == NULL) {
      ret = -ENOMEM;
      vfree(modlist);
      printk(KERN_INFO "modlist: Can't create /proc entry\n");
    } else {
      printk(KERN_INFO "modlist: Module loaded\n");
    }
  }

  
	 
  return ret;

}


void exit_modlist_module( void )
{
  remove_proc_entry("modlist", NULL);
  vfree(modlist);
  printk(KERN_INFO "modlist: Module unloaded.\n");
}


module_init( init_modlist_module );
module_exit( exit_modlist_module );
