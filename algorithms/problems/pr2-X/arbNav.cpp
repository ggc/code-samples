#include<stdio.h>
#include<iostream>
#include<sstream>
#include<string>
#include "Arbin.h"
#include "Exceptions.h"
#include "Queue.h"
using namespace std;

#define N 100000
//Definicion de funciones
void arbNav(string cadena){
  Arbin<char> arbol = Arbin();
}

//Main
int main(){
  string cadena;
  while(cin >> cadena){
    arbNav(cadena);
    cout << cadena << endl; //Llamada recursiva
  }
}
