package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;
import java.util.Date;


public class LigneDocFichier implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4253174139697479856L;

	 private Integer numPiece; //5;
	 private String numLigne; //3;
	 private String typePiece; //1;
	 private String reference; //9;
	 private Date datePiece; //8;
	 private String compteLigne; //8;
	 private String libelle; //23;
	 private BigDecimal montantDebit; //12;
	 private BigDecimal montantCredit; //12;
	 private BigDecimal qte1; //9;
	 private BigDecimal qte2; //9;
	 private String codeTVA; //1;
	 private BigDecimal tauxTVA; //6;
	 private BigDecimal montantDebitTVA; //11;
	 private BigDecimal montantCreditTVA; //11;
	 private Date dateEcheancePiece; //8;
	 private String compteColl; //8;
	 private String nomTiers; //30;
	 private String adr1; //30;
	 private String adr2; //30;
	 private String codePostal; //5;
	 private String ville; //25;
	 private String exibiliteTVA; //1;
	 private Date dateLivraisonLigne; //8;
	// private String Enregistrer; //1;
	// private String CompteTva; //8;
	 private String refContrepartie; //9;
	 private Date dateContrepartie; //8;
	 private BigDecimal montantDebitContrepartie; //11;
	 private BigDecimal montantCreditContrepartie; //11;
	 private BigDecimal montantPointage; //11
	 
	 
	 private BigDecimal montantTtcContrepartie; //11;
	 private BigDecimal montantHt; 
	 private BigDecimal montantTtc; 
	 private BigDecimal montantTva;
	 private String codeTVAAgrigest; //7;
	 private String refReglement; //9;
	 
	 
	 
	 
	public Integer getNumPiece() {
		return numPiece;
	}
	public void setNumPiece(Integer numPiece) {
		this.numPiece = numPiece;
	}
	public String getNumLigne() {
		return numLigne;
	}
	public void setNumLigne(String numLigne) {
		this.numLigne = numLigne;
	}
	public String getTypePiece() {
		return typePiece;
	}
	public void setTypePiece(String typePiece) {
		this.typePiece = typePiece;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Date getDatePiece() {
		return datePiece;
	}
	public void setDatePiece(Date datePiece) {
		this.datePiece = datePiece;
	}
	public String getCompteLigne() {
		return compteLigne;
	}
	public void setCompteLigne(String compteLigne) {
		this.compteLigne = compteLigne;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public BigDecimal getMontantDebit() {
		return montantDebit;
	}
	public void setMontantDebit(BigDecimal montantDebit) {
		this.montantDebit = montantDebit;
	}
	public BigDecimal getMontantCredit() {
		return montantCredit;
	}
	public void setMontantCredit(BigDecimal montantCredit) {
		this.montantCredit = montantCredit;
	}
	public BigDecimal getQte1() {
		return qte1;
	}
	public void setQte1(BigDecimal qte1) {
		this.qte1 = qte1;
	}
	public BigDecimal getQte2() {
		return qte2;
	}
	public void setQte2(BigDecimal qte2) {
		this.qte2 = qte2;
	}
	public String getCodeTVA() {
		return codeTVA;
	}
	public void setCodeTVA(String codeTVA) {
		this.codeTVA = codeTVA;
	}
	public BigDecimal getTauxTVA() {
		return tauxTVA;
	}
	public void setTauxTVA(BigDecimal tauxTVA) {
		this.tauxTVA = tauxTVA;
	}
	public BigDecimal getMontantDebitTVA() {
		return montantDebitTVA;
	}
	public void setMontantDebitTVA(BigDecimal montantDebitTVA) {
		this.montantDebitTVA = montantDebitTVA;
	}
	public BigDecimal getMontantCreditTVA() {
		return montantCreditTVA;
	}
	public void setMontantCreditTVA(BigDecimal montantCreditTVA) {
		this.montantCreditTVA = montantCreditTVA;
	}
	public Date getDateEcheancePiece() {
		return dateEcheancePiece;
	}
	public void setDateEcheancePiece(Date dateEcheancePiece) {
		this.dateEcheancePiece = dateEcheancePiece;
	}
	public String getCompteColl() {
		return compteColl;
	}
	public void setCompteColl(String compteColl) {
		this.compteColl = compteColl;
	}
	public String getNomTiers() {
		return nomTiers;
	}
	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}
	public String getAdr1() {
		return adr1;
	}
	public void setAdr1(String adr1) {
		this.adr1 = adr1;
	}
	public String getAdr2() {
		return adr2;
	}
	public void setAdr2(String adr2) {
		this.adr2 = adr2;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getExibiliteTVA() {
		return exibiliteTVA;
	}
	public void setExibiliteTVA(String exibiliteTVA) {
		this.exibiliteTVA = exibiliteTVA;
	}
	public Date getDateLivraisonLigne() {
		return dateLivraisonLigne;
	}
	public void setDateLivraisonLigne(Date dateLivraisonLigne) {
		this.dateLivraisonLigne = dateLivraisonLigne;
	}
	public String getRefContrepartie() {
		return refContrepartie;
	}
	public void setRefContrepartie(String refContrepartie) {
		this.refContrepartie = refContrepartie;
	}
	public Date getDateContrepartie() {
		return dateContrepartie;
	}
	public void setDateContrepartie(Date dateContrepartie) {
		this.dateContrepartie = dateContrepartie;
	}
	public BigDecimal getMontantDebitContrepartie() {
		return montantDebitContrepartie;
	}
	public void setMontantDebitContrepartie(BigDecimal montantDebitContrepartie) {
		this.montantDebitContrepartie = montantDebitContrepartie;
	}
	public BigDecimal getMontantCreditContrepartie() {
		return montantCreditContrepartie;
	}
	public void setMontantCreditContrepartie(BigDecimal montantCreditContrepartie) {
		this.montantCreditContrepartie = montantCreditContrepartie;
	}
	public BigDecimal getMontantPointage() {
		return montantPointage;
	}
	public void setMontantPointage(BigDecimal montantPointage) {
		this.montantPointage = montantPointage;
	}
	public BigDecimal getMontantHt() {
		return montantHt;
	}
	public void setMontantHt(BigDecimal montantHt) {
		this.montantHt = montantHt;
	}
	public BigDecimal getMontantTtc() {
		return montantTtc;
	}
	public void setMontantTtc(BigDecimal montantTtc) {
		this.montantTtc = montantTtc;
	}
	public BigDecimal getMontantTva() {
		return montantTva;
	}
	public void setMontantTva(BigDecimal montantTva) {
		this.montantTva = montantTva;
	}
	public String getCodeTVAAgrigest() {
		return codeTVAAgrigest;
	}
	public void setCodeTVAAgrigest(String codeTVAAgrigest) {
		this.codeTVAAgrigest = codeTVAAgrigest;
	}
	public BigDecimal getMontantTtcContrepartie() {
		return montantTtcContrepartie;
	}
	public void setMontantTtcContrepartie(BigDecimal montantTtcContrepartie) {
		this.montantTtcContrepartie = montantTtcContrepartie;
	}
	public String getRefReglement() {
		return refReglement;
	}
	public void setRefReglement(String refReglement) {
		this.refReglement = refReglement;
	} 
	 
	 
	
	
	
	
}
