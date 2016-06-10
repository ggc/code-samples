package mv.model.instructions.misc;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.instructions.NoParamNoOperandInstruction;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Halt extends NoParamNoOperandInstruction {


	public Halt(){
		super("HALT");
	}
	
	protected Instruction createInstruction(){
		return new Halt();
	}
	
	protected void operate(OperandStack stack,Memory memory,ControlUnit control){
		control.halt();
 	}

}
