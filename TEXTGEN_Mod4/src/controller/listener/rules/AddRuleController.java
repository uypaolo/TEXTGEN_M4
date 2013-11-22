package controller.listener.rules;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import managers.RuleManager;

import components.InputXMLDocument;

import rules.RuleGroup;
import rules.RuleNode;
import rules.Structural;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.TreeNode;
import view.rules.AddRemFeatPopUp;
import view.rules.ConceptEditFrame;
import view.rules.LexiconEditFrame;
import view.rules.RuleAddingFrame;
import view.rules.RuleCreationPanel;
import controller.listener.grammardev.SelectComponentActionListener;
import controller.listener.grammardev.editsemantics.FeaturePaletteResetBtnListener;
import features.Feature;

public class AddRuleController {

	RuleAddingFrame raFrame;
	SelectComponentActionListener selectListener;
	AddRemFeatPopUp remFeatPopUp;
	AddRuleController controller = this;
	MoveButtonListener moveButtonListener;
	Structural structRule;
	int currTag;
	String ruleName;
	DropTarget panelDT;
	RuleGroup parentGroup;
	CopyFeatureButtonListener copyFeat;
	ViewLogListener viewLogListener;
	JTree tree;
	TreeNode treeNode;
	String flag;
	/*public AddRuleController(String ruleName,RuleGroup parentGroup,Structural structRule){
		 
		 raFrame= new RuleAddingFrame();
		 this.ruleName = ruleName;
		 this.parentGroup = parentGroup;
		 this.structRule = structRule;
		 
		// raFrame.getLeftPanel().setInputXMLDoc(structRule.g)
		 selectListener = new SelectComponentActionListener(raFrame.getToolbar().getFeatPalette());
		 raFrame.getLeftPanel().setSelectComponentPanelListener(selectListener);
		 panelDT = new DropTarget();
		 raFrame.getLeftPanel().setDropTargetForPanel(panelDT, controller);
		 addListeners();
		 raFrame.getLeftPanel().setController(controller);
		 raFrame.getRightPanel().setController(controller);
	}*/

	public AddRuleController(JTree tree, TreeNode treenode, String rulename,String flag) {
		
		this.flag = flag;
		raFrame = new RuleAddingFrame();
		this.tree = tree;
		
		 raFrame.getLeftPanel().setController(controller);
		 raFrame.getRightPanel().setController(controller);
		
		if(flag.equals("ADD")){
			structRule = new Structural(rulename);
			parentGroup = (RuleGroup) treenode.getNode();
			treeNode = treenode;
		}
		else if(flag.equals("EDIT")){
			treeNode = treenode;
			structRule = (Structural)((RuleNode)treenode.getNode()).getRule().getCopy();
			parentGroup = (RuleGroup)((TreeNode)treeNode.getParent()).getNode();
			raFrame.getLeftPanel().setInputXMLDoc(new InputXMLDocument(null,null,"","",
								structRule.getInputToMatch()));
			InputXMLDocument docu = new InputXMLDocument(null,null,"","",structRule.getOutput());
			raFrame.getRightPanel().setInputXMLDoc(docu);
		}
		
		 selectListener = new SelectComponentActionListener(raFrame.getToolbar().getFeatPalette());
		 raFrame.getLeftPanel().setSelectComponentPanelListener(selectListener);
		 panelDT = new DropTarget();
		 raFrame.getLeftPanel().setDropTargetForPanel(panelDT, controller);
		 raFrame.getRightPanel().setDropTargetForPanel(panelDT, controller);
		 addListeners();
		 raFrame.getLeftPanel().setController(controller);
		 raFrame.getRightPanel().setController(controller);
	}

	public ComponentPanel getCurrSelectedPanel(){
		return selectListener.getSelectedPanel();
	}
	
	public void deselectCurrSelectedPanel(){
		selectListener.deselectCurrentPanel();
	}
	
	public void getCurrSelectedDocumentPanel(){
		
	}
	
	public void addListeners(){
		raFrame.getToolbar().setBtnEditConceptListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(getCurrSelectedPanel()!=null && getCurrSelectedPanel().getComponent().isLeaf())
					new ConceptEditFrame(getCurrSelectedPanel());
				else if(getCurrSelectedPanel()!=null && !getCurrSelectedPanel().getComponent().isLeaf()){
					JOptionPane.showMessageDialog(
					        null, "Selected Panel is not a leaf", "Failure", JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(
					        null, "No panel selected", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		raFrame.getToolbar().getFeatPalette().addCmbFeaturesListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if(getCurrSelectedPanel()!=null)
					raFrame.getToolbar().getFeatPalette().renewCmbValues(getCurrSelectedPanel().getComponent());		
			}
		});
		
		raFrame.getToolbar().getFeatPalette().addCmbValuesListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				ComponentPanel compPanel = getCurrSelectedPanel();
				Feature feat = raFrame.getToolbar().getFeatPalette().getFeature();
				if(feat != null){
					compPanel.getComponent().setFeature(feat);
					
					if(compPanel.getRootDocPanel().getName().equals("TO")){
						structRule.editFeature(compPanel.getComponent(), feat.getName(),feat.getValue());
					}
						
					compPanel.refreshLabelToolTip();
				}
			}
		});
		
		raFrame.getToolbar().getFeatPalette().getButtonRemoveFeat().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String featCurrSelected = raFrame.getToolbar().getFeatPalette().getSelectedFeatInCmb();
				if(featCurrSelected!=null){
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " +
								"the selected feature?", "Confirm Delete?",dialogButton);
					if(dialogResult==0){
					  getCurrSelectedPanel().getComponent().removeFeature(featCurrSelected);
					  if(getCurrSelectedPanel().getRootDocPanel().getName().equals("TO")){
						  structRule.removeFeature(getCurrSelectedPanel().getComponent(), featCurrSelected);
						  System.out.println("Should remove feat");
					  }
					  raFrame.getToolbar().getFeatPalette().initCmbValues(getCurrSelectedPanel().getComponent());
					}
				}
				else{
					JOptionPane.showMessageDialog(null,
						    "Nothing to delete",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		
		raFrame.getToolbar().setBtnEditLexiconListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(getCurrSelectedPanel()!=null && getCurrSelectedPanel().getComponent().isLeaf())
					new LexiconEditFrame(getCurrSelectedPanel());
				else if(getCurrSelectedPanel()!=null && !getCurrSelectedPanel().getComponent().isLeaf()){
					JOptionPane.showMessageDialog(
					        null, "Selected Panel is not a leaf", "Failure", JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(
					        null, "No panel selected", "Failure", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		
		raFrame.getToolbar().setBtnSaveRuleListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				structRule.setStatus(raFrame.getToolbar().includeInGG());
				InputXMLDocument docu = raFrame.getRightPanel().getXMLDocument();
				structRule.setOutput(docu.getClauses());
				
				if(flag.equals("ADD")){
					RuleManager.getInstance().addRule(parentGroup, new RuleNode(structRule));
					((TreeNode)treeNode).addChild(new TreeNode(new RuleNode(structRule)));
					((DefaultTreeModel) tree.getModel()).reload(treeNode);
				}
				else{
					RuleManager.getInstance().editRule((Structural)((RuleNode)treeNode.getNode()).getRule(), 
							structRule);
					((DefaultTreeModel) tree.getModel()).reload(treeNode.getRoot());
				}
				
				
				raFrame.dispose();
			}
		});
		
		raFrame.getToolbar().getFeatPalette().addResetListener(new 
										FeaturePaletteResetBtnListener(this,raFrame.getToolbar().getFeatPalette()));
		
		
		
		raFrame.getToolbar().getFeatPalette().getButtonFeat().addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0){
				if(remFeatPopUp==null || !remFeatPopUp.isActive())
					remFeatPopUp = new AddRemFeatPopUp(controller);
			}
		});
		
		raFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	if(remFeatPopUp!=null)
            		remFeatPopUp.dispose();
            	if(copyFeat!=null && copyFeat.getAdapter()!=null && copyFeat.getAdapter().getPopUpFrame()!=null)
            		copyFeat.getAdapter().getPopUpFrame().dispose();
            	if(viewLogListener!=null && viewLogListener.getFrame()!=null)
            		viewLogListener.getFrame().dispose();
            }
        });
		
		raFrame.getToolbar().setBtnDeleteCompListener(new DeleteComponentInRuleListener(controller));
		
		moveButtonListener = new MoveButtonListener(this);
		
		raFrame.getToolbar().setBtnMoveListener(moveButtonListener);
		
		raFrame.getToolbar().setBtnCreateOutputListener(new InputOutputButtonListener(this));
		
		viewLogListener = new ViewLogListener(this);
		raFrame.getToolbar().setBtnViewLogListener(viewLogListener);
		
		CopyCompButtonListener copyComp = new CopyCompButtonListener(this);
		raFrame.getToolbar().setBtnCopyCompListener(copyComp);
		copyFeat = new CopyFeatureButtonListener(this);
		raFrame.getToolbar().setBtnCopyFeatureListener(copyFeat);
	}
	
	public Structural getStructRule(){
		return structRule;
	}
	
	public void setLeftVisible(){
		selectListener.deselectCurrentPanel();
		removeListeners();
		
		raFrame.setLeftVisible(true);
		raFrame.getToolbar().setBtnIOName("Create Output");
		raFrame.getToolbar().setBtnOutputVisible(false);
		raFrame.setRightVisible(false);
		raFrame.getLeftPanel().refreshDisplay();
		raFrame.getLeftPanel().setSelectComponentPanelListener(selectListener);
		
	}
	
	public void setRightVisible(){
		selectListener.deselectCurrentPanel();
		removeListeners();
		raFrame.getRightPanel().setController(controller);
		
		raFrame.setRightVisible(true);
		raFrame.setLeftVisible(false);
		raFrame.getToolbar().setBtnIOName("Go back to Criteria");
		raFrame.getToolbar().setBtnOutputVisible(true);
		
		InputXMLDocument docu;
		if(flag.equals("ADD")){
			docu = raFrame.getLeftPanel().getXMLDocument();
			currTag = docu.setTagForStructuralRule(1);
			structRule.setInputToMatch(docu.getClauses());
			raFrame.getRightPanel().setInputXMLDoc(docu.getCopyWithoutFeatures());
			structRule.clearActions();
		}
		else if(flag.equals("EDIT_RESET")){
			docu = raFrame.getLeftPanel().getXMLDocument();
			currTag = docu.setTagForStructuralRule(1);
			structRule.setInputToMatch(docu.getClauses());
			raFrame.getRightPanel().setInputXMLDoc(docu.getCopyWithoutFeatures());
			structRule.clearActions();
		}
		else{
			structRule.setInputToMatch(raFrame.getLeftPanel().getXMLDocument().getClauses());
			docu = new InputXMLDocument(null,null,"","",structRule.getOutput());
			raFrame.getRightPanel().adjustPositioning();
			currTag = docu.getBiggestTag()+1;
		}
		raFrame.getRightPanel().setController(controller);
		raFrame.getToolbar().getFeatPalette().initComponents();
		raFrame.getRightPanel().setRuleMode(true);
		panelDT = new DropTarget();
		raFrame.getRightPanel().setDropTargetForPanel(panelDT, controller);
		raFrame.getRightPanel().setSelectComponentPanelListener(selectListener);	
		

	}

	
	public void setCurrTag(int value){
		currTag = value;
	}
	
	public int getCurrTag(){
		return currTag;
	}
	
	public void adjustPositioning(){
		raFrame.getLeftPanel().adjustPositioning();
		raFrame.getRightPanel().adjustPositioning();
	}
	
	public RuleCreationPanel getLeftPanel(){
		return raFrame.getLeftPanel();
	}
	
	public RuleCreationPanel getRightPanel(){
		return raFrame.getRightPanel();
	}
	
	public void initFeatCmb(){
		raFrame.getToolbar().getFeatPalette().initCmbValues(getCurrSelectedPanel().getComponent());
	}
	
	public void setPanelCopyListeners(CopyCompPanelAdapter copyPanelAdapter){
		raFrame.getLeftPanel().setCopyPanelListener(copyPanelAdapter);
		raFrame.getRightPanel().setCopyPanelListener(copyPanelAdapter);
	}
	
	public void setPanelCopyFeatListener(CopyFeaturePanelAdapter adapter){
		raFrame.getLeftPanel().addMoveFeatListener(adapter);
		raFrame.getRightPanel().addMoveFeatListener(adapter);
	}
	
	public void setPanelMoveListeners(MovePanelMouseAdapter movePanelAdapter){
		raFrame.getLeftPanel().setMovePanelListener(movePanelAdapter);
		raFrame.getRightPanel().setMovePanelListener(movePanelAdapter);
	}
	
	public String getFlag(){
		return flag;
	}
	
	public void setFlag(String flag){
		this.flag = flag;
	}
	
	public void removeListeners(){
		raFrame.getLeftPanel().removeListeners();
		raFrame.getRightPanel().removeListeners();
	}
}
