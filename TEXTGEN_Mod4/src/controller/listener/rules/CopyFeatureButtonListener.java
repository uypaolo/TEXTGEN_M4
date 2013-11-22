package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

public class CopyFeatureButtonListener implements ActionListener{

	AddRuleController controller;
	CopyFeaturePanelAdapter mouseAdapter ;
	
	public CopyFeatureButtonListener(AddRuleController controller){
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		controller.removeListeners();
		ComponentPanel panelToGetFeatFrom = controller.getCurrSelectedPanel();
		if(panelToGetFeatFrom!=null){
			mouseAdapter = new CopyFeaturePanelAdapter(controller,
					controller.getCurrSelectedPanel());
			controller.setPanelCopyFeatListener(mouseAdapter);
		}
		else{
			JOptionPane.showMessageDialog(null,
				    "No panel selected",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
		}
	}

	public CopyFeaturePanelAdapter getAdapter(){
		return mouseAdapter;
	}
}
