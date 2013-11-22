package view.rules;

import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;

import components.Component;
import components.InputXMLDocument;

import controller.listener.grammardev.SelectComponentActionListener;
import controller.listener.rules.AddRuleController;
import controller.listener.rules.CopyCompPanelAdapter;
import controller.listener.rules.CopyFeaturePanelAdapter;
import controller.listener.rules.MovePanelMouseAdapter;
import controller.listener.rules.MovePanelMouseAdapter;
import controller.listener.rules.RuleXMLDocDndListener;

public class RuleCreationPanel extends InputXMLDocumentPanel{
	
	AddRuleController controller;
	
	public RuleCreationPanel(InputXMLDocument doc){
		super(doc);
		/*DropTarget dt = new DropTarget();
		try {
			dt.addDropTargetListener(new RuleXMLDocDndListener(this));
			this.setDropTarget(dt);
			
		} catch (TooManyListenersException e) {}
		*/
		//setSelectComponentPanelListener(selectListener);
		
	}
	
	public void setController(AddRuleController controller){
		this.controller = controller;
		for(ComponentPanel compPanel : sentencePanels){
			compPanel.setController(controller);
		}
	}
	
	public int getSentencePanelCount(){
		return this.sentencePanels.size();
	}
	
	public AddRuleController getController(){
		return controller;
	}
	
	public void setDropTargetForPanel(DropTarget dt,AddRuleController controller){
		try {
			dt.addDropTargetListener(new RuleXMLDocDndListener(this,controller));
			this.setDropTarget(dt);
			
		} catch (TooManyListenersException e) {}
	}
	
	public ArrayList<Component> getComponentList(){
		ArrayList<Component> compList = new ArrayList<Component>();
		for(ComponentPanel compPanel : sentencePanels){
			compList.add(compPanel.getComponent());
		}
		return compList;
	}
	
	public void setMovePanelListener(MovePanelMouseAdapter mouseAdapter){
		removeListeners();
		this.addMouseListener(mouseAdapter);
		for(ComponentPanel sentencePanel: sentencePanels){
			sentencePanel.setListenerToPanel(mouseAdapter);
		}
	}
	
	public void setCopyPanelListener(CopyCompPanelAdapter copyPanelAdapter){
		removeListeners();
		this.addMouseListener(copyPanelAdapter);
		for(ComponentPanel sentencePanel: sentencePanels){
			sentencePanel.setListenerToPanel(copyPanelAdapter);
		}
	}
	

	public MovePanelMouseAdapter getMovePanelMouseAdapter(){
		for(MouseListener m: this.getMouseListeners()){
			if(m instanceof MovePanelMouseAdapter){
				return (MovePanelMouseAdapter) m;
			}
		}
	    return null;
	}
	
	
	public void removeListeners(){
		for(MouseListener m: this.getMouseListeners()){
			if(m instanceof MovePanelMouseAdapter)
				this.removeMouseListener(m);
			if(m instanceof CopyFeaturePanelAdapter)
				this.removeMouseListener(m);
			if(m instanceof CopyCompPanelAdapter)
				this.removeMouseListener(m);
		}
		for(ComponentPanel sentencePanel : sentencePanels)
			sentencePanel.removeListeners();
	}
	
	public void addMoveFeatListener(CopyFeaturePanelAdapter featAdapter){
		removeListeners();
		for(ComponentPanel sentencePanel: sentencePanels){
			sentencePanel.setListenerToPanel(featAdapter);
		}
	}
	
}
