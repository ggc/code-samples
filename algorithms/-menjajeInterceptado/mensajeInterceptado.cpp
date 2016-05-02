#include<stdio.h>
#include<iostream>
#include<sstream>
#include<list>
#include<stack>
#include<string>
#include<iterator>
using namespace std;
//Definir funciones
void codifica(string str){
  list<char> x1;
  list<char> x2;
  stack<char> cs;
  string ret;
  int i = 0;
  //cout << "empieza x1" << endl;
  //Primera transformacion
  for(int i = 0; i<str.length();i++){
    if(str[i]=='a' || str[i]=='e' || str[i]=='i' || str[i]=='o' || str[i]=='u' || str[i]=='A' || str[i]=='E' || str[i]=='I' || str[i]=='O' || str[i]=='U'){
      while(!cs.empty()){
	x1.push_back(cs.top());
	cs.pop();
      }
      x1.push_back(str[i]);
    }
    else
      cs.push(str[i]);
  }
  while(!cs.empty()){
    x1.push_back(cs.top());
    cs.pop();
  }

  copy (x1.begin(), x1.end(), ostream_iterator<char>(cout,""));
  cout << endl;
  //cout << "empieza x2" << endl;
  //Segunda transformacion
  while(!x1.empty()){
    x2.push_back(x1.front());
    x1.pop_front();
    //cout << x2.back() << endl;
    if(!x1.empty()){
      x2.push_back(x1.back());
      x1.pop_back();
      //cout << x2.back() << endl;
    }
  }
  //cout << endl;
  copy (x2.begin(), x2.end(), ostream_iterator<char>(cout,""));
  
  //cout << endl << "empieza retorno" << endl;
  //for(int j=0;j<str.length() && !x2.empty();j++){
  //  ret[j]=x2.front();
  //  x2.pop_front();
  //}
  //return ret;
}

void decodifica(string str){
  list<char> x1;
  list<char> x2;
  stack<char> cs;
  string ret;
  int i = 0;
  //Deshacer segunda transformacion
  for(int i=0;i<str.length();i++){
    if(i%2==0)
      x2.push_back(str[i]);
    else
      cs.push(str[i]);
  }
  while(!cs.empty()){
    x2.push_back(cs.top());
    cs.pop();
  }
  //copy (x2.begin(), x2.end(), ostream_iterator<char>(cout,""));

  str="";
  while(!x2.empty()){
    str=str+x2.front();
    x2.pop_front();
  }
  //Deshacer primera transformacion
  
  for(int i = 0; i<str.length();i++){
    if(str[i]=='a' || str[i]=='e' || str[i]=='i' || str[i]=='o' || str[i]=='u' || str[i]=='A' || str[i]=='E' || str[i]=='I' || str[i]=='O' || str[i]=='U'){
      while(!cs.empty()){
	x1.push_back(cs.top());
	cs.pop();
      }
      x1.push_back(str[i]);
    }
    else

      cs.push(str[i]);
  }
  while(!cs.empty()){
    x1.push_back(cs.top());
    cs.pop();
  }
  copy (x1.begin(), x1.end(), ostream_iterator<char>(cout,""));

}
bool resuelve(){
  string str;
  getline(cin,str);
  if(!cin)
    return false;
  cout << str << " => ";
  decodifica(str); 
  cout << endl;
  return true;
}

//Main
int main(){
  string str = "";
  while(resuelve());
}
