package managers;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import features.Feature;

import rules.RuleGroup;
import rules.RuleNode;
import rules.RuleTreeNode;
import rules.Rules;

public class RuleManager {
	
	//Singleton
	private static RuleManager instance;
	
	public static RuleManager getInstance(){
		if(instance == null)
			instance = new RuleManager();
		return instance;
	}
	
	//Regular attributes/methods
	
	private RuleTreeNode rootNode;
	private static final String DB_RULES_PATH = "Databases\\RulesDB.xml";
	
	private RuleManager(){
		loadRules();
	}
	
	public void loadRules(){
		SAXBuilder builder = new SAXBuilder();
		try{
			Document document = (Document) builder.build(new File(DB_RULES_PATH));
			Element rootGroupElement = document.getRootElement();	
			rootNode = RuleGroup.createInstance(rootGroupElement);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public boolean saveRulesToXML(){
		Element rootElement = rootNode.generateXMLElement();
		return XMLManager.getInstance().writeToXML(DB_RULES_PATH, rootElement);
	}
	
	public RuleTreeNode getRootNode(){
		return rootNode;
	}
	
	// Methods for manipulating rules. The methods return true if manipulation was successful, false otherwise.
	// Do necessary error checking inside these methods. For example, rule names must be unique.
	// IMPORTANT: if successful, call the method "saveRulesToXML()" to update the database.
	
	public boolean addRule(RuleGroup parentGroup, RuleNode newRule){
		//check if rule name already exists
		if(rootNode.doesRuleNameExist(newRule.getName()))
			return false;
		
		parentGroup.addRuleNode(newRule);
		if(saveRulesToXML())
			return true;
		
		//undo the add because saving to db cannot be done
		parentGroup.removeRule(newRule);
		return false;
	}
	
	public boolean moveRule(RuleGroup sourceGroup, RuleGroup destinationGroup, RuleNode ruleToMove){
		if(!rootNode.doesRuleNameExist(ruleToMove.getName())) //nothing to move if the rule doesn't exist
			return false;
		
		destinationGroup.addRuleNode(ruleToMove);
		sourceGroup.removeRule(ruleToMove);
		
		if(saveRulesToXML())
			return true;
		
		//undo the changes
		destinationGroup.removeRule(ruleToMove);
		sourceGroup.addRuleNode(ruleToMove);
		return false;
	}
	
	public boolean moveRuleWithinSameGroup(RuleGroup sourceGroup, RuleNode toMove, RuleNode referenceNode){
		if(!sourceGroup.moveRuleNodeBefore(toMove, referenceNode))
			return false;
		return saveRulesToXML();
	}
	
	public boolean deleteRule(RuleGroup parentGroup, RuleNode ruleToDelete){
		//check if ruletodelete does exist
		if(!parentGroup.getRules().contains(ruleToDelete))
			return false;

		parentGroup.removeRule(ruleToDelete);
		saveRulesToXML();
		return true;
	}
	
	public boolean editRule(Rules oldRule, Rules newRule){
		String originalRuleName = oldRule.getRuleName();
		oldRule.setRuleName(""); // just so the search won't count the target rule as a "duplicate"
		if(rootNode.doesRuleNameExist(newRule.getRuleName()))
			return false;
		oldRule.setRuleName(originalRuleName);

		if(oldRule.getClass().equals(newRule.getClass()))
			oldRule.copyFrom(newRule);
		
		return saveRulesToXML();
	}
	
	public boolean addGroup(RuleGroup parentGroup, RuleGroup newChildGroup){
		
		if(newChildGroup != null && !newChildGroup.getName().isEmpty() && !parentGroup.doesGroupNameExist(newChildGroup.getName(), "")) {
			parentGroup.addSubGroup(newChildGroup);
			saveRulesToXML();
			return true;
		}
		
		return false;
	}
	
	public boolean deleteGroup(RuleGroup parentGroup, RuleGroup groupToDelete){
		
		if(groupToDelete != null) {
			parentGroup.deleteSubGroup(groupToDelete);
			saveRulesToXML();
			return true;
		}
			
		return false;
	}
	
	public boolean editGroupName(RuleGroup group, String desiredGroupName){
		String originalGroupName = group.getName();
		if(!desiredGroupName.isEmpty() && !((RuleGroup)rootNode).doesGroupNameExist(desiredGroupName, originalGroupName)) {
			group.setName(desiredGroupName);
			saveRulesToXML();
			return true;
		}
		
		return false;
	}
}
