package fr.legrain.bdg.webapp.paiement;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.tiers.model.TaTiers;

public class PaiementParam implements Serializable{
	
	public static final String NOM_OBJET_EN_SESSION = "paramPaiement";
	
//	private TaFacture taFacture;
//	private TaTicketDeCaisse taTicketDeCaisse;
//	private TaAcompte taAcompte;
	private IDocumentPayableCB documentPayableCB;
	private BigDecimal montantPaiement;
	private String originePaiement;
	private TaTiers tiersPourReglementLibre;
	private boolean montantLibre = false;
	private String libelle;
	
//	public TaFacture getTaFacture() {
//		return taFacture;
//	}
//	public void setTaFacture(TaFacture taFacture) {
//		this.taFacture = taFacture;
//	}
//	public TaTicketDeCaisse getTaTicketDeCaisse() {
//		return taTicketDeCaisse;
//	}
//	public void setTaTicketDeCaisse(TaTicketDeCaisse taTicketDeCaisse) {
//		this.taTicketDeCaisse = taTicketDeCaisse;
//	}
//	public TaAcompte getTaAcompte() {
//		return taAcompte;
//	}
//	public void setTaAcompte(TaAcompte taAcompte) {
//		this.taAcompte = taAcompte;
//	}
	public BigDecimal getMontantPaiement() {
		return montantPaiement;
	}
	public void setMontantPaiement(BigDecimal montantPaiement) {
		this.montantPaiement = montantPaiement;
	}
	public String getOriginePaiement() {
		return originePaiement;
	}
	public void setOriginePaiement(String originePaiement) {
		this.originePaiement = originePaiement;
	}
	public IDocumentPayableCB getDocumentPayableCB() {
		return documentPayableCB;
	}
	public void setDocumentPayableCB(IDocumentPayableCB documentPayableCB) {
		this.documentPayableCB = documentPayableCB;
	}
	public boolean isMontantLibre() {
		return montantLibre;
	}
	public void setMontantLibre(boolean montantLibre) {
		this.montantLibre = montantLibre;
	}
	public TaTiers getTiersPourReglementLibre() {
		return tiersPourReglementLibre;
	}
	public void setTiersPourReglementLibre(TaTiers tiersPourReglementLibre) {
		this.tiersPourReglementLibre = tiersPourReglementLibre;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}
