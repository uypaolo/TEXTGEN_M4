package features;

import java.util.ArrayList;

import org.jdom2.Element;

public class DBFeatureValues {
	
	private String featureName;
	private ArrayList<String> values;
	private boolean isStandard;
	
	public DBFeatureValues(String featureName, boolean isStandard){
		this.featureName = featureName;
		values = new ArrayList<String>();
		this.isStandard = isStandard;
	}
	
	public void addValue(String value){
		values.add(value);
	}
	
	public ArrayList<String> getValues(){
		return (ArrayList<String>)values.clone();
	}

	public Feature getDefaultFeatureCopy(){
		if(values.size() > 0)
			return new Feature(featureName, values.get(0), isStandard);
		return new Feature(featureName, "", isStandard);
	}
	
	public boolean isStandard(){
		return isStandard;
	}
	
	public String getFeatureName(){
		return featureName;
	}
	
	public void setFeatureName(String newName){
		if(newName != null && !newName.trim().isEmpty())
			featureName = newName;
	}
	
	public Element generateXMLElementForLexicon(){
		Element featureElement = new Element("feature");
		featureElement.setAttribute("name", featureName);
		
		for(String value: values){
			Element valueElement = new Element("value");
			valueElement.setText(value);
			featureElement.addContent(valueElement);
		}
		return featureElement;
	}
	
	public void renameValue(String oldValueName, String newValueName){
		if(newValueName != null && !newValueName.isEmpty()){
			int indexToChange = -1;
			for(int i = 0; i < values.size(); i++){
				String value = values.get(i);
				if(value.equals(oldValueName)){
					indexToChange = i;
					break;
				}
			}
			
			if(indexToChange >=0 ){
				values.remove(indexToChange);
				values.add(indexToChange, newValueName);
			}
		}
	}
	
	//for debugging only
	public String print(){
		StringBuilder sb = new StringBuilder("Feature:"+featureName);
		if(isStandard)
			sb.append(" [Standard]: ");
		else
			sb.append(" [User-defined]: ");
		
		for(String val: values)
			sb.append(val+", ");
		
		sb.append("\n");
		return sb.toString();
	}

}