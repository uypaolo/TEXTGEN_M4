package features;

import org.jdom2.Element;

public class Feature {

	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_VALUE = "value";
	
	private String name;
	private String value;
	private boolean isStandard;
	
	public Feature(String name, String value, boolean isStandard){
		this.name = name;
		this.value = value;
		if(value == null)
			this.value = "";
		this.isStandard = isStandard;
	}
	
	public Feature getCopy(){
		return new Feature(name, value, isStandard);
	}
	
	public String getName(){
		return name;
	}
	
	public String getValue(){
		return value;
	}

	public void setName(String newName){
		this.name = newName;
	}
	
	public void setValue(String newValue){
		if(newValue != null)
			value = newValue;
	}

	public boolean isStandard(){
		return isStandard;
	}
	
	public Element generateXMLElement(){
		Element featureElement = new Element("feature");
		featureElement.setAttribute(ATTRIBUTE_NAME, name);
		featureElement.setAttribute(ATTRIBUTE_VALUE, value);
		return featureElement;
	}	
}