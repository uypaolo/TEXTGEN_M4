package view.rules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import managers.ComponentManager;
import ontology.PartOfSpeech;
import rules.Morphophonemic;

public class MorphoSpellOutDialog extends JDialog {
	private JTextField txtAffix;
	private JTextField txtNewAffix;
	private JTextField txtStem;
	private JTextField txtGroup;
	private JTextField txtRulesName;
	private JTextArea txtFeatures; 
	private JTextArea txtComment;
	private JLabel lblAffix;
	private JLabel lblNewAffix;
	private JLabel lblStem;
	private JLabel lblNewStem;
	private JButton btnFeatures;
	private JButton btnOK; 
	private JButton btnCancel;
	private JComboBox<PartOfSpeech> cmbCategory;
	private ItemListener cmbCategoryListener;
	
	//	status radio button
	private ButtonGroup groupStatus;
	private JRadioButton rdbtnOn;
	private JRadioButton rdbtnOff;
	
	//	morpheme type radio button
	private ButtonGroup groupAffix;
	private JRadioButton rdbtnPrefix;
	private JRadioButton rdbtnSuffix;
	private JRadioButton rdbtnInfixAsSuffix;
	private JRadioButton rdbtnInfixAsPrefix ;
	
	//	affix change radio button
	private ButtonGroup groupAffixChange;
	private JRadioButton rdbtnAffixDontChange;
	private JRadioButton rdbtnAffixPartChange;
	private JRadioButton rdbtnAffixEnitreChange;
	
	// stem change radio button
	private ButtonGroup groupStemChange;
	private JRadioButton rdbtnStemDontChange;
	private JRadioButton rdbtnStemChange;
	
	private final JPanel contentPanel = new JPanel();
	private JPanel affixPanel;
	private JTextField txtNewStem;

	
	public MorphoSpellOutDialog() {
		initializeBasicComponent();
		initializeMorphemeTypeComponents();
		initializeAffixComponents();
		initializeStemComponents();
		setCorrespondingLabel();
		revalidate();
		repaint();
	}
	
	public void setAffixLabel(String affixName, String newAffixName)
	{
		lblAffix.setText(affixName + ":");
		if(newAffixName == null)
		{
			lblNewAffix.setVisible(false);
			txtNewAffix.setVisible(false);
			return;
		}
		lblNewAffix.setText(newAffixName +":");
		lblNewAffix.setVisible(true);
		txtNewAffix.setVisible(true);
	}
	
	public boolean getStatus(){
		if(rdbtnOn.isSelected())
			return true;
		return false;
	}
	
	public void setStatus(boolean value){
		if(value == true)
			rdbtnOn.setSelected(true);
		else rdbtnOff.setSelected(false);
	}
	
	public void setStemLabel(String stemName, String newStemName)
	{
		lblStem.setText(stemName + ":");
		if(newStemName == null)
		{
			lblNewStem.setVisible(false);
			txtNewStem.setVisible(false);
			return;
		}
		
		lblNewStem.setText(newStemName +":");
		lblNewStem.setVisible(true);
		txtNewStem.setVisible(true);
	}
	
	public void setRadioButtonsName(String affixName, String affixPart)
	{
		rdbtnAffixDontChange.setText(affixName + " doesn't change");
		rdbtnAffixPartChange.setText(affixPart + " of " + affixName + " changes");
		rdbtnAffixEnitreChange.setText("Entire " + affixName + " changes");
		affixPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), affixName));
	}
	
	public void setCorrespondingLabel()
	{
		
		if(groupAffix.isSelected(rdbtnPrefix.getModel()))
		{
			/* set for affix labels */
			if(groupAffixChange.isSelected(rdbtnAffixDontChange.getModel()))
				setAffixLabel("Prefix", null);
			
			else if (groupAffixChange.isSelected(rdbtnAffixPartChange.getModel()))
				setAffixLabel("End of Prefix", "New End of Prefix");
			
			else if (groupAffixChange.isSelected(rdbtnAffixEnitreChange.getModel()))
				setAffixLabel("Prefix", "New Prefix");
			
			/* set for stem labels */
			if(groupStemChange.isSelected(rdbtnStemDontChange.getModel()))
				setStemLabel("Beginning of Stem", null);
			
			else
				setStemLabel("Beginning of Stem", "New Beginning of Stem");
			
		}
		
		else if(groupAffix.isSelected(rdbtnSuffix.getModel()))
		{
			/* set for affix labels */
			if(groupAffixChange.isSelected(rdbtnAffixDontChange.getModel()))
				setAffixLabel("Suffix", null);
			
			else if (groupAffixChange.isSelected(rdbtnAffixPartChange.getModel()))
				setAffixLabel("Beginning of Suffix", "New Beginning of Suffix");
			
			else if (groupAffixChange.isSelected(rdbtnAffixEnitreChange.getModel()))
				setAffixLabel("Suffix", "New Suffix");
			
			/* set for stem labels */
			if(groupStemChange.isSelected(rdbtnStemDontChange.getModel()))
				setStemLabel("End of Stem", null);
			else 
				setStemLabel("End of Stem", "New End of Stem");
		}
		
		else if(groupAffix.isSelected(rdbtnInfixAsPrefix.getModel()))
		{
			/* set for affix labels */
			if(groupAffixChange.isSelected(rdbtnAffixDontChange.getModel()))
				setAffixLabel("Infix", null);
			
			else if (groupAffixChange.isSelected(rdbtnAffixPartChange.getModel()))
				setAffixLabel("End of Infix", "New End of Infix");
			
			else if (groupAffixChange.isSelected(rdbtnAffixEnitreChange.getModel()))
				setAffixLabel("Infix", "New Infix");
			
			/* set for stem labels */
			if(groupStemChange.isSelected(rdbtnStemDontChange.getModel()))
				setStemLabel("Beginning of Stem", null);
			
			else
				setStemLabel("Beginning of Stem", "New Beginning of Stem");
		}
		
		else if(groupAffix.isSelected(rdbtnInfixAsSuffix.getModel()))
		{
			/* set for affix labels */
			if(groupAffixChange.isSelected(rdbtnAffixDontChange.getModel()))
				setAffixLabel("Suffix", null);
			
			else if (groupAffixChange.isSelected(rdbtnAffixPartChange.getModel()))
				setAffixLabel("Beginning of Infix", "New Beginning of Infix");
			
			else if (groupAffixChange.isSelected(rdbtnAffixEnitreChange.getModel()))
				setAffixLabel("Infix", "New Infix");
			
			/* set for stem labels */
			if(groupStemChange.isSelected(rdbtnStemDontChange.getModel()))
				setStemLabel("End of Stem", null);
			else 
				setStemLabel("End of Stem", "New End of Stem");
		}
	}
	
	public void populateCategory()
	{
		for(PartOfSpeech pos: ComponentManager.getInstance().getLeafPartsOfSpeech())
			cmbCategory.addItem(pos);
	}
	
	public void initializeStemComponents()
	{
		/*	Setup StemPanel */
		JPanel stemPanel = new JPanel();
		stemPanel.setBounds(348, 235, 276, 206);
		stemPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Stem"));
		contentPanel.add(stemPanel);
		stemPanel.setLayout(null);
		
		lblStem = new JLabel("Beginning of Stem:");
		lblStem.setBounds(0, 136, 146, 15);
		stemPanel.add(lblStem);
		lblStem.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblNewStem = new JLabel("New Beginning of Stem:");
		lblNewStem.setBounds(-10, 166, 156, 15);
		stemPanel.add(lblNewStem);
		lblNewStem.setHorizontalAlignment(SwingConstants.RIGHT);
		
		
		groupStemChange = new ButtonGroup();
		rdbtnStemDontChange = new JRadioButton("Stem Doesn't Change");
		rdbtnStemDontChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCorrespondingLabel();
			}
		});
		rdbtnStemDontChange.setBounds(68, 21, 186, 23);
		stemPanel.add(rdbtnStemDontChange);
		
		rdbtnStemChange = new JRadioButton("Stem Changes");
		rdbtnStemChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCorrespondingLabel();
			}
		});
		rdbtnStemChange.setBounds(68, 56, 121, 23);
		stemPanel.add(rdbtnStemChange);
		groupStemChange.add(rdbtnStemDontChange);
		groupStemChange.add(rdbtnStemChange);
		groupStemChange.setSelected(rdbtnStemDontChange.getModel(), true);
		
		txtStem = new JTextField();
		txtStem.setBounds(158, 133, 113, 21);
		stemPanel.add(txtStem);
		txtStem.setColumns(10);
		
		txtNewStem = new JTextField();
		txtNewStem.setBounds(158, 163, 113, 21);
		stemPanel.add(txtNewStem);
		txtNewStem.setColumns(10);
	}
	
	public void initializeAffixComponents()
	{
		/* Setup Affix Panel*/
		affixPanel = new JPanel();
		affixPanel.setBounds(10, 235, 328, 206);
		affixPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Prefix"));
		contentPanel.add(affixPanel);
		affixPanel.setLayout(null);
		
		lblAffix = new JLabel("Prefix:");
		lblAffix.setBounds(20, 137, 131, 15);
		affixPanel.add(lblAffix);
		lblAffix.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblNewAffix = new JLabel("New Prefix:");
		lblNewAffix.setBounds(10, 165, 141, 15);
		affixPanel.add(lblNewAffix);
		lblNewAffix.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtNewAffix = new JTextField();
		txtNewAffix.setBounds(162, 162, 113, 21);
		affixPanel.add(txtNewAffix);
		txtNewAffix.setColumns(10);
		
		txtAffix = new JTextField();
		txtAffix.setBounds(162, 134, 113, 21);
		affixPanel.add(txtAffix);
		txtAffix.setColumns(10);
		
		groupAffixChange = new ButtonGroup();
		
		rdbtnAffixDontChange = new JRadioButton("Prefix doesn't change");
		rdbtnAffixDontChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCorrespondingLabel();
			}
		});
		rdbtnAffixDontChange.setBounds(77, 31, 194, 23);
		affixPanel.add(rdbtnAffixDontChange);
		
		rdbtnAffixPartChange = new JRadioButton("Prefix part changes");
		rdbtnAffixPartChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCorrespondingLabel();
			}
		});
		rdbtnAffixPartChange.setBounds(77, 65, 194, 23);
		affixPanel.add(rdbtnAffixPartChange);
		
		rdbtnAffixEnitreChange = new JRadioButton("Entire prefix changes");
		rdbtnAffixEnitreChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCorrespondingLabel();
			}
		});
		rdbtnAffixEnitreChange.setBounds(77, 96, 194, 23);
		affixPanel.add(rdbtnAffixEnitreChange);
		
		
		groupAffixChange.add(rdbtnAffixDontChange);
		groupAffixChange.add(rdbtnAffixEnitreChange);
		groupAffixChange.add(rdbtnAffixPartChange);
		groupAffixChange.setSelected(rdbtnAffixDontChange.getModel(), true);
	}
	
	public void initializeMorphemeTypeComponents()
	{

		/* Setup Morpheme Type radio button */
		JPanel morphemeTypePanel = new JPanel();
		morphemeTypePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Morpheme Type"));
		morphemeTypePanel.setBounds(10, 144, 614, 81);
		contentPanel.add(morphemeTypePanel);
		morphemeTypePanel.setLayout(null);
		
		groupAffix = new ButtonGroup();
		
		rdbtnPrefix = new JRadioButton("Prefix");
		rdbtnPrefix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			 	setRadioButtonsName("Prefix", "End");
			 	setCorrespondingLabel();
			}
		});
		
		rdbtnPrefix.setBounds(137, 16, 121, 23);
		morphemeTypePanel.add(rdbtnPrefix);
		
		rdbtnSuffix = new JRadioButton("Suffix");
		rdbtnSuffix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRadioButtonsName("Suffix", "Beginning");
				setCorrespondingLabel();
			}
		});
		rdbtnSuffix.setBounds(137, 41, 121, 23);
		morphemeTypePanel.add(rdbtnSuffix);
		
		rdbtnInfixAsSuffix = new JRadioButton("Infix as Suffix");
		rdbtnInfixAsSuffix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRadioButtonsName("Infix", "Beginning");
				setCorrespondingLabel();
			}
		});
		rdbtnInfixAsSuffix.setBounds(301, 41, 121, 23);
		morphemeTypePanel.add(rdbtnInfixAsSuffix);
		
		rdbtnInfixAsPrefix = new JRadioButton("Infix as Prefix");
		rdbtnInfixAsPrefix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setRadioButtonsName("Infix", "End");
				setCorrespondingLabel();
			}
		});
		rdbtnInfixAsPrefix.setBounds(301, 16, 121, 23);
		morphemeTypePanel.add(rdbtnInfixAsPrefix);
		
		groupAffix.add(rdbtnPrefix);
		groupAffix.add(rdbtnSuffix);
		groupAffix.add(rdbtnInfixAsSuffix);
		groupAffix.add(rdbtnInfixAsPrefix);
		groupAffix.setSelected(rdbtnPrefix.getModel(), true);
	}
	
	public void initializeBasicComponent()
	{
		/* Setup basic components */
		setTitle("Morphophonemic Spell Out Rule");
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

		cmbCategory = new JComboBox<PartOfSpeech>();
		cmbCategory.setBounds(130, 23, 126, 21);
		contentPanel.add(cmbCategory);
		populateCategory();
		
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setBounds(297, 26, 46, 14);
		contentPanel.add(lblGroup);
		
		txtGroup = new JTextField();
		txtGroup.setEditable(false);
		txtGroup.setBounds(337, 22, 143, 23);
		contentPanel.add(txtGroup);
		txtGroup.setColumns(10);
		
		JLabel lblRuleName = new JLabel("Rule's Name:");
		lblRuleName.setBounds(48, 51, 88, 14);
		contentPanel.add(lblRuleName);
		
		txtRulesName = new JTextField();
		txtRulesName.setColumns(10);
		txtRulesName.setBounds(130, 50, 494, 23);
		contentPanel.add(txtRulesName);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBounds(192, 77, 236, 59);
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
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(130, 451, 494, 58);
		contentPanel.add(scrollPane_1);
		
		txtFeatures = new JTextArea();
		txtFeatures.setFont(new Font("Monospaced", Font.PLAIN, 10));
		txtFeatures.setBackground(UIManager.getColor("Button.background"));
		txtFeatures.setEditable(false);
		scrollPane_1.setViewportView(txtFeatures);
		
		btnFeatures = new JButton("Features");
		btnFeatures.setBounds(10, 464, 110, 23);
		contentPanel.add(btnFeatures);
		
		JLabel lblComment = new JLabel("Comment:");
		lblComment.setFont(new Font("SimSun", Font.PLAIN, 13));
		lblComment.setBounds(15, 519, 105, 14);
		contentPanel.add(lblComment);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 543, 614, 59);
		contentPanel.add(scrollPane);
		
		txtComment = new JTextArea();
		scrollPane.setViewportView(txtComment);
		
		btnOK = new JButton("OK");
		btnOK.setBounds(388, 627, 89, 23);
		getRootPane().setDefaultButton(btnOK);
		contentPanel.add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(490, 627, 89, 23);
		contentPanel.add(btnCancel);
		
		populateCategory();
	}

	public void setTxtGroup(String name) {
		txtGroup.setText(name);
	}

	public String getSelectedPOSCode() {
		return ((PartOfSpeech)cmbCategory.getSelectedItem()).getName();
	}
	
	public void addFeaturesBtnListener(ActionListener act)
	{
		btnFeatures.addActionListener(act);
	}
	
	public void addOKBtnListener(ActionListener act)
	{
		btnOK.addActionListener(act);
	}
	
	public void addCancelBtnListener(ActionListener act)
	{
		btnCancel.addActionListener(act);
	}
	
	public void setFeature(String feature){
		txtFeatures.setText(feature);
	}
	
	public void updateFeatureDisplay(ArrayList<String> feature)
	{
		String allFeature = "";
		for(int i = 0; i<feature.size(); i++)
			allFeature += feature.get(i) + "\n";
		txtFeatures.setText(allFeature);
	}
	
	public void setMorphemeType(String type){
		if(type.equals(Morphophonemic.MORPHEME_TYPE_PREFIX))
			rdbtnPrefix.setSelected(true);
		else if(type.equals(Morphophonemic.MORPHEME_TYPE_SUFFIX))
			rdbtnSuffix.setSelected(true);
	}
	
	public void setChangeType(String type){
		if(type.equals(Morphophonemic.AFFIX_CHANGE_TYPE_DOESNT_CHANGE))
			rdbtnAffixDontChange.setSelected(true);
		else if(type.equals(Morphophonemic.AFFIX_CHANGE_TYPE_PART_CHANGES))
			rdbtnAffixPartChange.setSelected(true);
		else if(type.equals(Morphophonemic.AFFIX_CHANGE_TYPE_ENTIRE_CHANGES))
			rdbtnAffixEnitreChange.setSelected(true);
	}
	
	public void setStemType(String type){
		if(type.equals(Morphophonemic.STEM_CHANGE_TYPE_DOESNT_CHANGE))
			rdbtnStemDontChange.setSelected(true);
		else if(type.equals(Morphophonemic.STEM_CHANGE_TYPE_ENTIRE_CHANGES))
			rdbtnStemChange.setSelected(true);
	}
	
	public void setOldValue(String value){
		txtAffix.setText(value);
	}
	
	public void setNewValue(String value){
		txtNewAffix.setText(value);
	}
	
	public void setStemBeginOrEnd(String value){
		txtStem.setText(value);
	}
	
	public void setStemNewBeginOrEnd(String value){
		txtNewStem.setText(value);
	}
	
	public void setDescription(String value){
		txtComment.setText(value);
	}
	
	public void setTxtRulesName(String value){
		txtRulesName.setText(value);
	}

	
	public String getMorphemeType(){
		if (rdbtnPrefix.isSelected() == true) 
			return Morphophonemic.MORPHEME_TYPE_PREFIX;
		else if (rdbtnSuffix.isSelected() == true)
			return Morphophonemic.MORPHEME_TYPE_SUFFIX;
		return null;
	}
	
	public String getChangeType(){
		if (rdbtnAffixDontChange.isSelected() == true) 
			return Morphophonemic.AFFIX_CHANGE_TYPE_DOESNT_CHANGE;
		else if (rdbtnAffixPartChange.isSelected() == true)
			return Morphophonemic.AFFIX_CHANGE_TYPE_PART_CHANGES;
		else if (rdbtnAffixEnitreChange.isSelected() == true)
			return Morphophonemic.AFFIX_CHANGE_TYPE_ENTIRE_CHANGES;
		return null;
	}
	
	public String getStemType(){
		if (rdbtnStemDontChange.isSelected() == true) 
			return  Morphophonemic.STEM_CHANGE_TYPE_DOESNT_CHANGE;
		else if (rdbtnStemChange.isSelected() == true)
			return Morphophonemic.STEM_CHANGE_TYPE_ENTIRE_CHANGES;
		return null;
	}
	
	public String getOldValue(){
		return txtAffix.getText();
	}
	
	public String getNewValue(){
		return txtNewAffix.getText();
	}
	
	public String getStemBeginOrEnd(){
		return txtStem.getText();
	}
	
	public String getStemNewBeginOrEnd(){
		return txtNewStem.getText();
	}
	
	public String getDescription(){
		return txtComment.getText();
	}

	public String getTxtRulesName(){
		return txtRulesName.getText();
	}
	
	public void addCategoryItemListener(ItemListener item) {
		cmbCategory.addItemListener(item);
		cmbCategoryListener = item;
	}
	
	public ItemListener getcmbCategoryListener() {
		return cmbCategoryListener;
	}

	public void setSelectedItemCmbCategory(PartOfSpeech pos){
		cmbCategory.removeItemListener(cmbCategoryListener);
		cmbCategory.setSelectedItem(pos);
		
		cmbCategory.addItemListener(cmbCategoryListener);
	}
}
