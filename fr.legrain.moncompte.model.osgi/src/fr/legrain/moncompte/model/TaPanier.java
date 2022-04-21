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
@Table(name = "ta_panier")
public class TaPanier implements java.io.Serializable {
	

	private static final long serialVersionUID = -1791584148276356085L;

	private Integer idPanier;
	private TaClient taClient;
	private TaDossier taDossier;
	private String codeRevendeur;
	private String niveau;
	private String codePromo;
	private BigDecimal tauxReduction;
	private Integer nbUtilisateur;
	private Integer nbPosteInstalle;
	private Integer nbMois;
	private Integer nbMoisRecurPaiement;
	private Boolean accesWS;
	private Date dateCreation;
	private Date datePaiement;
	private String libelle;
	private BigDecimal totalHT;
	private BigDecimal totalTTC;
	private BigDecimal totalTVA;
	private String refPaiement;
	private TaTypePaiement taTypePaiement;
	private Boolean paye = false;
	private boolean valideParClient = false;
	private TaCgv taCgv;
	private TaCategoriePro taCategoriePro;
	private List<TaLignePanier> lignes;
	
	private String commentaireLegrain;
	private String commentaireClient;

	private String ipAcces;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private Integer versionObj;

	public TaPanier() {
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taPanier", orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	//@OrderBy("numLigneLDocument")
	public List<TaLignePanier> getLignes() {
		return this.lignes;
	}

	public void setLignes(List<TaLignePanier> taLFactures) {
		this.lignes = taLFactures;
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
	@Column(name="id_panier")
	public Integer getIdPanier() {
		return idPanier;
	}

	public void setIdPanier(Integer idPanier) {
		this.idPanier = idPanier;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_client")
	public TaClient getTaClient() {
		return taClient;
	}

	public void setTaClient(TaClient taClient) {
		this.taClient = taClient;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_dossier")
	public TaDossier getTaDossier() {
		return taDossier;
	}

	public void setTaDossier(TaDossier taDossier) {
		this.taDossier = taDossier;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cgv")
	public TaCgv getTaCgv() {
		return taCgv;
	}

	public void setTaCgv(TaCgv taCgv) {
		this.taCgv = taCgv;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_categorie_pro")
	public TaCategoriePro getTaCategoriePro() {
		return taCategoriePro;
	}

	public void setTaCategoriePro(TaCategoriePro taCategoriePro) {
		this.taCategoriePro = taCategoriePro;
	}

	@Column(name="date_creation")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	@Column(name="date_paiement")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDatePaiement() {
		return datePaiement;
	}

	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}

	@Column(name="libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name="total_ht")
	public BigDecimal getTotalHT() {
		return totalHT;
	}

	public void setTotalHT(BigDecimal totalHT) {
		this.totalHT = totalHT;
	}

	@Column(name="totla_ttc")
	public BigDecimal getTotalTTC() {
		return totalTTC;
	}

	public void setTotalTTC(BigDecimal totalTTC) {
		this.totalTTC = totalTTC;
	}

	@Column(name="total_tva")
	public BigDecimal getTotalTVA() {
		return totalTVA;
	}

	public void setTotalTVA(BigDecimal totalTVA) {
		this.totalTVA = totalTVA;
	}

	@Column(name="paye")
	public Boolean getPaye() {
		return paye;
	}

	public void setPaye(Boolean paye) {
		this.paye = paye;
	}

	@Column(name="code_revendeur")
	public String getCodeRevendeur() {
		return codeRevendeur;
	}

	public void setCodeRevendeur(String codeRevendeur) {
		this.codeRevendeur = codeRevendeur;
	}

	@Column(name="niveau")
	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}
	
	@Column(name="code_promo")
	public String getCodePromo() {
		return codePromo;
	}

	public void setCodePromo(String codePromo) {
		this.codePromo = codePromo;
	}

	@Column(name="taux_reduction")
	public BigDecimal getTauxReduction() {
		return tauxReduction;
	}

	public void setTauxReduction(BigDecimal tauxReduction) {
		this.tauxReduction = tauxReduction;
	}

	@Column(name="nb_utilisateur")
	public Integer getNbUtilisateur() {
		return nbUtilisateur;
	}

	public void setNbUtilisateur(Integer nbUtilisateur) {
		this.nbUtilisateur = nbUtilisateur;
	}

	@Column(name="nb_poste_installe")
	public Integer getNbPosteInstalle() {
		return nbPosteInstalle;
	}

	public void setNbPosteInstalle(Integer nbPosteInstalle) {
		this.nbPosteInstalle = nbPosteInstalle;
	}

	@Column(name="nb_mois")
	public Integer getNbMois() {
		return nbMois;
	}

	public void setNbMois(Integer nbMois) {
		this.nbMois = nbMois;
	}

	@Column(name="acces_ws")
	public Boolean getAccesWS() {
		return accesWS;
	}

	public void setAccesWS(Boolean accesWS) {
		this.accesWS = accesWS;
	}

	@Column(name="ref_paiement")
	public String getRefPaiement() {
		return refPaiement;
	}

	public void setRefPaiement(String refPaiement) {
		this.refPaiement = refPaiement;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_type_paiement")
	public TaTypePaiement getTaTypePaiement() {
		return taTypePaiement;
	}

	public void setTaTypePaiement(TaTypePaiement taTypePaiement) {
		this.taTypePaiement = taTypePaiement;
	}

	@Column(name="nb_mois_recur_paiement")
	public Integer getNbMoisRecurPaiement() {
		return nbMoisRecurPaiement;
	}
	
	public void setNbMoisRecurPaiement(Integer nbMoisRecurPaiement) {
		this.nbMoisRecurPaiement = nbMoisRecurPaiement;
	}

	@Column(name="valide_par_client")
	public boolean isValideParClient() {
		return valideParClient;
	}

	public void setValideParClient(boolean valideParClient) {
		this.valideParClient = valideParClient;
	}

	@Column(name="commentaire_legrain")
	public String getCommentaireLegrain() {
		return commentaireLegrain;
	}

	public void setCommentaireLegrain(String commentaireLegrain) {
		this.commentaireLegrain = commentaireLegrain;
	}
	
	@Column(name="commentaire_client")
	public String getCommentaireClient() {
		return commentaireClient;
	}

	public void setCommentaireClient(String commentaireClient) {
		this.commentaireClient = commentaireClient;
	}

}
