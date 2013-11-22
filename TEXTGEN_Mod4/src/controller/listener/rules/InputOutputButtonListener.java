package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class InputOutputButtonListener implements ActionListener{
	
	int curr;
	final int CASE_INPUT = 1;
	final int CASE_OUTPUT = 2;
	AddRuleController controller;
	public InputOutputButtonListener(AddRuleController controller){
		this.controller = controller;
		curr = CASE_INPUT;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object[] options = {"Continue", "Cancel",};
		int n;
		switch(curr){
		case CASE_INPUT: 
						n = JOptionPane.showOptionDialog(null,
								  "Switching to Output will remove all info stored in the clipboard "
								  + "\n ex. Panels on Move\n Continue?",
								  "Please Read",
								  JOptionPane.YES_NO_CANCEL_OPTION,
								  JOptionPane.QUESTION_MESSAGE,
								  null,
								  options,
								  options[1]);
						  if(n == 0){
							  controller.setRightVisible();
								curr = CASE_OUTPUT;
						  }
				
						
						break;
		case CASE_OUTPUT: 
						  n = JOptionPane.showOptionDialog(null,
								  "Going back to Criteria page will remove all changes "
								  + "in the output page\n Continue?",
								  "Please Read",
								  JOptionPane.YES_NO_CANCEL_OPTION,
								  JOptionPane.QUESTION_MESSAGE,
								  null,
								  options,
								  options[1]);
						  if(n == 0){
							  controller.setLeftVisible();
							  curr = CASE_INPUT;
						  }
						break;
		}
	}

}
