package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JOptionPane;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

import components.Phrase;

public class CopyCompButtonListener implements ActionListener{
	AddRuleController controller;
	CopyCompPanelAdapter mouseAdapter;
	
	public CopyCompButtonListener(AddRuleController controller){
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		controller.removeListeners();
		ComponentPanel panelToMove = controller.getCurrSelectedPanel();
		ComponentPanel parentExtractedFrom;
		InputXMLDocumentPanel xmlParentExtractedFrom;
		int indexFromParent;
		if(panelToMove!=null){
			mouseAdapter = new CopyCompPanelAdapter(controller.getCurrSelectedPanel()
					,controller);
			controller.setPanelCopyListeners(mouseAdapter);
		}
		else{
			JOptionPane.showMessageDialog(null,
				    "No panel selected",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

}
