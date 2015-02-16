package edu.csss2013.cib.io;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.impl.util.HashCodeGenerator;

public class DynamicNetwork {
	
	private Map<LinkEncouter, TreeMap<Interval, Double>> map = 
			new HashMap<DynamicNetwork.LinkEncouter, TreeMap<Interval,Double>>();
	
	private Map<Integer,TreeMap<Interval,Map<String,Double>>> nodeMap = 
			new HashMap<Integer,TreeMap<Interval,Map<String,Double>>>();
	
	public void addNode(Interval interval,IScenarioNode node,Map<String,Double> properties){
		TreeMap<Interval,Map<String,Double>> tm = nodeMap.get(node.getIndex());
		if(tm==null){
			tm = new TreeMap<Interval, Map<String,Double>>();
			nodeMap.put(node.getIndex(), tm);
		}
		Map<String,Double> proeprties = new HashMap<String, Double>();
		proeprties.putAll(properties);
		tm.put(interval, proeprties);
		
	}
	
	
	
	public void addLink(Interval interval,IScenarioLink link){
		LinkEncouter le = new LinkEncouter();
		le.start=link.getStartNode().getIndex();
		le.end=link.getEndNode().getIndex();
		TreeMap<Interval, Double> localMap = map.get(le);
		if(localMap==null){
			localMap = new TreeMap<Interval, Double>();
			map.put(le, localMap);
		}
		localMap.put(interval, link.getWeight());
	}
	
	public Set<Integer> getNodes(){
		return nodeMap.keySet();
	}
	
	public TreeMap<Interval,Map<String,Double>> getNodeValues(int index){
		return nodeMap.get(index);
	}
	
	public Set<LinkEncouter> getLinks(){
		return map.keySet();
	}
	
	public TreeMap<Interval, Double> getLinkOccurences(LinkEncouter le){
		return map.get(le);
	}
	
	class LinkEncouter{
		int start,end;
		
		@Override
		public int hashCode() {
			return HashCodeGenerator.hashCode(start,end);
		}
		
		@Override
		public boolean equals(Object arg0) {
			if(arg0 instanceof LinkEncouter){
				LinkEncouter e = (LinkEncouter) arg0;
				return e.start==start && e.end==end;
			}
			return super.equals(arg0);
		}
		
		@Override
		public String toString() {
			return start+"->"+end;
		}
		
	}

}
