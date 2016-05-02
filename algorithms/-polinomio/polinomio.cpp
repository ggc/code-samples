#include<stdio.h>
#include<iostream>
using namespace std;

#define N 10000

int polinomio(int v[], int incog, int n, int pos, int acu, int result){
  //  int result=0;
  //  printf("acu=%d\n",acu);
  if(pos==n-1)
    result=result+acu*v[pos];
  else{
    result=result+acu*v[pos];
    result=polinomio(v,incog,n,pos+1,acu*incog, result);
  }
  //  printf("result=%d\n",result);
  return result;
}

int polinomio(int v[], int incog, int n){
  return polinomio(v, incog, n, 0, 1, 0);
}

int main(){
  int v[10], n, x, i=0, pos=0;
  cin >> n;
  while(n>0){
    i=0;
    while(i<n){
      cin >> v[i];
      i++;
    }
    cin >> x;
    cout << polinomio(v, x, n) << endl;
    cin >> n;
  }
}
