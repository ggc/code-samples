package mv.model.instructions.stack;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.instructions.NoParamOneOperandInstruction;


/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Pop extends NoParamOneOperandInstruction {

	public Pop(){
		super("POP");
	}

	protected Instruction createInstruction(){
		return new Pop();
	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand){
	}


}
