package fr.legrain.autorisations.autorisations.dao;

import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.autorisations.data.IGenericDAO;


//@Remote
public interface IAutorisationsDAO extends IGenericDAO<TaAutorisations> {
	public TaAutorisations findByDossierTypeProduit(String codeDossier, Integer idTypeProduit);

}
