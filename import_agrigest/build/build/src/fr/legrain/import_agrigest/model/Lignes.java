package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Date;

import fr.legrain.lib.data.LgrConstantes;

public class Lignes implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7176756229228707615L;
	
	private String lDossier;  //8
	private Integer lPiece;  //4
	private Date lDate;  //8
	private Integer lLig;  //4
	private String lTva; //5
	private String lLib; //35
	private String lGene; //1
	private String lAna; //1
	private String lJournal; //4
	private String lMtTVA; //1
	
	
	
	
	private Integer idDocument;
//	private String codeTLigne;
	private Integer numLigneLDocument;
	private String compteLDocument;
	private String tiersLDocument;
	private String libLDocument;
	private BigDecimal qteLDocument;
	private BigDecimal qte2LDocument;
	private BigDecimal tauxTvaLDocument;
	private String codeTvaLDocument;
	private String codeTTvaLDocument;
	private BigDecimal mtHtLApresRemiseGlobaleDocument;
	private BigDecimal mtTtcLApresRemiseGlobaleDocument;
	private BigDecimal mtTvaLApresRemiseGlobaleDocument;
	private String codeReglement=LgrConstantes.C_STR_VIDE;
	private Pieces pieces;
	
	public String getlDossier() {
		return lDossier;
	}
	public void setlDossier(String lDossier) {
		this.lDossier = lDossier;
	}
	public Integer getlPiece() {
		return lPiece;
	}
	public void setlPiece(Integer lPiece) {
		this.lPiece = lPiece;
	}
	public Date getlDate() {
		return lDate;
	}
	public void setlDate(Date lDate) {
		this.lDate = lDate;
	}
	public Integer getlLig() {
		return lLig;
	}
	public void setlLig(Integer lLig) {
		this.lLig = lLig;
	}
	public String getlTva() {
		return lTva;
	}
	public void setlTva(String lTva) {
		this.lTva = lTva;
	}
	public String getlLib() {
		return lLib;
	}
	public void setlLib(String lLib) {
		this.lLib = lLib;
	}
	public String getlGene() {
		return lGene;
	}
	public void setlGene(String lGene) {
		this.lGene = lGene;
	}
	public String getlAna() {
		return lAna;
	}
	public void setlAna(String lAna) {
		this.lAna = lAna;
	}
	public String getlJournal() {
		return lJournal;
	}
	public void setlJournal(String lJournal) {
		this.lJournal = lJournal;
	}
	public String getlMtTVA() {
		return lMtTVA;
	}
	public void setlMtTVA(String lMtTVA) {
		this.lMtTVA = lMtTVA;
	}
	public Integer getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}
	public Integer getNumLigneLDocument() {
		return numLigneLDocument;
	}
	public void setNumLigneLDocument(Integer numLigneLDocument) {
		this.numLigneLDocument = numLigneLDocument;
	}
	public String getLibLDocument() {
		return libLDocument;
	}
	public void setLibLDocument(String libLDocument) {
		this.libLDocument = libLDocument;
	}
	public BigDecimal getQteLDocument() {
		return qteLDocument;
	}
	public void setQteLDocument(BigDecimal qteLDocument) {
		this.qteLDocument = qteLDocument;
	}
	public BigDecimal getQte2LDocument() {
		return qte2LDocument;
	}
	public void setQte2LDocument(BigDecimal qte2lDocument) {
		qte2LDocument = qte2lDocument;
	}
	public BigDecimal getTauxTvaLDocument() {
		return tauxTvaLDocument;
	}
	public void setTauxTvaLDocument(BigDecimal tauxTvaLDocument) {
		this.tauxTvaLDocument = tauxTvaLDocument;
	}
	public String getCodeTvaLDocument() {
		return codeTvaLDocument;
	}
	public void setCodeTvaLDocument(String codeTvaLDocument) {
		this.codeTvaLDocument = codeTvaLDocument;
	}
	public BigDecimal getMtHtLApresRemiseGlobaleDocument() {
		return mtHtLApresRemiseGlobaleDocument;
	}
	public void setMtHtLApresRemiseGlobaleDocument(BigDecimal mtHtLApresRemiseGlobaleDocument) {
		this.mtHtLApresRemiseGlobaleDocument = mtHtLApresRemiseGlobaleDocument;
	}
	public BigDecimal getMtTtcLApresRemiseGlobaleDocument() {
		return mtTtcLApresRemiseGlobaleDocument;
	}
	public void setMtTtcLApresRemiseGlobaleDocument(BigDecimal mtTtcLApresRemiseGlobaleDocument) {
		this.mtTtcLApresRemiseGlobaleDocument = mtTtcLApresRemiseGlobaleDocument;
	}
	public BigDecimal getMtTvaLApresRemiseGlobaleDocument() {
		return mtTvaLApresRemiseGlobaleDocument;
	}
	public void setMtTvaLApresRemiseGlobaleDocument(BigDecimal mtTvaLApresRemiseGlobaleDocument) {
		this.mtTvaLApresRemiseGlobaleDocument = mtTvaLApresRemiseGlobaleDocument;
	}
	public Pieces getPieces() {
		return pieces;
	}
	public void setPieces(Pieces pieces) {
		this.pieces = pieces;
	}
	public String getCompteLDocument() {
		return compteLDocument;
	}
	public void setCompteLDocument(String compteLDocument) {
		this.compteLDocument = compteLDocument;
	}
	public String getCodeTTvaLDocument() {
		return codeTTvaLDocument;
	}
	public void setCodeTTvaLDocument(String codeTTvaLDocument) {
		this.codeTTvaLDocument = codeTTvaLDocument;
	}
	public String getTiersLDocument() {
		return tiersLDocument;
	}
	public void setTiersLDocument(String tiersLDocument) {
		this.tiersLDocument = tiersLDocument;
	}
	public String getCodeReglement() {
		return codeReglement;
	}
	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}
	
	
	
	
	
	
	

	
	
}
