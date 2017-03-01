//import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import japa.parser.*;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;



/**
 * This is the main java parser class which takes the java source codes dynamically
 * and convert them to a UML image
 * 
 * */
public class Main {

	public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
		String sourceFlder = args[1];
		String outputImageFileName = args[2];
		
		CompilationUnit compilationUnit = new CompilationUnit();
		
		File fileFolder = new File(sourceFlder);
		
		
		ArrayList<File> sourceFiles = new ArrayList<File>();
		
		File[] fileListInFolder = fileFolder.listFiles();
		
		for(File file : fileListInFolder )
		{
			if(file.isFile() && file.getName().endsWith(".java"))
			{
				sourceFiles.add(file);
			}
		}
		
		
		Iterator<File> fileItr = sourceFiles.iterator();
		
		
		HashMap<String, TypeDeclaration> compiledTypesMap = new HashMap<String, TypeDeclaration>();
		
		List<ClassGeneration> classOrInterfaceList = new ArrayList<ClassGeneration>();
		
		//ClassOrInterfaceDeclaration classOrInterfaceDeclaration = new ClassOrInterfaceDeclaration();
		
		List<ClassOrInterfaceType> classOrInterfaceTypes = new ArrayList<ClassOrInterfaceType>();
		
		
	
		List<TypeDeclaration> listOfTypes = new ArrayList<TypeDeclaration>();
		
		PlantUMLFigure plantUMLFigure = new PlantUMLFigure();
	
		
		/*
		 * This block of code iteratively fetches the return type, access modifier, name of 
		 * attributes present in the loaded .Java classes and store them in a list of Class Types 
		 * to be used later for conditional checking
		 * */
		while(fileItr.hasNext())
		{
			File individualJavafile = fileItr.next();
			compilationUnit = JavaParser.parse(individualJavafile);
			
			
			listOfTypes = compilationUnit.getTypes();
			
			Iterator<TypeDeclaration> typeItr = listOfTypes.iterator();
			ClassGeneration generatedClass = new ClassGeneration();
			while(typeItr.hasNext())
			{
				TypeDeclaration classOrInterfaceElement = typeItr.next();
				System.out.println("File name is :" + classOrInterfaceElement.getName() + ".java");
				
				compiledTypesMap.put(classOrInterfaceElement.getName(), classOrInterfaceElement);
				
				if(((ClassOrInterfaceDeclaration)classOrInterfaceElement).isInterface())
				{
					generatedClass.setInterfaceFlag(true);
				}
				else
				{
					generatedClass.setInterfaceFlag(false);
				}
				
				generatedClass.setClassName(classOrInterfaceElement.getName());
				
				List<BodyDeclaration> classBodyDeclarationList = classOrInterfaceElement.getMembers();
				List<FieldDeclaration> fieldDeclarationList = new ArrayList<FieldDeclaration>();
				
				for(BodyDeclaration elementBody : classBodyDeclarationList)
				{
//					if((FieldDeclaration)classBody instanceof FieldDeclaration)
//					{
//						System.out.println("File :" + it.next() + ""+ ((FieldDeclaration)classBody).getType());
//					}
					
					
					System.out.println("Members of this type class is :" +elementBody);
					
					if(elementBody instanceof FieldDeclaration)
					{
						if(((FieldDeclaration)elementBody).getType() instanceof PrimitiveType || 
								((FieldDeclaration)elementBody).getType().toString().equals("String") ||
								((FieldDeclaration)elementBody).getType().toString().equals("int[]") ||
								((FieldDeclaration)elementBody).getType().toString().equals("String[]"))
						{
							System.out.println("The variable data type is: "+ Modifier.toString(((FieldDeclaration)elementBody).getModifiers()));
							fieldDeclarationList.add(((FieldDeclaration)elementBody));
							//System.out.println("The variable type is :"+ ((FieldDeclaration)elementBody).getType().toString());
							generatedClass.setFieldNames(fieldDeclarationList);
							
						}
						
					}
					if(elementBody instanceof MethodDeclaration)
					{
						generatedClass.getMethodNames().add((MethodDeclaration)elementBody);
					}
					if(elementBody instanceof ConstructorDeclaration)
					{
						generatedClass.getConstuctorNames().add((ConstructorDeclaration)elementBody);
					}
				}
			}
			classOrInterfaceList.add(generatedClass);
			
		}
		plantUMLFigure.setGeneratedClass(classOrInterfaceList);
		
		
		/*
		 * This block of code fetches the type definition(Class/Interface) of the template
		 * and provide the dependency decision based on whether 'EXTENDS' or 'IMPLEMENTS'
		 * 
		 * */
		for(TypeDeclaration type : listOfTypes)
		{
			classOrInterfaceTypes = ((ClassOrInterfaceDeclaration)type).getExtends();
			
			if(classOrInterfaceTypes!=null)
			{
				for(ClassOrInterfaceType classInterfaceType : classOrInterfaceTypes)
				{
					ClassGeneration sourceNode = new ClassGeneration();
					//classInterfaceType
				}
			}
		}
		
		/*
		 * Draw dependencies between classes based on the 
		 * attribute types and composition types after fetching the TypeDeclaration from 
		 * the HashMap where we saved them
		 * */
		
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
						if(getReferenceTypeFlag(currentBodyDeclaration))
						{
							/*if(plantUMLFigure.getConnectedLineswithNodes().size()!=0)
							{
								for(NodesConnection node : plantUMLFigure.getConnectedLineswithNodes())
								{
									if(typeDeclarationElement.getName().equals(node.getSourceNode()) || typeDeclarationElement.getName().equals(node.getDestinationNode()))
									{
										break;
									}
									else
									{
										continue;
									}
								}
							}
							else
							{
								String destinationClassFullDesc = ((FieldDeclaration)currentBodyDeclaration).getType().toString();
								String destinationClassName = new String(((FieldDeclaration)currentBodyDeclaration).getVariables().get(0).toString().toUpperCase());
								String sourceNodeName = typeDeclarationElement.getName();
								ClassGeneration sourceNode = plantUMLFigure.getClassGeneration(sourceNodeName);
								ClassGeneration destinationNode = new ClassGeneration(destinationClassName); 
								NodesConnection nodeConnection = new NodesConnection();
								nodeConnection.setSourceNode(sourceNode);
								nodeConnection.setDestinationNode(destinationNode);
								nodeConnection.setConnectingLine(ConnectingLines.getAssociation());
								
								
								if(destinationClassFullDesc.contains("Collection"))
								{
									nodeConnection.setMultiplicityLevel(PlantUMLNotations.getOnetomanymultiplicity());
								}
								plantUMLFigure.getConnectedLineswithNodes().add(nodeConnection);
							}*/
							
							
									String destinationClassFullDesc = new String(((FieldDeclaration)currentBodyDeclaration).getType().toString());
									String destinationClassName = new String(((FieldDeclaration)currentBodyDeclaration).getVariables().get(0).toString().toUpperCase());
									String sourceNodeName = typeDeclarationElement.getName();
									ClassGeneration sourceNode = plantUMLFigure.getClassGeneration(sourceNodeName);
									ClassGeneration destinationNode = new ClassGeneration(destinationClassName); 
									NodesConnection nodeConnection = new NodesConnection();
									nodeConnection.setSourceNode(sourceNode);
									nodeConnection.setDestinationNode(destinationNode);
									nodeConnection.setConnectingLine(ConnectingLines.getAssociation());
									
									
									if(destinationClassFullDesc.contains("Collection"))
									{
										nodeConnection.setMultiplicityLevel(PlantUMLNotations.getOnetomanymultiplicity());
									}
									plantUMLFigure.getConnectedLineswithNodes().add(nodeConnection);
							//ClassGeneration destinationNode = new ClassGeneration();
						}
					}
				}
			}
			
		
		}
		new UMLStringOutput().generatePlantUMLTemplateFile(plantUMLFigure, sourceFlder, outputImageFileName);
		
	}
	
	public static boolean getReferenceTypeFlag(BodyDeclaration currentBodyDeclaration)
	{
		if(((FieldDeclaration)currentBodyDeclaration).getType() instanceof ReferenceType)
		{
			if(!(((FieldDeclaration)currentBodyDeclaration).getType().toString().equals("String")))
			{
				if(!(((FieldDeclaration)currentBodyDeclaration).getType().toString().equals("String[]")) &&
						!(((FieldDeclaration)currentBodyDeclaration).getType().toString().equals("int[]")))
				{
					return true;
				}
			}
		}
		return false;
	}

}
