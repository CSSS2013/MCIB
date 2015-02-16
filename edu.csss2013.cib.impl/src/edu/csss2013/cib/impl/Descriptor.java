package edu.csss2013.cib.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.impl.util.HashCodeGenerator;

public class Descriptor<T> extends CibElement implements IDescriptor<T> {

	private List<IState<T>> states = new ArrayList<IState<T>>();
	
	public Descriptor(String name) {
		super(name);		
	}

	@Override
	public int getStateCount() {
		return states.size();
	}

	@Override
	public IState<T> getState(int index) {
		return states.get(index);
	}

	@Override
	public int indexOf(IState<T> d) {
		return states.indexOf(d);
	}
	
	@XmlElement( name = "descriptor")
	public State[] getDescriptors(){
		return states.toArray(new State[states.size()]);
	}
	
	State<T> makeDescriptor(String name){
		State<T> d = new State<T>(name, this, getStateCount(), null);
		states.add(d);
		return d;
	}
	
	boolean removeState(IState<T> descriptor){
		if(states.contains(descriptor)){
			states.remove(descriptor);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0==this) return true;
		if(arg0 instanceof IDescriptor){
			IDescriptor<?> other = (IDescriptor<?>) arg0;
			if(!other.getName().equals(getName()) && other.getStateCount()!=getStateCount()){
				return false;
			}
			for(int i=0;i<other.getStateCount();i++){
				if(!getState(i).equals(other.getState(i))){
					return false;
				}
			}
			return true;
		}
		return super.equals(arg0);
	}
	
	@Override
	public int hashCode() {		
		return HashCodeGenerator.hashCode(getName(),states);
	}
	
	

}
