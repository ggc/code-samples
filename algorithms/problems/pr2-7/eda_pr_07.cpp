#include <iostream>
#include "Arbin.h"

using namespace std;
#define N 100000

Arbin<char> construir(string str, int &i, List<char> &l) {
  if(i>str.length()+10)
    cin >> i;
  int centro = i;
  if(str[i]==' ' && str[i+1]==' '){
    l.push_back(str[centro-1]);
    //    cout << l.front() << endl;
  }
  //cout << "centro: " << i << " letra: " << str[i] << endl;
  if(str[i]==' '){
    return Arbin<char>();
  }

  Arbin<char> aIz = construir(str, ++i, l);
  Arbin<char> aDr = construir(str, ++i, l);

  return Arbin<char>(aIz, str[centro], aDr);
}

int main() {
  
  string str="";
  while(getline(cin,str)){
    
    int i = 0;
    Arbin<char> arbol = Arbin<char>();
    List<char> lista = List<char>();
  
    arbol = construir(str, i, lista);
   
    //Recorrido de las hojas
    List<char>::ConstIterator it= lista.cbegin();
    while(!(it == lista.cend())){
      cout << it.elem();
      ++it;
    }
    cout << endl;
    
    i = 0;
    str="";
    //arbol;
    
  
  }
  return 0;
}
