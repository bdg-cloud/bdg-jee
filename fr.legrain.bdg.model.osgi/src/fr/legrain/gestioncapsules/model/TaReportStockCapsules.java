package fr.legrain.gestioncapsules.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_report_stock_capsules")
//@SequenceGenerator(name = "gen_report_stock_capsules", sequenceName = "num_id_report_stock_capsules", allocationSize = 1)
public class TaReportStockCapsules implements java.io.Serializable {

	private static final long serialVersionUID = -6066940853937989945L;
	
	private int idReportStock;
	private String version;
	private TaTitreTransport taTitreTransport;
	private Date dateDebReportStock;
	private Date dateFinReportStock;
	private BigDecimal qte1ReportStock;
	private String unite1ReportStock;
	private BigDecimal qte2ReportStock;
	private String unite2ReportStock;
	private String quiCreeReportStock;
	private Date quandCreeReportStock;
	private String quiModifReportStock;
	private Date quandModifReportStock;
	private String ipAcces;
	private Integer versionObj;

	public TaReportStockCapsules() {
	}

	public TaReportStockCapsules(int idReportStock) {
		this.idReportStock = idReportStock;
	}

	public TaReportStockCapsules(int idReportStock, TaTitreTransport taTitreTransport,
			Date dateDebReportStock, Date dateFinReportStock,
			BigDecimal qteReportStock, String uniteReportStock,
			String quiCreeReportStock, Date quandCreeReportStock,
			String quiModifReportStock, Date quandModifReportStock,
			String ipAcces, Integer versionObj) {
		this.idReportStock = idReportStock;
		this.taTitreTransport = taTitreTransport;
		this.dateDebReportStock = dateDebReportStock;
		this.dateFinReportStock = dateFinReportStock;
		this.qte1ReportStock = qteReportStock;
		this.unite1ReportStock = uniteReportStock;
		this.quiCreeReportStock = quiCreeReportStock;
		this.quandCreeReportStock = quandCreeReportStock;
		this.quiModifReportStock = quiModifReportStock;
		this.quandModifReportStock = quandModifReportStock;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_report_stock_capsules", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_report_stock_capsules",table = "ta_report_stock_capsules", champEntite="idReportStock", clazz = TaReportStockCapsules.class)
	public int getIdReportStock() {
		return this.idReportStock;
	}

	public void setIdReportStock(int idReportStock) {
		this.idReportStock = idReportStock;
	}

//	@Version
	@Column(name = "VERSION", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_titre_transport_report_stock")
	@LgrHibernateValidated(champBd = "id_titre_transport",table = "ta_titre_transport", champEntite="taTitreTransport.idTitreTransport", clazz = TaTitreTransport.class)
	public TaTitreTransport getTaTitreTransport() {
		return this.taTitreTransport;
	}

	public void setTaTitreTransport(TaTitreTransport taTitreTransport) {
		this.taTitreTransport = taTitreTransport;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_deb_report_stock", length = 19)
	@LgrHibernateValidated(champBd = "date_deb_report_stock",table = "ta_report_stock_capsules", champEntite="dateDebReportStock", clazz = TaReportStockCapsules.class)
	public Date getDateDebReportStock() {
		return this.dateDebReportStock;
	}

	public void setDateDebReportStock(Date dateDebReportStock) {
		this.dateDebReportStock = dateDebReportStock;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_fin_report_stock", length = 19)
	@LgrHibernateValidated(champBd = "date_fin_report_stock",table = "ta_report_stock_capsules", champEntite="dateFinReportStock", clazz = TaReportStockCapsules.class)
	public Date getDateFinReportStock() {
		return this.dateFinReportStock;
	}

	public void setDateFinReportStock(Date dateFinReportStock) {
		this.dateFinReportStock = dateFinReportStock;
	}

	@Column(name = "qte1_report_stock", precision = 15)
	@LgrHibernateValidated(champBd = "qte1_report_stock",table = "ta_report_stock_capsules", champEntite="qte1ReportStock", clazz = TaReportStockCapsules.class)
	public BigDecimal getQte1ReportStock() {
		return this.qte1ReportStock;
	}

	public void setQte1ReportStock(BigDecimal qte1ReportStock) {
		this.qte1ReportStock = qte1ReportStock;
	}

	@Column(name = "unite1_report_stock", length = 20)
	@LgrHibernateValidated(champBd = "unite1_report_stock",table = "ta_report_stock_capsules", champEntite="unite1ReportStock", clazz = TaReportStockCapsules.class)
	public String getUnite1ReportStock() {
		return this.unite1ReportStock;
	}

	public void setUnite1ReportStock(String unite1ReportStock) {
		this.unite1ReportStock = unite1ReportStock;
	}
	
	@Column(name = "qte2_report_stock", precision = 15)
	@LgrHibernateValidated(champBd = "qte2_report_stock",table = "ta_report_stock_capsules", champEntite="qte2ReportStock", clazz = TaReportStockCapsules.class)
	public BigDecimal getQte2ReportStock() {
		return this.qte2ReportStock;
	}

	public void setQte2ReportStock(BigDecimal qte2ReportStock) {
		this.qte2ReportStock = qte2ReportStock;
	}

	@Column(name = "unite2_report_stock", length = 20)
	@LgrHibernateValidated(champBd = "unite2_report_stock",table = "ta_report_stock_capsules", champEntite="unite2ReportStock", clazz = TaReportStockCapsules.class)
	public String getUnite2ReportStock() {
		return this.unite2ReportStock;
	}

	public void setUnite2ReportStock(String unite2ReportStock) {
		this.unite2ReportStock = unite2ReportStock;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCreeReportStock() {
		return this.quiCreeReportStock;
	}

	public void setQuiCreeReportStock(String quiCreeReportStock) {
		this.quiCreeReportStock = quiCreeReportStock;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCreeReportStock() {
		return this.quandCreeReportStock;
	}

	public void setQuandCreeReportStock(Date quandCreeReportStock) {
		this.quandCreeReportStock = quandCreeReportStock;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModifReportStock() {
		return this.quiModifReportStock;
	}

	public void setQuiModifReportStock(String quiModifReportStock) {
		this.quiModifReportStock = quiModifReportStock;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModifReportStock() {
		return this.quandModifReportStock;
	}

	public void setQuandModifReportStock(Date quandModifReportStock) {
		this.quandModifReportStock = quandModifReportStock;
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

}
