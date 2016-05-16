package mv.model.instructions.logic;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.NoParamOneOperandInstruction;


/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Not extends NoParamOneOperandInstruction {

	public Not(){
		super("NOT");
	}
	
	protected Instruction createInstruction(){
		return new Not(); 
	}
	
	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException{
		try{
		if (operand == 0)
			stack.push(1);
		else
			stack.push(0);
		
 		}catch(FullStackException e){
  			throw new InstructionException(this, e);
 		}
	}



}
