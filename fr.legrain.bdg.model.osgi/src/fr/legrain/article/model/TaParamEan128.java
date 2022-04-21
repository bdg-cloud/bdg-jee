package fr.legrain.article.model;

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

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_param_ean128")
public class TaParamEan128 extends TaObjetLgr     implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9121403450461361294L;
	
	private int idParamEan128;
	private String code128;
	private String libelle;
	private Integer longAi;
	private Integer longBorneMax;
	private String dataType;
	private Boolean longVariable=false;
	private String typeConversion;
	private String uniteDefaut;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
	
	
	
	public TaParamEan128() {}
	


	public TaParamEan128(int idParamEan128) {
		this.idParamEan128 = idParamEan128;
	}





	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_param_ean128", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_param_ean128",table = "ta_param_cree_doc_tiers",champEntite="idParamEan128", clazz = TaParamEan128.class)
	public int getIdParamEan128() {
		return idParamEan128;
	}



	public void setIdParamEan128(int idParamEan128) {
		this.idParamEan128 = idParamEan128;
	}
	
	@Column(name = "code_128")
	public String getCode128() {
		return code128;
	}

	public void setCode128(String code128) {
		this.code128 = code128;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "long_ai")
	public Integer getLongAi() {
		return longAi;
	}



	public void setLongAi(Integer longAi) {
		this.longAi = longAi;
	}


	@Column(name = "long_variable")
	public Boolean getLongVariable() {
		return longVariable;
	}



	public void setLongVariable(Boolean longVariable) {
		this.longVariable = longVariable;
	}	

	@Column(name = "long_borne_max")
	public Integer getLongBorneMax() {
		return longBorneMax;
	}

	public void setLongBorneMax(Integer longBorneMax) {
		this.longBorneMax = longBorneMax;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCree) {
//		this.quiCree = quiCree;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCree) {
//		this.quandCree = quandCree;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModif) {
//		this.quiModif = quiModif;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModif) {
//		this.quandModif = quandModif;
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


	@Column(name = "data_type")
	public String getDataType() {
		return dataType;
	}



	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	@Column(name = "type_conversion")
	public String getTypeConversion() {
		return typeConversion;
	}



	public void setTypeConversion(String typeConversion) {
		this.typeConversion = typeConversion;
	}


	@Column(name = "unite_defaut")
	public String getUniteDefaut() {
		return uniteDefaut;
	}



	public void setUniteDefaut(String uniteDefaut) {
		this.uniteDefaut = uniteDefaut;
	}



	
}
