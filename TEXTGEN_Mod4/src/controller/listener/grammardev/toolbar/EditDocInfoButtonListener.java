package controller.listener.grammardev.toolbar;

import java.awt.event.ActionEvent;

import view.MainFrame;
import view.grammardevelopment.editsemantics.NewSemanticsInfoDialog;

import components.InputXMLDocument;

import controller.GrammarDevController;
import controller.listener.grammardev.SemanticsInfoListener;

public class EditDocInfoButtonListener extends SemanticsInfoListener {
	private GrammarDevController grammarDevController;
	public EditDocInfoButtonListener(GrammarDevController grammarDevController){
		this.grammarDevController = grammarDevController;
	}
	
	public void actionPerformed(ActionEvent e) {
		dialog = new NewSemanticsInfoDialog(MainFrame.getInstance(), "New Semantics", this, grammarDevController.getCurrentlyDisplayedDocumentPanel().getXMLDocument());
		dialog.setVisible(true);
		dialog.setModal(true);
	}
	
	public void proceed(boolean isCanceled){
		if(!isCanceled){
			InputXMLDocument doc = grammarDevController.getCurrentlyDisplayedDocumentPanel().getXMLDocument();
			doc.setName(dialog.getName());
			doc.setCategory(dialog.getCategory());
			doc.setOtherInfo(dialog.getComments());
			grammarDevController.getCurrentlyDisplayedDocumentPanel().refreshTitle();
		}
	}
}