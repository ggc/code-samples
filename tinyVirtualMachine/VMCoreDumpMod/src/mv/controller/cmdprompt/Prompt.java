package mv.controller.cmdprompt;

import java.util.Scanner;

import mv.controller.cmdprompt.PromptCommandParser;
import mv.model.CPU;
import mv.model.Instruction;
import mv.model.InstructionParser;
import mv.model.exceptions.InvalidInstructionException;
import mv.model.exceptions.commands.CommandException;
import mv.model.exceptions.instructions.InstructionException;

public class Prompt {

	static final java.lang.String MSG_PROMPT = "> ";
	static final java.lang.String MSG_PARSE_ERROR = "Error: Comando no reconocido";
	static final java.lang.String END_TOKEN = "QUIT";

	public Prompt(){
	}

	/**
	 * Ejecuta el promt con la fuente input y sobre la cpu cpu para leer comandos
	 * @param input
	 * @param cpu
	 * @throws InvalidInstructionException 
	 * @throws InstructionException 
	 * @throws CommandException 
	 */
	public void runPrompt(Scanner input,CPU cpu) throws InvalidInstructionException, InstructionException, CommandException{

		String line = null;

		PromptCommand.configureCommandInterpreter(cpu);

		//if(interactive_mode)System.out.print(MSG_PROMPT);
		while(input.hasNext()){

			Instruction ins = null;
			line = input.nextLine();
			line = line.trim();

			if (line.equals("")){
				continue;
			}
			if(line.equalsIgnoreCase(END_TOKEN)){
				break;
			}
			PromptCommand cmd = PromptCommandParser.parseCommand(line);
			if (cmd == null){
				ins = InstructionParser.parse(line);
				if(ins == null)
					System.out.println(MSG_PARSE_ERROR);
				else
					ins.execute(cpu.getStack(), cpu.getMemory(), cpu.getCU());

			}
			else{
				cmd.executeCommand();//Si executeCommand = false es por que ha llegado al final.
			}
			//if(interactive_mode)System.out.print(MSG_PROMPT);

		};

	}

	//	System.out.println(MSG_PARSE_ERROR);

}
