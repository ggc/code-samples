package jlex;
import errors.GestionErrores;


public class AnalizadorLexico {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 65536;
	private final int YY_EOF = 65537;

  private ALexOperations ops;
  private GestionErrores errores;
  public String lexema() {return yytext();}
  public int fila() {return yyline+1;}
  public void fijaGestionErrores(GestionErrores errores) {
	   this.errores = errores;
	  }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public AnalizadorLexico (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	AnalizadorLexico (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private AnalizadorLexico () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

  ops = new ALexOperations(this);
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NOT_ACCEPT,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NOT_ACCEPT,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NOT_ACCEPT,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,65538,
"33:8,1:3,33:2,1,33:18,1,32,33:4,23,33,27,28,25,24,5,2,4,26,3:10,33,22,31,30" +
",29,33:2,20:4,6,20:21,33:4,21,33,17,10,20,19,15,16,20:5,12,9,7,11,20:2,14,1" +
"8,13,8,20:5,33:65413,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,55,
"0,1:2,2,3,1,2,4,1,2,1:4,5,6,7,4,1:5,8,9,4:6,10,1,11,12,13,14,15,16,17,18,12" +
",19,20,21,22,23,24,25,26,27,28,29,30,31")[0];

	private int yy_nxt[][] = unpackFromString(32,34,
"1,2,3,4,5,6,7,46,7:2,51,33,7,52,7:2,54,47,7:3,32,8,36,9,10,11,12,13,14,15,1" +
"6,39,32,-1:37,4,-1:33,4,31,35:2,-1:8,35,-1:21,7,-1:2,7:16,-1:42,19,-1:33,20" +
",-1:33,21,-1:6,23,-1,35:2,-1:8,35,-1:21,24,41,-1:32,23,-1:33,7,-1:2,7:8,17," +
"7:7,-1:15,34,-1:32,38,24,-1,38,-1:18,38,-1:32,18,-1:13,7,-1:2,7:3,25,7:12,-" +
"1:15,24,-1:60,22,-1:6,7,-1:2,7:7,26,7:8,-1:15,7,-1:2,7:13,27,7:2,-1:15,7,-1" +
":2,7:6,28,7:9,-1:15,7,-1:2,7:9,29,7:6,-1:15,7,-1:2,7:9,30,7:6,-1:15,7,-1:2," +
"7:2,37,7:2,40,7:10,-1:15,7,-1:2,7,42,7:14,-1:15,7,-1:2,7:5,43,7:10,-1:15,7," +
"-1:2,7:2,44,7:13,-1:15,7,-1:2,7:12,45,7:3,-1:15,7,-1:2,7:5,48,7:10,-1:15,7," +
"-1:2,7:8,49,7:7,-1:15,7,-1:2,7:6,50,7:9,-1:15,7,-1:2,7:11,53,7:4,-1:12");

	public UnidadLexica yylex ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return ops.unidadEOF();
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{}
					case -3:
						break;
					case 3:
						{return ops.unidadMenos();}
					case -4:
						break;
					case 4:
						{return ops.unidadInt();}
					case -5:
						break;
					case 5:
						{return ops.unidadPunto();}
					case -6:
						break;
					case 6:
						//ops.error();
						{errores.errorLexico(fila(),lexema());}
					case -7:
						break;
					case 7:
						{return ops.unidadID();}
					case -8:
						break;
					case 8:
						{return ops.unidadPuntoComa();}
					case -9:
						break;
					case 9:
						{return ops.unidadMas();}
					case -10:
						break;
					case 10:
						{return ops.unidadPor();}
					case -11:
						break;
					case 11:
						{return ops.unidadDiv();}
					case -12:
						break;
					case 12:
						{return ops.unidadPAp();}
					case -13:
						break;
					case 13:
						{return ops.unidadPCierre();}
					case -14:
						break;
					case 14:
						{return ops.unidadGT();}
					case -15:
						break;
					case 15:
						{return ops.unidadIgual();}
					case -16:
						break;
					case 16:
						{return ops.unidadLT();}
					case -17:
						break;
					case 17:
						{return ops.unidadOr();}
					case -18:
						break;
					case 18:
						{return ops.unidadAmpSep();}
					case -19:
						break;
					case 19:
						{return ops.unidadGE();}
					case -20:
						break;
					case 20:
						{return ops.unidadEQ();}
					case -21:
						break;
					case 21:
						{return ops.unidadLE();}
					case -22:
						break;
					case 22:
						{return ops.unidadNE();}
					case -23:
						break;
					case 23:
						{return ops.unidadReal();}
					case -24:
						break;
					case 24:
						{return ops.unidadFloat();}
					case -25:
						break;
					case 25:
						{return ops.unidadNum();}
					case -26:
						break;
					case 26:
						{return ops.unidadNot();}
					case -27:
						break;
					case 27:
						{return ops.unidadAnd();}
					case -28:
						break;
					case 28:
						{return ops.unidadBool();}
					case -29:
						break;
					case 29:
						{return ops.unidadTrue();}
					case -30:
						break;
					case 30:
						{return ops.unidadFalse();}
					case -31:
						break;
					case 32:
						{ops.error();}
					case -32:
						break;
					case 33:
						{return ops.unidadID();}
					case -33:
						break;
					case 34:
						{return ops.unidadFloat();}
					case -34:
						break;
					case 36:
						{ops.error();}
					case -35:
						break;
					case 37:
						{return ops.unidadID();}
					case -36:
						break;
					case 39:
						{ops.error();}
					case -37:
						break;
					case 40:
						{return ops.unidadID();}
					case -38:
						break;
					case 42:
						{return ops.unidadID();}
					case -39:
						break;
					case 43:
						{return ops.unidadID();}
					case -40:
						break;
					case 44:
						{return ops.unidadID();}
					case -41:
						break;
					case 45:
						{return ops.unidadID();}
					case -42:
						break;
					case 46:
						{return ops.unidadID();}
					case -43:
						break;
					case 47:
						{return ops.unidadID();}
					case -44:
						break;
					case 48:
						{return ops.unidadID();}
					case -45:
						break;
					case 49:
						{return ops.unidadID();}
					case -46:
						break;
					case 50:
						{return ops.unidadID();}
					case -47:
						break;
					case 51:
						{return ops.unidadID();}
					case -48:
						break;
					case 52:
						{return ops.unidadID();}
					case -49:
						break;
					case 53:
						{return ops.unidadID();}
					case -50:
						break;
					case 54:
						{return ops.unidadID();}
					case -51:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
