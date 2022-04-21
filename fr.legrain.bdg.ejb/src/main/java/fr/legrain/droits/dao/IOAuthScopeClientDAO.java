package fr.legrain.droits.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;

//@Remote
public interface IOAuthScopeClientDAO extends IGenericDAO<TaOAuthScopeClient> {
	
	public List<TaOAuthScopeClient> selectAll(TaOAuthServiceClient taOAuthServiceClient);
}
