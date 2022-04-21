package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;

import fr.legrain.lib.data.LgrConstantes;

public class Pointage implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8478211926688957170L;
	
	
	
	private String codeDocument=LgrConstantes.C_STR_VIDE;
	private String codeReglement=LgrConstantes.C_STR_VIDE;
	private String tiers=LgrConstantes.C_STR_VIDE;

	private BigDecimal netTtcCalc=new BigDecimal(0);
	private BigDecimal affectation=new BigDecimal(0);
	
	
	
	public String getCodeDocument() {
		return codeDocument;
	}
	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	public String getCodeReglement() {
		return codeReglement;
	}
	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}
	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}
	public void setNetTtcCalc(BigDecimal netTtcCalc) {
		this.netTtcCalc = netTtcCalc;
	}
	public BigDecimal getAffectation() {
		return affectation;
	}
	public void setAffectation(BigDecimal affectation) {
		this.affectation = affectation;
	}
	public String getTiers() {
		return tiers;
	}
	public void setTiers(String tiers) {
		this.tiers = tiers;
	}

	
	
	
	
	
	
}
