package fr.legrain.general.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.general.service.local.IDatabaseServiceLocal;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@Interceptors(ServerTenantInterceptor.class)
public class DevService {

	static Logger logger = Logger.getLogger(DevService.class);

	@Resource 
	private SessionContext context;

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private BdgProperties p = new BdgProperties();


	@EJB IDatabaseServiceLocal databaseService;

	public DevService() {}
	
//	private static Credential authorize() throws Exception {
//		  OAuth2ClientCredentials.errorIfNotSpecified();
//		  // set up authorization code flow
//		  AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken
//		      .authorizationHeaderAccessMethod(),
//		      HTTP_TRANSPORT,
//		      JSON_FACTORY,
//		      new GenericUrl(TOKEN_SERVER_URL),
//		      new ClientParametersAuthentication(
//		          OAuth2ClientCredentials.API_KEY, OAuth2ClientCredentials.API_SECRET),
//		      OAuth2ClientCredentials.API_KEY,
//		      AUTHORIZATION_SERVER_URL).setScopes(Arrays.asList(SCOPE))
//		      .setDataStoreFactory(DATA_STORE_FACTORY).build();
//		  // authorize
//		  LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost(
//		      OAuth2ClientCredentials.DOMAIN).setPort(OAuth2ClientCredentials.PORT).build();
//		  return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//		}

	
	public void connectOAuth() {
//		Googlecl
	}

	public String test() {
		System.out.println("user logged : "+context.getCallerPrincipal().getName());
		return "user logged : "+context.getCallerPrincipal().getName();
		//		return "aaa";
	}

	public void dbDump(String tagDump) throws IOException {
		databaseService.backupDB(tagDump);
	}
	
	public void dbDump(String tagDump, String dbName) throws IOException {
		databaseService.backupDB(tagDump,null,dbName);
	}
	
	public void schemaDump(String tagDump, String schema) throws IOException {
		databaseService.backupDB(tagDump,schema);
	}
	
	public void restaureDB(String fichierDump, String schema) throws IOException {
		databaseService.restaureDB(fichierDump,schema);
	}
	
	public void restaureDB(String fichierDump) throws IOException {
		databaseService.restaureDB(fichierDump);
	}

	public List<String> listeBdd() {
		return databaseService.listeBdd();
	}

	public List<Object[]> listeSchemaTailleConnection() {
		return databaseService.listeSchemaTailleConnection();
	}

	public void ajoutDossier(String nomDossier) {
		databaseService.ajoutDossier(nomDossier);
	}

	public void supprimerDossier(String nomDossier) {
		databaseService.supprimerDossier(nomDossier);
	}
	
	public void rebootServeur() {
		databaseService.rebootServeur();
	}
	
	public List<String> listeFichierDump(String nomDossier, String nomBase) {
		return databaseService.listeFichierDump(nomDossier,nomBase);
	}
	
	

}
