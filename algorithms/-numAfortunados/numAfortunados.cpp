#include<stdio.h>
#include<iostream>
#include<list>
using namespace std;

#define N 10000
//Definir funciones
void func(int n){
  cout << n << ":";
  list <int> xs;
  int m = 2, aux=0;
  for(int i=1;i<=n;i++)
    xs.push_back(i);
  while(m<=n){
    for(int i=0; i<n;i++){
      if(i%m==0)
	xs.pop_front();
      else{
	xs.push_back(xs.front());
	aux++;
	xs.pop_front();
      } 
    }
    n=aux;
    aux=0;
    m++;
  }
  while(!xs.empty()){
    cout << " " << xs.back();
    xs.pop_back();
  }
  cout << endl;
}

//Main
int main(){
  int n, i=0;
  cin >> n;
  while(n>0){
    func(n); //Llamada recursiva
    cin >> n;
  }
}
