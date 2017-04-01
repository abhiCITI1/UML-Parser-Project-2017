import java.util.List;

import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;

/**
 * @author Abhishek
 *
 */
public class ClassGeneration {
	
	private String className;
	private String interfaceName;
	private List<MethodDeclaration> methodNames;
	private List<FieldDeclaration> fieldNames;
	private List<ConstructorDeclaration> constuctorNames;
	private String parameterName;
	private boolean interfaceFlag;
	
	public ClassGeneration(String name) {
		// TODO Auto-generated constructor stub
		super();
		this.className = name;
	}
	public ClassGeneration() {
		// TODO Auto-generated constructor stub
		super();
	}
	/**
	 * @return the interfaceFlag
	 */
	public boolean isInterfaceFlag() {
		return interfaceFlag;
	}
	/**
	 * @param interfaceFlag the interfaceFlag to set
	 */
	public void setInterfaceFlag(boolean interfaceFlag) {
		this.interfaceFlag = interfaceFlag;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	/**
	 * @return the methodNames
	 */
	public List<MethodDeclaration> getMethodNames() {
		return methodNames;
	}
	/**
	 * @param methodNames the methodNames to set
	 */
	public void setMethodNames(List<MethodDeclaration> methodNames) {
		this.methodNames = methodNames;
	}
	/**
	 * @return the fieldNames
	 */
	public List<FieldDeclaration> getFieldNames() {
		return fieldNames;
	}
	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(List<FieldDeclaration> fieldNames) {
		this.fieldNames = fieldNames;
	}
	/**
	 * @return the constuctorNames
	 */
	public List<ConstructorDeclaration> getConstuctorNames() {
		return constuctorNames;
	}
	/**
	 * @param constuctorNames the constuctorNames to set
	 */
	public void setConstuctorNames(List<ConstructorDeclaration> constuctorNames) {
		this.constuctorNames = constuctorNames;
	}
	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}
	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	

}
