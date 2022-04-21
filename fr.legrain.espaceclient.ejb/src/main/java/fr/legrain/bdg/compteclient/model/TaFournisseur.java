package fr.legrain.bdg.compteclient.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name = "ta_fournisseur") // Table permet de préciser le nom de la table vers laquelle le bean sera mappé. L'utilisation de cette annotation est facultative si le nom de la table correspond au nom de la classe
public class TaFournisseur implements java.io.Serializable{

	private static final long serialVersionUID = -3409692450535179644L;
	
	private int idFournisseur;
	protected String codeDossier;
	private String raisonSocialeFournisseur;	
	private String adresse1Fournisseur;	
	private String adresse2Fournisseur;
	private String adresse3Fournisseur;
	private String codePostalFournisseur;
	private String villeFournisseur;
	private String paysFournisseur;
	private String siretFournisseur;
	private String capitalFournisseur;
	private String apeFournisseur;
	private String tvaIntraFournisseur;
	private String telFournisseur;
	private String faxFournisseur;
	private String emailFournisseur;
	private String webFournisseur;
	private String rcsFournisseur;
	private String quiCreer;
	private Date quandCreer;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private byte[] blobLogo;
	private boolean affiche;
	
	@Column(name = "logo")
	public byte[] getBlobLogo() {
		return blobLogo;
	}

	public void setBlobLogo(byte[] blobLogo) {
		this.blobLogo = blobLogo;
	}
	
	public TaFournisseur(){
		
	}
	
	public TaFournisseur(int idFournisseur) {
		this.setIdFournisseur(idFournisseur);
	}
	
	public TaFournisseur(int idFournisseur, String raisonSocialeFournisseur, String adresse1Fournisseur,
			String adresse2Fournisseur, String adresse3Fournisseur, String codePostalFournisseur, 
			String villeFournisseur, String paysFournisseur){
		
			this.setIdFournisseur(idFournisseur);
			this.raisonSocialeFournisseur = raisonSocialeFournisseur;
			this.adresse1Fournisseur = adresse1Fournisseur;
			this.adresse2Fournisseur = adresse2Fournisseur;
			this.adresse3Fournisseur = adresse3Fournisseur;
			this.codePostalFournisseur = codePostalFournisseur;
			this.villeFournisseur = villeFournisseur;
			this.paysFournisseur = paysFournisseur;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fournisseur", unique = true, nullable = false)
	public int getIdFournisseur() {
		return idFournisseur;
	}

	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	@Column(name = "code_dossier")
	public String getCodeDossier() {
	    return codeDossier;
	}
	public void setCodeDossier(String value) {
	    this.codeDossier = value;
	}
	
	@Column(name = "raison_sociale_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "raison_sociale_fournisseur",table = "ta_fournisseur", champEntite="raisonSocialeFournisseur", clazz = TaFournisseur.class)
	public String getRaisonSocialeFournisseur() {
		return raisonSocialeFournisseur;
	}

	public void setRaisonSocialeFournisseur(String raisonSocialeFournisseur) {
		this.raisonSocialeFournisseur = raisonSocialeFournisseur;
	}
	
	@Column(name = "adresse1_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "adresse1_fournisseur",table = "ta_fournisseur", champEntite="adresse1Fournisseur", clazz = TaFournisseur.class)
	public String getAdresse1Fournisseur() {
		return adresse1Fournisseur;
	}

	public void setAdresse1Fournisseur(String adresse1Fournisseur) {
		this.adresse1Fournisseur = adresse1Fournisseur;
	}
	
	@Column(name = "adresse2_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "adresse2_fournisseur",table = "ta_fournisseur", champEntite="adresse2Fournisseur", clazz = TaFournisseur.class)
	public String getAdresse2Fournisseur() {
		return adresse2Fournisseur;
	}
	
	public void setAdresse2Fournisseur(String adresse2Fournisseur) {
		this.adresse2Fournisseur = adresse2Fournisseur;
	}
	
	@Column(name = "adresse3_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "adresse3_fournisseur",table = "ta_fournisseur", champEntite="adresse3Fournisseur", clazz = TaFournisseur.class)
	public String getAdresse3Fournisseur() {
		return adresse3Fournisseur;
	}

	public void setAdresse3Fournisseur(String adresse3Fournisseur) {
		this.adresse3Fournisseur = adresse3Fournisseur;
	}
	
	@Column(name = "codepostal_fournisseur", length = 25)
//	@LgrHibernateValidated(champBd = "codepostal_fournisseur",table = "ta_fournisseur", champEntite="codepostalFournisseur", clazz = TaFournisseur.class)
	public String getCodePostalFournisseur() {
		return codePostalFournisseur;
	}

	public void setCodePostalFournisseur(String codePostalFournisseur) {
		this.codePostalFournisseur = codePostalFournisseur;
	}
	
	@Column(name = "ville_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "ville_fournisseur",table = "ta_fournisseur", champEntite="villeFournisseur", clazz = TaFournisseur.class)
	public String getVilleFournisseur() {
		return villeFournisseur;
	}

	public void setVilleFournisseur(String villeFournisseur) {
		this.villeFournisseur = villeFournisseur;
	}
	
	@Column(name = "pays_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "pays_fournisseur",table = "ta_fournisseur", champEntite="paysFournisseur", clazz = TaFournisseur.class)
	public String getPaysFournisseur() {
		return paysFournisseur;
	}

	public void setPaysFournisseur(String paysFournisseur) {
		this.paysFournisseur = paysFournisseur;
	}
	
	@Column(name = "siret_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "siret_fournisseur",table = "ta_fournisseur", champEntite="siretFournisseur", clazz = TaFournisseur.class)
	public String getSiretFournisseur() {
		return siretFournisseur;
	}

	public void setSiretFournisseur(String siretFournisseur) {
		this.siretFournisseur = siretFournisseur;
	}
	
	@Column(name = "capital_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "capital_fournisseur",table = "ta_fournisseur", champEntite="capitalFournisseur", clazz = TaFournisseur.class)
	public String getCapitalFournisseur() {
		return capitalFournisseur;
	}

	public void setCapitalFournisseur(String capitalFournisseur) {
		this.capitalFournisseur = capitalFournisseur;
	}
	
	@Column(name = "ape_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "ape_fournisseur",table = "ta_fournisseur", champEntite="apeFournisseur", clazz = TaFournisseur.class)
	public String getApeFournisseur() {
		return apeFournisseur;
	}

	public void setApeFournisseur(String apeFournisseur) {
		this.apeFournisseur = apeFournisseur;
	}
	
	@Column(name = "tva_intra_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "tva_intra_fournisseur",table = "ta_fournisseur", champEntite="tvaIntraFournisseur", clazz = TaFournisseur.class)
	public String getTvaIntraFournisseur() {
		return tvaIntraFournisseur;
	}

	public void setTvaIntraFournisseur(String tvaIntraFournisseur) {
		this.tvaIntraFournisseur = tvaIntraFournisseur;
	}
	
	@Column(name = "tel_fournisseur", length = 20)
//	@LgrHibernateValidated(champBd = "tel_info_fournisseur",table = "ta_fournisseur", champEntite="telFournisseur", clazz = TaFournisseur.class)
	public String getTelFournisseur() {
		return telFournisseur;
	}

	public void setTelFournisseur(String telFournisseur) {
		this.telFournisseur = telFournisseur;
	}
	
	@Column(name = "fax_fournisseur", length = 20)
//	@LgrHibernateValidated(champBd = "fax_fournisseur",table = "ta_fournisseur", champEntite="faxFournisseur", clazz = TaFournisseur.class)
	public String getFaxFournisseur() {
		return faxFournisseur;
	}

	public void setFaxFournisseur(String faxFournisseur) {
		this.faxFournisseur = faxFournisseur;
	}
	
	@Column(name = "email_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "email_fournisseur",table = "ta_fournisseur", champEntite="emailFournisseur", clazz = TaInfoFournisseur.class)
	public String getEmailFournisseur() {
		return emailFournisseur;
	}

	public void setEmailFournisseur(String emailFournisseur) {
		this.emailFournisseur = emailFournisseur;
	}
	
	@Column(name = "web_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "web_fournisseur",table = "ta_info_fournisseur", champEntite="webFournisseur", clazz = TaFournisseur.class)
	public String getWebFournisseur() {
		return webFournisseur;
	}

	public void setWebFournisseur(String webFournisseur) {
		this.webFournisseur = webFournisseur;
	}
	
	@Column(name = "rcs_fournisseur", length = 100)
//	@LgrHibernateValidated(champBd = "rcs_fournisseur",table = "ta_fournisseur", champEntite="rcsFournisseur", clazz = TaFournisseur.class)
	public String getRcsFournisseur() {
		return rcsFournisseur;
	}

	public void setRcsFournisseur(String rcsFournisseur) {
		this.rcsFournisseur = rcsFournisseur;
	}
	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCreer() {
		return quiCreer;
	}

	public void setQuiCreer(String quiCreer) {
		this.quiCreer = quiCreer;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCreer() {
		return quandCreer;
	}

	public void setQuandCreer(Date quandCreer) {
		this.quandCreer = quandCreer;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	@Version
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Transient
	public boolean isAffiche() {
		return affiche;
	}

	public void setAffiche(boolean affiche) {
		this.affiche = affiche;
	}
	
}
