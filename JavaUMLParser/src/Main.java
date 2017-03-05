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
import japa.parser.ast.type.Type;



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
				//System.out.println("File name is :" + classOrInterfaceElement.getName() + ".java");
				
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
				List<MethodDeclaration> methodDeclarationList = new ArrayList<MethodDeclaration>();
				List<ConstructorDeclaration> constructorDeclarationList = new ArrayList<ConstructorDeclaration>();
				
				for(BodyDeclaration elementBody : classBodyDeclarationList)
				{
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
							generatedClass.setFieldNames(fieldDeclarationList);
							
						}
						
					}
					if(elementBody instanceof MethodDeclaration)
					{
						methodDeclarationList.add((MethodDeclaration)elementBody);
						generatedClass.setMethodNames(methodDeclarationList);;
					}
					if(elementBody instanceof ConstructorDeclaration)
					{
						constructorDeclarationList.add((ConstructorDeclaration)elementBody);
						generatedClass.setConstuctorNames(constructorDeclarationList);
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
		for(File eachFile : sourceFiles)
		{
			//File individualJavafile = fileItr.next();
			compilationUnit = JavaParser.parse(eachFile);


			listOfTypes = compilationUnit.getTypes();

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
							ClassGeneration sourceNode = plantUMLFigure.getClassGeneration(source);
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
							ClassGeneration sourceNode = plantUMLFigure.getClassGeneration(source);
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

			} 

		
		/*
		 * Below code snippet draws dependencies between classes based on the 
		 * attribute types and composition types after fetching the TypeDeclaration from 
		 * the HashMap where we saved them
		 * */
		
		plantUMLFigure = new DependencyDrawer().drawDependeny(plantUMLFigure, compiledTypesMap);
		
		new UMLStringOutput().generatePlantUMLTemplateFile(plantUMLFigure, sourceFlder, outputImageFileName);
		
	}
	
	public static boolean getReferenceTypeFlag(Type passedType)
	{
		if(passedType instanceof ReferenceType)
		{
			if(!(passedType.toString().equals("String")))
			{
				if(!(passedType.toString().equals("String[]")) &&
						!(passedType.toString().equals("int[]")))
				{
					return true;
				}
			}
		}
		return false;
	}

}
