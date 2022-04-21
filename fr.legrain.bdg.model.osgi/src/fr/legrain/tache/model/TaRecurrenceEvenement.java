package fr.legrain.tache.model;

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
@Table(name = "ta_recurrence_evenement")
public class TaRecurrenceEvenement implements java.io.Serializable {

	private static final long serialVersionUID = -2520160548628880790L;
	
//	repetition quotidienne (chaque xxx jours,chaque jours ouvrable)
//	repetition hebdomadaire (choisir L/M/M/J/V/S/D et toute les combiens de semaines)
//	repetition mensuelle (le 1er du mois, le dernier jours, le xx du mois, le n-ième L/M/M/J/V/S/D du mois)
//	repetition annuelle
//
//	pas de date de fin
//	repetition jusqu'au xx/xx/xxxx
//	repetition xx fois
	
	private int idRecurrence;
	private Date dateDebutRecurrence;
	private Date dateFinRecurrence;
	
	private boolean quotidien;
	private boolean hebdomadaire;
	private boolean mensuelle;
	private boolean annuelle;
	
	private int chaqueNbJour;
	private boolean chaqueJourOuvrable;
	private boolean touteLesNbSemaines;
	private int lesXxduMois;
	private boolean lesDernierJoursDuMois;
	
	private boolean lesLundi;
	private boolean lesMardi;
	private boolean lesMercredi;
	private boolean lesJeudi;
	private boolean lesVendredi;
	private boolean lesSamedi;
	private boolean lesDimanche;
	
	private int seRepeteraNbFois;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_recurrence_evenement", unique = true, nullable = false)
	public int getIdRecurrence() {
		return idRecurrence;
	}
	public void setIdRecurrence(int idRecurrence) {
		this.idRecurrence = idRecurrence;
	}
	
	@Column(name = "date_debut_recurrence")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDebutRecurrence() {
		return dateDebutRecurrence;
	}
	public void setDateDebutRecurrence(Date dateDebutRecurrence) {
		this.dateDebutRecurrence = dateDebutRecurrence;
	}
	
	@Column(name = "date_fin_recurrence")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateFinRecurrence() {
		return dateFinRecurrence;
	}
	public void setDateFinRecurrence(Date datefinRecurrence) {
		this.dateFinRecurrence = datefinRecurrence;
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
	
	@Column(name = "quotidien")
	public boolean isQuotidien() {
		return quotidien;
	}
	public void setQuotidien(boolean quotidien) {
		this.quotidien = quotidien;
	}
	
	@Column(name = "hebdomadaire")
	public boolean isHebdomadaire() {
		return hebdomadaire;
	}
	public void setHebdomadaire(boolean hebdomadaire) {
		this.hebdomadaire = hebdomadaire;
	}
	
	@Column(name = "mensuelle")
	public boolean isMensuelle() {
		return mensuelle;
	}
	public void setMensuelle(boolean mensuelle) {
		this.mensuelle = mensuelle;
	}
	
	@Column(name = "annuelle")
	public boolean isAnnuelle() {
		return annuelle;
	}
	public void setAnnuelle(boolean annuelle) {
		this.annuelle = annuelle;
	}
	
	@Column(name = "chaque_nb_jour")
	public int getChaqueNbJour() {
		return chaqueNbJour;
	}
	public void setChaqueNbJour(int chaque_nb_jour) {
		this.chaqueNbJour = chaque_nb_jour;
	}
	
	@Column(name = "chaque_jour_ouvrable")
	public boolean isChaqueJourOuvrable() {
		return chaqueJourOuvrable;
	}
	public void setChaqueJourOuvrable(boolean chaque_jour_ouvrable) {
		this.chaqueJourOuvrable = chaque_jour_ouvrable;
	}
	
	@Column(name = "toute_les_nb_semaines")
	public boolean isTouteLesNbSemaines() {
		return touteLesNbSemaines;
	}
	public void setTouteLesNbSemaines(boolean toute_les_nb_semaines) {
		this.touteLesNbSemaines = toute_les_nb_semaines;
	}
	
	@Column(name = "les_xx_du_mois")
	public int getLesXxduMois() {
		return lesXxduMois;
	}
	public void setLesXxduMois(int les_xx_du_mois) {
		this.lesXxduMois = les_xx_du_mois;
	}
	
	@Column(name = "les_dernier_jours_du_mois")
	public boolean isLesDernierJoursDuMois() {
		return lesDernierJoursDuMois;
	}
	public void setLesDernierJoursDuMois(boolean les_dernier_jours_du_mois) {
		this.lesDernierJoursDuMois = les_dernier_jours_du_mois;
	}
	
	@Column(name = "les_lundi")
	public boolean isLesLundi() {
		return lesLundi;
	}
	public void setLesLundi(boolean les_lundi) {
		this.lesLundi = les_lundi;
	}
	
	@Column(name = "les_mardi")
	public boolean isLesMardi() {
		return lesMardi;
	}
	public void setLesMardi(boolean les_mardi) {
		this.lesMardi = les_mardi;
	}
	
	@Column(name = "les_mercredi")
	public boolean isLesMercredi() {
		return lesMercredi;
	}
	public void setLesMercredi(boolean les_mercredi) {
		this.lesMercredi = les_mercredi;
	}
	
	@Column(name = "les_jeudi")
	public boolean isLesJeudi() {
		return lesJeudi;
	}
	public void setLesJeudi(boolean les_jeudi) {
		this.lesJeudi = les_jeudi;
	}
	
	@Column(name = "les_vendredi")
	public boolean isLesVendredi() {
		return lesVendredi;
	}
	public void setLesVendredi(boolean les_vendredi) {
		this.lesVendredi = les_vendredi;
	}
	
	@Column(name = "les_samedi")
	public boolean isLesSamedi() {
		return lesSamedi;
	}
	public void setLesSamedi(boolean les_samedi) {
		this.lesSamedi = les_samedi;
	}
	
	@Column(name = "les_dimanche")
	public boolean isLesDimanche() {
		return lesDimanche;
	}
	public void setLesDimanche(boolean les_dimanche) {
		this.lesDimanche = les_dimanche;
	}
	
	@Column(name = "se_repetera_nb_fois")
	public int getSeRepeteraNbFois() {
		return seRepeteraNbFois;
	}
	public void setSeRepeteraNbFois(int se_repetera_nb_fois) {
		this.seRepeteraNbFois = se_repetera_nb_fois;
	}
}