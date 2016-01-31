obj-m += multififo.o #fifomod.c no ha de existir
multififo-objs = prf.o cbuffer.o
all:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) modules

clean:
	make -C /lib/modules/$(shell uname -r)/build M=$(PWD) clean
