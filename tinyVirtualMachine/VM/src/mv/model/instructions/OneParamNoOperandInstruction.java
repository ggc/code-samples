package mv.model.instructions;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.exceptions.instructions.InstructionException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */

public abstract class OneParamNoOperandInstruction implements Instruction {
	
	protected OneParamNoOperandInstruction(String type,int param){
		this.type=type;
		this.param=param;
	}
	
	protected int param;
	protected String type;
	
	protected abstract void operate(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException;
	protected abstract Instruction createInstruction(int param);
	
	public Instruction parse(String line) throws InstructionException{
		
		Instruction result = null;
		line = line.trim();
		String[] splitLine = line.split(" +");
		
		if (splitLine.length==2)
			try{
			result = createInstruction(Integer.parseInt(splitLine[1]));
			}catch(NumberFormatException e){
				throw new InstructionException("La direccion tiene que ser un numero entero.");
			}
		return result;
	}
	
	public void execute(OperandStack stack,Memory memory,ControlUnit control) throws InstructionException{
 			operate(stack, memory, control); 
	}
	
	public String toString(){
		return type + " " + param;
	}

}
