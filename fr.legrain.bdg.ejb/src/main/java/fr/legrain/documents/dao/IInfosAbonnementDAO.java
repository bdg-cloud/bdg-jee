package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaInfosDevis;

//@Remote
public interface IInfosAbonnementDAO extends IGenericDAO<TaInfosAbonnement> {
	public TaInfosAbonnement findByCodeDevis(String code);
}
