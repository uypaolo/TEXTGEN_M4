package rules;

import java.io.Serializable;
import java.util.ArrayList;

import org.jdom2.Element;

import components.Component;

public class Morphophonemic extends Rules implements Serializable {

	
	public static final String MORPHEME_TYPE_PREFIX = "prefix";
	public static final String MORPHEME_TYPE_SUFFIX = "suffix";
	public static final String AFFIX_CHANGE_TYPE_DOESNT_CHANGE = "doesn't change";
	public static final String AFFIX_CHANGE_TYPE_PART_CHANGES = "part changes";
	public static final String AFFIX_CHANGE_TYPE_ENTIRE_CHANGES ="entire changes";
	
	public static final String STEM_CHANGE_TYPE_DOESNT_CHANGE = "doesn't change";
	public static final String STEM_CHANGE_TYPE_ENTIRE_CHANGES = "entire changes";
	
	private String morphemeType; // prefix | suffix
	private String changeType; // prefix/suffix doesnt change | beginning/end of prefix changes | entire
	private String oldValue; // prefix - prefix/end of prefix | suffix - beginning of suffix
	private String newValue; // new end of prefix | new beginning of suffix
	private String stemType; // stem change | stem doesnt change
	private String stemBeginOrEnd; // beginning of stem(prefix) | new beginning of stem(prefix)
								// end of stem(suffix) | new end of stem(suffix)
 	private String stemNewBeginOrEnd; // if(changes) prefix - new beginning of stem | suffix - new end of stem
	private String description;
 	
	public Morphophonemic(String ruleName){
		super(ruleName, Rules.MORPHOPHONEMIC);
	}
	
	public Morphophonemic(Element e){
		super(e.getAttributeValue("name"), Rules.MORPHOPHONEMIC);
		setAttributes(e);
	}

	private void setAttributes(Element e){	
		this.ruleName = e.getAttributeValue("name");
		Element lhsElement = e.getChild("lhs");
		Element compElement = lhsElement.getChild("component");
		inputToMatch.clear();
		if(compElement != null)
			inputToMatch.add(Component.createInstance(compElement, false));

		Element rhsElement = e.getChild("rhs");
		morphemeType = rhsElement.getAttributeValue("morphemeType");
		changeType = rhsElement.getAttributeValue("changeType");
		oldValue = rhsElement.getAttributeValue("oldValue");
		
		if(rhsElement.getAttributeValue("newValue") != null)
			newValue = rhsElement.getAttributeValue("newValue");
		
		Element stemElement = e.getChild("stem");
		stemType = stemElement.getAttributeValue("stemType");
		stemBeginOrEnd = stemElement.getAttributeValue("beginOrEnd");
		
		if(stemElement.getAttributeValue("newBeginOrEnd") != null);
			stemNewBeginOrEnd = stemElement.getAttributeValue("newBeginOrEnd");

		description = e.getChildTextTrim("description");
	}
	
	public String getMorphemeType() {
		return morphemeType;
	}

	public void setMorphemeType(String morphemeType) {
		this.morphemeType = morphemeType;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getStemType() {
		return stemType;
	}

	public void setStemType(String stemType) {
		this.stemType = stemType;
	}

	public String getStemBeginOrEnd() {
		return stemBeginOrEnd;
	}

	public void setStemBeginOrEnd(String stemBeginOrEnd) {
		this.stemBeginOrEnd = stemBeginOrEnd;
	}

	public String getStemNewBeginOrEnd() {
		return stemNewBeginOrEnd;
	}

	public void setStemNewBeginOrEnd(String stemNewBeginOrEnd) {
		this.stemNewBeginOrEnd = stemNewBeginOrEnd;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	public void generateAdditionalXMLElement(Element baseElement) {
		
	//	if(getChangeType != null)
		if(getChangeType().equals("doesn't change")) {
			Element rhs = new Element("rhs");
			rhs.setAttribute("morphemeType", getMorphemeType());
			rhs.setAttribute("changeType", getChangeType());
			rhs.setAttribute("oldValue", getOldValue());
			baseElement.addContent(rhs);
		}
		else {
			Element rhs = new Element("rhs");
			if(getMorphemeType() != null)
				rhs.setAttribute("morphemeType", getMorphemeType());
			rhs.setAttribute("changeType", getChangeType());
			rhs.setAttribute("oldValue", getOldValue());
			rhs.setAttribute("newValue", getNewValue());
			baseElement.addContent(rhs);
		}
		
		
		if(getStemType().equals("doesn't change")){
			Element stem = new Element("stem");
			stem.setAttribute("stemType", getStemType());
			stem.setAttribute("beginOrEnd", getStemBeginOrEnd());
			baseElement.addContent(stem);
		}else {
			Element stem = new Element("stem");
			stem.setAttribute("stemType", getStemType());
			stem.setAttribute("beginOrEnd", getStemBeginOrEnd());
			stem.setAttribute("newBeginOrEnd", getStemNewBeginOrEnd());
			baseElement.addContent(stem);
		}
		
		Element descriptionElement = new Element("description");
		descriptionElement.setText(description);
		baseElement.addContent(descriptionElement);

	}

	@Override
	public void copyFrom(Rules ruleToCopy) {
		if(ruleToCopy instanceof Morphophonemic){
			Morphophonemic other = (Morphophonemic) ruleToCopy;
			this.setAttributes(other.generateXMLElement());
		}
	}

	@Override
	public Rules getCopy() {
		return new Morphophonemic(this.generateXMLElement());
	}

}
