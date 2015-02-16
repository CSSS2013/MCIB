package edu.csss2013.cib.impl;

import java.util.Iterator;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IScenarioNode;

public class NodeIterator implements Iterator<IScenarioNode>{

	private ScenarioTreeNode current,next=null;


	int[] nodesID;
	private ICibMatrix matrix;
	private ScenarioTreeNode treeNodes;

	NodeIterator(ScenarioTreeNode treeNodes, ICibMatrix matrix) {
		this.matrix = matrix;
		this.treeNodes = treeNodes;
		init();
	}

	private void init(){

		nodesID = new int[matrix.getFactors().size()];
		for(int i=0;i<nodesID.length;i++){				
			nodesID[i] = 0;
		}
		next = calculateNext(nodesID);			

	}

	@Override
	public boolean hasNext() {
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
