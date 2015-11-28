package aima.core.environment.puzzleBN;
import aima.core.search.framework.GoalTest;


public class PuzzleBNGoalTest implements GoalTest {

	@Override
	public boolean isGoalState(Object s) {
		boolean isGoal = false;
		EstadoPuzzleBN state = (EstadoPuzzleBN) s;
		if(state.hashCode()>=47027)
			isGoal = true;
		return isGoal;
	}
	
}
