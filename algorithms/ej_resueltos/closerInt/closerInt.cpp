#include<stdio.h>
#include<iostream>
using namespace std;

#define N 10000

int closerInt(int x[], int n){
  int i=0, min=x[n-1], result=0, aux;
  while(i<n-1){
    aux = (x[i]-x[i+1]);
    if(aux<0){
      aux=aux*-1;
    }
    if(aux<=min){
      result = i;
      min = aux;
    }
    i++;
  }
  printf("\nLos enteros mas cercanos son: %d y %d\n", x[result], x[result+1]);
  return 0;
}

int quickSort(int x[], int a, int b){
  int i = a, j = b, tmp; 
  int p = x[(a + b) / 2]; 
  
  while (i <= j) { 
    while (x[i] < p) i++; 
    while (x[j] > p) j--; 
    if (i <= j){ 
      tmp = x[i]; 
      x[i] = x[j]; 
      x[j] = tmp; 
      i++; 
      j--; 
    } 
  } 
  if (a < j) 
    quickSort(x, a, j); 
  if (i < b) 
    quickSort(x, i, b); 
}


int main(){
  int x[N], n, i=0, j=0;
  cin >> n;
  while(n>=0){
    i=0;
    j=0;
    while(i<n){
      cin >> x[i];
      i++;
    }
    quickSort(x,0,n-1);
    printf("Quicksorting\n");
    while(j<n){
      cout << x[j] << ' ';
      j++;
    }
    cout << closerInt(x,n) << endl;
    cin >> n;
  }
}
