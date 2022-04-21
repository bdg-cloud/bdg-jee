package fr.legrain.bdg.compteclient.dao;

import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.data.IGenericDAO;

/**
 *  Interface IFournisseurDAO étendu à l'interface IGenericDAO
 */
public interface IFournisseurDAO extends IGenericDAO<TaFournisseur> {
	public TaFournisseur findInstance();
	public TaFournisseur findByCodeDossier(String codeDossier);

}
