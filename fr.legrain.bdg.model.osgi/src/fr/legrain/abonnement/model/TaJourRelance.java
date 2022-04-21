package fr.legrain.abonnement.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.article.model.TaArticle;

@Entity
@Table(name = "ta_jour_relance")
@NamedQueries(value = { 
		@NamedQuery(name=TaJourRelance.QN.FIND_ALL_LIGHT, query="select new fr.legrain.abonnement.dto.TaJourRelanceDTO(f.idJourRelance,f.taArticle.codeArticle, f.taArticle.idArticle,f.taArticle.libellecArticle, f.nbJour) from TaJourRelance f order by f.idJourRelance")
})
public class TaJourRelance implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4624680353849432626L;
	
	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaJourRelance.findAllLight";
	}

	private int idJourRelance;
	
	private TaArticle taArticle;
	
	private Integer nbJour;
	
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_jour_relance", unique = true, nullable = false)
	public int getIdJourRelance() {
		return idJourRelance;
	}

	public void setIdJourRelance(int idJourRelance) {
		this.idJourRelance = idJourRelance;
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
	
	@Column(name = "version", length = 20)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_article")
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	@Column(name = "nb_jour")
	public Integer getNbJour() {
		return nbJour;
	}

	public void setNbJour(Integer nbJour) {
		this.nbJour = nbJour;
	}



}
