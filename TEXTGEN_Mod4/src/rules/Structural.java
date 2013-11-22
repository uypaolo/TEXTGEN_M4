package rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import rules.action.Action;
import rules.action.CopyComponent;
import rules.action.CopyFeature;
import rules.action.DeleteComponent;
import rules.action.EditFeature;
import rules.action.MoveComponent;
import rules.action.AddComponent;


import components.Component;
import components.Phrase;
import features.Feature;

public class Structural extends Rules implements Serializable{
	
	private ArrayList<Action> actions;
	private ArrayList<Component> output;
	private String description;
	
	public Structural(String ruleName){
		super(ruleName, Rules.STRUCTURAL);
		actions = new ArrayList<Action>();
		output = new ArrayList<Component>();
	}
	
	public Structural(Element e) {
		this(e.getAttributeValue("name"));
		setAttributes(e);

	}
	
	private void setAttributes(Element e){
		this.ruleName = e.getAttributeValue("name");
		
		//input to match
		inputToMatch.clear();
		Element lhsElement = e.getChild("lhs");
		List<Element> componentElements = lhsElement.getChildren("component");
		for(Element componentElement: componentElements)
			inputToMatch.add(Component.createInstance(componentElement, false));
		
		//output
		output.clear();
		Element outputElement = e.getChild("output");
		List<Element> compElements = outputElement.getChildren("component");
		for(Element componentElement: compElements)
			output.add(Component.createInstance(componentElement, false));

		//rhs
		actions.clear();
		Element rhsElement = e.getChild("rhs");
		List<Element> actionElements = rhsElement.getChildren("action");
		if(actionElements != null){
			for(Element actionElement: actionElements){
				String actionType = actionElement.getAttributeValue("type");
				Action currAction = null;
				if(actionType.equals(Action.ADD_COMPONENT))
					currAction = new AddComponent(actionElement);
				else if(actionType.equals(Action.COPY_COMPONENT))
					currAction = new CopyComponent(actionElement);
				else if(actionType.equals(Action.COPY_FEATURE))
					currAction = new CopyFeature(actionElement);
				else if(actionType.equals(Action.DELETE_COMPONENT))
					currAction = new DeleteComponent(actionElement);
				else if(actionType.equals(Action.EDIT_FEATURE))
					currAction = new EditFeature(actionElement);
				else if(actionType.equals(Action.MOVE_COMPONENT))
					currAction = new MoveComponent(actionElement);
				
				if(currAction != null)
					actions.add(currAction);
			}
		}
				
		description = e.getChildTextTrim("description");
	}

	@Override
	public void apply(Component component) {
		// TODO Auto-generated method stub
	}

	@Override
	public void generateAdditionalXMLElement(Element baseElement) {		
		//output
		Element outputElement = new Element("output");
		for(Component root: output)
			outputElement.addContent(root.generateXMLElementForRuleComponent());
		baseElement.addContent(outputElement);
		
		//rhs
		Element rhsElement = new Element("rhs");
		//generate actions here
		for(Action action: actions)
			rhsElement.addContent(action.generateXMLElement());
		baseElement.addContent(rhsElement);
		
		Element descriptionElement = new Element("description");
		descriptionElement.setText(description);
		baseElement.addContent(descriptionElement);

	}

	@Override
	public void copyFrom(Rules ruleToCopy) {
		if(ruleToCopy instanceof Structural){
			Structural other = (Structural) ruleToCopy;
			this.setAttributes(other.generateXMLElement());
		}
	}

	//setters to be called by GUI
	public void setInputToMatch(ArrayList<Component> inputToMatch){
		this.inputToMatch = (ArrayList<Component>)inputToMatch.clone();
		
	}
	
	public void setOutput(ArrayList<Component> output){
		this.output = (ArrayList<Component>)output.clone();
	}
	
	public ArrayList<Component> getOutput(){
		return (ArrayList<Component>) output.clone();
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Rules getCopy() {
		return new Structural(this.generateXMLElement());
	}
	
	//actions
	
	public void clearActions(){
		actions.clear();
	}
	
	//for insert,move, and copy, set their insert indices when output has been finalized (user saved rule). 
	public void insertComponentAsNonRoot(Phrase parentComponent, Component toInsert){
		AddComponent addComponentAction = new AddComponent();
		addComponentAction.setReference(parentComponent.getTag());
		addComponentAction.setToAdd(toInsert);
		addComponentAction.setAddType(AddComponent.ADD_TYPE_NONROOT);
		actions.add(addComponentAction);
	}
	
	public void insertComponentAsRoot(Component toInsert){
		AddComponent addComponentAction = new AddComponent();
		addComponentAction.setToAdd(toInsert);
		addComponentAction.setAddType(AddComponent.ADD_TYPE_ROOT);
		actions.add(addComponentAction);
	}
	
	//assumed to be non-root for now
	public void moveComponent(Phrase sourceParent, Phrase destinationParent, Component sourceComponent){
		MoveComponent moveComponent = new MoveComponent();
		if(sourceParent != null) //source parent may be null. if it is, that means the component is already a root.
			moveComponent.setSourceParent(sourceParent.getTag());
		moveComponent.setDestinationParent(destinationParent.getTag());
		moveComponent.setInputTag(sourceComponent.getTag());
		moveComponent.setType(MoveComponent.TYPE_NONROOT);
		moveComponent.setDescription(sourceParent, destinationParent, sourceComponent);
		actions.add(moveComponent);
	}
	
	public void moveComponentAsRoot(Phrase sourceParent, Component sourceComponent){
		MoveComponent moveComponent = new MoveComponent();
		if(sourceParent != null) //source parent may be null. if it is, that means the component is already a root.
			moveComponent.setSourceParent(sourceParent.getTag());
		moveComponent.setInputTag(sourceComponent.getTag());
		moveComponent.setType(MoveComponent.TYPE_ROOT);
		moveComponent.setDescription(sourceParent, null, sourceComponent);
		actions.add(moveComponent);
	}
	
	
	//copy is like move but without delete
	public void copyComponent(Phrase sourceParent, Phrase destinationParent, Component sourceComponent){
		CopyComponent copyComponent = new CopyComponent();
		if(sourceParent != null) //source parent may be null. if it is, that means the component is already a root.
			copyComponent.setSourceParent(sourceParent.getTag());
		copyComponent.setDestinationParent(destinationParent.getTag());
		copyComponent.setInputTag(sourceComponent.getTag());
		copyComponent.setType(CopyComponent.TYPE_NONROOT);
		copyComponent.setDescription(sourceParent, destinationParent, sourceComponent);
		actions.add(copyComponent);
	}
	
	public void copyComponentAsRoot(Phrase sourceParent,  Component sourceComponent){
		CopyComponent copyComponent = new CopyComponent();
		if(sourceParent != null) //source parent may be null. if it is, that means the component is already a root.
			copyComponent.setSourceParent(sourceParent.getTag());
		copyComponent.setInputTag(sourceComponent.getTag());
		copyComponent.setType(CopyComponent.TYPE_ROOT);
		copyComponent.setDescription(sourceParent, null, sourceComponent);
		actions.add(copyComponent);
	}
	
	public void deleteComponent(Phrase sourcePhrase, Component componentToDelete){
		//if not original component (part of input), remove the insert/copy/or move action
		//because we can infer that it was inserted through these three actions.
		
		
		if(this.getComponentInInputByTag(componentToDelete.getTag()) == null){
			ArrayList<Action> actionsToRemove = new ArrayList<Action>();
			
			for(Action action: actions){
				String actionType = action.getActionType(); 
				if(actionType.equals(Action.MOVE_COMPONENT)){
					MoveComponent moveAction = (MoveComponent)action;
					if(moveAction.getInputTag() == componentToDelete.getTag()) 
						actionsToRemove.add(moveAction);
				}	
				else if(actionType.equals(Action.COPY_COMPONENT)){
					CopyComponent copyAction = (CopyComponent) action;
					if(copyAction.getInputTag() == componentToDelete.getTag())
						actionsToRemove.add(copyAction);
				}
				else if(actionType.equals(Action.ADD_COMPONENT)){
					AddComponent addAction = (AddComponent)action;
					if( addAction.getComponentToAddTag() != -1 && addAction.getComponentToAddTag() == componentToDelete.getTag()){
						actionsToRemove.add(addAction);
					}
				}
			}
			
			for(Action toRemove: actionsToRemove)
				actions.remove(toRemove);
		}
		else{
			DeleteComponent deleteComponent = new DeleteComponent();
			deleteComponent.setParentSource(sourcePhrase.getTag());
			deleteComponent.setSource(componentToDelete.getTag());
			deleteComponent.setType(DeleteComponent.TYPE_NONROOT);
			deleteComponent.setDescription(componentToDelete);
			actions.add(deleteComponent);
		}
	}
	
	private Component getComponentInInputByTag(int tag){
		for(Component input: inputToMatch)
			if(input.getComponentByTag(tag) != null)
				return input;
		
		return null;
	}
	
	public void deleteComponentAsRoot(Component componentToDelete){
		//if not original component, remove the insert/copy/or move action
		if(this.getComponentInInputByTag(componentToDelete.getTag()) == null){
			ArrayList<Action> actionsToRemove = new ArrayList<Action>();
			
			for(Action action: actions){
				String actionType = action.getActionType(); 
				if(actionType.equals(Action.MOVE_COMPONENT)){
					MoveComponent moveAction = (MoveComponent)action;
					if(moveAction.getInputTag() == componentToDelete.getTag()) 
						actionsToRemove.add(moveAction);
				}	
				else if(actionType.equals(Action.COPY_COMPONENT)){
					CopyComponent copyAction = (CopyComponent) action;
					if(copyAction.getInputTag() == componentToDelete.getTag())
						actionsToRemove.add(copyAction);
				}
				else if(actionType.equals(Action.ADD_COMPONENT)){
					AddComponent addAction = (AddComponent)action;
					if( addAction.getComponentToAddTag() != -1 && addAction.getComponentToAddTag() == componentToDelete.getTag()){
						actionsToRemove.add(addAction);
					}
				}
			}
			
			for(Action toRemove: actionsToRemove)
				actions.remove(toRemove);
		}
		else{
			DeleteComponent deleteComponent = new DeleteComponent();
			deleteComponent.setSource(componentToDelete.getTag());
			deleteComponent.setType(DeleteComponent.TYPE_ROOT);
			deleteComponent.setDescription(componentToDelete);
			actions.add(deleteComponent);
		}
	}

	//features
	public void editFeature(Component targetComponent, String featureName, String featureValue){
		//need to check if the action has already been logged. if yes, overwrite the first one.
		//will still work even with duplicates kasi mahuhuli yung last na edit value, so, kahit walang error checking, will probably work.
		boolean checkDuplicate = false;
		
		for(Action action : actions) {
			if(action.getActionType().equals(Action.EDIT_FEATURE)) {
				int dupliTarget =((EditFeature)action).getTarget();
				String dupliFeatureName = ((EditFeature)action).getName();
				if(dupliTarget == targetComponent.getTag() && dupliFeatureName.equals(featureName)) {
					((EditFeature)action).setValue(featureValue);
					((EditFeature)action).setDescription(targetComponent);
					checkDuplicate = true;
					break;
				}
			}
		}
		
		if(!checkDuplicate){
			EditFeature editFeature = new EditFeature();
			editFeature.setTarget(targetComponent.getTag());
			editFeature.setName(featureName);
			editFeature.setValue(featureValue);
			editFeature.setDescription(targetComponent);
			actions.add(editFeature);
		}
	}
	
	public void copyFeature(Component sourceComponent, Component destinationComponent, String featureName, String newFeatureName){
		//not implemented in ui yet.
		
		boolean checkDuplicate = false;
		
		for(Action action : actions) {
			if(action.getActionType().equals(Action.COPY_FEATURE)) {
				int dupliSource =((CopyFeature)action).getSource();
				int dupliDestination =((CopyFeature)action).getDestination();
				String dupliOldFeatureName = ((CopyFeature)action).getName();
				if(dupliSource == sourceComponent.getTag() && dupliDestination == destinationComponent.getTag() &&
						dupliOldFeatureName.equals(featureName)) {
					((CopyFeature)action).setNewFeatureName(newFeatureName);
					checkDuplicate = true;
					break;
				}
			}
		}
		
		if(!checkDuplicate) {
			CopyFeature copyFeature = new CopyFeature();
			copyFeature.setSource(sourceComponent.getTag());
			copyFeature.setDestination(destinationComponent.getTag());
			copyFeature.setName(featureName);
			copyFeature.setNewFeatureName(newFeatureName);
			copyFeature.setDescription(sourceComponent, destinationComponent);
			actions.add(copyFeature);
		}

	}
	
	public void removeFeature(Component sourceComponent, String featureName){
		//(1)removed feature regularly (meaning remove the editFeature action)
		//(2) removed feature that was copied from another component.
		// either do complicated lookups in the actions, or when a copy feature has happened in the ui, do not actually copy the feature! just show in text form the copy feature action. this way we can assume the situation is always (1)
	
		Action toRemove = null;
		
		for(Action action: actions){
			if(action.getActionType().equals(Action.EDIT_FEATURE)){
				EditFeature editFeatureAction = (EditFeature) action;
				if(editFeatureAction.getTarget() == sourceComponent.getTag() && editFeatureAction.getName().equals(featureName)){
					toRemove = editFeatureAction;
					break;
				}
			}
		}
		if(toRemove != null)
			actions.remove(toRemove);
	}

	public String getActionLog(){
		String log = "";
		for(Action a : actions){
			log += a.toString() + "\n";
		}
		return log;
	}
	
}
