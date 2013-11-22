package rules.action;

import org.jdom2.Element;

import components.Component;

public class DeleteComponent extends Action{

	public static final String TYPE_ROOT = "root";
	public static final String TYPE_NONROOT = "non-root";
	
	private int parentSource;
	private int source;
	private String type;
	
	public DeleteComponent() {
		super(Action.DELETE_COMPONENT);
	}
	
	public DeleteComponent(Element e) {
		super(Action.DELETE_COMPONENT);
		
		actionDescription = e.getAttributeValue("description");
		
		Element deleteElement = e.getChild("delete");
		type = deleteElement.getAttributeValue("type");
		if(type.equals(TYPE_NONROOT))
			parentSource = Integer.parseInt(deleteElement.getAttributeValue("parentSource"));
		source = Integer.parseInt(deleteElement.getAttributeValue("source"));		
	}
	
	public int getParentSource() {
		return parentSource;
	}

	public void setParentSource(int parentSource) {
		this.parentSource = parentSource;
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public boolean isTypeRoot(){
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
		Element baseElement = new Element("delete");
		baseElement.setAttribute("type", type);
		if(type.equals(TYPE_NONROOT))
			baseElement.setAttribute("parentSource", "" + parentSource);
		baseElement.setAttribute("source", "" + source);
		return baseElement;
	}
	
	public void setDescription(Component toDelete){
		if(toDelete != null)
			actionDescription = "Deleted "+toDelete.toRuleSentence()+".";
	}

}
