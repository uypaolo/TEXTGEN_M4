package view.grammardevelopment.editsemantics;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JPanel;

import components.Component;

import features.Feature;

public class CreationRightPanel extends JPanel{
	private ComponentPaletteScrollPane cpScrollPane;
	private FeaturePaletteScrollPane fpScrollPane;
	private JButton editLeafButton;
	private JButton deleteBtn;
	private Component comp;
	
	public CreationRightPanel(){
		cpScrollPane = new ComponentPaletteScrollPane();
		fpScrollPane = new FeaturePaletteScrollPane();
		createDeleteButton();
		createEditButton();
		
		add(deleteBtn);
		add(cpScrollPane);
		add(fpScrollPane);
		add(editLeafButton);
	}
	
	private void createEditButton(){
		editLeafButton = new JButton("Edit Leaf");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int palettewidth = (int)(width*0.4);
		int paletteheight = (int)(height*0.1);
		Dimension dimension = new Dimension(palettewidth,paletteheight);
		deleteBtn.setFont(new Font(this.getFont().getFontName(), Font.PLAIN, paletteheight/4));
		deleteBtn.setPreferredSize(dimension);
	}

	private void createDeleteButton(){
		deleteBtn = new JButton("Delete Selected Component");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int palettewidth = (int)(width*0.4);
		int paletteheight = (int)(height*0.1);
		Dimension dimension = new Dimension(palettewidth,paletteheight);
		deleteBtn.setFont(new Font(this.getFont().getFontName(), Font.PLAIN, paletteheight/4));
		deleteBtn.setPreferredSize(dimension);
	}
	
	public void clearInput(){
		fpScrollPane.initComponents();
		editLeafButton.setEnabled(false);
	}

	//Setters
	public void setComponent(Component comp){
		if(comp != null){
			clearInput();
			this.comp = comp;
			fpScrollPane.initCmbValues(comp);
			if(comp.isLeaf()){
				editLeafButton.setEnabled(true);
			}
			else
				editLeafButton.setEnabled(false);
		}
	}
		
	public void addDnDListenerForAllButtons(MouseAdapter mouseAdapter){
		cpScrollPane.addListenersForAllButtons(mouseAdapter);
	}
	
	/*
	public void resetFeaturesDisplayToDefault(){
		if(comp != null){
			fpScrollPane.renewCmbValues(comp);
			fpScrollPane.resetCmbFeatIndex();
		}
	}
	*/
	
	public void refreshFeaturesDisplay(){
		fpScrollPane.renewCmbValues(comp);
	}
	
	//Getters
	public Feature getCurrDisplayedFeature(){
		return fpScrollPane.getFeatureForSaving();
	}
	
	public Component getComponent(){
		return comp;
	}
	
	//Delete Listener
	public void addDeleteBtnListener(ActionListener listener){
		deleteBtn.addActionListener(listener);
	}
	
	//CompPalette Listener
	public void addCompPaletteDragListener(MouseAdapter mouseAdapter){
		cpScrollPane.addListenersForAllButtons(mouseAdapter);
	}
	
	//Leaf Palette Listener
	public void addLeafEditBtnListener(ActionListener listener){
		editLeafButton.addActionListener(listener);
	}
	
	//Feature listeners
	public void addSaveFeatureListener(ItemListener listener){
		fpScrollPane.addCmbValuesListener(listener);
	}
	
	
	public void addResetFeatureBtnListener(ActionListener listener){
		fpScrollPane.addResetListener(listener);
	}
	
	public void addSelectFeatureComboBoxListener(ItemListener listener){
		fpScrollPane.addCmbFeaturesListener(listener);
	}

}
