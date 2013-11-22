package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import ontology.PartOfSpeech;

import lexicon.LexiconList;
import managers.ComponentManager;
import managers.LexiconManager;
import managers.RuleManager;
import rules.FormSelection;
import rules.Morphophonemic;
import rules.RuleGroup;
import rules.RuleNode;
import rules.Rules;
import view.grammardevelopment.TreeNode;
import view.rules.FormSelectDialog;
import view.rules.MorphoSpellOutDialog;
import view.rules.OpenStructureListener;
import components.Component;
import components.Leaf;
import components.Phrase;

public class MorphoSpellOutController implements RuleController {
	
	private RuleGroup parent;
	private TreeNode treenode;
	private JTree tree;
	private SelectFeatureController featureController;
	private Morphophonemic rule;
	private Leaf leafManipulate;
	private Phrase phraseManipulate;
	private Phrase clauseManipulate;
	private Phrase clauseOriginal;
	public MorphoSpellOutDialog view;
	public MorphoSpellOutController controller = this;

	private final static int ADDRULE = 0;
	private final static int EDITRULE = 1;
	private int flag = 0;
	
	public MorphoSpellOutController(JTree root, TreeNode node)
	{	
		
		parent = (RuleGroup)node.getNode();
		treenode = node;
		this.tree = root;
		initializeListener();
		view.setTxtGroup(parent.getName());
		clauseManipulate = (Phrase)Component.createInstance("CL", false);
		leafManipulate = (Leaf)Component.createInstance(view.getSelectedPOSCode(), false);
		
		String parentCode = ComponentManager.getInstance().getParentPhrasePOSCode(leafManipulate.getPOSCode());
		if(parentCode != null)
		{
			phraseManipulate = (Phrase)Component.createInstance(parentCode, false);
			clauseManipulate.addChild(phraseManipulate);
			phraseManipulate.addChild(leafManipulate);
		}
		else {
			clauseManipulate.addChild(leafManipulate);
			phraseManipulate = null;
		}
		flag = ADDRULE;
	}	
	
	public MorphoSpellOutController(JTree tree, TreeNode node, Rules rule)				//		this is for edit
	{	/* initialize view, set model */
		treenode = node;
		this.rule = (Morphophonemic)rule;
		this.tree = tree;
		clauseOriginal = (Phrase)rule.getFirstComponentInInputToMatch();
		clauseManipulate = (Phrase)clauseOriginal.getCopy();
		
		Component temp = clauseManipulate.getChildren().getChildren().get(0);
		if(!temp.isLeaf())
		{
			this.phraseManipulate = (Phrase)temp;
			leafManipulate = (Leaf)phraseManipulate.getChildren().getChildren().get(0);
		}
		
		else
		{
			leafManipulate = (Leaf)temp;
			phraseManipulate = null;
		}
		
		initializeListener();
		view.setTxtGroup(((TreeNode)treenode.getParent()).getNode().getName());
		view.setSelectedItemCmbCategory(leafManipulate.getPOS()); //this changes the contents of toManipulateComponent with a new Component instance (Which is wrong when editing)
		view.setTxtRulesName(rule.getRuleName());
		view.setDescription(leafManipulate.getDescription());
		view.setStemBeginOrEnd(((Morphophonemic) rule).getStemBeginOrEnd());
		view.setStemNewBeginOrEnd(((Morphophonemic) rule).getStemNewBeginOrEnd());
		view.setNewValue(((Morphophonemic) rule).getNewValue());
		view.setOldValue(((Morphophonemic) rule).getOldValue());
		view.setStemType(((Morphophonemic) rule).getStemType());
		view.setChangeType(((Morphophonemic) rule).getChangeType());
		view.setMorphemeType(((Morphophonemic) rule).getMorphemeType());
		view.setCorrespondingLabel();
		view.setStatus(rule.isOn());
		updateFeature();
		flag = EDITRULE;
	}
	
	public void initializeListener(){
		view = new MorphoSpellOutDialog();
		view.setLocationRelativeTo(null);
		view.addOKBtnListener(new OKActionListener());
		view.addCancelBtnListener(new CancelActionListener());
		view.addCategoryItemListener(new CategoryItemListener());
		view.addFeaturesBtnListener(new FeatureSelectionListener());
		view.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	if(featureController!=null)
    				featureController.view.dispose();
            }
        });
	}
	
	class CategoryItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			if(!clauseManipulate.getFeatures().isEmpty() || (phraseManipulate != null && !phraseManipulate.getFeatures().isEmpty()) || !leafManipulate.getFeatures().isEmpty())
			{
				 int result = JOptionPane.showConfirmDialog(null, "Modified features would be deleted after switching syntactic category", "alert", JOptionPane.OK_CANCEL_OPTION);
				 if(result == JOptionPane.YES_OPTION)
				 {
					createNewInstance();
					view.setFeature("");
				 }
			}
			else{
				createNewInstance();
				view.setFeature("");
			}
		}
		
		public void createNewInstance()
		{
			clauseManipulate = (Phrase)Component.createInstance("CL", false);
			leafManipulate = (Leaf)Component.createInstance(view.getSelectedPOSCode(), false);
			String parentCode = ComponentManager.getInstance().getParentPhrasePOSCode(leafManipulate.getPOSCode());
			
			if(parentCode != null)
			{
				phraseManipulate = (Phrase)Component.createInstance(parentCode, false);
				clauseManipulate.addChild(phraseManipulate);
				phraseManipulate.addChild(leafManipulate);
			}
			else
				new SelectPhraseController(controller);
		}
	}
	
	
	class FeatureSelectionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) 
		{	
			featureController = new SelectFeatureController(clauseManipulate, controller);
		}
		
	}

	class OKActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
		if(  !view.getTxtRulesName().equals("") && !view.getOldValue().equals("") &&
				(
				 (
			     (view.getChangeType().equals(Morphophonemic.AFFIX_CHANGE_TYPE_DOESNT_CHANGE))     ||
				 (view.getChangeType().equals(Morphophonemic.AFFIX_CHANGE_TYPE_PART_CHANGES) && !view.getNewValue().equals(""))     ||
			     (view.getChangeType().equals(Morphophonemic.AFFIX_CHANGE_TYPE_ENTIRE_CHANGES) && !view.getNewValue().equals(""))
			     )   
			     &&
			     
			     
			     (
			     (view.getStemType().equals(Morphophonemic.STEM_CHANGE_TYPE_DOESNT_CHANGE) && !view.getStemBeginOrEnd().equals(""))     ||
			     (view.getStemType().equals(Morphophonemic.STEM_CHANGE_TYPE_ENTIRE_CHANGES)  && !view.getStemBeginOrEnd().equals("") && !view.getStemNewBeginOrEnd().equals(""))
			     )
			   
				)		
			){
				if(flag == ADDRULE){
					rule = new Morphophonemic(view.getTxtRulesName());
					rule.setChangeType(view.getChangeType());
					rule.setDescription(view.getDescription());
					rule.setMorphemeType(view.getMorphemeType());
					rule.setNewValue(view.getNewValue());
					rule.setOldValue(view.getOldValue());
					rule.setStemBeginOrEnd(view.getStemBeginOrEnd());
					rule.setStemNewBeginOrEnd(view.getStemNewBeginOrEnd());
					rule.setStemType(view.getStemType());
					rule.setComponent(clauseManipulate);
					rule.setStatus(view.getStatus());
					RuleManager.getInstance().addRule(parent, new RuleNode(rule));
					view.dispose();
					((TreeNode)treenode).addChild(new TreeNode(new RuleNode(rule)));
					((DefaultTreeModel) tree.getModel()).reload(treenode);
				}
				else{
					Morphophonemic newRule = new Morphophonemic(view.getTxtRulesName());
					newRule.setChangeType(view.getChangeType());
					newRule.setDescription(view.getDescription());
					newRule.setMorphemeType(view.getMorphemeType());
					newRule.setNewValue(view.getNewValue());
					newRule.setOldValue(view.getOldValue());
					newRule.setStemBeginOrEnd(view.getStemBeginOrEnd());
					newRule.setStemNewBeginOrEnd(view.getStemNewBeginOrEnd());
					newRule.setStemType(view.getStemType());
					newRule.setComponent(clauseManipulate);
					RuleManager.getInstance().editRule(rule, newRule);
					view.dispose();
					((DefaultTreeModel) tree.getModel()).reload(treenode.getRoot());
				}
			}
			else
				JOptionPane.showMessageDialog(null, "Please complete all necessary fields.",
						"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	class CancelActionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) 
		{
			if(featureController!=null)
				featureController.view.dispose();
			view.dispose();
		}
	}
	
	public void updateFeature()
	{
		ArrayList<String> finalList = new ArrayList<String>();
		ArrayList<String> basicFeatureList = generateFeatureList(leafManipulate);
		for(String string: basicFeatureList)
			finalList.add(string);
		if(phraseManipulate != null)
		{
			ArrayList<String> phraseFeatureList = generateFeatureList(phraseManipulate);
			for(String string: phraseFeatureList)
				finalList.add(string);
		}
		ArrayList<String> clauseFeatureList = generateFeatureList(clauseManipulate);
		for(String string: clauseFeatureList)
			finalList.add(string);
		view.updateFeatureDisplay(finalList);
	}
	
	public ArrayList<String> generateFeatureList(Component component)
	{
		ArrayList<String> displayFeatureList = new ArrayList<String>();
		String tempName = "";
		for(int i = 0; i < component.getFeatures().size(); i++)
		{
			String feature="";
			if(tempName.equals("") || !tempName.equals(component.getFeatures().get(i).getName()))
			{
				tempName = component.getFeatures().get(i).getName();
				feature += "Feature name: " + component.getFeatures().get(i).getName() + ". Value: " + component.getFeatures().get(i).getValue();
			}
			else
				feature += " or " + component.getFeatures().get(i).getValue();
			displayFeatureList.add(feature);
		}
		return displayFeatureList;
	}

	
	public void setPhraseComponent(Phrase component){
		this.phraseManipulate = component;
	}

	@Override
	public void connectComponents(){
		if(phraseManipulate != null)
		{
			clauseManipulate.addChild(phraseManipulate);
			phraseManipulate.addChild(leafManipulate);
		}
		else
			clauseManipulate.addChild(leafManipulate);
	}
}
