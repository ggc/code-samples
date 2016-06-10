package mv.model.exceptions.instructions;

@SuppressWarnings("serial")
public class DivisionByZeroException extends InstructionException{

	public DivisionByZeroException(String string) {
		super(string);
	}

}
