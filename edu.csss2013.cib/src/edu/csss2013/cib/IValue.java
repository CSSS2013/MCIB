package edu.csss2013.cib;

public interface IValue {
	
	public IValueType getType();
	
	public <T> T getValue();
	
	public <T> T getValueAs(Class<T> clazz);

}
