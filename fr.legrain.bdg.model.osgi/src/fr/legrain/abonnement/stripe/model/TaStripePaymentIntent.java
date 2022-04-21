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

import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/charges?lang=java

@Entity
@Table(name = "ta_stripe_payment_intent")
public class TaStripePaymentIntent implements java.io.Serializable {

	private static final long serialVersionUID = 555543373235689541L;
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

	private int idStripePaymentIntent;
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
	private BigDecimal amount;
	private BigDecimal amountRefunded;
	private BigDecimal amountCapturable;
	private BigDecimal amountReceived;
	private String description;
	private String status;
	private String idExternePaymentMethod;
	private String methodeValidation; //validation manuelle ou par webhook
	private Date dateIntent;
	private String emailTicket;
	private Boolean conserverCarte = false;
	
	private TaFacture taFacture;
	private TaTicketDeCaisse taTicketDeCaisse;
	private TaAvisEcheance taAvisEcheance;
	private TaPanier taPanier;
	private TaTiers taTiers;
	private TaTPaiement taTPaiement;
	private BigDecimal netTtcCalc;
	private Boolean compteClient;
	
	private String captureMethod;
	private BigDecimal amountToCapture;
	private Date canceledAt;
	private String cancellationReason;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_payment_intent", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_payment_intent",table = "ta_stripe_payment_intent",champEntite="idStripePaymentIntent", clazz = TaStripePaymentIntent.class)
	public int getIdStripePaymentIntent() {
		return idStripePaymentIntent;
	}
	public void setIdStripePaymentIntent(int idCharge) {
		this.idStripePaymentIntent = idCharge;
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
	
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "date_charge")
//	public Date getDateCharge() {
//		return dateCharge;
//	}
//	public void setDateCharge(Date dateCharge) {
//		this.dateCharge = dateCharge;
//	}
	
	@Column(name = "amount_capturable")
	public BigDecimal getAmountCapturable() {
		return amountCapturable;
	}
	public void setAmountCapturable(BigDecimal amountCapturable) {
		this.amountCapturable = amountCapturable;
	}
	
	@Column(name = "amount_received")
	public BigDecimal getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}
	
	@Column(name = "id_externe_payment_method")
	public String getIdExternePaymentMethod() {
		return idExternePaymentMethod;
	}
	public void setIdExternePaymentMethod(String idExternePaymentMethod) {
		this.idExternePaymentMethod = idExternePaymentMethod;
	}
	
	@Column(name = "methode_validation")
	public String getMethodeValidation() {
		return methodeValidation;
	}
	public void setMethodeValidation(String methodeValidation) {
		this.methodeValidation = methodeValidation;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_facture")
	public TaFacture getTaFacture() {
		return taFacture;
	}
	public void setTaFacture(TaFacture id_facture) {
		this.taFacture = id_facture;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_ticket_caisse")
	public TaTicketDeCaisse getTaTicketDeCaisse() {
		return taTicketDeCaisse;
	}
	public void setTaTicketDeCaisse(TaTicketDeCaisse id_ticket_caisse) {
		this.taTicketDeCaisse = id_ticket_caisse;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_t_paiement")
	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}
	public void setTaTPaiement(TaTPaiement id_t_paiement) {
		this.taTPaiement = id_t_paiement;
	}
	
	@Column(name = "net_ttc_calc")
	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}
	public void setNetTtcCalc(BigDecimal net_ttc_calc) {
		this.netTtcCalc = net_ttc_calc;
	}
	
	@Column(name = "compte_client")
	public Boolean getCompteClient() {
		return compteClient;
	}
	public void setCompteClient(Boolean compte_client) {
		this.compteClient = compte_client;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tiers")
	public TaTiers getTaTiers() {
		return taTiers;
	}
	public void setTaTiers(TaTiers id_tiers) {
		this.taTiers = id_tiers;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_intent", length = 19)
	public Date getDateIntent() {
		return dateIntent;
	}
	public void setDateIntent(Date date_intent) {
		this.dateIntent = date_intent;
	}
	
	@Column(name = "email_ticket")
	public String getEmailTicket() {
		return emailTicket;
	}
	public void setEmailTicket(String email_ticket) {
		this.emailTicket = email_ticket;
	}
	
	@Column(name = "conserver_carte")
	public Boolean getConserverCarte() {
		return conserverCarte;
	}
	public void setConserverCarte(Boolean conserver_carte) {
		this.conserverCarte = conserver_carte;
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
	@JoinColumn(name = "id_panier")
	public TaPanier getTaPanier() {
		return taPanier;
	}
	public void setTaPanier(TaPanier taPanier) {
		this.taPanier = taPanier;
	}
	
	@Column(name = "capture_method")
	public String getCaptureMethod() {
		return captureMethod;
	}
		public void setCaptureMethod(String capture_method) {
		this.captureMethod = capture_method;
	}
		
	@Column(name = "amount_to_capture")
	public BigDecimal getAmountToCapture() {
		return amountToCapture;
	}
	public void setAmountToCapture(BigDecimal amount_to_capture) {
		this.amountToCapture = amount_to_capture;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "canceled_at")
	public Date getCanceledAt() {
		return canceledAt;
	}
	public void setCanceledAt(Date canceled_at) {
		this.canceledAt = canceled_at;
	}
	
	@Column(name = "cancellation_reason")
	public String getCancellationReason() {
		return cancellationReason;
	}
	public void setCancellationReason(String cancellation_reason) {
		this.cancellationReason = cancellation_reason;
	}
}
