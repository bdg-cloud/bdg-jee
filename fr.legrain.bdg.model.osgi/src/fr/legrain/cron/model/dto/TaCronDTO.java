package fr.legrain.cron.model.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;


public class TaCronDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = -5772067997820846258L;
	
	private Integer id;
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
	
	private String second;
	private String minute;
	private String hour;
	private String dayOfMonth; 
	private String month; 
	private String dayOfWeek;
	private String year; 
	
	private Date dateDerniereExecution;
	private Integer versionObj;

	public TaCronDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSchemaTenant() {
		return schemaTenant;
	}

	public void setSchemaTenant(String schemaTenant) {
		this.schemaTenant = schemaTenant;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTypeCron() {
		return typeCron;
	}

	public void setTypeCron(String type) {
		this.typeCron = type;
	}

	public boolean isSysteme() {
		return systeme;
	}

	public void setSysteme(boolean systeme) {
		this.systeme = systeme;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public byte[] getTimerHandle() {
		return timerHandle;
	}

	public void setTimerHandle(byte[] timerHandle) {
		this.timerHandle = timerHandle;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public boolean isEstUnique() {
		return estUnique;
	}

	public void setEstUnique(boolean estUnique) {
		this.estUnique = estUnique;
	}

	public String getIdentifiantUnique() {
		return identifiantUnique;
	}

	public void setIdentifiantUnique(String identifiantUnique) {
		this.identifiantUnique = identifiantUnique;
	}

	public Date getDateDerniereExecution() {
		return dateDerniereExecution;
	}

	public void setDateDerniereExecution(Date dateDerniereExecution) {
		this.dateDerniereExecution = dateDerniereExecution;
	}

	

}
