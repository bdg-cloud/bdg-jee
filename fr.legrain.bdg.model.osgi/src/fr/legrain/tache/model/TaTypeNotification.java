package fr.legrain.tache.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_type_notification")
public class TaTypeNotification implements java.io.Serializable {

	private static final long serialVersionUID = -8235519339772490574L;
	
	public static final String CODE_TYPE_NOTIFICATION_EMAIL = "EMAIL";
	public static final String CODE_TYPE_NOTIFICATION_WEBAPP = "WEBAPP";
	public static final String CODE_TYPE_NOTIFICATION_SMS = "SMS";
	public static final String CODE_TYPE_NOTIFICATION_APPLI_MOBILE = "APPLI_MOBILE";
	
	private int idTypeNotification;
	private String codeTypeNotification;
	private String libelleTypeNotification;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type_notification", unique = true, nullable = false)
	public int getIdTypeNotification() {
		return idTypeNotification;
	}
	public void setIdTypeNotification(int idTypeNotification) {
		this.idTypeNotification = idTypeNotification;
	}
	
	@Column(name = "code_type_notification")
	public String getCodeTypeNotification() {
		return codeTypeNotification;
	}
	public void setCodeTypeNotification(String codeTypeNotification) {
		this.codeTypeNotification = codeTypeNotification;
	}
	
	@Column(name = "libelle_type_notification")
	public String getLibelleTypeNotification() {
		return libelleTypeNotification;
	}
	public void setLibelleTypeNotification(String libelleTypeNotification) {
		this.libelleTypeNotification = libelleTypeNotification;
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
}