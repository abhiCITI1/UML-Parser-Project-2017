import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;

/**
 * 
 */

/**
 * @author Abhishek
 *
 */
public class PlantUMLClassFinalTemplateBuilder {
	
	private String umlVariables = "" ;
	UMLClassAttributesBuilder umlClassAttributesBuilder = new UMLClassAttributesBuilder();
	UMLClassConstructorBuilder umlClassConstructorBuilder = new UMLClassConstructorBuilder();
	private List<MethodDeclaration> getterSetterMethodList =  new ArrayList<MethodDeclaration>();
	UMLClassMethodsBuilder umlClassMethodsBuilder = new UMLClassMethodsBuilder();
	
	public String buildPlantUMLClassFinalTemplate(List<ClassGeneration> generatedClassList, boolean fieldAvailable)
	{
		
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
			
			if(generatedClass.getConstuctorNames()!=null)
			{
				umlVariables = umlVariables + umlClassConstructorBuilder.getUMLClassConstructor(generatedClass.getConstuctorNames());
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
				fieldAvailable = false;
				umlVariables = umlVariables + umlClassMethodsBuilder.getUMLClassMethods(generatedClass.getMethodNames());
			}
			
			
			
			umlVariables += "}\n";
		}
		return umlVariables;
	}

}
