package edu.csss2013.cib;

/**
 * A solver calculates an {@link IScenarioNetwork} for a given {@link ICibMatrix}
 * @author Stephan
 *
 */
public interface ISolver {
	
	/**
	 * For a given Cross-Impact Balance Matrix, this Solver evaluates a network of scenarios
	 * @param matrix
	 * @return
	 */
	public IScenarioNetwork solve(ICibMatrix matrix);

}
