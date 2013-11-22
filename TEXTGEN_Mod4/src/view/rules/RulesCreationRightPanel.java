package view.rules;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import view.grammardevelopment.editsemantics.ComponentPaletteScrollPane;

public class RulesCreationRightPanel extends JPanel{
	
	/**
	 *  HELLO
	 * 
	 * Instead of using .setVisible(false) for the buttons,
	 * try using setEnabled(false) instead.
	 * 
	 */
	
	private JFrame parentFrame;
	
	private JButton btnMove;
	private JButton btnEditConcept;
	private JButton btnEditLexicon;
	private JButton btnCreateOutput;
	private JButton btnDeleteComp;
	private JButton btnCopyComp;
	private JButton btnCopyFeature;
	
	private JButton btnOpenLog;
	private JButton btnSaveRule;
	
	private JCheckBox chkIncludeGG;
	private FeaturePaletteScrollPanelForRules featPalette;
	
	public RulesCreationRightPanel(JFrame parentFramex){
		this.parentFrame = parentFramex;
		
		setSize(500, 600);
		setBorder(BorderFactory.createTitledBorder("Tools"));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panButtons = new JPanel();
		add(panButtons, BorderLayout.NORTH);
		GridBagLayout gbl_panButtons = new GridBagLayout();
		gbl_panButtons.columnWidths = new int[] {5, 100, 5, 100, 5};
		gbl_panButtons.rowHeights = new int[] {5, 23, 5, 23, 5, 23, 5, 23, 5};
		gbl_panButtons.columnWeights = new double[]{0.0, 0.3, 0.0, 0.7};
		gbl_panButtons.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panButtons.setLayout(gbl_panButtons);
		
		btnEditLexicon = new JButton("Add/Edit Lexicon");
		GridBagConstraints gbc_btnEditLexicon = new GridBagConstraints();
		gbc_btnEditLexicon.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEditLexicon.insets = new Insets(0, 0, 0, 0);
		gbc_btnEditLexicon.gridwidth = 3;
		gbc_btnEditLexicon.gridx = 1;
		gbc_btnEditLexicon.gridy = 1;
		panButtons.add(btnEditLexicon, gbc_btnEditLexicon);
		
		btnMove = new JButton("Move");
		GridBagConstraints gbc_btnMove = new GridBagConstraints();
		gbc_btnMove.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnMove.anchor = GridBagConstraints.NORTH;
		gbc_btnMove.insets = new Insets(0, 0, 0, 0);
		gbc_btnMove.gridx = 1;
		gbc_btnMove.gridy = 3;
		panButtons.add(btnMove, gbc_btnMove);
		
		btnEditConcept = new JButton("Add/Edit Concept");
		GridBagConstraints gbc_btnEditConcept = new GridBagConstraints();
		gbc_btnEditConcept.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEditConcept.anchor = GridBagConstraints.NORTH;
		gbc_btnEditConcept.insets = new Insets(0, 0, 0, 0);
		gbc_btnEditConcept.gridx = 3;
		gbc_btnEditConcept.gridy = 3;
		panButtons.add(btnEditConcept, gbc_btnEditConcept);
		
		
		btnDeleteComp = new JButton("Delete Selected Component");
		GridBagConstraints gbc_btnDeleteComp = new GridBagConstraints();
		gbc_btnDeleteComp.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteComp.anchor = GridBagConstraints.NORTH;
		gbc_btnDeleteComp.insets = new Insets(0, 0, 0, 0);
		gbc_btnDeleteComp.gridx = 1;
		gbc_btnDeleteComp.gridy = 5;
		panButtons.add(btnDeleteComp, gbc_btnDeleteComp);
		
		btnCreateOutput = new JButton("Create Output");
		GridBagConstraints gbc_btnCreateOutput = new GridBagConstraints();
		gbc_btnCreateOutput.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCreateOutput.anchor = GridBagConstraints.NORTH;
		gbc_btnCreateOutput.insets = new Insets(0, 0, 0, 0);
		gbc_btnCreateOutput.gridx = 3;
		gbc_btnCreateOutput.gridy = 5;
		panButtons.add(btnCreateOutput, gbc_btnCreateOutput);
		
		btnCopyComp = new JButton("Copy Component");
		btnCopyComp.setEnabled(false);
		GridBagConstraints gbc_btnCopyComp = new GridBagConstraints();
		gbc_btnCopyComp.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCopyComp.anchor = GridBagConstraints.NORTH;
		gbc_btnCopyComp.insets = new Insets(0, 0, 0, 0);
		gbc_btnCopyComp.gridx = 1;
		gbc_btnCopyComp.gridy = 7;
		panButtons.add(btnCopyComp, gbc_btnCopyComp);
		
		btnCopyFeature = new JButton("Copy Feature");
		btnCopyFeature.setEnabled(false);
		GridBagConstraints gbc_btnCopyFeature = new GridBagConstraints();
		gbc_btnCopyFeature.insets = new Insets(0, 0, 0, 0);
		gbc_btnCopyFeature.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCopyFeature.anchor = GridBagConstraints.NORTH;
		gbc_btnCopyFeature.gridx = 3;
		gbc_btnCopyFeature.gridy = 7;
		panButtons.add(btnCopyFeature, gbc_btnCopyFeature);
		
		JPanel panCenter = new JPanel();
		add(panCenter);
		panCenter.setLayout(new BorderLayout(0, 0));
		
		ComponentPaletteScrollPane pane = new ComponentPaletteScrollPane();
		panCenter.add(pane, BorderLayout.CENTER);
		
		featPalette = new FeaturePaletteScrollPanelForRules();
		panCenter.add(featPalette, BorderLayout.SOUTH);
		pane.addListenersForAllButtons(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                JButton button = (JButton)e.getSource();
                TransferHandler handle = button.getTransferHandler();
                handle.exportAsDrag(button, e, TransferHandler.COPY);
            }
        });
		featPalette.setPreferredSize(new Dimension(0, 200));
		
		JPanel panBottom = new JPanel();
		add(panBottom, BorderLayout.SOUTH);
		panBottom.setLayout(new BorderLayout(0, 0));
		
		JPanel panButtonsBottom = new JPanel();
		panBottom.add(panButtonsBottom, BorderLayout.WEST);
		
		btnOpenLog = new JButton("View Log");
		btnOpenLog.setEnabled(false);
		panButtonsBottom.add(btnOpenLog);
		
		btnSaveRule = new JButton("Save Changes");
		btnSaveRule.setEnabled(false);
		panButtonsBottom.add(btnSaveRule);
		
		chkIncludeGG = new JCheckBox("Include in Grammar Generation");
		panBottom.add(chkIncludeGG, BorderLayout.EAST);
		chkIncludeGG.setSelected(true);
	}
	
	public boolean includeInGG(){
		if(chkIncludeGG.isSelected())
			return true;
		else
			return false;
	}
	
	public void setBtnOutputVisible(boolean b){
		btnCopyComp.setEnabled(b);
		btnCopyFeature.setEnabled(b);
		btnOpenLog.setEnabled(b);
		btnSaveRule.setEnabled(b);
	}
	
	public void setBtnIOName(String name){
		btnCreateOutput.setText(name);
	}
	
	public void setBtnEditLexiconListener(ActionListener listener){
		btnEditLexicon.addActionListener(listener);
	}
	
	public void setBtnViewLogListener(ActionListener listener){
		btnOpenLog.addActionListener(listener);
	}
	
	public void setBtnCopyFeatureListener(ActionListener listener){
		btnCopyFeature.addActionListener(listener);
	}
	
	public void setBtnCopyCompListener(ActionListener listener){
		btnCopyComp.addActionListener(listener);
	}
	
	public void setBtnCreateOutputListener(ActionListener listener){
		btnCreateOutput.addActionListener(listener);
	}
	
	public void setBtnSaveRuleListener(ActionListener listener){
		btnSaveRule.addActionListener(listener);
	}
	
	public void setBtnDeleteCompListener(ActionListener listener){
		btnDeleteComp.addActionListener(listener);
	}
	
	public void setBtnEditConceptListener(ActionListener listener){
		btnEditConcept.addActionListener(listener);
	}
	
	public void setBtnMoveListener(ActionListener listener){
		btnMove.addActionListener(listener);
	}
	
	public FeaturePaletteScrollPanelForRules getFeatPalette(){
		return featPalette;
	}
	
}
