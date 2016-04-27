package jlex;


public class UnidadLexicaUnivaluada extends UnidadLexica{
	
	public UnidadLexicaUnivaluada(int fila, ClaseLexica clase) {
		super(fila, clase);
	}
	
	public String lexema() throws Exception {throw new Exception();}
	public String toString(){
		return "[clase:"+clase()+",fila:"+fila()+",col:"+columna()+"]";
	}
}
