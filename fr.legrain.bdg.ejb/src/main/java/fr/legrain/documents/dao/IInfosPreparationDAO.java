package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosPreparation;

//@Remote
public interface IInfosPreparationDAO extends IGenericDAO<TaInfosPreparation> {
	public TaInfosPreparation findByCodePreparation(String code);
}
