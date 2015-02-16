package edu.csss2013.cib;


public interface IDescriptors extends Iterable<IDescriptor<?>>{
	
	public int size();
	public int indexOf(IDescriptor<?> factor);
	public <T> IDescriptor<T> getDescriptor(int index);
	public <T> boolean contains(IDescriptor<T> factors);
	//public int getDescriptorCount();
	/*public <T> void addFactor(IFactor<T> factor);
	public <T> void removeFactor(IFactor<T> factor);*/	

}
