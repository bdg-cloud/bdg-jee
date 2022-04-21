package fr.legrain.tiers.model;

import java.util.Date;
import java.util.Set;

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

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_terminal_mobile")
@NamedQueries(value = { 
		@NamedQuery(name=TaTerminalMobile.QN.FIND_ALL_LIGHT, 
				query="select new fr.legrain.tiers.dto.TaTerminalMobileDTO("
						+ "a.idTerminalMobile,"
						+ "a.versionObj, a.dateDerniereConnexion, a.androidRegistrationToken) "
						+ "from TaTerminalMobile a")
})
public class TaTerminalMobile implements java.io.Serializable {

	private static final long serialVersionUID = -6562986210429585616L;

	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaTerminalMobile.findAllLight";
	}
	
	@Transient
	static Logger logger = Logger.getLogger(TaTerminalMobile.class.getName());
	
	private int idTerminalMobile;
	
	private Date dateDerniereConnexion;
	private String androidRegistrationToken; //token Firebase pour l'instant
	
	private TaEspaceClient taEspaceClient;
	private TaUtilisateur taUtilisateur;
	
//	private String version;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_terminal_mobile", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_terminal_mobile",table = "ta_terminal_mobile",champEntite="idTerminalMobile", clazz = TaTerminalMobile.class)
	public int getIdTerminalMobile() {
		return this.idTerminalMobile;
	}

	public void setIdTerminalMobile(int idTerminalMobile) {
		this.idTerminalMobile = idTerminalMobile;
	}
	
	
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
//
//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeTelephone) {
//		this.quiCree = quiCreeTelephone;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeTelephone) {
//		this.quandCree = quandCreeTelephone;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifTelephone) {
//		this.quiModif = quiModifTelephone;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifTelephone) {
//		this.quandModif = quandModifTelephone;
//	}
//
//	@Column(name = "ip_acces", length = 50)
//	public String getIpAcces() {
//		return this.ipAcces;
//	}
//
//	public void setIpAcces(String ipAcces) {
//		this.ipAcces = ipAcces;
//	}
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
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


	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_espace_client")
	public TaEspaceClient getTaEspaceClient() {
		return taEspaceClient;
	}

	public void setTaEspaceClient(TaEspaceClient taEspaceClient) {
		this.taEspaceClient = taEspaceClient;
	}

//	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//	@JoinColumn(name = "id_utilisateur")
//	public TaUtilisateur getTaUtilisateur() {
//		return taUtilisateur;
//	}
//
//	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
//		this.taUtilisateur = taUtilisateur;
//	}
}
