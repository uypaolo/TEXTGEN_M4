package view.ontology;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import managers.OntologyManager;
import ontology.ConceptList;
import ontology.PartOfSpeech;
import controller.listener.ontology.OntoBtnNewConceptListener;
import controller.listener.ontology.OntologyTableListener;

public class OntologyPanel extends JPanel{

	private OntologyTable ontoTable;
	private JComboBox cmbCategory;
	private JButton btnNewConcept;
	
	private JScrollPane scrollPane;
	
	private ConceptList conceptList;
	private OntologyPanel ontoPanel = this;
	
	public OntologyPanel(){
		ontoTable = new OntologyTable();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		JLabel title = new JLabel("Ontology");
		title.setFont(new Font(title.getFont().getFontName(),Font.BOLD,16));
		JLabel category = new JLabel("Semantic Category");
		cmbCategory = new JComboBox(OntologyManager.getInstance().getAllPOSForDisplay().toArray());
		cmbCategory.setSelectedIndex(0);
		//set display properties
		btnNewConcept = new JButton("Add New Concept");
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension((int)screenSize.getWidth(),(int)screenSize.getHeight()-20));
		scrollPane.setSize(new Dimension((int)screenSize.getWidth(),(int)screenSize.getHeight()-20));
		scrollPane.setViewportView(ontoTable);
		
		add(title);
		add(category);
		add(cmbCategory);
		add(btnNewConcept);
		add(scrollPane);
		
		initTable();
		addListeners();
	}
	
	public void setToolTipForAll(){
		
	}
	
	public void initTable(){
		scrollPane.getVerticalScrollBar().setValue(0);
		
		if(cmbCategory.getSelectedItem() != null)
			ontoTable.setList(OntologyManager.getInstance().getDisplayConceptList((PartOfSpeech)cmbCategory.getSelectedItem()));
		
	}
	
	public JComboBox getCmbCategory(){
		return cmbCategory;
	}
	
	public JScrollPane getScrollPane(){
		return scrollPane;
	}
	
	public void addListeners(){
		
		cmbCategory.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				ontoTable.setList(OntologyManager.getInstance().getDisplayConceptList((PartOfSpeech)cmbCategory.getSelectedItem()));
				if(cmbCategory.getSelectedItem() != null){
					String selectedPOSName = ((PartOfSpeech)cmbCategory.getSelectedItem()).getName();
					ontoTable.setListener(new OntologyTableListener(ontoTable,selectedPOSName));
				}
			}
		});
		

		String selectedPOSName = ((PartOfSpeech)cmbCategory.getSelectedItem()).getName();
		ontoTable.setListener(new OntologyTableListener(ontoTable,selectedPOSName));
		btnNewConcept.addActionListener(new OntoBtnNewConceptListener(ontoPanel));
	}
	
}
