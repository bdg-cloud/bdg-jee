package fr.legrain.bdg.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

import fr.legrain.bdg.rest.filters.request.JWTTokenGlobalFilter;
import fr.legrain.bdg.rest.filters.response.LogFilter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;;

/*
 * https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-en/cn/part1/chapter12/ordering_filters_and_interceptors.html
 */
//@ApplicationPath("/rest")
@ApplicationPath(MyApplication.APPLICATION_PATH)
public class MyApplication extends Application {
	
	public static final String APPLICATION_PATH = "/v1";
	
	public static final String TAG_AUTH_ESPACE_CLIENT = "Auth - Espace Client";
	public static final String TAG_AUTH_BDG = "Auth - BDG";
	public static final String TAG_TIERS = "Tiers";
	public static final String TAG_ARTICLE = "Article";
	public static final String TAG_FLASH = "Flash";
	public static final String TAG_STRIPE = "Paiement - Stripe";
	public static final String TAG_GANDI = "Gandi";
	public static final String TAG_ESPACE_CLIENT = "Espace Client";
	public static final String TAG_CATALOGUE = "Catalogue";
	public static final String TAG_BOUTIQUE = "Boutique";
	public static final String TAG_LOT = "Lots";
	public static final String TAG_DOCUMENTS = "Documents";
	public static final String TAG_FACTURES = "Factures";
	public static final String TAG_AVIS_ECHEANCES = "Avis d'échéances";
	public static final String TAG_ABONNEMENT = "Abonnements";
	public static final String TAG_BON_LIVRAISON = "Bon de livraison";
	public static final String TAG_PANIER = "Panier";
	public static final String TAG_DEVIS = "Devis";
	public static final String TAG_COMMANDE = "Commande";
	public static final String TAG_DIVERS = "Divers";
	public static final String TAG_UTILISATEUR_ET_DOSSIER = "Utilisateurs et dossier";
	
//	public MyApplication(@Context ServletConfig servletConfig) {
//		 super();
//	        OpenAPI oas = new OpenAPI();
//	        Info info = new Info()
//	                .title("Swagger Sample App bootstrap code")
//	                .description("This is a sample server Petstore server.  You can find out more about Swagger " +
//	                        "at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, " +
//	                        "you can use the api key `special-key` to test the authorization filters.")
//	                .termsOfService("http://swagger.io/terms/")
//	                .contact(new Contact()
//	                        .email("apiteam@swagger.io"))
//	                .license(new License()
//	                        .name("Apache 2.0")
//	                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));
//
//	        oas.info(info);
//	        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
//	                .openAPI(oas)
//	                .prettyPrint(true)
//	                .resourcePackages(Stream.of("io.swagger.sample.resource").collect(Collectors.toSet()));
//
//	        try {
//	        	
////	        		    OpenAPISpecFilter filterImpl = my.custom.Filter;
////	        		    SpecFilter f = new SpecFilter();
////	        		    oas = f.filter(oas, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
////	        		            getHeaders(headers));
////	        	servletConfig.
//	            new JaxrsOpenApiContextBuilder<>()
//	                    .servletConfig(servletConfig)
//	                    .application(this)
//	                    .openApiConfiguration(oasConfig)
//	                    .buildContext(true);
//	        } catch (OpenApiConfigurationException e) {
//	            throw new RuntimeException(e.getMessage(), e);
//	        }
//
//	    
//	}
	
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        
        s.add(TiersRestService.class);
        s.add(ArticleRestService.class);
        s.add(LotRestService.class);
        
        //Documents
        s.add(BonLivRestService.class);
        s.add(AbonnementRestService.class);
        s.add(CommandeRestService.class);
        s.add(DevisRestService.class);
        s.add(FactureRestService.class);
        s.add(StripePaiementRestService.class);
        s.add(AvisEcheanceRestService.class);

        s.add(FlashRestService.class);
        s.add(CatalogueRestService.class);
        s.add(PanierRestService.class);
        
        //
        s.add(TestConnexionRestService.class);
        
        s.add(AuthentificationRestService.class);
        s.add(EspaceClientRestService.class);
        s.add(UtilisateurRestService.class);
                
        s.add(GandiRestService.class);
        
        //filters
//        s.add(LgrTokenFilter.class);
        //s.add(JWTTokenNeededFilter.class);
        s.add(JWTTokenGlobalFilter.class);
        s.add(LogFilter.class);
        //interceptors
        
        s.add(StripeWebHookRestService.class);
        
        s.add(OpenApiResource.class);
        s.add(SwaggerMainV1.class);
//        s.add(ApiListingResource.class);
//        s.add(SwaggerSerializers.class);
        
        return s;
    }

}