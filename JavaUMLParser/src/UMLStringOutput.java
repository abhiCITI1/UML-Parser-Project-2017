import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
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
		
		
		/** 
		 * This method invocation is to build the parsed plantUML input based on the class type or interface type and 
		 * to convert the private variable having public setter and getter methods into a public variable and removing those setter getter
		 * methods from the method list, creating string input based on parsed fieldVariables, methods, constructors for planUML 
		 */
		
		umlVariables = new PlantUMLClassFinalTemplateBuilder().buildPlantUMLClassFinalTemplate(generatedClassList,fieldAvailable);
		
			
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
