import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;

/**
 * 
 */

/**
 * @author Abhishek
 */
public class DependencyDrawer {
	
	private PlantUMLFigureTemplate figureWithDependency = new PlantUMLFigureTemplate();
	
	public PlantUMLFigureTemplate drawDependeny(PlantUMLFigureTemplate figureWithoutDependency, HashMap<String, TypeDeclaration> compiledTypesMap)
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
						if(JavaParserMain.getReferenceTypeFlag(((FieldDeclaration)currentBodyDeclaration).getType()))
						{
							String destinationClassFullDesc = new String(((FieldDeclaration)currentBodyDeclaration).getType().toString());
							
							Type destinationFieldDEclaration = ((FieldDeclaration)currentBodyDeclaration).getType();
							
							String destinationClassName = "";
							
							//condition to check for "Collection<B> b" type field variables and extracting the collection object "B" from it and setting in destination node
							if(((ClassOrInterfaceType)((ReferenceType)destinationFieldDEclaration).getType()).getTypeArgs()!=null)
							{
								
								destinationClassName = new String(((ClassOrInterfaceType)((ReferenceType)destinationFieldDEclaration).getType()).getTypeArgs().get(0).toString());
							}
							//getTypeArgs() will be null in case of "private C c" type field variables, so else will be executed
							
							else
							{
								destinationClassName = destinationClassFullDesc;
							}
							
							String sourceNodeName = typeDeclarationElement.getName();
							boolean isNodeExist = false;
							for(NodesConnection node : figureWithoutDependency.getConnectedLineswithNodes())
							{
								boolean sourceExists = sourceNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || sourceNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
								boolean destinationExists = destinationClassName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || destinationClassName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
								
								if(sourceExists && destinationExists)
								{
									if(node.getConnectingLine().equals(ConnectingLines.getAssociation()))
									{
										isNodeExist = true;
									}
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
								if(JavaParserMain.getReferenceTypeFlag(currentMethodParameter.getType()))
								{
									System.out.println("Current method parameter" + currentMethodParameter.getType());
									NodesConnection nodesConnection = new NodesConnection();
									String sourceNodeName = typeDeclarationElement.getName();
									ClassGeneration sourceNode = figureWithoutDependency.getClassGeneration(sourceNodeName);
									String destinationNodeName = currentMethodParameter.getType().toString();
									
									boolean isNodeExist = false;
									for(NodesConnection node : figureWithoutDependency.getConnectedLineswithNodes())
									{
										boolean sourceExists = sourceNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || sourceNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
										boolean destinationExists = destinationNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || destinationNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
										
										if(sourceExists && destinationExists)
										{
											if(node.getConnectingLine().equals(ConnectingLines.getDependency()))
											{
												isNodeExist = true;
											}
											
										}
									}
									if(isNodeExist)
									{
										break;
									}
									
									ClassGeneration destinationNode = new ClassGeneration(destinationNodeName);
									
									if(JavaParserMain.getReferenceTypeFlag(currentMethodParameter.getType()))
									{
										nodesConnection.setConnectingLine(ConnectingLines.getDependency());
									}
									
									nodesConnection.setSourceNode(sourceNode);
									nodesConnection.setDestinationNode(destinationNode);
									
									figureWithoutDependency.getConnectedLineswithNodes().add(nodesConnection);
								}
							}
						}
						String methodName = ((MethodDeclaration)currentBodyDeclaration).getName();
						String methodBody = ((MethodDeclaration)currentBodyDeclaration).getBody().toString();
						if(methodName.equals("main") && !methodBody.isEmpty())
						{
							for(ClassGeneration eachClass : figureWithoutDependency.getGeneratedClass())
							{
								if(methodBody.contains(eachClass.getClassName()))
								{
									if(eachClass.isInterfaceFlag())
									{
										NodesConnection nodesConnection = new NodesConnection();
										String sourceNodeName = typeDeclarationElement.getName();
										ClassGeneration sourceNode = figureWithoutDependency.getClassGeneration(sourceNodeName);
										String destinationNodeName = eachClass.getClassName();
										
										boolean isNodeExist = false;
										for(NodesConnection node : figureWithoutDependency.getConnectedLineswithNodes())
										{
											boolean sourceExists = sourceNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || sourceNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
											boolean destinationExists = destinationNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || destinationNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
											
											if(sourceExists && destinationExists)
											{
												if(node.getConnectingLine().equals(ConnectingLines.getDependency()))
												{
													isNodeExist = true;
												}
												
											}
										}
										if(isNodeExist)
										{
											break;
										}
										
										ClassGeneration destinationNode = new ClassGeneration(destinationNodeName);
										
										nodesConnection.setConnectingLine(ConnectingLines.getDependency());
										
										
										nodesConnection.setSourceNode(sourceNode);
										nodesConnection.setDestinationNode(destinationNode);
										
										figureWithoutDependency.getConnectedLineswithNodes().add(nodesConnection);
									}
								}
							}
							
						}
					}
					
					if(currentBodyDeclaration instanceof ConstructorDeclaration)
					{
						List<Parameter> constructorParamList = ((ConstructorDeclaration)currentBodyDeclaration).getParameters();
						boolean isConstructParamObjectInterface = false;
						if(constructorParamList!=null)
						{
							for(Parameter constructorParam : constructorParamList)
							{
								String constructParamObject = ((ReferenceType)constructorParam.getType()).toString();
								
								for(ClassGeneration eachClass : figureWithoutDependency.getGeneratedClass())
								{
									if(constructParamObject.equals(eachClass.getClassName()))
									{
										isConstructParamObjectInterface = eachClass.isInterfaceFlag();
										break;
									}
									
								}
								
								
								if(isConstructParamObjectInterface)
								{
									NodesConnection nodesConnection = new NodesConnection();
									String sourceNodeName = typeDeclarationElement.getName();
									ClassGeneration sourceNode = figureWithoutDependency.getClassGeneration(sourceNodeName);
									String destinationNodeName = constructParamObject;
									
									boolean isNodeExist = false;
									for(NodesConnection node : figureWithoutDependency.getConnectedLineswithNodes())
									{
										boolean sourceExists = sourceNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || sourceNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
										boolean destinationExists = destinationNodeName.equals(((ClassGeneration)node.getSourceNode()).getClassName()) || destinationNodeName.equals(((ClassGeneration)node.getDestinationNode()).getClassName());
										
										if(sourceExists && destinationExists)
										{
											if(node.getConnectingLine().equals(ConnectingLines.getDependency()))
											{
												isNodeExist = true;
											}
											
										}
									}
									if(isNodeExist)
									{
										break;
									}
									
									ClassGeneration destinationNode = new ClassGeneration(destinationNodeName);
									
									nodesConnection.setConnectingLine(ConnectingLines.getDependency());
									
									
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
	
	
	public PlantUMLFigureTemplate getExtendsImplementsConnection(PlantUMLFigureTemplate plantUMLFigure,List<TypeDeclaration> listOfTypes)
	{
		Iterator<TypeDeclaration> typeItr = listOfTypes.iterator();
		while(typeItr.hasNext())
		{
			TypeDeclaration typeDeclarationElement = typeItr.next();
			List<ClassOrInterfaceType> implementsList = ((ClassOrInterfaceDeclaration)typeDeclarationElement).getImplements();

			if(implementsList!=null)
			{
				for(ClassOrInterfaceType currentImplementingClass : implementsList)
				{
					String source = typeDeclarationElement.getName();
					ClassGeneration sourceNode = null;
					if(plantUMLFigure.getGeneratedClass().isEmpty())
					{
						sourceNode = new ClassGeneration(source);
					}
					else
					{
						sourceNode = plantUMLFigure.getClassGeneration(source);
					}
					String destination = currentImplementingClass.getName();
					ClassGeneration destinationNode = new ClassGeneration(destination);

					NodesConnection nodeConnection = new NodesConnection();
					nodeConnection.setSourceNode(sourceNode);
					nodeConnection.setDestinationNode(destinationNode);
					nodeConnection.setConnectingLine(ConnectingLines.getImplements());
					plantUMLFigure.getConnectedLineswithNodes().add(nodeConnection);
				}
			}

			List<ClassOrInterfaceType> extendsList = ((ClassOrInterfaceDeclaration)typeDeclarationElement).getExtends();
			if(extendsList!=null)
			{
				for(ClassOrInterfaceType currentExtendingClass : extendsList)
				{
					String source = typeDeclarationElement.getName();
					ClassGeneration sourceNode = null;
					if(plantUMLFigure.getGeneratedClass().isEmpty())
					{
						sourceNode = new ClassGeneration(source);
					}
					else
					{
						sourceNode = plantUMLFigure.getClassGeneration(source);
					}
					String destination = currentExtendingClass.getName();
					ClassGeneration destinationNode = new ClassGeneration(destination);

					NodesConnection nodeConnection = new NodesConnection();
					nodeConnection.setSourceNode(sourceNode);
					nodeConnection.setDestinationNode(destinationNode);
					nodeConnection.setConnectingLine(ConnectingLines.getExtends());
					plantUMLFigure.getConnectedLineswithNodes().add(nodeConnection);
				}	
			}
		}
		
		return plantUMLFigure;
		
	}
	
	
	
	
	
	

}
