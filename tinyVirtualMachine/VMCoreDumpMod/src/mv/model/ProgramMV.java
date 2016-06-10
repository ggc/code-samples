package mv.model;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
import java.util.Scanner;

import mv.model.exceptions.InvalidInstructionException;

public class ProgramMV {

	static final java.lang.String MSG_ERROR = "Error: Instruccion incorrecta";
	static final java.lang.String MSG_INTRO = "Introduce el programa fuente";
	static final java.lang.String MSG_SHOW = "El programa introducido es:";
	static final java.lang.String MSG_PROMPT = "> ";
	static final java.lang.String END_TOKEN = "END";
	private Instruction[] _program = new Instruction[100]; //Un poco chapuza.

	public ProgramMV(){
	}


	/**
	 * Clase utilizada para enviar los datos a la vista
	 */
	public class Data{
		
		private Instruction[] _data;
		
		public Instruction[] getInstructions(){
			return _data;
		}
		
		Data(Instruction[] instructions) {
			_data = instructions;
		}
	}
	
	public ProgramMV.Data getData(){
		return new Data(_program);
	}


	
	
	/**
	 * Lee el instrucciones consecutivamente de la fuente input.
	 * @param input
	 */
	public void readProgram(Scanner input) throws InvalidInstructionException{
		int i = 0;
		String line = "";

		do{
			line = input.nextLine();
			if (line.equalsIgnoreCase(""))
				continue;
			if (line.equalsIgnoreCase(END_TOKEN))
				break;
			line = line.trim();
				_program[i] = InstructionParser.parse(line);
//				if(Flags.mode) 		//Si es entrada por teclado debe seguir leyendo!!!
//					continue;
//				else
//					System.exit(2);
			//			if(_program[i]!=null){
			i++;
			//			}
			//			else{
			//				if(Flags.mode)System.out.println(MSG_ERROR);
			//				throw new InvalidInstructionException();
			//			}
		}while(input.hasNext());
		_program[i] = InstructionParser.parse("halt");
	}

	/**
	 * @param i
	 * @return devuelve la instruccion el la posicion i
	 */
	public Instruction getInstructionAt(int i){
		return _program[i];
	}


}
