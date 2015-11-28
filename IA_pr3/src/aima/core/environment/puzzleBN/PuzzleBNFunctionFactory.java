package aima.core.environment.puzzleBN;

import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

public class PuzzleBNFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;
	
	public static ActionsFunction getActionsFunction(){
		if(null == _actionsFunction){
			_actionsFunction = new PuzzleBNActionsFunction();
		}
		return _actionsFunction;
	}
	
	public static ResultFunction getResultFunction(){
		if(null == _resultFunction){
			_resultFunction = new PuzzleBNResultFunction();
		}
		return _resultFunction;
	}
	
}
