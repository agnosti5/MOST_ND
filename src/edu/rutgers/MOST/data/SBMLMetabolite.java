package edu.rutgers.MOST.data;

public class SBMLMetabolite implements ModelMetabolite {
	
	private Integer id;
	private String metaboliteAbbreviation;
	private String metaboliteName;
	private String compartment;
	private String charge;	
	private String boundary;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMetaboliteAbbreviation(String metaboliteAbbreviation) {
		this.metaboliteAbbreviation = metaboliteAbbreviation;
	}
	
	public String getMetaboliteAbbreviation() {
		return metaboliteAbbreviation;
	}

	public String getMetaboliteName() {
		return metaboliteName;
	}
	
	public void setMetaboliteName(String metaboliteName) {
		this.metaboliteName = metaboliteName;
	}
	
	public String getCompartment() {
		return compartment;
	}
	
	public void setCompartment(String compartment) {
		this.compartment = compartment;
	}
	
	public String getCharge() {
		return charge;
	}
	
	public void setCharge(String charge) {
		this.charge = charge;
	}	

	public String getBoundary() {
		return boundary;
	}
	
	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public void update() {

	}

	public void loadById(Integer id) {

	}

	/*
	@Override
	public String toString() {
		return "SBMLMetabolite [id=" + id + ", metaboliteAbbreviation=" + metaboliteAbbreviation
		+ ", metaboliteName=" + metaboliteName
		+ ", compartment=" + compartment
		+ ", charge=" + charge
		+ ", boundary=" + boundary + "]";
	}
	*/
	
	@Override
	public String toString() {
		return "SBMLMetabolite [id=" + id 
		//+ ", metaboliteAbbreviation=" + metaboliteAbbreviation
		//+ ", metaboliteName=" + metaboliteName
		//+ ", compartment=" + compartment
		//+ ", charge=" + charge
		+ ", boundary=" + boundary + "]";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
