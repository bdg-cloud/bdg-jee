package fr.legrain.tache.model;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_type_evenement")
public class TaTypeEvenement implements java.io.Serializable {

	private static final long serialVersionUID = -7728724504614144843L;
	
	private int idTypeEvenement;
	private String codeTypeEvenement;
	private String libelleTypeEvenement;
	private TaCategorieEvenement taCategorieEvenement;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type_evenement", unique = true, nullable = false)
	public int getIdTypeEvenement() {
		return idTypeEvenement;
	}
	public void setIdTypeEvenement(int idTypeEvenement) {
		this.idTypeEvenement = idTypeEvenement;
	}
	
	@Column(name = "code_type_evenement")
	public String getCodeTypeEvenement() {
		return codeTypeEvenement;
	}
	public void setCodeTypeEvenement(String codeTypeEvenement) {
		this.codeTypeEvenement = codeTypeEvenement;
	}
	
	@Column(name = "libelle_type_evenement")
	public String getLibelleTypeEvenement() {
		return libelleTypeEvenement;
	}
	public void setLibelleTypeEvenement(String libelleTypeEvenement) {
		this.libelleTypeEvenement = libelleTypeEvenement;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_categorie_evenement")
	public TaCategorieEvenement getTaCategorieEvenement() {
		return taCategorieEvenement;
	}
	public void setTaCategorieEvenement(TaCategorieEvenement taCategorieEvenement) {
		this.taCategorieEvenement = taCategorieEvenement;
	}
	
	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
	}
	
	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "version")
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}