package fr.legrain.tiers.model;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Sort;

import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_espace_client")
@NamedQueries(value = { 
		@NamedQuery(name=TaEspaceClient.QN.FIND_ALL_LIGHT, 
				query="select new fr.legrain.tiers.dto.TaEspaceClientDTO("
						+ "a.idEspaceClient, a.email, a.password, t.idTiers, t.codeTiers, a.actif,"
						+ "a.versionObj, a.nom, a.prenom, a.dateDerniereConnexion) "
						+ "from TaEspaceClient a left join a.taTiers t order by t.codeTiers")
})
public class TaEspaceClient implements java.io.Serializable {

	private static final long serialVersionUID = -2346162814985796013L;
	
	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaEspaceClient.findAllLight";
	}
	
	@Transient
	static Logger logger = Logger.getLogger(TaEspaceClient.class.getName());
	
	private int idEspaceClient;
	private TaTiers taTiers;
	private String nom;
	private String prenom;
	private String email;
	private String password;
	private Boolean actif;
	
	private String token;
	private String accessToken;
	private String refreshToken;
	
	private Date dateDerniereConnexion;
	
	private String androidRegistrationToken; //token Firebase pour l'instant
	private Date dernierAccesMobile;
	private Set<TaTerminalMobile> taTerminalMobiles;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_espace_client", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_espace_client",table = "ta_espace_client",champEntite="idEspaceClient", clazz = TaEspaceClient.class)
	public int getIdEspaceClient() {
		return this.idEspaceClient;
	}

	public void setIdEspaceClient(int idEspaceClient) {
		this.idEspaceClient = idEspaceClient;
	}
	
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_tiers")
	@LgrHibernateValidated(champBd = "id_tiers",table = "ta_tiers",champEntite="idTiers",clazz = TaTiers.class)
//	@XmlElement
//	@XmlInverseReference(mappedBy="taTelephones")
	public TaTiers getTaTiers() {
		return this.taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTelephone) {
		this.quiCree = quiCreeTelephone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTelephone) {
		this.quandCree = quandCreeTelephone;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTelephone) {
		this.quiModif = quiModifTelephone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTelephone) {
		this.quandModif = quandModifTelephone;
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

	@Column(name = "actif")
	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	@Column(name = "nom")
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Column(name = "prenom")
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	@Transient
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Transient
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Transient
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Column(name = "date_derniere_connexion")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDerniereConnexion() {
		return dateDerniereConnexion;
	}

	public void setDateDerniereConnexion(Date dateDerniereConnexion) {
		this.dateDerniereConnexion = dateDerniereConnexion;
	}

	@Column(name = "android_registration_token")
	public String getAndroidRegistrationToken() {
		return androidRegistrationToken;
	}

	public void setAndroidRegistrationToken(String androidRegistrationToken) {
		this.androidRegistrationToken = androidRegistrationToken;
	}

	@Column(name="dernier_acces_mobile")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDernierAccesMobile() {
		return dernierAccesMobile;
	}

	public void setDernierAccesMobile(Date dernierAccesMobile) {
		this.dernierAccesMobile = dernierAccesMobile;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taEspaceClient", orphanRemoval=true)
	public Set<TaTerminalMobile> getTaTerminalMobiles() {
		return this.taTerminalMobiles;
	}

	public void setTaTerminalMobiles(Set<TaTerminalMobile> taTerminalMobiles) {
		this.taTerminalMobiles = taTerminalMobiles;
	}

}
