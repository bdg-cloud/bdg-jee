package fr.legrain.bdg.tache.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tache.dto.TaNotificationDTO;
import fr.legrain.tache.model.TaNotification;

@Remote
public interface ITaNotificationServiceRemote extends IGenericCRUDServiceRemote<TaNotification,TaNotificationDTO>,
														IAbstractLgrDAOServer<TaNotification>,IAbstractLgrDAOServerDTO<TaNotificationDTO>{
	public static final String validationContext = "NOTIFICATION";
	
	public List<TaNotification> findNotificationInWebAppEnvoyee(Integer idCalendrier, Integer utilisateur, Boolean lu);
}
