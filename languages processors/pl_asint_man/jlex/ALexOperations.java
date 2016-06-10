package jlex;


public class ALexOperations {
	public AnalizadorLexico alex;
	public ALexOperations(AnalizadorLexico alex) {
		this.alex = alex;   
	}

	// public UnidadLexica unidadEvalua() {
	// return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.EVALUA); 
	//} 

	public UnidadLexica unidadNum() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.NUM); 
	}
	public UnidadLexica unidadBool() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.BOOL); 
	}
	public UnidadLexica unidadTrue() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.TRUE); 
	}
	public UnidadLexica unidadFalse() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.FALSE); 
	}
	public UnidadLexica unidadAnd() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.AND); 
	}
	public UnidadLexica unidadOr() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.OR); 
	}
	public UnidadLexica unidadNot() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.NOT); 
	}
	public UnidadLexica unidadID() {
		return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.ID, alex.lexema()); 
	}
	public UnidadLexica unidadInt() {
		return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.NUMBER_INTEGER,alex.lexema());     
	}    
	public UnidadLexica unidadReal() {
		return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.NUMBER_REAL,alex.lexema());     
	}    
	public UnidadLexica unidadFloat() {
		return new UnidadLexicaMultivaluada(alex.fila(),ClaseLexica.FLOAT, alex.lexema());    
	}
	public UnidadLexica unidadMas() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.ADD);     
	}    
	public UnidadLexica unidadMenos() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.SUB);     
	}    
	public UnidadLexica unidadPor() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.MUL);     
	}    
	public UnidadLexica unidadDiv() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.DIV);     
	}    
	public UnidadLexica unidadPAp() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.POPEN);     
	}    
	public UnidadLexica unidadPCierre() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.PCLOSE);     
	}    
	public UnidadLexica unidadIgual() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.ASSIGN);     
	}     
	public UnidadLexica unidadEOF() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.EOF);     
	}    
	public UnidadLexica unidadGT() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.GT);    
	}
	public UnidadLexica unidadGE() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.GE);    
	}
	public UnidadLexica unidadLT() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.LT);    
	}
	public UnidadLexica unidadLE() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.LE);    
	}
	public UnidadLexica unidadNE() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.NE);    
	}
	public UnidadLexica unidadEQ() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.EQ);    
	}
	public UnidadLexica unidadPunto() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.DOT);    
	}
	public UnidadLexica unidadPuntoComa() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.SEMICOLON);    
	}
	public UnidadLexica unidadAmpSep() {
		return new UnidadLexicaUnivaluada(alex.fila(),ClaseLexica.AMP);    
	}
	public void error() {
		System.err.println("***"+alex.fila()+" Caracter inexperado: "+alex.lexema());
	}
}
