package fr.legrain.moncompte.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "ta_partenaire")
@NamedQueries(value = { 
		@NamedQuery(name=TaPartenaire.QN.FIND_BY_CODE, query="select f from TaPartenaire f where f.codePartenaire= :codePartenaire")
		})
public class TaPartenaire implements Serializable {
	
	private static final long serialVersionUID = -8379509622899175682L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaPartenaire.findByCode";
	}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="actif")
	private Boolean actif = true;
	
	@Column(name="code_revendeur")
	private String codePartenaire;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_type_partenaire")
	private TaTypePartenaire taTypePartenaire;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cg_partenaire")
	private TaCgPartenaire taCgPartenaire;
	
	@Column(name="date_debut")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDebut;
	
	@Column(name="titulaire_compte_banque")
	private String titulaireCompteBanque;
	
	@Column(name="nom_banque")
	private String nomBanque;
	
	@Column(name="adresse1_banque")
	private String adresse1Banque;
	
	@Column(name="adresse2_banque")
	private String adresse2Banque;
	
	@Column(name="cp_banque")
	private String cpBanque;
	
	@Column(name="ville_banque")
	private String villeBanque;
	
	@Column(name="iban_banque")
	private String ibanBanque;
	
	@Column(name="bic_swift_banque")
	private String bicSwiftBanque;
	
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

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getCodePartenaire() {
		return codePartenaire;
	}

	public void setCodePartenaire(String codePartenaire) {
		this.codePartenaire = codePartenaire;
	}

	public TaTypePartenaire getTaTypePartenaire() {
		return taTypePartenaire;
	}

	public void setTaTypePartenaire(TaTypePartenaire taTypePartenaire) {
		this.taTypePartenaire = taTypePartenaire;
	}

	public TaCgPartenaire getTaCgPartenaire() {
		return taCgPartenaire;
	}

	public void setTaCgPartenaire(TaCgPartenaire taCgPartenaire) {
		this.taCgPartenaire = taCgPartenaire;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getTitulaireCompteBanque() {
		return titulaireCompteBanque;
	}

	public void setTitulaireCompteBanque(String titulaireCompteBanque) {
		this.titulaireCompteBanque = titulaireCompteBanque;
	}

	public String getNomBanque() {
		return nomBanque;
	}

	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}

	public String getAdresse1Banque() {
		return adresse1Banque;
	}

	public void setAdresse1Banque(String adresse1Banque) {
		this.adresse1Banque = adresse1Banque;
	}

	public String getAdresse2Banque() {
		return adresse2Banque;
	}

	public void setAdresse2Banque(String adresse2Banque) {
		this.adresse2Banque = adresse2Banque;
	}

	public String getCpBanque() {
		return cpBanque;
	}

	public void setCpBanque(String cpBanque) {
		this.cpBanque = cpBanque;
	}

	public String getVilleBanque() {
		return villeBanque;
	}

	public void setVilleBanque(String villeBanque) {
		this.villeBanque = villeBanque;
	}

	public String getIbanBanque() {
		return ibanBanque;
	}

	public void setIbanBanque(String ibanBanque) {
		this.ibanBanque = ibanBanque;
	}

	public String getBicSwiftBanque() {
		return bicSwiftBanque;
	}

	public void setBicSwiftBanque(String bicSwiftBanque) {
		this.bicSwiftBanque = bicSwiftBanque;
	}

}
