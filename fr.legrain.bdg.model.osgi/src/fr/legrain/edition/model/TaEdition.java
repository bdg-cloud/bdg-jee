package fr.legrain.edition.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name = "ta_edition")
@NamedQueries(value = { 
		@NamedQuery(name=TaEdition.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.edition.dto.TaEditionDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEdition f left join f.taTEdition t where f.codeEdition like :codeEdition order by f.codeEdition"),
		@NamedQuery(name=TaEdition.QN.FIND_ALL_LIGHT, query="select new fr.legrain.edition.dto.TaEditionDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEdition f left join f.taTEdition t order by f.codeEdition"),
		@NamedQuery(name=TaEdition.QN.FIND_BY_ID_TYPE_DTO, query="select new fr.legrain.edition.dto.TaEditionDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEdition f left join f.taTEdition t where t.idTEdition = :idTEdition order by f.codeEdition"),
		@NamedQuery(name=TaEdition.QN.FIND_BY_CODE_TYPE_DTO, query="select new fr.legrain.edition.dto.TaEditionDTO("
				+ "f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition)"
				+ " from TaEdition f"
				+ " left join f.taTEdition t"
				+ " where t.codeTEdition like :codeTEdition order by f.codeEdition")	
})
public class TaEdition implements java.io.Serializable {

	private static final long serialVersionUID = -8770247461622859075L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaEdition.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaEdition.findAllLight";
		public static final String FIND_BY_ID_TYPE_DTO = "TaEdition.findByIdTypeDTO";
		public static final String FIND_BY_CODE_TYPE_DTO = "TaEdition.findByCodeTypeDTO";
	}
	
	private int idEdition;

	private String codeEdition;
	private String libelleEdition;
	private String descriptionEdition;
	private TaTEdition taTEdition;
	private byte[] miniature;
	private byte[] fichierBlob;
	private String fichierChemin;
	private String fichierNom;
	private Boolean defaut = false;
	private Boolean actif = false;
	private Boolean systeme = false;
	private Boolean importationManuelle = false;
	private Date dateImportation;
	private String versionEdition;
	private Set<TaActionInterne> actionInterne;
	
	private Set<TaActionEdition> actionEdition = new HashSet<TaActionEdition>();
	
	private Map<String, Object> mapParametreEdition;
	private String resourcesPath;
	private String theme;
	private String librairie;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_edition", unique = true, nullable = false)
	public int getIdEdition() {
		return idEdition;
	}
	
	public void setIdEdition(int idEdition) {
		this.idEdition = idEdition;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_edition_action_edition",
			joinColumns = {@JoinColumn(name = "id_edition")},inverseJoinColumns = {@JoinColumn(name = "id_action_edition")})
	public Set<TaActionEdition> getActionEdition() {
		return actionEdition;
	}
	
	public void setActionEdition(Set<TaActionEdition> actionEdition) {
		this.actionEdition = actionEdition;
	}
	
	
	@Column(name = "code_edition")
	public String getCodeEdition() {
		return codeEdition;
	}
	
	public void setCodeEdition(String code_edition) {
		this.codeEdition = code_edition;
	}
	
	@Column(name = "libelle_edition")
	public String getLibelleEdition() {
		return libelleEdition;
	}
	
	public void setLibelleEdition(String libelle_edition) {
		this.libelleEdition = libelle_edition;
	}
	
	@Column(name = "description_edition")
	public String getDescriptionEdition() {
		return descriptionEdition;
	}
	
	public void setDescriptionEdition(String description_edition) {
		this.descriptionEdition = description_edition;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_edition")
	public TaTEdition getTaTEdition() {
		return taTEdition;
	}
	
	public void setTaTEdition(TaTEdition taTEdition) {
		this.taTEdition = taTEdition;
	}
	
	@Column(name = "miniature")
	public byte[] getMiniature() {
		return miniature;
	}
	
	public void setMiniature(byte[] miniature) {
		this.miniature = miniature;
	}
	
	@Column(name = "fichier_blob")
	public byte[] getFichierBlob() {
		return fichierBlob;
	}
	
	public void setFichierBlob(byte[] fichierBlob) {
		this.fichierBlob = fichierBlob;
	}
	
	@Column(name = "fichier_chemin")
	public String getFichierChemin() {
		return fichierChemin;
	}
	
	public void setFichierChemin(String fichierChemin) {
		this.fichierChemin = fichierChemin;
	}
	
	@Column(name = "defaut")
	public Boolean getDefaut() {
		return defaut;
	}
	
	public void setDefaut(Boolean defaut) {
		this.defaut = defaut;
	}
	
	@Column(name = "actif")
	public Boolean getActif() {
		return actif;
	}
	
	public void setActif(Boolean actif) {
		this.actif = actif;
	}
	
	@Column(name = "systeme")
	public Boolean getSysteme() {
		return systeme;
	}
	
	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
	}
	
	@Column(name = "importation_manuelle")
	public Boolean getImportationManuelle() {
		return importationManuelle;
	}
	
	public void setImportationManuelle(Boolean importationManuelle) {
		this.importationManuelle = importationManuelle;
	}
	
	@Column(name = "version_edition")
	public String getVersionEdition() {
		return versionEdition;
	}

	public void setVersionEdition(String versionEdition) {
		this.versionEdition = versionEdition;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_importation")
	public Date getDateImportation() {
		return dateImportation;
	}
	
	public void setDateImportation(Date dateImportation) {
		this.dateImportation = dateImportation;
	}
	
	@Column(name = "fichier_nom")
	public String getFichierNom() {
		return fichierNom;
	}

	public void setFichierNom(String fichierNom) {
		this.fichierNom = fichierNom;
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

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinTable(name = "ta_r_action_edition",
			joinColumns = {@JoinColumn(name = "id_edition")},inverseJoinColumns = {@JoinColumn(name = "id_action")})
	public Set<TaActionInterne> getActionInterne() {
		return actionInterne;
	}

	public void setActionInterne(Set<TaActionInterne> actionInterne) {
		this.actionInterne = actionInterne;
	}

	@Transient
	public Map<String, Object> getMapParametreEdition() {
		return mapParametreEdition;
	}

	public void setMapParametreEdition(Map<String, Object> mapParametreEdition) {
		this.mapParametreEdition = mapParametreEdition;
	}

	@Column(name = "resources_path")
	public String getResourcesPath() {
		return resourcesPath;
	}

	public void setResourcesPath(String resourcesPath) {
		this.resourcesPath = resourcesPath;
	}

	@Column(name = "theme")
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Column(name = "librairie")
	public String getLibrairie() {
		return librairie;
	}

	public void setLibrairie(String librairie) {
		this.librairie = librairie;
	}

	

}