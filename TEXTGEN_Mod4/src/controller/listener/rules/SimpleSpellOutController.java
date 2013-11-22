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

import lexicon.Lexicon;
import lexicon.LexiconList;
import managers.ComponentManager;
import managers.LexiconManager;
import managers.RuleManager;
import ontology.PartOfSpeech;
import rules.RuleGroup;
import rules.RuleNode;
import rules.RuleTreeNode;
import rules.Rules;
import rules.SimpleSpellout;
import view.grammardevelopment.TreeNode;
import view.rules.SimpleSpellOutDialog;

import components.Component;
import components.Leaf;
import components.Phrase;

public class SimpleSpellOutController implements RuleController{
	private RuleGroup parent;
	private TreeNode treenode;
	private RuleTreeNode ruleTreeNode;
	private JTree tree;
	private SelectFeatureController featureController;
	private SimpleSpellout rule;
	private Leaf leafManipulate;
	private Phrase phraseManipulate;
	private Phrase clauseManipulate;
	private Phrase clauseOriginal;
	private CategoryItemListener categoryItemListener;
	public SimpleSpellOutDialog view;
	private SimpleSpellOutController controller = this;
	final static int ADDRULE = 0;
	final static int EDITRULE = 1;
	private int flag;
	
	//add 
	public SimpleSpellOutController(JTree root, TreeNode node)
	{	
		parent = (RuleGroup)node.getNode();
		this.treenode = node;
		this.tree = root;
		initializeListeners();
		view.setTxtGroup(parent.getName());
		
		//create instance of components
		clauseManipulate = (Phrase)Component.createInstance("CL", false);
		leafManipulate = (Leaf)Component.createInstance(view.getSelectedPOSCode(), false);
		String parentCode = ComponentManager.getInstance().getParentPhrasePOSCode(leafManipulate.getPOSCode());
		if(parentCode != null){
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
	
	// edit 
	public SimpleSpellOutController(JTree tree, TreeNode node, Rules rule)				
	{	
		this.tree = tree;
		this.treenode = node;
		this.rule = (SimpleSpellout) rule;		
		clauseOriginal = (Phrase)rule.getFirstComponentInInputToMatch();
		clauseManipulate = (Phrase)clauseOriginal.getCopy();
		Component temp = clauseManipulate.getChildren().getChildren().get(0);
		if(!temp.isLeaf()){
			this.phraseManipulate = (Phrase)temp;
			leafManipulate = (Leaf)phraseManipulate.getChildren().getChildren().get(0);
		}
		
		else{
			leafManipulate = (Leaf)temp;
			phraseManipulate = null;
		}
		
		initializeListeners();
		view.setTxtGroup(((TreeNode)treenode.getParent()).getNode().getName());
		view.setTxtRuleName(rule.getRuleName());
		view.setSelectedItemCmbCategory(leafManipulate.getPOS(), categoryItemListener); //this changes the contents of toManipulateComponent with a new Component instance (Which is wrong when editing)
		view.setStatus(rule.isOn());
		view.setType(this.rule.getSpellOutType());
		view.setSubword(this.rule.getSubWord());
		view.setComment(this.rule.getDescription());
		

		//get lexicon part
		Lexicon lex = leafManipulate.getFirstMappedLexicon();
		System.out.println("firstmaplexicon: " + lex.toString());
		System.out.println("lex value: " + lex.getName());
		view.setTriggerWord(leafManipulate.getFirstMappedLexicon().getName());
		view.populateCmbForm(view.getSelectedPOSCode());
		
		updateFeature();
		flag = EDITRULE;
	}
	
	public void initializeListeners()
	{
		view = new SimpleSpellOutDialog();
		view.setLocationRelativeTo(null);
		categoryItemListener = new CategoryItemListener();
		view.addBtnOKListener(new OKActionListener());
		view.addBtnStructureListener(new StructureListener());
		view.addBtnFeaturesListener(new FeatureSelectionListener());
		view.addBtnCancelListener(new CancelActionListener());
		view.addCategoryItemListener(categoryItemListener);
		view.addBtnTriggerWordListener(new TriggerWordSelectionListener());
		view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
			public void windowClosing(java.awt.event.WindowEvent e) {
            	if(featureController!=null)
    				featureController.view.dispose();
            }
        });
		view.populateCmbForm(view.getSelectedPOSCode());
	}
	public void updateTriggerWord(String newWord){
		view.addNewTriggerWord(newWord);
	}
	

	@Override
	public void setPhraseComponent(Phrase component){
		this.phraseManipulate = component;
	}
	
	@Override
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
	
	class TriggerWordSelectionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			new SelectTriggerWordController(leafManipulate, controller);
		}
	}
	
	class FeatureSelectionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) 
		{	
			new SelectFeatureController(clauseManipulate, controller);
		}
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
	
	class CategoryItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			if(!clauseManipulate.getFeatures().isEmpty() || (phraseManipulate != null && !phraseManipulate.getFeatures().isEmpty()) || !leafManipulate.getFeatures().isEmpty() || view.getCmbTriggerWord().getSelectedItem()!=null)
			{
				 int result = JOptionPane.showConfirmDialog(null, "Modified features would be deleted after switching syntactic category", "alert", JOptionPane.OK_CANCEL_OPTION);
				 if(result == JOptionPane.YES_OPTION)
				 {
					changeBaseForm();
					createNewInstance();
					view.getCmbTriggerWord().removeAllItems();
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
				if(cmbBaseForm.getItemCount() < 1)
					cmbBaseForm.addItem(SimpleSpellout.BASE_FORM);
				for(String form: list.getPossibleForms())
					cmbBaseForm.addItem(form);
			}
			cmbBaseForm.addItemListener(listener);
		}
	}
	
	class StructureListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			System.out.println("structure here");
		}
	}

	class OKActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!view.getRuleName().equals("")
				&& !view.getSubword().equals("")
				&& view.getGroupAffix().getSelection() != null
				&& view.getGroupStatus().getSelection() != null)
			{
				if(flag == ADDRULE)
				{
					rule = new SimpleSpellout(view.getRuleName());
					boolean status = (view.getRdbtnOn().isSelected())? true : false;
					rule.setStatus(status);
					rule.setSubWord(view.getSubword());
					rule.setSpellOutType(view.getSpellOutType());
					rule.setDescription(view.getComment());
					rule.setForm(view.getBaseForm());
					// set lexicon part
					LexiconList lexList = LexiconManager.getInstance().getLexiconList(view.getSelectedPOSCode());
					Lexicon lexicon = lexList.getLexiconInstance(view.getTriggerWord());
					leafManipulate.setLexicon(lexicon);
					
					rule.setComponent(clauseManipulate);
					RuleManager.getInstance().addRule(parent, new RuleNode(rule));
					treenode.addChild(new TreeNode(new RuleNode(rule)));
					((DefaultTreeModel) tree.getModel()).reload(treenode);
					view.dispose();
				}

				else if(flag == EDITRULE)
				{
					SimpleSpellout newRule = new SimpleSpellout(view.getRuleName());
					boolean status = (view.getRdbtnOn().isSelected())? true : false;

					LexiconList lexList = LexiconManager.getInstance().getLexiconList(view.getSelectedPOSCode());
					Lexicon lexicon = lexList.getLexiconInstance(view.getTriggerWord());
					leafManipulate.setLexicon(lexicon);
					newRule.setComponent(clauseManipulate);
					newRule.setStatus(status);
					newRule.setSubWord(view.getSubword());
					newRule.setDescription(view.getComment());
					newRule.setSpellOutType(view.getSpellOutType());
					newRule.setForm(view.getBaseForm());
					RuleManager.getInstance().editRule(rule, newRule);
					view.dispose();
					((DefaultTreeModel) tree.getModel()).reload(treenode.getRoot());
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Please fill up all information needed",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	class CancelActionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(featureController!=null)
				featureController.view.dispose();
			view.dispose();
		}
	}
}
