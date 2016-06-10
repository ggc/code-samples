package mv.model;

import java.util.ArrayList;

import mv.model.exceptions.commands.CommandException;
import mv.model.exceptions.instructions.InstructionException;

public class CPU extends Observable<CPU.Observer>{

	private OperandStack _pila;
	private Memory _memoria;
	private ControlUnit _uc;
	private boolean _parada = false;
	private ProgramMV _program;
	//	private Window _window;//QUITAR

	/**
	 * Constructor por defecto con tamano de pila y memoria igual a 100
	 */
	public CPU(){
		this._memoria = new Memory(100);
		this._pila = new OperandStack(100);
		this._uc = new ControlUnit();
		this._parada = true;
		_observers = new ArrayList<CPU.Observer>();
	}

	public CPU(int memory_size,int stack_size){
		this._memoria = new Memory(memory_size);
		this._pila = new OperandStack(stack_size);
		this._uc = new ControlUnit();
		this._parada = true;
		_observers = new ArrayList<CPU.Observer>();
	}

	/**
	 * Interfaz implementado por los observadores de la clase
	 * para ser notificados cuando ocurre algún evento de alto nivel 
	 * en la CPU. Para eventos relacionados con la memoria, pila y unidad de control
	 * se deben utilizar los observadores especializados.
	 */
	public interface Observer {


		/**
		 * Se invoca cuando se carga un nuevo programa en la CPU. 
		 * Además indica que el contador de programa vale 0 y la memoria
		 * y la pila están vacías.
		 * 
		 * @param program Lista de las instrucciones del nuevo programa.
		 */
		public void onStart(ProgramMV.Data instructions);

		/**
		 * Se invoca justo antes de ejecutar una instrucción. 
		 * Si la ejecución de la instrucción tiene exito, a continuación
		 * se invocará a onInstructionEnd. Si la ejecución falla se
		 * invocará a onError. 
		 * 
		 * @param instr Instrucción que se va a ejecutar.
		 */
		public void onInstructionStart(Instruction instr);

		/**
		 * Se invoca justo después de ejecutar una instrucción de
		 * manera correcta. 
		 * 
		 * @param instr Instrucción que se completó.
		 * @param mem Estado final de la memoria 
		 * @param ops Estado final de la pila de operandos .
		 * @param pc Estado final del contador de programa.
		 */
		public void onInstructionEnd(Instruction instr,
				Memory.Data mem, OperandStack.Data ops, ControlUnit.Data pc);

		/**
		 * Se invoca cuando se produce un error al ejecutar una instrucción.
		 * 
		 * @param instr Instrucción que provocó el error.
		 * @param trapMessage Mensaje con la explicación del error.
		 */
		public void onError(Exception error);



	}


	/**
	 * Método que utilizará el controlador para indicar 
	 * que se carga un nuevo programa en la CPU.
	 * Avisa a los observadores enviándoles la lista de instrucciones cargadas
	 */

	public void start() {
		for(CPU.Observer o: _observers)
			o.onStart(_program.getData());
	}


	/*********************************************************/
	/* Métodos que utilizará el controlador para registrar
	 * la vista como oyente de los elementos internos de la 
	 * cpu: pila, memoria y unidad de control  
	/*********************************************************/


	public void addObserver(OperandStack.Observer obs) {
		_pila.addObserver(obs);
	}

	public void addObserver(Memory.Observer obs) {
		_memoria.addObserver(obs);
	}

	public void addObserver(ControlUnit.Observer obs) {
		_uc.addObserver(obs);
	}







	/**
	 * @param program
	 * Hace la carga de un programa.
	 */
	public void loadProgram(ProgramMV program){
		this._program = program;
		_parada = false;
	}

	/**
	 * @return True si se ejecuta correctamente.
	 * Ejecuta la siguiente instruccion en la lista del programa.
	 */
	public void step() throws InstructionException{

		if (_parada || (_uc.isHalted())){
			_uc.halt();
			return;
		}
		if (_program.getInstructionAt(_uc.getCP())!=null){
			for(Observer o: _observers)
				o.onInstructionStart(_program.getInstructionAt(_uc.getCP()));
			//								Descomentar para visualizar el contador de programa
			//								System.out.printf("   cp=%s\n",uc.getCP()); 
			try {
				_program.getInstructionAt((_uc.getCP())).execute(_pila, _memoria, _uc);
			} catch (InstructionException e) {
				for(Observer o: _observers)
					o.onError(e);
				throw e;
			}
			//Si no ha cambiado el cp (La instruccion no era de salto) prepara el siguiente cp.
			_uc.next();
			for(Observer o: _observers)
				o.onInstructionEnd(_program.getInstructionAt(_uc.getCP()),
						_memoria.getData(), _pila.getData(), _uc.getData());
			//CUIDADO!!!
			//			if(_window != null)
			//				_window.refresh();
		}
		else
			_uc.halt();
	}


	public String toString(){

		String cadena = "Memoria: " + _memoria.toString() +"\nPila de operandos: "+_pila.toString();
		return cadena;
	}

	public boolean isHalted(){
		return _uc.isHalted();
	}

	/**
	 * @return Nombre de la siguiente instruccion en el programa.
	 */
	public String getNextInstructionName(){
		if (_program.getInstructionAt(_uc.getCP())!=null){		
			return _program.getInstructionAt(_uc.getCP()).toString();
		}
		else
			return null;
	}


	/**
	 * @param inst
	 * @throws CommandException
	 */
	public void cpuExecute(Instruction inst) throws CommandException{
		try {
			inst.execute(_pila, _memoria, _uc);
		} catch (InstructionException e) {
			for(Observer o: _observers)
				o.onError(e);
			throw new CommandException("",e);
		}		
	}

	//Setters and Getters//
	public OperandStack getStack() {
		return _pila;
	}

	public ControlUnit getCU() {
		return _uc;
	}

	public ProgramMV getProgram() {
		return _program;
	}

	public Memory getMemory() {
		return _memoria;
	}
}
