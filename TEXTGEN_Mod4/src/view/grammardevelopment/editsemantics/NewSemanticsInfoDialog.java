package view.grammardevelopment.editsemantics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import components.InputXMLDocument;

import controller.listener.grammardev.SemanticsInfoListener;

public class NewSemanticsInfoDialog extends JDialog {
	private JTextField txtFieldName;
	private JTextField txtFieldCategory;
	private JTextPane txtAreaComments;
	private JLabel errorLabel;
	
	private SemanticsInfoListener controller;
	
	public NewSemanticsInfoDialog(Frame owner, String title, SemanticsInfoListener controller){
		super(owner, title);
		this.controller = controller;
		initGUI();
		setSize(450, 325);
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
	}
	
	public NewSemanticsInfoDialog(Frame owner, String title, SemanticsInfoListener controller, InputXMLDocument doc){
		this(owner, title, controller);
		txtFieldName.setText(doc.getName());
		txtFieldCategory.setText(doc.getCategory());
		txtAreaComments.setText(doc.getComments());
	}
	
	private void initGUI(){
		getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setBounds(48, 64, 49, 21);
		getContentPane().add(lblName);
		
		JLabel lblCreateNewSemantics = new JLabel("Create New Semantics Document");
		lblCreateNewSemantics.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCreateNewSemantics.setBounds(95, 11, 245, 30);
		getContentPane().add(lblCreateNewSemantics);
		
		txtFieldName = new JTextField();
		txtFieldName.setBounds(108, 65, 275, 20);
		getContentPane().add(txtFieldName);
		txtFieldName.setColumns(10);
		
		JLabel lblCategory = new JLabel("<html>Category:<br>(Optional)</html>");
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCategory.setBounds(48, 96, 60, 28);
		getContentPane().add(lblCategory);
		
		txtFieldCategory = new JTextField();
		txtFieldCategory.setBounds(108, 96, 275, 20);
		getContentPane().add(txtFieldCategory);
		txtFieldCategory.setColumns(10);
		
		JLabel lblComments = new JLabel("Comments (Optional) :");
		lblComments.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblComments.setBounds(48, 138, 151, 14);
		getContentPane().add(lblComments);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(119, 222, 89, 23);
		getContentPane().add(btnOk);
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(isInputValid()){
					controller.proceed(false);
					setVisible(false);
				}
				else
					errorLabel.setText("Please don't leave 'Name' blank.");	
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(218, 222, 89, 23);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.proceed(true);
				setVisible(false);
			}
		});
		
		txtAreaComments = new JTextPane();
		txtAreaComments.setBounds(48, 153, 335, 58);
		JScrollPane scrollPane = new JScrollPane(txtAreaComments);
		scrollPane.setBounds(48, 153, 335, 58);
		getContentPane().add(scrollPane);
		
		errorLabel = new JLabel("");
		errorLabel.setBounds(118, 257, 335, 18);
		errorLabel.setForeground(Color.RED);
		getContentPane().add(errorLabel);
	}

	public boolean isInputValid(){
		return !txtFieldName.getText().trim().isEmpty();
	}
	
	public String getName(){
		return txtFieldName.getText().trim();
	}
	
	public String getCategory(){
		return txtFieldCategory.getText().trim();
	}
	
	public String getComments(){
		return txtAreaComments.getText().trim();
	}

}