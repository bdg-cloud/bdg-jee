package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosBoncde;

//@Remote
public interface IInfosBoncdeDAO extends IGenericDAO<TaInfosBoncde> {
	public TaInfosBoncde findByCodeBoncde(String code);
}
