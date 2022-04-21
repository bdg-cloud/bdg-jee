package fr.legrain.bdg.webapp.solstyce;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.document.dto.TaLModeleFabricationDTO;
import fr.legrain.stock.model.TaMouvementStock;


public class TaLModeleFabricationDTOJSF  implements java.io.Serializable{

	private static final long serialVersionUID = -1870470751593796311L;
	
	private TaLModeleFabricationDTO dto;
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
	
	public TaLModeleFabricationDTOJSF() {
		
	}
	
	public void updateDTO() {
		if(dto!=null) {
			if(taArticle!=null) {
				dto.setCodeArticle(taArticle.getCodeArticle());
				dto.setIdArticle(taArticle.getIdArticle());
			}
			if(taArticleDTO!=null) {
				dto.setCodeArticle(taArticleDTO.getCodeArticle());
				dto.setIdArticle(taArticleDTO.getId());
			}
			if(taEntrepot!=null) {
				dto.setCodeEntrepot(taEntrepot.getCodeEntrepot());
			}
			if(taLot!=null) {
				dto.setNumLot(taLot.getNumLot());
				//dto.setLibelleLot(taLot.getNumLot());
			}	
			if(taUnite1!=null) {
				dto.setU1LDocument(taUnite1.getCodeUnite());
			}
			if(taUnite2!=null) {
				dto.setU2LDocument(taUnite2.getCodeUnite());
			}
		}
	}

	public TaLModeleFabricationDTO getDto() {
		return dto;
	}

	public void setDto(TaLModeleFabricationDTO ligne) {
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
	public TaCoefficientUnite getTaCoefficientUnite() {
		return taCoefficientUnite;
	}
	public void setTaCoefficientUnite(TaCoefficientUnite taCoefficientUnite) {
		this.taCoefficientUnite = taCoefficientUnite;
	}
}
