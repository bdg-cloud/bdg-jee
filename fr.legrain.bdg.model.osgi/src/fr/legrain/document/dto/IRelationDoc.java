package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface IRelationDoc {
//	public Integer getExport();
//	public void setExport(Integer export);
	
	public Date getDateExport();
	public void setDateExport(Date dateExport);
	
	public Date getDateVerrouillage();
	public void setDateVerrouillage(Date dateVerrouillage);
	
	public BigDecimal getAffectation();
	public void setAffectation(BigDecimal montant);
	
	public int getId();
	public void setId(int id);


	public String getTypeDocument() ;
	public void setTypeDocument(String typeDocument);
}
