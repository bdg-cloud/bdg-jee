package fr.legrain.tache.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tache.model.TaAgenda;
//import javax.ejb.Remote;

//@Remote
public interface IAgendaDAO extends IGenericDAO<TaAgenda> {
	public List<TaAgenda> findAgendaUtilisateur(TaUtilisateur utilisateur);
}
