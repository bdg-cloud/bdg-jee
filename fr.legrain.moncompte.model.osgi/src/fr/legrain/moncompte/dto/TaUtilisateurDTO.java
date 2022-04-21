package fr.legrain.moncompte.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fr.legrain.moncompte.commun.model.ModelObject;
import fr.legrain.moncompte.model.TaAdresse;


public class TaUtilisateurDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -3822222970506646502L;


	private Integer id;
	private String username;
	private String passwd;
	private List<TaRRoleUtilisateurDTO> roles;
	private String email;
	private Date dernierAcces;
	private Boolean actif;
	private Integer versionObj;	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		firePropertyChange("username", this.username, this.username = username);
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		firePropertyChange("passwd", this.passwd, this.passwd = passwd);
	}

	public List<TaRRoleUtilisateurDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<TaRRoleUtilisateurDTO> roles) {
		firePropertyChange("roles", this.roles, this.roles = roles);
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		firePropertyChange("email", this.email, this.email = email);
	}

	public Date getDernierAcces() {
		return dernierAcces;
	}

	public void setDernierAcces(Date dernierAcces) {
		firePropertyChange("dernierAcces", this.dernierAcces, this.dernierAcces = dernierAcces);
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		firePropertyChange("versionObj", this.versionObj, this.versionObj = versionObj);
	}

}
