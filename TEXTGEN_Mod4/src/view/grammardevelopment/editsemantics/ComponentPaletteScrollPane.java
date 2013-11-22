package view.grammardevelopment.editsemantics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

import managers.ColorManager;
import managers.ComponentManager;

import components.ComponentInfo;

public class ComponentPaletteScrollPane extends JScrollPane{
	JPanel panel;
	public ComponentPaletteScrollPane() {
		this.setBorder(BorderFactory.createTitledBorder("Component Palette - (Drag and Drop)    |    Phrases - White , Leaf - Black"));
		panel = new JPanel();  
		setViewportView(panel);
		panel.setLayout(new WrapLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int palettewidth = (int)(width*0.4);
		int paletteheight = (int)(height*0.3);
		this.setPreferredSize(new Dimension(palettewidth,paletteheight));
		addComponents(ComponentManager.getInstance().getComponentsInfo());
	}
	
	public void addComponents(ArrayList<ComponentInfo> components){
		for(ComponentInfo component : components){
			JButton button = new JButton(component.getName()){
				protected void paintComponent(Graphics g) {
				      Graphics2D g2 = (Graphics2D) g.create();
				      g2.setPaint(getBackground());
				      g2.fillRect( 0, 0, getWidth(), getHeight() );
				      g2.setPaint(getForeground());
				      g2.drawString(getText(), 50, 20);
				      g2.dispose();

				      //super.paintComponent(g);
				   }
			};
			Color color = ColorManager.getInstance().getColor(component.getName());
			button.setFont(new Font(button.getFont().getFontName(),Font.PLAIN,15));
			button.setHorizontalAlignment(SwingConstants.LEFT);
			button.setOpaque(true);
			button.setBackground(color);
			if(!component.isLeaf())
				button.setForeground(Color.WHITE);
			
			String leafOrPhrase;
			if(component.isLeaf()) leafOrPhrase = "Leaf";
			else leafOrPhrase = "Component";
			button.setToolTipText("<html>"+leafOrPhrase+":<br>"+component.getDescription()+"</html>");
			TransferHandler transfer = new TransferHandler("text"){
				public boolean canImport(TransferHandler.TransferSupport support) {
				    return false;
				}
			};
			button.setTransferHandler(transfer);
			button.setPreferredSize(new Dimension(150,30));
			panel.add(button);
		}
	}
	
	public void addListenersForAllButtons(MouseListener listener){
		for(Component comp : panel.getComponents()){
			
			if(comp instanceof JButton){
				((JButton)comp).addMouseListener(listener);
			}
				
		}
	}
}
