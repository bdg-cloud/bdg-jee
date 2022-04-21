package fr.legrain.abonnement.stripe.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/coupons/create?lang=java

@Entity
@Table(name = "ta_stripe_coupon")
public class TaStripeCoupon implements java.io.Serializable {

	private static final long serialVersionUID = -8524523873616534186L;
	
/*
com.stripe.model.Coupon JSON: {
  "id": "$1OFF",
  "object": "coupon",
  "amount_off": 100,
  "created": 1433453599,
  "currency": "usd",
  "duration": "forever",
  "duration_in_months": null,
  "livemode": false,
  "max_redemptions": null,
  "metadata": {
  },
  "name": null,
  "percent_off": null,
  "redeem_by": null,
  "times_redeemed": 0,
  "valid": true
}
 */
	
	
	private int idStripeCoupon;
	private String idExterne;
	private BigDecimal amountOff;
	private BigDecimal percentOff;
	private String currency;
	private String duration; //forever, once, and repeating
	private Integer durationInMonths;
	private String name;
	private Boolean valid;
	private Date redeemBy;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_coupon", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_coupon",table = "ta_stripe_coupon",champEntite="idStripeCoupon", clazz = TaStripeCoupon.class)
	public int getIdStripeCoupon() {
		return idStripeCoupon;
	}
	public void setIdStripeCoupon(int idCoupon) {
		this.idStripeCoupon = idCoupon;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@Column(name = "amount_off")
	public BigDecimal getAmountOff() {
		return amountOff;
	}
	public void setAmountOff(BigDecimal amountOff) {
		this.amountOff = amountOff;
	}
	
	@Column(name = "percent_off")
	public BigDecimal getPercentOff() {
		return percentOff;
	}
	public void setPercentOff(BigDecimal percentOff) {
		this.percentOff = percentOff;
	}
	
	@Column(name = "currency")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Column(name = "duration")
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@Column(name = "duration_in_months")
	public Integer getDurationInMonths() {
		return durationInMonths;
	}
	public void setDurationInMonths(Integer durationInMonths) {
		this.durationInMonths = durationInMonths;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "valid")
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	@Column(name = "redeem_by")
	public Date getRedeemBy() {
		return redeemBy;
	}
	public void setRedeemBy(Date redeemBy) {
		this.redeemBy = redeemBy;
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
