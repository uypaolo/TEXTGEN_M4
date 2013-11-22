package view.rules;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import rules.RuleNode;
import rules.Rules;
import view.grammardevelopment.InputXMLDocumentPanel;
import view.grammardevelopment.JTreeWithScrollPane;
import view.grammardevelopment.TreeNode;
import components.InputXMLDocument;

public class RuleApplyWindow extends JFrame{
	
	Rules selectedRule;
	ArrayList<InputXMLDocument> input;
	JSplitPane splitPane;
	RuleCreationPanel toMatchStruct;
	InputXMLDocumentPanel matchedStruct;
	JPanel LeftSide;
	
	public RuleApplyWindow(ArrayList<InputXMLDocumentPanel> docList){
		
		input = new ArrayList<InputXMLDocument>();
		for(InputXMLDocumentPanel doc: docList){
			input.add(doc.getXMLDocument());
		}
		
		setFrame();
		initComponents();
		
	}
	
	private boolean IsApplicable(){
		if(true){
			return true;
		}
		
		else{
			return false;
		}
	}
	
	private void setFrame(){
		this.setTitle("Ang <insert Filipino translation of Frame here> Ni Justin!");
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setVisible(true);
		this.setResizable(true);
		this.setSize(new Dimension(700, 500));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void initComponents(){
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.LeftSide = new JPanel();
		this.LeftSide.setLayout(new BoxLayout(this.LeftSide, BoxLayout.Y_AXIS));
		
		JTreeWithScrollPane ruleTree = new JTreeWithScrollPane("Rules");
		JTree tree = ruleTree.getTree();
		tree.removeMouseListener(ruleTree.getMl());
		tree.removeKeyListener(ruleTree.getKl());
		tree.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (SwingUtilities.isLeftMouseButton(e)) {
					//System.out.println("ENTERED HERE");
					JTree tTree = ((JTree)e.getSource());
					int selRow = tTree.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = tTree.getPathForLocation(e.getX(), e.getY());
					tTree.setSelectionRow(selRow);
					
					if(selPath!=null){
						TreeNode currNode = (TreeNode)selPath.getLastPathComponent();
						if(currNode.isLeaf()){
							Rules temp = ((RuleNode)currNode.getNode()).getRule();
							if(temp!=selectedRule){
								selectedRule = temp;
								System.out.println(selectedRule.getRuleName());
								selectedRuleChanged(selectedRule);
							}
							
						}
					}
					
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.toMatchStruct = new RuleCreationPanel(new InputXMLDocument(null, "", "", "", null));
		this.toMatchStruct.setBorder(BorderFactory.createTitledBorder("To Match"));
		
		this.matchedStruct = new InputXMLDocumentPanel(new InputXMLDocument(null, "", "", "", null));
		this.matchedStruct.setBorder(BorderFactory.createTitledBorder("Matched"));
		
		this.LeftSide.add(this.toMatchStruct);
		this.LeftSide.add(this.matchedStruct);
		
		this.splitPane.add(LeftSide);
		this.splitPane.add(ruleTree);
		this.splitPane.setDividerLocation((int)(this.getWidth()*.7));
		
		this.add(this.splitPane);
	}
	
	public void selectedRuleChanged(Rules selected){
		this.toMatchStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", selected.getCopyOfInputToMatch()));
		this.matchedStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", selected.getCopyOfInputToMatch()));
	}

}
