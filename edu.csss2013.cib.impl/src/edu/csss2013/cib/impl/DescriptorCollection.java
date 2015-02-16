package edu.csss2013.cib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;

public class DescriptorCollection implements IDescriptors{
	
	private List<IDescriptor<?>> factors = new ArrayList<IDescriptor<?>>();
	private List<IState<?>> states = new ArrayList<IState<?>>();
	

	@Override
	public Iterator<IDescriptor<?>> iterator() {
		return factors.iterator();
	}

	@Override
	public int size() {
		return factors.size();
	}

	@Override
	public int indexOf(IDescriptor<?> factor) {
		return factors.indexOf(factor);
	}

	@Override
	public <T> IDescriptor<T> getDescriptor(int index) {
		return (IDescriptor<T>) factors.get(index);
	}

	@XmlElement( name = "factor")
	public Descriptor[] getDescriptors(){
		return factors.toArray(new Descriptor[factors.size()]);
	}
	
	@Override
	public <T> boolean contains(IDescriptor<T> factor) {
		return factors.contains(factor);
	}
	
	public <T> Descriptor<T> makeFactor(String name){
		Descriptor<T> f = new Descriptor<T>(name);
		factors.add(f);		
		return f;
	}
	
	public <T> State<T> makeDescriptor(IDescriptor<T> factor,String name){
		
		if(factors.contains(factor)){
			State<T> d = ((Descriptor) factor).makeDescriptor(name);
			if(states.size()==0){
				states.add(d);
			}			
			else if(factor.getStateCount()>1){				
				int index = states.indexOf(factor.getState(factor.getStateCount()-2));				
				if(index==states.size()-1){
					states.add(d);
				}
				else{					
					states.add(index+1, d);
				}
			}
			else{
				int index = factors.indexOf(factor)-1;
				while(index>=0){
					IDescriptor<?> f = factors.get(index);
					if(f.getStateCount()>0){
						int ind = states.indexOf(f.getState(f.getStateCount()-1));
						if(ind == states.size()-1){
							states.add(d);
						}
						else{
							states.add(ind+1, d);
						}
						break;
					}
					else{
						index--;
					}
				}
				if(index==-1){
					states.add(d);
				}
			}			
			return d;
		}
		return null;
	}
	
	public <T> boolean removeDescriptor(IDescriptor<T> f){
		if(factors.contains(f)){
			factors.remove(f);
			for(int i = 0;i<f.getStateCount();i++){
				IState<?> d = f.getState(i);
				this.states.remove(d);
			}
			return true;
		}
		return false;
	}
	
	public <T> boolean removeState(IState<T> d){
		if(factors.contains(d.getFactor())){	
			Descriptor<T> factor = (Descriptor<T>) factors.get(factors.indexOf(d.getFactor()));
			this.states.remove(d);
			return factor.removeState(d);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return factors.toString();
	}

	
	List<IState<?>> getStates() {
		return Collections.unmodifiableList(states);
	}

}
