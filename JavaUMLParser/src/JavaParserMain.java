//import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ParseException;
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
 * */
public class JavaParserMain {
	
	private static HashMap<String, TypeDeclaration> compiledTypesMap = new HashMap<String, TypeDeclaration>();
	private static List<ClassGeneration> classOrInterfaceList = new ArrayList<ClassGeneration>();
	public static SequenceDiagramGenerator sdg = new SequenceDiagramGenerator();

	public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {


		if(args[0].equalsIgnoreCase("umlparserclassdiagram"))
		{
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
			List<TypeDeclaration> listOfTypes = new ArrayList<TypeDeclaration>();
			PlantUMLFigureTemplate plantUMLFigure = new PlantUMLFigureTemplate();

			/*
			 * This block of code iteratively fetches the return type, access modifier, name of 
			 * attributes present in the loaded .Java class files and store them in a list of Class Types 
			 * to be used later for conditional checking
			 * */
			while(fileItr.hasNext())
			{
				File individualJavafile = fileItr.next();
				compilationUnit = JavaParser.parse(individualJavafile);
				listOfTypes = compilationUnit.getTypes();
				classOrInterfaceList = getClassOrInterfaceList(listOfTypes);
			}

			plantUMLFigure.setGeneratedClass(classOrInterfaceList);
			/*
			 * This block of code fetches the type definition(Class/Interface) of the template
			 * and provide the dependency decision based on whether 'EXTENDS' or 'IMPLEMENTS'
			 **/
			for(File eachFile : sourceFiles)
			{
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
			 **/
			plantUMLFigure = new DependencyDrawer().drawDependeny(plantUMLFigure, compiledTypesMap);
			new PlantUMLImageGenerator().generatePlantUMLTemplateFile(plantUMLFigure, sourceFlder, outputImageFileName);
		}
		if(args[0].equalsIgnoreCase("umlparsersequencediagram"))
		{
			String sourceFlder = args[1];
			String outputImageFileName =args[2];
			String outputImageFilePath = "/Users/Abhishek/ParserUMLDiagramsImages/" + outputImageFileName;
			File fileFolder = new File(sourceFlder);
			File[] fileListInFolder = fileFolder.listFiles();
			URL url = fileFolder.toURL();
			URL[] urls = new URL[] { url };
			String finalSeqString = "";

			// Create a new class loader with the directory
			ClassLoader loader = new URLClassLoader(urls);

			for(File file : fileListInFolder )
			{
				if(file.isFile() && file.getName().endsWith(".class"))
				{
					if(file.getName().contains("Main"))
					{
						Class<?> thisClass1 = Class.forName(file.getName().split(".class")[0]);
						//Object instance = thisClass1.newInstance();
						Method method = thisClass1.getDeclaredMethod("main", String[].class);
						Object[] arguments = new Object[]{args};
						method.invoke(null, arguments);
					}
				}
			}

			//Loading the wsd file generated by the aspect program
			File sequenceTextFile = new File("/Users/Abhishek/sequenceOutputFile/sequence_diag.wsd");

			if(sequenceTextFile.isFile() && sequenceTextFile.getName().endsWith(".wsd"))
			{
				BufferedReader br = null;
				FileReader fr = null;
				try {

					fr = new FileReader(sequenceTextFile);
					br = new BufferedReader(fr);

					String sCurrentLine;

					br = new BufferedReader(new FileReader(sequenceTextFile));

					while ((sCurrentLine = br.readLine()) != null) {
						System.out.println(sCurrentLine);
						finalSeqString += sCurrentLine + "\n";
					}

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {

						if (br != null)
							br.close();

						if (fr != null)
							fr.close();

					} catch (IOException ex) {

						ex.printStackTrace();

					}

				}
			}


			new PlantUMLImageGenerator().generatePlantUMLSequenceDiagram(finalSeqString, outputImageFilePath);

		}
	}

	public static boolean getReferenceTypeFlag(Type passedType)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		int stackTop = stackTraceElements.length;
		String sourceParticipant = stackTraceElements[2].getClassName();
		String destinationParticipant = sourceParticipant;
		String message = "getReferenceTypeFlag(" + passedType + ")";
		sdg.generateSequenceDiagram(sourceParticipant, destinationParticipant,message);
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

	public static boolean getPrimitiveTypeFlag(Type passedType)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String sourceParticipant = stackTraceElements[2].getClassName();
		String destinationParticipant = sourceParticipant;
		String message = "getPrimitiveTypeFlag(" + passedType + ")";
		sdg.generateSequenceDiagram(sourceParticipant, destinationParticipant,message);
		
		if(passedType instanceof PrimitiveType || passedType.toString().equals("String") || 
				passedType.toString().equals("int[]") || passedType.toString().equals("String[]"))
		{
			return true;
		}
		else{
			return false;
		}

	}
	
	public static List<ClassGeneration> getClassOrInterfaceList(List<TypeDeclaration> listOfTypes)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String sourceParticipant = stackTraceElements[1].getClassName();
		String destinationParticipant = sourceParticipant;
		String message = "getClassOrInterfaceList(listOfTypes:List<TypeDeclaration>)";
		sdg.generateSequenceDiagram(sourceParticipant, destinationParticipant,message);



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
					if(getPrimitiveTypeFlag(((FieldDeclaration)elementBody).getType()))
					{
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

		return classOrInterfaceList;
	}
}
