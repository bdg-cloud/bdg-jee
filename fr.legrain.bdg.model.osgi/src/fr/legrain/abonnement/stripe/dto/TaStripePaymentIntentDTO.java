package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaStripePaymentIntentDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	
	private Integer idDocument;
	private String codeDocument;
	
	private Integer idStripeSource;
	private String idExterneSource;
	private Integer idStripeCard;
	private String idExterneCard;
	private Integer idStripeBankAccount;
	private String idExterneBankAccount;
	private Boolean refunded = false;
	private Boolean paid = false;
	private Boolean captured = false;
	private Integer idStripeInvoice;
	private String idExterneInvoice;
	private Integer idStripeCustomer;
	private String idExterneCustomer;
	private BigDecimal amount;
	private BigDecimal amountRefunded;
	private String description;
	private String status;
	private Date dateCharge;
	private Date dateReglement;
	
	private String captureMethod;
	private BigDecimal amountToCapture;
	private Date canceledAt;
	private String cancellationReason;
	
	private Integer versionObj;
	
	public TaStripePaymentIntentDTO() {
	}
	
	
	public TaStripePaymentIntentDTO(Integer id, String idExterne, 
			Integer idDocument, String codeDocument,
			Integer idStripeSource,
			String idExterneSource, Integer idStripeCard, String idExterneCard, Integer idStripeBankAccount,
			String idExterneBankAccount, Boolean refunded, Boolean paid, Boolean captured, Integer idStripeInvoice,
			String idExterneInvoice, Integer idStripeCustomer, String idExterneCustomer, BigDecimal amount,
			BigDecimal amountRefunded, String description, String status, Date dateCharge, Date dateReglement, Integer versionObj) {
		super();
		this.id = id;
		this.idExterne = idExterne;
		this.idDocument = idDocument;
		this.codeDocument = codeDocument;
		this.idStripeSource = idStripeSource;
		this.idExterneSource = idExterneSource;
		this.idStripeCard = idStripeCard;
		this.idExterneCard = idExterneCard;
		this.idStripeBankAccount = idStripeBankAccount;
		this.idExterneBankAccount = idExterneBankAccount;
		this.refunded = refunded;
		this.paid = paid;
		this.captured = captured;
		this.idStripeInvoice = idStripeInvoice;
		this.idExterneInvoice = idExterneInvoice;
		this.idStripeCustomer = idStripeCustomer;
		this.idExterneCustomer = idExterneCustomer;
		this.amount = amount;
		this.amountRefunded = amountRefunded;
		this.description = description;
		this.status = status;
		this.dateCharge = dateCharge;
		this.dateReglement = dateReglement;
		this.versionObj = versionObj;
	}


	public static TaStripePaymentIntentDTO copy(TaStripePaymentIntentDTO taEntrepot){
		TaStripePaymentIntentDTO taEntrepotLoc = new TaStripePaymentIntentDTO();
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

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}


	public int getIdStripeSource() {
		return idStripeSource;
	}


	public void setIdStripeSource(int idStripeSource) {
		this.idStripeSource = idStripeSource;
	}


	public String getIdExterneSource() {
		return idExterneSource;
	}


	public void setIdExterneSource(String idExterneSource) {
		this.idExterneSource = idExterneSource;
	}


	public int getIdStripeCard() {
		return idStripeCard;
	}


	public void setIdStripeCard(int idStripeCard) {
		this.idStripeCard = idStripeCard;
	}


	public String getIdExterneCard() {
		return idExterneCard;
	}


	public void setIdExterneCard(String idExterneCard) {
		this.idExterneCard = idExterneCard;
	}


	public int getIdStripeBankAccount() {
		return idStripeBankAccount;
	}


	public void setIdStripeBankAccount(int idStripeBankAccount) {
		this.idStripeBankAccount = idStripeBankAccount;
	}


	public String getIdExterneBankAccount() {
		return idExterneBankAccount;
	}


	public void setIdExterneBankAccount(String idExterneBankAccount) {
		this.idExterneBankAccount = idExterneBankAccount;
	}


	public Boolean getRefunded() {
		return refunded;
	}


	public void setRefunded(Boolean refunded) {
		this.refunded = refunded;
	}


	public Boolean getPaid() {
		return paid;
	}


	public void setPaid(Boolean paid) {
		this.paid = paid;
	}


	public Boolean getCaptured() {
		return captured;
	}


	public void setCaptured(Boolean captured) {
		this.captured = captured;
	}


	public int getIdStripeInvoice() {
		return idStripeInvoice;
	}


	public void setIdStripeInvoice(int idStripeInvoice) {
		this.idStripeInvoice = idStripeInvoice;
	}


	public String getIdExterneInvoice() {
		return idExterneInvoice;
	}


	public void setIdExterneInvoice(String idExterneInvoice) {
		this.idExterneInvoice = idExterneInvoice;
	}


	public int getIdStripeCustomer() {
		return idStripeCustomer;
	}


	public void setIdStripeCustomer(int idStripeCustomer) {
		this.idStripeCustomer = idStripeCustomer;
	}


	public String getIdExterneCustomer() {
		return idExterneCustomer;
	}


	public void setIdExterneCustomer(String idExterneCustomer) {
		this.idExterneCustomer = idExterneCustomer;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public BigDecimal getAmountRefunded() {
		return amountRefunded;
	}


	public void setAmountRefunded(BigDecimal amountRefunded) {
		this.amountRefunded = amountRefunded;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void setIdStripeSource(Integer idStripeSource) {
		this.idStripeSource = idStripeSource;
	}


	public void setIdStripeCard(Integer idStripeCard) {
		this.idStripeCard = idStripeCard;
	}


	public void setIdStripeBankAccount(Integer idStripeBankAccount) {
		this.idStripeBankAccount = idStripeBankAccount;
	}


	public void setIdStripeInvoice(Integer idStripeInvoice) {
		this.idStripeInvoice = idStripeInvoice;
	}


	public void setIdStripeCustomer(Integer idStripeCustomer) {
		this.idStripeCustomer = idStripeCustomer;
	}


	public Date getDateCharge() {
		return dateCharge;
	}


	public void setDateCharge(Date dateCharge) {
		this.dateCharge = dateCharge;
	}


	public Date getDateReglement() {
		return dateReglement;
	}


	public void setDateReglement(Date dateReglement) {
		this.dateReglement = dateReglement;
	}


	public String getCaptureMethod() {
		return captureMethod;
	}


	public void setCaptureMethod(String captureMethod) {
		this.captureMethod = captureMethod;
	}


	public BigDecimal getAmountToCapture() {
		return amountToCapture;
	}


	public void setAmountToCapture(BigDecimal amountToCapture) {
		this.amountToCapture = amountToCapture;
	}


	public Date getCanceledAt() {
		return canceledAt;
	}


	public void setCanceledAt(Date canceledAt) {
		this.canceledAt = canceledAt;
	}


	public String getCancellationReason() {
		return cancellationReason;
	}


	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

}
