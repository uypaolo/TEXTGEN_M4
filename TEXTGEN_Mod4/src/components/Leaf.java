package components;

import java.util.ArrayList;

import lexicon.Lexicon;
import lexicon.LexiconList;
import managers.FeatureManager;
import managers.LexiconManager;

import org.jdom2.Element;

import features.Feature;

public class Leaf extends Component{

	public static final String ATTRIBUTE_CONCEPT = "concept";
	public static final String ATTRIBUTE_LEXICAL_SENSE = "sense";
	public static final String ATTRIBUTE_LEXICON = "lexicon";
	
	private String concept;
	private String lexicalSense;
	private ArrayList<Lexicon> mappedLexicons;
	
	public Leaf(Element componentElement, boolean autoAddDefaultFeatures) {
		super(componentElement, autoAddDefaultFeatures);
	
		concept = componentElement.getAttributeValue(ATTRIBUTE_CONCEPT);
		lexicalSense = componentElement.getAttributeValue(ATTRIBUTE_LEXICAL_SENSE);
		
		if(concept != null)
			concept = concept.trim();
		else
			concept = "";
		
		if(lexicalSense!= null)
			lexicalSense = lexicalSense.trim();
		else
			lexicalSense = "";
		
		String lexiconName = componentElement.getAttributeValue(ATTRIBUTE_LEXICON);
		if(lexiconName != null){
			LexiconList lexList = LexiconManager.getInstance().getLexiconList(super.getPOSCode());
			Lexicon lexicon = lexList.getLexiconInstance(lexiconName);
			mappedLexicons = new ArrayList<Lexicon>();
			if(lexicon != null)
				mappedLexicons.add(lexicon);
		}
		else
			refreshLexicon();
	}
	
	public Leaf(String componentName, boolean autoAddDefaultFeatures){
		super(componentName, autoAddDefaultFeatures);
		concept = "";
		lexicalSense = "";
		refreshLexicon();
	}
	
	public void refreshLexicon(){
		if(mappedLexicons == null){
			mappedLexicons = LexiconManager.getInstance().getMappedLexicons(name, concept, lexicalSense);
			if(mappedLexicons == null)
				mappedLexicons = new ArrayList<Lexicon>();
		}
	}

	@Override
	public String toString() {
		StringBuilder string  = new StringBuilder();
		string.append(name);
		string.append(": ");
		string.append(concept);
		string.append("-");
		string.append(lexicalSense);
		return string.toString();
	}
	
	@Override
	public String toGeneratedString() {
		StringBuilder sb = new StringBuilder();
		if(!this.toLexiconSentence().isEmpty()){
			sb.append(this.toLexiconSentence());
			sb.append(" <");
			sb.append(this.toString());
			sb.append(">");
		}
		else
			sb.append(this.toString());
		
		return sb.toString();
	}

	@Override
	public String toLexiconSentence() {
		Lexicon lexicon = getFirstMappedLexicon();
		if(lexicon != null && lexicon.getParentLexiconList().getPOS().toLowerCase().equals(name.toLowerCase()))
			return lexicon.getName();
		return "???";
	}
	
	@Override
	public String toConceptSentence() {
		return concept;
	}
	
	protected String getFeatures(boolean includeDefaults, String nextLineToken){
		StringBuilder featureString = new StringBuilder();
		featureString.append("*****Features");
		if(!includeDefaults)
			featureString.append("(Non-default)");
		
		featureString.append("*****");
		featureString.append(nextLineToken);
		
		for(Feature feature: featureList.getFeatureList()){
			if(includeDefaults || !FeatureManager.isFeatureDefault(name, feature.getName(), feature.getValue())){
				featureString.append(feature.getName()+" = "+feature.getValue());
				featureString.append(nextLineToken);
			}
		}
		featureString.append(nextLineToken);
		
		
		Lexicon mappedLexicon = getFirstMappedLexicon();
		if(mappedLexicon != null){
			featureString.append("*****User-defined Lexical Features");
			if(!includeDefaults)
				featureString.append("(Non-default)");
			featureString.append("*****");
			featureString.append(nextLineToken);
			
			for(Feature feature: mappedLexicon.getFeatureList().getFeatureList()){
				if(includeDefaults || !mappedLexicon.getParentLexiconList().isFeatureDefault(feature) ){
					featureString.append(feature.getName()+" = "+feature.getValue());
					featureString.append(nextLineToken);
				}
			}
		}
		return featureString.toString();
		
	}
	
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Children getChildren() {
		return null;
	}
	
	public Lexicon getFirstMappedLexicon(){
		if(mappedLexicons.size() == 0)
			return null;
		
		return mappedLexicons.get(0);
	}

	protected void addAdditionalXMLContent(Element parentElement) {
		parentElement.setAttribute(ATTRIBUTE_CONCEPT, concept);
		parentElement.setAttribute(ATTRIBUTE_LEXICAL_SENSE, lexicalSense);
		Lexicon lexicon = getFirstMappedLexicon();
		if(lexicon!= null && !lexicon.getName().isEmpty())
			parentElement.setAttribute(ATTRIBUTE_LEXICON, lexicon.getName());
	}

	public String getLexicalSense() {
		return lexicalSense;
	}

	public void setLexicalSense(String lexicalSense) {
		this.lexicalSense = lexicalSense;
		refreshLexicon();
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
		refreshLexicon();
	}

	@Override
	public int setTag(int newTag) {
		this.tag = newTag;
		return newTag+1;
	}

	@Override
	public Component getComponentByTag(int tag) {
		if(this.tag == tag)
			return this;
		return null;
	}

	public int getMaxTag() {
		return tag;
	}
	
	public void setConceptWithoutUpdatingLexicon(String concept){
		if(concept != null)
			this.concept = concept;
		else
			this.concept = "";
	}
	
	public void setLexicon(Lexicon lexicon){
		if(lexicon != null){
			mappedLexicons = new ArrayList<Lexicon>();
			mappedLexicons.add(lexicon);
		}
	}
}