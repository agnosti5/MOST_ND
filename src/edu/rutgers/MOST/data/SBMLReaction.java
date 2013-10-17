package edu.rutgers.MOST.data;

public class SBMLReaction implements ModelReaction {
	
	private int id;	
	private String reactionAbbreviation;
	private String reactionName;
	private String knockout;
	private double fluxValue;
	private String reactionEqunAbbr;
	private String reactionEqunNames;
	private String reversible;
	private double biologicalObjective;
	private double upperBound;
	private double lowerBound;	
	private String geneAssociation;
	private String proteinAssociation;
	private String subsystem;
	private String proteinClass;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getReactionAbbreviation() {
		return reactionAbbreviation;
	}

	public void setReactionAbbreviation(String reactionAbbreviation) {
		this.reactionAbbreviation = reactionAbbreviation;
	}
	
	public String getReactionName() {
		return reactionName;
	}

	public void setReactionName(String reactionName) {
		this.reactionName = reactionName;
	}

	public String getKnockout() {
		return knockout;
	}

	public void setKnockout(String knockout) {
		this.knockout = knockout;
	}

	public double getFluxValue() {
		return fluxValue;
	}

	public void setFluxValue(double fluxValue) {
		this.fluxValue = fluxValue;
	}
	
	public String getReactionEqunAbbr() {
		return reactionEqunAbbr;
	}

	public void setReactionEqunAbbr(String reactionEqunAbbr) {
		this.reactionEqunAbbr = reactionEqunAbbr;
	}

	public String getReactionEqunNames() {
		return reactionEqunNames;
	}

	public void setReactionEqunNames(String reactionEqunNames) {
		this.reactionEqunNames = reactionEqunNames;
	}
	
	public String getReversible() {
		return reversible;
	}

	public void setReversible(String reversible) {
		this.reversible = reversible;
	}
	
	public double getBiologicalObjective() {
		return biologicalObjective;
	}

	public void setBiologicalObjective(double biologicalObjective) {
		this.biologicalObjective = biologicalObjective;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}
	
	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public void setGeneAssociation(String geneAssociation) {
		this.geneAssociation = geneAssociation;
	}

	public String getGeneAssociation() {
		return geneAssociation;
	}

	public void setProteinAssociation(String proteinAssociation) {
		this.proteinAssociation = proteinAssociation;
	}

	public String getProteinAssociation() {
		return proteinAssociation;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	public String getSubsystem() {
		return subsystem;
	}

	public void setProteinClass(String proteinClass) {
		this.proteinClass = proteinClass;
	}

	public String getProteinClass() {
		return proteinClass;
	}
	
	public void update() {

		
	}

	public void loadById(Integer reactionId) {

		
	}

	/*
	@Override
	public String toString() {
		return "SBMLReaction [id=" + id + ", reactionAbbreviation=" + reactionAbbreviation
				+ ", biologicalObjective=" + biologicalObjective
				+ ", upperBound=" + upperBound + ", lowerBound=" + lowerBound
				+ ", fluxValue=" + fluxValue
				+ ", geneAssociation=" + geneAssociation
				+ ", reactionName=" + reactionName + ", reversible="
				+ reversible + "]";
	}
	*/
	
	@Override
	public String toString() {
		return "SBMLReaction [id=" + id
		        + ", fluxValue=" + fluxValue
				+ ", biologicalObjective=" + biologicalObjective
				+ ", upperBound=" + upperBound
				+ ", lowerBound=" + lowerBound				
				+ ", geneAssociation=" + geneAssociation
				+ ", knockout=" + knockout + "]";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

