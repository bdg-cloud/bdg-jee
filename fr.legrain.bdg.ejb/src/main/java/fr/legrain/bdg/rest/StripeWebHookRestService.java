package fr.legrain.bdg.rest;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.stripe.model.Event;
import com.stripe.net.Webhook;

import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaymentIntentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.model.TaReglement;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.paiement.service.PaiementStripeDossierService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;
import io.swagger.v3.oas.annotations.Operation;

@Path("stripe-webhook")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class StripeWebHookRestService extends AbstractRestService {
	
	//http://www.mkyong.com/tutorials/jax-rs-tutorials/
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaStripePaymentIntentServiceRemote taStripePaymentIntentService;
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	//private @EJB ILgrStripe lgrStripe;
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	@Inject private	TenantInfo tenantInfo;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	@Resource private EJBContext ejbContext;

	private @Context UriInfo uriInfo;
	/////////////////////////////////////////////////////////////////////////////////////////
    /*  
    curl -k -X POST -H "Content-Type: application/json" -H "Dossier: demo" -H "Lgr: aa" -d '{"id":"aa"}' https://dev.demo.promethee.biz:8443/v1/stripe-webhook
    */
	/*
	 * NON RECOMMANDE (depuis que Stripe CLI est disponible, mais peu encore servir)
	 * Test avec l'utilitaire ultrahook + envois de webhook depuis le dasboard en mode test
	 * Webhook et mode test local pour le dev
	 * Dans le compte stripe : http://adddfgfd.bdg.ultrahook.com
	 * ligne de commande : ultrahook adddfgfd https://dev.demo.promethee.biz:8443/v1/stripe-webhook
	 * 
	 * webhook sur le serveur de production
	 * Dans le compte stripe : https://api.bdg.cloud/v1/stripe-webhook
	 */
	/*
	 * Test avec Stripe CLI (RECOMMANDE)
	 * https://stripe.com/docs/stripe-cli/webhooks
	 * https://stripe.com/docs/webhooks/test
	 * installer
	 * # stripe login
	 * # stripe listen --forward-to https://dev.demo.promethee.biz:8443/v1/stripe-webhook --skip-verify
	 * --copier la clé dans le java ci dessous (variable endpointSecret en modeTest)
	 * -- PENSER REMETTRE EN COMMENTAIRE LA CLE TEMPORAIRE DU CLI APRES LES TESTS
	 * # stripe trigger charge.succeeded
	 */
    @POST()
    @Path("/")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Transactional(value=TxType.REQUIRES_NEW)
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Operation(summary = "Web Hook appelé par l'API Stripe", tags = {MyApplication.TAG_STRIPE}, description = "Pour tester en local on peut utiliser ultrahook.")
    public Response stripeWebhook(
    		@HeaderParam("Stripe-Signature") String stripeSignatureHeader, 
//    		@Context HttpServlet request,
    		String body) {
    	
    	TaTiers t = new TaTiers();
    	String requestUri = uriInfo.getRequestUri().getHost();
    	
		System.out.println("dddd : "+requestUri);
		String tenant = null;
//		tenant = requestUri.substring(0,requestUri.lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
//		tenant = tenant.substring(0,tenant.lastIndexOf("."));//suprime le domaine principal (dev.demo.promethee => dev.demo)
//		tenant = tenant.substring(tenant.lastIndexOf(".")+1);//conserve uniquement le premier niveau de sous domaine (dev.demo => demo)
		
		taParametreService.initParamBdg();
		
		String endpointSecret = null;
		boolean modeTest = false;
		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
		if(modeTest) {
			if(lgrEnvironnementServeur.isServeurDevLocal()) {
				endpointSecret = paramBdg.getTaParametre().getStripeEndPointSecret();
			} else if(lgrEnvironnementServeur.isServeurPProd()) {
				endpointSecret = paramBdg.getTaParametre().getStripeEndPointSecret(); //pprod
			}
		} else {
			endpointSecret = paramBdg.getTaParametre().getStripeEndPointSecret(); //production
		}
		String sigHeader = stripeSignatureHeader;
		
		try {
			Event event = Webhook.constructEvent(body, sigHeader, endpointSecret);
			if(event.getType().equals("payment_intent.succeeded")) {
	    		tenant = ((com.stripe.model.PaymentIntent) event.getData().getObject()).getMetadata().get("Dossier");
			} else if(event.getType().equals("charge.succeeded")) {
				tenant = ((com.stripe.model.Charge) event.getData().getObject()).getMetadata().get("Dossier");
			} else if(event.getType().equals("charge.captured")) {
				tenant = ((com.stripe.model.Charge) event.getData().getObject()).getMetadata().get("Dossier");
			}
			
			if(tenant!=null) {
				
				tenant = initTenant(tenant);
				ejbContext.getContextData().put("tenant_ws_stripe", tenant);
			
		    	//if(initTenantAndAuthenticate(dossier,login, password)) {
		//    	if(initTenantAndAuthenticate("demo","demo", "demo")) {
				
				String connectedAccountId = event.getAccount();
				
				TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
    				   
		    	if(compte!=null) {
//			    	String endpointSecret = "whsec_mA9FuDnFZ6vCjbwoOZ7jPSpuoYRXkyNi";
//		//          endpointSecret = System.getenv("STRIPE_ENDPOINT_SECRET");
//		          
//			    	String sigHeader = stripeSignatureHeader;
//		//          sigHeader = request.getHeaderString("Stripe-Signature");
//			    	Event event = Webhook.constructEvent(body, sigHeader, endpointSecret);
			    	/**
			    	 * https://stripe.com/docs/api/events/types
			    	 */
			    	if(event.getType().equals("charge.succeeded")
			    			|| event.getType().equals("charge.captured")) {
			    		String id = ((com.stripe.model.Charge) event.getData().getObject()).getId();
	//		    		Charge ch = ((com.stripe.model.Charge) event.getData().getObject());
			    		TaStripeCharge taStripeCharge = paiementEnLigneDossierService.updateChargeFromStripe(compte,id,connectedAccountId);
			    		
//			    		rechercher le TaPaymentIntent pour voir son mode de capture,
//			    		si c'est manual et que la charge n'est donc pas capturé, créer le règlement et le bon de commande "temporaire"
			    		if(!taStripeCharge.getCaptured()) {
			    			TaStripePaymentIntent pi = paiementEnLigneDossierService.validerPaymentIntentReglement(tenant,compte, ((com.stripe.model.Charge) event.getData().getObject()).getPaymentIntent(),connectedAccountId);
			    			
			    			if(pi.getTaReglement()!=null) {
				    			@SuppressWarnings("unused")
								TaReglement r = taReglementService.findByCode(pi.getTaReglement().getCodeDocument());
					    		IDocumentPayableCB docPay = paiementEnLigneDossierService.validerPaymentIntentGenerationDoc(tenant, pi);
//					    		/*
//					    		 * Si le règlement concerne une Facture ou un Ticket de caisse, on doit réaliser l'affactation
//					    		 * sinon
//					    		 * le réglement a deja été lié au document d'origine (Devis, Panier, ...)
//					    		 */
//					    		if(docPay!=null) {
//					    			paiementEnLigneDossierService.validerPaymentIntentAffectation(tenant, docPay, r, pi);
//					    		}
				    		}
			    		} else {
//			    			
			    		}
			    	  	if(event.getType().equals("charge.captured")) {
//			    	  		TaStripePaymentIntent pi = paiementEnLigneDossierService.updatePaymentIntentFromStripe(compte,((com.stripe.model.Charge) event.getData().getObject()).getPaymentIntent(),connectedAccountId);
			    	  	}
			    	} else if(event.getType().equals("invoice.created")) {
			    		((com.stripe.model.Invoice) event.getData().getObject()).getId();
			    	} else if(event.getType().equals("payment_intent.payment_failed")) {
			    		((com.stripe.model.PaymentIntent) event.getData().getObject()).getId();
			    	} else if(event.getType().equals("payment_intent.amount_capturable_updated")) {
			    		((com.stripe.model.PaymentIntent) event.getData().getObject()).getId();	
			    	} else if(event.getType().equals("payment_intent.succeeded")) {
			    		((com.stripe.model.PaymentIntent) event.getData().getObject()).getId();
			    		String id = ((com.stripe.model.PaymentIntent) event.getData().getObject()).getId();
			    		
			    		//paiementEnLigneDossierService.validerPaymentIntent(compte, id,connectedAccountId);
			    		
			    		/*
			    		 * Génération du règlement dans BDG
			    		 * ET liaison de ce règlement au document d'origine (Devis, Panier, ... HORS Facture et Ticket de caisse) si possible
			    		 */
			    		TaStripePaymentIntent pi = paiementEnLigneDossierService.validerPaymentIntentReglement(tenant,compte, id,connectedAccountId);
			    		
			    		/*
			    		 * Génération automatique d'un document à partir du document d'origne (si besoin - suivant préférence)
			    		 * Panier => Bon commande
			    		 * Devis => Facture
			    		 * OU PAS dans le cas d'un document réglable Facture ou Ticket de caisse
			    		 */
			    		if(pi.getTaReglement()!=null) {
			    			@SuppressWarnings("unused")
							TaReglement r = taReglementService.findByCode(pi.getTaReglement().getCodeDocument());
				    		IDocumentPayableCB docPay = paiementEnLigneDossierService.validerPaymentIntentGenerationDoc(tenant, pi);
				    		/*
				    		 * Si le règlement concerne une Facture ou un Ticket de caisse, on doit réaliser l'affactation
				    		 * sinon
				    		 * le réglement a deja été lié au document d'origine (Devis, Panier, ...)
				    		 */
				    		if(docPay!=null) {
				    			paiementEnLigneDossierService.validerPaymentIntentAffectation(tenant, docPay, r, pi);
				    		}
			    		}
			    		
			    	}
			    	/*
			    	 * payment_intent.payment_failed : Occurs when a PaymentIntent has failed the attempt to create a source or a payment.
					 * payment_intent.succeeded :Occurs when a PaymentIntent has been successfully fulfilled.
			    	 */
			    	
			    	//return Response.status(Status.OK).entity(t).build();
//			    	return Response.status(Status.OK).entity("{OK - "+event.getType()+" pris en charge sur "+tenant).build();
			    	String msg = event.getType()+" pris en charge sur "+tenant;
		    		return Response.status(Status.OK).entity("{\"status\":\"OK\","
		    				+ "\"message\":\""+msg+"\"}").build();
		    	} else {
		    		//return Response.status(500).entity(t).build();
//		    		return Response.status(Status.OK).entity("ERREUR - "+event.getType()+" non pris en charge sur "+tenant+". Pas de compte stripe paramétré.").build();
		    		String msg = event.getType()+" non pris en charge sur "+tenant+". Pas de compte stripe paramétré.";
		    		return Response.status(Status.OK).entity("{\"status\":\"ERREUR\","
		    				+ "\"message\":\""+msg+"\"}").build();
		    	}
				
		//    	return Response.ok(t,MediaType.APPLICATION_XML).build();
		//		return Response.status(200).entity("getUserByName is called, name : " + id).build();
	    	} else {
//				return Response.status(Status.UNAUTHORIZED).entity(t).build();
//	    		return Response.status(Status.OK).entity("ERREUR - évènement "+event.getType()+" non pris en charge par BDG, ou tenant introuvable.").build();
	    		String msg = "évènement "+event.getType()+" non pris en charge par BDG, ou tenant introuvable.";
	    		return Response.status(Status.OK).entity("{\"status\":\"ERREUR\","
	    				+ "\"message\":\""+msg+"\"}").build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}