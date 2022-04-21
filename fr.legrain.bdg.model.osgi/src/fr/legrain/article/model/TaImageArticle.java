package fr.legrain.article.model;

// Generated Sep 1, 2008 3:06:27 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_image_article")
//@SequenceGenerator(name = "gen_image_article", sequenceName = "num_id_image_article", allocationSize = 1)
@NamedQueries(value = { 
	@NamedQuery(name=TaImageArticle.QN.FIND_BY_ARTICLE, query="select new fr.legrain.article.dto.TaLotDTO(a.idLot, a.numLot,a.libelle, art.codeArticle, a.unite1, a.unite2, a.rapport, a.nbDecimal, a.termine,a.versionObj) from TaLot a join a.taArticle art order by a.numLot"),
	@NamedQuery(name=TaImageArticle.QN.FIND_BY_ARTICLE_LIGHT, query="select new fr.legrain.article.dto.TaLotDTO(a.idLot, a.numLot,a.libelle, art.codeArticle, a.unite1, a.unite2, a.rapport,  a.nbDecimal, a.termine,a.versionObj) from TaLot a join a.taArticle art  where a.numLot like :numLot order by a.numLot")
})
public class TaImageArticle extends TaObjetLgr   implements java.io.Serializable {

	private static final long serialVersionUID = 4029378556671431905L;
	static Logger logger = Logger.getLogger(TaImageArticle.class.getName());
	
	public static class QN {
		public static final String FIND_BY_ARTICLE = "TaImageArticle.findByArticle";
		public static final String FIND_BY_ARTICLE_LIGHT = "TaImageArticle.findByArticleLight";
	}
	
	private int idImageArticle;

	private TaArticle taArticle;
	private String cheminImageArticle;
	private byte[] blobImageOrigine;
	private byte[] blobImageGrand;
	private byte[] blobImageMoyen;
	private byte[] blobImagePetit;
	private String urlImage;
	private String mime;
	private String altText;
	private String ariaText;
	private String description;
	private Integer position;
	private String nomImageArticle;
	private TaImageArticle imageOrigine;
	private String checksum;

	private Boolean defautImageArticle = false;
	
	private Integer versionObj;
	private Set<TaImageArticle> taImagesGenere = new HashSet<TaImageArticle>(0);
	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String version;
//	private String ipAcces;
	
	@Transient
	public Boolean getDefautImageArticle() {
		if(taArticle!=null && taArticle.getTaImageArticle()!=null)
		  return taArticle.getTaImageArticle().equals(this);
		else return false;
	}

	public void setDefautImageArticle(Boolean defaut) {

		this.defautImageArticle = defaut;
		if(taArticle!=null) {
			if(defaut) {
				taArticle.setTaImageArticle(this);
			} else {
				//selection d'une image "par defaut" (la premiere de la liste)
				if(!taArticle.getTaImageArticles().isEmpty())
					taArticle.setTaImageArticle(taArticle.getTaImageArticles().iterator().next());
			}
		}
	}

	public TaImageArticle() {
	}

	public TaImageArticle(int idImageArticle) {
		this.idImageArticle = idImageArticle;
	}



	public TaImageArticle(int idImageArticle, String version,
			TaArticle taArticle, String cheminImageArticle,
			String nomImageArticle, Integer defautImageArticle,
			String quiCreeImageArticle, Date quandCreeImageArticle,
			String quiModifImageArticle, Date quandModifImageArticle,
			String ipAcces, Integer versionObj) {
		super();
		this.idImageArticle = idImageArticle;
		this.version = version;
		this.taArticle = taArticle;
		this.cheminImageArticle = cheminImageArticle;
		this.nomImageArticle = nomImageArticle;
	//	this.defautImageArticle = defautImageArticle;
		this.quiCree = quiCreeImageArticle;
		this.quandCree = quandCreeImageArticle;
		this.quiModif = quiModifImageArticle;
		this.quandModif = quandModifImageArticle;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_image_article", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_image_article",table = "ta_image_article", champEntite="idImageArticle", clazz = TaImageArticle.class)
	public int getIdImageArticle() {
		return this.idImageArticle;
	}

	public void setIdImageArticle(int idImageArticle) {
		this.idImageArticle = idImageArticle;
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
	@JoinColumn(name = "id_article")
	@LgrHibernateValidated(champBd = "id_article",table = "ta_prix", champEntite="taArticle.idArticle", clazz = TaArticle.class)
	public TaArticle getTaArticle() {
		return this.taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	@Column(name = "chemin_image_article", precision = 15)
//	@LgrHibernateValidated(champBd = "chemin_image_article",table = "ta_image_article", champEntite="cheminImageArticle", clazz = TaImageArticle.class)
	public String getCheminImageArticle() {
		return this.cheminImageArticle;
	}

	public void setCheminImageArticle(String cheminImageArticle) {
		this.cheminImageArticle = cheminImageArticle;
	}
	
	@Column(name = "nom_image_article", precision = 15)
//	@LgrHibernateValidated(champBd = "nom_image_article",table = "ta_image_article", champEntite="nomImageArticle", clazz = TaImageArticle.class)
	public String getNomImageArticle() {
		return this.nomImageArticle;
	}

	public void setNomImageArticle(String nomImageArticle) {
		this.nomImageArticle = nomImageArticle;
	}
	
//	@Column(name = "defaut_image_article", precision = 15)
//	@LgrHibernateValidated(champ = "defaut_image_article",table = "ta_image_article", champEntite="xxxx", clazz = TaImageArticle.class)
//	public Integer getDefautImageArticle() {
//		return this.defautImageArticle;
//	}
//
//	public void setDefautImageArticle(Integer defautImageArticle) {
//		this.defautImageArticle = defautImageArticle;
//	}
	
	/*
	 * ****************************************************************************************
	 */
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "imageOrigine")
	//@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "imageOrigine")
	public Set<TaImageArticle> getTaImagesGenere() {
		return this.taImagesGenere;
	}
	
	public void setTaImagesGenere(Set<TaImageArticle> taImagesGenere) {
		this.taImagesGenere = taImagesGenere;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_image_origine")
	@LgrHibernateValidated(champBd = "id_image_origine",table = "ta_image_article", champEntite="imageOrigine", clazz = TaImageArticle.class)
	public TaImageArticle getImageOrigine() {
		return this.imageOrigine;
	}

	public void setImageOrigine(TaImageArticle imageOrigine) {
		this.imageOrigine = imageOrigine;
	}
	
	@Column(name = "checksum_image_article")
	@LgrHibernateValidated(champBd = "checksum_image_article",table = "ta_image_article", champEntite="checksum", clazz = TaImageArticle.class)
	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	/*
	 * ****************************************************************************************
	 */

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeImageArticle) {
//		this.quiCree = quiCreeImageArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeImageArticle) {
//		this.quandCree = quandCreeImageArticle;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifImageArticle) {
//		this.quiModif = quiModifImageArticle;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifImageArticle) {
//		this.quandModif = quandModifImageArticle;
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
		result = prime * result + idImageArticle;
		result = prime * result
				+ ((cheminImageArticle == null) ? 0 : cheminImageArticle.hashCode());
		result = prime * result
				+ ((nomImageArticle == null) ? 0 : nomImageArticle.hashCode());
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
		final TaImageArticle other = (TaImageArticle) obj;
		if (idImageArticle != other.idImageArticle)
			return false;
		if (cheminImageArticle == null) {
			if (other.cheminImageArticle != null)
				return false;
		} else if (!cheminImageArticle.equals(other.cheminImageArticle))
			return false;
		if (ipAcces == null) {
			if (other.ipAcces != null)
				return false;
		} else if (!ipAcces.equals(other.ipAcces))
			return false;
		if (nomImageArticle == null) {
			if (other.nomImageArticle != null)
				return false;
		} else if (!nomImageArticle.equals(other.nomImageArticle))
			return false;
		return true;
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

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "position")
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	
	

	
}
