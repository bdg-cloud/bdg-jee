package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosPanier;

//@Remote
public interface IInfosPanierDAO extends IGenericDAO<TaInfosPanier> {
	public TaInfosPanier findByCodePanier(String code);
}
