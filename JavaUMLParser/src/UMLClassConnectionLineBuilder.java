import java.util.List;

/**
 * 
 */

/**
 * @author Abhishek
 *
 */
public class UMLClassConnectionLineBuilder {
	

	public String getConnectedLinesDesc(List<NodesConnection> nodeConnections)
	{
		String plantUMLBodyOfLines = "";
		for(NodesConnection currentNode : nodeConnections)
		{
			if(currentNode.getConnectingLine().equals("ASSOCIATION"))
			{
				if(currentNode.getMultiplicityLevel()!=null)
				{
					plantUMLBodyOfLines += currentNode.getDestinationNode().getClassName() + " \"" + currentNode.getMultiplicityLevel() + "\"" + " .. "+ "\"1\" "+ currentNode.getSourceNode().getClassName() +"\n"; 
				}
				else
				{
					plantUMLBodyOfLines += currentNode.getDestinationNode().getClassName() + " -- " + currentNode.getSourceNode().getClassName() + "\n";
				}
			}
			else if(currentNode.getConnectingLine().equals("DEPENDENCY"))
			{
				plantUMLBodyOfLines += currentNode.getDestinationNode().getClassName() + " --> " + currentNode.getSourceNode().getClassName() + "\n";
			}
		}
		
		
		return plantUMLBodyOfLines;
	}
	
}