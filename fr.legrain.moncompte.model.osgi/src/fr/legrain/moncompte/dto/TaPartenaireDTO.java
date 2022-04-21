package fr.legrain.moncompte.dto;

import java.util.Date;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaPartenaireDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 5349364124510212026L;
	
	private Integer id;	
	private Boolean actif = true;
	private String codePartenaire;
	
	private Date dateDebut;
	private String titulaireCompteBanque;
	private String nomBanque;
	private String adresse1Banque;
	private String adresse2Banque;
	private String cpBanque;
	private String villeBanque;
	private String ibanBanque;
	private String bicSwiftBanque;
	
	private Date quandCree;
	private Date quandModif;
	private String quiCree;
	private String quiModif;
	private Integer versionObj;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getCodePartenaire() {
		return codePartenaire;
	}

	public void setCodePartenaire(String codePartenaire) {
		this.codePartenaire = codePartenaire;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getTitulaireCompteBanque() {
		return titulaireCompteBanque;
	}

	public void setTitulaireCompteBanque(String titulaireCompteBanque) {
		this.titulaireCompteBanque = titulaireCompteBanque;
	}

	public String getNomBanque() {
		return nomBanque;
	}

	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}

	public String getAdresse1Banque() {
		return adresse1Banque;
	}

	public void setAdresse1Banque(String adresse1Banque) {
		this.adresse1Banque = adresse1Banque;
	}

	public String getAdresse2Banque() {
		return adresse2Banque;
	}

	public void setAdresse2Banque(String adresse2Banque) {
		this.adresse2Banque = adresse2Banque;
	}

	public String getCpBanque() {
		return cpBanque;
	}

	public void setCpBanque(String cpBanque) {
		this.cpBanque = cpBanque;
	}

	public String getVilleBanque() {
		return villeBanque;
	}

	public void setVilleBanque(String villeBanque) {
		this.villeBanque = villeBanque;
	}

	public String getIbanBanque() {
		return ibanBanque;
	}

	public void setIbanBanque(String ibanBanque) {
		this.ibanBanque = ibanBanque;
	}

	public String getBicSwiftBanque() {
		return bicSwiftBanque;
	}

	public void setBicSwiftBanque(String bicSwiftBanque) {
		this.bicSwiftBanque = bicSwiftBanque;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public String getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public String getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	

}
