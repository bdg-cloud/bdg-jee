package fr.legrain.conformite.model;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.validator.LgrHibernateValidated;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "ta_modele_bareme")
public class TaModeleBareme implements java.io.Serializable {

	private static final long serialVersionUID = 5433520200375856906L;

	private int idModeleBareme;
	
	private String code;
	private String expressionVerifiee;
	private String cheminDoc;
	private byte[] blobFichier;
	private String actioncorrective;
	private Boolean forcage;
	private TaModeleConformite taConformite;

	private String version;


	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaModeleBareme() {
	}

	public TaModeleBareme(int idBareme) {
		this.idModeleBareme = idBareme;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_modele_bareme", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_modele_bareme",table = "ta_modele_bareme", champEntite="idModeleBareme", clazz = TaModeleBareme.class)
	public int getIdModeleBareme() {
		return this.idModeleBareme;
	}

	public void setIdModeleBareme(int idConformite) {
		this.idModeleBareme = idConformite;
	}

	
	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Lob
	@Column(name = "blob_fichier")
	@LgrHibernateValidated(champBd = "blob_fichier",table = "ta_bareme", champEntite="blobFichier", clazz = TaBareme.class)
	public byte[] getBlobFichier() {
		return blobFichier;
	}

	/**
	 * @param blobFichierOriginal the blobFichierOriginal to set
	 */

	public void setBlobFichier(byte[] blobFichier) {
		this.blobFichier = blobFichier;
	}


	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTCivilite) {
		this.quiCree = quiCreeTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTCivilite) {
		this.quandCree = quandCreeTCivilite;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTCivilite) {
		this.quiModif = quiModifTCivilite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTCivilite) {
		this.quandModif = quandModifTCivilite;
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

	@Column(name = "expression_verifiee", length = 50)
	@LgrHibernateValidated(champBd = "expression_verifiee",table = "ta_modele_bareme", champEntite="expressionVerifiee", clazz = TaModeleBareme.class)
	public String getExpressionVerifiee() {
		return expressionVerifiee;
	}

	public void setExpressionVerifiee(String valeurDefaut) {
		this.expressionVerifiee = valeurDefaut;
	}

	@Column(name = "chemin_doc", length = 50)
	@LgrHibernateValidated(champBd = "chemin_doc",table = "ta_modele_bareme", champEntite="cheminDoc", clazz = TaModeleBareme.class)
	public String getCheminDoc() {
		return cheminDoc;
	}

	public void setCheminDoc(String deuxiemeValeur) {
		this.cheminDoc = deuxiemeValeur;
	}

	@Column(name = "action_corrective", length = 50)
	@LgrHibernateValidated(champBd = "action_corrective",table = "ta_modele_bareme", champEntite="actioncorrective", clazz = TaModeleBareme.class)
	public String getActioncorrective() {
		return actioncorrective;
	}

	public void setActioncorrective(String libelleConformite) {
		this.actioncorrective = libelleConformite;
	}

	@Column(name = "forcage")
	public Boolean getForcage() {
		return forcage;
	}

	public void setValeurCalculee(Boolean forcage) {
		this.forcage = forcage;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_modele_conformite")
	@LgrHibernateValidated(champBd = "id_modele_conformite",table = "ta_modele_bareme", champEntite="taConformite.idModeleConformite", clazz = TaModeleConformite.class)
	@XmlElement
	@XmlInverseReference(mappedBy="taModeleBaremes")
	public TaModeleConformite getTaConformite() {
		return taConformite;
	}

	public void setTaConformite(TaModeleConformite taConformite) {
		this.taConformite = taConformite;
	}

	public void setForcage(Boolean forcage) {
		this.forcage = forcage;
	}
	
	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



}
