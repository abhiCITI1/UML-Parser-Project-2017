
/**
 * @author Abhishek
 * This class is responsible to build the notations
 * i.e the Multiplicity Notations(*), the association
 * Notations(one-to-one, one-to-many), the attributes visibility notations
 * (public, private, package, protected)
 */
public class PlantUMLNotations {

	/**
	 * @return the dependency
	 */
	public static String getDependency() {
		return DEPENDENCY;
	}

	/**
	 * @return the one to many multiplicity
	 */
	public static String getOnetomanymultiplicity() {
		return ONETOMANYMULTIPLICITY;
	}
	/**
	 * @return the many to many multiplicity
	 */
	public static String getManytomanymultiplicity() {
		return MANYTOMANYMULTIPLICITY;
	}
	/**
	 * @return the association
	 */
	public static String getAssociation() {
		return ASSOCIATION;
	}
	/**
	 * @return the extends
	 */
	public static String getExtends() {
		return EXTENDS;
	}
	/**
	 * @return the implements
	 */
	public static String getImplements() {
		return IMPLEMENTS;
	}

	private static final String ONETOMANYMULTIPLICITY = "*";
	private static final String MANYTOMANYMULTIPLICITY = "*--";
	private static final String ASSOCIATION = "--";
	private static final String DEPENDENCY = "<..";
	private static final String EXTENDS = "<|--";
	private static final String IMPLEMENTS = "<|..";
	
}
