package mv.model.exceptions.instructions;

import mv.controller.cmdprompt.PromptCommand;
import mv.model.Instruction;
import mv.model.exceptions.MVException;

@SuppressWarnings("serial")
public class InstructionException extends MVException{

	public InstructionException(String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}
	
	public InstructionException(String string, Throwable cause){
		super(string,cause);
	}
	
	public InstructionException(Instruction inst, Throwable cause){
		super("Error ejecutando " + inst.toString(),cause);
	}
	
	public InstructionException(PromptCommand cmd, Throwable cause){
		super("Error ejecutando comando "+cmd.toString(),cause);
	}

	public InstructionException(Throwable cause) {
		super(cause);
	}

}
