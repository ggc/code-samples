package mv.model.exceptions;

@SuppressWarnings("serial")
public class MVException extends Exception{

	
	public MVException(){
		super();
	}
	public MVException(String string) {
		super(string);
	}
	public MVException(String string, Throwable cause) {
		super(string,cause);
	}
	public MVException(Throwable cause) {
		super(cause);
	}
 
}
