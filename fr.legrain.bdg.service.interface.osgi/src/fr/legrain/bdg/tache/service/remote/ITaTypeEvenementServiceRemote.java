package fr.legrain.bdg.tache.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tache.dto.TaTypeEvenementDTO;
import fr.legrain.tache.model.TaTypeEvenement;

@Remote
public interface ITaTypeEvenementServiceRemote extends IGenericCRUDServiceRemote<TaTypeEvenement,TaTypeEvenementDTO>,
														IAbstractLgrDAOServer<TaTypeEvenement>,IAbstractLgrDAOServerDTO<TaTypeEvenementDTO>{
	public static final String validationContext = "TYPE_EVENEMENT";
}
