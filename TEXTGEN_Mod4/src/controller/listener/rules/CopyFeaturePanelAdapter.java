package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import managers.FeatureManager;

import rules.Structural;

import features.Feature;

import view.grammardevelopment.ComponentPanel;

public class CopyFeaturePanelAdapter extends MouseAdapter{

	AddRuleController controller;
	ComponentPanel panelToCopyFrom;
	JButton copyBtn;
	ComponentPanel panelToCopyTo;
	JFrame popUp;
	JTable table;
	public CopyFeaturePanelAdapter(AddRuleController controller, ComponentPanel panelToCopyFrom){
		this.controller = controller;
		this.panelToCopyFrom = panelToCopyFrom;
	}
	
	public void mouseClicked(MouseEvent arg0) {
		panelToCopyTo = (ComponentPanel)arg0.getSource();
		if(panelToCopyTo == panelToCopyFrom){
			JOptionPane.showMessageDialog(null,
				    "Useless Action To Copy Feature of the Same Panels",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
		}
		else{
			popUp = new JFrame("Features of "+panelToCopyFrom.getComponent().getName());
			popUp.setLayout(null);
			table = new JTable();
			String[] columnHeaders = {"Feature Name"};
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setColumnIdentifiers(columnHeaders);
			ArrayList<Feature> features = FeatureManager.getDefaultFeatures(panelToCopyFrom.getComponent()
					.getName());
					
			for(Feature f : features){
						model.addRow(new Object[] {f.getName()});
			}
			JButton button = new JButton("Copy This Feature to "+panelToCopyTo.getComponent().getName());
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(0, 0, 600, 300);
			button.setBounds(250,300,250,35);
			popUp.add(scrollPane);
			popUp.add(button);
			popUp.setSize(600,400);
			popUp.setLocationRelativeTo(null);
			popUp.setVisible(true);
			
			button.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent arg0) {
					//log here
					String s = (String)JOptionPane.showInputDialog(
		                    null,
		                    "Input Feature Name: " ,
		                    "Copy Feature",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    table.getValueAt(table.getSelectedRow(),0));

					//If a string was returned, say so.
						if ((s != null) && (s.length() > 0)) {
							Structural rule = controller.getStructRule();
							rule.copyFeature(panelToCopyFrom.getComponent(), panelToCopyTo.getComponent(), 
									(String)table.getValueAt(table.getSelectedRow(), 0), s);
							popUp.dispose();
						    return;
						}
					
					controller.removeListeners();
				}
			});
		}
		controller.removeListeners();
	}
	
	public JFrame getPopUpFrame(){
		return popUp;
	}
}
