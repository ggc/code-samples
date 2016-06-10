package mv.input;

 
public class NullReader implements Reader{
 
	public NullReader(){ 
	}

	public int read(){
		return -1;
	}
}
