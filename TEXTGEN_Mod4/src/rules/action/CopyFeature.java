package rules.action;

import org.jdom2.Element;

import components.Component;

public class CopyFeature extends Action{

	private int source;
	private int destination;
	private String name;
	private String newFeatureName;
	
	public CopyFeature() {
		super(Action.COPY_FEATURE);
	}
	
	public CopyFeature(Element e) {
		super(Action.COPY_FEATURE);
		
		actionDescription = e.getAttributeValue("description");
		
		Element copyElement = e.getChild("copyFeature");
		source = Integer.parseInt(copyElement.getAttributeValue("source"));
		destination = Integer.parseInt(copyElement.getAttributeValue("destination"));
		
		Element featureElement = copyElement.getChild("feature");
		name = featureElement.getAttributeValue("name");
		newFeatureName = featureElement.getAttributeValue("newFeatureName");
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNewFeatureName() {
		return newFeatureName;
	}

	public void setNewFeatureName(String newFeatureName) {
		this.newFeatureName = newFeatureName;
	}


	@Override
	public void performAction(Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element generateAdditionalXMLElement() {
		Element baseElement = new Element("copyFeature");
		baseElement.setAttribute("source", "" + source);
		baseElement.setAttribute("destination", "" + destination);
		
		Element featureElement = new Element("feature");
		featureElement.setAttribute("name", name);
		featureElement.setAttribute("newFeatureName", newFeatureName);
		baseElement.addContent(featureElement);
		
		return baseElement;
	}

	public void setDescription(Component sourceComponent, Component destinationComponent){
		if(sourceComponent != null && destinationComponent != null){
			actionDescription = "Copied feature:"+name+" as "+newFeatureName+" from "+sourceComponent.toRuleSentence()+" to "+destinationComponent.toRuleSentence()+".";
		}
	}

}
