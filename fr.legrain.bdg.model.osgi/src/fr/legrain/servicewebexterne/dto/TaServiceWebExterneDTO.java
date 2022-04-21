package fr.legrain.servicewebexterne.dto;

import fr.legrain.bdg.model.ModelObject;

public class TaServiceWebExterneDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 3219028163643906076L;

	private Integer id;
	
	private String codeServiceWebExterne;
	private String libelleServiceWebExterne;
	private String descriptionServiceWebExterne;
	private int idTAuthentification;
	private String codeTAuthentification;
	private int idTServiceWebExterne;
	private String codeTServiceWebExterne;
	private String urlEditeur;
	private String urlService;
	private String urlGestionService;
	private String idModuleBdgAutorisation;
	private String typeAPI;
	private String versionAPI;
	private boolean defaut;
	private boolean actif;
	private boolean apiMulticompte;
	private boolean systeme;
	private byte[] logo;
	
	private Integer versionObj;

	public TaServiceWebExterneDTO() {
	}

	public TaServiceWebExterneDTO(Integer id, String codeServiceWebExterne, String libelleServiceWebExterne,
			String descriptionServiceWebExterne, int idTAuthentification, String codeTAuthentification,
			int idTServiceWebExterne, String codeTServiceWebExterne, boolean actif, boolean systeme, byte[] logo) {
		super();
		this.id = id;
		this.codeServiceWebExterne = codeServiceWebExterne;
		this.libelleServiceWebExterne = libelleServiceWebExterne;
		this.descriptionServiceWebExterne = descriptionServiceWebExterne;
		this.idTAuthentification = idTAuthentification;
		this.codeTAuthentification = codeTAuthentification;
		this.idTServiceWebExterne = idTServiceWebExterne;
		this.codeTServiceWebExterne = codeTServiceWebExterne;
		this.actif = actif;
		this.systeme = systeme;
		this.logo = logo;
	}

	public void setTaServiceWebExterneDTO(TaServiceWebExterneDTO taServiceWebExterneDTO) {
		this.id = taServiceWebExterneDTO.id;
		this.codeServiceWebExterne = taServiceWebExterneDTO.codeServiceWebExterne;
		this.libelleServiceWebExterne = taServiceWebExterneDTO.libelleServiceWebExterne;
		this.descriptionServiceWebExterne = taServiceWebExterneDTO.descriptionServiceWebExterne;
		this.idTAuthentification = taServiceWebExterneDTO.idTAuthentification;
		this.codeTAuthentification = taServiceWebExterneDTO.codeTAuthentification;
		this.idTServiceWebExterne = taServiceWebExterneDTO.idTServiceWebExterne;
		this.codeTServiceWebExterne = taServiceWebExterneDTO.codeTServiceWebExterne;
		this.urlEditeur = taServiceWebExterneDTO.urlEditeur;
		this.typeAPI = taServiceWebExterneDTO.typeAPI;
		this.versionAPI = taServiceWebExterneDTO.versionAPI;
		this.actif = taServiceWebExterneDTO.actif;
		this.apiMulticompte = taServiceWebExterneDTO.apiMulticompte;
		this.systeme = taServiceWebExterneDTO.systeme;
		this.logo = taServiceWebExterneDTO.logo;	
	}

	public static TaServiceWebExterneDTO copy(TaServiceWebExterneDTO taServiceWebExterneDTO){
		TaServiceWebExterneDTO taServiceWebExterneDTOLoc = new TaServiceWebExterneDTO();
		taServiceWebExterneDTOLoc.setId(taServiceWebExterneDTO.getId());              
		taServiceWebExterneDTOLoc.setCodeServiceWebExterne(taServiceWebExterneDTO.getCodeServiceWebExterne());           
		taServiceWebExterneDTOLoc.setIdTAuthentification(taServiceWebExterneDTO.getIdTAuthentification());             
		taServiceWebExterneDTOLoc.setCodeTAuthentification(taServiceWebExterneDTO.getCodeTAuthentification());              
		taServiceWebExterneDTOLoc.setLibelleServiceWebExterne(taServiceWebExterneDTO.getLibelleServiceWebExterne()); 
		taServiceWebExterneDTOLoc.setDescriptionServiceWebExterne(taServiceWebExterneDTO.getDescriptionServiceWebExterne());    
		taServiceWebExterneDTOLoc.setCodeTServiceWebExterne(taServiceWebExterneDTO.getCodeTServiceWebExterne());    
		taServiceWebExterneDTOLoc.setIdTServiceWebExterne(taServiceWebExterneDTO.getIdTServiceWebExterne());    
		taServiceWebExterneDTOLoc.setUrlEditeur(taServiceWebExterneDTO.getUrlEditeur());    
		taServiceWebExterneDTOLoc.setTypeAPI(taServiceWebExterneDTO.getTypeAPI());    
		taServiceWebExterneDTOLoc.setVersionAPI(taServiceWebExterneDTO.getVersionAPI());    
		taServiceWebExterneDTOLoc.setActif(taServiceWebExterneDTO.isActif());    
		taServiceWebExterneDTOLoc.setApiMulticompte(taServiceWebExterneDTO.isApiMulticompte());    
		taServiceWebExterneDTOLoc.setSysteme(taServiceWebExterneDTO.isSysteme());    
		taServiceWebExterneDTOLoc.setLogo(taServiceWebExterneDTO.getLogo());    
		return taServiceWebExterneDTOLoc;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getCodeServiceWebExterne() {
		return codeServiceWebExterne;
	}

	public void setCodeServiceWebExterne(String codeServiceWebExterne) {
		firePropertyChange("codeServiceWebExterne", this.codeServiceWebExterne, this.codeServiceWebExterne = codeServiceWebExterne);
	}

	public String getLibelleServiceWebExterne() {
		return libelleServiceWebExterne;
	}

	public void setLibelleServiceWebExterne(String libelleServiceWebExterne) {
		firePropertyChange("libelleServiceWebExterne", this.libelleServiceWebExterne, this.libelleServiceWebExterne = libelleServiceWebExterne);
	}

	public String getDescriptionServiceWebExterne() {
		return descriptionServiceWebExterne;
	}

	public void setDescriptionServiceWebExterne(String descriptionServiceWebExterne) {
		firePropertyChange("descriptionServiceWebExterne", this.descriptionServiceWebExterne, this.descriptionServiceWebExterne = descriptionServiceWebExterne);
	}

	public int getIdTAuthentification() {
		return idTAuthentification;
	}

	public void setIdTAuthentification(int idTAuthentification) {
		firePropertyChange("idTAuthentification", this.idTAuthentification, this.idTAuthentification = idTAuthentification);
	}

	public String getCodeTAuthentification() {
		return codeTAuthentification;
	}

	public void setCodeTAuthentification(String codeTAuthentification) {
		firePropertyChange("codeTAuthentification", this.codeTAuthentification, this.codeTAuthentification = codeTAuthentification);
	}

	public int getIdTServiceWebExterne() {
		return idTServiceWebExterne;
	}

	public void setIdTServiceWebExterne(int idTServiceWebExterne) {
		firePropertyChange("idTServiceWebExterne", this.idTServiceWebExterne, this.idTServiceWebExterne = idTServiceWebExterne);
	}

	public String getCodeTServiceWebExterne() {
		return codeTServiceWebExterne;
	}

	public void setCodeTServiceWebExterne(String codeTServiceWebExterne) {
		firePropertyChange("codeTServiceWebExterne", this.codeTServiceWebExterne, this.codeTServiceWebExterne = codeTServiceWebExterne);
	}

	public String getUrlEditeur() {
		return urlEditeur;
	}

	public void setUrlEditeur(String urlEditeur) {
		firePropertyChange("urlEditeur", this.urlEditeur, this.urlEditeur = urlEditeur);
	}

	public String getTypeAPI() {
		return typeAPI;
	}

	public void setTypeAPI(String typeAPI) {
		firePropertyChange("typeAPI", this.typeAPI, this.typeAPI = typeAPI);
	}

	public String getVersionAPI() {
		return versionAPI;
	}

	public void setVersionAPI(String versionAPI) {
		firePropertyChange("versionAPI", this.versionAPI, this.versionAPI = versionAPI);
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}

	public boolean isApiMulticompte() {
		return apiMulticompte;
	}

	public void setApiMulticompte(boolean apiMulticompte) {
		firePropertyChange("apiMulticompte", this.apiMulticompte, this.apiMulticompte = apiMulticompte);
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		firePropertyChange("logo", this.logo, this.logo = logo);
	}

	public String getUrlService() {
		return urlService;
	}

	public void setUrlService(String urlService) {
		firePropertyChange("urlService", this.urlService, this.urlService = urlService);
	}

	public boolean isDefaut() {
		return defaut;
	}

	public void setDefaut(boolean defaut) {
		firePropertyChange("defaut", this.defaut, this.defaut = defaut);
	}

	public String getUrlGestionService() {
		return urlGestionService;
	}

	public void setUrlGestionService(String urlGestionService) {
		this.urlGestionService = urlGestionService;
	}

	public String getIdModuleBdgAutorisation() {
		return idModuleBdgAutorisation;
	}

	public void setIdModuleBdgAutorisation(String idModuleBdgAutorisation) {
		this.idModuleBdgAutorisation = idModuleBdgAutorisation;
	}

	

}
