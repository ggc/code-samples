package mv.model;

import java.util.ArrayList;

import mv.model.exceptions.memory.AddressNotExistException;
import mv.model.exceptions.memory.FullMemoryException;

/**
 * @author Alejandro Pequeño Pulleiro, Gabriel Galán Casillas
 *
 */
public class Memory extends Observable<Memory.Observer> {

	private DataMemoryRegister _memory[];
	private int _contador ;

	public Memory(int max_size){

		this._memory = new DataMemoryRegister[max_size];
		this._contador = -1;
		_observers = new ArrayList<Memory.Observer>();
	}


	/**
	 * Interfaz implementado por los observadores de la clase
	 * para ser notificados cuando ocurre algún evento en la memoria.
	 */
	public interface Observer {
		
		/**
		 * Se invoca cuando ha habido un cambio en la memoria
		 */
		void onMemoryChange(Memory.Data data);

	}
	
	/**
	 * Clase utilizada para enviar los datos a la vista
	 * @author Juan A. Recio García
	 */
	public class Data{
		
		private DataMemoryRegister[] _data;
		private int _cntr;
		
		Data(DataMemoryRegister[] data, int cntr) {
			_data = data;
			_cntr = cntr;
		}

		public DataMemoryRegister[] getData() {
			return _data;
		}
		
		public int getCntr(){
			return _cntr;
		}
		
		public String toString(){

			String cadenaTextoAuxiliar = "";

			if (this._cntr == -1)
				cadenaTextoAuxiliar = "<vacia>";
			else {
				for(int i=0 ; i<= this._cntr ; i++){//Sustituir por el toString de la clase DataMemoryRegister
					cadenaTextoAuxiliar +=this._data[i].toString();
				}
			}

			return cadenaTextoAuxiliar.trim();
		}
		
	}
	
	public Memory.Data getData(){
		return new Data(_memory, _contador);
	}
	
	public void setData(Data data){
		_memory = data._data;
		_contador = data._cntr;
		for(Memory.Observer o: _observers)
			o.onMemoryChange(getData());
	}
	
	
	/**
	 * Almacena el valor value en la posicion de memoria pos. La posicion de memoria es independiente
	 * de la posicion real en el array.
	 * @param pos
	 * @param value
	 * @return True si se almaceno correctamente
	 */
	public void store(int pos,int value) throws FullMemoryException,AddressNotExistException{
		boolean realizado = false;

		if(pos>=0){
			for(int i=0 ; i<= _contador ; i++){
				if(_memory[i].getPos()==pos){
					_memory[i].setValue(value);
					realizado = true;
				}
			}

			if((_contador!=_memory.length-1)&&(!realizado)) {	
				if(this._contador == -1)
				{
					_memory[0] = new DataMemoryRegister(pos, value);
					_contador++;
					realizado = true;
				}
				else{
					for(int k=0 ; k <=this._contador && !realizado; k++){	//Recorre la memoria de principio a fin
						if(_memory[k].getPos()>pos)	//Busca la primera posicion mayor que pos para anadir el nuevo elem antes.
						{
							for(int l=this._contador; l>=k ; l--)
							{
								int auxPos = _memory[l].getPos();
								int auxValue = _memory[l].getValue();
								_memory[l+1] = new DataMemoryRegister(auxPos, auxValue);
							}
							_memory[k] = new DataMemoryRegister(pos, value);
							_contador++;
							realizado = true;
						}
					}
					if(!realizado){
						_contador++;
						_memory[_contador]=new DataMemoryRegister(pos, value);
						realizado=true;
					}
				}
			}
		}
		else
			throw new AddressNotExistException("Direccion no valida.");

		if (!realizado)
			throw new FullMemoryException();
		
		for(Memory.Observer o: _observers)
			o.onMemoryChange(getData());
	}

	/**
	 * Realiza la carga del valor en la posicion pos.
	 * @param pos
	 * @return El valor cargado.
	 * @throws MemoryException 
	 */
	public int load(int pos) throws AddressNotExistException{

		int accesableValue = 0 ;

		if(this.canLoad(pos)){
			for(int i = 0 ; i<=_contador ; i++){
				if(_memory[i].getPos()==pos)
					accesableValue = _memory[i].getValue();
			}
		} 

		for(Memory.Observer o: _observers)
			o.onMemoryChange(getData());
		return accesableValue;
	}

	/**
	 * Comprueba que pueda ser leido un valor.
	 * @param pos
	 * @return True si puede ser leido.
	 * @throws MemoryException 
	 */
	public boolean canLoad(int pos) throws AddressNotExistException{

		boolean realizar = false;

		if (this.busquedaRregistro(pos))
			realizar = true;	

		return realizar;
	}

	/**
	 * Busca un valor en la memoria.
	 * @param pos
	 * @return True si es encontrado.
	 */
	private boolean busquedaRregistro(int pos) throws AddressNotExistException{

		int i =0;
		boolean encontrado = false;

		if(pos<0)
			throw new AddressNotExistException("Direccion no valida");
		while (i<= this._contador && !encontrado){
			if (this._memory[i].getPos() == pos){
				encontrado = true;
			}
			i++;
		}
		return encontrado;
	}


	public String toString(){

		String cadenaTextoAuxiliar = "";

		if (this._contador == -1)
			cadenaTextoAuxiliar = "<vacia>";
		else {
			for(int i=0 ; i<= this._contador ; i++){//Sustituir por el toString de la clase DataMemoryRegister
				cadenaTextoAuxiliar +=this._memory[i].toString();
			}
		}

		return cadenaTextoAuxiliar.trim();
	}
	
	public int getSize(){
		return _memory.length;
	}

	public DataMemoryRegister getNPair(int pair){
		return _memory[pair];
	}
}
