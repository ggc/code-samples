#include<stdio.h>
#include<sstream>
#include<iostream>
#include<stack>
#include<queue>

using namespace std;

string itos(int i){ // convert int to string
	stringstream s;
	s << i;
	return s.str();
}

bool isNumber(char c){
	return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
}

bool isOp(char c){
	return c == '+' || c == '-' || c == '*' || c == '/';
}

/*#########################
 * Con pila ###############
 *#########################
 */
string notacionPila(string expr){
	
	stack<int> nums;
	int i = 0, simbolo, op1, op2, result=0;
	string ret = "";
	
	//apilo valores
	while( i < expr.length() && isNumber(expr[i]) ){
		nums.push(expr[i]-'0');
		i++;
	}
	
	//Saca 2do operador (Solo la primera vez)
	if(!nums.empty()){
		op2=nums.top();
		nums.pop();
	}
  if(!nums.empty()){
		op1=nums.top();
		nums.pop();
		nums.push(op2);
	}	
	while( isOp(expr[i]) && i < expr.length()){
		if(!nums.empty()){
			op2=nums.top();nums.pop();
		} //Operando 2
		else
			return "ERROR";
		
	  printf("pila: %d %c %d\n", op1, expr[i], op2);
		switch (expr[i]) {
			case '+':
				op1 = op1 + op2;
				break;
			case '-':
				op1 = op1 - op2;
				break;
			case '*':
				op1 = op1 * op2;
				break;
			case '/':
				if(op1)
					op1 = op1 / op2;
				else
					return "ERROR";
				break;
			default:
				return "ERROR";
				break;  
		}
		//printf("result: %d\n", op2);
		i++;
	}  
	return itos(op1);
}
	/*#########################
	* Con cola ################
	* ######################### 
	*/
	string notacionCola(string expr){
	
	queue<int> nums;
	int i = 0, simbolo, op1, op2, result=0;
	string ret = "";
	
	while( i < expr.length() && isNumber(expr[i]) ){
		nums.push(expr[i]-'0');
		//printf("num Apilado: %d\n", nums.top());
		i++;
	}
	
	
	//Saca 2do operador (Solo la primera vez)
	if(!nums.empty()){
		op2=nums.front();
		//printf("2do operandor(front): %d\n", op2);
		nums.pop();
	}
	//printf("2do operando: %d\n", op2);
	//simbolo=sims.top();sims.pop(); //Operador
	while( isOp(expr[i]) && i < expr.length()){
	  //printf("Operandor: %c\n", expr[i]);
		if(!nums.empty()){
			op1=nums.front();nums.pop();
	    //printf("1er Operandor: %d\n", op1);
		} //Operando 2
		else
			return "ERROR";
		
	  printf("cola: %d %c %d\n", op1, expr[i], op2);
		//while( !nums.empty() ){ //Mientras haya operando y operador
		switch (expr[i]) {
			case '+':
				op1 = op1 + op2;
				break;
			case '-':
				op1 = op1 - op2;
				break;
			case '*':
				op1 = op1 * op2;
				break;
			case '/':
				if(op2)
				  if(i=0)
						op1 = op1 / op2;
					else
						op1 = op2 / op1;
						
				else
					return "ERROR";
				break;
			default:
				return "ERROR";
				break;  
		}
		//printf("result: %d\n", op2);
		i++;
	}  
	return itos(op2);
}

int main(){
	string expr = "", str1, str2;
	while(cin >> expr){
		//cout << "String: " << expr << endl;
		//Comparar y mostrar en funcion del resultado
		str1 = notacionPila(expr);
		str2 = notacionCola(expr);
		if( !str1.compare(str2) )
			cout << str1 << " == " << str2 << endl;
		else
			cout << str1 << " != " << str2 << endl;
		//cout << "Con pila: " << notacionPila(expr) << endl;
		//cout << "Con cola: " << notacionCola(expr) << endl;
	}
}
