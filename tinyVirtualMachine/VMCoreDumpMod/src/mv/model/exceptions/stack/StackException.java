package mv.model.exceptions.stack;

import mv.model.exceptions.MVException;

@SuppressWarnings("serial")
public class StackException extends MVException{

	public StackException(String string) {
		super(string);
	}
	
	public StackException(String string,Throwable cause){
		super(string,cause);
	}

}
