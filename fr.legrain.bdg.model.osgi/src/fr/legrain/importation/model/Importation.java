package fr.legrain.importation.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFamille;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;




public class Importation implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -3398426305505465412L;

	static Logger logger = Logger.getLogger(Importation.class.getName());
	
	private String fichierImportation ="D:/divers/Nouveau dossier/IMPORT.TXT";
	private String fichierImportationServeur ="/tmp/E2-IMPOR.TXT";
	public String finDeLigne = "\r\n";

    
    public List<String> listComplete=new LinkedList<String>();
    
	public String codeArticle; 
	public String libellecArticle; 
	public String libellelArticle; 
	public String numcptArticle; 
	public Boolean actif; 
	public String commentaireArticle; 
	public String diversArticle; 
	public BigDecimal stockMinArticle; 
	public String codeFamille; 
	public String libcFamille; 
	public BigDecimal prixPrix; 
	public BigDecimal prixttcPrix; 
	public String codeUnite; 
	public String codeUnite2; 
	public BigDecimal rapport; 
	public Boolean getSens; 
	public String codeTitreTransport; 
	public BigDecimal qteTitreTransport; 
	public String codeTTva; 
	public String libTTva; 
	public String codeTva; 
	public String numcptTva; 
	public BigDecimal tauxTva; 

//	public boolean produitfini; 
//	public boolean matierePremiere; 
//	public boolean gestionLot; 
//	public String codeBarre; 
//	public int dlc; 
	public BigDecimal prixHT; 
	public BigDecimal prixTTC ;
	public BigDecimal prix1HT; 
	public BigDecimal prix1TTC;
	public String codeTTarif1; 
	public String libelleTTarif1; 
	public BigDecimal prix2HT; 
	public BigDecimal prix2TTC ;
	public String codeTTarif2; 
	public String libelleTTarif2; 
	public BigDecimal prix3HT; 
	public BigDecimal prix3TTC ;
	public String codeTTarif3; 
	public String libelleTTarif3; 
	
	//besoins spécifiques
	public String codeFamille2; 
	public String libcFamille2; 
	
	public String codeFamille3; 
	public String libcFamille3; 
	
	public String marque; 
	
	public boolean mp=false;
	public boolean pf=false;
    
	public Importation() {}

	public String getFichierImportation() {
		return fichierImportation;
	}

	public void setFichierImportation(String fichierImportation) {
		this.fichierImportation = fichierImportation;
	}

	public String getFichierImportationServeur() {
		return fichierImportationServeur;
	}

	public void setFichierImportationServeur(String fichierImportationServeur) {
		this.fichierImportationServeur = fichierImportationServeur;
	}

	public String getFinDeLigne() {
		return finDeLigne;
	}

	public void setFinDeLigne(String finDeLigne) {
		this.finDeLigne = finDeLigne;
	}

	public List<String> getListComplete() {
		return listComplete;
	}

	public void setListComplete(List<String> listComplete) {
		this.listComplete = listComplete;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getLibellecArticle() {
		return libellecArticle;
	}

	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
	}

	public String getLibellelArticle() {
		return libellelArticle;
	}

	public void setLibellelArticle(String libellelArticle) {
		this.libellelArticle = libellelArticle;
	}

	public String getNumcptArticle() {
		return numcptArticle;
	}

	public void setNumcptArticle(String numcptArticle) {
		this.numcptArticle = numcptArticle;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	public String getCommentaireArticle() {
		return commentaireArticle;
	}

	public void setCommentaireArticle(String commentaireArticle) {
		this.commentaireArticle = commentaireArticle;
	}

	public String getDiversArticle() {
		return diversArticle;
	}

	public void setDiversArticle(String diversArticle) {
		this.diversArticle = diversArticle;
	}

	public BigDecimal getStockMinArticle() {
		return stockMinArticle;
	}

	public void setStockMinArticle(BigDecimal stockMinArticle) {
		this.stockMinArticle = stockMinArticle;
	}

	public String getCodeFamille() {
		return codeFamille;
	}

	public void setCodeFamille(String codeFamille) {
		this.codeFamille = codeFamille;
	}

	public String getLibcFamille() {
		return libcFamille;
	}

	public void setLibcFamille(String libcFamille) {
		this.libcFamille = libcFamille;
	}

	public BigDecimal getPrixPrix() {
		return prixPrix;
	}

	public void setPrixPrix(BigDecimal prixPrix) {
		this.prixPrix = prixPrix;
	}

	public BigDecimal getPrixttcPrix() {
		return prixttcPrix;
	}

	public void setPrixttcPrix(BigDecimal prixttcPrix) {
		this.prixttcPrix = prixttcPrix;
	}

	public String getCodeUnite() {
		return codeUnite;
	}

	public void setCodeUnite(String codeUnite) {
		this.codeUnite = codeUnite;
	}

	public String getCodeUnite2() {
		return codeUnite2;
	}

	public void setCodeUnite2(String codeUnite2) {
		this.codeUnite2 = codeUnite2;
	}

	public BigDecimal getRapport() {
		return rapport;
	}

	public void setRapport(BigDecimal rapport) {
		this.rapport = rapport;
	}

	public Boolean getGetSens() {
		return getSens;
	}

	public void setGetSens(Boolean getSens) {
		this.getSens = getSens;
	}

	public String getCodeTitreTransport() {
		return codeTitreTransport;
	}

	public void setCodeTitreTransport(String codeTitreTransport) {
		this.codeTitreTransport = codeTitreTransport;
	}

	public BigDecimal getQteTitreTransport() {
		return qteTitreTransport;
	}

	public void setQteTitreTransport(BigDecimal qteTitreTransport) {
		this.qteTitreTransport = qteTitreTransport;
	}

	public String getCodeTTva() {
		return codeTTva;
	}

	public void setCodeTTva(String codeTTva) {
		this.codeTTva = codeTTva;
	}

	public String getLibTTva() {
		return libTTva;
	}

	public void setLibTTva(String libTTva) {
		this.libTTva = libTTva;
	}

	public String getCodeTva() {
		return codeTva;
	}

	public void setCodeTva(String codeTva) {
		this.codeTva = codeTva;
	}

	public String getNumcptTva() {
		return numcptTva;
	}

	public void setNumcptTva(String numcptTva) {
		this.numcptTva = numcptTva;
	}

	public BigDecimal getTauxTva() {
		return tauxTva;
	}

	public void setTauxTva(BigDecimal tauxTva) {
		this.tauxTva = tauxTva;
	}

	public BigDecimal getPrix1HT() {
		return prix1HT;
	}

	public void setPrix1HT(BigDecimal prix1ht) {
		prix1HT = prix1ht;
	}

	public BigDecimal getPrix1TTC() {
		return prix1TTC;
	}

	public void setPrix1TTC(BigDecimal prix1ttc) {
		prix1TTC = prix1ttc;
	}

	public String getCodeTTarif1() {
		return codeTTarif1;
	}

	public void setCodeTTarif1(String codeTTarif1) {
		this.codeTTarif1 = codeTTarif1;
	}

	public String getLibelleTTarif1() {
		return libelleTTarif1;
	}

	public void setLibelleTTarif1(String libelleTTarif1) {
		this.libelleTTarif1 = libelleTTarif1;
	}

	public BigDecimal getPrix2HT() {
		return prix2HT;
	}

	public void setPrix2HT(BigDecimal prix2ht) {
		prix2HT = prix2ht;
	}

	public BigDecimal getPrix2TTC() {
		return prix2TTC;
	}

	public void setPrix2TTC(BigDecimal prix2ttc) {
		prix2TTC = prix2ttc;
	}

	public String getCodeTTarif2() {
		return codeTTarif2;
	}

	public void setCodeTTarif2(String codeTTarif2) {
		this.codeTTarif2 = codeTTarif2;
	}

	public String getLibelleTTarif2() {
		return libelleTTarif2;
	}

	public void setLibelleTTarif2(String libelleTTarif2) {
		this.libelleTTarif2 = libelleTTarif2;
	}

	public String getCodeFamille2() {
		return codeFamille2;
	}

	public void setCodeFamille2(String codeFamille2) {
		this.codeFamille2 = codeFamille2;
	}

	public String getLibcFamille2() {
		return libcFamille2;
	}

	public void setLibcFamille2(String libcFamille2) {
		this.libcFamille2 = libcFamille2;
	}

	public String getCodeFamille3() {
		return codeFamille3;
	}

	public void setCodeFamille3(String codeFamille3) {
		this.codeFamille3 = codeFamille3;
	}

	public String getLibcFamille3() {
		return libcFamille3;
	}

	public void setLibcFamille3(String libcFamille3) {
		this.libcFamille3 = libcFamille3;
	}

	public String getMarque() {
		return marque;
	}

	public void setMarque(String marque) {
		this.marque = marque;
	}

	public boolean isMp() {
		return mp;
	}

	public void setMp(boolean mp) {
		this.mp = mp;
	}

	public boolean isPf() {
		return pf;
	}

	public void setPf(boolean pf) {
		this.pf = pf;
	}
	
	
	
}





