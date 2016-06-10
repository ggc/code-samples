package jlex;


public class UnidadLexicaMultivaluada extends UnidadLexica{

	private String lexema;
	
	public UnidadLexicaMultivaluada(int fila, ClaseLexica clase, String lexema) {
		super(fila, clase);
		this.lexema=lexema;
	}

	public String lexema() throws Exception {
		return lexema;
	}
	public String toString(){
		try {
			return "[clase:"+clase()+",fila:"+fila()+",col:"+columna()+",lexema:"+lexema()+"]";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lexema;
	}

}
