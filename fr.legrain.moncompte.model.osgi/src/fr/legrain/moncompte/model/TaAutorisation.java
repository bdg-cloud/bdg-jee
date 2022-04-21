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

@Table(name = "ta_autorisation")


public class TaAutorisation implements java.io.Serializable {
	

	private static final long serialVersionUID = -1791584148276356085L;
	
	private Integer idAutorisation;
	@XmlElement
	@XmlInverseReference(mappedBy="listeAutorisation")
	private TaDossier taDossier;
	private TaProduit taProduit;
	private Date dateAchat;
	private Date dateFin;
	private boolean paye = true;
	private Integer numUtilisateurMax;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaAutorisation() {
	}
	
	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
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
	


	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_autorisation")
	public Integer getIdAutorisation() {
		return idAutorisation;
	}

	public void setIdAutorisation(Integer idAutorisation) {
		this.idAutorisation = idAutorisation;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_dossier")
	@XmlElement
	@XmlInverseReference(mappedBy="listeAutorisation")
	public TaDossier getTaDossier() {
		return taDossier;
	}

	public void setTaDossier(TaDossier taDossier) {
		this.taDossier = taDossier;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_produit")
	public TaProduit getTaProduit() {
		return taProduit;
	}

	public void setTaProduit(TaProduit taProduit) {
		this.taProduit = taProduit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_achat", length = 19)
	public Date getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(Date dateAchat) {
		this.dateAchat = dateAchat;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_fin", length = 19)
	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	@Column(name = "num_utilisateur_max")
	public Integer getNumUtilisateurMax() {
		return numUtilisateurMax;
	}

	public void setNumUtilisateurMax(Integer numUtilisateurMax) {
		this.numUtilisateurMax = numUtilisateurMax;
	}

	@Column(name = "paye")
	public boolean isPaye() {
		return paye;
	}

	public void setPaye(boolean paye) {
		this.paye = paye;
	}


}
