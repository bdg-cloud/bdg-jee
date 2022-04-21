package fr.legrain.bdg.webapp.oauth.google;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.oauth2.Oauth2Scopes;

import fr.legrain.bdg.droits.service.remote.ITaOAuthTokenClientServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.model.TaOAuthScopeClient;

@Named
public class GoogleOAuthUtil implements Serializable {
	
	private static final long serialVersionUID = -4677561553647719846L;
	
	/*
	 * https://stackoverflow.com/questions/22763934/google-oauth-fails-fetching-redirect-url
	 */
	private String googleAuthUrl = null;
	private LocalServerReceiver receiver = null;
	private GoogleAuthorizationCodeFlow flow = null;
	private String redirectUri = null;
	/** Global instance of the scopes required by this quickstart.
	 * If modifying these scopes, delete your previously saved credentials
	 * at ~/.credentials/calendar-java-quickstart
	 */
	private List<String> SCOPES = null;
	private BdgProperties bdgProperties = new BdgProperties();
	
	private JsonFactory JSON_FACTORY = null;
	private String APPLICATION_NAME = null;
	
	//private String REDIRECT_URI = "https://dev.oauth.promethee.biz:8443/callback-google";
	private String REDIRECT_URI = "https://oauth.bdg.cloud/callback-google";
	//private	FileDataStoreFactory DATA_STORE_FACTORY = null; /** Global instance of the {@link FileDataStoreFactory}. */
	private	JPADataStoreFactory DATA_STORE_FACTORY = null; /** Global instance of the {@link FileDataStoreFactory}. */
	private HttpTransport HTTP_TRANSPORT;/** Global instance of the HTTP transport. */
	private GoogleClientSecrets clientSecrets = null;
	
	private Credential credents = null;
	
	public GoogleOAuthUtil() {
		REDIRECT_URI = "https://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE)+"oauth."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+"/callback-google";
	}
	
	public void initOAuth(String user, ITaOAuthTokenClientServiceRemote taOAuthTokenClientService, List<TaOAuthScopeClient> listeScopeGoogleSelected, boolean modeTest) {
		try {
			JSON_FACTORY = JacksonFactory.getDefaultInstance();
			APPLICATION_NAME = "BDG Cloud";
	
			File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/calendar-java-quickstart"); /** Directory to store user credentials for this application. */
	
			if(listeScopeGoogleSelected!=null) {
				SCOPES = new ArrayList<>();
				for (TaOAuthScopeClient taOAuthScopeClient : listeScopeGoogleSelected) {
					if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.GOOGLE_PROFIL)) {
						SCOPES.add(Oauth2Scopes.USERINFO_PROFILE);
						SCOPES.add(Oauth2Scopes.USERINFO_EMAIL);
					} else if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.GOOGLE_CALENDAR)) {
						SCOPES.add(CalendarScopes.CALENDAR_READONLY);
//						SCOPES.add(CalendarScopes.CALENDAR);
					} else if(taOAuthScopeClient.getCode().equals(TaOAuthScopeClient.GOOGLE_DRIVE)) {
//						SCOPES.add(DriveScopes.DRIVE_FILE);
						SCOPES.add(DriveScopes.DRIVE_METADATA_READONLY);
					}
				}
			} else {
				//pas de liste de scope spécifiques, on demande "tout"
				SCOPES = Arrays.asList(
					CalendarScopes.CALENDAR_READONLY,
					//CalendarScopes.CALENDAR,
					
					Oauth2Scopes.USERINFO_PROFILE,
					Oauth2Scopes.USERINFO_EMAIL,
					
//					DriveScopes.DRIVE_FILE,
					DriveScopes.DRIVE_METADATA_READONLY
					);
			}
			
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
			DATA_STORE_FACTORY = new JPADataStoreFactory(taOAuthTokenClientService);
	
			BdgProperties bdgProperties = new BdgProperties();
			InputStream in = bdgProperties.getConfigFileInputStream("client_secret.json");
			
//			InputStream in = GoogleBean.class.getResourceAsStream("client_secret.json"); 

	        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	        
//	        String clientId = "xxxxxxxxx-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com";
//	        String clientSecret = "xxxxxxxxxxxxxxxxxxxxxxxxxx";
	        
	        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//	        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientId, clientSecret, SCOPES)
	        		 .setDataStoreFactory(DATA_STORE_FACTORY)
	        		 .setApprovalPrompt("force") //pour le dev, force l'affichage de la page demandant a l'utilisateur de valider les différents scopes  //https://stackoverflow.com/questions/13777842/how-to-get-offline-token-and-refresh-token-and-auto-refresh-access-to-google-api
	        		 .setAccessType("offline")
	        		 .build();
	    
//	        String usermail = "aaa";
//	        credents = flow.loadCredential(usermail);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public String getGoogleAuthUrl() {
		return googleAuthUrl;
	}

	public void setGoogleAuthUrl(String googleAuthUrl) {
		this.googleAuthUrl = googleAuthUrl;
	}

	public LocalServerReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(LocalServerReceiver receiver) {
		this.receiver = receiver;
	}

	public GoogleAuthorizationCodeFlow getFlow() {
		return flow;
	}

	public void setFlow(GoogleAuthorizationCodeFlow flow) {
		this.flow = flow;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public List<String> getSCOPES() {
		return SCOPES;
	}

	public void setSCOPES(List<String> sCOPES) {
		SCOPES = sCOPES;
	}

	public JsonFactory getJSON_FACTORY() {
		return JSON_FACTORY;
	}

	public void setJSON_FACTORY(JsonFactory jSON_FACTORY) {
		JSON_FACTORY = jSON_FACTORY;
	}

	public String getAPPLICATION_NAME() {
		return APPLICATION_NAME;
	}

	public void setAPPLICATION_NAME(String aPPLICATION_NAME) {
		APPLICATION_NAME = aPPLICATION_NAME;
	}

	public JPADataStoreFactory getDATA_STORE_FACTORY() {
		return DATA_STORE_FACTORY;
	}

	public void setDATA_STORE_FACTORY(JPADataStoreFactory dATA_STORE_FACTORY) {
		DATA_STORE_FACTORY = dATA_STORE_FACTORY;
	}

	public HttpTransport getHTTP_TRANSPORT() {
		return HTTP_TRANSPORT;
	}

	public void setHTTP_TRANSPORT(HttpTransport hTTP_TRANSPORT) {
		HTTP_TRANSPORT = hTTP_TRANSPORT;
	}

	public GoogleClientSecrets getClientSecrets() {
		return clientSecrets;
	}

	public void setClientSecrets(GoogleClientSecrets clientSecrets) {
		this.clientSecrets = clientSecrets;
	}

	public Credential getCredents() {
		return credents;
	}

	public void setCredents(Credential credents) {
		this.credents = credents;
	}

	public String getREDIRECT_URI() {
		return REDIRECT_URI;
	}

	public void setREDIRECT_URI(String rEDIRECT_URI) {
		REDIRECT_URI = rEDIRECT_URI;
	}

}
