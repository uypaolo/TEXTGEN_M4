package controller.listener.ontology;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import managers.OntologyManager;
import ontology.PartOfSpeech;
import view.ontology.OntologyPanel;

public class OntoBtnNewConceptListener implements ActionListener{
	
	OntologyPanel ontoPanel;
	public OntoBtnNewConceptListener(OntologyPanel ontoPanel){
		this.ontoPanel = ontoPanel;
	}
		JFrame popUp ;
		JTextField txtField;
		public void actionPerformed(ActionEvent e) {
			ontoPanel.getScrollPane().getVerticalScrollBar().setValue(0);
			popUp = new JFrame("Add New Concept");
			popUp.setSize(new Dimension(300,120));
			popUp.setLocationRelativeTo(null);
			popUp.setVisible(true);
			popUp.getContentPane().setLayout(null);
			
			JLabel label = new JLabel("Input the name of the new concept: ");
			label.setBounds(5,10,300,15);
			txtField = new JTextField();
			txtField.setBounds(10,30,270,20);
			JButton okBtn = new JButton("Ok");
			okBtn.setBounds(120,55,50,20);
			
			popUp.getContentPane().add(label);
			popUp.getContentPane().add(txtField);
			popUp.getContentPane().add(okBtn);
			
			okBtn.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					OntologyManager.getInstance().addNewConcept((PartOfSpeech)ontoPanel.getCmbCategory().getSelectedItem(), txtField.getText().trim());
					popUp.dispose();
					ontoPanel.initTable();
				}});
		}

}
