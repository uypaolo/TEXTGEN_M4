package rules.action;

import org.jdom2.Element;

import components.Component;
import components.Phrase;

public class CopyComponent extends Action{

	public static final String TYPE_ROOT = "root";
	public static final String TYPE_NONROOT = "non-root";
	
	public static final int SOURCE_IS_ROOT = -1;;
	
	private int sourceParent;
	private int destinationParent;
	private int inputTag;
	private int outputTag;
	private int insertIndex;
	private String type;
	
	public CopyComponent() {
		super(Action.COPY_COMPONENT);
		// TODO Auto-generated constructor stub
	}
	
	public CopyComponent(Element e) {
		super(Action.COPY_COMPONENT);
		
		actionDescription = e.getAttributeValue("description");
		
		Element copyElement = e.getChild("copy");
		type = copyElement.getAttributeValue("type");
		
		if(type.equals(TYPE_NONROOT))
			destinationParent = Integer.parseInt(copyElement.getAttributeValue("destinationReference"));
		
		String sourceParentTag = copyElement.getAttributeValue("sourceReference");
		if(sourceParentTag != null)
			sourceParent = Integer.parseInt(sourceParentTag);
		else
			sourceParent = SOURCE_IS_ROOT;
		
		inputTag = Integer.parseInt(copyElement.getAttributeValue("inputTag"));
		outputTag = Integer.parseInt(copyElement.getAttributeValue("outputTag"));
		insertIndex = Integer.parseInt(copyElement.getAttributeValue("insertIndex"));
	}
	
	public int getSourceParent() {
		return sourceParent;
	}

	public void setSourceParent(int sourceParent) {
		this.sourceParent = sourceParent;
	}

	public int getDestinationParent() {
		return destinationParent;
	}

	public void setDestinationParent(int destinationParent) {
		this.destinationParent = destinationParent;
	}

	public int getInputTag() {
		return inputTag;
	}

	public void setInputTag(int inputTag) {
		this.inputTag = inputTag;
	}
	
	public int getOutputTag(){
		return outputTag;
	}
	
	public void setOutputTag(int outputTag){
		this.outputTag = outputTag;
	}
	
	public int getInsertIndex() {
		return insertIndex;
	}

	public void setInsertIndex(int insertIndex) {
		this.insertIndex = insertIndex;
	}

	public boolean isTypeRoot(){
		if(type == null)
			return false;
		return type.equals(TYPE_ROOT);
	}
	
	public void setType(String type){
		if(type.equals(TYPE_ROOT) || type.equals(TYPE_NONROOT))
			this.type = type;
	}
	
	
	@Override
	public void performAction(Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element generateAdditionalXMLElement() {
		Element baseElement = new Element("copy");
		baseElement.setAttribute("type", type);
		
		if(type.equals(TYPE_NONROOT))
			baseElement.setAttribute("destinationReference", "" + destinationParent);
		
		baseElement.setAttribute("sourceReference", "" + sourceParent);
		baseElement.setAttribute("inputTag", "" + inputTag);
		baseElement.setAttribute("outputTag", ""+outputTag);
		baseElement.setAttribute("insertIndex", "" + insertIndex);
		
		return baseElement;
	}

	public void setDescription(Phrase sourceParent, Phrase destinationParent, Component sourceComponent){
		if(sourceComponent != null){
			actionDescription = "Moved "+sourceComponent.toRuleSentence();
			actionDescription += " from ";
			if(sourceParent!=null)
				actionDescription += sourceParent.toRuleSentence()+" to ";
			else
				actionDescription += "root to ";
			
			if(destinationParent != null)
				actionDescription += destinationParent.toRuleSentence()+".";
			else
				actionDescription += "root.";
		}
	}


}
