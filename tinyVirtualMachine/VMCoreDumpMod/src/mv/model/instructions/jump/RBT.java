package mv.model.instructions.jump;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.cp.CPNotExistException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.OneParamOneOperandInstruction;


public class RBT extends OneParamOneOperandInstruction {

	public RBT(int param){
		super("RBT", param);
	}

	public RBT(){
		this(0);

	}

	protected Instruction createInstruction(int param){
		return new RBT(param);

	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException{
		if (operand != 0){ 
			try {
				control.increaseCP(param);
			} catch (CPNotExistException e) {
				throw new InstructionException(this, e);
			}
		} 
	}	

}
