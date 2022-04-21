package fr.legrain.conformite.dao;

import fr.legrain.conformite.model.TaBareme;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IBaremeDAO extends IGenericDAO<TaBareme> {
	public void removeOID(TaBareme b);
}
