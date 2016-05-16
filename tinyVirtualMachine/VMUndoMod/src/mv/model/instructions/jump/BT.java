package mv.model.instructions.jump;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.cp.CPNotExistException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.OneParamOneOperandInstruction;


/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class BT extends OneParamOneOperandInstruction {

	public BT(int param){
		super("BT", param);
	}

	public BT(){
		this(0);

	}

	protected Instruction createInstruction(int param){
		return new BT(param);

	}

	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand) throws InstructionException{
		if (operand != 0){
			try {
				control.setCP(param);
			} catch (CPNotExistException e) {
 				throw new InstructionException(this, e);
			}
		}
	}
}
