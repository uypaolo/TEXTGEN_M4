package controller.listener.grammardev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.MainController;

public class DevelopGrammarListener implements ActionListener{

	private MainController mainController;
	
	public DevelopGrammarListener(MainController mainController){
		this.mainController = mainController;
	}
	
	public void actionPerformed(ActionEvent e) {
		mainController.goToGrammarDevelopment();
	}
}
