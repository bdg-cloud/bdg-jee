//package fr.legrain.article.model;
//
//import java.io.Serializable;
//import java.util.Date;
//
//
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
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Version;
//import javax.persistence.Table;
//import javax.persistence.Transient;
//
//import org.apache.log4j.Logger;
//
//import fr.legrain.stock.model.TaMouvementStock;
//
//@Entity
//@Table(name = "ta_produit")
//
//public class TaProduit implements Serializable{
//
//	private static final long serialVersionUID = -7030811834801383117L;
//
//	private Integer id;
//
//	private TaMouvementStock taMouvementStock;
//	private TaFabrication taFabrication;
//
//	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;
//
//	@Transient
//	static Logger logger = Logger.getLogger(TaProduit.class.getName());
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id_produit", unique = true, nullable = false)
//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer idMatierePremiere) {
//		this.id = idMatierePremiere;
//	}
//
//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name = "id_Stock")
//	public TaMouvementStock getTaMouvementStock() {
//		return taMouvementStock;
//	}
//
//	public void setTaMouvementStock(TaMouvementStock taStock) {
//		this.taMouvementStock = taStock;
//	}
//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
//	@JoinColumn(name = "id_fabrication")
//	public TaFabrication getTaFabrication() {
//		return taFabrication;
//	}
//
//	public void setTaFabrication(TaFabrication taFabrication) {
//		this.taFabrication = taFabrication;
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
//}
