package rules;

import java.io.Serializable;
import java.util.ArrayList;

import org.jdom2.Element;

import components.Component;

public abstract class Rules implements Serializable{

	
	public static final String STRUCTURAL = "structural";
	public static final String SIMPLE_SPELLOUT = "simple spellout";
	public static final String FORM_SELECTION = "form selection";
	public static final String MORPHOPHONEMIC = "morphophonemic";
	public static final String FEATURE_COPYING = "feature copying";
	
	
	private boolean isOn;
	protected ArrayList<Component> inputToMatch;
	protected String ruleName;
	protected String ruleType;

	public static Rules createInstance(Element e){
		String ruleType = e.getAttributeValue("type");
		
		if(ruleType.equals(STRUCTURAL))
			return new Structural(e);
		
		if(ruleType.equals(SIMPLE_SPELLOUT))
			return new SimpleSpellout(e);
		
		if(ruleType.equals(FORM_SELECTION))
			return new FormSelection(e);
		
		if(ruleType.equals(MORPHOPHONEMIC))
			return new Morphophonemic(e);
		
		return null;
	}
	
	protected Rules(String ruleName, String ruleType){
		this.ruleName = ruleName;
		this.ruleType = ruleType;
		inputToMatch = new ArrayList<Component>();
	}
	
	public ArrayList<Component> getInputToMatch(){
		return (ArrayList<Component>)inputToMatch.clone();
	}
	
	public String getRuleName(){
		return ruleName;
	}
	
	public String getType(){
		return ruleType;
	}
	
	public abstract Rules getCopy();
	
	public void setRuleName(String newRuleName){
		ruleName = newRuleName;
	}

	//just a helper method to get a copy of the inputToMatch (as in totally new object instances).
	public ArrayList<Component> getCopyOfInputToMatch(){
		ArrayList<Component> componentCopies = new ArrayList<Component>();
		
		for(Component comp: inputToMatch)
			componentCopies.add(comp.getCopy());
		
		return componentCopies;
		
	}
	
	public Component getFirstComponentInInputToMatch(){
		if(inputToMatch != null && inputToMatch.size() > 0)
			return inputToMatch.get(0);
		
		return null;
	} 
		
	public boolean isOn(){
		return isOn;
	}
	
	public void setStatus(boolean isOn){
		this.isOn = isOn;
	}
	
	public abstract void apply(Component component);

	//this method will make the rule copy all the necessary attributes from ruleToCopy.
	// Important: create new instances of objects. For example, for inputToMatch
	public abstract void copyFrom(Rules ruleToCopy);
	
	public Element generateXMLElement(){
		Element baseElement = new Element("rule");
		baseElement.setAttribute("name", ruleName);
		baseElement.setAttribute("type", ruleType);
		baseElement.setAttribute("status", "" + isOn);
		
		Element lhs = new Element("lhs");
		for(Component input: inputToMatch)
			lhs.addContent(input.generateXMLElementForRuleComponent());
		baseElement.addContent(lhs);

		generateAdditionalXMLElement(baseElement);
		return baseElement;
	}
	
	public abstract void generateAdditionalXMLElement(Element ruleElement);
}
