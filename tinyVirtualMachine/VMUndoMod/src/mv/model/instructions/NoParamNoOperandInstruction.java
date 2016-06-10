package mv.model.instructions;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public abstract class NoParamNoOperandInstruction implements Instruction {

	protected String type;

	protected NoParamNoOperandInstruction(String type){
		this.type=type;
	}

	protected abstract Instruction createInstruction();

	protected abstract void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException;

	public Instruction parse(String line){

		Instruction result = null;
		line = line.trim();
		String [] splitLine = line.split(" +");

		if (splitLine.length == 1)
			result = createInstruction();

		return result;
	}

	public void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		operate(stack, memory, control);
	}

	public String toString(){
		return type;
	}
}
