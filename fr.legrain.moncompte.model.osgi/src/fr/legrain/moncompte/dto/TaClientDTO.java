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


public class TaClientDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -3822222970506646502L;


	private Integer id;
	private String username;
	private String passwd;
	private String code;
	private String nom;
	private String prenom;
	private String email;
	private Date dernierAcces;
	private Boolean actif;
	private Integer versionObj;	
	private String identifiantFtp;	
	private String mdpFtp;	
	private String serveurFtp;
	
  
	
	
	private TaAdresseDTO adresse1 ;
	 
	private TaAdresseDTO adresse2 ;
	 
	private TaAdresseDTO adresse3 ;
	
	
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		firePropertyChange("nom", this.nom, this.nom = nom);
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		firePropertyChange("prenom", this.prenom, this.prenom = prenom);
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

	public String getIdentifiantFtp() {
		return identifiantFtp;
	}

	public void setIdentifiantFtp(String identifiantFtp) {
		firePropertyChange("identifiantFtp", this.identifiantFtp, this.identifiantFtp = identifiantFtp);
	}

	public String getMdpFtp() {
		return mdpFtp;
	}

	public void setMdpFtp(String mdpFtp) {
		firePropertyChange("mdpFtp", this.mdpFtp, this.mdpFtp = mdpFtp);
	}

	public String getServeurFtp() {
		return serveurFtp;
	}

	public void setServeurFtp(String serveurFtp) {
		firePropertyChange("serveurFtp", this.serveurFtp, this.serveurFtp = serveurFtp);
	}

	public TaAdresseDTO getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(TaAdresseDTO adresse1) {
		firePropertyChange("adresse1", this.adresse1, this.adresse1 = adresse1);
	}

	public TaAdresseDTO getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(TaAdresseDTO adresse2) {
		firePropertyChange("adresse2", this.adresse2, this.adresse2 = adresse2);
	}

	public TaAdresseDTO getAdresse3() {
		return adresse3;
	}

	public void setAdresse3(TaAdresseDTO adresse3) {
		firePropertyChange("adresse3", this.adresse3, this.adresse3 = adresse3);
	}

}
