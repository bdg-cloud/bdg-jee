package fr.legrain.document.model;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.legrain.lib.data.LibCalcul;

public class LigneTva implements Serializable {

	private static final long serialVersionUID = -7875167740438996727L;
	
	private String libelle = null;
	private String codeTva = null;
	private BigDecimal tauxTva = null;
	private BigDecimal mtTva = null;
	private BigDecimal mtTvaAvantRemise = null;
	private String numcptTva = null;
	private int nbLigneDocument = 0;
	/**
	 * Somme des lignes de la facture HT ou TTC suivant la propriété TTC du document.
	 */
	private BigDecimal montantTotalHt = null;
	private BigDecimal montantTotalTtc = null;
	private BigDecimal montantTotalHtAvecRemise = null;
	private BigDecimal montantTotalTtcAvecRemise = null;
	
	public BigDecimal getMtTva() {
		return LibCalcul.arrondi(mtTva);
	}
	public void setMtTva(BigDecimal mtTva) {
		this.mtTva = mtTva;
	}
	public BigDecimal getMontantTotalHt() {
		return LibCalcul.arrondi(montantTotalHt);
	}
	public void setMontantTotalHt(BigDecimal mtHt) {
		this.montantTotalHt = mtHt;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getCodeTva() {
		return codeTva;
	}
	public void setCodeTva(String codeTva) {
		this.codeTva = codeTva;
	}
	public BigDecimal getTauxTva() {
		return LibCalcul.arrondi(tauxTva);
	}
	public void setTauxTva(BigDecimal tauxTva) {
		this.tauxTva = tauxTva;
	}
	public int getNbLigneDocument() {
		return nbLigneDocument;
	}
	public void setNbLigneDocument(int nbLigneDocument) {
		this.nbLigneDocument = nbLigneDocument;
	}
	public BigDecimal getMontantTotalTtc() {
		return LibCalcul.arrondi(montantTotalTtc);
	}
	public void setMontantTotalTtc(BigDecimal montantTotalTtc) {
		this.montantTotalTtc = montantTotalTtc;
	}
	public BigDecimal getMontantTotalHtAvecRemise() {
		return LibCalcul.arrondi(montantTotalHtAvecRemise);
	}
	public void setMontantTotalHtAvecRemise(BigDecimal montantTotalHtAvecRemise) {
		this.montantTotalHtAvecRemise = montantTotalHtAvecRemise;
	}
	public BigDecimal getMontantTotalTtcAvecRemise() {
		return LibCalcul.arrondi(montantTotalTtcAvecRemise);
	}
	public void setMontantTotalTtcAvecRemise(BigDecimal montantTotalTtcAvecRemise) {
		this.montantTotalTtcAvecRemise = montantTotalTtcAvecRemise;
	}
	
	public BigDecimal getMtTvaAvantRemise() {
		return LibCalcul.arrondi(mtTvaAvantRemise);
	}
	public void setMtTvaAvantRemise(BigDecimal mtTvaAvantRemise) {
		this.mtTvaAvantRemise = mtTvaAvantRemise;
	}
	public String getNumcptTva() {
		return numcptTva;
	}
	public void setNumcptTva(String numcptTva) {
		this.numcptTva = numcptTva;
	}
}
