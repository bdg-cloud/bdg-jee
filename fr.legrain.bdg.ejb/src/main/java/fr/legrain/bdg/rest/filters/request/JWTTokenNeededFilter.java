//package fr.legrain.bdg.rest.filters.request;
//
//import java.io.IOException;
//import java.security.Key;
//
//import javax.annotation.Priority;
//import javax.ejb.EJB;
//import javax.inject.Inject;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.ext.Provider;
//
//import fr.legrain.bdg.rest.TiersRestService;
//import fr.legrain.general.service.TenantSoapHeaderHandler;
//import fr.legrain.general.service.ThreadLocalContextHolder;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
////@Provider
////@JWTTokenNeeded
////@Priority(Priorities.AUTHENTICATION)
////https://antoniogoncalves.org/2016/10/03/securing-jax-rs-endpoints-with-jwt/
//public class JWTTokenNeededFilter implements ContainerRequestFilter {
//	
//	public static final String TENANT_REST_TOKEN_KEY = "tenantREST";
//	private static String TENANT_REST_TOKEN_VALUE = null;
// 
////    @Inject
////    private KeyGenerator keyGenerator;
//	@Inject private SecretService f;
//	@EJB TiersRestService restService;
// 
//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//
//    	if(!requestContext.getUriInfo().getAbsolutePath().toString().endsWith("refresh")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("authenticate")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("creer-compte")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("demande-mdp")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("creer-compte-espace-client")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("liaison-nouveau-compte-espace-client") //TODO problème de sécurité ?
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("confirme-mdp")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("crypter")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("decrypter")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("logo-login")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("logos")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("background-login")
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("parametres") //séparer les paramètres de base accessible sans login, des autres pour plus de sécurité
//    			&& !requestContext.getUriInfo().getAbsolutePath().toString().endsWith("stripe-webhook")
//    			) {
//    		
//    		String token = "";
//    		try {
//    			
//		        // Get the HTTP Authorization header from the request
//		        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//		        String XDossier = requestContext.getHeaderString("X-Dossier");
//		        String XBdglogin = requestContext.getHeaderString("X-Bdg-login");
//		        String XBdgpassword = requestContext.getHeaderString("X-Bdg-password");
//		        
//		        
//		 
//		        if(XDossier!=null) {
//			        if(authorizationHeader!=null) {
//				        // Extract the token from the HTTP Authorization header
//				        token = authorizationHeader.substring("Bearer".length()).trim();
//			 
//			            // Validate the token
//			//            Key key = keyGenerator.generateKey();
//			            //Jwts.parser().setSigningKey(key).parseClaimsJws(token);
//			        	Jwts.parser().setSigningKey(f.getKeyHS256()).parseClaimsJws(token);
//			            System.out.println("#### valid token : " + token);
//			            ThreadLocalContextHolder.cleanupThread();
//						ThreadLocalContextHolder.put(JWTTokenNeededFilter.TENANT_REST_TOKEN_KEY, XDossier);
//			            restService.initTenant(XDossier);
//			        } else if(XBdglogin!=null && XBdgpassword!=null) {
//			        	//ancienne méthode login/password
//			        	if(restService.initTenantAndAuthenticate(XDossier, XBdglogin, XBdgpassword)) {
//			        		
//			        	} else {
//			        		throw new Exception("login/password incorrect");
//			        	}
//			        }
//		        } else {
//	        		throw new Exception("Dossier non spécifié");
//	        	}
//	 
//	        } catch (Exception e) {
//	        	System.err.println("#### invalid token on URL : " + requestContext.getUriInfo().getAbsolutePath().toString());
//	        	System.err.println("#### invalid token : " + token);
//	        	e.printStackTrace();
//	            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//	        }
//    	}
//    }
//}