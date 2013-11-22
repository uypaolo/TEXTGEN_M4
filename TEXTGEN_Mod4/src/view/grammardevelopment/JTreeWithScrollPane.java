package view.grammardevelopment;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.management.modelmbean.ModelMBean;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import managers.RuleManager;
import rules.RuleGroup;
import rules.RuleNode;
import rules.Rules;
import rules.Structural;
import view.MainFrame;
import controller.listener.rules.AddRuleController;
import controller.listener.rules.FormSelectionController;
import controller.listener.rules.MorphoSpellOutController;
import controller.listener.rules.SimpleSpellOutController;

public class JTreeWithScrollPane extends JScrollPane{
	
	private JTree tree;
	int selRow;
	public KeyListener getKl() {
		return kl;
	}

	public void setKl(KeyListener kl) {
		this.kl = kl;
	}

	public MouseListener getMl() {
		return ml;
	}

	public void setMl(MouseListener ml) {
		this.ml = ml;
	}

	TreePath selPath;
	KeyListener kl;
	MouseListener ml;
	
	public JTreeWithScrollPane(String title){
		tree = new JTree();
		setPreferredSize(new Dimension(MainFrame.getInstance().getWidth()*2/5, MainFrame.getInstance().getHeight()/2 ));
		setBorder(BorderFactory.createTitledBorder(title));
		getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);


	    getTree().setCellRenderer(new CustomIconRenderer());
	    
	    
		
		
				
	    kl = new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\u007F'){
					if(selPath == null)
						JOptionPane.showMessageDialog(null, "No tree node selected to delete.",
							    "Error", JOptionPane.ERROR_MESSAGE);
					else
						deleteNode(selPath);
				}
			}
		};
		getTree().addKeyListener(kl);
		
		ml = new MouseAdapter() {
		
		    public void mouseClicked(MouseEvent e) {
		        if (SwingUtilities.isRightMouseButton(e)) {
			        selRow = getTree().getRowForLocation(e.getX(), e.getY());
			        selPath = getTree().getPathForLocation(e.getX(), e.getY());
	                JPopupMenu popUpMenu = new JPopupMenu();
		            getTree().setSelectionRow(selRow);
		            
		            
		            ActionListener actionListener = new ActionListener(){
		            	public void actionPerformed(ActionEvent e)
		                {
		            		if(e.getActionCommand().equalsIgnoreCase("Add New Rule"))
		            			addNewRule((TreeNode)selPath.getLastPathComponent());
		            		else if(e.getActionCommand().equalsIgnoreCase("Add New Group"))
		            			addNewGroup((TreeNode)selPath.getLastPathComponent());
		            		else if(e.getActionCommand().equalsIgnoreCase("Delete Group") || e.getActionCommand().equalsIgnoreCase("Delete Rule"))
								deleteNode(selPath);
		            		else if(e.getActionCommand().equalsIgnoreCase("Edit Group Name"))
								editGroupName((TreeNode)selPath.getLastPathComponent());
		            		else if(e.getActionCommand().equalsIgnoreCase("Move Group") || e.getActionCommand().equalsIgnoreCase("Move Rule") )
								moveNode(selPath);
		            		else if(e.getActionCommand().equalsIgnoreCase("Edit Rule") )
								editRule((TreeNode)selPath.getLastPathComponent());
		                }
		            };
		            TreeNode currNode = (TreeNode)selPath.getLastPathComponent(); 
	                if(!currNode.getNode().isLeaf()){
			            JMenuItem menuRule = new JMenuItem("Add New Rule");
			            menuRule.addActionListener(actionListener);
			            popUpMenu.add(menuRule);
			            JMenuItem menuGroup = new JMenuItem("Add New Group");
			            menuGroup.addActionListener(actionListener);
			            popUpMenu.add(menuGroup);
			            JMenuItem menuEdit = new JMenuItem("Edit Group Name");
			            menuEdit.addActionListener(actionListener);
			            popUpMenu.add(menuEdit);
			            JMenuItem menuDelete = new JMenuItem("Delete Group");
			            menuDelete.addActionListener(actionListener);
			            popUpMenu.add(menuDelete);
			            JMenuItem menuMove = new JMenuItem("Move Group");
			            menuMove.addActionListener(actionListener);
			            popUpMenu.add(menuMove);
			            popUpMenu.show(e.getComponent(), e.getX(), e.getY());
	                }
	                else{
	                    JMenuItem menuEdit = new JMenuItem("Edit Rule");
	                    menuEdit.addActionListener(actionListener);
			            popUpMenu.add(menuEdit);
			            JMenuItem menuDelete = new JMenuItem("Delete Rule");
			            menuDelete.addActionListener(actionListener);
			            popUpMenu.add(menuDelete);
			            JMenuItem menuMove = new JMenuItem("Move Rule");
			            menuMove.addActionListener(actionListener);
			            popUpMenu.add(menuMove);
			            popUpMenu.show(e.getComponent(), e.getX(), e.getY());	
	                }
		        }
		        else{
		        	selRow = getTree().getRowForLocation(e.getX(), e.getY());
			        selPath = getTree().getPathForLocation(e.getX(), e.getY());
		        }
		    }
		};
		
		getTree().addMouseListener(ml);
		
		getTree().setModel(new DefaultTreeModel(TreeNode.createInstance(RuleManager.getInstance().getRootNode())));
		JPanel panel = new JPanel();
		panel.add(getTree());
		setViewportView(getTree());
		add(panel);
	}

	private void editRule(TreeNode node) {
		RuleNode rulenode = (RuleNode)node.getNode();
		Rules rule = rulenode.getRule();
		
		if(rule.getType().equals(Rules.SIMPLE_SPELLOUT))
			new SimpleSpellOutController(getTree(), node, rule);
		else if(rule.getType().equals(Rules.MORPHOPHONEMIC))
			new MorphoSpellOutController(getTree(), node, rule);
		else if(rule.getType().equals(Rules.FORM_SELECTION))
			new FormSelectionController(getTree(), node, rule);
		else if(rule.getType().equals(Rules.STRUCTURAL)){
			new AddRuleController(getTree(), node, rule.getRuleName(),"EDIT");
		}
	}

	private void editGroupName(TreeNode node) {
		String s = (String)JOptionPane.showInputDialog(null, "Enter New Group Name", "Edit Group Name", 
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		
		if(s != null && !node.getNode().isLeaf())
			RuleManager.getInstance().editGroupName((RuleGroup)node.getNode(), s);
		
/*		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.reload(node);*/
		
/*		TreeNode parent = (TreeNode) node.getParent();
		for(TreeNode item: parent.getChildList())
			if(item.equals(node))
				model.reload(item);*/

		refreshTree();
		
		revalidate();
		repaint();
	}

	public TreePath getSelPath() {
		return selPath;
	}

	private void moveNode(TreePath path) {		
		JScrollPane scrollpane = new JScrollPane(); 
		JTree selectionTree = new JTree();
		selectionTree.setModel(new DefaultTreeModel(TreeNode.createInstance(RuleManager.getInstance().getRootNode())));
		selectionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollpane = new JScrollPane(selectionTree);
		JPanel panel = new JPanel(); 
	    panel.add(scrollpane);
	    scrollpane.getViewport().add(selectionTree);
	    int answer = JOptionPane.showConfirmDialog(null, scrollpane, "Select Tree Destination", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (answer == JOptionPane.OK_OPTION)
		{
		    TreeNode destination = (TreeNode)selectionTree.getSelectionPath().getLastPathComponent();
		    if(!destination.getNode().isLeaf()){
		    	insertNode(destination, path);
		    }
		    else if(destination.getNode().isLeaf()){
		    	if(((TreeNode)destination.getParent()).getNode().equals(((TreeNode)path.getParentPath().getLastPathComponent()).getNode())){	    		
		    		
		    		TreeNode toMoveNode = (TreeNode)path.getLastPathComponent(); 
				    TreeNode parentGroupNode = (TreeNode)path.getParentPath().getLastPathComponent();
		    		
				    int destinationIndex = destination.getParent().getIndex(destination);
				    int toMoveIndex = toMoveNode.getParent().getIndex(toMoveNode);
				    if(destinationIndex != toMoveIndex){
				    	RuleNode toMove = (RuleNode)toMoveNode.getNode();
			    		RuleGroup parentGroup = (RuleGroup)parentGroupNode.getNode();
			    		RuleNode reference = (RuleNode)destination.getNode();
			    		
			    		if(RuleManager.getInstance().moveRuleWithinSameGroup(parentGroup, toMove, reference)){
			    			// do the same for ui (the tree nodes) because RuleManager only manipulates in the internal data structure
			    			
			    			parentGroupNode.remove(toMoveNode);
			    			parentGroupNode.insert(toMoveNode, destinationIndex);
			    			
			    			DefaultTreeModel model = (DefaultTreeModel)getTree().getModel();
							model.reload(parentGroupNode);
							//model.reload();
							setViewportView(getTree());
			    		}
				    	
				    }
		    	}
		    	else
					JOptionPane.showMessageDialog(null, "Tree node selected does not belong to the same rule group.",
						    "Error", JOptionPane.ERROR_MESSAGE);
		    }
			else
				JOptionPane.showMessageDialog(null, "No tree node selected as destination.",
					    "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
			JOptionPane.showMessageDialog(null, "No tree node selected as destination.",
				    "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void addNewGroup(TreeNode node){
		String s = (String)JOptionPane.showInputDialog(null, "Enter New Group Name", "Add New Group", 
				JOptionPane.PLAIN_MESSAGE, null, null, null);
		RuleGroup newGroup = new RuleGroup(s);
		if(!RuleManager.getInstance().addGroup((RuleGroup)node.getNode(), newGroup))
			JOptionPane.showMessageDialog(null, "Duplicate group name.",  "Error", JOptionPane.ERROR_MESSAGE);
		else{
			int position = 0;
			for(TreeNode tNode:node.getChildList()){
				if(tNode.getNode().isLeaf()){
					node.insert(new TreeNode(newGroup), position);
					break;
				}
				position++;
			}
		}
			
		
		DefaultTreeModel model = (DefaultTreeModel)getTree().getModel();
		model.reload(node);
		
		revalidate();
		repaint();
	}
	
	public void addNewRule(TreeNode node){
		Object[] choices = {"Simple", "Form Selection", "Morphophonemic", "Structural"};
		String s = (String) JOptionPane.showInputDialog(
		                    null, "Select Rule Type",
		                    "New Rule", JOptionPane.PLAIN_MESSAGE,
		                    null, choices, null);
		
		if(s != null && s.equalsIgnoreCase("Simple"))
			new SimpleSpellOutController(getTree(), node);
		else if(s != null && s.equalsIgnoreCase("Form Selection"))
			 new FormSelectionController(getTree(), node);
		else if(s != null && s.equalsIgnoreCase("Morphophonemic"))
			 new MorphoSpellOutController(getTree(), node);
		else if(s != null && s.equalsIgnoreCase("Structural")){
			String rulename = (String)JOptionPane.showInputDialog(
                    null, "Input rule name:", "New Structural Rule",
                    JOptionPane.PLAIN_MESSAGE, null,
                    null, "");
			 new AddRuleController(getTree(), node, rulename,"ADD");
		}
		
	}
	public void insertNode(TreeNode destination, TreePath path){
		TreeNode child = (TreeNode) path.getLastPathComponent();	
		TreeNode newnode = (TreeNode) child.clone();
		TreeNode source = (TreeNode) path.getParentPath().getLastPathComponent();
		
		if(newnode.getNode().isLeaf()){
	    	if(!RuleManager.getInstance().moveRule((RuleGroup)source.getNode(), (RuleGroup)destination.getNode(), (RuleNode)newnode.getNode()))
		    	JOptionPane.showMessageDialog(null, "Error in moving to destination",  "Error", JOptionPane.ERROR_MESSAGE);
		}
	    else{
	    	if(RuleManager.getInstance().addGroup((RuleGroup)destination.getNode(), (RuleGroup)newnode.getNode())){
	    		source.remove(child);
	    		RuleManager.getInstance().deleteGroup((RuleGroup)source.getNode(), (RuleGroup)child.getNode());
	    	}
	    	else
	    		JOptionPane.showMessageDialog(null, "Error in moving to destination",  "Error", JOptionPane.ERROR_MESSAGE);
	    }
		refreshTree();		//for now only
/*		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.reload(source);
		model.reload(destination);*/
		revalidate();
		repaint();

			
	}
	
	public void deleteNode(TreePath path){
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete?", "Cofirm Delete",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,  options, options[0]);
		if(n == 0){
			if(path.getLastPathComponent() != path.getPathComponent(0)){
				TreeNode parent = (TreeNode) path.getParentPath().getLastPathComponent();
				TreeNode child = (TreeNode) path.getLastPathComponent();
				
				if(!child.getNode().isLeaf())
					RuleManager.getInstance().deleteGroup((RuleGroup)parent.getNode(), (RuleGroup)child.getNode());
				else
					RuleManager.getInstance().deleteRule((RuleGroup)parent.getNode(), (RuleNode)child.getNode());
				
				parent.remove(child);
				DefaultTreeModel model = (DefaultTreeModel)getTree().getModel();
				model.reload(parent);
				
				revalidate();
				repaint();
			}
			else
	    		JOptionPane.showMessageDialog(null, "Root folder cannot be deleted.",  "Error", JOptionPane.ERROR_MESSAGE);
			
		}
	}
	
	public void refreshTree(){
		getTree().setModel(new DefaultTreeModel(TreeNode.createInstance(RuleManager.getInstance().getRootNode())));
	}

	public JTree getTree() {
		return tree;
	}

}

class CustomIconRenderer extends DefaultTreeCellRenderer {
    ImageIcon defaultIcon;
    ImageIcon specialIcon;

    public CustomIconRenderer() {
        defaultIcon = new ImageIcon("Resources/rule.png");
        specialIcon = new ImageIcon("Resources/group.png");
    }

    public Component getTreeCellRendererComponent(JTree tree,
      Object value,boolean sel,boolean expanded,boolean leaf,
      int row,boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, 
          expanded, leaf, row, hasFocus);

        TreeNode nodeObj = (TreeNode) value;
        // check whatever you need to on the node user object
        if (nodeObj.getNode().isLeaf()) {
            setIcon(defaultIcon);
        } else {
            setIcon(specialIcon);
        } 
        return this;
    }
}