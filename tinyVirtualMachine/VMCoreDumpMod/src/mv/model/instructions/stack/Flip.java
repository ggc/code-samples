package mv.model.instructions.stack;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.NoParamTwoOperandInstruction;


public class Flip extends NoParamTwoOperandInstruction {

	public Flip(){
		super("FLIP");
	}

	protected Instruction createInstruction(){
		return new Flip();
	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand1,int operand2) throws InstructionException{
		try{
			stack.push(operand2);
			stack.push(operand1);
		}catch(FullStackException  e2){
 			throw new InstructionException(this, e2);
		}

	}


}


