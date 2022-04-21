package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaTRelance;

//@Remote
public interface ITRelanceDAO extends IGenericDAO<TaTRelance> {
	public TaTRelance taTRelanceSuperieur(int ordre);
}
