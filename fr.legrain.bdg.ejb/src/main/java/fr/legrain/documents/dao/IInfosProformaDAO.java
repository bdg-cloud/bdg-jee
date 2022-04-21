package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosProforma;

//@Remote
public interface IInfosProformaDAO extends IGenericDAO<TaInfosProforma> {
	public TaInfosProforma findByCodeProforma(String code);
}
