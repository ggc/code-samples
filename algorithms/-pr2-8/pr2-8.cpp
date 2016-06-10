#include "Arbin.h"
#include "Exceptions.h"
#include "List.h"
#include "Queue.h"
#include "TreeMap.h"
#include <iostream>
#include <stdio.h>
using namespace std;

void comprobar(TreeMap<string, int> tm, int asig){
  TreeMap<string, int>::ConstIterator it = tm.cbegin();
  cout << "Asignatura " << asig << endl;
  while(it != tm.cend()){
    if(it.value() != 0)
      cout << it.key() << ", " << it.value() << endl;
    it.next();
  }

}

int main(){
  int nE, vnota, asignatura=1, notaAnt;
  string nombre, nota;
  
  TreeMap<string,int> tm;
  cin >> nE;
  cin.ignore();
  while(nE>0){
    tm = TreeMap<string,int>();
    
    while(nE>0){

      nombre = "";
      nota = "";
    
 
      getline(cin, nombre);
      getline(cin, nota);

      //cout << "      nombre:" << nombre << endl;
      
      if(nota.compare("CORRECTO"))
	vnota = -1;
      else if(nota.compare("INCORRECTO"))
	vnota = 1;
      else
	vnota = 0;

      if(tm.contains(nombre)){
	notaAnt=tm.at(nombre);
	tm.insert(nombre, vnota+notaAnt);
	//cout << "suma" << endl;
      }
      else
	tm.insert(nombre, vnota);
      //cout << tm.at(nombre) << endl;
      nE--;
    }

    comprobar(tm, asignatura);
    asignatura++;
    

    cin >> nE;
    cin.ignore();
  }
}
