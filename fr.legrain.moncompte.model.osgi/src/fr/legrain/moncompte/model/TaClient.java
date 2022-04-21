package fr.legrain.moncompte.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;


@Entity
@Table(name = "ta_client")
@NamedQueries(value = { 
		@NamedQuery(name=TaClient.QN.FIND_BY_CODE, query="select c from TaClient c where c.code= :code"),
		@NamedQuery(name=TaClient.QN.FIND_ALL_DEMANDE_PARTENARIAT, query="select c from TaClient c join c.taPartenaire p where p.codePartenaire='tmp' and p.actif = false"),
		@NamedQuery(name=TaClient.QN.FIND_ALL_PARTENAIRE, query="select c from TaClient c join c.taPartenaire p where p.codePartenaire<>'tmp' and p.actif = true"),
		@NamedQuery(name=TaClient.QN.FIND_ALL_PARTENAIRE_TYPE, query="select c from TaClient c join c.taPartenaire p where p.codePartenaire<>'tmp' and p.actif = true and p.taTypePartenaire.idTypePartenaire = :idTypePartenaire")
		})
public class TaClient implements Serializable {

	private static final long serialVersionUID = 8501805659367719874L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaClient.findByCode";
		public static final String FIND_ALL_DEMANDE_PARTENARIAT = "TaClient.listeDemandePartenariat";
		public static final String FIND_ALL_PARTENAIRE = "TaClient.listePartenaire";
		public static final String FIND_ALL_PARTENAIRE_TYPE = "TaClient.listePartenaireType";
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="code")
	private String code;
	
	@Column(name="ancien_code")
	private String ancienCode;
	
	@Column(name="nom")
	private String nom;
	
	@Column(name="prenom")
	private String prenom;
	
	@Column(name="actif")
	private Boolean actif = true;
	
	@Column(name="identifiant_ftp")
	private String identifiantFtp;
	
	@Column(name="mdp_ftp")
	private String mdpFtp;
	
	@Column(name="serveur_ftp")
	private String serveurFtp;
	
	@Column(name="num_tva")
	private String numeroTva;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_adresse_1")
	private TaAdresse adresse1;
	 
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_adresse_2")
	private TaAdresse adresse2;
	 
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_adresse_3")
	private TaAdresse adresse3;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_utilisateur")
	private TaUtilisateur taUtilisateur;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_ta_partenaire")
	private TaPartenaire taPartenaire;
	
	@Column(name="titulaire_compte_banque")
	private String titulaireCompteBanque;
	
	@Column(name="nom_banque")
	private String nomBanque;
	
	@Column(name="adresse1_banque")
	private String adresse1Banque;
	
	@Column(name="adresse2_banque")
	private String adresse2Banque;
	
	@Column(name="cp_banque")
	private String cpBanque;
	
	@Column(name="ville_banque")
	private String villeBanque;
	
	@Column(name="iban_banque")
	private String ibanBanque;
	
	@Column(name="bic_swift_banque")
	private String bicSwiftBanque;
	
	@Transient
	private List<TaLicence> listeLicence;
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taClient", orphanRemoval=true)
//	@XmlInverseReference(mappedBy="taClient")
//	@XmlElement
//	private List<TaDossier> listeDossier;
	
	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;
	
	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;
	
	@Column(name="qui_cree")
	private String quiCree;
	
	@Column(name="qui_modif")
	private String quiModif;
	
//	@Column(name = "ip_acces")
//	private String ipAcces;
	
	@Version
	@Column(name = "version_obj")
	private Integer versionObj;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public String getQuiCree() {
		return quiCree;
	}

	public String getQuiModif() {
		return quiModif;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setQuandCree(Date quanCree) {
		this.quandCree = quanCree;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}


	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIdentifiantFtp() {
		return identifiantFtp;
	}

	public void setIdentifiantFtp(String identifiantFtp) {
		this.identifiantFtp = identifiantFtp;
	}

	public String getMdpFtp() {
		return mdpFtp;
	}

	public void setMdpFtp(String mdpFtp) {
		this.mdpFtp = mdpFtp;
	}

	public String getServeurFtp() {
		return serveurFtp;
	}

	public void setServeurFtp(String serveurFtp) {
		this.serveurFtp = serveurFtp;
	}

	public TaAdresse getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(TaAdresse adresse1) {
		this.adresse1 = adresse1;
	}

	public TaAdresse getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(TaAdresse adresse2) {
		this.adresse2 = adresse2;
	}

	public TaAdresse getAdresse3() {
		return adresse3;
	}

	public void setAdresse3(TaAdresse adresse3) {
		this.adresse3 = adresse3;
	}

	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur taUtilisateur) {
		this.taUtilisateur = taUtilisateur;
	}

	public List<TaLicence> getListeLicence() {
		return listeLicence;
	}

	public void setListeLicence(List<TaLicence> listeLicence) {
		this.listeLicence = listeLicence;
	}

//	public List<TaDossier> getListeDossier() {
//		return listeDossier;
//	}
//
//	public void setListeDossier(List<TaDossier> listeDossier) {
//		this.listeDossier = listeDossier;
//	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getAncienCode() {
		return ancienCode;
	}

	public void setAncienCode(String ancienCode) {
		this.ancienCode = ancienCode;
	}

	public String getNumeroTva() {
		return numeroTva;
	}

	public void setNumeroTva(String numeroTva) {
		this.numeroTva = numeroTva;
	}

	public TaPartenaire getTaPartenaire() {
		return taPartenaire;
	}

	public void setTaPartenaire(TaPartenaire taPartenaire) {
		this.taPartenaire = taPartenaire;
	}

	public String getTitulaireCompteBanque() {
		return titulaireCompteBanque;
	}

	public void setTitulaireCompteBanque(String titulaireCompteBanque) {
		this.titulaireCompteBanque = titulaireCompteBanque;
	}

	public String getNomBanque() {
		return nomBanque;
	}

	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}

	public String getAdresse1Banque() {
		return adresse1Banque;
	}

	public void setAdresse1Banque(String adresse1Banque) {
		this.adresse1Banque = adresse1Banque;
	}

	public String getAdresse2Banque() {
		return adresse2Banque;
	}

	public void setAdresse2Banque(String adresse2Banque) {
		this.adresse2Banque = adresse2Banque;
	}

	public String getCpBanque() {
		return cpBanque;
	}

	public void setCpBanque(String cpBanque) {
		this.cpBanque = cpBanque;
	}

	public String getVilleBanque() {
		return villeBanque;
	}

	public void setVilleBanque(String villeBanque) {
		this.villeBanque = villeBanque;
	}

	public String getIbanBanque() {
		return ibanBanque;
	}

	public void setIbanBanque(String ibanBanque) {
		this.ibanBanque = ibanBanque;
	}

	public String getBicSwiftBanque() {
		return bicSwiftBanque;
	}

	public void setBicSwiftBanque(String bicSwiftBanque) {
		this.bicSwiftBanque = bicSwiftBanque;
	}

}
