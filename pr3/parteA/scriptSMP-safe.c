#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

int main(){
	int i;
	char * op;
	FILE * fd;
	op = malloc(10*sizeof(char));
	if(fork()!=0){
		for(i=0;i<500;i++){
			fd = fopen("/proc/modlist", "w");
			sprintf(op,"add %i\n\0",i);
			fputs(op,fd);	
			fclose(fd);
		}
	}else{
		for(i=500;i<1000;i++){
			fd = fopen("/proc/modlist", "w");
			sprintf(op,"add %i\n\0",i);
			fputs(op,fd);	
			fclose(fd);
		}
	}		
	wait();
	return 0;
}
