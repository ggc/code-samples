#include<stdio.h>
#include<iostream>
using namespace std;

#define N 10000
//Definir funciones
bool escaleraCreciente(int v[], int n, int actAncho, int maxAncho, int anterior){

  printf("n:%d ,actAncho:%d, maxAncho:%d, actual:%d, anterior:%d \n",n,actAncho,maxAncho,v[n],anterior);

  if(n>=0){
    if(maxAncho==-1){
      if(v[n]==anterior){
	//	printf("inicioEsc:actAncho:%d\n",actAncho);
	actAncho++;
	escaleraCreciente(v, n-1, actAncho, -1, v[n]);
      }
      else if(v[n]<anterior){
	//	printf("inicioFinEsc:actAncho:%d_Cambio\n",actAncho);
	escaleraCreciente(v, n-1, 1, actAncho, v[n]);
      }
      else{
	//	printf("inicioExit:actAncho:%d\n",actAncho);
	return false;
      }
    }
    else{
      if(v[n]==anterior){
	//	printf("consecutivosEsc:actAncho:%d\n",actAncho);
	actAncho++;
	escaleraCreciente(v, n-1, actAncho, maxAncho, v[n]);
      }
      else if(v[n]<anterior && actAncho==maxAncho){
	//	printf("consecutivosFinEsc:actAncho:%d_Cambio\n",actAncho);
	escaleraCreciente(v, n-1, 1, maxAncho, v[n]);
      }
      else{
	//	printf("consecutivosExit:actAncho:%d\n",actAncho);
	return false;
      }
    }
  }
  else{
    //    printf("Exit:actAncho:%d\n",actAncho);
    return (actAncho==maxAncho ||  maxAncho == -1);
  }
}

bool escaleraCreciente(int v[], int n){
  return escaleraCreciente(v, n-2, 1, -1, v[n-1]);
}
//Main
int main(){
  int x[N], n, i=0;
  cin >> n;
  while(n>0){
    i=0;
    while(i<n){
      cin >> x[i];
      i++;
    }
    cout << escaleraCreciente(x,n) << endl; //Llamada recursiva
    cin >> n;
  }
}
