package fr.legrain.droits.dao;

import java.util.List;
import java.util.Set;

import fr.legrain.data.IGenericDAO;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;

//@Remote
public interface IOAuthTokenClientDAO extends IGenericDAO<TaOAuthTokenClient> {
//	public List<TaUtilisateurDTO> findByCodeLight(String code);
//	public List<TaUtilisateurDTO> findByNomLight(String nom);
	public TaOAuthTokenClient findByKey(String key);
	public TaOAuthTokenClient findByKey(String key, TaOAuthServiceClient taOAuthServiceClient);
	public TaOAuthTokenClient findByAccessToken(String key);
	public TaOAuthTokenClient findByAccessToken(String key, TaOAuthServiceClient taOAuthServiceClient);
	public void removeAll();
	public void removeAll(TaOAuthServiceClient taOAuthServiceClient);
	public void removeKey(String key);
	public void removeKey(String key, TaOAuthServiceClient taOAuthServiceClient);
    //@Query(value = "select key from google_jpa_data_store_credential", nativeQuery = true)
	public Set<String> findAllKeys();
	public Set<String> findAllKeys(TaOAuthServiceClient taOAuthServiceClient);
	
	public List<TaOAuthTokenClient> selectAll(TaOAuthServiceClient taOAuthServiceClient);
}
