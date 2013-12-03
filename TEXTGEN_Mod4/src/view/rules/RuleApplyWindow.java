package view.rules;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import components.Children;
import components.Component;
import components.InputXMLDocument;
import components.Leaf;
import components.Phrase;

import controller.listener.grammardev.SelectComponentActionListener;
import features.Feature;

public class RuleApplyWindow extends JFrame{
	
	TextAreaWithScrollPane infoArea;
	Rules selectedRule;
	InputXMLDocument input;
	ArrayList<Component> affectedComp;
	JSplitPane splitPane;
	RuleCreationPanel toMatchStruct;
	InputXMLDocumentPanel matchedStruct;
	JPanel LeftSide;
	JPanel RightSide;
	JLabel isApplicable;
	boolean applicable = false;
	
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
		
		this.isApplicable = new JLabel("No Rule Selected");
		
		this.LeftSide.add(new JScrollPane(this.toMatchStruct));
		this.LeftSide.add(new JScrollPane(this.matchedStruct));
		this.LeftSide.add(this.isApplicable);
		
		this.RightSide.add(ruleTree);
		this.RightSide.add(this.infoArea);
		
		this.splitPane.add(LeftSide);
		this.splitPane.add(RightSide);
		this.splitPane.setDividerLocation((int)(this.getWidth()*.7));
		
		this.add(this.splitPane);
	}
	
	public void selectedRuleChanged(Rules selected){
		this.affectedComp = new ArrayList<Component>();
		this.applicable = false;
		this.toMatchStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", selected.getCopyOfInputToMatch()));
		this.toMatchStruct.setSelectComponentPanelListener(selectListener);
		//this.matchedStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", selected.getCopyOfInputToMatch()));
		
		
		removeAffected(input.getClauses());
		
		getMatch(input.getClauses(),selected.getCopyOfInputToMatch());
		
		this.matchedStruct.setInputXMLDoc(new InputXMLDocument(null, "", "", "", input.getClauses()));
		this.matchedStruct.setSelectComponentPanelListener(selectListener);
		if(applicable){
			System.out.println("jahsdkarnb!!");
			this.isApplicable.setText("Applicable");
		}
		else{
			this.isApplicable.setText("Not Applicable");
		}
	}
	
	public boolean getMatch(ArrayList<Component> struct, ArrayList<Component> toMatch){
		
			
			if(!applicable){
				this.applicable = phase1(struct, toMatch);
			}
			else{
				System.out.println("MATCHED!!");
				phase1(struct, toMatch);
			}
			
			for(int j=0; j<struct.size(); j++){			
				if(!struct.get(j).isLeaf()){
					getMatch(struct.get(j).getChildren().getChildren(), toMatch);
				}
			}
		
		
		return true;
		
	}
	
	public void removeAffected(ArrayList<Component> struct){
		for(Component c: struct){
			c.setAffected("");
			if(!c.isLeaf()){
				removeAffected(c.getChildren().getChildren());
			}
		}
		
	}
	
	public boolean phase1(ArrayList<Component> struct, ArrayList<Component> toMatch){
		int j=0;
		int i=0;
		Phrase temp;
		Children tempch;
		boolean matched = false;
		System.out.println(struct.size());
		for(i=0; i<toMatch.size() && j<struct.size();i++){	
			for(; j<struct.size(); j++){
				System.out.println("BeforeCheck "+struct.get(j).getName()+"  "+toMatch.get(i).getName());
				if(toMatch.get(i).getName().equalsIgnoreCase(struct.get(j).getName()) && featureMatch(struct.get(j).getFeatures(), toMatch.get(i).getFeatures())){
					System.out.println(toMatch.get(i).getName()+"    "+struct.get(j).getName());
					if(!toMatch.get(i).isLeaf()){
						if(phase1(struct.get(j).getChildren().getChildren(), toMatch.get(i).getChildren().getChildren())){
							matched = true;
							struct.get(j).setAffected("*MATCHED*");
							//i++;
							//j++;
							//break;
						}
					}
					else{
						if(((Leaf)toMatch.get(i)).getConcept().equalsIgnoreCase("")){
							System.out.println("EMPTY CONCEPT");
							matched = true;
							struct.get(j).setAffected("*MATCHED*");
						}
						else if(((Leaf)toMatch.get(i)).getConcept().equalsIgnoreCase(((Leaf)struct.get(j)).getConcept())){
							matched = true;
							struct.get(j).setAffected("*MATCHED*");
						}
						//i++;
						//j++;
						//break;
					}
				}
			}
			
		}
		//System.out.println(i+"      "+toMatch.size());
		if(toMatch.size()==0){
			return true;
		}
		if(i>=toMatch.size() && matched){
			//System.out.println(i+"      "+toMatch.size());
			return true;
		}
		
		return false;
	}
	
	public boolean featureMatch(ArrayList<Feature> struct, ArrayList<Feature> toMatch){
		
		Feature currFeature;
		ArrayList<Feature> checked = new ArrayList<Feature>();
		
		for(Feature f: toMatch){
			if(!inChecked(checked, f)){
				for(Feature fs: struct){
					if(f.getName().equalsIgnoreCase(fs.getName()) && f.getValue().equalsIgnoreCase(fs.getValue())){
						checked.add(f);
					}
				}
			}
			else{
				checked.add(f);
			}
		}
		
		if(checked.size() == toMatch.size()){
			return true;
		}
		
		return false;
	}
	
	public boolean inChecked(ArrayList<Feature> checked, Feature f){
		for(Feature fs: checked){
			if(f.getName().equalsIgnoreCase(fs.getName())){
				return true;
			}
		}
		
		return false;
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
