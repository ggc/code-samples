package mv.model.exceptions.stack;

@SuppressWarnings("serial")
public class FullStackException extends StackException{

	public FullStackException(String string) {
		super("La pila esta llena");
	}

}
