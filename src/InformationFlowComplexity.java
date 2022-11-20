import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InformationFlowComplexity {
	
	static public int InformationFlowComplexityMetric(Node blockDiagram) {
		NodeList MethodCallLists = BasicMetrics.MethodCallsLists(blockDiagram);
		ArrayList<Integer> FanIn_FanOut = new ArrayList<Integer>();
		for (int i = 0 ; i < MethodCallLists.getLength(); i ++) {
			int fanIn = BasicMetrics.Fan_In(MethodCallLists.item(i), blockDiagram);
			FanIn_FanOut.add(fanIn);
			//System.out.println(fanIn);
			int fanOut = BasicMetrics.Fan_Out(MethodCallLists.item(i), blockDiagram);
			//System.out.println(fanOut);
			FanIn_FanOut.add(fanOut);
		}
		
		int sum = 0;
		for (int i = 0; i < FanIn_FanOut.size(); i= i + 2) {
			sum += (FanIn_FanOut.get(i) + FanIn_FanOut.get(i+1)) * (FanIn_FanOut.get(i) + FanIn_FanOut.get(i+1));
		}
		return sum;
	}
}