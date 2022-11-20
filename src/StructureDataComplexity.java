import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StructureDataComplexity {

	public static double structuralComplexity(Node blockDiagram) {
		NodeList MethodCallLists = BasicMetrics.MethodCallsLists(blockDiagram);
		ArrayList<Integer> FanOut = new ArrayList<Integer>();
		for (int i = 0 ; i < MethodCallLists.getLength(); i ++) {
			int fanOut = BasicMetrics.Fan_Out(MethodCallLists.item(i), blockDiagram);
			//System.out.println(fanOut);
			FanOut.add(fanOut);
		}
		
		double sum = 0;
		for (int i = 0; i < FanOut.size(); i= i + 1) {
			sum += (FanOut.get(i)) * (FanOut.get(i));
		}
		if(MethodCallLists.getLength() == 0) {
			return 0;
		}else {
			return sum/MethodCallLists.getLength();}
	}
	
	public static double dataComplexity(Node blockDiagram) {
		int systemInput = BasicMetrics.SystemInput(blockDiagram);
		int systemOutput = BasicMetrics.SystemOutput(blockDiagram);
		NodeList MethodCallLists = BasicMetrics.MethodCallsLists(blockDiagram);
		ArrayList<Integer> FanOut = new ArrayList<Integer>();
		for (int i = 0 ; i < MethodCallLists.getLength(); i ++) {
			int fanOut = BasicMetrics.Fan_Out(MethodCallLists.item(i), blockDiagram);
			//System.out.println(fanOut);
			FanOut.add(fanOut);
		}
		int sum = 0;
		for (int i = 0; i < FanOut.size(); i= i + 1) {
			sum += FanOut.get(i);
		}
		double dataComplexity = ((double) (systemInput + systemOutput)) / (double)(sum + 1); 
		return dataComplexity;
	}
	
	public static double StructureAndDataComplexity(Node blockDiagram) {
		return (double)structuralComplexity(blockDiagram) + dataComplexity(blockDiagram);
	}
}
