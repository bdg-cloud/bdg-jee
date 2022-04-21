package fr.legrain.tache.model;

import java.util.Date;
import java.util.List;

import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
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
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tiers.model.TaTiers;

@Entity
@Table(name = "ta_notification")
public class TaNotification implements java.io.Serializable {

	private static final long serialVersionUID = 9051128569479962643L;
	
	private int idNotification;
	private TaEvenement taEvenementUtilisateur;
	private TaEvenement taEvenementTiers;
	private TaTypeNotification taTypeNotification;
	private Date dateNotification; // ou nbjour avant et heure
	private int nbMinutesAvantNotification;
	private int nbHeuresAvantNotification;
	private int nbJourAvantNotification;
	private int nbsemainesAvantNotification;
	private String texteNotification;
	
	private byte[] timerHandle;
	
	private boolean notificationEnvoyee;
	private boolean lu;
	private boolean auDebutdeEvenement = true;
	private List<TaTiers> listeTiers;
	private List<TaUtilisateur> listeUtilisateur;
	//reporter la notification (pop up par exemple) pour une certaine durée
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_notification", unique = true, nullable = false)
	public int getIdNotification() {
		return idNotification;
	}
	public void setIdNotification(int idNotification) {
		this.idNotification = idNotification;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_type_notification")
	public TaTypeNotification getTaTypeNotification() {
		return taTypeNotification;
	}
	public void setTaTypeNotification(TaTypeNotification taTypeNotification) {
		this.taTypeNotification = taTypeNotification;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_notification")
	public Date getDateNotification() {
		return dateNotification;
	}
	public void setDateNotification(Date dateNotification) {
		this.dateNotification = dateNotification;
	}
	
	@Column(name = "nb_jour_avant_notification")
	public int getNbJourAvantNotification() {
		return nbJourAvantNotification;
	}
	public void setNbJourAvantNotification(int nbJourAvantNotification) {
		this.nbJourAvantNotification = nbJourAvantNotification;
	}
	
	@Column(name = "notification_envoyee")
	public boolean isNotificationEnvoyee() {
		return notificationEnvoyee;
	}
	public void setNotificationEnvoyee(boolean notificationEnvoyee) {
		this.notificationEnvoyee = notificationEnvoyee;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_tiers_notification",
			joinColumns = {@JoinColumn(name = "id_notification")},inverseJoinColumns = {@JoinColumn(name = "id_tiers")})
	@Fetch(FetchMode.SUBSELECT)
	public List<TaTiers> getListeTiers() {
		return listeTiers;
	}
	public void setListeTiers(List<TaTiers> listeTiers) {
		this.listeTiers = listeTiers;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_utilisateur_notification",
			joinColumns = {@JoinColumn(name = "id_notification")},inverseJoinColumns = {@JoinColumn(name = "id_utilisateur")})
	@Fetch(FetchMode.SUBSELECT)
	public List<TaUtilisateur> getListeUtilisateur() {
		return listeUtilisateur;
	}
	public void setListeUtilisateur(List<TaUtilisateur> listeUtilisateur) {
		this.listeUtilisateur = listeUtilisateur;
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
	
	@Column(name = "nb_minutes_avant_notification")
	public int getNbMinutesAvantNotification() {
		return nbMinutesAvantNotification;
	}
	public void setNbMinutesAvantNotification(int nbMinutesAvantNotification) {
		this.nbMinutesAvantNotification = nbMinutesAvantNotification;
	}
	
	@Column(name = "nb_heures_avant_notification")
	public int getNbHeuresAvantNotification() {
		return nbHeuresAvantNotification;
	}
	public void setNbHeuresAvantNotification(int nbHeuresAvantNotification) {
		this.nbHeuresAvantNotification = nbHeuresAvantNotification;
	}
	
	@Column(name = "nb_semaines_avant_notification")
	public int getNbsemainesAvantNotification() {
		return nbsemainesAvantNotification;
	}
	public void setNbsemainesAvantNotification(int nbsemainesAvantNotification) {
		this.nbsemainesAvantNotification = nbsemainesAvantNotification;
	}
	
	@Column(name = "texte_notification")
	public String getTexteNotification() {
		return texteNotification;
	}
	public void setTexteNotification(String texteNotification) {
		this.texteNotification = texteNotification;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_evenement")
	public TaEvenement getTaEvenementUtilisateur() {
		return taEvenementUtilisateur;
	}
	public void setTaEvenementUtilisateur(TaEvenement taEvenementUtilisateur) {
		this.taEvenementUtilisateur = taEvenementUtilisateur;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_evenement_tiers")
	public TaEvenement getTaEvenementTiers() {
		return taEvenementTiers;
	}
	public void setTaEvenementTiers(TaEvenement taEvenementTiers) {
		this.taEvenementTiers = taEvenementTiers;
	}
	
	@Column(name = "au_debut_de_evenement")
	public boolean isAuDebutdeEvenement() {
		return auDebutdeEvenement;
	}
	public void setAuDebutdeEvenement(boolean au_debut_de_evenement) {
		this.auDebutdeEvenement = au_debut_de_evenement;
	}
	
	@Column(name = "timer_handle")
	public byte[] getTimerHandle() {
		return timerHandle;
	}
	public void setTimerHandle(byte[] timerHandle) {
		this.timerHandle = timerHandle;
	}
	
	@Column(name = "lu")
	public boolean isLu() {
		return lu;
	}
	public void setLu(boolean lu) {
		this.lu = lu;
	}
	
	@PreRemove
	public void preRemove() {
		if(getTimerHandle()!=null) {
			//suppression du timer associer dans le serveur d'application
			TimerHandle h = (TimerHandle) SerializationUtils.deserialize(getTimerHandle());
			try {
				Timer t = h.getTimer();
				t.cancel();
			} catch (NoSuchObjectLocalException ex) {
				System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
			}
		}
	}
	
}