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

import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.validator.LgrHibernateValidated;

//https://stripe.com/docs/api/accounts?lang=java

@Entity
@Table(name = "ta_stripe_bank_account")
public class TaStripeBankAccount implements java.io.Serializable {

	private static final long serialVersionUID = -8487912984925259337L;
	
/*
com.stripe.model.BankAccount JSON: {
  "id": "ba_1DYydE2eZvKYlo2Ccwy1R8b0",
  "object": "bank_account",
  "account_holder_name": "Jane Austen",
  "account_holder_type": "individual",
  "bank_name": "STRIPE TEST BANK",
  "country": "US",
  "currency": "usd",
  "customer": null,
  "fingerprint": "1JWtPxqbdX5Gamtc",
  "last4": "6789",
  "metadata": {
  },
  "routing_number": "110000000",
  "status": "new"
}
 */
	
	private int idStripeBankAccount;
	private String idExterne;
	private TaCompteBanque taCompteBanque;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_stripe_bank_account", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_stripe_bank_account",table = "ta_stripe_bank_account",champEntite="idStripeBankAccount", clazz = TaStripeBankAccount.class)
	public int getIdStripeBankAccount() {
		return idStripeBankAccount;
	}
	public void setIdStripeBankAccount(int idBankAccount) {
		this.idStripeBankAccount = idBankAccount;
	}
	
	@Column(name = "id_externe")
	public String getIdExterne() {
		return idExterne;
	}
	public void setIdExterne(String idExterne) {
		this.idExterne = idExterne;
	}
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY /*, mappedBy = "taDocument", orphanRemoval=true*/)
	@JoinColumn(name = "id_compte_banque")
	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}
	public void setTaCompteBanque(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
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
