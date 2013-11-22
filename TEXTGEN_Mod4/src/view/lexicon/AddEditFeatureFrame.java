package view.lexicon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import lexicon.LexiconList;
import features.DBFeatureValues;

public class AddEditFeatureFrame extends JFrame{
	
	LexiconList lexList;
	
	JTable featNames;
	DefaultTableModel namesModel;
	JButton addFeatName ;
	
	
	JLabel featValue;
	JTable featValues = new JTable();
	DefaultTableModel valuesModel ;
	JButton addFeatValue;
	JFrame frame = this;
	
	ArrayList<DBFeatureValues> arrDBFeatureValues;
	DBFeatureValues currSelected;
	public AddEditFeatureFrame(LexiconList lexList){
		
		super("Feature Table");
		this.lexList = lexList;
		
		setSize(new Dimension(700,500));
		setResizable(false);
		setLocationRelativeTo(null);
		JSplitPane splitPane = new JSplitPane();
		
		JPanel leftPanel = new JPanel();
		JLabel featName = new JLabel("Feature Names");
		featNames = new JTable();
		namesModel = (DefaultTableModel)featNames.getModel();
		namesModel.setColumnIdentifiers(new String[] {"Feature Names"});
		
		arrDBFeatureValues = lexList.getPossibleFeatures();
		for(DBFeatureValues feat : arrDBFeatureValues){
			namesModel.addRow(new String[]{feat.getFeatureName()});
		}
		
		JScrollPane scrollPaneFeatNames = new JScrollPane(featNames);
		scrollPaneFeatNames.setPreferredSize(new Dimension(320,350));
		addFeatName  = new JButton("Add New Feature Name");
		leftPanel.add(featName);
		leftPanel.add(scrollPaneFeatNames);
		leftPanel.add(addFeatName);
		
		JPanel rightPanel = new JPanel();
		featValue = new JLabel("Feature Values for :");
		featValues = new JTable();
		valuesModel = (DefaultTableModel)featValues.getModel();
		valuesModel.setColumnIdentifiers(new String[] {"Feature Values"});
		
		
		JScrollPane scrollPaneFeatValues = new JScrollPane(featValues);
		scrollPaneFeatValues.setPreferredSize(new Dimension(320,350));
		addFeatValue = new JButton("Add New Feature Value");
		rightPanel.add(featValue);
		rightPanel.add(scrollPaneFeatValues);
		rightPanel.add(addFeatValue);
		
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(rightPanel);
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerSize(2);
		add(splitPane);
		this.setVisible(true);
		
		addListeners();
	}
	
	public void addListeners(){
		
		class featNamesKeyListener implements KeyListener{
			
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE){
					
					if(featNames.getSelectedRowCount()>0){
						int n = JOptionPane.showConfirmDialog(
							    null,
							    "Are you sure you want to delete all selected feature names?",
							    "An Inane Question",
							    JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.YES_OPTION){
							for (int i = featNames.getSelectedRows().length - 1; i >= 0; i--) {
								lexList.deletePossibleFeature((String)namesModel.getValueAt(featNames.getSelectedRows()[i], 0));
								namesModel.removeRow(featNames.getSelectedRows()[i]);
							}
							currSelected = null;
						}
					}
						
				}
			}
			
			public void keyReleased(KeyEvent e) {
			}
			public void keyTyped(KeyEvent e) {
			}};
		
		featNames.addKeyListener(new featNamesKeyListener());
		
		
		    class featValuesKeyListener implements KeyListener{ // NOT YET IMPLEMENTED DUE TO PROBLEMS

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE){
					KeyListener[] keyList = featNames.getKeyListeners();
					for(KeyListener kl : featNames.getKeyListeners())
						featNames.removeKeyListener(kl);
					if(featValues.getSelectedRowCount()>0){
						int n = JOptionPane.showConfirmDialog(
							    null,
							    "Are you sure you want to delete all selected feature values?",
							    "An Inane Question",
							    JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.YES_OPTION){
							for (int i = featValues.getSelectedRows().length - 1; i >= 0; i--) {
								lexList.deleteFeatureValue(currSelected.getFeatureName(),(String)valuesModel.getValueAt(featValues.getSelectedRows()[i], 0));
								valuesModel.removeRow(featValues.getSelectedRows()[i]);
							}
							currSelected = null;
						}
					}
					if(keyList.length>0){
						for(KeyListener kl : keyList)
							featNames.addKeyListener(kl);
					}
				}
			}
			
			public void keyReleased(KeyEvent e) {
			}
			public void keyTyped(KeyEvent e) {
			}};
			
		//featValues.addKeyListener(new featValuesKeyListener());
		
		
		namesModel.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent arg0) {
				int row = arg0.getFirstRow();
				int col = arg0.getColumn();
				if(row>=0 && col>=0){
					
						if(checkDuplicateFeatName((String)namesModel.getValueAt(row, col),row)){
							JOptionPane.showMessageDialog(null,
								    "Duplicate Feature Names not allowed!",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
							namesModel.setValueAt(arrDBFeatureValues.get(row).getFeatureName(), row, col);
							
						}
						else{
							featValue.setText("Feature Values For "+(String)namesModel.getValueAt(row, col)+" :");
							lexList.renameFeatureName(arrDBFeatureValues.get(row).getFeatureName(), (String)namesModel.getValueAt(row, col));
							arrDBFeatureValues = lexList.getPossibleFeatures();
						}
				}
			}});
		
		featNames.getSelectionModel().addListSelectionListener(new ListSelectionListener(){	
			public void valueChanged(ListSelectionEvent arg0) {
				int row = featNames.getSelectedRow();
				if(featValue!=null && row != -1){
					clearTable();
					DBFeatureValues dbFeatVal = arrDBFeatureValues.get(row);
					currSelected = arrDBFeatureValues.get(row);
					featValue.setText("Feature Values For "+dbFeatVal.getFeatureName()+" :");
					for(String value : dbFeatVal.getValues()){
						valuesModel.addRow(new String[]{value});
					}
				}
				else
					featValue.setText("Feature Values For :");
			}});
		
		addFeatName.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(checkDuplicateFeatName("",-1))
					JOptionPane.showMessageDialog(null,
						    "Please fill up the blank feature name before adding a new one",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				else{
					namesModel.addRow(new String[] {""});
					lexList.addNewPossibleFeature(new DBFeatureValues("",true));
					arrDBFeatureValues = lexList.getPossibleFeatures();
				}
				
			}});
		
		valuesModel.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent arg0) {
				int row = arg0.getFirstRow();
				int col = arg0.getColumn();
				if(row>=0 && col>=0){
					if(currSelected!=null){
						if(checkDuplicateFeatValue((String)valuesModel.getValueAt(row, col),row)){
							JOptionPane.showMessageDialog(null,
								    "Duplicate Feature Values not allowed!",
								    "Error",
								    JOptionPane.ERROR_MESSAGE);
							valuesModel.setValueAt(currSelected.getValues().get(row), row, col);
							
						}
						else{
							lexList.renameFeatureValue(currSelected.getFeatureName(), currSelected.getValues().get(row),(String)valuesModel.getValueAt(row, col));
							arrDBFeatureValues = lexList.getPossibleFeatures();
							
						}
					}
					else{
						JOptionPane.showMessageDialog(null,
							    "An error occured, try again",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}});
		
		addFeatValue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(currSelected!=null){
					if(checkDuplicateFeatValue("",-1))
						JOptionPane.showMessageDialog(null,
							    "Please fill up the blank feature value before adding a new one",
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
					else{
						valuesModel.addRow(new String[] {""});
						lexList.addNewPossibleFeatureValue(currSelected.getFeatureName(), "");
						arrDBFeatureValues = lexList.getPossibleFeatures();
					}
				}
				else
					JOptionPane.showMessageDialog(null,
						    "An error occured, try again",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				
			}});
	}
	
	private void clearTable(){
		DefaultTableModel model = (DefaultTableModel)featValues.getModel();
		if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
	}
	
	private boolean checkDuplicateFeatName(String name,int index){
		if(index == -1){
			for(DBFeatureValues dbFeatVal : arrDBFeatureValues){
				if(dbFeatVal.getFeatureName().equals(name))
					return true;
			}
			return false;
		}
		else{
			for(int i = 0; i< arrDBFeatureValues.size();i++){
				if(arrDBFeatureValues.get(i).getFeatureName().equals(name) && index!=i )
					return true;
			}
			return false;
		}
		
		
		
	}
	
	private boolean checkDuplicateFeatValue(String newValue, int index){
		if(index == -1){
			for(String value: currSelected.getValues()){
				if(value.equals(newValue))
					return true;
			}
			return false;
		}
		else{
			for(int i = 0; i< currSelected.getValues().size();i++){
				if(currSelected.getValues().get(i).equals(newValue) && index!=i )
					return true;
			}
			return false;
		}
	}

}
