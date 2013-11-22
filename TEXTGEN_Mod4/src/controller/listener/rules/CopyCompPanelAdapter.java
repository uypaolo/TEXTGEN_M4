package controller.listener.rules;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import rules.Structural;

import components.Phrase;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

public class CopyCompPanelAdapter extends MouseAdapter{
	ComponentPanel panelToMove;
	AddRuleController controller;
	
	public CopyCompPanelAdapter(ComponentPanel panelToMove,AddRuleController controller){
		this.panelToMove = panelToMove;
		this.controller = controller;
	}
	
	public void mouseClicked(MouseEvent arg0) {
		ComponentPanel compPanelDest = null;
		InputXMLDocumentPanel xmlPanelDest = null;
		if(arg0.getSource() instanceof view.grammardevelopment.ComponentPanel)
			compPanelDest = ((ComponentPanel)arg0.getSource());
		else
			xmlPanelDest = ((InputXMLDocumentPanel)arg0.getSource());
		
		if(compPanelDest!= null && compPanelDest.getComponent().isLeaf()){
			JOptionPane.showMessageDialog(null,
				    "Cannot add to a leaf",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		else {
				int n = JOptionPane.showConfirmDialog(null,
			    "Pressing Yes will move "+panelToMove.getComponent().getName()+" to the panel" +
			    " selected\n"+ "Confirm Move?","Confirm",
			    JOptionPane.YES_NO_OPTION);
		
				if(n == JOptionPane.YES_OPTION){
					ComponentPanel clone = ComponentPanel.CreateInstance(panelToMove.getComponent().getCopy(),
							xmlPanelDest);
					clone.getComponent().setTag(controller.getCurrTag());
					controller.setCurrTag(controller.getCurrTag()+1);
					if(compPanelDest!=null){
						//adding to component panels
						int index = compPanelDest.determineInsertIndex(arg0.getPoint());
						((Phrase)compPanelDest.getComponent()).insertChildAt(index, clone.getComponent());
						
						clone.setSelectListener(compPanelDest.getSelectListener());
						
						
						if(panelToMove.getRootDocPanel().getName().equals("TO")){
							Structural structRule = controller.getStructRule();
							if(panelToMove.getParentComponentPanel()!=null)
								structRule.copyComponent((Phrase)panelToMove.getParentComponentPanel().getComponent(), 
										(Phrase)compPanelDest.getComponent(), panelToMove.getComponent());
							else
								structRule.copyComponent(null, 
										(Phrase)compPanelDest.getComponent(), panelToMove.getComponent());
						}
						
						compPanelDest.addChildAt(index, clone);
						clone.getRootDocPanel().setRuleMode(true);
						controller.adjustPositioning();	
						MouseEvent me = new MouseEvent(clone, 0,0,0,100,100,1,false);
						for(MouseListener ml: clone.getMouseListeners()){
							ml.mousePressed(me);
						}
						clone.setHighlighted(true);
					}
					else{
						int index = xmlPanelDest.determineInsertIndex(arg0.getPoint());
						
						
						if(panelToMove.getRootDocPanel().getName().equals("TO")){
							Structural structRule = controller.getStructRule();
							if(panelToMove.getParentComponentPanel()!= null)
								structRule.copyComponentAsRoot((Phrase)panelToMove.getParentComponentPanel()
										.getComponent(),panelToMove.getComponent());
							else
								structRule.copyComponentAsRoot(null,panelToMove.getComponent());
						}
						
						xmlPanelDest.getXMLDocument().addClauseAt(index, clone.getComponent());
						clone.setSelectListener(xmlPanelDest.getSelectListener());
						xmlPanelDest.setRuleMode(true);
						xmlPanelDest.addSentencePanelAt(index, clone);
						xmlPanelDest.adjustPositioning();
						panelToMove.setHighlighted(true);
					}
					
				}
		}
		controller.removeListeners();

	}
}
