package mv.model.instructions.stack;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.NoParamOneOperandInstruction;


public class Dup extends NoParamOneOperandInstruction {

	public Dup(){
		super("DUP");
	}

	protected Instruction createInstruction(){
		return new Dup();
	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException{
		try {
			stack.push(operand);
			stack.push(operand);
		} catch (FullStackException e) { 
 			throw new InstructionException(this, e);
		} 

	}
}


