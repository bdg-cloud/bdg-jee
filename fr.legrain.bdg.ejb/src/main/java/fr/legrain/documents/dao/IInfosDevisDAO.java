package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosDevis;

//@Remote
public interface IInfosDevisDAO extends IGenericDAO<TaInfosDevis> {
	public TaInfosDevis findByCodeDevis(String code);
}
