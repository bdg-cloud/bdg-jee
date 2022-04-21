package fr.legrain.dossier.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.dossier.model.TaVersion;

//@Remote
public interface IVersionDAO extends IGenericDAO<TaVersion> {
	public TaVersion findInstance();
}
