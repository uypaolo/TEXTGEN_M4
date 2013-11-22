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

import managers.OntologyManager;
import ontology.Concept;
import ontology.ConceptList;
import view.grammardevelopment.ComponentPanel;

import components.Leaf;

public class ConceptEditFrame extends JFrame{

	ComponentPanel compPanel;
	ConceptList conceptList;
	JTable conceptTable;
	
	public ConceptEditFrame(ComponentPanel compPanelx){
		this.compPanel = compPanelx;
		conceptList = OntologyManager.getInstance().getConceptList(compPanel.getComponent().getName());
		
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
		setButton.setBounds(400,420,200,30);
		getContentPane().add(label);
		getContentPane().add(scrollPane);
		getContentPane().add(setButton);
		setVisible(true);
		
		setButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(conceptTable.getSelectedRow()!= -1){
					Concept concept = conceptList.getConceptList().get(conceptTable.getSelectedRow());
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
