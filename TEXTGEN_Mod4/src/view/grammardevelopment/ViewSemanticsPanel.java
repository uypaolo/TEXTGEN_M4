package view.grammardevelopment;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import view.MainFrame;
import view.grammardevelopment.editsemantics.CreationRightPanel;

import components.Component;
import components.InputXMLDocument;

import controller.GrammarDevController;
import controller.listener.grammardev.SelectComponentActionListener;
import controller.listener.grammardev.toolbar.EditDocInfoButtonListener;
import controller.listener.grammardev.toolbar.LoadPanelToolbarBtnListener;
import controller.listener.grammardev.toolbar.NextPrevListener;
import controller.listener.rules.PindutanListener;

public class ViewSemanticsPanel extends JPanel{

	//Variables related to mode
	public static final int MODE_VIEW = 0;
	public static final int MODE_EDIT = 1;
	public static final int MODE_GENERATE = 2;
		
	private int currMode;
	
	//Other object references needed
	private InputXMLDocumentPanel initialDocPanel;
	
	//GUI Components
	private ViewSemanticsPanelToolBar toolBar;
	private JSplitPane splitPane;
	private JPanel viewPanel;
	private CreationRightPanel creationPanel;
	private DisplayScreen display;
	private TextAreaWithScrollPane generatedArea;
	//private TextAreaWithScrollPane docInfoArea;
	private JTreeWithScrollPane jTreePane;
	private TextAreaWithScrollPane infoArea;
	private ArrayList<InputXMLDocumentPanel> xmlDocPanels;
		
	private GrammarDevController grammarDevController;
	
	public ViewSemanticsPanel(GrammarDevController grammarDevController, ArrayList<InputXMLDocument> loadedDocuments){ // Used for Loading a document
		this.grammarDevController = grammarDevController;
		//this.XMLDocsList = loadedDocuments;
		
		xmlDocPanels = new ArrayList<InputXMLDocumentPanel>();
		for(InputXMLDocument doc: loadedDocuments)
			xmlDocPanels.add(new InputXMLDocumentPanel(doc));

		this.initialDocPanel = xmlDocPanels.get(0);
		
		//Create GUI elements
		initializePanelSettings();
		initializeBar();
		initializeDisplay(MODE_VIEW);	
	}
		
	public ViewSemanticsPanel(GrammarDevController grammarDevController, String name, String category, String comments){ //Used for Creating a new document
		this.grammarDevController = grammarDevController;
		this.initialDocPanel = new InputXMLDocumentPanel(new InputXMLDocument(null, name, category, comments, null));
		this.xmlDocPanels = new ArrayList<InputXMLDocumentPanel>();
		xmlDocPanels.add(initialDocPanel);
		
		initializePanelSettings();
		initializeBar();
		initializeDisplay(MODE_EDIT);
	}
		
	//Creation of the right panels for viewing
	private JPanel createRightViewPanel(){
		//Initialize right panel
		jTreePane = new JTreeWithScrollPane("Grammar Development");
		infoArea= new TextAreaWithScrollPane("Component Information");
		generatedArea = new TextAreaWithScrollPane("Generated Sentence");

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(jTreePane);
		rightPanel.add(infoArea);
		
		int panelHeight = rightPanel.getPreferredSize().height;
		generatedArea.setPreferredSize(new Dimension(generatedArea.getPreferredSize().width, panelHeight*3/5));
		jTreePane.setPreferredSize(new Dimension(generatedArea.getPreferredSize().width, panelHeight/5));
		infoArea.setPreferredSize(new Dimension(generatedArea.getPreferredSize().width, panelHeight/5));
		
		
		return rightPanel;
	}
	
	//Initialize methods
	private void initializePanelSettings(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	}
	
	private void initializeBar(){
		toolBar = new ViewSemanticsPanelToolBar();
		InputXMLDocument initialDocument = initialDocPanel.getXMLDocument();
		if(xmlDocPanels != null){
			toolBar.setCurrFileSelectedText("Current File Selected: "+initialDocument.getName()+ "     File: 1/"+xmlDocPanels.size());
		}
		add(toolBar);
	}
	
	private void initializeDisplay(int initialMode){
		//right panels
		viewPanel = createRightViewPanel();
		creationPanel = new CreationRightPanel();
	
		//LeftPanel
		display = new DisplayScreen();
		display.display(this.initialDocPanel); //displays the first
					
		//Split Pane
		if(initialMode == MODE_VIEW)
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, display, viewPanel);
		else{
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, display, creationPanel);
			setMode(MODE_EDIT);
		}
		splitPane.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		splitPane.setDividerLocation( (int)(MainFrame.getInstance().getWidth()*3.0/5 + splitPane.getInsets().left));
		splitPane.setDividerSize(0);
		add(splitPane);
		
		//Set initially displayed info
		generatedArea.setTextAreaContent(display.getDisplaySentence());
	}
	
	public void initializeSentences(){
		display.setMode(DisplayScreen.MODE_INITIALIZE);
		generatedArea.setTextAreaContent("");
	}
	
	//Setters
	public void setSelectComponentPanelListener(SelectComponentActionListener selectListener){
		for(InputXMLDocumentPanel docPanel: xmlDocPanels)
			docPanel.setSelectComponentPanelListener(selectListener);	
	}
	
	public void setComponent(Component component){
		if(component != null){
			if(currMode == MODE_EDIT){
				creationPanel.setComponent(component);
			}
			else{
				StringBuilder info = new StringBuilder("Information about ");
				
				if(!component.getDescription().equals(component.getName())){
					info.append("(");
					info.append(component.getDescription());
					info.append(") ");
				}
				
				info.append(component.toString());
				info.append("\n\n");
				
				if(component.isLeaf())
					info.append("***Target Lexicon***\n"+component.toLexiconSentence()+"\n\n");
				info.append(component.getFeaturesInString(true));

				infoArea.setTextAreaContent(info.toString());
			}
		}
	}
	
	public void setMode(int mode){
		
		toolBar.setMode(mode);
		switch(mode){
			case MODE_VIEW:
				display.setMode(DisplayScreen.MODE_INITIALIZE);
				splitPane.setRightComponent(viewPanel);
				splitPane.setDividerLocation(0.6);
				display.display(this.initialDocPanel);
				generatedArea.setTextAreaContent(display.getDisplaySentence());
				viewPanel.remove(generatedArea);
				break;
			case MODE_EDIT:
				//reset input
				creationPanel.clearInput();
				
				//initialize with the panel currently selected
				if(grammarDevController.getCurrSelectedComponentPanel() != null){
					Component selected = grammarDevController.getCurrSelectedComponentPanel().getComponent();
					creationPanel.setComponent(selected);
				}
			
				splitPane.setRightComponent(creationPanel);
				splitPane.setDividerLocation(0.6);
				break;		
				
			case MODE_GENERATE:
				this.initialDocPanel= display.getCurrentlyDisplayedDocumentPanel();
				//display.display(new InputXMLDocumentPanel(XMLManager.getVerse(new File("InputXML\\generated.xml")), new SelectComponentActionListener(this)));
				display.setMode(DisplayScreen.MODE_GENERATE);
				display.getCurrentlyDisplayedDocumentPanel().adjustPositioning();
				generatedArea.setTextAreaContent(display.getDisplaySentence());
				viewPanel.add(generatedArea, 0);
				break;
		}

		this.currMode = mode;
	}
	
	public void setDocumentPanelIndex(int index) {
		if(index >=0 && index <xmlDocPanels.size()){
			InputXMLDocumentPanel desiredPanel = xmlDocPanels.get(index);
			toolBar.setCurrFileSelectedText("Current File Selected: "+desiredPanel.getXMLDocument().getName()+"  File: "+(index+1)+"/"+xmlDocPanels.size());
			display.setToolTipText(desiredPanel.getComments());
			display.display(desiredPanel);
		}
	}
	
	public void setDragAndDropListener(MouseAdapter mouseAdapter){
		if(mouseAdapter != null)
			creationPanel.addDnDListenerForAllButtons(mouseAdapter);
	}
	
	public void resetCreationPanel(){
		creationPanel.clearInput();
	}
	
	public void resetInfoPanel(){
		infoArea.setTextAreaContent("");
	}
	
	public void refreshSemanticLexicons(){
		for(InputXMLDocumentPanel panel: xmlDocPanels)
			panel.refreshSemanticLexicons();
	}
	
	//Getters
	public InputXMLDocumentPanel getCurrentlyDisplayedDocumentPanel(){
		if(display == null)
			return null;
		return display.getCurrentlyDisplayedDocumentPanel();
	
	}
	
	public CreationRightPanel getCreationPanel(){
		return creationPanel;
	}
	
	public ArrayList<InputXMLDocumentPanel> getDocumentPanelList(){
		return xmlDocPanels;
	}
	
	//Listener setters
	public void setPindutanButtonListener(PindutanListener listener){
		toolBar.setPindutanListener(listener);
	}
	
	public void setEditButtonListener(LoadPanelToolbarBtnListener listener){
		toolBar.setEditButtonListener(listener);
	}
	
	public void setGenerateButtonListener(LoadPanelToolbarBtnListener listener){
		toolBar.setGenerateButtonListener(listener);
	}
	
	public void setDoneEditingButtonListener(LoadPanelToolbarBtnListener listener){
		toolBar.setDoneEditingButtonListener(listener);
	}

	public void setInitializeButtonListener(LoadPanelToolbarBtnListener listener){
		toolBar.setInitializeButtonListener(listener);
	}

	public void setEditDocInfoButtonListener(EditDocInfoButtonListener listener){
		toolBar.setEditDocInfoButtonListener(listener);
	}
	
	public void setNextButtonListener(NextPrevListener listener){
		toolBar.setBtnNextListener(listener);
	}
	
	public void setPrevButtonListener(NextPrevListener listener){
		toolBar.setBtnPrevListener(listener);
	}
	
}