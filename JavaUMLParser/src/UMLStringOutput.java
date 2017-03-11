import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.print.DocFlavor.STRING;
import javax.swing.text.FieldView;

import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.type.ClassOrInterfaceType;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;


/**
 * @author Abhishek
 */
public class UMLStringOutput {
	
	
	UMLClassAttributesBuilder umlClassAttributesBuilder = new UMLClassAttributesBuilder();
	UMLClassMethodsBuilder umlClassMethodsBuilder = new UMLClassMethodsBuilder();
	UMLClassConnectionLineBuilder umlClassConnectionLineBuilder = new UMLClassConnectionLineBuilder();
	
	private String  finalPlantUMLTemplate = "";
	
	/**
	 * @param plantUMLFigure
	 * @param sourceFolderName
	 * @param outputImageFileName
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("resource")
	public void generatePlantUMLTemplateFile(PlantUMLFigure plantUMLFigure, String sourceFolderName, String outputImageFileName ) throws IOException, ClassNotFoundException
	{
		String umlVariables = "";
		//Fetching the generated class list from the Plant UML Figure
		List<ClassGeneration> generatedClassList =  plantUMLFigure.getGeneratedClass();
		boolean fieldAvailable = false;
		List<MethodDeclaration> getterSetterMethodList =  new ArrayList<MethodDeclaration>();
		
		for(ClassGeneration generatedClass : generatedClassList)
		{
			if(!generatedClass.isInterfaceFlag())
			{
				umlVariables = umlVariables + "class "+ generatedClass.getClassName() + "{\n";
			}
			else
			{
				umlVariables = umlVariables + "interface "+ generatedClass.getClassName() + "<<interface>>" + "{\n";
			}
			if(generatedClass.getFieldNames() !=null)
			{
				fieldAvailable = true;
				umlVariables = umlVariables + umlClassAttributesBuilder.getUMLClassAttributes(generatedClass.getFieldNames());
				umlVariables = umlVariables+"\n";
			}
			if(generatedClass.getMethodNames() !=null)
			{
				/*
				 * Below code block is to convert the private variable based
				 * on its public setter and getter method
				 * */
				List<MethodDeclaration> methodList = generatedClass.getMethodNames();
				if(fieldAvailable)
				{
					for(MethodDeclaration currentMethod : methodList)
					{
						for(FieldDeclaration eachField : generatedClass.getFieldNames())
						{
							String fieldSetterMethod = "set" + eachField.getVariables().get(0).toString();
							String fieldGetterMethod = "get" + eachField.getVariables().get(0).toString();
							
							if(currentMethod.getName().equalsIgnoreCase(fieldGetterMethod) || currentMethod.getName().equalsIgnoreCase(fieldSetterMethod))
							{
								getterSetterMethodList.add(currentMethod);
								//System.out.println(Modifier.toString(1));
								eachField.setModifiers(1);
							}
							
						}
					}
					for(MethodDeclaration getterSetterMethod : getterSetterMethodList)
					{
						methodList.remove(getterSetterMethod);
					}
				}
				
				
				
				umlVariables = umlVariables + umlClassMethodsBuilder.getUMLClassMethods(generatedClass.getMethodNames());
			}
			umlVariables += "}\n";
		}
			
		List<NodesConnection> connectedNodesDesc = plantUMLFigure.getConnectedLineswithNodes();
		
		if(connectedNodesDesc !=null && connectedNodesDesc.size()!=0)
		{
			umlVariables = umlVariables + umlClassConnectionLineBuilder.getConnectedLinesDesc(connectedNodesDesc);
		}
		
		
		finalPlantUMLTemplate = "@startuml\n" + umlVariables + "\n@enduml";
		
		SourceStringReader sourceReader = new SourceStringReader(finalPlantUMLTemplate);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputImageFileName+".png");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		
		{
		sourceReader.generateImage(fos, new FileFormatOption(FileFormat.PNG , false));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
