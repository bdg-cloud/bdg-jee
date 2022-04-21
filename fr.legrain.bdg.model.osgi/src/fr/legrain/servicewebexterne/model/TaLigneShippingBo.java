package fr.legrain.servicewebexterne.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Entity
@Table(name = "ta_ligne_shippingbo")
public class TaLigneShippingBo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7011317516979678836L;
	
	private int idLigneShippingbo;
	
	private String libelle;
	
	private String jsonInitial;
	@Transient
	private TaTiers taTiers;
	@Transient
	private TaArticle taArticle;
	
	@Transient
	private TaArticleDTO taArticleDTO;
	@Transient
	private TaTiersDTO taTiersDTO;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ligne_shippingbo", unique = true, nullable = false)
	public int getIdLigneShippingbo() {
		return idLigneShippingbo;
	}

	public void setIdLigneShippingbo(int idLigneShippingbo) {
		this.idLigneShippingbo = idLigneShippingbo;
	}
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return quiCree;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return quandCree;
	}

	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}
	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return quiModif;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return quandModif;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	@Column(name = "version", length = 20)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	@Column(name = "json_initial")
	public String getJsonInitial() {
		return jsonInitial;
	}

	public void setJsonInitial(String jsonInitial) {
		this.jsonInitial = jsonInitial;
	}
	@Transient
	public TaArticle getTaArticle() {
		return taArticle;
	}
	@Transient
	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	@Transient
	public TaTiers getTaTiers() {
		return taTiers;
	}
	@Transient
	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}
	@Transient
	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}
	@Transient
	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}
	@Transient
	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}
	@Transient
	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}

}
