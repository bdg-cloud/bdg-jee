package fr.legrain.dossier.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.dossier.model.TaAutorisationsDossier;


//@Remote
public interface IAutorisationsDossierDAO extends IGenericDAO<TaAutorisationsDossier> {
	public TaAutorisationsDossier findByTiersTypeProduit(String codeTiers);
	public TaAutorisationsDossier findInstance();
}
