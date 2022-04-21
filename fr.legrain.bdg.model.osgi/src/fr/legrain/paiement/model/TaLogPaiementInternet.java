package fr.legrain.paiement.model;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_log_paiement_internet")
@NamedQueries(value = { 
		@NamedQuery(name=TaLogPaiementInternet.QN.FIND_ALL, query="select a from TaLogPaiementInternet a"),
		@NamedQuery(name=TaLogPaiementInternet.QN.FIND_ALL_DATE, query="select a from TaLogPaiementInternet a where a.datePaiement between :debut and :fin"),
		})
public class TaLogPaiementInternet implements java.io.Serializable {

	public static class QN {
		public static final String FIND_ALL = "TaLogPaiementInternet.findAll";
		public static final String FIND_ALL_DATE = "TaLogPaiementInternet.findAllDate";
	}

	private static final long serialVersionUID = -841985444192109353L;

	private int idLogPaiementInternet;
	
	private Date datePaiement;
	private String refPaiementService;
	private BigDecimal montantPaiement;
	private String devise;
	private Integer idReglement;
	private String codeReglement;
	private Integer idTiers;
	private String codeTiers;
	private String nomTiers;
	private Integer idDocument;
	private String codeDocument;
	private String typeDocument;
	
	private String originePaiement;
	private String servicePaiement;
	private String commentaire;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
		
	public TaLogPaiementInternet() {
	}

	public TaLogPaiementInternet(int idLogPaiementInternet) {
		this.idLogPaiementInternet = idLogPaiementInternet;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_log_paiement_internet", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_log_paiement_internet",table = "ta_log_paiement_internet",champEntite="idLogPaiementInternet", clazz = TaLogPaiementInternet.class)
	public int getIdLogPaiementInternet() {
		return this.idLogPaiementInternet;
	}

	public void setIdLogPaiementInternet(int idLogPaiementInternet) {
		this.idLogPaiementInternet = idLogPaiementInternet;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_paiement")
	public Date getDatePaiement() {
		return datePaiement;
	}

	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}

	@Column(name = "ref_paiement_service")
	public String getRefPaiementService() {
		return refPaiementService;
	}

	public void setRefPaiementService(String refPaiementService) {
		this.refPaiementService = refPaiementService;
	}

	@Column(name = "montant_paiement")
	public BigDecimal getMontantPaiement() {
		return montantPaiement;
	}

	public void setMontantPaiement(BigDecimal montantPaiement) {
		this.montantPaiement = montantPaiement;
	}

	@Column(name = "id_reglement")
	public Integer getIdReglement() {
		return idReglement;
	}

	public void setIdReglement(Integer idReglement) {
		this.idReglement = idReglement;
	}

	@Column(name = "code_reglement")
	public String getCodeReglement() {
		return codeReglement;
	}

	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}

	@Column(name = "id_tiers")
	public Integer getIdTiers() {
		return idTiers;
	}

	public void setIdTiers(Integer idTiers) {
		this.idTiers = idTiers;
	}

	@Column(name = "code_tiers")
	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	@Column(name = "nom_tiers")
	public String getNomTiers() {
		return nomTiers;
	}

	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}

	@Column(name = "id_document")
	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	@Column(name = "code_document")
	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}

	@Column(name = "type_document")
	public String getTypeDocument() {
		return typeDocument;
	}

	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}

	@Column(name = "origine_paiement")
	public String getOriginePaiement() {
		return originePaiement;
	}

	public void setOriginePaiement(String originePaiement) {
		this.originePaiement = originePaiement;
	}

	@Column(name = "service_paiement")
	public String getServicePaiement() {
		return servicePaiement;
	}

	public void setServicePaiement(String servicePaiement) {
		this.servicePaiement = servicePaiement;
	}

	@Column(name = "commentaire")
	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
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

	@Column(name = "devise")
	public String getDevise() {
		return devise;
	}

	public void setDevise(String devise) {
		this.devise = devise;
	}

}
