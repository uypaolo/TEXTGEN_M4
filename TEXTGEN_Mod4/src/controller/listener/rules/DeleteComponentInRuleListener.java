package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import rules.Structural;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

import components.Phrase;

public class DeleteComponentInRuleListener implements ActionListener{

	AddRuleController controller;
	
	public DeleteComponentInRuleListener(AddRuleController controller){
		this.controller = controller;
	}
	public void actionPerformed(ActionEvent arg0) {
		ComponentPanel selectedPanel = controller.getCurrSelectedPanel();
		if(selectedPanel != null){
			InputXMLDocumentPanel currDisplayedDocPanel = selectedPanel.getParentDocPanel();
			
			int n = JOptionPane.showConfirmDialog(
				    null,
				    "Are you sure you want to delete this component and all of its children?",
				    "Confirm",
				    JOptionPane.YES_NO_OPTION);
			
			
			
			if(n == JOptionPane.YES_OPTION){
				if(selectedPanel.getParentComponentPanel() != null){
					//remove internally
					if(selectedPanel.getRootDocPanel().getName().equals("FROM")){
						controller.setFlag("EDIT_RESET");
					}
					Phrase parentComponent = (Phrase)selectedPanel.getParentComponentPanel().getComponent();
					parentComponent.removeChild(selectedPanel.getComponent());
					if(selectedPanel.getRootDocPanel().getName().equals("TO")){
						Structural structRule = controller.getStructRule();
						structRule.deleteComponent(parentComponent, selectedPanel.getComponent());
					}
					//remove in gui
					selectedPanel.getParentComponentPanel().removeChild(selectedPanel);
					controller.adjustPositioning();
				}
				else{
					//remove internally
					if(currDisplayedDocPanel.getName().equals("FROM")){
						controller.setFlag("EDIT_RESET");
					}
					currDisplayedDocPanel.getXMLDocument().removeSentence(selectedPanel.getComponent());
					if(currDisplayedDocPanel.getName().equals("TO")){
						Structural structRule = controller.getStructRule();
						structRule.deleteComponentAsRoot(selectedPanel.getComponent());
					}
					//remove in gui
					currDisplayedDocPanel.removeChild(selectedPanel);
				}
				controller.deselectCurrSelectedPanel();
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
