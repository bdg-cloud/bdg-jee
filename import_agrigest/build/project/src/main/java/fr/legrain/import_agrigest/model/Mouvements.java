package fr.legrain.import_agrigest.model;

import java.math.BigDecimal;
import java.util.Date;

public class Mouvements  implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4188350060591365052L;
	
	
	
	private String mDossier; //8
	private Integer mPiece; //4
	private Date mDate; //8
	private Integer mLig; //4
	private Integer mOrdre; //1 Octet
	private String mCpt; //8
	private String mActi; //4
	private BigDecimal mMtDeb; //8
	private BigDecimal mMtCre; //8
	private String mD_C; //1
	private BigDecimal mQte1; //8
	private BigDecimal mQte2; //8
	private Integer mLettrage; //4
	private Date mEcheance; //8
	private Integer mFolio; //2
	private String mCptCtr; //8
	private String mActCtr; //4
	private Integer mCouleur; //4
	private Boolean mAN; //1
	
	
	
	
	public String getmDossier() {
		return mDossier;
	}
	public void setmDossier(String mDossier) {
		this.mDossier = mDossier;
	}
	public Integer getmPiece() {
		return mPiece;
	}
	public void setmPiece(Integer mPiece) {
		this.mPiece = mPiece;
	}
	public Date getmDate() {
		return mDate;
	}
	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}
	public Integer getmLig() {
		return mLig;
	}
	public void setmLig(Integer mLig) {
		this.mLig = mLig;
	}
	public Integer getmOrdre() {
		return mOrdre;
	}
	public void setmOrdre(Integer mOrdre) {
		this.mOrdre = mOrdre;
	}
	public String getmCpt() {
		return mCpt;
	}
	public void setmCpt(String mCpt) {
		this.mCpt = mCpt;
	}
	public String getmActi() {
		return mActi;
	}
	public void setmActi(String mActi) {
		this.mActi = mActi;
	}
	public BigDecimal getmMtDeb() {
		return mMtDeb;
	}
	public void setmMtDeb(BigDecimal mMtDeb) {
		this.mMtDeb = mMtDeb;
	}
	public BigDecimal getmMtCre() {
		return mMtCre;
	}
	public void setmMtCre(BigDecimal mMtCre) {
		this.mMtCre = mMtCre;
	}
	public String getmD_C() {
		return mD_C;
	}
	public void setmD_C(String mD_C) {
		this.mD_C = mD_C;
	}
	public BigDecimal getmQte1() {
		return mQte1;
	}
	public void setmQte1(BigDecimal mQte1) {
		this.mQte1 = mQte1;
	}
	public BigDecimal getmQte2() {
		return mQte2;
	}
	public void setmQte2(BigDecimal mQte2) {
		this.mQte2 = mQte2;
	}
	public Integer getmLettrage() {
		return mLettrage;
	}
	public void setmLettrage(Integer mLettrage) {
		this.mLettrage = mLettrage;
	}
	public Date getmEcheance() {
		return mEcheance;
	}
	public void setmEcheance(Date mEcheance) {
		this.mEcheance = mEcheance;
	}
	public Integer getmFolio() {
		return mFolio;
	}
	public void setmFolio(Integer mFolio) {
		this.mFolio = mFolio;
	}
	public String getmCptCtr() {
		return mCptCtr;
	}
	public void setmCptCtr(String mCptCtr) {
		this.mCptCtr = mCptCtr;
	}
	public String getmActCtr() {
		return mActCtr;
	}
	public void setmActCtr(String mActCtr) {
		this.mActCtr = mActCtr;
	}
	public Integer getmCouleur() {
		return mCouleur;
	}
	public void setmCouleur(Integer mCouleur) {
		this.mCouleur = mCouleur;
	}
	public Boolean getmAN() {
		return mAN;
	}
	public void setmAN(Boolean mAN) {
		this.mAN = mAN;
	}
	
	
	
	
	
}
