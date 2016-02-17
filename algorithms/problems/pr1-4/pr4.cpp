#include<stdio.h>
#include<iostream>
using namespace std;

#define N 10000

int dec2bin(int x, int pos){
  int i=0, result=0;
  if(x>1){
    //    printf("pos:%d\n",pos);
    result=dec2bin(x/2,pos*10)+(x%2)*pos;
  }
  else
    result=x*pos;
  //    printf("result:%d\n",result);
  return result;
}

int bin2dec(int x, int pos){
  int result=0;
  if(x>0){
    result=bin2dec(x/10,pos*2)+(x%10)*pos;
  }
  else{
    result=x*pos;
  }
  return result;
}

int main(){
  int x, n, i=0;
  cin >> x;
  while(x>=0){
    cout << bin2dec(x,1) << endl;
    cin >> x;
  }
}
