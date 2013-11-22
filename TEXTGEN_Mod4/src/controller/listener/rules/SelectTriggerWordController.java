package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import view.rules.SelectTriggerWord;

import components.Leaf;

public class SelectTriggerWordController {
	SelectTriggerWord view;
	Leaf component;
	SimpleSpellOutController controller;
	
	public SelectTriggerWordController(Leaf component, SimpleSpellOutController controller)
	{
		this.controller = controller;
		this.component = component;
		view = new SelectTriggerWord(component.getPOSCode());
		//removeSelectedTriggerWord();
		view.addOKBtnListener(new OKActionListener());
		view.addCancelBtnListener(new CancelActionListener());
	}
	
	public void removeSelectedTriggerWord()
	{
		JComboBox<String> selectedWord = controller.view.getCmbTriggerWord();
		JComboBox<String> wordList =  view.getCmbTriggerWord();
		for(int i = 0; i < selectedWord.getItemCount(); i++)
			for(int j = 0; j < wordList.getItemCount(); j++)
				if(selectedWord.getItemAt(i).equals(wordList.getItemAt(j)))
					wordList.removeItemAt(j);
	}
	
	class OKActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(view.getSelectedWord() != null)
			{
				controller.view.getCmbTriggerWord().removeAllItems();
				controller.updateTriggerWord(view.getSelectedWord());
				component.setLexicon(view.getSelectedLexicon());
			}
				
			view.dispose();
		}
	}
	
	class CancelActionListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			view.dispose();
		}
	}
	
}
