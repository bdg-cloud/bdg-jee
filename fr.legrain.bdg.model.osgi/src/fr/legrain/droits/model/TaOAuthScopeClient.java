package fr.legrain.droits.model;

import java.io.Serializable;
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

@Entity
@Table(name = "ta_oauth_scope_client")
@NamedQueries(value = { 
		@NamedQuery(name=TaOAuthScopeClient.QN.FIND_BY_USERNAME, query="select f from TaOAuthScopeClient f where f.code= :code")
		})
public class TaOAuthScopeClient implements Serializable {

	private static final long serialVersionUID = 5597213068180970922L;

	public static class QN {
		public static final String FIND_BY_USERNAME = "TaOAuthScopeClient.findByCode";
	}
	
	public static final String GOOGLE_PROFIL = "GOOGLE_PROFIL";
	public static final String GOOGLE_CALENDAR = "GOOGLE_CALENDAR";
	public static final String GOOGLE_DRIVE = "GOOGLE_DRIVE";
	public static final String MS_PROFIL = "MS_PROFIL";
	public static final String MS_CALENDAR = "MS_CALENDAR";
	public static final String STRIPE_READ_ONLY = "STRIPE_READ_ONLY";
	public static final String STRIPE_READ_WRITE = "STRIPE_READ_WRITE";

	private Integer id;
	  
	private String code;
	private String libelle;
	private String description;
	private String identifiantService;
	private Boolean actif;
	private Boolean systeme;

	private TaOAuthServiceClient taOAuthServiceClient;
	
	private Date quandCree;
	private Date quandModif;
	private Integer quiCree;
	private Integer quiModif;
	private String ipAcces;
	private Integer versionObj;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_oauth_service_client")
	public TaOAuthServiceClient getTaOAuthServiceClient() {
		return taOAuthServiceClient;
	}

	public void setTaOAuthServiceClient(TaOAuthServiceClient taOAuthServiceClient) {
		this.taOAuthServiceClient = taOAuthServiceClient;
	}

	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getQuandCree() {
		return quandCree;
	}
	
	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	@Column(name="qui_cree")
	public Integer getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(Integer quiCree) {
		this.quiCree = quiCree;
	}

	@Column(name="qui_modif")
	public Integer getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(Integer quiModif) {
		this.quiModif = quiModif;
	}

	@Column(name = "ip_acces")
	public String getIpAcces() {
		return ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="identifiant_service")
	public String getIdentifiantService() {
		return identifiantService;
	}

	public void setIdentifiantService(String identifiantService) {
		this.identifiantService = identifiantService;
	}

	@Column(name="actif")
	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	@Column(name="systeme")
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

}
