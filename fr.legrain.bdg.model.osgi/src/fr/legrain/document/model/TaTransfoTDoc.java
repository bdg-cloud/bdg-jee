package fr.legrain.document.model;

// Generated Apr 7, 2009 3:27:23 PM by Hibernate Tools 3.2.0.CR1

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
@Table(name = "ta_transfo_t_doc")
public class TaTransfoTDoc implements java.io.Serializable {

	
	private static final long serialVersionUID = 8229975152161533078L;
	
	private int idTransfoTDoc;
//	private String version;
	private TaTDoc taTDocOrg;
	private TaTDoc taTDocDest;
	private Boolean possible;
	private Boolean accepte;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;

	public TaTransfoTDoc() {
	}

	public TaTransfoTDoc(int idTransfoTDoc) {
		this.idTransfoTDoc = idTransfoTDoc;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_transfo_t_doc", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_transfo_t_doc",table = "ta_transfo_t_doc", champEntite="idTransfoTDoc", clazz = TaTransfoTDoc.class)
	public int getIdTransfoTDoc() {
		return this.idTransfoTDoc;
	}

	public void setIdTransfoTDoc(int idTransfoTDoc) {
		this.idTransfoTDoc = idTransfoTDoc;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_t_doc_org")
	public TaTDoc getTaTDocOrg() {
		return taTDocOrg;
	}

	public void setTaTDocOrg(TaTDoc taTDocOrg) {
		this.taTDocOrg = taTDocOrg;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_t_doc_dest")
	public TaTDoc getTaTDocDest() {
		return taTDocDest;
	}

	public void setTaTDocDest(TaTDoc taTDocDest) {
		this.taTDocDest = taTDocDest;
	}

	@Column(name = "possible")
	public Boolean getPossible() {
		return possible;
	}

	public void setPossible(Boolean possible) {
		this.possible = possible;
	}

	@Column(name = "accepte")
	public Boolean getAccepte() {
		return accepte;
	}

	public void setAccepte(Boolean accepte) {
		this.accepte = accepte;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeRDocument) {
//		this.quiCree = quiCreeRDocument;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeRDocument) {
//		this.quandCree = quandCreeRDocument;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifRDocument) {
//		this.quiModif = quiModifRDocument;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifRDocument) {
//		this.quandModif = quandModifRDocument;
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

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	

	
}
