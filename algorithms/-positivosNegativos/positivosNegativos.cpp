#include<stdio.h>
#include<iostream>
using namespace std;

void positivosNegativos(int x[], int N){ //Pone negativos a la izq y pos a la der
  int i=0,j=N-1,aux=0,exit=0;
  while(i<N && !exit){
    if(x[i]>0){
      //printf("i=%d,j=%d\n",i,j);
      if(x[j]<=0){
	aux=x[j];
	x[j]=x[i];
	x[i]=aux;
	j--;
	i++;
      }
      else
	j--;
    }
    else{
      //printf("i=%d,j=%d\n",i,j);
      i++;
    }
    if(i>=j)
      exit=1;
  }
}

int main(){
  int x[10000], N, i=0;
  cin >> N;
  while(i<N){
    cin >> x[i];
    i++;
  }
  positivosNegativos(x,N);
  i=0;
  while(i<N){
    cout << x[i] << ' ';
    i++;
  }
}
