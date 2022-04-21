package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaLimitationDTO;
import fr.legrain.moncompte.model.TaLimitation;


@Remote
public interface ITaLimitationServiceRemote extends IGenericCRUDServiceRemote<TaLimitation,TaLimitationDTO>,
														IAbstractLgrDAOServer<TaLimitation>,IAbstractLgrDAOServerDTO<TaLimitationDTO>{
	public static final String validationContext = "LIMITATION";

}
