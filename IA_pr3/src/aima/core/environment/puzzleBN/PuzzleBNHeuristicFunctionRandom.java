package aima.core.environment.puzzleBN;

import aima.core.search.framework.HeuristicFunction;
import java.util.Random;

public class PuzzleBNHeuristicFunctionRandom implements HeuristicFunction{

	
	@Override
	public double h(Object state) {
		
		return new Random().nextInt();
	}

}
