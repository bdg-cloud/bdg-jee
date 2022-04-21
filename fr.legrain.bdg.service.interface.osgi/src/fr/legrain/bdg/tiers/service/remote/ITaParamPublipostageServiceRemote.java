package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaParamPublipostageDTO;
import fr.legrain.document.model.TaParamPublipostage;

@Remote
public interface ITaParamPublipostageServiceRemote extends IGenericCRUDServiceRemote<TaParamPublipostage,TaParamPublipostageDTO>,
														IAbstractLgrDAOServer<TaParamPublipostage>,IAbstractLgrDAOServerDTO<TaParamPublipostageDTO>{
	public TaParamPublipostage findInstance();
}
