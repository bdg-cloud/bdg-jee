package fr.legrain.moncompte.model;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
@Table(name = "ta_categorie_pro")
public class TaCategoriePro implements java.io.Serializable {

	private static final long serialVersionUID = 3991454212740760599L;
	
	private Integer idCategoriePro;
	private String description;
	private String libelle;
	private  List<TaProduit> listeProduit;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaCategoriePro() {
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

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Integer getIdCategoriePro() {
		return idCategoriePro;
	}

	public void setIdCategoriePro(Integer idTypeProduit) {
		this.idCategoriePro = idTypeProduit;
	}

	@Column(name="libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name = "ta_r_produit_categorie_pro", joinColumns = @JoinColumn(name = "id_categorie_pro"),
    inverseJoinColumns = @JoinColumn(name = "id_produit"))
	@Fetch(FetchMode.SUBSELECT)
	public List<TaProduit> getListeProduit() {
		return listeProduit;
	}

	public void setListeProduit(List<TaProduit> listeProduit) {
		this.listeProduit = listeProduit;
	}

}
