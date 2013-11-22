package view.grammardevelopment;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import rules.RuleGroup;
import rules.RuleNode;
import rules.RuleTreeNode;

public class TreeNode extends DefaultMutableTreeNode{
	
	RuleTreeNode node;
	ArrayList<TreeNode> childList = new ArrayList<TreeNode>();

	public static TreeNode createInstance(RuleTreeNode node){
		
		if(node.isLeaf()){
			return new TreeNode(node);
		}
		
		TreeNode groupNode = new TreeNode(node);
		RuleGroup group = (RuleGroup)node;
		ArrayList<RuleGroup> subGroups = group.getSubGroups();
		
		for(RuleGroup rule: subGroups){
			groupNode.addChild(createInstance(rule));
		}
		
		for(RuleNode rule: group.getRules())
			groupNode.addChild(createInstance(rule));
		
		return groupNode;
	}
	
	public ArrayList<TreeNode> getChildList() {
		return childList;
	}

	public void setChildList(ArrayList<TreeNode> childList) {
		this.childList = childList;
	}

	public void addChild(TreeNode node){
		childList.add(node);
		add(node);
	}
	
	public TreeNode(RuleTreeNode node){
		super(node.getName());
		this.node = node;
	}
	
	public void setNodeName(String name){
		node.setName(name);
	}

	public RuleTreeNode getNode() {
		return node;
	}

	public void setNode(RuleTreeNode node) {
		this.node = node;
	}

}
