package mv.controller.cmdprompt.commands;

import mv.controller.cmdprompt.PromptCommand;
import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public class Steps extends Step {

	private int nsteps;
	
	public Steps(int nsteps){
		this.type = "STEPS";		
		this.nsteps = nsteps;
	}
	
	public Steps(){
		this.type = "STEPS";
	}

	public void executeCommand() throws InstructionException{
 		for(int i=0; i<this.nsteps; i++)
 				doStep();  
	}
	
	public PromptCommand parse(String cadena){
		
		PromptCommand ins = null;
		
		String[] split = cadena.split(" +");
					
		if (split[0].equalsIgnoreCase("STEP") && split.length==2)
			ins = new Steps(Integer.parseInt(split[1]));
			
		return ins;
	}
	
	public String toString(){
		return type + " " +nsteps;
	}

}
