package edu.rutgers.MOST.presentation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class AboutDialog  extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JLabel mostLabel = new JLabel(GraphicalInterfaceConstants.ABOUT_BOX_TEXT);
	public static JButton okButton = new JButton("    OK    ");
	
	
	
	
	public AboutDialog() {
		// need to set up box layout
		setTitle(GraphicalInterfaceConstants.ABOUT_BOX_TITLE);
		
		add(mostLabel);
		
		class OpenUrlAction implements ActionListener {
			@Override public void actionPerformed(ActionEvent e) {
				try{ 
					String url = GraphicalInterfaceConstants.ABOUT_LICENSE_URL;  
					java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));  
				}  
				catch (java.io.IOException e1) {  
					JOptionPane.showMessageDialog(null,                
							GraphicalInterfaceConstants.HELP_URL_NOT_FOUND_MESSAGE,                
							GraphicalInterfaceConstants.HELP_URL_NOT_FOUND_TITLE,                                
							JOptionPane.ERROR_MESSAGE);   
				}
			}
		}
		
		// based on http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
		// making a button into a link
		JButton button = new JButton();
	    button.setText("<HTML><FONT color=\"#000099\"><U>Licensing Information</U></FONT>"
	        + "</HTML>");
	    button.setHorizontalAlignment(SwingConstants.LEFT);
	    button.setBorderPainted(false);
	    button.setOpaque(false);
	    button.setBackground(Color.WHITE);
	    button.addActionListener(new OpenUrlAction());
	    add(button);	
	    
	    //add(okButton);
		
	}
	
	public static void main(String[] args) {
		AboutDialog d = new AboutDialog();
		d.setSize(200, 150);
		d.setVisible(true);
	}
	
}
