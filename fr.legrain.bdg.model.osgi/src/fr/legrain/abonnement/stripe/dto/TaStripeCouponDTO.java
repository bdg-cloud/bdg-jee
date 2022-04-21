package fr.legrain.abonnement.stripe.dto;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;

public class TaStripeCouponDTO extends ModelObject implements java.io.Serializable {

	private static final long serialVersionUID = 4874497258621637052L;
	
	private Integer id;
	
	private String idExterne;
	private BigDecimal amountOff;
	private BigDecimal percentOff;
	private String currency;
	private String duration; //forever, once, and repeating
	private Integer durationInMonths;
	private String name;
	private Boolean valid;
	private Date redeemBy;
	
	private Integer versionObj;
	
	public TaStripeCouponDTO() {
	}
	
	public TaStripeCouponDTO(Integer id) {
		this.id = id;

	}
	
	public static TaStripeCouponDTO copy(TaStripeCouponDTO taEntrepot){
		TaStripeCouponDTO taEntrepotLoc = new TaStripeCouponDTO();
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

	public BigDecimal getAmountOff() {
		return amountOff;
	}

	public void setAmountOff(BigDecimal amountOff) {
		this.amountOff = amountOff;
	}

	public BigDecimal getPercentOff() {
		return percentOff;
	}

	public void setPercentOff(BigDecimal percentOff) {
		this.percentOff = percentOff;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Integer getDurationInMonths() {
		return durationInMonths;
	}

	public void setDurationInMonths(Integer durationInMonths) {
		this.durationInMonths = durationInMonths;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Date getRedeemBy() {
		return redeemBy;
	}

	public void setRedeemBy(Date redeemBy) {
		this.redeemBy = redeemBy;
	}

}
