package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;

public class MontantTva implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5964473383081803232L;
	
	

	private String codeTVA; 
	private BigDecimal tauxTva; 
	private BigDecimal montantHT=BigDecimal.ZERO; 
	private BigDecimal montantTTC=BigDecimal.ZERO; 
	
	
	
	
	public MontantTva() {
		montantHT=BigDecimal.ZERO;
		montantTTC=BigDecimal.ZERO;
	}
	public String getCodeTVA() {
		return codeTVA;
	}
	public void setCodeTVA(String codeTVA) {
		this.codeTVA = codeTVA;
	}
	public BigDecimal getTauxTva() {
		return tauxTva;
	}
	public void setTauxTva(BigDecimal tauxTva) {
		this.tauxTva = tauxTva;
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
