import java.util.ArrayList;
import java.util.List;


/**
 * @author Abhishek
 *
 *This is the class which has the complete class or interface figures
 *with respective class names and the association/dependency between them
 */
public class PlantUMLFigureTemplate {
	
	private List<ClassGeneration> generatedClass;
	private List<NodesConnection> connectedLineswithNodes;
	/**
	 * @param generatedClass
	 * @param connectedLineswithNodes
	 */
	public PlantUMLFigureTemplate() {
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
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//		int stackTop = stackTraceElements.length;
//		String sourceParticipant = stackTraceElements[stackTop-1].getClassName();
//		String destinationParticipant = PlantUMLFigureTemplate.class.getName();
//		String message = "setGeneratedClass(" + generatedClass + ")";
//		Main.sdg.generateSequenceDiagram(sourceParticipant, destinationParticipant,message);
		this.generatedClass = generatedClass;
	}
	/**
	 * @return the connectedLineswithNodes
	 */
	public List<NodesConnection> getConnectedLineswithNodes() {
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//		int stackTop = stackTraceElements.length;
//		String sourceParticipant = stackTraceElements[2].getClassName();
//		String destinationParticipant = PlantUMLFigureTemplate.class.getName();
//		String message = "getConnectedLineswithNodes()";
//		Main.sdg.generateSequenceDiagram(sourceParticipant, destinationParticipant,message);
		
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
//		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//		int stackTop = stackTraceElements.length;
//		String sourceParticipant = stackTraceElements[2].getClassName();
//		String destinationParticipant = PlantUMLFigureTemplate.class.getName();
//		String message = "getClassGeneration("+ s + ")";
//		Main.sdg.generateSequenceDiagram(sourceParticipant, destinationParticipant,message);
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
