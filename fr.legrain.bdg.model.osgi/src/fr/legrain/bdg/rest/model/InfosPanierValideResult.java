package fr.legrain.bdg.rest.model;

import java.math.BigDecimal;

public class InfosPanierValideResult {
	private int id;
	private String codePanier;
	private String codeCommande;
	private String codeFacture;
	private String codeReglement;
	private String codeReglementInternet;
	private BigDecimal montantHT;
	private BigDecimal montantTTC;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCodePanier() {
		return codePanier;
	}
	public void setCodePanier(String codePanier) {
		this.codePanier = codePanier;
	}
	public String getCodeCommande() {
		return codeCommande;
	}
	public void setCodeCommande(String codeCommande) {
		this.codeCommande = codeCommande;
	}
	public String getCodeFacture() {
		return codeFacture;
	}
	public void setCodeFacture(String codeFacture) {
		this.codeFacture = codeFacture;
	}
	public String getCodeReglement() {
		return codeReglement;
	}
	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}
	public String getCodeReglementInternet() {
		return codeReglementInternet;
	}
	public void setCodeReglementInternet(String codeReglementInternet) {
		this.codeReglementInternet = codeReglementInternet;
	}
	public BigDecimal getMontantHT() {
		return montantHT;
	}
	public void setMontantHT(BigDecimal montantHT) {
		this.montantHT = montantHT;
	}
	public BigDecimal getMontantTTC() {
		return montantTTC;
	}
	public void setMontantTTC(BigDecimal montantTTC) {
		this.montantTTC = montantTTC;
	}
	

	
}
