package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JOptionPane;

import components.Phrase;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

public class MoveButtonListener implements ActionListener{

	AddRuleController controller;
	MovePanelMouseAdapter mouseAdapter;
	public MoveButtonListener(AddRuleController controller){
		this.controller = controller;
		
	}
	
	public void actionPerformed(ActionEvent arg0){
		controller.removeListeners();
		ComponentPanel panelToMove = controller.getCurrSelectedPanel();
		ComponentPanel parentExtractedFrom;
		InputXMLDocumentPanel xmlParentExtractedFrom;
		int indexFromParent;
		if(panelToMove!=null){
			if(panelToMove.getParentComponentPanel()!=null){
				parentExtractedFrom = panelToMove.getParentComponentPanel();
				panelToMove.getParentComponentPanel().removeChild(panelToMove);
				Phrase parent = (Phrase)panelToMove.getParentComponentPanel().getComponent();
				parent.removeChild(panelToMove.getComponent());
				controller.adjustPositioning();
				
			
			}
			else if(panelToMove.getParentDocPanel()!=null){
				xmlParentExtractedFrom = panelToMove.getParentDocPanel();
				panelToMove.getParentDocPanel().removeChild(panelToMove);
				panelToMove.getParentDocPanel().getXMLDocument().removeSentence(panelToMove.getComponent());
				controller.adjustPositioning();
				
			}
			mouseAdapter = new MovePanelMouseAdapter(controller.getCurrSelectedPanel()
					,controller);
			controller.setPanelMoveListeners(mouseAdapter);
		}
		else{
			JOptionPane.showMessageDialog(null,
				    "No panel selected",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
		}
		
		
	}
	
	public MovePanelMouseAdapter getMouseAdapterOfPanels(){
		return mouseAdapter;
	}

}
