#include "HashMap.h"
#include "Exceptions.h"
#include "Hash.h"
#include "List.h"
#include "Queue.h"
#include <iostream>
#include <stdio.h>
using namespace std;

int main(){
  int nR, nS, i;
  string reys, sucs;
  string nombre;
  HashMap<string, int> hR;
  cin >> nR;
  while(nR>0){
    i=0;
    hR = HashMap<string, int>();
    nombre = "";
    cin.ignore();
    getline(cin, reys);

    //Lectura de reyes actuales//
    while(i<reys.length() && nR>0){
      if(reys[i]==' ' || reys[i]=='\0'){
	++hR[nombre];
	nombre="";
	nR--;
      }else{
	nombre = nombre + reys[i];
      }
      i++;
    }
    ++hR[nombre];
    

    //Lectura de sucesores//
    cin >> nS;
    nombre="";
    cin.ignore();
    getline(cin, sucs);
    i=0;
    while(i<sucs.length() && nS>0){
      if(sucs[i]==' ' || sucs[i]=='\0'){
	cout << ++hR[nombre] << endl;
	hR[nombre];
	nombre="";
	nS--;
      }else{
	nombre = nombre + sucs[i];
      }
      i++;
    }
    cout << ++hR[nombre] << endl;
    cin >> nR;
  }
}
