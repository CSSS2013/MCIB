package edu.csss2013.cib;


/**
 * The Balance of an evaluated Secanrio
 * @author Stephan Lehner
 *
 */
public interface IBalance {
	
	/**
	 * The scenario
	 * @return
	 */
	public IScenario getScenario();
	
	/**
	 * Convenience method that returns whether the scenario is consistent
	 * @return 
	 */
	public boolean isConsistent();
	
	/**
	 * Returns the chosen factor for this descriptor
	 * @param factor
	 * @return
	 */
	public <T> IState<T> getDescriptor(IDescriptor<T> factor);
	
	/**
	 * Returns the calculated score for the factor
	 * @param factor
	 * @return
	 */
	public double getScore(IDescriptor<?> factor);
	
	/**
	 * Returns the best score for a factor
	 * @param factor
	 * @return
	 */
	public double getBestScore(IDescriptor<?> factor);
	
	/**
	 * Returns the best descriptor for a factor
	 * @param factor
	 * @return
	 */
	public <T> IState<T> getBestDescriptor(IDescriptor<T> factor);
	
	/**
	 * Returns whether the chosen scenario hass the best descriptor for a factor
	 * @param factor
	 * @return
	 */
	public boolean isBestDescriptor(IDescriptor<?> factor);
	
	/**
	 * Return the 
	 * @return
	 */
	public <T> double getValue(IState<T> descritptor);
	
	public <T> double getCurrentValue(IState<T> descritptor);
	
	/**
	 * Returns the sum of the selected options
	 * @return
	 */
	public double getBalanceSum();
	
	
	/**
	 * Returns the score that the solver calculated for this scenario
	 * @return
	 */
	public double getScore();
	
	public void setScore(double value);


}
