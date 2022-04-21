package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;

public class Activite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4253174139697479856L;

	
	private String aDossier; //8
	private String aActi; //4
	private String aLib; //20
	private BigDecimal aQte; //4
	private String aUnit; //2
	
	
	
	public String getaDossier() {
		return aDossier;
	}
	public void setaDossier(String aDossier) {
		this.aDossier = aDossier;
	}
	public String getaActi() {
		return aActi;
	}
	public void setaActi(String aActi) {
		this.aActi = aActi;
	}
	public String getaLib() {
		return aLib;
	}
	public void setaLib(String aLib) {
		this.aLib = aLib;
	}
	public BigDecimal getaQte() {
		return aQte;
	}
	public void setaQte(BigDecimal aQte) {
		this.aQte = aQte;
	}
	public String getaUnit() {
		return aUnit;
	}
	public void setaUnit(String aUnit) {
		this.aUnit = aUnit;
	}
	
	
}
