package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTWebDTO;
import fr.legrain.tiers.model.TaTWeb;

@Remote
public interface ITaTWebServiceRemote extends IGenericCRUDServiceRemote<TaTWeb,TaTWebDTO>,
														IAbstractLgrDAOServer<TaTWeb>,IAbstractLgrDAOServerDTO<TaTWebDTO>{
	public static final String validationContext = "T_WEB";	
}
