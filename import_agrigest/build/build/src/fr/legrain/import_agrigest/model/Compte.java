package fr.legrain.import_agrigest.model;

public class Compte implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7176756229228707615L;
	
	private String cDossier; //8
	private String cCpt; //8
	private String cLib; //25
	private String cU1; //2
	private String cU2; //2
	
	
	
	
	public String getcDossier() {
		return cDossier;
	}
	public void setcDossier(String cDossier) {
		this.cDossier = cDossier;
	}
	public String getcCpt() {
		return cCpt;
	}
	public void setcCpt(String cCpt) {
		this.cCpt = cCpt;
	}
	public String getcLib() {
		return cLib;
	}
	public void setcLib(String cLib) {
		this.cLib = cLib;
	}
	public String getcU1() {
		return cU1;
	}
	public void setcU1(String cU1) {
		this.cU1 = cU1;
	}
	public String getcU2() {
		return cU2;
	}
	public void setcU2(String cU2) {
		this.cU2 = cU2;
	}

	
	
}
