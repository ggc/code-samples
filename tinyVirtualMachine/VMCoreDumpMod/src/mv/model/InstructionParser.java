package mv.model;

import mv.model.exceptions.InvalidInstructionException;
import mv.model.exceptions.instructions.InstructionException;
import mv.model.instructions.arithmetic.*;
import mv.model.instructions.comparison.*;
import mv.model.instructions.io.*;
import mv.model.instructions.jump.*;
import mv.model.instructions.logic.*;
import mv.model.instructions.memory.*;
import mv.model.instructions.misc.Halt;
import mv.model.instructions.stack.*;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */


public class InstructionParser {


	static Instruction[] arrayIns = { 	
		new Add(), new Div(),		new Mul(),	new Neg(),		new Sub(), 
		new EQ(),  new GT(),		new LE(),	new LT(), 
		new In(),  new Out(), 
		new BF(),  new BT(),		new Jump(),	new Jumpind(),	new RBF(), new RBT(), new RJUMP(),
		new And(), new Not(),		new Or(), 
		new Load(),new Loadind(),	new Store(),new Storeind(), 
		new Halt(),
		new Dup(), new Flip(), 		new Pop(), 	new Push(), };

	public InstructionParser(){}

	/**
	 * Analiza la linea line pasada como argumento y la convierte en la instruccion asociada.
	 * @param line
	 * @return La instruccion correspondiente.
	 */
	public static Instruction parse(String line) throws InvalidInstructionException{

		line = line.trim();
		line = line.toUpperCase();
		String [] splitLine = line.split(" +");

		Instruction parsedInstruction = null;

		for (Instruction i:arrayIns){
			String[] defaultInstruction = i.toString().split(" +");   

			try {
				if (splitLine[0].equals(defaultInstruction[0])){
					try{
						parsedInstruction = i.parse(line);
					}catch(InstructionException e){
						throw new InvalidInstructionException(e);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (parsedInstruction == null)
			throw new InvalidInstructionException("Instruccion no reconocida: " + line);
		return parsedInstruction;
	}
}
