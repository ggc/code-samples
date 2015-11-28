package aima.core.environment.puzzleBN;
import java.util.LinkedHashSet;
import java.util.Set;
import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;



class PuzzleBNActionsFunction implements ActionsFunction{

	public Set<Action> actions(Object s) {
		EstadoPuzzleBN state = (EstadoPuzzleBN) s;
		Set<Action> actions = new LinkedHashSet<Action>();
		
		if(state.movimientoValido(EstadoPuzzleBN.d1))
			actions.add(EstadoPuzzleBN.d1);
		if(state.movimientoValido(EstadoPuzzleBN.d2))
			actions.add(EstadoPuzzleBN.d2);
		if(state.movimientoValido(EstadoPuzzleBN.d3))
			actions.add(EstadoPuzzleBN.d3);
		if(state.movimientoValido(EstadoPuzzleBN.i1))
			actions.add(EstadoPuzzleBN.i1);
		if(state.movimientoValido(EstadoPuzzleBN.i2))
			actions.add(EstadoPuzzleBN.i2);
		if(state.movimientoValido(EstadoPuzzleBN.i3))
			actions.add(EstadoPuzzleBN.i3);
		
		return actions;
	}

}
