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

import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaReglement;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/invoices/create?lang=java

@Entity
@Table(name = "ta_stripe_refund")
public class TaStripeRefund implements java.io.Serializable {

	private static final long serialVersionUID = 986815122352108464L;
/*
{
  "id": "re_1DxzOZESo4ojJRjqCy9ctkBe",
  "object": "refund",
  "amount": 100,
  "balance_transaction": null,
  "charge": "ch_1Clv6EESo4ojJRjqSeBKvQa3",
  "created": 1548779107,
  "currency": "eur",
  "metadata": {
  },
  "reason": null,
  "receipt_number": null,
  "source_transfer_reversal": null,
  "status": "succeeded",
  "transfer_reversal": null
}
 */
	private int idStripeRefund;
	private String idExterne;
	
	private TaStripeCharge taStripeCharge;
	private BigDecimal amount;
	private String status;
	private String reason;
	private String failureReason;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_invoice", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_invoice",table = "ta_stripe_invoice",champEntite="idStripeInvoice", clazz = TaStripeRefund.class)
	public int getIdStripeRefund() {
		return idStripeRefund;
	}
	public void setIdStripeRefund(int idStripeInvoice) {
		this.idStripeRefund = idStripeInvoice;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "reason")
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name = "failure_reason")
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
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
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_charge")
	public TaStripeCharge getTaStripeCharge() {
		return taStripeCharge;
	}
	public void setTaStripeCharge(TaStripeCharge taStripeCharge) {
		this.taStripeCharge = taStripeCharge;
	}
	
}
