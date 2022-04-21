package fr.legrain.abonnement.stripe.model;

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

//https://stripe.com/docs/api/customer_bank_accounts?lang=java

@Entity
@Table(name = "ta_stripe_account")
public class TaStripeAccount implements java.io.Serializable {

	private static final long serialVersionUID = 4791415109271756893L;
	
	public static final String STRIPE_CONNECT_INTEGRATION_OAUTH = "OAUTH";
	public static final String STRIPE_CONNECT_INTEGRATION_API = "API";
	
	public static final String STRIPE_CONNECT_TYPE_COMPTE_STANDARD = "standard";
	public static final String STRIPE_CONNECT_TYPE_COMPTE_EXPRESS = "express";
	public static final String STRIPE_CONNECT_TYPE_COMPTE_CUSTOM = "custom";
/*
com.stripe.model.Account JSON: {
  "id": "acct_1032D82eZvKYlo2C",
  "object": "account",
  "business_name": "Stripe.com",
  "business_url": null,
  "charges_enabled": false,
  "country": "US",
  "created": 1385798567,
  "debit_negative_balances": true,
  "decline_charge_on": {
    "avs_failure": true,
    "cvc_failure": false
  },
  "default_currency": "usd",
  "details_submitted": false,
  "display_name": "Stripe.com",
  "email": "site@stripe.com",
  "external_accounts": {
    "object": "list",
    "data": [

    ],
    "has_more": false,
    "total_count": 0,
    "url": "/v1/accounts/acct_1032D82eZvKYlo2C/external_accounts"
  },
  "legal_entity": {
    "additional_owners": [

    ],
    "address": {
      "city": null,
      "country": "US",
      "line1": null,
      "line2": null,
      "postal_code": null,
      "state": null
    },
    "business_name": null,
    "business_tax_id_provided": false,
    "dob": {
      "day": null,
      "month": null,
      "year": null
    },
    "first_name": null,
    "last_name": null,
    "personal_address": {
      "city": null,
      "country": "US",
      "line1": null,
      "line2": null,
      "postal_code": null,
      "state": null
    },
    "personal_id_number_provided": false,
    "ssn_last_4_provided": false,
    "type": null,
    "verification": {
      "details": null,
      "details_code": "failed_other",
      "document": null,
      "document_back": null,
      "status": "unverified"
    }
  },
  "metadata": {
  },
  "payout_schedule": {
    "delay_days": 7,
    "interval": "daily"
  },
  "payout_statement_descriptor": null,
  "payouts_enabled": false,
  "product_description": null,
  "statement_descriptor": null,
  "support_email": null,
  "support_phone": null,
  "timezone": "US/Pacific",
  "tos_acceptance": {
    "date": null,
    "ip": null,
    "user_agent": null
  },
  "type": "custom",
  "verification": {
    "disabled_reason": "fields_needed",
    "due_by": null,
    "fields_needed": [
      "business_url",
      "external_account",
      "legal_entity.address.city",
      "legal_entity.address.line1",
      "legal_entity.address.postal_code",
      "legal_entity.address.state",
      "legal_entity.dob.day",
      "legal_entity.dob.month",
      "legal_entity.dob.year",
      "legal_entity.first_name",
      "legal_entity.last_name",
      "legal_entity.type",
      "product_description",
      "support_phone",
      "tos_acceptance.date",
      "tos_acceptance.ip"
    ]
  }
}
 */
	
	private int idStripeAccount;
	private String idExterne;
	
	private String businessType;
	private String country;
	private String email;
	private String name;
	private String type;
	private String defaultCurrency;
	private Boolean detailsSubmitted;
	private Boolean chargesEnabled;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	public void effacerInfosCompte() {
		setIdExterne(null);
		setEmail(null);
		setChargesEnabled(null);
		setDetailsSubmitted(null);
		setCountry(null);
		setBusinessType(null);
		setDefaultCurrency(null);
		setType(null);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_account", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_account",table = "ta_stripe_account",champEntite="idStripeAccount", clazz = TaStripeAccount.class)
	public int getIdStripeAccount() {
		return idStripeAccount;
	}
	public void setIdStripeAccount(int idStripeAccount) {
		this.idStripeAccount = idStripeAccount;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
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
	
	@Column(name = "business_type")
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String business_type) {
		this.businessType = business_type;
	}
	
	@Column(name = "country")
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "account_type")
	public String getType() {
		return type;
	}
	public void setType(String account_type) {
		this.type = account_type;
	}
	
	@Column(name = "default_currency")
	public String getDefaultCurrency() {
		return defaultCurrency;
	}
	public void setDefaultCurrency(String default_currency) {
		this.defaultCurrency = default_currency;
	}
	
	@Column(name = "details_submitted")
	public Boolean getDetailsSubmitted() {
		return detailsSubmitted;
	}
	public void setDetailsSubmitted(Boolean details_submitted) {
		this.detailsSubmitted = details_submitted;
	}
	
	@Column(name = "charges_enabled")
	public Boolean getChargesEnabled() {
		return chargesEnabled;
	}
	public void setChargesEnabled(Boolean charges_enabled) {
		this.chargesEnabled = charges_enabled;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
