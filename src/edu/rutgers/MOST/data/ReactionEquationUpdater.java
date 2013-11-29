package edu.rutgers.MOST.data;

import edu.rutgers.MOST.config.LocalConfig;

public class ReactionEquationUpdater {

	public void updateReactionEquations(int reactionId, SBMLReactionEquation oldEquation, SBMLReactionEquation newEquation) {
		if (oldEquation != null) {
			// update for old equation
			for (int i = 0; i < oldEquation.getReactants().size(); i++){
				String reactant = oldEquation.getReactants().get(i).getMetaboliteAbbreviation();
				LocalConfig.getInstance().getMetaboliteNameIdMap().remove(reactant);
				updateMetaboliteUsedMap(reactant, "old");
			}
			for (int i = 0; i < oldEquation.getProducts().size(); i++){
				String product = oldEquation.getProducts().get(i).getMetaboliteAbbreviation();
				LocalConfig.getInstance().getMetaboliteNameIdMap().remove(product);
				updateMetaboliteUsedMap(product, "old");
			}
		}
		if (newEquation != null) {
			// update for new equation
			for (int i = 0; i < newEquation.getReactants().size(); i++){
				int metabId = oldEquation.getReactants().get(i).getMetaboliteId();
				String reactant = oldEquation.getReactants().get(i).getMetaboliteAbbreviation();
				LocalConfig.getInstance().getMetaboliteNameIdMap().put(reactant, metabId);
				updateMetaboliteUsedMap(reactant, "new");
			}
			for (int i = 0; i < newEquation.getProducts().size(); i++){
				int metabId = oldEquation.getProducts().get(i).getMetaboliteId();
				String product = oldEquation.getProducts().get(i).getMetaboliteAbbreviation();
				LocalConfig.getInstance().getMetaboliteNameIdMap().put(product, metabId);
				updateMetaboliteUsedMap(product, "new");
			}
			updateReactionEquationMap(reactionId, newEquation);
		}		
	}
	
	public void updateMetaboliteUsedMap(String species, String type) {
		if (type.equals("old")) {
			int usedCount = (Integer) LocalConfig.getInstance().getMetaboliteUsedMap().get(species);
			if (usedCount > 1) {
				LocalConfig.getInstance().getMetaboliteUsedMap().put(species, new Integer(usedCount - 1));									
			} else {
				LocalConfig.getInstance().getMetaboliteUsedMap().remove(species);
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


