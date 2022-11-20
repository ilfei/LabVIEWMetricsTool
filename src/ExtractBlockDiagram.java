import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ExtractBlockDiagram {
	public static Node ExtractBD(File file) throws SAXException, IOException, XPathExpressionException, TransformerException {
		
		System.out.println("Starting Extracting Block Diagram...");
		DocumentBuilder builder = null; 
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		Document document = builder.parse(file);
		System.out.println("Current GVI is: "+ file);
		//Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		Node result = (Node)xPath.evaluate("SourceFile/VirtualInstrument/BlockDiagram", document, XPathConstants.NODE);

		if(result== null) {
			System.out.println("Block Diagram retreive failed!");
		}
		return result;
	}
}
