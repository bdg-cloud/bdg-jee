package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosFacture;

//@Remote
public interface IInfosFactureDAO extends IGenericDAO<TaInfosFacture> {
	public TaInfosFacture findByCodeFacture(String code);
}
