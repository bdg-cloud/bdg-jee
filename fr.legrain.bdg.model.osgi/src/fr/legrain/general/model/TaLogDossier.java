package fr.legrain.general.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaRGroupeModeleConformite;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateurWebService;

@Entity
@Table(name = "ta_log_dossier")
public class TaLogDossier implements java.io.Serializable {

	private static final long serialVersionUID = 1469847214792590548L;

	private int id;

	private Date quand;
	private TaUtilisateur taUtilisateur;
	private TaUtilisateurWebService taUtilisateurWebService;
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

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id= id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand")
	public Date getQuand() {
		return quand;
	}

	public void setQuand(Date quand) {
		this.quand = quand;
	}

	@ManyToOne(fetch = FetchType.EAGER/*, cascade = CascadeType.ALL*/)
	@JoinColumn(name = "id_utilisateur_dossier")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
		this.taUtilisateur = taUtilisateur;
	}

	@ManyToOne(fetch = FetchType.EAGER/*, cascade = CascadeType.ALL*/)
	@JoinColumn(name = "id_utilisateur_webservice")
	public TaUtilisateurWebService getTaUtilisateurWebService() {
		return taUtilisateurWebService;
	}

	public void setTaUtilisateurWebService(TaUtilisateurWebService taUtilisateurWebService) {
		this.taUtilisateurWebService = taUtilisateurWebService;
	}

	@Column(name = "niveau_log")
	public int getNiveauLog() {
		return niveauLog;
	}

	public void setNiveauLog(int niveauLog) {
		this.niveauLog = niveauLog;
	}

	@Column(name = "type_log")
	public int getTypeLog() {
		return typeLog;
	}

	public void setTypeLog(int typeLog) {
		this.typeLog = typeLog;
	}

	@Column(name = "message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "trace")
	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	@Column(name = "entite")
	public String getEntite() {
		return entite;
	}

	public void setEntite(String entite) {
		this.entite = entite;
	}

	@Column(name = "id_entite")
	public Integer getIdEntite() {
		return idEntite;
	}

	public void setIdEntite(Integer idEntite) {
		this.idEntite = idEntite;
	}

	@Column(name = "code_entite")
	public String getCodeEntite() {
		return codeEntite;
	}

	public void setCodeEntite(String codeEntite) {
		this.codeEntite = codeEntite;
	}

	@Column(name = "ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "cron_dossier")
	public Boolean getCronDossier() {
		return cronDossier;
	}

	public void setCronDossier(Boolean cronDossier) {
		this.cronDossier = cronDossier;
	}

	@Column(name = "cron_systeme")
	public Boolean getCronSysteme() {
		return cronSysteme;
	}

	public void setCronSysteme(Boolean cronSysteme) {
		this.cronSysteme = cronSysteme;
	}
	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
	}
	
	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "url_api")
	public String getUrlApi() {
		return urlApi;
	}

	public void setUrlApi(String urlApi) {
		this.urlApi = urlApi;
	}

	@Column(name = "utilisateur")
	public String getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(String utilisateur) {
		this.utilisateur = utilisateur;
	}

	@Column(name = "dossier")
	public String getDossier() {
		return dossier;
	}

	public void setDossier(String dossier) {
		this.dossier = dossier;
	}
	
	@Column(name = "appel_distant")
	public Boolean getAppelDistant() {
		return appelDistant;
	}

	public void setAppelDistant(Boolean appelDistant) {
		this.appelDistant = appelDistant;
	}

	@Column(name = "source")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "origine")
	public String getOrigine() {
		return origine;
	}

	public void setOrigine(String origine) {
		this.origine = origine;
	}

	@Column(name = "ip_distante")
	public String getIpDistante() {
		return ipDistante;
	}

	public void setIpDistante(String ipDistante) {
		this.ipDistante = ipDistante;
	}

	@Column(name = "version_api")
	public String getVersionApi() {
		return versionApi;
	}

	public void setVersionApi(String versionApi) {
		this.versionApi = versionApi;
	}

	@Column(name = "parametre_entete_api")
	public String getParametreEnteteApi() {
		return parametreEnteteApi;
	}

	public void setParametreEnteteApi(String parametreEnteteApi) {
		this.parametreEnteteApi = parametreEnteteApi;
	}

	@Column(name = "parametre_requete_api")
	public String getParametreRequeteApi() {
		return parametreRequeteApi;
	}

	public void setParametreRequeteApi(String parametreRequetepi) {
		this.parametreRequeteApi = parametreRequetepi;
	}

	@Column(name = "corps_requete_api")
	public String getCorpsRequeteApi() {
		return corpsRequeteApi;
	}

	public void setCorpsRequeteApi(String corpsRequeteApi) {
		this.corpsRequeteApi = corpsRequeteApi;
	}

	@Column(name = "corps_reponse_api")
	public String getCorpsReponseApi() {
		return corpsReponseApi;
	}

	public void setCorpsReponseApi(String corpsReponseApi) {
		this.corpsReponseApi = corpsReponseApi;
	}

	@Column(name = "methode_http_api")
	public String getMethodeHttpApi() {
		return methodeHttpApi;
	}

	public void setMethodeHttpApi(String methodeHttpApi) {
		this.methodeHttpApi = methodeHttpApi;
	}

	@Column(name = "etat")
	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}