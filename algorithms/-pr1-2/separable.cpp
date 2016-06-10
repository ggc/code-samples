#include<iostream>
using namespace std;

int esSeparable(int x[], int N){
  int resultado = 1;
  if(N<=0)
    resultado = -1;
  for(int i=0;i<N-1;i++){
    if(x[i]-x[i+1] < 0)
      resultado=0;
  }
  return resultado;
}

int main(){
  int x[1000], N, i=0;
  cin >> N;
  while(N>=0){
    i=0;
    while(i<N){
      cin >> x[i];
      i++;
    }
    cout << esSeparable(x,N) << endl;
    cin >> N;
  }
}
