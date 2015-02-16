package edu.csss2013.cib.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNetwork;
import edu.csss2013.cib.IScenarioNode;

public class CSVDynamicWriter {
	
	private File nodeFile;
	private File edgeFile;
	private Map<IScenarioNode, Integer> map = new HashMap<IScenarioNode, Integer>();
	private BufferedWriter nodeWriter,edgeWriter;
	
	public CSVDynamicWriter(File nodeFile,File edgeFile) throws IOException{
		this.nodeFile=nodeFile;
		this.edgeFile=edgeFile;
		nodeWriter = new BufferedWriter(new FileWriter(nodeFile));
		nodeWriter.write("Id;Scenarios;Time Interval");
		nodeWriter.newLine();
		edgeWriter = new BufferedWriter(new FileWriter(edgeFile));			
		edgeWriter.write("Source;Target;Weight;Time Interval");
		edgeWriter.newLine();
	}
	
	
	
	
	public void addNodes(IScenarioNetwork network,String timeInterval) throws IOException{
		map = new HashMap<IScenarioNode, Integer>();
		
		int counter = 0;
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			map.put(node, counter);
			nodeWriter.write(counter+";"+node.getScenario()+";<"+timeInterval+">");
			nodeWriter.newLine();
			counter++;
		}		
	}
	
	public void addEdges(IScenarioNetwork network,String timeInterval) throws IOException{
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			for(IScenarioLink link:node.getOutgoing()){
				edgeWriter.write(map.get(link.getStartNode())+";"+map.get(link.getEndNode())+";"+
						link.getWeight()+";<"+timeInterval+">");
				edgeWriter.newLine();
			}
		}		
	}
	
	public void writeEdges(File file, IScenarioNetwork network,String timeInterval) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(edgeFile));			
		writer.write("Source;Target;Weight;Time Interval");
		writer.newLine();
		Iterator<? extends IScenarioNode> it = network.getNodes();
		while(it.hasNext()){
			IScenarioNode node = it.next();
			for(IScenarioLink link:node.getOutgoing()){
				writer.write(map.get(link.getStartNode())+";"+map.get(link.getEndNode())+";"+
						link.getWeight()+";<"+timeInterval+">");
				writer.newLine();
			}
		}	
		writer.close();
	}
	
	public void close() throws IOException{
		nodeWriter.close();
		edgeWriter.close();
	}
	
	

}
