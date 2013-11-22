package controller.listener.lexicon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import managers.LexiconManager;
import view.lexicon.LexiconPanel;

public class LexNewEntryButtonListener implements ActionListener{
	private JTextField lexiconTF;
	private JTextField glossTF;
	private JFrame popUp;
	
	private LexiconPanel lexPanel;
	
	public LexNewEntryButtonListener(LexiconPanel lexPanel){
		this.lexPanel = lexPanel;
	}
	
	public void actionPerformed(ActionEvent e) {
		popUp = new JFrame("Add New Stem");
		popUp.setSize(new Dimension(300,200));
		popUp.setLocationRelativeTo(null);
		popUp.setVisible(true);
		popUp.getContentPane().setLayout(null);
		
		JLabel label = new JLabel("Input the name of the new lexicon stem: ");
		label.setBounds(5,10,300,15);
		lexiconTF = new JTextField();
		lexiconTF.setBounds(10,30,270,20);
		
		JLabel label2 = new JLabel("Input the gloss of the new lexicon stem: ");
		label2.setBounds(5,60,300,15);
		glossTF = new JTextField();
		glossTF.setBounds(10,80,270,20);
		
		JButton okBtn = new JButton("Ok");
		okBtn.setBounds(120,115,50,20);
		
		popUp.getContentPane().add(label);
		popUp.getContentPane().add(lexiconTF);
		popUp.getContentPane().add(label2);
		popUp.getContentPane().add(glossTF);
		popUp.getContentPane().add(okBtn);
		
		okBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				//check text - store into manager side - reload table - sorted
				if(lexPanel.isLexiconExist(lexiconTF.getText().trim(),-1))
					JOptionPane.showMessageDialog(null,"No Duplicates Allowed!","ERROR",JOptionPane.ERROR_MESSAGE);
				else{
					LexiconManager.getInstance().getLexiconList(lexPanel.getCodeFromSelectedPOS()).addLexicon(lexiconTF.getText().trim(),glossTF.getText().trim());
					popUp.dispose();
					lexPanel.initTable();
				}
				
			}});
	}

}
