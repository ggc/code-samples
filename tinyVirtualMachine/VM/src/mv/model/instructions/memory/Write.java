package mv.model.instructions.memory;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.memory.AddressNotExistException;
import mv.model.exceptions.memory.FullMemoryException;
import mv.model.instructions.TwoParamNoOperandInstruction;


/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Write extends TwoParamNoOperandInstruction {

	public Write(int param1, int param2){
		super("Write", param1, param2);
	}

	public Write(){
		this(0,0);
	}

	protected Instruction createInstruction(int param1, int param2){
		return new Write(param1, param2);
	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
		try{
			memory.store(param1, param2);
		}catch(AddressNotExistException  e1){
			throw new InstructionException(this, e1);
		}catch(FullMemoryException  e2){
 			throw new InstructionException(this, e2);
		}
		//Descomentar para mostrar por pantalla si se realizo internamente el almacenado.
		//			if (stored)
		//				System.out.println("Stored == true");

	}


}
