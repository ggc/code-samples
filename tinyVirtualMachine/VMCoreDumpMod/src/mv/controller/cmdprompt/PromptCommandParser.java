package mv.controller.cmdprompt;

import mv.controller.cmdprompt.commands.Run;
import mv.controller.cmdprompt.commands.Step;
import mv.controller.cmdprompt.commands.Steps;
import mv.model.Instruction;
import mv.model.exceptions.commands.CommandParseException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.memory.Write;
import mv.model.instructions.stack.Pop;
import mv.model.instructions.stack.Push;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public class PromptCommandParser {

	static PromptCommand[] arrayIns = {new Step(), new Run(), new Steps()};
	static Instruction[] arrayIns2 = {new Push(), new Pop(), new Write()};

	public PromptCommandParser(){


	}

	/**
	 * analiza la linea line y la transforma en el comando correspondiente.
	 * @param line
	 * @return El comando correspondiente
	 * @throws InstructionException 
	 */
	public static PromptCommand parseCommand(String line) throws CommandParseException{
		PromptCommand command = null;
		
		line = line.trim(); // Quitar espacios delante y detrás
		line = line.toLowerCase(); // a minúsculas;

		for(PromptCommand i:arrayIns){
			command = i.parse(line);
			if (command!=null) 
				return command;
		}
	
		throw new CommandParseException("Comando no reconocido: "+line);
	}

	public static Instruction parseInstruction(String line) throws InstructionException{
		Instruction ins = null;
		
		line = line.trim(); // Quitar espacios delante y detrás
		line = line.toLowerCase(); // a minúsculas;

		for(Instruction i:arrayIns2){
			ins = i.parse(line);
			if (ins!=null) 
				return ins;
		}
		throw new InstructionException("Comando no reconocido: "+line);
	}
}
