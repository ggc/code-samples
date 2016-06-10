package mv.model.exceptions.stack;

@SuppressWarnings("serial")
public class MissingOperandException extends StackException{

	public MissingOperandException(String string) {
		super(string);
	}

}
