package aima.core.environment.puzzleBN;

import aima.core.search.framework.HeuristicFunction;

public class PuzzleBNHeuristicFunctionHash implements HeuristicFunction{

	
	@Override
	public double h(Object state) {
		
		EstadoPuzzleBN puzzle = (EstadoPuzzleBN) state;
		
		return puzzle.hashCode();
	}

}
