#include <stdio.h>
#include <iostream>
using namespace std;
 
int isGaspar(int n, int v[]){
  int gaspar = 1, semisuma = 0;
  for(int i = 0; (i<n) && (gaspar == 1); i++){
    semisuma += v[i];
    if(semisuma<0) gaspar=0;
  }
  if((gaspar == 1) && (semisuma != 0)) gaspar=0;
  return gaspar;
}
 
int main(){
  //Pre 0<=n<=10000
  int v[10001], i = 0, n;
  cin>>n;
  //printf("n: %i\n",n);
  while(n>0){
    for(i=0;i<n;i++){
      cin>>v[i];
      //printf("v[i]: %i\n",v[i]);
    }
    cout<<isGaspar(n, v)<<endl;
    //printf("isGaspar: %i\n",isGaspar(n,v));
    cin>>n;
  }
}
