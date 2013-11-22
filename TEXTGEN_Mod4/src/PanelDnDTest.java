import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.grammardevelopment.ComponentPanel;

public class PanelDnDTest extends JFrame
{



    public PanelDnDTest()
    {
        super("Panel Drop Test");

        JPanel fromPanel;
        JPanel toPanel;
        JPanel dragPanel;


        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        dragPanel = new JPanel( new FlowLayout() );
        dragPanel.add( new JLabel( "This is the panel to move" ) );

        fromPanel = new JPanel( new FlowLayout() );
        fromPanel.setBorder( BorderFactory.createTitledBorder("From Panel") );
        fromPanel.add( dragPanel );
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(
                                                     fromPanel, // component where drag originates
                                                     DnDConstants.ACTION_COPY_OR_MOVE, // actions
                                                     new PanelDragListener( fromPanel, dragPanel ) ); // drag gesture recognizer


        toPanel = new JPanel( new FlowLayout() );
        toPanel.setBorder( BorderFactory.createTitledBorder("To Panel") );
        DropTarget dropTarget = new DropTarget(toPanel, new PanelDropTargetListener( toPanel ) );
        dropTarget.setDefaultActions(DnDConstants.ACTION_COPY_OR_MOVE);


        getContentPane().add(fromPanel, BorderLayout.WEST );
        getContentPane().add(toPanel, BorderLayout.CENTER);
        setSize( 300, 300 );
        setVisible( true );


    }

    public class PanelDragListener implements DragGestureListener
    {
        private JPanel parentPanel;
        private JPanel childPanel;

        public PanelDragListener( JPanel p, JPanel c )
        {
            parentPanel = p;
            childPanel = c;
        }

        public void dragGestureRecognized(DragGestureEvent dge)
        {
            //Cursor null for native feedback

            dge.startDrag(null,
                          new PanelSelection( childPanel ) );
            parentPanel.remove( childPanel );
            parentPanel.validate();
            parentPanel.repaint();

        }
    }
    public static void main( String[] args )
    {
        new PanelDnDTest();
    }


    private class PanelDropTargetListener implements DropTargetListener
    {
        private JPanel dropPanel;

        PanelDropTargetListener( JPanel p )
        {
            dropPanel = p;
        }


        public void dragExit(DropTargetEvent dte)
        {
        }

        public void dragEnter(DropTargetDragEvent dtde)
        {
            if( dtde.isDataFlavorSupported( PANEL_FLAVOR ) )
            {
                dtde.acceptDrag( DnDConstants.ACTION_COPY_OR_MOVE );
            }
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
        }

        public void dropActionChanged(DropTargetDragEvent dtde)
        {
        }

        public void drop(DropTargetDropEvent dtde)
        {
            if( dtde.isDataFlavorSupported(PANEL_FLAVOR) )
            {
                try
                {
                    JPanel panel = (JPanel)dtde.getTransferable().getTransferData(PANEL_FLAVOR);
                    dropPanel.add(panel);
                    dropPanel.validate();
                    dropPanel.repaint();

                }
                catch( Exception exc )
                {
                    exc.printStackTrace();
                }
            }

        }


    }
    public final static DataFlavor PANEL_FLAVOR = new DataFlavor(ComponentPanel.class,"Panel Instances");

//YOUR TRANSFERABLE
    public class PanelSelection implements Transferable
    {
        public final DataFlavor PANEL_FLAVOR = new DataFlavor(ComponentPanel.class,"Panel Instances");
        private DataFlavor[] flavors = { PANEL_FLAVOR};
        protected JPanel panel;

        public PanelSelection(JPanel pnl)
        {
            panel = pnl;
        }  public DataFlavor[] getTransferDataFlavors()
        {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            for( int i = 0;i<flavors.length;i++ )
            {
                if( flavors.equals(flavor) )

{

return true;

}

}

return false;

}



public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException

{

if( flavor.equals(PANEL_FLAVOR) )

{

return panel;

}

throw new UnsupportedFlavorException(flavor);

}

}

}
