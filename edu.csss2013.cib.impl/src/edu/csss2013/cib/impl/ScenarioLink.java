package edu.csss2013.cib.impl;

import java.util.Arrays;
import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.impl.util.HashCodeGenerator;

public class ScenarioLink implements IScenarioLink{

	private IScenarioNode startNode;
	private IScenarioNode endNode;
	private double weight = 1.;
	

	public ScenarioLink(IScenarioNode startNode,IScenarioNode endNode){
		this(startNode, endNode, 1.);
	}
	
	public ScenarioLink(IScenarioNode startNode,IScenarioNode endNode,double weight){
		this.startNode = startNode;
		this.endNode = endNode;
		this.weight = weight;
	}
	
	
	@Override
	public IScenarioNode getStartNode() {
		return startNode;
	}

	@Override
	public IScenarioNode getEndNode() {
		return endNode;
	}

	@Override
	public double getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return getStartNode()+"->"+getEndNode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof IScenarioLink){
			IScenarioLink other = (IScenarioLink) obj;
			return other.getStartNode().equals(getStartNode()) && other.getEndNode().equals(getEndNode());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return HashCodeGenerator.hashCode(getStartNode(),getEndNode());
	}

}
