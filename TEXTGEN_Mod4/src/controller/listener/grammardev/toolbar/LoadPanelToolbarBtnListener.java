package controller.listener.grammardev.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.grammardevelopment.ViewSemanticsPanel;

public class LoadPanelToolbarBtnListener implements ActionListener{
	
	private ViewSemanticsPanel loadPanel;
	private int desiredMode;
	
	public LoadPanelToolbarBtnListener(ViewSemanticsPanel loadPanel, int desiredMode){
		this.loadPanel = loadPanel;
		this.desiredMode = desiredMode;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		loadPanel.setMode(desiredMode);
		
	}
}