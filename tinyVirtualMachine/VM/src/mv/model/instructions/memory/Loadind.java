package mv.model.instructions.memory;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.memory.AddressNotExistException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.NoParamOneOperandInstruction;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Loadind extends NoParamOneOperandInstruction{


	public Loadind(){
		super("LOADIND");
	}

	protected Instruction createInstruction(){
		return new Loadind();
	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control, int operand) throws InstructionException{
		try{
			stack.push(memory.load(operand)); 
		}catch(AddressNotExistException  e1){
 			throw new InstructionException(this, e1);
		}catch(FullStackException  e2){
 			throw new InstructionException(this, e2);
		}
	}

}
