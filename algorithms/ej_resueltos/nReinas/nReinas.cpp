#include<stdio.h>
#include<iostream>
using namespace std;

void mostrarSol(int solucion[], int N){
  cout << "Solucion: " << endl;
  int i=0;
  for(i=0;i<N;i++)
    cout << solucion[i] << endl;

}

int nReinas(int x[][1000], int N, int fila,  bool marcas[1000][1000], int solucion[]){
  int resultado=1, i=0, j=0;
  if(fila<N)
    if(N==0)
      mostrarSol(solucion, N);
    else
      for(i=fila;i<N && !marcas[fila][i];i++){
	solucion[fila]=i;
	marcas[fila][i]=true;
	N--;
	nReinas(x,N,1,marcas,solucion);
	N++;
	marcas[fila][i]=false;
      }
  return resultado;
}

int main(){
  int x[1000][1000], N, i=0, j=0, solucion[1000];
  bool marcas[1000][1000];
  cin >> N;
  while(N>=0){
    i=0;
    while(i<N){
      j=0;
      //printf("Introduce fila %d\n",i);
      while(j<N){
	//printf("j=%d\n",j);
	cin >> x[i][j];
	j++;
      }
      i++;
      //printf("i=%d\n",i);
    }
    //printf("Culo\n");
    cout << nReinas(x,N,0,marcas,solucion) << endl;
    cin >> N;
  }
}
