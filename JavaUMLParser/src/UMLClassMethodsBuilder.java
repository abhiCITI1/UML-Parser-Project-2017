import java.util.List;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;

/**
 * 
 */

/**
 * @author Abhishek
 *
 */
public class UMLClassMethodsBuilder {
	
	private List<MethodDeclaration> plantUMLBodyOfMethods;

	/**
	 * @return the plantUMLBodyOfMethods
	 */
	public List<MethodDeclaration> getPlantUMLBodyOfMethods() {
		return plantUMLBodyOfMethods;
	}

	/**
	 * @param plantUMLBodyOfMethods the plantUMLBodyOfMethods to set
	 */
	public void setPlantUMLBodyOfMethods(List<MethodDeclaration> plantUMLBodyOfMethods) {
		this.plantUMLBodyOfMethods = plantUMLBodyOfMethods;
	}

	
	
	
	
}
