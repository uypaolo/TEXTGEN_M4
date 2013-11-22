package rules.action;

import org.jdom2.Element;

import components.Component;
import components.Phrase;

public class MoveComponent extends Action{

	public static final String TYPE_ROOT = "root";
	public static final String TYPE_NONROOT = "non-root";
	
	public static final int SOURCE_IS_ROOT = -1;;
	
	private int sourceParent;
	private int destinationParent;
	private int inputTag;
	private int insertIndex;
	private String type;
	
	public MoveComponent() {
		super(Action.MOVE_COMPONENT);
		sourceParent = SOURCE_IS_ROOT;
	}
	
	public MoveComponent(Element e) {
		super(Action.MOVE_COMPONENT);
		
		actionDescription = e.getAttributeValue("description");
		
		Element moveElement = e.getChild("move");
		type = e.getAttributeValue("type");
		if(type.equals(TYPE_NONROOT))
			destinationParent = Integer.parseInt(moveElement.getAttributeValue("destinationParent"));
		
		String sourceParentTag = moveElement.getAttributeValue("sourceParent");
		if(sourceParentTag != null)
			sourceParent = Integer.parseInt(sourceParentTag);
		else
			sourceParent = SOURCE_IS_ROOT;
		inputTag = Integer.parseInt(moveElement.getAttributeValue("inputTag"));
		insertIndex = Integer.parseInt(moveElement.getAttributeValue("insertIndex"));
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
	
	public int getInsertIndex(){
		return insertIndex;
	}
	
	public void setInsertIndex(int insertIndex){
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
		Element baseElement = new Element("move");
		baseElement.setAttribute("type", type);
		baseElement.setAttribute("sourceParent", "" + sourceParent);
		if(type.equals(TYPE_NONROOT))
			baseElement.setAttribute("destinationParent", "" + destinationParent);
		baseElement.setAttribute("inputTag", "" + inputTag);
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
