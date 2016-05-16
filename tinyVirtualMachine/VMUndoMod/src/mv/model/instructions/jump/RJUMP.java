package mv.model.instructions.jump;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.cp.CPNotExistException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.OneParamNoOperandInstruction;


public class RJUMP extends OneParamNoOperandInstruction {

	public RJUMP(int param){
		super("RJUMP", param);
	}
	
	public RJUMP(){
		this(0);
	}
	
	protected Instruction createInstruction(int param){
		return new RJUMP(param);
	}
	
	protected void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		try {
			control.increaseCP(param);
		} catch (CPNotExistException e) {
			throw new InstructionException(this, e);
		}		
	}
	
	
}
