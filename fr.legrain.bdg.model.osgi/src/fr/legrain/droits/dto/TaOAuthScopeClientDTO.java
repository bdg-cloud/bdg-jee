package fr.legrain.droits.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;


public class TaOAuthScopeClientDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -4872682236905954542L;

	private Integer id;
	
	private String code;
	private String libelle;
	private String description;
	private String identifiantService;
	private Boolean actif;
	private Boolean systeme;
	
	private Integer idOAuthServiceClient;
	private String codeOAuthServiceClient;
	
	private Integer versionObj;
	
	public TaOAuthScopeClientDTO() {
		
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

	public String getIdentifiantService() {
		return identifiantService;
	}

	public void setIdentifiantService(String identifiantService) {
		this.identifiantService = identifiantService;
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
	
}
