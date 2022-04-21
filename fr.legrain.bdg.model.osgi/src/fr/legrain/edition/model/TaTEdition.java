package fr.legrain.edition.model;

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
@Table(name = "ta_t_edition")
@NamedQueries(value = { 
		@NamedQuery(name=TaTEdition.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.edition.dto.TaTEditionDTO(f.id,f.codeTEdition,f.libelle,f.description,f.systeme) from TaTEdition f where f.codeTEdition like :code order by f.codeTEdition"),
		@NamedQuery(name=TaTEdition.QN.FIND_ALL_LIGHT, query="select new fr.legrain.edition.dto.TaTEditionDTO(f.id,f.codeTEdition,f.libelle,f.description,f.systeme) from TaTEdition f order by f.codeTEdition")	
})
public class TaTEdition implements java.io.Serializable {

	private static final long serialVersionUID = 2048125031384449396L;
	
	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaTEdition.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaTEdition.findAllLight";
	}
	
//	public static final String TYPE_PAIEMENT = "PAIEMENT";
//	public static final String TYPE_EMAIL = "EMAIL";
	
	private int idTEdition;
	private String codeTEdition;
	private String libelle;
	private String description;
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
	@Column(name = "id_t_edition", unique = true, nullable = false)
	public int getIdTEdition() {
		return idTEdition;
	}
	public void setIdTEdition(int idTServiceWebExterne) {
		this.idTEdition = idTServiceWebExterne;
	}
	
	@Column(name = "code_t_edition")
	public String getCodeTEdition() {
		return codeTEdition;
	}
	public void setCodeTEdition(String codeTServiceWebExterne) {
		this.codeTEdition = codeTServiceWebExterne;
	}
	
	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelleTServiceWebExterne) {
		this.libelle = libelleTServiceWebExterne;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String descriptionTServiceWebExterne) {
		this.description = descriptionTServiceWebExterne;
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