import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BasicMetrics {
	
	static List<String> BlockNames = new ArrayList<String>();
	
	public static int SystemInput(Node BlockDiagram) {
		// System input in a block diagram is a terminal with direction "Output" 
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(DataAccessor/Terminal[@Direction = 'Output'])");
			Number SystemInputNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			// change to integer from predefined double
			//System.out.println("This VI contains "+ SystemInputNumber.intValue() + " Input");
			return SystemInputNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int SystemOutput(Node BlockDiagram) {
		// System output in a block diagram is a terminal with direction "Input"
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(DataAccessor/Terminal[@Direction = 'Input'])");
			Number SystemOutputNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			// change to integer from predefined double
			//System.out.println("This VI contains "+ SystemOutputNumber.intValue() + " output");
			return SystemOutputNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// get total number of blocks
	// Attention: Since data accessor is counted as system input and output in the above functions, node DataAccessor, node wire and node label are excluede here 
	public static int TotalBlocks(Node BlockDiagram) {
		//System.out.println(PublicFunctions.nodeToString(BlockDiagram.getChildNodes().item(1)));
		for(int i = 1; i < BlockDiagram.getChildNodes().getLength(); i ++) {
			if(BlockDiagram.getChildNodes().item(i).getNodeName() == "WhileLoop" || 
			   BlockDiagram.getChildNodes().item(i).getNodeName() == "ForLoop") {
			    // add loop to block list first
				BlockNames.add(BlockDiagram.getChildNodes().item(i).getNodeName());
				TotalBlocks(BlockDiagram.getChildNodes().item(i));
			}else if(BlockDiagram.getChildNodes().item(i).getNodeName() == "CaseStructure" || 
				BlockDiagram.getChildNodes().item(i).getNodeName() == "CaseStructure") {
				BlockNames.add(BlockDiagram.getChildNodes().item(i).getNodeName());
				TotalBlocks(BlockDiagram.getChildNodes().item(i));
			}else if(BlockDiagram.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE && // it is a valid node
			   //BlockDiagram.getChildNodes().item(i).getNodeName()!= "DataAccessor" && 
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "Wire" && 
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "NodeLabel" && 
			   //BlockDiagram.getChildNodes().item(i).getNodeName()!= "Literal" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "LoopIteration" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "LoopCount" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "LoopCondition" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "CaseStructure.Case" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "CaseStructure.Selector" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "EventStructure.EventData" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "EventStructure.EventCase" &&
			   BlockDiagram.getChildNodes().item(i).getNodeName()!= "Comment" &&
			   BlockDiagram.getChildNodes().item(i).getAttributes().getNamedItem("xmlns") != null) {
				//System.out.println("i is"+i);
				//System.out.print(PublicFunctions.nodeToString(BlockDiagram.getChildNodes().item(i)));
				//System.out.println(BlockDiagram.getChildNodes().item(i).getNodeName());
				BlockNames.add(BlockDiagram.getChildNodes().item(i).getNodeName());
			}
		}
		//System.out.println(BlockNames.size());
		return BlockNames.size();
	}
	
	// get the number of distinct blocks 
	public static int DistictBlocks(Node BlockDiagram) {
		// get all the block names first
		TotalBlocks(BlockDiagram);
		//System.out.println(PublicFunctions.removeDuplicates(BlockNames).size());
		return (PublicFunctions.removeDuplicates(BlockNames).size());
	}
	
	// for a VI and its SubVI, the fan-in for a VI is the number of output of this SubVI back to this VI
	public static int Fan_In(Node MethodCall, Node BlockDiagram) {
		
		// first get all the IDs of output terminal
		XPath xPath = XPathFactory.newInstance().newXPath();
		ArrayList<String> IdList = new ArrayList<String>();
		try {
			NodeList MethodCallTerminalList = (NodeList)xPath.evaluate("Terminal[@Direction = 'Output']", MethodCall, XPathConstants.NODESET);
			for(int i = 0; i < MethodCallTerminalList.getLength(); i++ ) {
				IdList.add(MethodCallTerminalList.item(i).getAttributes().getNamedItem("Id").toString());
			}
			//System.out.println(IdList);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// convert Idlist to the format to match the format in wires
		
		// first, get the id of this method call
		String MethodCallId = null;
		MethodCallId = MethodCall.getAttributes().getNamedItem("Id").toString();
		
		ArrayList<String> formattedIDs = PublicFunctions.combineIDs(IdList, MethodCallId);
		//System.out.println(formattedIDs);
		
		ArrayList<String> IDsWithinWires = PublicFunctions.getAllIDsConnectedWithWire(BlockDiagram);
		ArrayList<String> FormattedIDsWithinWires = new ArrayList<String>();
		// seperate IDs within IDsWithinWires
		for(int i = 0 ; i < IDsWithinWires.size(); i++) {
			// first, remove Joints=""
			String removeJoints = IDsWithinWires.get(i).substring(8, IDsWithinWires.get(i).length()-1);
			String[] splited = removeJoints.split("\\s+");
			for(int j=0; j < splited.length; j++) {
				//System.out.println(splited[j]);
				if(splited[j].charAt(0) == 'N') {
					FormattedIDsWithinWires.add(splited[j].replaceFirst("[|].*", ""));
				}
			}
		}
		//System.out.println(PublicFunctions.removeDuplicates(FormattedIDsWithinWires));
		
		// last, check common elements in these two array lists. This number is fan-in for this VI.
		formattedIDs.retainAll(PublicFunctions.removeDuplicates(FormattedIDsWithinWires));
		//System.out.println(formattedIDs);
		return formattedIDs.size();
	}
	
	// for a VI and its SubVI, fan-out is the number of input of this SubVI - reflected as direction="Input" in terminal in MethodCall
	public static int Fan_Out(Node MethodCall, Node BlockDiagram) {
		
		// first get all the IDs of output terminal
		XPath xPath = XPathFactory.newInstance().newXPath();
		ArrayList<String> IdList = new ArrayList<String>();
		try {
			NodeList MethodCallTerminalList = (NodeList)xPath.evaluate("Terminal[@Direction = 'Input']", MethodCall, XPathConstants.NODESET);
			for(int i = 0; i < MethodCallTerminalList.getLength(); i++ ) {
				IdList.add(MethodCallTerminalList.item(i).getAttributes().getNamedItem("Id").toString());
			}
			//System.out.println(IdList);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// convert Idlist to the format to match the format in wires
		
		// first, get the id of this method call
		String MethodCallId = null;
		MethodCallId = MethodCall.getAttributes().getNamedItem("Id").toString();
		
		ArrayList<String> formattedIDs = PublicFunctions.combineIDs(IdList, MethodCallId);
		//System.out.println(formattedIDs);
		
		ArrayList<String> IDsWithinWires = PublicFunctions.getAllIDsConnectedWithWire(BlockDiagram);
		ArrayList<String> FormattedIDsWithinWires = new ArrayList<String>();
		// Separate IDs within IDsWithinWires
		for(int i = 0 ; i < IDsWithinWires.size(); i++) {
			// first, remove Joints=""
			String removeJoints = IDsWithinWires.get(i).substring(8, IDsWithinWires.get(i).length()-1);
			String[] splited = removeJoints.split("\\s+");
			for(int j=0; j < splited.length; j++) {
				//System.out.println(splited[j]);
				if(splited[j].charAt(0) == 'N') {
					FormattedIDsWithinWires.add(splited[j].replaceFirst("[|].*", ""));
				}
			}
		}
		//System.out.println(PublicFunctions.removeDuplicates(FormattedIDsWithinWires));
		
		// last, check common elements in these two array lists. This number is fan-in for this VI.
		formattedIDs.retainAll(PublicFunctions.removeDuplicates(FormattedIDsWithinWires));
		//System.out.println(formattedIDs);
		return formattedIDs.size();
	}
	
	// number of method calls - use // to get all the method calls regarding their level
	public static NodeList MethodCallsLists(Node BlockDiagram) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			NodeList MethodCallsNodeLists = (NodeList)xPath.evaluate("//MethodCall", BlockDiagram, XPathConstants.NODESET);
			return MethodCallsNodeLists;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// number of wires 
	public static int NumberOfWires(Node BlockDiagram) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(//Wire)");
			Number WireNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			return WireNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// number of for loops
	public static int NumberOfForLoops(Node BlockDiagram) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(//ForLoop)");
			Number ForLoopNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			return ForLoopNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// number of while loops
	public static int NumberOfWhileLoops(Node BlockDiagram) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(//WhileLoop)");
			Number WhileLoopNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			return WhileLoopNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// number of case structure
	public static int NumberOfCaseStructure(Node BlockDiagram) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(//CaseStructure)");
			Number CaseStructureNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			return CaseStructureNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// number of event structure
	public static int NumberOfEventStructure(Node BlockDiagram) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(//EventStructure)");
			Number CaseStructureNumber = (Number) expr.evaluate(BlockDiagram, XPathConstants.NUMBER);
			return CaseStructureNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	// number of HTML tabs
	public static int NumberOfHtmlTabs(Node FrontPanel) {
		XPath xPath = XPathFactory.newInstance().newXPath();
		XPathExpression expr;
		try {
			expr = xPath.compile("count(//HtmlTabItem)");
			Number CaseStructureNumber = (Number) expr.evaluate(FrontPanel, XPathConstants.NUMBER);
			return CaseStructureNumber.intValue();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
}
