import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ExtractFrontPanel {
	public static Node ExtractFP(File file){
		
		DocumentBuilder builder = null; 
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		Document document = null;
		try {
			document = builder.parse(file);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		Node result = null;
		
		// if the file opened is a webgiv, then evaluate HtmlFrontPanel, else NativeFrontPanel
		//System.out.println(file.getName().split("\\.")[1]);
		if(file.getName().split("\\.")[1].equals("gviweb")) {
			try {
				result = (Node)xPath.evaluate("SourceFile/VirtualInstrument/HtmlFrontPanel", document, XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// print front panel
			/*
			try {
				System.out.println(PublicFunctions.nodeToString(result));
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			if(result== null) {
				System.out.println("Front Panel retreive failed!");
			}
			return result;
			
		}else {
			//System.out.println("It is a gvi");
			try {
				result = (Node)xPath.evaluate("SourceFile/VirtualInstrument/NativeFrontPanel", document, XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// print front panel
			/*
			try {
				System.out.println(PublicFunctions.nodeToString(result));
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			if(result== null) {
				System.out.println("Front Panel retreive failed!");
			}
			return result;
		}
	}
	
}
