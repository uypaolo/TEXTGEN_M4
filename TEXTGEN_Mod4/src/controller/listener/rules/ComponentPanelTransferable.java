package controller.listener.rules;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import view.grammardevelopment.ComponentPanel;

public class ComponentPanelTransferable implements Transferable{
	
	private ComponentPanel compPanel;
	 
	public final static DataFlavor PANEL_FLAVOR = new DataFlavor(ComponentPanel.class,"Panel Instances");
	
	public ComponentPanelTransferable(ComponentPanel compPanel) {
		this.compPanel = compPanel;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { PANEL_FLAVOR };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(PANEL_FLAVOR);
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {

		if (flavor.equals(PANEL_FLAVOR))
			return compPanel;
		else
			throw new UnsupportedFlavorException(flavor);
	}

}
