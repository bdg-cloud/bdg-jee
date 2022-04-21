package fr.legrain.servicewebexterne.model;

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
import javax.persistence.Transient;
import javax.persistence.Version;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Entity
@Table(name = "ta_ligne_service_web_externe")
@NamedQueries(value = { 
		@NamedQuery(name=TaLigneServiceWebExterne.QN.FIND_ALL_NON_TERMINE, query="select new fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO("
				+ " f.idLigneServiceWebExterne, f.libelle, f.refArticle, f.nomArticle, f.prenomTiers, f.nomTiers, f.refTiers, f.idCommandeExterne, f.refCommandeExterne, f.dateCreationExterne, f.montantHt, f.montantTtc, "
				+ " f.qteArticle, art.codeArticle, art.libellecArticle, tiers.codeTiers, lot.numLot, f.valeur1, f.valeur2, f.valeur3, f.valeur4, f.termine, compte.libelleCompteServiceWebExterne, f.montantTtcTotalDoc, f.montantTotalLivraisonDoc, f.montantTotalDiscountDoc, f.etatLigneExterne, tp.codeTPaiement, tp.libTPaiement, f.refTypePaiement )"
				+ " from TaLigneServiceWebExterne f left join f.taTiers tiers left join f.taArticle art left join f.taLot lot left join f.taCompteServiceWebExterne compte left join f.taTPaiement tp"
				+ " where f.termine IS NULL or f.termine = false "
				+ " order by f.dateCreationExterne"),
		@NamedQuery(name=TaLigneServiceWebExterne.QN.FIND_ALL_TERMINE, query="select new fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO("
				+ " f.idLigneServiceWebExterne, f.libelle, f.refArticle, f.nomArticle, f.prenomTiers, f.nomTiers, f.refTiers, f.idCommandeExterne, f.refCommandeExterne, f.dateCreationExterne, f.montantHt,f.montantTtc, "
				+ " f.qteArticle, art.codeArticle, art.libellecArticle, tiers.codeTiers, lot.numLot, f.valeur1, f.valeur2, f.valeur3, f.valeur4, f.termine, compte.libelleCompteServiceWebExterne, f.montantTtcTotalDoc, f.montantTotalLivraisonDoc, f.montantTotalDiscountDoc, f.etatLigneExterne, tp.codeTPaiement, tp.libTPaiement, f.refTypePaiement )"
				+ " from TaLigneServiceWebExterne f left join f.taTiers tiers left join f.taArticle art left join f.taLot lot left join f.taCompteServiceWebExterne compte left join f.taTPaiement tp"
				+ " where f.termine = true "
				+ " order by f.dateCreationExterne"),
		@NamedQuery(name=TaLigneServiceWebExterne.QN.FIND_ALL_NON_TERMINE_BY_ID_COMPTE_SERVICE_WEB_EXTERNE, query="select new fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO("
				+ " f.idLigneServiceWebExterne, f.libelle, f.refArticle, f.nomArticle, f.prenomTiers, f.nomTiers, f.refTiers, f.idCommandeExterne, f.refCommandeExterne, f.dateCreationExterne, f.montantHt,f.montantTtc, "
				+ " f.qteArticle, art.codeArticle, art.libellecArticle, tiers.codeTiers, lot.numLot, f.valeur1, f.valeur2, f.valeur3, f.valeur4, f.termine, compte.libelleCompteServiceWebExterne, f.montantTtcTotalDoc, f.montantTotalLivraisonDoc, f.montantTotalDiscountDoc, f.etatLigneExterne, tp.codeTPaiement, tp.libTPaiement, f.refTypePaiement )"
				+ " from TaLigneServiceWebExterne f left join f.taTiers tiers left join f.taArticle art left join f.taLot lot left join f.taCompteServiceWebExterne compte left join f.taTPaiement tp"
				+ " where (f.termine IS NULL or f.termine = false) "
				+ " and compte.idCompteServiceWebExterne = :id"
				+ " order by f.dateCreationExterne"),
		@NamedQuery(name=TaLigneServiceWebExterne.QN.FIND_ALL_TERMINE_BY_ID_COMPTE_SERVICE_WEB_EXTERNE, query="select new fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO("
				+ " f.idLigneServiceWebExterne, f.libelle, f.refArticle, f.nomArticle, f.prenomTiers, f.nomTiers, f.refTiers, f.idCommandeExterne, f.refCommandeExterne, f.dateCreationExterne, f.montantHt,f.montantTtc, "
				+ " f.qteArticle, art.codeArticle, art.libellecArticle, tiers.codeTiers, lot.numLot, f.valeur1, f.valeur2, f.valeur3, f.valeur4, f.termine, compte.libelleCompteServiceWebExterne, f.montantTtcTotalDoc, f.montantTotalLivraisonDoc, f.montantTotalDiscountDoc, f.etatLigneExterne, tp.codeTPaiement, tp.libTPaiement, f.refTypePaiement )"
				+ " from TaLigneServiceWebExterne f left join f.taTiers tiers left join f.taArticle art left join f.taLot lot left join f.taCompteServiceWebExterne compte left join f.taTPaiement tp"
				+ " where f.termine = true "
				+ " and compte.idCompteServiceWebExterne = :id"
				+ " order by f.dateCreationExterne"),
		@NamedQuery(name=TaLigneServiceWebExterne.QN.FIND_ALL_NON_TERMINE_BY_ID_SERVICE_WEB_EXTERNE, query="select new fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO("
				+ " f.idLigneServiceWebExterne, f.libelle, f.refArticle, f.nomArticle, f.prenomTiers, f.nomTiers, f.refTiers, f.idCommandeExterne, f.refCommandeExterne, f.dateCreationExterne, f.montantHt,f.montantTtc, "
				+ " f.qteArticle, art.codeArticle, art.libellecArticle, tiers.codeTiers, lot.numLot, f.valeur1, f.valeur2, f.valeur3, f.valeur4, f.termine, compte.libelleCompteServiceWebExterne, f.montantTtcTotalDoc, f.montantTotalLivraisonDoc, f.montantTotalDiscountDoc, f.etatLigneExterne, tp.codeTPaiement, tp.libTPaiement, f.refTypePaiement )"
				+ " from TaLigneServiceWebExterne f left join f.taTiers tiers left join f.taArticle art left join f.taLot lot left join f.taCompteServiceWebExterne compte left join compte.taServiceWebExterne service left join f.taTPaiement tp"
				+ " where (f.termine IS NULL or f.termine = false) "
				+ " and service.idServiceWebExterne = :id"
				+ " order by f.dateCreationExterne"),
		@NamedQuery(name=TaLigneServiceWebExterne.QN.FIND_ALL_TERMINE_BY_ID_SERVICE_WEB_EXTERNE, query="select new fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO("
				+ " f.idLigneServiceWebExterne, f.libelle, f.refArticle, f.nomArticle, f.prenomTiers, f.nomTiers, f.refTiers, f.idCommandeExterne, f.refCommandeExterne, f.dateCreationExterne, f.montantHt,f.montantTtc, "
				+ " f.qteArticle, art.codeArticle, art.libellecArticle, tiers.codeTiers, lot.numLot, f.valeur1, f.valeur2, f.valeur3, f.valeur4, f.termine, compte.libelleCompteServiceWebExterne, f.montantTtcTotalDoc, f.montantTotalLivraisonDoc, f.montantTotalDiscountDoc, f.etatLigneExterne, tp.codeTPaiement, tp.libTPaiement, f.refTypePaiement )"
				+ " from TaLigneServiceWebExterne f left join f.taTiers tiers left join f.taArticle art left join f.taLot lot left join f.taCompteServiceWebExterne compte left join compte.taServiceWebExterne service left join f.taTPaiement tp"
				+ " where f.termine = true "
				+ " and service.idServiceWebExterne = :id"
				+ " order by f.dateCreationExterne")

})		
public class TaLigneServiceWebExterne implements java.io.Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2036134599325364883L;
	
	public static class QN {
		public static final String FIND_ALL_NON_TERMINE = "TaLigneServiceWebExterne.findAllNonTermine";	
		public static final String FIND_ALL_TERMINE = "TaLigneServiceWebExterne.findAllTermine";
		public static final String FIND_ALL_NON_TERMINE_BY_ID_COMPTE_SERVICE_WEB_EXTERNE="TaLigneServiceWebExterne.findAllNonTermineByIdCompteServiceWebExterne";
		public static final String FIND_ALL_TERMINE_BY_ID_COMPTE_SERVICE_WEB_EXTERNE="TaLigneServiceWebExterne.findAllTermineByIdCompteServiceWebExterne";
		public static final String FIND_ALL_NON_TERMINE_BY_ID_SERVICE_WEB_EXTERNE="TaLigneServiceWebExterne.findAllNonTermineByIdServiceWebExterne";
		public static final String FIND_ALL_TERMINE_BY_ID_SERVICE_WEB_EXTERNE="TaLigneServiceWebExterne.findAllTermineByIdServiceWebExterne";
	}

	private int idLigneServiceWebExterne;
	
	private String libelle;
	
	private String jsonInitial;

	private TaTiers taTiers;
	private TaArticle taArticle;
	private TaCompteServiceWebExterne taCompteServiceWebExterne;
	private TaLot taLot;
	private TaTelephone taTelephone;
	private TaAdresse taAdresse;
	private TaEmail taEmail;
	private TaTPaiement taTPaiement;
	
	private String montantHtTotalDoc;
	private String montantTtcTotalDoc;
	private String prixUnitaire;
	
	private String montantTotalLivraisonDoc;
	private String montantTotalDiscountDoc;
	

	
	
  private String refArticle;
  private String nomArticle;
  private Integer qteArticle;
  private String uniteArticle;
  private String refTiers;
  private String nomTiers;

private String prenomTiers;
  private String civiliteTiers;
  private String mailTiers;
  private String telTiers ;
  private String nomEntrepriseTiers ;
  private String refEntrepriseTiers ;
  private String adresse1Livraison ;
  private String adresse2Livraison ;
  private String adresse3Livraison;
  private String codePostalLivraison;
  private String villeLivraison;
  private String paysLivraison;
  private String montantHt;
  private String montantTtc;
  private String tauxTva;
  private String remTx;
  private String remHt;
  private String mtHtApresRem;
  private String refLot;
  private String refCommandeExterne ; 
  private String idCommandeExterne;
  private Date dateCreationExterne;
  private Date dateUpdateExterne;
  
  private String refTypePaiement;
  
  private String valeur1;
  private String valeur2;
  private String valeur3;
  private String valeur4;
  
  private Boolean termine;
  
  private String etatLigneExterne;
	
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCommandeExterne == null) ? 0 : idCommandeExterne.hashCode());
		result = prime * result + idLigneServiceWebExterne;
		result = prime * result + ((montantHt == null) ? 0 : montantHt.hashCode());
		result = prime * result + ((montantHtTotalDoc == null) ? 0 : montantHtTotalDoc.hashCode());
		result = prime * result + ((montantTotalDiscountDoc == null) ? 0 : montantTotalDiscountDoc.hashCode());
		result = prime * result + ((montantTotalLivraisonDoc == null) ? 0 : montantTotalLivraisonDoc.hashCode());
		result = prime * result + ((montantTtc == null) ? 0 : montantTtc.hashCode());
		result = prime * result + ((montantTtcTotalDoc == null) ? 0 : montantTtcTotalDoc.hashCode());
		result = prime * result + ((mtHtApresRem == null) ? 0 : mtHtApresRem.hashCode());
		result = prime * result + ((nomArticle == null) ? 0 : nomArticle.hashCode());
		result = prime * result + ((nomEntrepriseTiers == null) ? 0 : nomEntrepriseTiers.hashCode());
		result = prime * result + ((nomTiers == null) ? 0 : nomTiers.hashCode());
		result = prime * result + ((prenomTiers == null) ? 0 : prenomTiers.hashCode());
		result = prime * result + ((qteArticle == null) ? 0 : qteArticle.hashCode());
		result = prime * result + ((refArticle == null) ? 0 : refArticle.hashCode());
		result = prime * result + ((refCommandeExterne == null) ? 0 : refCommandeExterne.hashCode());
		result = prime * result + ((refTiers == null) ? 0 : refTiers.hashCode());
		result = prime * result + ((refTypePaiement == null) ? 0 : refTypePaiement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaLigneServiceWebExterne other = (TaLigneServiceWebExterne) obj;
		if (idCommandeExterne == null) {
			if (other.idCommandeExterne != null)
				return false;
		} else if (!idCommandeExterne.equals(other.idCommandeExterne))
			return false;
		if (idLigneServiceWebExterne != other.idLigneServiceWebExterne)
			return false;
		if (montantHt == null) {
			if (other.montantHt != null)
				return false;
		} else if (!montantHt.equals(other.montantHt))
			return false;
		if (montantHtTotalDoc == null) {
			if (other.montantHtTotalDoc != null)
				return false;
		} else if (!montantHtTotalDoc.equals(other.montantHtTotalDoc))
			return false;
		if (montantTotalDiscountDoc == null) {
			if (other.montantTotalDiscountDoc != null)
				return false;
		} else if (!montantTotalDiscountDoc.equals(other.montantTotalDiscountDoc))
			return false;
		if (montantTotalLivraisonDoc == null) {
			if (other.montantTotalLivraisonDoc != null)
				return false;
		} else if (!montantTotalLivraisonDoc.equals(other.montantTotalLivraisonDoc))
			return false;
		if (montantTtc == null) {
			if (other.montantTtc != null)
				return false;
		} else if (!montantTtc.equals(other.montantTtc))
			return false;
		if (montantTtcTotalDoc == null) {
			if (other.montantTtcTotalDoc != null)
				return false;
		} else if (!montantTtcTotalDoc.equals(other.montantTtcTotalDoc))
			return false;
		if (mtHtApresRem == null) {
			if (other.mtHtApresRem != null)
				return false;
		} else if (!mtHtApresRem.equals(other.mtHtApresRem))
			return false;
		if (nomArticle == null) {
			if (other.nomArticle != null)
				return false;
		} else if (!nomArticle.equals(other.nomArticle))
			return false;
		if (nomEntrepriseTiers == null) {
			if (other.nomEntrepriseTiers != null)
				return false;
		} else if (!nomEntrepriseTiers.equals(other.nomEntrepriseTiers))
			return false;
		if (nomTiers == null) {
			if (other.nomTiers != null)
				return false;
		} else if (!nomTiers.equals(other.nomTiers))
			return false;
		if (prenomTiers == null) {
			if (other.prenomTiers != null)
				return false;
		} else if (!prenomTiers.equals(other.prenomTiers))
			return false;
		if (qteArticle == null) {
			if (other.qteArticle != null)
				return false;
		} else if (!qteArticle.equals(other.qteArticle))
			return false;
		if (refArticle == null) {
			if (other.refArticle != null)
				return false;
		} else if (!refArticle.equals(other.refArticle))
			return false;
		if (refCommandeExterne == null) {
			if (other.refCommandeExterne != null)
				return false;
		} else if (!refCommandeExterne.equals(other.refCommandeExterne))
			return false;
		if (refTiers == null) {
			if (other.refTiers != null)
				return false;
		} else if (!refTiers.equals(other.refTiers))
			return false;
		if (refTypePaiement == null) {
			if (other.refTypePaiement != null)
				return false;
		} else if (!refTypePaiement.equals(other.refTypePaiement))
			return false;
		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ligne_service_web_externe", unique = true, nullable = false)
	public int getIdLigneServiceWebExterne() {
		return idLigneServiceWebExterne;
	}

	public void setIdLigneServiceWebExterne(int idLigneServiceWebExterne) {
		this.idLigneServiceWebExterne = idLigneServiceWebExterne;
	}
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
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
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	@Column(name = "version", length = 20)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "json_initial")
	public String getJsonInitial() {
		return jsonInitial;
	}

	public void setJsonInitial(String jsonInitial) {
		this.jsonInitial = jsonInitial;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_article")
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinColumn(name = "id_tiers")
	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_compte_service_web_externe")
	public TaCompteServiceWebExterne getTaCompteServiceWebExterne() {
		return taCompteServiceWebExterne;
	}

	public void setTaCompteServiceWebExterne(TaCompteServiceWebExterne taCompteServiceWebExterne) {
		this.taCompteServiceWebExterne = taCompteServiceWebExterne;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_lot")
	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_tel_tiers")
	public TaTelephone getTaTelephone() {
		return taTelephone;
	}

	public void setTaTelephone(TaTelephone taTelephone) {
		this.taTelephone = taTelephone;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_adresse_livraison")
	public TaAdresse getTaAdresse() {
		return taAdresse;
	}

	public void setTaAdresse(TaAdresse taAdresse) {
		this.taAdresse = taAdresse;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_mail_tiers")
	public TaEmail getTaEmail() {
		return taEmail;
	}

	public void setTaEmail(TaEmail taEmail) {
		this.taEmail = taEmail;
	}
	@Column(name = "ref_article")
	public String getRefArticle() {
		return refArticle;
	}

	public void setRefArticle(String refArticle) {
		this.refArticle = refArticle;
	}
	@Column(name = "nom_article")
	public String getNomArticle() {
		return nomArticle;
	}

	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}
	@Column(name = "qte_article")
	public Integer getQteArticle() {
		return qteArticle;
	}

	public void setQteArticle(Integer qteArticle) {
		this.qteArticle = qteArticle;
	}
	@Column(name = "unite_article")
	public String getUniteArticle() {
		return uniteArticle;
	}

	public void setUniteArticle(String uniteArticle) {
		this.uniteArticle = uniteArticle;
	}
	@Column(name = "ref_tiers")
	public String getRefTiers() {
		return refTiers;
	}

	public void setRefTiers(String refTiers) {
		this.refTiers = refTiers;
	}
	@Column(name = "nom_tiers")
	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}
	@Column(name = "prenom_tiers")
	public String getPrenomTiers() {
		return prenomTiers;
	}

	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}
	@Column(name = "civilite_tiers")
	public String getCiviliteTiers() {
		return civiliteTiers;
	}

	public void setCiviliteTiers(String civiliteTiers) {
		this.civiliteTiers = civiliteTiers;
	}
	@Column(name = "mail_tiers")
	public String getMailTiers() {
		return mailTiers;
	}

	public void setMailTiers(String mailTiers) {
		this.mailTiers = mailTiers;
	}
	@Column(name = "tel_tiers")
	public String getTelTiers() {
		return telTiers;
	}

	public void setTelTiers(String telTiers) {
		this.telTiers = telTiers;
	}
	@Column(name = "nom_entreprise_tiers")
	public String getNomEntrepriseTiers() {
		return nomEntrepriseTiers;
	}

	public void setNomEntrepriseTiers(String nomEntrepriseTiers) {
		this.nomEntrepriseTiers = nomEntrepriseTiers;
	}
	@Column(name = "ref_entreprise_tiers")
	public String getRefEntrepriseTiers() {
		return refEntrepriseTiers;
	}

	public void setRefEntrepriseTiers(String refEntrepriseTiers) {
		this.refEntrepriseTiers = refEntrepriseTiers;
	}
	@Column(name = "adresse1_livraison")
	public String getAdresse1Livraison() {
		return adresse1Livraison;
	}

	public void setAdresse1Livraison(String adresse1Livraison) {
		this.adresse1Livraison = adresse1Livraison;
	}
	@Column(name = "adresse2_livraison")
	public String getAdresse2Livraison() {
		return adresse2Livraison;
	}

	public void setAdresse2Livraison(String adresse2Livraison) {
		this.adresse2Livraison = adresse2Livraison;
	}
	@Column(name = "adresse3_livraison")
	public String getAdresse3Livraison() {
		return adresse3Livraison;
	}

	public void setAdresse3Livraison(String adresse3Livraison) {
		this.adresse3Livraison = adresse3Livraison;
	}
	@Column(name = "code_postal_livraison")
	public String getCodePostalLivraison() {
		return codePostalLivraison;
	}

	public void setCodePostalLivraison(String codePostalLivraison) {
		this.codePostalLivraison = codePostalLivraison;
	}
	@Column(name = "ville_livraison")
	public String getVilleLivraison() {
		return villeLivraison;
	}

	public void setVilleLivraison(String villeLivraison) {
		this.villeLivraison = villeLivraison;
	}
	@Column(name = "pays_livraison")
	public String getPaysLivraison() {
		return paysLivraison;
	}

	public void setPaysLivraison(String paysLivraison) {
		this.paysLivraison = paysLivraison;
	}
	@Column(name = "montant_ht")
	public String getMontantHt() {
		return montantHt;
	}

	public void setMontantHt(String montantHt) {
		this.montantHt = montantHt;
	}
	@Column(name = "montant_ttc")
	public String getMontantTtc() {
		return montantTtc;
	}

	public void setMontantTtc(String montantTtc) {
		this.montantTtc = montantTtc;
	}
	@Column(name = "taux_tva")
	public String getTauxTva() {
		return tauxTva;
	}

	public void setTauxTva(String tauxTva) {
		this.tauxTva = tauxTva;
	}
	@Column(name = "rem_tx")
	public String getRemTx() {
		return remTx;
	}

	public void setRemTx(String remTx) {
		this.remTx = remTx;
	}
	@Column(name = "rem_ht")
	public String getRemHt() {
		return remHt;
	}

	public void setRemHt(String remHt) {
		this.remHt = remHt;
	}
	@Column(name = "mt_ht_apres_rem")
	public String getMtHtApresRem() {
		return mtHtApresRem;
	}

	public void setMtHtApresRem(String mtHtApresRem) {
		this.mtHtApresRem = mtHtApresRem;
	}
	@Column(name = "ref_lot")
	public String getRefLot() {
		return refLot;
	}

	public void setRefLot(String refLot) {
		this.refLot = refLot;
	}
	@Column(name = "ref_commande_externe")
	public String getRefCommandeExterne() {
		return refCommandeExterne;
	}
	public void setRefCommandeExterne(String refCommandeExterne) {
		this.refCommandeExterne = refCommandeExterne;
	}
	@Column(name = "id_commande_externe")
	public String getIdCommandeExterne() {
		return idCommandeExterne;
	}

	public void setIdCommandeExterne(String idCommandeExterne) {
		this.idCommandeExterne = idCommandeExterne;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_creation_externe", length = 19)
	public Date getDateCreationExterne() {
		return dateCreationExterne;
	}
	public void setDateCreationExterne(Date dateCreationExterne) {
		this.dateCreationExterne = dateCreationExterne;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_update_externe", length = 19)
	public Date getDateUpdateExterne() {
		return dateUpdateExterne;
	}

	public void setDateUpdateExterne(Date dateUpdateExterne) {
		this.dateUpdateExterne = dateUpdateExterne;
	}
	@Column(name = "termine")
	public Boolean getTermine() {
		return termine;
	}

	public void setTermine(Boolean termine) {
		this.termine = termine;
	}
	@Column(name = "valeur_1")
	public String getValeur1() {
		return valeur1;
	}

	public void setValeur1(String valeur1) {
		this.valeur1 = valeur1;
	}
	@Column(name = "valeur_2")
	public String getValeur2() {
		return valeur2;
	}

	public void setValeur2(String valeur2) {
		this.valeur2 = valeur2;
	}
	@Column(name = "valeur_3")
	public String getValeur3() {
		return valeur3;
	}

	public void setValeur3(String valeur3) {
		this.valeur3 = valeur3;
	}
	@Column(name = "valeur_4")
	public String getValeur4() {
		return valeur4;
	}

	public void setValeur4(String valeur4) {
		this.valeur4 = valeur4;
	}
	@Column(name = "montant_ht_total_doc")
	public String getMontantHtTotalDoc() {
		return montantHtTotalDoc;
	}

	public void setMontantHtTotalDoc(String montantHtTotalDoc) {
		this.montantHtTotalDoc = montantHtTotalDoc;
	}
	@Column(name = "montant_ttc_total_doc")
	public String getMontantTtcTotalDoc() {
		return montantTtcTotalDoc;
	}

	public void setMontantTtcTotalDoc(String montantTtcTotalDoc) {
		this.montantTtcTotalDoc = montantTtcTotalDoc;
	}
	@Column(name = "prix_unitaire")
	public String getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(String prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}
	@Column(name = "montant_total_livraison_doc")
	public String getMontantTotalLivraisonDoc() {
		return montantTotalLivraisonDoc;
	}

	public void setMontantTotalLivraisonDoc(String montantTotalLivraisonDoc) {
		this.montantTotalLivraisonDoc = montantTotalLivraisonDoc;
	}
	@Column(name = "montant_total_discount_doc")
	public String getMontantTotalDiscountDoc() {
		return montantTotalDiscountDoc;
	}

	public void setMontantTotalDiscountDoc(String montantTotalDiscountDoc) {
		this.montantTotalDiscountDoc = montantTotalDiscountDoc;
	}
	@Column(name = "etat_ligne_externe")
	public String getEtatLigneExterne() {
		return etatLigneExterne;
	}

	public void setEtatLigneExterne(String etatLigneExterne) {
		this.etatLigneExterne = etatLigneExterne;
	}
	@Column(name = "ref_type_paiement")
	public String getRefTypePaiement() {
		return refTypePaiement;
	}

	public void setRefTypePaiement(String refTypePaiement) {
		this.refTypePaiement = refTypePaiement;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_t_paiement")
	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}

}
