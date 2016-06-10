package mv.model.exceptions;
 
@SuppressWarnings("serial")
public class InvalidInstructionException extends MVException{
	
	public InvalidInstructionException(String string){
		super(string);
	}

	public InvalidInstructionException(Throwable cause) {
		super(cause);
	}

}
