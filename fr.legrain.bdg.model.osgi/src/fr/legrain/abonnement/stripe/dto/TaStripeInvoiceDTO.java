package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaReglement;

public class TaStripeInvoiceDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private Integer idStripeCustomer;
	private String idExterneCustomer;
	private Integer idAbonnement;
	private String idExterneSubscription;
	
	private BigDecimal amountDue;
	private String number;
	private Date dueDate;
	private Date created;
	private Integer idStripeCharge;
	private String idExterneCharge;
	private Boolean paid;
	private Date periodStart;
	private Date periodEnd;
	private Integer idStripeCoupon;
	private String idExterneCoupon;
	private BigDecimal taxe;
	private Integer idAvisEcheance;
	private String codeAvisEcheance;
	private Integer idReglement;
	private String codeReglement;
	private String status;
	
	private Date webhooksDeliveredAt;
	private Date nexPaymentAttempt;
	private String description;
	private String billingReason;
	private String billing;
	private Boolean attempted; 
	private Integer attemptCount;
	
	private Integer versionObj;
	
	public TaStripeInvoiceDTO() {
	}
	
	public TaStripeInvoiceDTO(Integer id, String idExterne, Integer idStripeCustomer, String idExterneCustomer,
			Integer idAbonnement, String idExterneSubscription,
			BigDecimal amountDue, String number, Date dueDate, Date created, Boolean paid, Date periodStart, Date periodEnd,
			Date webhooksDeliveredAt, Date nexPaymentAttempt, String description, String billingReason, String billing, Boolean attempted, Integer attemptCount,
			Integer idAvisEcheance, String codeAvisEcheance, Integer idReglement, String codeReglement,
			//String status,
			Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idStripeCustomer = idStripeCustomer;
		this.idExterneCustomer = idExterneCustomer;
		this.idAbonnement = idAbonnement;
		this.idExterneSubscription = idExterneSubscription;
		this.amountDue = amountDue;
		this.number = number;
		this.dueDate = dueDate;
		this.created = created;
		this.paid = paid;
		this.periodStart = periodStart;
		this.periodEnd = periodEnd;
		this.webhooksDeliveredAt = webhooksDeliveredAt;
		this.nexPaymentAttempt = nexPaymentAttempt;
		this.description = description;
		this.billingReason = billingReason; 
		this.billing = billing;
		this.attempted = attempted; 
		this.attemptCount = attemptCount;
		this.idAvisEcheance = idAvisEcheance;
		this.codeAvisEcheance = codeAvisEcheance;
		this.idReglement = idReglement;
		this.codeReglement = codeReglement;
		//this.status = status;
		this.versionObj = versionObj;
	}

	public static TaStripeInvoiceDTO copy(TaStripeInvoiceDTO taEntrepot){
		TaStripeInvoiceDTO taEntrepotLoc = new TaStripeInvoiceDTO();
		taEntrepotLoc.setId(taEntrepot.id);
		
		return taEntrepotLoc;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getIdExterne() {
		return idExterne;
	}

	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	public Integer getIdStripeCustomer() {
		return idStripeCustomer;
	}

	public void setIdStripeCustomer(Integer idStripeCustomer) {
		this.idStripeCustomer = idStripeCustomer;
	}

	public String getIdExterneCustomer() {
		return idExterneCustomer;
	}

	public void setIdExterneCustomer(String idExterneCustomer) {
		this.idExterneCustomer = idExterneCustomer;
	}

	public Integer getIdAbonnement() {
		return idAbonnement;
	}

	public void setIdAbonnement(Integer idAbonnement) {
		this.idAbonnement = idAbonnement;
	}

	public String getIdExterneSubscription() {
		return idExterneSubscription;
	}

	public void setIdExterneSubscription(String idExterneSubscription) {
		this.idExterneSubscription = idExterneSubscription;
	}

	public BigDecimal getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(BigDecimal amountDue) {
		this.amountDue = amountDue;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getIdStripeCharge() {
		return idStripeCharge;
	}

	public void setIdStripeCharge(Integer idStripeCharge) {
		this.idStripeCharge = idStripeCharge;
	}

	public String getIdExterneCharge() {
		return idExterneCharge;
	}

	public void setIdExterneCharge(String idExterneCharge) {
		this.idExterneCharge = idExterneCharge;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Date getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(Date periodStart) {
		this.periodStart = periodStart;
	}

	public Date getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(Date periodEnd) {
		this.periodEnd = periodEnd;
	}

	public Integer getIdStripeCoupon() {
		return idStripeCoupon;
	}

	public void setIdStripeCoupon(Integer idStripeCoupon) {
		this.idStripeCoupon = idStripeCoupon;
	}

	public String getIdExterneCoupon() {
		return idExterneCoupon;
	}

	public void setIdExterneCoupon(String idExterneCoupon) {
		this.idExterneCoupon = idExterneCoupon;
	}

	public BigDecimal getTaxe() {
		return taxe;
	}

	public void setTaxe(BigDecimal taxe) {
		this.taxe = taxe;
	}

	public Integer getIdAvisEcheance() {
		return idAvisEcheance;
	}

	public void setIdAvisEcheance(Integer idAvisEcheance) {
		this.idAvisEcheance = idAvisEcheance;
	}

	public String getCodeAvisEcheance() {
		return codeAvisEcheance;
	}

	public void setCodeAvisEcheance(String codeAvisEcheance) {
		this.codeAvisEcheance = codeAvisEcheance;
	}

	public Integer getIdReglement() {
		return idReglement;
	}

	public void setIdReglement(Integer idReglement) {
		this.idReglement = idReglement;
	}

	public String getCodeReglement() {
		return codeReglement;
	}

	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getWebhooksDeliveredAt() {
		return webhooksDeliveredAt;
	}

	public void setWebhooksDeliveredAt(Date webhooksDeliveredAt) {
		this.webhooksDeliveredAt = webhooksDeliveredAt;
	}

	public Date getNexPaymentAttempt() {
		return nexPaymentAttempt;
	}

	public void setNexPaymentAttempt(Date nexPaymentAttempt) {
		this.nexPaymentAttempt = nexPaymentAttempt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBillingReason() {
		return billingReason;
	}

	public void setBillingReason(String billingReason) {
		this.billingReason = billingReason;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public Boolean getAttempted() {
		return attempted;
	}

	public void setAttempted(Boolean attempted) {
		this.attempted = attempted;
	}

	public Integer getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}

}
