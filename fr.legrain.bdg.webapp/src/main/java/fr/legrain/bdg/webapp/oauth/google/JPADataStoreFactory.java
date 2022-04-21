package fr.legrain.bdg.webapp.oauth.google;

import java.io.IOException;

import javax.inject.Named;

import com.google.api.client.util.store.AbstractDataStoreFactory;

import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;

@Named
public class JPADataStoreFactory extends AbstractDataStoreFactory{
	private ITaOAuthTokenClientServiceRemote repository;

	public JPADataStoreFactory(ITaOAuthTokenClientServiceRemote repository) {
	    this.repository = repository;
	}

	@Override
	protected JPADataStore createDataStore(String id) throws IOException {
	    return new JPADataStore(this, id, repository);
	}
}
