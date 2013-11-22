package view.grammardevelopment;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import managers.RuleManager;
import rules.RuleGroup;
import rules.RuleNode;

public class TreeDragTest extends JFrame {

  TreeDragSource ds;

  TreeDropTarget dt;

  JTree tree;

  public TreeDragTest() {
    super("Rearrangeable Tree");
    setSize(300, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    tree = new AutoScrollingJTree();
    tree = new JTree(view.grammardevelopment.TreeNode.createInstance(RuleManager.getInstance().getRootNode()));
    getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
    ds = new TreeDragSource(tree, DnDConstants.ACTION_COPY_OR_MOVE);
    dt = new TreeDropTarget(tree);
    setVisible(true);
  }


  public class AutoScrollingJTree extends JTree implements Autoscroll {
    private int margin = 12;

    public AutoScrollingJTree() {
      super();
    }

    public void autoscroll(Point p) {
      int realrow = getRowForLocation(p.x, p.y);
      Rectangle outer = getBounds();
      realrow = (p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1
          : realrow < getRowCount() - 1 ? realrow + 1 : realrow);
      scrollRowToVisible(realrow);
    }

    public Insets getAutoscrollInsets() {
      Rectangle outer = getBounds();
      Rectangle inner = getParent().getBounds();
      return new Insets(inner.y - outer.y + margin, inner.x - outer.x
          + margin, outer.height - inner.height - inner.y + outer.y
          + margin, outer.width - inner.width - inner.x + outer.x
          + margin);
    }

  }

  public static void main(String args[]) {
    new TreeDragTest();
  }
}

//TreeDragSource.java
//A drag source wrapper for a JTree. This class can be used to make
//a rearrangeable DnD tree with the TransferableTreeNode class as the
//transfer data type.

class TreeDragSource implements DragSourceListener, DragGestureListener {

  DragSource source;

  DragGestureRecognizer recognizer;

  TransferableTreeNode transferable;

  view.grammardevelopment.TreeNode oldNode;

  JTree sourceTree;

  public TreeDragSource(JTree tree, int actions) {
    sourceTree = tree;
    source = new DragSource();
    recognizer = source.createDefaultDragGestureRecognizer(sourceTree,
        actions, this);
  }

  /*
   * Drag Gesture Handler
   */
  public void dragGestureRecognized(DragGestureEvent dge) {
    TreePath path = sourceTree.getSelectionPath();
    if ((path == null) || (path.getPathCount() <= 1)) {
      // We can't move the root node or an empty selection
      return;
    }
    oldNode = (view.grammardevelopment.TreeNode) path.getLastPathComponent();
    transferable = new TransferableTreeNode(oldNode);
    source.startDrag(dge, DragSource.DefaultMoveNoDrop, transferable, this);

    // If you support dropping the node anywhere, you should probably
    // start with a valid move cursor:
    //source.startDrag(dge, DragSource.DefaultMoveDrop, transferable,
    // this);
  }

  /*
   * Drag Event Handlers
   */
  public void dragEnter(DragSourceDragEvent dsde) {
  }

  public void dragExit(DragSourceEvent dse) {
  }

  public void dragOver(DragSourceDragEvent dsde) {
  }

  public void dropActionChanged(DragSourceDragEvent dsde) {
    System.out.println("Action: " + dsde.getDropAction());
    System.out.println("Target Action: " + dsde.getTargetActions());
    System.out.println("User Action: " + dsde.getUserAction());
  }

  public void dragDropEnd(DragSourceDropEvent dsde) {

    System.out.println("Drop Action: " + dsde.getDropAction());
    if (dsde.getDropSuccess()&& (dsde.getDropAction() == DnDConstants.ACTION_MOVE)) {
        //deleteNode(oldNode);
        //System.out.println(oldNode.getNode().getNameForPrinting());
      ((DefaultTreeModel) sourceTree.getModel()).removeNodeFromParent(oldNode);
    }

  }

	
	public void deleteNode(view.grammardevelopment.TreeNode child){
/*		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete?", "Cofirm Delete",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,  options, options[0]);
		if(n == 0){*/
			view.grammardevelopment.TreeNode parent = (view.grammardevelopment.TreeNode) child.getParent();
			
			if(!((view.grammardevelopment.TreeNode) child).getNode().isLeaf())
				RuleManager.getInstance().deleteGroup((RuleGroup)parent.getNode(), (RuleGroup)child.getNode());
			else
				RuleManager.getInstance().deleteRule((RuleGroup)parent.getNode(), (RuleNode)child.getNode());
			
//		}
	}
}

//TreeDropTarget.java
//A quick DropTarget that's looking for drops from draggable JTrees.
//

class TreeDropTarget implements DropTargetListener {

  DropTarget target;

  JTree targetTree;

  public TreeDropTarget(JTree tree) {
    targetTree = tree;
    target = new DropTarget(targetTree, this);
  }
  
  public void deleteNode(view.grammardevelopment.TreeNode child){
	  /*		Object[] options = {"Yes", "No"};
	  		int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete?", "Cofirm Delete",
	  				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,  options, options[0]);
	  		if(n == 0){*/
	  			view.grammardevelopment.TreeNode parent = (view.grammardevelopment.TreeNode) child.getParent();
	  			
	  			if(!((view.grammardevelopment.TreeNode) child).getNode().isLeaf())
	  				RuleManager.getInstance().deleteGroup((RuleGroup)parent.getNode(), (RuleGroup)child.getNode());
	  			else
	  				RuleManager.getInstance().deleteRule((RuleGroup)parent.getNode(), (RuleNode)child.getNode());
	  			
//	  		}
	  	}

  /*
   * Drop Event Handlers
   */
  private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
    Point p = dtde.getLocation();
    DropTargetContext dtc = dtde.getDropTargetContext();
    JTree tree = (JTree) dtc.getComponent();
    TreePath path = tree.getClosestPathForLocation(p.x, p.y);
    return (TreeNode) path.getLastPathComponent();
  }

  public void dragEnter(DropTargetDragEvent dtde) {
    TreeNode node = getNodeForEvent(dtde);
    if (node.isLeaf()) {
      dtde.rejectDrag();
    } else {
      // start by supporting move operations
      //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
      dtde.acceptDrag(dtde.getDropAction());
    }
  }

  public void dragOver(DropTargetDragEvent dtde) {
    TreeNode node = getNodeForEvent(dtde);
    if (node.isLeaf()) {
      dtde.rejectDrag();
    } else {
      // start by supporting move operations
      //dtde.acceptDrag(DnDConstants.ACTION_MOVE);
      dtde.acceptDrag(dtde.getDropAction());
    }
  }

  public void dragExit(DropTargetEvent dte) {
  }

  public void dropActionChanged(DropTargetDragEvent dtde) {
  }

  public void drop(DropTargetDropEvent dtde) {
	  
	//determine nearest parent
    Point pt = dtde.getLocation();
    DropTargetContext dtc = dtde.getDropTargetContext();
    JTree tree = (JTree) dtc.getComponent();
    TreePath parentpath = tree.getClosestPathForLocation(pt.x, pt.y);
    view.grammardevelopment.TreeNode parent = (view.grammardevelopment.TreeNode) parentpath.getLastPathComponent();
   
    /* 
    view.grammardevelopment.TreeNode dragged = (view.grammardevelopment.TreeNode)dtde.getSource();
    
    //delete from old parent
    deleteNode(dragged);*/
    
    if (parent.isLeaf()) {
      dtde.rejectDrop();
      return;
    }

    try {
      Transferable tr = dtde.getTransferable();
      DataFlavor[] flavors = tr.getTransferDataFlavors();
      //TreePath p = (TreePath) tr.getTransferData(flavors[0]);
      view.grammardevelopment.TreeNode node = (view.grammardevelopment.TreeNode)tr.getTransferData(flavors[0]);
      System.out.println("NOOOOOOOOOOOOOODE1        "+node.getNode().getName());
      
/*
        if (tr.isDataFlavorSupported(flavors[0])) {
          dtde.acceptDrop(dtde.getDropAction());
          TreePath p = (TreePath) tr.getTransferData(flavors[0]);
          view.grammardevelopment.TreeNode node = (view.grammardevelopment.TreeNode) p.getLastPathComponent();
          System.out.println("NOOOOOOOOOOOOOODE        "+node.getNode().getName());
          //deleteNode(node);
          if(node.getNode().getName() == null)
        	  System.out.println("NULLLL");
          //System.out.println("path: "+p.getLDropped at "+parent.getNode().getName()+" yung dinala "+node.getNode().getName());
          insertRule(parent, node);
          DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
          model.insertNodeInto(node, parent, 0);
          dtde.dropComplete(true);
          return;
        }
      */
      dtde.rejectDrop();
    } catch (Exception e) {
      e.printStackTrace();
      dtde.rejectDrop();
    }
  }
  
  
	public void insertRule(view.grammardevelopment.TreeNode parent, view.grammardevelopment.TreeNode newNode){
		
		if(!newNode.getNode().isLeaf()){
			if(!RuleManager.getInstance().addGroup((RuleGroup)((view.grammardevelopment.TreeNode) parent).getNode(), (RuleGroup)newNode.getNode())){
				JOptionPane.showMessageDialog(null, "Duplicate group name.",  "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			((view.grammardevelopment.TreeNode) parent).addChild(new view.grammardevelopment.TreeNode(newNode.getNode()));
		}
		else{
			if(!RuleManager.getInstance().addRule((RuleGroup)((view.grammardevelopment.TreeNode) parent).getNode(), (RuleNode)newNode.getNode())){
				JOptionPane.showMessageDialog(null, "Duplicate rule name.",  "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			((view.grammardevelopment.TreeNode) parent).addChild(new view.grammardevelopment.TreeNode(newNode.getNode()));
		}
			
	
		
	}
  
  
}

//TransferableTreeNode.java
//A Transferable TreePath to be used with Drag & Drop applications.
//

class TransferableTreeNode implements Transferable {

  public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class,
      "Tree Path");

  DataFlavor flavors[] = { TREE_PATH_FLAVOR };

  view.grammardevelopment.TreeNode path;

  public TransferableTreeNode(view.grammardevelopment.TreeNode tp) {
    path = tp;
  }

  public synchronized DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return (flavor.getRepresentationClass() == TreePath.class);
  }

  public synchronized Object getTransferData(DataFlavor flavor)
      throws UnsupportedFlavorException, IOException {
    if (isDataFlavorSupported(flavor)) {
      return (Object) path;
    } else {
      throw new UnsupportedFlavorException(flavor);
    }
  }
}
