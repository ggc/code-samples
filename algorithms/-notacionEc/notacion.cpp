#include<stdio.h>
#include<sstream>
#include<iostream>
#include<stack>

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
/*
 * Con pila 
 */
string notacionPila(string expr){
	
	stack<int> nums;
	int i = 0, simbolo, op1, op2, result=0;
	string ret = "";
	
	while( i < expr.length() && isNumber(expr[i]) ){
		nums.push(expr[i]-'0');
		//printf("num Apilado: %d\n", nums.top());
		i++;
	}

	while( isOp(expr[i]) && i < expr.length()){
		if(!nums.empty()){
			op2=nums.top();
			printf("2do operandor: %d\n", op2);
			nums.pop();
		}
		//printf("2do operando: %d\n", op2);
		//printf("Operandor: %c\n", expr[i]);
		//simbolo=sims.top();sims.pop(); //Operador

		if(!nums.empty()){
			op1=nums.top();nums.pop();
		} //Operando 2
		else
			return "ERROR";
			
		while( !nums.empty() ){ //Mientras haya operando y operador
		switch (expr[i]) {
			case '+':
				op2 = op1 + op2;
				break;
			case '-':
				op2 = op1 - op2;
				break;
			case '*':
				op2 = op1 * op2;
				break;
			case '/':
				if(op2)
					op2 = op1 / op2;
				else
					return "ERROR";
				break;
			default:
				return "ERROR";
				break;  
		}
		printf("result: %d\n", op2);
	}
	}  
	return itos(op2);
}
/*
 * Con cola 
 */
string notacionCola(string expr){
	
	queue<int> nums;
	int i = 0, simbolo, op1, op2, result=0;
	string ret = "";
	
	while( i < expr.length() && isNumber(expr[i]) ){
		nums.push(expr[i]-'0');
		printf("num Apilado: %d\n", nums.top());
		i++;
	}
	
	if(!nums.empty()){
		op2=nums.top();
		nums.pop();
	}
	
	while(!sims.empty() && !nums.empty()){ //Mientras haya operando y operador
		printf("2do operando: %d\n", op2);
		printf("Operandor: %c\n", sims.top());
		//simbolo=sims.top();sims.pop(); //Operador

		if(!nums.empty()){
			printf("1er operandor: %d\n", nums.top());
			op1=nums.top();nums.pop();
		} //Operando 2
		else
			return "ERROR";
			
		switch (expr[i]) {
			case '+':
				op2 = op1 + op2;
				break;
			case '-':
				op2 = op1 - op2;
				break;
			case '*':
				op2 = op1 * op2;
				break;
			case '/':
				if(op2)
					op2 = op1 / op2;
				else
					return "ERROR";
					break;
			default:
				return "ERROR";
				break;  
		}
		printf("result: %d\n", op2);
	}  
	return itos(op2);
}

int main(){
string expr = "";
while(cin >> expr){
cout << "String: " << expr << endl;
//Comparar y mostrar en funcion del resultado
cout << notacionPila(expr) << endl;
}
}
