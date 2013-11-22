package view.rules;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import lexicon.LexiconList;
import managers.ComponentManager;
import managers.LexiconManager;
import ontology.PartOfSpeech;
import rules.SimpleSpellout;

public class SimpleSpellOutDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtGroup;
	private JTextField txtRuleName;
	private JTextField txtSubword;
	private JLabel lblAffix;
	private JPanel cardPanel;
	private JButton btnStructures;
	private JComboBox<String> cmbBaseForm;
	private JComboBox<String> cmbTriggerWord;
	private JComboBox<PartOfSpeech> cmbCategory;
	private JButton btnTriggerWord;
	private JButton btnOK; 
	private JButton btnCancel;
	private JTextArea txtComment;
	private JTextArea txtFeatures; 
	private JButton btnFeatures;
	private ItemListener categoryItemListener;
	
	private CardLayout cardLayout;
	
	private ButtonGroup groupStatus;
	private JRadioButton rdbtnOn;
	
	
	public void setTxtRuleName(String ruleName)
	{
		txtRuleName.setText(ruleName);
	}
	
	public void setSelectedItemCmbCategory(PartOfSpeech pos, ItemListener listener){
		
		cmbCategory.removeItemListener(listener);
		cmbCategory.setSelectedItem(pos);
		cmbCategory.addItemListener(listener);
	}
	
	public String getSubword()
	{
		return txtSubword.getText();
	}
	
	public String getComment(){
		return txtComment.getText();
	}
	
	public JRadioButton getRdbtnOn() {
		return rdbtnOn;
	}

	private JRadioButton rdbtnOff;
	
	private ButtonGroup groupAffix;
	
	public ButtonGroup getGroupStatus() {
		return groupStatus;
	}

	public ButtonGroup getGroupAffix() {
		return groupAffix;
	}
	private JRadioButton rdbtnPrefix;
	private JRadioButton rdbtnSuffix;
	private JRadioButton rdbtnNewTranslation;
	private JRadioButton rdbtnAddWord;
	

	public SimpleSpellOutDialog() {
		setTitle("Simple Spell Out Rule");
		setBounds(100, 100, 650, 700);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblSyntacticCategory = new JLabel("Syntactic Category:");
		lblSyntacticCategory.setBounds(10, 26, 126, 14);
		contentPanel.add(lblSyntacticCategory);
		
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setBounds(297, 26, 46, 14);
		contentPanel.add(lblGroup);
		
		txtGroup = new JTextField();
		txtGroup.setEditable(false);
		txtGroup.setBounds(338, 23, 143, 20);
		contentPanel.add(txtGroup);
		txtGroup.setColumns(10);
		
		JLabel lblRuleName = new JLabel("Rule's Name:");
		lblRuleName.setBounds(48, 51, 88, 14);
		contentPanel.add(lblRuleName);
		
		txtRuleName = new JTextField();
		txtRuleName.setColumns(10);
		txtRuleName.setBounds(130, 50, 494, 20);
		contentPanel.add(txtRuleName);
		
		/* Setup Status Panel */
		JPanel statusPanel = new JPanel();
		statusPanel.setBounds(188, 86, 236, 59);
		statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Status"));
		contentPanel.add(statusPanel);
		statusPanel.setLayout(null);
		
		groupStatus = new ButtonGroup();
		rdbtnOn = new JRadioButton("On");
		rdbtnOn.setBounds(41, 19, 84, 23);
		statusPanel.add(rdbtnOn);
		rdbtnOff = new JRadioButton("Off");
		rdbtnOff.setBounds(132, 19, 77, 23);
		statusPanel.add(rdbtnOff);
		groupStatus.add(rdbtnOn);
		groupStatus.add(rdbtnOff);
		/******************************/
		
		
		/*	Simple Panel */
		JPanel simplePanel = new JPanel();
		simplePanel.setBounds(10, 155, 614, 180);
		simplePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Simple"));
		contentPanel.add(simplePanel);
		simplePanel.setLayout(null);
		
		btnStructures = new JButton("Structures");
		
		btnStructures.setBounds(10, 62, 110, 21);
		simplePanel.add(btnStructures);
		
		JPanel typeOfModifyPanel = new JPanel();
		typeOfModifyPanel.setBounds(130, 27, 474, 85);
		typeOfModifyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Type of Modification"));
		simplePanel.add(typeOfModifyPanel);

		typeOfModifyPanel.setLayout(null);
		
		btnTriggerWord = new JButton("Trigger Word");
		btnTriggerWord.setBounds(10, 134, 110, 21);
		simplePanel.add(btnTriggerWord);
		
		cmbTriggerWord = new JComboBox<String>();
		cmbTriggerWord.setBounds(130, 134, 339, 21);
		cmbTriggerWord.setBackground(Color.WHITE);
		simplePanel.add(cmbTriggerWord);
		
		/******************************************/
		
		/* card layout */
		
		/* card 1 */
		JPanel chooseFixPanel = new JPanel();
		chooseFixPanel.setBounds(68, 416, 461, 99);
		chooseFixPanel.setLayout(null);
		
		/* card 2 */
		chooseFixPanel.setBounds(68, 416, 461, 99);
		chooseFixPanel.setLayout(null);
		
		cardLayout = new CardLayout();
		cardPanel = new JPanel();
		cardPanel.setLayout(cardLayout);
		cardPanel.setBounds(138, 460, 371, 79);
		cardPanel.add(chooseFixPanel, "fix");
		
		
		/* setup card1 panel */
		JLabel lblBaseForm = new JLabel("Base Form:");
		lblBaseForm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBaseForm.setBounds(40, 10, 130, 21);
		chooseFixPanel.add(lblBaseForm);
		
		cmbBaseForm = new JComboBox<String>();
		cmbBaseForm.setBounds(190, 10, 133, 21);
		chooseFixPanel.add(cmbBaseForm);
		
		lblAffix = new JLabel("Affix:");
		lblAffix.setBounds(40, 41, 130, 15);
		chooseFixPanel.add(lblAffix);
		lblAffix.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtSubword = new JTextField();
		txtSubword.setBounds(190, 41, 133, 21);
		chooseFixPanel.add(txtSubword);
		txtSubword.setColumns(10);
		/***********************************************/
		
		contentPanel.add(cardPanel);
		groupAffix = new ButtonGroup();
		rdbtnPrefix = new JRadioButton("Prefix");
		rdbtnPrefix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblAffix.setText("Prefix:");
				cardLayout.show(cardPanel, "fix");
			}
		});
		rdbtnPrefix.setBounds(99, 21, 71, 23);
		typeOfModifyPanel.add(rdbtnPrefix);
		
		rdbtnSuffix = new JRadioButton("Suffix");
		rdbtnSuffix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAffix.setText("Suffix:");
				cardLayout.show(cardPanel, "fix");
			}
		});
		rdbtnSuffix.setBounds(99, 46, 71, 23);
		typeOfModifyPanel.add(rdbtnSuffix);
		
		rdbtnNewTranslation = new JRadioButton("New Translation");
		rdbtnNewTranslation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAffix.setText("Translation:");
				cardLayout.show(cardPanel, "fix");
			}
		});
		rdbtnNewTranslation.setBounds(253, 21, 121, 23);
		typeOfModifyPanel.add(rdbtnNewTranslation);
		
		rdbtnAddWord = new JRadioButton("Add Word");
		rdbtnAddWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblAffix.setText("New Word:");
				cardLayout.show(cardPanel, "fix");
			}
		});
		rdbtnAddWord.setBounds(253, 46, 121, 23);
		typeOfModifyPanel.add(rdbtnAddWord);
		
		groupAffix.add(rdbtnPrefix);
		groupAffix.add(rdbtnSuffix);;
		groupAffix.add(rdbtnNewTranslation);
		groupAffix.add(rdbtnAddWord);
		
		/*******************************************/
		
		btnOK = new JButton("OK");
		btnOK.setBounds(388, 627, 89, 23);
		getRootPane().setDefaultButton(btnOK);
		contentPanel.add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnCancel.setBounds(490, 627, 89, 23);
		contentPanel.add(btnCancel);
		
		JLabel lblComment = new JLabel("Comment:");
		lblComment.setFont(new Font("SimSun", Font.PLAIN, 13));
		lblComment.setBounds(31, 525, 105, 14);
		contentPanel.add(lblComment);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(31, 549, 581, 59);
		contentPanel.add(scrollPane);
		
		txtComment = new JTextArea();
		scrollPane.setViewportView(txtComment);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(140, 345, 472, 105);
		contentPanel.add(scrollPane_1);
		
		txtFeatures = new JTextArea();
		txtFeatures.setFont(new Font("SimSun", Font.PLAIN, 11));
		txtFeatures.setBackground(UIManager.getColor("Button.background"));
		txtFeatures.setEditable(false);
		scrollPane_1.setViewportView(txtFeatures);
		
		btnFeatures = new JButton("Features");
		btnFeatures.setBounds(20, 358, 110, 23);
		contentPanel.add(btnFeatures);
		
		cmbCategory = new JComboBox<PartOfSpeech>();
		cmbCategory.setBounds(130, 23, 143, 20);
		contentPanel.add(cmbCategory);
		
		populateCmbCategory();
		revalidate();
		repaint();
	}
	
	public void populateCmbForm(String pos){
		LexiconList list = LexiconManager.getInstance().getLexiconList(pos);
		cmbBaseForm.removeAllItems();
		cmbBaseForm.addItem(SimpleSpellout.BASE_FORM);
		for(String s: list.getPossibleForms())
			cmbBaseForm.addItem(s);
	}
	
	public void updateFeatureDisplay(ArrayList<String> feature)
	{
		String allFeature = "";
		for(int i = 0; i<feature.size(); i++)
			allFeature += feature.get(i) + "\n";
		txtFeatures.setText(allFeature);
	}
	
	public void setStatus(boolean status)
	{
		if(status == true)
			rdbtnOn.setSelected(true);
		else rdbtnOff.setSelected(true);
	}
	
	public void setType(String type)
	{
		if(type != null){
			if(type.equals(SimpleSpellout.TYPE_PREFIX))
				rdbtnPrefix.setSelected(true);
			else if (type.equals(SimpleSpellout.TYPE_SUFFIX))
				rdbtnSuffix.setSelected(true);
			else if(type.equals(SimpleSpellout.TYPE_NEWTRANSLATION))
				rdbtnNewTranslation.setSelected(true);
			else rdbtnAddWord.setSelected(true);
		}
	}
	
	public void setTriggerWord(String triggerword){
		cmbTriggerWord.removeAllItems();
		cmbTriggerWord.addItem(triggerword);
	}

	
	public void setSubword(String subword){
		txtSubword.setText(subword);
	}
	
	public void setComment(String comment){
		txtComment.setText(comment);
	}
	
	public JTextArea getTxtFeatures() {
		return txtFeatures;
	}
	
	public String getTriggerWord(){
		return cmbTriggerWord.getSelectedItem().toString();
	}
	
	public JComboBox<String> getCmbTriggerWord(){
		return cmbTriggerWord;
	}
	
	public String getBaseForm(){
		if(cmbBaseForm.getSelectedItem() != null)
			return cmbBaseForm.getSelectedItem().toString();
		return null;
	}
	
	public void addNewTriggerWord(String newWord)
	{
		cmbTriggerWord.addItem(newWord);
	}
	
	public String getRuleName(){
		return txtRuleName.getText().trim();
	}
	
	public void setTxtGroup(String group)
	{
		this.txtGroup.setText(group);
	}
	
	public String getSelectedPOSCode(){
		return ((PartOfSpeech)cmbCategory.getSelectedItem()).getName();
	}
	
	public JComboBox<String> getCmbBaseForm() {
		return cmbBaseForm;
	}
	
	public JComboBox<PartOfSpeech> getCmbCategory() {
		return cmbCategory;
	}

	private void populateCmbCategory() {
		for(PartOfSpeech pos: ComponentManager.getInstance().getLeafPartsOfSpeech())
			cmbCategory.addItem(pos);
	}
	
	public void addBtnTriggerWordListener(ActionListener action)
	{
		btnTriggerWord.addActionListener(action);
	}
	
	public void addBtnFeaturesListener(ActionListener action)
	{
		btnFeatures.addActionListener(action);
	}
	
	public void addBtnOKListener(ActionListener action)
	{
		btnOK.addActionListener(action);
	}
	
	public void addBtnCancelListener(ActionListener action)
	{
		btnCancel.addActionListener(action);
	}
	
	public void addCategoryItemListener(ItemListener item){
		cmbCategory.addItemListener(item);
		categoryItemListener = item;
	}
	
	public void addBtnStructureListener(ActionListener action)
	{
		btnStructures.addActionListener(action);
	}
	
	public ItemListener getCmbCategoryItemListener(){
		return categoryItemListener;
	}
	
	public String getSpellOutType(){
		if(rdbtnPrefix.isSelected())
			return SimpleSpellout.TYPE_PREFIX;
		else if(rdbtnSuffix.isSelected())
			return SimpleSpellout.TYPE_SUFFIX;
		else if(rdbtnNewTranslation.isSelected())
			return SimpleSpellout.TYPE_NEWTRANSLATION;
		else if(rdbtnAddWord.isSelected())
			return SimpleSpellout.TYPE_ADDWORD;
		return null;
	}
}
