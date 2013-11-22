package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.TransferHandler;

import view.grammardevelopment.editsemantics.CreationRightPanel;
import view.grammardevelopment.editsemantics.LeafEditFrame;

import components.Component;

import controller.listener.grammardev.editsemantics.DeleteComponentBtnListener;
import controller.listener.grammardev.editsemantics.FeaturePaletteCmbValuesItemListener;
import controller.listener.grammardev.editsemantics.FeaturePaletteResetBtnListener;

public class CreateController {

	private CreationRightPanel creationPanel;
	private GrammarDevController grammarDevController;
	
	public CreateController(GrammarDevController grammarDevController, CreationRightPanel creationPanel){
		this.creationPanel = creationPanel;
		this.grammarDevController = grammarDevController;
		
		addDeleteButtonListener();
		addListenersToCompPalette();
		addListenersToFeatPalette();
		addListenersToLeafEditPalette();
	}
	
	private void addDeleteButtonListener(){
		creationPanel.addDeleteBtnListener(new DeleteComponentBtnListener(grammarDevController));
	}

	private void addListenersToLeafEditPalette() {
		creationPanel.addLeafEditBtnListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Component comp = (Component)grammarDevController.getCurrSelectedComponentPanel().getComponent();
				new LeafEditFrame(comp,grammarDevController);
			}
			
		});
	}

	private void addListenersToFeatPalette() {
		creationPanel.addSaveFeatureListener(new FeaturePaletteCmbValuesItemListener(grammarDevController, creationPanel));
		creationPanel.addResetFeatureBtnListener(new FeaturePaletteResetBtnListener(creationPanel));
		creationPanel.addSelectFeatureComboBoxListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				creationPanel.refreshFeaturesDisplay();
			}
		});				
	}

	private void addListenersToCompPalette() {
		creationPanel.addCompPaletteDragListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                JButton button = (JButton)e.getSource();
                TransferHandler handle = button.getTransferHandler();
                handle.exportAsDrag(button, e, TransferHandler.COPY);
            }
        });
	}
	
}