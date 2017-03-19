import java.util.List;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;

/**
 * 
 */

/**
 * @author Abhishek
 *
 */
public class UMLClassConstructorBuilder {
	
	public String getUMLClassConstructor(List<ConstructorDeclaration> constructors)
	{
		String umlVariables = "" ;
		for(ConstructorDeclaration eachConstructor : constructors)
		{
			String constructorName = ((ConstructorDeclaration)eachConstructor).getName();
			//checking whether its a parameterized constructor or default constructor
			if(((ConstructorDeclaration)eachConstructor).getParameters()!=null)
			{
				//umlVariables = umlVariables + "+" + currentMethod.getName() + "(" + ((VariableDeclaratorId)eachParameter.getId()).getName() + " : " + eachParameter.getType() + ")\n";
				List<Parameter> consParameterList = ((ConstructorDeclaration)eachConstructor).getParameters();
				
				for(Parameter eachParameter : consParameterList)
				{
					String constructorParamRefTypeName = ((Parameter)eachParameter).getType().toString();
					String constructorParamVariableName = ((Parameter)eachParameter).getId().toString();
					umlVariables = umlVariables + "+" + constructorName + "(" + constructorParamVariableName + " : " + constructorParamRefTypeName +")\n";
				}
			}
			else
			{
				umlVariables = umlVariables + "+" + constructorName + "()\n";
			}
		}
		
		return umlVariables;
	}

}
