package mv.model.instructions.io;

import mv.input.Reader;
import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.exceptions.stack.FullStackException;
import mv.model.instructions.NoParamNoOperandInstruction;

public class In extends NoParamNoOperandInstruction{

	//Atributos
	private static Reader _input;

	//Constructores
	public In(){
		super("IN");
		//input.useDelimiter("");
	}

	//Metodos
	@Override
	protected Instruction createInstruction() {
		return new In();
	}

	@Override
	protected void operate(OperandStack stack, Memory memory,ControlUnit control) throws InstructionException{
 		try {
			stack.push(_input.read());
		} catch (FullStackException e) {
			throw new InstructionException(this, e); 
		} 
	}

	public static void setInput(Reader input){
		In._input = input;

	}  
}
