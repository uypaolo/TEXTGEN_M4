package view.rules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import managers.ComponentManager;
import ontology.PartOfSpeech;

public class FormSelectDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnStructures;
	private JButton btnFeatures;
	private JComboBox<String> cmbBaseForm; 

	private JComboBox<PartOfSpeech> cmbCategory; 
	private JTextArea txtFeatures;
	private JTextArea txtComment; 
	private JButton btnOK;
	private JButton btnCancel;
	private JTextField txtRuleName;
	private JTextField txtGroup;
	private ButtonGroup groupStatus;
	private JRadioButton rdbtnOn;
	private JRadioButton rdbtnOff;
	private ItemListener cmbCategoryListener;
	
	public JRadioButton getrdbtnOn()
	{
		return rdbtnOn;
	}
	
	public void setStatus(boolean value){
		if(value == true)
			rdbtnOn.setSelected(true);
		else rdbtnOff.setSelected(false);
	}
	
	public void setDesireForm(String item){
		cmbBaseForm.removeAllItems();
		cmbBaseForm.addItem(item);
	}
	
	public String getComment()
	{
		return txtComment.getText();
	}
	
	public String getSelectedPOSCode(){
		return ((PartOfSpeech)cmbCategory.getSelectedItem()).getName();
	}
	
	public void setTxtRuleName(String RuleName) {
		txtRuleName.setText(RuleName);
	}

	public void setTxtGroup(String GroupName) {
		txtGroup.setText(GroupName);
	}

	public String getCategory()
	{
		return cmbCategory.getSelectedItem().toString();
	}
	
	public JTextField getTxtRuleName() {
		return txtRuleName;
	}

	public JComboBox<PartOfSpeech> getCmbCategory() {
		return cmbCategory;
	}

	public void setCmbCategory(JComboBox<PartOfSpeech> cmbCategory) {
		this.cmbCategory = cmbCategory;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(JButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	public PartOfSpeech getSelectedPOS(){
		return (PartOfSpeech)cmbCategory.getSelectedItem();
	}
	
	public String getRuleName(){
		return txtRuleName.getText().trim();
	}
	
	public String getBaseForm(){
		if(cmbBaseForm.getSelectedItem() != null)
			return cmbBaseForm.getSelectedItem().toString();
		return null;
	}
	
	public void updateFeatureDisplay(ArrayList<String> feature)
	{
		String allFeature = "";
		for(int i = 0; i<feature.size(); i++)
			allFeature += feature.get(i) + "\n";
		txtFeatures.setText(allFeature);
	}
	
	// constructor given: object 
	public FormSelectDialog() {
		setLocationRelativeTo(null);
		setTitle("Form Selection");
		setBounds(100, 100, 604, 654);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		btnStructures = new JButton("Structures");
		btnStructures.setBounds(32, 195, 110, 29);
		contentPanel.add(btnStructures);
		
		JLabel lblSyntacticCategory = new JLabel("Syntactic Category:");
		lblSyntacticCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSyntacticCategory.setBounds(-11, 37, 163, 15);
		contentPanel.add(lblSyntacticCategory);
		
		JLabel lblGroup = new JLabel("Group:");
		lblGroup.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGroup.setBounds(304, 40, 66, 15);
		contentPanel.add(lblGroup);
		
		JLabel lblRuleName = new JLabel("Rule's Name:");
		lblRuleName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRuleName.setBounds(25, 80, 127, 15);
		contentPanel.add(lblRuleName);
		
		txtRuleName = new JTextField();
		txtRuleName.setColumns(10);
		txtRuleName.setBounds(179, 77, 320, 20);
		contentPanel.add(txtRuleName);
		
		txtGroup = new JTextField();
		txtGroup.setEditable(false);
		txtGroup.setColumns(10);
		txtGroup.setBounds(380, 37, 119, 20);
		contentPanel.add(txtGroup);

		//syntacticcategory + group text based on object
		
		JLabel lblBaseForm = new JLabel("Base Form:");
		lblBaseForm.setFont(new Font("SimSun", Font.PLAIN, 13));
		lblBaseForm.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBaseForm.setBounds(32, 234, 110, 36);
		contentPanel.add(lblBaseForm);
		
		cmbBaseForm = new JComboBox<String>();
		cmbBaseForm.setBounds(166, 238, 390, 29);
		contentPanel.add(cmbBaseForm);
		
		btnFeatures = new JButton("Features");
		btnFeatures.setBounds(32, 280, 110, 29);
		contentPanel.add(btnFeatures);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(166, 285, 390, 166);
		contentPanel.add(scrollPane);
		
		txtFeatures = new JTextArea();
		txtFeatures.setFont(new Font("SimSun", Font.PLAIN, 11));
		txtFeatures.setEditable(false);
		scrollPane.setViewportView(txtFeatures);
		
		JLabel lblComment = new JLabel("Comment:");
		lblComment.setFont(new Font("SimSun", Font.PLAIN, 13));
		lblComment.setBounds(32, 434, 83, 29);
		contentPanel.add(lblComment);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(32, 461, 524, 78);
		contentPanel.add(scrollPane_1);
		
		txtComment = new JTextArea();
		scrollPane_1.setViewportView(txtComment);
		
		btnOK = new JButton("OK");
		btnOK.setBounds(304, 565, 93, 23);
		contentPanel.add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(409, 565, 93, 23);
		contentPanel.add(btnCancel);
		
		cmbCategory = new JComboBox<PartOfSpeech>();
		cmbCategory.setBounds(179, 34, 117, 21);
		contentPanel.add(cmbCategory);
		
		JPanel statusPanel = new JPanel();
		statusPanel.setBounds(179, 118, 241, 53);
		contentPanel.add(statusPanel);
		statusPanel.setLayout(null);
		statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Status"));
		
		groupStatus = new ButtonGroup();
		rdbtnOn = new JRadioButton("On");
		rdbtnOn.setBounds(47, 15, 43, 23);
		statusPanel.add(rdbtnOn);
		
		rdbtnOff = new JRadioButton("Off");
		rdbtnOff.setBounds(139, 15, 63, 23);
		statusPanel.add(rdbtnOff);
		groupStatus.add(rdbtnOn);
		groupStatus.add(rdbtnOff);

		populateCmbCategory();
		
		revalidate();
		repaint();
	}

	public ItemListener getcmbCategoryListener() {
		return cmbCategoryListener;
	}

	public void setSelectedItemCmbCategory(PartOfSpeech pos){
		cmbCategory.removeItemListener(cmbCategoryListener);
		cmbCategory.setSelectedItem(pos);
		
		cmbCategory.addItemListener(cmbCategoryListener);
	}


	private void populateCmbCategory() {
		for(PartOfSpeech pos: ComponentManager.getInstance().getLeafPartsOfSpeech())
			cmbCategory.addItem(pos);
	}


	public JComboBox<String> getCmbBaseForm() {
		return cmbBaseForm;
	}

	public void setCmbBaseForm(JComboBox<String> cmbBaseForm) {
		this.cmbBaseForm = cmbBaseForm;
	}

	public JTextArea getTxtFeatures() {
		return txtFeatures;
	}

	public void setTxtFeatures(JTextArea txtFeatures) {
		this.txtFeatures = txtFeatures;
	}

	public JTextArea getTxtComment() {
		return txtComment;
	}

	public void setTxtComment(JTextArea txtComment) {
		this.txtComment = txtComment;
	}
	

	public void addStructuresBtnListener(ActionListener act)
	{
		btnStructures.addActionListener(act);
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

	public void addCategoryItemListener(ItemListener item) {
		cmbCategory.addItemListener(item);
		cmbCategoryListener = item;
	}
}
