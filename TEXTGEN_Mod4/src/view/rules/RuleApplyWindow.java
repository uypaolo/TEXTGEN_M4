package view.rules;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import rules.RuleNode;
import rules.Rules;
import view.grammardevelopment.InputXMLDocumentPanel;
import view.grammardevelopment.JTreeWithScrollPane;
import view.grammardevelopment.TextAreaWithScrollPane;
import view.grammardevelopment.TreeNode;

import components.Component;
import components.InputXMLDocument;

import controller.listener.grammardev.SelectComponentActionListener;

public class RuleApplyWindow extends JFrame{
	
	TextAreaWithScrollPane infoArea;
	Rules selectedRule;
	InputXMLDocument input;
	JSplitPane splitPane;
	RuleCreationPanel toMatchStruct;
	InputXMLDocumentPanel matchedStruct;
	JPanel LeftSide;
	JPanel RightSide;
	
	SelectComponentActionListener selectListener;
	
	public RuleApplyWindow(InputXMLDocumentPanel doc){
		
		input = doc.getXMLDocument();
		
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
		//this.setResizable(true);
		Rectangle rec = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		setSize(rec.width, rec.height);
		setResizable(false);
		//this.setSize(new Dimension(700, 500));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void initComponents(){
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		this.LeftSide = new JPanel();
		this.LeftSide.setLayout(new BoxLayout(this.LeftSide, BoxLayout.Y_AXIS));
		
		this.RightSide = new JPanel();
		this.RightSide.setLayout(new BoxLayout(this.RightSide, BoxLayout.Y_AXIS));
		
		this.infoArea = new TextAreaWithScrollPane("Info");
		
		this.selectListener = new SelectComponentActionListener(this);
		
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
		this.toMatchStruct.setSelectComponentPanelListener(selectListener);
		
		this.matchedStruct = new InputXMLDocumentPanel(new InputXMLDocument(null, "", "", "", null));
		this.matchedStruct.setBorder(BorderFactory.createTitledBorder("Matched"));
		this.matchedStruct.setSelectComponentPanelListener(selectListener);
		
		this.LeftSide.add(new JScrollPane(this.toMatchStruct));
		this.LeftSide.add(new JScrollPane(this.matchedStruct));
		
		this.RightSide.add(ruleTree);
		this.RightSide.add(this.infoArea);
		
		this.splitPane.add(LeftSide);
		this.splitPane.add(RightSide);
		this.splitPane.setDividerLocation((int)(this.getWidth()*.7));
		
		this.add(this.splitPane);
	}
	
	public void selectedRuleChanged(Rules selected){
		this.toMatchStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", selected.getCopyOfInputToMatch()));
		this.toMatchStruct.setSelectComponentPanelListener(selectListener);
		//this.matchedStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", selected.getCopyOfInputToMatch()));
		this.matchedStruct.setInputXMLDoc(match(selected.getCopyOfInputToMatch()));
		this.matchedStruct.setSelectComponentPanelListener(selectListener);
	}
	
	public InputXMLDocument match(ArrayList<Component> toMatch){
		//InputXMLDocument match = new InputXMLDocument(null, "", "", "", toMatch);
		
		ArrayList<Component> in = input.getClauses();
		
		return new InputXMLDocument(null, "", "", "", in);
	}
	
	public void setInfo(Component component){
		if(component != null){
			
				StringBuilder info = new StringBuilder("Information about ");
				
				if(!component.getDescription().equals(component.getName())){
					info.append("(");
					info.append(component.getDescription());
					info.append(") ");
				}
				
				info.append(component.toString());
				info.append("\n\n");
				
				if(component.isLeaf())
					info.append("***Target Lexicon***\n"+component.toLexiconSentence()+"\n\n");
				info.append(component.getFeaturesInString(true));

				infoArea.setTextAreaContent(info.toString());
			
		}
	}

}
