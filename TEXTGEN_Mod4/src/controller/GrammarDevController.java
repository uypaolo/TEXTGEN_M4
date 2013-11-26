package controller;

import java.util.ArrayList;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;
import view.grammardevelopment.ViewSemanticsPanel;
import view.grammardevelopment.editsemantics.CreationRightPanel;
import components.InputXMLDocument;
import controller.listener.grammardev.SelectComponentActionListener;
import controller.listener.grammardev.toolbar.EditDocInfoButtonListener;
import controller.listener.grammardev.toolbar.LoadPanelToolbarBtnListener;
import controller.listener.grammardev.toolbar.NextPrevListener;
import controller.listener.rules.PindutanListener;

public class GrammarDevController {

	//sub-controllers
	private CreateController createController; //not really used. it was just created to initialize the listeners
	private SelectComponentActionListener selectListener;
	
	//views
	private ViewSemanticsPanel grammarDevPanel;
	private MainController mainController;
	
	public GrammarDevController(MainController mainController){
		this.mainController = mainController;
	}
	
	private void refreshPanel(){
		initializeViewSemanticsPanelControllers();
		setToolbarListeners();
	}
	
	private void initializeViewSemanticsPanelControllers(){
		if(grammarDevPanel != null){
			selectListener = new SelectComponentActionListener(grammarDevPanel);
			grammarDevPanel.setSelectComponentPanelListener(selectListener);
			grammarDevPanel.resetCreationPanel();
			
			CreationRightPanel creationPanel = grammarDevPanel.getCreationPanel();
			if(creationPanel != null){
				createController = new CreateController(this, creationPanel);
			}
		}	
	}
		
	//Setters / Loaders
	public void loadDocuments(ArrayList<InputXMLDocument> loadedDocuments){
		grammarDevPanel = new ViewSemanticsPanel(this, loadedDocuments);
		refreshPanel();
		mainController.goToGrammarDevelopment();
		grammarDevPanel.getCurrentlyDisplayedDocumentPanel().adjustPositioning();
		grammarDevPanel.repaint();
	}
	
	public void createNewDocument(String name, String category, String comments){
		grammarDevPanel = new ViewSemanticsPanel(this, name, category, comments);
		refreshPanel();
		mainController.goToGrammarDevelopment();
	}
	
	public void deselectCurrSelectedPanel(){
		if(selectListener != null){
			selectListener.deselectCurrentPanel();
			grammarDevPanel.resetInfoPanel();
			grammarDevPanel.resetCreationPanel();
		}
	}
	
	public void refreshSemanticLexicons(){
		if(grammarDevPanel != null)
			grammarDevPanel.refreshSemanticLexicons();
	}
	
	//Setting listeners
	public void setToolbarListeners(){
		grammarDevPanel.setEditButtonListener(new LoadPanelToolbarBtnListener(grammarDevPanel, ViewSemanticsPanel.MODE_EDIT));
		grammarDevPanel.setGenerateButtonListener(new LoadPanelToolbarBtnListener(grammarDevPanel, ViewSemanticsPanel.MODE_GENERATE));
		grammarDevPanel.setDoneEditingButtonListener(new LoadPanelToolbarBtnListener(grammarDevPanel, ViewSemanticsPanel.MODE_VIEW));
		grammarDevPanel.setInitializeButtonListener(new LoadPanelToolbarBtnListener(grammarDevPanel, ViewSemanticsPanel.MODE_VIEW));
		grammarDevPanel.setEditDocInfoButtonListener(new EditDocInfoButtonListener(this));
		grammarDevPanel.setPindutanButtonListener(new PindutanListener(grammarDevPanel));
		
		grammarDevPanel.setNextButtonListener(new NextPrevListener(NextPrevListener.MODE_NEXT, this));
		grammarDevPanel.setPrevButtonListener(new NextPrevListener(NextPrevListener.MODE_PREV, this));
	}
	
	//For switching the view (unused for now)
	public void goToView(){
		grammarDevPanel.setMode(ViewSemanticsPanel.MODE_VIEW);
	}
	
	public void goToEdit(){
		grammarDevPanel.setMode(ViewSemanticsPanel.MODE_EDIT);
	}
	
	public void goToGenerate(){
		grammarDevPanel.setMode(ViewSemanticsPanel.MODE_GENERATE);
	}
	
	//Getters
	public ViewSemanticsPanel getPanel(){
		if(grammarDevPanel == null)
			return null;
		return grammarDevPanel;
	}
	
	public InputXMLDocumentPanel getCurrentlyDisplayedDocumentPanel(){
		if(grammarDevPanel == null)
			return null;
		return grammarDevPanel.getCurrentlyDisplayedDocumentPanel();
	}
	
	public ComponentPanel getCurrSelectedComponentPanel(){
		if(selectListener != null)
			return selectListener.getSelectedPanel();
		return null;
	}
}
