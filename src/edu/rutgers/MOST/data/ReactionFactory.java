package edu.rutgers.MOST.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import edu.rutgers.MOST.presentation.GraphicalInterface;
import edu.rutgers.MOST.presentation.GraphicalInterfaceConstants;

public class ReactionFactory {
	private String sourceType;
	private Map<Object, Object> reactionsIdPositionMap;
	private static String columnName;

	public ReactionFactory(String sourceType) {
		this.sourceType = sourceType;
	}

	public Map<Object, Object> getReactionsIdPositionMap() {
		return reactionsIdPositionMap;
	}

	public void setReactionsIdPositionMap(Map<Object, Object> reactionsIdPositionMap) {
		this.reactionsIdPositionMap = reactionsIdPositionMap;
	}

	public ModelReaction getReactionById(Integer reactionId){
		if("SBML".equals(sourceType)){
			SBMLReaction reaction = new SBMLReaction();
			reaction.loadById(reactionId);
			return reaction;
		}
		return new SBMLReaction(); //Default behavior.
	}

	public ModelReaction getReactionByRow(Integer row){
		if("SBML".equals(sourceType)){
			SBMLReaction reaction = new SBMLReaction();
			reaction.loadByRow(row);
			return reaction;
		}
		return new SBMLReaction(); //Default behavior.
	}
	
	public Vector<ModelReaction> getAllReactions() {
		Vector<ModelReaction> reactions = new Vector<ModelReaction>();
		Map<Object, Object> reactionsIdPositionMap = new HashMap<Object, Object>();
		int count = 0;

		if("SBML".equals(sourceType)){
			// returns a list of SBMLReactions
			// what parameters are actually needed, for example
			// reaction name is not going to be changed by any analysis			
			for (int i = 0; i < GraphicalInterface.reactionsTable.getRowCount(); i++) {
//				if (GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTION_ABBREVIATION_COLUMN) != null &&
//						((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTION_ABBREVIATION_COLUMN)).trim().length() > 0) {
					SBMLReaction reaction = new SBMLReaction();
					reaction.setId(Integer.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTIONS_ID_COLUMN)));
					reaction.setKnockout((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.KO_COLUMN));
					reaction.setFluxValue(Double.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.FLUX_VALUE_COLUMN)));
					reaction.setLowerBound(Double.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.LOWER_BOUND_COLUMN)));
					reaction.setUpperBound(Double.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.UPPER_BOUND_COLUMN)));
					reaction.setBiologicalObjective(Double.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.BIOLOGICAL_OBJECTIVE_COLUMN)));
					reaction.setGeneAssociation((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.GENE_ASSOCIATION_COLUMN));
					reactions.add(reaction);
					reactionsIdPositionMap.put(reaction.getId(), count);
					count += 1;
				//}				
			}
			setReactionsIdPositionMap(reactionsIdPositionMap);
		}
		
		return reactions;
	}

	public Vector<Double> getObjective() {
		Vector<ModelReaction> reactions = getAllReactions();
		Vector<Double> objective = new Vector<Double>(reactions.size());

		if("SBML".equals(sourceType)){
			for (int i = 0; i < reactions.size(); i++) {
				int id = ((SBMLReaction) reactions.get(i)).getId();
				Double obj = ((SBMLReaction) reactions.get(i)).getBiologicalObjective();
				objective.add((Integer) reactionsIdPositionMap.get(id), obj);
			}
		}

		return objective;
	}

	public void setFluxes(ArrayList<Double> fluxes) {
		DefaultTableModel reactionsOptModel = (DefaultTableModel) GraphicalInterface.reactionsTable.getModel();
		for (int i = 0; i < fluxes.size(); i++) {
			reactionsOptModel.setValueAt(fluxes.get(i).toString(), i, GraphicalInterfaceConstants.FLUX_VALUE_COLUMN);
		}
	}

	public ArrayList<String> setKnockouts(List<Double> knockouts) {
		ArrayList<String> knockoutGenes = new ArrayList<String>();

		ArrayList<Double> kVector = new ArrayList<Double>();

		Vector<String> uniqueGeneAssociations = getUniqueGeneAssociations();
		Vector<String> geneAssocaitons = getGeneAssociations();
		ArrayList<Integer> rowList = new ArrayList<Integer>();

		String queryKVector = "";
		for (int i = 0; i < geneAssocaitons.size(); i++) {
			for (int j = 0; j < uniqueGeneAssociations.size(); j++) {
				if (geneAssocaitons.elementAt(i).equals(uniqueGeneAssociations.elementAt(j))) {
					kVector.add(knockouts.get(j).doubleValue());
				}

				if (knockouts.get(j).doubleValue() != 0.0) {
					knockoutGenes.add(uniqueGeneAssociations.elementAt(j));
				}
			}

			if(kVector.get(i).doubleValue() != 0.0) {
				rowList.add(i);
				//queryKVector += " when " + (i + 1) + " then " + "\"" + GraphicalInterfaceConstants.BOOLEAN_VALUES[1] + "\"";
			}
		}

		//			for (int i = 0; i < uniqueGeneAssociations.size(); i++) {
		//				if (knockouts.get(i).doubleValue() != 0) {
		//					knockoutGenes.add(uniqueGeneAssociations.elementAt(i));
		////					System.out.println(uniqueGeneAssociations.elementAt(i));
		//				}
		//			}


		//			String queryKVector = "";
		//			for (int i = 0; i < geneAssocaitons.size(); i++) {
		//				if(kVector.get(i).doubleValue() != 0.0) {
		//					queryKVector += " when " + (i + 1) + " then " + kVector.get(i);
		//				}
		//			}

		DefaultTableModel reactionsOptModel = (DefaultTableModel) GraphicalInterface.reactionsTable.getModel();
		for (int j = 0; j < rowList.size(); j++) {
		//if (queryKVector.length() != 0) {
			reactionsOptModel.setValueAt(GraphicalInterfaceConstants.BOOLEAN_VALUES[1], rowList.get(j), GraphicalInterfaceConstants.KO_COLUMN);
			//String query = "update reactions set knockout = case id" + queryKVector + " end";
		}	

		return knockoutGenes;
	}

	/**
	 * @param args
	 */

	public Vector<String> getGeneAssociations() {
		Vector<ModelReaction> reactions = getAllReactions();
		Vector<String> geneAssociations = new Vector<String>();

		if("SBML".equals(sourceType)){
			for (int i = 0; i < reactions.size(); i++) {
				int id = ((SBMLReaction) reactions.get(i)).getId();
				String geneAssoc = ((SBMLReaction) reactions.get(i)).getGeneAssociation();
				geneAssociations.add((Integer) reactionsIdPositionMap.get(id), geneAssoc);
			}
		}
		System.out.println("gene assoc " + geneAssociations);

		return geneAssociations;
	}

	public Vector<String> getUniqueGeneAssociations() {
		Vector<String> geneAssociations = getGeneAssociations();
		Vector<String> uniqueGeneAssociations = new Vector<String>();

		if("SBML".equals(sourceType)){
			for (int i = 0; i < geneAssociations.size(); i++) {
				if (!uniqueGeneAssociations.contains(geneAssociations.get(i))) {
					uniqueGeneAssociations.add(geneAssociations.get(i));
				}
			}
		}
		System.out.println("unique gene assoc " + uniqueGeneAssociations);
		
		return uniqueGeneAssociations;
	}

	public Vector<Double> getSyntheticObjectiveVector() {
		Vector<ModelReaction> reactions = getAllReactions();
		Vector<Double> syntheticObjectiveVector = new Vector<Double>();

		if("SBML".equals(sourceType)){
			for (int i = 0; i < reactions.size(); i++) {
				int id = ((SBMLReaction) reactions.get(i)).getId();
				Double obj = ((SBMLReaction) reactions.get(i)).getSyntheticObjective();
				syntheticObjectiveVector.add((Integer) reactionsIdPositionMap.get(id), obj);
			}
		}

		return syntheticObjectiveVector;
	}

	public ArrayList<Integer> reactionIdList() {
		ArrayList<Integer> reactionIdList = new ArrayList<Integer>();

		if("SBML".equals(sourceType)){
			for (int i = 0; i < GraphicalInterface.reactionsTable.getRowCount(); i++) {
				int id = Integer.valueOf((String) GraphicalInterface.reactionsTable.getModel().getValueAt(i, GraphicalInterfaceConstants.REACTIONS_ID_COLUMN));
				reactionIdList.add(id);
			}
		}

		return reactionIdList;
	}
	
	public static String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		ReactionFactory.columnName = columnName;
	}

	public static void main(String[] args) {

	}

}
