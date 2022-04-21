package fr.legrain.document.model;

// Generated Apr 7, 2009 3:27:23 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersComplet;
import fr.legrain.document.dto.IInfosDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.lib.data.ChangeModeEvent;
import fr.legrain.lib.data.ChangeModeListener;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_reglement_traca")

public class TaReglementTraca implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5255112755556949557L;
	public static final String T_SUPPR_REGLEMENT = "suppr_reglement";
	public static final String T_SUPPR_AFFECTATION = "suppr_affectation";
	public static final String T_INSERT_REGLEMENT = "ajout_reglement";
	public static final String T_INSERT_AFFECTATION = "ajout_affectation";
	public static final String T_MODIF_REGLEMENT = "modif_reglement";
	public static final String T_MODIF_AFFECTATION = "modif_affectation";

	
	
	private int idReglementTraca;
	private String version;
	
	private String codeReglement = "";
	private TaTiers taTiers;
	private Date dateReglement;
	private Date dateExport;
	private String codeTPaiement;
	private String libelleReglement;
//	private Integer export = 0;
	private int etat;
	private BigDecimal netTtcCalc = new BigDecimal(0); //montantReglement
	private String codeEtat;
	private String codeAcompte ;
	
	
	private Date dateTraca;
	private String codeFactureAffectation = "";
	private Date dateFactureAffectation;
	private BigDecimal affectation = new BigDecimal(0); 
	private BigDecimal htFactureAffectation = new BigDecimal(0); 
	private BigDecimal tvaFactureAffectation = new BigDecimal(0); 
	private BigDecimal ttcFactureAffectation = new BigDecimal(0); 
	private String codeEtatAffectation;
	private Date dateExportAffectation;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	
	private String typeModif;
	private Integer versionObj;
	
	@Transient
	private int idReglement;
	

	public TaReglementTraca() {
	}


	

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_reglement_traca")
	public int getIdReglementTraca() {
		return this.idReglementTraca;
	}

	public void setIdReglementTraca(int idReglementTraca) {
		this.idReglementTraca = idReglementTraca;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}




	public void setVersion(String version) {
		this.version = version;
	}


	@Column(name = "code_reglement", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "code_reglement",table = "ta_reglement", champEntite="codeReglement", clazz = TaReglementTraca.class)
	public String getCodeReglement() {
		return codeReglement;
	}

	public void setCodeReglement(String codeReglement) {
		this.codeReglement = codeReglement;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tiers")
	@LgrHibernateValidated(champBd = "id_tiers",table = "ta_tiers", champEntite="taTiers.idTiers", clazz = TaTiers.class)
	public TaTiers getTaTiers() {
		return this.taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date_reglement", length = 19)
	@LgrHibernateValidated(champBd = "date_reglement",table = "ta_reglement", champEntite="dateReglement", clazz = TaReglementTraca.class)
	public Date getDateReglement() {
		return dateReglement;
	}

	public void setDateReglement(Date dateReglement) {
		this.dateReglement = dateReglement;
	}



	@Column(name = "libelle_reglement", unique = true, length = 20)
	@LgrHibernateValidated(champBd = "libelle_reglement",table = "ta_reglement", champEntite="libelleReglement", clazz = TaReglementTraca.class)
	public String getLibelleReglement() {
		return libelleReglement;
	}

	public void setLibelleReglement(String libelleReglement) {
		this.libelleReglement = libelleReglement;
	}



	@Column(name = "etat")
	@LgrHibernateValidated(champBd = "etat",table = "ta_reglement", champEntite="etat", clazz = TaReglement.class)
	public int getEtat() {
		return etat;
	}


	public void setEtat(int etat) {
		this.etat = etat;
	}


//	@Column(name = "export")
//	@LgrHibernateValidated(champBd = "export",table = "ta_reglement", champEntite="export", clazz = TaReglementTraca.class)
//	public Integer getExport() {
//		return export;
//	}
//
//	public void setExport(Integer export) {
//		this.export = export;
//	}
	


	@Column(name = "net_ttc_calc", precision = 15)
	@LgrHibernateValidated(champBd = "net_ttc_calc",table = "ta_reglement", champEntite="netTtcCalc", clazz = TaReglementTraca.class)
	public BigDecimal getNetTtcCalc() {
		return netTtcCalc;
	}

	public void setNetTtcCalc(BigDecimal montantReglement) {
		this.netTtcCalc = montantReglement;
	}
	
	

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeRDocument) {
		this.quiCree = quiCreeRDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeRDocument) {
		this.quandCree = quandCreeRDocument;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifRDocument) {
		this.quiModif = quiModifRDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifRDocument) {
		this.quandModif = quandModifRDocument;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	



	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_export")
	@LgrHibernateValidated(champBd = "date_export",table = "ta_reglement",champEntite="ta_remise", clazz = TaReglementTraca.class)
	public Date getDateExport() {
		return dateExport;
	}

	public void setDateExport(Date dateExport) {
		this.dateExport = dateExport;
	}



	@Column(name = "code_t_paiement", unique = true, length = 20)
	public String getCodeTPaiement() {
		return codeTPaiement;
	}




	public void setCodeTPaiement(String codeTPaiement) {
		this.codeTPaiement = codeTPaiement;
	}



	@Column(name = "code_etat", unique = true, length = 20)
	public String getCodeEtat() {
		return codeEtat;
	}




	public void setCodeEtat(String codeEtat) {
		this.codeEtat = codeEtat;
	}



	@Column(name = "code_acompte", unique = true, length = 20)
	public String getCodeAcompte() {
		return codeAcompte;
	}




	public void setCodeAcompte(String codeAcompte) {
		this.codeAcompte = codeAcompte;
	}



	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_traca", length = 19)
	public Date getDateTraca() {
		return dateTraca;
	}




	public void setDateTraca(Date dateTraca) {
		this.dateTraca = dateTraca;
	}



	@Column(name = "code_facture_affectation", unique = true, length = 20)
	public String getCodeFactureAffectation() {
		return codeFactureAffectation;
	}




	public void setCodeFactureAffectation(String codeFactureAffectation) {
		this.codeFactureAffectation = codeFactureAffectation;
	}



	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_facture_affectation")
	public Date getDateFactureAffectation() {
		return dateFactureAffectation;
	}




	public void setDateFactureAffectation(Date dateFactureAffectation) {
		this.dateFactureAffectation = dateFactureAffectation;
	}



	@Column(name = "affectation", precision = 15)
	public BigDecimal getAffectation() {
		return affectation;
	}




	public void setAffectation(BigDecimal affectation) {
		this.affectation = affectation;
	}



	@Column(name = "ht_facture_affectation", precision = 15)
	public BigDecimal getHtFactureAffectation() {
		return htFactureAffectation;
	}




	public void setHtFactureAffectation(BigDecimal htFactureAffectation) {
		this.htFactureAffectation = htFactureAffectation;
	}



	@Column(name = "tva_facture_affectation", precision = 15)
	public BigDecimal getTvaFactureAffectation() {
		return tvaFactureAffectation;
	}




	public void setTvaFactureAffectation(BigDecimal tvaFactureAffectation) {
		this.tvaFactureAffectation = tvaFactureAffectation;
	}



	@Column(name = "ttc_facture_affectation", precision = 15)
	public BigDecimal getTtcFactureAffectation() {
		return ttcFactureAffectation;
	}




	public void setTtcFactureAffectation(BigDecimal ttcFactureAffectation) {
		this.ttcFactureAffectation = ttcFactureAffectation;
	}



	@Column(name = "code_etat_affectation", unique = true, length = 20)
	public String getCodeEtatAffectation() {
		return codeEtatAffectation;
	}




	public void setCodeEtatAffectation(String codeEtatAffectation) {
		this.codeEtatAffectation = codeEtatAffectation;
	}



	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_export_affectation")
	public Date getDateExportAffectation() {
		return dateExportAffectation;
	}




	public void setDateExportAffectation(Date dateExportAffectation) {
		this.dateExportAffectation = dateExportAffectation;
	}



	@Column(name = "type_modif", unique = true, length = 20)
	public String getTypeModif() {
		return typeModif;
	}




	public void setTypeModif(String typeModif) {
		this.typeModif = typeModif;
	}



	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



	@Transient
	public int getIdReglement() {
		return idReglement;
	}

	public void setIdReglement(int idReglement) {
		this.idReglement = idReglement;
	}




}
