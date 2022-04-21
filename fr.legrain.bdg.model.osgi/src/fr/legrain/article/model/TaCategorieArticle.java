package fr.legrain.article.model;

// Generated Sep 1, 2008 3:06:27 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.Set;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.UniqueConstraint;
//import javax.persistence.Version;


import javax.persistence.*;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_categorie_article", uniqueConstraints = @UniqueConstraint(columnNames = "code_categorie_article"))
//@SequenceGenerator(name = "gen_categorie_article", sequenceName = "num_id_categorie_article", allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name=TaCategorieArticle.QN.FIND_BY_PARENT, query="select a from TaCategorieArticle a where a.categorieMereArticle.codeCategorieArticle = :codeCategorieArticle"),
		@NamedQuery(name=TaCategorieArticle.QN.FIND_CATEGORIE_MERE, query="select a from TaCategorieArticle a where a.categorieMereArticle is null"),
		@NamedQuery(name=TaCategorieArticle.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaCategorieArticleDTO(f.idCategorieArticle,f.codeCategorieArticle,cm.codeCategorieArticle,f.libelleCategorieArticle,f.desciptionCategorieArticle,f.urlRewritingCategorieArticle,f.cheminImageCategorieArticle,f.nomImageCategorieArticle) from TaCategorieArticle f left join f.categorieMereArticle cm where f.codeCategorieArticle like :codeCategorieArticle order by f.codeCategorieArticle"),
		@NamedQuery(name=TaCategorieArticle.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaCategorieArticleDTO(f.idCategorieArticle,f.codeCategorieArticle,cm.codeCategorieArticle,f.libelleCategorieArticle,f.desciptionCategorieArticle,f.urlRewritingCategorieArticle,f.cheminImageCategorieArticle,f.nomImageCategorieArticle) from TaCategorieArticle f left join f.categorieMereArticle cm order by f.codeCategorieArticle")
		//@NamedQuery(name=TaCategorieArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER, query="select a from TaCategorieArticle a where a.quandCree >= ? or a.quandModif >= ?")
		})
public class TaCategorieArticle extends TaObjetLgr   implements java.io.Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = -6983890684662014878L;

//	private static final long serialVersionUID = 3910151231507823424L;

	public static class QN {
		public static final String FIND_BY_PARENT = "TaCategorieArticle.findByParent";
		public static final String FIND_CATEGORIE_MERE = "TaCategorieArticle.findCategorieMere";
		public static final String FIND_BY_NEW_OR_UPDATED_AFTER = "TaCategorieArticle.findByNewOrUpdatedAfter";
		public static final String FIND_BY_CODE_LIGHT = "TaCategorieArticle.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaCategorieArticle.findAllFamilleLight";
	}

	private int idCategorieArticle;
//	private String version;
	private TaCategorieArticle categorieMereArticle;
	private String codeCategorieArticle;
	private String libelleCategorieArticle;
	private String desciptionCategorieArticle;
	private String urlRewritingCategorieArticle;
	private String cheminImageCategorieArticle;
	private String nomImageCategorieArticle;
	
	private byte[] blobImageOrigine;
	private byte[] blobImageGrand;
	private byte[] blobImageMoyen;
	private byte[] blobImagePetit;
	private String urlImage;
	private String mime;
	private String altText;
	private String ariaText;
	private String checksum;
	

	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
	private Set<TaArticle> taArticles;
	
	
	public TaCategorieArticle() {
	}

	public TaCategorieArticle(int idCategorieArticle) {
		this.idCategorieArticle = idCategorieArticle;
	}

	public TaCategorieArticle(int idCategorieArticle, String version,
			TaCategorieArticle categorieMereArticle, String codeCategorieArticle,
			String libelleCategorieArticle, String desciptionCategorieArticle,
			String urlRewritingCategorieArticle,String cheminImageArticle, String nomImageArticle,
			String quiCreeImageArticle, Date quandCreeImageArticle,
			String quiModifImageArticle, Date quandModifImageArticle,
			String ipAcces, Integer versionObj) {
		super();
		this.idCategorieArticle = idCategorieArticle;
		this.version = version;
		this.categorieMereArticle = categorieMereArticle;
		this.codeCategorieArticle = codeCategorieArticle;
		this.libelleCategorieArticle = libelleCategorieArticle;
		this.desciptionCategorieArticle = desciptionCategorieArticle;
		this.urlRewritingCategorieArticle = urlRewritingCategorieArticle;
		this.cheminImageCategorieArticle = cheminImageArticle;
		this.nomImageCategorieArticle = nomImageArticle;
		this.quiCree = quiCreeImageArticle;
		this.quandCree = quandCreeImageArticle;
		this.quiModif = quiModifImageArticle;
		this.quandModif = quandModifImageArticle;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categorie_article", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_categorie_article",table = "ta_categorie_article", champEntite="idCategorieArticle", clazz = TaCategorieArticle.class)
	public int getIdCategorieArticle() {
		return this.idCategorieArticle;
	}

	public void setIdCategorieArticle(int idCategorieArticle) {
		this.idCategorieArticle = idCategorieArticle;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_parent_categorie_article")
	@LgrHibernateValidated(champBd = "id_parent_categorie_article",table = "ta_categorie_article", champEntite="categorieMereArticle", clazz = TaCategorieArticle.class)
	public TaCategorieArticle getCategorieMereArticle() {
		return this.categorieMereArticle;
	}

	public void setCategorieMereArticle(TaCategorieArticle taCategorieArticle) {
		this.categorieMereArticle = taCategorieArticle;
	}

	@Column(name = "code_categorie_article", precision = 15)
	@LgrHibernateValidated(champBd = "code_categorie_article",table = "ta_categorie_article", champEntite="codeCategorieArticle", clazz = TaCategorieArticle.class)
	public String getCodeCategorieArticle() {
		return this.codeCategorieArticle;
	}

	public void setCodeCategorieArticle(String codeCategorieArticle) {
		this.codeCategorieArticle = codeCategorieArticle;
	}
	
	@Column(name = "libelle_categorie_article", precision = 15)
	@LgrHibernateValidated(champBd = "libelle_categorie_article",table = "ta_categorie_article", champEntite="libelleCategorieArticle", clazz = TaCategorieArticle.class)
	public String getLibelleCategorieArticle() {
		return this.libelleCategorieArticle;
	}

	public void setLibelleCategorieArticle(String libelleCategorieArticle) {
		this.libelleCategorieArticle = libelleCategorieArticle;
	}
	
	@Column(name = "description_categorie_article", precision = 15)
	@LgrHibernateValidated(champBd = "description_categorie_article",table = "ta_categorie_article", champEntite="desciptionCategorieArticle", clazz = TaCategorieArticle.class)
	public String getDesciptionCategorieArticle() {
		return this.desciptionCategorieArticle;
	}

	public void setDesciptionCategorieArticle(String desciptionCategorieArticle) {
		this.desciptionCategorieArticle = desciptionCategorieArticle;
	}
	
	@Column(name = "url_rewriting_categorie_article", precision = 15)
	@LgrHibernateValidated(champBd = "url_rewriting_categorie_article",table = "ta_categorie_article", champEntite="urlRewritingCategorieArticle", clazz = TaCategorieArticle.class)
	public String getUrlRewritingCategorieArticle() {
		return this.urlRewritingCategorieArticle;
	}

	public void setUrlRewritingCategorieArticle(String urlRewritingCategorieArticle) {
		this.urlRewritingCategorieArticle = urlRewritingCategorieArticle;
	}

	@Column(name = "chemin_image_categorie_article", precision = 15)
	@LgrHibernateValidated(champBd = "chemin_image_categorie_article",table = "ta_categorie_article", champEntite="cheminImageCategorieArticle", clazz = TaCategorieArticle.class)
	public String getCheminImageCategorieArticle() {
		return cheminImageCategorieArticle;
	}

	public void setCheminImageCategorieArticle(String cheminImageArticle) {
		this.cheminImageCategorieArticle = cheminImageArticle;
	}

	@Column(name = "nom_image_categorie_article", precision = 15)
	@LgrHibernateValidated(champBd = "nom_image_categorie_article",table = "ta_categorie_article", champEntite="nomImageCategorieArticle", clazz = TaCategorieArticle.class)
	public String getNomImageCategorieArticle() {
		return nomImageCategorieArticle;
	}

	public void setNomImageCategorieArticle(String nomImageArticle) {
		this.nomImageCategorieArticle = nomImageArticle;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeCategorieArticle) {
//		this.quiCree = quiCreeCategorieArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeCategorieArticle) {
//		this.quandCree = quandCreeCategorieArticle;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifCategorieArticle) {
//		this.quiModif = quiModifCategorieArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifCategorieArticle) {
//		this.quandModif = quandModifCategorieArticle;
//	}
//
//	@Column(name = "IP_ACCES", length = 50)
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
				+ ((categorieMereArticle == null) ? 0 : categorieMereArticle
						.hashCode());
		result = prime
				* result
				+ ((cheminImageCategorieArticle == null) ? 0
						: cheminImageCategorieArticle.hashCode());
		result = prime
				* result
				+ ((codeCategorieArticle == null) ? 0 : codeCategorieArticle
						.hashCode());
		result = prime
				* result
				+ ((desciptionCategorieArticle == null) ? 0
						: desciptionCategorieArticle.hashCode());
		result = prime * result + idCategorieArticle;
		result = prime * result + ((ipAcces == null) ? 0 : ipAcces.hashCode());
		result = prime
				* result
				+ ((libelleCategorieArticle == null) ? 0
						: libelleCategorieArticle.hashCode());
		result = prime
				* result
				+ ((nomImageCategorieArticle == null) ? 0
						: nomImageCategorieArticle.hashCode());
		result = prime * result
				+ ((quandCree == null) ? 0 : quandCree.hashCode());
		result = prime * result
				+ ((quandModif == null) ? 0 : quandModif.hashCode());
		result = prime * result + ((quiCree == null) ? 0 : quiCree.hashCode());
		result = prime * result
				+ ((quiModif == null) ? 0 : quiModif.hashCode());
		result = prime
				* result
				+ ((urlRewritingCategorieArticle == null) ? 0
						: urlRewritingCategorieArticle.hashCode());
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
		TaCategorieArticle other = (TaCategorieArticle) obj;
		if (categorieMereArticle == null) {
			if (other.categorieMereArticle != null)
				return false;
		} else if (!categorieMereArticle.equals(other.categorieMereArticle))
			return false;
		if (cheminImageCategorieArticle == null) {
			if (other.cheminImageCategorieArticle != null)
				return false;
		} else if (!cheminImageCategorieArticle
				.equals(other.cheminImageCategorieArticle))
			return false;
		if (codeCategorieArticle == null) {
			if (other.codeCategorieArticle != null)
				return false;
		} else if (!codeCategorieArticle.equals(other.codeCategorieArticle))
			return false;
		if (desciptionCategorieArticle == null) {
			if (other.desciptionCategorieArticle != null)
				return false;
		} else if (!desciptionCategorieArticle
				.equals(other.desciptionCategorieArticle))
			return false;
		if (idCategorieArticle != other.idCategorieArticle)
			return false;
		if (ipAcces == null) {
			if (other.ipAcces != null)
				return false;
		} else if (!ipAcces.equals(other.ipAcces))
			return false;
		if (libelleCategorieArticle == null) {
			if (other.libelleCategorieArticle != null)
				return false;
		} else if (!libelleCategorieArticle
				.equals(other.libelleCategorieArticle))
			return false;
		if (nomImageCategorieArticle == null) {
			if (other.nomImageCategorieArticle != null)
				return false;
		} else if (!nomImageCategorieArticle
				.equals(other.nomImageCategorieArticle))
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
		if (urlRewritingCategorieArticle == null) {
			if (other.urlRewritingCategorieArticle != null)
				return false;
		} else if (!urlRewritingCategorieArticle
				.equals(other.urlRewritingCategorieArticle))
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

	@ManyToMany(mappedBy = "taCategorieArticles", fetch = FetchType.LAZY)
	public Set<TaArticle> getTaArticles() {
		return taArticles;
	}

	public void setTaArticles(Set<TaArticle> taArticles) {
		this.taArticles = taArticles;
	}
	
	@Column(name = "checksum_image_article")
	@LgrHibernateValidated(champBd = "checksum_image_article",table = "ta_image_article", champEntite="checksum", clazz = TaImageArticle.class)
	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "image_origine")
	public byte[] getBlobImageOrigine() {
		return blobImageOrigine;
	}

	public void setBlobImageOrigine(byte[] blobImage) {
		this.blobImageOrigine = blobImage;
	}
	
	@Column(name = "image_grand")
	public byte[] getBlobImageGrand() {
		return blobImageGrand;
	}

	public void setBlobImageGrand(byte[] blobImageGrand) {
		this.blobImageGrand = blobImageGrand;
	}

	@Column(name = "image_moyen")
	public byte[] getBlobImageMoyen() {
		return blobImageMoyen;
	}

	public void setBlobImageMoyen(byte[] blobImageMoyen) {
		this.blobImageMoyen = blobImageMoyen;
	}

	@Column(name = "image_petit")
	public byte[] getBlobImagePetit() {
		return blobImagePetit;
	}

	public void setBlobImagePetit(byte[] blobImagePetit) {
		this.blobImagePetit = blobImagePetit;
	}

	@Column(name = "url_image")
	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	@Column(name = "mime")
	public String getMime() {
		return mime;
	}
	
	public void setMime(String mime) {
		this.mime = mime;
	}

	@Column(name = "alt_text")
	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	@Column(name = "aria_text")
	public String getAriaText() {
		return ariaText;
	}

	public void setAriaText(String ariaText) {
		this.ariaText = ariaText;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + idCategorieArticle;
//		result = prime * result
//				+ ((codeCategorieArticle == null) ? 0 : codeCategorieArticle.hashCode());
//		result = prime * result
//				+ ((libelleCategorieArticle == null) ? 0 : libelleCategorieArticle.hashCode());
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
//		final TaCategorieArticle other = (TaCategorieArticle) obj;
//		if (idCategorieArticle != other.idCategorieArticle)
//			return false;
//		if (codeCategorieArticle == null) {
//			if (other.codeCategorieArticle != null)
//				return false;
//		} else if (!codeCategorieArticle.equals(other.codeCategorieArticle))
//			return false;
//		if (ipAcces == null) {
//			if (other.ipAcces != null)
//				return false;
//		} else if (!ipAcces.equals(other.ipAcces))
//			return false;
//		if (libelleCategorieArticle == null) {
//			if (other.libelleCategorieArticle != null)
//				return false;
//		} else if (!libelleCategorieArticle.equals(other.libelleCategorieArticle))
//			return false;
//		return true;
//	}

}
