package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeRefundDTO extends ModelObject implements java.io.Serializable {

	


	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	private String idExterne;
	private Integer idStripeCharge;
	private String idExterneCharge;
	private BigDecimal amount;
	private String status;
	private String reason;
	private String failureReason;
	
	private Integer versionObj;
	
	public TaStripeRefundDTO() {
	}
	
	
	public static TaStripeRefundDTO copy(TaStripeRefundDTO taEntrepot){
		TaStripeRefundDTO taEntrepotLoc = new TaStripeRefundDTO();
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


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getFailureReason() {
		return failureReason;
	}


	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

}
