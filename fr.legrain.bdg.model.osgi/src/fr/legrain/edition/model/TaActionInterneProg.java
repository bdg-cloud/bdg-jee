package fr.legrain.edition.model;

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
@Table(name = "ta_action_interne")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaActionInterne.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.edition.dto.TaEditionCatalogueDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEditionCatalogue f left join f.taTEdition t where f.codeEdition like ? order by f.codeEdition"),
//		@NamedQuery(name=TaActionInterne.QN.FIND_ALL_LIGHT, query="select new fr.legrain.edition.dto.TaEditionCatalogueDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEditionCatalogue f left join f.taTEdition t order by f.codeEdition")	
//})
public class TaActionInterneProg implements java.io.Serializable {

	private static final long serialVersionUID = -8770247461622859075L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaActionInterne.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaActionInterne.findAllLight";
	}
	
	private int id;
	private String idAction;
	private String libelle;
	private String description;
	private Boolean systeme = true;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int idEdition) {
		this.id = idEdition;
	}
	
	@Column(name = "id_action")
	public String getIdAction() {
		return idAction;
	}
	
	public void setidAction(String idAction) {
		this.idAction = idAction;
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
	
	@Column(name = "systeme")
	public Boolean getSysteme() {
		return systeme;
	}
	
	public void setSysteme(Boolean systeme) {
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