package fr.legrain.servicewebexterne.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_t_authentification")
@NamedQueries(value = { 
		@NamedQuery(name=TaTAuthentification.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO(f.id,f.codeTAuthentification,f.libelleTAuthentification,f.descriptionTAuthentification,f.systeme) from TaTAuthentification f where f.codeTAuthentification like :codeTAuthentification order by f.codeTAuthentification"),
		@NamedQuery(name=TaTAuthentification.QN.FIND_ALL_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO(f.id,f.codeTAuthentification,f.libelleTAuthentification,f.descriptionTAuthentification,f.systeme) from TaTAuthentification f order by f.codeTAuthentification")	
})
public class TaTAuthentification implements java.io.Serializable {

	private static final long serialVersionUID = -3156895957123164821L;
	
	public static final String KEY = "KEY";
	public static final String LOGIN = "LOGIN";
	public static final String OAUTH = "OAuth";
	
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaTAuthentification.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaTAuthentification.findAllLight";
	}
	
	private int idTAuthentification;
	private String codeTAuthentification;
	private String libelleTAuthentification;
	private String descriptionTAuthentification;
	private boolean systeme;
	
	/*
	 * Idees autre champs :
	 * SSL/TLS
	 * version protocole auth : oauth 2 
	 * 
	 * 
	 * protocole communication : HTTP
	 * type de binding : XML,JSON,...
	 */
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_authentification", unique = true, nullable = false)
	public int getIdTAuthentification() {
		return idTAuthentification;
	}
	public void setIdTAuthentification(int idTAuthentification) {
		this.idTAuthentification = idTAuthentification;
	}
	
	@Column(name = "code_t_authentification")
	public String getCodeTAuthentification() {
		return codeTAuthentification;
	}
	public void setCodeTAuthentification(String codeTAuthentification) {
		this.codeTAuthentification = codeTAuthentification;
	}
	
	@Column(name = "libelle")
	public String getLibelleTAuthentification() {
		return libelleTAuthentification;
	}
	public void setLibelleTAuthentification(String libelleTAuthentification) {
		this.libelleTAuthentification = libelleTAuthentification;
	}
	
	@Column(name = "description")
	public String getDescriptionTAuthentification() {
		return descriptionTAuthentification;
	}
	public void setDescriptionTAuthentification(String descriptionTAuthentification) {
		this.descriptionTAuthentification = descriptionTAuthentification;
	}
	
	@Column(name = "systeme")
	public boolean isSysteme() {
		return systeme;
	}
	public void setSysteme(boolean systeme) {
		this.systeme = systeme;
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