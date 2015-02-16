package edu.csss2013.cib.impl.solver;

import edu.csss2013.cib.IBalance;
import edu.csss2013.cib.IState;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.impl.AbstractSolver;
import edu.csss2013.cib.impl.Scenario;
import edu.csss2013.cib.impl.ScenarioLinkHint;

public class UpdateAllSolver extends AbstractSolver {


	
	protected ScenarioLinkHint[] calculateUpdatedScenarios(IBalance balance){		
		if(!balance.isConsistent()){			
			IState<?>[] d = new IState[balance.getScenario().getFactors().size()];		
			int i=0;
			for(IDescriptor<?> f:balance.getScenario().getFactors()){
				d[i] = balance.getBestDescriptor(f);			
				i++;
			}		
			Scenario s = new Scenario(balance.getScenario().getFactors());
			s.setDescriptor(d);
			/*if(s.equals(balance.getScenario())){
				System.out.println("Consistent Scenario "+balance.getScenario());
				return null;
			}*/
			return new ScenarioLinkHint[]{new ScenarioLinkHint(balance.getScenario(),s,1.)};
		}	
		System.out.println("Consistent Scenario "+balance.getScenario());
		/*else{
			return new ScenarioLinkHint[]{new ScenarioLinkHint(balance.getScenario(),1.)};
		}*/
		return new ScenarioLinkHint[]{new ScenarioLinkHint(balance.getScenario(),balance.getScenario(),1.)}; // no new scenario required ==> "consistent" scenario
	}

	

}
