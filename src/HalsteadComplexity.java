import org.w3c.dom.Node;

public class HalsteadComplexity {
	// This class calculates all the metrics related to Halstead Complexity
	// eta_{1} = the number of distinct blocks
	// eta_{2} = the number of system input
	// N_{1} = the total number of blocks
	// N_{2} = the total number of system input and output
	
	// VI vocabulary: distinct block types and the number of system input  
	public static int VI_Vocabulary(Node BlockDiagram) {
		//System.out.println(BasicMetrics.DistictBlocks(BlockDiagram));
		//System.out.println(BasicMetrics.DistictBlocks(BlockDiagram) + "" + BasicMetrics.SystemInput(BlockDiagram));
		return BasicMetrics.DistictBlocks(BlockDiagram) + BasicMetrics.SystemInput(BlockDiagram);
	}
	
	// VI Size: total blocks and system input and output number
	public static int VI_length(Node BlockDiagram) {
		return BasicMetrics.NumberOfWires(BlockDiagram) + BasicMetrics.SystemInput(BlockDiagram) + BasicMetrics.SystemOutput(BlockDiagram);
	}
	
	// VI Volume V
	public static double Volume(Node BlockDiagram) {
		if(VI_Vocabulary(BlockDiagram) == 0) {
			return 0;
		}
		//System.out.println("VI length" + (double)VI_length(BlockDiagram) + "    ; vocabulary :" + VI_Vocabulary(BlockDiagram));
		return (double)VI_length(BlockDiagram) * ((double)(Math.log(VI_Vocabulary(BlockDiagram)+1)) / Math.log(2));
	}
	
	// VI Difficulty D
	public static double Difficulty(Node BlockDiagram) {
		if(BasicMetrics.SystemInput(BlockDiagram) == 0) {
			return (double)(BasicMetrics.DistictBlocks(BlockDiagram)/2.0);
		}else {
			return (double)(BasicMetrics.DistictBlocks(BlockDiagram)/2.0) * (double)(VI_length(BlockDiagram)/BasicMetrics.SystemInput(BlockDiagram));
		}
	}
	
	// VI Effort D
	public static double Effort(Node BLockDiagram) {
		return (double)Volume(BLockDiagram) * Difficulty(BLockDiagram);
	}
	
}
