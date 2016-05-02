#include<stdio.h>
#include<iostream>
#include<sstream>
#include<list>
#include<string>
using namespace std;
//Definir funciones
void func(string str){
  int pos=0;
  list<char> cs;
  list<char>::iterator it=cs.end();
  for(pos=0;pos<str.length();pos++){
    if(str[pos]=='-'){
      it=cs.begin();
    }else if(str[pos]=='+'){
      it=cs.end();
    }else if(str[pos]=='*'){
      it++;
    }else if(str[pos]=='3'){
      if(it != cs.end() && !cs.empty())
	it=cs.erase(it);
    }else if(str[pos]=='\n'){
      cout << endl;
    }else{
      cs.insert(it, str[pos]);
    }    
  }
  
  for(it=cs.begin(); it != cs.end(); it++)
    cout << *it;
  //  cout << endl;
}

//Main
int main(){
  string str = "";
while(getline(cin,str)){
    func(str); //Llamada recursiva
  }
}
