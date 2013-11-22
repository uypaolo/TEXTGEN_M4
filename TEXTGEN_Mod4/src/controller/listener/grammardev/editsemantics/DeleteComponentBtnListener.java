package controller.listener.grammardev.editsemantics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

import components.Phrase;

import controller.GrammarDevController;

public class DeleteComponentBtnListener implements ActionListener{
	
	private GrammarDevController grammarDevController;
	
	public DeleteComponentBtnListener(GrammarDevController grammarDevController){
		this.grammarDevController = grammarDevController;
	}

	
	public void actionPerformed(ActionEvent e) {
		ComponentPanel selectedPanel = grammarDevController.getCurrSelectedComponentPanel();
		if(selectedPanel != null){
			InputXMLDocumentPanel currDisplayedDocPanel = grammarDevController.getCurrentlyDisplayedDocumentPanel();
			
			int n = JOptionPane.showConfirmDialog(
				    null,
				    "Are you sure you want to delete this component and all of its children?",
				    "Confirm",
				    JOptionPane.YES_NO_OPTION);
			
			
			if(n == JOptionPane.YES_OPTION){
				if(selectedPanel.getParentComponentPanel() != null){
					//remove internally
					Phrase parentComponent = (Phrase)selectedPanel.getParentComponentPanel().getComponent();
					parentComponent.removeChild(selectedPanel.getComponent());
					
					//remove in gui
					selectedPanel.getParentComponentPanel().removeChild(selectedPanel);
					currDisplayedDocPanel.adjustPositioning();
				}
				else{
					//remove internally
					currDisplayedDocPanel.getXMLDocument().removeSentence(selectedPanel.getComponent());
					//remove in gui
					currDisplayedDocPanel.removeChild(selectedPanel);
				}
				grammarDevController.deselectCurrSelectedPanel();
			}
			
		}
		else{
			JOptionPane.showMessageDialog(null,
				    "No panel selected",
				    "No panel selected",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

}
