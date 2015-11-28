package aima.core.environment.puzzleBN;

import aima.core.agent.Action;
import aima.core.search.framework.ResultFunction;

class PuzzleBNResultFunction implements ResultFunction{

	@Override
	public Object result(Object s, Action a) {
		EstadoPuzzleBN state = (EstadoPuzzleBN) s;
		
		if(EstadoPuzzleBN.d1.equals(a)){
			EstadoPuzzleBN newState = new EstadoPuzzleBN(state);
			newState.move(1);
			return newState;
		}else if(EstadoPuzzleBN.d2.equals(a)){
			EstadoPuzzleBN newState = new EstadoPuzzleBN(state);
			newState.move(2);
			return newState;
		}else if(EstadoPuzzleBN.d3.equals(a)){
			EstadoPuzzleBN newState = new EstadoPuzzleBN(state);
			newState.move(3);
			return newState;
		}else if(EstadoPuzzleBN.i1.equals(a)){
			EstadoPuzzleBN newState = new EstadoPuzzleBN(state);
			newState.move(-1);
			return newState;
		}else if(EstadoPuzzleBN.i2.equals(a)){
			EstadoPuzzleBN newState = new EstadoPuzzleBN(state);
			newState.move(-2);
			return newState;
		}else if(EstadoPuzzleBN.i3.equals(a)){
			EstadoPuzzleBN newState = new EstadoPuzzleBN(state);
			newState.move(-3);	
			return newState;
		}
		return s;
	}
}
