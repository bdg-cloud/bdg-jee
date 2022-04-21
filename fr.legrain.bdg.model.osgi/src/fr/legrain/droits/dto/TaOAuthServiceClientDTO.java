package fr.legrain.droits.dto;

import fr.legrain.bdg.model.ModelObject;


public class TaOAuthServiceClientDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 8050600762472715563L;

	private Integer id;
	
	private String code;
	private String libelle;
	private String description;
	private String url;
	private String urlEndPoint;
	private Boolean systeme;
	private Boolean actif;
	
	private Integer versionObj;
	
	public TaOAuthServiceClientDTO() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		this.actif = actif;
	}


	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlEndPoint() {
		return urlEndPoint;
	}

	public void setUrlEndPoint(String urlEndPoint) {
		this.urlEndPoint = urlEndPoint;
	}

}
