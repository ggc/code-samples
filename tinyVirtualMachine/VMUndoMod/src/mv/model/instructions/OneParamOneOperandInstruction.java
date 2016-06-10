package mv.model.instructions;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.EmptyStackException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
 
public abstract class OneParamOneOperandInstruction implements Instruction {

	protected int param;
	protected java.lang.String type;

	protected OneParamOneOperandInstruction(String type,int param){
		this.type = type;
		this.param = param;
	}
	protected abstract void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException;

	protected abstract Instruction createInstruction(int param);

	public Instruction parse(String line){

		Instruction result = null;
		line.trim();
		String[] splitLine = line.split(" +");

		if (splitLine.length == 2)

			result = createInstruction(Integer.parseInt(splitLine[1]));

		return result;
	}

	public void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		int operand = 0;
		try{
			operand = stack.top();
			stack.pop();
			operate(stack, memory, control, operand);
		}catch(EmptyStackException e1){
 			throw new InstructionException(e1); 
		}
	}

	public String toString(){
		return type + " " + param;
	}

}
