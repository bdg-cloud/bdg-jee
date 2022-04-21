package fr.legrain.bdg.ws.rest.client;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import fr.legrain.bdg.ws.rest.client.service.AbonnementApiClientService;
import fr.legrain.bdg.ws.rest.client.service.ArticleApiClientService;
import fr.legrain.bdg.ws.rest.client.service.BonLivApiClientService;
import fr.legrain.bdg.ws.rest.client.service.EspaceClientApiClientService;
import fr.legrain.bdg.ws.rest.client.service.FactureApiClientService;
import fr.legrain.bdg.ws.rest.client.service.PanierApiClientService;
import fr.legrain.bdg.ws.rest.client.service.TiersApiClientService;
import fr.legrain.bdg.ws.rest.client.service.PaiementApiClientService;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class BdgRestClient {

	private Client client = null;
	private Config config = null;

	public static String baseURL = null;
	public static final String baseUrlDefault = "https://api.bdg.cloud";
	
	public static String version = null;
	public static final String versionDefault = "v1";

	public static String dossier = null; 
	
	public static String token = null; 
	public static String refreshToken = null; 
	public static String accessToken = null; 
	
	public static String login = null; 
	public static String password = null; 
	
	private static BdgRestClient bdgRestClient = null;
	
	public static BdgRestClient build(Config c) {
		BdgRestClient.login = null; 
		BdgRestClient.password = null;
		BdgRestClient.accessToken = null;
		
//		if(bdgRestClient==null) {
			bdgRestClient = new BdgRestClient(c);
//		}
		return bdgRestClient;
	}
	
	public static BdgRestClient findInstance() {
		return bdgRestClient;
	}

	private BdgRestClient(Config c) {
		this.config = c;
		try {
			HostnameVerifier hostnameVerifier = null;
			SSLContext sslContext = null;
			
			if(!c.isVerificationSsl()) {
				TrustManager[] selfTrustManager = new TrustManager[] {
					new X509TrustManager() {
	
						public X509Certificate[] getAcceptedIssuers() {
							return null;
						}
	
						public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	
						public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
					}
				};
	
				hostnameVerifier = new HostnameVerifier() {
					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				};
	
				sslContext = SSLContext.getInstance("ssl");
				sslContext.init(null, selfTrustManager, null );
			}

			////////////////
			ClientConfig clientConfig = new ClientConfig()
			.register(RequestClientFilter.class)
//		    .register(RequestClientWriterInterceptor.class);

			.register(JacksonFeature.class)
//			.register(MyObjectMapperProvider.class) // No need to register this provider if no special configuration is required.
//			.register(JacksonJsonProvider.class)
			;
			
		    /////////////////////////

			ClientBuilder builder = ClientBuilder.newBuilder().withConfig(clientConfig);
			if(!c.isVerificationSsl()) {
				builder = builder.hostnameVerifier(hostnameVerifier).sslContext(sslContext);
			}
			client = builder.build();
			//client = ClientBuilder.newClient();
			
			baseURL = baseUrlDefault;
			if(config.getBaseUrl()!=null) {
				baseURL = config.getBaseUrl();
			}
			version = versionDefault;
			if(config.getVersion()!=null) {
				version = config.getVersion();
			}
			baseURL = baseURL+"/"+version;
			
			if(config.isDevLegrain()) {
				baseURL = "https://dev.api.promethee.biz:8443/v1";
			}
			
			initBaseUrl(config.getDossier());
		} catch (Exception e) {
			;
		}
	}
	
	public Client getClient() {
		return client;
	}
	
	public void initBaseUrl(String dossier) {
		BdgRestClient.dossier = dossier;
		if(config.isDevLegrain()) {
//			baseURL = "https://dev."+dossier+".promethee.biz:8443/v1";
		}
	}
	
	public String connexionEspaceClient() {
		return connexionEspaceClient(config.getDossier(),config.getLogin(),config.getPassword());
	}

	private String connexionEspaceClient(String dossier, String login, String password) {
		//Compte WS valide sur le dossier (table utilisateur ws)
		/*
		 * Les appels se font au final avec un compte ws dossier, 
		 * le login/password de l'espace client sert uniquement a verifier l'identité de l'utilisateur qui se connecte,
		 * ce n'est pas lui qui fait les apples aux méthodes de bdg
		 */
		
		BdgRestClient.login = login; 
		BdgRestClient.password = password; 
		
		initBaseUrl(dossier);
		WebTarget target = client.target(baseURL).path("auth").path("authenticate-espace-client");

		Form form = new Form();
		form.param("login", login);
		form.param("password", password);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		String r = requestResult.readEntity(String.class);
		Gson gson = new Gson();
		JsonElement json = gson.fromJson(r, JsonElement.class);
		if(!json.getAsJsonObject().get("token").isJsonNull())
			token = json.getAsJsonObject().get("token").getAsString();
		
		if(!json.getAsJsonObject().get("accessToken").isJsonNull())
			refreshToken = json.getAsJsonObject().get("refreshToken").getAsString();
		
		if(!json.getAsJsonObject().get("accessToken").isJsonNull())
			accessToken = json.getAsJsonObject().get("accessToken").getAsString();
		
		String codeTiers = null;
		if(!json.getAsJsonObject().get("codeTiers").isJsonNull()) {
			codeTiers = json.getAsJsonObject().get("codeTiers").getAsString();
		}
		return codeTiers;
	}
	
	public String connexion() {
		return connexion(config.getDossier(),config.getLogin(),config.getPassword());
	}

	private String connexion(String dossier, String login, String password) {
		//Compte WS valide sur le dossier (table utilisateur ws)
		/*
		 * Les appels se font au final avec un compte ws dossier, 
		 * le login/password de l'espace client sert uniquement a verifier l'identité de l'utilisateur qui se connecte,
		 * ce n'est pas lui qui fait les apples aux méthodes de bdg
		 */
		
		initBaseUrl(dossier);
		WebTarget target = client.target(baseURL).path("auth").path("authenticate");

		Form form = new Form();
		form.param("login", login);
		form.param("password", password);
		
		Response requestResult =
				target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		String r = requestResult.readEntity(String.class);
		Gson gson = new Gson();
		JsonElement json = gson.fromJson(r, JsonElement.class);
//		token = json.getAsJsonObject().get("token").getAsString();
		if(!json.getAsJsonObject().get("refreshToken").isJsonNull())
			refreshToken = json.getAsJsonObject().get("refreshToken").getAsString();
		
		if(!json.getAsJsonObject().get("accessToken").isJsonNull())
			accessToken = json.getAsJsonObject().get("accessToken").getAsString();

		return accessToken;
	}
	
	public void refreshTokenEspaceClient() {
		initBaseUrl(dossier);
		WebTarget target = client.target(baseURL).path("auth").path("refresh-espace-client");
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.post(Entity.json("{token :"+refreshToken+"}"));
		String r = requestResult.readEntity(String.class);
		
		Gson gson = new Gson();
		JsonElement json = gson.fromJson(r, JsonElement.class);
		
		if(!json.getAsJsonObject().get("token").isJsonNull())
			token = json.getAsJsonObject().get("token").getAsString();
		
		if(!json.getAsJsonObject().get("refreshToken").isJsonNull())
			refreshToken = json.getAsJsonObject().get("refreshToken").getAsString();
		
		if(!json.getAsJsonObject().get("accessToken").isJsonNull())
			accessToken = json.getAsJsonObject().get("accessToken").getAsString();
	}
	
	public void refreshToken() {
		initBaseUrl(dossier);
		WebTarget target = client.target(baseURL).path("auth").path("refresh");
		
		Response requestResult =
				target.request(/*MediaType.APPLICATION_JSON_TYPE*/)
				.post(Entity.json("{token :"+refreshToken+"}"));
		String r = requestResult.readEntity(String.class);
		
		Gson gson = new Gson();
		JsonElement json = gson.fromJson(r, JsonElement.class);
		
		if(!json.getAsJsonObject().get("token").isJsonNull())
			token = json.getAsJsonObject().get("token").getAsString();
		
		if(!json.getAsJsonObject().get("refreshToken").isJsonNull())
			refreshToken = json.getAsJsonObject().get("refreshToken").getAsString();
		
		if(!json.getAsJsonObject().get("accessToken").isJsonNull())
			accessToken = json.getAsJsonObject().get("accessToken").getAsString();
	}
	
	/******************************************************************************************************************************/
	
	public ArticleApiClientService articles() {
		return new ArticleApiClientService();
	}
	
	public FactureApiClientService factures() {
		return new FactureApiClientService();
	}
	
	public TiersApiClientService tiers() {
		return new TiersApiClientService();
	}
	
	public BonLivApiClientService bonliv() {
		return new BonLivApiClientService();
	}
	public AbonnementApiClientService abonnements() {
		return new AbonnementApiClientService();
	}
	
	public PanierApiClientService paniers() {
		return new PanierApiClientService();
	}
	
	public EspaceClientApiClientService espaceClient() {
		return new EspaceClientApiClientService();
	}
	
	public PaiementApiClientService paiement() {
		return new PaiementApiClientService();
	}
	


}
