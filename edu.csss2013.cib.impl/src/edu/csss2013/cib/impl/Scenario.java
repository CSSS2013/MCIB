package edu.csss2013.cib.impl;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.impl.util.HashCodeGenerator;

public class Scenario implements IScenario {
	
	private Map<IDescriptor<?>, IState<?>> map;	
	private int[] choices;
	private IDescriptors factors;

	public Scenario(IDescriptors factors) {
		map = new HashMap<IDescriptor<?>, IState<?>>();
		this.factors = factors;		
	}	
	
	public void setDescriptor(int[] states){
		choices = new int[factors.size()];
		int i=0;
		for(IDescriptor<?> d:factors){
			//if(factors.contains(d.getFactor())){
				map.put(d, d.getState(states[i]));
				choices[i] = states[i];
				
				i++;
			//}			
		}
		if(i!=choices.length){
			throw new InvalidParameterException("Not all factors were set");
		}
	}
	
	public void setDescriptor(IState<?>[] states){
		choices = new int[factors.size()];
		int i=0;
		for(IState<?> d:states){
			//if(factors.contains(d.getFactor())){
				map.put(d.getFactor(), d);
				choices[i] = d.getChoiceNumber();
				
				i++;
			//}			
		}
		if(i!=choices.length){
			throw new InvalidParameterException("Not all factors were set");
		}
	}
	
	public void setDescriptor(List<IState<?>> states){
		choices = new int[factors.size()];
		int i=0;
		for(IState<?> d:states){
			
				map.put(d.getFactor(), d);
				choices[i] = d.getChoiceNumber();
				i++;
						
		}
		if(i!=choices.length){
			throw new InvalidParameterException("Not all factors were set");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> IState<T> getDescriptor(IDescriptor<T> factor) {		
		return (IState<T>) map.get(factor);
	}

	@Override
	public Iterator<IState<?>> iterator() {
		return map.values().iterator();
	}
	
	@Override
	public int hashCode() {
		Object[] object = new Object[factors.size()];
		//object[0] = factors;
		for(int i=0;i<factors.size();i++){
			object[i] = getDescriptor(factors.getDescriptor(i));
		}
		return HashCodeGenerator.hashCode(new Object[]{object});		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IScenario){
			IScenario other = (IScenario) obj;
			if(!getFactors().equals(other.getFactors())) return false;
			for(IDescriptor<?> f:getFactors()){
				if(!getDescriptor(f).equals(other.getDescriptor(f))){
					return false;
				}				
			}
			return true;
		}
		return super.equals(obj);
	}

	@Override
	public int[] getChoices() {
		return choices;
	}

	@Override
	public IDescriptors getFactors() {
		return factors;
	}
	
	@Override
	public String toString() {
		String text = "";
		for(IDescriptor<?> f: getFactors()){
			text+=getDescriptor(f)+",";
		}
		return text;
	}

}
