#include<stdio.h>
#include<iostream>
using namespace std;

void ferry(int x[], int N, int pos, int babor, int estribor, bool posiciones[], int &tamRestante){
  int i=0;
  for(i=0;i<N;i++){
    if(x[i]>babor && x[i]>estribor)
      if(babor+estribor < tamRestante)
	tamRestante=babor+estribor;

    babor=babor-x[i];
    posiciones[i]=false;
    ferry(x,N,pos,babor,estribor,posiciones,tamRestante);
    babor=babor+x[i];
    posiciones=true;
    estribor=estribor-x[i];
    ferry(x,N,pos,babor,estribor,posiciones,tamRestante);



  }  cout << "x[pos]: " << x[pos] << endl;
  
      cout << ">Babor: " << babor << endl;
      cout << "Estribor: " << estribor << endl;
  
    ferry(x,N,pos+1,babor,estribor,solucion);
  
}

int main(){
  int x[1000], N, solucion[1000], i=0;
  bool posiciones[1000];
  cin >> N;
  while(i<N){
    cin >> x[i];
    i++;
  }
  ferry(x,N,0,N,N,posiciones,N*2);
  i=0;
  while(i<N){
    cout << solucion[i] << endl;
    i++;
  }
}
