package fr.legrain.sms.model;

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

import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_contact_message_sms")
public class TaContactMessageSMS implements java.io.Serializable {

	private static final long serialVersionUID = -5148978288311668995L;

	private int idContactMessageSms;
	
	private String numeroTelephone;
	private TaTiers taTiers;
	private TaMessageSMS taMessageSms;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
		
	public TaContactMessageSMS() {
	}

	public TaContactMessageSMS(int idLogPaiementInternet) {
		this.idContactMessageSms = idLogPaiementInternet;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_contact_message_sms", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_contact_message_sms",table = "ta_contact_message_sms",champEntite="idContactMessageSms", clazz = TaContactMessageSMS.class)
	public int getIdContactMessageSms() {
		return this.idContactMessageSms;
	}

	public void setIdContactMessageSms(int idLogPaiementInternet) {
		this.idContactMessageSms = idLogPaiementInternet;
	}
	
	@Column(name = "numero_telephone")
	public String getNumeroTelephone() {
		return numeroTelephone;
	}

	public void setNumeroTelephone(String adresseEmail) {
		this.numeroTelephone = adresseEmail;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_tiers")
	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_sms")
	public TaMessageSMS getTaMessageSms() {
		return taMessageSms;
	}

	public void setTaMessageSms(TaMessageSMS taMessageEmail) {
		this.taMessageSms = taMessageEmail;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
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

}
