package fr.legrain.droits.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


@Entity
@Table(name = "ta_client_legrain")
public class TaClientLegrain implements Serializable {
	
	private static final long serialVersionUID = -8929636743919606499L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="taClientLegrain")
	private List<TaEntrepriseClient> multiSite;
	
	@Column(name="code")
	private String code;
	
	@Column(name="nom")
	private String nom;
	
	@Column(name="adresse1")
	private String adresse1;
	
	@Column(name="adresse2")
	private String adresse2;
	
	@Column(name="code_postal")
	private String codePostal;
	
	@Column(name="ville")
	private String ville;
	
	@Column(name="pays")
	private String pays;
	
	@Column(name="tva")
	private BigDecimal tva;
	
	@Column(name="actif")
	private Integer actif;
	
	@Column(name="telephone1")
	private String telephone1;
	
	@Column(name="telephone2")
	private String telephone2;
	
	@Column(name="fax")
	private String fax;
	
	@Column(name="email")
	private String email;
	
	@Column(name="date_creation")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;
	
	@Column(name="date_dernier_engagement")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDernierEngagement;

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
	
	public List<Integer> findIdMultiSite(Integer idUserCompanyANePasInclure) {
		List<Integer> idMultiSite = null;
		if(multiSite.size()>1) { //il n'y a pas que l'entreprise de l'utilisateur dans la liste
			idMultiSite = new ArrayList<Integer>();
			for (TaEntrepriseClient userCompany : multiSite) {
				if(idUserCompanyANePasInclure==null || !userCompany.getId().equals(idUserCompanyANePasInclure)) {
					idMultiSite.add(userCompany.getId());
				}
			}
		}
		return idMultiSite;
	}
	
	
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

	public Integer getActif() {
		return actif;
	}

	public void setActif(Integer actif) {
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

	public List<TaEntrepriseClient> getMultiSite() {
		return multiSite;
	}

	public void setMultiSite(List<TaEntrepriseClient> multiSite) {
		this.multiSite = multiSite;
	}
	
}
