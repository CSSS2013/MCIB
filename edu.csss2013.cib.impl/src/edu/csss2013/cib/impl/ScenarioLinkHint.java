package edu.csss2013.cib.impl;

import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioNode;

/**
 * A ScenarioLinkHint contains information on the target scenario and the 
 * weight of the link that connects to this scenario.
 * It is used by the {@link AbstractSolver} to define which scenarios to connect
 * @author Stephan
 *
 */
public class ScenarioLinkHint {
	
	private IScenario scenario;
	private double weight;
	private IScenario source;
	
	/**
	 * 
	 * @param targetScenario The target scenario to whcih the current scenario will be connected
	 * @param weight The weight of the connection
	 */
	public ScenarioLinkHint(IScenario source, IScenario targetScenario, double weight){
		this.scenario = targetScenario;
		this.weight = weight;
		this.source = source;
	}
	
	public IScenario getScenario(){
		return scenario;
	}
	
	public double getLinkWeight(){
		return weight;
	}
	
	
	
	public IScenario getSourceNode() {
		return source;
	}
	
	@Override
	public String toString() {
		return scenario.toString()+" with "+getLinkWeight();
	}

		

}
