package fr.legrain.moncompte.dto;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaAdresseDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -3822222970506646502L;


	private Integer id;
	private String nomEntreprise;
	private String adresse1;
	private String adresse2;
	private String adresse3;	
	private String codePostal;
	private String ville;	
	private String pays;
	private String numTel1;
	private String numTel2;
	private String numFax;
	private String numPort;
	private String email;
	private String web;
	
	private Integer versionObj;	

	
  
	

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		firePropertyChange("nomEntreprise", this.nomEntreprise, this.nomEntreprise = nomEntreprise);
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		firePropertyChange("adresse1", this.adresse1, this.adresse1 = adresse1);
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		firePropertyChange("adresse2", this.adresse2, this.adresse2 = adresse2);
	}

	public String getAdresse3() {
		return adresse3;
	}

	public void setAdresse3(String adresse3) {
		firePropertyChange("adresse3", this.adresse3, this.adresse3 = adresse3);
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		firePropertyChange("codePostal", this.codePostal, this.codePostal = codePostal);
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		firePropertyChange("ville", this.ville, this.ville = ville);
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		firePropertyChange("pays", this.pays, this.pays = pays);
	}

	public String getNumTel1() {
		return numTel1;
	}

	public void setNumTel1(String numTel1) {
		firePropertyChange("numTel1", this.numTel1, this.numTel1 = numTel1);
	}

	public String getNumTel2() {
		return numTel2;
	}

	public void setNumTel2(String numTel2) {
		firePropertyChange("numTel2", this.numTel2, this.numTel2 = numTel2);
	}

	public String getNumFax() {
		return numFax;
	}

	public void setNumFax(String numFax) {
		firePropertyChange("numFax", this.numFax, this.numFax = numFax);
	}

	public String getNumPort() {
		return numPort;
	}

	public void setNumPort(String numPort) {
		firePropertyChange("numPort", this.numPort, this.numPort = numPort);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		firePropertyChange("email", this.email, this.email = email);
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		firePropertyChange("web", this.web, this.web = web);
	}
}
