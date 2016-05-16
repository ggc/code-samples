package mv.model;

import java.util.Scanner;

import mv.model.exceptions.InvalidInstructionException;
import mv.view.Flags;

public class ProgramLoader {

	private static ProgramMV _program;
	
	static final String MSG_HELP = "Use -h| --help para mas detalles."; 
	static final String MSG_ERROR = "Error: Instruccion incorrecta";

	public static void setProgram(Flags flags,  Scanner sc){

		_program = new ProgramMV();

		try {
			_program.readProgram(sc);
		} catch (InvalidInstructionException e) {
			//if(flags.m_batch){
				System.err.println("Error al leer el programa.");
				System.exit(2);
			//}
		}
	}

	public static ProgramMV returnProgram(){

		return _program;
	}
}
