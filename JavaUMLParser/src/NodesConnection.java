/**
 * @author Abhishek
 */
public class NodesConnection {
	
	private ClassGeneration sourceNode;
	private ClassGeneration destinationNode;
	private String connectingLine;
	private String multiplicityLevel;
	/**
	 * @return the sourceNode
	 */
	public ClassGeneration getSourceNode() {
		return sourceNode;
	}
	/**
	 * @param sourceNode the sourceNode to set
	 */
	public void setSourceNode(ClassGeneration sourceNode) {
		this.sourceNode = sourceNode;
	}
	/**
	 * @return the destinationNode
	 */
	public ClassGeneration getDestinationNode() {
		return destinationNode;
	}
	/**
	 * @param destinationNode the destinationNode to set
	 */
	public void setDestinationNode(ClassGeneration destinationNode) {
		this.destinationNode = destinationNode;
	}
	/**
	 * @return the connectingLine
	 */
	public String getConnectingLine() {
		return connectingLine;
	}
	/**
	 * @param connectingLine the connectingLine to set
	 */
	public void setConnectingLine(String connectingLine) {
		this.connectingLine = connectingLine;
	}
	/**
	 * @return the multiplicityLevel
	 */
	public String getMultiplicityLevel() {
		return multiplicityLevel;
	}
	/**
	 * @param multiplicityLevel the multiplicityLevel to set
	 */
	public void setMultiplicityLevel(String multiplicityLevel) {
		this.multiplicityLevel = multiplicityLevel;
	}

}
