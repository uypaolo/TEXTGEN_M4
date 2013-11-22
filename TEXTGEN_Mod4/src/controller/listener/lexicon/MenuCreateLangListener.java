package controller.listener.lexicon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.lexicon.NewLanguagePopUp;
import controller.MainController;

public class MenuCreateLangListener implements ActionListener{

	private NewLanguagePopUp popUp;
	private MainController mainController;
	
	public MenuCreateLangListener(MainController mainController){
		this.mainController = mainController;
	}
	
	public void actionPerformed(ActionEvent e) {
		popUp = new NewLanguagePopUp(this);
	}
	
	public void proceed(){
		mainController.createLanguage(popUp.getNewLanguageName());
	}
}
