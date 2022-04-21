package fr.legrain.bdg.rest.filters.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;

import fr.legrain.bdg.general.service.remote.ITaLogDossierServiceRemote;
import fr.legrain.bdg.rest.AbstractRestService;
import fr.legrain.bdg.rest.TiersRestService;
import fr.legrain.bdg.rest.filters.AbstractLogFilter;
import fr.legrain.general.service.ThreadLocalContextHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Provider
@PreMatching
//@Priority(Priorities.AUTHENTICATION)
public class JWTTokenGlobalFilter extends AbstractLogFilter implements ContainerRequestFilter {
	
	public static final String TENANT_REST_TOKEN_KEY = "tenantREST";
	private static String TENANT_REST_TOKEN_VALUE = null;
 
//    @Inject
//    private KeyGenerator keyGenerator;
	@Inject private SecretService f;
	@EJB TiersRestService restService;
	@EJB ITaLogDossierServiceRemote taLogDossierService;
	
	@Context
	private HttpServletRequest servletRequest;
 
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	
    	/*
    	 * Quand l'entityStream est lu, le flux est "au bout", il n'y a plus rien a lire.
    	 * Si une méthode dans un filtre lit ce flux, avant les méthode REST et leur MessageBodyReader, l'entité sera nulle.
    	 * Le système de log des appels API logge le body, donc il lit ce flux (un flux ne peut etre lu qu'une seule fois)
    	 * Il faut donc soit pouvoir relire ce flux, soit trouver un système de cache
    	 * Pour l'instant on met en cache le flux 2 fois.
    	 * cacheEntityStream1 - pour palier à la lecture nécessaire à la mise en cache
    	 * cacheEntityStream2 - pour palier à la lecture par les méthode qui logge les appels API
    	 * Ce système peut entrainer des problème de mémoire si les flux sont gros (fichier binaire, images, gros fichier json)
    	 * il faudra peut être le faire de façon conditionnelle ou logger de façon conditionnelle suivant le type de fichier ou la taille
    	 * A amélioré surement.
    	 */
		byte[] byteArray = IOUtils.toByteArray(requestContext.getEntityStream()); //mise en cache (lecture du flux original)    
	    InputStream cacheEntityStream1 = new ByteArrayInputStream(byteArray);
	    InputStream cacheEntityStream2 = new ByteArrayInputStream(byteArray);
	    
	    requestContext.setEntityStream(cacheEntityStream1); //utilisation d'un des flux de cache (qui pourra etre lu par les méthodes de log des API)

        
//    	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    	
//    	requestContext.getPropertyNames();
//	    requestContext.setProperty(AbstractLogFilter.BDG_UUID_REQUEST, UUID.randomUUID().toString());

    	if(!requestContext.getUriInfo().getAbsolutePath().toString().endsWith("refresh")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("authenticate")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("refresh-espace-client")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("authenticate-espace-client")
    			
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("creer-compte")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("demande-mdp")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("creer-compte-espace-client")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("liaison-nouveau-compte-espace-client") //TODO problème de sécurité ?
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("confirme-mdp")
    			
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("crypter")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("decrypter")
    			
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("logo-login")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("logos")
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("background-login")
    			
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("parametres") //séparer les paramètres de base accessible sans login, des autres pour plus de sécurité
    		
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("stripe-webhook")
    			
    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("openapi.json") //pour autoriser les appels de swagger-ui et bloquer les autres
    			) {
    		
    		String token = "";
    		try {
    			
		        // Get the HTTP Authorization header from the request
		        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		        String XDossier = requestContext.getHeaderString(AbstractRestService.X_HEADER_DOSSIER);
		        String XBdglogin = requestContext.getHeaderString(AbstractRestService.X_HEADER_LOGIN);
		        String XBdgpassword = requestContext.getHeaderString(AbstractRestService.X_HEADER_PASSWORD);
		        String authHeaderLgr = requestContext.getHeaderString(AbstractRestService.X_HEADER_LGR); 
		        String authHeaderLgrApp = requestContext.getHeaderString(AbstractRestService.X_HEADER_LGR_APP);
		        String authHeaderClientApp = requestContext.getHeaderString(AbstractRestService.X_HEADER_CLIENT_APP);
		        
		        requestContext.getPropertyNames();
			    requestContext.setProperty(AbstractLogFilter.BDG_UUID_REQUEST, UUID.randomUUID().toString());

		        if(XDossier!=null) {
			        if(authorizationHeader!=null && authorizationHeader.contains("Bearer")) {
				        // Extract the token from the HTTP Authorization header
				        token = authorizationHeader.substring("Bearer".length()).trim();
			 
			            // Validate the token
			//            Key key = keyGenerator.generateKey();
			            //Jwts.parser().setSigningKey(key).parseClaimsJws(token);
			        	Jws<Claims> claims = Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(token);
			            System.out.println("#### valid token : " + token);
			            ThreadLocalContextHolder.cleanupThread();
			            String tenant = restService.initTenant(XDossier);
//						ThreadLocalContextHolder.put(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY, tenant);
			            
			            
			            logGeneralApi(requestContext, XDossier, claims.getBody().getSubject(),null);

			        } else if(authorizationHeader!=null && authorizationHeader.contains("Basic")) {
		        	    // Authorization: Basic base64credentials
		        	    String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
		        	    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		        	    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		        	    // credentials = username:password
		        	    final String[] values = credentials.split(":", 2);
		        	    if(restService.initTenantAndAuthenticate(XDossier, values[0], values[1])) {
		        	    	logGeneralApi(requestContext, XDossier, values[0],null);
			        	} else {
//			        		logGeneralApi(requestContext, XDossier, values[0],"HTTP Basic : login/password incorrect");
			        		throw new Exception("HTTP Basic : login/password incorrect");
			        	}
			        } else if(XBdglogin!=null && XBdgpassword!=null) {
			        	//ancienne méthode login/password
			        	if(restService.initTenantAndAuthenticate(XDossier, XBdglogin, XBdgpassword)) {
			        		logGeneralApi(requestContext, XDossier, XBdglogin,null);
			        	} else {
//			        		logGeneralApi(requestContext, XDossier, XBdglogin,"Header : login/password incorrect");
			        		throw new Exception("Header : login/password incorrect");
			        	}
			        } else {
//			        	logGeneralApi(requestContext, XDossier, null,"Méthode de connexion incorrecte (token ou login/password)");
			        	throw new Exception("Méthode de connexion incorrecte (token ou login/password)");
			        }
		        } else {
//		        	logGeneralApi(requestContext, null, null,"Dossier non spécifié");
	        		throw new Exception("Dossier non spécifié");
	        	}
	 
	        } catch (Exception e) {
	        	System.err.println("#### invalid token on URL : " + requestContext.getUriInfo().getAbsolutePath().toString());
	        	System.err.println("#### invalid token : " + token);
	        	//logGeneralApi(requestContext, null, null,"invalid token");
	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
	        }
    	} else { //url non protégée, on affecte seulement le tenant
    		String XDossier = requestContext.getHeaderString(AbstractRestService.X_HEADER_DOSSIER);
    		if(XDossier!=null) {
    			ThreadLocalContextHolder.cleanupThread();
    			String tenant = restService.initTenant(XDossier);
//    			ThreadLocalContextHolder.put(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY, tenant);
//    			taLogDossierService.logGeneralApi(requestContext, XDossier, null,null);
    		}
       }
    
    	requestContext.setEntityStream(cacheEntityStream2); //utilisation d'un des flux de cache (qui permet de rétablir le flux d'origine pour le pas perturber )
    } 
    
   

}