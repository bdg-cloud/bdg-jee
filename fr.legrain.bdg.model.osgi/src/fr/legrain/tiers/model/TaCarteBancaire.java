package fr.legrain.tiers.model;

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
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.tiers.model.TaTBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_carte_bancaire")
public class TaCarteBancaire extends TaObjetLgr  implements java.io.Serializable {

	private static final long serialVersionUID = -266502819375342649L;
	
	private int idCarteBancaire;
	
	private TaTiers taTiers;
	private TaTBanque taTBanque;
	private String nomBanque;
	
	
	private String nomProprietaire;// :No name provided
	private String numeroCarte; //Number :•••• 9363
	private String empreinte;//Fingerprint : 4SZKQRQv6IJpjeV7
	private int moisExpiration;//Expires : 2 / 2020
	private int anneeExpiration;
	private String type;//Type : MasterCard credit card
	private String paysOrigine;//Origine : France
	private String last4;
	private String cvc;//CVC : 777
	
	private String adresse1;
	private String adresse2;
	private String codePostal;
	private String ville;
	private String pays;
	

	private String nomCompte;
	private String cptcomptable;
	
//	private String version;

	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
	

	public TaCarteBancaire() {
	}

	public TaCarteBancaire(int idCarteBancaire, TaTiers taTiers) {
		this.idCarteBancaire = idCarteBancaire;
		this.taTiers = taTiers;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_carte_bancaire", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_carte_bancaire",table = "ta_carte_bancaire",champEntite="idCarteBancaire", clazz = TaCarteBancaire.class)
	public int getIdCarteBancaire() {
		return this.idCarteBancaire;
	}

	public void setIdCarteBancaire(int idCompteBanque) {
		this.idCarteBancaire = idCompteBanque;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

//	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_t_banque")
	@LgrHibernateValidated(champBd = "id_t_banque",table = "ta_t_banque",champEntite="idTBanque",clazz = TaTBanque.class)
	public TaTBanque getTaTBanque() {
		return this.taTBanque;
	}

	public void setTaTBanque(TaTBanque taTBanque) {
		this.taTBanque = taTBanque;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_tiers", nullable = false)
	@LgrHibernateValidated(champBd = "id_tiers",table = "ta_tiers",champEntite="idTiers",clazz = TaTiers.class)
	@XmlElement
	@XmlInverseReference(mappedBy="taCompteBanques")
	public TaTiers getTaTiers() {
		return this.taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@Column(name = "nom_banque", length = 20)
	@LgrHibernateValidated(champBd = "nom_banque",table = "ta_compte_banque",champEntite="nomBanque",clazz = TaCarteBancaire.class)
	public String getNomBanque() {
		return this.nomBanque;
	}

	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}

	
	@Column(name = "nom_proprietaire")
	public String getNomProprietaire() {
		return nomProprietaire;
	}

	public void setNomProprietaire(String nomProprietaire) {
		this.nomProprietaire = nomProprietaire;
	}

	@Column(name = "numero_carte")
	public String getNumeroCarte() {
		return numeroCarte;
	}

	public void setNumeroCarte(String numeroCarte) {
		this.numeroCarte = numeroCarte;
	}

	@Column(name = "empreinte")
	public String getEmpreinte() {
		return empreinte;
	}

	public void setEmpreinte(String empreinte) {
		this.empreinte = empreinte;
	}

	@Column(name = "mois_expiration")
	public int getMoisExpiration() {
		return moisExpiration;
	}

	public void setMoisExpiration(int moisExpiration) {
		this.moisExpiration = moisExpiration;
	}

	@Column(name = "annee_expiration")
	public int getAnneeExpiration() {
		return anneeExpiration;
	}

	public void setAnneeExpiration(int anneeExpiration) {
		this.anneeExpiration = anneeExpiration;
	}

	@Column(name = "type_carte")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "pays_origine")
	public String getPaysOrigine() {
		return paysOrigine;
	}

	public void setPaysOrigine(String paysOrigine) {
		this.paysOrigine = paysOrigine;
	}

	@Column(name = "last4")
	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	@Column(name = "cvc")
	public String getCvc() {
		return cvc;
	}

	public void setCvc(String cvc) {
		this.cvc = cvc;
	}

	@Column(name = "adresse1")
	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	@Column(name = "adresse2")
	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	@Column(name = "code_postal")
	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	@Column(name = "ville")
	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	@Column(name = "pays")
	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeCompteBanque) {
//		this.quiCree = quiCreeCompteBanque;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeCompteBanque) {
//		this.quandCree = quandCreeCompteBanque;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifCompteBanque) {
//		this.quiModif = quiModifCompteBanque;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifCompteBanque) {
//		this.quandModif = quandModifCompteBanque;
//	}
//
//	@Column(name = "ip_acces", length = 50)
//	public String getIpAcces() {
//		return this.ipAcces;
//	}
//
//	public void setIpAcces(String ipAcces) {
//		this.ipAcces = ipAcces;
//	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "cptcomptable", length = 100)
	@LgrHibernateValidated(champBd = "cptcomptable",table = "ta_carte_bancaire",champEntite="cptcomptable",clazz = TaCarteBancaire.class)
	public String getCptcomptable() {
		return cptcomptable;
	}

	public void setCptcomptable(String cptcomptable) {
		this.cptcomptable = cptcomptable;
	}
	
	@Column(name = "nom_compte", length = 50)
	@LgrHibernateValidated(champBd = "nom_compte",table = "ta_carte_bancaire",champEntite="nomCompte",clazz = TaCarteBancaire.class)
	public String getNomCompte() {
		return nomCompte;
	}

	public void setNomCompte(String nomCompte) {
		this.nomCompte = nomCompte;
	}

}
