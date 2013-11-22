package rules;

import java.io.Serializable;
import java.util.ArrayList;

import org.jdom2.Element;

import components.Component;
import features.Feature;

public class FormSelection extends Rules implements Serializable{

	private String desiredForm;
	private String description;
	
	public FormSelection(String ruleName){
		super(ruleName, Rules.FORM_SELECTION);
		desiredForm = "";
	}
	
	public FormSelection(Element e) {
		super(e.getAttributeValue("name"), Rules.FORM_SELECTION);
		setAttributes(e);
	}
	
	private void setAttributes(Element e){
		
		this.ruleName = e.getAttributeValue("name");
		
		Element lhsElement = e.getChild("lhs");
		Element compElement = lhsElement.getChild("component");
		if(compElement != null){
			inputToMatch.clear(); //since FormSelection will only have one input anyway.
			inputToMatch.add(Component.createInstance(compElement, false));
		}
		
		Element formElement = e.getChild("form");
		desiredForm = formElement.getAttributeValue("name");
		
		if(desiredForm == null)
			desiredForm = "";
		
		description = e.getChildTextTrim("description");
		
	}
	
	@Override
	public void apply(Component component) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void generateAdditionalXMLElement(Element baseElement) {
		Element form = new Element("form");
		if(desiredForm != null && !desiredForm.isEmpty())
			form.setAttribute("name", getDesiredForm());
		baseElement.addContent(form);
	
		Element descriptionElement = new Element("description");
		descriptionElement.setText(description);
		baseElement.addContent(descriptionElement);

	}

	@Override
	public void copyFrom(Rules ruleToCopy) {
		if(ruleToCopy instanceof FormSelection){
			FormSelection other = (FormSelection) ruleToCopy;
			this.setAttributes(other.generateXMLElement());
		}
	}

	@Override
	public Rules getCopy() {
		return new FormSelection(this.generateXMLElement());
	}
	
	public String getDesiredForm() {
		return desiredForm;
	}

	public void setDesiredForm(String desiredForm) {
		this.desiredForm = desiredForm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setComponent(Component input){
		inputToMatch = new ArrayList<Component>();
		if(input != null)
			inputToMatch.add(input);
	}


	
}
