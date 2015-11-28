package aima.core.environment.puzzleBN;

import aima.core.search.framework.HeuristicFunction;

public class PuzzleBNHeuristicFunctionMissplaced implements HeuristicFunction{

	
	@Override
	public double h(Object state) {
		
		EstadoPuzzleBN puzzle = (EstadoPuzzleBN) state;
		int i, menor = 7;
		for(i=0;i<3;i++){
			if(puzzle.getBlack()[i]<menor) menor = puzzle.getBlack()[i];
		}
		return menor;
	}

}
