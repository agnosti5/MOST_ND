package edu.rutgers.MOST.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import au.com.bytecode.opencsv.CSVReader;

import edu.rutgers.MOST.config.LocalConfig;
//import edu.rutgers.MOST.logic.ReactionParser;
import edu.rutgers.MOST.logic.ReactionParser;
import edu.rutgers.MOST.presentation.GraphicalInterfaceConstants;

public class TextReactionsModelReader {
	
	public boolean noReactants;     // type ==> p
	public boolean noProducts;      // type r ==>
	
	private static DefaultTableModel reactionsTableModel;
	
	public static DefaultTableModel getReactionsTableModel() {
		return reactionsTableModel;
	}

	public static void setReactionsTableModel(DefaultTableModel reactionsTableModel) {
		TextReactionsModelReader.reactionsTableModel = reactionsTableModel;
	}

	public ArrayList<String> columnNamesFromFile(File file, int row) {
		ArrayList<String> columnNamesFromFile = new ArrayList<String>();
		
		String[] dataArray = null;

		//use fileReader to read first line to get headers
		BufferedReader CSVFile;
		try {
			CSVFile = new BufferedReader(new FileReader(file));
			String dataRow = CSVFile.readLine();
			dataArray = dataRow.split(",");				

			//add all column names to list			
			for (int h = 0; h < dataArray.length; h++) { 

				//remove quotes if exist
				if (dataArray[h].startsWith("\"")) {
					//removes " " and null strings
					if (dataArray[h].compareTo("\" \"") != 0 && dataArray[h].trim().length() > 0) {
						columnNamesFromFile.add(dataArray[h].substring(1, dataArray[h].length() - 1));
					}					
				} else {
					if (dataArray[h].trim().length() > 0) {
						columnNamesFromFile.add(dataArray[h]);
					}					
				}			
			}

			if (row > 0) {
				for (int i = 0; i < row; i++) {
					dataRow = CSVFile.readLine();
					dataArray = dataRow.split(",");								
					columnNamesFromFile.clear();
					//add all column names to list			
					for (int h = 0; h < dataArray.length; h++) { 
						//remove quotes if exist
						if (dataArray[h].startsWith("\"")) {
							//removes " " and null strings
							if (dataArray[h].compareTo("\" \"") != 0 && dataArray[h].trim().length() > 0) {
								columnNamesFromFile.add(dataArray[h].substring(1, dataArray[h].length() - 1));
							}					
						} else {
							if (dataArray[h].trim().length() > 0) {
								columnNamesFromFile.add(dataArray[h]);
							}					
						}			
					} 
				}				
			}

			CSVFile.close();

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();							
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		return columnNamesFromFile;
	}	

	public Integer numberOfLines(File file) {
		int count = 0;
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(file), ',');
			String[] dataArray;
			try {
				while ((dataArray = reader.readNext()) != null) {
					count++; 	
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;		
	}

	public void load(File file){
		DefaultTableModel reacTableModel = new DefaultTableModel();
		for (int m = 0; m < GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES.length; m++) {
			reacTableModel.addColumn(GraphicalInterfaceConstants.REACTIONS_COLUMN_NAMES[m]);
		}
		for (int n = 0; n < LocalConfig.getInstance().getReactionsMetaColumnNames().size(); n++) {
			reacTableModel.addColumn(LocalConfig.getInstance().getReactionsMetaColumnNames().get(n));
		}		
		//if first row of file in not column names, starts reading after row that contains names
		int correction = LocalConfig.getInstance().getReactionsNextRowCorrection();
		int row = 1;
		//int maxMetabId = LocalConfig.getInstance().getMaxMetabolite();
		
		//LocalConfig.getInstance().getMetaboliteUsedMap().clear();
		
		//LocalConfig.getInstance().addMetaboliteOption = true;
		
		if (!LocalConfig.getInstance().hasMetabolitesFile) {
			LocalConfig.getInstance().getMetaboliteUsedMap().clear();
			LocalConfig.getInstance().getDuplicateIds().clear();
			LocalConfig.getInstance().getSuspiciousMetabolites().clear();
			LocalConfig.getInstance().getMetaboliteNameIdMap().clear();
			LocalConfig.getInstance().setMaxMetabolite(0);
			//LocalConfig.getInstance().setMaxMetaboliteId(0);
			//maxMetabId = 0;
			
		}
		
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(file), ',');
			String [] dataArray;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			reader = new CSVReader(new FileReader(file), ',');
			int numLines = numberOfLines(file);
			
			LocalConfig.getInstance().setMaxReactionId(numLines - 1 - correction);
			
			int id = 0;
			for (int i = 0; i < numLines; i++) {
				String [] dataArray = reader.readNext();
				for (int s = 0; s < dataArray.length; s++) {
					if (dataArray[s].length() > 0 && dataArray[s].substring(0,1).matches("\"")) {
						dataArray[s] = dataArray[s].substring(1, (dataArray[s].length() - 1));			
					}
				}
			
				if (i >= (row + correction)) {
					Vector <String> reacRow = new Vector<String>();
					reacRow.add(Integer.toString(id));
					String knockout = GraphicalInterfaceConstants.KO_DEFAULT;
					Double fluxValue = GraphicalInterfaceConstants.FLUX_VALUE_DEFAULT;
					String reactionAbbreviation = "";
					String reactionName = "";
					String reactionEqunAbbr = "";
					String reactionEqunNames = "";
					String reversible = "";
					Double lowerBound = GraphicalInterfaceConstants.LOWER_BOUND_DEFAULT;
					Double upperBound =	GraphicalInterfaceConstants.UPPER_BOUND_DEFAULT;
					Double biologicalObjective = GraphicalInterfaceConstants.BIOLOGICAL_OBJECTIVE_DEFAULT;
					Double syntheticObjective = GraphicalInterfaceConstants.SYNTHETIC_OBJECTIVE_DEFAULT;
					String geneAssociation = "";
					String proteinAssociation = "";
					String subsystem = "";
					String proteinClass = "";
                    
					if (LocalConfig.getInstance().getKnockoutColumnIndex() > -1) {
						if (dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("false") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("FALSE") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("0") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("0.0") == 0) {
							knockout = GraphicalInterfaceConstants.BOOLEAN_VALUES[0];
						} else if (dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("true") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("TRUE") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("1") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("1.0") == 0) {
							knockout = GraphicalInterfaceConstants.BOOLEAN_VALUES[1];													
						} 
					}
					reacRow.add(knockout);
					if (LocalConfig.getInstance().getFluxValueColumnIndex() > -1) {
						if (isNumber(dataArray[LocalConfig.getInstance().getFluxValueColumnIndex()])) {
							fluxValue = Double.valueOf(dataArray[LocalConfig.getInstance().getFluxValueColumnIndex()]);
						} 
					} 
					reacRow.add(Double.toString(fluxValue));
					reactionAbbreviation = dataArray[LocalConfig.getInstance().getReactionAbbreviationColumnIndex()];
					reacRow.add(reactionAbbreviation);
					if (LocalConfig.getInstance().getReactionNameColumnIndex() > -1) {
						reactionName = dataArray[LocalConfig.getInstance().getReactionNameColumnIndex()];
					}
					reacRow.add(reactionName);	
					
					ReactionParser parser = new ReactionParser();
					
					reactionEqunAbbr = dataArray[LocalConfig.getInstance().getReactionEquationColumnIndex()];
					reactionEqunAbbr = reactionEqunAbbr.trim();
					reacRow.add(reactionEqunAbbr);
					// TODO: equation needs to be created here
					reacRow.add(reactionEqunNames);
					
					if (LocalConfig.getInstance().getReversibleColumnIndex() > -1) {
						if (dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("false") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("FALSE") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("0") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("0.0") == 0) {
							reversible = GraphicalInterfaceConstants.BOOLEAN_VALUES[0];
						} else if (dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("true") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("TRUE") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("1") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("1.0") == 0) {
							reversible = GraphicalInterfaceConstants.BOOLEAN_VALUES[1];
						} else {
							//if reversible field contains a value it is used, otherwise determined by arrow in reaction 
							if (dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].length() > 0) {
								reversible = dataArray[LocalConfig.getInstance().getReversibleColumnIndex()];
							} else {
								if (reactionEqunAbbr != null) {
									if (reactionEqunAbbr.contains("<") || (reactionEqunAbbr.contains("=") && !reactionEqunAbbr.contains(">"))) {
										reversible = GraphicalInterfaceConstants.BOOLEAN_VALUES[0];
									} else if (reactionEqunAbbr.contains("-->") || reactionEqunAbbr.contains("->") || reactionEqunAbbr.contains("=>")) {
										reversible = GraphicalInterfaceConstants.BOOLEAN_VALUES[1];		    		
									}				
								} 
							}
						}
					}
					reacRow.add(reversible);
					if (LocalConfig.getInstance().getLowerBoundColumnIndex() > -1) {
						if (isNumber(dataArray[LocalConfig.getInstance().getLowerBoundColumnIndex()])) {
							lowerBound = Double.valueOf(dataArray[LocalConfig.getInstance().getLowerBoundColumnIndex()]);							
						} 
					} 
					// TODO : add error message here?
					if (lowerBound < 0.0 && reversible.equals(GraphicalInterfaceConstants.BOOLEAN_VALUES[0])) {
						lowerBound = 0.0;
					}
					reacRow.add(Double.toString(lowerBound));
					if (LocalConfig.getInstance().getUpperBoundColumnIndex() > -1) {
						if (isNumber(dataArray[LocalConfig.getInstance().getUpperBoundColumnIndex()])) {
							upperBound = Double.valueOf(dataArray[LocalConfig.getInstance().getUpperBoundColumnIndex()]);							
						}
					} 
					reacRow.add(Double.toString(upperBound));
					if (LocalConfig.getInstance().getBiologicalObjectiveColumnIndex() > -1) {
						if (isNumber(dataArray[LocalConfig.getInstance().getBiologicalObjectiveColumnIndex()])) {
							biologicalObjective = Double.valueOf(dataArray[LocalConfig.getInstance().getBiologicalObjectiveColumnIndex()]);							
						} 							
					} 
					reacRow.add(Double.toString(biologicalObjective));
					if (LocalConfig.getInstance().getSyntheticObjectiveColumnIndex() > -1) {
						if (isNumber(dataArray[LocalConfig.getInstance().getSyntheticObjectiveColumnIndex()])) {
							syntheticObjective = Double.valueOf(dataArray[LocalConfig.getInstance().getSyntheticObjectiveColumnIndex()]);							
						} 							
					} 
					reacRow.add(Double.toString(syntheticObjective));
					if (LocalConfig.getInstance().getGeneAssociationColumnIndex() > -1) {
						geneAssociation = dataArray[LocalConfig.getInstance().getGeneAssociationColumnIndex()];						 							
					} 
					reacRow.add(geneAssociation);
					if (LocalConfig.getInstance().getProteinAssociationColumnIndex() > -1) {
						proteinAssociation = dataArray[LocalConfig.getInstance().getProteinAssociationColumnIndex()];						 							
					} 
					reacRow.add(proteinAssociation);
					if (LocalConfig.getInstance().getSubsystemColumnIndex() > -1) {
						subsystem = dataArray[LocalConfig.getInstance().getSubsystemColumnIndex()];						 							
					} 
					reacRow.add(subsystem);
					if (LocalConfig.getInstance().getProteinClassColumnIndex() > -1) {
						proteinClass = dataArray[LocalConfig.getInstance().getProteinClassColumnIndex()];						 							
					} 
					reacRow.add(proteinClass);
					
					if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 0) {
						for (int m = 0; m < LocalConfig.getInstance().getReactionsMetaColumnIndexList().size(); m++) {
							reacRow.add(dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(m)]);
						}
					}

					reacTableModel.addRow(reacRow);
					id += 1;
				}
				//LocalConfig.getInstance().noButtonClicked = false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//GraphicalInterface.showPrompt = true;
		LocalConfig.getInstance().hasMetabolitesFile = false;
		setReactionsTableModel(reacTableModel);
	}
	
	/*
	public void load(File file, String databaseName){	
		//ReactionParser parser = new ReactionParser();		
		//if first row of file in not column names, starts reading after row that contains names
		int correction = LocalConfig.getInstance().getReactionsNextRowCorrection();
		int row = 1;
		int maxMetabId = LocalConfig.getInstance().getMaxMetabolite();
		
		//LocalConfig.getInstance().getMetaboliteUsedMap().clear();
		
		//LocalConfig.getInstance().addMetaboliteOption = true;
			
			if (!LocalConfig.getInstance().hasMetabolitesFile) {

				LocalConfig.getInstance().getMetaboliteUsedMap().clear();
				LocalConfig.getInstance().getDuplicateIds().clear();
				LocalConfig.getInstance().getSuspiciousMetabolites().clear();
				LocalConfig.getInstance().getMetaboliteIdNameMap().clear();

				LocalConfig.getInstance().setMaxMetabolite(0);
				maxMetabId = 0;
				
			}
			
			CSVReader reader;
			try {
				reader = new CSVReader(new FileReader(file), ',');
				String [] dataArray;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				reader = new CSVReader(new FileReader(file), ',');
				
				int numLines = numberOfLines(file);
				
				for (int i = 0; i < numLines; i++) {
					StringBuffer reacNamesBfr = new StringBuffer();
					StringBuffer prodNamesBfr = new StringBuffer();
					StringBuffer rxnNamesBfr = new StringBuffer();
					String [] dataArray = reader.readNext();
					for (int s = 0; s < dataArray.length; s++) {
						if (dataArray[s].length() > 0 && dataArray[s].substring(0,1).matches("\"")) {
							dataArray[s] = dataArray[s].substring(1, (dataArray[s].length() - 1));			
						}
					}
					
					if (i >= (row + correction)) {
						String knockout = GraphicalInterfaceConstants.KO_DEFAULT;
						Double fluxValue = GraphicalInterfaceConstants.FLUX_VALUE_DEFAULT;
						String reactionAbbreviation = "";
						String reactionName = "";
						String reactionEqunAbbr = "";
						String reactionEqunNames = "";
						String reversible = "";
						Double lowerBound = GraphicalInterfaceConstants.LOWER_BOUND_DEFAULT;
						Double upperBound =	GraphicalInterfaceConstants.UPPER_BOUND_DEFAULT;
						Double biologicalObjective = GraphicalInterfaceConstants.BIOLOGICAL_OBJECTIVE_DEFAULT;
						Double syntheticObjective = GraphicalInterfaceConstants.SYNTHETIC_OBJECTIVE_DEFAULT;
						String geneAssociations = "";
						String meta1 = "";
						String meta2 = "";
						String meta3 = "";
						String meta4 = "";
						String meta5 = "";
						String meta6 = "";
						String meta7 = "";
						String meta8 = "";
						String meta9 = "";
						String meta10 = "";
						String meta11 = "";
						String meta12 = "";
						String meta13 = "";
						String meta14 = "";
						String meta15 = "";
                        
						if (LocalConfig.getInstance().getKnockoutColumnIndex() > -1) {
							if (dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("false") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("FALSE") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("0") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("0.0") == 0) {
								knockout = GraphicalInterfaceConstants.BOOLEAN_VALUES[0];
							} else if (dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("true") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("TRUE") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("1") == 0 || dataArray[LocalConfig.getInstance().getKnockoutColumnIndex()].compareTo("1.0") == 0) {
								knockout = GraphicalInterfaceConstants.BOOLEAN_VALUES[1];													
							} 
						} 
						if (LocalConfig.getInstance().getFluxValueColumnIndex() > -1) {
							if (isNumber(dataArray[LocalConfig.getInstance().getFluxValueColumnIndex()])) {
								fluxValue = Double.valueOf(dataArray[LocalConfig.getInstance().getFluxValueColumnIndex()]);
							} 
						} 
						
						reactionAbbreviation = dataArray[LocalConfig.getInstance().getReactionAbbreviationColumnIndex()];
						
						if (LocalConfig.getInstance().getReactionNameColumnIndex() > -1) {
							reactionName = dataArray[LocalConfig.getInstance().getReactionNameColumnIndex()];
						}
								
						reactionEqunAbbr = dataArray[LocalConfig.getInstance().getReactionEquationColumnIndex()];
						reactionEqunAbbr = reactionEqunAbbr.trim();
						
						if (LocalConfig.getInstance().getReversibleColumnIndex() > -1) {
							if (dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("false") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("FALSE") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("0") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("0.0") == 0) {
								reversible = "false";
							} else if (dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("true") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("TRUE") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("1") == 0 || dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].compareTo("1.0") == 0) {
								reversible = "true";
							} else {
								//if reversible field contains a value it is used, otherwise determined by arrow in reaction 
								if (dataArray[LocalConfig.getInstance().getReversibleColumnIndex()].length() > 0) {
									reversible = dataArray[LocalConfig.getInstance().getReversibleColumnIndex()];
								} else {
									if (reactionEqunAbbr != null) {
										if (reactionEqunAbbr.contains("<") || (reactionEqunAbbr.contains("=") && !reactionEqunAbbr.contains(">"))) {
											reversible = "true";
										} else if (reactionEqunAbbr.contains("-->") || reactionEqunAbbr.contains("->") || reactionEqunAbbr.contains("=>")) {
											reversible = "false";		    		
										}				
									} 
								}
							}
						}
						
						try {
							//ReactionParser parser = new ReactionParser();
							boolean valid = true;
							
							//ArrayList<ArrayList<ArrayList<String>>> reactionList = parser.reactionList(reactionEqunAbbr.trim());
							if (parser.isValid(reactionEqunAbbr)) {
								noReactants = false;
								noProducts = false;
								ArrayList<ArrayList<String>> reactants = parser.reactionList(reactionEqunAbbr.trim()).get(0);
								// if user hits "No", reactant will not be included in equation
								ArrayList<String> removeReacList = new ArrayList<String>();
								ArrayList<String> removeProdList = new ArrayList<String>();
								//reactions of the type ==> b will be size 1, assigned the value [0] in parser			
								if (reactants.get(0).size() == 1) {
									noReactants = true;
								} else {
									for (int r = 0; r < reactants.size(); r++) {
										if (reactants.get(r).size() == 2) {											
											String stoicStr = (String) reactants.get(r).get(0);
											String reactant = (String) reactants.get(r).get(1);
											boolean newMetabolite = false;
											if (!(LocalConfig.getInstance().getMetaboliteIdNameMap().containsKey(reactant.trim()))) {
												newMetabolite = true;
												if (GraphicalInterface.showPrompt && reactant.length() > 0) {
													Object[] options = {"Yes",
															"Yes to All",
													"No"};
					
													int choice = JOptionPane.showOptionDialog(null, 
															"The metabolite " + reactant + " does not exist. Do you wish to add it?", 
															"Add Metabolite?", 
															JOptionPane.YES_NO_CANCEL_OPTION, 
															JOptionPane.QUESTION_MESSAGE, 
															null, options, options[0]);
													//options[0] sets "Yes" as default button

													// interpret the user's choice	  
													if (choice == JOptionPane.YES_OPTION)
													{
														LocalConfig.getInstance().addMetaboliteOption = true;
														addMetabPrep.setString(1, reactant);
														addMetabPrep.executeUpdate();
														maxMetabId += 1;
														LocalConfig.getInstance().getMetaboliteIdNameMap().put(reactant, new Integer(maxMetabId));
													}
													//No option actually corresponds to "Yes to All" button
													if (choice == JOptionPane.NO_OPTION)
													{
														LocalConfig.getInstance().addMetaboliteOption = true;
														GraphicalInterface.showPrompt = false;
														addMetabPrep.setString(1, reactant);
														addMetabPrep.executeUpdate();
														maxMetabId += 1;
														LocalConfig.getInstance().getMetaboliteIdNameMap().put(reactant, new Integer(maxMetabId));
													}
													//Cancel option actually corresponds to "No" button
													if (choice == JOptionPane.CANCEL_OPTION) {
														LocalConfig.getInstance().addMetaboliteOption = false;
														LocalConfig.getInstance().noButtonClicked = true;
														removeReacList.add(reactant);
													}	  
												} else {
													addMetabPrep.setString(1, reactant);
													addMetabPrep.executeUpdate();
													maxMetabId += 1;
													LocalConfig.getInstance().getMetaboliteIdNameMap().put(reactant, new Integer(maxMetabId));
												}											
											}																
											Integer id = (Integer) LocalConfig.getInstance().getMetaboliteIdNameMap().get(reactant);				
											String metabName = "";
											reacNamePrep.setInt(1, id);
											ResultSet rs = reacNamePrep.executeQuery();
											while (rs.next()) {
												metabName = rs.getString("metabolite_name");
											}
											rs.close();
											if (r == 0) {
												if (stoicStr.length() == 0 || Double.valueOf(stoicStr) == 1.0) {
													if (metabName != null && metabName.trim().length() > 0) {
														reacNamesBfr.append(metabName);
													} else {
														reacNamesBfr.append(reactant);
													}									
												} else {
													if (metabName != null && metabName.trim().length() > 0) {
														reacNamesBfr.append(stoicStr + " " + metabName);
													} else {
														reacNamesBfr.append(stoicStr + " " + reactant);
													}									
												}

											} else {
												if (stoicStr.length() == 0 || Double.valueOf(stoicStr) == 1.0) {
													if (metabName != null && metabName.trim().length() > 0) {
														reacNamesBfr.append(" + " + metabName);
													} else {
														reacNamesBfr.append(" + " + reactant);
													}
													
												} else {
													if (metabName != null && metabName.trim().length() > 0) {
														reacNamesBfr.append(" + " + stoicStr + " " + metabName);
													} else {
														reacNamesBfr.append(" + " + stoicStr + " " + reactant);
													}
												}				
											}			
											if (!newMetabolite || LocalConfig.getInstance().addMetaboliteOption) {
												rrInsertPrep.setInt(1, i - correction);
												rrInsertPrep.setDouble(2, Double.valueOf(stoicStr));
												rrInsertPrep.setInt(3, id);
												rrInsertPrep.executeUpdate();
												if (parser.isSuspicious(reactant)) {
													if (!LocalConfig.getInstance().getSuspiciousMetabolites().contains(id)) {
														LocalConfig.getInstance().getSuspiciousMetabolites().add(id);
													}							
												}
												if (LocalConfig.getInstance().getMetaboliteUsedMap().containsKey(reactant)) {
													int usedCount = (Integer) LocalConfig.getInstance().getMetaboliteUsedMap().get(reactant);
													LocalConfig.getInstance().getMetaboliteUsedMap().put(reactant, new Integer(usedCount + 1));									
												} else {
													LocalConfig.getInstance().getMetaboliteUsedMap().put(reactant, new Integer(1));
												}	
											}
										} else {
											//Invalid reaction
											valid = false;
											break;
										}								
									}
								}
										
								//reactions of the type a ==> will be size 1, assigned the value [0] in parser
								ArrayList<ArrayList<String>> products = parser.reactionList(reactionEqunAbbr.trim()).get(1);
								if (products.get(0).size() == 1) {
									noProducts = true;
								} else {
									for (int p = 0; p < products.size(); p++) {
										if (products.get(p).size() == 2) {
											String stoicStr = (String) products.get(p).get(0);
											String product = (String) products.get(p).get(1);	
											boolean newMetabolite = false;
											if (!(LocalConfig.getInstance().getMetaboliteIdNameMap().containsKey(product))) {
												newMetabolite = true;
												if (GraphicalInterface.showPrompt && product.length() > 0) {
													Object[] options = {"Yes",
															"Yes to All",
													"No"};

													int choice = JOptionPane.showOptionDialog(null, 
															"The metabolite " + product + " does not exist. Do you wish to add it?", 
															"Add Metabolite?", 
															JOptionPane.YES_NO_CANCEL_OPTION, 
															JOptionPane.QUESTION_MESSAGE, 
															null, options, options[0]);
													//options[0] sets "Yes" as default button

													// interpret the user's choice	  
													if (choice == JOptionPane.YES_OPTION)
													{
														LocalConfig.getInstance().addMetaboliteOption = true;
														addMetabPrep.setString(1, product);
														addMetabPrep.executeUpdate();
														maxMetabId += 1;
														LocalConfig.getInstance().getMetaboliteIdNameMap().put(product, new Integer(maxMetabId));
													}
													//No option actually corresponds to "Yes to All" button
													if (choice == JOptionPane.NO_OPTION)
													{
														LocalConfig.getInstance().addMetaboliteOption = true;
														GraphicalInterface.showPrompt = false;
														addMetabPrep.setString(1, product);
														addMetabPrep.executeUpdate();
														maxMetabId += 1;
														LocalConfig.getInstance().getMetaboliteIdNameMap().put(product, new Integer(maxMetabId));
													}
													//Cancel option actually corresponds to "No" button
													if (choice == JOptionPane.CANCEL_OPTION) {
														LocalConfig.getInstance().addMetaboliteOption = false;
														LocalConfig.getInstance().noButtonClicked = true;
														removeProdList.add(product);
													}	  
												} else {
													addMetabPrep.setString(1, product);
													addMetabPrep.executeUpdate();
													maxMetabId += 1;
													LocalConfig.getInstance().getMetaboliteIdNameMap().put(product, new Integer(maxMetabId));
												}		
											}
											
											Integer id = (Integer) LocalConfig.getInstance().getMetaboliteIdNameMap().get(product);											
											String metabName = "";
											reacNamePrep.setInt(1, id);
											ResultSet rs = reacNamePrep.executeQuery();
											while (rs.next()) {
												metabName = rs.getString("metabolite_name");
											}
											rs.close();
											if (p == 0) {
												if (stoicStr.length() == 0 || Double.valueOf(stoicStr) == 1.0) {
													if (metabName != null && metabName.trim().length() > 0) {
														prodNamesBfr.append(metabName);
													} else {
														prodNamesBfr.append(product);
													}									
												} else {
													if (metabName != null && metabName.trim().length() > 0) {
														prodNamesBfr.append(stoicStr + " " + metabName);
													} else {
														prodNamesBfr.append(stoicStr + " " + product);
													}									
												}

											} else {
												if (stoicStr.length() == 0 || Double.valueOf(stoicStr) == 1.0) {
													if (metabName != null && metabName.trim().length() > 0) {
														prodNamesBfr.append(" + " + metabName);
													} else {
														prodNamesBfr.append(" + " + product);
													}
													
												} else {
													if (metabName != null && metabName.trim().length() > 0) {
														prodNamesBfr.append(" + " + stoicStr + " " + metabName);
													} else {
														prodNamesBfr.append(" + " + stoicStr + " " + product);
													}
												}				
											}			
											if (!newMetabolite || LocalConfig.getInstance().addMetaboliteOption) {
												rpInsertPrep.setInt(1, i - correction);
												rpInsertPrep.setDouble(2, Double.valueOf(stoicStr));
												rpInsertPrep.setInt(3, id);
												rpInsertPrep.executeUpdate();	
												if (parser.isSuspicious(product)) {
													if (!LocalConfig.getInstance().getSuspiciousMetabolites().contains(id)) {
														LocalConfig.getInstance().getSuspiciousMetabolites().add(id);
													}							
												}
												if (LocalConfig.getInstance().getMetaboliteUsedMap().containsKey(product)) {
													int usedCount = (Integer) LocalConfig.getInstance().getMetaboliteUsedMap().get(product);
													LocalConfig.getInstance().getMetaboliteUsedMap().put(product, new Integer(usedCount + 1));									
												} else {
													LocalConfig.getInstance().getMetaboliteUsedMap().put(product, new Integer(1));
												}
											}										
											
										} else {
											//Invalid reaction
											valid = false;
											break;											
										}
									}							
								}
								// revise reaction equation if "No" button clicked
								if (valid && LocalConfig.getInstance().noButtonClicked) {
									System.out.println("no");
									String revisedReactants = "";
									String revisedProducts = "";
									String revisedReaction = "";
									if (!noReactants) {
										revisedReactants = revisedReactants(reactants, removeReacList);
									}									
									String splitString = parser.splitString(reactionEqunAbbr);
									if (!noProducts) {
										revisedProducts = revisedProducts(products, removeProdList);
									}									
									revisedReaction = revisedReactants + " " + splitString + revisedProducts;
									// prevents reaction equation from appearing as only an arrow such as ==>
									if (revisedReaction.trim().compareTo(splitString.trim()) != 0) {
										reactionEqunAbbr = revisedReaction.trim();
									} else {
										reactionEqunAbbr = "";
									}									
								}								
							} else {
								//Invalid reaction
								valid = false;
							}
														
							if (!valid) {
								if (reactionEqunAbbr != null && reactionEqunAbbr.length() > 0) {
									//LocalConfig.getInstance().getInvalidReactions().add(reactionEqunAbbr);
								}
							}
						} catch (Throwable t) {
							
						}

						if (reversible == "false") {
							rxnNamesBfr.append(reacNamesBfr).append(" --> ").append(prodNamesBfr);
						} else {
							rxnNamesBfr.append(reacNamesBfr).append(" <==> ").append(prodNamesBfr);
						}

						reactionEqunNames = rxnNamesBfr.toString().trim();

						if (LocalConfig.getInstance().getLowerBoundColumnIndex() > -1) {
							if (isNumber(dataArray[LocalConfig.getInstance().getLowerBoundColumnIndex()])) {
								lowerBound = Double.valueOf(dataArray[LocalConfig.getInstance().getLowerBoundColumnIndex()]);							
							} 
						} 
						if (LocalConfig.getInstance().getUpperBoundColumnIndex() > -1) {
							if (isNumber(dataArray[LocalConfig.getInstance().getUpperBoundColumnIndex()])) {
								upperBound = Double.valueOf(dataArray[LocalConfig.getInstance().getUpperBoundColumnIndex()]);							
							}
						} 
						if (LocalConfig.getInstance().getBiologicalObjectiveColumnIndex() > -1) {
							if (isNumber(dataArray[LocalConfig.getInstance().getBiologicalObjectiveColumnIndex()])) {
								biologicalObjective = Double.valueOf(dataArray[LocalConfig.getInstance().getBiologicalObjectiveColumnIndex()]);							
							} 							
						} 
						if (LocalConfig.getInstance().getSyntheticObjectiveColumnIndex() > -1) {
							if (isNumber(dataArray[LocalConfig.getInstance().getSyntheticObjectiveColumnIndex()])) {
								syntheticObjective = Double.valueOf(dataArray[LocalConfig.getInstance().getSyntheticObjectiveColumnIndex()]);							
							} 							
						} 
						if (LocalConfig.getInstance().getGeneAssociationColumnIndex() > -1) {
							geneAssociations = dataArray[LocalConfig.getInstance().getGeneAssociationColumnIndex()];						 							
						} 
						
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 0) {
							meta1 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(0)];						
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 1) {
							meta2 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(1)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 2) {
							meta3 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(2)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 3) {
							meta4 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(3)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 4) {
							meta5 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(4)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 5) {
							meta6 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(5)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 6) {
							meta7 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(6)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 7) {
							meta8 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(7)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 8) {
							meta9 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(8)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 9) {
							meta10 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(9)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 10) {
							meta11 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(10)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 11) {
							meta12 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(11)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 12) {
							meta13 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(12)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 13) {
							meta14 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(13)];
						}
						if (LocalConfig.getInstance().getReactionsMetaColumnIndexList().size() > 14) {
							meta15 = dataArray[LocalConfig.getInstance().getReactionsMetaColumnIndexList().get(14)];
						}

						// TODO : add error message here?
						if (lowerBound < 0.0 && reversible.equals("false")) {
							lowerBound = 0.0;
						}
					}
					LocalConfig.getInstance().noButtonClicked = false;
				}
				
		//GraphicalInterface.showPrompt = true;
		//LocalConfig.getInstance().hasMetabolitesFile = false;
	}
	*/
	
	public boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
}



