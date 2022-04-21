package fr.legrain.moncompte.dto;

import java.util.Date;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaCgPartenaireDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -860906493805432790L;
	
	private Integer id;
	private String cgPartenaire;
	private String url;
	private Date dateCgPartenaire;
	private Boolean actif;
	
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

	public String getCgPartenaire() {
		return cgPartenaire;
	}

	public void setCgPartenaire(String cgPartenaire) {
		firePropertyChange("cgPartenaire", this.cgPartenaire, this.cgPartenaire = cgPartenaire);
	}

	public Date getDateCgPartenaire() {
		return dateCgPartenaire;
	}

	public void setDateCgPartenaire(Date dateCgPartenaire) {
		firePropertyChange("dateCgPartenaire", this.dateCgPartenaire, this.dateCgPartenaire = dateCgPartenaire);
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		firePropertyChange("url", this.url, this.url = url);
	}

	
}
