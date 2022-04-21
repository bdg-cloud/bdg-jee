package fr.legrain.tache.model;

import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.document.model.SWTDocument;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tiers.model.TaTiers;

/*
Gestion des tâches dans Bdg

Créer une table des type de tâches modifiables, ainsi qu’une table de type de type de tâche (Rappel, Rdv, Création documents)
Lorsque l’on créé un type de tâche, on peut l’associer à un type de type afin que le programme puisse effectuer des traitements spécifiques à ce type de type de tâches.

Table des tâches

Cette table doit contenir une tâche programmé inscrite dans un calendrier, associé éventuellement à un tiers, ainsi qu’à un utilisateur. Une notion de public ou privé doit être renseigné. 
Notion de récurrence sur période
Notion de durée de la tâche 
Notion de en cours ou terminée

Créer un table de liaison tâche/tiers ainsi que tâche/utilisateur qui inclut des notions de notification par email, sms.
Créer une table de liaison avec les documents

*/
@Entity
@Table(name = "ta_evenement")
public class TaEvenement implements java.io.Serializable {

	private static final long serialVersionUID = -5330715588234043746L;
	
	private int idEvenement;
	private TaTypeEvenement taTypeEvenement;
	private TaUtilisateur proprietaire;
	private TaAgenda taAgenda;
	private String codeEvenement;
	private String libelleEvenement;
	private String description;
	private String emplacement;
	private String couleur;
	//private Blob data;
	private Date dateDebut;
	private Date dateFin;
	private TaRecurrenceEvenement taRecurrence;
	private boolean enCours;
	private boolean termine;
	private boolean recurrent;
	private boolean prive;
	private boolean touteLaJournee;
	private Integer nbMinuteDuree;
	private Integer nbHeureDuree;
	private Integer nbJoursDuree;
	
	private String origine;
	
	private List<TaTiers> listeTiers;
	private List<TaUtilisateur> listeUtilisateur;
	private List<TaRDocumentEvenement> listeDocument;
	
	private Set<TaNotification> taNotificationUtilisateur;
	private Set<TaNotification> taNotificationTiers;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_evenement", unique = true, nullable = false)
	public int getIdEvenement() {
		return idEvenement;
	}
	public void setIdEvenement(int idEvenement) {
		this.idEvenement = idEvenement;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_type_evenement")
	public TaTypeEvenement getTaTypeEvenement() {
		return taTypeEvenement;
	}
	public void setTaTypeEvenement(TaTypeEvenement taTypeEvenement) {
		this.taTypeEvenement = taTypeEvenement;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	public TaUtilisateur getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(TaUtilisateur proprietaire) {
		this.proprietaire = proprietaire;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_agenda")
	public TaAgenda getTaAgenda() {
		return taAgenda;
	}
	public void setTaAgenda(TaAgenda taAgenda) {
		this.taAgenda = taAgenda;
	}
	
	@Column(name = "code_evenement")
	public String getCodeEvenement() {
		return codeEvenement;
	}
	public void setCodeEvenement(String codeEvenement) {
		this.codeEvenement = codeEvenement;
	}
	
	@Column(name = "libelle_evenement")
	public String getLibelleEvenement() {
		return libelleEvenement;
	}
	public void setLibelleEvenement(String libelleEvenement) {
		this.libelleEvenement = libelleEvenement;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "emplacement")
	public String getEmplacement() {
		return emplacement;
	}
	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}
	
	@Column(name = "couleur")
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	
	@Column(name = "date_debut")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}
	
	@Column(name = "date_fin")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateFin() {
		return dateFin;
	}
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
	@JoinColumn(name = "id_recurrence_evenement")
	public TaRecurrenceEvenement getTaRecurrence() {
		return taRecurrence;
	}
	public void setTaRecurrence(TaRecurrenceEvenement taRecurrence) {
		this.taRecurrence = taRecurrence;
	}
	
	@Column(name = "en_cours")
	public boolean isEnCours() {
		return enCours;
	}
	public void setEnCours(boolean enCours) {
		this.enCours = enCours;
	}
	
	@Column(name = "termine")
	public boolean isTermine() {
		return termine;
	}
	public void setTermine(boolean termine) {
		this.termine = termine;
	}
	
	@Column(name = "recurrent")
	public boolean isRecurrent() {
		return recurrent;
	}
	public void setRecurrent(boolean recurrent) {
		this.recurrent = recurrent;
	}
	
	@Column(name = "prive")
	public boolean isPrive() {
		return prive;
	}
	public void setPrive(boolean prive) {
		this.prive = prive;
	}
	
	@Column(name = "toute_la_journee")
	public boolean isTouteLaJournee() {
		return touteLaJournee;
	}
	public void setTouteLaJournee(boolean touteLaJournee) {
		this.touteLaJournee = touteLaJournee;
	}
	
	@Column(name = "nbminute_duree")
	public Integer getNbMinuteDuree() {
		return nbMinuteDuree;
	}
	public void setNbMinuteDuree(Integer nbMinuteDuree) {
		this.nbMinuteDuree = nbMinuteDuree;
	}
	
	@Column(name = "nbheure_duree")
	public Integer getNbHeureDuree() {
		return nbHeureDuree;
	}
	public void setNbHeureDuree(Integer nbHeureDuree) {
		this.nbHeureDuree = nbHeureDuree;
	}
	
	@Column(name = "nbjours_duree")
	public Integer getNbJoursDuree() {
		return nbJoursDuree;
	}
	public void setNbJoursDuree(Integer nbJoursDuree) {
		this.nbJoursDuree = nbJoursDuree;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_tiers_evenement",
			joinColumns = {@JoinColumn(name = "id_evenement")},inverseJoinColumns = {@JoinColumn(name = "id_tiers")})
	@Fetch(FetchMode.SUBSELECT)
	public List<TaTiers> getListeTiers() {
		return listeTiers;
	}
	public void setListeTiers(List<TaTiers> listeTiers) {
		this.listeTiers = listeTiers;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_utilisateur_evenement",
			joinColumns = {@JoinColumn(name = "id_evenement")},inverseJoinColumns = {@JoinColumn(name = "id_utilisateur")})
	@Fetch(FetchMode.SUBSELECT)
	public List<TaUtilisateur> getListeUtilisateur() {
		return listeUtilisateur;
	}
	public void setListeUtilisateur(List<TaUtilisateur> listeUtilisateur) {
		this.listeUtilisateur = listeUtilisateur;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taEvenement", orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	public List<TaRDocumentEvenement> getListeDocument() {
		return listeDocument;
	}
	public void setListeDocument(List<TaRDocumentEvenement> listeDocument) {
		this.listeDocument = listeDocument;
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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taEvenementUtilisateur", orphanRemoval=true)
	public Set<TaNotification> getTaNotificationUtilisateur() {
		return taNotificationUtilisateur;
	}
	public void setTaNotificationUtilisateur(Set<TaNotification> taNotificationUtilisateur) {
		this.taNotificationUtilisateur = taNotificationUtilisateur;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taEvenementTiers", orphanRemoval=true)
	public Set<TaNotification> getTaNotificationTiers() {
		return taNotificationTiers;
	}
	public void setTaNotificationTiers(Set<TaNotification> taNotificationTiers) {
		this.taNotificationTiers = taNotificationTiers;
	}
	
	@Transient
	public String getOrigine() {
		return origine;
	}
	public void setOrigine(String origine) {
		this.origine = origine;
	}
}