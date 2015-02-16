package edu.csss2013.cib.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.ISolver;

public abstract class AbstractSolver implements ISolver {

	private Set<IScenario> encountered = new HashSet<IScenario>();
	private ICibMatrix matrix;
	private ScenarioNetwork network;
	private BalanceCalculator calc = new BalanceCalculator();


	@Override
	public IScenarioNetwork solve(ICibMatrix matrix) {
		setup(matrix);

		/*Queue<IDescriptor<?>> q=new LinkedList<IDescriptor<?>>();
		for(IDescriptor<?> f:matrix.getFactors()){
			q.add(f);
		}
		List<IState<?>> d = new ArrayList<IState<?>>(q.size());*/

		try {
			solve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return network;
	}

	private void setup(ICibMatrix matrix){
		this.matrix = matrix;
		network = new ScenarioNetwork(matrix);
		encountered = new HashSet<IScenario>(matrix.size()*matrix.size());
	}

	/**
	 * This essentially iterates through the CIB matrix. 
	 * This is done by using a queue that holds the factors that still need to be visitied before a 
	 * valid (i.e. descriptors are set for all factors) emerges. The list of currently chosen 
	 * descriptors is saved in a list. Both are constantly updated to represented the current position.
	 * If the last factor columns of the CIB are encountered, then the scenario is complete and the 
	 * evaluate method is called. 
	 * If the current position is not in the last column, then the solver method is recursively called.	 
	 * @param it
	 * @param descriptors
	 * @throws IOException 
	 */
	/*private void solve(Queue<IDescriptor<?>> it,List<IState<?>> descriptors) throws IOException{

		IDescriptor<?> factor = it.poll();
		IState<?> d;
		for(int i=0;i<factor.getStateCount();i++){
			d=factor.getState(i);
			descriptors.add(d);
			if(it.isEmpty()){ // all factors are set -> scenario complete
				Scenario s = new Scenario(matrix.getFactors());
				s.setDescriptor(descriptors.toArray(new IState[descriptors.size()]));
				evaluate(s);				
			}
			else{ // not all factors set, scenario incomplete, add more factors
				solve(it,descriptors);
			}
			descriptors.remove(d);
		}
		it.add(factor);

	}*/
	
	private void solve() throws IOException{
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			IBalance b = calc.calculate(node.getScenario(), matrix);
			ScenarioLinkHint[] hints = calculateUpdatedScenarios(b);
			
			for(ScenarioLinkHint hint:hints){
				network.connect(node, hint.getScenario(),hint.getLinkWeight());
			}
		}
		

	}

	/**
	 * Calculates the balance of a scenario. 
	 * Calls the method to update a given scenario (i.e. find "better" ones).
	 * If the method found new scenarios, these will be connected to input scenario 
	 * @param scenario The scenario to evaluate
	 * @throws IOException 
	 */
	private void evaluate(Scenario scenario) throws IOException{
		if(!encountered.contains(scenario)){
			IBalance b = calc.calculate(scenario, matrix);
			IScenarioNode node = network.getNode(scenario);//.addNode(scenario, b);			
			//System.out.println("Add scenario "+scenario);
			encountered.add(scenario);		
			ScenarioLinkHint[] scenarios = 
					calculateUpdatedScenarios(b);
			if(scenarios!=null){
				connect(node, scenarios);
			}			
		}
	}

	void connect(IScenarioNode node,ScenarioLinkHint... hints) throws IOException{		
		List<IScenarioNode> newNodes = new ArrayList<IScenarioNode>(hints.length);
		for(ScenarioLinkHint hint:hints){
			IScenario s = hint.getScenario();

			if(encountered.contains(s)){ // Node already exists
				network.connect(node, hint.getScenario(),hint.getLinkWeight());				
			}
			else{
				Balance b = calc.calculate(s, matrix);
				IScenarioNode newNode = network.getNode(s);				
				//System.out.println("Adding node "+s);
				network.connect(node, newNode,hint.getLinkWeight());
				//System.out.println("Connecting "+node.getScenario()+" to "+newNode.getScenario());
				encountered.add(hint.getScenario());
				newNodes.add(newNode);
			}
		}
		for(IScenarioNode newNode:newNodes){
			Balance b = calc.calculate(newNode.getScenario(), matrix);
			ScenarioLinkHint[] scenarios = 
					calculateUpdatedScenarios(b);
			if(scenarios!=null){
				connect(newNode, scenarios);
			}
		}


	}

	/*void connect(ScenarioNode node,ScenarioLinkHint... hints){
		List<ScenarioNode> newNodes = new ArrayList<>(hints.length);
		for(ScenarioLinkHint hint:hints){
			IScenario s = hint.getScenario();
			if(encountered.contains(s)){ // Node already exists
				//System.out.println("Connecting "+node.getScenario()+" to "+hint);
				network.connect(node, hint.getScenario(),hint.getLinkWeight());
			}
			else{
				IBalance b = calc.calculate(s, matrix);
				ScenarioNode newNode = network.addNode(s, b);				
				//System.out.println("Adding node "+s);
				network.connect(node, newNode);
				//System.out.println("Connecting "+node.getScenario()+" to "+newNode.getScenario());
				encountered.add(hint.getScenario());
				newNodes.add(newNode);
				ScenarioLinkHint[] scenarios = 
						calculateUpdatedScenarios(newNode.getBalance());
				if(scenarios!=null){
					connect(newNode, scenarios);
				}
			}
		}		
	}*/



	/**
	 * This method defines the logic of the solver.
	 * The method is called for each evaluated scenario. The input is the generated node that includes
	 * information of the scenario and the calculated balance.
	 * The purpose of this method is to decide how the balance will be used to decide to which other
	 * scenario nodes this node links.
	 * If there is a need to connect the scenario node to other scenarios, this method returns all these scenarios.
	 * If not, the method returns null.
	 * @param node
	 * @return 
	 */
	protected abstract ScenarioLinkHint[] calculateUpdatedScenarios(IBalance balance);


}


