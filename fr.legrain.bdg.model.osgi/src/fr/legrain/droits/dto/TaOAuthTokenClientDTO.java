package fr.legrain.droits.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;


public class TaOAuthTokenClientDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -4872682236905954542L;

	private Integer id;
	
	private String accessToken;
	private String refreshToken;
	private String scope;
	private int expiresIn;
	private String tokenType;
	private Date dateCreation;
	
	private String idUtilisateurSurService;
	private String emailUtilisateurSurService;
	
	private Integer idUtilisateur;
	private String usernameUtilisateur;
	
	private Integer idOAuthServiceClient;
	private String codeOAuthServiceClient;
	
	private Integer versionObj;
	
	public TaOAuthTokenClientDTO() {
		
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public String getIdUtilisateurSurService() {
		return idUtilisateurSurService;
	}

	public void setIdUtilisateurSurService(String idUtilisateurSurService) {
		this.idUtilisateurSurService = idUtilisateurSurService;
	}

	public String getEmailUtilisateurSurService() {
		return emailUtilisateurSurService;
	}

	public void setEmailUtilisateurSurService(String emailUtilisateurSurService) {
		this.emailUtilisateurSurService = emailUtilisateurSurService;
	}

	public Integer getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Integer idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getUsernameUtilisateur() {
		return usernameUtilisateur;
	}

	public void setUsernameUtilisateur(String usernameUtilisateur) {
		this.usernameUtilisateur = usernameUtilisateur;
	}

	public Integer getIdOAuthServiceClient() {
		return idOAuthServiceClient;
	}

	public void setIdOAuthServiceClient(Integer idOAuthServiceClient) {
		this.idOAuthServiceClient = idOAuthServiceClient;
	}

	public String getCodeOAuthServiceClient() {
		return codeOAuthServiceClient;
	}

	public void setCodeOAuthServiceClient(String codeOAuthServiceClient) {
		this.codeOAuthServiceClient = codeOAuthServiceClient;
	}
	
}
