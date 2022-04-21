package fr.legrain.abonnement.stripe.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/invoiceitems?lang=java

@Entity
@Table(name = "ta_stripe_invoice_item")
public class TaStripeInvoiceItem implements java.io.Serializable {
/*
com.stripe.model.InvoiceItem JSON: {
  "id": "ii_1DYwYQ2eZvKYlo2C8MzUkgi1",
  "object": "invoiceitem",
  "amount": 1000,
  "currency": "usd",
  "customer": "cus_E10dOLo0U8K1Ne",
  "date": 1542809986,
  "description": "Time on Limited Edition T-Shirt after 21 Nov 2018",
  "discountable": false,
  "invoice": "in_1DYwYQ2eZvKYlo2CheqYMyT9",
  "livemode": false,
  "metadata": {
  },
  "period": {
    "start": 1542809986,
    "end": 1545350400
  },
  "plan": {
    "id": "plan_E0yV3fJiR0HYy0",
    "object": "plan",
    "active": true,
    "aggregate_usage": null,
    "amount": 1,
    "billing_scheme": "per_unit",
    "created": 1542809984,
    "currency": "usd",
    "interval": "year",
    "interval_count": 1,
    "livemode": false,
    "metadata": {
    },
    "nickname": "Pro Plan",
    "product": "prod_E0yVWSaa1RODRd",
    "tiers": null,
    "tiers_mode": null,
    "transform_usage": null,
    "trial_period_days": null,
    "usage_type": "licensed"
  },
  "proration": true,
  "quantity": 1,
  "subscription": "sub_E0yVXmufci2fti",
  "subscription_item": "si_E0yVBuhHcB3pNY",
  "unit_amount": 1000
}
 */

	private static final long serialVersionUID = 6058273977995491848L;
	
	private int idStripeInvoiceItem;
	private String idExterne;
	
	private TaStripeInvoice taStripeInvoice;
	
	private TaStripePlan taPlan;
	private Integer quantity;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_invoice_item", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_invoice_item",table = "ta_stripe_invoice_item",champEntite="idStripeInvoiceItem", clazz = TaStripeInvoiceItem.class)
	public int getIdStripeInvoiceItem() {
		return idStripeInvoiceItem;
	}
	public void setIdStripeInvoiceItem(int idStripeInvoiceItem) {
		this.idStripeInvoiceItem = idStripeInvoiceItem;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_invoice")
	public TaStripeInvoice getTaStripeInvoice() {
		return taStripeInvoice;
	}
	public void setTaStripeInvoice(TaStripeInvoice taStripeInvoice) {
		this.taStripeInvoice = taStripeInvoice;
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
}
