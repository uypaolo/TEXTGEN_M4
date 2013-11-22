package view.grammardevelopment;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import view.MainFrame;

public class TextAreaWithScrollPane extends JPanel{
	
	private JTextPane textArea;
	
	public TextAreaWithScrollPane(String title){
		//panel settings
		setPreferredSize(new Dimension(MainFrame.getInstance().getWidth()*2/5, MainFrame.getInstance().getHeight()/2 ));
		setBorder(BorderFactory.createTitledBorder(title));
		setLayout(new GridLayout());
		
		//Text Area
		textArea= new JTextPane();
		textArea.setEditable(false);
		textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		
		//Scroll Pane
		JScrollPane sp = new JScrollPane(textArea);
		sp.getVerticalScrollBar().setUnitIncrement(16);
		sp.getVerticalScrollBar().setValue(0);
		
		add(sp);
	}
	
	public void setTextAreaContent(String content){
		textArea.setText(content);
		textArea.setCaretPosition(0);
	}
	
	public void clearTextArea(){
		textArea.setText("");
	}
}
