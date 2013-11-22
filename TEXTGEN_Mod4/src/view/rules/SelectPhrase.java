package view.rules;

import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRadioButton;

public class SelectPhrase extends JDialog {

	private JRadioButton rdbtnNP;
	private JRadioButton rdbtnVP;
	private JRadioButton rdbtnAdjP;
	private JRadioButton rdbtnAdvP;
	private JRadioButton rdbtnNoP;
	private JButton btnOK;
	private ButtonGroup phraseGroup;
	
	public SelectPhrase() {
		setTitle("Phrase Type Selection");
	    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	    setVisible(true);
	    getContentPane().setLayout(null);
		setBounds(100, 100, 344, 346);
		getContentPane().setLayout(null);
		
		rdbtnNP = new JRadioButton("Noun Phrase");
		rdbtnNP.setBounds(102, 34, 121, 23);
		getContentPane().add(rdbtnNP);
		
	    rdbtnVP = new JRadioButton("Verb Phrase");
		rdbtnVP.setBounds(102, 71, 121, 23);
		getContentPane().add(rdbtnVP);
		
		rdbtnAdjP = new JRadioButton("Adjective Phrase");
		rdbtnAdjP.setBounds(102, 105, 121, 23);
		getContentPane().add(rdbtnAdjP);
		
		rdbtnAdvP = new JRadioButton("Adverb Phrase");
		rdbtnAdvP.setBounds(102, 144, 121, 23);
		getContentPane().add(rdbtnAdvP);
		
		rdbtnNoP = new JRadioButton("No Phrase");
		rdbtnNoP.setBounds(102, 180, 121, 23);
		getContentPane().add(rdbtnNoP);
		
		phraseGroup = new ButtonGroup();
		phraseGroup.add(rdbtnNP);
		phraseGroup.add(rdbtnVP);
		phraseGroup.add(rdbtnAdjP);
		phraseGroup.add(rdbtnAdvP);
		phraseGroup.add(rdbtnNoP);
		
		btnOK = new JButton("OK");
		btnOK.setBounds(118, 243, 93, 23);
		getContentPane().add(btnOK);
	}
	
	public ButtonModel getSelectedPhraseModel(){
		return phraseGroup.getSelection();
	}
	
	public JRadioButton getRdbtnNP() {
		return rdbtnNP;
	}

	public JRadioButton getRdbtnVP() {
		return rdbtnVP;
	}

	public JRadioButton getRdbtnAdjP() {
		return rdbtnAdjP;
	}

	public JRadioButton getRdbtnAdvP() {
		return rdbtnAdvP;
	}

	public JRadioButton getRdbtnNoP() {
		return rdbtnNoP;
	}
	
	public void addBtnOKListener(ActionListener action){
		btnOK.addActionListener(action);
	}
}
