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

public abstract class NoParamOneOperandInstruction implements Instruction {

	protected String type;

	protected NoParamOneOperandInstruction(String type){
		this.type=type;
	}

	protected abstract Instruction createInstruction();

	protected abstract void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException;

	public Instruction parse(String line){

		Instruction result = null;
		line = line.trim();
		String[] splitLine = line.split(" +");

		if (splitLine.length == 1)
			result = createInstruction();

		return result;
	}

	public void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		int operand = 0;
		try{
			operand = stack.top();
			stack.pop();
			operate(stack, memory, control, operand);
		}catch(EmptyStackException e){
			try {stack.push(operand);/*Siempre sera posible*/} catch (FullStackException e1) {}
			throw new InstructionException(e); 
		}
	}

	public String toString(){
		return type;
	}

}
