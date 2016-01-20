#include<stdio.h>
#include<iostream>
using namespace std;

int esSimetrica(int x[][1000], int N){
  int resultado=1, i=0, j=0;
  while(resultado && i<N-1){
    //    printf("i:%d,j:%d\n",i,j);
    if(x[i][j]!=x[j][i])
      resultado=0;
    else{
      //      printf("x-i,j:%d,x-j,i:%d\n",x[i][j],x[j][i]);
      j++;
      if(j==N){
	i++;
	j=i+1;
      }
    }
  }
  return resultado;
}

int main(){
  int x[1000][1000], N, i=0, j=0;
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
    cout << esSimetrica(x,N) << endl;
    cin >> N;
  }
}
