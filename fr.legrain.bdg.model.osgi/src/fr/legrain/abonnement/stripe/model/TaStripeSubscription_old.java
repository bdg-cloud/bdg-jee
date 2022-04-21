package fr.legrain.abonnement.stripe.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/subscriptions?lang=java

@Entity
@Table(name = "ta_stripe_subscription")
public class TaStripeSubscription_old implements java.io.Serializable {

	private static final long serialVersionUID = 4384207151677693468L;
	
/*
com.stripe.model.Subscription JSON: {
  "id": "sub_8epEF0PuRhmltU",
  "object": "subscription",
  "application_fee_percent": null,
  "billing": "charge_automatically",
  "billing_cycle_anchor": 1466202990,
  "cancel_at_period_end": false,
  "canceled_at": 1517528245,
  "created": 1466202990,
  "current_period_end": 1518906990,
  "current_period_start": 1516228590,
  "customer": "cus_8epDebVEl8Bs2V",
  "days_until_due": null,
  "default_source": null,
  "discount": null,
  "ended_at": 1517528245,
  "items": {
    "object": "list",
    "data": [
      {
        "id": "si_18NVZi2eZvKYlo2CUtBNGL9x",
        "object": "subscription_item",
        "created": 1466202990,
        "metadata": {
        },
        "plan": {
          "id": "ivory-freelance-040",
          "object": "plan",
          "active": true,
          "aggregate_usage": null,
          "amount": 999,
          "billing_scheme": "per_unit",
          "created": 1466202980,
          "currency": "usd",
          "interval": "month",
          "interval_count": 1,
          "livemode": false,
          "metadata": {
          },
          "nickname": null,
          "product": "prod_BUthVRQ7KdFfa7",
          "tiers": null,
          "tiers_mode": null,
          "transform_usage": null,
          "trial_period_days": null,
          "usage_type": "licensed"
        },
        "quantity": 1,
        "subscription": "sub_8epEF0PuRhmltU"
      }
    ],
    "has_more": false,
    "total_count": 1,
    "url": "/v1/subscription_items?subscription=sub_8epEF0PuRhmltU"
  },
  "livemode": false,
  "metadata": {
  },
  "plan": {
    "id": "ivory-freelance-040",
    "object": "plan",
    "active": true,
    "aggregate_usage": null,
    "amount": 999,
    "billing_scheme": "per_unit",
    "created": 1466202980,
    "currency": "usd",
    "interval": "month",
    "interval_count": 1,
    "livemode": false,
    "metadata": {
    },
    "nickname": null,
    "product": "prod_BUthVRQ7KdFfa7",
    "tiers": null,
    "tiers_mode": null,
    "transform_usage": null,
    "trial_period_days": null,
    "usage_type": "licensed"
  },
  "quantity": 1,
  "start": 1466202990,
  "status": "canceled",
  "tax_percent": null,
  "trial_end": null,
  "trial_start": null
}
 */

	private int idStripeSubscription;
	private String idExterne;
	private String codeDocument;
	private TaStripeCustomer taStripeCustomer;
	private TaStripeSource taStripeSource;
	private TaAbonnement taAbonnement;
	private Integer quantity;
	private String status;
	private String billing;
	
	private Boolean prorata = true;
	private Date dateDebut;
	private Date dateFin;
	private Date billingCycleAnchor;
	
	private BigDecimal taxe;
	private Integer trial;
	private TaStripeCoupon taStripeCoupon;
	private Integer nbEcheance;
	private Integer daysUntilDue;
	
	private Date dateAnnulation;
	private String commentaireAnnulation;
	
	private byte[] timerHandle;
	
	private Set<TaStripeSubscriptionItem_old> items;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	public void reinitialiseNumLignesMP(Integer aPartirDe,int increment){
//		int i=aPartirDe+increment;
//		//int i=aPartirDe;
//		for (Object ligne : items) {
////			if(((SWTLigneDocument)ligne).getNUM_LIGNE()>=aPartirDe)
//			if(((SWTLigneDocument)ligne).getNumLigneLDocument()>=aPartirDe)
//			{
//				((SWTLigneDocument)ligne).setNumLigneLDocument(i);
//				i++;
//			}
//		}
////		reinitialiseNumLignes();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_subscription", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_subscription",table = "ta_stripe_subscription",champEntite="idStripeSubscription", clazz = TaStripeSubscription_old.class)
	public int getIdStripeSubscription() {
		return idStripeSubscription;
	}
	public void setIdStripeSubscription(int idSubscription) {
		this.idStripeSubscription = idSubscription;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_customer")
	public TaStripeCustomer getTaStripeCustomer() {
		return taStripeCustomer;
	}
	public void setTaStripeCustomer(TaStripeCustomer taCustomer) {
		this.taStripeCustomer = taCustomer;
	}
	
	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taStripeSubscription", orphanRemoval=true)
	public Set<TaStripeSubscriptionItem_old> getItems() {
		return items;
	}
	public void setItems(Set<TaStripeSubscriptionItem_old> items) {
		this.items = items;
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
	
	@Column(name = "prorata")
	public Boolean getProrata() {
		return prorata;
	}
	public void setProrata(Boolean prorata) {
		this.prorata = prorata;
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
	
	@Column(name = "billing_cycle_anchor")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getBillingCycleAnchor() {
		return billingCycleAnchor;
	}
	public void setBillingCycleAnchor(Date billingCycleAnchor) {
		this.billingCycleAnchor = billingCycleAnchor;
	}
	
	@Column(name = "taxe")
	public BigDecimal getTaxe() {
		return taxe;
	}
	public void setTaxe(BigDecimal taxe) {
		this.taxe = taxe;
	}
	
	@Column(name = "trial")
	public Integer getTrial() {
		return trial;
	}
	public void setTrial(Integer trial) {
		this.trial = trial;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_coupon")
	public TaStripeCoupon getTaStripeCoupon() {
		return taStripeCoupon;
	}
	public void setTaStripeCoupon(TaStripeCoupon taStripeCoupon) {
		this.taStripeCoupon = taStripeCoupon;
	}
	
	@Column(name = "nb_echeance")
	public Integer getNbEcheance() {
		return nbEcheance;
	}
	public void setNbEcheance(Integer nbEcheance) {
		this.nbEcheance = nbEcheance;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_source")
	public TaStripeSource getTaStripeSource() {
		return taStripeSource;
	}
	public void setTaStripeSource(TaStripeSource taStripeSource) {
		this.taStripeSource = taStripeSource;
	}
	
	@Column(name = "billing")
	public String getBilling() {
		return billing;
	}
	public void setBilling(String billing) {
		this.billing = billing;
	}
	
	@Column(name = "days_until_due")
	public Integer getDaysUntilDue() {
		return daysUntilDue;
	}
	public void setDaysUntilDue(Integer daysUntilDue) {
		this.daysUntilDue = daysUntilDue;
	}
	
	@Column(name = "timer_handle")
	public byte[] getTimerHandle() {
		return timerHandle;
	}
	public void setTimerHandle(byte[] timerHandle) {
		this.timerHandle = timerHandle;
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
	
	@Column(name = "code_document")
	public String getCodeDocument() {
		return codeDocument;
	}
	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_abonnement",unique = true)
	public TaAbonnement getTaAbonnement() {
		return taAbonnement;
	}

	public void setTaAbonnement(TaAbonnement taAbonnement) {
		this.taAbonnement = taAbonnement;
	}

}
