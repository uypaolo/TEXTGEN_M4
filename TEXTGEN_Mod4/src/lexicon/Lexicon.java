package lexicon;

import java.util.List;

import managers.LexiconManager;
import managers.OntologyManager;

import org.jdom2.Element;

import features.Feature;
import features.FeatureList;

public class Lexicon implements Comparable {
	
	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_GLOSS = "gloss";
	private static final String CHILD_COMMENTS = "comments";
	private static final String CHILD_SAMPLE_SENTENCE = "sentence";
	
	private LexiconList parentLexiconList;
	private String name;
	private FormList formList;
	private FeatureList featureList;
	private String gloss;
	private String comments;
	private String sampleSentence;
	private String mappedConceptName;
	private String mappedConceptSense;
	private String mappedConceptPos;
	
	//Constructors/ Initialize methods
	
	public Lexicon(LexiconList parentLexiconList, String name, String gloss){
		this.parentLexiconList = parentLexiconList;
		this.name = name;
		this.gloss = gloss;
		this.mappedConceptName = "";
		this.mappedConceptSense = "";
		
		featureList = parentLexiconList.getDefaultFeatureList();
		formList = parentLexiconList.getDefaultFormList(name);
	}
	
	public Lexicon(LexiconList parentLexiconList, Element stemElement){
		this.parentLexiconList = parentLexiconList;
		
		name = stemElement.getAttributeValue(ATTRIBUTE_NAME);
	    gloss = stemElement.getAttributeValue(ATTRIBUTE_GLOSS);

	    if(name == null)
	    	name = "";
	    if(gloss == null)
	    	gloss = "";

	    
	    comments = stemElement.getChildTextTrim(CHILD_COMMENTS);
	    sampleSentence = stemElement.getChildTextTrim(CHILD_SAMPLE_SENTENCE);
	    
	    if(comments == null)
	    	comments = "";
	    if(sampleSentence == null)
	    	sampleSentence = "";
	    
	    initFeatures(stemElement);
	    initForms(stemElement);
	    initMapping(stemElement);
	}
	
	private void initFeatures(Element stemElement){
		if(parentLexiconList != null){
			this.featureList = parentLexiconList.getDefaultFeatureList(); //defaults
	        
		    if(stemElement != null){
		    	 //set new features
			    Element featuresElement = stemElement.getChild("features");
			    if(featuresElement != null){
			    	List<Element> featureElements = featuresElement.getChildren("feature");
			    	
			    	for(Element featureElement: featureElements){
			    		String featureName = featureElement.getAttributeValue("name");
			    		String featureValue = featureElement.getAttributeValue("value");
			    		if(featureName == null)
			    			featureName = "";
			    		if(featureValue == null)
			    			featureValue = "";
			    		featureList.setFeature(new Feature(featureName, featureValue, false));
			   
			    	}
			    }
		    }
		}
	}
	
	private void initForms(Element stemElement){
		if(parentLexiconList != null)
		{
		    this.formList = parentLexiconList.getDefaultFormList(name); //defaults
		    
		    Element formsElement = stemElement.getChild("forms");
		    if(formsElement != null){
		    	List<Element> formElements = formsElement.getChildren("form");
		    	
		    	for(Element formElement: formElements)
		    		formList.setForm(new Form(formElement.getAttributeValue("name"), formElement.getAttributeValue("value")));
		    }
		}
	}
	
	private void initMapping(Element stemElement){
		Element mappingsElement = stemElement.getChild("mappings");
		
		if(mappingsElement != null){ //put mapping inside mappingsElement because there might be a need to have multiple mappings in the future
			Element mappingElement = mappingsElement.getChild("mapping");
			mappedConceptName = mappingElement.getAttributeValue("concept");
			mappedConceptSense = mappingElement.getAttributeValue("sense");		
			mappedConceptPos = mappingElement.getAttributeValue("pos");
			
			if(mappedConceptName == null)
				mappedConceptName = "";
			if(mappedConceptSense == null)
				mappedConceptSense = "";
			if(mappedConceptPos == null)
				mappedConceptSense = "";
			
			//remove mapping if the concept doesn't exist
			if(!OntologyManager.getInstance().doesConceptExist(mappedConceptPos, mappedConceptName, mappedConceptSense)){
				mappedConceptName = "";
				mappedConceptSense = "";
				mappedConceptPos = "";
			}
		}
		else{
			mappedConceptName = "";
			mappedConceptSense = "";
			mappedConceptPos = "";
		}
	}

	//Getters
	public String getName() {
		return name;
	}

	public String getGloss() {
		return gloss;
	}

	public String getComments(){
		return comments;
	}
	
	public String getSampleSentence(){
		return sampleSentence;
	}
	
	public FeatureList getFeatureList() {
		return featureList;
	}

	public String getMappedConceptName(){
		return mappedConceptName;
	}
	
	public String getMappedConceptSense() {
		return mappedConceptSense;
	}

	public String getMappedConceptPos() {
		return mappedConceptPos;
	}

	public String getMappedConcept() {
		if(mappedConceptName.isEmpty() || mappedConceptSense.isEmpty() || mappedConceptPos.isEmpty())
			return "";
		
		return LexiconManager.combineIntoString(mappedConceptPos, mappedConceptName, mappedConceptSense);
	}

	public FormList getFormList(){
		return formList;
	}
	
	public LexiconList getParentLexiconList(){
		return parentLexiconList;
	}
	
	//setters
	
	public void removeMapping(){
		mappedConceptName = "";
		mappedConceptSense = "";
		mappedConceptPos = "";
		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
		LexiconManager.getInstance().refresh();
	}
	
	public void setMapping(String concept, String sense, String pos){
		//only perform set if the concept-sense exists
		if(OntologyManager.getInstance().doesConceptExist(concept, sense)){
			mappedConceptName = concept;
			mappedConceptSense = sense;
			mappedConceptPos = pos;
			if(parentLexiconList != null)
				parentLexiconList.saveToXML();
			LexiconManager.getInstance().refresh();
		}
	}
	
	public void setName(String newName){
		if(name.equals(newName))
			return;
		
		if(parentLexiconList != null && !parentLexiconList.doesLexiconExist(newName))
			this.name = newName;

		parentLexiconList.saveToXML();
	}
	
	public void setGloss(String newGloss){
		if(newGloss != null)
			gloss = newGloss;

		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}
	
	public void setComments(String newComments){
		if(newComments != null)
			comments = newComments;

		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}
	
	public void setSampleSentence(String newSampleSentence){
		if(newSampleSentence != null)
			comments = newSampleSentence;

		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}
	
	//XML Elements
	public Element generateXMLElement(){
		Element stemElement = new Element("stem");
		stemElement.setAttribute(ATTRIBUTE_NAME, name);
		stemElement.setAttribute(ATTRIBUTE_GLOSS, gloss);
		
		Element commentsElement = new Element(CHILD_COMMENTS);
		commentsElement.setText(comments);
		stemElement.addContent(commentsElement);
		
		Element sampleSentenceelement = new Element(CHILD_SAMPLE_SENTENCE);
		sampleSentenceelement.setText(sampleSentence);
		stemElement.addContent(sampleSentenceelement);
		
		Element featuresElement = featureList.generateXMLElementForLexicon(parentLexiconList);
		if(featuresElement != null)
			stemElement.addContent(featuresElement);
		
		Element formsElement = formList.generateXMLElement();
		if(formsElement != null)
			stemElement.addContent(formsElement);
		
		if(!mappedConceptName.isEmpty() && !mappedConceptSense.isEmpty() && !mappedConceptPos.isEmpty()){
			Element mappingsElement = new Element("mappings");
			
			Element mappingElement = new Element("mapping");
			mappingElement.setAttribute("concept", mappedConceptName);
			mappingElement.setAttribute("sense",mappedConceptSense);
			mappingElement.setAttribute("pos", mappedConceptPos);
			
			mappingsElement.addContent(mappingElement);
			stemElement.addContent(mappingsElement);
		}
		
		return stemElement;
	}

	//Manipulating Features
	public void setFeature(String featureName, String featureValue){
		featureList.setFeature(new Feature(featureName, featureValue, false));
		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}
	
	public void renameFeatureName(String oldFeatureName, String newFeatureName){
		featureList.renameFeatureName(oldFeatureName, newFeatureName);
		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}
	
	public void renameFeatureValue(String featureName, String oldFeatureValue, String newFeatureValue){
		featureList.renameFeatureValue(featureName, oldFeatureValue, newFeatureValue);
		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}

	//Manipulating Forms
	public void setForm(String formName, String formValue){
		formList.setForm(new Form(formName, formValue));
		if(parentLexiconList != null)
			parentLexiconList.saveToXML();
	}
	
	
	public int compareTo(Object o){
		Lexicon otherLexicon = (Lexicon)o;
		
		return name.compareTo(otherLexicon.getName());
	}
}