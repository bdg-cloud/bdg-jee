package fr.legrain.droits.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fr.legrain.bdg.model.ModelObject;


public class TaUtilisateurWebServiceDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -3822222970506646502L;

	private Integer id;
	private String login;
	private String passwd;
	private String description;
	private String email;
	private String autorisations;
	private Date dernierAcces;
	private String accessToken;
	private String refreshToken;
	private Boolean actif;
	private Boolean systeme;
	
	private Integer versionObj;
	
	public TaUtilisateurWebServiceDTO() {
		
	}
	
	public TaUtilisateurWebServiceDTO(Integer id, String login, String passwd, String description, String email,
			String autorisations, Date dernierAcces, String accessToken, String refreshToken, Boolean actif,
			Boolean systeme) {
		super();
		this.id = id;
		this.login = login;
		this.passwd = passwd;
		this.description = description;
		this.email = email;
		this.autorisations = autorisations;
		this.dernierAcces = dernierAcces;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.actif = actif;
		this.systeme = systeme;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAutorisations() {
		return autorisations;
	}

	public void setAutorisations(String autorisations) {
		this.autorisations = autorisations;
	}

	public Date getDernierAcces() {
		return dernierAcces;
	}

	public void setDernierAcces(Date dernierAcces) {
		this.dernierAcces = dernierAcces;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

}
