package fr.legrain.bdg.rest.model;

import java.util.List;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.ArticleLotEntrepotDTO;

public class RechercheParCodeBarreResult {
	
	private TaArticleDTO taArticleDTO;
	private ArticleLotEntrepotDTO taLotSelectionneDTO;
	private List<ArticleLotEntrepotDTO> listeLotDisponible;
	
	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}
	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}
	public ArticleLotEntrepotDTO getTaLotSelectionneDTO() {
		return taLotSelectionneDTO;
	}
	public void setTaLotSelectionneDTO(ArticleLotEntrepotDTO taLotSelectionneDTO) {
		this.taLotSelectionneDTO = taLotSelectionneDTO;
	}
	public List<ArticleLotEntrepotDTO> getListeLotDisponible() {
		return listeLotDisponible;
	}
	public void setListeLotDisponible(List<ArticleLotEntrepotDTO> listeLotDisponible) {
		this.listeLotDisponible = listeLotDisponible;
	}
}
