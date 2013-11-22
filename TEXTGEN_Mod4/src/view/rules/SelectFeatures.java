package view.rules;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import managers.FeatureManager;
import ontology.PartOfSpeech;

import components.Component;
import components.Phrase;

import features.DBFeatureValues;
import features.Feature;

public class SelectFeatures extends JDialog{

	private JButton btnOK;
	private JButton btnCancel;
	private JLabel lblFeature;
	private JTable featureTable ;
	private JTable phraseTable;
	private JTable clauseTable;
	
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	
	DefaultTableModel featureModel;
	DefaultTableModel phraseModel;
	DefaultTableModel clauseModel;
	
	
  public SelectFeatures(String pos, String description, String phrase) //given somerule
  {
    setTitle("Features");
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setVisible(true);
    getContentPane().setLayout(null);
    

    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    tabbedPane.setBounds(51, 90, 975, 431);
    getContentPane().add(tabbedPane);
    //basic feature
    featureModel = getCustomizeModel(featureTable, pos);
    featureTable = CustomizeTable(featureModel);
    featureTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JScrollPane scrollPane = new JScrollPane();
    tabbedPane.addTab("Word", null, scrollPane, null);
    scrollPane.setViewportView(featureTable);
    
    //phrase feature
    if(phrase != null)
    {
    	phraseModel = getCustomizeModel(phraseTable, phrase);
    	scrollPane_1 = new JScrollPane();
        tabbedPane.addTab("Phrase", null, scrollPane_1, null);
        phraseTable = CustomizeTable(phraseModel);
        phraseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane_1.setViewportView(phraseTable);
    }
    
    //clause feature
    clauseModel = getCustomizeModel(clauseTable, PartOfSpeech.CLAUSE);
    scrollPane_2 = new JScrollPane();
    clauseTable = CustomizeTable(clauseModel);
    clauseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    tabbedPane.addTab("Clause", null, scrollPane_2, null);
    scrollPane_2.setViewportView(clauseTable);
    
    
    btnOK = new JButton("OK");
    btnOK.setBounds(796, 541, 89, 23);
    getContentPane().add(btnOK);
    
    btnCancel = new JButton("Cancel");
    btnCancel.setBounds(937, 541, 89, 23);
    getContentPane().add(btnCancel);
    
    lblFeature = new JLabel(description);
    lblFeature.setBounds(74, 53, 147, 15);
    getContentPane().add(lblFeature);
    
    //mainTable.setFillsViewportHeight(true);
    
    setBounds(100, 50, 1100, 650);
  }  
  
  public JTable CustomizeTable( DefaultTableModel model){
	  JTable table = new JTable(model)
	  {
	        private static final long serialVersionUID = 1L;
	        public boolean isCellEditable(int row, int column){
	        	if(column%2 == 0)
	        		return false;
	        	else
	        		return true;
	        }
	        @Override
	        public Class getColumnClass(int column) {
	            switch (column%2) {
	                case 0:
	                    return String.class;
	                default:
	                    return Boolean.class;
	            }
	        }
	        
	        
	    };

	    for(int i = 0; i < model.getColumnCount(); i++)
	    {
	    	TableColumn column = table.getColumnModel().getColumn(i);
	    	switch(i%2)
	    	{
	    		case 0:	column.setPreferredWidth(150);break;
	    		case 1: column.setPreferredWidth(25);break;
	    	} 
	    }
	    return table;
  }
  
  public DefaultTableModel getCustomizeModel(JTable table, String pos)
  {
	  DefaultTableModel model = new DefaultTableModel();
	    
	    ArrayList<Feature> featureList = FeatureManager.getDefaultFeatures(pos);
		for(Feature feature : featureList)
		{
			DBFeatureValues valueList = FeatureManager.getFeatureValues(pos, feature.getName());
			Object[] values = valueList.getValues().toArray();
			model.addColumn(feature.getName(), values);


			ArrayList<Boolean> bool = new ArrayList<Boolean>();
			for(Object value: values)
				bool.add(Boolean.FALSE);  
			model.addColumn("", bool.toArray());
		}
	    
	   return model;
  }
  
  public JTable getFeatureTable(){
	  return featureTable;
  }
  
  public JTable getPhraseTable(){
	  return phraseTable;
  }
  
  public JTable getClauseTable()
  {
	  return clauseTable;
  }
  
  public void addOKBtnListener(ActionListener act)
  {
	  btnOK.addActionListener(act);
  }
  
  public void addCancelBtnListener(ActionListener act)
  {
	  btnCancel.addActionListener(act);
  }
  
  public void addTabPaneListener(ChangeListener change){
	  tabbedPane.addChangeListener(change);
  }
}

