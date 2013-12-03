package controller.listener.grammardev.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.grammardevelopment.InputXMLDocumentPanel;
import view.grammardevelopment.ViewSemanticsPanel;

import components.InputXMLDocument;

public class LoadPanelToolbarBtnListener implements ActionListener{
	
	private ViewSemanticsPanel loadPanel;
	public static int desiredMode;
	
	public LoadPanelToolbarBtnListener(ViewSemanticsPanel loadPanel, int desiredMode){
		this.loadPanel = loadPanel;
		this.desiredMode = desiredMode;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(LoadPanelToolbarBtnListener.desiredMode == ViewSemanticsPanel.MODE_GENERATE){
			ArrayList<InputXMLDocumentPanel> docs;
			int currIndex;
			docs = loadPanel.getDocumentPanelList();
			currIndex = loadPanel.getCurrIndex();
			InputXMLDocumentPanel currXML = docs.get(currIndex);
			InputXMLDocument curr = currXML.getXMLDocument();
			curr.getClauses();
		}
		loadPanel.setMode(desiredMode);
		
	}
}