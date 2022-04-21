package fr.legrain.abonnement.stripe.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.document.model.TaAbonnement;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/customers?lang=java

@Entity
@Table(name = "ta_stripe_customer")
public class TaStripeCustomer implements java.io.Serializable {

	private static final long serialVersionUID = -1823566304170561340L;
	
/*
com.stripe.model.Customer JSON: {
  "id": "cus_E10dOLo0U8K1Ne",
  "object": "customer",
  "account_balance": 0,
  "created": 1542817885,
  "currency": "usd",
  "default_source": null,
  "delinquent": false,
  "description": null,
  "discount": null,
  "email": null,
  "invoice_prefix": "0883DC6",
  "livemode": false,
  "metadata": {
  },
  "shipping": null,
  "sources": {
    "object": "list",
    "data": [

    ],
    "has_more": false,
    "total_count": 0,
    "url": "/v1/customers/cus_E10dOLo0U8K1Ne/sources"
  },
  "subscriptions": {
    "object": "list",
    "data": [

    ],
    "has_more": false,
    "total_count": 0,
    "url": "/v1/customers/cus_E10dOLo0U8K1Ne/subscriptions"
  },
  "tax_info": null,
  "tax_info_verification": null
}
 */
	
	private int idStripeCustomer;
	private String idExterne;
	private TaTiers taTiers;
	private String currency;
	private TaStripeDiscount taDiscount;
	private TaStripeSource taSourceDefault;
	private Set<TaStripeSource> sources;
	private String description;
	private String email;
	//private Set<TaStripeSubscription> subscriptions;
	private Set<TaAbonnement> subscriptions;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
//	CREATE TABLE ta_stripe_customer (
//			  id_stripe_customer serial NOT NULL,
//			  description dlib255,
//			  email dlib255,
//			  
//			  id_tiers did_facultatif,
//			  id_discount did_facultatif,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_customer", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_customer",table = "ta_stripe_customer",champEntite="idStripePlan", clazz = TaStripeCustomer.class)
	public int getIdStripeCustomer() {
		return idStripeCustomer;
	}

	public void setIdStripeCustomer(int idCustomer) {
		this.idStripeCustomer = idCustomer;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}

	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_tiers")
	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@Column(name = "currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_stripe_discount")
	public TaStripeDiscount getTaDiscount() {
		return taDiscount;
	}

	public void setTaDiscount(TaStripeDiscount taDiscount) {
		this.taDiscount = taDiscount;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.REFRESH} , orphanRemoval=true)
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL , mappedBy="taTiers", orphanRemoval=true)
	@JoinColumn(name = "id_stripe_source")
	public TaStripeSource getTaSourceDefault() {
		return taSourceDefault;
	}

	public void setTaSourceDefault(TaStripeSource taSourceDefault) {
		this.taSourceDefault = taSourceDefault;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taStripeCustomer", orphanRemoval=true)
	public Set<TaStripeSource> getSources() {
		return sources;
	}

	public void setSources(Set<TaStripeSource> sources) {
		this.sources = sources;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taStripeCustomer", orphanRemoval=true)
	public Set<TaAbonnement> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<TaAbonnement> subscriptions) {
		this.subscriptions = subscriptions;
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
