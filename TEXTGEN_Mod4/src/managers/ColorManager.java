package managers;

import java.awt.Color;
import java.util.HashMap;

import ontology.PartOfSpeech;

public class ColorManager {

	private static  HashMap<String, Color> colorMap;
	
	private static ColorManager instance;
	
	public static ColorManager getInstance(){
		if(instance == null)
			instance = new ColorManager();
		return instance;
	}
	
	private ColorManager(){
		loadColorMap();
	}
	
	//Getter
	private void loadColorMap(){
		colorMap = new HashMap<String, Color>();
		//239,149,115
		colorMap.put(PartOfSpeech.VERB_PHRASE.toLowerCase(), new Color(0xFF, 0x63, 0x47));
		
		colorMap.put(PartOfSpeech.PARTICLE.toLowerCase(), new Color(0x16, 0xBF, 0x86));
		colorMap.put(PartOfSpeech.CLAUSE.toLowerCase(), new Color(28,141,123));
			
		colorMap.put(PartOfSpeech.ADJECTIVE.toLowerCase(), new Color(242, 173, 227));
		colorMap.put(PartOfSpeech.ADJECTIVE_PHRASE.toLowerCase(), new Color(212, 110, 175));
		
		colorMap.put(PartOfSpeech.ADVERB.toLowerCase(), new Color(195,195,197));
		
		colorMap.put(PartOfSpeech.ADPOSITION.toLowerCase(), new Color(0xF3, 0xE1, 0xCB));
		colorMap.put(PartOfSpeech.VERB.toLowerCase(), new Color(241, 148, 117));
		
		colorMap.put(PartOfSpeech.NOUN.toLowerCase(), new Color(0x90, 0xD7, 0xF7));
		colorMap.put(PartOfSpeech.NOUN_PHRASE.toLowerCase(), new Color(0x37, 0x55, 0x9B));
		
		colorMap.put(PartOfSpeech.CONJUCTION.toLowerCase(),new Color(0xFA, 0xE5, 0x88));

		colorMap.put(PartOfSpeech.ADVERBIAL_PHRASE.toLowerCase(), new Color(116, 132, 165)); //violet
		
		colorMap.put(PartOfSpeech.PERIOD, new Color(0x9F, 0xA0, 0xA4));
	}
	
	public Color getColor(String partOfSpeech){
		return colorMap.get(partOfSpeech.toLowerCase());
	}
}
