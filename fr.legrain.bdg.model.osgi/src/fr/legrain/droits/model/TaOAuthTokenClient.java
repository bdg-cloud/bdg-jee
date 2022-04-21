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
@Table(name = "ta_oauth_token_client")
@NamedQueries(value = { 
		@NamedQuery(name=TaOAuthTokenClient.QN.FIND_BY_USERNAME, query="select f from TaOAuthTokenClient f where f.key= :code")
		})
public class TaOAuthTokenClient implements Serializable {

	private static final long serialVersionUID = -700708140680839726L;

	public static class QN {
		public static final String FIND_BY_USERNAME = "TaOAuthTokenClient.findByCode";
	}

	private Integer id;
	private String key;
	private String accessToken;
	private String refreshToken;
	private String scope;
	private long expiresIn;
//	private long expiresAt;
//	private long expiresatDate;
	private String tokenType;
	private Date dateCreation;
	private Date dateMiseAJour;
	
	private String idUtilisateurSurService;
	private String emailUtilisateurSurService;
	
	private TaUtilisateur taUtilisateur;
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

	@Column(name="access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name="refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Column(name="scope")
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Column(name="expires_in")
	public long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	@Column(name="token_type")
	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@Column(name="date_creation")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Column(name="id_utilisateur_sur_service")
	public String getIdUtilisateurSurService() {
		return idUtilisateurSurService;
	}

	public void setIdUtilisateurSurService(String idUtilisateurSurService) {
		this.idUtilisateurSurService = idUtilisateurSurService;
	}

	@Column(name="email_utilisateur_sur_service")
	public String getEmailUtilisateurSurService() {
		return emailUtilisateurSurService;
	}

	public void setEmailUtilisateurSurService(String emailUtilisateurSurService) {
		this.emailUtilisateurSurService = emailUtilisateurSurService;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
		this.taUtilisateur = taUtilisateur;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_ta_oauth_service_client")
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

	@Column(name = "key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name="date_mise_a_jour")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateMiseAJour() {
		return dateMiseAJour;
	}

	public void setDateMiseAJour(Date dateMiseAJour) {
		this.dateMiseAJour = dateMiseAJour;
	}

}
