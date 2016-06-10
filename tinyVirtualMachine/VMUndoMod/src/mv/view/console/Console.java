package mv.view.console;

import mv.controller.Controller;
import mv.model.Instruction;
import mv.model.CPU.Observer;
import mv.model.ProgramMV.Data;

/**
 * Esta clase se encargar de mostrar la consola. La implementacion y funcionalidad se encuentra en la clase Prompt
 * @author anima
 *
 */
public class Console implements Observer{

	//Atributos
	private Controller _controller;
	//Step
	static final String MSG_EXEC_BEGIN = "Comienza la ejecucion de ";
	static final String MSG_EXEC_END = "El estado de la maquina tras ejecutar la instruccion es:";
	static final String MSG_EXEC_ERROR = "Error en la ejecucion de la instruccion";
	//Prompt
	static final java.lang.String MSG_PROMPT = "> ";
	static final java.lang.String MSG_PARSE_ERROR = "Error: Comando no reconocido";
	static final java.lang.String END_TOKEN1 = "QUIT";
	//ProgramMV
	static final java.lang.String MSG_ERROR = "Error: Instruccion incorrecta";
	static final java.lang.String MSG_INTRO = "Introduce el programa fuente";
	static final java.lang.String MSG_SHOW = "El programa introducido es:";
	static final java.lang.String END_TOKEN2 = "END";

	//Constructores
	public Console(Controller controller){
		_controller = controller;	

		_controller.addCPUObserver(this);
	}

	//Metodos

	@Override
	public void onStart(Data instructions) {
		System.out.println(MSG_SHOW);
		int i = 0;
		while(instructions.getInstructions()[i]!=null){
			System.out.printf("%s: %s\n",i,instructions.getInstructions()[i].toString());
			i++;
		}
		System.out.print(MSG_PROMPT);
	}

	@Override
	public void onInstructionStart(Instruction instr) {
		System.out.println(MSG_EXEC_BEGIN + instr.toString());
	}

	@Override
	public void onInstructionEnd(Instruction instr,
			mv.model.Memory.Data mem,
			mv.model.OperandStack.Data ops,
			mv.model.ControlUnit.Data pc) {
		System.out.println(MSG_EXEC_END);
		System.out.println("Memoria: " + mem.toString() +"\nPila de operandos: "+ops.toString());
	}

	@Override
	public void onError(Exception error) {
		System.err.println(error.getMessage());
		Throwable cause = error;
		while(cause.getCause()!=null){
			cause=cause.getCause();
			System.err.println("Causado por: " + cause.getMessage());
		} 

	}


}
