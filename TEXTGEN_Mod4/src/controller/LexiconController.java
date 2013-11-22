package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lexicon.LexiconList;
import managers.LexiconManager;
import view.lexicon.AddEditFeatureFrame;
import view.lexicon.AddEditFormFrame;
import view.lexicon.LexiconPanel;
import controller.listener.lexicon.LexAddNewPOSActionListener;
import controller.listener.lexicon.LexComboCategoryItemListener;
import controller.listener.lexicon.LexMapToNewConcept;
import controller.listener.lexicon.LexNewEntryButtonListener;
import controller.listener.lexicon.LexiconTableKeyListener;
import controller.listener.lexicon.LexiconTableListener;

public class LexiconController {
	
	private LexiconPanel lexiconPanel;
	private LexiconList lexList;
	
	public LexiconController(){
		lexiconPanel = new LexiconPanel();
		lexList =  LexiconManager.getInstance().getLexiconList(lexiconPanel.getCodeFromSelectedPOS());
		addListeners();
	}
	
	//Listeners
	public void addListeners(){
		lexiconPanel.getTable().addKeyListener(new LexiconTableKeyListener(lexiconPanel));
		
		lexiconPanel.getTable().getModel().addTableModelListener(new LexiconTableListener(lexiconPanel));
		
		lexiconPanel.getCmbCategory().addItemListener(new LexComboCategoryItemListener(lexiconPanel));
		
		lexiconPanel.getBtnViewStems().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				lexiconPanel.setTable(LexiconPanel.TABLE_STEM);
			}
		});
		
		lexiconPanel.getBtnViewFeatures().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				lexiconPanel.setTable(LexiconPanel.TABLE_FEATURE);
			}
		});
		
		lexiconPanel.getBtnViewForms().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				lexiconPanel.setTable(LexiconPanel.TABLE_FORM);
			}
		});
		
		lexiconPanel.getBtnNewEntry().addActionListener(new LexNewEntryButtonListener(lexiconPanel));
		
		lexiconPanel.getBtnMapToNewConcept().addActionListener(new LexMapToNewConcept(lexiconPanel));
		
		lexiconPanel.getBtnAddEditFeatures().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new AddEditFeatureFrame(lexList); 
				lexiconPanel.initTable(); // para bumalik nalang sa viewstem, hassle kasi pag irerefresh mo pa if nasa viewfeature ba siya or ano man
			}});
		
		lexiconPanel.getBtnAddEditForms().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new AddEditFormFrame(lexList);
				lexiconPanel.initTable();
			}
			
		});
		
		lexiconPanel.getBtnAddPOS().addActionListener(new LexAddNewPOSActionListener(lexiconPanel));
	}
	
	//Refresh
	public void refreshLexiconPanel(){
		lexiconPanel.initTable();
	}
	
	//Getters
	public LexiconPanel getPanel(){
		return lexiconPanel;
	}
	
}
