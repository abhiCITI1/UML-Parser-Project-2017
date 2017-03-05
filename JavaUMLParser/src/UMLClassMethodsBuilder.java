import java.lang.reflect.Modifier;
import java.util.List;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.type.ReferenceType;

/**
 * 
 */

/**
 * @author Abhishek
 *
 */
public class UMLClassMethodsBuilder {
	
	public String getUMLClassMethods(List<MethodDeclaration> declaredMethods)
	{
		String umlVariables = "" ;
		
		for(MethodDeclaration currentMethod : declaredMethods)
		{

			if(Modifier.toString(currentMethod.getModifiers()).equals("public"))
			{
				List<Parameter> parameterList = currentMethod.getParameters();
				if(parameterList !=null)
				{
					if(currentMethod.getType().toString().equals("void"))
					{

						for(Parameter eachParameter : parameterList)
						{
							umlVariables = umlVariables + "+" + currentMethod.getName() + "(" + ((VariableDeclaratorId)eachParameter.getId()).getName() + " : " + eachParameter.getType() + ")\n";
						}
					}
					else
					{
						for(Parameter eachParameter : parameterList)
						{
							umlVariables = umlVariables + "+" + currentMethod.getName() + "(" + ((VariableDeclaratorId)eachParameter.getId()).getName() + " : " + eachParameter.getType() + ")" + ":" + currentMethod.getType().toString() + "\n";                       
						}
					}
				}
				else
				{
					if(currentMethod.getType().toString().equals("void"))
					{
							umlVariables = umlVariables + "+" + currentMethod.getName() + "()\n";
					}
					else
					{
							umlVariables = umlVariables + "+" + currentMethod.getName() + "()" + ":" + currentMethod.getType().toString() + "\n";                       
					}
				}
				
			}
		}
		
		return umlVariables;
	}
	
}
