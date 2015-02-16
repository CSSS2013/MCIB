package edu.csss2013.cib;


/**
 * The Cib Matrix with the assigned interactions
 * @author Stephan
 *
 */
public interface ICibMatrix {
	
	public int size();
	
	public double getImpact(IState<?> descriptor1, IState<?> descriptor2);
	
	public int getFactorIndex(IDescriptor<?> factor);
	
	public int getDescriptorIndex(IState<?> factor);
	
	public int getIndexOf(ICibElement element);
		
	public IState<?>[] getElements();
	
	public void setImpact(IState<?> one,IState<?> two, double value);
	
	public IDescriptors getFactors();
	
	

}
