package fr.legrain.bdg.rest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurWebServiceServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.rest.filters.request.JWTTokenGlobalFilter;
import fr.legrain.bdg.rest.filters.request.SecretService;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.droits.model.TaUtilisateurWebService;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.service.ThreadLocalContextHolder;
import fr.legrain.general.service.remote.ITaAliasEspaceClientServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import io.jsonwebtoken.Jwts;

public abstract class AbstractRestService {
	
	/**
	 *  obligatoire à tous les appels
	 */
	public static final String X_HEADER_DOSSIER = "X-Dossier";
	//soit clé API soit login/password obligatoire pour une connection
	public static final String X_HEADER_APIKEY_1 = "X-Apikey-1";
	public static final String X_HEADER_APIKEY_2 = "X-Apikey-2";
	public static final String X_HEADER_LOGIN = "X-Bdg-login";
	public static final String X_HEADER_PASSWORD = "X-Bdg-password";
	
	/**
     * token de sécurité dans le cas d'une connexion par un logiciel client legrain :
     */
	public static final String X_HEADER_LGR = "X-Lgr";
	/**
     * Quel logiciel legrain fait l'appel :
     * - Espace Client / Boutique Angular
     * - Espace Client / Boutique Android
     * - Application dossier Angular
     * - peut être le client riche SWT/JFace/E4
     */
	public static final String X_HEADER_LGR_APP = "X-Lgr-app";
	/**
     * Logiciel client d'un éditeur tiers
     */
	public static final String X_HEADER_CLIENT_APP = "X-Client-app";
	
	public static final String JWT_CLAIM_DOSSIER = "dossier";
	public static final String JWT_CLAIM_TIERS = "tiers";
	public static final String JWT_CLAIM_UTILISATEUR = "utilisateur";
	
	public static final String CODE_HTTP_200 = "200";
	public static final String CODE_HTTP_401 = "401";
	public static final String CODE_HTTP_403 = "403";
	public static final String CODE_HTTP_404 = "404";
	public static final String CODE_HTTP_500 = "500";
	
	
	public static final String DATE_FORMAT = "yy-MM-dd";
	
	@EJB protected LgrEnvironnementServeur lgrEnvironnementServeur;
	
	protected TransactionSynchronizationRegistry reg = null;
	
	@EJB protected ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB protected ITaAliasEspaceClientServiceRemote taAliasEspaceClientService;
//	@EJB protected IEspaceClientRestMultitenantProxy multitenantProxy;
	@EJB protected ITaUtilisateurWebServiceServiceRemote taUtilisateurWebServiceService;
	
	@Context protected HttpServletRequest httpRequest;
	@Inject private SecretService f;
	@Inject private TenantInfo tenantInfo;
	
	protected BdgProperties bdgProperties;
	
	public String replaceTenantAlias(String tenantAlias) {
		
//		return multitenantProxy.replaceTenantAlias(tenantAlias);
		System.out.println(tenantAlias);
		String tmp = "";
		if(tenantAlias!=null && tenantAlias.startsWith("alias:")) {
			tenantAlias = tenantAlias.replace("alias:", "");
			System.out.println("Recherche d'un dossier pour l'alias : "+tenantAlias); tmp = tenantAlias;
			TaAliasEspaceClient taAliasEspaceClient = null;
			try {
				taAliasEspaceClient = taAliasEspaceClientService.findByCode(tenantAlias);
				if(taAliasEspaceClient!=null) {
					tenantAlias = taAliasEspaceClient.getTenant();
					System.out.println("Tenant de l'alias '"+tmp+"' : "+tenantAlias);
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tenantAlias;
	}
	
	public boolean initTenantAndAuthenticate(String tenant, String login, String password) {
		tenant = initTenant(tenant);
		return authentification(tenant,login,password);
	}
	
	public String initTenant(String tenant) {
		tenant = replaceTenantAlias(tenant);
//		initTenantRegistry();
////		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
//		reg.putResource(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY,tenant);
		
		ThreadLocalContextHolder.cleanupThread();
		ThreadLocalContextHolder.put(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY, tenant);
		
//      try {
//			TransactionSynchronizationRegistry reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
      
//      Map<String, Object> contextData = invocationContext.getContextData();
//      if (contextData.containsKey(TENANT_TOKEN_KEY)) {
//          tenant = (String) contextData.get(TENANT_TOKEN_KEY);
//          System.out.println("ServerTenantInterceptor.aroundInvoke() : "+tenant);
//          if(tenant!=null && !tenant.equals("")) {
//          	TENANT_TOKEN_VALUE = tenant;
//          	tenantRequest.setTenantRequest(tenant);
          	
//          	TenantInfo t = new TenantInfo();
          	tenantInfo.setTenantId(tenant);
//          	tenantInfoService.setTenantInfo(t);
          	
//          	tenantInfoService.getRegistry().putResource(SECURITY_TOKEN_KEY, authToken);
//          	reg.putResource(TENANT_TOKEN_KEY, tenant);
//          }

//      }
		
		return tenant;
	}
	
	public String getJwtToken() {
		String token = null;
		String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader!=null) {
			// Extract the token from the HTTP Authorization header
			token = authorizationHeader.substring("Bearer".length()).trim();
		}
		return token;
	}
	
	public String getDossierFromJwtToken() {
		String token = getJwtToken();
		String dossier = null;
		if(token!=null) {
			dossier = (String) Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(token).getBody().get(AbstractRestService.JWT_CLAIM_DOSSIER);
		}
		return dossier;
	}
	
	public Integer getTiersFromJwtToken() {
		String token = getJwtToken();
		Integer idTiers = null;
		if(token!=null) {
			idTiers = (Integer) Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(token).getBody().get(AbstractRestService.JWT_CLAIM_TIERS);
		}
		return idTiers;
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	protected boolean authentification(String dossier, String login, String password) {
//		return multitenantProxy.authentification(dossier,login,password);
		try {
			initTenantRegistry();
			initTenant(dossier);
			//TaUtilisateur u = taUtilisateurService.findByCode(login);
			TaUtilisateurWebService u = taUtilisateurWebServiceService.findByCode(login);
			if(u!=null && u.getPasswd()!=null && u.getPasswd().equals(u.passwordHashSHA256_Base64(password))) {
				return true;
			} else {
				//System.out.println("Utilisateur webservice ("+login+") rest invalide pour le dossier : "+reg.getResource(ServerTenantInterceptor.TENANT_TOKEN_KEY));
				System.out.println("Utilisateur webservice ("+login+") rest invalide pour le dossier : "+reg.getResource(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY));
				System.out.println("Vérifier le mot de passe dans le schéma correspondant au dossier - hash : "+u.passwordHashSHA256_Base64(password));
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}

	private void initTenantRegistry() {
//		try {
//			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		} 
	}
	
	public Date parseDateStringToDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		Date debut = null;
		try {
			debut = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return debut;
	}
	
	

}