package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;

public class Dossiers  implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3462656652870605476L;
	
	
	
	private String dDossier; // 8
	private String dExpl; // 6
	private Date dDtDebEx; // 8
	private Date dDtFinEx; // 8
	private Date dDtArrete; // 8
	private String dBqCpt; // 8
	private BigDecimal dBqVal; // 8
	private Integer dBqFolio1; // 2
	private Integer dCptOuvert; // 4
	private String dMethodeInventaire; // 25
	
	
	
	public String getdDossier() {
		return dDossier;
	}
	public void setdDossier(String dDossier) {
		this.dDossier = dDossier;
	}
	public String getdExpl() {
		return dExpl;
	}
	public void setdExpl(String dExpl) {
		this.dExpl = dExpl;
	}
	public Date getdDtDebEx() {
		return dDtDebEx;
	}
	public void setdDtDebEx(Date dDtDebEx) {
		this.dDtDebEx = dDtDebEx;
	}
	public Date getdDtFinEx() {
		return dDtFinEx;
	}
	public void setdDtFinEx(Date dDtFinEx) {
		this.dDtFinEx = dDtFinEx;
	}
	public Date getdDtArrete() {
		return dDtArrete;
	}
	public void setdDtArrete(Date dDtArrete) {
		this.dDtArrete = dDtArrete;
	}
	public String getdBqCpt() {
		return dBqCpt;
	}
	public void setdBqCpt(String dBqCpt) {
		this.dBqCpt = dBqCpt;
	}
	public BigDecimal getdBqVal() {
		return dBqVal;
	}
	public void setdBqVal(BigDecimal dBqVal) {
		this.dBqVal = dBqVal;
	}
	public Integer getdBqFolio1() {
		return dBqFolio1;
	}
	public void setdBqFolio1(Integer dBqFolio1) {
		this.dBqFolio1 = dBqFolio1;
	}
	public Integer getdCptOuvert() {
		return dCptOuvert;
	}
	public void setdCptOuvert(Integer dCptOuvert) {
		this.dCptOuvert = dCptOuvert;
	}
	public String getdMethodeInventaire() {
		return dMethodeInventaire;
	}
	public void setdMethodeInventaire(String dMethodeInventaire) {
		this.dMethodeInventaire = dMethodeInventaire;
	}
	
	
	
}
