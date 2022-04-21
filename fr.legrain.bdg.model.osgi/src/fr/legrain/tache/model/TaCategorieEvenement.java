package fr.legrain.tache.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_categorie_evenement")
public class TaCategorieEvenement implements java.io.Serializable { //Type de type

	private static final long serialVersionUID = -6926848298317866786L;
	
	private int idCategorieEvenement;
	private String codeCategorieEvenement;
	private String libelleCategorieEvenement;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_categorie_evenement", unique = true, nullable = false)
	public int getIdCategorieEvenement() {
		return idCategorieEvenement;
	}
	public void setIdCategorieEvenement(int idCategorieEvenement) {
		this.idCategorieEvenement = idCategorieEvenement;
	}
	
	@Column(name = "code_categorie_evenement")
	public String getCodeCategorieEvenement() {
		return codeCategorieEvenement;
	}
	public void setCodeCategorieEvenement(String codeCategorieEvenement) {
		this.codeCategorieEvenement = codeCategorieEvenement;
	}
	
	@Column(name = "libelle_categorie_evenement")
	public String getLibelleCategorieEvenement() {
		return libelleCategorieEvenement;
	}
	public void setLibelleCategorieEvenement(String libelleCategorieEvenement) {
		this.libelleCategorieEvenement = libelleCategorieEvenement;
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