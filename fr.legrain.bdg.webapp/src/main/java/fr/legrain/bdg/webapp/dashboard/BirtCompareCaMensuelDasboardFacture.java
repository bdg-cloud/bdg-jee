package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;

public class BirtCompareCaMensuelDasboardFacture implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 197045711339164600L;
	private String dateDoc=new String();
	private BigDecimal montantHtPeriode1=new BigDecimal(0);
	private BigDecimal montantHtPeriode2=new BigDecimal(0);
	

public class BirtCompareCaMensuelDashboardFacture {
	
}
	
	
	public String getDateDoc() {
		return dateDoc;
	}
	public void setDateDoc(String dateDoc) {
		this.dateDoc = dateDoc;
	}
	
	public BigDecimal getMontantHtPeriode1() {
		return montantHtPeriode1;
	}
	public void setMontantHtPeriode1(BigDecimal montantHtPeriode1) {
		this.montantHtPeriode1 = montantHtPeriode1;
	}
	public BigDecimal getMontantHtPeriode2() {
		return montantHtPeriode2;
	}
	public void setMontantHtPeriode2(BigDecimal montantHtPeriode2) {
		this.montantHtPeriode2 = montantHtPeriode2;
	}
	

}
