package lexicon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managers.LexiconManager;
import managers.XMLManager;

import org.jdom2.Element;

import features.DBFeatureValues;
import features.Feature;
import features.FeatureList;

public class LexiconList {

	private String pos;
	private ArrayList<DBFeatureValues> possibleFeatures;
	private ArrayList<String> possibleForms;
	private ArrayList<Lexicon> lexiconList;
	private HashMap<String, ArrayList<Lexicon>> conceptToLexiconMap;
	
	//Initialization
	public LexiconList(Element posElement){
		pos = posElement.getAttributeValue("name");
		if(pos == null)
			pos = "";
		
		initPossibleFeatures(posElement);
		initPossibleForms(posElement);
		initLexiconList(posElement);
		initializeConceptToLexiconMap();
	}
	
	private void initPossibleFeatures(Element posElement){
		possibleFeatures = new ArrayList<DBFeatureValues>();
		
		Element featuresElement = posElement.getChild("features");
		if(featuresElement != null){
			
			List<Element> featureElements = featuresElement.getChildren("feature");
			for(Element featureElement: featureElements){
				String featureName = featureElement.getAttributeValue("name");
				if(featureName == null)
					featureName = "";
				DBFeatureValues currFeatureValues = new DBFeatureValues(featureName, false);
				
				List<Element> valueElements = featureElement.getChildren("value");
				
				if(valueElements != null)
					for(Element valueElement: valueElements)
						currFeatureValues.addValue(valueElement.getTextTrim());
			

				possibleFeatures.add(currFeatureValues);
			}
		}
	}
	
	private void initPossibleForms(Element posElement){
		possibleForms = new ArrayList<String>();
		
		Element formsElement = posElement.getChild("forms");
		
		if(formsElement != null){
			List<Element> formElements = formsElement.getChildren("form");
			for(Element formElement: formElements)
				possibleForms.add(formElement.getTextTrim());
		}
	}
	
	private void initLexiconList(Element posElement){
		lexiconList = new ArrayList<Lexicon>();
		
		Element stemsElement = posElement.getChild("stems");
		if(stemsElement != null){
			List<Element> stemElements = stemsElement.getChildren("stem");
			if(stemElements != null){
				for(Element stemElement: stemElements){
					lexiconList.add(new Lexicon(this, stemElement));
				}
			}
		}
	}
	
	//Mapping
	public void initializeConceptToLexiconMap(){
		conceptToLexiconMap = new HashMap<String, ArrayList<Lexicon>> ();
		
		//loop through lexiconlist
		for(Lexicon lexicon: lexiconList){
			String currMappedConcept = lexicon.getMappedConcept();
			
			if(!currMappedConcept.isEmpty()){ // this lexicon is mapped to some concept
				ArrayList<Lexicon> listToAddTo;
				if(conceptToLexiconMap.containsKey(currMappedConcept))
					listToAddTo = conceptToLexiconMap.get(currMappedConcept);
				else
					listToAddTo = new ArrayList<Lexicon>(); //create new list if this is the first time
				
				listToAddTo.add(lexicon); //add this lexicon to the list
				conceptToLexiconMap.put(currMappedConcept, listToAddTo); //add new or overwrite previous entry
			}
		}
		
		//sort in alphabetical order the lexicons
		for (Map.Entry<String, ArrayList<Lexicon>> entry : conceptToLexiconMap.entrySet()){
			Collections.sort(entry.getValue());
		}
	}

	//Manipulating Lexicons
	public void addLexicon(String lexiconName, String gloss){
		Lexicon newLexicon = new Lexicon(this, lexiconName, gloss);
		
		//insertion sort
		int insertIndex = 0; //first by default
		int listSize = lexiconList.size();
		int i;
		
		for(i=0; i < listSize; i++){
			Lexicon curr = lexiconList.get(i);
			if(newLexicon.compareTo(curr) <= 0){
				insertIndex = i;
				break;
			}
		}
		
		if(i == listSize) //last
			insertIndex = i;
		
		lexiconList.add(insertIndex, newLexicon);
		saveToXML();
	}
	
	public void removeLexicon(Lexicon lexicon){
		lexiconList.remove(lexicon);
		saveToXML();
	}

	//Basic Getters
	public String getPOS() {
		return pos;
	}

	public ArrayList<DBFeatureValues> getPossibleFeatures() {
		return possibleFeatures;
	}

	public ArrayList<String> getPossibleForms() {
		return possibleForms;
	}

	public ArrayList<Lexicon> getLexiconList() {
		return lexiconList;
	}
		
	public HashMap<String, ArrayList<Lexicon>> getConceptToLexiconMap() {
		return conceptToLexiconMap;
	}

	public ArrayList<String> getFeatureValues(String featureName){
		for(DBFeatureValues fv: possibleFeatures){
			if(fv.getFeatureName().equals(featureName)){
				return fv.getValues();
			}
		}
		return new ArrayList<String>();
	}
	
	public Lexicon getLexiconInstance(String lexiconName){
		for(Lexicon lex: lexiconList){
			if(lex.getName().equals(lexiconName))
				return lex;
		}
		return null;
	}
	
	//Checkers
	public boolean doesLexiconExist(String lexicon){
		for(Lexicon l: lexiconList)
			if(l.getName().equals(lexicon))
				return true;
		
		return false;
	}
	
	//For XML Element Generation
	public Element generateXMLElement(){
		Element rootElement = new Element("pos");
		rootElement.setAttribute("name", pos);
		rootElement.addContent(generateFeaturesXMLElement());
		rootElement.addContent(generateFormsXMLElement());
		rootElement.addContent(generateStemsXMLElement());
		return rootElement;
	}
	
	private Element generateFeaturesXMLElement(){
		Element featuresElement = new Element("features");
		for(DBFeatureValues featureValues: possibleFeatures)
			featuresElement.addContent(featureValues.generateXMLElementForLexicon());
		
		return featuresElement;
	}
	
	private Element generateFormsXMLElement(){
		Element formsElement = new Element("forms");
		
		for(String form: possibleForms){
			Element formElement = new Element("form");
			formElement.setText(form);
			formsElement.addContent(formElement);
		}
		
		return formsElement;
	}
	
	private Element generateStemsXMLElement(){
		//lexicons
		Element stemsElement = new Element("stems");
		
		for(Lexicon lexicon: lexiconList){
			Element stemElement = lexicon.generateXMLElement();
			if(stemElement != null)
				stemsElement.addContent(stemElement);
		}
		
		return stemsElement;
	}
	
	//Manipulating Features
	public void renameFeatureName(String oldFeatureName, String newFeatureName){
		//perform rename on possibleFeatures
		for(DBFeatureValues featureValues: possibleFeatures)
			if(featureValues.getFeatureName().equals(oldFeatureName))
				featureValues.setFeatureName(newFeatureName);
		
		//perform rename on all lexicons
		for(Lexicon lexicon: lexiconList)
			lexicon.renameFeatureName(oldFeatureName, newFeatureName);
		
		saveToXML();
	}
	
	public void renameFeatureValue(String featureName, String oldFeatureValue, String newFeatureValue){
		//perform rename on possible features
		for(DBFeatureValues featureValues: possibleFeatures)
			if(featureValues.getFeatureName().equals(featureName))
				featureValues.renameValue(oldFeatureValue, newFeatureValue);
			
		//perform rename on all lexicons
		for(Lexicon lexicon: lexiconList)
			lexicon.renameFeatureValue(featureName, oldFeatureValue, newFeatureValue);
		
		saveToXML();
	}
	
	public void addNewPossibleFeature(DBFeatureValues featureValues){
		possibleFeatures.add(featureValues);
		Feature defaultFeature = featureValues.getDefaultFeatureCopy();
		
		//update all lexicons to have default value for this new possible feature
		if(defaultFeature != null){
			for(Lexicon lexicon: lexiconList)
				lexicon.getFeatureList().setFeature(defaultFeature);
		}
		saveToXML();
	}

	public void addNewPossibleFeatureValue(String featureName, String newValue){
		DBFeatureValues featureValues = getDBFeatureValues(featureName);
		if(featureValues != null)
			featureValues.addValue(newValue);
		
		saveToXML();
	}

	public void deletePossibleFeature(String featureName){
		DBFeatureValues featureValues = getDBFeatureValues(featureName);
		if(featureValues != null)
			possibleFeatures.remove(featureValues);
		
		//remove from all lexicons
		for(Lexicon lexicon: lexiconList){
			lexicon.getFeatureList().removeFeature(featureName);
		}
		
		saveToXML();
	}
	
	public void deleteFeatureValue(String featureName, String featureValue){
		DBFeatureValues featureValues = getDBFeatureValues(featureName);
		if(featureValues != null)
			possibleFeatures.remove(featureValues);
		
		//set lexicon who has this feature value to default value
		for(Lexicon lexicon: lexiconList)
			lexicon.getFeatureList().setFeature(featureValues.getDefaultFeatureCopy());				
		
		saveToXML();
	}
	
	private DBFeatureValues getDBFeatureValues(String featureName){
		for(DBFeatureValues featureValues: possibleFeatures)
			if(featureValues.getFeatureName().equals(featureName))
				return featureValues;
		
		return null;
	}
	
	public FeatureList getDefaultFeatureList(){
		ArrayList<Feature> defaultFeatures = new ArrayList<Feature>();
		for(DBFeatureValues featureValues: possibleFeatures)
				defaultFeatures.add(featureValues.getDefaultFeatureCopy());
		return new FeatureList(defaultFeatures);
	}

	public boolean isFeatureDefault(Feature feature){
		for(DBFeatureValues featureValues: possibleFeatures){
			if(featureValues.getFeatureName().equals(feature.getName())){
				if(featureValues.getDefaultFeatureCopy().getValue().equals(feature.getValue()))
					return true;
			}
		}
		
		return false;
	}
	
	//Manipulating Forms
	public FormList getDefaultFormList(String lexicon){
		ArrayList<Form> formList = new ArrayList<Form>();
		for(String form: possibleForms){
			formList.add(new Form(form, lexicon));
		}
		return new FormList(formList);
	}

	public void addNewPossibleForm(String formName){
		if(!possibleForms.contains(formName))
			possibleForms.add(formName);
		
		//add form for lexicon, set value to the lexicon name itself (default)
		for(Lexicon lexicon: lexiconList){
			lexicon.getFormList().setForm(new Form(formName, lexicon.getName()));
		}
		
		saveToXML();
	}
	
	public void renamePossibleForm(String formName, String newFormName){
		if(!possibleForms.contains(formName))
			return;
		
		int index = possibleForms.indexOf(formName);
		possibleForms.remove(index);
		possibleForms.add(index, newFormName);
		
		//rename all lexicon
		for(Lexicon lexicon: lexiconList){
			lexicon.getFormList().renameForm(formName, newFormName);
		}
		
		saveToXML();
	}
	
	public void deletePossibleForm(String formName){
		if(!possibleForms.contains(formName))
			return;
		
		possibleForms.remove(formName);
		
		for(Lexicon lexicon: lexiconList)
			lexicon.getFormList().removeForm(formName);	
		
		saveToXML();
	}

	//Private getter
	private Lexicon getLexicon(String lexicon){
		for(Lexicon l: lexiconList){
			if(lexicon.equals(l.getName()))
				return l;
		}
		return null;
	}
	
	//Manipulating Lexicons
	public void removeLexiconMapping(String lexiconName){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.removeMapping();
			saveToXML();
			LexiconManager.getInstance().refresh(); //refresh the lexicon list to update the mappings
		}
	}
	
	//The following set methods are not used yet. Ideally, all editing should go through this class, but because of rushed development, it wasn't followed.
	public void setLexiconMapping(String lexiconName, String concept, String sense, String pos){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setMapping(concept, sense, pos);
			saveToXML();	
			LexiconManager.getInstance().refresh(); //refresh the lexicon list to update the mappings
		}
	}
	
	public void setLexiconName(String lexiconName, String newConceptName){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setName(newConceptName);
			saveToXML();			
		}
	}
	
	public void setLexiconGloss(String lexiconName, String newGloss){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setGloss(newGloss);
			saveToXML();			
		}
	}
	
	public void setLexiconComments(String lexiconName, String newComments){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setComments(newComments);
			saveToXML();			
		}
	}
	
	public void setLexiconSampleSentence(String lexiconName, String newSampleSentence){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setSampleSentence(newSampleSentence);
			saveToXML();			
		}
	}
	
	public void setLexiconFeature(String lexiconName, String featureName, String featureValue){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setFeature(featureName, featureValue);
			saveToXML();			
		}
	}
	
	//Manipulating Forms
	public void setLexiconForm(String lexiconName, String formName, String formValue){
		Lexicon lexicon = getLexicon(lexiconName);
		if(lexicon != null){
			lexicon.setForm(formName, formValue);
			saveToXML();			
		}
	}
	
	//update xml
	public void saveToXML(){
		Element xmlElement = generateXMLElement();
		String targetFile = LexiconManager.ROOT_LANGUAGE_FOLDER+"\\"+LexiconManager.getInstance().getCurrSelectedLanguage()+"\\"+pos+".xml";

		XMLManager.getInstance().writeToXML(targetFile, xmlElement);
	}

}
