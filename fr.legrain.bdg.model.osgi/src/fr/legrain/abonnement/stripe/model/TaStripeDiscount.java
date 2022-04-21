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
import javax.persistence.Version;

import fr.legrain.document.model.TaAbonnement;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/discounts?lang=java

@Entity
@Table(name = "ta_stripe_discount")
public class TaStripeDiscount implements java.io.Serializable {

	private static final long serialVersionUID = 9180092232089693073L;
	
/*
com.stripe.model.Discount JSON: {
  "object": "discount",
  "coupon": {
    "id": "35OFF",
    "object": "coupon",
    "amount_off": null,
    "created": 1391694467,
    "currency": null,
    "duration": "repeating",
    "duration_in_months": 3,
    "livemode": false,
    "max_redemptions": null,
    "metadata": {
    },
    "name": null,
    "percent_off": 25.0,
    "redeem_by": null,
    "times_redeemed": 1,
    "valid": true
  },
  "customer": "cus_E10dOLo0U8K1Ne",
  "end": 1399384361,
  "start": 1391694761,
  "subscription": null
}
 */
	
	
	private int idStripeDiscount;
	private String idExterne;
	private TaStripeCoupon taCoupon;
	private TaStripeCustomer taCustomer;
	//private TaStripeSubscription taSubscription;
	private TaAbonnement taAbonnement;
	private Date end;
	private Date start;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_discount", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_discount",table = "ta_stripe_discount",champEntite="idStripeDiscount", clazz = TaStripeDiscount.class)
	public int getIdStripeDiscount() {
		return idStripeDiscount;
	}
	public void setIdStripeDiscount(int idDiscount) {
		this.idStripeDiscount = idDiscount;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_coupon")
	public TaStripeCoupon getTaCoupon() {
		return taCoupon;
	}
	public void setTaCoupon(TaStripeCoupon taCoupon) {
		this.taCoupon = taCoupon;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_customer")
	public TaStripeCustomer getTaCustomer() {
		return taCustomer;
	}
	public void setTaCustomer(TaStripeCustomer taCustomer) {
		this.taCustomer = taCustomer;
	}
	
//	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
//	@JoinColumn(name = "id_stripe_subscription")
//	public TaStripeSubscription getTaSubscription() {
//		return taSubscription;
//	}
//	public void setTaSubscription(TaStripeSubscription taSubscription) {
//		this.taSubscription = taSubscription;
//	}
	
	@Column(name = "discount_end")
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Column(name = "discount_start")
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
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
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_abonnement")
	public TaAbonnement getTaAbonnement() {
		return taAbonnement;
	}
	public void setTaAbonnement(TaAbonnement taAbonnement) {
		this.taAbonnement = taAbonnement;
	}
	
	
}
