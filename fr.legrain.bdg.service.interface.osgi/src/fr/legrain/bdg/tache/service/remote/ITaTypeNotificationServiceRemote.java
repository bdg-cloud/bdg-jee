package fr.legrain.bdg.tache.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tache.dto.TaTypeNotificationDTO;
import fr.legrain.tache.model.TaTypeNotification;

@Remote
public interface ITaTypeNotificationServiceRemote extends IGenericCRUDServiceRemote<TaTypeNotification,TaTypeNotificationDTO>,
														IAbstractLgrDAOServer<TaTypeNotification>,IAbstractLgrDAOServerDTO<TaTypeNotificationDTO>{
	public static final String validationContext = "TYPE_NOTIFICATION";
}
