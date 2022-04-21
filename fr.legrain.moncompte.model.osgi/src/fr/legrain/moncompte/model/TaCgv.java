package fr.legrain.moncompte.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


@Entity
@Table(name = "ta_cgv")
public class TaCgv implements java.io.Serializable {
	
	private static final long serialVersionUID = 7183843067266339740L;
	
	private Integer idCgv;
	private String cgv;
	private byte[] blobFichier;
	private String url;
	private Date dateCgv;
	private Boolean actif;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaCgv() {
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
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

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getIdCgv() {
		return idCgv;
	}

	public void setIdCgv(Integer idCgv) {
		this.idCgv = idCgv;
	}

	@Column(name="cgv")
	public String getCgv() {
		return cgv;
	}

	public void setCgv(String cgv) {
		this.cgv = cgv;
	}

	@Column(name="actif")
	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	@Column(name="date_cgv")
	public Date getDateCgv() {
		return dateCgv;
	}

	public void setDateCgv(Date dateCgv) {
		this.dateCgv = dateCgv;
	}
	
	@Column(name = "blob_fichier")
	public byte[] getBlobFichier() {
		return blobFichier;
	}

	public void setBlobFichier(byte[] blobFichier) {
		this.blobFichier = blobFichier;
	}
	
	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
