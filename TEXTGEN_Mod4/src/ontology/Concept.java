package ontology;

import org.jdom2.Element;

public class Concept {
	
	public static final String ATTRIBUTE_NAME = "name";
	public static final String ATTRIBUTE_SENSE = "sense";
	public static final String CHILD_DEFINITION = "definition";
	public static final String CHILD_COMMENTS = "comments";
	public static final String CHILD_SAMPLE_SENTENCE = "sentence";
	
	private String name;
	private String sense;
	private String definition;
	private String comments;
	private String sampleSentence;
	
	public Concept(Element conceptElement){
		//set values here		
		name = conceptElement.getAttributeValue(ATTRIBUTE_NAME);
		sense = conceptElement.getAttributeValue(ATTRIBUTE_SENSE);
		definition = conceptElement.getChildTextTrim(CHILD_DEFINITION);
		comments = conceptElement.getChildTextTrim(CHILD_COMMENTS);
		sampleSentence = conceptElement.getChildTextTrim(CHILD_SAMPLE_SENTENCE);
		
		if(name == null)
			name = "";
		if(sense == null)
			sense = "";
		if(definition == null)
			definition = "";
		if(comments == null)
			comments = "";
		if(sampleSentence == null)
			sampleSentence = "";
	}

	public Concept(String name, String sense){
		this.name = name;
		this.sense = sense;
		
		definition = "";
		comments = "";
		sampleSentence = "";
	}
	
	public Concept(String name, String sense, String definition, String comments, String sampleSentence){
		this.name = name;
		this.sense = sense;
		this.definition = definition;
		this.comments = comments;
		this.sampleSentence = sampleSentence;
	}
	
	//Overriden methods
	public boolean equals(Object o){
		Concept otherConcept = (Concept) o;
		return name.equals(otherConcept.getName()) && sense.equals(otherConcept.getSense());
	}
	
	public int compareTo(Object o){
		Concept otherConcept = (Concept)o;
		
		int result = name.compareTo(otherConcept.getName());
		
		if(result != 0 )
			return result;
		
		return sense.compareTo(otherConcept.getSense());
	}

	public String toString(){
		StringBuilder sb = new StringBuilder(name);
		sb.append("-");
		sb.append(sense);
		sb.append(": ");
		sb.append(definition);
		sb.append(" (");
		sb.append(comments);
		sb.append(")");
		return sb.toString();
	}

	//Getters
	public String getName() {
		return name;
	}

	public String getSense() {
		return sense;
	}

	public String getDefinition() {
		return definition;
	}

	public String getComments() {
		return comments;
	}

	public String getSampleSentence(){
		return sampleSentence;
	}

	public void setDefinition(String newDefinition){
		if(newDefinition != null){
			definition = newDefinition;
		}
	}

	public void setComments(String newComments){
		if(newComments != null){
			comments = newComments;
		}
	}

	public void setSampleSentence(String newSampleSentence){
		if(newSampleSentence != null){
			sampleSentence = newSampleSentence;
		}
	}

	//XML
	public Element generateXMLElement(){
		Element stemElement = new Element("stem");
		stemElement.setAttribute(ATTRIBUTE_NAME, name);
		stemElement.setAttribute(ATTRIBUTE_SENSE, sense);
		
		if(!definition.isEmpty()){
			Element definitionElement = new Element(CHILD_DEFINITION);
			definitionElement.setText(definition);
			stemElement.addContent(definitionElement);
		}
		
		if(!comments.isEmpty()){
			Element commentsElement = new Element(CHILD_COMMENTS);
			commentsElement.setText(comments);
			stemElement.addContent(commentsElement);
		}
		
		if(!sampleSentence.isEmpty()){
			Element sampleSentenceElement = new Element(CHILD_SAMPLE_SENTENCE);
			sampleSentenceElement.setText(sampleSentence);
			stemElement.addContent(sampleSentenceElement);
		}
		
		return stemElement;
		
	}
}
