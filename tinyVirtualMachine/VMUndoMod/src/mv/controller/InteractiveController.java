package mv.controller;

import java.util.Scanner;

import mv.controller.cmdprompt.Prompt;
import mv.model.CPU;
import mv.model.exceptions.InvalidInstructionException;
import mv.model.exceptions.commands.CommandException;
import mv.model.exceptions.instructions.InstructionException;

public class InteractiveController extends Controller {
	private Scanner _input;
	private Prompt _prompt;

	public InteractiveController(CPU model, Scanner sc) {
		super(model);
		_input = sc;
		_prompt = new Prompt();
	}

	@Override
	public void start() {
		_cpu.start();
		runPrompt();
	}

	public void runPrompt(){
		try{
			try {
				_prompt.runPrompt(_input, _cpu);
			} catch (InvalidInstructionException e) {
				runPrompt();
			} catch (InstructionException e) {
				System.err.println("Nope: InstrutionException");
			} catch (CommandException e) {
				System.err.println("Error al leer el comando");
			}
			System.exit(0);
			//} catch (IOException ioe){
			//	System.err.println("Uso incorrecto: Error al acceder al fichero de entrada: " + ioe.getMessage()
			//			+ "\nUse -h|--help para más detalles.");
			//	System.exit(1);
			//} catch (ParseException ue){
			//	System.err.println("Uso incorrecto: " + ue.getMessage()
			//			+ "\nUse -h| --help para mas detalles.");
			//	System.exit(1);
		}finally {
			if(_input!= null)
				_input.close();
		}
	}

}
