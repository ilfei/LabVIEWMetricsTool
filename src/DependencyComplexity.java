import java.util.ArrayList;

import org.w3c.dom.Node;

public class DependencyComplexity {
	
	static public double BlockDependecy(Node blockDiagram) {
		ArrayList<String> IDListsWithinWires = PublicFunctions.getAllIDsConnectedWithWire(blockDiagram);
		//System.out.println(IDListsWithinWires);
		//System.out.println(IDListsWithinWires.size());
		ArrayList<String> WireIDs = new ArrayList<String>();
		ArrayList<String> OutputTerminalIDs = new ArrayList<String>();
		ArrayList<String> InputTerminalIDs = new ArrayList<String>();
		// Separate IDs within IDsWithinWires
		for(int i = 0 ; i < IDListsWithinWires.size(); i++) {
			// first, remove Joints=""
			String removeJoints = IDListsWithinWires.get(i).substring(8, IDListsWithinWires.get(i).length()-1);
			String[] splited = removeJoints.split("\\s+");
			for(int j=0; j < splited.length; j++) {
				//System.out.println(splited[j]);
				if(splited[j].charAt(0) == 'N') {
					WireIDs.add(splited[j].replaceFirst("[|].*", ""));
				}
				// if j == 0, it means this ID is output terminal ID 
				if(j==0) {
					OutputTerminalIDs.add(splited[j].replaceFirst("[|].*", ""));
				}else if(splited[j].charAt(0) == 'N') {// else it is input terminal ID if it starts with N
					InputTerminalIDs.add(splited[j].replaceFirst("[|].*", ""));
				}
			}
		}
		//System.out.println(OutputTerminalIDs);
		//System.out.println(InputTerminalIDs);
	
		// get all unique block IDs
		ArrayList<String> UniqueBlock = PublicFunctions.GetUniqueBlockIDs(WireIDs);
		//System.out.println(UniqueBlock.size());
		// get all unique blocks with input terminals
		ArrayList<String> UniqueBlockWithInput = PublicFunctions.GetUniqueBlockIDs(InputTerminalIDs);
		//System.out.println(UniqueBlockWithInput.size());
		//System.out.println("Block Dependency is: " + (double)UniqueBlockWithInput.size()/UniqueBlock.size());
		return ((double)UniqueBlockWithInput.size()/UniqueBlock.size());
	} 
	
	// terminal dependency
	
	static public double TerminalDependecy(Node blockDiagram) {
		
		ArrayList<String> IDListsWithinWires = PublicFunctions.getAllIDsConnectedWithWire(blockDiagram);
		ArrayList<String> AllTerminalID = new ArrayList<String>();
		ArrayList<String> InputTerminalIDs = new ArrayList<String>();
		// Separate IDs within IDsWithinWires
		for(int i = 0 ; i < IDListsWithinWires.size(); i++) {
			// first, remove Joints=""
			String removeJoints = IDListsWithinWires.get(i).substring(8, IDListsWithinWires.get(i).length()-1);
			String[] splited = removeJoints.split("\\s+");
			for(int j=0; j < splited.length; j++) {
				if(splited[j].charAt(0) == 'N') {
					AllTerminalID.add(splited[j].replaceFirst("[|].*", ""));
				}
				
				if(j!=0 && splited[j].charAt(0) == 'N') {// else it is input terminal ID if it starts with N
					InputTerminalIDs.add(splited[j].replaceFirst("[|].*", ""));
				}
			}
		}
		//System.out.println(AllTerminalID.size());
		//System.out.println(InputTerminalIDs.size());
		//System.out.println((double)InputTerminalIDs.size()/AllTerminalID.size());
		//System.out.println("Terminal Dependency is: " +  (double)InputTerminalIDs.size()/AllTerminalID.size());
		return (double)InputTerminalIDs.size()/AllTerminalID.size();
	} 
	
	// UI dependency
	static public double VIDependency(Node blockDiagram) {
		return 2*((BlockDependecy(blockDiagram) * TerminalDependecy(blockDiagram))/(BlockDependecy(blockDiagram) + TerminalDependecy(blockDiagram)));
	}
}
