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
	
	JCheckBox AddRemoveCheckBox;  
    JPanel ButtonPanel;  
    JButton OKButton;  
    JButton DetailsButton;
    JPanel StuffPanel;  
	
	private static final long serialVersionUID = 1L;
	public ResizableDialog()  
    {  
        super();  
        initComponents();  
        setVisible(true);  
    }  
                           
    private void initComponents()  
    {  
        StuffPanel = new javax.swing.JPanel();  
        AddRemoveCheckBox = new javax.swing.JCheckBox();  
        ButtonPanel = new javax.swing.JPanel();  
        OKButton = new javax.swing.JButton();  
        DetailsButton = new javax.swing.JButton();  
   
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);  
        AddRemoveCheckBox.setText("Add Stuff!");  
        AddRemoveCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));  
        AddRemoveCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));  
        AddRemoveCheckBox.addItemListener(new java.awt.event.ItemListener()  
        {  
            public void itemStateChanged(java.awt.event.ItemEvent evt)  
            {  
                AddRemoveCheckBoxItemStateChanged(evt);  
            }  
        });  
   
        StuffPanel.add(AddRemoveCheckBox);  
   
        getContentPane().add(StuffPanel, java.awt.BorderLayout.CENTER);  
   
        ButtonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));  
   
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
                //OKButtonActionPerformed(evt);  
            }  
        });  
   
        ButtonPanel.add(DetailsButton);
        
        getContentPane().add(ButtonPanel, java.awt.BorderLayout.SOUTH);  
   
        pack();  
    }  
   
    private void AddRemoveCheckBoxItemStateChanged(java.awt.event.ItemEvent evt)                                                     
    {                                                         
        // Add or remove some stuff!  
        if( AddRemoveCheckBox.isSelected() )  
        {  
            // add stuff  
            for( int i = 0; i < 10; i++ )  
            {  
                JLabel temp = new JLabel("New Stuff " + i);  
                StuffPanel.add(temp);  
            }  
        }  
        else  
        {  
            // remove the stuff  
            Component[] oldStuff = StuffPanel.getComponents();  
            for( Component c : oldStuff )  
            {  
                if( c instanceof JLabel )  
                {  
                    StuffPanel.remove(c);  
                }  
            }  
        }  
          
        // resize with the new stuff!  
        pack();  
    }                                                                                                  
    
    public static void main(String[] args) {
    	ResizableDialog r = new ResizableDialog();
    	r.setVisible(true);
    }
}  
