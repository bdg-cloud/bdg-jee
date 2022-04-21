package fr.legrain.abonnement.stripe.model;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/sources?lang=java

@Entity
@Table(name = "ta_stripe_source")
public class TaStripeSource implements java.io.Serializable {

	private static final long serialVersionUID = 823089870958819111L;
	
/*
com.stripe.model.Source JSON: {
  "id": "src_1DYydE2eZvKYlo2Cu5zFTIgX",
  "object": "source",
  "ach_credit_transfer": {
    "account_number": "test_52796e3294dc",
    "routing_number": "110000000",
    "fingerprint": "ecpwEzmBOSMOqQTL",
    "bank_name": "TEST BANK",
    "swift_code": "TSTEZ122"
  },
  "amount": null,
  "client_secret": "src_client_secret_E10ebGiD0ZBjrJOr98brNsUF",
  "created": 1542817972,
  "currency": "usd",
  "flow": "receiver",
  "livemode": false,
  "metadata": {
  },
  "owner": {
    "address": null,
    "email": "jenny.rosen@example.com",
    "name": null,
    "phone": null,
    "verified_address": null,
    "verified_email": null,
    "verified_name": null,
    "verified_phone": null
  },
  "receiver": {
    "address": "121042882-38381234567890123",
    "amount_charged": 0,
    "amount_received": 0,
    "amount_returned": 0,
    "refund_attributes_method": "email",
    "refund_attributes_status": "missing"
  },
  "statement_descriptor": null,
  "status": "pending",
  "type": "ach_credit_transfer",
  "usage": "reusable"
}
 */
	/*
	
	*/
	private int idStripeSource;
	private String idExterne;
	
	private TaStripeCustomer taStripeCustomer;
	private TaCompteBanque taCompteBanque;
	private TaCarteBancaire taCarteBancaire;
	private TaStripeTSource taStripeTSource;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_source", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_source",table = "ta_stripe_source",champEntite="idStripeSource", clazz = TaStripeSource.class)
	public int getIdStripeSource() {
		return idStripeSource;
	}

	public void setIdStripeSource(int idSource) {
		this.idStripeSource = idSource;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_stripe_customer")
	public TaStripeCustomer getTaStripeCustomer() {
		return taStripeCustomer;
	}

	public void setTaStripeCustomer(TaStripeCustomer taStripeCustomer) {
		this.taStripeCustomer = taStripeCustomer;
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
	@JoinColumn(name = "id_compte_banque")
	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}

	public void setTaCompteBanque(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_carte_bancaire")
	public TaCarteBancaire getTaCarteBancaire() {
		return taCarteBancaire;
	}

	public void setTaCarteBancaire(TaCarteBancaire taCarteBancaire) {
		this.taCarteBancaire = taCarteBancaire;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_stripe_t_source")
	@LgrHibernateValidated(champBd = "id_stripe_t_source",table = "ta_stripe_t_source",champEntite="idStripeTSource",clazz = TaTTiers.class)
	public TaStripeTSource getTaStripeTSource() {
		return taStripeTSource;
	}

	public void setTaStripeTSource(TaStripeTSource taStripeTSource) {
		this.taStripeTSource = taStripeTSource;
	}
	
}
