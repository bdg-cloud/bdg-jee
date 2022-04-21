package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.validator.LgrHibernateValidated;

public interface IDocumentDTO {

	Integer getId();
	void setId(Integer id);

	String getCodeDocument();
	
	void setCodeDocument(String code);

	Integer getIdTiers();

	String getCodeTiers();

	String getCodeTTvaDoc();
	
	public String getNomTiers();
	
	public String getCodeTPaiement();
	
	public void setNomTiers(String NOM_TIERS);
	
	public void setCodeTiers(String CODE_TIERS);
	
	public Date getDateDocument();
	public void setDateDocument(Date dateDocument);
	
	public Date getDateLivDocument();
	public void setDateLivDocument(Date dateLivDocument);
	
	public void setCodeTTvaDoc(String codeTTvaDoc);
	
	public void setCodeTPaiement(String CODE_T_PAIEMENT);

	public Boolean getTtc();
	public void setTtc(Boolean ttc);
	
	public String getLibelleDocument();
	public void setLibelleDocument(String libelleDocument);
	
	public String getCodeEtat();
	public void setCodeEtat(String taEtat);
	
	public String getLiblEtat();
	public void setLiblEtat(String taEtat);
	
//	public Boolean getExport();
	public Date getDateExport();
	public Date getDateVerrouillage() ;
	public boolean getEstMisADisposition();
	public boolean setEstMisADisposition(boolean mad);
	
	public String getPrenomTiers();
	public String getNomEntreprise();
	public BigDecimal getNetHtCalc();
	public BigDecimal getNetTvaCalc();
	public BigDecimal getNetTtcCalc();
	
	@JsonIgnore
	public String getTypeDocument();
	

}