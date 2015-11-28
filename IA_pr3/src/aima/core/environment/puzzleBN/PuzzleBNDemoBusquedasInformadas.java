package aima.core.environment.puzzleBN;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;

public class PuzzleBNDemoBusquedasInformadas {
	
	static EstadoPuzzleBN estadoInicial = new EstadoPuzzleBN();
	static PuzzleBNHeuristicFunctionMissplaced hf = new PuzzleBNHeuristicFunctionMissplaced();
	
	
	
	public static void main(String[] args){
		PuzzleBFSDemoAStar();
		PuzzleBFSDemoGreedy();
	}
	
	private static void PuzzleBFSDemoAStar(){
		System.out.println("\nPuzzleBFSDemo A*:");
		try{
			Problem problem = new Problem(estadoInicial,
					PuzzleBNFunctionFactory.getActionsFunction(), 
					PuzzleBNFunctionFactory.getResultFunction(),
					new PuzzleBNGoalTest(),
					new PuzzleBNStepCostFunction());
		
			Search search = new AStarSearch(new GraphSearch(), hf);
			SearchAgent agent = new SearchAgent(problem, search);
			
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void PuzzleBFSDemoGreedy(){
		System.out.println("\nPuzzleBFSDemo Greedy:");
		try{
			Problem problem = new Problem(estadoInicial,
					PuzzleBNFunctionFactory.getActionsFunction(), 
					PuzzleBNFunctionFactory.getResultFunction(),
					new PuzzleBNGoalTest(),
					new PuzzleBNStepCostFunction());
			
			Search search = new GreedyBestFirstSearch(new GraphSearch(), hf);
			SearchAgent agent = new SearchAgent(problem, search);
			
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void printInstrumentation(Properties instrumentation) {
		System.out.println("instrumentation:" + instrumentation.toString());
		
	}
	
	private static void printActions(List<Action> actions) {
		Iterator<Action> it = actions.iterator();
		System.out.println("Secuencia de acciones:");
		while(it.hasNext()){
			System.out.println("\t" + it.next());
		}
	}
}
