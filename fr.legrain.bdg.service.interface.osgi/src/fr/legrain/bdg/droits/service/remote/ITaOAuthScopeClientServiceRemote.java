package fr.legrain.bdg.droits.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaOAuthScopeClientDTO;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;


@Remote
public interface ITaOAuthScopeClientServiceRemote extends IGenericCRUDServiceRemote<TaOAuthScopeClient,TaOAuthScopeClientDTO>,
														IAbstractLgrDAOServer<TaOAuthScopeClient>,IAbstractLgrDAOServerDTO<TaOAuthScopeClientDTO>{
	public static final String validationContext = "OAUTH_SCOPE_CLIENT";
	
	public List<TaOAuthScopeClient> selectAll(TaOAuthServiceClient taOAuthServiceClient);
	
}
