package edu.csss2013.cib.impl;

import java.util.HashMap;
import java.util.Map;

import edu.csss2013.cib.ICibElement;
import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;

public class CibMatrix implements ICibMatrix {
	
	private Map<IState<?>, Map<IState<?>, Double>> map = 
			new HashMap<IState<?>, Map<IState<?>,Double>>();
	private DescriptorCollection descriptors;
	
	public CibMatrix(DescriptorCollection factors){
		this.descriptors = factors;
	}

	@Override
	public int size() {
		return descriptors.getStates().size();
	}

	@Override
	public double getImpact(IState<?> descriptor1,
			IState<?> descriptor2) {
		if(map.containsKey(descriptor1)){
			Map<IState<?>,Double> m = map.get(descriptor1);
			if(m.containsKey(descriptor2)){
				return m.get(descriptor2);
			}
		}
		return 0;
		
	}

	@Override
	public int getFactorIndex(IDescriptor<?> factor) {
		return descriptors.indexOf(factor);
	}

	@Override
	public int getDescriptorIndex(IState<?> descriptor) {
		if(descriptors.contains(descriptor.getFactor())){
			int count = 0;
			for(int i=0;i<descriptors.indexOf(descriptor.getFactor());i++){
				count+=descriptors.getDescriptor(i).getStateCount();
			}
			return count + descriptor.getChoiceNumber();
			
		}
		return -1;
	}

	@Override
	public int getIndexOf(ICibElement element) {
		if(element instanceof IDescriptor){
			return getFactorIndex((IDescriptor<?>) element);
		}
		if(element instanceof IState){
			return getDescriptorIndex((IState<?>) element);
		}
		return -1;
	}

	@Override
	public IDescriptors getFactors() {
		return descriptors;
	}

	@Override
	public IState<?>[] getElements() {
		return descriptors.getStates().toArray(new IState[descriptors.getStates().size()]);
	}	
	
	public void setImpact(IState<?> one,IState<?> two, double value){
		Map<IState<?>,Double> myMap;
		if(!map.containsKey(one)){
			myMap = new HashMap<IState<?>, Double>();
			map.put(one, myMap);
		}
		else{
			myMap = map.get(one);
		}
		myMap.put(two, value);
	}

}
