package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ontology.PartOfSpeech;
import view.rules.SelectPhrase;

import components.Component;
import components.Leaf;
import components.Phrase;

public class SelectPhraseController {
	private SelectPhrase view;
	private Phrase clauseComponent;
	private Phrase phraseComponent;
	private Leaf leafComponent;
	RuleController controller; 
	
	public SelectPhraseController(RuleController controller)
	{
		this.controller= controller;
		view = new SelectPhrase();
		view.setLocationRelativeTo(null);
		view.addBtnOKListener(new OKActionListener());
	}
	
	class OKActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			if(view.getRdbtnNP().isSelected())
				phraseComponent = (Phrase)Component.createInstance(PartOfSpeech.NOUN_PHRASE, false);
			else if(view.getRdbtnVP().isSelected())
				phraseComponent = (Phrase)Component.createInstance(PartOfSpeech.VERB_PHRASE, false);
			else if(view.getRdbtnAdjP().isSelected())
				phraseComponent = (Phrase)Component.createInstance(PartOfSpeech.ADJECTIVE_PHRASE, false);
			else if(view.getRdbtnAdvP().isSelected())
				phraseComponent = (Phrase)Component.createInstance(PartOfSpeech.ADVERBIAL_PHRASE, false);
			else phraseComponent = null;
			
			
			controller.setPhraseComponent(phraseComponent);
			controller.connectComponents();
			view.dispose();
		}
	}
}
