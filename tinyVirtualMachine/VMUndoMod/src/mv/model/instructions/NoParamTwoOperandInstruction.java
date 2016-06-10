package mv.model.instructions;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.EmptyStackException;
import mv.model.exceptions.stack.FullStackException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public abstract class NoParamTwoOperandInstruction implements Instruction {

	protected String type;

	protected NoParamTwoOperandInstruction(String type){
		this.type=type;
	}

	protected abstract void operate(OperandStack stack,Memory memory,ControlUnit control,int operand1,int operand2) throws InstructionException;

	protected abstract Instruction createInstruction();

	public Instruction parse(String line){

		Instruction result = null;
		line = line.trim();
		String[] splitLine = line.split(" +");

		if (splitLine.length==1)
			result = createInstruction();

		return result;
	}

	public void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		int operand1 = 0, operand2 = 0;
		//Saca el primer operando de la pila
		try{
			operand2 = stack.top();
			stack.pop();
		}catch(EmptyStackException e){
 			throw new InstructionException("Faltan operandos (Hay 0. Requeridos 2)",e);
		}
		//Saca el segundo operando de la pila
		try{
			operand1 = stack.top();
			stack.pop();
		}catch(EmptyStackException e){
 			try {stack.push(operand2);} catch (FullStackException e1) {}
			throw new InstructionException("Faltan operandos (Hay 1. Requeridos 2)",e);
		}
		//Si habia los dos elem en la pila, se opera
		try{
			operate(stack, memory, control, operand1, operand2);
		}catch(InstructionException e){//Excepcion del operate
 			try {stack.push(operand1);
				stack.push(operand2);
			} catch (FullStackException e1) {}
			throw e;
		}
	}

	public String toString(){
		return type;
	}

}
