package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import fr.legrain.lib.data.LgrConstantes;

public class Pieces implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4188954388542576061L;

	
	private String pDossier; //8
	private Integer pPiece; //4
	private Date pDate; //8
	private Boolean pAExtourner; //1
	private Boolean pEstExtourne; //1
	
	
	
	private int idDocument=0;
	private String typeDocument=LgrConstantes.C_STR_VIDE;
	private String codeDocument=LgrConstantes.C_STR_VIDE;
	private Date dateDocument=new Date();
	private Date dateEchDocument=new Date();
	private String libelleDocument=LgrConstantes.C_STR_VIDE;
	private String nomTiers=LgrConstantes.C_STR_VIDE;
	private String compte=LgrConstantes.C_STR_VIDE;
	private BigDecimal netTtcCalc=new BigDecimal(0);
	private BigDecimal netHtCalc=new BigDecimal(0);
	private BigDecimal netTvaCalc=new BigDecimal(0);	
	private String libelleReglement;
	private BigDecimal montantReglement;
	
	private List<Lignes> lignes=new LinkedList<>();
	
	
	
	
	public String getpDossier() {
		return pDossier;
	}
	public void setpDossier(String pDossier) {
		this.pDossier = pDossier;
	}
	public Integer getpPiece() {
		return pPiece;
	}
	public void setpPiece(Integer pPiece) {
		this.pPiece = pPiece;
	}
	public Date getpDate() {
		return pDate;
	}
	public void setpDate(Date pDate) {
		this.pDate = pDate;
	}
	public Boolean getpAExtourner() {
		return pAExtourner;
	}
	public void setpAExtourner(Boolean pAExtourner) {
		this.pAExtourner = pAExtourner;
	}
	public Boolean getpEstExtourne() {
		return pEstExtourne;
	}
	public void setpEstExtourne(Boolean pEstExtourne) {
		this.pEstExtourne = pEstExtourne;
	}
	public int getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}
	public String getCodeDocument() {
		return codeDocument;
	}
	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	public Date getDateDocument() {
		return dateDocument;
	}
	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}
	public Date getDateEchDocument() {
		return dateEchDocument;
	}
	public void setDateEchDocument(Date dateEchDocument) {
		this.dateEchDocument = dateEchDocument;
	}
	public String getLibelleDocument() {
		return libelleDocument;
	}
	public void setLibelleDocument(String libelleDocument) {
		this.libelleDocument = libelleDocument;
	}
	public String getNomTiers() {
		return nomTiers;
	}
	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}
	public String getCompte() {
		return compte;
	}
	public void setCompte(String compte) {
		this.compte = compte;
	}
	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}
	public void setNetTtcCalc(BigDecimal netTtcCalc) {
		this.netTtcCalc = netTtcCalc;
	}
	public BigDecimal getNetHtCalc() {
		return netHtCalc;
	}
	public void setNetHtCalc(BigDecimal netHtCalc) {
		this.netHtCalc = netHtCalc;
	}
	public BigDecimal getNetTvaCalc() {
		return netTvaCalc;
	}
	public void setNetTvaCalc(BigDecimal netTvaCalc) {
		this.netTvaCalc = netTvaCalc;
	}
	public String getLibelleReglement() {
		return libelleReglement;
	}
	public void setLibelleReglement(String libelleReglement) {
		this.libelleReglement = libelleReglement;
	}
	public BigDecimal getMontantReglement() {
		return montantReglement;
	}
	public void setMontantReglement(BigDecimal montantReglement) {
		this.montantReglement = montantReglement;
	}
	public String getTypeDocument() {
		return typeDocument;
	}
	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}
	public List<Lignes> getLignes() {
		return lignes;
	}
	public void setLignes(List<Lignes> lignes) {
		this.lignes = lignes;
	}
	
	
	
}
