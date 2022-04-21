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

import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaReglement;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/invoices/create?lang=java

@Entity
@Table(name = "ta_stripe_invoice")
public class TaStripeInvoice implements java.io.Serializable {
/*
com.stripe.model.Invoice JSON: {
  "id": "in_1Bj7W62eZvKYlo2Co9KYEt61",
  "object": "invoice",
  "amount_due": 1000,
  "amount_paid": 0,
  "amount_remaining": 1000,
  "application_fee": null,
  "attempt_count": 0,
  "attempted": false,
  "auto_advance": true,
  "billing": "send_invoice",
  "billing_reason": null,
  "charge": null,
  "currency": "usd",
  "customer": "cus_E10dOLo0U8K1Ne",
  "date": 1515682734,
  "default_source": null,
  "description": null,
  "discount": null,
  "due_date": 1516287534,
  "ending_balance": 0,
  "finalized_at": 1515686335,
  "hosted_invoice_url": "https://pay.stripe.com/invoice/invst_kU2ETYe90IdpBmk3he0V5afZSG",
  "invoice_pdf": "https://pay.stripe.com/invoice/invst_kU2ETYe90IdpBmk3he0V5afZSG/pdf",
  "lines": {
    "data": [
      {
        "id": "sli_06bab3ae5b3624",
        "object": "line_item",
        "amount": 999,
        "currency": "usd",
        "description": "1 × Ivory Freelance (at $9.99 / month)",
        "discountable": true,
        "livemode": false,
        "metadata": {
        },
        "period": {
          "end": 1521326190,
          "start": 1518906990
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
        "proration": false,
        "quantity": 1,
        "subscription": "sub_8epEF0PuRhmltU",
        "subscription_item": "si_18NVZi2eZvKYlo2CUtBNGL9x",
        "type": "subscription"
      }
    ],
    "has_more": false,
    "object": "list",
    "url": "/v1/invoices/in_1Bj7W62eZvKYlo2Co9KYEt61/lines"
  },
  "livemode": false,
  "metadata": {
  },
  "next_payment_attempt": null,
  "number": "8717A4A-0001",
  "paid": false,
  "payment_intent": null,
  "period_end": 1515682734,
  "period_start": 1515682734,
  "receipt_number": null,
  "starting_balance": 0,
  "statement_descriptor": null,
  "status": "open",
  "subscription": "sub_C7MEeS3r3gfd66",
  "subtotal": 1000,
  "tax": 0,
  "tax_percent": null,
  "total": 1000,
  "webhooks_delivered_at": 1515682734
}
 */

	private static final long serialVersionUID = -784969605448766693L;
	
	private int idStripeInvoice;
	private String idExterne;
	private TaStripeCustomer taCustomer;
//	private TaStripeSubscription taSubscription;
	private TaAbonnement taAbonnement;
	private Set<TaStripeInvoiceItem> items;
	
	private BigDecimal amountDue;
	private String number;
	private Date dueDate;
	private Date created;
	private TaStripeCharge taStripeCharge;
	private Boolean paid;
	private Date periodStart;
	private Date periodEnd;
	private TaStripeCoupon taStripeCoupon;
	private BigDecimal taxe;
	private TaAvisEcheance taAvisEcheance;
	private TaReglement taReglement;
	private String status;
	
	private Date webhooksDeliveredAt;
	private Date nexPaymentAttempt;
	private String description;
	private String billingReason;
	private String billing;
	private Boolean attempted; 
	private Integer attemptCount;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_invoice", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_invoice",table = "ta_stripe_invoice",champEntite="idStripeInvoice", clazz = TaStripeInvoice.class)
	public int getIdStripeInvoice() {
		return idStripeInvoice;
	}
	public void setIdStripeInvoice(int idStripeInvoice) {
		this.idStripeInvoice = idStripeInvoice;
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
	public TaStripeCustomer getTaCustomer() {
		return taCustomer;
	}
	public void setTaCustomer(TaStripeCustomer taCustomer) {
		this.taCustomer = taCustomer;
	}
	
//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id_stripe_subscription")
//	public TaStripeSubscription getTaSubscription() {
//		return taSubscription;
//	}
//	public void setTaSubscription(TaStripeSubscription taSubscription) {
//		this.taSubscription = taSubscription;
//	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taStripeInvoice", orphanRemoval=true)
	public Set<TaStripeInvoiceItem> getItems() {
		return items;
	}
	public void setItems(Set<TaStripeInvoiceItem> items) {
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
	
	@Column(name = "amount_due")
	public BigDecimal getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(BigDecimal amountDue) {
		this.amountDue = amountDue;
	}
	
	@Column(name = "number")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name = "due_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_charge")
	public TaStripeCharge getTaStripeCharge() {
		return taStripeCharge;
	}
	public void setTaStripeCharge(TaStripeCharge taStripeCharge) {
		this.taStripeCharge = taStripeCharge;
	}
	
	@Column(name = "paid")
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
	@Column(name = "period_start")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPeriodStart() {
		return periodStart;
	}
	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}
	
	@Column(name = "period_end")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_coupon")
	public TaStripeCoupon getTaStripeCoupon() {
		return taStripeCoupon;
	}
	public void setTaStripeCoupon(TaStripeCoupon taStripeCoupon) {
		this.taStripeCoupon = taStripeCoupon;
	}
	
	@Column(name = "taxe")
	public BigDecimal getTaxe() {
		return taxe;
	}
	public void setTaxe(BigDecimal taxe) {
		this.taxe = taxe;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_avis_echeance")
	public TaAvisEcheance getTaAvisEcheance() {
		return taAvisEcheance;
	}
	public void setTaAvisEcheance(TaAvisEcheance taAvisEcheance) {
		this.taAvisEcheance = taAvisEcheance;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_reglement")
	public TaReglement getTaReglement() {
		return taReglement;
	}
	public void setTaReglement(TaReglement taReglement) {
		this.taReglement = taReglement;
	}
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "webhooks_delivered_at")
	public Date getWebhooksDeliveredAt() {
		return webhooksDeliveredAt;
	}
	public void setWebhooksDeliveredAt(Date webhooksDeliveredAt) {
		this.webhooksDeliveredAt = webhooksDeliveredAt;
	}
	
	@Column(name = "nex_payment_attempt")
	public Date getNexPaymentAttempt() {
		return nexPaymentAttempt;
	}
	public void setNexPaymentAttempt(Date nexPaymentAttempt) {
		this.nexPaymentAttempt = nexPaymentAttempt;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "billing_reason")
	public String getBillingReason() {
		return billingReason;
	}
	public void setBillingReason(String billingReason) {
		this.billingReason = billingReason;
	}
	
	@Column(name = "billing")
	public String getBilling() {
		return billing;
	}
	public void setBilling(String billing) {
		this.billing = billing;
	}
	
	@Column(name = "attempted")
	public Boolean getAttempted() {
		return attempted;
	}
	public void setAttempted(Boolean attempted) {
		this.attempted = attempted;
	}
	
	@Column(name = "attempt_count")
	public Integer getAttemptCount() {
		return attemptCount;
	}
	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_abonnement")
	public TaAbonnement getTaAbonnement() {
		return taAbonnement;
	}
	public void setTaAbonnement(TaAbonnement taAbonnement) {
		this.taAbonnement = taAbonnement;
	}
}
