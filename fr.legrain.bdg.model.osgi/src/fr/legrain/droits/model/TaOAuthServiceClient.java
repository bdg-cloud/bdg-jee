package fr.legrain.droits.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_oauth_service_client")
@NamedQueries(value = { 
		@NamedQuery(name=TaOAuthServiceClient.QN.FIND_BY_USERNAME, query="select f from TaOAuthServiceClient f where f.code= :code")
		})
public class TaOAuthServiceClient implements Serializable {

	private static final long serialVersionUID = -6127005803934710400L;

	public static class QN {
		public static final String FIND_BY_USERNAME = "TaOAuthServiceClient.findByCode";
	}
	
	public static final String SERVICE_GOOGLE    = "GOOGLE";
	public static final String SERVICE_MICROSOFT = "MS";
	public static final String SERVICE_STRIPE 	 = "STRIPE";
	
	private Integer id;
	private String code;
	private String libelle;
	private String description;
	private String url;
	private String urlEndPoint;
	private Boolean systeme;
	private Boolean actif;
	
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

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="systeme")
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}

	@Column(name="actif")
	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
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

	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name="urlendpoint")
	public String getUrlEndPoint() {
		return urlEndPoint;
	}

	public void setUrlEndPoint(String urlEndPoint) {
		this.urlEndPoint = urlEndPoint;
	}

	@Column(name="libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	

}
