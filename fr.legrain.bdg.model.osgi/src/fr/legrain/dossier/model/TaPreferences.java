package fr.legrain.dossier.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_preferences")
@NamedQueries(value = { 
	@NamedQuery(name=TaPreferences.QN.FIND_BY_CATEGORIE, query="select a from TaPreferences a left join a.taGroupePreference grp  "
			+ " where a.visible=true and a.taCategoriePreference.idCategoriePreference = :idCategorie order by a.position,a.libelle,grp.position"),
})
public class TaPreferences implements java.io.Serializable {

	private static final long serialVersionUID = 5463130764046156519L;
	
	public static class QN {
		public static final String FIND_BY_CATEGORIE = "TaPreferences.findByCategorie";
	}
	
	private int idPreferences;
	private String cle;
	private String libelle;
	private String valeur;
	private String valeurDefaut;
	private String valeursPossibles;
	private String typeDonnees;
	
	private TaCategoriePreference taCategoriePreference;
	private TaGroupePreference taGroupePreference;
	private String fichierVue;
	private Integer position;
	private Integer positionDansGroupe;
	private Boolean estUnGroupe = false;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String groupe;
	private Boolean visible;
	private Boolean readOnly;
	

	public TaPreferences() {
	}

	public TaPreferences(int idInfoEntreprise) {
		this.idPreferences = idInfoEntreprise;
	}

	public TaPreferences(int idInfoEntreprise,
			String quiCree, Date quandCree,
			String quiModif, Date quandModif,
			String ipAcces, Integer versionObj) {
		this.idPreferences = idInfoEntreprise;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_preferences", unique = true, nullable = false)
	public int getIdPreferences() {
		return this.idPreferences;
	}

	public void setIdPreferences(int idInfoEntreprise) {
		this.idPreferences = idInfoEntreprise;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeInfoEntreprise) {
		this.quiCree = quiCreeInfoEntreprise;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeInfoEntreprise) {
		this.quandCree = quandCreeInfoEntreprise;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifInfoEntreprise) {
		this.quiModif = quiModifInfoEntreprise;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifInfoEntreprise) {
		this.quandModif = quandModifInfoEntreprise;
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

	@Version
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "cle")
	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}

	@Column(name = "valeur")
	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	@Column(name = "valeur_defaut")
	public String getValeurDefaut() {
		return valeurDefaut;
	}

	public void setValeurDefaut(String valeurDefaut) {
		this.valeurDefaut = valeurDefaut;
	}

	@Column(name = "type_donnees")
	public String getTypeDonnees() {
		return typeDonnees;
	}

	public void setTypeDonnees(String type) {
		this.typeDonnees = type;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "valeurs_possibles")
	public String getValeursPossibles() {
		return valeursPossibles;
	}

	public void setValeursPossibles(String valeursPossibles) {
		this.valeursPossibles = valeursPossibles;
	}

	@Column(name = "groupe", length = 100)
	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String objectConcerne) {
		this.groupe = objectConcerne;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_categorie_preference")
	public TaCategoriePreference getTaCategoriePreference() {
		return taCategoriePreference;
	}

	public void setTaCategoriePreference(TaCategoriePreference id_categorie_preference) {
		this.taCategoriePreference = id_categorie_preference;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_groupe_preference")
	public TaGroupePreference getTaGroupePreference() {
		return taGroupePreference;
	}

	public void setTaGroupePreference(TaGroupePreference id_groupe_preference) {
		this.taGroupePreference = id_groupe_preference;
	}

	@Column(name = "fichier_vue")
	public String getFichierVue() {
		return fichierVue;
	}

	public void setFichierVue(String fichier_vue) {
		this.fichierVue = fichier_vue;
	}

	@Column(name = "position")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column(name = "position_dans_groupe")
	public Integer getPositionDansGroupe() {
		return positionDansGroupe;
	}

	public void setPositionDansGroupe(Integer position_dans_groupe) {
		this.positionDansGroupe = position_dans_groupe;
	}

	@Column(name = "est_un_groupe")
	public Boolean getEstUnGroupe() {
		return estUnGroupe;
	}

	public void setEstUnGroupe(Boolean est_un_groupe) {
		this.estUnGroupe = est_un_groupe;
	}

	@Column(name = "visible")
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@Column(name = "read_only")
	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}



}
