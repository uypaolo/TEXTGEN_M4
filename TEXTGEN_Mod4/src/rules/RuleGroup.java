package rules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class RuleGroup extends RuleTreeNode implements Serializable{

	
	private ArrayList<RuleGroup> subGroups;
	private ArrayList<RuleNode> ruleNodes;
	
	public static RuleGroup createInstance(Element e){ //expects a group element
		String groupName = e.getAttributeValue("name");
		
		RuleGroup currGroup = new RuleGroup(groupName);
		
		//add child groups
		List<Element> groupElements = e.getChildren("group");
		
		for(Element groupElement: groupElements)
			currGroup.addSubGroup(createInstance(groupElement));
		
		//add rules
		List<Element> ruleElements = e.getChildren("rule");
		for(Element ruleElement: ruleElements){
			Rules ruleObject = Rules.createInstance(ruleElement);
			if(ruleObject != null)
				currGroup.addRuleNode(new RuleNode(ruleObject));
		}
		
		return currGroup;
		
	}
	
	public RuleGroup(String name){
		super(name);
		subGroups = new ArrayList<RuleGroup>();
		ruleNodes = new ArrayList<RuleNode>();
	}
	
	public void addSubGroup(RuleGroup subGroup){
		subGroups.add(subGroup);
	}
	
	public void addRuleNode(RuleNode ruleNode){
		ruleNodes.add(ruleNode);
	}
	
	public boolean moveRuleNodeBefore(RuleNode toMove, RuleNode referenceNode){
		
		if(!ruleNodes.contains(toMove)){
			for(RuleNode rule: ruleNodes)
				if(rule.getName().equals(toMove.getName()))
				{
					toMove = rule;
					break;
				}
		}
		
		if(!ruleNodes.contains(referenceNode)){
			for(RuleNode rule: ruleNodes)
				if(rule.getName().equals(referenceNode.getName()))
				{
					referenceNode = rule;
					break;
				}
		}
	
		if(!(ruleNodes.contains(toMove) && ruleNodes.contains(referenceNode)))
			return false;
		
		ruleNodes.remove(toMove);
		int insertIndex = ruleNodes.indexOf(referenceNode);
		ruleNodes.add(insertIndex, toMove);
		return true;
	}
	
	public void deleteSubGroup(RuleGroup subGroup) {
		subGroups.remove(subGroup);
	}

	@Override
	public Element generateXMLElement() {
		Element baseElement = new Element("group");
		baseElement.setAttribute("name", name);
		
		for(RuleGroup subGroup: subGroups)
			baseElement.addContent(subGroup.generateXMLElement());
		
		for(RuleNode rule: ruleNodes)
			baseElement.addContent(rule.generateXMLElement());
		
		return baseElement;
	}

	@Override
	public String getNameForPrinting() {
		StringBuilder sb = new StringBuilder(name);
		sb.append("\n");
		
		for(RuleGroup group: subGroups){
			sb.append(group.getNameForPrinting());
			sb.append("\n");
		}
		
		for(RuleNode node: ruleNodes){
			sb.append(node.getNameForPrinting());
			sb.append("\n");
		}
		return sb.toString();
		
	}

	public boolean isLeaf() {
		return false;
	}
	
	public ArrayList<RuleGroup> getSubGroups(){
		return (ArrayList<RuleGroup>)subGroups.clone();
	}
	
	public ArrayList<RuleNode> getRules(){
		return (ArrayList<RuleNode>)ruleNodes.clone();
	}
	
	public void removeRule(RuleNode toRemove){
		ruleNodes.remove(toRemove);
	}

	@Override
	public boolean doesRuleNameExist(String ruleName) {
		for(RuleNode rule: ruleNodes)
			if(rule.doesRuleNameExist(ruleName))
				return true;
		
		for(RuleGroup group: subGroups)
			if(group.doesRuleNameExist(ruleName))
				return true;
		
		return false;
	}

	public boolean doesGroupNameExist(String groupName, String exception) {
		for(RuleGroup group: subGroups)
			if(groupName.trim().equalsIgnoreCase(group.getName().trim()) && !groupName.trim().equalsIgnoreCase(exception.trim()))
				return true;

		
		return false;
	}
	
}
