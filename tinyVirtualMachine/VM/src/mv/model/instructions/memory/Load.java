package mv.model.instructions.memory;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.memory.AddressNotExistException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.OneParamNoOperandInstruction;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Load extends OneParamNoOperandInstruction{


	public Load(int param){
		super("LOAD", param);
	}

	public Load(){
		super("LOAD", 0);
	}

	protected Instruction createInstruction(int param){
		return new Load(param);
	}


	protected void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		try{ 
			stack.push(memory.load(param)); 
		}catch(AddressNotExistException e){
			throw new InstructionException(this, e);
		}catch(FullStackException  e){
 			throw new InstructionException(this, e);
		}
	}
}
