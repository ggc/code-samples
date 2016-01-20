#include "cbuffer.h"

int main(){
  /*  int *head;
  int *ints;
  head=ints;
  int i = 0;
  //int n1 = 99;
  //ints=&n;
  //  ints+=+sizeof(int);
  //ints+=1;
  //ints=&n1;
  for(i=0;i<5;i++)
    (ints+i)=i*100;
  
  for(i=0;i<5;i++)
    printf("*ints=%d\n",*(ints+i));
  
  printf("*head=%d\n",*head);
  printf("sizeofint=%d\n",(int)sizeof(int));

  //printf("ints=%d",ints); //Mal
  */

  cbuffer_t *buf;
  buf=create_cbuffer_t(10);
  
  return 0;
}
