package fr.legrain.bdg.webapp.stocks;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.stock.dto.MouvementStocksDTO;


public class TaMouvementStockDTOJSF  implements java.io.Serializable{

	private static final long serialVersionUID = 894328367072057512L;

	private MouvementStocksDTO dto;
	private TaLot taLot;
	private TaArticle taArticle;
	private TaArticleDTO taArticleDTO;
	private TaEntrepot taEntrepotOrg;
	private TaEntrepot taEntrepotDest;
	private TaRapportUnite taRapport;
	private ArticleLotEntrepotDTO articleLotEntrepotDTO;
	private TaUnite taUnite1;
	private TaUnite taUnite2;
	
	protected TaTitreTransport taTitreTransport;
	protected TaCoefficientUnite taCoefficientUnite;
	
	public TaMouvementStockDTOJSF() {
		
	}

	
	// TODO gestion annulation ligne
	public TaMouvementStockDTOJSF copy(TaMouvementStockDTOJSF i) {
		TaMouvementStockDTOJSF ihm = (TaMouvementStockDTOJSF) i;
		TaMouvementStockDTOJSF a = new TaMouvementStockDTOJSF();
		a.setDto(MouvementStocksDTO.copy(ihm.dto));
		a.setTaArticle(ihm.taArticle);
		a.setTaArticleDTO(ihm.taArticleDTO);
		a.setTaEntrepotOrg(ihm.taEntrepotOrg);
		a.setTaEntrepotDest(ihm.taEntrepotDest);
		a.setTaLot(ihm.taLot);
		a.setArticleLotEntrepotDTO(ihm.articleLotEntrepotDTO);
		a.setTaRapport(ihm.taRapport);
		a.setTaUnite1(ihm.taUnite1);
		a.setTaUnite2(ihm.taUnite2);
		return a;
	}
	
	public MouvementStocksDTO getDto() {
		return dto;
	}

	public void setDto(MouvementStocksDTO ligne) {
		this.dto = ligne;
	}

	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	public TaEntrepot getTaEntrepotOrg() {
		return taEntrepotOrg;
	}

	public void setTaEntrepotOrg(TaEntrepot taEntrepot) {
		this.taEntrepotOrg = taEntrepot;
	}

	public TaEntrepot getTaEntrepotDest() {
		return taEntrepotDest;
	}

	public void setTaEntrepotDest(TaEntrepot taEntrepotDest) {
		this.taEntrepotDest = taEntrepotDest;
	}

	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
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
			if(taEntrepotOrg!=null) {
				dto.setCodeEntrepot(taEntrepotOrg.getCodeEntrepot());
			}
			if(taEntrepotDest!=null) {
				dto.setCodeEntrepotDest(taEntrepotDest.getCodeEntrepot());
			}
			if(taTitreTransport!=null) {
				dto.setCodeTitreTransport(taTitreTransport.getCodeTitreTransport());
			}
			if(taLot!=null) {
				dto.setNumLot(taLot.getNumLot());
				dto.setIdLot(taLot.getIdLot());
			}	
			if(taUnite1!=null) {
				dto.setUn1Stock(taUnite1.getCodeUnite());
			}
			if(taUnite2!=null) {
				dto.setUn2Stock(taUnite2.getCodeUnite());
			}
		}
	}

	public ArticleLotEntrepotDTO getArticleLotEntrepotDTO() {
		return articleLotEntrepotDTO;
	}

	public void setArticleLotEntrepotDTO(ArticleLotEntrepotDTO articleLotEntrepotDTO) {
		this.articleLotEntrepotDTO = articleLotEntrepotDTO;
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
	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getLibelleStock()!=null && !dto.getLibelleStock().equals(""))return false;		
		return true;
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


	
	public TaTitreTransport getTaTitreTransport() {
		return taTitreTransport;
	}

	public void setTaTitreTransport(TaTitreTransport taTitreTransport) {
		this.taTitreTransport = taTitreTransport;
	}


	
	public TaCoefficientUnite getTaCoefficientUnite() {
		return taCoefficientUnite;
	}
	public void setTaCoefficientUnite(TaCoefficientUnite taCoefficientUnite) {
		this.taCoefficientUnite = taCoefficientUnite;
	}

}
