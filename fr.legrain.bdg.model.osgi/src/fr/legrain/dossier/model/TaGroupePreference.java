package fr.legrain.dossier.model;

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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import fr.legrain.validator.LgrHibernateValidated;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_groupe_preference")
//@SequenceGenerator(name = "gen_groupe_preference", sequenceName = "num_id_groupe_preference", allocationSize = 1)
//@NamedQueries(value = { 
//		@NamedQuery(name=TaCategorieArticle.QN.FIND_BY_PARENT, query="select a from TaCategorieArticle a where a.groupeMereArticle.codeCategorieArticle = ?"),
//		@NamedQuery(name=TaCategorieArticle.QN.FIND_CATEGORIE_MERE, query="select a from TaCategorieArticle a where a.groupeMereArticle is null"),
//		@NamedQuery(name=TaCategorieArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER, query="select a from TaCategorieArticle a where a.quandCree >= ? or a.quandModif >= ?")
//		})
public class TaGroupePreference implements java.io.Serializable {

	private static final long serialVersionUID = -9090661879541440344L;

	public static class QN {
		public static final String FIND_BY_PARENT = "TaCategorieArticle.findByParent";
		public static final String FIND_CATEGORIE_MERE = "TaCategorieArticle.findCategorieMere";
		public static final String FIND_BY_NEW_OR_UPDATED_AFTER = "TaCategorieArticle.findByNewOrUpdatedAfter";
	}

	private int idGroupeArticle;
	private String version;
	private String libelleGroupeArticle;
	private String descriptionGroupeArticle;
	private String position;
	private String composantAffichage;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	

	public TaGroupePreference() {
	}

	public TaGroupePreference(int idCategorieArticle) {
		this.idGroupeArticle = idCategorieArticle;
	}

	public TaGroupePreference(int idCategorieArticle, String version,
			TaGroupePreference groupeMereArticle, String codeCategorieArticle,
			String libelleCategorieArticle, String desciptionCategorieArticle,
			String urlRewritingCategorieArticle,String cheminImageArticle, String nomImageArticle,
			String quiCreeImageArticle, Date quandCreeImageArticle,
			String quiModifImageArticle, Date quandModifImageArticle,
			String ipAcces, Integer versionObj) {
		super();
		this.idGroupeArticle = idCategorieArticle;
		this.version = version;
		this.libelleGroupeArticle = libelleCategorieArticle;
		this.descriptionGroupeArticle = desciptionCategorieArticle;
		this.position = urlRewritingCategorieArticle;
		this.composantAffichage = cheminImageArticle;
		this.quiCree = quiCreeImageArticle;
		this.quandCree = quandCreeImageArticle;
		this.quiModif = quiModifImageArticle;
		this.quandModif = quandModifImageArticle;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_groupe_preference", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_groupe_preference",table = "ta_groupe_preference", champEntite="idGroupeArticle", clazz = TaGroupePreference.class)
	public int getIdGroupeArticle() {
		return this.idGroupeArticle;
	}

	public void setIdGroupeArticle(int idCategorieArticle) {
		this.idGroupeArticle = idCategorieArticle;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Version
	@Column(name = "version_obj", precision = 15)
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
	
	@Column(name = "libelle_groupe_preference", precision = 15)
	@LgrHibernateValidated(champBd = "libelle_groupe_preference",table = "ta_groupe_preference", champEntite="libelleGroupeArticle", clazz = TaGroupePreference.class)
	public String getLibelleGroupeArticle() {
		return this.libelleGroupeArticle;
	}

	public void setLibelleGroupeArticle(String libelleCategorieArticle) {
		this.libelleGroupeArticle = libelleCategorieArticle;
	}
	
	@Column(name = "description_groupe_preference", precision = 15)
	@LgrHibernateValidated(champBd = "description_groupe_preference",table = "ta_groupe_preference", champEntite="descriptionGroupeArticle", clazz = TaGroupePreference.class)
	public String getDescriptionGroupeArticle() {
		return this.descriptionGroupeArticle;
	}

	public void setDescriptionGroupeArticle(String desciptionCategorieArticle) {
		this.descriptionGroupeArticle = desciptionCategorieArticle;
	}
	
	@Column(name = "position", precision = 15)
	@LgrHibernateValidated(champBd = "position",table = "ta_groupe_preference", champEntite="position", clazz = TaGroupePreference.class)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String urlRewritingCategorieArticle) {
		this.position = urlRewritingCategorieArticle;
	}

	@Column(name = "composant_affichage", precision = 15)
	@LgrHibernateValidated(champBd = "composantAffichage",table = "ta_groupe_preference", champEntite="composantAffichage", clazz = TaGroupePreference.class)
	public String getComposantAffichage() {
		return composantAffichage;
	}

	public void setComposantAffichage(String cheminImageArticle) {
		this.composantAffichage = cheminImageArticle;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeCategorieArticle) {
		this.quiCree = quiCreeCategorieArticle;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeCategorieArticle) {
		this.quandCree = quandCreeCategorieArticle;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifCategorieArticle) {
		this.quiModif = quiModifCategorieArticle;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifCategorieArticle) {
		this.quandModif = quandModifCategorieArticle;
	}

	@Column(name = "IP_ACCES", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idGroupeArticle;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaGroupePreference other = (TaGroupePreference) obj;
		if (idGroupeArticle != other.idGroupeArticle)
			return false;
		return true;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + idGroupeArticle;
//		result = prime * result
//				+ ((codeCategorieArticle == null) ? 0 : codeCategorieArticle.hashCode());
//		result = prime * result
//				+ ((libelleGroupeArticle == null) ? 0 : libelleGroupeArticle.hashCode());
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
//		if (idGroupeArticle != other.idCategorieArticle)
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
//		if (libelleGroupeArticle == null) {
//			if (other.libelleCategorieArticle != null)
//				return false;
//		} else if (!libelleGroupeArticle.equals(other.libelleCategorieArticle))
//			return false;
//		return true;
//	}

}
