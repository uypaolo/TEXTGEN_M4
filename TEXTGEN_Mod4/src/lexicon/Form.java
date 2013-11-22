package lexicon;

import org.jdom2.Element;

public class Form {

	private String name;
	private String value;
	
	public Form(String name, String value){
		this.name = name;	
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public void setName(String newName){
		if(newName != null)
			name = newName;
	}
	
	public void setValue(String newValue){
		if(newValue != null)
			value = newValue;
	}

	public Element generateXMLElement(){
		Element formElement = new Element("form");
		
		formElement.setAttribute("name", name);
		formElement.setAttribute("value", value);
		
		return formElement;
	}
	
}
