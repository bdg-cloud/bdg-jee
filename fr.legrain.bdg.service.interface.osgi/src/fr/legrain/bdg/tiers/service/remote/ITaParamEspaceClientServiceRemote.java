package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.model.TaParamEspaceClient;

@Remote
public interface ITaParamEspaceClientServiceRemote extends IGenericCRUDServiceRemote<TaParamEspaceClient,TaParamEspaceClientDTO>,
														IAbstractLgrDAOServer<TaParamEspaceClient>,IAbstractLgrDAOServerDTO<TaParamEspaceClientDTO>{
	public static final String validationContext = "PARAM_ESPACE_CLIENT";
	
	public TaParamEspaceClient findInstance();
	public TaParamEspaceClientDTO findInstanceDTO();
	
	

}
