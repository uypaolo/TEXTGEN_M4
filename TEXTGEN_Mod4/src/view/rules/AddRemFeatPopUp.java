package view.rules;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rules.Structural;

import managers.FeatureManager;

import components.Component;

import controller.listener.rules.AddRuleController;
import features.Feature;

public class AddRemFeatPopUp extends JFrame{
	
	JComboBox cmb;
	ArrayList<Feature> featList;
	AddRuleController controller;
	
	public AddRemFeatPopUp(AddRuleController controllerx ){
		this.controller = controllerx;
		setTitle("Add Remaining Features");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setSize(300,200);
		setLocationRelativeTo(null);
		
		JButton button = new JButton("Add This");
		
		button.setMinimumSize(new Dimension(200,20));
		button.setMaximumSize(new Dimension(200,20));
		button.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		Component comp = controller.getCurrSelectedPanel().getComponent();
		 featList = FeatureManager.getDefaultFeatures(comp.getName());
		ArrayList<String> cmbContent = new ArrayList<String>();
		for(int i = featList.size()-1 ; i>=0 ; i--){
			Feature feat = featList.get(i);
			if(featIsInComp(feat.getName())){
				featList.remove(feat);
			}
		}
		
		for(Feature feat : featList){
			cmbContent.add(feat.getName());
		}
		
		cmb = new JComboBox(cmbContent.toArray());
		cmb.setMinimumSize(new Dimension(200,30));
		cmb.setMaximumSize(new Dimension(200,30));
		cmb.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		JLabel blankLabel = new JLabel("");
		blankLabel.setMinimumSize(new Dimension(200,20));
		blankLabel.setMaximumSize(new Dimension(200,20));
		blankLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		JLabel blankLabel2 = new JLabel("");
		blankLabel2.setMinimumSize(new Dimension(200,30));
		blankLabel2.setMaximumSize(new Dimension(200,30));
		blankLabel2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		getContentPane().add(blankLabel2);
		getContentPane().add(cmb);
		getContentPane().add(blankLabel);
		getContentPane().add(button);
		setAlwaysOnTop(true);
		setVisible(true);
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				
				if(cmb.getSelectedIndex()!=-1){
					Feature feat = featList.get(cmb.getSelectedIndex());
					Component comp = controller.getCurrSelectedPanel().getComponent();
					comp.setFeature(feat);
					controller.initFeatCmb();
					Structural structRule = controller.getStructRule();
					structRule.editFeature(comp, feat.getName(), feat.getValue());
					dispose();
					
				}
				
			
			}});
	}
	
	public boolean featIsInComp(String feat){
		Component  comp = controller.getCurrSelectedPanel().getComponent();
		for(Feature featInComp : comp.getFeatures()){
			if(featInComp.getName().equals(feat))
				return true;
		}
		return false;
	}

}
