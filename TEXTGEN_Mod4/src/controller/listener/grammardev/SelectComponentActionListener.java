package controller.listener.grammardev;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.ViewSemanticsPanel;
import view.rules.FeaturePaletteScrollPanelForRules;

public class SelectComponentActionListener extends MouseAdapter {

	private ComponentPanel prevSelectedPanel;
	private ViewSemanticsPanel loadPanel;
	final int RULE_CREATION = 1;
	final int RULE_VIEWSTEM = 2;
	private FeaturePaletteScrollPanelForRules featPalette;
	int flag;
	public SelectComponentActionListener(ViewSemanticsPanel loadPanel){
		flag = RULE_VIEWSTEM;
		this.loadPanel = loadPanel;
	}
	
	public SelectComponentActionListener(FeaturePaletteScrollPanelForRules featPalette){
		flag = RULE_CREATION;
		this.featPalette = featPalette;
	}
	
	public ComponentPanel getSelectedPanel(){
		return prevSelectedPanel;
	}
	
	public void deselectCurrentPanel(){
		if(prevSelectedPanel != null)
			prevSelectedPanel.setHighlighted(false);
		prevSelectedPanel = null;
	}
	 	
	public void mousePressed(MouseEvent e) {
		ComponentPanel selectedPanel = (ComponentPanel) e.getSource();
		deselectCurrentPanel();
		selectedPanel.setHighlighted(true);
		prevSelectedPanel = selectedPanel;
		if(flag == RULE_VIEWSTEM)
			loadPanel.setComponent(selectedPanel.getComponent());
		if(flag == RULE_CREATION)
			featPalette.initCmbValues(selectedPanel.getComponent());
			
	}
}