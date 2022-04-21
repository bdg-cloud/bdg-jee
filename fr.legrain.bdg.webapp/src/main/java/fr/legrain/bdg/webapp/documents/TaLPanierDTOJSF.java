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
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.stock.model.TaMouvementStock;


public class TaLPanierDTOJSF extends TaLDocDTOJSF  implements java.io.Serializable, ILigneDocumentJSF<TaLPanierDTO>{

	private static final long serialVersionUID = -3584548556600518493L;
	
	
	private TaLPanierDTO dto;
	private TaLot taLot;
	private TaArticle taArticle;
	private TaArticleDTO taArticleDTO;
	private TaEntrepot taEntrepot;
	private TaRapportUnite taRapport;
	private TaMouvementStock taMouvementStock;
	private ArticleLotEntrepotDTO articleLotEntrepotDTO;
	private TaUnite taUnite1;
	private TaUnite taUnite2;
	private TaCoefficientUnite taCoefficientUnite;
	private TaREtatLigneDocument taREtat;
	
	List<TaLigneALigneDTO> ligneLieeFils;
	List<TaLigneALigneDTO> ligneLieeMere; 
	
	public TaLPanierDTOJSF() {
		
	}
	
	// TODO gestion annulation ligne
	public TaLPanierDTOJSF copy(ILigneDocumentJSF i) {
		TaLPanierDTOJSF ihm = (TaLPanierDTOJSF) i;
		TaLPanierDTOJSF a = new TaLPanierDTOJSF();
		a.setDto(TaLPanierDTO.copy(ihm.dto));
		a.setTaArticle(ihm.taArticle);
		a.setTaArticleDTO(ihm.taArticleDTO);
		a.setTaEntrepot(ihm.taEntrepot);
		a.setTaLot(ihm.taLot);
		a.setTaRapport(ihm.taRapport);
		a.setTaUnite1(ihm.taUnite1);
		a.setTaUnite2(ihm.taUnite2);
		a.setTaMouvementStock(ihm.taMouvementStock);
		a.setTaCoefficientUnite(ihm.taCoefficientUnite);
		a.setTaREtatLigneDocument(ihm.taREtat);
		a.setTaUniteSaisie(ihm.taUniteSaisie);
		a.setTaCoefficientUniteSaisie(ihm.taCoefficientUniteSaisie);
		return a;
	}
	
	public void updateDTO(boolean modification) {
		if(dto!=null) {
			if(taArticle!=null) {
				dto.setCodeArticle(taArticle.getCodeArticle());
				dto.setIdArticle(taArticle.getIdArticle());
			}
			if(taArticle!=null) {
				if(taArticleDTO==null) {
					taArticleDTO = new TaArticleDTO();
				}
				taArticleDTO.setCodeArticle(taArticle.getCodeArticle());
				//taArticleDTO.setIdArticle(taArticle.getIdArticle());
			}
			if(!modification && taEntrepot!=null) {
				dto.setCodeEntrepot(taEntrepot.getCodeEntrepot());
			}
			if(!modification && taLot!=null) {
				dto.setNumLot(taLot.getNumLot());
				dto.setLibelleLot(taLot.getLibelle());
			}	
			if(!modification && taUnite1!=null) {
				dto.setU1LDocument(taUnite1.getCodeUnite());
			}
			if(!modification && taUnite2!=null) {
				dto.setU2LDocument(taUnite2.getCodeUnite());
			}
			if(taUniteSaisie!=null) {
				dto.setUSaisieLDocument(taUniteSaisie.getCodeUnite());
			}
			if(taREtat!=null && taREtat.getTaEtat()!=null) {
				dto.setCodeEtat(taREtat.getTaEtat().getCodeEtat());
			}
		}
	}

	public TaLPanierDTO getDto() {
		return dto;
	}

	public void setDto(TaLPanierDTO ligne) {
		this.dto = ligne;
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
	
	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getLibLDocument()!=null && !dto.getLibLDocument().equals(""))return false;		
		return true;
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
	
	public boolean ligneCommentaire() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getCodeTLigne()!=null && dto.getCodeTLigne().equals("C"))return true;	
		return true;
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
}

