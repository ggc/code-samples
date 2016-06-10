package mv.model.instructions.comparison;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.NoParamTwoOperandInstruction;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public class EQ extends NoParamTwoOperandInstruction{

	public EQ(){
		super("EQ");
	}
	
	protected Instruction createInstruction(){
		return new EQ();
		
	}
	
	
	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand1,int operand2) throws InstructionException{
		try{
		if (operand1 == operand2)
			stack.push(1);
		else
			stack.push(0);
		}catch(FullStackException e){
 			throw new InstructionException(this, e);
		}
	}
	
	
}
