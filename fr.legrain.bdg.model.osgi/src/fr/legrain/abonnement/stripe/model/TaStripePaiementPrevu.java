package fr.legrain.abonnement.stripe.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_stripe_paiement_prevu")
public class TaStripePaiementPrevu implements java.io.Serializable {

	private int idStripePaiementPrevu;
	private TaStripeSource taStripeSource;
	private TaStripeCharge taStripeCharge;
//	private TaStripeSubscription taStripeSubscription; 
	
	private TaAbonnement taAbonnement;
	
	private TaAvisEcheance taAvisEcheance;
	private TaPrelevement taPrelevement;

	private Boolean annule = false;
	private Boolean capture = true;
	private TaStripeInvoice taStripeInvoice;
	private TaStripeCustomer taStripeCustomer;
	private BigDecimal montant;
	private Date dateDeclenchement;
	private String raisonAnnulation;
	private String raisonPaiement;
	private String commentaire;
	
	private Date dateAnnulation;
	private String commentaireAnnulation;
	
	private byte[] timerHandle;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_paiement_prevu", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_paiement_prevu",table = "ta_stripe_paiement_prevu",champEntite="idStripePaiementPrevu", clazz = TaStripePaiementPrevu.class)
	public int getIdStripePaiementPrevu() {
		return idStripePaiementPrevu;
	}
	public void setIdStripePaiementPrevu(int idCharge) {
		this.idStripePaiementPrevu = idCharge;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeCompteBanque) {
		this.quiCree = quiCreeCompteBanque;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeCompteBanque) {
		this.quandCree = quandCreeCompteBanque;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifCompteBanque) {
		this.quiModif = quiModifCompteBanque;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifCompteBanque) {
		this.quandModif = quandModifCompteBanque;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_source")
	public TaStripeSource getTaStripeSource() {
		return taStripeSource;
	}
	public void setTaStripeSource(TaStripeSource taStripeSource) {
		this.taStripeSource = taStripeSource;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stripe_invoice")
	public TaStripeInvoice getTaStripeInvoice() {
		return taStripeInvoice;
	}
	public void setTaStripeInvoice(TaStripeInvoice taStripeInvoice) {
		this.taStripeInvoice = taStripeInvoice;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_customer")
	public TaStripeCustomer getTaStripeCustomer() {
		return taStripeCustomer;
	}
	public void setTaStripeCustomer(TaStripeCustomer taStripeCustomer) {
		this.taStripeCustomer = taStripeCustomer;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stripe_charge")
	public TaStripeCharge getTaStripeCharge() {
		return taStripeCharge;
	}
	public void setTaStripeCharge(TaStripeCharge taStripeCharge) {
		this.taStripeCharge = taStripeCharge;
	}
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_stripe_subscription")
//	public TaStripeSubscription getTaStripeSubscription() {
//		return taStripeSubscription;
//	}
//	public void setTaStripeSubscription(TaStripeSubscription taStripeSubscription) {
//		this.taStripeSubscription = taStripeSubscription;
//	}
	
	@Column(name = "annule")
	public Boolean getAnnule() {
		return annule;
	}
	public void setAnnule(Boolean annule) {
		this.annule = annule;
	}
	
	@Column(name = "capture")
	public Boolean getCapture() {
		return capture;
	}
	public void setCapture(Boolean capture) {
		this.capture = capture;
	}
	
	@Column(name = "montant")
	public BigDecimal getMontant() {
		return montant;
	}
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	
	@Column(name = "date_declenchement")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateDeclenchement() {
		return dateDeclenchement;
	}
	public void setDateDeclenchement(Date date_declenchement) {
		this.dateDeclenchement = date_declenchement;
	}
	
	@Column(name = "raison_annulation")
	public String getRaisonAnnulation() {
		return raisonAnnulation;
	}
	public void setRaisonAnnulation(String raison_annulation) {
		this.raisonAnnulation = raison_annulation;
	}
	
	@Column(name = "raison_paiement")
	public String getRaisonPaiement() {
		return raisonPaiement;
	}
	public void setRaisonPaiement(String raison_paiement) {
		this.raisonPaiement = raison_paiement;
	}
	
	@Column(name = "commentaire")
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	@Column(name = "timer_handle")
	public byte[] getTimerHandle() {
		return timerHandle;
	}
	public void setTimerHandle(byte[] timerHandle) {
		this.timerHandle = timerHandle;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avis_echeance")
	public TaAvisEcheance getTaAvisEcheance() {
		return taAvisEcheance;
	}
	public void setTaAvisEcheance(TaAvisEcheance taAvisEcheance) {
		this.taAvisEcheance = taAvisEcheance;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_prelevement")
	public TaPrelevement getTaPrelevement() {
		return taPrelevement;
	}
	public void setTaPrelevement(TaPrelevement taPrelevement) {
		this.taPrelevement = taPrelevement;
	}
	
	@Column(name = "date_annulation")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateAnnulation() {
		return dateAnnulation;
	}
	public void setDateAnnulation(Date dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}
	
	@Column(name = "commentaire_annulation")
	public String getCommentaireAnnulation() {
		return commentaireAnnulation;
	}
	public void setCommentaireAnnulation(String commentaireAnnulation) {
		this.commentaireAnnulation = commentaireAnnulation;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_abonnement")
	public TaAbonnement getTaAbonnement() {
		return taAbonnement;
	}
	public void setTaAbonnement(TaAbonnement taAbonnement) {
		this.taAbonnement = taAbonnement;
	}
	
}
