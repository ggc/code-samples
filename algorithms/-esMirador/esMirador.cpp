#include<iostream>
using namespace std;

int esMirador(int x[], int N){
  int sigMax=x[N-1], count=1;
  for(int i=N-1;i>=0;i--){
    if(x[i]>sigMax){
      count++;
      sigMax=x[i];
    }
  }
  return count;
}

int main(){
  int x[10000], N, i=0;
  cin >> N;
  while(N!=0){
    while(i<N){
      cin >> x[i];
      i++;
    }
    cout << esMirador(x,N) << endl;
    cin >> N;
  }
}
