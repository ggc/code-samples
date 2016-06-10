#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/proc_fs.h>
#include <linux/string.h>
#include <linux/vmalloc.h>
#include <linux/ftrace.h>
#include <linux/list.h>
#include <linux/list_sort.h>
#include <linux/spinlock.h>
#include <asm-generic/uaccess.h>

MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("Kernel Module: \
Allows to manage multiple lists");
MODULE_AUTHOR("Gabriel Galan Casillas");


//Definiciones
#define BUFFER_LENGTH       PAGE_SIZE
#define PATH "modmultilist"
DEFINE_SPINLOCK(spin);

// Struct that contains int's (core list)
typedef struct {
  int data;
  struct list_head links;
}list_item_t;

// Struct that contains name and list_item_t associated to a proc_entry
typedef struct {
  char *name;
  struct list_head *data;  
  struct list_head links;
}list_proc_data_t;

// Variables: Simple
char funcion[10];
int pos=0;
int valor;
static char *kbuf; //max 32 entries with 128 chars/entry


// Variables: Struct
struct list_head myList[32];

struct list_head procData_list;

static struct proc_dir_entry *proc_entry;


//Prototipos
static ssize_t modmultilist_control_write(struct file *filp, const char __user *buf, size_t len, loff_t *off);

static ssize_t modmultilist_read(struct file *filp, char __user *buf, size_t len, loff_t *off);
static ssize_t modmultilist_write(struct file *filp, const char __user *buf, size_t len, loff_t *off);


// Functions

int dup_check(char *entryName){
  list_proc_data_t *item=NULL;
  struct list_head *cur_node=NULL;
  printk(KERN_INFO "list_for_each nameEntry:%s\n",entryName);
  
  list_for_each(cur_node, &procData_list){
    item = list_entry(cur_node, list_proc_data_t, links);
    printk(KERN_INFO "list_for_each item->%s\n",item->name);
    if(strcmp(entryName, item->name)==0){
      printk(KERN_INFO "list_for_each equals!!!\n");
      return -1;
    }
  }
  return 0;
}


int clean_proc(void){
  list_proc_data_t *item=NULL;
  struct list_head *aux = NULL;
  struct list_head *cur_node=NULL;
  
  // If addr for vfree is NULL, no operation is performed.
  // So we're good
  list_for_each_safe(cur_node, aux, &procData_list){
    item = list_entry(cur_node, list_proc_data_t, links);
    vfree(item->name);
    vfree(item->data);
    list_del(&item->links);
  }
  printk("modmultilist: %d deleted\n", valor);
  printk(KERN_INFO "clean_proc\n");

  return 0;
}


//###########################
//###Entrada \proc\<list> ###
//###########################


static ssize_t modmultilist_read(struct file *filp, char __user *buf, size_t len, loff_t *off) {
  
  int nr_bytes; // Bytes to read
  list_item_t *item=NULL; // item to iterate through lists
  struct list_head* cur_node=NULL; //Aux node for safe delete
  char *strValue; //substrings of "_valor"

  struct list_head *myListTmp=(struct list_head *)PDE_DATA(filp->f_inode);

  kbuf = (char *)vmalloc(BUFFER_LENGTH); //kernel space string with size BUFFER_LENGTH
  strValue = vmalloc(5*sizeof(char));
  
  if ((*off) > 0) 
    return 0;

  spin_lock(&spin);
  list_for_each(cur_node, myListTmp){
    item = list_entry(cur_node, list_item_t, links);
    sprintf(strValue, " %d", item->data);
    // Solucionar strcat con \0 fijo
    strcat(kbuf, strValue); //Concatenar el subsegmento a modlist
    strcat(kbuf, "\n"); //Concatena salto de linea
  }	
  kbuf[BUFFER_LENGTH-1]='\0'; //adds EOF
  spin_unlock(&spin);

  nr_bytes=strlen(kbuf);
    
  if (len<nr_bytes)
    return -ENOSPC;

  if (copy_to_user( buf, &kbuf[0], nr_bytes))
    return -EINVAL;
  
  (*off)+=len;
  
  vfree(strValue);
  vfree(kbuf);
  
  return nr_bytes; 
}



static ssize_t modmultilist_write(struct file *filp, const char __user *buf, size_t len, loff_t *off) {

  list_item_t *tmp;
  list_item_t *item=NULL;
  struct list_head *cur_node=NULL; // current node while iterating
  struct list_head *aux = NULL; // Aux node to iterate through lists
  struct list_head *myListTmp = NULL; // points to proc_entry->data

  kbuf = (char *)vmalloc(BUFFER_LENGTH);

  if (copy_from_user( &kbuf[0], buf, len ))  
    return -EFAULT;

  myListTmp = (struct list_head *)PDE_DATA(filp->f_inode);
  
  spin_lock(&spin);
  kbuf[BUFFER_LENGTH-1]='\0'; //adds EOF
  spin_unlock(&spin);

  //###OPTIONS###
  if(sscanf(kbuf, "add %d", &valor)){ 
    tmp = (list_item_t*)vmalloc(sizeof(list_item_t));
    tmp->data=valor;
    spin_lock(&spin);
    list_add_tail(&tmp->links, myListTmp);
    spin_unlock(&spin);
    printk("modmultilist: %d added\n", tmp->data);
  }


  if(strcmp(kbuf, "cleanup\n")==0){
    spin_lock(&spin);
    list_for_each_safe(cur_node,aux, myListTmp){
      item = list_entry(cur_node, list_item_t, links);
      list_del(&item->links);
    }
    spin_unlock(&spin);
  }

  if(sscanf(kbuf, "remove %d", &valor)){
    spin_lock(&spin);
    list_for_each_safe(cur_node, aux, myListTmp){
      item = list_entry(cur_node, list_item_t, links);
      if(item->data == valor){
	list_del(&item->links);
	printk("modmultilist: %d deleted\n", valor);
      }
    }
    spin_unlock(&spin);    
  }
  
  (*off)+=len;
	
  return len;
}

struct file_operations fops = {
  .read = modmultilist_read,	
  .write = modmultilist_write
};



//###########################
//###Entrada \proc\control###
//###########################

static ssize_t modmultilist_control_write(struct file *filp, const char __user *buf, size_t len, loff_t *off){

  char *newEntry; // string with /proc entry name
  list_proc_data_t *tmp;
  list_proc_data_t *item=NULL;
  struct list_head *aux = NULL;
  struct list_head *cur_node=NULL;
  kbuf = (char *)vmalloc(64*sizeof(char));


  if((*off) > 0){
    printk(KERN_INFO "modmultilist: No se puede escribir mas!\n");
    return 0;
  }
  
  if(len > 64){
    printk(KERN_INFO "modmultilist: Proc entry name too long!\n");
    return -ENOSPC;
  }
  
  if (copy_from_user( &kbuf[0], buf, len ))  
    return -EFAULT;
  
  (*off)+=len;
  
  kbuf[63]='\0';
  printk(KERN_INFO "kbuf=%s\n", kbuf);

  //###OPTIONS###
  newEntry = vmalloc(len);
  
  if(sscanf(kbuf, "create %s", newEntry)){
    if(dup_check(newEntry) != 0 ){
      printk(KERN_INFO "modmultilist: Already exist this entry\n");
      vfree(kbuf);
      return -1;
    }
    INIT_LIST_HEAD(&(myList[pos]));

    if(proc_create_data(newEntry, 0666, proc_entry, &fops, &myList[pos])){
      tmp = (list_proc_data_t*)vmalloc(sizeof(list_proc_data_t));
      tmp->name=newEntry;
      tmp->data=&(myList[pos]);
    
      spin_lock(&spin);
      list_add_tail(&tmp->links, &procData_list);
      pos++;
      spin_unlock(&spin);
    
      printk("modmultilist: %s created\n",newEntry);
    }
  }


  if(sscanf(kbuf, "delete %s", newEntry)){
    if(!dup_check(newEntry)){
      printk("modmultilist: %s doesn't exist\n", newEntry);      
      return -1;
    }
    list_for_each_safe(cur_node, aux, &procData_list){
      item = list_entry(cur_node, list_proc_data_t, links);
      if(!strcmp(item->name, newEntry)){
	vfree(item->name);
	vfree(item->data);
	
	spin_lock(&spin);
	list_del(&item->links);
	spin_unlock(&spin);
	
	printk("modmultilist: valor %d deleted\n", valor);
	break;
      }
      remove_proc_entry(newEntry,proc_entry);
      printk("modmultilist: entry %s deleted\n",kbuf);
    }
  }
  
  return len;
}

struct file_operations fops_control = {
  .write = modmultilist_control_write
};



//####################
//###Module init   ###
//####################

int init_modmultilist_module( void )
{
  list_proc_data_t *tmp;
  INIT_LIST_HEAD(&(myList[0]));
  INIT_LIST_HEAD(&procData_list);
 
  proc_entry = proc_mkdir( "modmultilist", NULL);

  if (proc_entry == NULL) {
    printk(KERN_INFO "modmultilist: Couldn't create modmultilist\n");
    return -ENOMEM;
  }
  printk(KERN_INFO "modmultilist: Module modmultilist loaded\n");

  // Appearently remove_proc_subtree take care of this entries
  proc_create("control", 0666, proc_entry, &fops_control);
    
  proc_create_data("default", 0666 ,proc_entry, &fops, &(myList[0]));

  // Adds manually default entry with name and data
  tmp = (list_proc_data_t*)vmalloc(sizeof(list_proc_data_t));
  tmp->name="default";
  tmp->data=&(myList[0]);

  spin_lock(&spin);
  list_add_tail(&tmp->links, &procData_list);
  pos++;
  spin_unlock(&spin);
  
  return 0;
}


void exit_modmultilist_module( void )
{ 
  clean_proc();
  if(remove_proc_subtree("modmultilist", NULL))
    printk(KERN_INFO "modmultilist: Couldn't remove entries\n");
  printk(KERN_INFO "modmultilist: Module modmultilist unloaded.\n");
}

  
module_init( init_modmultilist_module );
module_exit( exit_modmultilist_module );
