package fr.legrain.tiers.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.model.TaTiers;

public class TaEspaceClientDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = 2371159789946188318L;
	private Integer id;
	private String email;
	private String password;
	private Integer idTiers;
	private String codeTiers;
	private Boolean actif;
	private String infoCompteCryptees;
	private Integer versionObj;
	private String nom;
	private String prenom;
	
	private String token; //A creer en base quand la connexion BDG sera reellement OAuth ?
	private String accessToken; //A creer en base quand la connexion BDG sera reellement OAuth ?
	private String refreshToken; //A creer en base quand la connexion BDG sera reellement OAuth ?
	
	private Date dateDerniereConnexion;

	public TaEspaceClientDTO() {
	}
	
	

	public TaEspaceClientDTO(Integer id, String email, String password, Integer idTiers, String codeTiers,
			Boolean actif, Integer versionObj, String nom, String prenom, Date dateDerniereConnexion) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.idTiers = idTiers;
		this.codeTiers = codeTiers;
		this.actif = actif;
		this.versionObj = versionObj;
		this.nom = nom;
		this.prenom = prenom;
		this.dateDerniereConnexion = dateDerniereConnexion;

	}



	public void setEspaceClientDTO(TaEspaceClientDTO taEspaceClientDTO) {
		this.id = taEspaceClientDTO.id;
		this.email = taEspaceClientDTO.email;
		this.password = taEspaceClientDTO.password;
		this.idTiers = taEspaceClientDTO.idTiers;
	}

	
	public static TaEspaceClientDTO copy(TaEspaceClientDTO taEspaceClientDTO){
		TaEspaceClientDTO taEspaceClientDTOLoc = new TaEspaceClientDTO();
		taEspaceClientDTOLoc.setId(taEspaceClientDTO.getId());                
		taEspaceClientDTOLoc.setIdTiers(taEspaceClientDTO.getIdTiers());                
		taEspaceClientDTOLoc.setEmail(taEspaceClientDTO.getEmail());                
		taEspaceClientDTOLoc.setPassword(taEspaceClientDTO.getPassword());                
		return taEspaceClientDTOLoc;
	}
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String login) {
		firePropertyChange("login", this.email, this.email = login);
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		firePropertyChange("password", this.password, this.password = password);
	}

	

	public Integer getIdTiers() {
		return this.idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		firePropertyChange("idTiers", this.idTiers, this.idTiers = idTiers);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getInfoCompteCryptees() {
		return infoCompteCryptees;
	}

	public void setInfoCompteCryptees(String infoCompteCryptees) {
		this.infoCompteCryptees = infoCompteCryptees;
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



	public Date getDateDerniereConnexion() {
		return dateDerniereConnexion;
	}



	public void setDateDerniereConnexion(Date dateDerniereConnexion) {
		this.dateDerniereConnexion = dateDerniereConnexion;
	}

}
