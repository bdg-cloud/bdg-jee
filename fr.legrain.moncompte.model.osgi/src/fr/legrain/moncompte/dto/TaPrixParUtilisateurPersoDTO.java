package fr.legrain.moncompte.dto;

import java.math.BigDecimal;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaPrixParUtilisateurPersoDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -1159785041426015863L;
	
	private Integer id;
	private BigDecimal prixHT;
	private BigDecimal tauxTVA;
	private BigDecimal prixTTC;
	private Integer nbUtilisateur;
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

	public BigDecimal getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(BigDecimal prixHT) {
		firePropertyChange("prixHT", this.prixHT, this.prixHT = prixHT);
	}

	public BigDecimal getTauxTVA() {
		return tauxTVA;
	}

	public void setTauxTVA(BigDecimal tauxTVA) {
		firePropertyChange("tauxTVA", this.tauxTVA, this.tauxTVA = tauxTVA);
	}

	public BigDecimal getPrixTTC() {
		return prixTTC;
	}

	public void setPrixTTC(BigDecimal prixTTC) {
		firePropertyChange("prixTTC", this.prixTTC, this.prixTTC = prixTTC);
	}

	public Integer getNbUtilisateur() {
		return nbUtilisateur;
	}

	public void setNbUtilisateur(Integer nbUtilisateur) {
		firePropertyChange("nbUtilisateur", this.nbUtilisateur, this.nbUtilisateur = nbUtilisateur);
	}



}
