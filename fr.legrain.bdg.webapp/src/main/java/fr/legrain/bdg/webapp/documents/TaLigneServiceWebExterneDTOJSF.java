package fr.legrain.bdg.webapp.documents;


import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.tiers.dto.TaTiersDTO;


public class TaLigneServiceWebExterneDTOJSF  implements java.io.Serializable{

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2534381659431693700L;
	private TaLigneServiceWebExterneDTO dto;
	private TaLigneServiceWebExterneDTO dtoOld;
	private TaEtat taEtatOld;
	private TaEtat taEtat;
	private TaTiersDTO taTiersDTO;
	private TaArticleDTO taArticleDTO;
	private TaLotDTO taLotDTO;
	private TaTPaiementDTO taTPaiementDTO;
	private ArticleLotEntrepotDTO articleLotEntrepotDTO;

	public TaLigneServiceWebExterneDTOJSF() {
		
	}



	public TaLigneServiceWebExterneDTO getDto() {
		return dto;
	}

	public void setDto(TaLigneServiceWebExterneDTO dto) {
		this.dto = dto;
	}

	public TaEtat getTaEtat() {
		return taEtat;
	}

	public void setTaEtat(TaEtat taEtat) {
		this.taEtat = taEtat;
	}


	public TaEtat getTaEtatOld() {
		return taEtatOld;
	}


	public void setTaEtatOld(TaEtat taEtatOld) {
		this.taEtatOld = taEtatOld;
	}


	public boolean equalsEtat(TaEtat oldTaEtat) {
		if (taEtat == null) {
			if (oldTaEtat != null)
				return false;
		} else if (!taEtat.equals(oldTaEtat))
			return false;
		return true;
	}


	public TaLigneServiceWebExterneDTO getDtoOld() {
		return dtoOld;
	}



	public void setDtoOld(TaLigneServiceWebExterneDTO dtoOld) {
		this.dtoOld = dtoOld;
	}


	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}



	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}



	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}


	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}



	public TaLotDTO getTaLotDTO() {
		return taLotDTO;
	}



	public void setTaLotDTO(TaLotDTO taLotDTO) {
		this.taLotDTO = taLotDTO;
	}



	public ArticleLotEntrepotDTO getArticleLotEntrepotDTO() {
		return articleLotEntrepotDTO;
	}



	public void setArticleLotEntrepotDTO(ArticleLotEntrepotDTO articleLotEntrepotDTO) {
		this.articleLotEntrepotDTO = articleLotEntrepotDTO;
	}



	public TaTPaiementDTO getTaTPaiementDTO() {
		return taTPaiementDTO;
	}



	public void setTaTPaiementDTO(TaTPaiementDTO taTPaiementDTO) {
		this.taTPaiementDTO = taTPaiementDTO;
	}
	
	
	
}
