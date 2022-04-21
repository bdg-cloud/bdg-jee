package fr.legrain.bdg.droits.service.remote;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaOAuthTokenClientDTO;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;


@Remote
public interface ITaOAuthTokenClientServiceRemote extends IGenericCRUDServiceRemote<TaOAuthTokenClient,TaOAuthTokenClientDTO>,
														IAbstractLgrDAOServer<TaOAuthTokenClient>,IAbstractLgrDAOServerDTO<TaOAuthTokenClientDTO>{
	public static final String validationContext = "OAUTH_TOKEN_CLIENT";
	
	public TaOAuthTokenClient findByKey(String key);
	public TaOAuthTokenClient findByKey(String key, TaOAuthServiceClient taOAuthServiceClient);
	public TaOAuthTokenClient findByAccessToken(String key);
	public TaOAuthTokenClient findByAccessToken(String key, TaOAuthServiceClient taOAuthServiceClient);
	public void removeAll();
	public void removeAll(TaOAuthServiceClient taOAuthServiceClient);
	public void removeKey(String key);
	public void removeKey(String key, TaOAuthServiceClient taOAuthServiceClient);
	public Set<String> findAllKeys();
	public Set<String> findAllKeys(TaOAuthServiceClient taOAuthServiceClient);
	
	public List<TaOAuthTokenClient> selectAll(TaOAuthServiceClient taOAuthServiceClient);
	
	public TaOAuthServiceClient findByCodeService(String code);
	
}
