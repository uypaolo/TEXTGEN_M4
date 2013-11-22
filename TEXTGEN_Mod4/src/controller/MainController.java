package controller;

import javax.swing.JOptionPane;

import managers.LexiconManager;
import view.MainFrame;
import controller.listener.grammardev.DevelopGrammarListener;
import controller.listener.grammardev.LoadSemanticsActionListener;
import controller.listener.grammardev.NewSemanticsActionListener;
import controller.listener.grammardev.SaveActionListener;
import controller.listener.grammardev.SaveAsListener;
import controller.listener.lexicon.MenuCreateLangListener;
import controller.listener.lexicon.MenuLexiconListener;
import controller.listener.lexicon.MenuSelectLanguageListener;
import controller.listener.ontology.MenuOntologyListener;

public class MainController {

	private GrammarDevController grammarDevController;
	private OntologyController ontologyController;
	private LexiconController lexiconController;
	
	private MainFrame mainFrame;
	
	public MainController(){
		grammarDevController = new GrammarDevController(this);
		ontologyController = new OntologyController();
		lexiconController = new LexiconController();
		initMainFrame();
		mainFrame.setTitle("Linguist's Assistant: "+LexiconManager.getInstance().getCurrSelectedLanguage());
	}
	
	private void initMainFrame(){
		mainFrame = MainFrame.getInstance();
		
		mainFrame.setNewSemanticsListener(new NewSemanticsActionListener(grammarDevController));
		mainFrame.setSaveSemanticsListener(new SaveActionListener(grammarDevController));
		mainFrame.setSaveAsSemanticsListener(new SaveAsListener(grammarDevController));
		mainFrame.setLoadSemanticsListener(new LoadSemanticsActionListener(grammarDevController));
		
		mainFrame.setDevelopGrammarListener(new DevelopGrammarListener(this));
		mainFrame.setCreateNewLanguageListener(new MenuCreateLangListener(this));
		mainFrame.setSelectLanguageListener(new MenuSelectLanguageListener(this));
		mainFrame.setOntologyListener(new MenuOntologyListener(this));
		mainFrame.setLexiconListener(new MenuLexiconListener(this));
		mainFrame.setVisible(true);
	}
	
	public void createLanguage(String languageName){
		LexiconManager.getInstance().addLanguage(languageName);
		mainFrame.setTitle(languageName);
		ontologyController.refreshOntologyPanel();
		lexiconController.refreshLexiconPanel();
		grammarDevController.refreshSemanticLexicons();
	}
	
	public void loadLanguage(String languageName){
		LexiconManager.getInstance().loadLexicon(languageName);
		mainFrame.setTitle(languageName);
		ontologyController.refreshOntologyPanel();
		lexiconController.refreshLexiconPanel();
		grammarDevController.refreshSemanticLexicons();
		mainFrame.setTitle("Linguist's Assistant: "+LexiconManager.getInstance().getCurrSelectedLanguage());
	}
	
	public void createNewSemantics(String category, String name, String comments){
		grammarDevController.createNewDocument(category, name, comments);
		goToGrammarDevelopment();
	}
	
	public void goToGrammarDevelopment(){
		if(grammarDevController.getPanel() == null){
			JOptionPane.showMessageDialog(mainFrame, "Create or Load a document first.", "No Document", JOptionPane.ERROR_MESSAGE );
		}
		else
			mainFrame.setPanel(grammarDevController.getPanel());

			mainFrame.revalidate();
			mainFrame.repaint();
	}
	
	public void goToOntology(){
		ontologyController.refreshOntologyPanel();
		mainFrame.setPanel(ontologyController.getPanel());
	}
	
	public void goToLexicon(){
		lexiconController.refreshLexiconPanel();
		mainFrame.setPanel(lexiconController.getPanel());
	}
}
