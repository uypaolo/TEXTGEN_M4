package view.lexicon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import lexicon.LexiconList;

public class AddEditFormFrame extends JFrame{
	
	JTable formTable;
	DefaultTableModel model ;
	LexiconList lexList;
	
	public AddEditFormFrame(LexiconList list ) {
		
		this.lexList = list;

		setTitle("Forms");
		setSize(200,400);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setVisible(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(1, 1, 200, 300);
		formTable = new JTable();
		formTable.setPreferredSize(new Dimension(200,320));
		model = (DefaultTableModel)formTable.getModel();
		model.setColumnIdentifiers(new String[] {"Forms"});
		for(String form : lexList.getPossibleForms())
			model.addRow(new String[] {form});
		scrollPane.setViewportView(formTable);
		add(scrollPane);
		
		JButton addForm = new JButton("Add Form");
		addForm.setBounds(50,320,80,30);
		add(addForm);
		
		addForm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				model.addRow(new String[] {""});
			}
		});
		
		formTable.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE){
					if(formTable.getSelectedRowCount()>0){
						int n = JOptionPane.showConfirmDialog(
							    null,
							    "Are you sure you want to delete all selected forms?",
							    "An Inane Question",
							    JOptionPane.YES_NO_OPTION);
						if(n == JOptionPane.YES_OPTION){
							for (int i = formTable.getSelectedRows().length - 1; i >= 0; i--) {
								lexList.deletePossibleForm((String)model.getValueAt(formTable.getSelectedRows()[i], 0));
								model.removeRow(formTable.getSelectedRows()[i]);

							}
						}
					}
				}
			}
			
			public void keyReleased(KeyEvent e) {
			}
			public void keyTyped(KeyEvent e) {
			}});
		
		model.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent arg0) {
				int row = arg0.getFirstRow();
				int column = arg0.getColumn();
				if(column>=0 && row>=0){
					if(row >= lexList.getPossibleForms().size()){
						lexList.addNewPossibleForm((String)model.getValueAt(row, column));
					}
					
				    else{
						lexList.renamePossibleForm(lexList.getPossibleForms().get(row), (String)model.getValueAt(row, column));
					}
				}	
				
			}});
		
	}

}
