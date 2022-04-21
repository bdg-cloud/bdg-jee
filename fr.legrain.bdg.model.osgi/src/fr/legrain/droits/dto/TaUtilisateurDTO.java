package fr.legrain.droits.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fr.legrain.bdg.model.ModelObject;


public class TaUtilisateurDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -3822222970506646502L;


	private Integer id;
	private String username;
	private String passwd;
	private List<TaRRoleUtilisateurDTO> roles;
	private TaEntrepriseClientDTO taEntreprise;
	private String nom;
	private String prenom;
	private String email;
	private Date dernierAcces;
	private Boolean actif;
	private String autorisations;
	private Boolean adminDossier = false;
	private Boolean systeme = false;
	
	private Integer idUtilisateurWebService;
     
	private String token; //A creer en base quand la connexion BDG sera reellement OAuth ?
	private String accessToken; //A creer en base quand la connexion BDG sera reellement OAuth ?
	private String refreshToken; //A creer en base quand la connexion BDG sera reellement OAuth ?
	
	private String androidRegistrationToken; //token Firebase pour l'instant
	private Date dernierAccesMobile;
	
	private Integer versionObj;
	
	public TaUtilisateurDTO() {
		
	}
	
	public TaUtilisateurDTO(Integer id, String username, String nom, String prenom, String email, Date dernierAcces,
			Boolean actif) {
		super();
		this.id = id;
		this.username = username;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.dernierAcces = dernierAcces;
		this.actif = actif;
	}
	
	public TaUtilisateurDTO(Integer id, String username, String nom, String prenom, String email, Date dernierAcces,
			Boolean actif, String autorisations, Integer versionObj) {
		super();
		this.id = id;
		this.username = username;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.dernierAcces = dernierAcces;
		this.actif = actif;
		this.autorisations = autorisations;
		this.versionObj = versionObj;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public List<TaRRoleUtilisateurDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<TaRRoleUtilisateurDTO> roles) {
		this.roles = roles;
	}

	public TaEntrepriseClientDTO getTaEntreprise() {
		return taEntreprise;
	}

	public void setTaEntreprise(TaEntrepriseClientDTO userCompany) {
		this.taEntreprise = userCompany;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDernierAcces() {
		return dernierAcces;
	}

	public void setDernierAcces(Date dernierAcces) {
		this.dernierAcces = dernierAcces;
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

	public Boolean getAdminDossier() {
		return adminDossier;
	}

	public void setAdminDossier(Boolean adminDossier) {
		this.adminDossier = adminDossier;
	}

	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

	public String getAutorisations() {
		return autorisations;
	}

	public void setAutorisations(String autorisations) {
		this.autorisations = autorisations;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getAndroidRegistrationToken() {
		return androidRegistrationToken;
	}

	public void setAndroidRegistrationToken(String androidRegistrationToken) {
		this.androidRegistrationToken = androidRegistrationToken;
	}

	public Date getDernierAccesMobile() {
		return dernierAccesMobile;
	}

	public void setDernierAccesMobile(Date dernierAccesMobile) {
		this.dernierAccesMobile = dernierAccesMobile;
	}

	public Integer getIdUtilisateurWebService() {
		return idUtilisateurWebService;
	}

	public void setIdUtilisateurWebService(Integer idUtilisateurWebService) {
		this.idUtilisateurWebService = idUtilisateurWebService;
	}

}
