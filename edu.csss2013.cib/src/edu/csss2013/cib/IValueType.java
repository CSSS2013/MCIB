package edu.csss2013.cib;

public interface IValueType {
	
	public String getName();
	
	public <T> Class<T> getValueClass();

}
