package fr.legrain.tache.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
//import javax.ejb.Remote;
import fr.legrain.tache.model.TaRDocumentEvenement;

//@Remote
public interface IEvenementDAO extends IGenericDAO<TaEvenement> {
	public List<TaEvenement> findByDate(Date debut, Date fin, List<TaAgenda> listeAgenda);
	public List<TaRDocumentEvenement> findListeDocuments(TaEvenement event);
}
