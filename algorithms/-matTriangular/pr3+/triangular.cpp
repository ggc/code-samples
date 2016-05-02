#include<stdio.h>
#include<iostream>
using namespace std;

int esTriangular(int x[][1000], int N){
  int triInf=1, triSup=1, i=0, j=1;
  while((triInf||triSup) && i<N-1){
    if(x[j][i]!=0)
      triInf=0;
    if(x[i][j]!=0)
      triSup=0;
    j++;
    if(j==N){
      i++;
      j=i+1;
    }
  }
  return (triSup || triInf);
}

int main(){
  int x[1000][1000], N, i=0, j=0;
  cin >> N;
  while(N>0){
    i=0;
    while(i<N){
      j=0;
      while(j<N){
	cin >> x[i][j];
	j++;
      }
      i++;
    }
    if(esTriangular(x, N))
      cout << "SI" << endl;
    else
      cout << "NO" << endl;
    cin >> N;
  }
}
