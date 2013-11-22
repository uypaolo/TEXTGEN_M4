package managers;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import features.DBFeatureValues;
import features.Feature;

public class FeatureManager{

	private static final String FEATURES_DB_FILE_PATH = "Databases\\FeaturesDB.xml";
	
	private static LinkedHashMap<String, ArrayList<DBFeatureValues>> featureMap;
	
	//For initializing the featureMap
	public static DBFeatureValues getFeatureValues(Element featureElement){
		
		boolean isStandard = true;
		if(featureElement.getParentElement().getAttributeValue("type").equals("user"))
			isStandard = false;
		
		String featureName = featureElement.getAttributeValue("name");
		List<Element> valueElements = featureElement.getChildren("value");
		
		DBFeatureValues featureValues = new DBFeatureValues(featureName, isStandard);
		
		for(Element valueElement: valueElements )
			featureValues.addValue(valueElement.getTextTrim());
		
		return featureValues;
	}
	
	public static LinkedHashMap<String, ArrayList<DBFeatureValues>> getFeatureMap(){
		if(featureMap == null){			
			File xmlFile = new File(FEATURES_DB_FILE_PATH);
			SAXBuilder builder = new SAXBuilder();
			featureMap = new LinkedHashMap<String, ArrayList<DBFeatureValues>>();
			try{
				Document document = (Document) builder.build(xmlFile);
				
				List<Element> componentElements = document.getRootElement().getChildren("component");
				
				for(Element componentElement: componentElements){
					List<Element> featureGroups = componentElement.getChildren("featuregroup");
					
					ArrayList<DBFeatureValues> featureValuesList = new ArrayList<DBFeatureValues>(); 
					
					for(Element featureGroup: featureGroups){
						List<Element> features = featureGroup.getChildren("feature");
						
						for(Element feature: features)
							featureValuesList.add(getFeatureValues(feature));
					}
					
					featureMap.put(componentElement.getAttributeValue("name").toLowerCase(), featureValuesList);
				}
			}catch(Exception e){e.printStackTrace();}
		}
		return featureMap;
	}
	
	//For getting the default features / features
	public static ArrayList<Feature> getDefaultFeatures(String componentName){
		//InitializeDefaults
		ArrayList<DBFeatureValues> featureValuesList = getFeatureMap().get(componentName.toLowerCase());
		if(featureValuesList == null)
			return new ArrayList<Feature>();
	
		ArrayList<Feature> defaultFeatures = new ArrayList<Feature>();
		
		for(DBFeatureValues fv: featureValuesList)
			defaultFeatures.add(fv.getDefaultFeatureCopy());
		
		return defaultFeatures;
	}
	
	public static DBFeatureValues getFeatureValues(String componentName, String featureName){
		ArrayList<DBFeatureValues> featureValues = getFeatureMap().get(componentName.toLowerCase());
		for(DBFeatureValues fv: featureValues)
			if(fv.getFeatureName().equals(featureName))
				return fv;
		
		return null;
	}
	
	
	//Methods for checking details about the features
	public static boolean doesFeatureExist(String componentName, String featureName){
		ArrayList<DBFeatureValues> featureValuesList = getFeatureMap().get(componentName.toLowerCase());
		if(featureValuesList == null)
			return false;
		
		for(DBFeatureValues featureValues: featureValuesList){
			if(featureValues.getFeatureName().equals(featureName))
				return true;
		}
		
		return false;
	}

	public static boolean doesFeatureValueExist(String componentName, String featureName, String featureValue){
		
		if(doesFeatureExist(componentName, featureName)){
			DBFeatureValues featureValues = getFeatureValues(componentName, featureName);
			
			ArrayList<String> values = featureValues.getValues();
			for(String value: values)
				if(value.equals(featureValue)) //the value exists
					return true;
			
			return false;
		}
		return false; //feature doesn't exist
	}
	
	public static Feature getDefaultFeatureCopy(String componentName, String featureName){
		if(doesFeatureExist(componentName, featureName)){
			return getFeatureValues(componentName, featureName).getDefaultFeatureCopy();
		}
		return null; 
	}
	
	public static boolean isFeatureDefault(String componentName,String featureName, String value){
		//InitializeDefaults
		if(doesFeatureExist(componentName, featureName)){
			Feature defaultFeatureCopy = getFeatureValues(componentName, featureName).getDefaultFeatureCopy();
			if(defaultFeatureCopy.getValue().equals(value))
				return true;
		}
		return false; // feature doesn't exist or value is not default
	}
	
	public static boolean isFeatureStandard(String componentName, String featureName){
		if(doesFeatureExist(componentName, featureName)){
			return getFeatureValues(componentName, featureName).isStandard();	
		}
		return false;
	}
}