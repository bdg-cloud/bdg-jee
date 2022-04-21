package fr.legrain.article.dao;

import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.data.IGenericDAO;

public interface ITypeMouvementDAO extends IGenericDAO<TaTypeMouvement> {
	public Boolean typeMouvementReserve(String code);

}
