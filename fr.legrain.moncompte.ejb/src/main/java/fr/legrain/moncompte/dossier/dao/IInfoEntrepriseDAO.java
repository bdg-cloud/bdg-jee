package fr.legrain.moncompte.dossier.dao;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.dossier.model.TaInfoEntreprise;

//@Remote
public interface IInfoEntrepriseDAO extends IGenericDAO<TaInfoEntreprise> {
	public TaInfoEntreprise findInstance();
}
