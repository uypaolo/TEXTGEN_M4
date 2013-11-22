package controller.listener.grammardev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import managers.XMLManager;

import org.jdom2.Element;

import view.grammardevelopment.FileSaveLoad;
import view.grammardevelopment.InputXMLDocumentPanel;

import components.InputXMLDocument;

import controller.GrammarDevController;

public class SaveActionListener implements ActionListener {

	private GrammarDevController grammarDevController;
	
	public SaveActionListener(GrammarDevController grammarDevController){
		this.grammarDevController = grammarDevController;
	}
	
	public void actionPerformed(ActionEvent arg0){

		
		InputXMLDocumentPanel documentPanel = grammarDevController.getCurrentlyDisplayedDocumentPanel();
		
		if(documentPanel != null){
			InputXMLDocument document = documentPanel.getXMLDocument();
			
			if(document.getXmlFile()!=null){
				Element documentElement = document.generateXMLCopy();
				if(XMLManager.getInstance().writeToXML(document.getXmlFile().getPath(), documentElement)){
					JOptionPane.showMessageDialog(null,
						    "Successfully Saved '"+document.getXmlFile().getName()+"'",
						    "Save Success!",
						    JOptionPane.INFORMATION_MESSAGE);
					
				}
				else{
					JOptionPane.showMessageDialog(null,
						    "Error in saving file!",
						    "Save Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
			else{
				FileSaveLoad fsl = new FileSaveLoad(grammarDevController);
				fsl.saveFile(document);
			}
		}
		else{ //No file being worked on yet.
			JOptionPane.showMessageDialog(null,
				    "No document to save, please create or load a document before saving",
				    "Save Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}