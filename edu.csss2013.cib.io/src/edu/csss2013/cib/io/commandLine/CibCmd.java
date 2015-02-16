package edu.csss2013.cib.io.commandLine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import edu.csss2013.cib.ICibMatrix;
import edu.csss2013.cib.IDescriptor;
import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.impl.AbstractTreeSolver.THRESHOLD_MODE;
import edu.csss2013.cib.impl.Balance;
import edu.csss2013.cib.impl.BalanceCalculator;
import edu.csss2013.cib.impl.ScenarioNetwork;
import edu.csss2013.cib.impl.solver.AdiabaticSolver;
import edu.csss2013.cib.impl.solver.ArctanSolver2;
import edu.csss2013.cib.impl.solver.BoltzmannSolver;
import edu.csss2013.cib.impl.solver.LogisticSolver2;
import edu.csss2013.cib.impl.solver.UpdateAllSolver;
import edu.csss2013.cib.io.DynamicGEFXWriter;
import edu.csss2013.cib.io.DynamicNetwork;
import edu.csss2013.cib.io.Eigenvector;
import edu.csss2013.cib.io.Interval;
import edu.csss2013.cib.io.ScenarioWizardReader;
import edu.csss2013.cib.io.SimpleCSVWriter;


public class CibCmd {

	public static void main(String[] args) throws IOException, XMLStreamException {
		
		Eigenvector ev = new Eigenvector();

		/*Scenario scenario = new Scenario(matrix.getFactors());
		scenario.setDescriptor(new int[]{1, 1, 1, 1, 1, 0, 0, 1});*/

		
		//solver.setThreshold(1);
		double[] betas = {0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,3,4,5,6,7,8,9,10};
		//double[] betas = {100};
		System.out.println("Beta,Entropy,RelEntropy,EntropyProduction,RelEntropyProduction");
		TreeMap<Double, List<IScenarioLink>> linkList = new TreeMap<Double, List<IScenarioLink>>();
		IScenarioNetwork net = null;
		DynamicNetwork dynNet = new DynamicNetwork();
		int count = -1;
		for(double beta:betas){
			//File file = new File("D:\\STUDENTEN\\LIANG\\Daten\\Schweizer\\Schweizer_Kriegler_2012_SRES.scw");
			//File file = new File("D:\\Scenario\\TFSC\\ThreeDescriptor.scw");
			File file = new File("C:\\Forschung\\CIB\\ThreeDescriptor\\ThreeDescriptor.scw");
			ScenarioWizardReader s = new ScenarioWizardReader();
			ICibMatrix matrix = s.read(file);
			
			BoltzmannSolver solver = new BoltzmannSolver();
			solver.setMode(THRESHOLD_MODE.NONE);
			solver.setBeta(beta);
			//System.out.println("Solving");
			net = solver.solve(matrix);
			double[] eigenvector= ev.calculate(net);
			Interval interval;
			if(count==-1){
				interval = new Interval(0, beta);
			}
			else{
				interval = new Interval(betas[count], beta);
			}
			
			Iterator<? extends IScenarioNode> it = net.getNodes();
			Map<String,Double> nodeProperty = new HashMap<String,Double>();
			double sum = 0;
			for(int i=0;i<eigenvector.length;i++){
				sum+=eigenvector[i];
			}
			for(int i=0;i<eigenvector.length;i++){
				eigenvector[i]/=sum;			
			}
			while(it.hasNext()){
				IScenarioNode node = it.next();
				for(IScenarioLink link : node.getOutgoing()){
					dynNet.addLink(interval, link);
				}
				nodeProperty.clear();
				nodeProperty.put("Consistency", calculateConsistency(node, matrix));
				nodeProperty.put("SelfLink", calculateSelfLink(node));
				nodeProperty.put("Eigenvector Centrality", eigenvector[node.getIndex()]);
				dynNet.addNode(interval, node, nodeProperty);
			}
			
			//System.out.print(beta+",");
			//printEntropy(matrix, net);
			
			
			
			SimpleCSVWriter writer = new  SimpleCSVWriter();
			System.out.print(beta+",");			
			writer.write(net,matrix,String.valueOf(beta),new File("C:\\Forschung\\CIB\\ThreeDescriptor\\"));
			//System.out.println("Solving done");
			/*JungWrapper j = new JungWrapper(net);
			EigenvectorCentrality<IScenarioNode, IScenarioLink> evc = new EigenvectorCentrality<IScenarioNode, IScenarioLink>(j);
			evc.setTolerance(1E-6);
			evc.setMaxIterations(500);
			evc.initialize();
			evc.evaluate();
			Eigenvector ev = new Eigenvector();
			double[] d = ev.calculate(net);
			Iterator<IScenarioNode> it = net.getNodes();
			IScenarioNode node;
			BufferedWriter myWriter = new BufferedWriter(new FileWriter("J.dat"));
			while(it.hasNext()){
				node = it.next();
				//System.out.println(node.getIndex()+","+d[node.getIndex()]);
				myWriter.write(node.getIndex()+","+d[node.getIndex()]+","+evc.getVertexScore(node));
				myWriter.newLine();
			}
			myWriter.close();
			System.out.println(evc.getIterations());*/
			count++;
		}
		List<IScenarioNode> nodeList = new ArrayList<IScenarioNode>();
		Iterator<? extends IScenarioNode> it = net.getNodes();
		while(it.hasNext()){
			nodeList.add(it.next());
		}
		DynamicGEFXWriter dyn = new DynamicGEFXWriter();
		dyn.write(new File("Dynamic.gexf"), dynNet,betas[0],betas[betas.length-1]);
		/*File nodeFile = new File("D:\\STUDENTEN\\LIANG\\Daten\\nodes.csv");
		File edgeFile = new File("D:\\STUDENTEN\\LIANG\\Daten\\edges.csv");
		CSVWriter writer = new CSVWriter(nodeFile, edgeFile);
		writer.write(net);*/

	}
	
	public static double calculateSelfLink(IScenarioNode node){
		for(IScenarioLink l:node.getOutgoing()){
			if(l.getEndNode().equals(node)){
				return l.getWeight();
			}
		}
		return 0.;
	}
	
	public static double calculateConsistency(IScenarioNode node,ICibMatrix matrix){
		BalanceCalculator bc = new BalanceCalculator();
		Balance b = bc.calculate(node.getScenario(), matrix);
		double con=0,conSum=0;
		for(IDescriptor<?> d : b.getScenario().getFactors()){
			con+=b.getScore(d);
			conSum+=b.getBestScore(d);
		}
		con /=conSum;
		return con;
	}
	
	public static void printEntropy(ICibMatrix matrix, IScenarioNetwork network){
		Eigenvector ev = new Eigenvector();
		IScenarioNode node;
		Iterator<? extends IScenarioNode> it = network.getNodes();
		double selfLink = 0.;
		double[] eigenvector= ev.calculate(network);
		double sum = 0;
		double entropy = 0;
		double entropyProduction = 0;
		
		for(int i=0;i<eigenvector.length;i++){
			sum+=eigenvector[i];
		}
		for(int i=0;i<eigenvector.length;i++){
			eigenvector[i]/=sum;			
		}
		
		it = network.getNodes();
		double e,score;
		while(it.hasNext()){
			node = it.next();
			e = eigenvector[node.getIndex()];
			if(e!=0){
				entropy-=e*Math.log(e);
				score = 0;
				for(IScenarioLink link:node.getOutgoing()){
					score-=link.getWeight()*Math.log(link.getWeight());
				}
				entropyProduction+=e*score;
			}
		}
		double relEntropy = entropy/Math.log(network.getNodeCount());
		
		double stateCount = 1; //selflink
		for(IDescriptor<?> d:matrix.getFactors()){
			stateCount+=d.getStateCount();
		}
		
		stateCount-=matrix.getFactors().size();
		
		double relEntropyProduction = entropyProduction/Math.log(stateCount);
		
		System.out.println(entropy+","+relEntropy+","+entropyProduction+","+relEntropyProduction);
		
	}

}
