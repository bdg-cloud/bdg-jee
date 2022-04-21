package fr.legrain.conformite.model;

import java.io.Serializable;
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

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@Table(name = "ta_r_groupe_modele_conformite")
public class TaRGroupeModeleConformite implements Serializable {

	private static final long serialVersionUID = 4781674127197474660L;


	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_modele_groupe")
	@XmlElement
	@XmlInverseReference(mappedBy="listeModeleConformite")
	private TaModeleGroupe modeleGroupe; 
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_modele_conformite")
	private TaModeleConformite modeleConformite;
	
	@Column(name="position")
	private Integer position;
	
	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;
	
	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;
	
	@Column(name="qui_cree")
	private String quiCree;
	
	@Column(name="qui_modif")
	private String quiModif;
	
	@Column(name = "ip_acces")
	private String ipAcces;
	
	@Version
	@Column(name = "version_obj")
	private Integer versionObj;
	
	public TaRGroupeModeleConformite() {
	}

	public TaRGroupeModeleConformite(TaModeleGroupe modeleGroupe, TaModeleConformite modeleConformite, int position) {
		super();
		this.modeleGroupe = modeleGroupe;
		this.modeleConformite = modeleConformite;
		this.position = position;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public String getQuiCree() {
		return quiCree;
	}

	public String getQuiModif() {
		return quiModif;
	}


	public Integer getVersionObj() {
		return versionObj;
	}

	public void setQuandCree(Date quanCree) {
		this.quandCree = quanCree;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}

	public void setIpAcces(String ip) {
		this.ipAcces = ip;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getIpAcces() {
		return ipAcces;
	}

	public TaModeleGroupe getModeleGroupe() {
		return modeleGroupe;
	}

	public void setModeleGroupe(TaModeleGroupe modeleGroupe) {
		this.modeleGroupe = modeleGroupe;
	}

	public TaModeleConformite getModeleConformite() {
		return modeleConformite;
	}

	public void setModeleConformite(TaModeleConformite modeleConformite) {
		this.modeleConformite = modeleConformite;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
