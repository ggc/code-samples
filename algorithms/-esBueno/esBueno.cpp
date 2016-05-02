#include<iostream>
using namespace std;

int sumaBuenos(int x[], int N){
  int suma=0, exp2=1;
  for(int i=0;i<N;i++){
    if(x[i]==exp2)
      suma+=exp2;
    exp2=exp2*2;
  }
  return suma;
}

int main(){
  int x[10000], N, i=0;
  cin >> N;
  while(i<N){
    cin >> x[i];
    i++;
  }
  cout << sumaBuenos(x,N) << endl;
}
