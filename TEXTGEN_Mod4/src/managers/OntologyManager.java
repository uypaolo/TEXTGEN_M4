package managers;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import ontology.Concept;
import ontology.ConceptList;
import ontology.PartOfSpeech;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class OntologyManager {
	
	/* OntologyManager class is used to handle all interactions with the ontology.
	 * The idea is that you use this class to get whatever ConceptList you need (through getConceptList(pos)),
	 * A ConcepList is just a list of the concepts, but with methods that allow you to manipulate(add/delete) these concepts,
	 * to check whether some concept exists.
	 * 
	 * When modifying concepts through the ConceptList, changes are automatically saved to the XML. 
	 * 
	 * 
	 * (To make this fully functional you might need to add new methods in ConceptList in the future, like a method for renaming concepts)
	 * Important: Sometimes, when exceptions happen during saving, the original XML file could possible be corrupted. 
	 * Thus, it is wise to always keep a back up of the current databases. 
	 * Also, it might be a good idea to handle these exceptions well so that when they occur, the original file can be restored. 
	 * But as of now, this has not been implemented yet.
	 * 
	 * Also Important:
	 * You can also access a Concept by getting the ArrayList<Concept> of ConceptList and then modifying it, 
	 * but it WILL NOT SAVE TO XML. 
	 * 
	 * The weird part in this class is that it holds both the Ontology and displayOntology. 
	 * Ontology holds the concepts as they are, Nouns, Adjectives, etc.
	 * DisplayOntology holds the concepts categorized in a way for display. (Nouns are split into masculine, feminine, placenames).
	 * What's important to remember is that although these hold different ConceptLists,
	 * the Concepts contained in these lists ARE THE SAME OBJECTS. 
	 * In other words, whether you access a Concept through ontology or displayOntology, they refer to the same object.
	 *  
	 * The reason for this weird complication is because it is convenient to use the ConceptList class for representing the DisplayOntology,
	 * but the actual structure in the XML files is represented accurately by Ontology. 
	 * (Remember, Sir wanted the Masculine and Feminine Nouns, and Place Names to still be represented as a Noun in the XML).
	 * 
	 * With that said, when modifying concepts, always use the getConceptList(pos) method and NOT getDisplayConceptList(pos).
	 * Using the latter might lead to undesired results.
	 */
	
	//Singleton to make sure every part in the code refers to the same ontology
	private static  OntologyManager instance;
	
	public static OntologyManager getInstance(){
		if(instance == null)
			instance = new OntologyManager();
		return instance;
	}
	
	//"Normal" class attributes and methods
	public static final String DB_PATH_ONTOLOGY = "Databases\\Ontology\\";
	public static final String DB_PATH_ONTOLOGY_SPECIAL = "Databases\\Ontology\\special";
	
	private ArrayList<ConceptList> ontology;
	
	private ArrayList<ConceptList> displayOntology;
	
	private ArrayList<ConceptList> specialConceptList;
	
	//*****Constructor / Initialize Methods*****
	private OntologyManager(){
		refreshOntology();
	}
	
	//it only contains loadOntology() right now but might contain other methods in the future depending on how you want to refresh
	public void refreshOntology(){
		loadOntology();
	}
	
	private void loadOntology(){
		ontology = new ArrayList<ConceptList>();
		displayOntology = new ArrayList<ConceptList>();
		
		//Load the special nouns (which will be used for filtering the noun list)
		this.specialConceptList = loadSpecialConceptList();
		
		//Load Ontology
		SAXBuilder builder = new SAXBuilder();
		File ontologyDirectory = new File(DB_PATH_ONTOLOGY);
		
		//Get only all the xml files
		File[] posFiles = ontologyDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
		for(File xmlFile: posFiles){
			try{
				Document document = (Document) builder.build(xmlFile);
				Element posElement = document.getRootElement();		
				ConceptList currConceptList = new ConceptList(posElement);
				ontology.add(currConceptList);
				
				//If Noun, do something special with the display ontology
				String pos = posElement.getAttributeValue("name");
				
				if(pos.toLowerCase().equals(PartOfSpeech.NOUN.toLowerCase())){ //split the nouns for display ontology
					ConceptList filteredNounList = new ConceptList(pos, "Noun");
					ConceptList masculineNounList = new ConceptList(pos, "Masculine Noun");
					ConceptList feminineNounList = new ConceptList(pos, "Feminine Noun");
					ConceptList placeNameList = new ConceptList(pos, "Place Name");
					
					//loop through and pick the non-special ones, and add to display conceptlist
					for(Concept noun: currConceptList.getConceptList()){
						if(isNounMasculine(specialConceptList, noun.getName(), noun.getSense())){
							masculineNounList.addNewConceptWithoutSavingToXML(noun);
						}
						else if(isNounFeminine(specialConceptList, noun.getName(), noun.getSense())){
							feminineNounList.addNewConceptWithoutSavingToXML(noun);
						}
						else if(isNounPlace(specialConceptList, noun.getName(), noun.getSense())){
							placeNameList.addNewConceptWithoutSavingToXML(noun);
						}
						else{
							filteredNounList.addNewConceptWithoutSavingToXML(noun);
						}
					}
					
					//Add all these new lists to the display ontology
					displayOntology.add(filteredNounList);
					displayOntology.add(masculineNounList);
					displayOntology.add(feminineNounList);
					displayOntology.add(placeNameList);	
				}
				else //simply add to display ontology if not noun
					displayOntology.add(currConceptList);
						
			}catch(Exception e){e.printStackTrace();}
		}
		
		//It is important that the ontology and display ontology have references to the same Concept Objects so that when alteration happens (no matter the display), the noun db is still updated.
	}
	
	//This method is used in loadOntology() to load the special nouns (masculine, feminine, place-names).
	private ArrayList<ConceptList> loadSpecialConceptList(){
		//Load Special
		ArrayList<ConceptList> specialNouns = new ArrayList<ConceptList>();
		SAXBuilder builder = new SAXBuilder();
		File ontologyDirectory = new File(DB_PATH_ONTOLOGY+"\\special");
		File[] posFiles = ontologyDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        });
		for(File xmlFile: posFiles){
			try{
				Document document = (Document) builder.build(xmlFile);
				Element posElement = document.getRootElement();		
				String pos = posElement.getAttributeValue("name");
				String description = posElement.getAttributeValue("description");
				ConceptList currConceptList = new ConceptList(pos, description);
				
				List<Element> stemElements = posElement.getChildren("stem");
				
				for(Element stemElement: stemElements){
					currConceptList.addNewConceptWithoutSavingToXML(new Concept(stemElement));
				}
				
				specialNouns.add(currConceptList);
				
			}catch(Exception e){e.printStackTrace();}
		}
		
		return specialNouns;
	}

	//*****Public Getters*****

	public ArrayList<ConceptList> getDisplayOntology(){
		return (ArrayList<ConceptList>)displayOntology.clone();
	}
	
	public ConceptList getConceptList(String pos){
		for(ConceptList c : ontology){
			if(c.getPOS().toLowerCase().equals(pos.toLowerCase()))
				return c;
		}
		return null;
	}
	
	public  ArrayList<PartOfSpeech> getAllPOSForDisplay(){
		ArrayList<PartOfSpeech> posList = new ArrayList<PartOfSpeech>();
		for(ConceptList c : displayOntology){
			posList.add(new PartOfSpeech(c.getPOS(), c.getPOSDescription()));
		}
		return posList;
	}
		
	//***** Private Getters*****
	public ConceptList getDisplayConceptList(PartOfSpeech pos){
		if(pos.getName().toLowerCase().equals(PartOfSpeech.NOUN.toLowerCase())){
			for(ConceptList c : displayOntology){
				if(c.getPOS().toLowerCase().equals(PartOfSpeech.NOUN.toLowerCase()) && c.getPOSDescription().toLowerCase().equals(pos.getDescription().toLowerCase()))
					return c;
			}
		}
		
		for(ConceptList c : displayOntology){
			if(c.getPOS().toLowerCase().equals(pos.getName().toLowerCase()))
				return c;
		}
		return null;
	}
	
	private ConceptList getSpecialConceptList(String posDescription){
		for(ConceptList special: specialConceptList)
			if(special.getPOSDescription().equals(posDescription))
				return special;
		
		return null;
	}
	
	private  ArrayList<PartOfSpeech> getAllPOS(){
		ArrayList<PartOfSpeech> posList = new ArrayList<PartOfSpeech>();
		for(ConceptList c : ontology){
			posList.add(new PartOfSpeech(c.getPOS(), c.getPOSDescription()));
		}
		return posList;
	}
	
	//setters
	public boolean addNewConcept(PartOfSpeech pos, String conceptName){
		ConceptList conceptList = getConceptList(pos.getName());
		if(conceptList == null)
			return false;
		
		Concept newConcept= conceptList.addNewConcept(conceptName);
		
		if(pos.getDescription().equals(PartOfSpeech.MASCULINE_NOUN) || pos.getDescription().equals(PartOfSpeech.FEMININE_NOUN) || pos.getDescription().equals(PartOfSpeech.PLACE_NAME))
			updateSpecialList(pos.getDescription(), newConcept);
		
		this.refreshOntology();
		return true;
	}
	
	private void updateSpecialList(String posDescription, Concept newConcept){
		ConceptList specialList = getSpecialConceptList(posDescription);
		specialList.addNewConceptWithoutSavingToXML(newConcept);
		
		Element rootElement = new Element("pos");
		rootElement.setAttribute("name", PartOfSpeech.NOUN);
		rootElement.setAttribute("description", posDescription);
		
		ArrayList<Element> stems = specialList.generateBasicStemElements();
		
		for(Element stem: stems)
			rootElement.addContent(stem);
		
		String targetPath = DB_PATH_ONTOLOGY_SPECIAL+"\\"+posDescription+".xml";
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try{
			xmlOutput.output(rootElement, new FileWriter(new File(targetPath)));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Checkers
	public boolean doesConceptExist(String pos, String concept, String sense){
		ConceptList conceptList = getConceptList(pos);
		if(conceptList == null)
			return false;
		
		return conceptList.contains(concept, sense);
	}
	
	public boolean doesConceptExist(String concept, String sense){
		ArrayList<PartOfSpeech> partsOfSpeech = getAllPOS();
		for(PartOfSpeech pos: partsOfSpeech){
			if(doesConceptExist(pos.getName(), concept, sense))
				return true;
		}
		return false;
	}
	
	private boolean isNounMasculine(ArrayList<ConceptList> specialConcepts, String noun, String sense){
		
		ConceptList toSearch = null;
		for(ConceptList list: specialConcepts){
			if(list.getPOS().toLowerCase().equals(PartOfSpeech.NOUN.toLowerCase()) && list.getPOSDescription().equals(PartOfSpeech.MASCULINE_NOUN)){
				toSearch = list;
				break;
			}
		}
		if(toSearch == null)
			return false;
		
		return toSearch.contains(noun, sense);
	}
	
	private boolean isNounFeminine(ArrayList<ConceptList> specialConcepts, String noun, String sense){
		ConceptList toSearch = null;
		for(ConceptList list: specialConcepts)
			if(list.getPOS().toLowerCase().equals(PartOfSpeech.NOUN.toLowerCase()) && list.getPOSDescription().equals(PartOfSpeech.FEMININE_NOUN)){
				toSearch = list;
				break;
			}
		
		if(toSearch == null)
			return false;
		
		return toSearch.contains(noun, sense);
	}
	
	private boolean isNounPlace(ArrayList<ConceptList> specialConcepts, String noun, String sense){
		ConceptList toSearch = null;
		for(ConceptList list: specialConcepts)
			if(list.getPOS().toLowerCase().equals(PartOfSpeech.NOUN.toLowerCase()) && list.getPOSDescription().equals(PartOfSpeech.PLACE_NAME)){
				toSearch = list;
				break;
			}
		
		if(toSearch == null)
			return false;
		
		return toSearch.contains(noun, sense);
	}
	
}
