import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CognitiveComplexity {
	
	// control cognitive complexity is related to basic control blocks cognition
	// use recursive call to handle different level of control blocks
	static public int CognitionMetric(Node BlockDiagram) {
		int currentLevelCognition = 0;
		// Analyze every node
		for(int i = 1; i <= BlockDiagram.getChildNodes().getLength()-1; i++) {
			//System.out.println("i is : " + i);
			//System.out.println("currentLevelCognition is : " + currentLevelCognition);
			// if it is a first-level node
			if(BlockDiagram.getChildNodes().item(i).getNodeType() == 1) {
				//System.out.println(BlockDiagram.getChildNodes().item(i).getNodeName().toString());
				if(BlockDiagram.getChildNodes().item(i).getNodeName() == "MethodCall") {
					//System.out.println("This is a method call. Its cognition compleixty is:" + MethodCallCognition(BlockDiagram.getChildNodes().item(i)));
					//currentLevelCognition += MethodCallCognition(BlockDiagram.getChildNodes().item(i));
					currentLevelCognition += 1;
				}else if(BlockDiagram.getChildNodes().item(i).getNodeName() == "ForLoop" || BlockDiagram.getChildNodes().item(i).getNodeName() == "WhileLoop") {
					//System.out.println("Loops - Contribution = 3");
					currentLevelCognition += 3*CognitionMetric(BlockDiagram.getChildNodes().item(i));
				}else if(BlockDiagram.getChildNodes().item(i).getNodeName() == "CaseStructure") {
					//System.out.println("Case Structure -  contribution = 2");
					currentLevelCognition += 2*CognitionMetric(BlockDiagram.getChildNodes().item(i));
				}else if(BlockDiagram.getChildNodes().item(i).getAttributes().getNamedItem("xmlns") != null ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "LoopCount" || 
						BlockDiagram.getChildNodes().item(i).getNodeName() == "LoopIteration" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "LoopTunnel" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "CaseStructure.Case" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "CaseStructure.Selector" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "EventStructure.EventData" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "EventStructure.EventCase" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "Comment" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "DataAccessor" || 
						BlockDiagram.getChildNodes().item(i).getNodeName() == "Wire" ||
						BlockDiagram.getChildNodes().item(i).getNodeName() == "NodeLabel" || 
						BlockDiagram.getChildNodes().item(i).getNodeName() == "Literal" ){
					//System.out.println("This is not a contribution block");
					currentLevelCognition += 0;
				}else { // other pre-defined processing blocks
					//System.out.println("contribution = 1");
					currentLevelCognition+=1;
				}
			}else {
				//System.out.println("This is not a valid node");
			}
		}
		return currentLevelCognition;
	}
	
	// control cognitive complexity is related to method call cognition 
	static public int MethodCallCognition(Node MethodCall) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			// .// represents elements in current node 
			NodeList MethodCallParList = (NodeList)xPath.evaluate(".//Terminal", MethodCall, XPathConstants.NODESET);
			//System.out.println("There are: " + MethodCallParList.getLength() + " parameters");
			return MethodCallParList.getLength();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
