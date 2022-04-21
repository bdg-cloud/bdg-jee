package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;

public class Tva implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5964473383081803232L;
	
	
	
	private String tTVA; //255
	private String tJournal; //255
	private String tMtTVA; //255
	private String tCpt; //255
	private String tCtrlMt; //255	
	private BigDecimal tTaux; //4
	private Integer tColonne; //1 octet
	private Integer tLigne; //1 octet
	private String tCtrlCl_DC; //255
	private String tCtrlCl_Num; //255	
	private String libelle; //255
	private String typTVA; //255
	
	
	
	
	
	public String gettTVA() {
		return tTVA;
	}
	public void settTVA(String tTVA) {
		this.tTVA = tTVA;
	}
	public String gettJournal() {
		return tJournal;
	}
	public void settJournal(String tJournal) {
		this.tJournal = tJournal;
	}
	public String gettMtTVA() {
		return tMtTVA;
	}
	public void settMtTVA(String tMtTVA) {
		this.tMtTVA = tMtTVA;
	}
	public String gettCpt() {
		return tCpt;
	}
	public void settCpt(String tCpt) {
		this.tCpt = tCpt;
	}
	public String gettCtrlMt() {
		return tCtrlMt;
	}
	public void settCtrlMt(String tCtrlMt) {
		this.tCtrlMt = tCtrlMt;
	}
	public BigDecimal gettTaux() {
		return tTaux;
	}
	public void settTaux(BigDecimal tTaux) {
		this.tTaux = tTaux;
	}
	public Integer gettColonne() {
		return tColonne;
	}
	public void settColonne(Integer tColonne) {
		this.tColonne = tColonne;
	}
	public Integer gettLigne() {
		return tLigne;
	}
	public void settLigne(Integer tLigne) {
		this.tLigne = tLigne;
	}
	public String gettCtrlCl_DC() {
		return tCtrlCl_DC;
	}
	public void settCtrlCl_DC(String tCtrlCl_DC) {
		this.tCtrlCl_DC = tCtrlCl_DC;
	}
	public String gettCtrlCl_Num() {
		return tCtrlCl_Num;
	}
	public void settCtrlCl_Num(String tCtrlCl_Num) {
		this.tCtrlCl_Num = tCtrlCl_Num;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public String getTypTVA() {
		return typTVA;
	}
	public void setTypTVA(String typTVA) {
		this.typTVA = typTVA;
	}
	
	
	
	
}
