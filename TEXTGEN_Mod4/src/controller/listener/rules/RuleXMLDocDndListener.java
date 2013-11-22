package controller.listener.rules;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import rules.Structural;

import view.grammardevelopment.ComponentPanel;
import view.rules.RuleCreationPanel;

import components.Component;

public class RuleXMLDocDndListener extends DropTargetAdapter	{

	private RuleCreationPanel parentDocPanel;
	AddRuleController controller;
	public RuleXMLDocDndListener(RuleCreationPanel parentDocPanel,AddRuleController controller){
		this.parentDocPanel = parentDocPanel;
		this.controller = controller;
	}
	
	public void drop(DropTargetDropEvent arg0) {
		//System.out.println(arg0.getSource()+" "+arg0.getLocation());
		RuleCreationPanel panel = (RuleCreationPanel)((DropTarget)arg0.getSource()).getComponent(); // panel kung saan dinrop yung button
		Transferable t = arg0.getTransferable();
		DataFlavor[] d = t.getTransferDataFlavors();
		String buttonName;
		try {
			buttonName = (String)t.getTransferData(d[0]);
			//System.out.println("BUTTON "+ buttonName);
			int index = panel.determineInsertIndex(arg0.getLocation());
			Component newComponent = Component.createInstance(buttonName, false);
			if(panel.getName().equals("FROM")){
				if(controller.getFlag().equals("EDIT"))
					controller.setFlag("EDIT_RESET");
			}
			if(panel.getName().equals("TO")){
				controller.setCurrTag(newComponent.setTag(controller.getCurrTag()));
				Structural structRule = controller.getStructRule();
				structRule.insertComponentAsRoot(newComponent);
			}
			panel.getXMLDocument().addClauseAt(index, newComponent);
			ComponentPanel newPanel = ComponentPanel.CreateInstance(newComponent, parentDocPanel);
			
			newPanel.setSelectListener(panel.getSelectListener());
			//System.out.println(panel.getSelectListener());
			
			panel.addSentencePanelAt(index, newPanel);
			if(panel.getName().equals("TO"))
				newPanel.getRootDocPanel().setRuleMode(true);
			parentDocPanel.adjustPositioning();

			MouseEvent me = new MouseEvent(newPanel, 0,0,0,100,100,1,false);
			for(MouseListener ml: newPanel.getMouseListeners()){
				ml.mousePressed(me);
			}
			
		} catch (UnsupportedFlavorException e) {}
		  catch (IOException e) {}
	
	}	
	
	
}
