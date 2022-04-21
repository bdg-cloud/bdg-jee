package fr.legrain.dossier.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.dossier.model.TaInfoEntreprise;

//@Remote
public interface IInfoEntrepriseDAO extends IGenericDAO<TaInfoEntreprise> {
	public TaInfoEntreprise findInstance();
}
