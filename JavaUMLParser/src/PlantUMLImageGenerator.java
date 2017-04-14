import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.nio.file.Paths;
import java.nio.file.Path;
/**
 * @author Abhishek
 */
public class PlantUMLImageGenerator {


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
	public void generatePlantUMLTemplateFile(PlantUMLFigureTemplate plantUMLFigure, String sourceFolderName, String outputImageFileName ) throws IOException, ClassNotFoundException
	{
		String umlVariables = "";
		List<ClassGeneration> generatedClassList =  plantUMLFigure.getGeneratedClass();
		boolean fieldAvailable = false;


		/** 
		 * This method invocation is to build the parsed plantUML input based on the class type or interface type and 
		 * to convert the private variable having public setter and getter methods into a public variable and removing those setter getter
		 * methods from the method list, creating string input based on parsed fieldVariables, methods, constructors for plantUML 
		 */

		umlVariables = new PlantUMLClassFinalTemplateBuilder().buildPlantUMLClassFinalTemplate(generatedClassList,fieldAvailable);


		List<NodesConnection> connectedNodesDesc = plantUMLFigure.getConnectedLineswithNodes();

		if(connectedNodesDesc !=null && connectedNodesDesc.size()!=0)
		{
			umlVariables = umlVariables + umlClassConnectionLineBuilder.getConnectedLinesDesc(connectedNodesDesc);
		}


		finalPlantUMLTemplate = "@startuml\nskinparam classAttributeIconSize 0\n" + umlVariables + "\n@enduml";

		SourceStringReader sourceReader = new SourceStringReader(finalPlantUMLTemplate);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputImageFileName+".png");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try 
		{
			sourceReader.generateImage(fos, new FileFormatOption(FileFormat.PNG , false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void generatePlantUMLSequenceDiagram(String sequenceDiagramInputString, String outputImagePath) throws IOException
	{
		
		String finalPlantUMLSeqTemplate = "@startuml\nskinparam classAttributeIconSize 0\n" + sequenceDiagramInputString + "\n@enduml";

		SourceStringReader sourceReader = new SourceStringReader(finalPlantUMLSeqTemplate);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outputImagePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try 
		{
			sourceReader.generateImage(fos, new FileFormatOption(FileFormat.PNG , false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
