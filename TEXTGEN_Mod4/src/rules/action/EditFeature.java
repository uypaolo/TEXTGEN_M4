package rules.action;

import org.jdom2.Element;

import components.Component;

public class EditFeature extends Action{

	private int target;
	private String name;
	private String value;
	
	public EditFeature() {
		super(Action.EDIT_FEATURE);
	}
	
	public EditFeature(Element e) {
		super(Action.EDIT_FEATURE);
		
		actionDescription = e.getAttributeValue("description");
		
		Element editElement = e.getChild("editFeature");
		target = Integer.parseInt(editElement.getAttributeValue("target"));
		
		Element featureElement = editElement.getChild("feature");
		name = featureElement.getAttributeValue("name");
		value = featureElement.getAttributeValue("value");
		
	}
	
	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public void performAction(Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element generateAdditionalXMLElement() {
		Element baseElement = new Element("editFeature");
		baseElement.setAttribute("target", "" + target);
		
		Element featureElement = new Element("feature");
		featureElement.setAttribute("name", name);
		featureElement.setAttribute("value", value);
		baseElement.addContent(featureElement);
		
		return baseElement;
	}
	
	public void setDescription(Component targetComponent){
		actionDescription = "Set "+targetComponent.toRuleSentence()+" feature: "+name+" to "+value+".";
	}

}
