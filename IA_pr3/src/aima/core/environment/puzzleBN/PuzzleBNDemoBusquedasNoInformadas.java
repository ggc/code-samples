package aima.core.environment.puzzleBN;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import aima.core.agent.Action;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.uninformed.*;

public class PuzzleBNDemoBusquedasNoInformadas {
	static EstadoPuzzleBN estadoInicial = new EstadoPuzzleBN();
	public static void main(String[] args){
		PuzzleBFSDemoBread();
		//PuzzleBFSDemoBidirectional();
		PuzzleBFSDemoDepth();
		PuzzleBFSDemoUniCost();
	}
	private static void PuzzleBFSDemoBread(){
		System.out.println("\nPuzzleBFSDemo BreadFirst--->");
		try{
			Problem problem = new Problem(estadoInicial,
					PuzzleBNFunctionFactory.getActionsFunction(), 
					PuzzleBNFunctionFactory.getResultFunction(),
					new PuzzleBNGoalTest(),
					new PuzzleBNStepCostFunction()); 
			Search search = new BreadthFirstSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void PuzzleBFSDemoBidirectional(){
		System.out.println("\nPuzzleBFSDemo Bidirectional--->");
		try{
			Problem problem = new Problem(estadoInicial,
					PuzzleBNFunctionFactory.getActionsFunction(), 
					PuzzleBNFunctionFactory.getResultFunction(),
					new PuzzleBNGoalTest()); 
			Search search = new BidirectionalSearch();
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}private static void PuzzleBFSDemoDepth(){
		System.out.println("\nPuzzleBFSDemo Depth limited--->");
		try{
			Problem problem = new Problem(estadoInicial,
					PuzzleBNFunctionFactory.getActionsFunction(), 
					PuzzleBNFunctionFactory.getResultFunction(),
					new PuzzleBNGoalTest(),
					new PuzzleBNStepCostFunction()); 
			Search search = new DepthLimitedSearch(27);
			SearchAgent agent = new SearchAgent(problem, search);
			printActions(agent.getActions());
			printInstrumentation(agent.getInstrumentation());
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}private static void PuzzleBFSDemoUniCost(){
		System.out.println("\nPuzzleBFSDemo Uniform Cost--->");
		try{
			Problem problem = new Problem(estadoInicial,
					PuzzleBNFunctionFactory.getActionsFunction(), 
					PuzzleBNFunctionFactory.getResultFunction(),
					new PuzzleBNGoalTest(),
					new PuzzleBNStepCostFunction()); 
			Search search = new UniformCostSearch();
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
