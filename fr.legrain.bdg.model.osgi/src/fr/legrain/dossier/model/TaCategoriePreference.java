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
@Table(name = "ta_categorie_preference", uniqueConstraints = @UniqueConstraint(columnNames = "code_categorie_preference"))
//@SequenceGenerator(name = "gen_categorie_preference", sequenceName = "num_id_categorie_preference", allocationSize = 1)
//@NamedQueries(value = { 
//		@NamedQuery(name=TaCategoriePreference.QN.FIND_BY_PARENT, query="select a from TaCategoriePreference a where a.categorieMerePreference.codeCategoriePreference = ?"),
//		@NamedQuery(name=TaCategoriePreference.QN.FIND_CATEGORIE_MERE, query="select a from TaCategoriePreference a where a.categorieMerePreference is null"),
//		@NamedQuery(name=TaCategoriePreference.QN.FIND_BY_NEW_OR_UPDATED_AFTER, query="select a from TaCategoriePreference a where a.quandCree >= ? or a.quandModif >= ?")
//		})
public class TaCategoriePreference implements java.io.Serializable {

	private static final long serialVersionUID = -214973272602621711L;

	public static class QN {
//		public static final String FIND_BY_PARENT = "TaCategoriePreference.findByParent";
//		public static final String FIND_CATEGORIE_MERE = "TaCategoriePreference.findCategorieMere";
//		public static final String FIND_BY_NEW_OR_UPDATED_AFTER = "TaCategoriePreference.findByNewOrUpdatedAfter";
	}

	private int idCategoriePreference;
	private String version;
	private TaCategoriePreference categorieMerePreference;

//	  identifiantModule dlib255,
//	  position integer,
	  
	private String codeCategoriePreference;
	private String libelleCategoriePreference;
	private String descriptionCategoriePreference;
	private String identifiantModule;
	private Integer position;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	

	public TaCategoriePreference() {
	}

	public TaCategoriePreference(int idCategoriePreference) {
		this.idCategoriePreference = idCategoriePreference;
	}

	public TaCategoriePreference(int idCategoriePreference, String version,
			TaCategoriePreference categorieMerePreference, String codeCategoriePreference,
			String libelleCategoriePreference, String desciptionCategoriePreference,
			String urlRewritingCategoriePreference,Integer position, String nomImagePreference,
			String quiCreeImagePreference, Date quandCreeImagePreference,
			String quiModifImagePreference, Date quandModifImagePreference,
			String ipAcces, Integer versionObj) {
		super();
		this.idCategoriePreference = idCategoriePreference;
		this.version = version;
		this.categorieMerePreference = categorieMerePreference;
		this.codeCategoriePreference = codeCategoriePreference;
		this.libelleCategoriePreference = libelleCategoriePreference;
		this.descriptionCategoriePreference = desciptionCategoriePreference;
		this.identifiantModule = urlRewritingCategoriePreference;
		this.position = position;
		this.quiCree = quiCreeImagePreference;
		this.quandCree = quandCreeImagePreference;
		this.quiModif = quiModifImagePreference;
		this.quandModif = quandModifImagePreference;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categorie_preference", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_categorie_preference",table = "ta_categorie_preference", champEntite="idCategoriePreference", clazz = TaCategoriePreference.class)
	public int getIdCategoriePreference() {
		return this.idCategoriePreference;
	}

	public void setIdCategoriePreference(int idCategoriePreference) {
		this.idCategoriePreference = idCategoriePreference;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_parent_categorie_preference")
	@LgrHibernateValidated(champBd = "id_parent_categorie_preference",table = "ta_categorie_preference", champEntite="categorieMerePreference", clazz = TaCategoriePreference.class)
	public TaCategoriePreference getCategorieMerePreference() {
		return this.categorieMerePreference;
	}

	public void setCategorieMerePreference(TaCategoriePreference taCategoriePreference) {
		this.categorieMerePreference = taCategoriePreference;
	}

	@Column(name = "code_categorie_preference", precision = 15)
	@LgrHibernateValidated(champBd = "code_categorie_preference",table = "ta_categorie_preference", champEntite="codeCategoriePreference", clazz = TaCategoriePreference.class)
	public String getCodeCategoriePreference() {
		return this.codeCategoriePreference;
	}

	public void setCodeCategoriePreference(String codeCategoriePreference) {
		this.codeCategoriePreference = codeCategoriePreference;
	}
	
	@Column(name = "libelle_categorie_preference", precision = 15)
	@LgrHibernateValidated(champBd = "libelle_categorie_preference",table = "ta_categorie_preference", champEntite="libelleCategoriePreference", clazz = TaCategoriePreference.class)
	public String getLibelleCategoriePreference() {
		return this.libelleCategoriePreference;
	}

	public void setLibelleCategoriePreference(String libelleCategoriePreference) {
		this.libelleCategoriePreference = libelleCategoriePreference;
	}
	
	@Column(name = "description_categorie_preference", precision = 15)
	@LgrHibernateValidated(champBd = "description_categorie_preference",table = "ta_categorie_preference", champEntite="descriptionCategoriePreference", clazz = TaCategoriePreference.class)
	public String getDescriptionCategoriePreference() {
		return this.descriptionCategoriePreference;
	}

	public void setDescriptionCategoriePreference(String desciptionCategoriePreference) {
		this.descriptionCategoriePreference = desciptionCategoriePreference;
	}
	
	@Column(name = "identifiant_module", precision = 15)
	@LgrHibernateValidated(champBd = "identifiant_module",table = "ta_categorie_preference", champEntite="identifiantModule", clazz = TaCategoriePreference.class)
	public String getIdentifiantModule() {
		return this.identifiantModule;
	}

	public void setIdentifiantModule(String urlRewritingCategoriePreference) {
		this.identifiantModule = urlRewritingCategoriePreference;
	}

	@Column(name = "position", precision = 15)
	@LgrHibernateValidated(champBd = "position",table = "ta_categorie_preference", champEntite="position", clazz = TaCategoriePreference.class)
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeCategoriePreference) {
		this.quiCree = quiCreeCategoriePreference;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeCategoriePreference) {
		this.quandCree = quandCreeCategoriePreference;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifCategoriePreference) {
		this.quiModif = quiModifCategoriePreference;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifCategoriePreference) {
		this.quandModif = quandModifCategoriePreference;
	}

	@Column(name = "IP_ACCES", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	
}
