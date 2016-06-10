package mv.model.exceptions.commands;

import mv.controller.cmdprompt.PromptCommand;
import mv.model.exceptions.MVException;

@SuppressWarnings("serial")
public class CommandException extends MVException{

	public CommandException(){
		super();
	}
	
	public CommandException(String string){
		super(string);
	}
	public CommandException(String string, Throwable cause) {
		super(string,cause);
	}
	
	public CommandException(PromptCommand cmd, Throwable cause){
		super("Error al ejecutar el comando " + cmd.toString(),cause);
	}

}
