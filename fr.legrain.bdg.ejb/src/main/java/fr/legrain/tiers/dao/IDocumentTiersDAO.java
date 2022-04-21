package fr.legrain.tiers.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaDocumentTiers;
//import javax.ejb.Remote;

//@Remote
public interface IDocumentTiersDAO extends IGenericDAO<TaDocumentTiers> {
	public void RAZCheminVersion(String typeLogiciel);
}
