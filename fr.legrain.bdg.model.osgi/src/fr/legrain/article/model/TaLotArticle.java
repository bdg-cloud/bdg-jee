//package fr.legrain.article.model;
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;
//import javax.persistence.Version;
//
//import org.apache.log4j.Logger;
//
//import fr.legrain.stock.model.TaMouvementStock;
//
//@Entity
//@Table(name = "ta_lot_article")
//public class TaLotArticle  implements Serializable{
//
//
//	private static final long serialVersionUID = 8983076083823180816L;
//
//	private int idLotArticle;
//
//	private TaArticle taArticle;
//	private TaLot taLot;
////	private TaCodeBarre taCodeBarre;
//
//	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;
//	private Set<TaMouvementStock> taMouvementStockes = new HashSet<TaMouvementStock>(0);
//
//
//
//
//	@Transient
//	static Logger logger = Logger.getLogger(TaLotArticle.class.getName());
//
//	public TaLotArticle() {
//	}
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id_lot_article", unique = true, nullable = false)
//	public int getIdLotArticle() {
//		return idLotArticle;
//	}
//
//	public void setIdLotArticle(int lotArticle) {
//		this.idLotArticle = lotArticle;
//	}
//
//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
//	@JoinColumn(name = "id_article")
//	public TaArticle getTaArticle() {
//		return taArticle;
//	}
//
//	public void setTaArticle(TaArticle taArticle) {
//		this.taArticle = taArticle;
//	}
//
//
//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//	@JoinColumn(name = "id_lot")
//	public TaLot getTaLot() {
//		return taLot;
//	}
//
//	public void setTaLot(TaLot taLot) {
//		this.taLot = taLot;
//	}
//
//
////	@OneToOne(fetch = FetchType.EAGER)
////	@JoinColumn(name = "id_ta_code_barre")
////	public TaCodeBarre getTaCodeBarre() {
////		return taCodeBarre;
////	}
////
////	public void setTaCodeBarre(TaCodeBarre codeBarre) {
////		this.taCodeBarre = codeBarre;
////	}
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lotArticle", orphanRemoval=true)
//	public Set<TaMouvementStock> getTaMouvementStockes() {
//		return taMouvementStockes;
//	}
//
//	public void setTaMouvementStockes(Set<TaMouvementStock> taLignesStockes) {
//		this.taMouvementStockes = taLignesStockes;
//	}
//
//	@Version
//	@Column(name = "version_obj")
//	public Integer getVersionObj() {
//		return this.versionObj;
//	}
//
//	public void setVersionObj(Integer versionObj) {
//		this.versionObj = versionObj;
//	}
//
//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCree) {
//		this.quiCree = quiCree;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCree) {
//		this.quandCree = quandCree;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModif) {
//		this.quiModif = quiModif;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModif) {
//		this.quandModif = quandModif;
//	}
//
//	@Column(name = "ip_acces", length = 50)
//	public String getIpAcces() {
//		return this.ipAcces;
//	}
//
//	public void setIpAcces(String ipAcces) {
//		this.ipAcces = ipAcces;
//	}
//
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
//
//
//}
