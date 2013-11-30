package edu.rutgers.MOST.data;

import java.util.ArrayList;

import edu.rutgers.MOST.config.LocalConfig;
import edu.rutgers.MOST.logic.ReactionParser;

public class ReactionEquationUpdater {

	public void updateReactionEquations(int reactionId, String oldEquation, String newEquation) {
		ReactionParser parser = new ReactionParser();
		// contains species in old reaction equation not present in new reaction equation
		// to be removed or adjusted in maps
		ArrayList<String> removeList = new ArrayList<String>();
		// contains species in new reaction equation not present in old reaction equation,
		// added if user does not click "No" button
		ArrayList<String> maybeAddList = new ArrayList<String>();
		ArrayList<String> oldSpeciesList = new ArrayList<String>();
		ArrayList<String> newSpeciesList = new ArrayList<String>();
		if (oldEquation != null) {
			parser.reactionList(oldEquation);
			SBMLReactionEquation oldEqun = parser.getEquation();
			//System.out.println("old " + oldEquation.equationAbbreviations);
			// update for old equation
			for (int i = 0; i < oldEqun.getReactants().size(); i++){
				String reactant = oldEqun.getReactants().get(i).getMetaboliteAbbreviation();
				oldSpeciesList.add(reactant);
				//LocalConfig.getInstance().getMetaboliteNameIdMap().remove(reactant);
				//updateMetaboliteUsedMap(reactant, "old");
			}
			for (int i = 0; i < oldEqun.getProducts().size(); i++){
				String product = oldEqun.getProducts().get(i).getMetaboliteAbbreviation();
				oldSpeciesList.add(product);
				//LocalConfig.getInstance().getMetaboliteNameIdMap().remove(product);
				//updateMetaboliteUsedMap(product, "old");
			}
		}
		if (newEquation != null) {
			parser.reactionList(newEquation);
			SBMLReactionEquation newEqun = parser.getEquation();
//			// update for new equation
			for (int i = 0; i < newEqun.getReactants().size(); i++){
				//int metabId = newEqun.getReactants().get(i).getMetaboliteId();
				String reactant = newEqun.getReactants().get(i).getMetaboliteAbbreviation();
				newSpeciesList.add(reactant);
//				LocalConfig.getInstance().getMetaboliteNameIdMap().put(reactant, metabId);
//				//updateMetaboliteUsedMap(reactant, "new");
			}
			for (int i = 0; i < newEqun.getProducts().size(); i++){
				//int metabId = newEqun.getProducts().get(i).getMetaboliteId();
				String product = newEqun.getProducts().get(i).getMetaboliteAbbreviation();
				newSpeciesList.add(product);
//				LocalConfig.getInstance().getMetaboliteNameIdMap().put(product, metabId);
//				//updateMetaboliteUsedMap(product, "new");
			}
//			updateReactionEquationMap(reactionId, newEquation);
		}
		System.out.println("old sp " + oldSpeciesList);
		System.out.println("new sp " + newSpeciesList);
//		System.out.println("old id " + LocalConfig.getInstance().getMetaboliteNameIdMap());
//		System.out.println("old used " + LocalConfig.getInstance().getMetaboliteUsedMap());
		
		for (int i = 0; i < oldSpeciesList.size(); i++) {
			if (!newSpeciesList.contains(oldSpeciesList.get(i))) {
				removeList.add(oldSpeciesList.get(i));
			}
		}
		
		for (int i = 0; i < newSpeciesList.size(); i++) {
			if (!oldSpeciesList.contains(newSpeciesList.get(i))) {
				maybeAddList.add(newSpeciesList.get(i));
			}
		}
				
		System.out.println("remove " + removeList);
		System.out.println("maybe add " + maybeAddList);
		
	}
	
	public void updateMetaboliteUsedMap(String species, String type) {
		if (type.equals("old")) {
			if (LocalConfig.getInstance().getMetaboliteUsedMap().containsKey(species)) {
				System.out.println("old sp " + species);
				int usedCount = (Integer) LocalConfig.getInstance().getMetaboliteUsedMap().get(species);
				System.out.println("old sp " + usedCount);
				if (usedCount > 1) {
					LocalConfig.getInstance().getMetaboliteUsedMap().put(species, new Integer(usedCount - 1));									
				} else {
					LocalConfig.getInstance().getMetaboliteUsedMap().remove(species);
				}
				//System.out.println("old sp " + usedCount);
			}			
		} else if (type.equals("new")) {
			if (LocalConfig.getInstance().getMetaboliteUsedMap().containsKey(species)) {
				int usedCount = (Integer) LocalConfig.getInstance().getMetaboliteUsedMap().get(species);
				LocalConfig.getInstance().getMetaboliteUsedMap().put(species, new Integer(usedCount + 1));									
			} else {
				LocalConfig.getInstance().getMetaboliteUsedMap().put(species, new Integer(1));
			}
		}
	}
	
	public void updateReactionEquationMap(int reactionId, SBMLReactionEquation newEquation) {
		LocalConfig.getInstance().getReactionEquationMap().put(reactionId, newEquation);
	}

}


