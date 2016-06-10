package mv.model.exceptions.memory;

import mv.model.exceptions.MVException;

@SuppressWarnings("serial")
public class MemoryException extends MVException{

	
	public MemoryException(){
		super();
	}
	public MemoryException(String string) {
		super(string);
	}
	public MemoryException(String string, Throwable cause){
		super(string,cause);
	}

}
