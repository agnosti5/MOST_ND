package edu.rutgers.MOST.presentation;

import javax.swing.*;  

import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
   
// based on code from http://www.coderanch.com/t/344723/GUI/java/JDialog-resize
public class ResizableDialog extends javax.swing.JDialog  
{  
    /**
	 * 
	 */
	 
	private JPanel LabelPanel;
    private JPanel ButtonPanel;  
    private JButton OKButton;  
    private JButton DetailsButton;
    private JPanel MessagePanel;  
    private JLabel Label;
    public boolean messageShown;
    
    private String errorTitle;

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	private String errorDescription;
	
	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private static final long serialVersionUID = 1L;
	public ResizableDialog()  
    {  
        super();  
        initComponents();  
    }  
                           
    private void initComponents()  
    {  
    	setErrorTitle("Error");
    	setErrorDescription("Testing a long, long, long, long error message");
    	setErrorMessage("Testing\nTesting");
    	
    	messageShown = false;
    	LabelPanel = new javax.swing.JPanel(); 
    	Label = new javax.swing.JLabel(); 
        ButtonPanel = new javax.swing.JPanel();  
        OKButton = new javax.swing.JButton();  
        DetailsButton = new javax.swing.JButton();  
        MessagePanel = new javax.swing.JPanel();       
              
        getRootPane().setDefaultButton(OKButton);
        
        setTitle(getErrorTitle());
        Label.setText(getErrorDescription());
   
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  
   
        LabelPanel.add(Label);
        getContentPane().add(LabelPanel, java.awt.BorderLayout.NORTH); 
        getContentPane().add(MessagePanel, java.awt.BorderLayout.SOUTH);  
   
        ButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));  
   
        OKButton.setText("  OK  ");  
        OKButton.addActionListener(new ActionListener()  
        {  
            public void actionPerformed(ActionEvent evt)  
            {  
            	setVisible(false); 
            }  
        });  
   
        ButtonPanel.add(OKButton);
        
        DetailsButton.setText("Details >>"); 
        DetailsButton.setMnemonic(KeyEvent.VK_D);
        DetailsButton.addActionListener(new java.awt.event.ActionListener()  
        {  
            public void actionPerformed(java.awt.event.ActionEvent evt)  
            {  
            	// Add or remove some stuff!  
                if(!messageShown) {
                	TextArea textArea = new TextArea();
                	MessagePanel.add(textArea);
                	DetailsButton.setText("<< Details");
                	textArea.setText(getErrorMessage());
                	messageShown = true;
                } else {
                	DetailsButton.setText("Details >>");
                	Component[] old = MessagePanel.getComponents();  
                    for( Component c : old )  
                    {   
                        if( c instanceof TextArea )  
                        {  
                        	MessagePanel.remove(c);  
                        } 
                    } 
                    messageShown = false;
                }
                              
                // resize with the new components  
                pack();   
            }  
        });  
   
        ButtonPanel.add(DetailsButton);
        
        getContentPane().add(ButtonPanel, java.awt.BorderLayout.CENTER);  
   
        pack();  
    }                                                                                                  
    
    public static void main(String[] args) {
    	final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon("etc/most16.jpg").getImage()); 
		icons.add(new ImageIcon("etc/most32.jpg").getImage());
    	
    	ResizableDialog r = new ResizableDialog();
    	r.setIconImages(icons);
    	r.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	r.setLocationRelativeTo(null);
    	r.setVisible(true);
    }
}  
