import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

/**
 * @author Abhishek
 *
 *This is the class which has the complete class or interface figures
 *with respective class names and the association/dependency between them
 *
 */
public class PlantUMLFigure {
	
	private List<ClassGeneration> generatedClass;
	private List<NodesConnection> connectedLineswithNodes;
	/**
	 * @param generatedClass
	 * @param connectedLineswithNodes
	 */
	public PlantUMLFigure() {
		super();
		this.generatedClass = new ArrayList<ClassGeneration>(0);
		this.connectedLineswithNodes = new ArrayList<NodesConnection>(0);
	}
	/**
	 * @return the generatedClass
	 */
	public List<ClassGeneration> getGeneratedClass() {
		return generatedClass;
	}
	/**
	 * @param generatedClass the generatedClass to set
	 */
	public void setGeneratedClass(List<ClassGeneration> generatedClass) {
		this.generatedClass = generatedClass;
	}
	/**
	 * @return the connectedLineswithNodes
	 */
	public List<NodesConnection> getConnectedLineswithNodes() {
		return connectedLineswithNodes;
	}
	/**
	 * @param connectedLineswithNodes the connectedLineswithNodes to set
	 */
	public void setConnectedLineswithNodes(List<NodesConnection> connectedLineswithNodes) {
		this.connectedLineswithNodes = connectedLineswithNodes;
	}
	
	
	public ClassGeneration getClassGeneration(String s)
	{
		for (ClassGeneration currentclass : generatedClass) 
		{
			if(currentclass.getClassName().equals(s))
			{
				return currentclass;
			}
		}
		return null;
	}
	
	
	
}
