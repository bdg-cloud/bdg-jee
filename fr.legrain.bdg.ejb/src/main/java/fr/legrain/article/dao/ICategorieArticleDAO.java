package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ICategorieArticleDAO extends IGenericDAO<TaCategorieArticle> {
	
	public List<TaCategorieArticle> findByCodeCategorieParent(String codeCategorieParent);
	public List<TaCategorieArticle> findCategorieMere();
	
	public List<TaCategorieArticleDTO> findByCodeLight(String code);
	public List<TaCategorieArticleDTO> findAllLight();
	
	public List<TaCategorieArticleDTO> findCategorieDTOByIdArticle(int idArticle);
	public List<TaCategorieArticle> findCategorieByIdArticle(int idArticle);
}
