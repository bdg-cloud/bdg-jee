package fr.legrain.bdg.compteclient.dto.droits;

import fr.legrain.bdg.compteclient.dto.ModelObject;

public class TaEntrepriseClientDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = 1502457728368751255L;

	private Integer id;
	private TaUtilisateurDTO utilisateurPrincipal; //admin
	private TaGroupeEntrepriseDTO taGroupeEntreprise;
	private String nom;
	private TaClientLegrainDTO taClientLegrain;
	private String adresse1;
	private String adresse2;
	private String codePostal;
	private String ville;
	private String departement;
	private String region;
	private String longitude;
	private String latitude;
	private String telephone;
	private String fax;
	private String email;
	private Boolean active;
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

	public TaGroupeEntrepriseDTO getTaGroupeEntreprise() {
		return taGroupeEntreprise;
	}

	public void setTaGroupeEntreprise(TaGroupeEntrepriseDTO idGroupeEntreprise) {
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
		TaEntrepriseClientDTO other = (TaEntrepriseClientDTO) obj;
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

	public TaClientLegrainDTO getTaClientLegrain() {
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

	public Boolean getActive() {
		return active;
	}

	public void setTaClientLegrain(TaClientLegrainDTO idAdherent) {
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

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaUtilisateurDTO getUtilisateurPrincipal() {
		return utilisateurPrincipal;
	}

	public void setUtilisateurPrincipal(TaUtilisateurDTO utilisateurPrincipal) {
		this.utilisateurPrincipal = utilisateurPrincipal;
	}

}
