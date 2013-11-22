package controller.listener.grammardev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import view.grammardevelopment.FileSaveLoad;
import view.grammardevelopment.InputXMLDocumentPanel;
import controller.GrammarDevController;

public class SaveAsListener implements ActionListener{

	private GrammarDevController grammarDevController;
	
	public SaveAsListener(GrammarDevController grammarDevController){
		this.grammarDevController = grammarDevController;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		FileSaveLoad fsl = new FileSaveLoad(grammarDevController);
		InputXMLDocumentPanel currDisplayedDocumentPanel = grammarDevController.getCurrentlyDisplayedDocumentPanel();
		
		
		if( currDisplayedDocumentPanel != null)
			fsl.saveFile(currDisplayedDocumentPanel.getXMLDocument());
		else{
			JOptionPane.showMessageDialog(null,
				    "No document to save, please create/load a document before saving",
				    "Save Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}
