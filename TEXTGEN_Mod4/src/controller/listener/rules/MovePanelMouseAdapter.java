package controller.listener.rules;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import rules.Structural;

import components.Component;
import components.Phrase;

import managers.ComponentManager;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

public class MovePanelMouseAdapter extends MouseAdapter{
	
	ComponentPanel panelToMove;
	AddRuleController controller;
	
	public MovePanelMouseAdapter(ComponentPanel panelToMove,AddRuleController controller){
		this.panelToMove = panelToMove;
		this.controller = controller;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		ComponentPanel compPanelDest = null;
		InputXMLDocumentPanel xmlPanelDest = null;
		if(arg0.getSource() instanceof view.grammardevelopment.ComponentPanel)
			compPanelDest = ((ComponentPanel)arg0.getSource());
		else
			xmlPanelDest = ((InputXMLDocumentPanel)arg0.getSource());
		//add here to this panel
		if(compPanelDest!= null && compPanelDest.getComponent().isLeaf()){
			JOptionPane.showMessageDialog(null,
				    "Cannot add to a leaf",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			
			if(panelToMove.getParentComponentPanel()!=null){
				int index = panelToMove.getParentComponentPanel().determineInsertIndex(arg0.getPoint());
				((Phrase)panelToMove.getParentComponentPanel().getComponent()).insertChildAt(index, panelToMove.getComponent());
				
				panelToMove.setSelectListener(panelToMove.getParentComponentPanel()
													.getSelectListener());
				
				panelToMove.getParentComponentPanel().addChildAt(index, panelToMove);
				controller.adjustPositioning();
				
				MouseEvent me = new MouseEvent(panelToMove, 0,0,0,100,100,1,false);
				for(MouseListener ml: panelToMove.getMouseListeners()){
					ml.mousePressed(me);
				};
				panelToMove.setHighlighted(true);
			}
			else if(panelToMove.getParentDocPanel()!=null){
				int index = panelToMove.getIndexInParentChildList();
				panelToMove.getParentDocPanel().getXMLDocument().addClauseAt(index, panelToMove.getComponent());
				ComponentPanel newPanel = ComponentPanel.CreateInstance(panelToMove.getComponent()
											, panelToMove.getParentDocPanel());
				newPanel.setSelectListener(panelToMove.getParentDocPanel().getSelectListener());
				
				panelToMove.getParentDocPanel().addSentencePanelAt(index, newPanel);
				panelToMove.getParentDocPanel().adjustPositioning();
				panelToMove.setHighlighted(true);
			}
		}
		else{
			int n = JOptionPane.showConfirmDialog(null,
				    "Pressing Yes will move "+panelToMove.getComponent().getName()+" to the panel" +
				    " selected\n"+ "Confirm Move?","Confirm",
				    JOptionPane.YES_NO_OPTION);
			
			if(n == JOptionPane.YES_OPTION){
				if(compPanelDest!=null){
					//adding to component panels
					if(compPanelDest.getRootDocPanel().getName().equals("FROM")){
						controller.setFlag("EDIT_RESET");
					}
					
					if(panelToMove.getRootDocPanel().getName().equals("TO")){
						Structural structRule = controller.getStructRule();
						if(panelToMove.getParentComponentPanel()!=null)
							structRule.moveComponent((Phrase)panelToMove.getParentComponentPanel().getComponent(), 
									(Phrase)compPanelDest.getComponent(), panelToMove.getComponent());
						else
							structRule.moveComponent(null, 
									(Phrase)compPanelDest.getComponent(), panelToMove.getComponent());
						System.out.println("Moveed");
					}
					
					int index = compPanelDest.determineInsertIndex(arg0.getPoint());
					((Phrase)compPanelDest.getComponent()).insertChildAt(index, panelToMove.getComponent());
					
					panelToMove.setSelectListener(compPanelDest.getSelectListener());
					
					compPanelDest.addChildAt(index, panelToMove);
					controller.adjustPositioning();
					compPanelDest.getRootDocPanel().setRuleMode(true);
					MouseEvent me = new MouseEvent(panelToMove, 0,0,0,100,100,1,false);
					for(MouseListener ml: panelToMove.getMouseListeners()){
						ml.mousePressed(me);
					}
					panelToMove.setHighlighted(true);
				}
				else{
					//adding to xml panel
					if(xmlPanelDest.getName().equals("FROM")){
						controller.setFlag("EDIT_RESET");
					}
					
					if(panelToMove.getRootDocPanel().getName().equals("TO")){
						Structural structRule = controller.getStructRule();
						if(panelToMove.getParentComponentPanel()!= null)
							structRule.moveComponentAsRoot((Phrase)panelToMove.getParentComponentPanel().getComponent(),
								panelToMove.getComponent());
						else
							structRule.moveComponentAsRoot(null,panelToMove.getComponent());
					}
					
					int index = xmlPanelDest.determineInsertIndex(arg0.getPoint());
					xmlPanelDest.getXMLDocument().addClauseAt(index, panelToMove.getComponent());
					ComponentPanel newPanel = ComponentPanel.CreateInstance(panelToMove.getComponent()
												, xmlPanelDest);
					newPanel.setSelectListener(xmlPanelDest.getSelectListener());
					
					xmlPanelDest.addSentencePanelAt(index, newPanel);
					xmlPanelDest.setRuleMode(true);
					xmlPanelDest.adjustPositioning();
					panelToMove.setHighlighted(true);
				}
				
			}
			else if(n == JOptionPane.NO_OPTION){
				if(panelToMove.getParentComponentPanel()!=null){
					int index = panelToMove.getParentComponentPanel().determineInsertIndex(arg0.getPoint());
					((Phrase)panelToMove.getParentComponentPanel().getComponent()).insertChildAt(index, panelToMove.getComponent());
					
					panelToMove.setSelectListener(panelToMove.getParentComponentPanel()
														.getSelectListener());
					
					panelToMove.getParentComponentPanel().addChildAt(index, panelToMove);
					controller.adjustPositioning();
					
					MouseEvent me = new MouseEvent(panelToMove, 0,0,0,100,100,1,false);
					for(MouseListener ml: panelToMove.getMouseListeners()){
						ml.mousePressed(me);
					};
					panelToMove.setHighlighted(true);
				}
				else if(panelToMove.getParentDocPanel()!=null){
					int index = panelToMove.getIndexInParentChildList();
					panelToMove.getParentDocPanel().getXMLDocument().addClauseAt(index, panelToMove.getComponent());
					ComponentPanel newPanel = ComponentPanel.CreateInstance(panelToMove.getComponent()
												, panelToMove.getParentDocPanel());
					newPanel.setSelectListener(panelToMove.getParentDocPanel().getSelectListener());
					
					panelToMove.getParentDocPanel().addSentencePanelAt(index, newPanel);
					panelToMove.getParentDocPanel().adjustPositioning();
					panelToMove.setHighlighted(true);
				}
			}
			//if yes, add to arg0.getSource()
			//if no , add to parent- xml or comp panel
		}
		
		controller.removeListeners();
	}
	
}
