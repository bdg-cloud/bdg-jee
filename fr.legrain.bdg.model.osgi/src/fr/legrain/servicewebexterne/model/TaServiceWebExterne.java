package fr.legrain.servicewebexterne.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.droits.model.TaUtilisateur;

/*
 * Idées champs supplémentaires : 
 * type de 'service technique'/action fourni : envoi de mail, stockage cloud, ouvrir document doc, ... pour pouvoir afficher des listes de service "compatible" avec l'action a réaliser.
 * principe des Actions/Intent sur Android par exemple => principe adaptable à des logiciel installé dans le cas du client lourd ou de plugins navigateur
 */
@Entity
@Table(name = "ta_service_web_externe")
@NamedQueries(value = { 
		@NamedQuery(name=TaServiceWebExterne.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO(f.id,f.codeServiceWebExterne,f.libelleServiceWebExterne,f.descriptionServiceWebExterne,a.idTAuthentification,a.codeTAuthentification,t.idTServiceWebExterne,t.codeTServiceWebExterne,f.actif,f.systeme,f.logo) from TaServiceWebExterne f join f.taTAuthentification a join f.taTServiceWebExterne t where f.codeServiceWebExterne like :codeServiceWebExterne order by f.codeServiceWebExterne"),
		@NamedQuery(name=TaServiceWebExterne.QN.FIND_ALL_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO(f.id,f.codeServiceWebExterne,f.libelleServiceWebExterne,f.descriptionServiceWebExterne,a.idTAuthentification,a.codeTAuthentification,t.idTServiceWebExterne,t.codeTServiceWebExterne,f.actif,f.systeme,f.logo) from TaServiceWebExterne f join f.taTAuthentification a join f.taTServiceWebExterne t order by f.codeServiceWebExterne"),
		@NamedQuery(name=TaServiceWebExterne.QN.FIND_ALL_LIGHT_ACTIF, query="select new fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO(f.id,f.codeServiceWebExterne,f.libelleServiceWebExterne,f.descriptionServiceWebExterne,a.idTAuthentification,a.codeTAuthentification,t.idTServiceWebExterne,t.codeTServiceWebExterne,f.actif,f.systeme,f.logo) from TaServiceWebExterne f join f.taTAuthentification a join f.taTServiceWebExterne t where f.actif = true order by f.codeServiceWebExterne")
})
public class TaServiceWebExterne implements java.io.Serializable {

	private static final long serialVersionUID = 9159166281537173035L;
	
	public static final String CODE_SERVICE_SHIPPINGBO = "SHIPPINGBO";
	public static final String CODE_SERVICE_MENSURA = "MENSURA";
	public static final String CODE_SERVICE_WOOCOMMERCE = "WOOCOMMERCE";
	public static final String CODE_SERVICE_PRESTASHOP = "PRESTASHOP";
	
//	public static final String CODE_SERVICE_STRIPE_CONNECT = "STRIPE_CONNECT"; //=> Constante dans la classe UtilServiceWebExterne, il faudrait peut être également y déplacer celles au dessus (presta, ...) ou au moins trouver une facçon homogène pour ce genre de constante
		
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaServiceWebExterne.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaServiceWebExterne.findAllLight";
		public static final String FIND_ALL_LIGHT_ACTIF = "TaServiceWebExterne.findAllLightActif";
	}
	
	private int idServiceWebExterne;
	private String codeServiceWebExterne;
	private String libelleServiceWebExterne;
	private String descriptionServiceWebExterne;
	private TaTAuthentification taTAuthentification;
	private TaTServiceWebExterne taTServiceWebExterne;
	private String urlEditeur;
	private String urlService;
	private String urlGestionService;
	private String idModuleBdgAutorisation;
	private boolean defaut;
	private String typeAPI;
	private String versionAPI;
	private boolean actif;
	private boolean apiMulticompte;
	private boolean systeme;
	private byte[] logo;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_service_web_externe", unique = true, nullable = false)
	public int getIdServiceWebExterne() {
		return idServiceWebExterne;
	}
	public void setIdServiceWebExterne(int idServiceWebExterne) {
		this.idServiceWebExterne = idServiceWebExterne;
	}
	
	@Column(name = "code_service_web_externe")
	public String getCodeServiceWebExterne() {
		return codeServiceWebExterne;
	}
	public void setCodeServiceWebExterne(String codeServiceWebExterne) {
		this.codeServiceWebExterne = codeServiceWebExterne;
	}
	
	@Column(name = "libelle_service_web_externe")
	public String getLibelleServiceWebExterne() {
		return libelleServiceWebExterne;
	}
	public void setLibelleServiceWebExterne(String libelleServiceWebExterne) {
		this.libelleServiceWebExterne = libelleServiceWebExterne;
	}
	
	@Column(name = "description_service_web_externe")
	public String getDescriptionServiceWebExterne() {
		return descriptionServiceWebExterne;
	}
	public void setDescriptionServiceWebExterne(String descriptionServiceWebExterne) {
		this.descriptionServiceWebExterne = descriptionServiceWebExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_authentification_defaut")
	public TaTAuthentification getTaTAuthentification() {
		return taTAuthentification;
	}
	public void setTaTAuthentification(TaTAuthentification taTAuthentification) {
		this.taTAuthentification = taTAuthentification;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_service_web_externe")
	public TaTServiceWebExterne getTaTServiceWebExterne() {
		return taTServiceWebExterne;
	}
	public void setTaTServiceWebExterne(TaTServiceWebExterne taTServiceWebExterne) {
		this.taTServiceWebExterne = taTServiceWebExterne;
	}

	@Column(name = "url_editeur")
	public String getUrlEditeur() {
		return urlEditeur;
	}
	public void setUrlEditeur(String urlEditeur) {
		this.urlEditeur = urlEditeur;
	}
	
	@Column(name = "url_service")
	public String getUrlService() {
		return urlService;
	}
	public void setUrlService(String urlService) {
		this.urlService = urlService;
	}
	
	@Column(name = "type_api")
	public String getTypeAPI() {
		return typeAPI;
	}
	public void setTypeAPI(String typeAPI) {
		this.typeAPI = typeAPI;
	}
	
	@Column(name = "verson_api")
	public String getVersionAPI() {
		return versionAPI;
	}
	public void setVersionAPI(String versionAPI) {
		this.versionAPI = versionAPI;
	}
	
	@Column(name = "defaut")
	public boolean isDefaut() {
		return defaut;
	}
	public void setDefaut(boolean defaut) {
		this.defaut = defaut;
	}
	
	@Column(name = "actif")
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
	@Column(name = "api_multicompte")
	public boolean isApiMulticompte() {
		return apiMulticompte;
	}
	public void setApiMulticompte(boolean apiMulticompte) {
		this.apiMulticompte = apiMulticompte;
	}
	
	@Column(name = "systeme")
	public boolean isSysteme() {
		return systeme;
	}
	public void setSysteme(boolean systeme) {
		this.systeme = systeme;
	}
	
	@Column(name = "logo")
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
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
	
	@Column(name = "url_gestion_service_web_externe")
	public String getUrlGestionService() {
		return urlGestionService;
	}
	public void setUrlGestionService(String urlGestionService) {
		this.urlGestionService = urlGestionService;
	}
	
	@Column(name = "id_module_bdg_autorisation")
	public String getIdModuleBdgAutorisation() {
		return idModuleBdgAutorisation;
	}
	public void setIdModuleBdgAutorisation(String idModuleBdgAutorisation) {
		this.idModuleBdgAutorisation = idModuleBdgAutorisation;
	}
	
}