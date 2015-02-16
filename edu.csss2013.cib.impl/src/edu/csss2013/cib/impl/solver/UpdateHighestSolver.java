package edu.csss2013.cib.impl.solver;

import java.util.Arrays;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.impl.AbstractSolver;
import edu.csss2013.cib.impl.Scenario;
import edu.csss2013.cib.impl.ScenarioLinkHint;

public class UpdateHighestSolver extends AbstractSolver {



	protected ScenarioLinkHint[] calculateUpdatedScenarios(IBalance b){
		System.out.println(b.isConsistent());
		if(!b.isConsistent()){			
			System.out.println("Inconsitent Scenario "+b.getScenario());
			IState<?>[] d = new IState[b.getScenario().getFactors().size()];		
			int i=0;
			IState<?> best = null;
			double diff=0,mydiff=0;
			int counter = -1;
			for(IDescriptor<?> f:b.getScenario().getFactors()){				
				if(!b.getBestDescriptor(f).equals(b.getDescriptor(f))){

					mydiff = b.getBestScore(f)-b.getScore(f);
					if(best == null || mydiff>diff){
						best = b.getBestDescriptor(f);
						diff = mydiff;
						counter = i;
					}					
				}
				d[i] = b.getDescriptor(f);			
				i++;
			}
			if(counter>=0){
				d[counter] = best;			
				Scenario s = new Scenario(b.getScenario().getFactors());
				s.setDescriptor(d);
				return new ScenarioLinkHint[]{new ScenarioLinkHint(b.getScenario(),s,1.)};
			}
		}
		return null; // no new scenario required ==> "consistent" scenario
	}



}
