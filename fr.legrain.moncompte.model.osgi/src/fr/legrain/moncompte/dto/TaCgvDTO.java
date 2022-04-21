package fr.legrain.moncompte.dto;

import java.util.Date;

import fr.legrain.moncompte.commun.model.ModelObject;


public class TaCgvDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -3822222970506646502L;


	private Integer id;
	private String cgv;
	private String url;
	private Date dateCgv;
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

	public String getCgv() {
		return cgv;
	}

	public void setCgv(String cgv) {
		firePropertyChange("cgv", this.cgv, this.cgv = cgv);
	}

	public Date getDateCgv() {
		return dateCgv;
	}

	public void setDateCgv(Date dateCgv) {
		firePropertyChange("dateCgv", this.dateCgv, this.dateCgv = dateCgv);
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
