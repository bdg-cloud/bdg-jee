package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.legrain.article.model.TaArticle;
import fr.legrain.document.model.TaTLigne;

public interface ILigneDocumentDTO {

	public Integer getIdLDocument();

	public Integer getIdDocument();

	public String getCodeTLigne();
	

	public String getCodeArticle();
	public void setCodeArticle(String codeArticle);
	
	public Integer getNumLigneLDocument();

	public void setNumLigneLDocument(Integer numLigne);

	//public IDocumentTiers getTaDocument(); 
	
	
	public String getLibLDocument();
	public void setLibLDocument(String libLFacture);
	
	public BigDecimal getQteLDocument(); 
	public void setQteLDocument(BigDecimal qteLFacture);
	
	public BigDecimal getQte2LDocument();
	public void setQte2LDocument(BigDecimal qte2LFacture);

	public String getU1LDocument();
	public void setU1LDocument(String u1LFacture);

	public String getU2LDocument();
	public void setU2LDocument(String u2LFacture); 

	public BigDecimal getPrixULDocument();
	public void setPrixULDocument(BigDecimal prixULFacture); 

	public BigDecimal getTauxTvaLDocument();
	public void setTauxTvaLDocument(BigDecimal tauxTvaLFacture);
	
//	public String getCompteLDocument();
//	public void setCompteLDocument(String compteLFacture);
	
	public String getCodeTvaLDocument();
	public void setCodeTvaLDocument(String codeTvaLFacture);
	
	public String getCodeTTvaLDocument();
	public void setCodeTTvaLDocument(String codeTTvaLFacture);

	public BigDecimal getMtHtLDocument();
	public void setMtHtLDocument(BigDecimal mtHtLFacture);
	
	public BigDecimal getMtTtcLDocument();
	public void setMtTtcLDocument(BigDecimal mtTtcLFacture);
	
	public BigDecimal getRemTxLDocument();
	public void setRemTxLDocument(BigDecimal remTxLFacture);
	
	public BigDecimal getRemHtLDocument();
	public void setRemHtLDocument(BigDecimal remHtLFacture);

	public BigDecimal getTxRemHtDocument();
	public void setTxRemHtDocument(BigDecimal txRemHtFacture);

	public BigDecimal getMtHtLApresRemiseGlobaleDocument();
	public void setMtHtLApresRemiseGlobaleDocument(BigDecimal mtHtLApresRemiseGlobaleDocument);
	
	public BigDecimal getMtTtcLApresRemiseGlobaleDocument();
	public void setMtTtcLApresRemiseGlobaleDocument(BigDecimal mtTtcLApresRemiseGlobaleDocument);
	@JsonIgnore
	public String getTypeDocument();
}