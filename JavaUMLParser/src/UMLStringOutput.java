import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * 
 */

/**
 * @author Abhishek
 *
 */
public class UMLStringOutput {
	
	
	UMLClassAttributesBuilder umlClassAttributesBuilder = new UMLClassAttributesBuilder();
	UMLClassMethodsBuilder umlClassMethodsBuilder = new UMLClassMethodsBuilder();
	UMLClassConnectionLineBuilder umlClassConnectionLineBuilder = new UMLClassConnectionLineBuilder();
	
	private String  finalPlantUMLTemplate = "@startuml\n";
	
	
	
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
		
		for(ClassGeneration generatedClass : generatedClassList)
		{
			if(generatedClass.getFieldNames() !=null)
			{
				//Class className  = generatedClass.getClass();
				
				if(!generatedClass.isInterfaceFlag())
				{
					umlVariables = umlVariables + "Class "+ generatedClass.getClassName() + "{\n";
					umlVariables = umlVariables + umlClassAttributesBuilder.getUMLClassAttributes(generatedClass.getFieldNames());
					umlVariables = umlVariables+"\n";
				}
				else
				{
					umlVariables = umlVariables + "Interface "+ generatedClass.getClassName() + "{\n";
					umlVariables = umlVariables + umlClassAttributesBuilder.getUMLClassAttributes(generatedClass.getFieldNames());
					umlVariables = umlVariables+"\n";
				}
				
				//System.out.println("class name is :" + className.getName() + "" + className.isInterface());
				//if(!className.isInterface())
				//{
					
				//}
				
			}
//			if(generatedClass.getMethodNames() !=null)
//			{
//				umlClassMethodsBuilder.setPlantUMLBodyOfMethods(generatedClass.getMethodNames());
//			}
		}
		umlVariables += "}\n";
		
		List<NodesConnection> connectedNodesDesc = plantUMLFigure.getConnectedLineswithNodes();
		
		if(connectedNodesDesc !=null && connectedNodesDesc.size()!=0)
		{
			umlVariables = umlVariables + umlClassConnectionLineBuilder.getConnectedLinesDesc(connectedNodesDesc);
			umlVariables = umlVariables + "\n";
		}
		
		
		finalPlantUMLTemplate = "@startuml\n" + umlVariables + "\n@enduml";
		//finalPlantUMLTemplate = "";
		
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
