package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosAvisEcheance;

//@Remote
public interface IInfosAvisEcheanceDAO extends IGenericDAO<TaInfosAvisEcheance> {
	public TaInfosAvisEcheance findByCodeAvisEcheance(String code);
}
