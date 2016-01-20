#include <linux/random.h>
#include "cbuffer.h"


static struct proc_dir_entry *proc_entry //crea entrada /proc
#haydos?
static struct workqueue_struct *my_wq;
struct timer_list timer;

//Variables visibles en modconfig
#inicializar
static unsigned long timer_period;
static int max_random;
static unsigned int emergency_threshold;

//Wrapper para el work_struct
typedef struct {
  struct work_struct wq;
  int param;
}

//Lista enlazada
typedef struct {
  int data;
  struct list_head link;
}
  LIST_HEAD(lnls); //init LiNkedLiSt
////Funciones
static ssize_t modconfig_read(struct file *filp, char __user *buff, size_t len, loff_t *off);

static ssize_t modconfig_write(struct file *file, const char __user *buffer, unsigned long count, loff_t *data);

static const struct file_operacions proc_entry_fops_modconfig = {
  .read = modconfig_read,
  .write = modconfig_write,
};

static int modtimer_open(struct inode *inode, struct file *file);

static int modtimer_release(struct inode *inode, struct file *file);

static ssize_t modtimer_read(struct file *file, char *user, size_t nbytes, loff_t *offset);

void modtimer_init(void){}
void modtimer_clean(void){}

static const struct file_operacions proc_entry_fops_modtimer = {
  .open = modtimer_open,
  .release = modtimer_release,
  .write = modtimer_write,
};

module_init(modtimer_init);
module_exit(modtimer_clean);

//###Timer###
//Function invoked when timer expires
static void fire_timer(unsigned long data){
  static int n = 0;

  n = get_random_int();
  /* Re-activate the timer one second from now */
  mod_timer( &(timer), jiffies + timer_period); 
}

#usodetimer
// Create timer 
    init_timer(&my_timer);
// Initialize field
    my_timer.data=0;
    my_timer.function=fire_timer;
my_timer.expires=jiffies + HZ;  // Activate it one second from now
// Activate the timer for the first time
    add_timer(&my_timer); 
#m

//// Tarea diferida
//Workqueue

static void wq_function(struct work_struct *wq){

}
