package managers;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lexicon.Lexicon;
import lexicon.LexiconList;
import ontology.PartOfSpeech;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class LexiconManager {

	//For singleton
	private static LexiconManager lexiconManager;
	
	public static LexiconManager getInstance(){
		if(lexiconManager == null){
			lexiconManager = new LexiconManager();
		}
		return lexiconManager;
	}
	
	//The "normal" attributes and methods
	public static final String ROOT_LANGUAGE_FOLDER = "languages";
	private static final String LANGUAGES_DB_PATH = "Databases\\LanguagesDB.xml";
	
	private ArrayList<String> currentLanguages;
	private  ArrayList<LexiconList> languageLexicon;
	private String currSelectedLanguage;
	
	//Constructor / Initialize Methods
	private LexiconManager(){
		initializeCurrentLanguages();
		if(currentLanguages.size() > 0){
			currSelectedLanguage = currentLanguages.get(0);
			refresh();
		}
	}
	
	private void initializeCurrentLanguages(){
		currentLanguages = new ArrayList<String>();
		File languagesDB = new File(LANGUAGES_DB_PATH);
		
		try{
			SAXBuilder builder = new SAXBuilder();
			Document document = (Document) builder.build(languagesDB);
			
			Element rootElement = document.getRootElement();
			
			List<Element> languagesElement = rootElement.getChildren("language");
			for(Element languageElement: languagesElement)
				currentLanguages.add(languageElement.getTextTrim());
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void refresh(){
		loadLexicon(currSelectedLanguage);
		initializeMappings();
	}
	
	public void loadLexicon(String language){
		if(isLanguageValid(language)){
			languageLexicon = new ArrayList<LexiconList>();
			File languageFolder = new File(ROOT_LANGUAGE_FOLDER+"\\"+language);
			
			//Get only all the xml files
			File[] posFiles = languageFolder.listFiles(new FilenameFilter() {
	            @Override
	            public boolean accept(File folder, String name) {
	            	if(name.toLowerCase().endsWith(".xml")){
	            		//String fileName = name.substring(0, name.length() - 4);
	            		//if(POSManager.getInstance().getPOS(fileName) != null)
	            			return true;
	            	}
	                return false;
	            }
	        });
			
			//create lexicon list for each pos (each pos is in a diff file)
			for(File xmlFile: posFiles){
				try{
					SAXBuilder builder = new SAXBuilder();
					Document document = (Document) builder.build(xmlFile);
					
					languageLexicon.add(new LexiconList(document.getRootElement()));
				}
				catch(Exception e){e.printStackTrace();}
			}
			
			this.currSelectedLanguage = language;
		}
		initializeMappings();
	}
	
	private HashMap<String, ArrayList<Lexicon>> conceptToLexiconMap;

	private void initializeMappings(){
		conceptToLexiconMap = new HashMap<String, ArrayList<Lexicon>>();
		
		//loop through all parts of speech
		for(LexiconList lexiconList: languageLexicon){
			
			//get mapping from each part of speech
			HashMap<String, ArrayList<Lexicon>> currMap = lexiconList.getConceptToLexiconMap();
		
			//collate
			for (Map.Entry<String, ArrayList<Lexicon>> entry : currMap.entrySet()){
				ArrayList<Lexicon> listToAddTo = conceptToLexiconMap.get(entry.getKey());
				if(listToAddTo == null)
					listToAddTo = new ArrayList<Lexicon>();
				listToAddTo.addAll(entry.getValue()); 
				conceptToLexiconMap.put(entry.getKey(), listToAddTo);
			}
		}
		
		//sort in alphabetical order the lexicons
		for (Map.Entry<String, ArrayList<Lexicon>> entry : conceptToLexiconMap.entrySet()){
			Collections.sort(entry.getValue());
		}
		
	}
	
	//Getters
	public ArrayList<PartOfSpeech> getAllPOS(){
		ArrayList<PartOfSpeech> posList = new ArrayList<PartOfSpeech>();
		for(LexiconList list: languageLexicon){
			String description = ComponentManager.getInstance().getDescription(list.getPOS());
			if(description == null || description.trim().isEmpty())
				description = list.getPOS();
			posList.add(new PartOfSpeech(list.getPOS(), description));
		}
		return posList;
	}
	
	public LexiconList getLexiconList(String pos){
		for(LexiconList list : languageLexicon){
			if(list.getPOS().equalsIgnoreCase(pos))
				return list;
		}
		return null;
	}
	
	public ArrayList<LexiconList> getLanguageLexicon(){
		return languageLexicon;
	}

	public ArrayList<String> getLanguages(){
		return (ArrayList<String>)currentLanguages.clone();
	}

	public String getCurrSelectedLanguage(){
		return currSelectedLanguage;
	}

	public ArrayList<Lexicon> getMappedLexicons(String pos, String concept, String sense){
		if(conceptToLexiconMap == null)
			return new ArrayList<Lexicon>();
		
		ArrayList<Lexicon> mappedLexicons = conceptToLexiconMap.get(combineIntoString(pos, concept, sense));
		if(mappedLexicons == null)
			mappedLexicons = new ArrayList<Lexicon>();
		return mappedLexicons;
	}
	
	//created this method for use (adding and checking) in the map. (primary key is determined by the string produced by this method)
	public static String combineIntoString(String pos, String concept, String sense){
		return concept.toLowerCase()+"-"+sense.toLowerCase()+"("+pos.toLowerCase()+")";
	}
	
	//Checkers
	public  boolean isLanguageValid(String language){
		for(String curr: currentLanguages){
			if(curr.equals(language))
				return true;
		}
		return false;
	}
		
	//For Manipulating languages
	public boolean addLanguage(String newLanguage){
		if(isLanguageValid(newLanguage))
			return false;
		
		//insertion sort
		int insertIndex = 0; //first by default
		int listSize = currentLanguages.size();
		int i;
		
		for(i=0; i < listSize; i++){
			String curr = currentLanguages.get(i);
			if(newLanguage.compareTo(curr) <= 0){
				insertIndex = i;
				break;
			}
		}
		
		if(i == listSize) //last
			insertIndex = i;
		
		currentLanguages.add(insertIndex, newLanguage);
		
		//save to xml
	
		if(!XMLManager.getInstance().writeToXML(LANGUAGES_DB_PATH, generateLanguageDBXMLElement()))
			return false;
		
		if(!createNewLanguageDirectory(newLanguage))
			return false;

		loadLexicon(newLanguage); //refresh the current lexicon
		return true;
	}
	
	private boolean createNewLanguageDirectory(String newLanguage){
		String newPath = ROOT_LANGUAGE_FOLDER + "\\" + newLanguage;
		File newFolder = new File(newPath);
		
		newFolder.mkdir();
		
		ArrayList<PartOfSpeech> partsOfSpeech = ComponentManager.getInstance().getLeafPartsOfSpeech();
		try{
			for(PartOfSpeech pos: partsOfSpeech){
				String xmlPath = newPath+"\\"+pos.getName()+".xml";
				Element baseElement = createBasePOSElement(pos.getName());
				XMLManager.getInstance().writeToXML(xmlPath, baseElement);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addNewPOSForCurrentLanguage(String newPOS){
		try{
			File targetFile = new File(ROOT_LANGUAGE_FOLDER + "\\" + currSelectedLanguage+"\\"+newPOS+".xml");
			
			if(!targetFile.exists()){ //pos doesn't exist yet
				Element baseElement = createBasePOSElement("pos");
				baseElement.setAttribute("name", newPOS);
				XMLManager.getInstance().writeToXML(targetFile.getPath(), baseElement);
			
				this.refresh();	//re-load the lexicons
				return true;
			}
		
		}catch(Exception e){e.printStackTrace();}
		
		return false;
	}
	
	//XML Element generation
	private Element createBasePOSElement(String posName){
		Element baseElement = new Element("pos");
		baseElement.setAttribute("name", posName);
		return baseElement;
	}
	
	private Element generateLanguageDBXMLElement(){
		Element languageDBElement = new Element("db");
		languageDBElement.setAttribute("name", "languages");
		
		for(String language: currentLanguages){
			Element languageElement = new Element("language");
			languageElement.setText(language);
			languageDBElement.addContent(languageElement);
		}
		
		return languageDBElement;
	}
	
	//for retrieving the lexicon instance 
	public Lexicon getLexiconInstance(String lexiconName, String posCode){
		LexiconList lexList = getLexiconList(posCode);
		if(lexList == null)
			return null;
		
		return lexList.getLexiconInstance(lexiconName);
	}
}
