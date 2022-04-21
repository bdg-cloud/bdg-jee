package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ICatalogueWebDAO extends IGenericDAO<TaCatalogueWeb> {
	
	public List<TaArticleDTO> findListeArticleCatalogue();
	public List<TaArticleDTO> findListeArticleCatalogue(int idCategorie);
	public TaArticleDTO findArticleCatalogue(int idArticle);
	
	public List<TaCategorieArticleDTO> findListeCategorieArticleCatalogue();
	public TaCategorieArticleDTO findCategorieArticleCatalogue(int idCategorie);

}
