package view.rules;

import java.awt.Dimension;

import javax.swing.JButton;

import managers.FeatureManager;
import view.grammardevelopment.editsemantics.FeaturePaletteScrollPane;

import components.Component;

import features.DBFeatureValues;
import features.Feature;

public class FeaturePaletteScrollPanelForRules extends FeaturePaletteScrollPane{

	JButton btnAddFeat;
	JButton btnRemoveFeat;
	
	public FeaturePaletteScrollPanelForRules(){
		btnAddFeat = new JButton("Add Features");
		btnAddFeat.setMinimumSize(new Dimension(180,20));
		btnAddFeat.setMaximumSize(new Dimension(180,20));
		btnAddFeat.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		btnRemoveFeat = new JButton("Remove This Feature");
		btnRemoveFeat.setMinimumSize(new Dimension(180,20));
		btnRemoveFeat.setMaximumSize(new Dimension(180,20));
		btnRemoveFeat.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		this.addComponent(btnAddFeat);
		this.addComponent(btnRemoveFeat);
		resetButton.setVisible(true);
		panel.remove(resetButton);
	}
	
	public void renewCmbValues(Component comp){ //call when an item state changes in Cmb
		DBFeatureValues values = null;
		if(comp!=null && cmbFeatures.getSelectedItem() != null){
			//try{
				values = FeatureManager.getFeatureValues(comp.getName(), cmbFeatures.getSelectedItem().toString());
			//}catch(Exception e){}
			
			unlockComponents();
			
			//remove listeners first so that changing the values in combo box will not trigger saves
			removeCmbValuesListeners();
			
			cmbValues.removeAllItems();
			if(values != null){
				for(String value : values.getValues()){
					cmbValues.addItem(value);
				}
				//cmbValues.addItem("");
				if(cmbFeatures.getSelectedItem() != null)
					cmbValues.setSelectedItem(comp.getFeature(cmbFeatures.getSelectedItem().toString()).getValue());
			}
			
			cmbValues.addItemListener(saveFeatureListener);
		}
		
	}
	public Feature getFeature(){
		if(cmbFeatures.getSelectedIndex() == -1 || cmbValues.getSelectedIndex() == -1)
			return null;
		
		Feature feat = new Feature((String)cmbFeatures.getSelectedItem(),(String)cmbValues.getSelectedItem(),true);
		return feat;
	}
	public JButton getButtonFeat(){
		return btnAddFeat;
	}
	
	public JButton getButtonRemoveFeat(){
		return btnRemoveFeat;
	}
	
}
