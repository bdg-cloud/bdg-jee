package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.document.model.TaInfosBoncdeAchat;

//@Remote
public interface IInfosBoncdeAchatDAO extends IGenericDAO<TaInfosBoncdeAchat> {
	public TaInfosBoncdeAchat findByCodeBoncde(String code);
}
