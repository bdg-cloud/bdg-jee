package fr.legrain.droits.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_utilisateur_webservice")
@NamedQueries(value = { 
		@NamedQuery(name=TaUtilisateurWebService.QN.FIND_BY_LOGIN, query="select f from TaUtilisateurWebService f where f.login= :code")
		})
public class TaUtilisateurWebService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static class QN {
		public static final String FIND_BY_LOGIN = "User.findByLogin";
	}

	private Integer id;
	private String login;
	private String passwd;
	private String description;
	private String email;
	private String autorisations;
	private Date dernierAcces;
	private String accessToken;
	private String refreshToken;
	private Boolean actif;
	private Boolean systeme;//utilisateur specifique au dossier mais pour un usage specifique, per exemple communication de l'espace client d'un dossier avec son dossier
	
	private Date quandCree;
	private Date quandModif;
	private Integer quiCree;
	private Integer quiModif;
	private String ipAcces;
	private Integer versionObj;
	
	public String passwordHashSHA256_Base64(String originalPassword) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			
			Base64.Encoder enc = Base64.getEncoder()/*.withoutPadding()*/;
			
//			BASE64Encoder enc = new sun.misc.BASE64Encoder();

			byte[] digest = md.digest(originalPassword.getBytes("UTF-8")); // Missing charset
			String base64 = enc.encodeToString(digest);

			return base64;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Column(name = "login")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "passwd")
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "autorisations")
	public String getAutorisations() {
		return autorisations;
	}

	public void setAutorisations(String autorisations) {
		this.autorisations = autorisations;
	}

	@Column(name = "dernier_acces")
	public Date getDernierAcces() {
		return dernierAcces;
	}

	public void setDernierAcces(Date dernierAcces) {
		this.dernierAcces = dernierAcces;
	}

	@Column(name = "access_token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Column(name = "refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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

}
