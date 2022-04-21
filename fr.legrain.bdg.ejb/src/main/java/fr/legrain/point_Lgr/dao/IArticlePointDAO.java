package fr.legrain.point_Lgr.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.pointLgr.model.TaArticlePoint;

//@Remote
public interface IArticlePointDAO extends IGenericDAO<TaArticlePoint> {
	public TaArticlePoint findByArticle(Integer idArticle);
}
