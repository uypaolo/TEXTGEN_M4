package view.grammardevelopment.editsemantics;


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

import managers.OntologyManager;
import ontology.Concept;
import ontology.ConceptList;
import view.grammardevelopment.ComponentPanel;

import components.Component;
import components.Leaf;

import controller.GrammarDevController;

public class LeafEditFrame extends JFrame{
	
	Leaf comp;
	ConceptList conceptList;
	JTable conceptTable;
	GrammarDevController grammarDevController;
	
	public LeafEditFrame(Component compo,GrammarDevController grammarDevControllerX){
		this.comp = (Leaf)compo;
		this.grammarDevController = grammarDevControllerX;
		conceptList = OntologyManager.getInstance().getConceptList(comp.getName()); //pos
		
		setSize(new Dimension(600,500));
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		
		JLabel label = new JLabel("Set Concept In Leaf");
		
		JScrollPane scrollPane = new JScrollPane();
		conceptTable = new JTable(){
			public boolean isCellEditable(int row, int column) {
			       return false;
			    }
		};
		DefaultTableModel model = (DefaultTableModel)conceptTable.getModel();
		model.setColumnIdentifiers(new String[] {"Concept Stems","Senses", "Definition"}); // <-- column headings
		ArrayList<Concept> concepts = conceptList.getConceptList();
		for(Concept concept: concepts){
			String[] data = {concept.getName(),concept.getSense(), concept.getDefinition()}; // should get values as displayed above.
			model.addRow(data);
		}
		
		//GUI Settings
		label.setBounds(250,10,300,25);
		scrollPane.setViewportView(conceptTable);
		scrollPane.setBounds(10,40,550,350);
		JButton setButton = new JButton("Set this concept into the leaf");
		setButton.setBounds(400,420,170,30);
		getContentPane().add(label);
		getContentPane().add(scrollPane);
		getContentPane().add(setButton);
		setVisible(true);
		
		
		setButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(conceptTable.getSelectedRow()!= -1){
					Concept concept = conceptList.getConceptList().get(conceptTable.getSelectedRow());
					ComponentPanel compPanel = grammarDevController.getCurrSelectedComponentPanel();
					Leaf comp = (Leaf)compPanel.getComponent();
					
					comp.setConcept(concept.getName());
					comp.setLexicalSense(concept.getSense());
					compPanel.refreshLabelText();
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
