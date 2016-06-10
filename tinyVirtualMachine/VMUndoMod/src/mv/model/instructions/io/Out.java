package mv.model.instructions.io;

import mv.model.ControlUnit;
import mv.model.Instruction;
import mv.model.Memory;
import mv.model.OperandStack;
import mv.model.instructions.NoParamOneOperandInstruction;
import mv.ouput.TwoWayWriter;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Out extends NoParamOneOperandInstruction {

	//Atributos
	//private static PrintStream output;
	private static TwoWayWriter _output1;
	private static TwoWayWriter _output2;

	//Constructores
	public Out(){
		super("OUT");

	}

	//Metodos
	protected Instruction createInstruction(){
		return new Out();
	}


	protected void operate(OperandStack stack,Memory memory,ControlUnit control,int operand){ 
		_output1.write((char)operand);
		if(_output2 != null)
			_output2.write((char)operand);
	}

	public static void setOutput1(TwoWayWriter output1){
		_output1 = output1;
	}
	
	public static void setOutput2(TwoWayWriter output2){
		_output2 = output2;
	}

}
