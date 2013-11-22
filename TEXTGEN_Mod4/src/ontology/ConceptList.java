package ontology;

import java.util.ArrayList;
import java.util.List;

import managers.ComponentManager;
import managers.OntologyManager;
import managers.XMLManager;

import org.jdom2.Element;

public class ConceptList {
	
	private String pos;
	private String posDescription;
	private ArrayList<Concept> conceptList;
	
	public ConceptList(Element posElement){
		pos = posElement.getAttributeValue("name");
		
		if(pos == null)
			pos = "";
		
		//Initialize Description
		posDescription = ComponentManager.getInstance().getDescription(pos);
		if(posDescription == null || posDescription.trim().isEmpty())
			posDescription = pos;
		
		//Initialize List
		conceptList = new ArrayList<Concept>();
		List<Element> stemElements = posElement.getChildren("stem");
		for(Element stemElement: stemElements)
			conceptList.add(new Concept(stemElement));
	}
	
	public ConceptList(String pos, String posDescription){
		this.pos = pos;
		this.posDescription = posDescription;
		conceptList = new ArrayList<Concept>();
	}
	
	//Getters
	public String getPOS(){
		return pos;
	}
	
	public ArrayList<Concept> getConceptList(){
		return (ArrayList<Concept>)conceptList.clone();
	}
	
	public String getPOSDescription(){
		return posDescription;
	}
	
	//For Manipulating
	public Concept addNewConcept(String conceptName){
		
		Concept concept = new Concept(conceptName, determineSense(conceptName));
		addNewConceptWithoutSavingToXML(concept);
		saveToXML();
		return concept;
	}
		
	public void addNewConceptWithoutSavingToXML(Concept concept){
		//insertion sort
		int insertIndex = 0; //first by default
		int listSize = conceptList.size();
		int i;
		
		for(i=0; i < listSize; i++){
			Concept curr = conceptList.get(i);
			if(concept.compareTo(curr) <= 0){
				insertIndex = i;
				break;
			}
		}
		
		if(i == listSize) //last
			insertIndex = i;
		
		conceptList.add(insertIndex, concept);
	}
	
	private String determineSense(String conceptName){
		String senses = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int currSenseIndex = 0;
		for(Concept concept: conceptList){
			if(concept.getName().equalsIgnoreCase(conceptName))
			{
				if(senses.indexOf(concept.getSense()) >= currSenseIndex)
					currSenseIndex = senses.indexOf(concept.getSense()) + 1;
			}
		}
		
		return ""+senses.charAt(currSenseIndex);
	}
	
	public void removeConcept(Concept concept){
		conceptList.remove(concept);
	}

	public void updateConceptComment(String conceptName, String conceptSense, String newComments){
		Concept targetConcept = getConceptObject(conceptName, conceptSense);
		if(targetConcept != null){
			targetConcept.setComments(newComments);
			saveToXML();
		}
	}
	
	public void updateConceptDefinition(String conceptName, String conceptSense, String newDefinition){
		Concept targetConcept = getConceptObject(conceptName, conceptSense);
		if(targetConcept != null){
			targetConcept.setDefinition(newDefinition);
			saveToXML();
		}
	}
	
	public void updateConceptSampleSentence(String conceptName, String conceptSense, String newSampleSentence){
		Concept targetConcept = getConceptObject(conceptName, conceptSense);
		if(targetConcept != null){
			targetConcept.setSampleSentence(newSampleSentence);
			saveToXML();
		}
	}
	
	//Checkers
	public boolean contains(String concept, String sense){
		return getConceptObject(concept, sense) != null;
	}

	public Concept getConceptObject(String concept, String sense){
		for(Concept currConcept: conceptList){
			if(currConcept.getName().equalsIgnoreCase(concept) && currConcept.getSense().equalsIgnoreCase(sense))
				return currConcept;
		}
		return null;
	}
	
	//XML
	
	public ArrayList<Element> generateBasicStemElements(){
		ArrayList<Element> list = new ArrayList<Element>();
		
		for(Concept concept: conceptList){
			Element basicStem = new Element("stem");
			basicStem.setAttribute(Concept.ATTRIBUTE_NAME, concept.getName());
			basicStem.setAttribute(Concept.ATTRIBUTE_SENSE, concept.getSense());
			list.add(basicStem);
		}
		return list;
	}
	
	private void saveToXML(){
		String path = OntologyManager.DB_PATH_ONTOLOGY+"\\"+pos+".xml";
		Element rootElement = new Element("pos");
		rootElement.setAttribute("name", pos);
		
		for(Concept concept: conceptList){
			rootElement.addContent(concept.generateXMLElement());
		}
		
		XMLManager.getInstance().writeToXML(path, rootElement);
	}
}
