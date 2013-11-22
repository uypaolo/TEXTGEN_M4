package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
		
	//Singleton
	private static MainFrame instance;
	
	public static MainFrame getInstance(){
		if(instance == null){
			instance = new MainFrame();
		}
		return instance;
	}
	
	//Other attributes/methods
	
	private JPanel mainPanel;
	
	//Menu
	private JMenuItem developGrammar;
	private JMenuItem newSemantics;
	private JMenuItem saveSemantics;
	private JMenuItem saveAsSemantics;
	private JMenuItem loadSemantics;
	
	
	private JMenuItem ontology;
	private JMenuItem lexicon;
	private JMenuItem selectLanguage;
	private JMenuItem createNewLanguage;
	
	
	private MainFrame() {
		InitializeBars();
		setTitle("Linguist's Assistant");

		/*
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
		setExtendedState(Frame.MAXIMIZED_BOTH);
		 */

		Rectangle rec = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		setSize(rec.width, rec.height);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	private void InitializeBars(){
		 //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //menuBar.setSize(new Dimension(1200, 30));
        
        //Add a JMenu
        JMenu fileMenu = new JMenu("Grammar Development");
        developGrammar = new JMenuItem("Develop Grammar");
        newSemantics = new JMenuItem("New Document");
        saveSemantics = new JMenuItem("Save Document");
        saveAsSemantics = new JMenuItem("Save Document As");
        loadSemantics = new JMenuItem("Load Document");
        menuBar.add(fileMenu);
        
        //Add to the JMenu
        fileMenu.add(developGrammar);
        fileMenu.add(newSemantics);
        fileMenu.add(saveSemantics);
        fileMenu.add(saveAsSemantics);
        fileMenu.add(loadSemantics);
        
        JMenu ontoAndLex = new JMenu("Ontology and Lexicon");
        ontology = new JMenuItem("Ontology");
        lexicon = new JMenuItem("Lexicon");
        JMenu language = new JMenu("Language");
        
        selectLanguage = new JMenuItem("Select Language");
        createNewLanguage = new JMenuItem("Create New Language");
        language.add(selectLanguage);
        language.add(createNewLanguage);
        
        ontoAndLex.add(ontology);
        ontoAndLex.add(lexicon);
        ontoAndLex.add(language);
        
        menuBar.add(ontoAndLex);
        
        this.setJMenuBar(menuBar);
	}
	
	//Listener setters
	public void setNewSemanticsListener(ActionListener listener){
		setListener(newSemantics, listener);
	}
	
	public void setSaveSemanticsListener(ActionListener listener){
		setListener(saveSemantics, listener);
	}
	
	public void setSaveAsSemanticsListener(ActionListener listener){
		setListener(saveAsSemantics, listener);
	}
	
	public void setLoadSemanticsListener(ActionListener listener){
		setListener(loadSemantics, listener);
	}
	
	public void setOntologyListener(ActionListener listener){
		setListener(ontology, listener);
	}
	
	public void setLexiconListener(ActionListener listener){
		setListener(lexicon, listener);
	}
	
	public void setSelectLanguageListener(ActionListener listener){
		setListener(selectLanguage, listener);
	}
	
	public void setCreateNewLanguageListener(ActionListener listener){
		setListener(createNewLanguage, listener);
	}
	
	public void setDevelopGrammarListener(ActionListener listener){
		setListener(developGrammar, listener);
	}
	
	private void setListener(JMenuItem menuItem, ActionListener listener){
		ActionListener[] currListeners = menuItem.getActionListeners();
		
		for(ActionListener curr: currListeners)
			menuItem.removeActionListener(curr);
		
		menuItem.addActionListener(listener);
	}
	
	
	//Other setters
	public void setPanel(JPanel newPanel){
		if(mainPanel != null)
			getContentPane().remove(mainPanel);
		
		mainPanel = newPanel;	
	
		if(mainPanel != null)
			getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().revalidate();
		getContentPane().repaint();
	}
	
}
