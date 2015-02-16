package edu.csss2013.cib.impl;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;

public class State<T> extends CibElement implements IState<T>{

	private IDescriptor<T> factor;
	private int choiceNumber;
	private T value;

	State(String name, IDescriptor<T> factor, int choiceNumber, T value) {
		super(name);
		this.factor = factor;
		this.choiceNumber = choiceNumber;
		this.value = value;
		
	}

	@Override
	public IDescriptor<?> getFactor() {
		return factor;
	}

	@XmlElement( name = "value")
	@Override
	public T getValue() {
		return value;
	}

	@Override
	public int getChoiceNumber() {
		return choiceNumber;
	}

}
