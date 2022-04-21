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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_mandat_prelevement")
/**
 * https://www.banque-france.fr/stabilite-financiere/comite-national-des-paiements-scripturaux/sepa/le-prelevement-sepa
 */
public class TaMandatPrelevement extends TaObjetLgr   implements java.io.Serializable {

	private static final long serialVersionUID = -3420790355122574127L;

	private int idMandatPrelevement;
	
	private TaCompteBanque taCompteBanque;
	private String titre;
	private String referenceUniqueMandatRum;
	private String identifiantCreancierSepaIcs;
	private Boolean validationParClientDebiteur;
	private String nomCreancier;
	private String adresse1Creancier;
	private String adresse2Creancier;
	private String adresse3Creancier;
	private String codepostalCreancier;
	private String villeCreancier;
	private String paysCreancier;
	private String texteMandat;
	private byte[] fichier;
	private String typeMimeFichier;
	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;
	private Integer versionObj;


	public TaMandatPrelevement() {
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_mandat_prelevement", unique = true, nullable = false)
	public int getIdMandatPrelevement() {
		return this.idMandatPrelevement;
	}

	public void setIdMandatPrelevement(int idMandatPrelevement) {
		this.idMandatPrelevement = idMandatPrelevement;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_compte_banque")
	public TaCompteBanque getTaCompteBanque() {
		return this.taCompteBanque;
	}

	public void setTaCompteBanque(TaCompteBanque TaCompteBanque) {
		this.taCompteBanque = TaCompteBanque;
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

	@Column(name = "titre")
	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Column(name = "reference_unique_mandat_rum")
	public String getReferenceUniqueMandatRum() {
		return referenceUniqueMandatRum;
	}

	public void setReferenceUniqueMandatRum(String referenceUniqueMandatRum) {
		this.referenceUniqueMandatRum = referenceUniqueMandatRum;
	}

	@Column(name = "identifiant_creancier_sepa_ics")
	public String getIdentifiantCreancierSepaIcs() {
		return identifiantCreancierSepaIcs;
	}

	public void setIdentifiantCreancierSepaIcs(String identifiantCreancierSepaIcs) {
		this.identifiantCreancierSepaIcs = identifiantCreancierSepaIcs;
	}

	@Column(name = "validation_par_client_debiteur")
	public Boolean getValidationParClientDebiteur() {
		return validationParClientDebiteur;
	}


	public void setValidationParClientDebiteur(Boolean validationParClientDebiteur) {
		this.validationParClientDebiteur = validationParClientDebiteur;
	}

	@Column(name = "nom_creancier")
	public String getNomCreancier() {
		return nomCreancier;
	}

	public void setNomCreancier(String nom_creancier) {
		this.nomCreancier = nom_creancier;
	}

	@Column(name = "adresse1_creancier")
	public String getAdresse1Creancier() {
		return adresse1Creancier;
	}

	public void setAdresse1Creancier(String adresse1Creancier) {
		this.adresse1Creancier = adresse1Creancier;
	}

	@Column(name = "adresse2_creancier")
	public String getAdresse2Creancier() {
		return adresse2Creancier;
	}

	public void setAdresse2Creancier(String adresse3Creancier) {
		this.adresse2Creancier = adresse3Creancier;
	}

	@Column(name = "adresse3_creancier")
	public String getAdresse3Creancier() {
		return adresse3Creancier;
	}

	public void setAdresse3Creancier(String adresse3Creancier) {
		this.adresse3Creancier = adresse3Creancier;
	}

	@Column(name = "codepostal_creancier")
	public String getCodepostalCreancier() {
		return codepostalCreancier;
	}

	public void setCodepostalCreancier(String codepostalCreancier) {
		this.codepostalCreancier = codepostalCreancier;
	}

	@Column(name = "ville_creancier")
	public String getVilleCreancier() {
		return villeCreancier;
	}

	public void setVilleCreancier(String villeCreancier) {
		this.villeCreancier = villeCreancier;
	}

	@Column(name = "pays_creancier")
	public String getPaysCreancier() {
		return paysCreancier;
	}

	public void setPaysCreancier(String paysCreancier) {
		this.paysCreancier = paysCreancier;
	}

	@Column(name = "texte_mandat")
	public String getTexteMandat() {
		return texteMandat;
	}

	public void setTexteMandat(String texteMandat) {
		this.texteMandat = texteMandat;
	}

	@Column(name = "fichier")
	public byte[] getFichier() {
		return fichier;
	}

	public void setFichier(byte[] fichier) {
		this.fichier = fichier;
	}

	@Column(name = "type_mime_fichier")
	public String getTypeMimeFichier() {
		return typeMimeFichier;
	}

	public void setTypeMimeFichier(String typeMimeFichier) {
		this.typeMimeFichier = typeMimeFichier;
	}

}
