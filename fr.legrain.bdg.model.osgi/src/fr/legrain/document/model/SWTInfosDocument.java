package fr.legrain.document.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name = "gen_infos_facture", sequenceName = "num_id_infos_document", allocationSize = 1)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SWTInfosDocument implements Serializable {
	
	private static final long serialVersionUID = -5924273677593035064L;
	
	protected int idInfosDocument;
	protected Integer versionObj;
	protected String quiCree;
	protected Date quandCree;
	protected String quiModif;
	protected Date quandModif;
	protected String ipAcces;
	protected String version;

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_infos_facture")
	@Column(name = "id_infos_document", unique = true, nullable = false)
	public int getIdInfosDocument() {
		return this.idInfosDocument;
	}

	public void setIdInfosDocument(int idInfosFacture) {
		this.idInfosDocument = idInfosFacture;
	}
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}
	
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeInfosAcompte) {
		this.quiCree = quiCreeInfosAcompte;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeInfosAcompte) {
		this.quandCree = quandCreeInfosAcompte;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifInfosAcompte) {
		this.quiModif = quiModifInfosAcompte;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifInfosAcompte) {
		this.quandModif = quandModifInfosAcompte;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
	
//	@PrePersist
	public void beforeInsert()throws Exception{
//passage ejb
//		TaVersionDAO daoVersion =new TaVersionDAO();
//		TaVersion version= daoVersion.findInstance();
//		this.setQuiCreeInfosDocument("");
//		this.setQuandCreeInfosDocument(new Date());
//		this.setQuiModifInfosDocument("");
//		this.setQuandModifInfosDocument(new Date());
//		this.setIpAcces("");
//		this.setVersion(version.getNumVersion());
	}
	
//	@PreUpdate
	public void beforeUpdate()throws Exception{
//passage ejb
//		TaVersionDAO daoVersion =new TaVersionDAO();
//		TaVersion version= daoVersion.findInstance();
//		this.setQuiModifInfosDocument("");
//		this.setQuandModifInfosDocument(new Date());
//		this.setIpAcces("");
//		this.setVersion(version.getNumVersion());
	}
}
