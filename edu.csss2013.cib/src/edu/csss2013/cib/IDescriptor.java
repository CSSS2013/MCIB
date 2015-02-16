package edu.csss2013.cib;

/**
 * A factor is a specific characteristic of a scenario
 * @author Stephan
 *
 * @param <T>
 */
public interface IDescriptor<T> extends ICibElement{	
	
	/**
	 * Returns the number of descritpors that this factor has
	 * @return
	 */
	public int getStateCount();
	
	/**
	 * Returns the descritpro for the given if
	 * @param index
	 * @return
	 */
	public IState<T> getState(int index);
	
	/**
	 * Returns the index of a given descriptor, or -1 if it is not a valid descritpor for this factor
	 * @param index
	 * @return
	 */
	public int indexOf(IState<T> index);

}
