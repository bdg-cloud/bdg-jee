package fr.legrain.tache.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tache.model.TaNotification;
//import javax.ejb.Remote;

//@Remote
public interface INotificationDAO extends IGenericDAO<TaNotification> {
	public List<TaNotification> findNotificationInWebAppEnvoyee(Integer idCalendrier, Integer utilisateur, Boolean lu);
}
