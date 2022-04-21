package fr.legrain.bdg.compteclient.dto.droits;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import fr.legrain.bdg.compteclient.dto.ModelObject;


public class TaClientLegrainDTO extends ModelObject implements java.io.Serializable {
	
	private static final long serialVersionUID = -1710423405029946807L;

	private Integer id;
	private List<TaEntrepriseClientDTO> multiSite;
	private String code;
	private String nom;
	private String adresse1;
	private String adresse2;
	private String codePostal;
	private String ville;
	private String pays;
	private BigDecimal tva;
	private Boolean actif;
	private String telephone1;
	private String telephone2;
	private String fax;
	private String email;
	private Date dateCreation;
	private Date dateDernierEngagement;
	private Integer versionObj;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public BigDecimal getTva() {
		return tva;
	}

	public void setTva(BigDecimal tva) {
		this.tva = tva;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getTelephone1() {
		return telephone1;
	}

	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}

	public String getTelephone2() {
		return telephone2;
	}

	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateDernierEngagement() {
		return dateDernierEngagement;
	}

	public void setDateDernierEngagement(Date dateDernierEngagement) {
		this.dateDernierEngagement = dateDernierEngagement;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public List<TaEntrepriseClientDTO> getMultiSite() {
		return multiSite;
	}

	public void setMultiSite(List<TaEntrepriseClientDTO> multiSite) {
		this.multiSite = multiSite;
	}
	
}
