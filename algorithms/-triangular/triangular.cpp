#include<stdio.h>
#include<iostream>
using namespace std;

int esTriangular(int x[][1000], int N){
  int triSup=1, triInf=0, i=0, j=1;
  while((triSup || triInf) && i<N-1){
    //    printf("i:%d,j:%d\n",i,j);
    if(x[i][j]!=0)
      triSup=0;
    else if(x[j][i]!=0)
      triInf=0;
    else{
      //      printf("x-i,j:%d,x-j,i:%d\n",x[i][j],x[j][i]);
      j++;
      if(j==N){
	i++;
	j=i+1;
      }
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
    if(esTriangular(x, N))
      cout << "SI" << endl;
    else
      cout << "NO" << endl;

    //    cout << esTriangular(x,N) << endl;
    cin >> N;
  }
}
