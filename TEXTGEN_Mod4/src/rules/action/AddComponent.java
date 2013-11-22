package rules.action;

import org.jdom2.Element;

import components.Component;

public class AddComponent extends Action{

	public static final String ADD_TYPE_ROOT = "root";
	public static final String ADD_TYPE_NONROOT = "non-root";
	
	private int reference;
	private int insertIndex;
	private Component toAdd;
	private String addType;
	
	public AddComponent() {
		super(Action.ADD_COMPONENT);
		// TODO Auto-generated constructor stub
	}

	
	public AddComponent(Element e) {
		super(Action.ADD_COMPONENT);
		
		actionDescription = e.getAttributeValue("description");

		Element addElement = e.getChild("add");
		addType = addElement.getAttributeValue("type");
		
		if(addType.equals(ADD_TYPE_NONROOT))
			reference = Integer.parseInt(addElement.getAttributeValue("reference"));
		
		insertIndex = Integer.parseInt(addElement.getAttributeValue("insertIndex"));

		Element componentElement = addElement.getChild("component");
		if(componentElement != null)
			toAdd = Component.createInstance(componentElement, false);
	}
	
	public Component getToAdd(){
		return toAdd;
	}
	
	public void setToAdd(Component toAdd){
		this.toAdd = toAdd;
		setDescription();
	}
	
	public int getReference() {
		return reference;
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	public int getInsertIndex() {
		return insertIndex;
	}

	public void setInsertIndex(int insertIndex) {
		this.insertIndex = insertIndex;
	}
	
	public boolean isRootAdd(){
		return ADD_TYPE_ROOT.equals(addType);
	}
	
	public void setAddType(String addType){
		if(addType.equals(ADD_TYPE_ROOT) || addType.equals(ADD_TYPE_NONROOT))
			this.addType = addType;
	}

	@Override
	public void performAction(Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element generateAdditionalXMLElement() {
		Element baseElement = new Element("add");
		baseElement.setAttribute("type", addType);
		baseElement.setAttribute("insertIndex", "" + insertIndex);
		
		if(addType.equals(ADD_TYPE_NONROOT))
			baseElement.setAttribute("reference", ""+reference);
		
		if(toAdd != null)
			baseElement.addContent(toAdd.generateXMLElementForRuleComponent());
		
		return baseElement;
	}

	public int getComponentToAddTag(){
		if(toAdd != null)
			return toAdd.getTag();
		return -1;
	}


	public void setDescription() {
		if(toAdd != null)
			actionDescription = "Added "+toAdd.toRuleSentence();
	}
	
}
