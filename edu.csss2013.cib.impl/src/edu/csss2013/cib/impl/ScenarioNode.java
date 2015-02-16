package edu.csss2013.cib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.IValue;

class ScenarioNode implements IScenarioNode{
	
	private List<IScenarioNode> previousScenarios = new ArrayList<IScenarioNode>();
	private List<IScenarioNode> nextScenarios = new ArrayList<IScenarioNode>();
	private List<IScenarioLink> outgoing = new ArrayList<IScenarioLink>();
	private List<IScenarioLink> incoming = new ArrayList<IScenarioLink>();
	private IScenario scenario;
	private IBalance balance;
	private int index;
	private Map<String,IValue> values = new HashMap<String,IValue>();
	
	
	public ScenarioNode(int index, IScenario scenario) {
		this.index = index;
		this.scenario = scenario;			
	}
	
	public ScenarioNode(int index, IScenario scenario,IBalance balance) {
		this.index = index;
		this.scenario = scenario;
		this.balance = balance;		
	}
	
	public void addOutgoing(IScenarioLink link){
		/*if(node.equals(this)){
			throw new IllegalArgumentException("Cannot connect to itself");
		}*/
		previousScenarios.add(link.getStartNode());
		incoming.add(link);
	}
	
	public void addIncoming(IScenarioLink link){
		/*if(node.equals(this)){
			throw new IllegalArgumentException("Cannot connect to itself");
		}*/
		nextScenarios.add(link.getEndNode());
		outgoing.add(link);
	}
	
	public List<IScenarioNode> getPrevious(){
		return Collections.unmodifiableList(previousScenarios);
	}
	
	public List<IScenarioNode> getNext(){
		return Collections.unmodifiableList(nextScenarios);
	}

	@XmlElement( name = "scenario")
	@Override
	public IScenario getScenario() {
		return scenario;
	}
	
	public void setBalance(IBalance b){
		this.balance = b;
	}

	@Override
	public IBalance getBalance() {
		return balance;
	}
	
	@Override
	public String toString() {		
		return "Node("+scenario+")";
	}

	@Override
	public List<IScenarioLink> getIncoming() {
		return Collections.unmodifiableList(incoming);
	}

	@Override
	public List<IScenarioLink> getOutgoing() {
		return Collections.unmodifiableList(outgoing);
	} 
	
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof IScenarioNode){
			return getScenario().equals(((IScenarioNode)arg0).getScenario());
		}
		return super.equals(arg0);
	}
	
	@Override
	public int hashCode() {
		return scenario.hashCode();
	}

	@Override
	public int getIndex() {
		return index;
	}

	

	@Override
	public Set<String> getValueKeys() {
		return values.keySet();
	}

	@Override
	public IValue getValue(String key) {
		return values.get(key);
	}

	@Override
	public void setValue(String key, IValue value) {
		this.values.put(key, value);		
	}

	
	
	
	 

}
