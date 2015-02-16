package edu.csss2013.cib.impl.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.impl.AbstractSolver;
import edu.csss2013.cib.impl.Scenario;
import edu.csss2013.cib.impl.ScenarioLinkHint;

public class LogisticSolver extends AbstractSolver {

	private double beta = 1;
    private double shift=1.0;
		
	public double getShift() {
		return shift;
	}

	public void setShift(double shift) {
		this.shift = shift;
	}

	private List<ScenarioLinkHint> hints = new ArrayList<ScenarioLinkHint>();	
	

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	@Override
	protected ScenarioLinkHint[] calculateUpdatedScenarios(IBalance balance){

		//if(!balance.isConsistent()){
		//			System.out.println("Inconsitent Scenario "+balance.getScenario());						
		
		hints.clear();
		/*(for(IFactor<?> f:balance.getScenario().getFactors()){
			for (int d=0; d<f.getDescriptorCount(); d++){
				balanceSum+=Math.exp(beta*(balance.getValue(f.getDescriptor(d)) ));				
			}  //go through and sum over ALL descriptors to find all possible exists.                          
		}*/

		List<Double> values  = new ArrayList<Double>();
		List<IState[]> descriptors  = new ArrayList<IState[]>();
		
		double sum = 0;
		IState<?>[] original = new IState<?>[balance.getScenario().getFactors().size()];
		int iii=0;
		for(IDescriptor<?> g:balance.getScenario().getFactors()){
			original[iii]= balance.getDescriptor(g);			
			iii++;
		}	
		
		double v = calculate(original, balance);		
		values.add(v);
		sum+=v;
		descriptors.add(original);
		
		for(IDescriptor<?> f:balance.getScenario().getFactors()){			
			
			int currentF = balance.getScenario().getFactors().indexOf(f);
			//System.out.println(f+" "+currentF);
			for (int d=0; d<f.getStateCount(); d++){
				
				if(!f.getState(d).equals(balance.getDescriptor(f))){
					IState<?>[] dArray = Arrays.copyOf(original, original.length);
					dArray[currentF]=f.getState(d);
					v = calculate(dArray, balance);
					values.add(v);					
					sum+=v;
					descriptors.add(dArray);                            
					
				}
			}  
		}
		
		for(int i=0;i<descriptors.size();i++){
			Scenario s = new Scenario(balance.getScenario().getFactors());			
			s.setDescriptor(descriptors.get(i));			
			hints.add(new ScenarioLinkHint(balance.getScenario(),s, values.get(i)/sum));
		}				

		return hints.toArray(new ScenarioLinkHint[hints.size()]);
		//}
		//return null; // no new scenario required ==> "consistent" scenario
	}
	
	private double calculate(IState<?>[] descriptor,IBalance balance){
		double sum = 0;
		for(IState<?> d:descriptor){
			sum += balance.getValue(d)-balance.getCurrentValue(d);			
		}
		return Math.exp(beta*(sum+shift))/(1+Math.exp(beta*(sum+shift)));
	}
	
	
	

	/*@Override
	public void evaluate(IScenario scenario, FactorIterator iterator) {
		double energyLevel = 0;
		for(IFactor<?> f: scenario.getFactors()){
			energyLevel += balance.getValue(scenario.getDescriptor(f));
		}
		/**
	 * TODO check if the following is right
	 */
	/*	double probability = Math.exp(-beta*(balanceSum-energyLevel)); 
		this.hints.add(new ScenarioLinkHint(scenario, probability));
	}*/



}
