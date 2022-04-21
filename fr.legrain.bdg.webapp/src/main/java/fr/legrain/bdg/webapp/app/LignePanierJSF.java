package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.moncompte.ws.TaPrixPerso;
import fr.legrain.moncompte.ws.TaProduit;

public class LignePanierJSF implements Serializable {
	
	private static final long serialVersionUID = 1869201284169362478L;

	private TaProduit produit;
	
	private String typeNiveau;
	
	private Date dateExpiration;
	
	private BigDecimal prixHT;
	private BigDecimal tva;
	private BigDecimal prixTTC;
	
	private BigDecimal prixHTLight;
	private BigDecimal tvaLight;
	private BigDecimal prixTTCLight;
	
	private BigDecimal prixHTParPosteInstalle;
	private BigDecimal tvaParPosteInstalle;
	private BigDecimal prixTTCParPosteInstalle;
	
	private BigDecimal prixHTWs;
	private BigDecimal tvaWs;
	private BigDecimal prixTTCWs;
	
	private BigDecimal prixHTLicence;
	private BigDecimal tvaLicence;
	private BigDecimal prixTTCLicence;
	
	private BigDecimal prixHTMaintenance;
	private BigDecimal tvaMaintenance;
	private BigDecimal prixTTCMaintenance;
	
	private BigDecimal montantHT;
	private BigDecimal montantTVA;
	private BigDecimal montantTTC;
	
	public LignePanierJSF() {
		
	}
	
	public LignePanierJSF(TaProduit produit) {
		
		this.produit = produit;
		
		setTypeNiveau(produit.getTaTNiveau().getLibelle());
		
		setPrixHT(produit.getPrixHT());
		setTva(produit.getTva());
		setPrixTTC(produit.getPrixTTC());
		
		setPrixHTLight(produit.getPrixHTLight());
		setTvaLight(produit.getTvaLight());
		setPrixTTCLight(produit.getPrixTTCLight());
		
		setPrixHTParPosteInstalle(produit.getPrixHTParPosteInstalle());
		setTvaParPosteInstalle(produit.getTvaParPosteInstalle());
		setPrixTTCParPosteInstalle(produit.getPrixTTCParPosteInstalle());
		
		setPrixHTWs(produit.getPrixHTWs());
		setTvaWs(produit.getTvaWs());
		setPrixTTCWs(produit.getPrixTTCWs());
		
		setPrixHTLicence(produit.getPrixHTLicence());
		setTvaLicence(produit.getTvaLicence());
		setPrixTTCLicence(produit.getPrixTTCLicence());
		
		setPrixHTMaintenance(produit.getPrixHTMaintenance());
		setTvaMaintenance(produit.getTvaMaintenance());
		setPrixTTCMaintenance(produit.getPrixTTCMaintenance());
	}
	
	public LignePanierJSF(TaProduit produit, TaPrixPerso prixPerso) {
		
		this.produit = produit;
		setTypeNiveau(produit.getTaTNiveau().getLibelle());
		
		setPrixHT(prixPerso.getPrixHT());
		setTva(prixPerso.getTva());
		setPrixTTC(prixPerso.getPrixTTC());
		
		setPrixHTLight(prixPerso.getPrixHTLight());
		setTvaLight(prixPerso.getTvaLight());
		setPrixTTCLight(prixPerso.getPrixTTCLight());
		
		setPrixHTParPosteInstalle(prixPerso.getPrixHTParPosteInstalle());
		setTvaParPosteInstalle(prixPerso.getTvaParPosteInstalle());
		setPrixTTCParPosteInstalle(prixPerso.getPrixTTCParPosteInstalle());
		
		setPrixHTWs(prixPerso.getPrixHTWs());
		setTvaWs(prixPerso.getTvaWs());
		setPrixTTCWs(prixPerso.getPrixTTCWs());
		
		setPrixHTLicence(prixPerso.getPrixHTLicence());
		setTvaLicence(prixPerso.getTvaLicence());
		setPrixTTCLicence(prixPerso.getPrixTTCLicence());
		
		setPrixHTMaintenance(prixPerso.getPrixHTMaintenance());
		setTvaMaintenance(prixPerso.getTvaMaintenance());
		setPrixTTCMaintenance(prixPerso.getPrixTTCMaintenance());
	}
	
	public TaProduit getProduit() {
		return produit;
	}
	public void setProduit(TaProduit produit) {
		this.produit = produit;
	}
	public BigDecimal getPrixHT() {
		return prixHT;
	}
	public void setPrixHT(BigDecimal prixHT) {
		this.prixHT = prixHT;
	}
	public BigDecimal getTva() {
		return tva;
	}
	public void setTva(BigDecimal tva) {
		this.tva = tva;
	}
	public BigDecimal getPrixTTC() {
		return prixTTC;
	}
	public void setPrixTTC(BigDecimal prixTTC) {
		this.prixTTC = prixTTC;
	}
	public BigDecimal getPrixHTLight() {
		return prixHTLight;
	}
	public void setPrixHTLight(BigDecimal prixHTLight) {
		this.prixHTLight = prixHTLight;
	}
	public BigDecimal getTvaLight() {
		return tvaLight;
	}
	public void setTvaLight(BigDecimal tvaLight) {
		this.tvaLight = tvaLight;
	}
	public BigDecimal getPrixTTCLight() {
		return prixTTCLight;
	}
	public void setPrixTTCLight(BigDecimal prixTTCLight) {
		this.prixTTCLight = prixTTCLight;
	}
	public BigDecimal getPrixHTParPosteInstalle() {
		return prixHTParPosteInstalle;
	}
	public void setPrixHTParPosteInstalle(BigDecimal prixHTParPosteInstalle) {
		this.prixHTParPosteInstalle = prixHTParPosteInstalle;
	}
	public BigDecimal getTvaParPosteInstalle() {
		return tvaParPosteInstalle;
	}
	public void setTvaParPosteInstalle(BigDecimal tvaParPosteInstalle) {
		this.tvaParPosteInstalle = tvaParPosteInstalle;
	}
	public BigDecimal getPrixTTCParPosteInstalle() {
		return prixTTCParPosteInstalle;
	}
	public void setPrixTTCParPosteInstalle(BigDecimal prixTTCParPosteInstalle) {
		this.prixTTCParPosteInstalle = prixTTCParPosteInstalle;
	}
	public BigDecimal getPrixHTWs() {
		return prixHTWs;
	}
	public void setPrixHTWs(BigDecimal prixHTWs) {
		this.prixHTWs = prixHTWs;
	}
	public BigDecimal getTvaWs() {
		return tvaWs;
	}
	public void setTvaWs(BigDecimal tvaWs) {
		this.tvaWs = tvaWs;
	}
	public BigDecimal getPrixTTCWs() {
		return prixTTCWs;
	}
	public void setPrixTTCWs(BigDecimal prixTTCWs) {
		this.prixTTCWs = prixTTCWs;
	}
	public BigDecimal getPrixHTLicence() {
		return prixHTLicence;
	}
	public void setPrixHTLicence(BigDecimal prixHTLicence) {
		this.prixHTLicence = prixHTLicence;
	}
	public BigDecimal getTvaLicence() {
		return tvaLicence;
	}
	public void setTvaLicence(BigDecimal tvaLicence) {
		this.tvaLicence = tvaLicence;
	}
	public BigDecimal getPrixTTCLicence() {
		return prixTTCLicence;
	}
	public void setPrixTTCLicence(BigDecimal prixTTCLicence) {
		this.prixTTCLicence = prixTTCLicence;
	}
	public BigDecimal getPrixHTMaintenance() {
		return prixHTMaintenance;
	}
	public void setPrixHTMaintenance(BigDecimal prixHTMaintenance) {
		this.prixHTMaintenance = prixHTMaintenance;
	}
	public BigDecimal getTvaMaintenance() {
		return tvaMaintenance;
	}
	public void setTvaMaintenance(BigDecimal tvaMaintenance) {
		this.tvaMaintenance = tvaMaintenance;
	}
	public BigDecimal getPrixTTCMaintenance() {
		return prixTTCMaintenance;
	}
	public void setPrixTTCMaintenance(BigDecimal prixTTCMaintenance) {
		this.prixTTCMaintenance = prixTTCMaintenance;
	}

	public String getTypeNiveau() {
		return typeNiveau;
	}

	public void setTypeNiveau(String typeNiveau) {
		this.typeNiveau = typeNiveau;
	}

	public Date getDateExpiration() {
		return dateExpiration;
	}

	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
	}

	public BigDecimal getMontantHT() {
		return montantHT;
	}

	public void setMontantHT(BigDecimal montantHT) {
		this.montantHT = montantHT;
	}

	public BigDecimal getMontantTVA() {
		return montantTVA;
	}

	public void setMontantTVA(BigDecimal montantTVA) {
		this.montantTVA = montantTVA;
	}

	public BigDecimal getMontantTTC() {
		return montantTTC;
	}

	public void setMontantTTC(BigDecimal montantTTC) {
		this.montantTTC = montantTTC;
	}
	
}
