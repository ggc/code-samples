package mv.model.instructions.memory;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.memory.AddressNotExistException;
import mv.model.exceptions.memory.FullMemoryException;
import mv.model.instructions.OneParamOneOperandInstruction;


/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Store extends OneParamOneOperandInstruction {

	public Store(int param){
		super("STORE", param);
	}

	public Store(){
		this(0);
	}

	protected Instruction createInstruction(int param){
		return new Store(param);
	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException{
		try{
			memory.store(param, operand);
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
