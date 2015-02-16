package edu.csss2013.cib;

import java.util.List;
import java.util.Set;

/**
 * A Scenario Node wraps a scenario and a balance and is part of a {@link IScenarioNetwork}
 * @author Stephan
 *
 */
public interface IScenarioNode {
	
	/**
	 * Returns the scenario
	 * @return
	 */
	public IScenario getScenario();
	
	/**
	 * Returns the balance
	 * @return
	 */
	public IBalance getBalance();
	
	/**
	 * Returns the nodes from which this node has an incoming connection
	 * @return
	 */
	public List<IScenarioNode> getPrevious();
	
	/**
	 * Returns the incoming
	 * @return
	 */
	public List<IScenarioLink> getIncoming();
	
	
	/**
	 * Returns the nodes to which this node has a outgoing connection
	 * @return
	 */
	public List<IScenarioNode> getNext();
	
	/**
	 * Returns the outgoing link
	 * @return
	 */
	public List<IScenarioLink> getOutgoing();
	
	/**
	 * Returns the index of the node
	 * @return
	 */
	public int getIndex();
	
	public void addIncoming(IScenarioLink link);
	
	public void addOutgoing(IScenarioLink link);
	
	public Set<String> getValueKeys();
	
	public IValue getValue(String key);
	
	public void setValue(String key, IValue value);
	
	

}
