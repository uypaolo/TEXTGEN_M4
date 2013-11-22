package controller.listener.grammardev.editsemantics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import managers.FeatureManager;
import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.editsemantics.CreationRightPanel;
import view.rules.FeaturePaletteScrollPanelForRules;

import components.Component;

import controller.listener.rules.AddRuleController;
import features.Feature;

public class FeaturePaletteResetBtnListener implements ActionListener{

	private CreationRightPanel creationPanel;
	
	int mode;
	final int EDIT_SEM = 1;
	final int ADD_RULE = 2;
	
	private AddRuleController controller;
	private FeaturePaletteScrollPanelForRules pane;
	
	public FeaturePaletteResetBtnListener(CreationRightPanel creationPanel){
		mode = EDIT_SEM;
		this.creationPanel = creationPanel;
	}
	
	public FeaturePaletteResetBtnListener(AddRuleController controller,FeaturePaletteScrollPanelForRules pane){
		mode = ADD_RULE;
		this.controller = controller;
		this.pane = pane;
	}
	
	public void actionPerformed(ActionEvent e) {
		Object[] options = {"Yes","No"};
		int n = JOptionPane.showOptionDialog(new JFrame(),
					    "This would RESET the values of ALL FEATURES to default\nContinue?",
					    "WARNING",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.WARNING_MESSAGE,
					    null,
					    options,
					    options[1]);
		if(n == 0){
			Component comp = null;
			if(mode == EDIT_SEM)
				comp = creationPanel.getComponent();
			if(mode == ADD_RULE){
				ComponentPanel compPanel = controller.getCurrSelectedPanel();
				comp = compPanel.getComponent();
				pane.initCmbValues(comp);
			}
				
			ArrayList<Feature> featList = FeatureManager.getDefaultFeatures(comp.getName());
			for(Feature feature : featList)
				comp.setFeature(feature);
			
			if(comp!= null && mode == EDIT_SEM)
				creationPanel.setComponent(comp);
		}
	}
}
