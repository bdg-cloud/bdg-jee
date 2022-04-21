package fr.legrain.bdg.webapp.documents;

import java.util.List;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.stock.model.TaMouvementStock;


public class TaLDocDTOJSF  implements java.io.Serializable{

	private static final long serialVersionUID = -310675944497718855L;
	

	protected TaLot taLot;
	protected TaArticle taArticle;
	protected TaArticleDTO taArticleDTO;
	protected TaEntrepot taEntrepot;
	protected TaRapportUnite taRapport;
	protected TaMouvementStock taMouvementStock;
	protected ArticleLotEntrepotDTO articleLotEntrepotDTO;
	protected TaUnite taUnite1;
	protected TaUnite taUnite2;
	protected TaUnite taUniteSaisie;
	protected TaCoefficientUnite taCoefficientUnite;
	protected TaCoefficientUnite taCoefficientUniteSaisie;
	
	protected TaREtatLigneDocument taREtat;
	
	protected List<TaLigneALigneDTO> ligneLieeFils;
	protected List<TaLigneALigneDTO> ligneLieeMere;
	protected List<TaLigneALigneEcheanceDTO> ligneAbonnement;
	
	protected String codeEtat;
	protected String libelleEtat;
	

	
	public TaLDocDTOJSF() {
		
	}
	
	
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	public TaEntrepot getTaEntrepot() {
		return taEntrepot;
	}

	public void setTaEntrepot(TaEntrepot taEntrepot) {
		this.taEntrepot = taEntrepot;
	}

	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}

	public TaRapportUnite getTaRapport() {
		return taRapport;
	}

	public void setTaRapport(TaRapportUnite taRapport) {
		this.taRapport = taRapport;
	}

	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}

	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	public TaUnite getTaUnite1() {
		return taUnite1;
	}

	public void setTaUnite1(TaUnite taUnite1) {
		this.taUnite1 = taUnite1;
	}

	public TaUnite getTaUnite2() {
		return taUnite2;
	}

	public void setTaUnite2(TaUnite taUnite2) {
		this.taUnite2 = taUnite2;
	}
	

	public TaMouvementStock getTaMouvementStock() {
		return taMouvementStock;
	}

	public void setTaMouvementStock(TaMouvementStock taMouvementStock) {
		this.taMouvementStock = taMouvementStock;
	}

	public ArticleLotEntrepotDTO getArticleLotEntrepotDTO() {
		return articleLotEntrepotDTO;
	}

	public void setArticleLotEntrepotDTO(ArticleLotEntrepotDTO articleLotEntrepotDTO) {
		this.articleLotEntrepotDTO = articleLotEntrepotDTO;
	}

	
	public TaCoefficientUnite getTaCoefficientUnite() {
		return taCoefficientUnite;
	}
	public void setTaCoefficientUnite(TaCoefficientUnite taCoefficientUnite) {
		this.taCoefficientUnite = taCoefficientUnite;
	}
	

	public TaREtatLigneDocument getTaREtatLigneDocument() {
		return taREtat;
	}

	public void setTaREtatLigneDocument(TaREtatLigneDocument taREtat) {
		this.taREtat = taREtat;
	}



	public List<TaLigneALigneDTO> getLigneLieeFils() {
		return ligneLieeFils;
	}

	public void setLigneLieeFils(List<TaLigneALigneDTO> ligneLieeFils) {
		this.ligneLieeFils = ligneLieeFils;
	}

	public List<TaLigneALigneDTO> getLigneLieeMere() {
		return ligneLieeMere;
	}

	public void setLigneLieeMere(List<TaLigneALigneDTO> ligneLieeMere) {
		this.ligneLieeMere = ligneLieeMere;
	}


	public String getCodeEtat() {
		return codeEtat;
	}


	public void setCodeEtat(String codeEtat) {
		this.codeEtat = codeEtat;
	}


	public String getLibelleEtat() {
		return libelleEtat;
	}


	public void setLibelleEtat(String libelleEtat) {
		this.libelleEtat = libelleEtat;
	}


	public List<TaLigneALigneEcheanceDTO> getLigneAbonnement() {
		return ligneAbonnement;
	}


	public void setLigneAbonnement(List<TaLigneALigneEcheanceDTO> ligneAbonnement) {
		this.ligneAbonnement = ligneAbonnement;
	}


	public TaUnite getTaUniteSaisie() {
		return taUniteSaisie;
	}


	public void setTaUniteSaisie(TaUnite taUniteSaisie) {
		this.taUniteSaisie = taUniteSaisie;
	}


	public TaCoefficientUnite getTaCoefficientUniteSaisie() {
		return taCoefficientUniteSaisie;
	}


	public void setTaCoefficientUniteSaisie(TaCoefficientUnite taCoefficientUniteSaisie) {
		this.taCoefficientUniteSaisie = taCoefficientUniteSaisie;
	}

}

