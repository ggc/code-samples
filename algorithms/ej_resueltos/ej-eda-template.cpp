#include<stdio.h>
#include<iostream>
using namespace std;

#define N 10000
//Definir funciones
bool escaleraCreciente(int v[], int n, int actAncho, int maxAncho, int siguiente){
  if(n<0){
    if(maxAncho==-1){
      if(v[n]==siguiente){
	actAncho++;
	escaleraCreciente(v, n-1, actAncho, -1, v[n]);
      }
      else if(v[n]<siguiente){
	maxAncho=actAncho;
	escaleraCreciente(v, n-1, 1, v[n]);
      }
      else
	return false;
    }
  }
  else{
    if(v[n]==siguiente){
	actAncho++;
	escaleraCreciente(v, n-1, actAncho, -1, v[n]);
      }
    else if(v[n]<siguiente && actAncho==maxAncho){  
      escaleraCreciente(v, n-1, 1, v[n]);
    }
    else
      return false;
  }
}

bool escaleraCreciente(int v[], int n){
  return escaleraCreciente(v, n-2, 1, -1, v[n]);
}
//Main
int main(){
  int x[N], n, i=0;
  cin >> n;
  while(n>0){
    i=0;
    while(i<n){
      cin >> v[i];
      i++;
    }
    cin >> x;
    cout << escaleraCreciente(v,n) << endl; //Llamada recursiva
    cin >> n;
  }
}
