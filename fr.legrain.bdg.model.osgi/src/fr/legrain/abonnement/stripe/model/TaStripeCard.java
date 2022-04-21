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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/cards?lang=java
@Entity
@Table(name = "ta_stripe_card")
public class TaStripeCard implements java.io.Serializable {
/*
com.stripe.model.Card JSON: {
  "id": "card_1DYydC2eZvKYlo2CIqYqs4zb",
  "object": "card",
  "address_city": null,
  "address_country": null,
  "address_line1": null,
  "address_line1_check": null,
  "address_line2": null,
  "address_state": null,
  "address_zip": null,
  "address_zip_check": null,
  "brand": "Visa",
  "country": "US",
  "customer": null,
  "cvc_check": null,
  "dynamic_last4": null,
  "exp_month": 8,
  "exp_year": 2019,
  "fingerprint": "Xt5EWLLDS7FJjR1c",
  "funding": "credit",
  "last4": "4242",
  "metadata": {
  },
  "name": null,
  "tokenization_method": null
}
 */
	private int idStripeCard;
	private String idExterne;
	private TaCarteBancaire taCarteBancaire;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_card", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_card",table = "ta_stripe_card",champEntite="idStripeCard", clazz = TaStripeCard.class)
	public int getIdStripeCard() {
		return idStripeCard;
	}
	public void setIdStripeCard(int idStripeCard) {
		this.idStripeCard = idStripeCard;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_carte_bancaire")
	public TaCarteBancaire geTaCarteBancaire() {
		return taCarteBancaire;
	}
	public void setTaCarteBancaire(TaCarteBancaire taCarteBancaire) {
		this.taCarteBancaire = taCarteBancaire;
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
