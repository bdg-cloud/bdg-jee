package fr.legrain.paiement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.stripe.Stripe;

import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceItem;
import com.stripe.model.InvoiceItemCollection;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.stripe.model.Product;
import com.stripe.model.Refund;
import com.stripe.model.Source;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.SubscriptionItem;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.service.TaStripeChargeService;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCouponServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceItemServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaReglement;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;

/**
 * Classe (EJB) à utiliser uniquement pour les paiements que les client de BDG doivent faire directememt à Legrain.
 * Les clé stripe sont celles de Legrain.
 * On peut l'utiliser pour faire un paiment de panier BDG, acheter des modules/options BDG, un renouvellement BDG, ...
 * 
 * Pour tous les autres paiement qui sont lié aux dossiers (facture, ...) il faut utiliser une des classe qui implémente IPaiementEnLigneDossierService
 * par exemple EnvoiSMSMailjetDossierService en ayant au préalable paramétré la clé API du service de paiement choisi dans l'interface du programme
 * @author nicolas
 *
 */
@Stateless
public class LgrStripe implements ILgrStripe {
	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	private boolean modeTest = false;
	
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaStripeProductServiceRemote taStripeProductService;
	private @EJB ITaStripePlanServiceRemote taStripePlanService;
	private @EJB ITaStripeCouponServiceRemote taStripeCouponService;
	
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeInvoiceServiceRemote taStripeInvoiceService;
	private @EJB ITaStripeInvoiceItemServiceRemote taStripeInvoiceItemService;
	private @EJB ITaStripeChargeServiceRemote taStripeChargeService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	private boolean synchroniserStripeVersBdg = true;
	
	private void initCleStripe() throws Exception {
		
//		Properties props = new Properties(); 
//		String propertiesFileName = "bdg.properties";  
//		String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
//
//		String ip = null;
//		if(new File(path).exists()) { 
//			File f = new File(path);
//			props.load(new FileInputStream(f));  
//			ip = props.getProperty("ip");
//		}
		
		modeTest = lgrEnvironnementServeur.isModeTestPaiementProgramme();
		
//		modeTest = false;
		taParametreService.initParamBdg();
		
		if(!modeTest) { //on est à prioris bien sur internet et pas sur un poste de développement
			Stripe.apiKey = paramBdg.getTaParametre().getStripeLiveSecretApiKeyProgramme(); //clé compte legrain
		} else {	
			//modeTest = true;
			Stripe.apiKey = paramBdg.getTaParametre().getStripeTestSecretApiKeyProgramme(); //clé test compte legrain
		}

	}
	
	@Override
	public String payer(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String description) throws EJBException{
		return payer(montant, numeroCarte,moisCarte,anneeCarte,cryptogrammeCarte,null,description);
	}
	
	@Override
	public String payer(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description) throws EJBException{
		try {
			initCleStripe();
			
			if(modeTest) {
				System.out.println("** LgrStripe ** mode TEST activé, utilisation d'information de paiement de TEST.");
				montant = new BigDecimal(2000);
				numeroCarte = "5555555555554444";
				moisCarte = 8;
				anneeCarte = 2025;
				cryptogrammeCarte = "314";
				nomPorteurCarte = "LEGRAIN - mode TEST";
				description = "Paiement Stripe Legrain - mode TEST";
			}
			
			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
			
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			chargeParams.put("amount", amount);
			chargeParams.put("currency", "eur");
			chargeParams.put("description", description);
			
			Map<String, Object> sourceParams = new HashMap<String, Object>();
			sourceParams.put("number", numeroCarte);
			sourceParams.put("exp_month", moisCarte);
			sourceParams.put("exp_year", anneeCarte);
			sourceParams.put("cvc", cryptogrammeCarte);
			sourceParams.put("name", nomPorteurCarte);
			
			chargeParams.put("source", sourceParams);
			
		
			
			System.out.println("LgrStripe ** paiment Stripe - montant : "+amount);
			Charge c = Charge.create(chargeParams);
			
			if(!c.getStatus().equals("succeeded")) {
				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
			}
			if(!c.getPaid()) {
				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
			}

			return c.getId();
		} catch (AuthenticationException e) {
			// Authentication with Stripe's API failed
            // (maybe you changed API keys recently)
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} catch (InvalidRequestException e) {
			// Invalid parameters were supplied to Stripe's API
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (APIConnectionException e) {
//			// Network communication with Stripe failed
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} catch (CardException e) {
			// Since it's a decline, \Stripe\Error\Card will be caught
			if(e.getCode()!=null && e.getCode().equals("incorrect_cvc")) {
				throw new EJBException("le code de sécurité est incorrect",e);
			} else if(e.getCode()!=null && e.getCode().equals("expired_card")) {
				throw new EJBException("La carte a expiré",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_cvc")) {
				throw new EJBException("Le code de sécurité de la carte est invalide",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_expiry_month")) {
				throw new EJBException("Le mois d'expiration de la carte est invalide",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_expiry_year")) {
				throw new EJBException("L'année d'expiration de la carte est invalide",e);
			} else if(e.getCode()!=null && (e.getCode().equals("invalid_number") || e.getCode().equals("incorrect_number"))) {
				throw new EJBException("Le numéro de carte saisie est invalide",e);
			} else {
				e.printStackTrace();
				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
			}
//		} catch (APIException e) {
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		}
	}

	public void test() {
		try {
			
			initCleStripe();
		    
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			chargeParams.put("amount", 2000);
			chargeParams.put("currency", "eur");
			Map<String, Object> sourceParams = new HashMap<String, Object>();
			sourceParams.put("number", "5555555555554444");
			sourceParams.put("exp_month", 8);
			sourceParams.put("exp_year", 2025);
			sourceParams.put("cvc", "314");
			chargeParams.put("source", sourceParams);
			chargeParams.put("description", "Charge for emma.brown@example.com");
	
		
			Charge.create(chargeParams);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (APIConnectionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (APIException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
////	@Override
//	public Charge remontePaiement(String idPaiement) {
//	
//		try {
//			initCleStripe();
//			return Charge.retrieve(idPaiement);
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public String creerCarteStripe(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description) throws EJBException {
//		return null;
//	}
//	
//	public String creerCarteStripe(String carteTokenStripe)  throws EJBException{
//		try {
//			initCleStripe();
//			
//			
////			Map<String, Object> sepaParams = new HashMap<String, Object>();
////			sepaParams.put("token", carteTokenStripe); 
//////			FR1420041010050500013M02606 	The charge status transitions from pending to succeeded.
//////			FR8420041010050500013M02607 	The charge status transitions from pending to failed.
////
////			Map<String, Object> ownerParams = new HashMap<String, Object>();
////			ownerParams.put("name", nomPersonneAPrelever);
////
////			Map<String, Object> sourceParams = new HashMap<String, Object>();
////			sourceParams.put("type", "sepa_debit");
////			sourceParams.put("sepa_debit", sepaParams);
////			sourceParams.put("currency", "eur");
////			sourceParams.put("owner", ownerParams);
////			
////			System.out.println("LgrStripe ** Création d'une source Carte par token Stripe - pour : "+nomPersonneAPrelever);
////			Source source = Source.create(sourceParams);
////			
////			System.out.println("LgrStripe ** Création d'une source Carte par token Stripe - ID : "+source.getId());
//////			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - URL Mandat : "+source.getInstanceURL());
////			
////			if(!source.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//////			if(!source.getPaid()) {
//////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//////			}
////
////			return source.getId();
//			return null;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	
////	@Override
//	public String creerSourcePrelevementSEPA(String iban, String nomPersonneAPrelever) throws EJBException{
//		try {
//			initCleStripe();
//			
//			
//			Map<String, Object> sepaParams = new HashMap<String, Object>();
//			sepaParams.put("iban", iban); 
////			FR1420041010050500013M02606 	The charge status transitions from pending to succeeded.
////			FR8420041010050500013M02607 	The charge status transitions from pending to failed.
//
//			Map<String, Object> ownerParams = new HashMap<String, Object>();
//			ownerParams.put("name", nomPersonneAPrelever);
//
//			Map<String, Object> sourceParams = new HashMap<String, Object>();
//			sourceParams.put("type", "sepa_debit");
//			sourceParams.put("sepa_debit", sepaParams);
//			sourceParams.put("currency", "eur");
//			sourceParams.put("owner", ownerParams);
//			
//			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - pour : "+nomPersonneAPrelever);
//			Source source = Source.create(sourceParams);
//			
//			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - ID : "+source.getId());
////			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - URL Mandat : "+source.getInstanceURL());
//			
//			if(!source.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return source.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerCustomerStripe(String email, String sourceId, String description) throws EJBException{
//		try {
//			initCleStripe();
//			
//			
//			Map<String, Object> customerParams = new HashMap<String, Object>();
//			if(email!=null) {
//				customerParams.put("email", email);//"paying.user@example.com"
//			}
//			if(sourceId!=null) {
//				customerParams.put("source", sourceId);//"src_18eYalAHEMiOZZp1l9ZTjSU0"
//			}
//			if(description!=null) {
//				customerParams.put("description", description);
//			}
//			
//			System.out.println("LgrStripe ** Création d'un customer Stripe - pour : "+email);
//			Customer customer = Customer.create(customerParams);
//			
//			System.out.println("LgrStripe ** Création d'un customer Stripe - ID : "+customer.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return customer.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String attacherSourceStripe(String sourceId, String customerId) throws EJBException{
//		try {
//			initCleStripe();
//			
//			Customer customer = Customer.retrieve(customerId);
//			
//			Map<String, Object> params = new HashMap<String, Object>();
//			if(sourceId!=null) {
//				params.put("source", sourceId);//"src_1DjSutESo4ojJRjqUbmKo88D"
//			}
//			
//			System.out.println("LgrStripe ** Attache d'une source à un customer Stripe - : "+sourceId +" <--->"+customerId);
//			customer.getSources().create(params);
//			
////			System.out.println("LgrStripe ** Création d'un customer Stripe - ID : "+customer.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return customer.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String detacherSourceStripe(String sourceId) throws EJBException{
//		try {
//			initCleStripe();
//			
//			System.out.println("LgrStripe ** Detache la source Stripe -  : "+sourceId);
//			Source source = Source.retrieve(sourceId);//"src_1DjSutESo4ojJRjqUbmKo88D"
//			source.detach();
//			
////			System.out.println("LgrStripe ** Création d'un customer Stripe - ID : "+customer.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return source.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerProductStripe(String name, String type) throws EJBException{
//		try {
//			initCleStripe();
//			
//			
//			Map<String, Object> productParams = new HashMap<String, Object>();
//			if(name!=null) {
//				productParams.put("name", name);//"Monthly membership base fee"
//			}
//			if(type!=null) {
//				productParams.put("type", "service");//"service" ou "good"
//			}
//			
//			System.out.println("LgrStripe ** Création d'un product Stripe - pour : "+name);
//			Product product = Product.create(productParams);
//			
//			System.out.println("LgrStripe ** Création d'un product Stripe - ID : "+product.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return product.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerPlanStripe(String nickname, String interval, String currency, BigDecimal montant, String productStripeID) throws EJBException{
//		try {
//			initCleStripe();
//			
//			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
//			
////			Map<String, Object> productParams = new HashMap<String, Object>();
////			productParams.put("name", "Gold special");
////
////			Map<String, Object> planParams = new HashMap<String, Object>();
////			planParams.put("amount", 5000);
////			planParams.put("interval", "month");
////			planParams.put("product", "productParams"); //prod_E0sue83NPHQEDK
////			planParams.put("currency", "usd");
//			
//			Map<String, Object> planParams = new HashMap<String, Object>();
//			if(currency!=null) {
//				planParams.put("currency", "eur");//"obligatoire
//			}
//			if(interval!=null) {
//				planParams.put("interval", interval);//day, week, month or year
//			}
//			if(productStripeID!=null) {
//				planParams.put("product", productStripeID);//id product (ex:Map<String, Object> productParams ) ou une map pour créer un nouveau produit
//			}
//			if(montant!=null) { //obligatoire si billing_scheme=per_unit, correspond au prix (5000 pour 50,00€)
//				planParams.put("amount", amount);
//			}
//			if(nickname!=null) { //facultatif
//				planParams.put("nickname", nickname);
//			}
//			
//			System.out.println("LgrStripe ** Création d'un plan Stripe - pour le produit : "+productStripeID);
//			Plan plan = Plan.create(planParams);
//			
//			System.out.println("LgrStripe ** Création d'un plan Stripe - ID : "+plan.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return plan.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerSubscriptionStripe(String customerStripeID, Map<String,Integer> mapSubscriptionItemPlanStripeIDQuantity,
//			String sourceStripeID, Integer timpeStampBillingCycleAnchor, String billing, Integer daysUntilDue) throws EJBException{
//		try {
//			initCleStripe();
//				
//			Map<String, Object> params = new HashMap<String, Object>();
//			if(customerStripeID!=null) {
//				params.put("customer", customerStripeID);//cus_ENkir6GZT1coas
//			}
//			
//			if(sourceStripeID!=null) {
//				params.put("default_source", sourceStripeID);//src_1DjSutESo4ojJRjqUbmKo88D
//			}
//			
//			if(timpeStampBillingCycleAnchor!=null) {
//				params.put("billing_cycle_anchor", timpeStampBillingCycleAnchor);//1549623023
//			}
//			
//			if(billing!=null) {
//				params.put("billing", billing);//charge_automatically, or send_invoice
//			}
//			
//			if(daysUntilDue!=null) {
//				params.put("days_until_due", daysUntilDue);
//			}
//			
//			Map<String, Object> items = new HashMap<String, Object>();
//			Map<String, Object> item = null;
//			if(mapSubscriptionItemPlanStripeIDQuantity!=null) {
//				int i = 0;
//				for (String planStripeID : mapSubscriptionItemPlanStripeIDQuantity.keySet()) {
//					item = new HashMap<String, Object>();
//					item.put("plan", planStripeID);//plan_ENkjvo6pZIX2og
//					item.put("quantity", mapSubscriptionItemPlanStripeIDQuantity.get(planStripeID));
//					items.put(LibConversion.integerToString(i),item); //items.put("0", item);
//				}
//				params.put("items", items);
//			}
//			
//			
//			System.out.println("LgrStripe ** Création d'une subscription Stripe - pour le customer : "+customerStripeID);
//			Subscription subscription = Subscription.create(params);
//			
//			System.out.println("LgrStripe ** Création d'un subscription Stripe - ID : "+subscription.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return subscription.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String annulerSubscriptionStripe(String subscriptionStripeID) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Subscription sub = Subscription.retrieve(subscriptionStripeID); //sub_ENkpQA7eMGNU3U
//			
//			System.out.println("LgrStripe ** Annulation d'une subscription Stripe - pour la subscription : "+subscriptionStripeID);
//			Subscription subscription = sub.cancel(null);
//			
//			System.out.println("LgrStripe ** Annulation d'une subscription Stripe - ID : "+subscription.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return subscription.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerInvoiceStripe(String customerStripeID) throws EJBException{
//		try {
//			initCleStripe();
//			
////			Map<String, Object> invoiceParams = new HashMap<String, Object>();
////			invoiceParams.put("customer", "cus_E0s4xZ5iCBzyaP");
////
////			Invoice.create(invoiceParams);
//			
//			Map<String, Object> invoiceParams = new HashMap<String, Object>();
//			if(customerStripeID!=null) {
//				invoiceParams.put("customerStripeID", customerStripeID);//"obligatoire
//			}
//			
//			
//			System.out.println("LgrStripe ** Création d'un invoice Stripe - pour le customer : "+customerStripeID);
//			Invoice invoice = Invoice.create(invoiceParams);
//			
//			System.out.println("LgrStripe ** Création d'un invoice Stripe - ID : "+invoice.getId());
//
//
//			return invoice.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerInvoiceItemStripe(String customerStripeID, String description, BigDecimal montant) throws EJBException{
//		try {
//			initCleStripe();
//			
//			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
//			
////			Map<String, Object> invoiceItemParams = new HashMap<String, Object>();
////			invoiceItemParams.put("customer", "cus_E0s4xZ5iCBzyaP");
////			invoiceItemParams.put("amount", 2500);
////			invoiceItemParams.put("currency", "usd");
////			invoiceItemParams.put("description", "One-time setup fee");
//			
//			String currency = "eur";
//			
//			Map<String, Object> invoiceItemParams = new HashMap<String, Object>();
//			if(customerStripeID!=null) {
//				invoiceItemParams.put("customerStripeID", customerStripeID);//"obligatoire
//			}
//			if(currency!=null) {
//				invoiceItemParams.put("currency", currency);//"obligatoire
//			}
//			if(description!=null) {
//				invoiceItemParams.put("description", description);//"obligatoire
//			}
//			if(montant!=null) {
//				invoiceItemParams.put("amount", amount);//"obligatoire
//			}
//			
//			
//			System.out.println("LgrStripe ** Création d'un invoice Stripe - pour le customer : "+customerStripeID);
//			InvoiceItem invoiceItem = InvoiceItem.create(invoiceItemParams);
//			
//			System.out.println("LgrStripe ** Création d'un invoice Stripe - ID : "+invoiceItem.getId());
//
//
//			return invoiceItem.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public int montantBdgVersStripe(BigDecimal montant) {
//		int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
//		return amount;
//	}
//	
//	public BigDecimal montantStripeVersBdg(Long montant) {
//		BigDecimal amount = new BigDecimal(montant).divide(new BigDecimal(100));
//		return amount;
//	}
//	
//	public Long dateBdgVersStripe(Date montant) {
//		//int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
//		return montant.getTime();
////		return amount;
//	}
//	
//	public Date dateStripeVersBdg(Long montant) {
////		int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
//		if(montant!=null)
//			return new Date(montant);
//		else
//			return null;
//	}
//	
//	
//	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscription(String subscriptionStripeID, Integer limite) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Map<String, Object> invoiceParams = new HashMap<String, Object>();
//			if(limite!=null) {
//				invoiceParams.put("limit", LibConversion.integerToString(limite));
//			}
//			invoiceParams.put("subscription", subscriptionStripeID);
//
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour la subscription : "+subscriptionStripeID);
//			InvoiceCollection invoiceCollection = Invoice.list(invoiceParams);
//			
//			List<TaStripeInvoiceDTO> result = new ArrayList<>();
//			TaStripeInvoiceDTO taStripeInvoiceDTO = null;
//			for (Invoice inv : invoiceCollection.getData()) {
//				taStripeInvoiceDTO = new TaStripeInvoiceDTO();
//				taStripeInvoiceDTO.setIdExterne(inv.getId());
//				result.add(taStripeInvoiceDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updateInvoiceFromStripe(inv, subscriptionStripeID);
//				}
//			}
//			
//			
////			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return result;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public TaStripeInvoice updateInvoiceFromStripe(String invoiceStripeID, String subscriptionStripeID) {
//		try {
//			initCleStripe();
//			
//			Invoice c = Invoice.retrieve(invoiceStripeID);//"in_1DuzK8ESo4ojJRjqZB51noQ3"
//			return updateInvoiceFromStripe(c,subscriptionStripeID);
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	private TaStripeInvoice updateInvoiceFromStripe(Invoice inv, String subscriptionStripeID) {
//		//récupération et enregistrement des informations de Stripe vers Bdg
//		TaStripeInvoice taStripeInvoice = null;
//		try {
//			taStripeInvoice = taStripeInvoiceService.findByCode(inv.getId());
//			if(taStripeInvoice==null) {
//				taStripeInvoice = new TaStripeInvoice();
//				taStripeInvoice.setIdExterne(inv.getId());
//				taStripeInvoice.setItems(new HashSet<>());
//			}
//			taStripeInvoice.setAmountDue(montantStripeVersBdg(inv.getAmountDue()));
//			taStripeInvoice.setNumber(inv.getNumber());
//			taStripeInvoice.setDueDate(dateStripeVersBdg(inv.getDueDate()));
//			taStripeInvoice.setCreated(dateStripeVersBdg(inv.getCreated()));
//			if(inv.getCharge()!=null) {
//				//taStripeInvoice.setTaStripeCharge(taStripeChargeService.);
//			}
//			taStripeInvoice.setPaid(inv.getPaid());
//			taStripeInvoice.setPeriodStart(dateStripeVersBdg(inv.getPeriodStart()));
//			taStripeInvoice.setPeriodEnd(dateStripeVersBdg(inv.getPeriodEnd()));
//			
//			taStripeInvoice.setAttemptCount(inv.getAttemptCount().intValue());
//			taStripeInvoice.setAttempted(inv.getAttempted());
//			taStripeInvoice.setBilling(inv.getBilling());
//			taStripeInvoice.setBillingReason(inv.getBillingReason());
//			taStripeInvoice.setDescription(inv.getDescription());
//			taStripeInvoice.setNexPaymentAttempt(dateStripeVersBdg(inv.getNextPaymentAttempt()));
//			taStripeInvoice.setWebhooksDeliveredAt(dateStripeVersBdg(inv.getWebhooksDeliveredAt()));
//	//		if(inv.getDiscount()!=null) {
//	//			taStripeInvoice.setTaStripeCoupon(inv.getcPeriodEnd());
//	//		}
//			taStripeInvoice.setTaxe(inv.getTaxPercent());
//			//taStripeInvoice.setStatus(inv.getStatus());// mettre à jour l'API vers 7.18.0 ?
//			//TaAvisEcheance id
//			//TaReglement id
//			
//			for (InvoiceLineItem invItem : inv.getLines().getData()) {
//				TaStripeInvoiceItem taStripeInvoiceItem = taStripeInvoiceItemService.findByCode(invItem.getId());
//				if(taStripeInvoiceItem==null) {
//					taStripeInvoiceItem = new TaStripeInvoiceItem();
//					taStripeInvoiceItem.setIdExterne(invItem.getId());
//					taStripeInvoiceItem.setTaStripeInvoice(taStripeInvoice);
//				}
//				
//				taStripeInvoiceItem.setTaPlan(taStripePlanService.findByCode(invItem.getPlan().getId()));
//
//				taStripeInvoice.getItems().add(taStripeInvoiceItem);
//			}
//			
//			if(subscriptionStripeID!=null) {
//				taStripeInvoice.setTaSubscription(taStripeSubscriptionService.findByCode(subscriptionStripeID));
//			} else if(inv.getSubscription()!=null) {
//				taStripeInvoice.setTaSubscription(taStripeSubscriptionService.findByCode(inv.getSubscription()));
//			}
//			
//			taStripeInvoice.setTaCustomer(taStripeInvoice.getTaSubscription().getTaStripeCustomer());
//			
//			taStripeInvoice = taStripeInvoiceService.merge(taStripeInvoice);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return taStripeInvoice;
//	}
//	
//	public List<TaStripeInvoiceDTO>rechercheInvoiceStripeForCustomer(String customerStripeID, Integer limite) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Map<String, Object> invoiceParams = new HashMap<String, Object>();
//			if(limite!=null) {
//				invoiceParams.put("limit", LibConversion.integerToString(limite));
//			}
//			invoiceParams.put("customer", customerStripeID);
//			
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour le customer : "+customerStripeID);
//			InvoiceCollection invoiceCollection = Invoice.list(invoiceParams);
//			
//			List<TaStripeInvoiceDTO> result = new ArrayList<>();
//			TaStripeInvoiceDTO taStripeInvoiceDTO = null;
//			for (Invoice inv : invoiceCollection.getData()) {
//				taStripeInvoiceDTO = new TaStripeInvoiceDTO();
//				taStripeInvoiceDTO.setIdExterne(inv.getId());
//				result.add(taStripeInvoiceDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updateInvoiceFromStripe(inv, null);
//				}
//			}
//			
////			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return result;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public List<TaStripeSubscriptionDTO>rechercheSubscriptionStripeForCustomer(String customerStripeID, Integer limite, String status) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Map<String, Object> subscriptionParams = new HashMap<String, Object>();
//			if(limite!=null) {
//				subscriptionParams.put("limit", LibConversion.integerToString(limite));
//			}
//			if(status!=null) {
//				subscriptionParams.put("status", status); //trialing, active, past_due, unpaid, canceled, or all
//			}
//			
//			subscriptionParams.put("customer", customerStripeID);
//			
//			//System.out.println("LgrStripe ** Recherche des subscriptions Stripe - pour le customer : "+customerStripeID);
//			SubscriptionCollection subscriptionCollection = Subscription.list(subscriptionParams);
//			
//			List<TaStripeSubscriptionDTO> result = new ArrayList<>();
//			TaStripeSubscriptionDTO taStripeSubscriptionDTO = null;
//			for (Subscription sub : subscriptionCollection.getData()) {
//				taStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//				taStripeSubscriptionDTO.setIdExterne(sub.getId());
//				result.add(taStripeSubscriptionDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updateSubscriptionFromStripe(sub);
//				}
//			}
//			
////			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return result;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public TaStripeSubscription updateSubscriptionFromStripe(String subscriptionStripeID) {
//		try {
//			initCleStripe();
//			
//			Subscription c = Subscription.retrieve(subscriptionStripeID);//"sub_ENkpQA7eMGNU3U"
//			return updateSubscriptionFromStripe(c);
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	private TaStripeSubscription updateSubscriptionFromStripe(Subscription sub) {
//		//récupération et enregistrement des informations de Stripe vers Bdg
//		TaStripeSubscription taStripeSubscription = null;
//		try {
//			taStripeSubscription = taStripeSubscriptionService.findByCode(sub.getId());
//			if(taStripeSubscription==null) {
//				taStripeSubscription = new TaStripeSubscription();
//				taStripeSubscription.setIdExterne(sub.getId());
//				taStripeSubscription.setItems(new HashSet<>());
//			}
//			taStripeSubscription.setStatus(sub.getStatus());
//			taStripeSubscription.setBilling(sub.getBilling());
////			taStripeSubscription.setDaysUntilDue(sub.getDaysUntilDue());
//			
//			for (SubscriptionItem subItem : sub.getSubscriptionItems().getData()) {
//				TaStripeSubscriptionItem taStripeSubscriptionItem = taStripeSubscriptionItemService.findByCode(subItem.getId());
//				if(taStripeSubscriptionItem==null) {
//					taStripeSubscriptionItem = new TaStripeSubscriptionItem();
//					taStripeSubscriptionItem.setIdExterne(subItem.getId());
//					taStripeSubscriptionItem.setTaStripeSubscription(taStripeSubscription);
//					if(sub.getQuantity()!=null) {
//						taStripeSubscriptionItem.setQuantity(sub.getQuantity().intValue());
//					}
//					if(sub.getPlan()!=null) {
//						taStripeSubscriptionItem.setTaPlan(taStripePlanService.findByCode(sub.getPlan().getId()));
//					}
//					taStripeSubscription.getItems().add(taStripeSubscriptionItem);
//				}
//				
//				taStripeSubscription.setTaStripeCustomer(taStripeSubscription.getTaStripeCustomer());
//				
//			}
//			
//			taStripeSubscription = taStripeSubscriptionService.merge(taStripeSubscription);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return taStripeSubscription;
//	}
//	
//	public TaStripeCharge updateChargeFromStripe(String chargeStripeID) {
//		try {
//			initCleStripe();
//			
//			Charge c = Charge.retrieve(chargeStripeID);//"ch_Dr8U8upUHIEQwG"
//			return updateChargeFromStripe(c);
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	private TaStripeCharge updateChargeFromStripe(Charge ch) {
//		//récupération et enregistrement des informations de Stripe vers Bdg
//		TaStripeCharge taStripeCharge = null;
//		try {
//			taStripeCharge = taStripeChargeService.findByCode(ch.getId());
//			if(taStripeCharge==null) {
//				taStripeCharge = new TaStripeCharge();
//				taStripeCharge.setIdExterne(ch.getId());
////				taStripeCharge.setItems(new HashSet<>());
//			}
//			taStripeCharge.setStatus(ch.getStatus());
//			taStripeCharge.setAmount(montantStripeVersBdg(ch.getAmount()));
//			if(ch.getCustomer()!=null) {
//				taStripeCharge.setTaStripeCustomer(taStripeCustomerService.rechercherCustomer(ch.getCustomer()));
//			}
//			if(ch.getInvoice()!=null) {
//				taStripeCharge.setTaStripeInvoice(taStripeInvoiceService.findByCode(ch.getInvoice()));
//			}
//			if(ch.getSource()!=null && ch.getSource().getId()!=null) {
//				taStripeCharge.setTaStripeSource(taStripeSourceService.rechercherSource(ch.getSource().getId()));
//			}
//			taStripeCharge.setPaid(ch.getPaid());
//			taStripeCharge.setDescription(ch.getDescription());
//			taStripeCharge.setRefunded(ch.getRefunded());
//			
//			taStripeCharge = taStripeChargeService.merge(taStripeCharge);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return taStripeCharge;
//	}
//	
//	public TaStripePlan updatePlanFromStripe(String planStripeID) {
//		
//		try {
//			initCleStripe();
//			
//			Plan c = Plan.retrieve(planStripeID);//"cus_Dr8U8upUHIEQwG"
//			return updatePlanFromStripe(c);
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	private TaStripePlan updatePlanFromStripe(Plan pl) {
//		//récupération et enregistrement des informations de Stripe vers Bdg
//		TaStripePlan taStripePlan = null;
//		try {
//			taStripePlan = taStripePlanService.findByCode(pl.getId());
//			if(taStripePlan==null) {
//				taStripePlan = new TaStripePlan();
//				taStripePlan.setIdExterne(pl.getId());
////				taStripeCharge.setItems(new HashSet<>());
//			}
//			taStripePlan.setActive(pl.getActive());
////			taStripePlan.setDeleted(pl.getDeleted());
//			if(pl.getProduct()!=null) {
//				taStripePlan.setTaStripeProduct(taStripeProductService.rechercherProduct(pl.getProduct()));
//			}
//			taStripePlan = taStripePlanService.merge(taStripePlan);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return taStripePlan;
//	}
//	public List<TaStripeChargeDTO>rechercheChargeStripe(Integer limite, Map<String,String> sourceType) throws EJBException {
//		return rechercheChargeStripeForCustomer(null,limite,sourceType);
//	}
//	
//	public List<TaStripeChargeDTO>rechercheChargeStripeForCustomer(String customerStripeID, Integer limite, Map<String,String> sourceType) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Map<String, Object> chargeParams = new HashMap<String, Object>();
//			if(limite!=null) {
//				chargeParams.put("limit", LibConversion.integerToString(limite));
//			}
//			if(sourceType!=null) {
//				chargeParams.put(" source", LibConversion.integerToString(limite)); //all, alipay_account, bank_account, bitcoin_receiver, or card
//			}
//			if(customerStripeID!=null) {
//				chargeParams.put("customer", customerStripeID);
//			}
//			
//			//System.out.println("LgrStripe ** Recherche des subscriptions Stripe - pour le customer : "+customerStripeID);
//			ChargeCollection chargeCollection = Charge.list(chargeParams);
//			
//			List<TaStripeChargeDTO> result = new ArrayList<>();
//			TaStripeChargeDTO taStripeChargeDTO = null;
//			for (Charge ch : chargeCollection.getData()) {
//				taStripeChargeDTO = new TaStripeChargeDTO();
//				taStripeChargeDTO.setIdExterne(ch.getId());
//				result.add(taStripeChargeDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updateChargeFromStripe(ch);
//				}
//			}
//			
////			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return result;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String creerCouponStripe(String duration, Integer durationInMonths, String id, BigDecimal percentOff/*, BigDecimal amountOff*/) throws EJBException{
//		try {
//			initCleStripe();
//			
////			Map<String, Object> couponParams = new HashMap<String, Object>();
////			couponParams.put("percent_off", 25);
////			couponParams.put("duration", "repeating");
////			couponParams.put("duration_in_months", 3);
////			couponParams.put("id", "25OFF");
//
//			
//			Map<String, Object> couponParams = new HashMap<String, Object>();
//			if(duration!=null) {
//				couponParams.put("duration", "repeating");//"obligatoire : forever, once, or repeating
//			}
//			if(durationInMonths!=null) {
//				couponParams.put("duration_in_months", 3); // 3 mois
//			}
//			if(id!=null) {
//				couponParams.put("id", id); //"25OFF"
//			}
//			if(percentOff!=null) {
//				couponParams.put("percent_off", percentOff); //25
//			}
////			if(amountOff!=null) {
////				couponParams.put("amount_off", amountOff); 
////			}
//			
//			System.out.println("LgrStripe ** Création d'une coupon Stripe ");
//			Coupon coupon = Coupon.create(couponParams);
//			
//			System.out.println("LgrStripe ** Création d'un coupon Stripe - ID : "+coupon.getId());
//
//
//			return coupon.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public String payerParPrelevementSEPA(BigDecimal montant, String customerStripeID, String sourceStripeID) throws EJBException{
//		return payerAvecSourceStripe(montant,customerStripeID,sourceStripeID,null);
//	}
//	
//	public String payerAvecSourceStripe(BigDecimal montant, String customerStripeID, String sourceStripeID, String description) throws EJBException{
//		try {
//			initCleStripe();
//			
//			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
//			
//			Map<String, Object> chargeParams = new HashMap<String, Object>();
//			chargeParams.put("amount", amount);//1099
//			chargeParams.put("currency", "eur");
//			chargeParams.put("customer", customerStripeID);//"cus_AFGbOSiITuJVDs"
//			chargeParams.put("source", sourceStripeID);//"src_18eYalAHEMiOZZp1l9ZTjSU0"
//			if(description!=null) {
//				chargeParams.put("description", description);
//			}
//			/*
//			 By default, your account’s statement descriptor appears on customer statements whenever you create a SEPA Direct Debit payment. 
//			 If you need to provide a custom description for a payment, include the statement_descriptor parameter when making a charge request. 
//			 Statement descriptors are limited to 22 characters and cannot use the special characters <, >, ', or ".
//			 */
//			
//			System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - montant : "+montant);
//			Charge charge = Charge.create(chargeParams);
//			
//			System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - ID : "+charge.getId());
//			System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - customer : "+customerStripeID);
//			System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - source : "+sourceStripeID);
//			
//			if(!charge.getStatus().equals("pending")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
////			if(!charge.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
//
//			return charge.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public TaStripeInvoiceDTO upcomingInvoiceForCustomer(String customerStripeID) {
//		try {
//			initCleStripe();
//			
//			Map<String, Object> invoiceParams = new HashMap<String, Object>();
//			invoiceParams.put("customer", customerStripeID); //"cus_EQMUyvcFYsEJZI"
//			Invoice.upcoming(invoiceParams);
//			
//			TaStripeInvoiceDTO a = new TaStripeInvoiceDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public List<TaStripePlanDTO>recherchePlanStripe(Integer limite, Boolean active) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Map<String, Object> planParams = new HashMap<String, Object>();
//			if(limite!=null) {
//				planParams.put("limit", LibConversion.integerToString(limite));
//			}
//			if(active!=null) {
//				planParams.put("active", active);
//			}
////			planParams.put("customer", customerStripeID);
//			
////			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour le customer : "+customerStripeID);
//			PlanCollection planCollection = Plan.list(planParams);
//			
//			List<TaStripePlanDTO> result = new ArrayList<>();
//			TaStripePlanDTO taStripePlanDTO = null;
//			for (Plan pl : planCollection.getData()) {
//				taStripePlanDTO = new TaStripePlanDTO();
//				taStripePlanDTO.setIdExterne(pl.getId());
//				result.add(taStripePlanDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updatePlanFromStripe(pl);
//				}
//			}
//
//			return result;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public List<TaStripePlanDTO>recherchePlanStripeForProduct(String productStripeID, Integer limite, Boolean active) throws EJBException {
//		try {
//			initCleStripe();
//				
//			Map<String, Object> planParams = new HashMap<String, Object>();
//			if(limite!=null) {
//				planParams.put("limit", LibConversion.integerToString(limite));
//			}
//			if(active!=null) {
//				planParams.put("active", active);
//			}
//			if(productStripeID!=null) {
//				planParams.put("product", productStripeID);
//			}
//			
////			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour le customer : "+customerStripeID);
//			PlanCollection planCollection = Plan.list(planParams);
//			
//			List<TaStripePlanDTO> result = new ArrayList<>();
//			TaStripePlanDTO taStripePlanDTO = null;
//			for (Plan pl : planCollection.getData()) {
//				taStripePlanDTO = new TaStripePlanDTO();
//				taStripePlanDTO.setIdExterne(pl.getId());
//				result.add(taStripePlanDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updatePlanFromStripe(pl);
//				}
//			}
//
//			return result;
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//	
//	public TaStripeInvoiceDTO payerInvoice(String invoiceStripeID, Boolean  paidOutOfBand ) {
//		try {
//			initCleStripe();
//			Map<String, Object> payParams = new HashMap<String, Object>();
//			if(paidOutOfBand!=null) {
//				payParams.put("paid_out_of_band", paidOutOfBand); //true pour les paiements fait en dehors de stripe (chèque, virement, ...)
//			}
//			
//			Invoice invoice = Invoice.retrieve(invoiceStripeID);//"in_1DuzK8ESo4ojJRjqZB51noQ3"
//			invoice = invoice.pay(payParams);
//			TaStripeInvoiceDTO a = new TaStripeInvoiceDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripeInvoiceDTO rechercherInvoice(String invoiceStripeID) {
//		try {
//			initCleStripe();
//			
//			Invoice c = Invoice.retrieve(invoiceStripeID);//"in_1DuzK8ESo4ojJRjqZB51noQ3"
//			TaStripeInvoiceDTO a = new TaStripeInvoiceDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripeSubscriptionDTO rechercherSubscription(String subsciprtionStripeID) {
//		try {
//			initCleStripe();
//			
//			Subscription c = Subscription.retrieve(subsciprtionStripeID);//"sub_ENkpQA7eMGNU3U"
//			TaStripeSubscriptionDTO a = new TaStripeSubscriptionDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripeCustomerDTO rechercherCustomer(String customerStripeID) {
//		try {
//			initCleStripe();
//			
//			Customer c = Customer.retrieve(customerStripeID);//"cus_Dr8U8upUHIEQwG"
//			TaStripeCustomerDTO a = new TaStripeCustomerDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripeChargeDTO rechercherCharge(String chargeStripeID) {
//		try {
//			initCleStripe();
//			
//			Charge c = Charge.retrieve(chargeStripeID);//"ch_Dr8U8upUHIEQwG"
//			TaStripeChargeDTO a = new TaStripeChargeDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripePlanDTO rechercherPlan(String planStripeID) {
//		try {
//			initCleStripe();
//			
//			Plan c = Plan.retrieve(planStripeID);//"cus_Dr8U8upUHIEQwG"
//			TaStripePlanDTO a = new TaStripePlanDTO();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripeProductDTO rechercherProduct(String productStripeID) {
//		try {
//			initCleStripe();
//			
//			Product c = Product.retrieve(productStripeID);//"prod_E3vbcn8BqJvbDO"
//			TaStripeProductDTO a = new TaStripeProductDTO();
//			a.setName(c.getName());
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public TaStripeSourceDTO rechercherSource(String sourceStripeID) {
//		try {
//			initCleStripe();
//			
//			Source s = Source.retrieve(sourceStripeID);//"src_1DPQEI2eZvKYlo2C8VeUif3c"
//			TaStripeSourceDTO a = new TaStripeSourceDTO();
//			a.setIdExterne(s.getId());
//			if(s.getType().equals("card")) {
//				System.out.println("LgrStripe.rechercherSource() - address_zip_check : "+s.getTypeData().get("address_zip_check"));
//				System.out.println("LgrStripe.rechercherSource() - country : "+s.getTypeData().get("country"));
//				System.out.println("LgrStripe.rechercherSource() - last4 : "+s.getTypeData().get("last4"));
//				System.out.println("LgrStripe.rechercherSource() - funding : "+s.getTypeData().get("funding"));
//				System.out.println("LgrStripe.rechercherSource() - cvc_check : "+s.getTypeData().get("cvc_check"));
//				System.out.println("LgrStripe.rechercherSource() - fingerprint : "+s.getTypeData().get("fingerprint"));
//				System.out.println("LgrStripe.rechercherSource() - exp_month : "+s.getTypeData().get("exp_month"));
//				System.out.println("LgrStripe.rechercherSource() - exp_year : "+s.getTypeData().get("exp_year"));
//				System.out.println("LgrStripe.rechercherSource() - three_d_secure : "+s.getTypeData().get("three_d_secure"));
//				System.out.println("LgrStripe.rechercherSource() - brand : "+s.getTypeData().get("brand"));
//				TaCarteBancaireDTO taCarteBancaireDTO = new TaCarteBancaireDTO();
//				taCarteBancaireDTO.setLast4(s.getTypeData().get("last4"));
//				taCarteBancaireDTO.setType(s.getTypeData().get("brand"));
//				taCarteBancaireDTO.setMoisExpiration(LibConversion.stringToInteger(s.getTypeData().get("exp_month")));
//				taCarteBancaireDTO.setAnneeExpiration(LibConversion.stringToInteger(s.getTypeData().get("exp_year")));
//				a.setTaCarteBancaireDTO(taCarteBancaireDTO);
//			} else if(s.getType().equals("sepa_debit")) {
//				System.out.println("LgrStripe.rechercherSource() - branch_code : "+s.getTypeData().get("branch_code"));
//				System.out.println("LgrStripe.rechercherSource() - bank_code : "+s.getTypeData().get("bank_code"));
//				System.out.println("LgrStripe.rechercherSource() - country : "+s.getTypeData().get("country"));
//				System.out.println("LgrStripe.rechercherSource() - last4 : "+s.getTypeData().get("last4"));
//				System.out.println("LgrStripe.rechercherSource() - mandate_url : "+s.getTypeData().get("mandate_url"));
//				System.out.println("LgrStripe.rechercherSource() - fingerprint : "+s.getTypeData().get("fingerprint"));
//				System.out.println("LgrStripe.rechercherSource() - mandate_reference : "+s.getTypeData().get("mandate_reference"));
//				TaCompteBanqueDTO taCompteBanqueDTO = new TaCompteBanqueDTO();
//				taCompteBanqueDTO.setBranchCode(s.getTypeData().get("branch_code"));
//				taCompteBanqueDTO.setBankCode(s.getTypeData().get("bank_code"));
//				taCompteBanqueDTO.setCountry(s.getTypeData().get("country"));
//				taCompteBanqueDTO.setLast4(s.getTypeData().get("last4"));
//				a.setTaCompteBanqueDTO(taCompteBanqueDTO);
//			}
//			
////			a.setIdExterne(s.getId());
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String 
//		return null;
//	}
//	
//	public Boolean supprimerPlan(String planStripeID) {
//		try {
//			initCleStripe();
//			
//			Plan c = Plan.retrieve(planStripeID);//"plan_EOZsEvcw9TpxGY"
//			c = c.delete();
//			return c.getDeleted();
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public Boolean supprimerProduct(String productStripeID) {
//		try {
//			initCleStripe();
//			
//			Product c = Product.retrieve(productStripeID);//"prod_EPKCCBb7klsk9z"
//			c = c.delete();
//			return c.getDeleted();
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public Boolean supprimerCustomer(String customerStripeID) {
//		try {
//			initCleStripe();
//			
//			Customer c = Customer.retrieve(customerStripeID);//"cus_EOZsEvcw9TpxGY"
//			c = c.delete();
//			return c.getDeleted();
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public Boolean supprimerCoupon(String couponStripeID) {
//		try {
//			initCleStripe();
//			
//			Coupon c = Coupon.retrieve(couponStripeID);//"XXXXXXXXXXXX"
//			c = c.delete();
//			return c.getDeleted();
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String sourceStripeID
//		return null;
//	}
//	
//	public String refundCharge(String chargeStripeID, BigDecimal montant) {
//		
//		try {
//			initCleStripe();
//			
//			Map<String, Object> params = new HashMap<>();
//			params.put("charge", chargeStripeID); //"ch_ZslYWOaA7ielnGe40n7g"
//			if(montant!=null) {
//				params.put("amount", montantBdgVersStripe(montant));
//			}
//			
//			Refund refund = Refund.create(params);
//			System.out.println("LgrStripe ** Création d'un remboursement Stripe - pour le paiement : "+chargeStripeID);
//			System.out.println("LgrStripe ** Création d'un remboursement Stripe : "+refund.getId());
//			return refund.getId();
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
//	
//	public TaStripeAccount rechercherAccount(String accountStripeID) {
//		try {
//			initCleStripe();
//			
//			Account s = Account.retrieve(accountStripeID,null);//"acct_16gu0lESo4ojJRjq"
//			TaStripeAccount a = new TaStripeAccount();
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String 
//		return null;
//	}
//	
//	public Account creerAccount(String email) {
//		try {
//			initCleStripe();
//			
//			Map<String, Object> accountParams = new HashMap<String, Object>();
//			accountParams.put("type", "custom");
//			accountParams.put("country", "FR");
//			accountParams.put("email", email);
//
//			Account a = Account.create(accountParams);
////			a.getKeys()
//			return a;
//		} catch (AuthenticationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIConnectionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (CardException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////		} catch (APIException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (StripeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//, String 
//		return null;
//	}
//	
//	public String creerWebhook(String url, String enabledEvents) throws EJBException{
//		try {
//			initCleStripe();
//			return null;
//			
//////			Map<String, Object> webhookendpointParams = new HashMap<String, Object>();
//////			webhookendpointParams.put("url", "https://example.com/my/webhook/endpoint");
//////			webhookendpointParams.put("enabled_events", ["charge.failed", "charge.succeeded"]);
////			
////			Map<String, Object> webhookendpointParams = new HashMap<String, Object>();
////			webhookendpointParams.put("url", url);
////			webhookendpointParams.put("enabled_events", enabledEvents);//["charge.failed", "charge.succeeded"]
////
////			
////			System.out.println("LgrStripe ** Création d'un webhook Stripe : "+enabledEvents);
////			WebhookEndpoint webhook = WebhookEndpoint.create(webhookendpointParams);
////			
////			System.out.println("LgrStripe ** Création d'un webhook Stripe - ID : "+webhook.getId());
//
////			if(!charge.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////
////			return webhook.getId();
//		} catch (AuthenticationException e) {
//			// Authentication with Stripe's API failed
//            // (maybe you changed API keys recently)
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (InvalidRequestException e) {
//			// Invalid parameters were supplied to Stripe's API
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
////		} catch (APIConnectionException e) {
////			// Network communication with Stripe failed
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (CardException e) {
//			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
//				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
//				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
//				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
//				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		} catch (Exception e) {
//			// Something else happened, completely unrelated to Stripe
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//		}
//	}
//
//	@Override
//	public TaStripePlanDTO updatePlan(String planStripeID, String nickname, Boolean active) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeProductDTO updateProduct(String productStripeID, String name, Boolean active) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeCustomerDTO updateCustomer(String customerStripeID, String email, String sourceDefaultId,
//			String description, String couponId, String invoicePrefix) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeCouponDTO updateCoupon(String couponStripeID, String name) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionDTO updateSubscription(String subscriptionStripeID, String billing,
//			String billing_cycle_anchor, String coupon, String days_until_due, String default_source) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	/*
	 * api.gwi-hosting.fr/src/Legrain/ApiBundle/Services/GwiHostingService.php
	 * 
	 * 
	 * 
	     case 'card':
                // Si gestionnaire ou si l'agence ne facture pas
                if ($userConnected->getParent() == null || $userConnected->getAgency()->getFacturationBylegrain()) {

                    // On loade l'agence Legrain (1)
                    $agency = $agencyRepository->find(1);
                } else {
                    $agency = $userConnected->getAgency();
                }
                if ($agency->getStripeKey() == null) throw new \SoapFault('error', 'Aucun compte stripe n\'est parametré');
                $description = 'Rechargement compte pré payé';
                // Appel stripe ( clef publique à parametrer)
                // \Stripe\Stripe::setApiKey('sk_test_gSTXDQxFFZiiBdKOGlrcLZWh');
                try {
                    \Stripe\Stripe::setApiKey($agency->getStripeKey());
                    $myCard = array('number' => $cardNumber, 'exp_month' => $cardExpirationMonth, 'exp_year' => $cardExpirationYear, 'cvc' => $cvc, 'name' => $cardFullName);
                    // Amount en centime
                    $charge = \Stripe\Charge::create(array('card' => $myCard, 'amount' => ($amount * 100), 'currency' => 'eur'));
                } catch (\Stripe\Error\Card $e) {
                    // Since it's a decline, \Stripe\Error\Card will be caught
                    $body = $e->getJsonBody();
                    $err = $body['error'];
                    switch ($err['code']) {

                        case 'incorrect_cvc':
                            $error = "le code de sécurité est incorrect";
                            break;
                        case 'expired_card':
                            $error = "la carte a expiré";
                            break;
                        case 'invalid_cvc':
                            $error = "Le code de sécurité de la carte est invalide";
                            break;
                        case 'invalid_expiry_month':
                            $error = "Le mois d'expiration de la carte est invalide";
                            break;
                        case 'invalid_expiry_year':
                            $error = "L'année d'expiration de la carte est invalide";
                            break;
                        case 'invalid_number':
                        case 'incorrect_number':
                            $error = 'Le numéro de carte saisie est invalide';
                            break;
                        default:
                            $error = 'Une erreur est survenue, rééssayez ultérieurement';
                            break;
                    }
                    throw new \SoapFault('server', $error);
                } catch (\Stripe\Error\InvalidRequest $e) {
                    // Invalid parameters were supplied to Stripe's API
                    throw new \SoapFault('server', 'Une erreur est survenue, merci de contacter votre agence');

                } catch (\Stripe\Error\Authentication $e) {
                    // Authentication with Stripe's API failed
                    // (maybe you changed API keys recently)
                    throw new \SoapFault('server', 'Une erreur est survenue, merci de contacter votre agence');

                } catch (\Stripe\Error\ApiConnection $e) {
                    // Network communication with Stripe failed
                    throw new \SoapFault('server', 'Une erreur est survenue, merci de contacter votre agence');

                } catch (\Stripe\Error\Base $e) {
                    // Display a very generic error to the user, and maybe send
                    // yourself an email
                    throw new \SoapFault('server', 'Une erreur est survenue, merci de contacter votre agence');

                } catch (\Exception $e) {
                    // Something else happened, completely unrelated to Stripe
                    throw new \SoapFault('server', 'Une erreur est survenue, merci de contacter votre agence');

                }
                if ($charge->status != "succeeded") throw new \SoapFault('error', 'Erreur au moment du paiement');
                if (!$charge->paid) throw new \SoapFault('error', 'Erreur au moment du paiement');
                $idTransactionExterne = $charge->id;
	 */
}
