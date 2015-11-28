package aima.core.environment.puzzleBN;

import aima.core.agent.Action;
import aima.core.search.framework.StepCostFunction;

public class PuzzleBNStepCostFunction implements StepCostFunction{

	@SuppressWarnings("static-access")
	@Override
	public double c(Object s, Action a, Object sDelta) {
		EstadoPuzzleBN state = (EstadoPuzzleBN) s;
		int cost = 1;
		
		if(		state.d1.equals(a) ||
				state.i1.equals(a)) cost = 1;
		if(		state.d2.equals(a) ||
				state.i2.equals(a)) cost = 2;
		if(		state.d3.equals(a) ||
				state.i3.equals(a)) cost = 3;
		return cost;
		
	}

}
