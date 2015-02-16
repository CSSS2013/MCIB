package edu.csss2013.cib;

import java.util.Iterator;

/**
 * A secneario network represents a directed network that illustrates connected scenarios
 * @author Stephan
 *
 */
public interface IScenarioNetwork {
	
		
	/**
	 * Returns the nodes
	 * @return
	 */
	public Iterator<? extends IScenarioNode> getNodes();
	
	/**
	 * Returns the links
	 * @return
	 */
	public Iterator<? extends IScenarioLink> getLinks();
	
	/**
	 * Returns the node with the given id
	 * @param id
	 * @return
	 */
	public IScenarioNode getNode(int id);
	
	public int getNodeCount();
	
	public IValueModel getValueModel();
	
	

}
