package fr.legrain.document.model;

//Generated Apr 9, 2009 12:40:07 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.article.model.TaArticle;

public class TaSumMouvDocument implements java.io.Serializable {


/**
	 * 
	 */
	private static final long serialVersionUID = 4566782414794565556L;
//	private TaTPaiement taTPaiement;
//	private TaTiers taTiers;
//	private String codeDocument;
	private Date dateDocument;
	private TaArticle taArticle;
	private BigDecimal qteLDocument;
	private String u1LDocument;
	private BigDecimal qte2LDocument;
	private String u2LDocument;	
//	private Date dateEchDocument;
//	private Date dateLivDocument;
//	private String libelleDocument;
//	private BigDecimal regleDocument = new BigDecimal(0);
//	private BigDecimal remHtDocument = new BigDecimal(0);
//	private BigDecimal txRemHtDocument = new BigDecimal(0);
//	private BigDecimal remTtcDocument = new BigDecimal(0);
//	private BigDecimal txRemTtcDocument = new BigDecimal(0);
//	private Integer nbEDocument = 0;
//	private Integer ttc = 0;
//	private Integer export = 0;
//	private String commentaire;
	
	
	public TaSumMouvDocument(Date dateDocument, TaArticle taArticle,
			BigDecimal qteLDocument, String u1LDocument, BigDecimal qte2LDocument,
			String u2LDocument) {
		this.dateDocument = dateDocument;
		this.taArticle = taArticle;
		this.qteLDocument = qteLDocument;
		this.u1LDocument = u1LDocument;
		this.qte2LDocument = qte2LDocument;
		this.u2LDocument = u2LDocument;
	}


	public Date getDateDocument() {
		return dateDocument;
	}


	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}


	public TaArticle getTaArticle() {
		return taArticle;
	}


	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}


	public BigDecimal getQteLDocument() {
		return qteLDocument;
	}


	public void setQteLDocument(BigDecimal qteLDocument) {
		this.qteLDocument = qteLDocument;
	}


	public String getU1LDocument() {
		return u1LDocument;
	}


	public void setU1LDocument(String document) {
		u1LDocument = document;
	}


	public BigDecimal getQte2LDocument() {
		return qte2LDocument;
	}


	public void setQte2LDocument(BigDecimal qte2LDocument) {
		this.qte2LDocument = qte2LDocument;
	}


	public String getU2LDocument() {
		return u2LDocument;
	}


	public void setU2LDocument(String document) {
		u2LDocument = document;
	}
	
	



}
