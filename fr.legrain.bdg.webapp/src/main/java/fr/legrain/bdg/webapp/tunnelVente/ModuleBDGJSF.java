package fr.legrain.bdg.webapp.tunnelVente;

import java.math.BigDecimal;
import java.util.Date;

import fr.legrain.document.dto.TaLEcheanceDTO;

/**
 * 
 * @author Yann
 * Cette classe sert a présenter de façon commerciale des modules ou ensembles de modules BDG (versions) dans les vues du tunnel de vente
 * Elle rassemble donc des informations des échéances, des lignes abonnements, des articles, des plans etc...
 */
public class ModuleBDGJSF {
	
	private String libelleModule;
	private String codeArticle;
	private String codeModuleBDG;
	
	private Integer idLEcheance;
	private Integer idArticle;
	private Integer idPlan;
	private Integer idLAbonnement;
	
	private Date debutAbonnement;
	
	private String liblPlan;
	private BigDecimal prixTTCMois;
	private BigDecimal prixTTCAn;
	private Integer nbMois;
	private BigDecimal prixTTCXMois;
	
	private Date dateDebutPeriode;
	private Date dateFinPeriode;
	private Date dateEcheance;
	
	private BigDecimal nbLicenses;
	
	private String liblEtat;
	
	private String liblComplet;
	private String liblPrixMois;
	
	private String descriptifModule;
	
	private TaLEcheanceDTO taLEcheanceDTO;
	
	
	
	
	
	
	

	public String getLibelleModule() {
		return libelleModule;
	}
	public String getCodeArticle() {
		return codeArticle;
	}
	public String getCodeModuleBDG() {
		return codeModuleBDG;
	}
	public Integer getIdArticle() {
		return idArticle;
	}
	public Integer getIdPlan() {
		return idPlan;
	}
	public Date getDebutAbonnement() {
		return debutAbonnement;
	}
	public String getLiblPlan() {
		return liblPlan;
	}
	public BigDecimal getPrixTTCMois() {
		return prixTTCMois;
	}
	public BigDecimal getPrixTTCAn() {
		return prixTTCAn;
	}
	public Integer getNbMois() {
		return nbMois;
	}
	public BigDecimal getPrixTTCXMois() {
		return prixTTCXMois;
	}
	public Date getDateDebutPeriode() {
		return dateDebutPeriode;
	}
	public Date getDateFinPeriode() {
		return dateFinPeriode;
	}
	public Date getDateEcheance() {
		return dateEcheance;
	}
	public void setLibelleModule(String libelleModule) {
		this.libelleModule = libelleModule;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}
	public void setCodeModuleBDG(String codeModuleBDG) {
		this.codeModuleBDG = codeModuleBDG;
	}
	public void setIdArticle(Integer idArticle) {
		this.idArticle = idArticle;
	}
	public void setIdPlan(Integer idPlan) {
		this.idPlan = idPlan;
	}
	public void setDebutAbonnement(Date debutAbonnement) {
		this.debutAbonnement = debutAbonnement;
	}
	public void setLiblPlan(String liblPlan) {
		this.liblPlan = liblPlan;
	}
	public void setPrixTTCMois(BigDecimal prixTTCMois) {
		this.prixTTCMois = prixTTCMois;
	}
	public void setPrixTTCAn(BigDecimal prixTTCAn) {
		this.prixTTCAn = prixTTCAn;
	}
	public void setNbMois(Integer nbMois) {
		this.nbMois = nbMois;
	}
	public void setPrixTTCXMois(BigDecimal prixTTCXMois) {
		this.prixTTCXMois = prixTTCXMois;
	}
	public void setDateDebutPeriode(Date dateDebutPeriode) {
		this.dateDebutPeriode = dateDebutPeriode;
	}
	public void setDateFinPeriode(Date dateFinPeriode) {
		this.dateFinPeriode = dateFinPeriode;
	}
	public void setDateEcheance(Date dateEcheance) {
		this.dateEcheance = dateEcheance;
	}
	public Integer getIdLAbonnement() {
		return idLAbonnement;
	}
	public void setIdLAbonnement(Integer idLAbonnement) {
		this.idLAbonnement = idLAbonnement;
	}
	public String getLiblEtat() {
		return liblEtat;
	}
	public void setLiblEtat(String liblEtat) {
		this.liblEtat = liblEtat;
	}
	public BigDecimal getNbLicenses() {
		return nbLicenses;
	}
	public void setNbLicenses(BigDecimal nbLicenses) {
		this.nbLicenses = nbLicenses;
	}
	public String getLiblComplet() {
		return liblComplet;
	}
	public void setLiblComplet(String liblComplet) {
		this.liblComplet = liblComplet;
	}
	public String getLiblPrixMois() {
		return liblPrixMois;
	}
	public void setLiblPrixMois(String liblPrixMois) {
		this.liblPrixMois = liblPrixMois;
	}
	public Integer getIdLEcheance() {
		return idLEcheance;
	}
	public void setIdLEcheance(Integer idLEcheance) {
		this.idLEcheance = idLEcheance;
	}
	public String getDescriptifModule() {
		return descriptifModule;
	}
	public void setDescriptifModule(String descriptifModule) {
		this.descriptifModule = descriptifModule;
	}
	public TaLEcheanceDTO getTaLEcheanceDTO() {
		return taLEcheanceDTO;
	}
	public void setTaLEcheanceDTO(TaLEcheanceDTO taLEcheanceDTO) {
		this.taLEcheanceDTO = taLEcheanceDTO;
	}
	
	
	
	
	
	

	
	
}
