#include<stdio.h>
#include<iostream>
#include<string>
using namespace std;

char* creditoSegMax(int A[], int N){
  char *result;
  result = new char[10*sizeof(char)];
  int cntr=0, index=0, i=0, q=N-1, p=0, topCntr=0;
  while(i<N){
    if(A[i]>0){
      //      printf("A[i]>0\n");
      cntr++;}
    else if(A[i]<0 && cntr>0){
      //      printf("A[i]<0\n");
      cntr--;}
    if(cntr==0 && i<q){
      //      printf("cntr==0 && p<=q\n");
      p=i+1;}
    if(cntr>topCntr){
      //      printf("cntr>topCntr\n");
      topCntr=cntr;
      q=i;
    }
    i++;
  }
  sprintf(result,"%d-%d", p, q);
  return result;
}

int main(){
  int A[1000], N, i=0, j=0;
  printf("Tamano del muestreo: ");
  cin >> N;
  while(N>=0){
    i=0;
    printf("Valores separados por espacio: \n");
    while(i<N){
      cin >> A[i];
      i++;
    }
    printf("Lamada a funcion\n");
    cout << creditoSegMax(A,N) << endl;
    printf("Tamano del muestreo: ");
    cin >> N;
  }
  return 0;
}
