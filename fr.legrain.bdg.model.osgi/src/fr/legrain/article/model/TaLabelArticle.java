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
import javax.persistence.Table;
import javax.persistence.Version;

import fr.legrain.general.model.TaFichier;
import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_label_article")
//@SequenceGenerator(name = "gen_label_article", sequenceName = "num_id_label_article", allocationSize = 1)
@NamedQueries(value = { 
//		@NamedQuery(name=TaLabelArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER, query="select a from TaLabelArticle a where a.quandCree >= :dateDebut or a.quandModif >= :dateFin")
		})
public class TaLabelArticle extends TaObjetLgr implements java.io.Serializable {
	
	private static final long serialVersionUID = -8978290442922860971L;

	public static class QN {
//		public static final String FIND_BY_NEW_OR_UPDATED_AFTER = "TaLabelArticle.findByNewOrUpdatedAfter";
	}

	private int idLabelArticle;
//	private String version;
	private String codeLabelArticle;
	private String libelleLabelArticle;
	private String desciptionLabelArticle;
	private String cheminImageLabelArticle;
	private String nomImageLabelArticle;
	private byte[] blobLogo;
//	private TaFichier idImageLabelArticle;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
	

	public TaLabelArticle() {
	}

	public TaLabelArticle(int idLabelArticle) {
		this.idLabelArticle = idLabelArticle;
	}

	/**
	 * @param idLabelArticle
	 * @param version
	 * @param codeLabelArticle
	 * @param libelleLabelArticle
	 * @param desciptionLabelArticle
	 * @param cheminImageLabelArticle
	 * @param nomImageLabelArticle
	 * @param idImageLabelArticle
	 * @param quiCree
	 * @param quandCree
	 * @param quiModif
	 * @param quandModif
	 * @param ipAcces
	 * @param versionObj
	 */
	public TaLabelArticle(int idLabelArticle, String version,
			String codeLabelArticle, String libelleLabelArticle,
			String desciptionLabelArticle, String cheminImageLabelArticle,
			String nomImageLabelArticle, TaFichier idImageLabelArticle,
			String quiCree, Date quandCree, String quiModif, Date quandModif,
			String ipAcces, Integer versionObj) {
		super();
		this.idLabelArticle = idLabelArticle;
		this.version = version;
		this.codeLabelArticle = codeLabelArticle;
		this.libelleLabelArticle = libelleLabelArticle;
		this.desciptionLabelArticle = desciptionLabelArticle;
		this.cheminImageLabelArticle = cheminImageLabelArticle;
		this.nomImageLabelArticle = nomImageLabelArticle;
//		this.idImageLabelArticle = idImageLabelArticle;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_label_article", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_label_article",table = "ta_label_article", champEntite="idLabelArticle", clazz = TaLabelArticle.class)
	public int getIdLabelArticle() {
		return this.idLabelArticle;
	}

	public void setIdLabelArticle(int idLabelArticle) {
		this.idLabelArticle = idLabelArticle;
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

	@Column(name = "code_label_article", precision = 15)
	@LgrHibernateValidated(champBd = "code_label_article",table = "ta_label_article", champEntite="codeLabelArticle", clazz = TaLabelArticle.class)
	public String getCodeLabelArticle() {
		return this.codeLabelArticle;
	}

	public void setCodeLabelArticle(String codeLabelArticle) {
		this.codeLabelArticle = codeLabelArticle;
	}
	
	@Column(name = "libelle_label_article", precision = 15)
	@LgrHibernateValidated(champBd = "libelle_label_article",table = "ta_label_article", champEntite="libelleLabelArticle", clazz = TaLabelArticle.class)
	public String getLibelleLabelArticle() {
		return this.libelleLabelArticle;
	}

	public void setLibelleLabelArticle(String libelleLabelArticle) {
		this.libelleLabelArticle = libelleLabelArticle;
	}
	
	@Column(name = "description_label_article", precision = 15)
	@LgrHibernateValidated(champBd = "description_label_article",table = "ta_label_article", champEntite="desciptionLabelArticle", clazz = TaLabelArticle.class)
	public String getDesciptionLabelArticle() {
		return this.desciptionLabelArticle;
	}

	public void setDesciptionLabelArticle(String desciptionLabelArticle) {
		this.desciptionLabelArticle = desciptionLabelArticle;
	}

	@Column(name = "chemin_image_label_article", precision = 15)
	@LgrHibernateValidated(champBd = "chemin_image_label_article",table = "ta_label_article", champEntite="cheminImageLabelArticle", clazz = TaLabelArticle.class)
	public String getCheminImageLabelArticle() {
		return cheminImageLabelArticle;
	}

	public void setCheminImageLabelArticle(String cheminImageArticle) {
		this.cheminImageLabelArticle = cheminImageArticle;
	}

	@Column(name = "nom_image_label_article", precision = 15)
	@LgrHibernateValidated(champBd = "nom_image_label_article",table = "ta_label_article", champEntite="nomImageLabelArticle", clazz = TaLabelArticle.class)
	public String getNomImageLabelArticle() {
		return nomImageLabelArticle;
	}

	public void setNomImageLabelArticle(String nomImageArticle) {
		this.nomImageLabelArticle = nomImageArticle;
	}

//	/**
//	 * @return the idImageLabelArticle
//	 */
//	@Column(name = "id_image_label_article", unique = true, nullable = false)
//	@LgrHibernateValidated(champBd = "id_image_label_article",table = "ta_label_article", champEntite="idImageLabelArticle", clazz = TaLabelArticle.class)
//	public TaFichier getIdImageLabelArticle() {
//		return idImageLabelArticle;
//	}
//
//	/**
//	 * @param idImageLabelArticle the idImageLabelArticle to set
//	 */
//	public void setIdImageLabelArticle(TaFichier idImageLabelArticle) {
//		this.idImageLabelArticle = idImageLabelArticle;
//	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeLabelArticle) {
//		this.quiCree = quiCreeLabelArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeLabelArticle) {
//		this.quandCree = quandCreeLabelArticle;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifLabelArticle) {
//		this.quiModif = quiModifLabelArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifLabelArticle) {
//		this.quandModif = quandModifLabelArticle;
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
	
	@Column(name = "logo")
	public byte[] getBlobLogo() {
		return blobLogo;
	}

	public void setBlobLogo(byte[] blobLogo) {
		this.blobLogo = blobLogo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cheminImageLabelArticle == null) ? 0
						: cheminImageLabelArticle.hashCode());
		result = prime
				* result
				+ ((codeLabelArticle == null) ? 0 : codeLabelArticle.hashCode());
		result = prime
				* result
				+ ((desciptionLabelArticle == null) ? 0
						: desciptionLabelArticle.hashCode());
		result = prime * result + idLabelArticle;
		result = prime * result + ((ipAcces == null) ? 0 : ipAcces.hashCode());
		result = prime
				* result
				+ ((libelleLabelArticle == null) ? 0 : libelleLabelArticle
						.hashCode());
		result = prime
				* result
				+ ((nomImageLabelArticle == null) ? 0 : nomImageLabelArticle
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
		TaLabelArticle other = (TaLabelArticle) obj;
		if (cheminImageLabelArticle == null) {
			if (other.cheminImageLabelArticle != null)
				return false;
		} else if (!cheminImageLabelArticle
				.equals(other.cheminImageLabelArticle))
			return false;
		if (codeLabelArticle == null) {
			if (other.codeLabelArticle != null)
				return false;
		} else if (!codeLabelArticle.equals(other.codeLabelArticle))
			return false;
		if (desciptionLabelArticle == null) {
			if (other.desciptionLabelArticle != null)
				return false;
		} else if (!desciptionLabelArticle.equals(other.desciptionLabelArticle))
			return false;
//		if (idImageLabelArticle != other.idImageLabelArticle)
//			return false;
		if (idLabelArticle != other.idLabelArticle)
			return false;
		if (ipAcces == null) {
			if (other.ipAcces != null)
				return false;
		} else if (!ipAcces.equals(other.ipAcces))
			return false;
		if (libelleLabelArticle == null) {
			if (other.libelleLabelArticle != null)
				return false;
		} else if (!libelleLabelArticle.equals(other.libelleLabelArticle))
			return false;
		if (nomImageLabelArticle == null) {
			if (other.nomImageLabelArticle != null)
				return false;
		} else if (!nomImageLabelArticle.equals(other.nomImageLabelArticle))
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
//		result = prime * result + idLabelArticle;
//		result = prime * result
//				+ ((codeLabelArticle == null) ? 0 : codeLabelArticle.hashCode());
//		result = prime * result
//				+ ((libelleLabelArticle == null) ? 0 : libelleLabelArticle.hashCode());
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
//		final TaLabelArticle other = (TaLabelArticle) obj;
//		if (idLabelArticle != other.idLabelArticle)
//			return false;
//		if (codeLabelArticle == null) {
//			if (other.codeLabelArticle != null)
//				return false;
//		} else if (!codeLabelArticle.equals(other.codeLabelArticle))
//			return false;
//		if (ipAcces == null) {
//			if (other.ipAcces != null)
//				return false;
//		} else if (!ipAcces.equals(other.ipAcces))
//			return false;
//		if (libelleLabelArticle == null) {
//			if (other.libelleLabelArticle != null)
//				return false;
//		} else if (!libelleLabelArticle.equals(other.libelleLabelArticle))
//			return false;
//		return true;
//	}

}
