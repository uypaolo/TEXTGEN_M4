package view.lexicon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import managers.LexiconManager;
import controller.listener.lexicon.MenuCreateLangListener;

public class NewLanguagePopUp extends JDialog{
	
	private MenuCreateLangListener parentListener;
	private JTextField nameField;
	
	public NewLanguagePopUp(MenuCreateLangListener parentListener){
		
		this.parentListener = parentListener;
		
		setSize(new Dimension(300,120));
		setLocationRelativeTo(null);
		setVisible(true);
		getContentPane().setLayout(null);
		//setModal(true);
		
		JLabel label = new JLabel("Input the name of the new language: ");
		label.setBounds(5,10,300,15);
		nameField = new JTextField();
		nameField.setBounds(10,30,270,20);
		JButton okBtn = new JButton("Ok");
		okBtn.setBounds(120,55,50,20);
				
		okBtn.addActionListener(new OkListener());
		
		getContentPane().add(label);
		getContentPane().add(nameField);
		getContentPane().add(okBtn);
		
	}
	
	public String getNewLanguageName(){
		return nameField.getText().trim();
	}
	
	class OkListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(nameField.getText().trim().isEmpty())
				JOptionPane.showMessageDialog(null,  new String("Please enter a name."));
			else{
				String languageName = nameField.getText().trim();
				
				if(LexiconManager.getInstance().isLanguageValid(languageName)){
					JOptionPane.showMessageDialog(null,  new String("That language already exists in the database."));
				}
				else{
					setVisible(false);
					parentListener.proceed();
				}
			}
				
		}
		
		
	}
	
}
