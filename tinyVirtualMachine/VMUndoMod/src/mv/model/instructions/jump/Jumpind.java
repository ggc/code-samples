package mv.model.instructions.jump;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.cp.CPNotExistException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.NoParamOneOperandInstruction;


public class Jumpind extends NoParamOneOperandInstruction{

	public Jumpind() {
		super("JUMPIND");
	}

	protected Instruction createInstruction() {
		return new Jumpind();
	}

	protected void operate(OperandStack stack, Memory memory,ControlUnit control, int operand) throws InstructionException{
		try{
			control.setCP(operand);
		}catch(CPNotExistException e){
 			throw new InstructionException(this, e);
		}
	}

}
