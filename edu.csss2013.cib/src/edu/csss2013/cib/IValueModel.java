package edu.csss2013.cib;

import java.util.Set;

public interface IValueModel {
	
	public Set<String> getValueKeys();
	
	public int getValueCount();
	
	public IValueType getType(String key);

}
