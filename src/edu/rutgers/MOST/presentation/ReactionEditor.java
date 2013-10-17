package edu.rutgers.MOST.presentation;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import edu.rutgers.MOST.data.MetaboliteFactory;
import edu.rutgers.MOST.logic.ReactionParser;

public class ReactionEditor extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton okButton = new JButton("   OK   ");
	public JButton cancelButton = new JButton("Cancel");
	public JButton clearButton = new JButton(" Clear ");
	public final JTextArea reactionArea = new JTextArea();

	private String reactantString;
	private int numReactantFields;
	private int numProductFields;
	private int numPopulatedReacBoxes;
	private int numPopulatedProdBoxes;
	private String arrowString;
	private String productString;
	private String reactionEquation;
	private String oldReaction;

	public void setReactantString(String reactantString) {
		this.reactantString = reactantString;
	}

	public String getReactantString() {
		return reactantString;
	}

	public void setProductString(String productString) {
		this.productString = productString;
	}

	public String getProductString() {
		return productString;
	}

	public void setNumReactantFields(int numReactantFields) {
		this.numReactantFields = numReactantFields;
	}

	public int getNumReactantFields() {
		return numReactantFields;
	}

	public void setNumProductFields(int numProductFields) {
		this.numProductFields = numProductFields;
	}

	public int getNumProductFields() {
		return numProductFields;
	}

	public void setNumPopulatedReacBoxes(int numPopulatedReacBoxes) {
		this.numPopulatedReacBoxes = numPopulatedReacBoxes;
	}

	public int getNumPopulatedReacBoxes() {
		return numPopulatedReacBoxes;
	}

	public void setNumPopulatedProdBoxes(int numPopulatedProdBoxes) {
		this.numPopulatedProdBoxes = numPopulatedProdBoxes;
	}

	public int getNumPopulatedProdBoxes() {
		return numPopulatedProdBoxes;
	}

	public void setArrowString(String arrowString) {
		this.arrowString = arrowString;
	}

	public String getArrowString() {
		return arrowString;
	}

	public void setReactionEquation(String reactionEquation) {
		this.reactionEquation = reactionEquation;
	}

	public String getReactionEquation() {
		return reactionEquation;
	}
	
	public void setOldReaction(String oldReaction) {
		this.oldReaction = oldReaction;
	}

	public String getOldReaction() {
		return oldReaction;
	}

	// private final JList names;
	public ReactionEditor() {
	setTitle("Reaction Editor");

		//getRootPane().setDefaultButton(okButton);

		//100 an arbitrary number, should be large enough to accommodate the large reaction
		setNumReactantFields(100);
		setNumProductFields(100);
		
		reactionArea.setEditable(false);
		reactionArea.setLineWrap(true);
		
		// create sorted list of metabolites to populate combo boxes
		MetaboliteFactory mFactory = new MetaboliteFactory("SBML");
		final ArrayList<String> metabList = mFactory.metaboliteAbbreviationList();
		Collections.sort(metabList);
		
		int viewRow = GraphicalInterface.reactionsTable.convertRowIndexToModel(GraphicalInterface.getCurrentReactionsRow());
		int id = Integer.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(viewRow, GraphicalInterfaceConstants.REACTIONS_ID_COLUMN));
		String reactionEquation = ((String) GraphicalInterface.reactionsTable.getModel().getValueAt(viewRow, GraphicalInterfaceConstants.REACTION_EQUN_ABBR_COLUMN));
		System.out.println(reactionEquation);
		
		if (((String) GraphicalInterface.reactionsTable.getModel().getValueAt(viewRow, GraphicalInterfaceConstants.REVERSIBLE_COLUMN)).compareTo("true") == 0) {
			setArrowString("<==>");
		} else {
			setArrowString("-->");
		}

		/*************************************************************************/
		//create Box layout
		/*************************************************************************/

		//reactants
		Box hbLabeledReactant[] = new Box[numReactantFields];
		Box vbReactants = Box.createVerticalBox();   

		//reversible box
		Box vbRev = Box.createVerticalBox(); 

		//products
		Box hbLabeledProduct[] = new Box[numReactantFields];
		Box vbProducts = Box.createVerticalBox();   

		Box hbReacProd = Box.createHorizontalBox(); //holds reactants, rev, and products boxes (top box)
		Box hbReaction = Box.createHorizontalBox(); //holds reaction (middle box)  
		Box hbButton = Box.createHorizontalBox(); //holds buttons (bottom box)
		Box vbAll = Box.createVerticalBox();    

		//labels for comboboxes
		String reacNum[] = new String[numReactantFields];
		JLabel reactantLabel[] = new JLabel[numReactantFields];

		String prodNum[] = new String[numReactantFields];
		JLabel productLabel[] = new JLabel[numReactantFields];

		//comboboxes
		//final SizedComboBox cbReactant[] = new SizedComboBox[numReactantFields];    
		//final SizedComboBox cbProduct[] = new SizedComboBox[numProductFields];
		final JComboBox cbReactant[] = new JComboBox[numReactantFields];    
		final JComboBox cbProduct[] = new JComboBox[numProductFields];

		final JTextField reactantCoeffField[] = new JTextField[numReactantFields];
		final JTextField reactantEditor[] = new JTextField[numReactantFields];
		final JTextField productCoeffField[] = new JTextField[numProductFields];    
		final JTextField productEditor[] = new JTextField[numProductFields];

		JPanel panelReactants = new JPanel();
		panelReactants.setLayout(new BoxLayout(panelReactants, BoxLayout.Y_AXIS));

		panelReactants.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		JPanel panelProducts = new JPanel();
		panelProducts.setLayout(new BoxLayout(panelProducts, BoxLayout.Y_AXIS));

		panelProducts.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		final JRadioButton trueButton = new JRadioButton("True");
		final JRadioButton falseButton = new JRadioButton("False");

		//create reactant combo boxes
		for (int i = 0; i < numReactantFields; i++) {
			//cbReactant[i] = new SizedComboBox();
			cbReactant[i] = new JComboBox<String>();
			cbReactant[i].setEditable(true);
			cbReactant[i].setEnabled(false);
			
			reactantCoeffField[i] = new JTextField();
			reactantCoeffField[i].setPreferredSize(new Dimension(40, 25));
			cbReactant[i].setPreferredSize(new Dimension(200, 25));

			//create label
			reacNum[i] = "Reactant " + (i + 1) + "     ";

			reactantLabel[i] = new JLabel();
			reactantLabel[i].setText(reacNum[i]);
			reactantLabel[i].setAlignmentX(CENTER_ALIGNMENT);

			//create reactant box
			hbLabeledReactant[i] = Box.createHorizontalBox();
			//hbReactant[i] = Box.createHorizontalBox(); 
			
			JPanel panelReactant[] = new JPanel[numReactantFields];    
			panelReactant[i] = new JPanel();
			panelReactant[i].setLayout(new BoxLayout(panelReactant[i], BoxLayout.X_AXIS));
			panelReactant[i].add(reactantLabel[i]);
			panelReactant[i].add(reactantCoeffField[i]);
			panelReactant[i].add(cbReactant[i]);
			panelReactant[i].setBorder(BorderFactory.createEmptyBorder(20,20,0,20));

			hbLabeledReactant[i].add(panelReactant[i]);

			//add labeled boxes to reactants panel
			panelReactants.add(hbLabeledReactant[i]);
					
			// add reactant action listener
			ActionListener reactantsActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					String reactantSelection[] = new String[numReactantFields];
					String reactant[] = new String[numReactantFields];
					ArrayList<String> tempReactantsList = new ArrayList<String>();
					for (int h = 0; h < numReactantFields; h++) {
						reactant[h] = "";

						reactantSelection[h] = (String) cbReactant[h].getSelectedItem();
						// TODO: check if second condition necessary
						if (reactantSelection[h] != null && reactantSelection[h].length() > 0) {
							if (reactantCoeffField[h].getText().length() > 0) {
								reactant[h] = reactantCoeffField[h].getText() + " " + reactantSelection[h];
							} else {
								reactant[h] = reactantSelection[h];
							}
							tempReactantsList.add(reactant[h]);
						}      	    	      
					} 
					//set reaction equation into text box
					String reacString = "";

					for (int i = 0; i < tempReactantsList.size(); i++) {
						if (i == 0) {
							reacString += (String) tempReactantsList.get(i);
						} else {
							reacString += " + " + (String) tempReactantsList.get(i);
						}
					}

					setReactantString(reacString);
					if (getReactantString() == null || getReactantString().length() == 0) {
						reactionArea.setText(getArrowString() + " " + getProductString());
						okButton.setEnabled(true);
					} else if (getProductString() == null || getProductString().length() == 0) {
						reactionArea.setText(getReactantString() + " " + getArrowString());
						okButton.setEnabled(true);
					} else {
						reactionArea.setText(getReactantString() + " " + getArrowString() + " " + getProductString());
						okButton.setEnabled(true);
					}
					ReactionParser parser = new ReactionParser();
					if (reactionArea.getText() != null && parser.isValid(reactionArea.getText())) {
						parser.reactionList(reactionArea.getText().trim());
						if ((getNumPopulatedReacBoxes() + 1) < getNumReactantFields()) {
							int index = parser.getEquation().getReactants().size();
							if (index == getNumPopulatedReacBoxes()) {
								for (int m = 0; m < metabList.size(); m++) {
									cbReactant[index].addItem(metabList.get(m));
								}
								cbReactant[index].setEnabled(true);
								cbReactant[index].setSelectedIndex(-1);
								reactantEditor[index] = new JTextField();
								reactantEditor[index] = (JTextField)cbReactant[index].getEditor().getEditorComponent();
								reactantEditor[index].addKeyListener(new ComboKeyHandler(cbReactant[index]));
								setNumPopulatedReacBoxes(index + 1);
							}
						}
					}
				}
			};
			reactantCoeffField[i].addActionListener(reactantsActionListener);
			cbReactant[i].addActionListener(reactantsActionListener);			
		} 

		//end reactant combo boxes
		/***************************************************************************/

		//create product combo boxes
		for (int j = 0; j < numProductFields; j++) {
			//cbProduct[j] = new SizedComboBox();
			cbProduct[j] = new JComboBox<String>();
			cbProduct[j].setEditable(true); 
			cbProduct[j].setEnabled(false);
			
			productCoeffField[j] = new JTextField();
			productCoeffField[j].setPreferredSize(new Dimension(40, 25));
			cbProduct[j].setPreferredSize(new Dimension(200, 25));        

			//create label
			prodNum[j] = "Product " + (j + 1) + "     ";

			productLabel[j] = new JLabel();
			productLabel[j].setText(prodNum[j]);
			productLabel[j].setAlignmentX(CENTER_ALIGNMENT);
			
			//create product box
			hbLabeledProduct[j] = Box.createHorizontalBox();
			//hbProduct[j] = Box.createHorizontalBox(); 

			JPanel panelProduct[] = new JPanel[numProductFields];    
			panelProduct[j] = new JPanel();
			panelProduct[j].setLayout(new BoxLayout(panelProduct[j], BoxLayout.X_AXIS));
			panelProduct[j].add(productLabel[j]);
			panelProduct[j].add(productCoeffField[j]);
			panelProduct[j].add(cbProduct[j]);
			panelProduct[j].setBorder(BorderFactory.createEmptyBorder(20,20,0,20));

			hbLabeledProduct[j].add(panelProduct[j]);

			//add labeled boxes to products panel
			panelProducts.add(hbLabeledProduct[j]);
			
			cbProduct[j].setSelectedIndex(-1);
			productEditor[j] = new JTextField();
			productEditor[j] = (JTextField)cbProduct[j].getEditor().getEditorComponent();
			productEditor[j].addKeyListener(new ComboKeyHandler(cbProduct[j]));
			
			// add product action listener
			ActionListener productsActionListener = new ActionListener() {
				public void actionPerformed(ActionEvent actionEvent) {
					String productSelection[] = new String[numProductFields];
					String product[] = new String[numProductFields];
					ArrayList<String> tempProductsList = new ArrayList<String>();
					for (int h = 0; h < numProductFields; h++) {
						product[h] = "";	      
						productSelection[h] = (String) cbProduct[h].getSelectedItem();
						if (productSelection[h] != null) {
							if (productCoeffField[h].getText().length() > 0) {
								product[h] = productCoeffField[h].getText() + " " + productSelection[h];
							} else {
								product[h] = productSelection[h];
							}
							tempProductsList.add(product[h]);
						}      	    	      
					}
					//set reaction equation into text box
					String prodString = "";

					for (int i = 0; i < tempProductsList.size(); i++) {
						if (i == 0) {
							prodString += (String) tempProductsList.get(i);
						} else {
							prodString += " + " + (String) tempProductsList.get(i);
						}
					}

					setProductString(prodString);
					if (getReactantString() == null || getReactantString().length() == 0) {
						reactionArea.setText(getArrowString() + " " + prodString);
						okButton.setEnabled(true);
					} else if (getProductString() == null || getProductString().length() == 0) {
						reactionArea.setText(getReactantString() + " " + getArrowString());
						okButton.setEnabled(true);
					} else {
						reactionArea.setText(getReactantString() + " " + getArrowString() + " " + prodString);
						okButton.setEnabled(true);
					}					
					ReactionParser parser = new ReactionParser();
					if (reactionArea.getText() != null && parser.isValid(reactionArea.getText())) {
						parser.reactionList(reactionArea.getText().trim());
						if ((getNumPopulatedProdBoxes() + 1) < getNumProductFields()) {
							int index = parser.getEquation().getProducts().size();
							if (index == getNumPopulatedProdBoxes()) {
								for (int m = 0; m < metabList.size(); m++) {
									cbProduct[index].addItem(metabList.get(m));
								}
								cbProduct[index].setEnabled(true);
								cbProduct[index].setSelectedIndex(-1);
								productEditor[index] = new JTextField();
								productEditor[index] = (JTextField)cbProduct[index].getEditor().getEditorComponent();
								productEditor[index].addKeyListener(new ComboKeyHandler(cbProduct[index]));
								setNumPopulatedProdBoxes(index + 1);
							}
						}
					}
				}
			};
			productCoeffField[j].addActionListener(productsActionListener);
			cbProduct[j].addActionListener(productsActionListener);			
		} 
		//end product combo boxes
		/***************************************************************************/
		
		//add reactants panel to scrollpane
		JScrollPane reactantPane = new JScrollPane(panelReactants);
		//reactantPane.setPreferredSize(new Dimension(300, 600));
		vbReactants.add(reactantPane);		
		//set scroll speed
		reactantPane.getHorizontalScrollBar().setUnitIncrement(30);

		JScrollPane productPane = new JScrollPane(panelProducts);
		//productPane.setPreferredSize(new Dimension(300, 600));
		vbProducts.add(productPane);
		productPane.getHorizontalScrollBar().setUnitIncrement(30);

		//Reversible panel     
		JLabel labelRev = new JLabel("Reversible");

		JPanel panelRev = new JPanel();
		panelRev.setLayout(new BoxLayout(panelRev, BoxLayout.Y_AXIS));

		panelRev.add(trueButton);
		panelRev.add(falseButton);

		//trueButton.setMnemonic(KeyEvent.VK_T);//bug - clears first reactant in reactionEquation
		trueButton.setEnabled(true);  
		//falseButton.setMnemonic(KeyEvent.VK_F);//bug - clears first reactant in reactionEquation
		falseButton.setEnabled(true); 

		if (((String) GraphicalInterface.reactionsTable.getModel().getValueAt(GraphicalInterface.getCurrentReactionsRow(), GraphicalInterfaceConstants.REVERSIBLE_COLUMN)).compareTo("true") == 0) {
			trueButton.setSelected(true);
		} else {
			falseButton.setSelected(true);
		} 

		vbRev.add(labelRev);
		vbRev.add(panelRev);

		ButtonGroup bg = new ButtonGroup();
		bg.add(trueButton);
		bg.add(falseButton);    

		//add reactants, reversible, and products boxes to reacprod (top) box
		hbReacProd.add(vbReactants);
		hbReacProd.add(vbRev);
		hbReacProd.add(vbProducts);  

		//add reaction field to scroll pane		
		JScrollPane reactionPane = new JScrollPane(reactionArea);
		reactionPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,20));
		reactionPane.setPreferredSize(new Dimension(200, 400));

		//add reaction pane to reaction (middle) box
		hbReaction.add(reactionPane);

		okButton.setMnemonic(KeyEvent.VK_O);
		JLabel blank = new JLabel("      ");    
		clearButton.setMnemonic(KeyEvent.VK_E);
		JLabel blank2 = new JLabel("      ");
		cancelButton.setMnemonic(KeyEvent.VK_C);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		lowerPanel.add(okButton);
		lowerPanel.add(blank);
		lowerPanel.add(cancelButton);
		lowerPanel.add(blank2);		
		lowerPanel.add(clearButton);
		lowerPanel.setBorder(BorderFactory.createEmptyBorder(0,20,10,20));

		hbButton.add(lowerPanel);

		vbAll.add(hbReacProd);
		vbAll.add(hbReaction);
		vbAll.add(lowerPanel);
		add(vbAll);

		/*****************************************************************************/
		//end create box layout
		/*****************************************************************************/
		
		/*****************************************************************************/
		//populate text fields of combo boxes with existing reactants and products and
		//create reaction string from these species
		/*****************************************************************************/

		ReactionParser parser = new ReactionParser();
		if (reactionEquation != null && parser.isValid(reactionEquation)) {
			// TODO: determine if needed
			setOldReaction(reactionEquation);
			parser.reactionList(reactionEquation);
			int index = parser.getEquation().getReactants().size();
			System.out.println(index);
			for (int i = 0; i < index; i++) {
				System.out.println("editor" + parser.getEquation().getReactants().get(i).toString());
			}
			
			//ArrayList<ArrayList<String>> reactants = parser.reactionList(reactionEquation.trim()).get(0);
			//reactions of the type ==> b will have a reactants list of size 0
			if (index == 0) {	
				for (int m = 0; m < metabList.size(); m++) {
					cbReactant[0].addItem(metabList.get(m));
				}
				cbReactant[0].setEnabled(true);
				cbReactant[0].setSelectedIndex(-1);
				reactantEditor[0] = new JTextField();
				reactantEditor[0] = (JTextField)cbReactant[0].getEditor().getEditorComponent();
				reactantEditor[0].addKeyListener(new ComboKeyHandler(cbReactant[0]));
				setNumPopulatedReacBoxes(1);
			} else {
				for (int r = 0; r < index; r++) {
					for (int m = 0; m < metabList.size(); m++) {
						cbReactant[r].addItem(metabList.get(m));
					}
					cbReactant[r].setEnabled(true);
					cbReactant[r].setSelectedIndex(-1);
					reactantEditor[r] = new JTextField();
					reactantEditor[r] = (JTextField)cbReactant[r].getEditor().getEditorComponent();
					reactantEditor[r].addKeyListener(new ComboKeyHandler(cbReactant[r]));
					setNumPopulatedReacBoxes(r);
					System.out.println("editor" + parser.getEquation().getReactants().get(r).toString());
					String stoicStr = Double.toString(parser.getEquation().getReactants().get(r).getStoic());
					if (!(Double.valueOf(stoicStr) == 1)) {
						if (stoicStr.endsWith(".0")) {
							stoicStr = stoicStr.substring(0, stoicStr.length() - 2);
						}
						reactantCoeffField[r].setText(stoicStr);
					}
					String reactant = parser.getEquation().getReactants().get(r).getMetaboliteAbbreviation();
					cbReactant[r].setSelectedItem(reactant);
				}
				if (index < getNumReactantFields()) {
					for (int m = 0; m < metabList.size(); m++) {
						cbReactant[index].addItem(metabList.get(m));
					}
					cbReactant[index].setEnabled(true);
					cbReactant[index].setSelectedIndex(-1);
					reactantEditor[index] = new JTextField();
					reactantEditor[index] = (JTextField)cbReactant[index].getEditor().getEditorComponent();
					reactantEditor[index].addKeyListener(new ComboKeyHandler(cbReactant[index]));
					setNumPopulatedReacBoxes(index + 1);
				}
			}
			int index2 = parser.getEquation().getProducts().size();
			//ArrayList<ArrayList<String>> products = parser.reactionList(reactionEquation.trim()).get(1);
			//reactions of the type a ==> will will have a products list of size 0
			if (index2 == 0) {
				for (int m = 0; m < metabList.size(); m++) {
					cbProduct[0].addItem(metabList.get(m));
				}
				cbProduct[0].setEnabled(true);
				cbProduct[0].setSelectedIndex(-1);
				productEditor[0] = new JTextField();
				productEditor[0] = (JTextField)cbProduct[0].getEditor().getEditorComponent();
				productEditor[0].addKeyListener(new ComboKeyHandler(cbProduct[0]));
				setNumPopulatedProdBoxes(1);
			} else {
				for (int p = 0; p < index2; p++) {
					for (int m = 0; m < metabList.size(); m++) {
						cbProduct[p].addItem(metabList.get(m));
					}
					cbProduct[p].setEnabled(true);
					cbProduct[p].setSelectedIndex(-1);
					productEditor[p] = new JTextField();
					productEditor[p] = (JTextField)cbProduct[p].getEditor().getEditorComponent();
					productEditor[p].addKeyListener(new ComboKeyHandler(cbProduct[p]));
					String stoicStr = Double.toString(parser.getEquation().getProducts().get(p).getStoic());
					if (!(Double.valueOf(stoicStr) == 1)) {
						if (stoicStr.endsWith(".0")) {
							stoicStr = stoicStr.substring(0, stoicStr.length() - 2);
						}
						productCoeffField[p].setText(stoicStr);
					}
					String product = parser.getEquation().getProducts().get(p).getMetaboliteAbbreviation();
					cbProduct[p].setSelectedItem(product);
					setNumPopulatedProdBoxes(p);
				}
				if (index2 < getNumProductFields()) {
					for (int m = 0; m < metabList.size(); m++) {
						cbProduct[index2].addItem(metabList.get(m));
					}
					cbProduct[index2].setEnabled(true);
					cbProduct[index2].setSelectedIndex(-1);
					productEditor[index2] = new JTextField();
					productEditor[index2] = (JTextField)cbProduct[index2].getEditor().getEditorComponent();
					productEditor[index2].addKeyListener(new ComboKeyHandler(cbProduct[index2]));
					setNumPopulatedProdBoxes(index2 + 1);
				}
			}
		} else {
			for (int m = 0; m < metabList.size(); m++) {
				cbReactant[0].addItem(metabList.get(m));
			}
			cbReactant[0].setEnabled(true);
			cbReactant[0].setSelectedIndex(-1);
			reactantEditor[0] = new JTextField();
			reactantEditor[0] = (JTextField)cbReactant[0].getEditor().getEditorComponent();
			reactantEditor[0].addKeyListener(new ComboKeyHandler(cbReactant[0]));
			setNumPopulatedReacBoxes(1);
			
			for (int m = 0; m < metabList.size(); m++) {
				cbProduct[0].addItem(metabList.get(m));
			}
			cbProduct[0].setEnabled(true);
			cbProduct[0].setSelectedIndex(-1);
			productEditor[0] = new JTextField();
			productEditor[0] = (JTextField)cbProduct[0].getEditor().getEditorComponent();
			productEditor[0].addKeyListener(new ComboKeyHandler(cbProduct[0]));
			setNumPopulatedProdBoxes(1);
		}
		
		/*****************************************************************************/
		// end populate text fields of combo boxes
		/*****************************************************************************/
		
		/*****************************************************************************/
		// button listeners
		/*****************************************************************************/

		ActionListener revActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent revActionEvent) {
				//ReactionFactory rFactory = new ReactionFactory("SBML");			
				cbProduct[0].grabFocus();
				if (trueButton.isSelected()) {
					setArrowString(" <==> ");
				} else if (falseButton.isSelected()) {
					setArrowString("-->");
				}
				if (getReactantString()!= null && getProductString() != null) {
					reactionArea.setText(getReactantString() + " " + getArrowString() + " " + getProductString());	
				} else if (getReactantString()!= null) {
					reactionArea.setText(getReactantString() + " " + getArrowString());
				} else {
					reactionArea.setText("");
				}
			}
		};

		ActionListener cancelButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {
				setVisible(false);
				dispose();
			}
		}; 
		
		// TODO: check functionality w/lazy instantiation, keep populated combos that are populated
		ActionListener clearButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {				    	  
				for (int i = 0; i < numReactantFields; i++) {
					//cbReacCoeff[i].setSelectedIndex(-1);
					cbReactant[i].setSelectedIndex(-1);
					cbProduct[i].setSelectedIndex(-1);
				}
			
				reactionArea.setText("");
				okButton.setEnabled(false);
				trueButton.setSelected(false);
				falseButton.setSelected(true);    	    
				//sends cursor to first reactant coefficient combobox
				cbReactant[0].grabFocus();
			}
		};        

		trueButton.addActionListener(revActionListener);
		falseButton.addActionListener(revActionListener);

		clearButton.addActionListener(clearButtonActionListener);
		cancelButton.addActionListener(cancelButtonActionListener);	
		
	}

}


