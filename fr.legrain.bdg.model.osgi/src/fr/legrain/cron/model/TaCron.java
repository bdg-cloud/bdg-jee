package fr.legrain.cron.model;

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

/**
 * cf : https://docs.oracle.com/javaee/7/api/javax/ejb/Schedule.html
 */
@Entity
@Table(name = "ta_cron")
public class TaCron implements java.io.Serializable {

	private static final long serialVersionUID = -3331207068585903961L;
	
	public static final String CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT = "CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT";
	public static final String CRON_SYS_DECLENCHE_PAIEMENT_STRIPE = "CRON_SYS_DECLENCHE_PAIEMENT_STRIPE";

	private int idCron;
	private String schemaTenant;
	private String code;
	private String libelle;
	private String description;
	private String typeCron;
	private boolean systeme;
	private boolean visible;
	private boolean actif;
	private byte[] timerHandle;
	private boolean estUnique;
	private String identifiantUnique;
	
	private Date dateDerniereExecution;

	/**
	 * one or more seconds within a minute
	 * Allowable values: [0,59]
	 */
	private String second;

	/**
	 * one or more minutes within an hour
	 * Allowable values : [0,59]
	 */
	private String minute;

	/**
	 * one or more hours within a day
	 * Allowable values : [0,23]
	 */
	private String hour;

	/**
	 * one or more days within a month
	 * Allowable values:
	 * [1,31] // "1st","2nd", etc. applied to a day of the week identifies a single occurrence of that day within the month.
	 * [-7, -1] //-x (where x is in the range [-7, -1]) means x day(s) before the last day of the month
	 * "Last" //"Last" means the last day of the month
	 * {"1st", "2nd", "3rd", "4th", "5th", "Last"} {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"} 
	 */
	private String dayOfMonth; 

	/**
	 * one or more months within a year
	 * Allowable values :
	 * [1,12]
	 * {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", Dec"} 
	 */
	private String month; 

	/**
	 * one or more days within a week
	 * Allowable values :
	 * [0,7]
	 * {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"} 
	 * "0" and "7" both refer to Sunday
	 */
	private String dayOfWeek;
	/**
	 * a particular calendar year
	 * Allowable values : a four-digit calendar year 
	 */
	private String year; 

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cron", unique = true, nullable = false)
	public int getIdCron() {
		return idCron;
	}

	public void setIdCron(int idCron) {
		this.idCron = idCron;
	}

	@Column(name = "schema_tenant")
	public String getSchemaTenant() {
		return schemaTenant;
	}

	public void setSchemaTenant(String schemaTenant) {
		this.schemaTenant = schemaTenant;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "type_cron")
	public String getTypeCron() {
		return typeCron;
	}

	public void setTypeCron(String type) {
		this.typeCron = type;
	}

	@Column(name = "systeme")
	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		this.systeme = systeme;
	}

	@Column(name = "visible")
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Column(name = "actif")
	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	@Column(name = "timer_handle")
	public byte[] getTimerHandle() {
		return timerHandle;
	}

	public void setTimerHandle(byte[] timerHandle) {
		this.timerHandle = timerHandle;
	}

	@Column(name = "second")
	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	@Column(name = "minute")
	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	@Column(name = "hour")
	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	@Column(name = "day_of_month")
	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	@Column(name = "month")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "day_of_Week")
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Column(name = "year")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "est_unique")
	public boolean isEstUnique() {
		return estUnique;
	}

	public void setEstUnique(boolean unique) {
		this.estUnique = unique;
	}

	@Column(name = "identifiant_unique")
	public String getIdentifiantUnique() {
		return identifiantUnique;
	}

	public void setIdentifiantUnique(String identifiantUnique) {
		this.identifiantUnique = identifiantUnique;
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_derniere_execution", length = 19)
	public Date getDateDerniereExecution() {
		return dateDerniereExecution;
	}

	public void setDateDerniereExecution(Date dateDerniereExecution) {
		this.dateDerniereExecution = dateDerniereExecution;
	}

}
