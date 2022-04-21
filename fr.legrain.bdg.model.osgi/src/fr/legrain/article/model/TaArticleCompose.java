package fr.legrain.article.model;


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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;



@Entity
@Table(name = "ta_article_compose")
@NamedQueries(value = { 
		@NamedQuery(name=TaArticleCompose.QN.FIND_BY_ID_ARTICLE_PARENT_AND_BY_ID_ARTICLE_ENFANT,
				query="select ac from TaArticleCompose ac where ac.taArticleParent.idArticle = :idArticleParent and ac.taArticle.idArticle = :idArticleEnfant"),
		@NamedQuery(name=TaArticleCompose.QN.FIND_ALL_BY_ID_ARTICLE_ENFANT,
				query="select ac from TaArticleCompose ac where ac.taArticle.idArticle = :idArticleEnfant")
})
public class TaArticleCompose implements java.io.Serializable {
	private static final long serialVersionUID = 7891008218254694479L;
	
	
	public static class QN {
		public static final String FIND_BY_ID_ARTICLE_PARENT_AND_BY_ID_ARTICLE_ENFANT = "TaArticleCompose.findByIdArticleParentAndByIdArticleEnfant";
		public static final String FIND_ALL_BY_ID_ARTICLE_ENFANT="TaArticleCompose.findAllByIdArticleEnfant";
	}
	
	
	private Integer idArticleCompose;
	private TaArticle taArticleParent;
	private TaArticle taArticle;
	
	private BigDecimal qte;
	private BigDecimal qte2;
	
	private String u1;
	private String u2;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaArticleCompose() {
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}
	

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
	}
	
	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_article_compose")
	public Integer getIdArticleCompose() {
		return idArticleCompose;
	}

	public void setIdArticleCompose(Integer idArticleCompose) {
		this.idArticleCompose = idArticleCompose;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_article_parent")
	public TaArticle getTaArticleParent() {
		return taArticleParent;
	}

	public void setTaArticleParent(TaArticle taArticleParent) {
		this.taArticleParent = taArticleParent;
	}
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_article")
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	@Column(name = "qte", precision = 15)
	public BigDecimal getQte() {
		return qte;
	}

	public void setQte(BigDecimal qte) {
		this.qte = qte;
	}
	@Column(name = "qte2", precision = 15)
	public BigDecimal getQte2() {
		return qte2;
	}

	public void setQte2(BigDecimal qte2) {
		this.qte2 = qte2;
	}
	@Column(name = "u1", length = 20)
	public String getU1() {
		return u1;
	}

	public void setU1(String u1) {
		this.u1 = u1;
	}
	@Column(name = "u2", length = 20)
	public String getU2() {
		return u2;
	}

	public void setU2(String u2) {
		this.u2 = u2;
	}


}
