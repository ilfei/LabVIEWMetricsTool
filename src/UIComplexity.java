import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UIComplexity extends ExtractFrontPanel {
	
	Node FrontPanel = ExtractFP(FileOperation.OpenFile());
	static XPath xPath = XPathFactory.newInstance().newXPath();
	
	// if the file opened is webvi, then the number of BaseName tags reprents the real number of elements;
	// if the file opened is a gvi, then the number of Heights tags represents the real number of elements.
	
	// Metric: Number of Elements in the front panel
	public static int NumberOfElements(Node FrontPanel) {
		try {
			if(FrontPanel.getNodeName().equals("HtmlFrontPanel")) {
				int tabNumber = BasicMetrics.NumberOfHtmlTabs(FrontPanel);
				if(tabNumber == 0) {
					tabNumber = 1;
				}
				return (((NodeList)xPath.evaluate(".//*[@BaseName]", FrontPanel, XPathConstants.NODESET)).getLength())/tabNumber;
			}
			return ((NodeList)xPath.evaluate(".//*[@Height]", FrontPanel, XPathConstants.NODESET)).getLength();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// screen height
	public static int ScreenHeight(Node FrontPanel) {
		
		// 4 steps to fetch the height/width
		// Original Height/WidthAttribute Height="[float]1600"
		// 1. Remove "Height="
		// 2. Remove [] and the content inside of []
		// 3. Remove ""
		// 4. Convert string to integer 
		return  Integer.parseInt(FrontPanel.getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
	}
	
	// screen width
	public static int ScreenWidth(Node FrontPanel) {
		
		// 4 steps to fetch the height/width
		// Original Height/WidthAttribute Height="[float]1600"
		// 1. Remove "Height="
		// 2. Remove [] and the content inside of []
		// 3. Remove ""
		// 4. Convert string to integer
		return Integer.parseInt(FrontPanel.getAttributes().getNamedItem("Width").toString().replace("Width=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
	}
	
	// Average height of all the elements in the front panel
	public static double AverageHeight (Node FrontPanel) {	
		if(NumberOfElements(FrontPanel) ==  0) {
			//System.out.println("No elements on front panel");
			return 0;
		}else {
			double sum = 0;
			try {
				NodeList NodeWithHeightList;
				List<Node>  NodeWithHeightListFilter = new ArrayList<Node>();
				if(FrontPanel.getNodeName().equals("HtmlFrontPanel")) {
					NodeWithHeightList = (NodeList)xPath.evaluate(".//*[@BaseName]", FrontPanel, XPathConstants.NODESET);
					for(int i = 0; i < NodeWithHeightList.getLength(); i++) {
						if(NodeWithHeightList.item(i).getNodeName().equals("HtmlCluster") == false) {
							NodeWithHeightListFilter.add(NodeWithHeightList.item(i));
						}
					}		
					// traverse all the node to get the average height of all the elements in the front panel.
					for (int i = 0; i < NodeWithHeightListFilter.size(); i++) {
						//System.out.println(Integer.parseInt(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", "")));
						sum += Double.parseDouble(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
					}
					
				}else {
					NodeWithHeightList = (NodeList)xPath.evaluate(".//*[@Height]", FrontPanel, XPathConstants.NODESET);			
					// traverse all the node to get the average height of all the elements in the front panel.
					for (int i = 0; i < NodeWithHeightList.getLength(); i++) {
						//System.out.println(Integer.parseInt(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", "")));
						sum += Double.parseDouble(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
					}
				}
				
				//System.out.println(sum/NumberOfElements(FrontPanel));
				return sum / NumberOfElements(FrontPanel);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	// Average width of all the elements in the front panel
	public static double AverageWidth (Node FrontPanel) {
		if(NumberOfElements(FrontPanel) ==  0) {
			//System.out.println("No elements on front panel");
			return 0;
		}else {
			double sum = 0;
			try {
				// old
				//NodeList NodeWithWidthList = (NodeList)xPath.evaluate(".//*[@Width]", FrontPanel, XPathConstants.NODESET);
				NodeList NodeWithWidthList;
				List<Node>  NodeWithWidthListFilter = new ArrayList<Node>();
				if(FrontPanel.getNodeName().equals("HtmlFrontPanel")) {
					NodeWithWidthList = (NodeList)xPath.evaluate(".//*[@BaseName]", FrontPanel, XPathConstants.NODESET);
					for(int i = 0; i < NodeWithWidthList.getLength(); i++) {
						if(NodeWithWidthList.item(i).getNodeName().equals("HtmlCluster") == false) {
							NodeWithWidthListFilter.add(NodeWithWidthList.item(i));
						}
					}		
					// traverse all the node to get the average height of all the elements in the front panel.
					for (int i = 0; i < NodeWithWidthListFilter.size(); i++) {
						//System.out.println(Integer.parseInt(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", "")));
						sum += Double.parseDouble(NodeWithWidthListFilter.get(i).getAttributes().getNamedItem("Width").toString().replace("Width=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
					}
				}else {
					NodeWithWidthList = (NodeList)xPath.evaluate(".//*[@Height]", FrontPanel, XPathConstants.NODESET);
					// traverse all the node to get the average height of all the elements in the front panel.
					for (int i = 0; i < NodeWithWidthList.getLength(); i++) {
						//System.out.println(Integer.parseInt(NodeWithWidthList.item(i).getAttributes().getNamedItem("Width").toString().replace("Width=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", "")));
						sum += Double.parseDouble(NodeWithWidthList.item(i).getAttributes().getNamedItem("Width").toString().replace("Width=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
					}
				}
				
				// Define an array to store all the heights					
				// System.out.println(sum/NumberOfElements(FrontPanel));
				return sum / NumberOfElements(FrontPanel);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			
			return 0;
		}
	}
	
	// Metric: Height and Width
	public static double HeightAndWidth (Node FrontPanel) {
		int tabNumber = BasicMetrics.NumberOfHtmlTabs(FrontPanel);
		if(tabNumber == 0) {
			tabNumber = 1;
		}
		
		return ((AverageWidth(FrontPanel)/ScreenWidth(FrontPanel) +  (double)AverageHeight(FrontPanel)/ScreenHeight(FrontPanel))/2)/tabNumber;
	}
	
	// Metric: Density
	public static double Density(Node FrontPanel) {
		try {
			double sum = 0;
			//old
			//NodeList NodeWithHeightList = (NodeList)xPath.evaluate(".//*[@Height]", FrontPanel, XPathConstants.NODESET);

			NodeList NodeWithHeightList;
			List<Node>  NodeWithHeightListFilter = new ArrayList<Node>();
			if(FrontPanel.getNodeName().equals("HtmlFrontPanel")) {
				NodeWithHeightList = (NodeList)xPath.evaluate(".//*[@BaseName]", FrontPanel, XPathConstants.NODESET);
				ArrayList<ArrayList<Double>> CoordinatesAndSize = new ArrayList<ArrayList<Double>>();
				for(int i = 0; i < NodeWithHeightList.getLength(); i++) {
					if(NodeWithHeightList.item(i).getNodeName().equals("HtmlCluster") == false) {
						NodeWithHeightListFilter.add(NodeWithHeightList.item(i));
					}
				}
				for (int i = 0; i < NodeWithHeightListFilter.size(); i++) {
					
					//System.out.println("Size is: "+ NodeWithHeightListFilter.size() + "Current i is + " + i);
					//System.out.println(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Left"));
					if(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Height") != null && 
							NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Width") != null &&
							NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Left") != null && 
							NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Top") != null) {
						//System.out.println(Integer.parseInt(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", "")));
						double ElementHeight = Double.parseDouble(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
						double ElementWidth = Double.parseDouble(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Width").toString().replace("Width=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
						double Left = Double.parseDouble(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Left").toString().replace("Left=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));;
						double Top = Double.parseDouble(NodeWithHeightListFilter.get(i).getAttributes().getNamedItem("Top").toString().replace("Top=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));;
						// store the coordinates of elements that will be calculated in sum
						ArrayList<Double> Temp = new ArrayList<Double>();
						Temp.add(Left);
						Temp.add(Top);
						Temp.add(ElementWidth);
						Temp.add(ElementHeight);
						
						CoordinatesAndSize.add(Temp);
					}else {
						//System.out.println("Invlaid ");
					}

				}
				
//				for(int i =0; i < CoordinatesAndSize.size(); i++) {
//					System.out.println(i +"th Element:");
//					System.out.println("left: " + CoordinatesAndSize.get(i).get(0));
//					System.out.println("top: " + CoordinatesAndSize.get(i).get(1));
//					System.out.println("width: " + CoordinatesAndSize.get(i).get(2));
//					System.out.println("height: " + CoordinatesAndSize.get(i).get(3));
//				}
				ArrayList<ArrayList<Double>> finalElements = new ArrayList<ArrayList<Double>>();
				finalElements = boxInclusion (CoordinatesAndSize);
				for(int i = 0; i < finalElements.size(); i ++) {
					sum += finalElements.get(i).get(2)*finalElements.get(i).get(3);
				}
				
			}else {
				NodeWithHeightList = (NodeList)xPath.evaluate(".//*[@Height]", FrontPanel, XPathConstants.NODESET);
				// traverse all the node to get the average height of all the elements in the front panel.
				for (int i = 0; i < NodeWithHeightList.getLength(); i++) {
					//System.out.println(Integer.parseInt(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", "")));
					double ElementHeight = Double.parseDouble(NodeWithHeightList.item(i).getAttributes().getNamedItem("Height").toString().replace("Height=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
					double ElementWidth = Double.parseDouble(NodeWithHeightList.item(i).getAttributes().getNamedItem("Width").toString().replace("Width=", "").replaceFirst("[\\[].*?[\\]]", "").replace("\"", ""));
					double ElementSize = ElementHeight * ElementWidth;
					sum += ElementSize;
				}
				
			}
			int tabNumber = BasicMetrics.NumberOfHtmlTabs(FrontPanel);
			if(tabNumber == 0) {
				tabNumber = 1;
			}
			
			//System.out.println("Screen size is: " + ScreenHeight(FrontPanel)*ScreenWidth(FrontPanel));
			//System.out.println("sum is"+ sum);
			//System.out.println("sum per tab is: " + sum/tabNumber);
			return ((double)sum/tabNumber) / (ScreenHeight(FrontPanel)*ScreenWidth(FrontPanel));
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	// judge if there are inclusion elements
	public static ArrayList<ArrayList<Double>> boxInclusion(ArrayList<ArrayList<Double>> CoordinatesAndSize) {
		
		ArrayList<ArrayList<Double>> finalElements = new ArrayList<ArrayList<Double>>();
		//System.out.println("CoordinatesAndSize is:" + CoordinatesAndSize.size());
		for(int i = 0; i < CoordinatesAndSize.size(); i++) {
			//System.out.println("size is:" + i);
			double Left = CoordinatesAndSize.get(i).get(0);
			double Top = CoordinatesAndSize.get(i).get(1);
			double Width = CoordinatesAndSize.get(i).get(2);
			double Height = CoordinatesAndSize.get(i).get(3);
			
			//System.out.println("Current Element: Left"+CoordinatesAndSize.get(i).get(0)+"  Top"+CoordinatesAndSize.get(i).get(1)+"  Width"+ CoordinatesAndSize.get(i).get(2) + "   Height" + CoordinatesAndSize.get(i).get(3));
			// if current rectangle is contained within another, remove this rectangle from analysis
			int flag = 0;
			for(int t = 0; t < CoordinatesAndSize.size(); t++) {
				if(t==i) {// this is the same rectangle
					
				}else {
					if(Left+Width < CoordinatesAndSize.get(t).get(0) + CoordinatesAndSize.get(t).get(2) && 
					   Top + Height < CoordinatesAndSize.get(t).get(1) + CoordinatesAndSize.get(t).get(3) &&
					   Left > CoordinatesAndSize.get(t).get(0) && 
					   Top > CoordinatesAndSize.get(t).get(1) ) {
					}else {
						// this rec is not included by another rec
						flag +=1;
					}
				}
			}
			// if rec i is not included by all the rest rec, add it to final rec list
			if(flag ==CoordinatesAndSize.size() -1) {
				//System.out.println("this is an element needed");
				finalElements.add(CoordinatesAndSize.get(i));
				//System.out.println(finalElements.size());
//				for(int m = 0; m < finalElements.size(); m++) {
//					//System.out.println(finalElements.get(m));
//				}
			}
		}
		return finalElements;
	}

	// UI Complexity
	public static double UIComplexityMetric(Node FrontPanel) {
		//System.out.println(HeightAndWidth(FrontPanel));
		//System.out.println(Density(FrontPanel));
		return (HeightAndWidth(FrontPanel) + Density(FrontPanel))/2;
	}
}
