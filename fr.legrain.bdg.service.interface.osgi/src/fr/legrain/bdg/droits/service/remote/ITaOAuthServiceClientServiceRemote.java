package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaOAuthServiceClientDTO;
import fr.legrain.droits.model.TaOAuthServiceClient;


@Remote
public interface ITaOAuthServiceClientServiceRemote extends IGenericCRUDServiceRemote<TaOAuthServiceClient,TaOAuthServiceClientDTO>,
														IAbstractLgrDAOServer<TaOAuthServiceClient>,IAbstractLgrDAOServerDTO<TaOAuthServiceClientDTO>{
	public static final String validationContext = "OAUTH_SERVICE_CLIENT";
	
//	public List<TaOAuthServiceClientDTO> findByCodeLight(String code);
//	public List<TaOAuthServiceClientDTO> findByNomLight(String nom);
	
}
