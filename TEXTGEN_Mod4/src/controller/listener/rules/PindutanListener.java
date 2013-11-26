package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.grammardevelopment.InputXMLDocumentPanel;
import view.grammardevelopment.ViewSemanticsPanel;
import view.rules.RuleApplyWindow;

public class PindutanListener implements ActionListener{
	
	private ArrayList<InputXMLDocumentPanel> docs;
	private int currIndex;
	
	ViewSemanticsPanel devpanel;
	
	public PindutanListener(ViewSemanticsPanel devpanel){
		this.devpanel = devpanel;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.docs = devpanel.getDocumentPanelList();
		this.currIndex = devpanel.getCurrIndex();
		new RuleApplyWindow(docs.get(currIndex));
	}

}
