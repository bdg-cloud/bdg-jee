package fr.legrain.autorisations.dossier.dao;

import fr.legrain.autorisations.data.IGenericDAO;
import fr.legrain.autorisations.dossier.model.TaInfoEntreprise;

//@Remote
public interface IInfoEntrepriseDAO extends IGenericDAO<TaInfoEntreprise> {
	public TaInfoEntreprise findInstance();
}
