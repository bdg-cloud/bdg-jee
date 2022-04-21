package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosBonliv;

//@Remote
public interface IInfosBonlivDAO extends IGenericDAO<TaInfosBonliv> {
	public TaInfosBonliv findByCodeBonliv(String code);
}
