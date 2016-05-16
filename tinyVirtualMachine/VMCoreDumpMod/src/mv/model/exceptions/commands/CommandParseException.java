package mv.model.exceptions.commands;

@SuppressWarnings("serial")
public class CommandParseException extends CommandException{

	public CommandParseException(String string) {
		super(string); 
	}
 
}
