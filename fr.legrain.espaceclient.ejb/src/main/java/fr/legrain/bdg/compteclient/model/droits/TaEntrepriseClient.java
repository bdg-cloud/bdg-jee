package fr.legrain.bdg.compteclient.model.droits;

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

@Entity
@Table(name = "ta_entreprise_client")
public class TaEntrepriseClient implements Serializable {

	private static final long serialVersionUID = 7461227814502775961L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_utilisateur")
	private TaUtilisateur utilisateurPrincipal; //admin
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_groupe_entreprise")
	private TaGroupeEntreprise taGroupeEntreprise;
	
	@Column(name="nom")
	private String nom;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_client")
	private TaClientLegrain taClientLegrain;
	
 	
 	@Column(name="adresse1")
	private String adresse1;
 	
 	@Column(name="adresse2")
	private String adresse2;
 	
 	@Column(name="code_postal")
	private String codePostal;
 	
 	@Column(name="ville")
	private String ville;
 	
 	@Column(name="departement")
	private String departement;
 	
 	@Column(name="region")
	private String region;
	
	@Column(name="longitude")
	private String longitude;
 	
 	@Column(name="latitude")
	private String latitude;
 	
 	@Column(name="telephone")
	private String telephone;
 	
 	@Column(name="fax")
	private String fax;
 	
 	@Column(name="email")
	private String email;
 	
 	@Column(name="active")
	private Integer active;
 	
 	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;
	
	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;
	
	@Column(name="qui_cree")
	private Integer quiCree;
	
	@Column(name="qui_modif")
	private Integer quiModif;
	
	@Column(name = "ip_acces")
	private String ipAcces;
	
	@Version
	@Column(name = "version_obj")
	private Integer versionObj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public TaGroupeEntreprise getTaGroupeEntreprise() {
		return taGroupeEntreprise;
	}

	public void setTaGroupeEntreprise(TaGroupeEntreprise idGroupeEntreprise) {
		this.taGroupeEntreprise = idGroupeEntreprise;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((taGroupeEntreprise == null) ? 0 : taGroupeEntreprise
						.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
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
		TaEntrepriseClient other = (TaEntrepriseClient) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
//		if (idGroupeEntreprise == null) {
//			if (other.idGroupeEntreprise != null)
//				return false;
//		} else if (!idGroupeEntreprise.equals(other.idGroupeEntreprise))
//			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

	public TaClientLegrain getTaClientLegrain() {
		return taClientLegrain;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public String getVille() {
		return ville;
	}

	public String getDepartement() {
		return departement;
	}

	public String getRegion() {
		return region;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getFax() {
		return fax;
	}

	public String getEmail() {
		return email;
	}

	public Integer getActive() {
		return active;
	}

	public void setTaClientLegrain(TaClientLegrain idAdherent) {
		this.taClientLegrain = idAdherent;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public Integer getQuiCree() {
		return quiCree;
	}

	public Integer getQuiModif() {
		return quiModif;
	}

	public String getIpAcces() {
		return ipAcces;
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

	public void setQuiCree(Integer quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(Integer quiModif) {
		this.quiModif = quiModif;
	}

	public void setIpAcces(String ip) {
		this.ipAcces = ip;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaUtilisateur getUtilisateurPrincipal() {
		return utilisateurPrincipal;
	}

	public void setUtilisateurPrincipal(TaUtilisateur utilisateurPrincipal) {
		this.utilisateurPrincipal = utilisateurPrincipal;
	}

}
