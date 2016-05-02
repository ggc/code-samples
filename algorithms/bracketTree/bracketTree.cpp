#include "Arbin.h"
#include "Exceptions.h"
#include "List.h"
#include "Queue.h"
#include <iostream>
#include <stdio.h>
using namespace std;

string brackets(Arbin<T> t){
  //Caso base - Es vacio
  if t.esVacio() return "()";
  //Caso recursivo
  

  return brackets(t.hijoIz() + t.raiz() + t.hijoDr());
}

int main(){
  Arbin<char> arbol = Arbin<char>();
  cout << brackets(arbol) << endl;
  arbol = Arbin<char>(Arbin<char>(), 'A', Arbin<char>());
  cout << brackets(arbol) << endl;
}
