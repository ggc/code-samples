package mv.model;

import java.util.ArrayList;

import mv.model.exceptions.cp.CPNotExistException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class ControlUnit extends Observable<ControlUnit.Observer> {

	private int _cp;
	private boolean _halted;
	private boolean _set;

	public ControlUnit(){
		_cp = 0;
		_observers = new ArrayList<ControlUnit.Observer>();
	}

	/**
	 * Interfaz implementado por los observadores de la clase
	 * para ser notificados cuando ocurre algún evento en el ControlUnit.
	 */
	public interface Observer {
		
		
		/**
		 * Se invoca cuando ha habido un cambio en la unidad de control
		 */
		void onCPchange(ControlUnit.Data cpData);
		
		/**
		 * Se invoca cuando la ejecución del programa ha terminado de manera
		 * correcta, es decir, cuando se alcanzada una instrucción HALT.
		 */
		public void onHalt();
	}
	
	/**
	 * Clase utilizada para enviar los datos a la vista
	 */
	public class Data{

		private int _data;
		
		public int getCp() {
			
			return _data;
		}

		Data(int cp) {
			_data = cp;
		}
		
	}
	
	public ControlUnit.Data getData(){
		
		return new Data(_cp); 
	}
	
	public void setData(Data data){
		_cp = data._data;
		notificar();
	}
	
	
	/**
	 * @return Valor actual del contador de programa.
	 */
	public int getCP(){
		return _cp;
	}

	/**
	 * @param newCP
	 * @return True si se realizo el cambio de cp 
	 */
	public void setCP(int newCP) throws CPNotExistException{

		if(_halted)
			return ;
		if(newCP>=0){
			this._cp = newCP;
			_set=true;
			notificar();
			return ;
		}
		else
			throw new CPNotExistException("CP no valido.");
	}

	/**
	 * @param increment
	 * @return true si se incremento el cp. (Solo usado en RJUMP)
	 */
	public void increaseCP(int increment) throws CPNotExistException{

		if(_halted)
			return ;
		this._cp +=increment;
		this._set = true;
		if(this._cp < 0)
			throw new CPNotExistException("CP no valido.");
		notificar();
	}

	/**
	 * Incrementa el contador de programa
	 */
	public void next(){

		if (_halted)
			return;
		if(!_set){
			this._cp++;
			notificar();
		}
		else
			_set=false;
	}

	/**
	 * Suspende la ejecucion del programa.
	 */
	public void halt(){
		this._halted = true;
		for(ControlUnit.Observer o: _observers)
			o.onHalt();
	}

	/**
	 * @return True si esta suspendida.
	 */
	public boolean isHalted(){
		return _halted;
	}

	private void notificar(){
		for(ControlUnit.Observer o: _observers)
			o.onCPchange(getData());
	}

}
