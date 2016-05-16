package mv.model.instructions;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;

public abstract class TwoParamNoOperandInstruction implements Instruction{

	protected int param1;
	protected int param2;
	protected java.lang.String type;

	protected TwoParamNoOperandInstruction(String type,int param1, int param2){
		this.type = type;
		this.param1 = param1;
		this.param2 = param2;
	}
	protected abstract void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException;

	protected abstract Instruction createInstruction(int param1, int param2);

	public Instruction parse(String line){

		Instruction result = null;
		line.trim();
		String[] splitLine = line.split(" +");

		if (splitLine.length == 3)

			result = createInstruction(Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));

		return result;
	}

	public void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
			operate(stack, memory, control);
	}

	public String toString(){
		return type + " " + param1 + " " + param2;
	}

}
