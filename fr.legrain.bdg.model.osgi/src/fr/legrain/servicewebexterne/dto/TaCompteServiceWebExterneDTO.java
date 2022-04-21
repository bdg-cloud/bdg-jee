package fr.legrain.servicewebexterne.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaCompteServiceWebExterneDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8524983769544606811L;

	private Integer id;
	
	//private int idCompteServiceWebExterne;
	private String codeCompteServiceWebExterne;
	private String libelleCompteServiceWebExterne;
	private String descriptionCompteServiceWebExterne;
	
	private int idTAuthentification;
	private String codeTAuthentification;
	private int idServiceWebExterne;
	private String codeServiceWebExterne;
	private byte[] logo;
	private boolean serviceActif;
	private String libelleServiceWebExterne;
	
	
	private String urlService;
	private String login;
	private String password;
	private String apiKey1;
	private String apiKey2;
	private String valeur1;
	private String valeur2;
	private String valeur3;
	private String valeur4;
	private String valeur5;
	private String valeur6;
	private String valeur7;
	private String valeur8;
	private String valeur9;
	private String valeur10;
	private boolean actif;
	private boolean defaut;
	private boolean systeme;
	private boolean compteTest;
	private Date debut;
	private Date fin;
	private Date derniereUtilisation;
	
	private Integer versionObj;

	public TaCompteServiceWebExterneDTO() {
	}

	public void setTaCompteServiceWebExterneDTO(TaCompteServiceWebExterneDTO taCompteServiceWebExterneDTO) {
		this.id = taCompteServiceWebExterneDTO.id;
		this.codeCompteServiceWebExterne = taCompteServiceWebExterneDTO.codeCompteServiceWebExterne;
		this.libelleCompteServiceWebExterne = taCompteServiceWebExterneDTO.libelleCompteServiceWebExterne;
		this.descriptionCompteServiceWebExterne = taCompteServiceWebExterneDTO.descriptionCompteServiceWebExterne;
		this.idTAuthentification = taCompteServiceWebExterneDTO.idTAuthentification;
		this.codeTAuthentification = taCompteServiceWebExterneDTO.codeTAuthentification;
		this.idServiceWebExterne = taCompteServiceWebExterneDTO.idServiceWebExterne;
		this.codeServiceWebExterne = taCompteServiceWebExterneDTO.codeServiceWebExterne;
		this.urlService = taCompteServiceWebExterneDTO.urlService;
		this.login = taCompteServiceWebExterneDTO.login;
		this.password = taCompteServiceWebExterneDTO.password;
		this.apiKey1 = taCompteServiceWebExterneDTO.apiKey1;
		this.apiKey2 = taCompteServiceWebExterneDTO.apiKey2;
		this.valeur1 = taCompteServiceWebExterneDTO.valeur1;
		this.valeur2 = taCompteServiceWebExterneDTO.valeur2;
		this.valeur3 = taCompteServiceWebExterneDTO.valeur3;
		this.valeur4 = taCompteServiceWebExterneDTO.valeur4;
		this.valeur5 = taCompteServiceWebExterneDTO.valeur5;
		this.valeur6 = taCompteServiceWebExterneDTO.valeur6;
		this.valeur7 = taCompteServiceWebExterneDTO.valeur7;
		this.valeur8 = taCompteServiceWebExterneDTO.valeur8;
		this.valeur9 = taCompteServiceWebExterneDTO.valeur9;
		this.valeur10 = taCompteServiceWebExterneDTO.valeur10;
		this.actif = taCompteServiceWebExterneDTO.actif;
		this.defaut = taCompteServiceWebExterneDTO.defaut;
		this.systeme = taCompteServiceWebExterneDTO.systeme;
		this.compteTest = taCompteServiceWebExterneDTO.compteTest;
		this.debut = taCompteServiceWebExterneDTO.debut;
		this.fin = taCompteServiceWebExterneDTO.fin;
		this.derniereUtilisation = taCompteServiceWebExterneDTO.derniereUtilisation;
	}

	public static TaCompteServiceWebExterneDTO copy(TaCompteServiceWebExterneDTO taCompteServiceWebExterneDTO){
		TaCompteServiceWebExterneDTO taCompteServiceWebExterneDTOLoc = new TaCompteServiceWebExterneDTO();
		taCompteServiceWebExterneDTOLoc.setId(taCompteServiceWebExterneDTO.getId());              
//		taCompteServiceWebExterneDTOLoc.setIdCompteServiceWebExterne(taCompteServiceWebExterneDTO.getIdCompteServiceWebExterne());
		taCompteServiceWebExterneDTOLoc.setCodeCompteServiceWebExterne(taCompteServiceWebExterneDTO.getCodeCompteServiceWebExterne());
		taCompteServiceWebExterneDTOLoc.setLibelleCompteServiceWebExterne(taCompteServiceWebExterneDTO.getLibelleCompteServiceWebExterne());
		taCompteServiceWebExterneDTOLoc.setDescriptionCompteServiceWebExterne(taCompteServiceWebExterneDTO.getDescriptionCompteServiceWebExterne());
		taCompteServiceWebExterneDTOLoc.setIdServiceWebExterne(taCompteServiceWebExterneDTO.getIdServiceWebExterne());
		taCompteServiceWebExterneDTOLoc.setCodeServiceWebExterne(taCompteServiceWebExterneDTO.getCodeServiceWebExterne());
		taCompteServiceWebExterneDTOLoc.setIdTAuthentification(taCompteServiceWebExterneDTO.getIdTAuthentification());
		taCompteServiceWebExterneDTOLoc.setCodeTAuthentification(taCompteServiceWebExterneDTO.getCodeTAuthentification());
		taCompteServiceWebExterneDTOLoc.setUrlService(taCompteServiceWebExterneDTO.getUrlService());
		taCompteServiceWebExterneDTOLoc.setLogin(taCompteServiceWebExterneDTO.getLogin());
		taCompteServiceWebExterneDTOLoc.setPassword(taCompteServiceWebExterneDTO.getPassword());
		taCompteServiceWebExterneDTOLoc.setApiKey1(taCompteServiceWebExterneDTO.getApiKey1());
		taCompteServiceWebExterneDTOLoc.setApiKey2(taCompteServiceWebExterneDTO.getApiKey2());
		taCompteServiceWebExterneDTOLoc.setValeur1(taCompteServiceWebExterneDTO.getValeur1());
		taCompteServiceWebExterneDTOLoc.setValeur2(taCompteServiceWebExterneDTO.getValeur2());
		taCompteServiceWebExterneDTOLoc.setValeur3(taCompteServiceWebExterneDTO.getValeur3());
		taCompteServiceWebExterneDTOLoc.setValeur4(taCompteServiceWebExterneDTO.getValeur4());
		taCompteServiceWebExterneDTOLoc.setValeur5(taCompteServiceWebExterneDTO.getValeur5());
		taCompteServiceWebExterneDTOLoc.setValeur6(taCompteServiceWebExterneDTO.getValeur6());
		taCompteServiceWebExterneDTOLoc.setValeur7(taCompteServiceWebExterneDTO.getValeur7());
		taCompteServiceWebExterneDTOLoc.setValeur8(taCompteServiceWebExterneDTO.getValeur8());
		taCompteServiceWebExterneDTOLoc.setValeur9(taCompteServiceWebExterneDTO.getValeur1());
		taCompteServiceWebExterneDTOLoc.setValeur10(taCompteServiceWebExterneDTO.getValeur10());
		taCompteServiceWebExterneDTOLoc.setActif(taCompteServiceWebExterneDTO.isActif());
		taCompteServiceWebExterneDTOLoc.setDefaut(taCompteServiceWebExterneDTO.isDefaut());
		taCompteServiceWebExterneDTOLoc.setSysteme(taCompteServiceWebExterneDTO.isSysteme());
		taCompteServiceWebExterneDTOLoc.setCompteTest(taCompteServiceWebExterneDTO.isCompteTest());
		taCompteServiceWebExterneDTOLoc.setDebut(taCompteServiceWebExterneDTO.getDebut());
		taCompteServiceWebExterneDTOLoc.setFin(taCompteServiceWebExterneDTO.getFin());
		taCompteServiceWebExterneDTOLoc.setDerniereUtilisation(taCompteServiceWebExterneDTO.getDerniereUtilisation());
		return taCompteServiceWebExterneDTOLoc;
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

//	public int getIdCompteServiceWebExterne() {
//		return idCompteServiceWebExterne;
//	}
//
//	public void setIdCompteServiceWebExterne(int idCompteServiceWebExterne) {
//		firePropertyChange("idCompteServiceWebExterne", this.idCompteServiceWebExterne, this.idCompteServiceWebExterne = idCompteServiceWebExterne);
//	}

	public String getCodeCompteServiceWebExterne() {
		return codeCompteServiceWebExterne;
	}

	public void setCodeCompteServiceWebExterne(String codeCompteServiceWebExterne) {
		firePropertyChange("codeCompteServiceWebExterne", this.codeCompteServiceWebExterne, this.codeCompteServiceWebExterne = codeCompteServiceWebExterne);
	}

	public String getLibelleCompteServiceWebExterne() {
		return libelleCompteServiceWebExterne;
	}

	public void setLibelleCompteServiceWebExterne(String libelleCompteServiceWebExterne) {
		firePropertyChange("libelleCompteServiceWebExterne", this.libelleCompteServiceWebExterne, this.libelleCompteServiceWebExterne = libelleCompteServiceWebExterne);
	}

	public String getDescriptionCompteServiceWebExterne() {
		return descriptionCompteServiceWebExterne;
	}

	public void setDescriptionCompteServiceWebExterne(String descriptionCompteServiceWebExterne) {
		firePropertyChange("descriptionCompteServiceWebExterne", this.descriptionCompteServiceWebExterne, this.descriptionCompteServiceWebExterne = descriptionCompteServiceWebExterne);
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

	public int getIdServiceWebExterne() {
		return idServiceWebExterne;
	}

	public void setIdServiceWebExterne(int idServiceWebExterne) {
		firePropertyChange("idServiceWebExterne", this.idServiceWebExterne, this.idServiceWebExterne = idServiceWebExterne);
	}

	public String getCodeServiceWebExterne() {
		return codeServiceWebExterne;
	}

	public void setCodeServiceWebExterne(String codeServiceWebExterne) {
		firePropertyChange("codeServiceWebExterne", this.codeServiceWebExterne, this.codeServiceWebExterne = codeServiceWebExterne);
	}

	public String getUrlService() {
		return urlService;
	}

	public void setUrlService(String urlService) {
		firePropertyChange("urlService", this.urlService, this.urlService = urlService);
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		firePropertyChange("login", this.login, this.login = login);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		firePropertyChange("password", this.password, this.password = password);
	}

	public String getApiKey1() {
		return apiKey1;
	}

	public void setApiKey1(String apiKey1) {
		firePropertyChange("apiKey1", this.apiKey1, this.apiKey1 = apiKey1);
	}

	public String getApiKey2() {
		return apiKey2;
	}

	public void setApiKey2(String apiKey2) {
		firePropertyChange("apiKey2", this.apiKey2, this.apiKey2 = apiKey2);
	}

	public String getValeur1() {
		return valeur1;
	}

	public void setValeur1(String valeur1) {
		firePropertyChange("valeur1", this.valeur1, this.valeur1 = valeur1);
	}

	public String getValeur2() {
		return valeur2;
	}

	public void setValeur2(String valeur2) {
		firePropertyChange("valeur2", this.valeur2, this.valeur2 = valeur2);
	}

	public String getValeur3() {
		return valeur3;
	}

	public void setValeur3(String valeur3) {
		firePropertyChange("valeur3", this.valeur3, this.valeur3 = valeur3);
	}

	public String getValeur4() {
		return valeur4;
	}

	public void setValeur4(String valeur4) {
		firePropertyChange("valeur4", this.valeur4, this.valeur4 = valeur4);
	}

	public String getValeur5() {
		return valeur5;
	}

	public void setValeur5(String valeur5) {
		firePropertyChange("valeur5", this.valeur5, this.valeur5 = valeur5);
	}

	public String getValeur6() {
		return valeur6;
	}

	public void setValeur6(String valeur6) {
		firePropertyChange("valeur6", this.valeur6, this.valeur6 = valeur6);
	}

	public String getValeur7() {
		return valeur7;
	}

	public void setValeur7(String valeur7) {
		firePropertyChange("valeur7", this.valeur7, this.valeur7 = valeur7);
	}

	public String getValeur8() {
		return valeur8;
	}

	public void setValeur8(String valeur8) {
		firePropertyChange("valeur8", this.valeur8, this.valeur8 = valeur8);
	}

	public String getValeur9() {
		return valeur9;
	}

	public void setValeur9(String valeur9) {
		firePropertyChange("valeur9", this.valeur9, this.valeur9 = valeur9);
	}

	public String getValeur10() {
		return valeur10;
	}

	public void setValeur10(String valeur10) {
		firePropertyChange("valeur10", this.valeur10, this.valeur10 = valeur10);
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		firePropertyChange("actif", this.actif, this.actif = actif);
	}

	public boolean isDefaut() {
		return defaut;
	}

	public void setDefaut(boolean defaut) {
		firePropertyChange("defaut", this.defaut, this.defaut = defaut);
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		firePropertyChange("systeme", this.systeme, this.systeme = systeme);
	}

	public boolean isCompteTest() {
		return compteTest;
	}

	public void setCompteTest(boolean compteTest) {
		firePropertyChange("compteTest", this.compteTest, this.compteTest = compteTest);
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		firePropertyChange("debut", this.debut, this.debut = debut);
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		firePropertyChange("fin", this.fin, this.fin = fin);
	}

	public Date getDerniereUtilisation() {
		return derniereUtilisation;
	}

	public void setDerniereUtilisation(Date derniereUtilisation) {
		firePropertyChange("derniereUtilisation", this.derniereUtilisation, this.derniereUtilisation = derniereUtilisation);
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public boolean isServiceActif() {
		return serviceActif;
	}

	public void setServiceActif(boolean serviceActif) {
		this.serviceActif = serviceActif;
	}

	public String getLibelleServiceWebExterne() {
		return libelleServiceWebExterne;
	}

	public void setLibelleServiceWebExterne(String libelleServiceWebExterne) {
		this.libelleServiceWebExterne = libelleServiceWebExterne;
	}

	
}
