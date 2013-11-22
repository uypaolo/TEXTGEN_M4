package controller.listener.grammardev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import managers.SemanticsManager;
import view.MainFrame;
import view.grammardevelopment.FileSaveLoad;

import components.InputXMLDocument;

import controller.GrammarDevController;

public class LoadSemanticsActionListener implements ActionListener {

	private GrammarDevController grammarDevController;
	
	public LoadSemanticsActionListener(GrammarDevController grammarDevController){
		this.grammarDevController = grammarDevController;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FileSaveLoad fsl = new FileSaveLoad(grammarDevController);
		File[] filesLoaded = fsl.loadFiles();
		if(filesLoaded.length>0){

			ArrayList<InputXMLDocument> loadedDocuments = new ArrayList<InputXMLDocument>();
			for(File file: filesLoaded)
				loadedDocuments.add(SemanticsManager.readSemanticsDocumentFromFile(file));
		
			grammarDevController.loadDocuments(loadedDocuments);
			MainFrame.getInstance().setPanel(grammarDevController.getPanel());
		}
	}
}
