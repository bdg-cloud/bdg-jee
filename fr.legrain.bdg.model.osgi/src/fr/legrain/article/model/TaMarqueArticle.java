package fr.legrain.article.model;

// Generated Sep 1, 2008 3:06:27 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_marque_article")
//@SequenceGenerator(name = "gen_Marque_article", sequenceName = "num_id_Marque_article", allocationSize = 1)
@NamedQueries(value = { 
//		@NamedQuery(name=TaMarqueArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER, query="select a from TaMarqueArticle a where a.quandCree >= :dateDebut or a.quandModif >= :dateFin"),
		@NamedQuery(name=TaMarqueArticle.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaMarqueArticleDTO(f.idMarqueArticle,f.codeMarqueArticle,f.libelleMarqueArticle) from TaMarqueArticle f where f.codeMarqueArticle like :codeMarqueArticle order by f.codeMarqueArticle"),
		@NamedQuery(name=TaMarqueArticle.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaMarqueArticleDTO(f.idMarqueArticle,f.codeMarqueArticle,f.libelleMarqueArticle) from TaMarqueArticle f order by f.codeMarqueArticle")

		})
public class TaMarqueArticle extends TaObjetLgr   implements java.io.Serializable {
	
	private static final long serialVersionUID = -8978290442922860971L;

	public static class QN {
//		public static final String FIND_BY_NEW_OR_UPDATED_AFTER = "TaMarqueArticle.findByNewOrUpdatedAfter";
		public static final String FIND_BY_CODE_LIGHT = "TaMarqueArticle.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaMarqueArticle.findAllLight";
	}

	private int idMarqueArticle;
//	private String version;
	private String codeMarqueArticle;
	private String libelleMarqueArticle;
	private String desciptionMarqueArticle;
	private String cheminImageMarqueArticle;
	private String nomImageMarqueArticle;
	private int idImageMarqueArticle;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
	

	public TaMarqueArticle() {
	}

	public TaMarqueArticle(int idMarqueArticle) {
		this.idMarqueArticle = idMarqueArticle;
	}

	/**
	 * @param idMarqueArticle
	 * @param version
	 * @param codeMarqueArticle
	 * @param libelleMarqueArticle
	 * @param desciptionMarqueArticle
	 * @param cheminImageMarqueArticle
	 * @param nomImageMarqueArticle
	 * @param idImageMarqueArticle
	 * @param quiCree
	 * @param quandCree
	 * @param quiModif
	 * @param quandModif
	 * @param ipAcces
	 * @param versionObj
	 */
	public TaMarqueArticle(int idMarqueArticle, String version,
			String codeMarqueArticle, String libelleMarqueArticle,
			String desciptionMarqueArticle, String cheminImageMarqueArticle,
			String nomImageMarqueArticle, int idImageMarqueArticle,
			String quiCree, Date quandCree, String quiModif, Date quandModif,
			String ipAcces, Integer versionObj) {
		super();
		this.idMarqueArticle = idMarqueArticle;
		this.version = version;
		this.codeMarqueArticle = codeMarqueArticle;
		this.libelleMarqueArticle = libelleMarqueArticle;
		this.desciptionMarqueArticle = desciptionMarqueArticle;
		this.cheminImageMarqueArticle = cheminImageMarqueArticle;
		this.nomImageMarqueArticle = nomImageMarqueArticle;
		this.idImageMarqueArticle = idImageMarqueArticle;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_Marque_article", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_Marque_article",table = "ta_Marque_article", champEntite="idMarqueArticle", clazz = TaMarqueArticle.class)
	public int getIdMarqueArticle() {
		return this.idMarqueArticle;
	}

	public void setIdMarqueArticle(int idMarqueArticle) {
		this.idMarqueArticle = idMarqueArticle;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
	
	@Version
	@Column(name = "version_obj", precision = 15)
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "code_Marque_article", precision = 15)
	@LgrHibernateValidated(champBd = "code_Marque_article",table = "ta_Marque_article", champEntite="codeMarqueArticle", clazz = TaMarqueArticle.class)
	public String getCodeMarqueArticle() {
		return this.codeMarqueArticle;
	}

	public void setCodeMarqueArticle(String codeMarqueArticle) {
		this.codeMarqueArticle = codeMarqueArticle;
	}
	
	@Column(name = "libelle_Marque_article", precision = 15)
	@LgrHibernateValidated(champBd = "libelle_Marque_article",table = "ta_Marque_article", champEntite="libelleMarqueArticle", clazz = TaMarqueArticle.class)
	public String getLibelleMarqueArticle() {
		return this.libelleMarqueArticle;
	}

	public void setLibelleMarqueArticle(String libelleMarqueArticle) {
		this.libelleMarqueArticle = libelleMarqueArticle;
	}
	
	@Column(name = "description_Marque_article", precision = 15)
	@LgrHibernateValidated(champBd = "description_Marque_article",table = "ta_Marque_article", champEntite="desciptionMarqueArticle", clazz = TaMarqueArticle.class)
	public String getDesciptionMarqueArticle() {
		return this.desciptionMarqueArticle;
	}

	public void setDesciptionMarqueArticle(String desciptionMarqueArticle) {
		this.desciptionMarqueArticle = desciptionMarqueArticle;
	}

	@Column(name = "chemin_image_Marque_article", precision = 15)
	@LgrHibernateValidated(champBd = "chemin_image_Marque_article",table = "ta_Marque_article", champEntite="cheminImageMarqueArticle", clazz = TaMarqueArticle.class)
	public String getCheminImageMarqueArticle() {
		return cheminImageMarqueArticle;
	}

	public void setCheminImageMarqueArticle(String cheminImageArticle) {
		this.cheminImageMarqueArticle = cheminImageArticle;
	}

	@Column(name = "nom_image_Marque_article", precision = 15)
	@LgrHibernateValidated(champBd = "nom_image_Marque_article",table = "ta_Marque_article", champEntite="nomImageMarqueArticle", clazz = TaMarqueArticle.class)
	public String getNomImageMarqueArticle() {
		return nomImageMarqueArticle;
	}

	public void setNomImageMarqueArticle(String nomImageArticle) {
		this.nomImageMarqueArticle = nomImageArticle;
	}

	/**
	 * @return the idImageMarqueArticle
	 */
	@Column(name = "id_image_Marque_article", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_image_Marque_article",table = "ta_Marque_article", champEntite="idImageMarqueArticle", clazz = TaMarqueArticle.class)
	public int getIdImageMarqueArticle() {
		return idImageMarqueArticle;
	}

	/**
	 * @param idImageMarqueArticle the idImageMarqueArticle to set
	 */
	public void setIdImageMarqueArticle(int idImageMarqueArticle) {
		this.idImageMarqueArticle = idImageMarqueArticle;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeMarqueArticle) {
//		this.quiCree = quiCreeMarqueArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeMarqueArticle) {
//		this.quandCree = quandCreeMarqueArticle;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifMarqueArticle) {
//		this.quiModif = quiModifMarqueArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifMarqueArticle) {
//		this.quandModif = quandModifMarqueArticle;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cheminImageMarqueArticle == null) ? 0
						: cheminImageMarqueArticle.hashCode());
		result = prime
				* result
				+ ((codeMarqueArticle == null) ? 0 : codeMarqueArticle.hashCode());
		result = prime
				* result
				+ ((desciptionMarqueArticle == null) ? 0
						: desciptionMarqueArticle.hashCode());
		result = prime * result + idImageMarqueArticle;
		result = prime * result + idMarqueArticle;
		result = prime * result + ((ipAcces == null) ? 0 : ipAcces.hashCode());
		result = prime
				* result
				+ ((libelleMarqueArticle == null) ? 0 : libelleMarqueArticle
						.hashCode());
		result = prime
				* result
				+ ((nomImageMarqueArticle == null) ? 0 : nomImageMarqueArticle
						.hashCode());
		result = prime * result
				+ ((quandCree == null) ? 0 : quandCree.hashCode());
		result = prime * result
				+ ((quandModif == null) ? 0 : quandModif.hashCode());
		result = prime * result + ((quiCree == null) ? 0 : quiCree.hashCode());
		result = prime * result
				+ ((quiModif == null) ? 0 : quiModif.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result
				+ ((versionObj == null) ? 0 : versionObj.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaMarqueArticle other = (TaMarqueArticle) obj;
		if (cheminImageMarqueArticle == null) {
			if (other.cheminImageMarqueArticle != null)
				return false;
		} else if (!cheminImageMarqueArticle
				.equals(other.cheminImageMarqueArticle))
			return false;
		if (codeMarqueArticle == null) {
			if (other.codeMarqueArticle != null)
				return false;
		} else if (!codeMarqueArticle.equals(other.codeMarqueArticle))
			return false;
		if (desciptionMarqueArticle == null) {
			if (other.desciptionMarqueArticle != null)
				return false;
		} else if (!desciptionMarqueArticle.equals(other.desciptionMarqueArticle))
			return false;
		if (idImageMarqueArticle != other.idImageMarqueArticle)
			return false;
		if (idMarqueArticle != other.idMarqueArticle)
			return false;
		if (ipAcces == null) {
			if (other.ipAcces != null)
				return false;
		} else if (!ipAcces.equals(other.ipAcces))
			return false;
		if (libelleMarqueArticle == null) {
			if (other.libelleMarqueArticle != null)
				return false;
		} else if (!libelleMarqueArticle.equals(other.libelleMarqueArticle))
			return false;
		if (nomImageMarqueArticle == null) {
			if (other.nomImageMarqueArticle != null)
				return false;
		} else if (!nomImageMarqueArticle.equals(other.nomImageMarqueArticle))
			return false;
		if (quandCree == null) {
			if (other.quandCree != null)
				return false;
		} else if (!quandCree.equals(other.quandCree))
			return false;
		if (quandModif == null) {
			if (other.quandModif != null)
				return false;
		} else if (!quandModif.equals(other.quandModif))
			return false;
		if (quiCree == null) {
			if (other.quiCree != null)
				return false;
		} else if (!quiCree.equals(other.quiCree))
			return false;
		if (quiModif == null) {
			if (other.quiModif != null)
				return false;
		} else if (!quiModif.equals(other.quiModif))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (versionObj == null) {
			if (other.versionObj != null)
				return false;
		} else if (!versionObj.equals(other.versionObj))
			return false;
		return true;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + idMarqueArticle;
//		result = prime * result
//				+ ((codeMarqueArticle == null) ? 0 : codeMarqueArticle.hashCode());
//		result = prime * result
//				+ ((libelleMarqueArticle == null) ? 0 : libelleMarqueArticle.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		final TaMarqueArticle other = (TaMarqueArticle) obj;
//		if (idMarqueArticle != other.idMarqueArticle)
//			return false;
//		if (codeMarqueArticle == null) {
//			if (other.codeMarqueArticle != null)
//				return false;
//		} else if (!codeMarqueArticle.equals(other.codeMarqueArticle))
//			return false;
//		if (ipAcces == null) {
//			if (other.ipAcces != null)
//				return false;
//		} else if (!ipAcces.equals(other.ipAcces))
//			return false;
//		if (libelleMarqueArticle == null) {
//			if (other.libelleMarqueArticle != null)
//				return false;
//		} else if (!libelleMarqueArticle.equals(other.libelleMarqueArticle))
//			return false;
//		return true;
//	}

}
