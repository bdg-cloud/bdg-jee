package fr.legrain.servicewebexterne.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.document.dto.TaLigneALigneDTO;

public class TaLigneServiceWebExterneDTO  extends ModelObject implements java.io.Serializable{



	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7529609678000411506L;

	private Integer id;
	
	private String libelle;
	
	private String libelleCompteServiceWebExterne;
	
	private Boolean enteteDoc = false;
	
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
	  private String telTiers;
	  private String nomEntrepriseTiers;
	  private String refEntrepriseTiers;
	  private String adresse1Livraison;
	  private String adresse2Livraison;
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
	  private String numLot;
	  private String refCommandeExterne; 
	  private String idCommandeExterne;
	  private Date dateCreationExterne;
	  private Date dateUpdateExterne;
	  
	  private String codeArticle;
	  private String libellecArticle;
	  private String codeTiers;
	  
	  private String codeTPaiement;
	  private String liblTPaiement;
	  private String refTypePaiement;
	  
	  private String valeur1;
	  private String valeur2;
	  private String valeur3;
	  private String valeur4;
	  
	  private Boolean termine;
	  
	  private String etatLigneExterne;
	  
	  private String jsonInitial;
	  
	  private Boolean complete;


	private Integer versionObj;
	
	
	public TaLigneServiceWebExterneDTO() {
	}
	
	public TaLigneServiceWebExterneDTO(
			Integer id,
			String libelle,
			String refArticle,
			String nomArticle,
			String prenomTiers,
			String nomTiers,
			String refTiers,
			String idCommandeExterne,
			String refCommandeExterne,
			Date dateCreationExterne,
			String montantHt,
			String montantTtc,
			Integer qteArticle,
			String codeArticle,
			String libellecArticle,
			String codeTiers,
			String numLot,
			String valeur1,
			String valeur2,
			String valeur3,
			String valeur4,
			Boolean termine,
			String libelleCompteServiceWebExterne,
			String montantTtcTotalDoc,
			String montantTotalLivraisonDoc,
			String montantTotalDiscountDoc,
			String etatLigneExterne,
			String codeTPaiement,
			String liblTPaiement,
			String refTypePaiement
			) {
		this.id = id;
		this.libelle = libelle;
		this.refArticle = refArticle;
		this.nomArticle = nomArticle;
		this.prenomTiers = prenomTiers;
		this.nomTiers = nomTiers;
		this.refTiers = refTiers;
		this.idCommandeExterne = idCommandeExterne;
		this.refCommandeExterne = refCommandeExterne;
		this.dateCreationExterne = dateCreationExterne;
		this.montantHt = montantHt;
		this.montantTtc = montantTtc;
		this.qteArticle = qteArticle;
		this.codeArticle = codeArticle;
		this.libellecArticle = libellecArticle;
		this.codeTiers = codeTiers;
		this.numLot = numLot;
		this.valeur1 = valeur1;
		this.valeur2 = valeur2;
		this.valeur3 = valeur3;
		this.valeur4 = valeur4;
		this.termine = termine;
		this.libelleCompteServiceWebExterne = libelleCompteServiceWebExterne;
		this.montantTtcTotalDoc = montantTtcTotalDoc;
		this.montantTotalLivraisonDoc = montantTotalLivraisonDoc;
		this.montantTotalDiscountDoc = montantTotalDiscountDoc;
		this.etatLigneExterne = etatLigneExterne;
		this.codeTPaiement = codeTPaiement;
		this.liblTPaiement = liblTPaiement;
		this.refTypePaiement = refTypePaiement;

	}
	
	static public TaLigneServiceWebExterneDTO copy(TaLigneServiceWebExterneDTO obj) {
		TaLigneServiceWebExterneDTO l = new TaLigneServiceWebExterneDTO();
		l.setCodeArticle(obj.codeArticle);
		l.setCodeTiers(obj.codeTiers);
		l.setNumLot(obj.numLot);
		l.setId(obj.id);
		
		return l;		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getRefArticle() {
		return refArticle;
	}

	public void setRefArticle(String refArticle) {
		this.refArticle = refArticle;
	}

	public String getNomArticle() {
		return nomArticle;
	}

	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}

	public Integer getQteArticle() {
		return qteArticle;
	}

	public void setQteArticle(Integer qteArticle) {
		this.qteArticle = qteArticle;
	}

	public String getUniteArticle() {
		return uniteArticle;
	}

	public void setUniteArticle(String uniteArticle) {
		this.uniteArticle = uniteArticle;
	}

	public String getRefTiers() {
		return refTiers;
	}

	public void setRefTiers(String refTiers) {
		this.refTiers = refTiers;
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}

	public String getPrenomTiers() {
		return prenomTiers;
	}

	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}

	public String getCiviliteTiers() {
		return civiliteTiers;
	}

	public void setCiviliteTiers(String civiliteTiers) {
		this.civiliteTiers = civiliteTiers;
	}

	public String getMailTiers() {
		return mailTiers;
	}

	public void setMailTiers(String mailTiers) {
		this.mailTiers = mailTiers;
	}

	public String getTelTiers() {
		return telTiers;
	}

	public void setTelTiers(String telTiers) {
		this.telTiers = telTiers;
	}

	public String getNomEntrepriseTiers() {
		return nomEntrepriseTiers;
	}

	public void setNomEntrepriseTiers(String nomEntrepriseTiers) {
		this.nomEntrepriseTiers = nomEntrepriseTiers;
	}

	public String getRefEntrepriseTiers() {
		return refEntrepriseTiers;
	}

	public void setRefEntrepriseTiers(String refEntrepriseTiers) {
		this.refEntrepriseTiers = refEntrepriseTiers;
	}

	public String getAdresse1Livraison() {
		return adresse1Livraison;
	}

	public void setAdresse1Livraison(String adresse1Livraison) {
		this.adresse1Livraison = adresse1Livraison;
	}

	public String getAdresse2Livraison() {
		return adresse2Livraison;
	}

	public void setAdresse2Livraison(String adresse2Livraison) {
		this.adresse2Livraison = adresse2Livraison;
	}

	public String getAdresse3Livraison() {
		return adresse3Livraison;
	}

	public void setAdresse3Livraison(String adresse3Livraison) {
		this.adresse3Livraison = adresse3Livraison;
	}

	public String getCodePostalLivraison() {
		return codePostalLivraison;
	}

	public void setCodePostalLivraison(String codePostalLivraison) {
		this.codePostalLivraison = codePostalLivraison;
	}

	public String getVilleLivraison() {
		return villeLivraison;
	}

	public void setVilleLivraison(String villeLivraison) {
		this.villeLivraison = villeLivraison;
	}

	public String getPaysLivraison() {
		return paysLivraison;
	}

	public void setPaysLivraison(String paysLivraison) {
		this.paysLivraison = paysLivraison;
	}

	public String getMontantHt() {
		return montantHt;
	}

	public void setMontantHt(String montantHt) {
		this.montantHt = montantHt;
	}

	public String getMontantTtc() {
		return montantTtc;
	}

	public void setMontantTtc(String montantTtc) {
		this.montantTtc = montantTtc;
	}

	public String getTauxTva() {
		return tauxTva;
	}

	public void setTauxTva(String tauxTva) {
		this.tauxTva = tauxTva;
	}

	public String getRemTx() {
		return remTx;
	}

	public void setRemTx(String remTx) {
		this.remTx = remTx;
	}

	public String getRemHt() {
		return remHt;
	}

	public void setRemHt(String remHt) {
		this.remHt = remHt;
	}

	public String getMtHtApresRem() {
		return mtHtApresRem;
	}

	public void setMtHtApresRem(String mtHtApresRem) {
		this.mtHtApresRem = mtHtApresRem;
	}

	public String getRefLot() {
		return refLot;
	}

	public void setRefLot(String refLot) {
		this.refLot = refLot;
	}

	public String getRefCommandeExterne() {
		return refCommandeExterne;
	}

	public void setRefCommandeExterne(String refCommandeExterne) {
		this.refCommandeExterne = refCommandeExterne;
	}

	public String getIdCommandeExterne() {
		return idCommandeExterne;
	}

	public void setIdCommandeExterne(String idCommandeExterne) {
		this.idCommandeExterne = idCommandeExterne;
	}

	public Date getDateCreationExterne() {
		return dateCreationExterne;
	}

	public void setDateCreationExterne(Date dateCreationExterne) {
		this.dateCreationExterne = dateCreationExterne;
	}

	public Date getDateUpdateExterne() {
		return dateUpdateExterne;
	}

	public void setDateUpdateExterne(Date dateUpdateExterne) {
		this.dateUpdateExterne = dateUpdateExterne;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getLibellecArticle() {
		return libellecArticle;
	}

	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public String getJsonInitial() {
		return jsonInitial;
	}

	public void setJsonInitial(String jsonInitial) {
		this.jsonInitial = jsonInitial;
	}

	public String getValeur1() {
		return valeur1;
	}

	public void setValeur1(String valeur1) {
		this.valeur1 = valeur1;
	}

	public String getValeur2() {
		return valeur2;
	}

	public void setValeur2(String valeur2) {
		this.valeur2 = valeur2;
	}

	public String getValeur3() {
		return valeur3;
	}

	public void setValeur3(String valeur3) {
		this.valeur3 = valeur3;
	}

	public String getValeur4() {
		return valeur4;
	}

	public void setValeur4(String valeur4) {
		this.valeur4 = valeur4;
	}

	public Boolean getTermine() {
		return termine;
	}

	public void setTermine(Boolean termine) {
		this.termine = termine;
	}

	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		this.numLot = numLot;
	}

	public String getLibelleCompteServiceWebExterne() {
		return libelleCompteServiceWebExterne;
	}

	public void setLibelleCompteServiceWebExterne(String libelleCompteServiceWebExterne) {
		this.libelleCompteServiceWebExterne = libelleCompteServiceWebExterne;
	}

	public Boolean getEnteteDoc() {
		return enteteDoc;
	}

	public void setEnteteDoc(Boolean enteteDoc) {
		this.enteteDoc = enteteDoc;
	}

	public String getMontantHtTotalDoc() {
		return montantHtTotalDoc;
	}

	public void setMontantHtTotalDoc(String montantHtTotalDoc) {
		this.montantHtTotalDoc = montantHtTotalDoc;
	}

	public String getMontantTtcTotalDoc() {
		return montantTtcTotalDoc;
	}

	public void setMontantTtcTotalDoc(String montantTtcTotalDoc) {
		this.montantTtcTotalDoc = montantTtcTotalDoc;
	}

	public String getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(String prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public String getMontantTotalLivraisonDoc() {
		return montantTotalLivraisonDoc;
	}

	public void setMontantTotalLivraisonDoc(String montantTotalLivraisonDoc) {
		this.montantTotalLivraisonDoc = montantTotalLivraisonDoc;
	}

	public String getMontantTotalDiscountDoc() {
		return montantTotalDiscountDoc;
	}

	public void setMontantTotalDiscountDoc(String montantTotalDiscountDoc) {
		this.montantTotalDiscountDoc = montantTotalDiscountDoc;
	}

	public Boolean getComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
	}

	public String getEtatLigneExterne() {
		return etatLigneExterne;
	}

	public void setEtatLigneExterne(String etatLigneExterne) {
		this.etatLigneExterne = etatLigneExterne;
	}

	public String getCodeTPaiement() {
		return codeTPaiement;
	}

	public void setCodeTPaiement(String codeTPaiement) {
		this.codeTPaiement = codeTPaiement;
	}

	public String getLiblTPaiement() {
		return liblTPaiement;
	}

	public void setLiblTPaiement(String liblTPaiement) {
		this.liblTPaiement = liblTPaiement;
	}

	public String getRefTypePaiement() {
		return refTypePaiement;
	}

	public void setRefTypePaiement(String refTypePaiement) {
		this.refTypePaiement = refTypePaiement;
	}

}
