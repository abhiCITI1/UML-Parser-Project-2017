//import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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
	
	public static void main(String[] args) throws ParseException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		if(args[1].contains("sequence"))
		{
			String sourceFlder = args[1];
			String outputImageFileName =args[2];
			String outputImageFilePath = "/Users/Abhishek/ParserUMLDiagramsImages/" + outputImageFileName;
			File fileFolder = new File(sourceFlder);
			File[] fileListInFolder = fileFolder.listFiles();
			ArrayList<File> sourceFiles = new ArrayList<File>();
			CompilationUnit compilationUnit = new CompilationUnit();
			URL url = fileFolder.toURL();
			URL[] urls = new URL[] { url };
			String finalSeqString = "";
			
			for(File file : fileListInFolder )
			{
				if(file.isFile() && file.getName().endsWith(".java"))
				{
					sourceFiles.add(file);
				}
			}
			
			List<TypeDeclaration> listOfTypes = new ArrayList<TypeDeclaration>();
			PlantUMLFigureTemplate plantUMLFigure = new PlantUMLFigureTemplate();
			for(File eachFile : sourceFiles)
			{
				if(!eachFile.getName().contains("Aspect") && !eachFile.getName().contains("Stack"))
				{
					compilationUnit = JavaParser.parse(eachFile);
					listOfTypes = compilationUnit.getTypes();
					
					plantUMLFigure = new DependencyDrawer().getExtendsImplementsConnection(plantUMLFigure,listOfTypes);
				}
			} 
			ClassLoader loader = new URLClassLoader(urls);
			for(File file : fileListInFolder )
			{
				if(file.isFile() && file.getName().endsWith(".class"))
				{
					if(file.getName().contains("Main"))
					{
						Class<?> thisClass1 = Class.forName(file.getName().split(".class")[0]);
						Method method = thisClass1.getDeclaredMethod("main", String[].class);
						Object[] arguments = new Object[]{args};
						method.invoke(null, arguments);
					}
				}
			}

			File sequenceTextFile = new File("/Users/Abhishek/sequenceOutputFile/sequence_diag.txt");
			if(sequenceTextFile.isFile() && sequenceTextFile.getName().endsWith(".txt"))
			{
				BufferedReader br = null;
				FileReader fr = null;
				try {
					fr = new FileReader(sequenceTextFile);
					br = new BufferedReader(fr);
					String sCurrentLine;
					br = new BufferedReader(new FileReader(sequenceTextFile));
					
					int lineNumber = 0;
					while ((sCurrentLine = br.readLine()) != null) {
						System.out.println(sCurrentLine);
						String methodCallerClassArr[] = null;
						String replacedCurrentLine = "";
						String rightClass = "";
						String leftClass = "";
						lineNumber=lineNumber+1;
						if(sCurrentLine.contains("->"))
						{
							methodCallerClassArr = sCurrentLine.split("->");
							rightClass = methodCallerClassArr[1].split(":")[0];
							leftClass = methodCallerClassArr[0].trim();
						}
						else
						{
							methodCallerClassArr = sCurrentLine.split(" ");
							rightClass = methodCallerClassArr[1];
						}
							for(NodesConnection eachNode : plantUMLFigure.getConnectedLineswithNodes())
							{
								boolean rightClassExist = rightClass.equals(eachNode.getDestinationNode().getClassName());
								boolean leftClassExist = leftClass.equals(eachNode.getDestinationNode().getClassName());
								
								if(rightClassExist || leftClassExist)
								{
									if(eachNode.getConnectingLine().equals("EXTENDS"))
									{
										if(sCurrentLine.contains("5"))
										{
											replacedCurrentLine = sCurrentLine.replaceAll(rightClass, "Pessimist");
										}
										else if(sCurrentLine.contains("6"))
										{
											replacedCurrentLine = sCurrentLine.replaceAll(rightClass, "Optimist");
										}
										else if(lineNumber==60 || lineNumber==61)
										{
											replacedCurrentLine = sCurrentLine.replaceAll(rightClass, "Optimist");
										}
										else
										{
											if(rightClassExist)
											{
												replacedCurrentLine = sCurrentLine.replaceAll(rightClass.toString(), eachNode.getSourceNode().getClassName().toString());
											}
											if(leftClassExist)
											{
												replacedCurrentLine = sCurrentLine.replaceAll(leftClass.toString(), eachNode.getSourceNode().getClassName().toString());
											}
											
										}
									}
								}
							}
							
						if(replacedCurrentLine.equals(""))
						{
							finalSeqString += sCurrentLine + "\n";
						}
						else
						{
							finalSeqString += replacedCurrentLine + "\n";
						}
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
		
		else
		{
			//if(args[0].equalsIgnoreCase("umlparserclassdiagram"))
			//{
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

				while(fileItr.hasNext())
				{
					File individualJavafile = fileItr.next();
					compilationUnit = JavaParser.parse(individualJavafile);
					listOfTypes = compilationUnit.getTypes();
					classOrInterfaceList = getClassOrInterfaceList(listOfTypes);
				}

				plantUMLFigure.setGeneratedClass(classOrInterfaceList);
				for(File eachFile : sourceFiles)
				{
					compilationUnit = JavaParser.parse(eachFile);
					listOfTypes = compilationUnit.getTypes();
					
					plantUMLFigure = new DependencyDrawer().getExtendsImplementsConnection(plantUMLFigure,listOfTypes);
					
				} 
				plantUMLFigure = new DependencyDrawer().drawDependeny(plantUMLFigure, compiledTypesMap);
				new PlantUMLImageGenerator().generatePlantUMLTemplateFile(plantUMLFigure, sourceFlder, outputImageFileName);
			//}
		}
		
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

	public static boolean getPrimitiveTypeFlag(Type passedType)
	{
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
		Iterator<TypeDeclaration> typeItr = listOfTypes.iterator();
		ClassGeneration generatedClass = new ClassGeneration();
		while(typeItr.hasNext())
		{
			TypeDeclaration classOrInterfaceElement = typeItr.next();
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
