package edu.csss2013.cib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IValue;

public class ScenarioTreeNode implements Iterable<ScenarioTreeNode>,IScenarioNode{

	private ScenarioTreeNode parent;
	private List<ScenarioTreeNode> children;
	private IState<?> state = null;
	private boolean isLeaf =false;
	private ScenarioNode scenarioNode;
	private BalanceCalculator calc = new BalanceCalculator();
	private  int id = 0;
	private IDescriptors d;
	private Scenario s;
	private List<IScenarioLink> incoming,outgoing;
	private double degree=0;	
	private Map<String,IValue> values = new HashMap<String,IValue>();


	public ScenarioTreeNode(IDescriptors d,ScenarioNetwork network){
		this.d = d;

		IDescriptor<?> descriptor = d.getDescriptor(0);
		children = new ArrayList<ScenarioTreeNode>(descriptor.getStateCount());
		Stack<ScenarioTreeNode> s = new Stack<ScenarioTreeNode>();
		
		for(int i=0;i<descriptor.getStateCount();i++){
			
			children.add(new ScenarioTreeNode(this, descriptor.getState(i), 1, d,s,network));
		}
	}

	private ScenarioTreeNode(ScenarioTreeNode parent,IState<?> state,int index,IDescriptors d,Stack<ScenarioTreeNode> s,ScenarioNetwork network) {
		this.parent = parent;
		this.state = state;
		s.push(this);
		if(index<d.size()){

			IDescriptor<?> descriptor = d.getDescriptor(index);
			children = new ArrayList<ScenarioTreeNode>(descriptor.getStateCount());
			index = index+1;
			for(int i=0;i<descriptor.getStateCount();i++){
				children.add(new ScenarioTreeNode(this, descriptor.getState(i), index, d,s,network));
			}

		}
		else{
			isLeaf=true;
			this.id = network.writeNode(this);
			incoming = new ArrayList<IScenarioLink>();
			outgoing = new ArrayList<IScenarioLink>();		
			
			
			/*Scenario scenario = new Scenario(d);
			IState<?>[] states = new IState[s.size()];
			int count = 0;
			for(ScenarioTreeNode node:s){
				states[count] = node.getState();
				count++;
			}

			scenario.setDescriptor(states);			
			this.scenarioNode = network.addNode(scenario);*/


		}
		s.pop();
	}

	public ScenarioTreeNode getChild(int index){
		return children.get(index);
	}

	public IState<?> getState(){
		return state;
	}

	public boolean isLeaf(){
		return isLeaf;
	}

	public ScenarioTreeNode getParent(){
		return parent;
	}

	public ScenarioNode getScenarioNode(){
		return scenarioNode;
	}

	@Override
	public String toString() {	
		return state.getName();
	}

	@Override
	public Iterator<ScenarioTreeNode> iterator() {
		return children.iterator();
	}

	public int getChildrenCount(){
		return children.size();
	}

	@Override
	public IScenario getScenario() {
		if(isLeaf()){
			if(this.s == null){
				List<IState<?>> l = new ArrayList<IState<?>>();
				l.add(getState());
				ScenarioTreeNode parent = this;			
				while(parent.getParent()!=null){
					parent = parent.getParent();
					if(parent.getState()!=null) l.add(parent.getState());				
				}

				Collections.reverse(l);	


				s = new Scenario(parent.d);
				s.setDescriptor(l);
			}

			return s;
		}
		return null;
	}

	@Override
	public IBalance getBalance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IScenarioNode> getPrevious() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IScenarioLink> getIncoming() {
		return incoming;
	}

	@Override
	public List<IScenarioNode> getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IScenarioLink> getOutgoing() {
		return outgoing;
	}

	@Override
	public int getIndex() {
		return id;
	}

	@Override
	public void addIncoming(IScenarioLink link) {
		incoming.add(link);
		degree+=link.getWeight();
	}
	
	public double getDegree(){
		return degree;
	}
	
	public void setIndex(int id){
		this.id = id;
	}

	@Override
	public void addOutgoing(IScenarioLink link) {
		outgoing.add(link);		
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
