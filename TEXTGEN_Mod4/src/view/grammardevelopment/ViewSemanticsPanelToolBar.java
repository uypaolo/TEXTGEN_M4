package view.grammardevelopment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.MainFrame;

public class ViewSemanticsPanelToolBar extends JPanel{

	private JButton editButton;
	private JButton cancelEditButton;
	private JButton generateButton;
	private JButton initializeButton;
	private JButton editDocInfoButton;
	private JButton AngPindutanNiJustin;
	
	private JButton btnPrev;
	private JButton btnNext;
	private JLabel currFileSelected;
		
	private int currMode;
	
	
	public ViewSemanticsPanelToolBar(){
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(MainFrame.getInstance().getWidth(), 30));
		
		//Create Buttons
		editButton = new JButton("Edit Current Document");
		cancelEditButton = new JButton("Done Editing");
		generateButton =  new JButton("Generate Sentence(s)");
		initializeButton = new JButton("Back to Initial Semantics");
		editDocInfoButton = new JButton("Edit Document Info");
		AngPindutanNiJustin = new JButton("Ang Pindutan ni Justin!");
		
		//"Navigate between files"
		btnPrev = new JButton("Prev");
		btnNext = new JButton("Next");
		currFileSelected = new JLabel("Current file selected: ");
		currFileSelected.setForeground(Color.WHITE);
		
		//Initialize Display
		currMode = -1;
		setMode(ViewSemanticsPanel.MODE_VIEW);
	}
	
	public void setBtnPrevListener(ActionListener listener){
		setButtonListener(listener,btnPrev);
	}
	
	public void setBtnNextListener(ActionListener listener){
		setButtonListener(listener,btnNext);
	}
	
	public void setPindutanListener(ActionListener listener){
		setButtonListener(listener, AngPindutanNiJustin);
	}
	
	public void setCurrFileSelectedText(String text){
		currFileSelected.setText(text);
	}
	
	public void hideFileNavigation(){
		btnPrev.setVisible(false);
		btnNext.setVisible(false);
		currFileSelected.setVisible(false);
	}
	
	public void unhideFileNavigation(){
		btnPrev.setVisible(true);
		btnNext.setVisible(true);
		currFileSelected.setVisible(true);
	}
	
	public void setEditButtonListener(ActionListener listener){
		setButtonListener(listener, editButton);
	}

	public void setEditDocInfoButtonListener(ActionListener listener){
		setButtonListener(listener, editDocInfoButton);
	}
	
	public void setDoneEditingButtonListener(ActionListener listener){
		setButtonListener(listener, cancelEditButton);
	}
	
	public void setInitializeButtonListener(ActionListener listener){
		setButtonListener(listener, initializeButton);
	}
	
	public void setGenerateButtonListener(ActionListener listener){
		setButtonListener(listener, generateButton);
	}
	
	private void setButtonListener(ActionListener listener, JButton button){
		ActionListener[] listeners = button.getActionListeners();
		for(ActionListener curr: listeners)
			button.removeActionListener(curr);
		button.addActionListener(listener);
	}
	
	public void setMode(int mode){
		if(currMode == mode)
			return;
		
		this.removeAll();
		
		if(mode == ViewSemanticsPanel.MODE_VIEW){
			add(currFileSelected);
			add(btnPrev);
			add(btnNext);
			add(AngPindutanNiJustin);
			
			add(generateButton);
			add(editButton);
		}
		else if(mode == ViewSemanticsPanel.MODE_EDIT){
			add(editDocInfoButton);
			add(cancelEditButton);
		}
		else //if(mode == MODE_GENERATE){
			add(initializeButton);
		
			
		this.currMode = mode;
		revalidate();
		repaint();
	}
}