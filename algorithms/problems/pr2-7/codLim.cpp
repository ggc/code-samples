#include "HashMap.h"
#include "Exceptions.h"
#include "Hash.h"
#include "List.h"
#include "Queue.h"
#include <iostream>
#include <stdio.h>
using namespace std;

int main(){
  int nR, nS, i=0;
  string reys, sucs;
  string nombre = "";
  HashMap<string, int> hR = HashMap<string, int>();
  while(cin >> nR){
    cin.ignore();
    getline(cin, reys);
    
    while(i<reys.length() && nR>0){
      if(reys[i]==' ' || reys[i]=='\0'){
	cout << nombre << endl;
	cout << "valor de tabla hash: " << ++hR[nombre] << endl;
	nombre="";
	nR--;
      }else{
	nombre = nombre + reys[i];
      }
      i++;
    }
    cout << nombre << endl;
    cout << "valor de tabla hash: " << ++hR[nombre] << endl;
	

    cout << "Introducir sucesores: " << endl;
    cin >> nS;
    
    nombre="";
    cin.ignore();
    getline(cin, sucs);
    i=0;
    while(i<sucs.length() && nS>0){
      if(sucs[i]==' ' || sucs[i]=='\0'){
	cout << nombre << endl;
	cout << "Solucion:  " << ++hR[nombre] << endl;
	hR[nombre];
	nombre="";
	nS--;
      }else{
	nombre = nombre + sucs[i];
      }
      i++;
    }
    cout << nombre << endl;
    cout << "valor de tabla hash: " << ++hR[nombre] << endl;
  }
}
