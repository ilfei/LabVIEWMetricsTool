import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Main {
	
	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, TransformerException {
		 
		File file = FileOperation.OpenFile();
		
		Node BD = ExtractBlockDiagram.ExtractBD(file);
		Node FP = ExtractFrontPanel.ExtractFP(file);
		
		//System.out.println("Block Dependency is: " + DependencyComplexity.BlockDependecy(BD));
		//System.out.println("Terminal Dependency is: " + DependencyComplexity.TerminalDependecy(BD));
		//System.out.println("VI Dependency is: " + DependencyComplexity.VIDependency(BD));
		System.out.println("Cyclomatic Complexity is: " + CyclomaticComplexity.CyclomaticMetric(BD));
		System.out.println("Volume in Halstead complexity is: " + HalsteadComplexity.Volume(BD));
		System.out.println("Difficulty in Halstead complexity is: " + HalsteadComplexity.Difficulty(BD));
		System.out.println("Effort in Halstead complexity is: " + HalsteadComplexity.Effort(BD));
		System.out.println("Information Flow Complexity is: " + InformationFlowComplexity.InformationFlowComplexityMetric(BD));
		System.out.println("Data Complexity is: " + StructureDataComplexity.dataComplexity(BD));
		System.out.println("Structural Complexity is: " + StructureDataComplexity.structuralComplexity(BD));
		System.out.println("System Complexity is: " + StructureDataComplexity.StructureAndDataComplexity(BD));
		System.out.println("Cognitive Complexity is: " + CognitiveComplexity.CognitionMetric(BD));
		System.out.println("Average Height and Width is: "+ UIComplexity.HeightAndWidth(FP));
		System.out.println("Density is: "+ UIComplexity.Density(FP));
		//System.out.println("UI complexity is: "+ UIComplexity.UIComplexityMetric(FP));
	 }
}
