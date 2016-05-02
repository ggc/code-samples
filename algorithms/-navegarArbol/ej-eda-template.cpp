#include<stdio.h>
#include<iostream>
#include "arbin.h"
using namespace std;

#define N 100000
//Definir funciones

Arbin<char> hacerArb(String cadena){
  int i = 1;
  
  if(cadena.length()==0)
    return Arbin();
  if(cadena.length()==1)
    return Arbin(cadena[0]);
  
  while(cadena[i]!='\0'){
    if(cadena[i]=='Y'){
      fin=i-1;
    }

    else if(cadena[i]=='*'){
      if(hijoIz()==null)
	Arbin<char> iz = Arbin('*');
      else if(hijoDr()==null)
	Arbin<char> dr = Arbin('*');
      else
	return false;
    }

    else if(cadena[i]=='.'){
      if(hijoIz()==null)
	Arbin<char> iz = Arbin('.');
      else if(hijoDr()==null)
	Arbin<char> dr = Arbin('.');
      else
	return false
    }
    else


  }
arbNav = arbin(cadena[ini]);


}


bool arbNav(arbin<char> &arbNav, String cadena, int ini, int fin){
  

}
//Main
int main(){
  int x[N], n, i=0;
  String cadena [N];
  cin >> cadena;
  if(arbNav())
    cout << "OK";
  else
    cout << "KO";
  while(true){
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
