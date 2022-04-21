package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IMarqueArticleDAO extends IGenericDAO<TaMarqueArticle> {
	public List<TaMarqueArticleDTO> findByCodeLight(String code);
}
