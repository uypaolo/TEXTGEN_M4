package rules;

import java.io.Serializable;
import java.util.ArrayList;

import org.jdom2.Element;

import components.Component;
import components.Leaf;

public class SimpleSpellout extends Rules implements Serializable{

	private String spelloutType;
	public static final String TYPE_PREFIX = "prefix";
	public static final String TYPE_SUFFIX = "suffix";
	public static final String TYPE_NEWTRANSLATION = "newtranslation";
	public static final String TYPE_ADDWORD = "addword";
	
	public static final String BASE_FORM = "base";
	
	private String subWord;
	private String description;
	private String form;
	
	
	//toMatchAffix may refer to either prefix or suffix, depending on the mode.
	private String toMatchAffix;

	//whether this should be at the beginning or end of the stem depends on whether we are in prefix or suffix mode.
	private String toMatchStem; 
	
	public SimpleSpellout(String ruleName){
		super(ruleName, Rules.SIMPLE_SPELLOUT);
	}
	
	public SimpleSpellout(Element e) {
		super(e.getAttributeValue("name"), Rules.SIMPLE_SPELLOUT);
		setAttributes(e);
		
	}
	
	private void setAttributes(Element e){
		this.ruleName = e.getAttributeValue("name");
		Element lhsElement = e.getChild("lhs");
		inputToMatch = new ArrayList<Component>();
		inputToMatch.add(Component.createInstance(lhsElement.getChild("component"), false));
		
		Element rhsElement = e.getChild("rhs");
		spelloutType = rhsElement.getAttributeValue("type");
		subWord = rhsElement.getAttributeValue("subWord");
		form = rhsElement.getAttributeValue("form");
		if(form == null)
			form = BASE_FORM;
		
		description = e.getChildTextTrim("description");
	}
	
	public void setInput(Component toMatch){
		inputToMatch = new ArrayList<Component>();
		if(toMatch != null)
			inputToMatch.add(toMatch);
	}
		
	@Override
	public void generateAdditionalXMLElement(Element baseElement) {

		Element rhs = new Element("rhs");
		rhs.setAttribute("type", spelloutType);
		rhs.setAttribute("subWord", getSubWord());
		if(form != null)
		rhs.setAttribute("form", form);
		baseElement.addContent(rhs);
	
		Element descriptionElement = new Element("description");
		descriptionElement.setText(description);
		baseElement.addContent(descriptionElement);

	}

	@Override
	public void copyFrom(Rules ruleToCopy) {
		if(ruleToCopy instanceof SimpleSpellout){

			SimpleSpellout other = (SimpleSpellout) ruleToCopy;
			this.setAttributes(other.generateXMLElement());
			
		}
	}
	
	//Getters
	public String getSpellOutType() {
		return spelloutType;
	}

	public String getSubWord() {
		return subWord;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getForm(){
		return form;
	}
	
	//Setters
	public void setSpellOutType(String type) {
		this.spelloutType = type;
	}
	
	public void setSubWord(String subWord) {
		this.subWord = subWord;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setForm(String form){
		if(form == null)
			form = "";
		this.form = form;
	}
	
	public void setComponent(Component input){
		inputToMatch = new ArrayList<Component>();
		if(input != null)
			inputToMatch.add(input);
	}

	@Override
	public void apply(Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rules getCopy() {
		return new SimpleSpellout(this.generateXMLElement());
	}
}
