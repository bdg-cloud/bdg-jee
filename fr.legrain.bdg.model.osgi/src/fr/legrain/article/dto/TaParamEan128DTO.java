package fr.legrain.article.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;


public class TaParamEan128DTO   extends ModelObject implements java.io.Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1929112142485684362L;
	
	private int id;
	private String code128;
	private String libelle;
	private Integer longAi;
	private Integer longBorneMax;
	private String dataType;
	private Boolean longVariable=false;
	private String typeConversion;
	private String uniteDefaut;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	
	
	public TaParamEan128DTO() {}
	


	public TaParamEan128DTO(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}
	

	public String getCode128() {
		return code128;
	}

	public void setCode128(String code128) {
		this.code128 = code128;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	public Integer getLongBorneMax() {
		return longBorneMax;
	}

	public void setLongBorneMax(Integer longBorneMax) {
		this.longBorneMax = longBorneMax;
	}

	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}

	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



	public String getDataType() {
		return dataType;
	}



	public void setDataType(String dataType) {
		this.dataType = dataType;
	}



	public Integer getLongAi() {
		return longAi;
	}



	public void setLongAi(Integer longAi) {
		this.longAi = longAi;
	}



	public Boolean getLongVariable() {
		return longVariable;
	}



	public void setLongVariable(Boolean longVariable) {
		this.longVariable = longVariable;
	}



	public String getTypeConversion() {
		return typeConversion;
	}



	public void setTypeConversion(String typeConversion) {
		this.typeConversion = typeConversion;
	}



	public String getUniteDefaut() {
		return uniteDefaut;
	}



	public void setUniteDefaut(String uniteDefaut) {
		this.uniteDefaut = uniteDefaut;
	}


	
	
}
