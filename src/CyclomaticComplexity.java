import org.w3c.dom.Node;

public class CyclomaticComplexity {
	// Cyclomatic Complexity is closely related to decision blocks
	static public int CyclomaticMetric(Node BlockDiagram) {
		// decision blocks contain three categories
		// 1. Condition Statement
		// 2. Loop blocks, including while loop and for loop
		// 3. Switch Statement
		// In LabVIEW, switch statements and condition statements are grouped into Case Structure
		
		// This metric calculate the total number of above-mentioned blocks.
		
		
		return BasicMetrics.NumberOfForLoops(BlockDiagram) + BasicMetrics.NumberOfWhileLoops(BlockDiagram) + 
				BasicMetrics.NumberOfCaseStructure(BlockDiagram) + + BasicMetrics.NumberOfEventStructure(BlockDiagram) + 1;
	}
}
