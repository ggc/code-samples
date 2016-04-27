/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package asint;

import jlex.UnidadLexica;
import jlex.AnalizadorLexico;
import jlex.ClaseLexica;
import errors.GestionErrores;
import java.io.IOException;
import java.io.Reader;

public class AnalizadorSintactico {
	private UnidadLexica anticipo;
	private AnalizadorLexico alex;
	private GestionErrores errores;
	public AnalizadorSintactico(Reader input) {
		errores = new GestionErrores();
		alex = new AnalizadorLexico(input);
		alex.fijaGestionErrores(errores);
		sigToken();
	}

	//////////////////
	// Producciones //
	//////////////////
	public void Sp(){
		S();
		empareja(ClaseLexica.EOF);
	}

	private void S(){
		switch(anticipo.clase()){
		case BOOL: case NUM:    
			D();
			empareja(ClaseLexica.AMP);
			I();
			break;
		case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NUM, 
				ClaseLexica.BOOL);                                       
		}
	}
	private void D(){
		switch(anticipo.clase()){
		case NUM: case BOOL:    
			T();
			empareja(ClaseLexica.ID);
			Df();
			break;
		case AMP: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NUM, 
				ClaseLexica.BOOL);
		}
	}
	private void T(){
		switch(anticipo.clase()){
		case NUM: empareja(ClaseLexica.NUM); break;
		case BOOL: empareja(ClaseLexica.BOOL); break;
		case ID: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NUM, 
				ClaseLexica.BOOL);      
		}
	}

	private void Df(){
		switch(anticipo.clase()){
		case SEMICOLON:    
			empareja(ClaseLexica.SEMICOLON);
			D();
			break;
		case AMP: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.SEMICOLON);
		}
	}
	private void I(){
		switch(anticipo.clase()){
		case ID:    
			empareja(ClaseLexica.ID);
			empareja(ClaseLexica.ASSIGN);
			E0();
			If();
			break;
		case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.ID);
		}
	}
	private void If(){
		switch(anticipo.clase()){
		case SEMICOLON:
			empareja(ClaseLexica.SEMICOLON);
			I();
			break;
		case EOF: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.SEMICOLON);
		}
	}
	private void E0(){
		switch(anticipo.clase()){
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER:
			E1();
			E0a();
			break;
		case PCLOSE: case SEMICOLON: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NEG, 
				ClaseLexica.NOT, ClaseLexica.FLOAT, ClaseLexica.NUMBER_REAL, ClaseLexica.POS, 
				ClaseLexica.POPEN, ClaseLexica.TRUE, ClaseLexica.FALSE, ClaseLexica.ID, 
				ClaseLexica.NUMBER_INTEGER);
		}
	}
	private void E0a(){
		switch(anticipo.clase()){
		case NE: case LT: case LE: case EQ: case GT: case GE:
			T0();
			break;
		case OR:
			OP0b();
			E0();
			break;
		case PCLOSE: case SEMICOLON: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.OR, 
				ClaseLexica.EQ, ClaseLexica.NE, ClaseLexica.LE, ClaseLexica.LT, ClaseLexica.GE, ClaseLexica.GT);
		}
	}
	private void T0(){
		switch(anticipo.clase()){
		case NE: case LT: case LE: case EQ: case GT: case GE:
			OP0a();
			E1();
			break;
		case PCLOSE: case SEMICOLON: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.EQ, 
				ClaseLexica.NE, ClaseLexica.LE, ClaseLexica.LT, ClaseLexica.GE, ClaseLexica.GT);
		}
	}
	private void E1(){
		switch(anticipo.clase()){
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER:
			E2();
			E1r();
			break;
		case SEMICOLON: case OR: case EQ: case NE: case LE: case LT: case GE: case GT: 
		case PCLOSE: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NEG, 
				ClaseLexica.NOT, ClaseLexica.FLOAT, ClaseLexica.NUMBER_REAL, ClaseLexica.POS, 
				ClaseLexica.POPEN, ClaseLexica.TRUE, ClaseLexica.FALSE, ClaseLexica.ID, 
				ClaseLexica.NUMBER_INTEGER);
		}
	}
	private void E1r(){
		switch(anticipo.clase()){
		case ADD: case SUB:
			OP1();
			E2();
			E1r();
			break;
		case SEMICOLON: case OR: case EQ: case NE: case LE: case LT: case GE: case GT: 
		case PCLOSE: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.ADD, 
				ClaseLexica.SUB);
		}
	}
	private void E2(){
		switch(anticipo.clase()){
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER:
			E3();
			T2();
			break;
		case SEMICOLON: case OR: case EQ: case NE: case LE: case LT: case GE: case GT: 
		case ADD: case SUB: case PCLOSE: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.NEG, 
				ClaseLexica.NOT, ClaseLexica.FLOAT, ClaseLexica.NUMBER_REAL, ClaseLexica.POS, 
				ClaseLexica.POPEN, ClaseLexica.TRUE, ClaseLexica.FALSE, ClaseLexica.ID, 
				ClaseLexica.NUMBER_INTEGER);
		}
	}

	private void T2(){
		switch(anticipo.clase()){
		case MUL: case DIV:
			E2r();
			break;
		case AND:
			OP2b();
			E3();
			break;
		case SEMICOLON: case OR: case EQ: case NE: case LE: case LT: case GE: case GT: 
		case ADD: case SUB: case PCLOSE: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.MUL, 
				ClaseLexica.DIV, ClaseLexica.AND);
		}
	}

	private void E2r(){
		switch(anticipo.clase()){
		case MUL: case DIV:    
			OP2a();
			E3();
			E2r();
			break;
		case SEMICOLON: case OR: case EQ: case NE: case LE: case LT: case GE: case GT: 
			case ADD: case SUB: case PCLOSE: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.MUL, 
				ClaseLexica.DIV);
		}
	}

	private void E3(){
		switch(anticipo.clase()){
		case NEG:  case POS: 
			E4();
			break;	    
		case NOT: 
			E3();
			break;
		case FLOAT: case NUMBER_REAL: case TRUE: case FALSE: case ID: case NUMBER_INTEGER: 
		case POPEN: 
			E4();
			break;
		case SEMICOLON: case AND: case MUL: case DIV: case OR: case EQ: case NE: case LE: 
			case LT: case GE: case GT: case ADD: case SUB: case PCLOSE: case EOF: break;
		default:  errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.FLOAT, 
				ClaseLexica.NUMBER_REAL, ClaseLexica.TRUE, ClaseLexica.FALSE, ClaseLexica.ID, 
				ClaseLexica.NUMBER_INTEGER, ClaseLexica.POPEN);
		}
	}
	private void E4(){
		switch(anticipo.clase()){
		case FLOAT: 
			empareja(ClaseLexica.FLOAT); break;
		case NUMBER_REAL: 
			empareja(ClaseLexica.NUMBER_REAL); break;
		case TRUE: 
			empareja(ClaseLexica.TRUE); break;
		case FALSE: 
			empareja(ClaseLexica.FALSE); break;
		case ID: 
			empareja(ClaseLexica.ID); break;
		case NUMBER_INTEGER:
			empareja(ClaseLexica.NUMBER_INTEGER); break;
		case POPEN:
			empareja(ClaseLexica.POPEN);
			E0();
			empareja(ClaseLexica.PCLOSE); 
			break;
		case SEMICOLON: case AND: case MUL: case DIV: case OR: case EQ: case NE: case LE: 
			case LT: case GE: case GT: case ADD: case SUB: case PCLOSE: case EOF: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.FLOAT, 
				ClaseLexica.NUMBER_REAL, ClaseLexica.TRUE, ClaseLexica.FALSE, ClaseLexica.ID, 
				ClaseLexica.NUMBER_INTEGER, ClaseLexica.POPEN);
		}
	}

	private void OP0a(){
		switch(anticipo.clase()){
		case NE: 
			empareja(ClaseLexica.NE); break;
		case LT:
			empareja(ClaseLexica.LT); break;
		case LE:
			empareja(ClaseLexica.LE); break;
		case EQ: 
			empareja(ClaseLexica.EQ); break;
		case GT: 
			empareja(ClaseLexica.GT); break;
		case GE:
			empareja(ClaseLexica.GE); break;
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.EQ, 
				ClaseLexica.NE, ClaseLexica.LE, ClaseLexica.LT, ClaseLexica.GE, ClaseLexica.GT);
		}
	}

	private void OP0b(){
		switch(anticipo.clase()){
		case OR: 
			empareja(ClaseLexica.OR);
			break;
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.OR, 
				ClaseLexica.NEG, ClaseLexica.NOT, ClaseLexica.FLOAT, ClaseLexica.NUMBER_REAL, 
				ClaseLexica.POS, ClaseLexica.POPEN, ClaseLexica.TRUE, ClaseLexica.FALSE, 
				ClaseLexica.ID, ClaseLexica.NUMBER_INTEGER);
		}
	}

	private void OP1(){
		switch(anticipo.clase()){
		case ADD: 
			empareja(ClaseLexica.ADD);
			break;
		case SUB:
			empareja(ClaseLexica.SUB);
			break;
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.ADD, 
				ClaseLexica.SUB, ClaseLexica.NEG, ClaseLexica.NOT, ClaseLexica.NUMBER_REAL, 
				ClaseLexica.POS, ClaseLexica.POPEN, ClaseLexica.TRUE, ClaseLexica.FALSE, 
				ClaseLexica.ID, ClaseLexica.NUMBER_INTEGER);
		}
	}

	private void OP2a(){
		switch(anticipo.clase()){
		case MUL: 
			empareja(ClaseLexica.MUL);
			break;
		case DIV:
			empareja(ClaseLexica.DIV);
			break;
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.MUL, 
				ClaseLexica.DIV);
		}
	}

	private void OP2b(){
		switch(anticipo.clase()){
		case AND: 
			empareja(ClaseLexica.AND); 
			break;
		case NEG: case NOT: case FLOAT: case NUMBER_REAL: case POS: case POPEN: case TRUE: 
		case FALSE: case ID: case NUMBER_INTEGER: break;
		default: errores.errorSintactico(anticipo.fila(),anticipo.clase(), ClaseLexica.AND);
		}
	}

	//TODO
	private void empareja(ClaseLexica claseEsperada) {
		if (anticipo.clase() == claseEsperada)
			sigToken();
		else errores.errorSintactico(anticipo.fila(),anticipo.clase(),claseEsperada); 
	}
	//DONE
	private void sigToken() {
		try {
			anticipo = alex.yylex();
		}
		catch(IOException e) {
			errores.errorFatal(e);
		}
	}

}
