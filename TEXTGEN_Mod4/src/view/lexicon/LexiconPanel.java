package view.lexicon;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import lexicon.Form;
import lexicon.Lexicon;
import lexicon.LexiconList;
import managers.LexiconManager;
import ontology.PartOfSpeech;
import features.DBFeatureValues;
import features.Feature;

public class LexiconPanel extends JPanel{
	
	public final static int TABLE_STEM = 1;
	public final static int TABLE_FEATURE = 2;
	public final static int TABLE_FORM = 3;
	
	private LexiconTable lexTable;
	private JComboBox cmbCategory;
	private JButton btnAddPOS;
	
	private JPanel viewPanel;
	private JButton btnViewStems, btnViewFeatures,btnViewForms;
	
	private JPanel featPanel; //mini panels above the jtable
	private JButton btnAddEditFeatures;
	
	private JPanel stemPanel;
	private JButton btnNewEntry;
	private JButton btnMapToNewConcept;
	
	private JPanel formPanel;
	private JButton btnAddEditForms;
	
	private JScrollPane scrollPane; 
	private String language;
	private LexiconList lexList;
		
	private int currMode;

	public LexiconPanel(){	
		
		JLabel title = new JLabel("Lexicon");
		lexTable = new LexiconTable();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		title.setFont(new Font(title.getFont().getFontName(),Font.BOLD,16));
		
		JLabel category = new JLabel("Syntactic Category");
		cmbCategory = new JComboBox(LexiconManager.getInstance().getAllPOS().toArray());
		btnAddPOS = new JButton("Add New POS");
		
		btnViewStems = new JButton("Stems");
		btnViewFeatures =  new JButton("Features");
		btnViewForms = new JButton("Forms");
		viewPanel = new JPanel();
		viewPanel.setBorder(BorderFactory.createTitledBorder("View"));
		viewPanel.add(btnViewStems);
		viewPanel.add(btnViewFeatures);
		viewPanel.add(btnViewForms);
		
		
		btnNewEntry = new JButton("Add New Target Stem");
		btnMapToNewConcept = new JButton("Map A Stem To A Concept");
		
		stemPanel = new JPanel();
		stemPanel.setBorder(BorderFactory.createTitledBorder("Stem"));
		stemPanel.add(btnNewEntry);
		stemPanel.add(btnMapToNewConcept);
		
		featPanel = new JPanel();
		featPanel.setBorder(BorderFactory.createTitledBorder("Feature"));
		btnAddEditFeatures = new JButton("Add/Edit Feature/s");
		featPanel.add(btnAddEditFeatures);
		
		formPanel = new JPanel();
		formPanel.setBorder(BorderFactory.createTitledBorder("Form"));
		btnAddEditForms = new JButton("Add/Edit Form/s");
		formPanel.add(btnAddEditForms);
		
		scrollPane = new JScrollPane();
		scrollPane.setMinimumSize(new Dimension((int)screenSize.getWidth(),(int)screenSize.getHeight()-20));
		scrollPane.setMaximumSize(new Dimension((int)screenSize.getWidth(),(int)screenSize.getHeight()-20));
		scrollPane.setPreferredSize(new Dimension((int)screenSize.getWidth(),(int)screenSize.getHeight()-20));
		scrollPane.setSize(new Dimension((int)screenSize.getWidth(),(int)screenSize.getHeight()-20));
		scrollPane.setViewportView(lexTable);
		
		add(title);
		add(category);
		add(cmbCategory);
		add(btnAddPOS);
		add(viewPanel);
		add(stemPanel);
		add(featPanel);
		add(formPanel);
		add(scrollPane);
		
		lexList = LexiconManager.getInstance().getLexiconList(getCodeFromSelectedPOS());
		
		//set display properties
		this.currMode = TABLE_STEM;
		initTable();
	}
	
	public void initTable(){
		setTable(TABLE_STEM);
	}
	
	public void setLanguage(String language){
		this.language = language;
	}

	public String getCodeFromSelectedPOS(){
		if(cmbCategory.getSelectedItem() == null)
			return "";
		
		return ((PartOfSpeech)cmbCategory.getSelectedItem()).getName();
	}
	
	public void clearTable(){
		DefaultTableModel model = (DefaultTableModel)lexTable.getModel();
		if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
	}
	
	public boolean isLexiconExist(String newLexName, int row){
		
		for(int i = 0 ; i < lexList.getLexiconList().size();i++){
			if(lexList.getLexiconList().get(i).getName().equals(newLexName) && row!= i)
				return true;
		}
		return false;
	}
	
	public void setTable(int currMode){
		this.currMode = currMode;
		clearTable();
		DefaultTableModel model = (DefaultTableModel)lexTable.getModel();
		lexList = LexiconManager.getInstance().getLexiconList(getCodeFromSelectedPOS());
		switch(currMode){
			case TABLE_STEM: 
							model.setColumnIdentifiers(new String[] {"Stems","Glosses","Comments","Sample Sentences","Mapped Concept"}); // <-- column headings
							if(lexList != null){
								for(Lexicon lex : lexList.getLexiconList()){
									String[] data = {lex.getName(),lex.getGloss(),lex.getComments()
														,lex.getSampleSentence(),lex.getMappedConcept()};
									model.addRow(data);
								}
							}
								break;
			case TABLE_FEATURE: 
							ArrayList<String> columnIdentifiers = new ArrayList<String>();
							columnIdentifiers.add("Stem");
							columnIdentifiers.add("Glosses");
							
							for(DBFeatureValues dbfv : lexList.getPossibleFeatures())
								columnIdentifiers.add(dbfv.getFeatureName());
							
							model.setColumnIdentifiers(columnIdentifiers.toArray()); // <-- column headings (get from manager)
							 
							for (int col = 2 ; col< model.getColumnCount();col++){
								 TableColumn tblColumn = lexTable.getColumnModel().getColumn(col);
								 ArrayList<String> featVals = lexList.getFeatureValues(columnIdentifiers.get(col));
								 
								 JComboBox cmb = new JComboBox(featVals.toArray());
								 tblColumn.setCellEditor(new DefaultCellEditor(cmb));
							 }
							 if(lexList != null){
								 int row = 0;
								 for(Lexicon lex : lexList.getLexiconList()){
									 ArrayList<Object> data = new ArrayList<Object>();
									 data.add(lex.getName());
									 data.add(lex.getGloss());
									 int col = 2; 
									 model.addRow(data.toArray());
									 for(Feature feat : lex.getFeatureList().getFeatureList()){
										 model.setValueAt(feat.getValue(), row, col);
										 col++;
									 }
									 row++;
								 }
							 }
								break;
			case TABLE_FORM:
							ArrayList<String> columns= new ArrayList<String>();
							columns.add("Stem");
							columns.add("Glosses");
							for(String form : lexList.getPossibleForms()){
								columns.add(form);
							}
							 model.setColumnIdentifiers(columns.toArray()); 
							 if(lexList != null){
								 for(Lexicon lex : lexList.getLexiconList()){
									 ArrayList<String> data = new ArrayList<String>();
									 data.add(lex.getName());
									 data.add(lex.getGloss());
									 for(Form form : lex.getFormList().getFormList()){
										 data.add(form.getValue());
									 }
									 model.addRow(data.toArray());
								 }
							 }
									break;
		}
	}
	
	public int getCurrMode(){
		return currMode;
	}
	
	public PartOfSpeech getSelectedPOS(){
		if(cmbCategory.getSelectedItem() != null)
			return (PartOfSpeech) cmbCategory.getSelectedItem();
		return null;
	}
	
	public JButton getBtnAddPOS() {
		return btnAddPOS;
	}

	public JButton getBtnViewStems() {
		return btnViewStems;
	}

	public JButton getBtnViewFeatures() {
		return btnViewFeatures;
	}

	public JButton getBtnViewForms() {
		return btnViewForms;
	}

	public JButton getBtnAddEditFeatures() {
		return btnAddEditFeatures;
	}

	public JButton getBtnNewEntry() {
		return btnNewEntry;
	}

	public JButton getBtnMapToNewConcept() {
		return btnMapToNewConcept;
	}

	public JButton getBtnAddEditForms() {
		return btnAddEditForms;
	}

	public void setLexiconList(LexiconList lexList){
		this.lexList = lexList;
	}
	
	public LexiconTable getTable(){
		return lexTable;
	}
	
	public JComboBox getCmbCategory(){
		return cmbCategory;
	}
	
}
