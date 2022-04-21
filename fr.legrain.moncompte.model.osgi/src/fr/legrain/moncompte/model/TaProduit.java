package fr.legrain.moncompte.model;


import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;



@Entity
@Table(name = "ta_produit")
public class TaProduit implements java.io.Serializable {
	
	private Integer idProduit;
	private String code;
	private String libelle;
	private String libelleHtml;
	private String identifiantModule;
	private String description;
	private String descriptionHtml;
	private Boolean sousProduit;
	private Boolean vendable;
	private Boolean compose;
	private Boolean eligibleCommission;
	private String versionProduit;
	private String versionOS;
	private String versionBrowser;

	private BigDecimal prixHT;
	private BigDecimal tva;
	private BigDecimal prixTTC;
	
	private BigDecimal prixHTLight;
	private BigDecimal tvaLight;
	private BigDecimal prixTTCLight;
	
	private BigDecimal prixHTParPosteInstalle;
	private BigDecimal tvaParPosteInstalle;
	private BigDecimal prixTTCParPosteInstalle;
	
	private BigDecimal prixHTWs;
	private BigDecimal tvaWs;
	private BigDecimal prixTTCWs;
	
	private BigDecimal prixHTLicence;
	private BigDecimal tvaLicence;
	private BigDecimal prixTTCLicence;
	
	private BigDecimal prixHTMaintenance;
	private BigDecimal tvaMaintenance;
	private BigDecimal prixTTCMaintenance;
	
	private BigDecimal tauxTVA;
	private TaTypeProduit taTypeProduit;
	private TaTNiveau taTNiveau;
	private TaGroupeProduit taGroupeProduit;
	
	private List<TaProduit> listeSousProduit;
	private List<TaProduit> listeProduitDependant;
	private List<TaSetup> listeSetup;
	private List<TaPrixParUtilisateur> listePrixParUtilisateur;

	private static final long serialVersionUID = -1791584148276356085L;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaProduit() {
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_produit")
	public Integer getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(Integer idProduit) {
		this.idProduit = idProduit;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	@Column(name = "libelle_html")
	public String getLibelleHtml() {
		return libelleHtml;
	}

	public void setLibelleHtml(String libelleHtml) {
		this.libelleHtml = libelleHtml;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "description_html")
	public String getDescriptionHtml() {
		return descriptionHtml;
	}

	public void setDescriptionHtml(String descriptionHtml) {
		this.descriptionHtml = descriptionHtml;
	}

	@Column(name = "sous_produit")
	public Boolean getSousProduit() {
		return sousProduit;
	}

	public void setSousProduit(Boolean sousProduit) {
		this.sousProduit = sousProduit;
	}

	
	@Column(name = "compose")
	public Boolean getCompose() {
		return compose;
	}

	public void setCompose(Boolean compose) {
		this.compose = compose;
	}
	
	@Column(name = "vendable")
	public Boolean getVendable() {
		return vendable;
	}

	public void setVendable(Boolean vendable) {
		this.vendable = vendable;
	}
	
	@Column(name = "version_produit")
	public String getVersionProduit() {
		return versionProduit;
	}

	public void setVersionProduit(String versionProduit) {
		this.versionProduit = versionProduit;
	}

	@Column(name = "version_os")
	public String getVersionOS() {
		return versionOS;
	}

	public void setVersionOS(String versionOS) {
		this.versionOS = versionOS;
	}

	@Column(name = "version_browser")
	public String getVersionBrowser() {
		return versionBrowser;
	}

	public void setVersionBrowser(String versionBrowser) {
		this.versionBrowser = versionBrowser;
	}

	@Column(name = "prix_ht")
	public BigDecimal getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(BigDecimal prixHT) {
		this.prixHT = prixHT;
	}
	
	@Column(name = "prix_ht_light")
	public BigDecimal getPrixHTLight() {
		return prixHTLight;
	}

	public void setPrixHTLight(BigDecimal prixHTLight) {
		this.prixHTLight = prixHTLight;
	}

	@Column(name = "tva_light")
	public BigDecimal getTvaLight() {
		return tvaLight;
	}

	public void setTvaLight(BigDecimal tvaLight) {
		this.tvaLight = tvaLight;
	}
	
	@Column(name = "prix_ttc_light")
	public BigDecimal getPrixTTCLight() {
		return prixTTCLight;
	}

	public void setPrixTTCLight(BigDecimal prixTTCLight) {
		this.prixTTCLight = prixTTCLight;
	}

	@Column(name = "prix_ht_par_poste_installe")
	public BigDecimal getPrixHTParPosteInstalle() {
		return prixHTParPosteInstalle;
	}

	public void setPrixHTParPosteInstalle(BigDecimal prixHTParPosteInstalle) {
		this.prixHTParPosteInstalle = prixHTParPosteInstalle;
	}
	
	@Column(name = "tva_par_poste_installe")
	public BigDecimal getTvaParPosteInstalle() {
		return tvaParPosteInstalle;
	}

	public void setTvaParPosteInstalle(BigDecimal tvaParPosteInstalle) {
		this.tvaParPosteInstalle = tvaParPosteInstalle;
	}

	@Column(name = "prix_ttc_par_poste_installe")
	public BigDecimal getPrixTTCParPosteInstalle() {
		return prixTTCParPosteInstalle;
	}

	public void setPrixTTCParPosteInstalle(BigDecimal prixTTCParPosteInstalle) {
		this.prixTTCParPosteInstalle = prixTTCParPosteInstalle;
	}

	@Column(name = "prix_ht_ws")
	public BigDecimal getPrixHTWs() {
		return prixHTWs;
	}

	public void setPrixHTWs(BigDecimal prixHTWs) {
		this.prixHTWs = prixHTWs;
	}

	@Column(name = "tva_ws")
	public BigDecimal getTvaWs() {
		return tvaWs;
	}

	public void setTvaWs(BigDecimal tvaWs) {
		this.tvaWs = tvaWs;
	}

	@Column(name = "prix_ttc_ws")
	public BigDecimal getPrixTTCWs() {
		return prixTTCWs;
	}

	public void setPrixTTCWs(BigDecimal prixTTCWs) {
		this.prixTTCWs = prixTTCWs;
	}
	
	@Column(name = "indentifiant_module")
	public String getIdentifiantModule() {
		return identifiantModule;
	}

	public void setIdentifiantModule(String identifiantModule) {
		this.identifiantModule = identifiantModule;
	}

	@Column(name = "tva")
	public BigDecimal getTva() {
		return tva;
	}

	public void setTva(BigDecimal tva) {
		this.tva = tva;
	}

	@Column(name = "taux_tva")
	public BigDecimal getTauxTVA() {
		return tauxTVA;
	}

	public void setTauxTVA(BigDecimal tauxTVA) {
		this.tauxTVA = tauxTVA;
	}

	@Column(name = "prix_ttc")
	public BigDecimal getPrixTTC() {
		return prixTTC;
	}

	public void setPrixTTC(BigDecimal prixTTC) {
		this.prixTTC = prixTTC;
	}
	  
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_type_produit")
	public TaTypeProduit getTaTypeProduit() {
		return taTypeProduit;
	}

	public void setTaTypeProduit(TaTypeProduit taTypeProduit) {
		this.taTypeProduit = taTypeProduit;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_niveau")
	public TaTNiveau getTaTNiveau() {
		return taTNiveau;
	}

	public void setTaTNiveau(TaTNiveau taTNiveau) {
		this.taTNiveau = taTNiveau;
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

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinTable(name="ta_produit_compose", 
          joinColumns=@JoinColumn(name="id_article_parent"),
          inverseJoinColumns=@JoinColumn(name="id_article"))
	@Fetch(FetchMode.SUBSELECT)
	public List<TaProduit> getListeSousProduit() {
		return listeSousProduit;
	}

	public void setListeSousProduit(List<TaProduit> listeSousProduit) {
		this.listeSousProduit = listeSousProduit;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @JoinTable(name="ta_dependance_produit", 
          joinColumns=@JoinColumn(name="id_produit_dependant"),
          inverseJoinColumns=@JoinColumn(name="id_produit"))
	@Fetch(FetchMode.SUBSELECT)
	public List<TaProduit> getListeProduitDependant() {
		return listeProduitDependant;
	}

	public void setListeProduitDependant(List<TaProduit> listeProduitDependant) {
		this.listeProduitDependant = listeProduitDependant;
	}
	
	//@OrderBy("dateVersion")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taProduit"/*, orphanRemoval=true*/)
	@Fetch(FetchMode.SUBSELECT)
	public List<TaSetup> getListeSetup() {
		return listeSetup;
	}

	public void setListeSetup(List<TaSetup> listeSetup) {
		this.listeSetup = listeSetup;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taProduit"/*, orphanRemoval=true*/)
	@Fetch(FetchMode.SUBSELECT)
	public List<TaPrixParUtilisateur> getListePrixParUtilisateur() {
		return listePrixParUtilisateur;
	}

	public void setListePrixParUtilisateur(
			List<TaPrixParUtilisateur> listePrixParUtilisateur) {
		this.listePrixParUtilisateur = listePrixParUtilisateur;
	}



	
//	public boolean equalsProduitAndVersion(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		TaProduit other = (TaProduit) obj;
//		if (code == null) {
//			if (other.code != null)
//				return false;
//		} else if (!code.equals(other.code))
//			return false;
//		if (idProduit == null) {
//			if (other.idProduit != null)
//				return false;
//		} else if (!idProduit.equals(other.idProduit))
//			return false;
//		if (versionBrowser == null) {
//			if (other.versionBrowser != null)
//				return false;
//		} else if (!versionBrowser.equals(other.versionBrowser))
//			return false;
//		if (versionOS == null) {
//			if (other.versionOS != null)
//				return false;
//		} else if (!versionOS.equals(other.versionOS))
//			return false;
//		if (versionProduit == null) {
//			if (other.versionProduit != null)
//				return false;
//		} else if (!versionProduit.equals(other.versionProduit))
//			return false;
//		return true;
//	}

	@Column(name = "prix_ht_licence")
	public BigDecimal getPrixHTLicence() {
		return prixHTLicence;
	}

	public void setPrixHTLicence(BigDecimal prixHTLicence) {
		this.prixHTLicence = prixHTLicence;
	}

	@Column(name = "tva_licence")
	public BigDecimal getTvaLicence() {
		return tvaLicence;
	}

	public void setTvaLicence(BigDecimal tvaLicence) {
		this.tvaLicence = tvaLicence;
	}

	@Column(name = "prix_ttc_licence")
	public BigDecimal getPrixTTCLicence() {
		return prixTTCLicence;
	}

	public void setPrixTTCLicence(BigDecimal prixTTCLicence) {
		this.prixTTCLicence = prixTTCLicence;
	}

	@Column(name = "prix_ht_maintenance")
	public BigDecimal getPrixHTMaintenance() {
		return prixHTMaintenance;
	}

	public void setPrixHTMaintenance(BigDecimal prixHTMaintenance) {
		this.prixHTMaintenance = prixHTMaintenance;
	}

	@Column(name = "tva_maintenance")
	public BigDecimal getTvaMaintenance() {
		return tvaMaintenance;
	}

	public void setTvaMaintenance(BigDecimal tvaMaintenance) {
		this.tvaMaintenance = tvaMaintenance;
	}

	@Column(name = "prix_ttc_maintenance")
	public BigDecimal getPrixTTCMaintenance() {
		return prixTTCMaintenance;
	}

	public void setPrixTTCMaintenance(BigDecimal prixTTCMaintenance) {
		this.prixTTCMaintenance = prixTTCMaintenance;
	}
	
	@Column(name = "eligible_commission")
	public Boolean getEligibleCommission() {
		return eligibleCommission;
	}

	public void setEligibleCommission(Boolean eligibleCommission) {
		this.eligibleCommission = eligibleCommission;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_groupe_produit")
	public TaGroupeProduit getTaGroupeProduit() {
		return taGroupeProduit;
	}

	public void setTaGroupeProduit(TaGroupeProduit taGroupeProduit) {
		this.taGroupeProduit = taGroupeProduit;
	}
	
}
