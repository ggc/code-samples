package mv.model.instructions.jump;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.cp.CPNotExistException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.OneParamNoOperandInstruction;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Jump extends OneParamNoOperandInstruction{

	public Jump(int param){
		super("JUMP", param);
	}
	
	public Jump(){
		this(0);
	}
	
	protected Instruction createInstruction(int param){
		return new Jump(param);
	}
	
	protected void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		try{
		  control.setCP(param);
		}catch( CPNotExistException e){ 
 			throw new InstructionException(this, e);
		}
	}

}
