package edu.rutgers.MOST.data;

import java.util.ArrayList;

import edu.rutgers.MOST.config.LocalConfig;
import edu.rutgers.MOST.logic.ReactionParser;

public class ReactionEquationUpdater {
	
	private ArrayList<String> removeReactantsList;
	private ArrayList<String> removeProductsList;
	private ArrayList<String> maybeAddReactants;
	private ArrayList<String> maybeAddProducts;

	public ArrayList<String> getRemoveReactantsList() {
		return removeReactantsList;
	}

	public void setRemoveReactantsList(ArrayList<String> removeReactantsList) {
		this.removeReactantsList = removeReactantsList;
	}

	public ArrayList<String> getRemoveProductsList() {
		return removeProductsList;
	}

	public void setRemoveProductsList(ArrayList<String> removeProductsList) {
		this.removeProductsList = removeProductsList;
	}

	public ArrayList<String> getMaybeAddReactants() {
		return maybeAddReactants;
	}

	public void setMaybeAddReactants(ArrayList<String> maybeAddReactants) {
		this.maybeAddReactants = maybeAddReactants;
	}

	public ArrayList<String> getMaybeAddProducts() {
		return maybeAddProducts;
	}

	public void setMaybeAddProducts(ArrayList<String> maybeAddProducts) {
		this.maybeAddProducts = maybeAddProducts;
	}
	
	public void createLists(String oldEquation, String newEquation) {
		ReactionParser parser = new ReactionParser();
		ArrayList<String> oldReactantsList = new ArrayList<String>();
		ArrayList<String> oldProductsList = new ArrayList<String>();
		ArrayList<String> newReactantsList = new ArrayList<String>();
		ArrayList<String> newProductsList = new ArrayList<String>();
		if (oldEquation != null) {
			parser.reactionList(oldEquation);
			SBMLReactionEquation oldEqun = parser.getEquation();
			
			for (int i = 0; i < oldEqun.getReactants().size(); i++){
				String reactant = oldEqun.getReactants().get(i).getMetaboliteAbbreviation();
				oldReactantsList.add(reactant);
				//LocalConfig.getInstance().getMetaboliteNameIdMap().remove(reactant);
				//updateMetaboliteUsedMap(reactant, "old");
			}
			for (int i = 0; i < oldEqun.getProducts().size(); i++){
				String product = oldEqun.getProducts().get(i).getMetaboliteAbbreviation();
				oldProductsList.add(product);
				//LocalConfig.getInstance().getMetaboliteNameIdMap().remove(product);
				//updateMetaboliteUsedMap(product, "old");
			}
		}
		if (newEquation != null) {
			parser.reactionList(newEquation);
			SBMLReactionEquation newEqun = parser.getEquation();
			
			for (int i = 0; i < newEqun.getReactants().size(); i++){
				String reactant = newEqun.getReactants().get(i).getMetaboliteAbbreviation();
				newReactantsList.add(reactant);
				//LocalConfig.getInstance().getMetaboliteNameIdMap().remove(reactant);
				//updateMetaboliteUsedMap(reactant, "new");
			}
			for (int i = 0; i < newEqun.getProducts().size(); i++){
				String product = newEqun.getProducts().get(i).getMetaboliteAbbreviation();
				newProductsList.add(product);
				//LocalConfig.getInstance().getMetaboliteNameIdMap().remove(product);
				//updateMetaboliteUsedMap(product, "new");
			}
		}
		// contains species in old reaction equation not present in new reaction equation
		// to be removed or adjusted in maps
		ArrayList<String> removeReactantsList = removeList(oldReactantsList, newReactantsList);
		ArrayList<String> removeProductsList = removeList(oldProductsList, newProductsList);
		setRemoveReactantsList(removeReactantsList);
		setRemoveReactantsList(removeProductsList);
		
		// contains species in new reaction equation not present in old reaction equation,
		// added if user does not click "No" button
		ArrayList<String> maybeAddReactants = maybeAddList(oldReactantsList, newReactantsList);
		ArrayList<String> maybeAddProducts = maybeAddList(oldProductsList, newProductsList);
		setMaybeAddReactants(maybeAddReactants);
		setMaybeAddProducts(maybeAddProducts);
	}
	
	public ArrayList<String> removeList(ArrayList<String> oldSpeciesList, ArrayList<String> newSpeciesList) {
		ArrayList<String> removeList = new ArrayList<String>();
		for (int i = 0; i < oldSpeciesList.size(); i++) {
			if (!newSpeciesList.contains(oldSpeciesList.get(i))) {
				removeList.add(oldSpeciesList.get(i));
			}
		}
				
		System.out.println("remove " + removeList);
		return removeList;
	}
	
	public ArrayList<String> maybeAddList(ArrayList<String> oldSpeciesList, ArrayList<String> newSpeciesList) {
		ArrayList<String> maybeAddList = new ArrayList<String>();
		for (int i = 0; i < newSpeciesList.size(); i++) {
			if (!oldSpeciesList.contains(newSpeciesList.get(i))) {
				maybeAddList.add(newSpeciesList.get(i));
			}
		}
				
		System.out.println("maybe add " + maybeAddList);
		return maybeAddList;
	}
	
	public void removeOldItems(ArrayList<String> removeReactantsList, ArrayList<String> removeProductsList) {
		if (removeReactantsList != null) {
			for (int i = 0; i < removeReactantsList.size(); i++) {
				updateMetaboliteUsedMap(removeReactantsList.get(i), "old");
			}
		}		
		if (removeProductsList != null) {
			for (int i = 0; i < removeProductsList.size(); i++) {
				updateMetaboliteUsedMap(removeProductsList.get(i), "old");
			}
		}		
		System.out.println("rem" + LocalConfig.getInstance().getMetaboliteUsedMap());
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


