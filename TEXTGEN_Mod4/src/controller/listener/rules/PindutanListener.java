package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import view.grammardevelopment.InputXMLDocumentPanel;
import view.rules.RuleApplyWindow;

public class PindutanListener implements ActionListener{
	
	private ArrayList<InputXMLDocumentPanel> docs;
	
	public PindutanListener(ArrayList<InputXMLDocumentPanel> docList){
		docs = docList;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		new RuleApplyWindow(docs);
	}

}
