package rules;

import java.io.Serializable;

import org.jdom2.Element;

public class RuleNode extends RuleTreeNode implements Serializable{

	private Rules rule;
	
	public RuleNode(Rules rule){
		super(rule.getRuleName());
		this.rule = rule;
	}

	public Rules getRule(){
		return rule;
	}
	
	@Override
	public Element generateXMLElement() {
		return rule.generateXMLElement();
	}

	@Override
	public String getNameForPrinting() {
		return name;
	}
	
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean doesRuleNameExist(String ruleName) {
		if(ruleName.trim().equalsIgnoreCase(rule.getRuleName()))
			return true;
		return false;
	}

	
}
