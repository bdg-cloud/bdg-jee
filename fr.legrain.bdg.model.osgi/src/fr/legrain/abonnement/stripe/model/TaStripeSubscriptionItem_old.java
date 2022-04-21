package fr.legrain.abonnement.stripe.model;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/subscription_items?lang=java

@Entity
@Table(name = "ta_stripe_subscription_item")
public class TaStripeSubscriptionItem_old implements java.io.Serializable {

	private static final long serialVersionUID = -768602221972250076L;
	
/*
com.stripe.model.SubscriptionItem JSON: {
  "id": "si_E10el9jZsQtjqX",
  "object": "subscription_item",
  "created": 1542817973,
  "metadata": {
  },
  "plan": {
    "id": "emerald-company-234",
    "object": "plan",
    "active": true,
    "aggregate_usage": null,
    "amount": 999,
    "billing_scheme": "per_unit",
    "created": 1506408191,
    "currency": "usd",
    "interval": "month",
    "interval_count": 1,
    "livemode": false,
    "metadata": {
    },
    "nickname": null,
    "product": "prod_BT942zL7VcClrn",
    "tiers": null,
    "tiers_mode": null,
    "transform_usage": null,
    "trial_period_days": null,
    "usage_type": "licensed"
  },
  "quantity": 1,
  "subscription": "sub_E10e5sEj8gQJdD"
}
 */
	
	private int idStripeSubscriptionItem;
	private String idExterne;
	private Integer numLigneLDocument;
	private TaStripePlan taPlan;
	private TaLAbonnement taLAbonnement;
	private Integer quantity;
	private String complement1;
	private String complement2;
	private String complement3;
	private TaStripeSubscription_old taStripeSubscription;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	public boolean ligneEstVide() {
		if (taPlan!=null)return false;
//		if (libLDocument!=null && !libLDocument.equals(""))return false;	
		return true;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_subscription_item", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_subscription_item",table = "ta_stripe_subscription_item",champEntite="idStripeSubscriptionItem", clazz = TaStripeSubscriptionItem_old.class)
	public int getIdStripeSubscriptionItem() {
		return idStripeSubscriptionItem;
	}
	public void setIdStripeSubscriptionItem(int idSubscriptionItem) {
		this.idStripeSubscriptionItem = idSubscriptionItem;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stripe_plan")
	public TaStripePlan getTaPlan() {
		return taPlan;
	}
	public void setTaPlan(TaStripePlan taPlan) {
		this.taPlan = taPlan;
	}
	
	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_subscription")
	public TaStripeSubscription_old getTaStripeSubscription() {
		return taStripeSubscription;
	}
	public void setTaStripeSubscription(TaStripeSubscription_old taSubscription) {
		this.taStripeSubscription = taSubscription;
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
	
	@Column(name = "complement_1")
	public String getComplement1() {
		return complement1;
	}
	public void setComplement1(String complement1) {
		this.complement1 = complement1;
	}
	
	@Column(name = "complement_2")
	public String getComplement2() {
		return complement2;
	}
	public void setComplement2(String complement2) {
		this.complement2 = complement2;
	}
	
	@Column(name = "complement_3")
	public String getComplement3() {
		return complement3;
	}
	public void setComplement3(String complement3) {
		this.complement3 = complement3;
	}

	@Transient //a modifier
	public Integer getNumLigneLDocument() {
		return numLigneLDocument;
	}

	public void setNumLigneLDocument(Integer numLigneLDocument) {
		this.numLigneLDocument = numLigneLDocument;
	}

//	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "id_l_abonnement",unique = true)
	public TaLAbonnement getTaLAbonnement() {
		return taLAbonnement;
	}

	public void setTaLAbonnement(TaLAbonnement taLAbonnement) {
		this.taLAbonnement = taLAbonnement;
	}
}
