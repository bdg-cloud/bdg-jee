package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosApporteur;
import fr.legrain.document.model.TaInfosFacture;

//@Remote
public interface IInfosApporteurDAO extends IGenericDAO<TaInfosApporteur> {
	public TaInfosApporteur findByCodeApporteur(String code);
}
