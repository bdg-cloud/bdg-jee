package fr.legrain.moncompte.model;


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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;



@Entity
@Table(name = "ta_setup")
public class TaSetup implements java.io.Serializable {
	
	private static final long serialVersionUID = 4244303838889275022L;
	
	private Integer idSetup;
	private TaProduit taProduit;
	private String libelle;
	private String description;
	private String fichierSetup;
	private Boolean actif;
	private Boolean maj;
	private String versionProduit;
	private Date dateSetup;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaSetup() {
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_setup")
	public Integer getIdSetup() {
		return idSetup;
	}

	public void setIdSetup(Integer idSetup) {
		this.idSetup = idSetup;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
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

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_produit")
//	@XmlTransient //Pour éviter la création de XML trop gros (récursif : produit-setup-produit) dans les appels webservices soap
	@XmlElement
	@XmlInverseReference(mappedBy="listeSetup")
	public TaProduit getTaProduit() {
		return taProduit;
	}

	public void setTaProduit(TaProduit taProduit) {
		this.taProduit = taProduit;
	}

	@Column(name = "fichier_setup")
	public String getFichierSetup() {
		return fichierSetup;
	}

	public void setFichierSetup(String fichierSetup) {
		this.fichierSetup = fichierSetup;
	}

	@Column(name = "actif")
	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	@Column(name = "version_produit")
	public String getVersionProduit() {
		return versionProduit;
	}

	public void setVersionProduit(String versionProduit) {
		this.versionProduit = versionProduit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_setup", length = 19)
	public Date getDateSetup() {
		return dateSetup;
	}

	public void setDateSetup(Date dateSetup) {
		this.dateSetup = dateSetup;
	}

	@Column(name = "maj")
	public Boolean getMaj() {
		return maj;
	}

	public void setMaj(Boolean maj) {
		this.maj = maj;
	}

}
