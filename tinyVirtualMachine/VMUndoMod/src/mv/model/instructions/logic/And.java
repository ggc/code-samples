package mv.model.instructions.logic;

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
public class And extends NoParamTwoOperandInstruction{

	public And(){
		super("AND");
	}

	protected Instruction createInstruction(){
		return new And();
	}


	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand1,int operand2) throws InstructionException{
		try{
			int resultado = operand2 * operand1;

			if (resultado == 0)
				stack.push(0); 
			else 
				stack.push(1); 

		}catch(FullStackException e){
			throw new InstructionException(this, e);
		}
	}

}
