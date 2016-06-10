package mv.controller;

import java.util.Scanner;

import mv.controller.cmdprompt.Prompt;
import mv.model.CPU;
import mv.model.exceptions.InvalidInstructionException;
import mv.model.exceptions.commands.CommandException;
import mv.model.exceptions.instructions.InstructionException;

public class BatchController extends Controller{
	
	public BatchController(CPU model) {
		super(model);
	}

	@Override
	public void start() {
		_cpu.start();
		Prompt prompt = new Prompt();
		try {
			while(!_cpu.isHalted())
				prompt.runPrompt(new Scanner("step"), _cpu);
		} catch (InvalidInstructionException e) {
			e.printStackTrace();
		} catch (InstructionException e) {
			e.printStackTrace();
		} catch (CommandException e) {
			e.printStackTrace();
		}
	}

}
