package components;

import java.util.ArrayList;
import java.util.List;

import managers.ComponentManager;
import managers.FeatureManager;
import managers.SemanticsManager;

import ontology.PartOfSpeech;

import org.jdom2.Element;

import features.Feature;
import features.FeatureList;

public abstract class Component {
	
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_TAG = "tag";
	
	public static final int NO_TAG = -1;
	protected int tag;
	
	protected ComponentInfo info;
	protected String name;
	protected FeatureList featureList;

	protected boolean autoAddDefaultFeaturesInCreation;
	
	//Constructor
	protected Component(Element componentElement, boolean autoAddDefaultFeatures){
		
		//Set the features defined in the XML
		this.name = componentElement.getAttributeValue(ATTRIBUTE_NAME);
		
		if(autoAddDefaultFeatures)
			this.featureList = new FeatureList(FeatureManager.getDefaultFeatures(name));
		else
			this.featureList = new FeatureList(new ArrayList<Feature>());
		
		//get info (may be null for now as input xml may contain user defined phrases)
		info = ComponentManager.getInstance().getComponentInfo(name);

		//get tag
		String tagString = componentElement.getAttributeValue(ATTRIBUTE_TAG);
		if(tagString == null)
			tag = NO_TAG;
		else
			tag = Integer.parseInt(tagString);
		
		//override defaults
		ArrayList<Feature> specifiedFeatures = SemanticsManager.getSpecifiedFeatures(componentElement);
		for(Feature feature: specifiedFeatures)
			//setFeature(feature);
			addFeature(feature); //used add to accomodate creation of components in rules
		
	}
		
	protected Component(String componentName, boolean autoAddDefaultFeatures){
		this.name = componentName;
		this.autoAddDefaultFeaturesInCreation = autoAddDefaultFeatures;
		info = ComponentManager.getInstance().getComponentInfo(name);
		if(autoAddDefaultFeatures)
			this.featureList = new FeatureList(FeatureManager.getDefaultFeatures(componentName));
		else
			this.featureList = new FeatureList(new ArrayList<Feature>());
		tag = NO_TAG;
	}
	
	//this creator creates a component with all default features. this was made for adding new components in the editor.
	public static Component createInstance(String componentName, boolean autoAddDefaultFeatures){
		Component newComponent;
		
		if(ComponentManager.getInstance().isLeaf(componentName))
			newComponent = new Leaf(componentName, autoAddDefaultFeatures);
		else
			newComponent = new Phrase(componentName, autoAddDefaultFeatures);
		
		return newComponent;
	}
	
	//this creator is used for creating components from a loaded XML file
	public static Component createInstance(Element e, boolean autoAddDefaultFeatures){
		String componentName = e.getAttributeValue(ATTRIBUTE_NAME);
		
		if(ComponentManager.getInstance().isLeaf(componentName)){
			return new Leaf(e, autoAddDefaultFeatures);
		}
		else{
			List<Element> childrenElements = (List<Element>)e.getChildren(SemanticsManager.COMPONENT);
			if(childrenElements == null)
				childrenElements = new ArrayList<Element>();

			Phrase phrase = new Phrase(e, autoAddDefaultFeatures);
			for(Element child: childrenElements)
				phrase.addChild(createInstance(child, autoAddDefaultFeatures));
			
			return phrase;
		}
	}
		
	//Abstract Methods
	protected abstract String getFeatures(boolean includeDefaults, String nextLineToken);
	
	protected abstract void addAdditionalXMLContent(Element parentElement);
	
	public abstract String toString();

	public abstract String toGeneratedString();
	
	public abstract String toConceptSentence();
	
	public abstract String toLexiconSentence();
	

	
	public abstract boolean isLeaf();
	
	public abstract Children getChildren();
	
	//Getters

	public String toRuleSentence(){
		if(tag != NO_TAG)
			return tag+"-"+toString();
		return toString();
	}
	
	public abstract Component getComponentByTag(int tag);
	
	public Component getCopy(){
		return Component.createInstance(generateXMLElement(), this.autoAddDefaultFeaturesInCreation);
	}
	
	public Component getCopyWithoutFeatures(){
		Component copy = this.getCopy();
		copy.featureList = new FeatureList(new ArrayList<Feature>());
		return copy;
	}
	
	public String getDescription(){
		if(info != null)
			return info.getDescription();
		return name;
	}
	
	public String getName(){
		return name;
	}

	public PartOfSpeech getPOS(){
		return new PartOfSpeech(info.getName(), info.getDescription());
	}
	
	public String getPOSCode(){
		return info.getName();
	}
	
	public String getFeaturesInHTML(boolean includeDefaults){
		return getFeatures(includeDefaults, "<br>");
	}
	
	public String getFeaturesInString(boolean includeDefaults) {
		return getFeatures(includeDefaults, "\n");
	}
	
	public Feature getFeature(String featureName){
		if(featureList == null)
			return null;
		
		return featureList.getFeature(featureName);
	}

	public ArrayList<Feature> getAllFeatureInstances(String featureName){
		if(featureList == null)
			return new ArrayList<Feature>();
		
		return featureList.getAllFeatureInstances(featureName);
	}

	public abstract int getMaxTag();
	
	public int getTag(){
		return tag;
	}

	//public abstract int retrieveComponentWithTag(int tag);
	
	//recursive function that tags the tree depth first. used for tagging components for the creation of structural rules
	public abstract int setTag(int nextTag);
	
	//Setters
	public void setFeature(Feature newFeature){
		featureList.setFeature(newFeature);
	}

	/*This is like setFeature but it simply adds (doesn't check for duplicates etc).
	 * To be used for matching input in rules
	 */
	public void addFeature(Feature newFeature){
		featureList.addFeature(newFeature);
	}
	
	public void removeFeature(String featureName){
		featureList.removeFeature(featureName);
	}
	
	//Methods related to generating XML file
	public Element generateXMLElement(){
		Element xmlElement = new Element("component");
		xmlElement.setAttribute(ATTRIBUTE_NAME, name);
		
		//tag
		if(tag != NO_TAG)
			xmlElement.setAttribute(ATTRIBUTE_TAG, ""+tag);
		
		if(featureList != null){
			Element featuresElement = featureList.generateXMLElementForComponent(name);
			if(featuresElement != null)
				xmlElement.addContent(featuresElement);
		}
		addAdditionalXMLContent(xmlElement);
		return xmlElement;
	}
	
	public Element generateXMLElementForRuleComponent(){
		Element xmlElement = new Element("component");
		xmlElement.setAttribute(ATTRIBUTE_NAME, name);
		
		//tag
		if(tag != NO_TAG)
			xmlElement.setAttribute(ATTRIBUTE_TAG, ""+tag);
		
		if(featureList != null){
			Element featuresElement = featureList.generateXMLElementForRuleComponent();
			
			if(featuresElement != null)
				xmlElement.addContent(featuresElement);
		}
		addAdditionalXMLContent(xmlElement);
		return xmlElement;	
	}
		
	public ArrayList<Feature> getFeatures(){
		return featureList.getFeatureList();
	}
}