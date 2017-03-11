import java.lang.reflect.Modifier;
import java.util.List;

import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.ReferenceType;

/**
 * @author Abhishek
 **/
public class UMLClassAttributesBuilder {
	
	public String getUMLClassAttributes(List<FieldDeclaration> declaredFields)
	{
		String umlVariables = "" ;
		
		for(FieldDeclaration currentField : declaredFields)
		{
			if(Modifier.toString(currentField.getModifiers()).equals("private"))
			{
				if(currentField.getType() instanceof ReferenceType && !currentField.getType().toString().equals("String"))
				{
					umlVariables = umlVariables + "-" + currentField.getVariables().get(0) + " : " + ((ReferenceType)currentField.getType()).getType() + "(*)\n";
				}
				else if(currentField.getType() instanceof ReferenceType && currentField.getType().toString().equals("String"))
				{
					umlVariables = umlVariables + "-" + currentField.getVariables().get(0)+ " : " + currentField.getType()+ "\n";
				}
				else
				{
					umlVariables = umlVariables + "-" + currentField.getVariables().get(0)+ " : " + currentField.getType() + "\n";
				}
				
			}
			else if(Modifier.toString(currentField.getModifiers()).equals("public"))
			{
				umlVariables = umlVariables + "+" + currentField.getVariables() + " : " + currentField.getType() + "\n";
			}
		}
		
		return umlVariables;
	}
	
	
	
}
