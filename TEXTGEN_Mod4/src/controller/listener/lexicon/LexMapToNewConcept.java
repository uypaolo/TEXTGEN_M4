package controller.listener.lexicon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import ontology.ConceptList;
import ontology.PartOfSpeech;
import view.lexicon.LexiconPanel;

public class LexMapToNewConcept implements ActionListener{
	
	JComboBox cmb ;
	JTable conceptTable;
	
	LexiconPanel lexPanel;
	ConceptList conceptList;
	
	LexiconList lexList;
	
	public LexMapToNewConcept(LexiconPanel lexPanel){
		this.lexPanel = lexPanel;
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(lexPanel.getTable().getSelectedRow()== -1 )
			JOptionPane.showMessageDialog(null,
				    "Error: No row selected!",
				    "No row selected",
				    JOptionPane.ERROR_MESSAGE);
		else{
			final JFrame frame = new JFrame("Concept Table");
			frame.setSize(new Dimension(600,500));
			frame.setLayout(null);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			JLabel label = new JLabel("Parts of Speech");
		
			//Just set the default Ontology POS to the Lexicon POS
			cmb = new JComboBox(OntologyManager.getInstance().getAllPOSForDisplay().toArray());
			PartOfSpeech selectedPOS = lexPanel.getSelectedPOS();
			if(selectedPOS != null){
				int numChoices = cmb.getModel().getSize();
				for(int i=0; i < numChoices; i++){
					PartOfSpeech currPOS = (PartOfSpeech)cmb.getModel().getElementAt(i);
					if(currPOS.getName().equalsIgnoreCase(selectedPOS.getName())){
						cmb.setSelectedItem(currPOS);
						break;
					}
				}
					
			}
			
			
			JScrollPane scrollPane = new JScrollPane();
			conceptTable = new JTable(){
				public boolean isCellEditable(int row, int column) {
				       return false;
				    }
			};
			conceptList = OntologyManager.getInstance().getDisplayConceptList((PartOfSpeech)lexPanel.getCmbCategory().getSelectedItem() );
			DefaultTableModel model = (DefaultTableModel)conceptTable.getModel();
			model.setColumnIdentifiers(new String[] {"Concept Stems","Senses", "Definition"}); // <-- column headings
			conceptList = OntologyManager.getInstance().getDisplayConceptList(LexiconManager.getInstance().getAllPOS().get(0));
			ArrayList<Concept> concepts = conceptList.getConceptList();
			for(Concept concept: concepts){
				String[] data = {concept.getName(),concept.getSense(), concept.getDefinition()}; // should get values as displayed above.
				model.addRow(data);
			}
			
			//GUI Settings
			label.setBounds(90,10,100,25);
			cmb.setBounds(200,10, 100, 25);
			scrollPane.setViewportView(conceptTable);
			scrollPane.setBounds(10,40,550,350);
			JButton mapButton = new JButton("Map this concept to lexicon");
			mapButton.setBounds(400,420,170,30);
			JButton removeButton = new JButton("Remove currently mapped concept");
			removeButton.setBounds(145,420,225,30);
			frame.getContentPane().add(label);
			frame.getContentPane().add(cmb);
			frame.getContentPane().add(scrollPane);
			frame.getContentPane().add(mapButton);
			frame.getContentPane().add(removeButton);
			frame.setVisible(true);
			
			lexList = LexiconManager.getInstance().getLexiconList(lexPanel.getCodeFromSelectedPOS());
			Lexicon lex = lexList.getLexiconList().get(lexPanel.getTable().getSelectedRow());
			for(int i = 0 ; i<conceptList.getConceptList().size();i++){
				if(lex.getMappedConcept().equals(conceptList.getConceptList().get(i).getName()))
					conceptTable.setRowSelectionInterval(i, i);
			}
			
			cmb.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent arg0) {
					DefaultTableModel model = (DefaultTableModel)conceptTable.getModel();
					if (model.getRowCount() > 0) {
			            for (int i = model.getRowCount() - 1; i > -1; i--) {
			                model.removeRow(i);
			            }
			        }
					conceptList = OntologyManager.getInstance().getDisplayConceptList((PartOfSpeech)cmb.getSelectedItem() );
					if(conceptList != null){
						ArrayList<Concept> concepts = conceptList.getConceptList();
						for(Concept concept: concepts){
							String[] data = {concept.getName(),concept.getSense(),concept.getDefinition()}; // should get values as displayed above.
							model.addRow(data);
						}
						conceptTable.setModel(model);
					}
				}});
			
			mapButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(conceptTable.getSelectedRow()!= -1){
						Lexicon lex = lexList.getLexiconList().get(lexPanel.getTable().getSelectedRow()); //-selected lex that will be mapped
						Concept concept = conceptList.getConceptList().get(conceptTable.getSelectedRow());
						lex.setMapping(concept.getName(), concept.getSense(), ((PartOfSpeech)cmb.getSelectedItem()).getName());
						lexPanel.initTable();
						frame.dispose();
					}
					else{
						JOptionPane.showMessageDialog(null,
							    "Error: No row selected!",
							    "No row selected",
							    JOptionPane.ERROR_MESSAGE);
					}	
				}
			});
			
			removeButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					Lexicon lex = lexList.getLexiconList().get(lexPanel.getTable().getSelectedRow());
					lex.removeMapping();
					lexPanel.initTable();
					frame.dispose();
				}});
		
		}
	}

}
