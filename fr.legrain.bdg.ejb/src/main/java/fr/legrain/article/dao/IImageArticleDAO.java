package fr.legrain.article.dao;

import java.util.List;

import javax.persistence.Query;

import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IImageArticleDAO extends IGenericDAO<TaImageArticle> {
	public List<TaImageArticle> findByArticle(int id);
	public List<TaImageArticleDTO> findByArticleLight(int id);
	
	public void changeImageDefaut(TaImageArticle image);
}
