package view.rules;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import managers.RuleManager;
import rules.RuleGroup;
import rules.Structural;

import components.InputXMLDocument;

import controller.listener.rules.AddRuleController;

public class RuleAddingFrame extends JFrame {
	
	JFrame ruleAddFrame = this;
	
	JPanel panLeft;
	JScrollPane scrollPaneFrom;
	JScrollPane scrollPaneTo;
	RuleCreationPanel fromPanel;
	RuleCreationPanel toPanel;
	
	RulesCreationRightPanel toolbar;
	
	public RuleAddingFrame() {
		this.setMinimumSize(new Dimension(800, 600));
        this.setSize(900,600);
        this.setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(1.0);
        getContentPane().add(splitPane);
        
        panLeft = new JPanel();
		panLeft.setLayout(new BorderLayout(0, 0));
        splitPane.setLeftComponent(panLeft);
        
		scrollPaneFrom = new JScrollPane(fromPanel);
		scrollPaneFrom.getVerticalScrollBar().setUnitIncrement(16);
		panLeft.add(scrollPaneFrom, BorderLayout.CENTER);
        
		fromPanel = new RuleCreationPanel(new InputXMLDocument(null,"","","", null));
		fromPanel.setName("FROM");
		fromPanel.setRuleMode(true);
		fromPanel.setBorder(BorderFactory.createTitledBorder("Criteria"));
		fromPanel.setPreferredSize(null);
		scrollPaneFrom.setViewportView(fromPanel);
		
		scrollPaneTo = new JScrollPane(toPanel);
		scrollPaneTo.getVerticalScrollBar().setUnitIncrement(16);
		
		toPanel = new RuleCreationPanel(new InputXMLDocument(null,"","","", null));
		toPanel.setName("TO");
		toPanel.setRuleMode(true);
		toPanel.setBorder(BorderFactory.createTitledBorder("Result"));
		toPanel.setPreferredSize(null);
		scrollPaneTo.setViewportView(toPanel);
		
		toolbar = new RulesCreationRightPanel(this);
		toolbar.setMinimumSize(new Dimension(430, 600));
		toolbar.setPreferredSize(new Dimension(430, 600));
		splitPane.setRightComponent(toolbar);
		
        this.setVisible(true);
	}
	
	public void setLeftVisible(boolean b) {
		if (b) {
			panLeft.add(scrollPaneFrom, BorderLayout.CENTER);
		}
		else {
			panLeft.remove(scrollPaneFrom);
		}
		panLeft.updateUI();
	}
	
	public void setRightVisible(boolean b) {
		if (b) {
			panLeft.add(scrollPaneTo, BorderLayout.CENTER);
			this.repaint();
			this.revalidate();
		}
		else {
			panLeft.remove(scrollPaneTo);
		}
		panLeft.updateUI();
	}
		
	
	public RulesCreationRightPanel getToolbar() {
		return toolbar;
	}
	
	public RuleCreationPanel getLeftPanel() {
		return fromPanel;
	}
	
	public RuleCreationPanel getRightPanel() {
		return toPanel;
	}
}
