package fr.legrain.article.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;

import fr.legrain.general.model.TaObjetLgr;
@Entity
@Table(name = "ta_type_controle")

public class TaTypeControle extends TaObjetLgr   implements Serializable{

	private static final long serialVersionUID = -1293581006739216759L;

	private int id;
	private String code;
	private String libelleTypeControle;
	private String libellePropriete;
	private String pathIMG;

	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;

	@Transient
	static Logger logger = Logger.getLogger(TaTypeControle.class.getName());

	public TaTypeControle() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type_controle", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "path_img")
	public String getPathIMG() {
		return pathIMG;
	}

	public void setPathIMG(String pathIMG) {
		this.pathIMG = pathIMG;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "libelle_type")
	public String getLibelleTypeControle() {
		return libelleTypeControle;
	}

	public void setLibelleTypeControle(String libelleTypeControle) {
		this.libelleTypeControle = libelleTypeControle;
	}
	@Column(name = "libelle_propriete")
	public String getLibellePropriete() {
		return libellePropriete;
	}

	public void setLibellePropriete(String libellePropriete) {
		this.libellePropriete = libellePropriete;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
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
//
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

}
