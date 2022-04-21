package fr.legrain.general.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.model.TaTiers;

public class TaLiaisonDossierMaitreDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = 2371159789946188318L;
	private Integer id;
	private String email;
	private String password;
	
	private Integer versionObj;
	
	private String codeTiers;

	
	private String token; 
	private String refreshToken;
	

	public TaLiaisonDossierMaitreDTO() {
	}
	
	

	public TaLiaisonDossierMaitreDTO(Integer id, String email, String password, Integer versionObj) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.versionObj = versionObj;

	}



	public void setLiaisonDossierMaitreDTO(TaLiaisonDossierMaitreDTO taLiaisonDossierMaitreDTO) {
		this.id = taLiaisonDossierMaitreDTO.id;
		this.email = taLiaisonDossierMaitreDTO.email;
		this.password = taLiaisonDossierMaitreDTO.password;
	}

	
	public static TaLiaisonDossierMaitreDTO copy(TaLiaisonDossierMaitreDTO taLiaisonDossierMaitreDTO){
		TaLiaisonDossierMaitreDTO taLiaisonDossierMaitreDTOLoc = new TaLiaisonDossierMaitreDTO();
		taLiaisonDossierMaitreDTOLoc.setId(taLiaisonDossierMaitreDTO.getId());                
		taLiaisonDossierMaitreDTOLoc.setEmail(taLiaisonDossierMaitreDTO.getEmail());                
		taLiaisonDossierMaitreDTOLoc.setPassword(taLiaisonDossierMaitreDTO.getPassword());                
		return taLiaisonDossierMaitreDTOLoc;
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

	
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}



	public String getCodeTiers() {
		return codeTiers;
	}



	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}





}
