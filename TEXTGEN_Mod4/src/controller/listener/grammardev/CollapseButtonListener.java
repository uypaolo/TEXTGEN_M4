package controller.listener.grammardev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import view.grammardevelopment.ComponentPanel;

public class CollapseButtonListener implements ActionListener{
	private ComponentPanel panelToListenTo;
	
	public CollapseButtonListener(ComponentPanel panelToListenTo){
		this.panelToListenTo = panelToListenTo;
	}
	
	public void actionPerformed(ActionEvent e){
		JButton btn = (JButton) e.getSource();
		
		if(btn.getActionCommand().equals(ComponentPanel.COMMAND_COLLAPSE))
			panelToListenTo.setCollapsed(true);
		else
			panelToListenTo.setCollapsed(false);
		panelToListenTo.getParentDocPanel().adjustPositioning();
	}

}
