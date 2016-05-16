package mv.model;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class DataMemoryRegister {

	private int posicion;
	private int valor;
	
	public DataMemoryRegister(int pos,int value){
		
		this.posicion = pos;
		this.valor = value;
		
	}

	/**
	 * Establece un nuevo valor
	 * @param newValue
	 */
	public void setValue(int newValue){
		
		this.valor = newValue;
	}
	
	/**
	 * Devuelve el valor.
	 * @return value
	 */
	public int getValue(){
		
		return this.valor;
	}
	
	/**
	 * Devuelve la posicion
	 * @return posicion
	 */
	public int getPos(){
		
		return this.posicion;
	}
	
	public String toString(){
		return "[" + posicion +"]:" +valor + " ";
	}
	
}
