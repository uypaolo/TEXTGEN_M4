package controller.listener.grammardev.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.grammardevelopment.InputXMLDocumentPanel;
import view.grammardevelopment.ViewSemanticsPanel;
import controller.GrammarDevController;

public class NextPrevListener implements ActionListener{

	//This class takes care of switching files (previous or next) in the viewing of documents
	
	public static final int MODE_PREV = 0;
	public static final int MODE_NEXT = 1;
	
	private int mode;
	private ArrayList<InputXMLDocumentPanel> XMLList;
	private ViewSemanticsPanel loadPanel;
	private GrammarDevController grammarDevController;
	
	public NextPrevListener(int mode, GrammarDevController grammarDevController){
		this.grammarDevController = grammarDevController;
		this.loadPanel = grammarDevController.getPanel();
		this.XMLList = loadPanel.getDocumentPanelList();
		this.mode = mode;
	}
	
	public void actionPerformed(ActionEvent e) {
		int index = -1;
		InputXMLDocumentPanel currDisplayedDocumentPanel = loadPanel.getCurrentlyDisplayedDocumentPanel();
		
		switch(mode){
		case MODE_NEXT:   
			index = checkIndex(currDisplayedDocumentPanel);
			if(index+1 < XMLList.size()){
				loadPanel.setDocumentPanelIndex(index+1);
				grammarDevController.deselectCurrSelectedPanel();
			}
			break;
									
									
		case MODE_PREV:   
			index = checkIndex(currDisplayedDocumentPanel);
			if(index-1 >=0){
				loadPanel.setDocumentPanelIndex(index-1);
				grammarDevController.deselectCurrSelectedPanel();
			}
		    break;
		}
		
	}
	
	private int checkIndex(InputXMLDocumentPanel currPanel){
		int index = -1;
		for(int i = 0 ; i <XMLList.size();i++){
			if (XMLList.get(i).equals(currPanel))
				index = i ;
		}
		return index;
			
	}

}
