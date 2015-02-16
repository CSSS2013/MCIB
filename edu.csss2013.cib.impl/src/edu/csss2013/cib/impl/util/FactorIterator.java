package edu.csss2013.cib.impl.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.impl.Scenario;

public class FactorIterator {
	
	private Set<IScenario> encountered = new HashSet<IScenario>();
	private IFactorEvaluator evaluator;
	
	public void iterate(IDescriptors collection,IFactorEvaluator evaluator){
		encountered = new HashSet<IScenario>();
		this.evaluator = evaluator;
		
		Queue<IDescriptor<?>> q=new LinkedList<IDescriptor<?>>();
		for(IDescriptor<?> f:collection){
			q.add(f);
		}
		List<IState<?>> d = new ArrayList<IState<?>>(q.size());
		solve(q,d,collection);
	}
	
	/**
	 * This essentially iterates through the CIB matrix. 
	 * This is done by using a queue that holds the factors that still need to be visitied before a 
	 * valid (i.e. descriptors are set for all factors) emerges. The list of currently chosen 
	 * descriptors is saved in a list. Both are constantly updated to represented the current position.
	 * If the last factor columns of the CIB are encountered, then the scenario is complete and the 
	 * evaluate method is called. 
	 * If the current position is not in the last column, then the solver method is recursively called.
	 * in the CIB matrix 
	 * @param it
	 * @param descriptors
	 */
	private void solve(Queue<IDescriptor<?>> it,List<IState<?>> descriptors,IDescriptors collection){

		IDescriptor<?> factor = it.poll();
		IState<?> d;
		for(int i=0;i<factor.getStateCount();i++){
			d=factor.getState(i);
			descriptors.add(d);
			if(it.isEmpty()){ // all factors are set -> scenario complete
				Scenario s = new Scenario(collection);
				s.setDescriptor(descriptors.toArray(new IState[descriptors.size()]));
				evaluator.evaluate(s,this);				
			}
			else{ // not all factors set, scenario incomplete, add more factors
				solve(it,descriptors,collection);
			}
			descriptors.remove(d);
		}
		it.add(factor);
	}
	
	public void setEncountered(IScenario scenario){
		encountered.add(scenario);
	}

}
