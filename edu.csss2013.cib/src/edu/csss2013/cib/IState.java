package edu.csss2013.cib;

/**
 * A descriptor represents a possible choice for a factor in a scenario
 * @author Stephan
 *
 * @param <T>
 */
public interface IState<T> extends ICibElement{
	
	/**
	 * The factor that this descriptor belongs to
	 * @return
	 */
	public IDescriptor<?> getFactor();
	
	/**
	 * The value of this descrptor
	 * @return
	 */
	public T getValue();
	
	/**
	 * The choice number is the id of this descrptor in the factor
	 * @return
	 */
	public int getChoiceNumber();

}
