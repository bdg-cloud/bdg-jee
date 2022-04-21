package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosAvoir;

//@Remote
public interface IInfosAvoirDAO extends IGenericDAO<TaInfosAvoir> {
	public TaInfosAvoir findByCodeAvoir(String code);
}
