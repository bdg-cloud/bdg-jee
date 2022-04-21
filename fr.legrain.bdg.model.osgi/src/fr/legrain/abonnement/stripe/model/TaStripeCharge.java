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

import fr.legrain.document.model.TaReglement;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/charges?lang=java

@Entity
@Table(name = "ta_stripe_charge")
public class TaStripeCharge implements java.io.Serializable {

	private static final long serialVersionUID = -1984475383731856103L;
	
/*
com.stripe.model.Charge JSON: {
  "id": "ch_1DYui92eZvKYlo2CkhfGhC4z",
  "object": "charge",
  "amount": 100,
  "amount_refunded": 0,
  "application": null,
  "application_fee": null,
  "balance_transaction": "txn_19XJJ02eZvKYlo2ClwuJ1rbA",
  "captured": false,
  "created": 1542802901,
  "currency": "usd",
  "customer": null,
  "description": "My First Test Charge (created for API docs)",
  "destination": null,
  "dispute": null,
  "failure_code": null,
  "failure_message": null,
  "fraud_details": {
  },
  "invoice": null,
  "livemode": false,
  "metadata": {
  },
  "on_behalf_of": null,
  "order": null,
  "outcome": null,
  "paid": true,
  "payment_intent": null,
  "receipt_email": null,
  "receipt_number": null,
  "refunded": false,
  "refunds": {
    "object": "list",
    "data": [

    ],
    "has_more": false,
    "total_count": 0,
    "url": "/v1/charges/ch_1DYui92eZvKYlo2CkhfGhC4z/refunds"
  },
  "review": null,
  "shipping": null,
  "source": {
    "id": "card_19yUNL2eZvKYlo2CNGsN6EWH",
    "object": "card",
    "address_city": null,
    "address_country": null,
    "address_line1": null,
    "address_line1_check": null,
    "address_line2": null,
    "address_state": null,
    "address_zip": null,
    "address_zip_check": null,
    "brand": "Visa",
    "country": "US",
    "customer": null,
    "cvc_check": "unchecked",
    "dynamic_last4": null,
    "exp_month": 12,
    "exp_year": 2020,
    "fingerprint": "Xt5EWLLDS7FJjR1c",
    "funding": "credit",
    "last4": "4242",
    "metadata": {
    },
    "name": "Jenny Rosen",
    "tokenization_method": null
  },
  "source_transfer": null,
  "statement_descriptor": null,
  "status": "succeeded",
  "transfer_group": null
}
 */

	private int idStripeCharge;
	private String idExterne;
	private TaReglement taReglement;
	
	private TaStripeSource taStripeSource; 
	private TaStripeCard taStripeCard;
	private TaStripeBankAccount taStripeBankAccount;
	private Boolean refunded = false;
	private Boolean paid = false;
	private Boolean captured = false;
	private TaStripeInvoice taStripeInvoice;
	private TaStripeCustomer taStripeCustomer;
	private TaStripePaymentIntent taStripePaymentIntent;
	private BigDecimal amount;
	private BigDecimal amountRefunded;
	private String description;
	private String status;
	private Date dateCharge;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_charge", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_charge",table = "ta_stripe_charge",champEntite="idStripeCharge", clazz = TaStripeCharge.class)
	public int getIdStripeCharge() {
		return idStripeCharge;
	}
	public void setIdStripeCharge(int idCharge) {
		this.idStripeCharge = idCharge;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_reglement")
	public TaReglement getTaReglement() {
		return taReglement;
	}
	public void setTaReglement(TaReglement taReglement) {
		this.taReglement = taReglement;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stripe_card")
	public TaStripeCard getTaStripeCard() {
		return taStripeCard;
	}
	public void setTaStripeCard(TaStripeCard taStripeCard) {
		this.taStripeCard = taStripeCard;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_stripe_bank_account")
	public TaStripeBankAccount getTaStripeBankAccount() {
		return taStripeBankAccount;
	}
	public void setTaStripeBankAccount(TaStripeBankAccount taStripeBankAccount) {
		this.taStripeBankAccount = taStripeBankAccount;
	}
	
	@Column(name = "refunded")
	public Boolean getRefunded() {
		return refunded;
	}
	public void setRefunded(Boolean refunded) {
		this.refunded = refunded;
	}
	
	@Column(name = "paid")
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
	@Column(name = "captured")
	public Boolean getCaptured() {
		return captured;
	}
	public void setCaptured(Boolean captured) {
		this.captured = captured;
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
	
	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column(name = "amount_refunded")
	public BigDecimal getAmountRefunded() {
		return amountRefunded;
	}
	public void setAmountRefunded(BigDecimal amountRefunded) {
		this.amountRefunded = amountRefunded;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_charge")
	public Date getDateCharge() {
		return dateCharge;
	}
	public void setDateCharge(Date dateCharge) {
		this.dateCharge = dateCharge;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_payment_intent")
	public TaStripePaymentIntent getTaStripePaymentIntent() {
		return taStripePaymentIntent;
	}
	public void setTaStripePaymentIntent(TaStripePaymentIntent taStripePaymentIntent) {
		this.taStripePaymentIntent = taStripePaymentIntent;
	}
}
