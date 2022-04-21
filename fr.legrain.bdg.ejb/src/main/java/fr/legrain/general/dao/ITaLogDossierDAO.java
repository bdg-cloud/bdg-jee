package fr.legrain.general.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.general.model.TaLogDossier;

public interface ITaLogDossierDAO extends IGenericDAO<TaLogDossier> {
	public TaLogDossier findByUUID(String uuid);
}
