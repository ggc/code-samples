package mv.model;

import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public interface Instruction {

	//Interfaz
	Instruction parse(String line) throws InstructionException;
	
	void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException;
}
