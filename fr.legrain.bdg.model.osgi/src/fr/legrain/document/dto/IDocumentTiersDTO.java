package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface IDocumentTiersDTO {
	
	
	public String getCodeDocument();
	public void setCodeDocument(String codeDocument);
	
	public Integer getId();
//	public void setIdDocument(int idDocument);
	
	public Date getDateDocument();
	public void setDateDocument(Date dateDocument);
	
	
//	public Date getDateLivDocument();
//	public void setDateLivDocument(Date dateLivDocument);
	
	public BigDecimal getNetHtCalc();
	
	public BigDecimal getNetTtcCalc();
	public void setNetTtcCalc(BigDecimal netTtcCalc);
	
	
	public String getLibelleDocument();
	public void setLibelleDocument(String libelleDocument);
	
//	public Boolean getAccepte();
//	public void setAccepte(Boolean accepte);
	
//	public Integer getTtc();
//	public void setTtc(Integer accepte);
	
	
	public String getCodeTiers();
	public void setCodeTiers(String codeTiers);
	
	public String getNomTiers();
	public void setNomTiers(String nomTiers);

	public void setDateExport(Date dateExport);
	public Date getDateExport();
	
//
//	public String getCommentaire();
//	public void setCommentaire(String commentaire);

	public String getTypeDocument();

}
