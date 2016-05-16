package mv.model.instructions.arithmetic;

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

public class Neg extends NoParamOneOperandInstruction {


	public Neg(){

		super("NEG"); 

	}

	protected Instruction createInstruction(){
		return new Neg();

	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException{
		try{				
			stack.push(operand * (-1));
		}catch (FullStackException e){
 			throw new InstructionException(this, e);
		} 
	}
}
