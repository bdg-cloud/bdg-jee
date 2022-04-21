package fr.legrain.bdg.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;

//https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations#OpenAPIDefinition
@OpenAPIDefinition(
        info = @Info(
                title = "Bureau de Gestion Cloud",
                version = "v1",
                description = ""
                		+ "API REST de Bureau de Gestion<br>"
                		+ "<br>"
                		+ "<b>Le paramètre X-Dossier doit être présent dans l'entête HTTP de tous les appels</b>.<br>"
                		+ "Ce paramètre contient le nom du dossier Bureau de Gestion cible.<br>"
                		+ "<hr>"
                		+ "<br>"
                		+ "Pour obtenir un token d'authentification (accessToken) en tant qu'utilisateur du dossier ou en tant qu'utilisateur web service (api), utiliser la méthode : /auth/authenticate<br>"
                		+ "<br>"
                		+ "Pour obtenir un token d'authentification (accessToken) en tant qu'utilisateur final du dossier (tiers/client), utiliser la méthode : /auth/authenticate-espace-client<br>"
                		+ "<br>"
                		+ "L'ancienne méthode consistant à utiliser les paramètres X-Bdg-login et X-Bdg-password dans toutes les entêtes HTTP au lieu d'un token JWT fonctionne encore mais est dépréciée.<br>"
                		+ "<br>"
                		+ "Il est également possible d'utiliser une authentification standard HTTP Basic mais elle est dépréciée également et sera remplacer par HTTP Bearer + Token JWT.<br>"
                		+ "<br>"
                		+ "<hr>"
                		+ "Pour ne pas valider les certificats HTTPS lors d'un appel curl, utiliser l'option -k .<br>"
                		+ "",
                termsOfService = "termsOfService",
                contact = @Contact(
                        name = "Legrain Informatique",
                        url = "http://legrain.fr",
                        email = "legrain@legrain.biz"
                ),
                license = @License(
                        name = "License Legrain",
                        url = "http://license.org/license"
                )
        )
//        ,
//        servers = {@Server(description = "aazzeeerrttyy",url = "/v1")}
        
        ,security = { 
        		@SecurityRequirement (name = "bdgBasicHttpSecurity"),
        		@SecurityRequirement (name = "bdgBearerSecurity")
//        		,@SecurityRequirement (name = "myApiDossierKeySecurity", scopes = "write: read")
        }
//        ,
//        security = { @SecurityRequirement (name = "myOauth2Security", scopes = "write: read") }
////        ,
////        host = "host.org",
////        basePath = "",
////        schemes = SwaggerDefinition.Scheme.HTTPS
)

@SecuritySchemes({
		@SecurityScheme(name = "bdgBearerSecurity",
			type = SecuritySchemeType.HTTP,
			in = SecuritySchemeIn.HEADER,
			scheme = "bearer",
			bearerFormat = "JWT")
		,
		@SecurityScheme(name = "bdgBasicHttpSecurity",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER,
		scheme = "basic")
		
//		,@SecurityScheme(name = "myOauth2Security",
//		type = SecuritySchemeType.OAUTH2,
//		in = SecuritySchemeIn.HEADER,
//		flows = @OAuthFlows(
//		        implicit = @OAuthFlow(authorizationUrl = "http://url.com/auth",
//		                scopes = @Scopes(name = "write:pets", description = "modify pets in your account")
//		        )
//		        )
//		)
		
//		,@SecurityScheme(name = "myApiDossierKeySecurity",
//			type = SecuritySchemeType.APIKEY,
//			in = SecuritySchemeIn.HEADER,
//			paramName = "X-Dossier"),
//		
//		@SecurityScheme(name = "myApiLoginKeySecurity",
//			type = SecuritySchemeType.APIKEY,
//			in = SecuritySchemeIn.HEADER,
//			paramName = "X-Bdg-login"),
//		
//		@SecurityScheme(name = "myApiPasswordKeySecurity",
//			type = SecuritySchemeType.APIKEY,
//			in = SecuritySchemeIn.HEADER,
//			paramName = "X-Bdg-password")
//		}
})
public class SwaggerMainV1 {

}

//TODO arriver a faire fonctionnner un filtre pour ajouter le paramètre X-Dossier en header de toute le opération de l'api
//public class NoPetOperationsFilter extends AbstractSpecFilter {
//
//    public static final String PET_RESOURCE = "/pet";
//
//    @Override
//    public Optional<Operation> filterOperation(Operation operation, ApiDescription api, Map<String, List<String>> params,
//    		Map<String, String> cookies, Map<String, List<String>> headers) {
//        if (api.getPath().startsWith(PET_RESOURCE)) {
//            return Optional.empty();
//        }
//        return Optional.of(operation);
//    } 
//} 
