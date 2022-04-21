package fr.legrain.edition.model;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Version;

import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaRGroupeModeleConformite;
import fr.legrain.droits.model.TaUtilisateur;

@Entity
@Table(name = "ta_edition_catalogue")
@NamedQueries(value = { 
		@NamedQuery(name=TaEditionCatalogue.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.edition.dto.TaEditionCatalogueDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEditionCatalogue f left join f.taTEdition t where f.codeEdition like :codeEdition order by f.codeEdition"),
		@NamedQuery(name=TaEditionCatalogue.QN.FIND_ALL_LIGHT, query="select new fr.legrain.edition.dto.TaEditionCatalogueDTO(f.id,f.codeEdition,f.libelleEdition,f.descriptionEdition,f.miniature,f.defaut,f.actif,f.systeme,f.versionEdition,t.idTEdition,t.codeTEdition) from TaEditionCatalogue f left join f.taTEdition t order by f.codeEdition")	
})
public class TaEditionCatalogue implements java.io.Serializable {

	private static final long serialVersionUID = -8770247461622859075L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaEditionCatalogue.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaEditionCatalogue.findAllLight";
	}
	
	private int idEdition;

	private String codeEdition;
	private String libelleEdition;
	private String descriptionEdition;
	private TaTEditionCatalogue taTEdition;
	private byte[] miniature;
	private byte[] fichierBlob;
	private byte[] fichierExemple;
	private String codeDossierEditionPersonalisee;
	private String fichierChemin;
	private String fichierNom;
	private Boolean defaut = false;
	private Boolean actif = false;
	private Boolean systeme = false;
	private Boolean payant = false; 
	private Boolean importationManuelle = false;
	private Date dateImportation;
	private String versionEdition;
	private Set<TaActionInterneProg> actionInterne;
	
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
	
	public TaActionInterneProg chercheTaActionInterne(TaActionInterneProg act) {
		TaActionInterneProg retour = null;
		if(actionInterne==null) {
			actionInterne = new HashSet<TaActionInterneProg>();
		}
		for (TaActionInterneProg r : actionInterne) {
			if(r.getId()==act.getId()) {
				retour = r;
			}
		}
		return retour;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_edition_catalogue", unique = true, nullable = false)
	public int getIdEdition() {
		return idEdition;
	}
	
	public void setIdEdition(int idEdition) {
		this.idEdition = idEdition;
	}
	
	@Column(name = "code_edition_catalogue")
	public String getCodeEdition() {
		return codeEdition;
	}
	
	public void setCodeEdition(String code_edition) {
		this.codeEdition = code_edition;
	}
	
	@Column(name = "libelle_edition_catalogue")
	public String getLibelleEdition() {
		return libelleEdition;
	}
	
	public void setLibelleEdition(String libelle_edition) {
		this.libelleEdition = libelle_edition;
	}
	
	@Column(name = "description_edition_catalogue")
	public String getDescriptionEdition() {
		return descriptionEdition;
	}
	
	public void setDescriptionEdition(String description_edition) {
		this.descriptionEdition = description_edition;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_edition_catalogue")
	public TaTEditionCatalogue getTaTEdition() {
		return taTEdition;
	}
	
	public void setTaTEdition(TaTEditionCatalogue taTEditionCatalogue) {
		this.taTEdition = taTEditionCatalogue;
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
	
	@Column(name = "version_edition_catalogue")
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

	@Column(name = "fichier_exemple")
	public byte[] getFichierExemple() {
		return fichierExemple;
	}

	public void setFichierExemple(byte[] fichierExemple) {
		this.fichierExemple = fichierExemple;
	}

	@Column(name = "code_dossier_edition_personalisee")
	public String getCodeDossierEditionPersonalisee() {
		return codeDossierEditionPersonalisee;
	}

	public void setCodeDossierEditionPersonalisee(String codeDossierEditionPersonalisee) {
		this.codeDossierEditionPersonalisee = codeDossierEditionPersonalisee;
	}

	@Column(name = "payant")
	public Boolean getPayant() {
		return payant;
	}

	public void setPayant(Boolean payant) {
		this.payant = payant;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_action_edition",
			joinColumns = {@JoinColumn(name = "id_edition")},inverseJoinColumns = {@JoinColumn(name = "id_action")})
	public Set<TaActionInterneProg> getActionInterne() {
		return actionInterne;
	}

	public void setActionInterne(Set<TaActionInterneProg> actionInterne) {
		this.actionInterne = actionInterne;
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