package edu.csss2013.cib.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.ISolver;
import edu.csss2013.cib.IState;

public abstract class AbstractTreeSolver implements ISolver {

	private ScenarioTreeNode tree;
	private ICibMatrix matrix;
	private ScenarioNetwork network;
	private BalanceCalculator calc = new BalanceCalculator();
	private double threshold = 0.01;
	public static enum THRESHOLD_MODE {MAX,MEAN,NONE};
	private THRESHOLD_MODE mode = THRESHOLD_MODE.NONE;


	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}


	@Override
	public ScenarioNetwork solve(ICibMatrix matrix)  {
		try{

			setup(matrix);

			Queue<IDescriptor<?>> q=new LinkedList<IDescriptor<?>>();
			for(IDescriptor<?> f:matrix.getFactors()){
				q.add(f);
			}
			List<IState<?>> d = new ArrayList<IState<?>>(q.size());

			//solve(q,d,matrix);
			Iterator<IScenarioNode> it = network.getNodes();
			while(it.hasNext()){
				IScenarioNode node = it.next();
				//System.out.println(node.getIndex());
				evaluate(node, matrix);
			}
			

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		

		return network;
	}

	private void setup(ICibMatrix matrix) throws IOException{
		this.matrix = matrix;
		network = new ScenarioNetwork(matrix);	

	}

	

	/**
	 * Calculates the balance of a scenario. 
	 * Calls the method to update a given scenario (i.e. find "better" ones).
	 * If the method found new scenarios, these will be connected to input scenario 
	 * @param scenario The scenario to evaluate
	 * @throws IOException 
	 */
	private void evaluate(IScenarioNode node,ICibMatrix matrix) throws IOException{


		//System.out.println("Add scenario "+scenario);
		//ScenarioNode node = network.getNode(scenario);
		ScenarioLinkHint[] hints = 
				calculateUpdatedScenarios(node,matrix);

		double t = Double.MIN_VALUE;
		if(mode.equals(THRESHOLD_MODE.MAX)) t = getMax(hints);
		else if(mode.equals(THRESHOLD_MODE.MEAN)) t = getMean(hints);
		
		double score = 0;
		for(ScenarioLinkHint hint:hints){	
			if(hint.getLinkWeight()>=t){
				score+=hint.getLinkWeight();
			}
		}		
		
		for(ScenarioLinkHint hint:hints){	
			if(hint.getLinkWeight()>=t){
				network.connect(node, hint.getScenario(), hint.getLinkWeight()/score);
			}
		}
		
	}

	private double getMax(ScenarioLinkHint[] hints){
		double m = 0.;
		for(ScenarioLinkHint hint:hints){
			if(hint.getLinkWeight()>m) m=hint.getLinkWeight();
		}
		return m;
	}

	private double getMean(ScenarioLinkHint[] hints){
		double sum = 0.,count=0;
		for(ScenarioLinkHint hint:hints){
			sum+=hint.getLinkWeight();
			count++;
		}
		return sum/count;
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
	protected abstract ScenarioLinkHint[] calculateUpdatedScenarios(IScenarioNode scenario,ICibMatrix matrix);

	public THRESHOLD_MODE getMode() {
		return mode;
	}

	public void setMode(THRESHOLD_MODE mode) {
		this.mode = mode;
	}


}


