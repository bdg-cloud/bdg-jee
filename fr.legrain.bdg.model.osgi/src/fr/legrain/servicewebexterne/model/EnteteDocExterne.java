package fr.legrain.servicewebexterne.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

public class EnteteDocExterne implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5184420372520735095L;
	
	
	private TaTiers taTiers;
	private TaTiersDTO taTiersDTO;
	private String refTiers;
	private String nomTiers;
	private String prenomTiers;
	
	private String refCommandeExterne; 
	private String idCommandeExterne;
	private Date dateCreationExterne;
	private Date dateUpdateExterne;
	  
	private TaCompteServiceWebExterne taCompteServiceWebExterne;
	private String montantHtTotalDoc;
	private String montantTtcTotalDoc;
	
	private String montantTotalLivraisonDoc;
	private String montantTotalDiscountDoc;
	
	private List<TaLigneServiceWebExterne> listeLigneServiceWebExterne = new ArrayList<TaLigneServiceWebExterne>();
	private List<TaLigneServiceWebExterneDTO> listeLigneServiceWebExterneDTO = new ArrayList<TaLigneServiceWebExterneDTO>();
	
	private Boolean termine;
	
	
	
	
	public TaTiers getTaTiers() {
		return taTiers;
	}

	public String getRefTiers() {
		return refTiers;
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public String getPrenomTiers() {
		return prenomTiers;
	}

	public String getRefCommandeExterne() {
		return refCommandeExterne;
	}

	public String getIdCommandeExterne() {
		return idCommandeExterne;
	}

	public Date getDateCreationExterne() {
		return dateCreationExterne;
	}

	public Date getDateUpdateExterne() {
		return dateUpdateExterne;
	}

	public TaCompteServiceWebExterne getTaCompteServiceWebExterne() {
		return taCompteServiceWebExterne;
	}

	public String getMontantHtTotalDoc() {
		return montantHtTotalDoc;
	}

	public String getMontantTtcTotalDoc() {
		return montantTtcTotalDoc;
	}

	public String getMontantTotalLivraisonDoc() {
		return montantTotalLivraisonDoc;
	}

	public String getMontantTotalDiscountDoc() {
		return montantTotalDiscountDoc;
	}

	public List<TaLigneServiceWebExterne> getListeLigneServiceWebExterne() {
		return listeLigneServiceWebExterne;
	}

	public List<TaLigneServiceWebExterneDTO> getListeLigneServiceWebExterneDTO() {
		return listeLigneServiceWebExterneDTO;
	}

	public Boolean getTermine() {
		return termine;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public void setRefTiers(String refTiers) {
		this.refTiers = refTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}

	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}

	public void setRefCommandeExterne(String refCommandeExterne) {
		this.refCommandeExterne = refCommandeExterne;
	}

	public void setIdCommandeExterne(String idCommandeExterne) {
		this.idCommandeExterne = idCommandeExterne;
	}

	public void setDateCreationExterne(Date dateCreationExterne) {
		this.dateCreationExterne = dateCreationExterne;
	}

	public void setDateUpdateExterne(Date dateUpdateExterne) {
		this.dateUpdateExterne = dateUpdateExterne;
	}

	public void setTaCompteServiceWebExterne(TaCompteServiceWebExterne taCompteServiceWebExterne) {
		this.taCompteServiceWebExterne = taCompteServiceWebExterne;
	}

	public void setMontantHtTotalDoc(String montantHtTotalDoc) {
		this.montantHtTotalDoc = montantHtTotalDoc;
	}

	public void setMontantTtcTotalDoc(String montantTtcTotalDoc) {
		this.montantTtcTotalDoc = montantTtcTotalDoc;
	}

	public void setMontantTotalLivraisonDoc(String montantTotalLivraisonDoc) {
		this.montantTotalLivraisonDoc = montantTotalLivraisonDoc;
	}

	public void setMontantTotalDiscountDoc(String montantTotalDiscountDoc) {
		this.montantTotalDiscountDoc = montantTotalDiscountDoc;
	}

	public void setListeLigneServiceWebExterne(List<TaLigneServiceWebExterne> listeLigneServiceWebExterne) {
		this.listeLigneServiceWebExterne = listeLigneServiceWebExterne;
	}

	public void setListeLigneServiceWebExterneDTO(List<TaLigneServiceWebExterneDTO> listeLigneServiceWebExterneDTO) {
		this.listeLigneServiceWebExterneDTO = listeLigneServiceWebExterneDTO;
	}

	public void setTermine(Boolean termine) {
		this.termine = termine;
	}

	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}



}
