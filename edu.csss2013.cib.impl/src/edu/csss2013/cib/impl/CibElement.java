package edu.csss2013.cib.impl;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.ICibElement;

public abstract class CibElement implements ICibElement {

	private String name;

	public CibElement(String name){
		this.name = name;
	}
	
	@XmlElement( name = "name")
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
