package fr.legrain.moncompte.dto;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaDossierDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 2308371531866296305L;
	
	private Integer id;

	private String code;
	private String description;
	private Boolean actif;
	private Integer nbUtilisateur;	
	private Integer nbPosteInstalle;	
	private Boolean accesWs;
	private String nomTiers;
	private String codeTiers;
	public String getCodeNiveau() {
		return codeNiveau;
	}

	public void setCodeNiveau(String codeNiveau) {
		this.codeNiveau = codeNiveau;
	}

	public String getLibelleNiveau() {
		return libelleNiveau;
	}

	public void setLibelleNiveau(String libelleNiveau) {
		this.libelleNiveau = libelleNiveau;
	}

	private String codeNiveau;
	private String libelleNiveau;
	
	public TaDossierDTO(Integer id, String code, String description, Boolean actif,
			Integer nbUtilisateur, Integer nbPosteInstalle, Boolean accesWs,
			String nomTiers, String codeTiers) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.actif = actif;
		this.nbUtilisateur = nbUtilisateur;
		this.nbPosteInstalle = nbPosteInstalle;
		this.accesWs = accesWs;
		this.nomTiers = nomTiers;
		this.codeTiers = codeTiers;
	}

	private Integer versionObj;
	
	public TaDossierDTO() {
		
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		firePropertyChange("description", this.description, this.description = description);
	}

	

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		firePropertyChange("code", this.code, this.code = code);
	}

	public Integer getNbUtilisateur() {
		return nbUtilisateur;
	}

	public void setNbUtilisateur(Integer nbUtilisateur) {
		firePropertyChange("nbUtilisateur", this.nbUtilisateur, this.nbUtilisateur = nbUtilisateur);
	}

	public Integer getNbPosteInstalle() {
		return nbPosteInstalle;
	}

	public void setNbPosteInstalle(Integer nbPosteInstalle) {
		firePropertyChange("nbPosteInstalle", this.nbPosteInstalle, this.nbPosteInstalle = nbPosteInstalle);
	}

	public Boolean getAccesWs() {
		return accesWs;
	}

	public void setAccesWs(Boolean accesWs) {
		firePropertyChange("accesWs", this.accesWs, this.accesWs = accesWs);
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		firePropertyChange("nomTiers", this.nomTiers, this.nomTiers = nomTiers);
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		firePropertyChange("codeTiers", this.codeTiers, this.codeTiers = codeTiers);
	}

	
}
