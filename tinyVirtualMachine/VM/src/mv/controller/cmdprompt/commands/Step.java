package mv.controller.cmdprompt.commands;

import mv.controller.cmdprompt.PromptCommand;
import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public class Step extends PromptCommand {

	public Step(){
		super("STEP");
	}

	public void executeCommand() throws InstructionException{
		doStep(); 
	}

	/**
	 * Ejecuta la siguiente instruccion.
	 * @return True si se realizo correctamente.
	 * @throws MVException 
	 */
	protected void doStep() throws InstructionException{
		String currInstruction;
		if(!cpu.isHalted() && !(cpu.getNextInstructionName()==null)){
			currInstruction = cpu.getNextInstructionName();
			cpu.step(); 
			//if(Flags.mode)System.out.println(MSG_EXEC_END);
			//System.out.println("Antes de mostrar estado de cpu");
			//if(Flags.mode)System.out.println(cpu.toString());
			//System.out.println("Despues de mostrar estado de cpu");
			//			else
			//				System.out.println(MSG_EXEC_ERROR);
			if (currInstruction.equals("HALT"))
				return;
		}
	}

	public PromptCommand parse(String line){

		PromptCommand ins = null;

		if (line.equalsIgnoreCase("STEP"))
			ins = new Step();

		return ins;
	}

}
