package fr.legrain.bdg.rest.filters.request;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.ContainerRequestContext;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;

@Provider
@PreMatching
/*
 * Actions sur le HEADER
 */
//https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter12/server_side_filters.html
public class LgrTokenFilter implements ContainerRequestFilter {

	public void filter(ContainerRequestContext ctx) throws IOException {
//		String authHeader = ctx.getHeaderString("Lgr");
		String authHeaderX = ctx.getHeaderString("X-Lgr");
		if(!ctx.getUriInfo().getAbsolutePath().toString().endsWith("openapi.json") //pour autoriser les appels de swagger-ui et bloquer les autres
				&& !ctx.getUriInfo().getAbsolutePath().toString().endsWith("stripe-webhook")) { //pour autoriser les appels webhooks de stripe et bloquer les autres
			
			
			/*
			 * Pour les appels AJAX provenant d'autres domaines, par exemple un Add-In Microsoft Word hébergé sur un autre domaine,
			 * les protections CORS - Cross-Origin Resource Sharing - du navigateur peuvent bloquer la requete.
			 * Dans ce cas il faut soit faire une modification sur les entetes de réponse du serveur (CORS), soit utiliser JSONP
			 * 
			 * Dans le cas d'une requete CORS, le navigateur envoie une pré-requete avec la methode HTTP OPTION pour verifier les parametre CORS du serveur,
			 * cette requete n'est pas la "vrai" requete, elle la précède seulement et ne possède donc pas les entetes qui dans notre cas peremettent
			 * d'autoriser l'acces ou non au web service.
			 * Il faut donc laisser passer ces requetes OPTION, si on les bloques, le client ne sait pas si le serveur autorise les requete CORS et donc
			 * il ne fait jamais la "vrai" requete.
			 * 
			 * Autorisation des requetes CORS (sans JSONP) dans WildFly 10, dans le standalone.xml :
			 <host name="default-host" alias="localhost" default-web-module="fr.legrain.bdg.webapp.war">
                    <location name="/" handler="welcome-content"/>
                    <filter-ref name="server-header"/>
                    <filter-ref name="x-powered-by-header"/>
                    <filter-ref name="Access-Control-Allow-Origin"/>
					<filter-ref name="Access-Control-Allow-Methods"/>
					<filter-ref name="Access-Control-Allow-Headers"/>
					<filter-ref name="Access-Control-Allow-Credentials"/>
             </host>
             
             <filters>
                <response-header name="server-header" header-name="Server" header-value="WildFly/10"/>
                <response-header name="x-powered-by-header" header-name="X-Powered-By" header-value="Undertow/1"/>
                <response-header name="Access-Control-Allow-Origin" header-name="Access-Control-Allow-Origin" header-value="*"/>
				<response-header name="Access-Control-Allow-Methods" header-name="Access-Control-Allow-Methods" header-value="GET, POST, OPTIONS, PUT"/>
				<response-header name="Access-Control-Allow-Headers" 
				header-name="Access-Control-Allow-Headers" header-value="Dossier, Lgr, Bdg-login, Bdg-password, 
					accept, authorization, content-type, x-requested-with"/>
				<response-header name="Access-Control-Allow-Credentials" header-name="Access-Control-Allow-Credentials" header-value="true"/>
             </filters>
			 */
			if(!ctx.getMethod().equals("OPTIONS")) { //Ne pas vérifier les methode OPTIONS
				if ( /*(authHeader == null) &&*/ (authHeaderX == null)) {
					throw new NotAuthorizedException("Legrain");
				}
				String token = null;
				if(authHeaderX!=null) {
					token = parseToken(authHeaderX);
//				} else if(authHeader!=null) {
//					token = parseToken(authHeader);
				}
				
				if (verifyToken(token) == false) {
					throw new NotAuthorizedException("Legrain error=\"invalid_token\"");
				}
			}
		} else {
			System.out.println("LgrTokenFilter.filter() *** ATTENTION - SWAGGER-UI : Faille de sécurité potentielle ");
			System.out.println("LgrTokenFilter.filter() *** ATTENTION - STRIPE WEBHOOKS : Faille de sécurité potentielle ");
		}
	}

	private String parseToken(String header) {
		return header;
	}
	
	private boolean verifyToken(String token) {
		if(token.equals("aa"))
			return true;
		else 
			return false;
	}
}