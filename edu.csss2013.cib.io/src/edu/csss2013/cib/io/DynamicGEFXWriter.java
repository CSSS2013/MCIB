package edu.csss2013.cib.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.TreeMap;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import edu.csss2013.cib.IScenario;
import edu.csss2013.cib.IScenarioLink;
import edu.csss2013.cib.IScenarioNode;
import edu.csss2013.cib.impl.util.HashCodeGenerator;

public class DynamicGEFXWriter {



	public void write(File file,DynamicNetwork dynNet,double start,double end) throws FileNotFoundException, XMLStreamException{

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = factory.createXMLStreamWriter( new FileOutputStream( file ));
		writer.writeStartDocument();
		writer.writeStartElement("gefx");		
		writer.writeAttribute("xmlns", "http://www.gexf.net/1.1draft");
		writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		writer.writeAttribute("xsi:schemaLocation", "http://www.gexf.net/1.1draft " +
				"http://www.gexf.net/1.1draft/gexf.xsd");




		writer.writeStartElement("graph");		
		writer.writeAttribute("mode", "dynamic");
		writer.writeAttribute("defaultedgetype", "directed");

		writer.writeStartElement("attributes");
		writer.writeAttribute("class", "node");
		writer.writeAttribute("mode", "dynamic");
		
		writer.writeStartElement("attribute");
		writer.writeAttribute("id", "Consistency");
		writer.writeAttribute("title", "Consistency");
		writer.writeAttribute("type", "float");
		writer.writeEndElement();
		
		writer.writeStartElement("attribute");
		writer.writeAttribute("id", "SelfLink");
		writer.writeAttribute("title", "SelfLink");
		writer.writeAttribute("type", "float");
		writer.writeEndElement();
		
		writer.writeStartElement("attribute");
		writer.writeAttribute("id", "Eigenvector Centrality");
		writer.writeAttribute("title", "Eigenvector Centrality");
		writer.writeAttribute("type", "float");
		writer.writeEndElement();	
		
		
		writer.writeEndElement();

		writer.writeStartElement("attributes");
		writer.writeAttribute("class", "edge");
		writer.writeAttribute("mode", "dynamic");
		writer.writeStartElement("attribute");

		writer.writeAttribute("id", "weight");
		writer.writeAttribute("title", "Weight");
		writer.writeAttribute("type", "float");
		writer.writeEndElement();

		writer.writeEndElement();

		//<node id="n2" label="Node 2" start="2007" end="2009" />

		writer.writeStartElement("nodes");
		
		for(int i:dynNet.getNodes()){
			
			writer.writeStartElement("node");
			writer.writeAttribute("id", String.valueOf((i)));
			writer.writeStartElement("attvalues");
			TreeMap<Interval, Map<String,Double>> tm = dynNet.getNodeValues(i);
			for(Interval interval:tm.keySet()){
				 
				for(Entry<String,Double> entry:tm.get(interval).entrySet()){
					writer.writeEmptyElement("attvalue");
					writer.writeAttribute("for", entry.getKey());
					writer.writeAttribute("value", String.valueOf(entry.getValue()));
					writer.writeAttribute("start", String.valueOf(interval.getStartTime()));
					writer.writeAttribute("end", String.valueOf(interval.getEndTime()));
				}
			}

			


			writer.writeEndElement();
			writer.writeEndElement();


			//writer.writeAttribute("label", node.getScenario().toString());


			
		}

		writer.writeEndElement();

		writer.writeStartElement("edges");



		List<LinkEncouter> e = new ArrayList<LinkEncouter>();
		for(edu.csss2013.cib.io.DynamicNetwork.LinkEncouter link:dynNet.getLinks()){
			writer.writeStartElement("edge");

			TreeMap<Interval, Double> tm = dynNet.getLinkOccurences(link);
			/*if(e.contains(en)){
				throw new IllegalArgumentException("Link "+en+" already encountered");
			}*/

			writer.writeAttribute("source", String.valueOf(link.start));
			writer.writeAttribute("target", String.valueOf(link.end));
			double myEndTime = 0;
			writer.writeStartElement("attvalues");
			for(Entry<Interval, Double> entry:tm.entrySet()){
				writer.writeEmptyElement("attvalue");
				writer.writeAttribute("for", "weight");
				writer.writeAttribute("value", String.valueOf(entry.getValue()));
				writer.writeAttribute("start", String.valueOf(entry.getKey().getStartTime()));
				writer.writeAttribute("end", String.valueOf(entry.getKey().getEndTime()));
				myEndTime = entry.getKey().getEndTime();
			}
			if(myEndTime<end){
				writer.writeEmptyElement("attvalue");
				writer.writeAttribute("for", "weight");
				writer.writeAttribute("value", String.valueOf(0));
				writer.writeAttribute("start", String.valueOf(myEndTime));
				writer.writeAttribute("end", String.valueOf(end));
			}
			writer.writeEndElement();
			writer.writeEndElement();			
		}


		writer.writeEndElement();

		writer.writeEndElement();		
		writer.writeEndElement();
		writer.writeEndDocument();
		writer.close();
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
