package fr.legrain.documents.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaInfosTicketDeCaisse;

//@Remote
public interface IInfosTicketDeCaisseDAO extends IGenericDAO<TaInfosTicketDeCaisse> {
	public TaInfosTicketDeCaisse findByCodeFacture(String code);
}
