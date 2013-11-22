package controller.listener.lexicon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import managers.LexiconManager;
import view.lexicon.LexiconPanel;

public class LexAddNewPOSActionListener implements ActionListener{

	JFrame addPOS;
	JTextField newPOS = new JTextField();
	
	LexiconPanel lexPanel;
	
	public LexAddNewPOSActionListener(LexiconPanel lexPanel){
		this.lexPanel = lexPanel;
	}
		
		
		public void actionPerformed(ActionEvent e) {
			addPOS = new JFrame("Add New POS");
			addPOS.setSize(new Dimension(300,110));
			addPOS.setLocationRelativeTo(null);
			addPOS.setLayout(null);
			JButton okBtn = new JButton("OK");
			newPOS.setBounds(2, 10, 280, 20);
			okBtn.setBounds(180, 40, 70, 30);
			addPOS.add(newPOS);
			addPOS.add(okBtn);
			addPOS.setVisible(true);
			
			okBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					
					if(LexiconManager.getInstance().getAllPOS().contains(newPOS.getText())) //gagawa nito sa manager side.
						JOptionPane.showMessageDialog(null,
							    "POS to be added is already in the list! ERROR!",
							    "Duplicate entry",
							    JOptionPane.ERROR_MESSAGE);
					else{
						LexiconManager.getInstance().addNewPOSForCurrentLanguage(newPOS.getText().trim());
						lexPanel.getCmbCategory().setModel(new JComboBox(LexiconManager.getInstance().getAllPOS().toArray()).getModel());
						lexPanel.getCmbCategory().setSelectedItem(newPOS.getText().trim());
						lexPanel.initTable();
						addPOS.dispose();
					}
						
				}
				
			});
		}

}
