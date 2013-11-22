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

import lexicon.LexiconList;
import managers.ComponentManager;
import managers.LexiconManager;
import managers.RuleManager;
import ontology.PartOfSpeech;
import rules.FormSelection;
import rules.RuleGroup;
import rules.RuleNode;
import rules.Rules;
import view.grammardevelopment.TreeNode;
import view.rules.FormSelectDialog;
import view.rules.OpenStructureListener;

import components.Component;
import components.Leaf;
import components.Phrase;

public class FormSelectionController implements RuleController{
	
	private RuleGroup parent;
	private TreeNode treenode;
	private JTree tree;
	private SelectFeatureController featureController;
	private FormSelection rule;
	private Leaf leafManipulate;
	private Phrase phraseManipulate;
	private Phrase clauseManipulate;
	private Phrase clauseOriginal;
	public FormSelectDialog view;
	public FormSelectionController controller = this;

	private final static int ADDRULE = 0;
	private final static int EDITRULE = 1;
	private int flag = 0;
	
	public FormSelectionController(JTree root, TreeNode node)
	{	/* initialize view, set model */
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
	
	public FormSelectionController(JTree tree, TreeNode node, Rules rule)				//		this is for edit
	{	/* initialize view, set model */
		treenode = node;

		this.rule = (FormSelection) rule;
		this.tree = tree;
		initializeListener();
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
		view.setTxtGroup(((TreeNode)treenode.getParent()).getNode().getName());
		view.setTxtRuleName(rule.getRuleName());
		view.setSelectedItemCmbCategory(leafManipulate.getPOS()); //this changes the contents of toManipulateComponent with a new Component instance (Which is wrong when editing)
		view.setStatus(rule.isOn());
		view.setDesireForm(this.rule.getDesiredForm());
		updateFeature();
		flag = EDITRULE;
	}
	
	public void initializeListener(){
		view = new FormSelectDialog();
		view.setLocationRelativeTo(null);
		view.addOKBtnListener(new OKActionListener());
		view.addStructuresBtnListener(new OpenStructureListener());
		view.addFeaturesBtnListener(new FeatureSelectionListener());
		view.addCancelBtnListener(new CancelActionListener());
		view.addCategoryItemListener(new CategoryItemListener());
		view.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	if(featureController!=null)
    				featureController.view.dispose();
            }
        });
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
	
	class CategoryItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			if(!clauseManipulate.getFeatures().isEmpty() || (phraseManipulate != null && !phraseManipulate.getFeatures().isEmpty()) || !leafManipulate.getFeatures().isEmpty())
			{
				 int result = JOptionPane.showConfirmDialog(null, "Modified features would be deleted after switching syntactic category", "alert", JOptionPane.OK_CANCEL_OPTION);
				 if(result == JOptionPane.YES_OPTION)
				 {
					changeBaseForm();
					createNewInstance();
					view.getTxtFeatures().setText("");
				 }
			}
			else
			{
				changeBaseForm();
				createNewInstance();
				view.getTxtFeatures().setText("");
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
		
		private void changeBaseForm()
		{
			JComboBox<String> cmbBaseForm = view.getCmbBaseForm();
			JComboBox<PartOfSpeech> cmbCategory = view.getCmbCategory();
			ItemListener listener = cmbBaseForm.getItemListeners()[0];
			cmbBaseForm.removeItemListener(listener);
			if(cmbCategory.getSelectedItem() != null)
			{
				LexiconList list = LexiconManager.getInstance().getLexiconList(((PartOfSpeech)cmbCategory.getSelectedItem()).getName());
				cmbBaseForm.removeAllItems();
				for(String form: list.getPossibleForms())
					cmbBaseForm.addItem(form);
			}
			cmbBaseForm.addItemListener(listener);
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

			if(view.getBaseForm() != null && !view.getRuleName().equals("")){
			
				if(flag == ADDRULE)
				{
					rule = new FormSelection(view.getRuleName());
					boolean status = (view.getrdbtnOn().isSelected())? true : false;
					rule.setStatus(status);
					rule.setDesiredForm(view.getBaseForm());
					rule.setComponent(clauseManipulate);
					rule.setDescription(view.getComment());
					RuleManager.getInstance().addRule(parent, new RuleNode(rule));
					view.dispose();
					((TreeNode)treenode).addChild(new TreeNode(new RuleNode(rule)));
					((DefaultTreeModel) tree.getModel()).reload(treenode);
				}
				else
				{
					FormSelection newRule = new FormSelection(view.getRuleName());
					boolean status = (view.getrdbtnOn().isSelected())? true : false;
					newRule.setStatus(status);
					newRule.setDesiredForm(view.getBaseForm());
					newRule.setComponent(clauseManipulate);
					newRule.setDescription(view.getComment());
					RuleManager.getInstance().editRule(rule, newRule);
					view.dispose();
					((DefaultTreeModel) tree.getModel()).reload();
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

