package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosPrelevement;

//@Remote
public interface IInfosPrelevementDAO extends IGenericDAO<TaInfosPrelevement> {
	public TaInfosPrelevement findByCodePrelevement(String code);
}
