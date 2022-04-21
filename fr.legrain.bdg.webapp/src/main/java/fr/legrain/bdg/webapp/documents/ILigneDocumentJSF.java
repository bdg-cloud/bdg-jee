package fr.legrain.bdg.webapp.documents;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.document.dto.ILigneDocumentDTO;

public interface ILigneDocumentJSF<DTO extends ILigneDocumentDTO> {
	
	public ILigneDocumentJSF copy(ILigneDocumentJSF ihm);
	
	void updateDTO(boolean modification);

	DTO getDto();

	void setDto(DTO ligne);
	
	//test rajout yann
//	DTO getDto2();
//	void setDto2(DTO ligne);

	TaArticle getTaArticle();

	void setTaArticle(TaArticle taArticle);

	TaEntrepot getTaEntrepot();

	void setTaEntrepot(TaEntrepot taEntrepot);

	TaLot getTaLot();

	void setTaLot(TaLot taLot);

	TaRapportUnite getTaRapport();

	void setTaRapport(TaRapportUnite taRapport);

	boolean ligneEstVide();

	TaArticleDTO getTaArticleDTO();

	void setTaArticleDTO(TaArticleDTO taArticleDTO);

	TaUnite getTaUnite1();

	void setTaUnite1(TaUnite taUnite1);

	TaUnite getTaUnite2();

	void setTaUnite2(TaUnite taUnite2);


	TaCoefficientUnite getTaCoefficientUniteSaisie();
	
	void setTaCoefficientUniteSaisie(TaCoefficientUnite taCoefficientUnite);

	TaCoefficientUnite getTaCoefficientUnite();

}