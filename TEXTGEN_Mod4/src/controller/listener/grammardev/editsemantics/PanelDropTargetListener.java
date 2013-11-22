package controller.listener.grammardev.editsemantics;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import view.grammardevelopment.ComponentPanel;
import view.grammardevelopment.InputXMLDocumentPanel;



public class PanelDropTargetListener implements DropTargetListener{
	
	public final static DataFlavor PANEL_FLAVOR = new DataFlavor(ComponentPanel.class,"Panel Instances");
	private InputXMLDocumentPanel parentDocPanel;
	
	public PanelDropTargetListener(InputXMLDocumentPanel parentDocPanel){
		this.parentDocPanel = parentDocPanel;
	}

	public void dragEnter(DropTargetDragEvent dtde) {

        if( dtde.isDataFlavorSupported( PANEL_FLAVOR ) )
        {
            dtde.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE );
        }
	}
	public void dragExit(DropTargetEvent dte) {
	}
	public void dragOver(DropTargetDragEvent dtde) {
	}
	public void drop(DropTargetDropEvent dtde) {
		if( dtde.isDataFlavorSupported(PANEL_FLAVOR) )
        {
            try
            {
                ComponentPanel fromPanel = (ComponentPanel)dtde.getTransferable().getTransferData(PANEL_FLAVOR);
                
                ComponentPanel toPanel = (ComponentPanel)dtde.getSource();
                dtde.getLocation(); // manipulate location to save in correct position
                //add the fromPanel to toPanel as its new children.

            }
            catch( Exception exc )
            {
                exc.printStackTrace();
            }
        }
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

}
