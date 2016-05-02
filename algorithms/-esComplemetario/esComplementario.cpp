#include<stdio.h>
#include<iostream>
using namespace std;

#define N 10000
//Definir funciones
void esComplementario(int x, int dec, int& result){
  if(x!=0){
    result = result + ((9-x%10)*dec);
    esComplementario(x/10, dec*10, result);
  }
}

int esComplementario(int x){
  int result;
  if(x==0)
    result=9;
  else
    result=0;
  esComplementario(x,1,result);
  return result;
}
//Main
int main(){
  int x;
  cin >> x;
  while(x>=0){
    cout << esComplementario(x) << endl; //Llamada recursiva
    cin >> x;
  }
}
