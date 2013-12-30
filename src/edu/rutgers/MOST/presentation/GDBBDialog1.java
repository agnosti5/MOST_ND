package edu.rutgers.MOST.presentation;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class GDBBDialog1  extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField numKnockoutsField = new JTextField();
	private JComboBox<String> cbNumThreads = new JComboBox<String>();
	private SizedComboBox cbSynObj = new SizedComboBox();
	private SizedComboBox cbSynObj1 = new SizedComboBox();
	private SizedComboBox cbSynObj2 = new SizedComboBox();

	private JButton startButton = new JButton("Start");
	private JButton stopButton = new JButton("Stop");
	private JRadioButton indefiniteTimeButton = new JRadioButton(GDBBConstants.INDEFINITE_TIME_LABEL);
	private JRadioButton finiteTimeButton = new JRadioButton(GDBBConstants.FINITE_TIME_LABEL);
	private JLabel blankLabel = new JLabel("");
	private JTextField finiteTimeField = new JTextField();
	private JLabel counterLabel = new JLabel();

	public GDBBDialog1() {
		
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon("etc/most16.jpg").getImage()); 
		icons.add(new ImageIcon("etc/most32.jpg").getImage());
		
		getRootPane().setDefaultButton(startButton);

		setTitle(GDBBConstants.GDBB_DIALOG_TITLE);
		//setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			
		cbNumThreads.setEditable(false);
		cbSynObj.setEditable(false);
		
		cbSynObj.addItem(GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES[GraphicalInterfaceConstants.SYNTHETIC_OBJECTIVE_COLUMN]);
		cbSynObj.addItem(GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES[GraphicalInterfaceConstants.BIOLOGICAL_OBJECTIVE_COLUMN]);		

		numKnockoutsField.setPreferredSize(new Dimension(150, 25));
		numKnockoutsField.setMaximumSize(new Dimension(150, 25));
		numKnockoutsField.setMinimumSize(new Dimension(150, 25));

		cbNumThreads.setPreferredSize(new Dimension(150, 25));
		cbNumThreads.setMaximumSize(new Dimension(150, 25));
		cbNumThreads.setMinimumSize(new Dimension(150, 25));

		cbSynObj.setPreferredSize(new Dimension(150, 25));
		cbSynObj.setMaximumSize(new Dimension(150, 25));
		cbSynObj.setMinimumSize(new Dimension(150, 25));
		
		blankLabel.setPreferredSize(new Dimension(150, 25));
		blankLabel.setMaximumSize(new Dimension(150, 25));
		blankLabel.setMinimumSize(new Dimension(150, 25));
		
		finiteTimeField.setPreferredSize(new Dimension(150, 25));
		finiteTimeField.setMaximumSize(new Dimension(150, 25));
		finiteTimeField.setMinimumSize(new Dimension(150, 25));
		
		//box layout
		Box vb = Box.createVerticalBox();
   	    
		Box hbTopLabel = Box.createHorizontalBox();
		Box hbTop = Box.createHorizontalBox();
		Box hbNumKnockoutsLabel = Box.createHorizontalBox();	    
		Box hbNumKnockouts = Box.createHorizontalBox();
		Box hbNumThreadsLabel = Box.createHorizontalBox();	    
		Box hbMetabolite = Box.createHorizontalBox();
		Box hbSynObjLabel = Box.createHorizontalBox();	    
		Box hbSynObj = Box.createHorizontalBox();
		Box hbSynObjLabel1 = Box.createHorizontalBox();	    
//		Box hbSynObj1 = Box.createHorizontalBox();
		Box hbSynObjLabel2 = Box.createHorizontalBox();	    
//		Box hbIndefiniteTime = Box.createHorizontalBox();	    
		Box hbBlankLabel = Box.createHorizontalBox();
//		Box hbFiniteTime = Box.createHorizontalBox();	    
		Box hbFiniteTimeField = Box.createHorizontalBox();
		
		Box vbLabels = Box.createVerticalBox();
		Box vbCombos = Box.createVerticalBox();

		Box hbLabeledComponents = Box.createHorizontalBox();
		Box hbButton = Box.createHorizontalBox();
		Box hbCounterLabel = Box.createHorizontalBox();

		//top label
		JLabel topLabel = new JLabel("");
		topLabel.setSize(new Dimension(300, 10));
		//top, left, bottom. right
		topLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		topLabel.setAlignmentX(LEFT_ALIGNMENT);

		hbTop.add(topLabel);	
		hbTop.setAlignmentX(LEFT_ALIGNMENT);

		hbTopLabel.add(hbTop);
		
		//Number of Knockouts Label and combo
		JLabel numKnockoutsLabel = new JLabel();
		numKnockoutsLabel.setText(GDBBConstants.NUM_KNOCKOUTS_LABEL);
		numKnockoutsLabel.setPreferredSize(new Dimension(200, 25));
		numKnockoutsLabel.setMaximumSize(new Dimension(200, 25));
		numKnockoutsLabel.setMinimumSize(new Dimension(200, 25));
		numKnockoutsLabel.setBorder(BorderFactory.createEmptyBorder(10,0,ColumnInterfaceConstants.LABEL_BOTTOM_BORDER_SIZE,10));
		numKnockoutsLabel.setAlignmentX(LEFT_ALIGNMENT);
		//numKnockoutsLabel.setAlignmentY(TOP_ALIGNMENT);	    	    

		JPanel panelNumKnockoutsLabel = new JPanel();
		panelNumKnockoutsLabel.setLayout(new BoxLayout(panelNumKnockoutsLabel, BoxLayout.X_AXIS));
		panelNumKnockoutsLabel.add(numKnockoutsLabel);
		panelNumKnockoutsLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		hbNumKnockoutsLabel.add(panelNumKnockoutsLabel);
		hbNumKnockoutsLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelNumKnockouts = new JPanel();
		panelNumKnockouts.setLayout(new BoxLayout(panelNumKnockouts, BoxLayout.X_AXIS));
		panelNumKnockouts.add(numKnockoutsField);
		panelNumKnockouts.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		panelNumKnockouts.setAlignmentX(RIGHT_ALIGNMENT);

		hbNumKnockouts.add(panelNumKnockouts);
		hbNumKnockouts.setAlignmentX(RIGHT_ALIGNMENT);

		vbLabels.add(hbNumKnockoutsLabel);
		JLabel blankLabel1 = new JLabel("");
		vbLabels.add(blankLabel1);
		vbCombos.add(hbNumKnockouts);

		//Number of Threads Label and combo
		JLabel numThreadsLabel = new JLabel();
		numThreadsLabel.setText(GDBBConstants.NUM_THREADS_LABEL);
		numThreadsLabel.setPreferredSize(new Dimension(200, 25));
		numThreadsLabel.setMaximumSize(new Dimension(200, 25));
		numThreadsLabel.setMinimumSize(new Dimension(200, 25));
		numThreadsLabel.setBorder(BorderFactory.createEmptyBorder(ColumnInterfaceConstants.LABEL_TOP_BORDER_SIZE,0,ColumnInterfaceConstants.LABEL_BOTTOM_BORDER_SIZE,10));
		numThreadsLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelNumThreadsLabel = new JPanel();
		panelNumThreadsLabel.setLayout(new BoxLayout(panelNumThreadsLabel, BoxLayout.X_AXIS));
		panelNumThreadsLabel.add(numThreadsLabel);
		panelNumThreadsLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		hbNumThreadsLabel.add(panelNumThreadsLabel);
		hbNumThreadsLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelNumThreads = new JPanel();
		panelNumThreads.setLayout(new BoxLayout(panelNumThreads, BoxLayout.X_AXIS));
		panelNumThreads.add(cbNumThreads);
		panelNumThreads.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		panelNumThreads.setAlignmentX(RIGHT_ALIGNMENT);

		hbMetabolite.add(panelNumThreads);
		hbMetabolite.setAlignmentX(RIGHT_ALIGNMENT);

		vbLabels.add(hbNumThreadsLabel);
		JLabel blankLabel2 = new JLabel("");
		vbLabels.add(blankLabel2);
		vbCombos.add(hbMetabolite);

		//synObj label and combo
		JLabel synObjLabel = new JLabel();
		synObjLabel.setText(GDBBConstants.SYN_OBJ_COLUMN_LABEL);
		synObjLabel.setPreferredSize(new Dimension(200, 25));
		synObjLabel.setMaximumSize(new Dimension(200, 25));
		synObjLabel.setMinimumSize(new Dimension(200, 25));
		synObjLabel.setBorder(BorderFactory.createEmptyBorder(ColumnInterfaceConstants.LABEL_TOP_BORDER_SIZE,0,ColumnInterfaceConstants.LABEL_BOTTOM_BORDER_SIZE,10));
		synObjLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelSynObjLabel = new JPanel();
		panelSynObjLabel.setLayout(new BoxLayout(panelSynObjLabel, BoxLayout.X_AXIS));
		panelSynObjLabel.add(synObjLabel);
		panelSynObjLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		hbSynObjLabel.add(panelSynObjLabel);
		hbSynObjLabel.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelSynObj = new JPanel();
		panelSynObj.setLayout(new BoxLayout(panelSynObj, BoxLayout.X_AXIS));
		panelSynObj.add(cbSynObj);
		panelSynObj.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		panelSynObj.setAlignmentX(RIGHT_ALIGNMENT);

		hbSynObj.add(panelSynObj);
		hbSynObj.setAlignmentX(RIGHT_ALIGNMENT);

		vbLabels.add(hbSynObjLabel);
		JLabel blankLabel3 = new JLabel("");
		vbLabels.add(blankLabel3);
		vbCombos.add(hbSynObj);
		
		//synObj1 label and combo
		JLabel synObjLabel1 = new JLabel();
		synObjLabel1.setText("test");
		synObjLabel1.setPreferredSize(new Dimension(200, 25));
		synObjLabel1.setMaximumSize(new Dimension(200, 25));
		synObjLabel1.setMinimumSize(new Dimension(200, 25));
		synObjLabel1.setBorder(BorderFactory.createEmptyBorder(ColumnInterfaceConstants.LABEL_TOP_BORDER_SIZE,0,ColumnInterfaceConstants.LABEL_BOTTOM_BORDER_SIZE,10));
		synObjLabel1.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelSynObjLabel1 = new JPanel();
		panelSynObjLabel1.setLayout(new BoxLayout(panelSynObjLabel1, BoxLayout.X_AXIS));
		panelSynObjLabel1.add(synObjLabel1);
		panelSynObjLabel1.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		hbSynObjLabel1.add(panelSynObjLabel1);
		hbSynObjLabel1.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelBlankLabel = new JPanel();
		panelBlankLabel.setLayout(new BoxLayout(panelBlankLabel, BoxLayout.X_AXIS));
		panelBlankLabel.add(blankLabel);
		panelBlankLabel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		panelBlankLabel.setAlignmentX(RIGHT_ALIGNMENT);

		hbBlankLabel.add(panelBlankLabel);
		hbBlankLabel.setAlignmentX(RIGHT_ALIGNMENT);

		vbLabels.add(hbSynObjLabel1);
		JLabel blankLabel4 = new JLabel("");
		vbLabels.add(blankLabel4);
		vbCombos.add(hbBlankLabel);
		
		//synObj2 label and combo
		JLabel synObjLabel2 = new JLabel();
		synObjLabel2.setText("test");
		synObjLabel2.setPreferredSize(new Dimension(200, 25));
		synObjLabel2.setMaximumSize(new Dimension(200, 25));
		synObjLabel2.setMinimumSize(new Dimension(200, 25));
		synObjLabel2.setBorder(BorderFactory.createEmptyBorder(ColumnInterfaceConstants.LABEL_TOP_BORDER_SIZE,0,ColumnInterfaceConstants.LABEL_BOTTOM_BORDER_SIZE,10));
		synObjLabel2.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelSynObjLabel2 = new JPanel();
		panelSynObjLabel2.setLayout(new BoxLayout(panelSynObjLabel2, BoxLayout.X_AXIS));
		panelSynObjLabel2.add(synObjLabel2);
		panelSynObjLabel2.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		hbSynObjLabel2.add(panelSynObjLabel2);
		hbSynObjLabel2.setAlignmentX(LEFT_ALIGNMENT);

		JPanel panelFiniteTimeField = new JPanel();
		panelFiniteTimeField.setLayout(new BoxLayout(panelFiniteTimeField, BoxLayout.X_AXIS));
		panelFiniteTimeField.add(finiteTimeField);
		panelFiniteTimeField.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		panelFiniteTimeField.setAlignmentX(RIGHT_ALIGNMENT);

		hbFiniteTimeField.add(panelFiniteTimeField);
		hbFiniteTimeField.setAlignmentX(RIGHT_ALIGNMENT);

		vbLabels.add(hbSynObjLabel2);
		JLabel blankLabel5 = new JLabel("");
		vbLabels.add(blankLabel5);
		vbCombos.add(hbFiniteTimeField);
		
		startButton.setMnemonic(KeyEvent.VK_S);
		JLabel blank = new JLabel("    "); 
		stopButton.setMnemonic(KeyEvent.VK_T);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(startButton);
		buttonPanel.add(blank);
		buttonPanel.add(stopButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,20,20,20));

		hbButton.add(buttonPanel);
		
		JLabel counterLabel = new JLabel(GDBBConstants.COUNTER_LABEL_PREFIX + "0" + GDBBConstants.COUNTER_LABEL_SUFFIX);
		hbCounterLabel.add(counterLabel);

		vb.add(hbTopLabel);
		hbLabeledComponents.add(vbLabels);
		hbLabeledComponents.add(vbCombos);
		vb.add(hbLabeledComponents);		
		vb.add(hbButton);
		vb.add(hbCounterLabel);

		add(vb);
		
		ActionListener startButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {
				
			}
		};

		startButton.addActionListener(startButtonActionListener);

		ActionListener stopButtonActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent prodActionEvent) {
				
			}
		};

		stopButton.addActionListener(stopButtonActionListener);

	} 	 
	
	public static void main(String[] args) throws Exception {
		//based on code from http:stackoverflow.com/questions/6403821/how-to-add-an-image-to-a-jframe-title-bar
		final ArrayList<Image> icons = new ArrayList<Image>(); 
		icons.add(new ImageIcon("etc/most16.jpg").getImage()); 
		icons.add(new ImageIcon("etc/most32.jpg").getImage());
		
		GDBBDialog1 d = new GDBBDialog1();
		
		d.setIconImages(icons);
    	d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	d.setSize(400, 320);
    	d.setLocationRelativeTo(null);
    	d.setVisible(true);

	}
}




