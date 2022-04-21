package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosBonReception;

//@Remote
public interface IInfosBonReceptionDAO extends IGenericDAO<TaInfosBonReception> {
	public TaInfosBonReception findByCodeBonReception(String code);
}
