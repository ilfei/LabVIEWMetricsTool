import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PublicFunctions {
	
	static XPath xPath = XPathFactory.newInstance().newXPath();
	
	public static String nodeToString (Node node) throws TransformerException{
		 StringWriter buf = new StringWriter();
	     Transformer xform = TransformerFactory.newInstance().newTransformer();
		 xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		 // keep indent from original XML file
		 xform.setOutputProperty(OutputKeys.INDENT, "yes");
		 xform.transform(new DOMSource(node), new StreamResult(buf));
	     return(buf.toString());
     }
	 
	 public static List<String> removeDuplicates(List<String> lists) {
		 // Store unique items in result.
	     List<String> result = new ArrayList<String>();
	     // Record encountered Strings in HashSet.
	     HashSet<String> set = new HashSet<>();
	     // Loop over argument list.
	     for (String item : lists) {
	     // If String is not in set, add it to the list and the set.
	    	 if (!set.contains(item)) {
	            result.add(item);
	            set.add(item);
	           }
	     	}
	     	return result;
	 }
	 
	 public static ArrayList<String> combineIDs(List<String> terminalIDs, String MethodCallID){
		 ArrayList<String> formattedIDs = new ArrayList<String>();
		 for(int i = 0 ; i < terminalIDs.size(); i ++) {
			 formattedIDs.add("N(" + MethodCallID.substring(4, MethodCallID.length()-1) + ":" +
					                 terminalIDs.get(i).substring(4, terminalIDs.get(i).length()-1) + ")");
		 }
		 return formattedIDs;
	 }
	 
	 public static ArrayList<String> getAllIDsConnectedWithWire(Node BlockDiagram){
		 XPath xPath = XPathFactory.newInstance().newXPath();
		 ArrayList<String> ConnectedIdList = new ArrayList<String>();
		 try {
			 // //wire specifies all the wires nodes regardless of levels
			 NodeList WireNodeList = (NodeList)xPath.evaluate("//Wire", BlockDiagram, XPathConstants.NODESET);
			 for(int i = 0; i < WireNodeList.getLength(); i++ ) {
				 ConnectedIdList.add(WireNodeList.item(i).getAttributes().getNamedItem("Joints").toString());
			 }
			 //System.out.println(ConnectedIdList);
			 return ConnectedIdList;
		 } 
		 catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return null;
	 }
	 
	 public static NodeList MethodCallsNodeLists (Node BlockDiagram) {
		 try {
			NodeList MethodCallTerminalList = (NodeList)xPath.evaluate("//MethodCall", BlockDiagram, XPathConstants.NODESET);
			System.out.println(MethodCallTerminalList.getLength());
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
	 }
	 
	 public static ArrayList<String> GetUniqueBlockIDs(ArrayList<String> IDsWithTerminals){
		 
		ArrayList<String> UniqueIDs = new ArrayList<String>();
		UniqueIDs = (ArrayList<String>) PublicFunctions.removeDuplicates(IDsWithTerminals);
		//System.out.println(UniqueIDs.size());
		ArrayList<String> ConnectedBlocks = new ArrayList<String>();
		for(int i = 0; i < UniqueIDs.size(); i++) {
			String removeN = UniqueIDs.get(i).substring(2, UniqueIDs.get(i).length()-1);
			String[] splited =  removeN.split("\\:");
			ConnectedBlocks.add(splited[0]);
		}
		ArrayList<String> UniqueBlocks = new ArrayList<String>();
		UniqueBlocks = (ArrayList<String>) PublicFunctions.removeDuplicates(ConnectedBlocks);
		return UniqueBlocks;
	 }

}
