package fr.legrain.moncompte.model;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@Table(name = "ta_prix_perso")
@NamedQueries(value = { 
		@NamedQuery(name=TaPrixPerso.QN.FIND_BY_CODE, query="select f from TaPrixPerso f where f.id= :code")
})
public class TaPrixPerso implements Serializable {

	private static final long serialVersionUID = -569443777253534183L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaPrixPerso.findByCode";
	}

	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_dossier")
	@XmlInverseReference(mappedBy="listePrixPerso")
	private TaDossier taDossier;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_produit")
	private TaProduit taProduit;

	@Column(name = "prix_ht")
	private BigDecimal prixHT;

	@Column(name = "tva")
	private BigDecimal tva;

	@Column(name = "prix_ttc")
	private BigDecimal prixTTC;
	
	@Column(name = "prix_ht_light")
	private BigDecimal prixHTLight;

	@Column(name = "tva_light")
	private BigDecimal tvaLight;

	@Column(name = "prix_ttc_light")
	private BigDecimal prixTTCLight;
	
	@Column(name = "prix_ht_par_poste_installe")
	private BigDecimal prixHTParPosteInstalle;

	@Column(name = "tva_par_poste_installe")
	private BigDecimal tvaParPosteInstalle;

	@Column(name = "prix_ttc_par_poste_installe")
	private BigDecimal prixTTCParPosteInstalle;
	
	@Column(name = "prix_ht_ws")
	private BigDecimal prixHTWs;

	@Column(name = "tva_ws")
	private BigDecimal tvaWs;

	@Column(name = "prix_ttc_ws")
	private BigDecimal prixTTCWs;
	
	@Column(name = "prix_ht_licence")
	private BigDecimal prixHTLicence;
	
	@Column(name = "tva_licence")
	private BigDecimal tvaLicence;
	
	@Column(name = "prix_ttc_licence")
	private BigDecimal prixTTCLicence;
	
	@Column(name = "prix_ht_maintenance")
	private BigDecimal prixHTMaintenance;
	
	@Column(name = "tva_maintenance")
	private BigDecimal tvaMaintenance;
	
	@Column(name = "prix_ttc_maintenance")
	private BigDecimal prixTTCMaintenance;
	
	@Column(name = "taux_tva")
	private BigDecimal tauxTVA;

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

	//	@Column(name = "ip_acces")
	//	private String ipAcces;

	@Version
	@Column(name = "version_obj")
	private Integer versionObj;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(BigDecimal prixHT) {
		this.prixHT = prixHT;
	}

	public BigDecimal getTauxTVA() {
		return tauxTVA;
	}

	public void setTauxTVA(BigDecimal tauxTVA) {
		this.tauxTVA = tauxTVA;
	}

	public BigDecimal getPrixTTC() {
		return prixTTC;
	}

	public void setPrixTTC(BigDecimal prixTTC) {
		this.prixTTC = prixTTC;
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


	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public TaProduit getTaProduit() {
		return taProduit;
	}

	public void setTaProduit(TaProduit taProduit) {
		this.taProduit = taProduit;
	}

	public TaDossier getTaDossier() {
		return taDossier;
	}

	public void setTaDossier(TaDossier taDossier) {
		this.taDossier = taDossier;
	}

	public BigDecimal getTva() {
		return tva;
	}

	public void setTva(BigDecimal tva) {
		this.tva = tva;
	}

	public BigDecimal getPrixHTLight() {
		return prixHTLight;
	}

	public void setPrixHTLight(BigDecimal prixHTLight) {
		this.prixHTLight = prixHTLight;
	}

	public BigDecimal getTvaLight() {
		return tvaLight;
	}

	public void setTvaLight(BigDecimal tvaLight) {
		this.tvaLight = tvaLight;
	}

	public BigDecimal getPrixTTCLight() {
		return prixTTCLight;
	}

	public void setPrixTTCLight(BigDecimal prixTTCLight) {
		this.prixTTCLight = prixTTCLight;
	}

	public BigDecimal getPrixHTParPosteInstalle() {
		return prixHTParPosteInstalle;
	}

	public void setPrixHTParPosteInstalle(BigDecimal prixHTParPosteInstalle) {
		this.prixHTParPosteInstalle = prixHTParPosteInstalle;
	}

	public BigDecimal getTvaParPosteInstalle() {
		return tvaParPosteInstalle;
	}

	public void setTvaParPosteInstalle(BigDecimal tvaParPosteInstalle) {
		this.tvaParPosteInstalle = tvaParPosteInstalle;
	}

	public BigDecimal getPrixTTCParPosteInstalle() {
		return prixTTCParPosteInstalle;
	}

	public void setPrixTTCParPosteInstalle(BigDecimal prixTTCParPosteInstalle) {
		this.prixTTCParPosteInstalle = prixTTCParPosteInstalle;
	}

	public BigDecimal getPrixHTWs() {
		return prixHTWs;
	}

	public void setPrixHTWs(BigDecimal prixHTWs) {
		this.prixHTWs = prixHTWs;
	}

	public BigDecimal getTvaWs() {
		return tvaWs;
	}

	public void setTvaWs(BigDecimal tvaWs) {
		this.tvaWs = tvaWs;
	}

	public BigDecimal getPrixTTCWs() {
		return prixTTCWs;
	}

	public void setPrixTTCWs(BigDecimal prixTTCWs) {
		this.prixTTCWs = prixTTCWs;
	}

	public BigDecimal getPrixHTLicence() {
		return prixHTLicence;
	}

	public void setPrixHTLicence(BigDecimal prixHTLicence) {
		this.prixHTLicence = prixHTLicence;
	}

	public BigDecimal getTvaLicence() {
		return tvaLicence;
	}

	public void setTvaLicence(BigDecimal tvaLicence) {
		this.tvaLicence = tvaLicence;
	}

	public BigDecimal getPrixTTCLicence() {
		return prixTTCLicence;
	}

	public void setPrixTTCLicence(BigDecimal prixTTCLicence) {
		this.prixTTCLicence = prixTTCLicence;
	}

	public BigDecimal getPrixHTMaintenance() {
		return prixHTMaintenance;
	}

	public void setPrixHTMaintenance(BigDecimal prixHTMaintenance) {
		this.prixHTMaintenance = prixHTMaintenance;
	}

	public BigDecimal getTvaMaintenance() {
		return tvaMaintenance;
	}

	public void setTvaMaintenance(BigDecimal tvaMaintenance) {
		this.tvaMaintenance = tvaMaintenance;
	}

	public BigDecimal getPrixTTCMaintenance() {
		return prixTTCMaintenance;
	}

	public void setPrixTTCMaintenance(BigDecimal prixTTCMaintenance) {
		this.prixTTCMaintenance = prixTTCMaintenance;
	}
	
	

}
