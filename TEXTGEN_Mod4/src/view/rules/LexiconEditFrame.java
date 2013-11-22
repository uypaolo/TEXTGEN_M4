package view.rules;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lexicon.Lexicon;
import lexicon.LexiconList;
import managers.LexiconManager;
import managers.OntologyManager;
import ontology.Concept;

import components.Leaf;

import view.grammardevelopment.ComponentPanel;

public class LexiconEditFrame extends JFrame{
	
	ComponentPanel currSelectedPanel;
	LexiconList lexiconList;
	JTable lexiconTable;
	public LexiconEditFrame(ComponentPanel panel){
		this.currSelectedPanel = panel;
		
		lexiconList = LexiconManager.getInstance().getLexiconList(panel.getComponent().getName());
		
		setSize(new Dimension(600,500));
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		
		JLabel label = new JLabel("Set Lexicon In Leaf");
		
		JScrollPane scrollPane = new JScrollPane();
		lexiconTable = new JTable(){
			public boolean isCellEditable(int row, int column) {
			       return false;
			    }
		};
		DefaultTableModel model = (DefaultTableModel)lexiconTable.getModel();
		model.setColumnIdentifiers(new String[] {"Lexicon Stems","Glosses", "Comments"}); // <-- column headings
		ArrayList<Lexicon> lexiList = lexiconList.getLexiconList();
		for(Lexicon lexicon: lexiList){
			String[] data = {lexicon.getName(),lexicon.getGloss(), lexicon.getComments()}; // should get values as displayed above.
			model.addRow(data);
		}
		
		//GUI Settings
		label.setBounds(250,10,300,25);
		scrollPane.setViewportView(lexiconTable);
		scrollPane.setBounds(10,40,550,350);
		JButton setButton = new JButton("Set this concept into the leaf");
		setButton.setBounds(400,420,200,30);
		getContentPane().add(label);
		getContentPane().add(scrollPane);
		getContentPane().add(setButton);
		setVisible(true);
		
		setButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(lexiconTable.getSelectedRow()!= -1){
					Lexicon lexicon = lexiconList.getLexiconList().get(lexiconTable.getSelectedRow());
					Leaf comp = (Leaf)currSelectedPanel.getComponent();
					comp.setLexicon(lexicon);
					currSelectedPanel.refreshLabelText();
					dispose();
				}
				else{
					JOptionPane.showMessageDialog(null,
						    "Error: No row selected!",
						    "No row selected",
						    JOptionPane.ERROR_MESSAGE);
				}	
			}
		});
	}
}
