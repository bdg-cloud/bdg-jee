package fr.legrain.article.model;

// Generated Sep 1, 2008 3:06:27 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_conditionnement_article")
//@SequenceGenerator(name = "gen_conditionnement_article", sequenceName = "num_id_conditionnement_article", allocationSize = 1)
public class TaConditionnementArticle extends TaObjetLgr   implements java.io.Serializable {

	private static final long serialVersionUID = 9074943858704211288L;
	
	private int idConditionnementArticle;
//	private String version;
	//private TaTConditionnement taTConditionnement;
	private TaArticle taArticle;
	private String code;
	private String libelle;
	private Integer nbUnite;
	private BigDecimal hauteur;
	private BigDecimal longueur;
	private BigDecimal largeur;
	private BigDecimal poids;
	private BigDecimal reduction;
	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;

	public TaConditionnementArticle() {
	}

	public TaConditionnementArticle(int id) {
		this.idConditionnementArticle = id;
	}

	public TaConditionnementArticle(int id, String code, String libelle,
			String quiCree, Date quandCree, String quiModif,
			Date quandModif, String ipAcces) {
		this.idConditionnementArticle = id;
		this.code = libelle;
		this.libelle = libelle;
		this.quiCree = quiCree;
		this.quandCree = quandCree;
		this.quiModif = quiModif;
		this.quandModif = quandModif;
		this.ipAcces = ipAcces;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id",table = "ta_conditionnement_article", champEntite="idConditionnementArticle", clazz = TaConditionnementArticle.class)
	public int getIdConditionnementArticle() {
		return this.idConditionnementArticle;
	}

	public void setIdConditionnementArticle(int id) {
		this.idConditionnementArticle = id;
	}

//	//@Version
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

	@Column(name = "code", length = 1)
	@LgrHibernateValidated(champBd = "code",table = "ta_conditionnement_article", champEntite="code", clazz = TaConditionnementArticle.class)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "libelle")
	@LgrHibernateValidated(champBd = "libelle",table = "ta_conditionnement_article", champEntite="libelle", clazz = TaConditionnementArticle.class)
	public String getLibelle() {
		return this.libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_t_conditionnement")
////	@Column(name = "id_t_conditionnement")
//	@LgrHibernateValidated(champ = "id_t_conditionnement",table = "ta_conditionnement_article",clazz = TaTConditionnement.class)
//	public TaTConditionnement getTaTConditionnement() {
//		return taTConditionnement;
//	}
//
//	public void setTaTConditionnement(TaTConditionnement taTConditionnement) {
//		this.taTConditionnement = taTConditionnement;
//	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_article")
	//@Column(name = "id_article")
	@LgrHibernateValidated(champBd = "id_article",table = "ta_conditionnement_article", champEntite="taArticle.idArticle", clazz = TaArticle.class)
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	@Column(name = "nb_unite")
	@LgrHibernateValidated(champBd = "nb_unite",table = "ta_conditionnement_article", champEntite="nbUnite", clazz = TaConditionnementArticle.class)
	public Integer getNbUnite() {
		return nbUnite;
	}

	public void setNbUnite(Integer nbUnite) {
		this.nbUnite = nbUnite;
	}

	@Column(name = "hauteur")
	@LgrHibernateValidated(champBd = "hauteur",table = "ta_conditionnement_article", champEntite="hauteur", clazz = TaConditionnementArticle.class)
	public BigDecimal getHauteur() {
		return hauteur;
	}

	public void setHauteur(BigDecimal hauteur) {
		this.hauteur = hauteur;
	}

	@Column(name = "longueur")
	@LgrHibernateValidated(champBd = "longueur",table = "ta_conditionnement_article", champEntite="longueur", clazz = TaConditionnementArticle.class)
	public BigDecimal getLongueur() {
		return longueur;
	}

	public void setLongueur(BigDecimal longueur) {
		this.longueur = longueur;
	}

	@Column(name = "largeur")
	@LgrHibernateValidated(champBd = "largeur",table = "ta_conditionnement_article", champEntite="largeur", clazz = TaConditionnementArticle.class)
	public BigDecimal getLargeur() {
		return largeur;
	}

	public void setLargeur(BigDecimal largeur) {
		this.largeur = largeur;
	}

	@Column(name = "poids")
	@LgrHibernateValidated(champBd = "poids",table = "ta_conditionnement_article", champEntite="poids", clazz = TaConditionnementArticle.class)
	public BigDecimal getPoids() {
		return poids;
	}

	public void setPoids(BigDecimal poids) {
		this.poids = poids;
	}

	@Column(name = "reduction")
	@LgrHibernateValidated(champBd = "reduction",table = "ta_conditionnement_article", champEntite="reduction", clazz = TaConditionnementArticle.class)
	public BigDecimal getReduction() {
		return reduction;
	}

	public void setReduction(BigDecimal reduction) {
		this.reduction = reduction;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCree) {
//		this.quiCree = quiCree;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCree) {
//		this.quandCree = quandCree;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModif) {
//		this.quiModif = quiModif;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModif) {
//		this.quandModif = quandModif;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((code == null) ? 0 : code.hashCode());
		return result;
	}


}
