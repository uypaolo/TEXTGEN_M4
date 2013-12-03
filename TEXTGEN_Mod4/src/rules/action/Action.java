package rules.action;

import org.jdom2.Element;

import components.Component;

public abstract class Action {

	private String actionType;
	protected String actionDescription;
	
	public final static String ADD_COMPONENT = "add component";
	public final static String COPY_COMPONENT = "copy component";
	public final static String DELETE_COMPONENT = "delete component";
	public final static String MOVE_COMPONENT = "move component";
	public final static String COPY_FEATURE = "copy feature";
	public final static String EDIT_FEATURE = "edit feature";
	
	protected Action(String actionType){
		this.actionType = actionType;
		actionDescription = "";
	}
	
	public abstract void performAction(Component component);
	
	public Element generateXMLElement(){
		Element actionElement = new Element("action");
		actionElement.setAttribute("type", actionType);
		actionElement.setAttribute("description", actionDescription);
		actionElement.addContent(generateAdditionalXMLElement());
		return actionElement;
	}
	
	public String getActionType() {
		return actionType;
	}
	
	public abstract Element generateAdditionalXMLElement();
	
	public String toString(){
		if(actionDescription == null)
			return "";
		return actionDescription;
	}
}
