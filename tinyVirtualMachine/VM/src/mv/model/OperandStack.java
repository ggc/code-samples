package mv.model;

import java.util.ArrayList;

import mv.model.exceptions.stack.EmptyStackException;
import mv.model.exceptions.stack.FullStackException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class OperandStack extends Observable<OperandStack.Observer> {

	private int _pila[];
	private int _contador;

	/**
	 * @param max_size tamanyo maximo de la _pila
	 * Constructor de la _pila.
	 */
	public OperandStack(int max_size){
		this._pila = new int[max_size];
		this._contador = -1;
		_observers = new ArrayList<OperandStack.Observer>();
	}


	/**
	 * Interfaz implementado por los observadores de la clase
	 * para ser notificados cuando ocurre algún evento en la _pila.
	 */
	public interface Observer {

		/**
		 * Se invoca cuando ha habido un cambio en la _pila
		 */
		void onStackChange(OperandStack.Data data);
	}

	/**
	 * Clase utilizada para enviar los datos a la vista
	 */
	public class Data{
		int[] _stack;
		int _cntr;
		
		Data(int[] stack, int cntr) {
			_stack = stack;
			_cntr = cntr;
		}
		
		public int[] getStack(){
			return _stack;
		}
		public int getCntr(){
			return _cntr;
		}
		
		public String toString(){

			String cadenaTextoAuxiliar = "";

			if (_cntr == -1)
				cadenaTextoAuxiliar = "<vacia>";
			else {
				for(int i=0 ; i<= this._cntr ; i++){
					if (i != this._cntr)
						cadenaTextoAuxiliar += Integer.toString(this._stack[i]) + " ";
					else 
						cadenaTextoAuxiliar += Integer.toString(this._stack[i]);
				}
			}

			return cadenaTextoAuxiliar;
		}
	}




	public OperandStack.Data getData(){
		return new Data(_pila, _contador);
	}

	/**
	 * Apila el valor value en la cima de la _pila
	 * @param value
	 * @return true si apilo correctamente.
	 */
	public void push(int value) throws FullStackException{

		if (this._contador < this._pila.length - 1) {
			this._contador++;
			this._pila[this._contador] = value;
			for(OperandStack.Observer o: _observers)
				o.onStackChange(getData());
		}
		else
			throw new FullStackException("La memoria esta llena");
	}

	/**
	 * Desapila la cima
	 */
	public void pop() throws EmptyStackException{

		if (!this.isEmpty()) {
			this._contador--;
			for(OperandStack.Observer o: _observers)
				o.onStackChange(getData());
		}
		else
			throw new EmptyStackException("La pila esta vacia");
	}

	/**
	 * @return valor de la cima
	 */
	public int top() throws EmptyStackException{

		if (!this.isEmpty()) {
			for(OperandStack.Observer o: _observers)
				o.onStackChange(getData());
			return this._pila[this._contador];
		}
		else
			throw new EmptyStackException("La pila esta vacia");
	}

	/**
	 * Comprueba que este vacia la _pila
	 * @return True si esta vacia.
	 */
	public boolean isEmpty(){

		boolean isEmpty = false;

		if (this._contador == -1)
			isEmpty = true;

		return isEmpty;
	}

	public String toString(){

		String cadenaTextoAuxiliar = "";

		if (this.isEmpty())
			cadenaTextoAuxiliar = "<vacia>";
		else {
			for(int i=0 ; i<= this._contador ; i++){
				if (i != this._contador)
					cadenaTextoAuxiliar += Integer.toString(this._pila[i]) + " ";
				else 
					cadenaTextoAuxiliar += Integer.toString(this._pila[i]);
			}
		}

		return cadenaTextoAuxiliar;
	}


}
