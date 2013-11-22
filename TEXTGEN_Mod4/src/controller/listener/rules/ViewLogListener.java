package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ViewLogListener implements ActionListener{

	AddRuleController controller;
	JFrame popUp;
	JTextArea txtArea;
	public ViewLogListener(AddRuleController controller){
		this.controller = controller;
	}
	
	
	public void actionPerformed(ActionEvent arg0) {
		popUp = new JFrame("View Log");
		txtArea = new JTextArea();
		txtArea.setEditable(false);
		txtArea.setText(controller.getStructRule().getActionLog());
		JScrollPane scrollPane = new JScrollPane(txtArea);
		popUp.add(scrollPane);
		popUp.setSize(600,400);
		popUp.setVisible(true);
		popUp.setLocationRelativeTo(null);
	}
	public JFrame getFrame(){
		return popUp;
	}
	
	public void setText(String string){
		txtArea.setText(string);
	}

}
