package fr.legrain.document.model;


import java.util.Date;

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

@Entity
@Table(name = "ta_t_moyen_paiement")
public class TaTMoyenPaiement implements java.io.Serializable {

	private static final long serialVersionUID = 912010179867881383L;
	
	private int idTMoyenPaiement;
	
	private String codeTMoyenPaiement;
	private String libTMoyenPaiement;
	private Boolean systeme;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private String version;
	private Integer versionObj;

	public TaTMoyenPaiement() {
	}

	public TaTMoyenPaiement(int idTPaiement) {
		this.idTMoyenPaiement = idTPaiement;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_moyen_paiement", unique = true, nullable = false)
	public int getIdTMoyenPaiement() {
		return this.idTMoyenPaiement;
	}

	public void setIdTMoyenPaiement(int idTPaiement) {
		this.idTMoyenPaiement = idTPaiement;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "code_t_moyen_paiement", unique = true, length = 50)
	public String getCodeTMoyenPaiement() {
		return this.codeTMoyenPaiement;
	}

	public void setCodeTMoyenPaiement(String codeTMoyenPaiement) {
		this.codeTMoyenPaiement = codeTMoyenPaiement;
	}

	@Column(name = "libl_t_moyen_paiement")
	public String getLibTMoyenPaiement() {
		return this.libTMoyenPaiement;
	}

	public void setLibTMoyenPaiement(String libTMoyenPaiement) {
		this.libTMoyenPaiement = libTMoyenPaiement;
	}

	@Column(name = "systeme")
	public Boolean getSysteme() {
		return systeme;
	}

	public void setSysteme(Boolean systeme) {
		this.systeme = systeme;
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
		this.setCodeTMoyenPaiement(codeTMoyenPaiement.toUpperCase());
	}

}
