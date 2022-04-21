package fr.legrain.controle.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_gen_code_ex"/*, uniqueConstraints = @UniqueConstraint(columnNames = "code_controle")*/)

public class TaGenCodeEx implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4792091933280080451L;
	private int idGenCodeEx;
	private String cleGencode;
	private String valeurGenCode;
	private String codeGencode;
	
	@Transient
	private String codeFournisseur;
	
	@Transient
	private String nomFournisseur;
	
	@Transient
	private String codeDocument;
	
	@Transient
	private Date dateDocument;
	
	@Transient
	private String descArticle;
	
	@Transient
	private String codeTypeDocument;
	
	@Transient
	private String valeurVerifie="isa";
	
	@Transient
	private boolean virtuel=false;
	
	@Transient
	private String idArticle;



	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaGenCodeEx() {
	}

	public TaGenCodeEx(int idGenCodeEx) {
		this.idGenCodeEx = idGenCodeEx;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_gen_code_ex", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_gen_code_ex",table = "ta_gen_code_ex", champEntite="idGenCodeEx", clazz = TaGenCodeEx.class)
	public int getIdGenCodeEx() {
		return this.idGenCodeEx;
	}

	public void setIdGenCodeEx(int idGenCode) {
		this.idGenCodeEx = idGenCode;
	}
	
	@Column(name = "valeur_gen_code")
	public String getValeurGenCode() {
		return valeurGenCode;
	}

	public void setValeurGenCode(String entite) {
		this.valeurGenCode = entite;
	}
	
	@Column(name = "cle_gen_code")
	public String getCleGencode() {
		return cleGencode;
	}

	public void setCleGencode(String entite) {
		this.cleGencode = entite;
	}

	@Column(name = "code_gen_code")
	public String getCodeGencode() {
		return codeGencode;
	}

	public void setCodeGencode(String codeGencode) {
		this.codeGencode = codeGencode;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTPaiement) {
		this.quiCree = quiCreeTPaiement;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTPaiement) {
		this.quandCree = quandCreeTPaiement;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTPaiement) {
		this.quiModif = quiModifTPaiement;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTPaiement) {
		this.quandModif = quandModifTPaiement;
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


//	@PrePersist
//	@PreUpdate
	public void beforePost ()throws Exception{
//		this.setCodeTPaiement(codeTPaiement.toUpperCase());
	}

	@Transient
	public String getValeurVerifie() {
		return valeurVerifie;
	}

	@Transient
	public void setValeurVerifie(String valeurVerifie) {
		this.valeurVerifie = valeurVerifie;
	}

	@Transient
	public String getCodeFournisseur() {
		return codeFournisseur;
	}

	@Transient
	public void setCodeFournisseur(String codeFournisseur) {
		this.codeFournisseur = codeFournisseur;
	}

	@Transient
	public String getCodeDocument() {
		return codeDocument;
	}

	@Transient
	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	
	@Transient
	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}

	
	@Transient
	public Date getDateDocument() {
		return dateDocument;
	}
	
	@Transient
	public String getNomFournisseur() {
		return nomFournisseur;
	}

	@Transient
	public void setNomFournisseur(String nomFournisseur) {
		this.nomFournisseur = nomFournisseur;
	}

	@Transient
	public String getDescArticle() {
		return descArticle;
	}

	@Transient
	public void setDescArticle(String descArticle) {
		this.descArticle = descArticle;
	}

	@Transient
	public String getCodeTypeDocument() {
		return codeTypeDocument;
	}

	@Transient
	public void setCodeTypeDocument(String codeTypeDocument) {
		this.codeTypeDocument = codeTypeDocument;
	}

	@Transient
	public boolean isVirtuel() {
		return virtuel;
	}

	@Transient
	public void setVirtuel(boolean virtuel) {
		this.virtuel = virtuel;
	}

	@Transient
	public String getIdArticle() {
		return idArticle;
	}

	@Transient
	public void setIdArticle(String codeArticle) {
		this.idArticle = codeArticle;
	}




}
