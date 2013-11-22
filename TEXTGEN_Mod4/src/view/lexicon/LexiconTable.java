package view.lexicon;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import lexicon.LexiconList;

public class LexiconTable extends JTable{
	
	LexiconList list;
	
	public LexiconTable(){
		//set display properties
	}
	 public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	        Component c = super.prepareRenderer(renderer, row, column);
	        if (c instanceof JComponent) {
	           if(column == 0){
	            JComponent jc = (JComponent) c;
	            jc.setToolTipText(" More Info Here");
	           }
	        }
	        return c;
	    }
	
	public void setList(LexiconList list ){
		this.list = list;
		// update table
	}
	

}
