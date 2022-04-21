package fr.legrain.article.dao;

import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IRTaTitreTransportDAO extends IGenericDAO<TaRTaTitreTransport> {
	public TaRTaTitreTransport findByIdArticle(int idArticle);
}
