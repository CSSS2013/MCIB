package edu.csss2013.cib.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IDescriptors;
import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.IValueModel;

public class ScenarioNetwork implements IScenarioNetwork{

	//private List<ScenarioNode> nodes = 	new ArrayList<ScenarioNode>(15000000);
	private List<ScenarioLink> links = 	new ArrayList<ScenarioLink>(10000000);
	private BalanceCalculator calc = new BalanceCalculator();
	private double threshold = 0;

	private List<ScenarioNode> consistent =	new ArrayList<ScenarioNode>();	
	private int indexCounter = 0;
	private int linkIndexCounter = 0;
	private ScenarioTreeNode treeNodes;
	private ICibMatrix matrix;
	private BufferedWriter nodeWriter;
	private BufferedWriter linkWriter;
	private Map<Integer,Double> inWeightMap = new HashMap<Integer,Double>();
	private int nNodes = 100000;
	private Map<Integer,IScenarioNode> nodes = new HashMap<Integer, IScenarioNode>();
	private BalanceCalculator bc = new BalanceCalculator();


	public ScenarioNetwork(ICibMatrix matrix){
		this.matrix = matrix;		
		this.treeNodes = new ScenarioTreeNode(matrix.getFactors(), this);		
	}
	
	

	

	
	/*public List<ScenarioNode> getConsistentScenarios() {		
		return Collections.unmodifiableList(consistent);
	}*/

	@XmlElement( name = "node")
	@Override
	public Iterator<IScenarioNode> getNodes() {
		return new ScenarioIterator(matrix.getFactors());
	}	

	@XmlElement( name = "link")
	@Override
	public Iterator<? extends IScenarioLink> getLinks() {
		return null;//Collections.unmodifiableList(links);
	}

	public ScenarioNode addNode(IScenario scenario, IBalance balance){
		ScenarioNode node = new ScenarioNode(indexCounter,scenario, balance);
		System.out.println("Adding node "+indexCounter);
		writeNode(node);
		return node;
	}

	public int writeNode(IScenarioNode node){
		nodes.put(indexCounter, node);		
		indexCounter++;
		
		return indexCounter-1;

	}

	public ScenarioNode addNode(Scenario scenario){
		return this.addNode(scenario, null);//calc.calculate(scenario, matrix)); //calc.calculate(scenario, matrix)
	}	

	void connect(IScenarioNode firstNode,IScenarioNode secondNode,double weight) throws IOException{
		if(weight>threshold){
			ScenarioLink l = new ScenarioLink(firstNode, secondNode,weight);		
			firstNode.addOutgoing(l);
			secondNode.addIncoming(l);			

			linkIndexCounter++;
			if(linkIndexCounter%1E6==0) System.out.println(linkIndexCounter);
		}
		// TODO Add leaf tracking
	}

	/*void connect(ScenarioNode firstNode,IScenario scenario){
		connect(firstNode, getNode(scenario));
	}*/	

	void connect(IScenarioNode firstNode,IScenario scenario,double weight) throws IOException{
		if(weight>threshold){			
			connect(firstNode, getNode(scenario),weight);
		}
	}

	void connect(IScenario firstNode,IScenario scenario,double weight) throws IOException{
		if(weight>threshold){
			connect(getNode(firstNode), getNode(scenario),weight);
		}
	}

	boolean isConnected(Scenario s1,Scenario s2){
		return links.contains(new ScenarioLink(new ScenarioNode(0,s1, null), new ScenarioNode(0,s2, null)));
	}

	public IScenarioNode getNode(IScenario scenario){
		int[] choices = scenario.getChoices();
		ScenarioTreeNode current = treeNodes;
		int i = 0;		
		while(!current.isLeaf()){			
			current = current.getChild(choices[i]);
			i++;
		}
		return current;
	}

	@Override
	public IScenarioNode getNode(int id) {
		return null;
	}

	

	class NodeIterator implements Iterator<ScenarioNode>{

		ScenarioTreeNode current,next;

		int[] nodesID;

		public NodeIterator() {

		}

		@Override
		public boolean hasNext() {
			if(current==null){				
				nodesID = new int[matrix.getFactors().size()];
				for(int i=0;i<nodesID.length;i++){				
					nodesID[i] = 0;
				}
				next = calculateNext(nodesID);
				return next!=null;
			}
			int indicator = nodesID.length-1;	

			while(indicator>=0 && matrix.getFactors().getDescriptor(indicator).getStateCount()-1==nodesID[indicator]){
				indicator--;
			}
			if(indicator<nodesID.length-1){
				if(indicator<0){
					return false;
				}
				for(int i=indicator+1;i<nodesID.length;i++){
					nodesID[i] = 0;

				}
			}
			nodesID[indicator] = nodesID[indicator]+1;
			next = calculateNext(nodesID);

			return next!=null;
		}

		private ScenarioTreeNode calculateNext(int[] choices){
			ScenarioTreeNode node = treeNodes;
			for(int c:choices){
				node = node.getChild(c);
			}
			return node;
		}

		@Override
		public ScenarioNode next() {
			current = next;

			return current.getScenarioNode();
		}

		@Override
		public void remove() {
			// not implemented

		}

	}

	class ScenarioIterator implements Iterator<IScenarioNode>{

		private ScenarioTreeNode current,next;
		private Scenario scenario;

		int[] nodesID;

		public ScenarioIterator(IDescriptors factors) {
			this.scenario = new Scenario(factors);
		}

		@Override
		public boolean hasNext() {
			if(current==null){				
				nodesID = new int[matrix.getFactors().size()];
				for(int i=0;i<nodesID.length;i++){				
					nodesID[i] = 0;
				}
				next = calculateNext(nodesID);
				return next!=null;
			}
			int indicator = nodesID.length-1;	

			while(indicator>=0 && matrix.getFactors().getDescriptor(indicator).getStateCount()-1==nodesID[indicator]){
				indicator--;
			}
			if(indicator<nodesID.length-1){
				if(indicator<0){
					return false;
				}
				for(int i=indicator+1;i<nodesID.length;i++){
					nodesID[i] = 0;

				}
			}
			nodesID[indicator] = nodesID[indicator]+1;
			next = calculateNext(nodesID);

			return next!=null;
		}

		private ScenarioTreeNode calculateNext(int[] choices){
			ScenarioTreeNode node = treeNodes;
			for(int c:choices){
				node = node.getChild(c);
			}
			return node;
		}

		@Override
		public ScenarioTreeNode next() {
			current = next;			
			return current;
		}

		@Override
		public void remove() {
			// not implemented

		}

	}

	@Override
	public int getNodeCount() {
		return nodes.size();
	}






	@Override
	public IValueModel getValueModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
