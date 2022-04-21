package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaWebDTO;
import fr.legrain.tiers.model.TaWeb;

@Remote
public interface ITaWebServiceRemote extends IGenericCRUDServiceRemote<TaWeb,TaWebDTO>,
														IAbstractLgrDAOServer<TaWeb>,IAbstractLgrDAOServerDTO<TaWebDTO>{
	public static final String validationContext = "WEB";
}
