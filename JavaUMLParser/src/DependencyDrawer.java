import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;

/**
 * 
 */

/**
 * @author Abhishek
 */
public class DependencyDrawer {
	
	private PlantUMLFigure figureWithDependency = new PlantUMLFigure();
	
	public PlantUMLFigure drawDependeny(PlantUMLFigure figureWithoutDependency, HashMap<String, TypeDeclaration> compiledTypesMap)
	{
		
		Iterator<Entry<String, TypeDeclaration>> typeDeclarationItr = compiledTypesMap.entrySet().iterator();
		while(typeDeclarationItr.hasNext())
		{
			Map.Entry<String, TypeDeclaration> typeDeclarationEntry = (Map.Entry<String, TypeDeclaration>)typeDeclarationItr.next();
			
			System.out.println("Type Declaration element is :" + typeDeclarationEntry.getValue());	
			
			TypeDeclaration typeDeclarationElement = typeDeclarationEntry.getValue();
			
			if(!((ClassOrInterfaceDeclaration)typeDeclarationElement).isInterface())
			{
				List<BodyDeclaration> bodyDeclarations = typeDeclarationElement.getMembers();
				
				for(BodyDeclaration currentBodyDeclaration : bodyDeclarations)
				{
					if(currentBodyDeclaration instanceof FieldDeclaration)
					{
						if(Main.getReferenceTypeFlag(((FieldDeclaration)currentBodyDeclaration).getType()))
						{
							String destinationClassFullDesc = new String(((FieldDeclaration)currentBodyDeclaration).getType().toString());
							String destinationClassName = new String(((FieldDeclaration)currentBodyDeclaration).getVariables().get(0).toString().toUpperCase());
							String sourceNodeName = typeDeclarationElement.getName();
							boolean isNodeExist = false;
							for(NodesConnection node : figureWithoutDependency.getConnectedLineswithNodes())
							{
								if((sourceNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || sourceNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName())) &&
										destinationClassName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || destinationClassName.equals(((ClassGeneration)node.getDestinationNode()).getClassName()))
								{
									isNodeExist = true;
								}
							}
							if(isNodeExist)
							{
								break;
							}
							else
							{
								ClassGeneration sourceNode = figureWithoutDependency.getClassGeneration(sourceNodeName);
								ClassGeneration destinationNode = new ClassGeneration(destinationClassName); 
								NodesConnection nodeConnection = new NodesConnection();
								nodeConnection.setSourceNode(sourceNode);
								nodeConnection.setDestinationNode(destinationNode);
								nodeConnection.setConnectingLine(ConnectingLines.getAssociation());


								if(destinationClassFullDesc.contains("Collection"))
								{
									nodeConnection.setMultiplicityLevel(PlantUMLNotations.getOnetomanymultiplicity());
								}
								figureWithoutDependency.getConnectedLineswithNodes().add(nodeConnection);
							}
						}
					}
					
					if(currentBodyDeclaration instanceof MethodDeclaration)
					{
						List<Parameter> methodParameterList = ((MethodDeclaration)currentBodyDeclaration).getParameters();
						
						if(methodParameterList!=null)
						{
							for(Parameter currentMethodParameter : methodParameterList)
							{
								if(Main.getReferenceTypeFlag(currentMethodParameter.getType()))
								{
									System.out.println("Current method parameter" + currentMethodParameter.getType());
									NodesConnection nodesConnection = new NodesConnection();
									String sourceNodeName = typeDeclarationElement.getName();
									ClassGeneration sourceNode = figureWithoutDependency.getClassGeneration(sourceNodeName);
									String destinationNodeName = currentMethodParameter.getType().toString();
									ClassGeneration destinationNode = new ClassGeneration(destinationNodeName);
									
									if(Main.getReferenceTypeFlag(currentMethodParameter.getType()))
									{
										nodesConnection.setConnectingLine(ConnectingLines.getDependency());
									}
									
									nodesConnection.setSourceNode(sourceNode);
									nodesConnection.setDestinationNode(destinationNode);
									
									figureWithoutDependency.getConnectedLineswithNodes().add(nodesConnection);
								}
							}
						}
						
						
						
						
						
					}
				}
			}
			
		
		}
		
		figureWithDependency = figureWithoutDependency;
		return figureWithDependency;
	}

}
