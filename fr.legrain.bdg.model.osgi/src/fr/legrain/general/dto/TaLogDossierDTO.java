package fr.legrain.general.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateurWebService;

public class TaLogDossierDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -5724492806619377832L;
	
	private int id;
	private Date quand;
	private Integer idUtilisateurDossier;
	private String usernameUtilisateurDossier;
	private Integer idUtilisateurWebService;
	private String loginUtilisateurDossier;
	private String utilisateur;
	private String dossier;
	private String uuid;
	private int niveauLog; //trace, info, warning, erreur, ... => faire une table ? base programme ?
	private int typeLog; //CRUD,login, logout => faire une table ? base programme ?
	private Boolean appelDistant; //ejb, api => faire une table ? base programme ?
	private String source; //bdg, espace-client angular, espace client android, appli dossier android, librairie cliente, autre programme => faire une table ? base programme ?
	private String origine; //url de la page appelante si elle on peu la trouver
	private String ipDistante;
	private String etat;

	private String message;
	private String trace;
	private String entite;
	private Integer idEntite;
	private String codeEntite;
	private String ip;
	private Boolean cronDossier;
	private Boolean cronSysteme;
	private String urlApi;
	private String versionApi;
	private String parametreEnteteApi;
	private String parametreRequeteApi;
	private String corpsRequeteApi;
	private String corpsReponseApi;
	private String methodeHttpApi;
	
	private Integer versionObj;

	public TaLogDossierDTO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getQuand() {
		return quand;
	}

	public void setQuand(Date quand) {
		this.quand = quand;
	}

	public int getNiveauLog() {
		return niveauLog;
	}

	public void setNiveauLog(int niveauLog) {
		this.niveauLog = niveauLog;
	}

	public int getTypeLog() {
		return typeLog;
	}

	public void setTypeLog(int typeLog) {
		this.typeLog = typeLog;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	public Integer getIdEntite() {
		return idEntite;
	}

	public void setIdEntite(Integer idEntite) {
		this.idEntite = idEntite;
	}

	public String getCodeEntite() {
		return codeEntite;
	}

	public void setCodeEntite(String codeEntite) {
		this.codeEntite = codeEntite;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Boolean getCronDossier() {
		return cronDossier;
	}

	public void setCronDossier(Boolean cronDossier) {
		this.cronDossier = cronDossier;
	}

	public Boolean getCronSysteme() {
		return cronSysteme;
	}

	public void setCronSysteme(Boolean cronSysteme) {
		this.cronSysteme = cronSysteme;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public Integer getIdUtilisateurDossier() {
		return idUtilisateurDossier;
	}

	public void setIdUtilisateurDossier(Integer idUtilisateurDossier) {
		this.idUtilisateurDossier = idUtilisateurDossier;
	}

	public String getUsernameUtilisateurDossier() {
		return usernameUtilisateurDossier;
	}

	public void setUsernameUtilisateurDossier(String usernameUtilisateurDossier) {
		this.usernameUtilisateurDossier = usernameUtilisateurDossier;
	}

	public Integer getIdUtilisateurWebService() {
		return idUtilisateurWebService;
	}

	public void setIdUtilisateurWebService(Integer idUtilisateurWebService) {
		this.idUtilisateurWebService = idUtilisateurWebService;
	}

	public String getLoginUtilisateurDossier() {
		return loginUtilisateurDossier;
	}

	public void setLoginUtilisateurDossier(String loginUtilisateurDossier) {
		this.loginUtilisateurDossier = loginUtilisateurDossier;
	}

	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getDossier() {
		return dossier;
	}

	public void setDossier(String dossier) {
		this.dossier = dossier;
	}

	public Boolean getAppelDistant() {
		return appelDistant;
	}

	public void setAppelDistant(Boolean appelDistant) {
		this.appelDistant = appelDistant;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOrigine() {
		return origine;
	}

	public void setOrigine(String origine) {
		this.origine = origine;
	}

	public String getIpDistante() {
		return ipDistante;
	}

	public void setIpDistante(String ipDistante) {
		this.ipDistante = ipDistante;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getUrlApi() {
		return urlApi;
	}

	public void setUrlApi(String urlApi) {
		this.urlApi = urlApi;
	}

	public String getVersionApi() {
		return versionApi;
	}

	public void setVersionApi(String versionApi) {
		this.versionApi = versionApi;
	}

	public String getParametreEnteteApi() {
		return parametreEnteteApi;
	}

	public void setParametreEnteteApi(String parametreEnteteApi) {
		this.parametreEnteteApi = parametreEnteteApi;
	}

	public String getParametreRequeteApi() {
		return parametreRequeteApi;
	}

	public void setParametreRequeteApi(String parametreRequeteApi) {
		this.parametreRequeteApi = parametreRequeteApi;
	}

	public String getCorpsRequeteApi() {
		return corpsRequeteApi;
	}

	public void setCorpsRequeteApi(String corpsRequeteApi) {
		this.corpsRequeteApi = corpsRequeteApi;
	}

	public String getCorpsReponseApi() {
		return corpsReponseApi;
	}

	public void setCorpsReponseApi(String corpsReponseApi) {
		this.corpsReponseApi = corpsReponseApi;
	}

	public String getMethodeHttpApi() {
		return methodeHttpApi;
	}

	public void setMethodeHttpApi(String methodeHttpApi) {
		this.methodeHttpApi = methodeHttpApi;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
