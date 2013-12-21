package edu.rutgers.MOST.presentation;

import javax.swing.*;  

import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
   
// based on code from http://www.coderanch.com/t/344723/GUI/java/JDialog-resize
public class ResizableDialog extends javax.swing.JDialog  
{  
    /**
	 * 
	 */
	
//	JCheckBox AddRemoveCheckBox;  
    JPanel ButtonPanel;  
    JButton OKButton;  
    JButton DetailsButton;
    JPanel StuffPanel;  
    public boolean messageShown;
	
	private static final long serialVersionUID = 1L;
	public ResizableDialog()  
    {  
        super();  
        initComponents();  
        setVisible(true);  
    }  
                           
    private void initComponents()  
    {  
    	messageShown = false;
//        StuffPanel = new javax.swing.JPanel();  
//        AddRemoveCheckBox = new javax.swing.JCheckBox();  
        ButtonPanel = new javax.swing.JPanel();  
        OKButton = new javax.swing.JButton();  
        DetailsButton = new javax.swing.JButton();  
        StuffPanel = new javax.swing.JPanel();  
//        AddRemoveCheckBox = new javax.swing.JCheckBox(); 
   
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  
//        AddRemoveCheckBox.setText("Add Stuff!");  
//        AddRemoveCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));  
//        AddRemoveCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));  
//        AddRemoveCheckBox.addItemListener(new java.awt.event.ItemListener()  
//        {  
//            public void itemStateChanged(java.awt.event.ItemEvent evt)  
//            {  
//                AddRemoveCheckBoxItemStateChanged(evt);  
//            }  
//        });  
   
//        StuffPanel.add(AddRemoveCheckBox);  
   
        getContentPane().add(StuffPanel, java.awt.BorderLayout.SOUTH);  
   
        ButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));  
   
        OKButton.setText("OK");  
        OKButton.addActionListener(new ActionListener()  
        {  
            public void actionPerformed(ActionEvent evt)  
            {  
            	System.out.println("ok");
            	setVisible(false); 
            }  
        });  
   
        ButtonPanel.add(OKButton);
        
        DetailsButton.setText("Details >>");  
        DetailsButton.addActionListener(new java.awt.event.ActionListener()  
        {  
            public void actionPerformed(java.awt.event.ActionEvent evt)  
            {  
            	// Add or remove some stuff!  
                if(!messageShown) {
                	TextArea textArea = new TextArea();
                	StuffPanel.add(textArea);
                	DetailsButton.setText("<< Details");
                	messageShown = true;
                } else {
                	DetailsButton.setText("Details >>");
                	Component[] oldStuff = StuffPanel.getComponents();  
                    for( Component c : oldStuff )  
                    {   
                        if( c instanceof TextArea )  
                        {  
                            StuffPanel.remove(c);  
                        } 
                    } 
                    messageShown = false;
                }
                              
                // resize with the new stuff!  
                pack();   
            }  
        });  
   
        ButtonPanel.add(DetailsButton);
        
        getContentPane().add(ButtonPanel, java.awt.BorderLayout.CENTER);  
   
        pack();  
    }  
   
//    private void AddRemoveCheckBoxItemStateChanged(java.awt.event.ItemEvent evt)                                                     
//    {                                                         
//        // Add or remove some stuff!  
//        if( AddRemoveCheckBox.isSelected() )  
//        {  
//        	TextArea textArea = new TextArea();
//        	StuffPanel.add(textArea);
//        	DetailsButton.setText("<< Details");
//        	// add stuff  
//            for( int i = 0; i < 10; i++ )  
//            {   
////            	JLabel temp = new JLabel("New Stuff " + i);  
////              StuffPanel.add(temp);  
//            }  
//        }  
//        else  
//        {  
//            // remove the stuff  
//            Component[] oldStuff = StuffPanel.getComponents();  
//            for( Component c : oldStuff )  
//            {  
////                if( c instanceof JLabel )  
////                {  
////                    StuffPanel.remove(c);  
////                }  
//                if( c instanceof TextArea )  
//                {  
//                    StuffPanel.remove(c);  
//                } 
//            }  
//        }  
//          
//        // resize with the new stuff!  
//        pack();  
//    }                                                                                                  
    
    public static void main(String[] args) {
    	ResizableDialog r = new ResizableDialog();
    	r.setVisible(true);
    }
}  
