package fr.legrain.article.dto;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.bdg.model.ModelObject;




public class TaArticleComposeDTO extends ModelObject implements java.io.Serializable {	
		

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8097498310154076450L;
	
	
	private Integer idArticleCompose;
//	private TaArticleDTO taArticleParent;
//	private TaArticleDTO taArticle;
	
	
	//article parent
	private Integer idArticleParent;
	
	//article enfant
	private Integer idArticle;
	private String codeArticle;
	private String libellecArticle;
	private String libellelArticle;
	private Integer idFamille;
	private String codeFamille;
	private String libcFamille;
	private boolean compose;
	private BigDecimal prixPrix;
	private BigDecimal prixttcPrix;
	private BigDecimal prixPrixCalc;
	private BigDecimal prixttcPrixCalc;
	
	
	private BigDecimal qte;
	private BigDecimal qte2;
	private String u1;
	private String u2;


	public TaArticleComposeDTO() {
	}
	
	public TaArticleComposeDTO(Integer idArticleCompose, Integer idArticle, String codeArticle, String libellecArticle, BigDecimal qte, BigDecimal qte2, String u1, String u2, BigDecimal prixPrix,BigDecimal prixttcPrix) {
		super();
		this.idArticleCompose = idArticleCompose;
		this.idArticle = idArticle;
		this.codeArticle = codeArticle;
		this.libellecArticle = libellecArticle;
		this.qte = qte;
		this.qte2 = qte2;
		this.u1 = u1;
		this.u2 = u2;
		this.prixPrix = prixPrix;
		this.prixttcPrix = prixttcPrix;
	}

	
	public Integer getIdArticleCompose() {
		return idArticleCompose;
	}

	public void setIdArticleCompose(Integer idArticleCompose) {
		this.idArticleCompose = idArticleCompose;
	}
	
//	public TaArticleDTO getTaArticleParent() {
//		return taArticleParent;
//	}
//
//	public void setTaArticleParent(TaArticleDTO taArticleParent) {
//		this.taArticleParent = taArticleParent;
//	}
//
//	public TaArticleDTO getTaArticle() {
//		return taArticle;
//	}
//
//	public void setTaArticle(TaArticleDTO taArticle) {
//		this.taArticle = taArticle;
//	}

	public BigDecimal getQte() {
		return qte;
	}

	public void setQte(BigDecimal qte) {
		this.qte = qte;
	}

	public BigDecimal getQte2() {
		return qte2;
	}

	public void setQte2(BigDecimal qte2) {
		this.qte2 = qte2;
	}



	public Integer getIdArticleParent() {
		return idArticleParent;
	}



	public void setIdArticleParent(Integer idArticleParent) {
		this.idArticleParent = idArticleParent;
	}



	public Integer getIdArticle() {
		return idArticle;
	}



	public void setIdArticle(Integer idArticle) {
		this.idArticle = idArticle;
	}



	public String getCodeArticle() {
		return codeArticle;
	}



	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}



	public String getLibellecArticle() {
		return libellecArticle;
	}



	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
	}



	public String getLibellelArticle() {
		return libellelArticle;
	}



	public void setLibellelArticle(String libellelArticle) {
		this.libellelArticle = libellelArticle;
	}



	public Integer getIdFamille() {
		return idFamille;
	}



	public void setIdFamille(Integer idFamille) {
		this.idFamille = idFamille;
	}



	public String getCodeFamille() {
		return codeFamille;
	}



	public void setCodeFamille(String codeFamille) {
		this.codeFamille = codeFamille;
	}



	public String getLibcFamille() {
		return libcFamille;
	}



	public void setLibcFamille(String libcFamille) {
		this.libcFamille = libcFamille;
	}



	public boolean isCompose() {
		return compose;
	}



	public void setCompose(boolean compose) {
		this.compose = compose;
	}



	public BigDecimal getPrixPrix() {
		return prixPrix;
	}



	public void setPrixPrix(BigDecimal prixPrix) {
		this.prixPrix = prixPrix;
	}



	public BigDecimal getPrixttcPrix() {
		return prixttcPrix;
	}



	public void setPrixttcPrix(BigDecimal prixttcPrix) {
		this.prixttcPrix = prixttcPrix;
	}



	public BigDecimal getPrixPrixCalc() {
		return prixPrixCalc;
	}



	public void setPrixPrixCalc(BigDecimal prixPrixCalc) {
		this.prixPrixCalc = prixPrixCalc;
	}



	public BigDecimal getPrixttcPrixCalc() {
		return prixttcPrixCalc;
	}



	public void setPrixttcPrixCalc(BigDecimal prixttcPrixCalc) {
		this.prixttcPrixCalc = prixttcPrixCalc;
	}

	public String getU1() {
		return u1;
	}

	public void setU1(String u1) {
		this.u1 = u1;
	}

	public String getU2() {
		return u2;
	}

	public void setU2(String u2) {
		this.u2 = u2;
	}


}
