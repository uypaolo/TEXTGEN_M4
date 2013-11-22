package controller.listener.grammardev.editsemantics;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import rules.Structural;

import components.Component;
import components.Phrase;
import controller.GrammarDevController;
import controller.listener.grammardev.SelectComponentActionListener;
import controller.listener.rules.AddRuleController;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.DisplayScreen;
import view.grammardevelopment.InputXMLDocumentPanel;

public class ComponentPanelDnDListener extends DropTargetAdapter{
	private InputXMLDocumentPanel parentDocPanel;
	AddRuleController controller;
	
	public ComponentPanelDnDListener(InputXMLDocumentPanel parentDocPanel){
		this.parentDocPanel = parentDocPanel;
		this.controller = null;
	}
	
	public ComponentPanelDnDListener(InputXMLDocumentPanel parentDocPanel,AddRuleController controller){
		this.parentDocPanel = parentDocPanel;
		this.controller = controller;
	}
	
	public void drop(DropTargetDropEvent arg0) {
		//System.out.println(arg0.getSource()+" "+arg0.getLocation());
		ComponentPanel panel = (ComponentPanel)((DropTarget)arg0.getSource()).getComponent(); // panel kung saan dinrop yung button
		Transferable t = arg0.getTransferable();
		DataFlavor[] d = t.getTransferDataFlavors();
		String buttonName;
		try {
			buttonName = (String)t.getTransferData(d[0]);
			//System.out.println("BUTTON "+ buttonName);
			int index = panel.determineInsertIndex(arg0.getLocation());
			
			Component parent = panel.getComponent();
			if(!parent.isLeaf()){ //only add if the target is not a leaf
				//System.out.println("Index is "+index+" of "+parent.getName());
				Component newComponent;
				System.out.println("Add Rule Controller " + controller);
				if(controller!=null)
					newComponent = Component.createInstance(buttonName, false);
				else
					newComponent = Component.createInstance(buttonName, true);
				((Phrase)parent).insertChildAt(index, newComponent);
				
				ComponentPanel newPanel = ComponentPanel.CreateInstance(newComponent, parentDocPanel);
				newPanel.setSelectListener(panel.getSelectListener());
				
				panel.addChildAt(index, newPanel);
				parentDocPanel.adjustPositioning();
				if(controller!=null){ // controller not null if called from addrule
					if(parentDocPanel.getName().equals("FROM")){
						if(controller.getFlag().equals("EDIT")){
							controller.setFlag("EDIT_RESET");
						}
					}
					if(parentDocPanel.getName().equals("TO")){
						controller.setCurrTag(newComponent.setTag(controller.getCurrTag()));
						newPanel.getRootDocPanel().setRuleMode(true);
						Structural structRule = controller.getStructRule();
						structRule.insertComponentAsNonRoot((Phrase)panel.getComponent(), newComponent);
					}
				}
				
				MouseEvent me = new MouseEvent(newPanel, 0,0,0,100,100,1,false);
				for(MouseListener ml: newPanel.getMouseListeners()){
					ml.mousePressed(me);
				}
			}
			else
				JOptionPane.showMessageDialog(null, "You can't add a child to a leaf.\nLeaves have black labels, Phrases have white ones.", "Oops!", JOptionPane.WARNING_MESSAGE);
		} catch (UnsupportedFlavorException e) {}
		  catch (IOException e) {}
	
	}	
}
