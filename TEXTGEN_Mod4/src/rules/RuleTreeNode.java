package rules;

import org.jdom2.Element;

public abstract class RuleTreeNode {

	protected String name; //name of rule or group depending on the actual object
	
	public RuleTreeNode() {
		
	}
	
	protected RuleTreeNode(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public abstract Element generateXMLElement();
	
	public abstract String getNameForPrinting();
	
	public abstract boolean isLeaf();
	
	public abstract boolean doesRuleNameExist(String ruleName);

}
