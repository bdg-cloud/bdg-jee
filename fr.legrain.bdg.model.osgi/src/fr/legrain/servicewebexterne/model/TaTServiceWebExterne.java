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
@Table(name = "ta_t_service_web_externe")
@NamedQueries(value = { 
		@NamedQuery(name=TaTServiceWebExterne.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO(f.id,f.codeTServiceWebExterne,f.libelleTServiceWebExterne,f.descriptionTServiceWebExterne,f.systeme) from TaTServiceWebExterne f where f.codeTServiceWebExterne like :codeTServiceWebExterne order by f.codeTServiceWebExterne"),
		@NamedQuery(name=TaTServiceWebExterne.QN.FIND_ALL_LIGHT, query="select new fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO(f.id,f.codeTServiceWebExterne,f.libelleTServiceWebExterne,f.descriptionTServiceWebExterne,f.systeme) from TaTServiceWebExterne f order by f.codeTServiceWebExterne")	
})
public class TaTServiceWebExterne implements java.io.Serializable {

	private static final long serialVersionUID = 5168382025851704061L;
	
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaTServiceWebExterne.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaTServiceWebExterne.findAllLight";
	}
	
	public static final String TYPE_PAIEMENT = "PAIEMENT";
	public static final String TYPE_EMAIL = "EMAIL";
	public static final String TYPE_SMS = "SMS";
	
	private int idTServiceWebExterne;
	private String codeTServiceWebExterne;
	private String libelleTServiceWebExterne;
	private String descriptionTServiceWebExterne;
	private boolean systeme;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_service_web_externe", unique = true, nullable = false)
	public int getIdTServiceWebExterne() {
		return idTServiceWebExterne;
	}
	public void setIdTServiceWebExterne(int idTServiceWebExterne) {
		this.idTServiceWebExterne = idTServiceWebExterne;
	}
	
	@Column(name = "code_t_service_web_externe")
	public String getCodeTServiceWebExterne() {
		return codeTServiceWebExterne;
	}
	public void setCodeTServiceWebExterne(String codeTServiceWebExterne) {
		this.codeTServiceWebExterne = codeTServiceWebExterne;
	}
	
	@Column(name = "libelle")
	public String getLibelleTServiceWebExterne() {
		return libelleTServiceWebExterne;
	}
	public void setLibelleTServiceWebExterne(String libelleTServiceWebExterne) {
		this.libelleTServiceWebExterne = libelleTServiceWebExterne;
	}
	
	@Column(name = "description")
	public String getDescriptionTServiceWebExterne() {
		return descriptionTServiceWebExterne;
	}
	public void setDescriptionTServiceWebExterne(String descriptionTServiceWebExterne) {
		this.descriptionTServiceWebExterne = descriptionTServiceWebExterne;
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