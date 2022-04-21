package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosAcompte;

//@Remote
public interface IInfosAcompteDAO extends IGenericDAO<TaInfosAcompte> {
	public TaInfosAcompte findByCodeAcompte(String code);
}
