package controller.listener.lexicon;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import lexicon.LexiconList;
import managers.LexiconManager;
import view.lexicon.LexiconPanel;
import view.lexicon.LexiconTable;

public class LexiconTableKeyListener implements KeyListener{

	LexiconPanel lexPanel;
	LexiconTable lexTable;
	LexiconList  lexList;
	
	public LexiconTableKeyListener(LexiconPanel lexPanel){
		this.lexPanel = lexPanel;
		this.lexTable = lexPanel.getTable();
	}
	
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_DELETE){
			if(lexTable.getSelectedRowCount()>0){
				int n = JOptionPane.showConfirmDialog(
					    null,
					    "Are you sure you want to delete all selected lexicons?",
					    "Question",
					    JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.YES_OPTION){
					DefaultTableModel model = (DefaultTableModel) lexTable.getModel();
					lexList = LexiconManager.getInstance().getLexiconList(lexPanel.getCodeFromSelectedPOS());
					for (int i = lexTable.getSelectedRows().length - 1; i >= 0; i--) {
						lexList.removeLexicon(lexList.getLexiconList().get(i));
						model.removeRow(lexTable.getSelectedRows()[i]);
					}
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent arg0) {
	}
	public void keyTyped(KeyEvent arg0) {
	}

}
