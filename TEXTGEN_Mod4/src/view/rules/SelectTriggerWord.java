package view.rules;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import lexicon.Lexicon;
import lexicon.LexiconList;
import managers.LexiconManager;

public class SelectTriggerWord extends JDialog {

	JButton btnOK;
	JButton btnCancel;
	JComboBox<String> cmbTriggerWord;
	String pos;
	
	public SelectTriggerWord(String pos) {
		this.pos = pos;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		setBounds(100, 100, 501, 340);
		getContentPane().setLayout(null);
		
		btnOK = new JButton("OK");
		btnOK.setBounds(141, 205, 93, 23);
		getContentPane().add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(288, 205, 93, 23);
		getContentPane().add(btnCancel);
		
		cmbTriggerWord = new JComboBox<String>();
		cmbTriggerWord.setBounds(141, 108, 240, 30);
		getContentPane().add(cmbTriggerWord);
		populateCmbTriggerWord();
		
		JLabel lblTriggerWord = new JLabel("Trigger Word:");
		lblTriggerWord.setBounds(40, 116, 91, 15);
		getContentPane().add(lblTriggerWord);
	}
	
	private void populateCmbTriggerWord() {
		LexiconList lList = LexiconManager.getInstance().getLexiconList(pos);
		for(Lexicon lexicon: lList.getLexiconList())
			if(lexicon.getName()!=null)
				cmbTriggerWord.addItem(lexicon.getName());
	}
	
	public JComboBox<String> getCmbTriggerWord(){
		return cmbTriggerWord;
	}
	
	public Lexicon getSelectedLexicon(){
		LexiconList lList = LexiconManager.getInstance().getLexiconList(pos);
		int selectedIndex = cmbTriggerWord.getSelectedIndex();
		if(selectedIndex >= 0 && selectedIndex < lList.getLexiconList().size())
			return lList.getLexiconList().get(selectedIndex);
		
		return null;
	}
	
	public String getSelectedWord(){
		return cmbTriggerWord.getSelectedItem().toString();
	}
	
	public void addOKBtnListener(ActionListener action){
		btnOK.addActionListener(action);
	}
	
	public void addCancelBtnListener(ActionListener action){
		btnCancel.addActionListener(action);
	}
}
