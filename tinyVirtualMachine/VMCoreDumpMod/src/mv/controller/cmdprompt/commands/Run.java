package mv.controller.cmdprompt.commands;

import mv.controller.cmdprompt.PromptCommand;
import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public class Run extends Step {


	public Run(){
		this.type = "RUN";
	}

	public void executeCommand() throws InstructionException{
		 
			while(!cpu.isHalted() && cpu.getNextInstructionName()!=null){
				doStep();
			}
		 
	}	

	public PromptCommand parse(String cadena){

		PromptCommand ins = null;

		if(cadena.equalsIgnoreCase("RUN"))

			ins = new Run(); 
		return ins;
	}

}
