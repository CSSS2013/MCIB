package edu.csss2013.cib.impl;

import java.util.HashMap;
import java.util.Map;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenario;

public class Balance implements IBalance {
		
	private IScenario scenario;	
	private Map<IDescriptor<?>, IState<?>> bestDescriptor;
	private Map<IDescriptor<?>, Double> chosenDescriptorValue;
	private Map<IDescriptor<?>, Double> bestDescriptorValue;
	private Map<IState<?>,Double> factorMap = new HashMap<IState<?>, Double>();
	private double sum = 0;
	private double score = 0;
	
	
	private boolean isConsistent = true;
	
	public Balance(){
		
	}

	public Balance(IScenario scenario){
		setScenario(scenario);		
		
		
		
	}
	
	public void setScenario(IScenario scenario){
		this.scenario = scenario;
		if(bestDescriptor==null){
			bestDescriptor = new HashMap<IDescriptor<?>, IState<?>>(scenario.getFactors().size());
		}
		else{
			bestDescriptor.clear();
		}
		if(bestDescriptorValue==null){
			bestDescriptorValue = new HashMap<IDescriptor<?>, Double>(scenario.getFactors().size());
		}
		else{
			bestDescriptorValue.clear();
		}
		if(chosenDescriptorValue==null){
			chosenDescriptorValue = new HashMap<IDescriptor<?>, Double>(scenario.getFactors().size());
		}
		else{
			chosenDescriptorValue.clear();
		}
		
	}

	@Override
	public IScenario getScenario() {
		return scenario;
	}

	@Override
	public boolean isConsistent() {
		return isConsistent;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IState<T> getDescriptor(IDescriptor<T> factor) {
		return scenario.getDescriptor(factor);		
	}

	@Override
	public double getScore(IDescriptor<?> factor) {		
		return chosenDescriptorValue.get(factor);
	}

	@Override
	public double getBestScore(IDescriptor<?> factor) {
		return bestDescriptorValue.get(factor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> IState<T> getBestDescriptor(IDescriptor<T> factor) {
		return (IState<T>) bestDescriptor.get(factor);
	}

	@Override
	public boolean isBestDescriptor(IDescriptor<?> descriptor) {
		return bestDescriptor.get(descriptor).equals(scenario.getDescriptor(descriptor));
	}
	
	void setChosenDescriptor(IState<?> d, double value){
		IDescriptor<?> factor = d.getFactor();		
		this.chosenDescriptorValue.put(factor, value);
		sum+=value;
		if(bestDescriptor.containsKey(factor)){
			if(bestDescriptorValue.get(factor)<=value){ // TODO What if this is equal?
				bestDescriptor.put(d.getFactor(), d);
				bestDescriptorValue.put(d.getFactor(), value);
			}
			else{
				isConsistent=false;
			}
		}
		else{
			bestDescriptor.put(d.getFactor(), d);
			bestDescriptorValue.put(d.getFactor(), value);
		}
		setDescriptor(d, value);
		
	}
	
	void setNonChosenDescriptor(IState<?> d, double value){
		IDescriptor<?> factor = d.getFactor();
		if(bestDescriptor.containsKey(factor)){
			if(bestDescriptorValue.get(factor)<value){ // TODO What if this is equal?
				bestDescriptor.put(d.getFactor(), d);
				bestDescriptorValue.put(d.getFactor(), value);
				isConsistent=false;
			}
		}
		else{
			bestDescriptor.put(d.getFactor(), d);
			bestDescriptorValue.put(d.getFactor(), value);			
		}
		setDescriptor(d, value);
	}
	
	void setDescriptor(IState<?> d, double value){
		factorMap.put(d, value);
	}
	
	@Override
	public String toString() {		
		String text = "";
		for(IDescriptor<?> f:getScenario().getFactors()){
			text+="("+getDescriptor(f)+"="+getScore(f)+")";
		}
		return text;	
	}

	@Override
	public <T> double getValue(IState<T> descritptor) {
		return factorMap.get(descritptor);
	}
	
	@Override
	public <T> double getCurrentValue(IState<T> descritptor) {
		return factorMap.get(scenario.getDescriptor(descritptor.getFactor()) );
	}

	@Override
	public double getBalanceSum() {
		return sum;
	}

	@Override
	public double getScore() {
		return score;
	}

	@Override
	public void setScore(double value) {
		this.score = value;
	}

	
}
