package mv.model.instructions.stack;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.OneParamNoOperandInstruction;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Push extends OneParamNoOperandInstruction {

	public Push(int param){
		super("PUSH", param);
	}
	
	public Push(){
		this(0);
	}
	
	protected Instruction createInstruction(int param){
		return new Push(param);	
	}
	
	protected void operate(OperandStack stack, Memory memory, ControlUnit control) throws InstructionException{
		//Instruccion que anade el parametro a la pila de operandos (operandStack).
		//No usa ni memoria ni unidad de control.
		try{
			stack.push(param);
		}catch(FullStackException e){
 			throw new InstructionException(this, e);
		}
	}
	
}
