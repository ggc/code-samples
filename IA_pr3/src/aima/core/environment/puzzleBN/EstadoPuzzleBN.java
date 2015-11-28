package aima.core.environment.puzzleBN;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;

class EstadoPuzzleBN 
{
	/*
	 * Codificacion del estado
	 */

	//Posiciones de fichas negras y espacio
	//private int n1,n2,n3,s; 
	// Reimplementar con numeros primos como posiciones
	private int[] primes = {17,19,23,29,31,37,41};
	private int[] black = new int[3];
	private int empty = 3;

	/**
	 * Constructores
	 */
	public EstadoPuzzleBN()
	{
		this(0, 1, 2, 3);
	}


	public EstadoPuzzleBN(EstadoPuzzleBN e)
	{
		this(e.getBlack()[0], e.getBlack()[1], e.getBlack()[2], e.getS());
	}

	public EstadoPuzzleBN(int n1, int n2, int n3, int s)
	{
		if((n1!=n2) && (n1!=n3) && (n1!=s) && 
				(n2!=n3) && (n2!=s) && 
				(n3!=s)){
			black[0]=n1;
			black[1]=n2;
			black[2]=n3;
			empty=s;
		}else{
			black[0]=0;
			black[1]=1;
			black[2]=2;
			empty=3;
		}
	}

	// Las acciones mueven el espacio a la izq o der de 1 a 6 pos.
	/**
	 * Operaciones. d1 mover espacio der una pos, d2 der dos pos, etc
	 */
	public static Action d1 = new DynamicAction("d1");
	public static Action d2 = new DynamicAction("d2");
	public static Action d3 = new DynamicAction("d3");

	public static Action i1 = new DynamicAction("i1");
	public static Action i2 = new DynamicAction("i2");	
	public static Action i3 = new DynamicAction("i3");

	/**
	 * Operadores para transformar un estado en otro
	 */

	public void move(int pos){
		int space = empty;
		empty+=pos;
		for(int i = 0;i<3;i++){
			if(black[i]==empty) black[i]=space;
		}
	}


	/**
	 * Aplicabilidad de un operador
	 * @param where
	 * @return 
	 */
	@SuppressWarnings("static-access")
	public boolean movimientoValido(Action where){
		boolean valid = false;
		EstadoPuzzleBN nextState = new EstadoPuzzleBN(this);

		if(nextState.d1.equals(where)){ 
			if(empty<6){
				nextState.move(1);
				valid=!nextState.peligro();
			}
		}
		if(nextState.d2.equals(where)){ 
			if(empty<5){
				nextState.move(2);
				valid=!nextState.peligro();}
		}
		if(nextState.d3.equals(where)){ 
			if(empty<4){
				nextState.move(3);
				valid=!nextState.peligro();}
		}
		if(nextState.i1.equals(where)){ 
			if(empty>0){
				nextState.move(-1);
				valid=!nextState.peligro();}
		}
		if(nextState.i2.equals(where)){ 
			if(empty>1){
				nextState.move(-2);
				valid=!nextState.peligro();}
		}
		if(nextState.i3.equals(where)){ 
			if(empty>2){
				nextState.move(-3);
				valid=!nextState.peligro();}
		}
		return valid;
	}


	/**
	 * Comprobacion de estados de peligro. BBBNSNN, d3 peligro?
	 * @return True siempre. No hay estados de peligro
	 */
	private boolean peligro(){
		//		int i;
		//		if (empty>6 || empty<0) return true;
		//		for(i=0;i<3;i++){
		//			if(black[i]>6 || black[i]<0) return true;
		//		}
		//		if(		(black[0]==black[1]) || 
		//				(black[0]==black[2]) || 
		//				(black[0]==empty) || 
		//				(black[1]==black[2]) || 
		//				(black[1]==empty) || 
		//				(black[2]==empty)){
		//			return true;
		//		}
		return false;
	}

	/** 
	 * Comparador entre estados 
	 */
	public boolean equals(Object o){
		//Fisicamente iguales
		if (this == o) { 
			return true;
		}//Nulo o tipos diferentes
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		EstadoPuzzleBN newState = (EstadoPuzzleBN) o;
		int total=this.hashCode();// Como son diferentes se asegura una suma unica con estados diferentes;
		int oTotal=newState.hashCode();

		if( (total==oTotal) && (empty==newState.empty)){
			return true;
		}else
			return false;
	}

	/**
	 * 
	 * @return
	 */
	public int hashCode(){
		// Se asegura que no se repiten los valores
		return (primes[black[0]]*primes[black[1]]*primes[black[2]])+primes[empty];
	}

	/**
	 * Getters
	 * @return
	 */
	public int[] getBlack()
	{
		return black;
	}
	public int getS()
	{
		return empty;
	}
}