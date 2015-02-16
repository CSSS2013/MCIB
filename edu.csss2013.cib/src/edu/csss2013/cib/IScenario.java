package edu.csss2013.cib;

/**
 * A scenario is a set of factors with a chosen descriptor
 * @author Stephan
 *
 */
public interface IScenario extends Iterable<IState<?>> {
	
	/**
	 * Returns the chosen descriptor for a factor, or null if no such factor exists
	 * @param factor
	 * @return
	 */
	public <T> IState<T> getDescriptor(IDescriptor<T> factor);
	
	/**
	 * Returns the choise ids in the order of the factors
	 * @return
	 */
	public int[] getChoices();
	
	/**
	 * Returns all factors of this scenario
	 * @return
	 */
	public IDescriptors getFactors();

}
