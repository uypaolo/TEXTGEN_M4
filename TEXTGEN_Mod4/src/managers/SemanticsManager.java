package managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import components.Component;
import components.InputXMLDocument;

import features.Feature;

public class SemanticsManager {

	public static final String CLAUSE = "clause";
	public static final String COMPONENT = "component";
	public static final String FEATURE = "feature";
	public static final String FEATURES = "features";
	
	//For extracting info from XML
	public static InputXMLDocument readSemanticsDocumentFromFile(File xmlFile){
		SAXBuilder builder = new SAXBuilder();
		
		String documentName = "";
		String category = "";
		
		try{
			Document document = (Document) builder.build(xmlFile);
			Element verseNode = document.getRootElement();
			
			//get name and category
			documentName = verseNode.getAttributeValue("name");
			category = verseNode.getAttributeValue("category");
			
			//comments
			Element commentNode = verseNode.getChild("comments");
			String comments = "";
			if(commentNode != null)
				comments = commentNode.getText().trim();
			
			//get clauses
			List<Element> clauseNodes = (List<Element>) verseNode.getChildren(COMPONENT);
			ArrayList<Component> clauseComponents = new ArrayList<Component>();
			for(Element clauseNode: clauseNodes)
				clauseComponents.add(Component.createInstance(clauseNode, true));
			
			return new InputXMLDocument(xmlFile, category, documentName, comments, clauseComponents);
			
		}catch(Exception e){e.printStackTrace();}
		
		return null;
	}
	
	//gets the input features in the input xml. parameter must be an Element object containing the component with all child components and features inside it
	public static ArrayList<Feature> getSpecifiedFeatures(Element componentElement){
		
		if(componentElement == null)
			return new ArrayList<Feature>();
		
		Element featuresNode = componentElement.getChild(FEATURES);
		
		if(featuresNode == null) //there are no features
			return new ArrayList<Feature>();
		
		String componentName = componentElement.getAttributeValue(Component.ATTRIBUTE_NAME);
		
		List<Element> featureNodes = (List<Element>)featuresNode.getChildren(FEATURE);
		ArrayList<Feature> features = new ArrayList<Feature>();
		for(Element featureNode: featureNodes){
			String featureName = featureNode.getAttributeValue(Feature.ATTRIBUTE_NAME);
			String featureValue = featureNode.getAttributeValue(Feature.ATTRIBUTE_VALUE);
			if(FeatureManager.doesFeatureExist(componentName, featureName)){ //disregard the input feature if it does not exist in the database
				//check if value is in db
				if(FeatureManager.doesFeatureValueExist(componentName, featureName, featureValue))
					features.add(new Feature(featureName, featureValue, FeatureManager.isFeatureStandard(componentName, featureName)));
				else
					features.add(FeatureManager.getDefaultFeatureCopy(componentName, featureName));
			}
		}
		return features;
	}

}