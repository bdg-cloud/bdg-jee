package fr.legrain.bdg.webapp.oauth.google;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.util.store.AbstractDataStore;
import com.google.api.client.util.store.DataStore;

import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;

public class JPADataStore extends AbstractDataStore<StoredCredential> {
	
    private ITaOAuthTokenClientServiceRemote repository;
    private JPADataStoreFactory jpaDataStoreFactory;

    /**
     * @param dataStoreFactory data store factory
     * @param id               data store ID
     */
    protected JPADataStore(JPADataStoreFactory dataStoreFactory, String id, ITaOAuthTokenClientServiceRemote repository) {
        super(dataStoreFactory, id);
        this.repository = repository;
    }

    @Override
    public JPADataStoreFactory getDataStoreFactory() {
        return jpaDataStoreFactory;
    }

    @Override
    public int size() throws IOException {
        return (int) repository.selectAll(repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE)).size();
    }

    @Override
    public boolean isEmpty() throws IOException {
        return size() == 0;
    }

    @Override
    public boolean containsKey(String key) throws IOException {
        return repository.findByKey(key,repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE))!=null?true:false;
    }

    @Override
    public boolean containsValue(StoredCredential value) throws IOException {
        return repository.findByAccessToken(value.getAccessToken(),repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE))!=null?true:false;
    }

    @Override
    public Set<String> keySet() throws IOException {
        return repository.findAllKeys(repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
    }

    @Override
    public Collection<StoredCredential> values() throws IOException {
        return repository.selectAll(repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE)).stream().map(c -> {
            StoredCredential credential = new StoredCredential();
            credential.setAccessToken(c.getAccessToken());
            credential.setRefreshToken(c.getRefreshToken());
            credential.setExpirationTimeMilliseconds(c.getExpiresIn());
            return credential;
        }).collect(Collectors.toList());
    }

    @Override
    public StoredCredential get(String key) throws IOException {
    	TaOAuthTokenClient jpaStoredCredentialOptional = repository.findByKey(key,repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
        if (jpaStoredCredentialOptional==null) {
            return null;
        }
        TaOAuthTokenClient googleCredential = jpaStoredCredentialOptional/*.get()*/;
        StoredCredential credential = new StoredCredential();
        credential.setAccessToken(googleCredential.getAccessToken());
        credential.setRefreshToken(googleCredential.getRefreshToken());
        //credential.setExpirationTimeMilliseconds(googleCredential.getExpirationTimeMilliseconds());
        credential.setExpirationTimeMilliseconds(googleCredential.getExpiresIn());
        return credential;
    }

    @Override
    public DataStore<StoredCredential> set(String key, StoredCredential value) throws IOException {
    	TaOAuthTokenClient googleCredential = repository.findByKey(key,repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
    	if(googleCredential==null) {
    		googleCredential = new TaOAuthTokenClient();
    		googleCredential.setKey(key);
    		
    		googleCredential.setAccessToken(value.getAccessToken());
    		googleCredential.setExpiresIn(value.getExpirationTimeMilliseconds());
    		googleCredential.setRefreshToken(value.getRefreshToken());
    		Date d = new Date();
    		googleCredential.setDateCreation(d);
    		googleCredential.setDateMiseAJour(d);
    		googleCredential.setTaOAuthServiceClient(repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
    	}
//        googleCredential.apply(value);
   		googleCredential.setAccessToken(value.getAccessToken());
		googleCredential.setExpiresIn(value.getExpirationTimeMilliseconds());
		googleCredential.setRefreshToken(value.getRefreshToken());
		Date d = new Date();
//		googleCredential.setDateCreation(d);
		googleCredential.setDateMiseAJour(d);
		googleCredential.setTaOAuthServiceClient(repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
		
        repository.merge(googleCredential);
        return this;
    }

    @Override
    public DataStore<StoredCredential> clear() throws IOException {
        repository.removeAll(repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
        return this;
    }

    @Override
    public DataStore<StoredCredential> delete(String key) throws IOException {
//        try {
			repository.removeKey(key,repository.findByCodeService(TaOAuthServiceClient.SERVICE_GOOGLE));
//		} catch (RemoveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        return this;
    }
}