package ontology;

public class PartOfSpeech {

	public static final String CLAUSE = "cl";
	public static final String NOUN_PHRASE = "np";
	public static final String VERB_PHRASE = "vp";
	public static final String ADVERBIAL_PHRASE = "advp";
	public static final String ADJECTIVE_PHRASE = "adjp";
	public static final String PERIOD = "period";
	
	public static final String ADJECTIVE = "adj";
	public static final String ADPOSITION = "adp";
	public static final String ADVERB = "adv";
	public static final String CONJUCTION = "conj";
	public static final String NOUN = "n";
	public static final String PARTICLE = "par";
	public static final String VERB = "v";
	public static final String MASCULINE_NOUN = "Masculine Noun";
	public static final String FEMININE_NOUN = "Feminine Noun";
	public static final String PLACE_NAME = "Place Name";
	
	private String name;
	private String description;
	
	public PartOfSpeech(String name, String description){
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public String toString(){
		return description;
	}

	
	public boolean equals(Object other){
		PartOfSpeech otherPos = (PartOfSpeech) other;
		return name.equals(otherPos.name) && description.equals(otherPos.description);
	}
	
}
