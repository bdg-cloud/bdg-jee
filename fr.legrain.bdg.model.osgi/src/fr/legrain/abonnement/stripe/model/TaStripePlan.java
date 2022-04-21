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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.article.model.TaArticle;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/plans?lang=java

@Entity
@Table(name = "ta_stripe_plan")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaStripePlan.QN.FIND_BY_PRODUCT, query="select a from TaStripePlan a where a.taStripeProduct.id = :idStripeProduct"),
//		@NamedQuery(name=TaStripePlan.QN.FIND_BY_PRODUCT_LIGHT, query="select new fr.legrain.abonnement.stripe.dto.TaStripePlanDTO("
//				+ "a.idStripePlan, a.idExterne, a.taArticle.idArticle, a.taArticle.codeArticle, a.taStripeProduct.idStripeProduct, a.taStripeProduct.idExterne "
//				+ "a.nickname, a.interval, a.intervalCount, a.amount, a.currency, a.active, a.trialPeriodDays, a.versionObj) "
//				+ "from TaStripePlan a where a.taStripeProduct.id = :idStripeProduct")
//})
public class TaStripePlan implements java.io.Serializable {

	private static final long serialVersionUID = -4425329493039893069L;
	
//	public static class QN {
//		public static final String FIND_BY_PRODUCT = "TaStripePlan.findByProduct";
//		public static final String FIND_BY_PRODUCT_LIGHT = "TaStripePlan.findByProductLight";
//	}
	
	/*
com.stripe.model.Plan JSON: {
  "id": "emerald-company-234",
  "object": "plan",
  "active": true,
  "aggregate_usage": null,
  "amount": 999,
  "billing_scheme": "per_unit",
  "created": 1506408191,
  "currency": "usd",
  "interval": "month",
  "interval_count": 1,
  "livemode": false,
  "metadata": {
  },
  "nickname": null,
  "product": "prod_BT942zL7VcClrn",
  "tiers": null,
  "tiers_mode": null,
  "transform_usage": null,
  "trial_period_days": null,
  "usage_type": "licensed"
}
	 */

	private int idStripePlan;
	private String idExterne;
	private TaArticle taArticle;
	private TaStripeProduct taStripeProduct;
	private String nickname;
	private String interval; //day, week, month or year
	private int intervalCount;
	private String currency;
	private Boolean active;
	private BigDecimal amount;
	private Integer trialPrediodDays;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	private TaFrequence taFrequence;
	
	private Integer nbMois;
	
//	id_stripe_plan serial NOT NULL,
//	  id_externe dlib255,
//	  nickname dlib255,
//	  interval dlib255,
//	  intervalCount integer,
//	  currency dlib100,
//	  active boolean,
//	  trialPrediodDays integer,
//	  id_article did_facultatif,
//	  id_stripe_product did_facultatif,
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_plan", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_plan",table = "ta_stripe_plan",champEntite="idStripePlan", clazz = TaStripePlan.class)
	public int getIdStripePlan() {
		return idStripePlan;
	}

	public void setIdStripePlan(int idPlan) {
		this.idStripePlan = idPlan;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}

	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_article")
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_product")
	public TaStripeProduct getTaStripeProduct() {
		return taStripeProduct;
	}

	public void setTaStripeProduct(TaStripeProduct taProduct) {
		this.taStripeProduct = taProduct;
	}

	@Column(name = "nickname")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "interval")
	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	@Column(name = "interval_count")
	public int getIntervalCount() {
		return intervalCount;
	}

	public void setIntervalCount(int intervalCount) {
		this.intervalCount = intervalCount;
	}

	@Column(name = "currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Column(name = "trial_prediod_days")
	public Integer getTrialPrediodDays() {
		return trialPrediodDays;
	}

	public void setTrialPrediodDays(Integer trialPrediodDays) {
		this.trialPrediodDays = trialPrediodDays;
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

	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_frequence")
	public TaFrequence getTaFrequence() {
		return taFrequence;
	}

	public void setTaFrequence(TaFrequence taFrequence) {
		this.taFrequence = taFrequence;
	}
	@Column(name = "nb_mois")
	public Integer getNbMois() {
		return nbMois;
	}

	public void setNbMois(Integer nbMois) {
		this.nbMois = nbMois;
	}

}
