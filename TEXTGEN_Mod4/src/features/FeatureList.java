package features;

import java.util.ArrayList;

import lexicon.LexiconList;
import managers.FeatureManager;

import org.jdom2.Element;

public class FeatureList {
	private ArrayList<Feature> featureList;
	
	public FeatureList(ArrayList<Feature> featureList){
		this.featureList = featureList;
		if(featureList == null)
			this.featureList = new ArrayList<Feature>();
	}
	
	/*like set feature but doesn't check for duplicate feature names. just ignored dupliate feature name+value combination
		used for creating components to match in rules
	*/
	public void addFeature(Feature newFeature){
		for(Feature feature: featureList){
			if(feature.getName().equals(newFeature.getName()) && feature.getValue().equals(newFeature.getValue()))
				return; //don't do the add if there is already a feature with same name and value
		}
		
		featureList.add(newFeature.getCopy()); //just to make sure that when newFeature is modified outside, the saved copy is not altered
	}
	
	public void setFeature(Feature newFeature){
		for(Feature feature: featureList){
			if(feature.getName().equals(newFeature.getName())){
				feature.setValue(newFeature.getValue());
				return;
			}
		}
		
		//New Feature is user-defined
		featureList.add(newFeature.getCopy());
	}
	
	//gets the first feature having the given featureName. used in actual semantics (because there will only be 
	//one feature given a certain name. 
	public Feature getFeature(String featureName){
		for(Feature feature: featureList)
			if(feature.getName().equals(featureName))
				return feature;
		
		return null;
	}
	
	//gets all features having this featureName. used for rules (to implement the "or"s in matching criteria for features)
	public ArrayList<Feature> getAllFeatureInstances(String featureName){
		ArrayList<Feature> instances = new ArrayList<Feature>();
		for(Feature feature: featureList)
			if(feature.getName().equals(featureName))
				instances.add(feature);
		
		return instances;
	}
	
	public ArrayList<Feature> getFeatureList(){
		return featureList;
	}

	public Element generateXMLElementForRuleComponent(){
		Element featuresElement = new Element("features");
		for(Feature feature: featureList)
			featuresElement.addContent(feature.generateXMLElement());
			
		if(featuresElement.getChildren("feature").size() == 0)
			return null;

		return featuresElement;
	}
	
	public Element generateXMLElementForComponent(String componentName){
		
		Element featuresElement = new Element("features");
	
		for(Feature feature: featureList)
			if(!FeatureManager.isFeatureDefault(componentName, feature.getName(), feature.getValue()))
				featuresElement.addContent(feature.generateXMLElement());
			
		if(featuresElement.getChildren("feature").size() == 0)
			return null;
		return featuresElement;
	}
	
	public Element generateXMLElementForLexicon(LexiconList parentList){
		Element featuresElement = new Element("features");
		
		for(Feature feature: featureList){
			if(!parentList.isFeatureDefault(feature))
				featuresElement.addContent(feature.generateXMLElement());
		}
			
		if(featuresElement.getChildren("feature").size() > 0)
			return featuresElement;
		
		return null;
	}

	public void renameFeatureName(String oldFeatureName, String newFeatureName){
		if(newFeatureName != null && !newFeatureName.trim().isEmpty()){
			for(Feature feature: featureList){
				if(feature.getName().equals(oldFeatureName))
					feature.setName(newFeatureName);
			}
		}
	}
	
	public void renameFeatureValue(String featureName, String oldFeatureValue, String newFeatureValue){
		if(newFeatureValue != null && !newFeatureValue.trim().isEmpty()){
			for(Feature feature: featureList){
				if(feature.getName().equalsIgnoreCase(featureName)){
					if(feature.getValue().equalsIgnoreCase(oldFeatureValue))
						feature.setValue(newFeatureValue);
					break;
				}
			}
		}
	}
	
	public void removeFeature(String featureName){
		Feature target = getFeature(featureName);
		if(target != null)
			featureList.remove(target);
	}
}