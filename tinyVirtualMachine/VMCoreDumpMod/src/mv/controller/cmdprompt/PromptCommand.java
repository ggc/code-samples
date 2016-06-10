package mv.controller.cmdprompt;

import mv.model.CPU;
import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public abstract class PromptCommand {

	protected static CPU cpu;
	protected String type;
	
	public PromptCommand(String type){
		this.type=type;
	}
	
	protected static void configureCommandInterpreter(CPU cpu){
		PromptCommand.cpu = cpu;
	}
	
	public abstract void executeCommand() throws InstructionException;
	
	public abstract PromptCommand parse(String cadena);
	
	public String toString(){
		return type;
	}
	
}
