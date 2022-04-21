package fr.legrain.paiement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.json.JSONObject;

import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.PermissionException;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Charge;
import com.stripe.model.ChargeCollection;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceItem;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Plan;
import com.stripe.model.PlanCollection;
import com.stripe.model.Product;
import com.stripe.model.Refund;
import com.stripe.model.SetupIntent;
import com.stripe.model.Source;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.SubscriptionItem;
import com.stripe.model.oauth.TokenResponse;
import com.stripe.net.OAuth;
import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.AccountLinkCreateParams.Type;
import com.stripe.param.PaymentIntentCaptureParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SubscriptionCancelParams;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.service.TaStripeBankAccountService;
import fr.legrain.abonnement.stripe.service.TaStripePaymentIntentService;
import fr.legrain.autorisations.ws.FindByCode;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeBankAccountServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCouponServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceItemServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaymentIntentServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.generation.service.remote.IAbstractGenereDocServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCarteBancaireServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.dto.IDocumentReglementLiable;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaRReglementLiaison;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IFactureDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.generation.service.CreationDocumentMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.model.TaLogPaiementInternet;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@PaiementLgr(PaiementLgr.TYPE_STRIPE)
public class PaiementStripeDossierService implements IPaiementEnLigneDossierService {
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	@EJB private CreationDocumentMultiple creationDocumentMultiple;
	
	private @Inject ILogPaiementInternetDAO logPaiementInternetDAO;
	private @Inject IFactureDAO factureDAO;
	
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaRReglementServiceRemote taRReglementService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;
	private @EJB ITaFactureServiceRemote taFactureService;
	private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaBoncdeServiceRemote taBoncdeService;
	private @EJB ITaAcompteServiceRemote taAcompteService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaEtatServiceRemote taEtatService;
	
	private @EJB ITaTTiersServiceRemote taTTiersService;
	
	@EJB protected LgrEnvironnementServeur lgrEnvironnementServeur;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	
	protected @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	protected @EJB ITaStripeAccountServiceRemote taStripeAccountService;
	private @EJB ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaStripeProductServiceRemote taStripeProductService;
	private @EJB ITaStripePlanServiceRemote taStripePlanService;
	private @EJB ITaStripeCouponServiceRemote taStripeCouponService;
	private @EJB ITaCarteBancaireServiceRemote taCarteBancaireService;
	
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeInvoiceServiceRemote taStripeInvoiceService;
	private @EJB ITaStripeInvoiceItemServiceRemote taStripeInvoiceItemService;
	private @EJB ITaStripeChargeServiceRemote taStripeChargeService;
	private @EJB ITaStripePaymentIntentServiceRemote taStripePaymentIntentService;
	
	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	
	private boolean synchroniserStripeVersBdg = true;
	
	private TransactionSynchronizationRegistry reg;

	private void initTenant(String tenant) {
		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
		tenantInfo.setTenantId(tenant);
	}

	private void initTenantRegistry() {
		try {
			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
//	@Override
//	public String payer(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String description) throws EJBException{
//		return payer(montant, numeroCarte,moisCarte,anneeCarte,cryptogrammeCarte,null,description);
//	}
//	
//	@Override
//	public String payer(BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description) throws EJBException{
//		System.out.println("EnvoiSMSMailjetDossierService.payer()");
//		return "stripe";
//	}
	
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaTiers taTiers, String libelle)
			throws EJBException {
		return creerPaymentIntentSWTDocument(configService, paiementCarteBancaire, null, taTiers, libelle);
	}
	
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, String libelle)
			throws EJBException {
		return creerPaymentIntent(configService, paiementCarteBancaire, documentPayableCB, null, libelle);
	}
	
	/**
	 * @deprecated Rendre tous les documents IDocumentPayableCB 
	 * ou trouver un moyen de lier un paiement à un document "non payable" (autre table TaRReglement en parallele des "vrai" règlement ?)
	 */
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaAvisEcheance taAvisEcheance, String libelle)
			throws EJBException {
		return creerPaymentIntentSWTDocument(configService, paiementCarteBancaire, taAvisEcheance, null, libelle);
	}
	
	/**
	 * @deprecated Rendre tous les documents IDocumentPayableCB 
	 * ou trouver un moyen de lier un paiement à un document "non payable" (autre table TaRReglement en parallele des "vrai" règlement ?)
	 */
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaPanier taPanier, String libelle)
			throws EJBException {
		return creerPaymentIntentSWTDocument(configService, paiementCarteBancaire, taPanier, null, libelle);
	}
	
	/**
	 * Depuis la réglementation SCA il est recommendé d'utiliser les méthodes  xxxPaymentIntent() plutot que les méthodes payerXXX() 
	 * qui utilisent les anciennes API Stripe Charge et Source
	 * <br>
	 * La création du paymentIntent ne génère pas de règlement dans BDG
	 */
//	@Transactional(value=TxType.REQUIRES_NEW)
	@Transactional(value=TxType.REQUIRED)
	public String creerPaymentIntent(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, TaTiers taTiers, String libelle)
			throws EJBException {
		
		System.out.println("PaiementStripeDossierService.creerPaymentIntent() TYPE_STRIPE");
		BdgProperties bdgProperties = new BdgProperties();
		
//		throw new EJBException("le code de sécurité est incorrect");
		
		String TYPE_PAIEMENT_DEFAUT = "CB";
		
		TaPreferences prefCB;
		try {
			prefCB = taPreferencesService.findByCode("paiement_cb");
			if(prefCB!=null) TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TaLogPaiementInternet log = new TaLogPaiementInternet();
		Date datePaiement = new Date();
		
		if(configService!=null) {
			UtilServiceWebExterne util = new UtilServiceWebExterne();
			configService = util.decrypter(configService);
			
			System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
			System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
		}
			
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
//			boolean modeTest = false;
//			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
//			
//			//Stripe.apiKey = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
////			String API_KEY_DOSSIER = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
//			String API_KEY_DOSSIER = configService.getApiKey1();
			
			BigDecimal montant = paiementCarteBancaire.getMontantPaiement();
			String numeroCarte = paiementCarteBancaire.getNumeroCarte();
			int moisCarte = paiementCarteBancaire.getMoisCarte();
			int anneeCarte = paiementCarteBancaire.getAnneeCarte();
			String cryptogrammeCarte = paiementCarteBancaire.getCryptogrammeCarte();
			String nomPorteurCarte = paiementCarteBancaire.getNomPorteurCarte();
			String description = paiementCarteBancaire.getDescriptionPaiement();
			String devise = paiementCarteBancaire.getDevise();
			if(requestOptions.getStripeAccount()!=null) {
				//on utilise stripe connect
				//if(tenantInfo.getTenantId()!=null && !tenantInfo.getTenantId().equals(LgrStripe.DOSSIER_MAITRE)) {
				if(tenantInfo.getTenantId()!=null && !tenantInfo.getTenantId().equals(paramBdg.getTaParametre().getDossierMaitre())) {
					paiementCarteBancaire.calculFraisPlateform();
				}
			}
			BigDecimal fraisPlateforme = paiementCarteBancaire.getFraisPlateforme();
			boolean capturablePlusTard = paiementCarteBancaire.isCapturablePlusTard();
			
//			if(modeTest) {
			if(lgrEnvironnementServeur.isModeTestPaiementDossier()) {
				System.out.println("** LgrStripe ** mode TEST activé, utilisation d'information de paiement de TEST.");
				//montant = new BigDecimal(2000);
				numeroCarte = "5555555555554444";
				moisCarte = 8;
				anneeCarte = 2025;
				cryptogrammeCarte = "314";
				nomPorteurCarte = "LEGRAIN - mode TEST";
				description = "Paiement Stripe Legrain - mode TEST";
			}
			
			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
			Integer fees = null;
			if(fraisPlateforme!=null) {
				fees = fraisPlateforme.multiply(BigDecimal.valueOf(100)).intValue();
			}
			if(devise==null) {
				devise = "eur";
			}
			
			Map<String, Object> paymentintentParams = new HashMap<String, Object>();
			paymentintentParams.put("amount", amount);
			paymentintentParams.put("currency", devise);
			paymentintentParams.put("description", description);
			if(fees!=null) {
				paymentintentParams.put("application_fee_amount", fees);
			}
			paymentintentParams.put("setup_future_usage", "off_session");
			
			if(paiementCarteBancaire.getEmailTicketRecu()!=null && !paiementCarteBancaire.getEmailTicketRecu().equals("")) {
				paymentintentParams.put("receipt_email", paiementCarteBancaire.getEmailTicketRecu());
			}
			
			if(capturablePlusTard) {
				paymentintentParams.put("application_fee_amount", PaymentIntentCreateParams.CaptureMethod.MANUAL);
			}
			
			ArrayList<String> paymentMethodTypes = new ArrayList<>();
				paymentMethodTypes.add("card");
				paymentintentParams.put(
				    "payment_method_types",
				    paymentMethodTypes
				);
			
			log.setMontantPaiement(montant);
			log.setDevise(devise);
			
			Map<String, String> initialMetadata = new HashMap<String, String>();
			
			if(documentPayableCB!=null) {
				
//				documentPayableCB = findById(documentPayableCB);
				documentPayableCB = findByCode(documentPayableCB);
				
				initialMetadata.put("Numero "+typeDoc(documentPayableCB), documentPayableCB.getCodeDocument()!=null?documentPayableCB.getCodeDocument():".");
				initialMetadata.put("Date "+typeDoc(documentPayableCB), documentPayableCB.getDateDocument()!=null?LibDate.dateToString(documentPayableCB.getDateDocument()):".");
				initialMetadata.put("Montant TTC "+typeDoc(documentPayableCB), documentPayableCB.getMtTtcCalc()!=null?documentPayableCB.getMtTtcCalc().toString():".");
				initialMetadata.put("Code tiers", documentPayableCB.getTaTiers().getCodeTiers()!=null?documentPayableCB.getTaTiers().getCodeTiers():".");
				initialMetadata.put("Nom tiers", documentPayableCB.getTaTiers().getNomTiers()!=null?documentPayableCB.getTaTiers().getNomTiers():".");
				initialMetadata.put("Prenom tiers", (documentPayableCB.getTaTiers().getPrenomTiers()!=null && !documentPayableCB.getTaTiers().getPrenomTiers().equals(""))?documentPayableCB.getTaTiers().getPrenomTiers():".");
				if(documentPayableCB.getTaTiers().getTaEntreprise()!=null) {
					initialMetadata.put("Raison sociale", documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise()!=null?documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise():"");
				}
				
				log.setCodeDocument(documentPayableCB.getCodeDocument());
				log.setIdDocument(documentPayableCB.getIdDocument());
				log.setTypeDocument(typeDoc(documentPayableCB));
				log.setIdTiers(documentPayableCB.getTaTiers().getIdTiers());
				log.setCodeTiers(documentPayableCB.getTaTiers().getCodeTiers());
				log.setNomTiers(documentPayableCB.getTaTiers().getNomTiers());
			} else if(taTiers!=null) {
				initialMetadata.put("Code tiers", taTiers.getCodeTiers()!=null?taTiers.getCodeTiers():".");
				initialMetadata.put("Nom tiers", taTiers.getNomTiers()!=null?taTiers.getNomTiers():".");
				initialMetadata.put("Prenom tiers", (taTiers.getPrenomTiers()!=null && !taTiers.getPrenomTiers().equals(""))?taTiers.getPrenomTiers():".");
				if(taTiers.getTaEntreprise()!=null) {
					initialMetadata.put("Raison sociale", taTiers.getTaEntreprise().getNomEntreprise()!=null?taTiers.getTaEntreprise().getNomEntreprise():"");
				}
				
				log.setIdTiers(taTiers.getIdTiers());
				log.setCodeTiers(taTiers.getCodeTiers());
				log.setNomTiers(taTiers.getNomTiers());
			}
			initialMetadata.put("Origine", paiementCarteBancaire.getOriginePaiement()!=null?paiementCarteBancaire.getOriginePaiement():".");
			initialMetadata.put("Dossier", tenantInfo.getTenantId()!=null?tenantInfo.getTenantId():".");
			initialMetadata.put("Serveur", bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)!=null?bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE):".");
			
			
			log.setServicePaiement(PaiementLgr.TYPE_STRIPE);
			log.setOriginePaiement(paiementCarteBancaire.getOriginePaiement());
			
			paymentintentParams.put("metadata", initialMetadata);
			
			Map<String, Object> sourceParams = new HashMap<String, Object>();
			sourceParams.put("number", numeroCarte);
			sourceParams.put("exp_month", moisCarte);
			sourceParams.put("exp_year", anneeCarte);
			sourceParams.put("cvc", cryptogrammeCarte);
			sourceParams.put("name", nomPorteurCarte);
			
//			paymentintentParams.put("source", sourceParams);
			
		
			
			System.out.println("LgrStripe ** paiment Stripe - montant : "+amount);
//			RequestOptions requestOptions = RequestOptions.builder().setApiKey(API_KEY_DOSSIER).build();
			//Charge c = Charge.create(paymentintentParams,requestOptions);
			PaymentIntent pi = PaymentIntent.create(paymentintentParams,requestOptions);
			TaStripePaymentIntent taPaymentIntent = updatePaymentIntentFromStripe(configService, pi);
			
			taPaymentIntent.setNetTtcCalc(montant);
			taPaymentIntent.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
			taPaymentIntent.setConserverCarte(paiementCarteBancaire.isConserverCarte());
			if(documentPayableCB!=null) {
				taPaymentIntent.setTaTiers(documentPayableCB.getTaTiers());
				if(documentPayableCB instanceof TaFacture) {
					taPaymentIntent.setTaFacture((TaFacture)documentPayableCB);
				} else if(documentPayableCB instanceof TaTicketDeCaisse) {
					taPaymentIntent.setTaTicketDeCaisse((TaTicketDeCaisse)documentPayableCB);
				}
			} else if(taTiers!=null) {
				taPaymentIntent.setTaTiers(taTiers);
			}
			taStripePaymentIntentService.merge(taPaymentIntent);
			
			return pi.getClientSecret();
			
//			if(!c.getStatus().equals("succeeded")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!c.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			
//			log.setRefPaiementService(c.getId());
//			log.setDatePaiement(datePaiement);
//			
//			//création du règlement
//			TaReglement r = new TaReglement();
//			r.setNetTtcCalc(montant);
//			r.setCodeDocument(taReglementService.genereCode(null));
//			r.setLibelleDocument("Paiement CB");
//			r.setDateDocument(datePaiement);
//			r.setDateLivDocument(datePaiement);
//			r.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
//			
//			r.setService(PaiementLgr.TYPE_STRIPE);
//			r.setCompteClient(paiementCarteBancaire.isCompteClient());
//			r.setRefPaiement(c.getId());
//			
//			//r = taReglementService.merge(r);
//				
//			//affecter le réglement au document
//			if(documentPayableCB!=null) {	
//				r.setTaTiers(documentPayableCB.getTaTiers());
//				
//				TaRReglement rr = new TaRReglement();
//				rr.setAffectation(montant);
//				rr.setEtat(IHMEtat.integre);
//				//rr.setTaFacture(documentPayableCB);
//				if(documentPayableCB instanceof TaFacture) {
//					rr.setTaFacture((TaFacture) documentPayableCB);
//				} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
//					rr.setTaTicketDeCaisse((TaTicketDeCaisse) documentPayableCB);
//				} else if(documentPayableCB instanceof TaAcompte) {
//					//rr.setTaAcompte(documentPayableCB);
//					throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
//				} 
//				rr.setTaReglement(r);
//				if(documentPayableCB.getTaRReglements()==null) {
//					documentPayableCB.setTaRReglements(new HashSet<>());
//				}
//				documentPayableCB.addRReglement(rr);
//				r.addRReglement(rr);
//			} else if(taTiers!=null) {
//				r.setTaTiers(taTiers);
//			}
//				
//			r = taReglementService.merge(r);
//			
//			if(documentPayableCB!=null) {	
//				//taFacture = taFactureService.merge(taFacture);
////				documentPayableCB = findById(documentPayableCB);
//				documentPayableCB = findByCode(documentPayableCB);
//				
////				if(taFacture.getTaReglement()!=null) {
////					log.setIdReglement(taFacture.getTaReglement().getIdDocument());
////					log.setCodeReglement(taFacture.getTaReglement().getCodeDocument());
////				}
//				if(documentPayableCB.getTaRReglements()!=null && !documentPayableCB.getTaRReglements().isEmpty()) {
//					TaReglement r2 = documentPayableCB.getTaRReglements().iterator().next().getTaReglement();
//					log.setIdReglement(r2.getIdDocument());
//					log.setCodeReglement(r2.getCodeDocument());
//				}
//			} else {
//				if(r!=null) {
//					log.setIdReglement(r.getIdDocument());
//					log.setCodeReglement(r.getCodeDocument());
//				}
//			}
//			 
//			
//			RetourPaiementCarteBancaire retour = new RetourPaiementCarteBancaire();
//			retour.setPaye(c.getPaid());
//			retour.setRefPaiement(c.getId());
//			if(documentPayableCB!=null && documentPayableCB.getTaReglement()!=null) {
//				retour.setIdReglement(LibConversion.integerToString(log.getIdDocument()));
//				retour.setCodeReglement(log.getCodeReglement());
//			}
//			
//			return retour;
			
			
			
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
		} finally {
			
			//créer le réglement CB dans la table des règlements CB
			logPaiementInternetDAO.merge(log);
			
		}
	}
	
	/**
	 * @deprecated Rendre tous les documents IDocumentPayableCB 
	 * ou trouver un moyen de lier un paiement à un document "non payable" (autre table TaRReglement en parallele des "vrai" règlement ?)
	 */
	@Transactional(value=TxType.REQUIRED)
	public String creerPaymentIntentSWTDocument(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, SWTDocument documentPayableCB, TaTiers taTiers, String libelle)
			throws EJBException {
		
		System.out.println("PaiementStripeDossierService.creerPaymentIntent() TYPE_STRIPE");
		BdgProperties bdgProperties = new BdgProperties();
		
//		throw new EJBException("le code de sécurité est incorrect");
		
		String TYPE_PAIEMENT_DEFAUT = "CB";
		
		TaPreferences prefCB;
		try {
			prefCB = taPreferencesService.findByCode("paiement_cb");
			if(prefCB!=null) TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TaLogPaiementInternet log = new TaLogPaiementInternet();
		Date datePaiement = new Date();
		
		if(configService!=null) {
			UtilServiceWebExterne util = new UtilServiceWebExterne();
			configService = util.decrypter(configService);
			
			System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
			System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
		}
			
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
//			boolean modeTest = false;
//			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
//			
//			//Stripe.apiKey = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
////			String API_KEY_DOSSIER = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
//			String API_KEY_DOSSIER = configService.getApiKey1();
			
			BigDecimal montant = paiementCarteBancaire.getMontantPaiement();
			String numeroCarte = paiementCarteBancaire.getNumeroCarte();
			int moisCarte = paiementCarteBancaire.getMoisCarte();
			int anneeCarte = paiementCarteBancaire.getAnneeCarte();
			String cryptogrammeCarte = paiementCarteBancaire.getCryptogrammeCarte();
			String nomPorteurCarte = paiementCarteBancaire.getNomPorteurCarte();
			String description = paiementCarteBancaire.getDescriptionPaiement();
			String devise = paiementCarteBancaire.getDevise();
			if(requestOptions.getStripeAccount()!=null) {
				//on utilise stripe connect
				//if(tenantInfo.getTenantId()!=null && !tenantInfo.getTenantId().equals(LgrStripe.DOSSIER_MAITRE)) {
				if(tenantInfo.getTenantId()!=null && !tenantInfo.getTenantId().equals(paramBdg.getTaParametre().getDossierMaitre())) {
					paiementCarteBancaire.calculFraisPlateform();
				}
			}
			BigDecimal fraisPlateforme = paiementCarteBancaire.getFraisPlateforme();
			boolean capturablePlusTard = paiementCarteBancaire.isCapturablePlusTard();
			
//			if(modeTest) {
			if(lgrEnvironnementServeur.isModeTestPaiementDossier()) {
				System.out.println("** LgrStripe ** mode TEST activé, utilisation d'information de paiement de TEST.");
				//montant = new BigDecimal(2000);
				numeroCarte = "5555555555554444";
				moisCarte = 8;
				anneeCarte = 2025;
				cryptogrammeCarte = "314";
				nomPorteurCarte = "LEGRAIN - mode TEST";
				description = "Paiement Stripe Legrain - mode TEST";
			}
			
			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
			Integer fees = null;
			if(fraisPlateforme!=null) {
				fees = fraisPlateforme.multiply(BigDecimal.valueOf(100)).intValue();
			}
			if(devise==null) {
				devise = "eur";
			}
			
			Map<String, Object> paymentintentParams = new HashMap<String, Object>();
			paymentintentParams.put("amount", amount);
			paymentintentParams.put("currency", devise);
			paymentintentParams.put("description", description);
			if(fees!=null) {
				paymentintentParams.put("application_fee_amount", fees);
			}
			
			paymentintentParams.put("setup_future_usage", "off_session");
			
			if(paiementCarteBancaire.getEmailTicketRecu()!=null && !paiementCarteBancaire.getEmailTicketRecu().equals("")) {
				paymentintentParams.put("receipt_email", paiementCarteBancaire.getEmailTicketRecu());
			}
			
			if(capturablePlusTard) {
				paymentintentParams.put("capture_method", PaymentIntentCreateParams.CaptureMethod.MANUAL);
			}
			
			ArrayList<String> paymentMethodTypes = new ArrayList<>();
				paymentMethodTypes.add("card");
				paymentintentParams.put(
				    "payment_method_types",
				    paymentMethodTypes
				);
			
			log.setMontantPaiement(montant);
			log.setDevise(devise);
			
			Map<String, String> initialMetadata = new HashMap<String, String>();
			
			if(documentPayableCB!=null) {
				
//				documentPayableCB = findById(documentPayableCB);
				documentPayableCB = findByCode(documentPayableCB);
				
				initialMetadata.put("Numero "+typeDoc(documentPayableCB), documentPayableCB.getCodeDocument()!=null?documentPayableCB.getCodeDocument():".");
				initialMetadata.put("Date "+typeDoc(documentPayableCB), documentPayableCB.getDateDocument()!=null?LibDate.dateToString(documentPayableCB.getDateDocument()):".");
				initialMetadata.put("Montant TTC "+typeDoc(documentPayableCB), documentPayableCB.getNetTtcCalc()!=null?documentPayableCB.getNetTtcCalc().toString():".");
				initialMetadata.put("Code tiers", documentPayableCB.getTaTiers().getCodeTiers()!=null?documentPayableCB.getTaTiers().getCodeTiers():".");
				initialMetadata.put("Nom tiers", documentPayableCB.getTaTiers().getNomTiers()!=null?documentPayableCB.getTaTiers().getNomTiers():".");
				initialMetadata.put("Prenom tiers", (documentPayableCB.getTaTiers().getPrenomTiers()!=null && !documentPayableCB.getTaTiers().getPrenomTiers().equals(""))?documentPayableCB.getTaTiers().getPrenomTiers():".");
				if(documentPayableCB.getTaTiers().getTaEntreprise()!=null) {
					initialMetadata.put("Raison sociale", documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise()!=null?documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise():"");
				}
				
				log.setCodeDocument(documentPayableCB.getCodeDocument());
				log.setIdDocument(documentPayableCB.getIdDocument());
				log.setTypeDocument(typeDoc(documentPayableCB));
				log.setIdTiers(documentPayableCB.getTaTiers().getIdTiers());
				log.setCodeTiers(documentPayableCB.getTaTiers().getCodeTiers());
				log.setNomTiers(documentPayableCB.getTaTiers().getNomTiers());
			} else if(taTiers!=null) {
				initialMetadata.put("Code tiers", taTiers.getCodeTiers()!=null?taTiers.getCodeTiers():".");
				initialMetadata.put("Nom tiers", taTiers.getNomTiers()!=null?taTiers.getNomTiers():".");
				initialMetadata.put("Prenom tiers", (taTiers.getPrenomTiers()!=null && !taTiers.getPrenomTiers().equals(""))?taTiers.getPrenomTiers():".");
				if(taTiers.getTaEntreprise()!=null) {
					initialMetadata.put("Raison sociale", taTiers.getTaEntreprise().getNomEntreprise()!=null?taTiers.getTaEntreprise().getNomEntreprise():"");
				}
				
				log.setIdTiers(taTiers.getIdTiers());
				log.setCodeTiers(taTiers.getCodeTiers());
				log.setNomTiers(taTiers.getNomTiers());
			}
			initialMetadata.put("Origine", paiementCarteBancaire.getOriginePaiement()!=null?paiementCarteBancaire.getOriginePaiement():".");
			initialMetadata.put("Espace client", paiementCarteBancaire.isCompteClient()?"true":"false");
			initialMetadata.put("Dossier", tenantInfo.getTenantId()!=null?tenantInfo.getTenantId():".");
			initialMetadata.put("Serveur", bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)!=null?bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE):".");
			
			
			log.setServicePaiement(PaiementLgr.TYPE_STRIPE);
			log.setOriginePaiement(paiementCarteBancaire.getOriginePaiement());
			
			paymentintentParams.put("metadata", initialMetadata);
			
			Map<String, Object> sourceParams = new HashMap<String, Object>();
			sourceParams.put("number", numeroCarte);
			sourceParams.put("exp_month", moisCarte);
			sourceParams.put("exp_year", anneeCarte);
			sourceParams.put("cvc", cryptogrammeCarte);
			sourceParams.put("name", nomPorteurCarte);
			
//			paymentintentParams.put("source", sourceParams);
			
		
			
			System.out.println("LgrStripe ** paiment Stripe - montant : "+amount);
//			RequestOptions requestOptions = RequestOptions.builder().setApiKey(API_KEY_DOSSIER).build();
			//Charge c = Charge.create(paymentintentParams,requestOptions);
			PaymentIntent pi = PaymentIntent.create(paymentintentParams,requestOptions);
			TaStripePaymentIntent taPaymentIntent = updatePaymentIntentFromStripe(configService, pi);
			
			taPaymentIntent.setNetTtcCalc(montant);
			taPaymentIntent.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
			taPaymentIntent.setConserverCarte(paiementCarteBancaire.isConserverCarte());
			taPaymentIntent.setCompteClient(paiementCarteBancaire.isCompteClient());
			if(documentPayableCB!=null) {
				taPaymentIntent.setTaTiers(documentPayableCB.getTaTiers());
				if(documentPayableCB instanceof TaFacture) {
					taPaymentIntent.setTaFacture((TaFacture)documentPayableCB);
				} else if(documentPayableCB instanceof TaTicketDeCaisse) {
					taPaymentIntent.setTaTicketDeCaisse((TaTicketDeCaisse)documentPayableCB);
				} else if(documentPayableCB instanceof TaAvisEcheance) {
					taPaymentIntent.setTaAvisEcheance((TaAvisEcheance)documentPayableCB);
				} else if(documentPayableCB instanceof TaPanier) {
					taPaymentIntent.setTaPanier((TaPanier)documentPayableCB);
				}
			} else if(taTiers!=null) {
				taPaymentIntent.setTaTiers(taTiers);
			}
			taStripePaymentIntentService.merge(taPaymentIntent);
			
			return pi.getClientSecret();

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
		} finally {
			
			//créer le réglement CB dans la table des règlements CB
			logPaiementInternetDAO.merge(log);
			
		}
	}
	
	public RetourPaiementCarteBancaire validerPaymentIntent(TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException {
		return validerPaymentIntent(null, configService, paymentIntentStripeID, connectedAccountId);
	}
	
	
	public TaStripePaymentIntent validerPaymentIntentReglement(TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException {
		return validerPaymentIntentReglement(null, configService, paymentIntentStripeID, connectedAccountId);
	}
	/**
	 * Appeler par les webhooks.
	 * <br>
	 * La validation d'un payementIntent entraine la création du règlement correspondant dans BDG<br>
	 * Le payementIntent ayant une référence au tiers et au document à payer le cas échéant, les affectations de reglement se font automatiquement
	 */
//	@Transactional(value=TxType.REQUIRED)
	@Transactional(value=TxType.REQUIRES_NEW)
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public RetourPaiementCarteBancaire validerPaymentIntent(String tenant, TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException {
		
		System.out.println("PaiementStripeDossierService.creerPaymentIntent() TYPE_STRIPE");
		
		if(tenant!=null) {
			initTenantRegistry();
			initTenant(tenant);
		}	
		
//		throw new EJBException("le code de sécurité est incorrect");
		
		String TYPE_PAIEMENT_DEFAUT = "CB";
		
		TaPreferences prefCB;
		try {
			prefCB = taPreferencesService.findByCode("paiement_cb");
			if(prefCB!=null) TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TaLogPaiementInternet log = new TaLogPaiementInternet();
		Date datePaiement = new Date();
		
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		configService = util.decrypter(configService);
		
		System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
		System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
			
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService,connectedAccountId);
			
//			boolean modeTest = false;
//			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
//			
////			//Stripe.apiKey = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
//////			String API_KEY_DOSSIER = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
//			String API_KEY_DOSSIER = configService.getApiKey1();
//			RequestOptions requestOptions = RequestOptions.builder().setApiKey(API_KEY_DOSSIER).build();
			PaymentIntent pi = PaymentIntent.retrieve(paymentIntentStripeID,requestOptions);
			
//			TaStripePaymentIntent taStripePaymentIntent = updatePaymentIntentFromStripe(configService, pi);
			TaStripePaymentIntent taStripePaymentIntent = taStripePaymentIntentService.findByCode(pi.getId());
			BigDecimal montant = taStripePaymentIntent.getNetTtcCalc();
			TaTiers taTiers = null;
			
//			if(!pi.getStatus().equals("succeeded")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!pi.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
			
			log.setRefPaiementService(pi.getId());
			log.setDatePaiement(datePaiement);
			
			//création du règlement
			TaReglement r = new TaReglement();
			r.setNetTtcCalc(montant);
			r.setCodeDocument(taReglementService.genereCode(null));
			r.setLibelleDocument("Paiement CB");
			r.setDateDocument(datePaiement);
			r.setDateLivDocument(datePaiement);
			r.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
			
			r.setService(PaiementLgr.TYPE_STRIPE);
			r.setCompteClient(taStripePaymentIntent.getCompteClient());
			r.setRefPaiement(pi.getId());
			
			//r = taReglementService.merge(r);
			
			IDocumentPayableCB documentPayableCB = null;
			SWTDocument swtDocument = null;
			List<TaAbonnement> listeAbonnementRegle = null;
			if(taStripePaymentIntent.getTaFacture()!=null) {
				documentPayableCB = taStripePaymentIntent.getTaFacture();
			} else if(taStripePaymentIntent.getTaTicketDeCaisse()!=null) {
				documentPayableCB = taStripePaymentIntent.getTaTicketDeCaisse();
			} else if(taStripePaymentIntent.getTaPanier()!=null) {
				swtDocument = taStripePaymentIntent.getTaPanier();
			} else if(taStripePaymentIntent.getTaAvisEcheance()!=null) {
				swtDocument = taStripePaymentIntent.getTaAvisEcheance();
				listeAbonnementRegle = taAvisEcheanceService.rechercheAbonnementDansAvisEcheance((TaAvisEcheance)swtDocument);
			} else if(taStripePaymentIntent.getTaTiers()!=null) {
				taTiers = taStripePaymentIntent.getTaTiers();
			}
			
			if(swtDocument!=null) {	
				//générer un document payable à partir du document d'origine et affecter le réglement au document généré (le plus souvent une facture)
				TaFacture taFacture = null;
				//TODO copier de TaStripePaimentPrevuService.declencherPaiementStripe() => à inifier
				if( (swtDocument instanceof TaAvisEcheance)
						|| (swtDocument instanceof TaPanier) ) { 
					ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
					if(swtDocument instanceof TaAvisEcheance) {
						//TaAvisEcheance taAvisEcheance = taAvisEcheanceService.findById(37);
						TaAvisEcheance taAvisEcheance = taAvisEcheanceService.findById(swtDocument.getIdDocument());
						swtDocument = taAvisEcheance;
						
						param.setDateDocument(new Date());
	//					result.setLibelle(nouveauLibelle);
						param.setTypeDest(TaFacture.TYPE_DOC);
	//					if(option==null)option=option3;
	//					result.setRepriseReferenceSrc(option.equals(option1));
	//					result.setRepriseLibelleSrc(option.equals(option2));
						
						((TaAvisEcheance)swtDocument).setNbDecimalesQte(2);
						((TaAvisEcheance)swtDocument).setNbDecimalesPrix(2);
						
						param.setDocumentSrc((TaAvisEcheance)swtDocument);
					} else if(swtDocument instanceof TaPanier) { 
						TaPanier taPanier = taPanierService.findById(swtDocument.getIdDocument());
						swtDocument = taPanier;
						
						param.setDateDocument(new Date());
	//					result.setLibelle(nouveauLibelle);
						param.setTypeDest(TaPanier.TYPE_DOC);
	//					if(option==null)option=option3;
	//					result.setRepriseReferenceSrc(option.equals(option1));
	//					result.setRepriseLibelleSrc(option.equals(option2));
						
						((TaPanier)swtDocument).setNbDecimalesQte(2);
						((TaPanier)swtDocument).setNbDecimalesPrix(2);
						
						param.setDocumentSrc((TaPanier)swtDocument);
					}
						List<IDocumentTiers> listeDocumentSrc = new ArrayList<>();
						listeDocumentSrc.add(swtDocument); //listeDocumentSrc.add(taAvisEcheance); 
						param.setListeDocumentSrc(listeDocumentSrc);
						
						param.setRepriseAucun(true);
	//					result.setRepriseAucun(option.equals(option3));
		//
	//					result.setCodeTiers(codeTiers);
						param.setRetour(true);
						creationDocumentMultiple.setParam(param);
						RetourGenerationDoc retourGenerationDoc = creationDocumentMultiple.creationDocument(true);
						taFacture = (TaFacture) retourGenerationDoc.getDoc();
						
						
						//taFacture = taFactureService.merge(taFacture);
	//					taFacture = taFactureService.findByCode(taFacture.getCodeDocument());
						//taFacture = taFactureService.getReference(taFacture.getIdDocument());
						taFacture = taFactureService.getReference(retourGenerationDoc.getIdDoc());
						taFacture.setLegrain(false);
						for (TaLFacture lf : taFacture.getLignes()) {
							lf.setLegrain(false);
						}
						if(taStripePaymentIntent.getCompteClient()!=null && taStripePaymentIntent.getCompteClient()) {
							TaParamEspaceClientDTO paramEspaceClientDTO = taParamEspaceClientService.findInstanceDTO();
							if(paramEspaceClientDTO!=null 
									&& paramEspaceClientDTO.getMiseADispositionAutomatiqueDesFactures()!=null
									&& paramEspaceClientDTO.getMiseADispositionAutomatiqueDesFactures()) {
								TaMiseADisposition taMiseADisposition = new TaMiseADisposition();
								taMiseADisposition.setAccessibleSurCompteClient(new Date());
								taFacture.setTaMiseADisposition(taMiseADisposition);
	//							taFacture = taFactureService.merge(taFacture);
								taFacture = factureDAO.merge(taFacture);
							}
							
						}
						documentPayableCB = taFacture;
					} else {
						
					}
				
			}
			
			if(documentPayableCB!=null) {	//affecter le réglement au document
				r.setTaTiers(documentPayableCB.getTaTiers());
				
				TaRReglement rr = new TaRReglement();
				//rr.setAffectation(montant);
				rr.setAffectation(documentPayableCB.getNetTtcCalc());
				rr.setEtat(IHMEtat.integre);
				//rr.setTaFacture(documentPayableCB);
				if(documentPayableCB instanceof TaFacture) {
					rr.setTaFacture((TaFacture) documentPayableCB);
				} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
					rr.setTaTicketDeCaisse((TaTicketDeCaisse) documentPayableCB);
				} else if(documentPayableCB instanceof TaAcompte) {
					//rr.setTaAcompte(documentPayableCB);
					throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
				} 
				rr.setTaReglement(r);
				if(documentPayableCB.getTaRReglements()==null) {
					documentPayableCB.setTaRReglements(new HashSet<>());
				}
				documentPayableCB.addRReglement(rr);
				r.addRReglement(rr);
			} else if(taTiers!=null) {
				//affecter le réglement au tiers
				r.setTaTiers(taTiers);
			}
				
			r = taReglementService.merge(r);
			
			taStripePaymentIntent.setTaReglement(r);
			taStripePaymentIntent = taStripePaymentIntentService.merge(taStripePaymentIntent);
			
			/*
			 * Mise jour de l'état des abonnements (suspendu ou non suspendu) suite au reglement d'un avis d'écheance
			 */
			if(listeAbonnementRegle!=null && !listeAbonnementRegle.isEmpty()) {
				for (TaAbonnement taAbonnement : listeAbonnementRegle) {
					taAbonnement.setSuspension(false);
					taAbonnementService.merge(taAbonnement);
				}
			}
			
			if(documentPayableCB!=null) {	
				//taFacture = taFactureService.merge(taFacture);
//				documentPayableCB = findById(documentPayableCB);
				documentPayableCB = findByCode(documentPayableCB);
				
//				if(taFacture.getTaReglement()!=null) {
//					log.setIdReglement(taFacture.getTaReglement().getIdDocument());
//					log.setCodeReglement(taFacture.getTaReglement().getCodeDocument());
//				}
				if(documentPayableCB.getTaRReglements()!=null && !documentPayableCB.getTaRReglements().isEmpty()) {
					TaReglement r2 = documentPayableCB.getTaRReglements().iterator().next().getTaReglement();
					log.setIdReglement(r2.getIdDocument());
					log.setCodeReglement(r2.getCodeDocument());
				}
			} else {
				if(r!=null) {
					log.setIdReglement(r.getIdDocument());
					log.setCodeReglement(r.getCodeDocument());
				}
			}
			
			if(taStripePaymentIntent.getConserverCarte()!=null && taStripePaymentIntent.getConserverCarte()) {
				actCreerSourceStripeCB(configService,taStripePaymentIntent.getTaTiers(),null,taStripePaymentIntent.getIdExternePaymentMethod());
			}
			 
			
			RetourPaiementCarteBancaire retour = new RetourPaiementCarteBancaire();
		//	retour.setPaye(pi.getPaid());
			retour.setRefPaiement(pi.getId());
			if(documentPayableCB!=null && documentPayableCB.getTaReglement()!=null) {
				retour.setIdReglement(LibConversion.integerToString(log.getIdDocument()));
				retour.setCodeReglement(log.getCodeReglement());
			}
			
			return retour;
			
			
			
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
//			// Since it's a decline, \Stripe\Error\Card will be caught
//			if(e.getCode()!=null && e.getCode().equals("incorrect_cvc")) {
//				throw new EJBException("le code de sécurité est incorrect",e);
//			} else if(e.getCode()!=null && e.getCode().equals("expired_card")) {
//				throw new EJBException("La carte a expiré",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_cvc")) {
//				throw new EJBException("Le code de sécurité de la carte est invalide",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_expiry_month")) {
//				throw new EJBException("Le mois d'expiration de la carte est invalide",e);
//			} else if(e.getCode()!=null && e.getCode().equals("invalid_expiry_year")) {
//				throw new EJBException("L'année d'expiration de la carte est invalide",e);
//			} else if(e.getCode()!=null && (e.getCode().equals("invalid_number") || e.getCode().equals("incorrect_number"))) {
//				throw new EJBException("Le numéro de carte saisie est invalide",e);
//			} else {
//				e.printStackTrace();
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
//			}
////		} catch (APIException e) {
////			e.printStackTrace();
////			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} finally {
			
			//créer le réglement CB dans la table des règlements CB
			logPaiementInternetDAO.merge(log);
			
		}
	}
	
//	@Transactional(value=TxType.REQUIRES_NEW)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public TaStripePaymentIntent validerPaymentIntentReglement(String tenant, TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId)
			throws EJBException {
		
		System.out.println("PaiementStripeDossierService.creerPaymentIntent() TYPE_STRIPE");
		
		if(tenant!=null) {
			initTenantRegistry();
			initTenant(tenant);
		}	
				
		String TYPE_PAIEMENT_DEFAUT = "CB";
		
		TaPreferences prefCB;
		try {
			prefCB = taPreferencesService.findByCode("paiement_cb");
			if(prefCB!=null) TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		TaLogPaiementInternet log = new TaLogPaiementInternet();
		Date datePaiement = new Date();
		
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		configService = util.decrypter(configService);
			
		try {
			
//			boolean modeTest = false;
//			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
//			
//			String API_KEY_DOSSIER = configService.getApiKey1();
//			RequestOptions requestOptions = RequestOptions.builder().setApiKey(API_KEY_DOSSIER).build();
			
			RequestOptions requestOptions = initCleStripeDossier(configService,connectedAccountId);
			
			PaymentIntent pi = PaymentIntent.retrieve(paymentIntentStripeID,requestOptions);
//			TaStripePaymentIntent taStripePaymentIntent = updatePaymentIntentFromStripe(configService, paymentIntentStripeID, connectedAccountId);
			
			TaStripePaymentIntent taStripePaymentIntent = taStripePaymentIntentService.findByCode(pi.getId());
			BigDecimal montant = taStripePaymentIntent.getNetTtcCalc();
			TaTiers taTiers = null;

			log.setRefPaiementService(pi.getId());
			log.setDatePaiement(datePaiement);
			
			//création du règlement
			if(taStripePaymentIntent.getTaReglement()==null) {
				//le règlement pour ce payment intent n'a pas encore était créer
				TaReglement r = new TaReglement();
				r.setNetTtcCalc(montant);
				r.setCodeDocument(taReglementService.genereCode(null));
				r.setLibelleDocument("Paiement CB"); //libellé par défaut
				r.setDateDocument(datePaiement);
				r.setDateLivDocument(datePaiement);
				r.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
				
				r.setService(PaiementLgr.TYPE_STRIPE);
				r.setCompteClient(taStripePaymentIntent.getCompteClient());
				r.setRefPaiement(pi.getId());
				
				//////////////////////////////
				// il serait préférable d'appeler la méthode updatePaymentIntentFromStripe(configService, pi) mais cela pose de problème de transaction.
				boolean paid = false;
				boolean captured = false;
				for(Charge c : pi.getCharges().getData()) {
					//dans une utilisation BDG il ne devrait y avoir qu'une seule charge pour un paymentIntent
					paid = c.getPaid();
					captured = c.getCaptured();
//					amountCaptured = montantStripeVersBdg(c.getAmountCaptured());
				}
				
				taStripePaymentIntent.setCaptured(captured);
				taStripePaymentIntent.setPaid(paid);
				taStripePaymentIntent.setAmountReceived(montantStripeVersBdg(pi.getAmountReceived()));
				//////////////////////////////
				
				if(taStripePaymentIntent.getTaFacture()!=null) {
					taTiers = taStripePaymentIntent.getTaFacture().getTaTiers();
				} else if(taStripePaymentIntent.getTaTicketDeCaisse()!=null) {
					taTiers = taStripePaymentIntent.getTaTicketDeCaisse().getTaTiers();
				} else if(taStripePaymentIntent.getTaAvisEcheance()!=null) {
					taTiers = taStripePaymentIntent.getTaAvisEcheance().getTaTiers();
					r.setLibelleDocument("Paiement CB - Avis d'échéance "+taStripePaymentIntent.getTaAvisEcheance().getCodeDocument()+" - "+pi.getId());
				} else if(taStripePaymentIntent.getTaPanier()!=null) {
					taTiers = taStripePaymentIntent.getTaPanier().getTaTiers();
					r.setLibelleDocument("Paiement CB - Panier "+taStripePaymentIntent.getTaPanier().getCodeDocument()+" - "+pi.getId());
					/********************************************************************************************************************************/
					IDocumentReglementLiable documentReglementLiable = (IDocumentReglementLiable)taPanierService.findByIDFetch(taStripePaymentIntent.getTaPanier().getIdDocument());
	//				IDocumentReglementLiable documentReglementLiable = (IDocumentReglementLiable)taStripePaymentIntent.getTaPanier();
					
					TaRReglementLiaison rrl = new TaRReglementLiaison();
					rrl.setAffectation(documentReglementLiable.getNetTtcCalc());
					rrl.setEtat(IHMEtat.integre);
					rrl.setTaPanier(taStripePaymentIntent.getTaPanier());
					
					rrl.setTaReglement(r);
					if(documentReglementLiable.getTaRReglementLiaisons()==null) {
						documentReglementLiable.setTaRReglementLiaisons(new HashSet<>());
					}
					documentReglementLiable.addRReglementLiaison(rrl);
					r.addRReglementLiaison(rrl);
					/********************************************************************************************************************************/
	
				} else if(taStripePaymentIntent.getTaTiers()!=null) {
					taTiers = taStripePaymentIntent.getTaTiers();
				}
				r.setTaTiers(taTiers);
					
				r = taReglementService.merge(r);
				
				taStripePaymentIntent.setTaReglement(r);
				taStripePaymentIntent = taStripePaymentIntentService.merge(taStripePaymentIntent);
				
				log.setIdReglement(r.getIdDocument());
				log.setCodeReglement(r.getCodeDocument());
				
				if(taStripePaymentIntent.getConserverCarte()!=null && taStripePaymentIntent.getConserverCarte()) {
					actCreerSourceStripeCB(configService,taStripePaymentIntent.getTaTiers(),null,taStripePaymentIntent.getIdExternePaymentMethod());
				}
				
				RetourPaiementCarteBancaire retour = new RetourPaiementCarteBancaire();
				retour.setRefPaiement(pi.getId());
				if(r!=null) {
					retour.setIdReglement(LibConversion.integerToString(log.getIdDocument()));
					retour.setCodeReglement(log.getCodeReglement());
				}
			} else {
				taStripePaymentIntent = updatePaymentIntentFromStripe(configService,paymentIntentStripeID,connectedAccountId);
			}
			
			return taStripePaymentIntent;
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} finally {
			//créer le réglement CB dans la table des règlements CB
			logPaiementInternetDAO.merge(log);
			
		}
	}
	
	/**
	 * Si le règlement Stripe ne règle pas un type de document payable (Facture ou TicketDeCaisse),
	 * génère une facture à partir du document réglé. Le réglement pourra par la suite affecté à cette facture.
	 * 
	 * Pas de génération (car déjà "réglable" directement - IDocumentPayableCB)
	 * Facture => Facture
	 * Ticket de caisse => Ticket de caisse
	 * 
	 * Génération 
	 * Avis d'échéance => Facture (choix "en dur")
	 * Panier => Commande (par defaut, modifiable vers facture dans paramètre boutique)
	 * 
	 * @see PanierRestService#validerCommandePourReglementUlterieur
	 */
//	@Transactional(value=TxType.REQUIRES_NEW)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public IDocumentPayableCB validerPaymentIntentGenerationDoc(String tenant, TaStripePaymentIntent paymentIntentStripe)
			throws EJBException {
		
		System.out.println("PaiementStripeDossierService.creerPaymentIntent() TYPE_STRIPE");
		
		if(tenant!=null) {
			initTenantRegistry();
			initTenant(tenant);
		}
			
		try {
			TaParamEspaceClientDTO paramEspaceClientDTO = taParamEspaceClientService.findInstanceDTO();
			
			TaStripePaymentIntent taStripePaymentIntent = taStripePaymentIntentService.findById(paymentIntentStripe.getIdStripePaymentIntent());
			BigDecimal montant = taStripePaymentIntent.getNetTtcCalc();
			TaTiers taTiers = null;
			TaTiers taTiersAverifier = null;
			
			IDocumentPayableCB documentPayableCB = null;
			SWTDocument swtDocument = null;
			List<String> listeTypeDocAGenerer = new LinkedList<>();
			
			if(taStripePaymentIntent.getTaFacture()!=null) {
				documentPayableCB = taStripePaymentIntent.getTaFacture();
			} else if(taStripePaymentIntent.getTaTicketDeCaisse()!=null) {
				documentPayableCB = taStripePaymentIntent.getTaTicketDeCaisse();
			} else if(taStripePaymentIntent.getTaAvisEcheance()!=null) {
				swtDocument = taStripePaymentIntent.getTaAvisEcheance();
				listeTypeDocAGenerer.add(TaFacture.TYPE_DOC);
			} else if(taStripePaymentIntent.getTaPanier()!=null) {
				swtDocument = taStripePaymentIntent.getTaPanier();
				swtDocument = taPanierService.findByIDFetch(taStripePaymentIntent.getTaPanier().getIdDocument());
				
				if(paramEspaceClientDTO!=null 
						&& paramEspaceClientDTO.getGenerationDocAuPaiementPanier()!=null) {
					if(paramEspaceClientDTO.getGenerationDocAuPaiementPanier().equals(TaParamEspaceClient.GENERATION_DOCUMENT_FACTURE)) {
//						param.setTypeDest(TaFacture.TYPE_DOC);
						listeTypeDocAGenerer.add(TaFacture.TYPE_DOC);
					} else if(paramEspaceClientDTO.getGenerationDocAuPaiementPanier().equals(TaParamEspaceClient.GENERATION_DOCUMENT_BON_COMMANDE)) {
//						param.setTypeDest(TaBoncde.TYPE_DOC);
						listeTypeDocAGenerer.add(TaBoncde.TYPE_DOC);
					} else if(paramEspaceClientDTO.getGenerationDocAuPaiementPanier().equals(TaParamEspaceClient.GENERATION_DOCUMENT_BON_COMMANDE_ET_FACTURE)) {
						listeTypeDocAGenerer.add(TaBoncde.TYPE_DOC);
						//if(taStripePaymentIntent.getCaptured()) { //si c'est juste une empreinte CB, le montant du règlement n'est pas encore définitif donc on ne génére pas de facture
							listeTypeDocAGenerer.add(TaFacture.TYPE_DOC);
						//}
					}
					
				}
			} else if(taStripePaymentIntent.getTaTiers()!=null) {
				taTiers = taStripePaymentIntent.getTaTiers();
				taTiersAverifier = taTiers;
			}
			
			for (String typeDest : listeTypeDocAGenerer) {
				
				if(swtDocument!=null) {	
					//générer un document payable à partir du document d'origine et affecter le réglement au document généré (le plus souvent une facture)
					TaFacture taFacture = null;
					//TODO copier de TaStripePaimentPrevuService.declencherPaiementStripe() => à vérifier
					ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
					param.setDateDocument(new Date());
					if( (swtDocument instanceof TaAvisEcheance)
							|| (swtDocument instanceof TaPanier) 
							|| (swtDocument instanceof TaBoncde) ////&&&&&&
							) { 
						
						//result.setLibelle(nouveauLibelle);
						
						param.setTypeDest(TaFacture.TYPE_DOC); //génération de facture "par défaut"
						
//						if( (swtDocument instanceof TaAvisEcheance) ) { 
//							//pour l'instant, le règlement stripe d'un avis d'échéance génère forcément une facture
//							param.setTypeDest(TaFacture.TYPE_DOC);
//							param.setTypeDest(typeDest);
//							
//						}else if( (swtDocument instanceof TaPanier) ) { 
//							//pour l'instant, le règlement stripe d'un panier génère forcément un bon de commande, mais cela peut etre modifier par un paramètre
//							param.setTypeDest(TaBoncde.TYPE_DOC);
//							if(paramEspaceClientDTO!=null 
//									&& paramEspaceClientDTO.getGenerationDocAuPaiementPanier()!=null) {
//								if(paramEspaceClientDTO.getGenerationDocAuPaiementPanier().equals(TaParamEspaceClient.GENERATION_DOCUMENT_FACTURE)) {
//									param.setTypeDest(TaFacture.TYPE_DOC);
//								} else if(paramEspaceClientDTO.getGenerationDocAuPaiementPanier().equals(TaParamEspaceClient.GENERATION_DOCUMENT_BON_COMMANDE)) {
//									param.setTypeDest(TaBoncde.TYPE_DOC);
//								}
//								
//							}
//							param.setTypeDest(typeDest);
//						}
						param.setTypeDest(typeDest);
						
						//if(option==null)option=option3;
						//result.setRepriseReferenceSrc(option.equals(option1));
						//result.setRepriseLibelleSrc(option.equals(option2));
	
						if(swtDocument instanceof TaAvisEcheance) {
							//TaAvisEcheance taAvisEcheance = taAvisEcheanceService.findById(37);
							TaAvisEcheance taAvisEcheance = taAvisEcheanceService.findById(swtDocument.getIdDocument());
							swtDocument = taAvisEcheance;
							param.setDocumentSrc((TaAvisEcheance)swtDocument);
						} else if(swtDocument instanceof TaPanier) { 
							TaPanier taPanier = taPanierService.findById(swtDocument.getIdDocument());
							swtDocument = taPanier;
							param.setDocumentSrc((TaPanier)swtDocument);
						} else if(swtDocument instanceof TaBoncde) { 
							TaBoncde taBoncde = taBoncdeService.findById(swtDocument.getIdDocument());
							swtDocument = taBoncde;
							param.setDocumentSrc((TaBoncde)swtDocument);
						}
						
						(swtDocument).setNbDecimalesQte(2);
						(swtDocument).setNbDecimalesPrix(2);
						
						List<IDocumentTiers> listeDocumentSrc = new ArrayList<>();
						listeDocumentSrc.add(swtDocument); //listeDocumentSrc.add(taAvisEcheance); 
						param.setListeDocumentSrc(listeDocumentSrc);
						param.setRepriseAucun(true);
						param.setRetour(true);
						
						creationDocumentMultiple.setParam(param);
						RetourGenerationDoc retourGenerationDoc = creationDocumentMultiple.creationDocument(true);
						
						/*
						 * Pour tous les documents SWTDocument qui ne sont pas des document payable directement
						 * (facture et ticket de caisse)
						 * on peut continuer les transformations en série et donc on réacffecte la variable swtDocument
						 * pour une prochaine entrée dans la boucle
						 */
						if(param.getTypeDest().equals(TaBoncde.TYPE_DOC)) {
							swtDocument = (TaBoncde) retourGenerationDoc.getDoc();
						} else if(param.getTypeDest().equals(TaFacture.TYPE_DOC)) {
							taFacture = (TaFacture) retourGenerationDoc.getDoc();
						
						
							//taFacture = taFactureService.merge(taFacture);
		//					taFacture = taFactureService.findByCode(taFacture.getCodeDocument());
							//taFacture = taFactureService.getReference(taFacture.getIdDocument());
							taFacture = taFactureService.getReference(retourGenerationDoc.getIdDoc());
							taFacture.setLegrain(false);
							for (TaLFacture lf : taFacture.getLignes()) {
								lf.setLegrain(false);
							}
							if(taStripePaymentIntent.getCompteClient()!=null && taStripePaymentIntent.getCompteClient()) {
								
								if(paramEspaceClientDTO!=null 
										&& paramEspaceClientDTO.getMiseADispositionAutomatiqueDesFactures()!=null
										&& paramEspaceClientDTO.getMiseADispositionAutomatiqueDesFactures()) {
									TaMiseADisposition taMiseADisposition = new TaMiseADisposition();
									taMiseADisposition.setAccessibleSurCompteClient(new Date());
									taFacture.setTaMiseADisposition(taMiseADisposition);
		//							taFacture = taFactureService.merge(taFacture);
									taFacture = factureDAO.merge(taFacture);
								}
								
							}
							documentPayableCB = taFacture;
						} //else on n'a pas généré de facture (surement un bon de commande à la place)
					} else {
						//ni panier, ni avis d'échéance
					}
				}
				
			}
			
			if(documentPayableCB!=null) {
				taTiersAverifier = documentPayableCB.getTaTiers();
			} else if(swtDocument!=null) {
				taTiersAverifier = swtDocument.getTaTiers();
			}
			if(taTiersAverifier!=null) {
				if(taTiersAverifier.getTaTTiers()!=null && taTiersAverifier.getTaTTiers().getCodeTTiers().equals(TaTTiers.VISITEUR_BOUTIQUE)) {
					//si le tiers était un "visiteur", il devient un client
					TaTTiers taTTiers = taTTiersService.findByCode(TaTTiers.CLIENT);
					taTiersAverifier.setTaTTiers(taTTiers);
					taTiersService.merge(taTiersAverifier);
				}
			}

			return documentPayableCB;

		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} finally {
		}
	}
	
//	@Transactional(value=TxType.REQUIRES_NEW)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public TaStripePaymentIntent validerPaymentIntentAffectation(String tenant, IDocumentPayableCB doc, TaReglement taReglement,TaStripePaymentIntent paymentIntentStripe)
			throws EJBException {
		
		System.out.println("PaiementStripeDossierService.creerPaymentIntent() TYPE_STRIPE");
		
		if(tenant!=null) {
			initTenantRegistry();
			initTenant(tenant);
		}	
					
		try {
			
			boolean modeTest = false;
			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
			
//			TaStripePaymentIntent taStripePaymentIntent = paymentIntentStripe;
			TaStripePaymentIntent taStripePaymentIntent = taStripePaymentIntentService.findById(paymentIntentStripe.getIdStripePaymentIntent());
//			BigDecimal montant = taStripePaymentIntent.getNetTtcCalc();
			TaTiers taTiers = null;
			
//			TaReglement r = taStripePaymentIntent.getTaReglement();
//			TaReglement r = taReglement;
			TaReglement r = taReglementService.findByCode(taReglement.getCodeDocument());
			
			IDocumentPayableCB documentPayableCB = doc;
			SWTDocument swtDocument = null;
			List<TaAbonnement> listeAbonnementRegle = null;
			
			if(taStripePaymentIntent.getTaFacture()!=null) {
			} else if(taStripePaymentIntent.getTaTicketDeCaisse()!=null) {
			} else if(taStripePaymentIntent.getTaAvisEcheance()!=null) {
				swtDocument = taStripePaymentIntent.getTaAvisEcheance();
				listeAbonnementRegle = taAvisEcheanceService.rechercheAbonnementDansAvisEcheance((TaAvisEcheance)swtDocument);
			} else if(taStripePaymentIntent.getTaTiers()!=null) {
				taTiers = taStripePaymentIntent.getTaTiers();
			}
			
			if(documentPayableCB!=null) {	//affecter le réglement au document
				r.setTaTiers(documentPayableCB.getTaTiers());
				
				TaRReglementLiaison rrl = new TaRReglementLiaison();
				rrl.setAffectation(documentPayableCB.getNetTtcCalc());
				rrl.setEtat(IHMEtat.integre);
				
				TaRReglement rr = new TaRReglement();
				//rr.setAffectation(montant);
				rr.setAffectation(documentPayableCB.getNetTtcCalc());
				rr.setEtat(IHMEtat.integre);
				//rr.setTaFacture(documentPayableCB);
				if(documentPayableCB instanceof TaFacture) {
					rr.setTaFacture((TaFacture) documentPayableCB);
				} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
					rr.setTaTicketDeCaisse((TaTicketDeCaisse) documentPayableCB);
				} else if(documentPayableCB instanceof TaAcompte) {
					//rr.setTaAcompte(documentPayableCB);
					throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
				} 
				rr.setTaReglement(r);
				if(documentPayableCB.getTaRReglements()==null) {
					documentPayableCB.setTaRReglements(new HashSet<>());
				}
				boolean reglementDejaAffectePrecedement = false;
				for (TaRReglement rreg : documentPayableCB.getTaRReglements()) {
					if(rreg.getTaReglement()!=null 
							&& rreg.getTaReglement().getCodeDocument()!=null
							&& rreg.getTaReglement().getCodeDocument().equals(r.getCodeDocument())) {
						//il y a surement eu 2 ou plusieurs génération de documents successive
						//le règlement était deja lié un SWTDocument, 
						//en générant le documentPayable le règlement a suivi et lui a été affecté
						//il ne faut donc pas le réaffecté 
						//TODO ce controle devrait être fait dans la génération d'un documentPayable à partirr d'un swtDocument deja lié aà un règlement
						reglementDejaAffectePrecedement = true;
					}
				}
				if(!reglementDejaAffectePrecedement) {
					documentPayableCB.addRReglement(rr);
//					r.addRReglement(rr);
				}
			} else if(taTiers!=null) {
				//affecter le réglement au tiers
				r.setTaTiers(taTiers);
			}
			
			r = taReglementService.merge(r);
			
			/*
			 * Mise jour de l'état des abonnements (suspendu ou non suspendu) suite au reglement d'un avis d'écheance
			 */
			if(listeAbonnementRegle!=null && !listeAbonnementRegle.isEmpty()) {
				for (TaAbonnement taAbonnement : listeAbonnementRegle) {
					taAbonnement.setSuspension(false);
					taAbonnementService.merge(taAbonnement);
				}
			}
			
			return taStripePaymentIntent;
	
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} finally {
			
		}
	}
	
	public void ajouterAdresseEmailPourRecu(TaCompteServiceWebExterne compte, String paymentIntentId, String adresseEmailTicketRecu) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(compte);
			
			PaymentIntent pi = PaymentIntent.retrieve(paymentIntentId,requestOptions);
			Map<String, Object> paymentintentParams = new HashMap<String, Object>();
//			if(adresseEmailTicketRecu!=null && !adresseEmailTicketRecu.equals("")) {
				paymentintentParams.put("receipt_email", adresseEmailTicketRecu);
//			}
			pi.update(paymentintentParams,requestOptions);
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actCreerSourceStripeCB(TaCompteServiceWebExterne compte, TaTiers taTiers, TaCarteBancaire taCarteBancaire, String paymentMethodId) {
//		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		String x = params.get("x");
		System.out.println("PaiementCbController.actCreerSourceStripeCB() ******* **-- -*-*-- "+paymentMethodId);
		
		//TaStripePaymentIntent pi = paiementEnLigneDossierService.updatePaymentIntentFromStripe(compte,  secretClient.split("_secret_")[0]);
		
		try {
			TaStripeSource s = null;
			if(taCarteBancaire==null) {
				taCarteBancaire = new TaCarteBancaire();
				taCarteBancaire.setTaTiers(taTiers);
				taCarteBancaire = taCarteBancaireService.merge(taCarteBancaire);
			} else {
				s = taStripeSourceService.rechercherSource(taCarteBancaireService.findById(taCarteBancaire.getIdCarteBancaire()));
			}
			if(s == null) {//si pas de source/payment_method pour ce moyen de paiement
				//créer une source 
				s = new TaStripeSource();
				TaStripeSourceDTO taStripeSourceDTO = rechercherPaymentMethod(compte,paymentMethodId);
				s.setIdExterne(paymentMethodId);
				s.setTaCarteBancaire(taCarteBancaireService.findById(taCarteBancaire.getIdCarteBancaire()));
				
				
				s = taStripeSourceService.merge(s);
				taCarteBancaire.setLast4(taStripeSourceDTO.getTaCarteBancaireDTO().getLast4());
				taCarteBancaire.setMoisExpiration(taStripeSourceDTO.getTaCarteBancaireDTO().getMoisExpiration());
				taCarteBancaire.setAnneeExpiration(taStripeSourceDTO.getTaCarteBancaireDTO().getAnneeExpiration());
				taCarteBancaire.setType(taStripeSourceDTO.getTaCarteBancaireDTO().getType());
				taCarteBancaire = taCarteBancaireService.merge(taCarteBancaire);
			}
			
			TaStripeCustomer taStripeCustomer =  taStripeCustomerService.rechercherCustomer(taTiers);
			if(taStripeCustomer!=null) {
				//l'ajouter à ce customer
				attacherPayementMethodStripe(compte, s.getIdExterne(), taStripeCustomer.getIdExterne());
				s.setTaStripeCustomer(taStripeCustomer);
				s = taStripeSourceService.merge(s);
				//si ce tiers n'a pas de source par defaut, affecter cette nouvelle source en tant que source par defaut
				taStripeCustomer.getSources().add(s);
				taStripeCustomer.setTaSourceDefault(s);
				taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
			} else {
				//créer un customer avec cette source par defaut
				String idCustomerStripe = creerCustomerStripe(compte, taTiers.getTaEmail().getAdresseEmail(), s.getIdExterne(), taTiers.getNomTiers()+" "+taTiers.getCodeTiers());
				taStripeCustomer = new TaStripeCustomer();
				taStripeCustomer.setIdExterne(idCustomerStripe);
				taStripeCustomer.setEmail(taTiers.getTaEmail().getAdresseEmail());
				taStripeCustomer.setTaTiers(taTiers);
				if(taStripeCustomer.getSources()==null) {
					taStripeCustomer.setSources(new HashSet<>());
				}
				taStripeCustomer.getSources().add(s);
				taStripeCustomer.setTaSourceDefault(s);
				
				taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
				
				s.setTaStripeCustomer(taStripeCustomer);
				s = taStripeSourceService.merge(s);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @deprecated remplacer par la nouvelle API paymentIntent
	 */
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, TaTiers taTiers, String libelle)
			throws EJBException {
		return payer(configService, paiementCarteBancaire, null, taTiers, libelle);
	}
	
	/**
	 * @deprecated remplacer par la nouvelle API paymentIntent
	 */
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, String libelle)
			throws EJBException {
		return payer(configService, paiementCarteBancaire, documentPayableCB, null, libelle);
	}
	
	/**
	 * @deprecated remplacer par la nouvelle API paymentIntent
	 */
	public RetourPaiementCarteBancaire payer(TaCompteServiceWebExterne configService, PaiementCarteBancaire paiementCarteBancaire, IDocumentPayableCB documentPayableCB, TaTiers taTiers, String libelle)
			throws EJBException {
		
		System.out.println("EnvoiSMSMailjetDossierService.payer() TYPE_STRIPE");
		
//		throw new EJBException("le code de sécurité est incorrect");
		
		String TYPE_PAIEMENT_DEFAUT = "CB";
		
		TaPreferences prefCB;
		try {
			prefCB = taPreferencesService.findByCode("paiement_cb");
			if(prefCB!=null) TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TaLogPaiementInternet log = new TaLogPaiementInternet();
		Date datePaiement = new Date();
		
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		configService = util.decrypter(configService);
		
		System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
		System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
			
		try {
//			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			boolean modeTest = false;
			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
			
			//Stripe.apiKey = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
//			String API_KEY_DOSSIER = "sk_test_gSTXDQxFFZiiBdKOGlrcLZWh"; //clé test compte legrain
			String API_KEY_DOSSIER = configService.getApiKey1();
			
			BigDecimal montant = paiementCarteBancaire.getMontantPaiement();
			String numeroCarte = paiementCarteBancaire.getNumeroCarte();
			int moisCarte = paiementCarteBancaire.getMoisCarte();
			int anneeCarte = paiementCarteBancaire.getAnneeCarte();
			String cryptogrammeCarte = paiementCarteBancaire.getCryptogrammeCarte();
			String nomPorteurCarte = paiementCarteBancaire.getNomPorteurCarte();
			String description = paiementCarteBancaire.getDescriptionPaiement();
			
			if(modeTest) {
				System.out.println("** LgrStripe ** mode TEST activé, utilisation d'information de paiement de TEST.");
				//montant = new BigDecimal(2000);
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
			
			log.setMontantPaiement(montant);
			log.setDevise("eur");
			
			Map<String, String> initialMetadata = new HashMap<String, String>();
			
			if(documentPayableCB!=null) {
				
//				documentPayableCB = findById(documentPayableCB);
				documentPayableCB = findByCode(documentPayableCB);
				
				initialMetadata.put("Numero "+typeDoc(documentPayableCB), documentPayableCB.getCodeDocument()!=null?documentPayableCB.getCodeDocument():".");
				initialMetadata.put("Date "+typeDoc(documentPayableCB), documentPayableCB.getDateDocument()!=null?LibDate.dateToString(documentPayableCB.getDateDocument()):".");
				initialMetadata.put("Montant TTC "+typeDoc(documentPayableCB), documentPayableCB.getMtTtcCalc()!=null?documentPayableCB.getMtTtcCalc().toString():".");
				initialMetadata.put("Code tiers", documentPayableCB.getTaTiers().getCodeTiers()!=null?documentPayableCB.getTaTiers().getCodeTiers():".");
				initialMetadata.put("Nom tiers", documentPayableCB.getTaTiers().getNomTiers()!=null?documentPayableCB.getTaTiers().getNomTiers():".");
				initialMetadata.put("Prenom tiers", (documentPayableCB.getTaTiers().getPrenomTiers()!=null && !documentPayableCB.getTaTiers().getPrenomTiers().equals(""))?documentPayableCB.getTaTiers().getPrenomTiers():".");
				if(documentPayableCB.getTaTiers().getTaEntreprise()!=null) {
					initialMetadata.put("Raison sociale", documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise()!=null?documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise():"");
				}
				
				log.setCodeDocument(documentPayableCB.getCodeDocument());
				log.setIdDocument(documentPayableCB.getIdDocument());
				log.setTypeDocument(typeDoc(documentPayableCB));
				log.setIdTiers(documentPayableCB.getTaTiers().getIdTiers());
				log.setCodeTiers(documentPayableCB.getTaTiers().getCodeTiers());
				log.setNomTiers(documentPayableCB.getTaTiers().getNomTiers());
			} 
			initialMetadata.put("Origine", paiementCarteBancaire.getOriginePaiement()!=null?paiementCarteBancaire.getOriginePaiement():".");
			
			log.setServicePaiement(PaiementLgr.TYPE_STRIPE);
			log.setOriginePaiement(paiementCarteBancaire.getOriginePaiement());
			
			chargeParams.put("metadata", initialMetadata);
			
			Map<String, Object> sourceParams = new HashMap<String, Object>();
			sourceParams.put("number", numeroCarte);
			sourceParams.put("exp_month", moisCarte);
			sourceParams.put("exp_year", anneeCarte);
			sourceParams.put("cvc", cryptogrammeCarte);
			sourceParams.put("name", nomPorteurCarte);
			
			chargeParams.put("source", sourceParams);
			
		
			
			System.out.println("LgrStripe ** paiment Stripe - montant : "+amount);
			RequestOptions requestOptions = RequestOptions.builder().setApiKey(API_KEY_DOSSIER).build();
			Charge c = Charge.create(chargeParams,requestOptions);
			
			if(!c.getStatus().equals("succeeded")) {
				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
			}
			if(!c.getPaid()) {
				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
			}
			
			log.setRefPaiementService(c.getId());
			log.setDatePaiement(datePaiement);
			
			//création du règlement
			TaReglement r = new TaReglement();
			r.setNetTtcCalc(montant);
			r.setCodeDocument(taReglementService.genereCode(null));
			r.setLibelleDocument("Paiement CB");
			r.setDateDocument(datePaiement);
			r.setDateLivDocument(datePaiement);
			r.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
			
			r.setService(PaiementLgr.TYPE_STRIPE);
			r.setCompteClient(paiementCarteBancaire.isCompteClient());
			r.setRefPaiement(c.getId());
			
			//r = taReglementService.merge(r);
				
			//affecter le réglement au document
			if(documentPayableCB!=null) {	
				r.setTaTiers(documentPayableCB.getTaTiers());
				
				TaRReglement rr = new TaRReglement();
				rr.setAffectation(montant);
				rr.setEtat(IHMEtat.integre);
				//rr.setTaFacture(documentPayableCB);
				if(documentPayableCB instanceof TaFacture) {
					rr.setTaFacture((TaFacture) documentPayableCB);
				} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
					rr.setTaTicketDeCaisse((TaTicketDeCaisse) documentPayableCB);
				} else if(documentPayableCB instanceof TaAcompte) {
					//rr.setTaAcompte(documentPayableCB);
					throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
				} 
				rr.setTaReglement(r);
				if(documentPayableCB.getTaRReglements()==null) {
					documentPayableCB.setTaRReglements(new HashSet<>());
				}
				documentPayableCB.addRReglement(rr);
				r.addRReglement(rr);
			} else if(taTiers!=null) {
				r.setTaTiers(taTiers);
			}
				
			r = taReglementService.merge(r);
			
			if(documentPayableCB!=null) {	
				//taFacture = taFactureService.merge(taFacture);
//				documentPayableCB = findById(documentPayableCB);
				documentPayableCB = findByCode(documentPayableCB);
				
//				if(taFacture.getTaReglement()!=null) {
//					log.setIdReglement(taFacture.getTaReglement().getIdDocument());
//					log.setCodeReglement(taFacture.getTaReglement().getCodeDocument());
//				}
				if(documentPayableCB.getTaRReglements()!=null && !documentPayableCB.getTaRReglements().isEmpty()) {
					TaReglement r2 = documentPayableCB.getTaRReglements().iterator().next().getTaReglement();
					log.setIdReglement(r2.getIdDocument());
					log.setCodeReglement(r2.getCodeDocument());
				}
			} else {
				if(r!=null) {
					log.setIdReglement(r.getIdDocument());
					log.setCodeReglement(r.getCodeDocument());
				}
			}
			 
			
			RetourPaiementCarteBancaire retour = new RetourPaiementCarteBancaire();
			retour.setPaye(c.getPaid());
			retour.setRefPaiement(c.getId());
			if(documentPayableCB!=null && documentPayableCB.getTaReglement()!=null) {
				retour.setIdReglement(LibConversion.integerToString(log.getIdDocument()));
				retour.setCodeReglement(log.getCodeReglement());
			}
			
			return retour;
			
			
			
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
		} finally {
			
			//créer le réglement CB dans la table des règlements CB
			logPaiementInternetDAO.merge(log);
			
		}
	}
	
	//TODO, utiliser plus le polymophisme
	public IDocumentPayableCB findById(IDocumentPayableCB documentPayableCB) throws FinderException {
		if(documentPayableCB instanceof TaFacture) {
			return taFactureService.findById(documentPayableCB.getIdDocument());
		} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
			return taTicketDeCaisseService.findById(documentPayableCB.getIdDocument());
		} else if(documentPayableCB instanceof TaAcompte) {
			//return taAcompteService.findById(documentPayableCB.getIdDocument());
			throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
		} else  {
			return null;
		}
	}
	
	//TODO, utiliser plus le polymophisme
	public IDocumentPayableCB findByCode(IDocumentPayableCB documentPayableCB) throws FinderException {
		if(documentPayableCB instanceof TaFacture) {
			return taFactureService.findByCode(documentPayableCB.getCodeDocument());
		} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
			return taTicketDeCaisseService.findByCode(documentPayableCB.getCodeDocument());
		} else if(documentPayableCB instanceof TaAcompte) {
			//return taAcompteService.findById(documentPayableCB.getIdDocument());
			throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
		} else  {
			return null;
		}
	}
	
	//TODO, utiliser plus le polymophisme
	public SWTDocument findByCode(SWTDocument swtDocument) throws FinderException {
		if(swtDocument instanceof TaFacture) {
			return taFactureService.findByCode(swtDocument.getCodeDocument());
		} else  if(swtDocument instanceof TaTicketDeCaisse) {
			return taTicketDeCaisseService.findByCode(swtDocument.getCodeDocument());
		} else  if(swtDocument instanceof TaPanier) {
			return taPanierService.findByCode(swtDocument.getCodeDocument());
		} else  if(swtDocument instanceof TaAvisEcheance) {
			return taAvisEcheanceService.findByCode(swtDocument.getCodeDocument());
		} else if(swtDocument instanceof TaAcompte) {
			//return taAcompteService.findById(documentPayableCB.getIdDocument());
			throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
		} else  {
			return null;
		}
	}
	
	//TODO, utiliser plus le polymophisme
	public String typeDoc(IDocumentPayableCB documentPayableCB) {
		if(documentPayableCB!=null) {
			if(documentPayableCB instanceof TaFacture) {
				return TaFacture.TYPE_DOC;
			} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
				return TaTicketDeCaisse.TYPE_DOC;
			} else  if(documentPayableCB instanceof TaPanier) {
				return TaPanier.TYPE_DOC;
			} else if(documentPayableCB instanceof TaAcompte) {
				return TaAcompte.TYPE_DOC;
			} else  {
				throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
				//return null;
			}
		} else {
			return TaReglement.TYPE_DOC;
		}
		
	}
	
	//TODO, utiliser plus le polymophisme
	public String typeDoc(SWTDocument swtDocument) {
		if(swtDocument!=null) {
			if(swtDocument instanceof TaFacture) {
				return TaFacture.TYPE_DOC;
			} else  if(swtDocument instanceof TaTicketDeCaisse) {
				return TaTicketDeCaisse.TYPE_DOC;
			} else  if(swtDocument instanceof TaPanier) {
				return TaPanier.TYPE_DOC;
			} else  if(swtDocument instanceof TaAvisEcheance) {
				return TaAvisEcheance.TYPE_DOC;
			} else if(swtDocument instanceof TaAcompte) {
				return TaAcompte.TYPE_DOC;
			} else  {
				throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
				//return null;
			}
		} else {
			return TaReglement.TYPE_DOC;
		}
		
	}
	
	/******************************************************************************************************************************************************/
	public RequestOptions initCleStripeProgramme(TaCompteServiceWebExterne configService) {
		boolean modeTest = false;
		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();

		taParametreService.initParamBdg();
		
		RequestOptionsBuilder requestOptionsBuilder = RequestOptions.builder();
		if(modeTest) {
			//requestOptionsBuilder.setApiKey(LgrStripe.TEST_SECRET_API_KEY_PROGRAMME);
			requestOptionsBuilder.setApiKey(paramBdg.getTaParametre().getStripeTestSecretApiKeyProgramme());
		} else {
			//requestOptionsBuilder.setApiKey(LgrStripe.LIVE_SECRET_API_KEY_PROGRAMME);
			requestOptionsBuilder.setApiKey(paramBdg.getTaParametre().getStripeLiveSecretApiKeyProgramme());
		}
		
		return requestOptionsBuilder.build();
	}
	
	public RequestOptions initCleStripeDossier(TaCompteServiceWebExterne configService) {
		return initCleStripeDossier(configService,null);
	}
	
	public RequestOptions initCleStripeDossier(TaCompteServiceWebExterne configService, String connectedAccountId) {
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		TaCompteServiceWebExterne config = taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		if(config!=null) {
			config = util.decrypter(config);
			/*
			 * TODO
			 * A l'intérieur de BDG
			 * 		Dans les methodes de paiement du AbstractApplcationDAOServeur => Si pas de compte serviceWebExterne de paiement, vérifier si lien Stripe Connect
			 * 
			 * booleen modeConnect ici et dans PaiementCbController => voir comment on peu le controler
			 * 
			 * Pour les autres appels qui passe directement par les service stripe sans test d'injection
			 * refaire une verif ici sur l'existance d'un compte serviceWebExterne de paiement et si pas de compte vérifier si lien Stripe Connect
			 * 
			 * voir la priorité entre serviceWebExterne et connect pour stripe
			 * 
			 * si on utilise pas le oauth pour connect, voir si on met la clé du compte principal (legrain) dans le dossier legrain82 ou une table programme ou en dur
			 * 
			 * Finaliser la création/liaison de compte Stripe
			 * API : création de compte en double mais plus "souplesse" déconnection par BDG impossible
			 * OAuth : pas de compte en double, mais aucun paramétrage de creation de compte, déconnexion depuis BDG possible
			 * 
			 * OU =====================
			 * 
			 * TODO
			 * Arriver a faire fonctionner ensemble les compte service web externe et le système de connexion OAuth
			 * Ce qui était prévu à la base via le champs type d'autentification
			 * pour du oauth il faudrait faire apparaitre un bouton à la place des champs
			 * 
			 */
			
			System.out.println("PaiementStripeDossierService clé 1 "+ config.getApiKey1());	
			System.out.println("PaiementStripeDossierService clé 2 "+ config.getApiKey2());
			System.out.println("PaiementStripeDossierService clé 3 "+ config.getValeur1());
			System.out.println("PaiementStripeDossierService clé 4 "+ config.getValeur2());
		}
		
		
	
		
		boolean modeTest = false;
		boolean modeStripeConnect = false;
		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();

		/*
		 * TODO
		 * POUR L'INSTANT la clé utiliser est celle utiliser dans le dossier COURANT,
		 * Mais si c'est pour du CONNECT il faudrait aller chercher les clé PUBLIQUE et PRIVEE dans une base programme/plateforme 
		 * => Pour l'instant test avec des contantes dans LgrStripe mais pas assez sécurisé
		 * A FAIRE SUREMENT ici et au moins dans PaiementCbController pour la clé publique et dans EspaceClientRestMultiTenantProxy.clePubliqueStripe()
		 */
//		String API_KEY_DOSSIER_SK_LIVE = config.getApiKey1();
			
		RequestOptionsBuilder requestOptionsBuilder = RequestOptions.builder();
		/*************************************************MODIF CONNECT **/
		if(connectedAccountId!=null && !connectedAccountId.equals("")) {
			modeStripeConnect = true;
		}
		if(!modeStripeConnect) { 
			//TODO
			//il n'y a pas de compte stripe connect en paramètre, donc a priori on ne vient pas d'un webhook
			//par contre POUR L'INSTANT on FORCE stripe connect si un compte connect est paramétré dans le dossier
			/*
			 * A verifier dans PaiementCbController aussi
			 * 
			 * TODO pour les paiement connect exterieur (appli mobile et angular) il faudra prévoir de récuprer ces clés "plateforme" à la place de celle du dossier
			 */
			TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
			if(taStripeAccount.getIdExterne()!=null && !taStripeAccount.getIdExterne().equals("")) {
				//TODO faire une double vérification, vérifier si un compte stripe connect est renseigné ET si un compte stripe connect existe dans la liste des compte service service web externe actif
				connectedAccountId = taStripeAccount.getIdExterne();
				modeStripeConnect = true;
			}
		}
		if(modeStripeConnect) {
			boolean problemeCompteConnect = false;
			TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
			if(connectedAccountId!=null && !connectedAccountId.equals("")) {
				//connectedAccountId vient probablement d'un appel WebHook, il faut vérifier la concordance avec le compte connect du dossier
				if(taStripeAccount.getIdExterne().equals(connectedAccountId)) {
					//OK
				}
			}
			/*
			 * TODO Verifier la liaison chez stripe
			 * TODO mettre a jour
			 * TODO vérifier les que le compte stripe est valide (document)
			 * TODO SINON .... EXCEPTION et bloquer le paiement
			 * 
			 * TODO Si connect peut être essayer de récupérer ici les frais par rapport au tiers et au service conserné s'il y en a 
			 */
//TODO si vérifié a chaque appel avec le code si dessous boucle : api_compte => init_cle => api_compte
//			if(liaisonBDGCompteOKChezStripe(connectedAccountId, false)) {
//				//updateAccountFromStripe(null, connectedAccountId);
//				if(!taStripeAccount.getChargesEnabled() || !taStripeAccount.getDetailsSubmitted()) {
//					//compte non verifié par stripe
//					problemeCompteConnect = true;
//				}
//			} else {
//				//compte non lié
//				problemeCompteConnect = true;
//			}
			if(problemeCompteConnect) {
				//exception
			}
			
			taParametreService.initParamBdg();
			
			//**
			//connect, donc utilisation des clés programmes
			if(modeTest) {
				//requestOptionsBuilder.setApiKey(LgrStripe.TEST_SECRET_API_KEY_PROGRAMME);
				requestOptionsBuilder.setApiKey(paramBdg.getTaParametre().getStripeTestSecretApiKeyProgramme());
			} else {
				//requestOptionsBuilder.setApiKey(LgrStripe.LIVE_SECRET_API_KEY_PROGRAMME);
				requestOptionsBuilder.setApiKey(paramBdg.getTaParametre().getStripeLiveSecretApiKeyProgramme());
			}
			requestOptionsBuilder.setStripeAccount(taStripeAccount.getIdExterne());
		} else {
			//utilisation des clé API du dossier s'il y en a, sinon erreur
			if(config!=null && config.getApiKey1()!=null) {
				String API_KEY_DOSSIER_SK_LIVE = config.getApiKey1();
				requestOptionsBuilder.setApiKey(API_KEY_DOSSIER_SK_LIVE);
			} else {
				//TODO exception, pas de compte connect et aucun cle dans le dossier
			}
			
		}
		/*************************************************MODIF CONNECT **/
		
		RequestOptions requestOptions = requestOptionsBuilder.build();
		
		return requestOptions;
	}
	
	//@Override
	public Charge remontePaiement(TaCompteServiceWebExterne configService, String idPaiement) {
	
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			return Charge.retrieve(idPaiement,requestOptions);
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
		return null;
	}
	
	public String creerCarteStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String numeroCarte, int moisCarte, int anneeCarte, String cryptogrammeCarte, String nomPorteurCarte, String description) throws EJBException {
		return null;
	}
	
	public String creerCarteStripe(TaCompteServiceWebExterne configService, String carteTokenStripe)  throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			
//			Map<String, Object> sepaParams = new HashMap<String, Object>();
//			sepaParams.put("token", carteTokenStripe); 
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
//			System.out.println("LgrStripe ** Création d'une source Carte par token Stripe - pour : "+nomPersonneAPrelever);
//			Source source = Source.create(sourceParams);
//			
//			System.out.println("LgrStripe ** Création d'une source Carte par token Stripe - ID : "+source.getId());
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
			return null;
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
//		} catch (APIException e) {
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		}
	}
	
	
//	@Override
	public String creerSourcePrelevementSEPA(TaCompteServiceWebExterne configService, String iban, String nomPersonneAPrelever) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			
			Map<String, Object> sepaParams = new HashMap<String, Object>();
			sepaParams.put("iban", iban); 
//			FR1420041010050500013M02606 	The charge status transitions from pending to succeeded.
//			FR8420041010050500013M02607 	The charge status transitions from pending to failed.

			Map<String, Object> ownerParams = new HashMap<String, Object>();
			ownerParams.put("name", nomPersonneAPrelever);

			Map<String, Object> sourceParams = new HashMap<String, Object>();
			sourceParams.put("type", "sepa_debit");
			sourceParams.put("sepa_debit", sepaParams);
			sourceParams.put("currency", "eur");
			sourceParams.put("owner", ownerParams);
			
			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - pour : "+nomPersonneAPrelever);
			Source source = Source.create(sourceParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - ID : "+source.getId());
//			System.out.println("LgrStripe ** Création d'une source de prélèvement SEPA Stripe - URL Mandat : "+source.getInstanceURL());
			
			if(!source.getStatus().equals("chargeable")) {
				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return source.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	/*
	 * Pour enregistrer une carte (payment_method) sans réaliser de paiement réel
	 */
	public String creerSetupIntent(TaCompteServiceWebExterne configService) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			
			Map<String, Object> setupParams = new HashMap<String, Object>();
//			if(email!=null) {
//				setupParams.put("email", email);//"paying.user@example.com"
//			}
//			if(sourceId!=null) {
//				setupParams.put("source", sourceId);//"src_18eYalAHEMiOZZp1l9ZTjSU0"
//			}
//			if(description!=null) {
//				setupParams.put("description", description);
//			}
			
//			setupParams.put("usage", "off_session"); // The default usage is off_session
			
//			System.out.println("LgrStripe ** Création d'un setupIntent Stripe - pour : "+email);
			SetupIntent setupIntent = SetupIntent.create(setupParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un setupIntent Stripe - ID : "+setupIntent.getClientSecret());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return setupIntent.getClientSecret();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	/**
	 * @param String sourceId, sourceID (src_18eYalAHEMiOZZp1l9ZTjSU0) 
	 * <br>ou payementMethodID (pm_1EUn7Y2VYugoKSBzuKpIUIe6)
	 * <br> les sources sont déprécier pour les paiement par carte, les payment_method sont la nouvelle API compatible SCA (mais pas encore utilisable pour les prelevement SEPA)
	 */
	public String creerCustomerStripe(TaCompteServiceWebExterne configService, String email, String sourceId, String description) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			
			Map<String, Object> customerParams = new HashMap<String, Object>();
			if(email!=null) {
				customerParams.put("email", email);//"paying.user@example.com"
			}
			if(sourceId!=null) {
				if(sourceId.startsWith("src_")) {
					customerParams.put("source", sourceId);//"src_18eYalAHEMiOZZp1l9ZTjSU0"
				} else if(sourceId.startsWith("pm_")) {
					customerParams.put("payment_method", sourceId);//"pm_1EUn7Y2VYugoKSBzuKpIUIe6"
				}
			}
			
			if(description!=null) {
				customerParams.put("description", description);
			}
			
			System.out.println("LgrStripe ** Création d'un customer Stripe - pour : "+email);
			Customer customer = Customer.create(customerParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un customer Stripe - ID : "+customer.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return customer.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String attacherSourceStripe(TaCompteServiceWebExterne configService, String sourceId, String customerId) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
//			Customer customer = Customer.retrieve(customerId,requestOptions);
//			
//			Map<String, Object> params = new HashMap<String, Object>();
//			if(sourceId!=null) {
//				params.put("source", sourceId);//"src_1DjSutESo4ojJRjqUbmKo88D"
//			}
//			
//			System.out.println("LgrStripe ** Attache d'une source à un customer Stripe - : "+sourceId +" <--->"+customerId);
//			customer.getSources().create(params,requestOptions);
//			
////			System.out.println("LgrStripe ** Création d'un customer Stripe - ID : "+customer.getId());
//			
////			if(!customer.getStatus().equals("chargeable")) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
////			if(!source.getPaid()) {
////				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
////			}
			
			List<String> expandList = new ArrayList<>();
			expandList.add("sources");

			Map<String, Object> retrieveParams = new HashMap<>();
			retrieveParams.put("expand", expandList);

			Customer customer =
			  Customer.retrieve(
					  customerId,
			    retrieveParams,
			    requestOptions
			  );
			Map<String, Object> params = new HashMap<>();
			params.put("source", sourceId);
			customer.getSources().create(params,requestOptions);

			return customer.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String detacherSourceStripe(TaCompteServiceWebExterne configService, String sourceId) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			System.out.println("LgrStripe ** Detache la source Stripe -  : "+sourceId);
			Source source = Source.retrieve(sourceId,requestOptions);//"src_1DjSutESo4ojJRjqUbmKo88D"
			source.detach(/*requestOptions*/);
			
//			System.out.println("LgrStripe ** Création d'un customer Stripe - ID : "+customer.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return source.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String attacherPayementMethodStripe(TaCompteServiceWebExterne configService, String paymentMethodId, String customerId) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
//			Customer customer = Customer.retrieve(customerId,requestOptions);
			PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId,requestOptions);
			
			Map<String, Object> params = new HashMap<String, Object>();
			if(customerId!=null) {
				params.put("customer", customerId);//"cus_F2dPL0zO7Uk9ya"
			}
			
			System.out.println("LgrStripe ** Attache d'une methode de paiement à un customer Stripe - : "+paymentMethodId +" <--->"+customerId);
			
			paymentMethod.attach(params,requestOptions);

			return paymentMethod.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String detacherPaymentMethodStripe(TaCompteServiceWebExterne configService, String paymentMethodId) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			System.out.println("LgrStripe ** Detache la methode de paiement Stripe -  : "+paymentMethodId);
			PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId,requestOptions);//"pm_1EUn7Y2VYugoKSBzuKpIUIe6"
			paymentMethod.detach(requestOptions);


			return paymentMethod.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String creerProductStripe(TaCompteServiceWebExterne configService, String name, String type) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			
			Map<String, Object> productParams = new HashMap<String, Object>();
			if(name!=null) {
				productParams.put("name", name);//"Monthly membership base fee"
			}
			if(type!=null) {
				productParams.put("type", "service");//"service" ou "good"
			}
			
			System.out.println("LgrStripe ** Création d'un product Stripe - pour : "+name);
			Product product = Product.create(productParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un product Stripe - ID : "+product.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return product.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String creerPlanStripe(TaCompteServiceWebExterne configService, String nickname, String interval, String currency, BigDecimal montant, String productStripeID) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
			
//			Map<String, Object> productParams = new HashMap<String, Object>();
//			productParams.put("name", "Gold special");
//
//			Map<String, Object> planParams = new HashMap<String, Object>();
//			planParams.put("amount", 5000);
//			planParams.put("interval", "month");
//			planParams.put("product", "productParams"); //prod_E0sue83NPHQEDK
//			planParams.put("currency", "usd");
			
			Map<String, Object> planParams = new HashMap<String, Object>();
			if(currency!=null) {
				planParams.put("currency", "eur");//"obligatoire
			}
			if(interval!=null) {
				planParams.put("interval", interval);//day, week, month or year
			}
			if(productStripeID!=null) {
				planParams.put("product", productStripeID);//id product (ex:Map<String, Object> productParams ) ou une map pour créer un nouveau produit
			}
			if(montant!=null) { //obligatoire si billing_scheme=per_unit, correspond au prix (5000 pour 50,00€)
				planParams.put("amount", amount);
			}
			if(nickname!=null) { //facultatif
				planParams.put("nickname", nickname);
			}
			
			System.out.println("LgrStripe ** Création d'un plan Stripe - pour le produit : "+productStripeID);
			Plan plan = Plan.create(planParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un plan Stripe - ID : "+plan.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return plan.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String creerSubscriptionStripe(TaCompteServiceWebExterne configService, String customerStripeID, Map<String,Integer> mapSubscriptionItemPlanStripeIDQuantity,
			String sourceStripeID, Integer timpeStampBillingCycleAnchor, String billing, Integer daysUntilDue) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Map<String, Object> params = new HashMap<String, Object>();
			if(customerStripeID!=null) {
				params.put("customer", customerStripeID);//cus_ENkir6GZT1coas
			}
			
			if(sourceStripeID!=null) {
				params.put("default_source", sourceStripeID);//src_1DjSutESo4ojJRjqUbmKo88D
			}
			
			if(timpeStampBillingCycleAnchor!=null) {
				params.put("billing_cycle_anchor", timpeStampBillingCycleAnchor);//1549623023
			}
			
			if(billing!=null) {
				params.put("billing", billing);//charge_automatically, or send_invoice
			}
			
			if(daysUntilDue!=null) {
				params.put("days_until_due", daysUntilDue);
			}
			
			Map<String, Object> items = new HashMap<String, Object>();
			Map<String, Object> item = null;
			if(mapSubscriptionItemPlanStripeIDQuantity!=null) {
				int i = 0;
				for (String planStripeID : mapSubscriptionItemPlanStripeIDQuantity.keySet()) {
					item = new HashMap<String, Object>();
					item.put("plan", planStripeID);//plan_ENkjvo6pZIX2og
					item.put("quantity", mapSubscriptionItemPlanStripeIDQuantity.get(planStripeID));
					items.put(LibConversion.integerToString(i),item); //items.put("0", item);
				}
				params.put("items", items);
			}
			
			
			System.out.println("LgrStripe ** Création d'une subscription Stripe - pour le customer : "+customerStripeID);
			Subscription subscription = Subscription.create(params,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un subscription Stripe - ID : "+subscription.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return subscription.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String annulerSubscriptionStripe(TaCompteServiceWebExterne configService, String subscriptionStripeID) throws EJBException {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Subscription sub = Subscription.retrieve(subscriptionStripeID,requestOptions); //sub_ENkpQA7eMGNU3U
			
			System.out.println("LgrStripe ** Annulation d'une subscription Stripe - pour la subscription : "+subscriptionStripeID);
			SubscriptionCancelParams p = SubscriptionCancelParams.builder().build();
			Subscription subscription = sub.cancel(p,requestOptions);
			
			System.out.println("LgrStripe ** Annulation d'une subscription Stripe - ID : "+subscription.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return subscription.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String creerInvoiceStripe(TaCompteServiceWebExterne configService, String customerStripeID) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
//			Map<String, Object> invoiceParams = new HashMap<String, Object>();
//			invoiceParams.put("customer", "cus_E0s4xZ5iCBzyaP");
//
//			Invoice.create(invoiceParams);
			
			Map<String, Object> invoiceParams = new HashMap<String, Object>();
			if(customerStripeID!=null) {
				invoiceParams.put("customerStripeID", customerStripeID);//"obligatoire
			}
			
			
			System.out.println("LgrStripe ** Création d'un invoice Stripe - pour le customer : "+customerStripeID);
			Invoice invoice = Invoice.create(invoiceParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un invoice Stripe - ID : "+invoice.getId());


			return invoice.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String creerInvoiceItemStripe(TaCompteServiceWebExterne configService, String customerStripeID, String description, BigDecimal montant) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
			
//			Map<String, Object> invoiceItemParams = new HashMap<String, Object>();
//			invoiceItemParams.put("customer", "cus_E0s4xZ5iCBzyaP");
//			invoiceItemParams.put("amount", 2500);
//			invoiceItemParams.put("currency", "usd");
//			invoiceItemParams.put("description", "One-time setup fee");
			
			String currency = "eur";
			
			Map<String, Object> invoiceItemParams = new HashMap<String, Object>();
			if(customerStripeID!=null) {
				invoiceItemParams.put("customerStripeID", customerStripeID);//"obligatoire
			}
			if(currency!=null) {
				invoiceItemParams.put("currency", currency);//"obligatoire
			}
			if(description!=null) {
				invoiceItemParams.put("description", description);//"obligatoire
			}
			if(montant!=null) {
				invoiceItemParams.put("amount", amount);//"obligatoire
			}
			
			
			System.out.println("LgrStripe ** Création d'un invoice Stripe - pour le customer : "+customerStripeID);
			InvoiceItem invoiceItem = InvoiceItem.create(invoiceItemParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un invoice Stripe - ID : "+invoiceItem.getId());


			return invoiceItem.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public int montantBdgVersStripe(BigDecimal montant) {
		int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
		return amount;
	}
	
	public BigDecimal montantStripeVersBdg(Long montant) {
		BigDecimal amount = new BigDecimal(montant).divide(new BigDecimal(100));
		return amount;
	}
	
	public Long dateBdgVersStripe(Date montant) {
		//int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
		return montant.getTime();
//		return amount;
	}
	
	public Date dateStripeVersBdg(Long montant) {
//		int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
		if(montant!=null)
			return new Date(TimeUnit.SECONDS.toMillis(montant));
		else
			return null;
	}
	
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscription(TaCompteServiceWebExterne configService, String subscriptionStripeID, Integer limite) throws EJBException {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Map<String, Object> invoiceParams = new HashMap<String, Object>();
			if(limite!=null) {
				invoiceParams.put("limit", LibConversion.integerToString(limite));
			}
			invoiceParams.put("subscription", subscriptionStripeID);

			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour la subscription : "+subscriptionStripeID);
			InvoiceCollection invoiceCollection = Invoice.list(invoiceParams,requestOptions);
			
			List<TaStripeInvoiceDTO> result = new ArrayList<>();
			TaStripeInvoiceDTO taStripeInvoiceDTO = null;
			for (Invoice inv : invoiceCollection.getData()) {
				taStripeInvoiceDTO = new TaStripeInvoiceDTO();
				taStripeInvoiceDTO.setIdExterne(inv.getId());
				result.add(taStripeInvoiceDTO);
				
				if(synchroniserStripeVersBdg) {
					updateInvoiceFromStripe(configService, inv, subscriptionStripeID);
				}
			}
			
			
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return result;
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public TaStripeInvoice updateInvoiceFromStripe(TaCompteServiceWebExterne configService, String invoiceStripeID, String subscriptionStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Invoice c = Invoice.retrieve(invoiceStripeID,requestOptions);//"in_1DuzK8ESo4ojJRjqZB51noQ3"
			return updateInvoiceFromStripe(configService,c,subscriptionStripeID);
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private TaStripeInvoice updateInvoiceFromStripe(TaCompteServiceWebExterne configService, Invoice inv, String subscriptionStripeID) {
		//récupération et enregistrement des informations de Stripe vers Bdg
		TaStripeInvoice taStripeInvoice = null;
		try {
			taStripeInvoice = taStripeInvoiceService.findByCode(inv.getId());
			if(taStripeInvoice==null) {
				taStripeInvoice = new TaStripeInvoice();
				taStripeInvoice.setIdExterne(inv.getId());
				taStripeInvoice.setItems(new HashSet<>());
			}
			taStripeInvoice.setAmountDue(montantStripeVersBdg(inv.getAmountDue()));
			taStripeInvoice.setNumber(inv.getNumber());
			taStripeInvoice.setDueDate(dateStripeVersBdg(inv.getDueDate()));
			taStripeInvoice.setCreated(dateStripeVersBdg(inv.getCreated()));
			if(inv.getCharge()!=null) {
				//taStripeInvoice.setTaStripeCharge(taStripeChargeService.);
			}
			taStripeInvoice.setPaid(inv.getPaid());
			taStripeInvoice.setPeriodStart(dateStripeVersBdg(inv.getPeriodStart()));
			taStripeInvoice.setPeriodEnd(dateStripeVersBdg(inv.getPeriodEnd()));
			
			taStripeInvoice.setAttemptCount(inv.getAttemptCount().intValue());
			taStripeInvoice.setAttempted(inv.getAttempted());
			//taStripeInvoice.setBilling(inv.getBilling()); //non dispo depuis le passage a l'api 20.35.0
			taStripeInvoice.setBillingReason(inv.getBillingReason());
			taStripeInvoice.setDescription(inv.getDescription());
			taStripeInvoice.setNexPaymentAttempt(dateStripeVersBdg(inv.getNextPaymentAttempt()));
			taStripeInvoice.setWebhooksDeliveredAt(dateStripeVersBdg(inv.getWebhooksDeliveredAt()));
	//		if(inv.getDiscount()!=null) {
	//			taStripeInvoice.setTaStripeCoupon(inv.getcPeriodEnd());
	//		}
			//taStripeInvoice.setTaxe(inv.getTaxPercent()); //non dispo depuis le passage a l'api 20.35.0
			//taStripeInvoice.setStatus(inv.getStatus());// mettre à jour l'API vers 7.18.0 ?
			//TaAvisEcheance id
			//TaReglement id
			
			for (InvoiceLineItem invItem : inv.getLines().getData()) {
				TaStripeInvoiceItem taStripeInvoiceItem = taStripeInvoiceItemService.findByCode(invItem.getId());
				if(taStripeInvoiceItem==null) {
					taStripeInvoiceItem = new TaStripeInvoiceItem();
					taStripeInvoiceItem.setIdExterne(invItem.getId());
					taStripeInvoiceItem.setTaStripeInvoice(taStripeInvoice);
				}
				
				taStripeInvoiceItem.setTaPlan(taStripePlanService.findByCode(invItem.getPlan().getId()));

				taStripeInvoice.getItems().add(taStripeInvoiceItem);
			}
			
			if(subscriptionStripeID!=null) {
				//taStripeInvoice.setTaSubscription(taStripeSubscriptionService.findByCode(subscriptionStripeID));
				taStripeInvoice.setTaAbonnement(taAbonnementService.findByCode(subscriptionStripeID));
			} else if(inv.getSubscription()!=null) {
				//taStripeInvoice.setTaSubscription(taStripeSubscriptionService.findByCode(inv.getSubscription()));
				taStripeInvoice.setTaAbonnement(taAbonnementService.findByCode(inv.getSubscription()));
			}
			
			//taStripeInvoice.setTaCustomer(taStripeInvoice.getTaSubscription().getTaStripeCustomer());
			taStripeInvoice.setTaCustomer(taStripeInvoice.getTaAbonnement().getTaStripeCustomer());
			
			taStripeInvoice = taStripeInvoiceService.merge(taStripeInvoice);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return taStripeInvoice;
	}
	
	public List<TaStripeInvoiceDTO>rechercheInvoiceStripeForCustomer(TaCompteServiceWebExterne configService, String customerStripeID, Integer limite) throws EJBException {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Map<String, Object> invoiceParams = new HashMap<String, Object>();
			if(limite!=null) {
				invoiceParams.put("limit", LibConversion.integerToString(limite));
			}
			invoiceParams.put("customer", customerStripeID);
			
			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour le customer : "+customerStripeID);
			InvoiceCollection invoiceCollection = Invoice.list(invoiceParams,requestOptions);
			
			List<TaStripeInvoiceDTO> result = new ArrayList<>();
			TaStripeInvoiceDTO taStripeInvoiceDTO = null;
			for (Invoice inv : invoiceCollection.getData()) {
				taStripeInvoiceDTO = new TaStripeInvoiceDTO();
				taStripeInvoiceDTO.setIdExterne(inv.getId());
				result.add(taStripeInvoiceDTO);
				
				if(synchroniserStripeVersBdg) {
					updateInvoiceFromStripe(configService,inv, null);
				}
			}
			
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return result;
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
//	public List<TaStripeSubscriptionDTO>rechercheSubscriptionStripeForCustomer(TaCompteServiceWebExterne configService, String customerStripeID, Integer limite, String status) throws EJBException {
//		try {
//			RequestOptions requestOptions = initCleStripeDossier(configService);
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
//			SubscriptionCollection subscriptionCollection = Subscription.list(subscriptionParams,requestOptions);
//			
//			List<TaStripeSubscriptionDTO> result = new ArrayList<>();
//			TaStripeSubscriptionDTO taStripeSubscriptionDTO = null;
//			for (Subscription sub : subscriptionCollection.getData()) {
//				taStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//				taStripeSubscriptionDTO.setIdExterne(sub.getId());
//				result.add(taStripeSubscriptionDTO);
//				
//				if(synchroniserStripeVersBdg) {
//					updateSubscriptionFromStripe(configService,sub);
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
	
//	public TaStripeSubscription updateSubscriptionFromStripe(TaCompteServiceWebExterne configService, String subscriptionStripeID) {
//		try {
//			RequestOptions requestOptions = initCleStripeDossier(configService);
//			
//			Subscription c = Subscription.retrieve(subscriptionStripeID,requestOptions);//"sub_ENkpQA7eMGNU3U"
//			return updateSubscriptionFromStripe(configService,c);
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
	
//	private TaStripeSubscription updateSubscriptionFromStripe(TaCompteServiceWebExterne configService, Subscription sub) {
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
//			for (SubscriptionItem subItem : sub.getItems().getData()) {
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
	
	public TaStripeCharge updateChargeFromStripe(TaCompteServiceWebExterne configService, String chargeStripeID, String connectedAccountId) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService,connectedAccountId);
			
			Charge c = Charge.retrieve(chargeStripeID,requestOptions);//"ch_Dr8U8upUHIEQwG"
			return updateChargeFromStripe(configService,c);
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	private TaStripeCharge updateChargeFromStripe(TaCompteServiceWebExterne configService, Charge ch) {
		//récupération et enregistrement des informations de Stripe vers Bdg
		TaStripeCharge taStripeCharge = null;
		try {
			taStripeCharge = taStripeChargeService.findByCode(ch.getId());

			if(taStripeCharge==null) {
				taStripeCharge = new TaStripeCharge();
				taStripeCharge.setIdExterne(ch.getId());
//				taStripeCharge.setItems(new HashSet<>());
			}
			boolean changementStatus = true;
			if(taStripeCharge.getStatus()!=null) {
				changementStatus = (!taStripeCharge.getStatus().equals(ch.getStatus())) || taStripeCharge.getStatus().equals("succeeded"); 
			}
			taStripeCharge.setCaptured(ch.getCaptured());
			taStripeCharge.setStatus(ch.getStatus());
			if(changementStatus && taStripeCharge.getTaReglement()!=null) { 
				TaEtat taEtatReglement = null;
				if(taStripeCharge.getStatus().equals("succeeded")) {
					if(taStripeCharge.getCaptured()) {
						taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_VALIDE);
					} else {
						taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_ENCOURS);
					}
				} else if(taStripeCharge.getStatus().equals("pending")) {
					taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_ENCOURS);
				} else if(taStripeCharge.getStatus().equals("failed")) {
					taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_REJETE);
				}
				taStripeCharge.getTaReglement().setTaEtat(taEtatReglement);
				taReglementService.enregistrerMerge(taStripeCharge.getTaReglement());
			}
			
			taStripeCharge.setDateCharge(dateStripeVersBdg(ch.getCreated()));
			taStripeCharge.setAmount(montantStripeVersBdg(ch.getAmount()));
			if(ch.getCustomer()!=null) {
				taStripeCharge.setTaStripeCustomer(taStripeCustomerService.rechercherCustomer(ch.getCustomer()));
			}
			if(ch.getInvoice()!=null) {
				taStripeCharge.setTaStripeInvoice(taStripeInvoiceService.findByCode(ch.getInvoice()));
			}
			if(ch.getSource()!=null && ch.getSource().getId()!=null) {
				taStripeCharge.setTaStripeSource(taStripeSourceService.rechercherSource(ch.getSource().getId()));
			}
			taStripeCharge.setPaid(ch.getPaid());
			taStripeCharge.setDescription(ch.getDescription());
			taStripeCharge.setRefunded(ch.getRefunded());
			
//			taStripeCharge.setAmountRefunded(ch.getAmountRefunded());
//			taStripeCharge.setAmountCaptured(ch.getAmountCaptured());
			
			taStripeCharge = taStripeChargeService.merge(taStripeCharge);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return taStripeCharge;
	}
	/**
	 * @author yann
	 * @param tenant
	 * @param configService
	 * @param paymentIntentStripeID
	 * @param connectedAccountId
	 * @return
	 */
	public TaStripePaymentIntent updatePaymentIntentFromStripe(String tenant,TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId) {
		try {
//			if(tenant!=null) {
//				initTenantRegistry();
//				initTenant(tenant);
//			}
			RequestOptions requestOptions = initCleStripeDossier(configService,connectedAccountId);
			
			PaymentIntent pi = PaymentIntent.retrieve(paymentIntentStripeID,requestOptions);//"ch_Dr8U8upUHIEQwG"
			return updatePaymentIntentFromStripe(configService,pi);
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	public TaStripePaymentIntent updatePaymentIntentFromStripe(TaCompteServiceWebExterne configService, String paymentIntentStripeID, String connectedAccountId) {
		return updatePaymentIntentFromStripe( null, configService,  paymentIntentStripeID,  connectedAccountId);
			
	}

	private TaStripePaymentIntent updatePaymentIntentFromStripe(TaCompteServiceWebExterne configService, PaymentIntent pi) {
		//récupération et enregistrement des informations de Stripe vers Bdg
		
				TaStripePaymentIntent taStripePaymentIntent = null;
				try {
					taStripePaymentIntent = taStripePaymentIntentService.findByCode(pi.getId());

					if(taStripePaymentIntent==null) {
						taStripePaymentIntent = new TaStripePaymentIntent();
						taStripePaymentIntent.setIdExterne(pi.getId());
//						taStripeCharge.setItems(new HashSet<>());
					}
					boolean changementStatus = true;
					
					if(taStripePaymentIntent.getStatus()!=null) {
						changementStatus = !taStripePaymentIntent.getStatus().equals(pi.getStatus()); 
					}
					changementStatus = true;
					taStripePaymentIntent.setStatus(pi.getStatus());
					if(changementStatus && taStripePaymentIntent.getTaReglement()!=null) { 
						TaEtat taEtatReglement = null;
						if(taStripePaymentIntent.getStatus().equals("succeeded")) {
							taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_VALIDE);
						} else if(taStripePaymentIntent.getStatus().equals("requires_capture")) {
							taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_ENCOURS);
						} else if(taStripePaymentIntent.getStatus().equals("pending")) {
							taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_ENCOURS);
						} else if(taStripePaymentIntent.getStatus().equals("failed")) {
							taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_REJETE);
						}
						taStripePaymentIntent.getTaReglement().setTaEtat(taEtatReglement);
						TaReglement r = taReglementService.enregistrerMerge(taStripePaymentIntent.getTaReglement());
						taStripePaymentIntent.setTaReglement(r);
//						taStripePaymentIntent = taStripePaymentIntentService.findByCode(pi.getId());
//						taStripePaymentIntent.setStatus(pi.getStatus());
					}
					
					boolean paid = false;
					boolean captured = false;
					BigDecimal amountCaptured = null;
					for(Charge c : pi.getCharges().getData()) {
						//dans une utilisation BDG il ne devrait y avoir qu'une seule charge pour un paymentIntent
						paid = c.getPaid();
						captured = c.getCaptured();
//						amountCaptured = montantStripeVersBdg(c.getAmountCaptured());
					}
					
					taStripePaymentIntent.setCaptured(captured);
					taStripePaymentIntent.setPaid(paid);
					taStripePaymentIntent.setAmountReceived(montantStripeVersBdg(pi.getAmountReceived()));
					
					taStripePaymentIntent.setDateIntent(dateStripeVersBdg(pi.getCreated()));
					taStripePaymentIntent.setAmount(montantStripeVersBdg(pi.getAmount()));
					taStripePaymentIntent.setCaptureMethod(pi.getCaptureMethod());
					taStripePaymentIntent.setAmountCapturable(montantStripeVersBdg(pi.getAmountCapturable()));
					taStripePaymentIntent.setEmailTicket(pi.getReceiptEmail());
					taStripePaymentIntent.setIdExternePaymentMethod(pi.getPaymentMethod());
					if(pi.getCustomer()!=null) {
						taStripePaymentIntent.setTaStripeCustomer(taStripeCustomerService.rechercherCustomer(pi.getCustomer()));
					}
					if(pi.getCharges()!=null) {
						List<TaStripeChargeDTO> result = new ArrayList<>();
						TaStripeChargeDTO taStripeChargeDTO = null;
						for (Charge ch : pi.getCharges().getData()) {
							taStripeChargeDTO = new TaStripeChargeDTO();
							taStripeChargeDTO.setIdExterne(ch.getId());
							result.add(taStripeChargeDTO);
							
							if(synchroniserStripeVersBdg) {
								updateChargeFromStripe(configService,ch);
							}
						}
					}
					if(pi.getSourceObject()!=null && pi.getSourceObject().getId()!=null) {
						taStripePaymentIntent.setTaStripeSource(taStripeSourceService.rechercherSource(pi.getSourceObject().getId()));
					}
//					taStripePaymentIntent.setPaid(pi.getPaid());
//					taStripePaymentIntent.setDescription(pi.getDescription());
//					taStripePaymentIntent.setRefunded(pi.getRefunded());
					

					
					taStripePaymentIntent = taStripePaymentIntentService.merge(taStripePaymentIntent);
				} catch(Exception e) {
					e.printStackTrace();
				}
				return taStripePaymentIntent;
	}
	
	
	public TaStripePaymentIntentDTO rechercherPaymentIntent(TaCompteServiceWebExterne configService, String paymentIntentStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			PaymentIntent pi = PaymentIntent.retrieve(paymentIntentStripeID,requestOptions);//"ch_Dr8U8upUHIEQwG"
			TaStripePaymentIntentDTO a = new TaStripePaymentIntentDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public TaStripePlan updatePlanFromStripe(TaCompteServiceWebExterne configService, String planStripeID) {
		
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Plan c = Plan.retrieve(planStripeID,requestOptions);//"cus_Dr8U8upUHIEQwG"
			return updatePlanFromStripe(configService,c);
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	private TaStripePlan updatePlanFromStripe(TaCompteServiceWebExterne configService, Plan pl) {
		//récupération et enregistrement des informations de Stripe vers Bdg
		TaStripePlan taStripePlan = null;
		try {
			taStripePlan = taStripePlanService.findByCode(pl.getId());
			if(taStripePlan==null) {
				taStripePlan = new TaStripePlan();
				taStripePlan.setIdExterne(pl.getId());
//				taStripeCharge.setItems(new HashSet<>());
			}
			taStripePlan.setActive(pl.getActive());
//			taStripePlan.setDeleted(pl.getDeleted());
			if(pl.getProduct()!=null) {
				taStripePlan.setTaStripeProduct(taStripeProductService.rechercherProduct(pl.getProduct()));
			}
			taStripePlan = taStripePlanService.merge(taStripePlan);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return taStripePlan;
	}
	public List<TaStripeChargeDTO>rechercheChargeStripe(TaCompteServiceWebExterne configService, Integer limite, Map<String,String> sourceType) throws EJBException {
		return rechercheChargeStripeForCustomer(configService,null,limite,sourceType);
	}
	
	public List<TaStripeChargeDTO>rechercheChargeStripeForCustomer(TaCompteServiceWebExterne configService, String customerStripeID, Integer limite, Map<String,String> sourceType) throws EJBException {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			if(limite!=null) {
				chargeParams.put("limit", LibConversion.integerToString(limite));
			}
			if(sourceType!=null) {
				chargeParams.put(" source", LibConversion.integerToString(limite)); //all, alipay_account, bank_account, bitcoin_receiver, or card
			}
			if(customerStripeID!=null) {
				chargeParams.put("customer", customerStripeID);
			}
			
			//System.out.println("LgrStripe ** Recherche des subscriptions Stripe - pour le customer : "+customerStripeID);
			ChargeCollection chargeCollection = Charge.list(chargeParams,requestOptions);
			
			List<TaStripeChargeDTO> result = new ArrayList<>();
			TaStripeChargeDTO taStripeChargeDTO = null;
			for (Charge ch : chargeCollection.getData()) {
				taStripeChargeDTO = new TaStripeChargeDTO();
				taStripeChargeDTO.setIdExterne(ch.getId());
				result.add(taStripeChargeDTO);
				
				if(synchroniserStripeVersBdg) {
					updateChargeFromStripe(configService,ch);
				}
			}
			
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - ID : "+subscription.getId());
			
//			if(!customer.getStatus().equals("chargeable")) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//			if(!source.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}

			return result;
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String creerCouponStripe(TaCompteServiceWebExterne configService, String duration, Integer durationInMonths, String id, BigDecimal percentOff/*, BigDecimal amountOff*/) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
//			Map<String, Object> couponParams = new HashMap<String, Object>();
//			couponParams.put("percent_off", 25);
//			couponParams.put("duration", "repeating");
//			couponParams.put("duration_in_months", 3);
//			couponParams.put("id", "25OFF");

			
			Map<String, Object> couponParams = new HashMap<String, Object>();
			if(duration!=null) {
				couponParams.put("duration", "repeating");//"obligatoire : forever, once, or repeating
			}
			if(durationInMonths!=null) {
				couponParams.put("duration_in_months", 3); // 3 mois
			}
			if(id!=null) {
				couponParams.put("id", id); //"25OFF"
			}
			if(percentOff!=null) {
				couponParams.put("percent_off", percentOff); //25
			}
//			if(amountOff!=null) {
//				couponParams.put("amount_off", amountOff); 
//			}
			
			System.out.println("LgrStripe ** Création d'une coupon Stripe ");
			Coupon coupon = Coupon.create(couponParams,requestOptions);
			
			System.out.println("LgrStripe ** Création d'un coupon Stripe - ID : "+coupon.getId());


			return coupon.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public String payerParPrelevementSEPA(TaCompteServiceWebExterne configService, BigDecimal montant, String customerStripeID, String sourceStripeID) throws EJBException{
		return payerAvecSourceStripe(configService,montant,customerStripeID,sourceStripeID,null);
	}
	
	public String payerAvecSourceStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String customerStripeID, String sourceStripeID, String description) throws EJBException{
		//IDocumentPayableCB documentPayableCB, TaTiers taTiers, String originePaiement
		return payerAvecSourceStripe(configService,montant,customerStripeID,sourceStripeID,description,null,null,null);
	}
	
	/**
	 * @param String sourceId, sourceID (src_18eYalAHEMiOZZp1l9ZTjSU0) 
	 * <br>ou payementMethodID (pm_1EUn7Y2VYugoKSBzuKpIUIe6)
	 * <br> les sources sont déprécier pour les paiement par carte, les payment_method sont la nouvelle API compatible SCA (mais pas encore utilisable pour les prelevement SEPA)
	 * <br> Pour les paiement par carte avec un payment_method, le paiement Stripe est off-session dans ce cas
	 */
	public String payerAvecSourceStripe(TaCompteServiceWebExterne configService, BigDecimal montant, String customerStripeID, String sourceStripeID, String description, IDocumentPayableCB documentPayableCB, TaTiers taTiers, String originePaiement) throws EJBException{
		try {
			
			String TYPE_PAIEMENT_DEFAUT = "CB";
			TaStripeSource taStripeSource = taStripeSourceService.rechercherSource(sourceStripeID);
			
			TaEtat taEtatReglement = null;
			
			Map<String, Object> chargeParams = new HashMap<String, Object>();
			Map<String, Object> paymentIntentParams = new HashMap<String, Object>();
			
			int amount = montant.multiply(BigDecimal.valueOf(100)).intValue();
			
			if(taStripeSource.getTaCarteBancaire()!=null) {
				//paiement par carte
				TaPreferences prefCB;
				try {
					prefCB = taPreferencesService.findByCode("paiement_cb");
					if(prefCB!=null) TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//https://stripe.com/docs/payments/cards/charging-saved-cards
				paymentIntentParams.put("amount", amount);
				paymentIntentParams.put("currency", "eur");
				ArrayList payment_method_types = new ArrayList();
				payment_method_types.add("card");
				paymentIntentParams.put("payment_method_types", payment_method_types);
				paymentIntentParams.put("customer", customerStripeID);
				paymentIntentParams.put("payment_method", sourceStripeID); //pm_1EUn7Y2VYugoKSBzuKpIUIe6
				paymentIntentParams.put("off_session", true);
				paymentIntentParams.put("confirm", true);
			} else if(taStripeSource.getTaCompteBanque()!=null) {
				//paiement par prélèvement
				TYPE_PAIEMENT_DEFAUT = "PREL";
				
				chargeParams.put("amount", amount);//1099
				chargeParams.put("currency", "eur");
				chargeParams.put("customer", customerStripeID);//"cus_AFGbOSiITuJVDs"
				chargeParams.put("source", sourceStripeID);//"src_18eYalAHEMiOZZp1l9ZTjSU0"
				if(description!=null && !description.equals("")) {
					chargeParams.put("description", description);
				}
			}
			
			
			TaLogPaiementInternet log = new TaLogPaiementInternet();
			Date datePaiement = new Date();
			
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			
			
			
			/*
			 By default, your account’s statement descriptor appears on customer statements whenever you create a SEPA Direct Debit payment. 
			 If you need to provide a custom description for a payment, include the statement_descriptor parameter when making a charge request. 
			 Statement descriptors are limited to 22 characters and cannot use the special characters <, >, ', or ".
			 */
			
			log.setMontantPaiement(montant);
			log.setDevise("eur");
			
			Map<String, String> initialMetadata = new HashMap<String, String>();
			
			if(documentPayableCB!=null) {
				
				documentPayableCB = findById(documentPayableCB);
				
				initialMetadata.put("Numero "+typeDoc(documentPayableCB), documentPayableCB.getCodeDocument()!=null?documentPayableCB.getCodeDocument():".");
				initialMetadata.put("Date "+typeDoc(documentPayableCB), documentPayableCB.getDateDocument()!=null?LibDate.dateToString(documentPayableCB.getDateDocument()):".");
				initialMetadata.put("Montant TTC "+typeDoc(documentPayableCB), documentPayableCB.getMtTtcCalc()!=null?documentPayableCB.getMtTtcCalc().toString():".");
				initialMetadata.put("Code tiers", documentPayableCB.getTaTiers().getCodeTiers()!=null?documentPayableCB.getTaTiers().getCodeTiers():".");
				initialMetadata.put("Nom tiers", documentPayableCB.getTaTiers().getNomTiers()!=null?documentPayableCB.getTaTiers().getNomTiers():".");
				initialMetadata.put("Prenom tiers", (documentPayableCB.getTaTiers().getPrenomTiers()!=null && !documentPayableCB.getTaTiers().getPrenomTiers().equals(""))?documentPayableCB.getTaTiers().getPrenomTiers():".");
				if(documentPayableCB.getTaTiers().getTaEntreprise()!=null) {
					initialMetadata.put("Raison sociale", documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise()!=null?documentPayableCB.getTaTiers().getTaEntreprise().getNomEntreprise():"");
				}
				
				log.setCodeDocument(documentPayableCB.getCodeDocument());
				log.setIdDocument(documentPayableCB.getIdDocument());
				log.setTypeDocument(typeDoc(documentPayableCB));
				log.setIdTiers(documentPayableCB.getTaTiers().getIdTiers());
				log.setCodeTiers(documentPayableCB.getTaTiers().getCodeTiers());
				log.setNomTiers(documentPayableCB.getTaTiers().getNomTiers());
			} 
			initialMetadata.put("Origine", originePaiement!=null?originePaiement:".");
					
			log.setServicePaiement(PaiementLgr.TYPE_STRIPE);
			log.setOriginePaiement(originePaiement);//log.setOriginePaiement(paiementCarteBancaire.getOriginePaiement());
			
			chargeParams.put("metadata", initialMetadata);
			paymentIntentParams.put("metadata", initialMetadata);
			
			PaymentIntent paymentIntent = null;
			Charge charge = null;
			if(taStripeSource.getTaCarteBancaire()!=null) { //paiement par carte
				System.out.println("LgrStripe ** Création d'un paiement par carte off-session - montant : "+montant);
				paymentIntent = PaymentIntent.create(paymentIntentParams,requestOptions);
				System.out.println("LgrStripe ** Création d'un paiement par carte off-session - ID : "+paymentIntent.getId());
			} else if(taStripeSource.getTaCompteBanque()!=null) {
				System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - montant : "+montant);
				charge = Charge.create(chargeParams,requestOptions);
				System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - ID : "+charge.getId());
			}
			
			System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - customer : "+customerStripeID);
			System.out.println("LgrStripe ** Création d'un prélèvement SEPA Stripe - source : "+sourceStripeID);
			
			if(taStripeSource.getTaCarteBancaire()!=null) { //paiement par carte
				if(!paymentIntent.getStatus().equals("succeeded")) {
					throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
				} else {
					taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_VALIDE);
				}
			} else if(taStripeSource.getTaCompteBanque()!=null) { //paiement par prélèvement SEPA
				if(!charge.getStatus().equals("pending")) {
					throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
				} else {
					taEtatReglement = taEtatService.findByCode(TaEtat.ETAT_REG_ENCOURS);
				}
			}
//			if(!charge.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
			
			log.setRefPaiementService(charge.getId());
			log.setDatePaiement(datePaiement);
			
			//création du règlement
			TaReglement r = new TaReglement();
			r.setNetTtcCalc(montant);
			r.setCodeDocument(taReglementService.genereCode(null));
			r.setLibelleDocument("Paiement Stripe "+TYPE_PAIEMENT_DEFAUT);//r.setLibelleDocument("Paiement CB");
			r.setDateDocument(datePaiement);
			r.setDateLivDocument(datePaiement);
			r.setTaTPaiement(taTPaiementService.findByCode(TYPE_PAIEMENT_DEFAUT));
			
			r.setTaEtat(taEtatReglement);
			
			r.setService(PaiementLgr.TYPE_STRIPE);
			r.setCompteClient(false); //r.setCompteClient(paiementCarteBancaire.isCompteClient());
			r.setRefPaiement(charge.getId());
			
			//r = taReglementService.merge(r);
				
			//affecter le réglement au document
			if(documentPayableCB!=null) {	
				r.setTaTiers(documentPayableCB.getTaTiers());
				
				TaRReglement rr = new TaRReglement();
				rr.setAffectation(montant);
				rr.setEtat(IHMEtat.integre);
				//rr.setTaFacture(documentPayableCB);
				if(documentPayableCB instanceof TaFacture) {
					rr.setTaFacture((TaFacture) documentPayableCB);
				} else  if(documentPayableCB instanceof TaTicketDeCaisse) {
					rr.setTaTicketDeCaisse((TaTicketDeCaisse) documentPayableCB);
				} else if(documentPayableCB instanceof TaAcompte) {
					//rr.setTaAcompte(documentPayableCB);
					throw new EJBException("Une erreur est survenue : Type de document non pris en charge actuellement pour le paiement par carte.");
				} 
				rr.setTaReglement(r);
				if(documentPayableCB.getTaRReglements()==null) {
					documentPayableCB.setTaRReglements(new HashSet<>());
				}
				documentPayableCB.addRReglement(rr);
				r.addRReglement(rr);
			} else if(taTiers!=null) {
				r.setTaTiers(taTiers);
			} else if(taTiers==null) {
				r.setTaTiers(taStripeCustomerService.rechercherCustomer(customerStripeID).getTaTiers());
			}
			
			r = taReglementService.merge(r);
			
			TaStripeCharge taStripeCharge = updateChargeFromStripe(configService,charge);
			taStripeCharge.setTaReglement(r);
			taStripeChargeService.merge(taStripeCharge);
			
//			if(documentPayableCB!=null) {	
//				//taFacture = taFactureService.merge(taFacture);
//				documentPayableCB = findById(documentPayableCB);
//				
////				if(taFacture.getTaReglement()!=null) {
////					log.setIdReglement(taFacture.getTaReglement().getIdDocument());
////					log.setCodeReglement(taFacture.getTaReglement().getCodeDocument());
////				}
//				if(documentPayableCB.getTaRReglements()!=null && !documentPayableCB.getTaRReglements().isEmpty()) {
//					TaReglement r2 = documentPayableCB.getTaRReglements().iterator().next().getTaReglement();
//					log.setIdReglement(r2.getIdDocument());
//					log.setCodeReglement(r2.getCodeDocument());
//				}
//			} else {
				if(r!=null) {
					log.setIdReglement(r.getIdDocument());
					log.setCodeReglement(r.getCodeDocument());
				}
//			}
				
			

			return charge.getId();
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public TaStripeInvoiceDTO upcomingInvoiceForCustomer(TaCompteServiceWebExterne configService, String customerStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Map<String, Object> invoiceParams = new HashMap<String, Object>();
			invoiceParams.put("customer", customerStripeID); //"cus_EQMUyvcFYsEJZI"
			Invoice.upcoming(invoiceParams,requestOptions);
			
			TaStripeInvoiceDTO a = new TaStripeInvoiceDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public List<TaStripePlanDTO>recherchePlanStripe(TaCompteServiceWebExterne configService, Integer limite, Boolean active) throws EJBException {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Map<String, Object> planParams = new HashMap<String, Object>();
			if(limite!=null) {
				planParams.put("limit", LibConversion.integerToString(limite));
			}
			if(active!=null) {
				planParams.put("active", active);
			}
//			planParams.put("customer", customerStripeID);
			
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour le customer : "+customerStripeID);
			PlanCollection planCollection = Plan.list(planParams,requestOptions);
			
			List<TaStripePlanDTO> result = new ArrayList<>();
			TaStripePlanDTO taStripePlanDTO = null;
			for (Plan pl : planCollection.getData()) {
				taStripePlanDTO = new TaStripePlanDTO();
				taStripePlanDTO.setIdExterne(pl.getId());
				result.add(taStripePlanDTO);
				
				if(synchroniserStripeVersBdg) {
					updatePlanFromStripe(configService,pl);
				}
			}

			return result;
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public List<TaStripePlanDTO>recherchePlanStripeForProduct(TaCompteServiceWebExterne configService, String productStripeID, Integer limite, Boolean active) throws EJBException {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
				
			Map<String, Object> planParams = new HashMap<String, Object>();
			if(limite!=null) {
				planParams.put("limit", LibConversion.integerToString(limite));
			}
			if(active!=null) {
				planParams.put("active", active);
			}
			if(productStripeID!=null) {
				planParams.put("product", productStripeID);
			}
			
//			System.out.println("LgrStripe ** Recherche des invoices Stripe - pour le customer : "+customerStripeID);
			PlanCollection planCollection = Plan.list(planParams,requestOptions);
			
			List<TaStripePlanDTO> result = new ArrayList<>();
			TaStripePlanDTO taStripePlanDTO = null;
			for (Plan pl : planCollection.getData()) {
				taStripePlanDTO = new TaStripePlanDTO();
				taStripePlanDTO.setIdExterne(pl.getId());
				result.add(taStripePlanDTO);
				
				if(synchroniserStripeVersBdg) {
					updatePlanFromStripe(configService,pl);
				}
			}

			return result;
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
			if(e.getCode()!=null && e.getCode().equals("payment_method_not_available")) {
				throw new EJBException("The payment method is currently not available. You should invite your customer to fallback to another payment method to proceed.",e);
			} else if(e.getCode()!=null && e.getCode().equals("processing_error")) {
				throw new EJBException("An unexpected error occurred preventing us from creating the source. The source creation should be retried.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_bank_account_iban")) {
				throw new EJBException("The IBAN provided appears to be invalid. Request the customer to check their information and try again.",e);
			} else if(e.getCode()!=null && e.getCode().equals("invalid_owner_name")) {
				throw new EJBException("The owner name is invalid. It must be at least three characters in length.",e);
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
	
	public TaStripeInvoiceDTO payerInvoice(TaCompteServiceWebExterne configService, String invoiceStripeID, Boolean  paidOutOfBand ) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			Map<String, Object> payParams = new HashMap<String, Object>();
			if(paidOutOfBand!=null) {
				payParams.put("paid_out_of_band", paidOutOfBand); //true pour les paiements fait en dehors de stripe (chèque, virement, ...)
			}
			
			Invoice invoice = Invoice.retrieve(invoiceStripeID,requestOptions);//"in_1DuzK8ESo4ojJRjqZB51noQ3"
			invoice = invoice.pay(payParams,requestOptions);
			TaStripeInvoiceDTO a = new TaStripeInvoiceDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public TaStripeInvoiceDTO rechercherInvoice(TaCompteServiceWebExterne configService, String invoiceStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Invoice c = Invoice.retrieve(invoiceStripeID,requestOptions);//"in_1DuzK8ESo4ojJRjqZB51noQ3"
			TaStripeInvoiceDTO a = new TaStripeInvoiceDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
//	public TaStripeSubscriptionDTO rechercherSubscription(TaCompteServiceWebExterne configService, String subsciprtionStripeID) {
//		try {
//			RequestOptions requestOptions = initCleStripeDossier(configService);
//			
//			Subscription c = Subscription.retrieve(subsciprtionStripeID,requestOptions);//"sub_ENkpQA7eMGNU3U"
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
	
	public TaStripeCustomerDTO rechercherCustomer(TaCompteServiceWebExterne configService, String customerStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Customer c = Customer.retrieve(customerStripeID,requestOptions);//"cus_Dr8U8upUHIEQwG"
			TaStripeCustomerDTO a = new TaStripeCustomerDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public TaStripeChargeDTO rechercherCharge(TaCompteServiceWebExterne configService, String chargeStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Charge c = Charge.retrieve(chargeStripeID,requestOptions);//"ch_Dr8U8upUHIEQwG"
			TaStripeChargeDTO a = new TaStripeChargeDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public TaStripePlanDTO rechercherPlan(TaCompteServiceWebExterne configService, String planStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Plan c = Plan.retrieve(planStripeID,requestOptions);//"cus_Dr8U8upUHIEQwG"
			TaStripePlanDTO a = new TaStripePlanDTO();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public TaStripeProductDTO rechercherProduct(TaCompteServiceWebExterne configService, String productStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Product c = Product.retrieve(productStripeID,requestOptions);//"prod_E3vbcn8BqJvbDO"
			TaStripeProductDTO a = new TaStripeProductDTO();
			a.setName(c.getName());
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public TaStripeSourceDTO rechercherSource(TaCompteServiceWebExterne configService, String sourceStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Source s = Source.retrieve(sourceStripeID,requestOptions);//"src_1DPQEI2eZvKYlo2C8VeUif3c"
			TaStripeSourceDTO a = new TaStripeSourceDTO();
			a.setIdExterne(s.getId());
			if(s.getType().equals("card")) {
				System.out.println("LgrStripe.rechercherSource() - address_zip_check : "+s.getCard().getAddressZipCheck());
				System.out.println("LgrStripe.rechercherSource() - country : "+s.getCard().getCountry());
				System.out.println("LgrStripe.rechercherSource() - last4 : "+s.getCard().getLast4());
				System.out.println("LgrStripe.rechercherSource() - funding : "+s.getCard().getFunding());
				System.out.println("LgrStripe.rechercherSource() - cvc_check : "+s.getCard().getCvcCheck());
				System.out.println("LgrStripe.rechercherSource() - fingerprint : "+s.getCard().getFingerprint());
				System.out.println("LgrStripe.rechercherSource() - exp_month : "+s.getCard().getExpMonth());
				System.out.println("LgrStripe.rechercherSource() - exp_year : "+s.getCard().getExpYear());
				System.out.println("LgrStripe.rechercherSource() - three_d_secure : "+s.getCard().getThreeDSecure());
				System.out.println("LgrStripe.rechercherSource() - brand : "+s.getCard().getBrand());
				TaCarteBancaireDTO taCarteBancaireDTO = new TaCarteBancaireDTO();
				taCarteBancaireDTO.setLast4(s.getCard().getLast4());
				taCarteBancaireDTO.setType(s.getCard().getBrand());
				taCarteBancaireDTO.setMoisExpiration(Math.toIntExact((s.getCard().getExpMonth())));
				taCarteBancaireDTO.setAnneeExpiration(Math.toIntExact((s.getCard().getExpYear())));
				a.setTaCarteBancaireDTO(taCarteBancaireDTO);
			} else if(s.getType().equals("sepa_debit")) {
				System.out.println("LgrStripe.rechercherSource() - branch_code : "+s.getSepaDebit().getBranchCode());
				System.out.println("LgrStripe.rechercherSource() - bank_code : "+s.getSepaDebit().getBankCode());
				System.out.println("LgrStripe.rechercherSource() - country : "+s.getSepaDebit().getCountry());
				System.out.println("LgrStripe.rechercherSource() - last4 : "+s.getSepaDebit().getLast4());
				System.out.println("LgrStripe.rechercherSource() - mandate_url : "+s.getSepaDebit().getMandateUrl());
				System.out.println("LgrStripe.rechercherSource() - fingerprint : "+s.getSepaDebit().getFingerprint());
				System.out.println("LgrStripe.rechercherSource() - mandate_reference : "+s.getSepaDebit().getMandateReference());
				TaCompteBanqueDTO taCompteBanqueDTO = new TaCompteBanqueDTO();
				taCompteBanqueDTO.setBranchCode(s.getSepaDebit().getBranchCode());
				taCompteBanqueDTO.setBankCode(s.getSepaDebit().getBankCode());
				taCompteBanqueDTO.setCountry(s.getSepaDebit().getCountry());
				taCompteBanqueDTO.setLast4(s.getSepaDebit().getLast4());
				a.setTaCompteBanqueDTO(taCompteBanqueDTO);
			}
			
//			a.setIdExterne(s.getId());
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String 
		return null;
	}
	
	public TaStripeSourceDTO rechercherPaymentMethod(TaCompteServiceWebExterne configService, String paymentMethodStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			PaymentMethod s = PaymentMethod.retrieve(paymentMethodStripeID,requestOptions);//"src_1DPQEI2eZvKYlo2C8VeUif3c"
			TaStripeSourceDTO a = new TaStripeSourceDTO();
			a.setIdExterne(s.getId());
			if(s.getType().equals("card")) {
				System.out.println("LgrStripe.rechercherSource() - address_zip_check : "+s.getCard().getChecks().getAddressPostalCodeCheck());
				System.out.println("LgrStripe.rechercherSource() - country : "+s.getCard().getCountry());
				System.out.println("LgrStripe.rechercherSource() - last4 : "+s.getCard().getLast4());
				System.out.println("LgrStripe.rechercherSource() - funding : "+s.getCard().getFunding());
				System.out.println("LgrStripe.rechercherSource() - cvc_check : "+s.getCard().getChecks().getCvcCheck());
				System.out.println("LgrStripe.rechercherSource() - fingerprint : "+s.getCard().getFingerprint());
				System.out.println("LgrStripe.rechercherSource() - exp_month : "+s.getCard().getExpMonth());
				System.out.println("LgrStripe.rechercherSource() - exp_year : "+s.getCard().getExpYear());
				System.out.println("LgrStripe.rechercherSource() - three_d_secure : "+s.getCard().getThreeDSecureUsage());
				System.out.println("LgrStripe.rechercherSource() - brand : "+s.getCard().getBrand());
				TaCarteBancaireDTO taCarteBancaireDTO = new TaCarteBancaireDTO();
				taCarteBancaireDTO.setLast4(s.getCard().getLast4());
				taCarteBancaireDTO.setType(s.getCard().getBrand());
				taCarteBancaireDTO.setMoisExpiration(Math.toIntExact((s.getCard().getExpMonth())));
				taCarteBancaireDTO.setAnneeExpiration(Math.toIntExact((s.getCard().getExpYear())));
				a.setTaCarteBancaireDTO(taCarteBancaireDTO);
				
				//L'API payment method n'implémente pas encore SEPA_DEBIT  
//			} else if(s.getType().equals("sepa_debit")) {
//				System.out.println("LgrStripe.rechercherSource() - branch_code : "+s.getSepaDebit().getBranchCode());
//				System.out.println("LgrStripe.rechercherSource() - bank_code : "+s.getSepaDebit().getBankCode());
//				System.out.println("LgrStripe.rechercherSource() - country : "+s.getSepaDebit().getCountry());
//				System.out.println("LgrStripe.rechercherSource() - last4 : "+s.getSepaDebit().getLast4());
//				System.out.println("LgrStripe.rechercherSource() - mandate_url : "+s.getSepaDebit().getMandateUrl());
//				System.out.println("LgrStripe.rechercherSource() - fingerprint : "+s.getSepaDebit().getFingerprint());
//				System.out.println("LgrStripe.rechercherSource() - mandate_reference : "+s.getSepaDebit().getMandateReference());
//				TaCompteBanqueDTO taCompteBanqueDTO = new TaCompteBanqueDTO();
//				taCompteBanqueDTO.setBranchCode(s.getSepaDebit().getBranchCode());
//				taCompteBanqueDTO.setBankCode(s.getSepaDebit().getBankCode());
//				taCompteBanqueDTO.setCountry(s.getSepaDebit().getCountry());
//				taCompteBanqueDTO.setLast4(s.getSepaDebit().getLast4());
//				a.setTaCompteBanqueDTO(taCompteBanqueDTO);
			}
			
//			a.setIdExterne(s.getId());
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String 
		return null;
	}
	
	public Boolean supprimerPlan(TaCompteServiceWebExterne configService, String planStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Plan c = Plan.retrieve(planStripeID,requestOptions);//"plan_EOZsEvcw9TpxGY"
			c = c.delete();
			return c.getDeleted();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public Boolean supprimerProduct(TaCompteServiceWebExterne configService, String productStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Product c = Product.retrieve(productStripeID,requestOptions);//"prod_EPKCCBb7klsk9z"
			c = c.delete();
			return c.getDeleted();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public Boolean supprimerCustomer(TaCompteServiceWebExterne configService, String customerStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Customer c = Customer.retrieve(customerStripeID,requestOptions);//"cus_EOZsEvcw9TpxGY"
			c = c.delete();
			return c.getDeleted();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public Boolean supprimerCoupon(TaCompteServiceWebExterne configService, String couponStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Coupon c = Coupon.retrieve(couponStripeID,requestOptions);//"XXXXXXXXXXXX"
			c = c.delete();
			return c.getDeleted();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	public String refundCharge(TaCompteServiceWebExterne configService, String chargeStripeID, BigDecimal montant) {
		
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Map<String, Object> params = new HashMap<>();
			params.put("charge", chargeStripeID); //"ch_ZslYWOaA7ielnGe40n7g"
			if(montant!=null) {
				params.put("amount", montantBdgVersStripe(montant));
			}
			
			Refund refund = Refund.create(params,requestOptions);
			System.out.println("LgrStripe ** Création d'un remboursement Stripe - pour le paiement : "+chargeStripeID);
			System.out.println("LgrStripe ** Création d'un remboursement Stripe : "+refund.getId());
			return refund.getId();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public TaStripeAccount rechercherAccount(TaCompteServiceWebExterne configService, String accountStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Account s = Account.retrieve(accountStripeID,requestOptions);//"acct_16gu0lESo4ojJRjq"
			TaStripeAccount a = new TaStripeAccount();
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String 
		return null;
	}
	
	public Account creerAccount(TaCompteServiceWebExterne configService, String email) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Map<String, Object> accountParams = new HashMap<String, Object>();
			accountParams.put("type", "custom");
			accountParams.put("country", "FR");
			accountParams.put("email", email);

			Account a = Account.create(accountParams,requestOptions);
//			a.getKeys()
			return a;
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String 
		return null;
	}
	
	public String creerWebhook(TaCompteServiceWebExterne configService, String url, String enabledEvents) throws EJBException{
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			return null;
			
////			Map<String, Object> webhookendpointParams = new HashMap<String, Object>();
////			webhookendpointParams.put("url", "https://example.com/my/webhook/endpoint");
////			webhookendpointParams.put("enabled_events", ["charge.failed", "charge.succeeded"]);
//			
//			Map<String, Object> webhookendpointParams = new HashMap<String, Object>();
//			webhookendpointParams.put("url", url);
//			webhookendpointParams.put("enabled_events", enabledEvents);//["charge.failed", "charge.succeeded"]
//
//			
//			System.out.println("LgrStripe ** Création d'un webhook Stripe : "+enabledEvents);
//			WebhookEndpoint webhook = WebhookEndpoint.create(webhookendpointParams,requestOptions);
//			
//			System.out.println("LgrStripe ** Création d'un webhook Stripe - ID : "+webhook.getId());

//			if(!charge.getPaid()) {
//				throw new EJBException("Une erreur est survenue, rééssayez ultérieurement");
//			}
//
//			return webhook.getId();
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
//		} catch (APIException e) {
//			e.printStackTrace();
//			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		} catch (Exception e) {
			// Something else happened, completely unrelated to Stripe
			e.printStackTrace();
			throw new EJBException("Une erreur est survenue, rééssayez ultérieurement",e);
		}
	}

	@Override
	public TaStripePlanDTO updatePlan(TaCompteServiceWebExterne configService, String planStripeID, String nickname, Boolean active) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeProductDTO updateProduct(TaCompteServiceWebExterne configService, String productStripeID, String name, Boolean active) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeCustomerDTO updateCustomer(TaCompteServiceWebExterne configService, String customerStripeID, String email, String sourceDefaultId,
			String description, String couponId, String invoicePrefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaStripeCouponDTO updateCoupon(TaCompteServiceWebExterne configService, String couponStripeID, String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public TaStripeAccount updateAccountFromStripe(TaCompteServiceWebExterne configService, String accountStripeID) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			Account a = Account.retrieve(accountStripeID,requestOptions);//"acct_1IBdhVK7lTFRoLxk"
			return updateAccountFromStripe(configService,a);
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private TaStripeAccount updateAccountFromStripe(TaCompteServiceWebExterne configService, Account account) {
		//récupération et enregistrement des informations de Stripe vers Bdg
		TaStripeAccount taStripeAccount = null;
		try {
			taStripeAccount = taStripeAccountService.findInstance();
			if(taStripeAccount==null) {
				taStripeAccount = new TaStripeAccount();
			}
			if(account!=null) {
//				account = Account.retrieve(accountId);
				taStripeAccount.setIdExterne(account.getId());
				taStripeAccount.setBusinessType(account.getBusinessType());
				taStripeAccount.setChargesEnabled(account.getChargesEnabled());
				taStripeAccount.setCountry(account.getCountry());
				taStripeAccount.setDefaultCurrency(account.getDefaultCurrency());
				taStripeAccount.setDetailsSubmitted(account.getDetailsSubmitted());
				taStripeAccount.setEmail(account.getEmail());
				taStripeAccount.setType(account.getType());
//				taStripeAccount.setName(account.getName());
				System.out.println("Deleted ===== "+account.getDeleted());
				taStripeAccount = taStripeAccountService.merge(taStripeAccount);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return taStripeAccount;
	}
	
	public String createAccount() {		
		RequestOptions requestOptions = initCleStripeProgramme(null);
		
		AccountCreateParams params = AccountCreateParams.builder()
		    .setType(AccountCreateParams.Type.STANDARD)
		    .build();

		try {
			Account account = Account.create(params,requestOptions);
			return account.getId();
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String createAccountLink(String idAccount, String refreshUrl, String returnUrl, String param) {

		RequestOptions requestOptions = initCleStripeProgramme(null);
		
		AccountLinkCreateParams params =
		  AccountLinkCreateParams.builder()
		    .setAccount(idAccount) //acct_1032D82eZvKYlo2C
		    .setRefreshUrl(refreshUrl+"?state="+param)
		    .setReturnUrl(returnUrl+"?state="+param)
		    .setType(Type.ACCOUNT_ONBOARDING)//"account_onboarding"
		    .build();

		try {
			AccountLink accountLink = AccountLink.create(params,requestOptions);
			return accountLink.getUrl();
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean liaisonBDGCompteOKChezStripe(String idAccount) {
		return liaisonBDGCompteOKChezStripe(idAccount, true);
	}
	
	public boolean liaisonBDGCompteOKChezStripe(String idAccount, boolean updateInfosCompte) {
		try {
			RequestOptions requestOptions = initCleStripeProgramme(null);
			
			boolean liaisonBDGCompteOKChezStripe = false;
			Account account = Account.retrieve(idAccount,requestOptions);
			liaisonBDGCompteOKChezStripe = true; 
			if(liaisonBDGCompteOKChezStripe) {
				if(updateInfosCompte)
					updateAccountFromStripe(null,idAccount);//mise a jour du compte deja fait dans les initCleDossier (a chaque appel stripe)
			}
			return liaisonBDGCompteOKChezStripe;
		} catch (PermissionException e) {
			e.printStackTrace();
			return false;
		} catch (StripeException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String autorizationTokenOAuthAccount(String authorisationCode) {
		try {
			RequestOptions requestOptions = initCleStripeProgramme(null);

			Map<String, Object> params = new HashMap<>();
			params.put("grant_type", "authorization_code");
			params.put("code", authorisationCode);

			TokenResponse rep = OAuth.token(params, requestOptions);

			// Access the connected account ID in the response
			String accountId = rep.getStripeUserId();
			
//			 JSONObject o = new JSONObject(rep.getLastResponse().body());
			 return rep.getLastResponse().body();
		} catch (PermissionException e) {
			e.printStackTrace();
			return null;
		} catch (StripeException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean deauthorizeStripeOAuthAccount(String clientIdSecretKey, String idAccount) {
		try {
			RequestOptions requestOptions = initCleStripeProgramme(null);
			
			Map<String, Object> params = new HashMap<>();
			params.put("client_id", clientIdSecretKey);
			params.put("stripe_user_id", idAccount);
	
			OAuth.deauthorize(params, requestOptions);
			return true;
		} catch (PermissionException e) {
			e.printStackTrace();
			return false;
		} catch (StripeException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * requires_capture
	 */
	public String capture(TaCompteServiceWebExterne configService, String paymentIntentStripeID, BigDecimal amountToCapture) {
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			PaymentIntent intent = PaymentIntent.retrieve(paymentIntentStripeID,requestOptions);
			
			long amount = amountToCapture.multiply(BigDecimal.valueOf(100)).longValue();
			PaymentIntentCaptureParams params =
			  PaymentIntentCaptureParams.builder()
			    .setAmountToCapture(amount)
			    .build();

			intent = intent.capture(params,requestOptions);
			
			TaStripePaymentIntent taStripePaymentIntent = taStripePaymentIntentService.findByCode(intent.getId());
			taStripePaymentIntent.setStatus(intent.getStatus());
			taStripePaymentIntentService.merge(taStripePaymentIntent);
			
			return intent.getId();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	
	/*
	 * A PaymentIntent can only be canceled when it has one of the following statuses: requires_payment_method, requires_capture, requires_confirmation, or requires_action—a PaymentIntent can’t be canceled while it’s actively processing or after it has succeeded.
	 */
	public String cancelPaymentIntent(TaCompteServiceWebExterne configService, String paymentIntentStripeID) {
		
		
		try {
			RequestOptions requestOptions = initCleStripeDossier(configService);
			
			PaymentIntent intent = PaymentIntent.retrieve(paymentIntentStripeID,requestOptions);
			intent = intent.cancel(requestOptions);
			

			TaStripePaymentIntent taStripePaymentIntent = taStripePaymentIntentService.findByCode(intent.getId());
			taStripePaymentIntent.setStatus(intent.getStatus());
			taStripePaymentIntentService.merge(taStripePaymentIntent);
			
			return intent.getId();
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
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//, String sourceStripeID
		return null;
	}
	

//	@Override
//	public TaStripeSubscriptionDTO updateSubscription(TaCompteServiceWebExterne configService, String subscriptionStripeID, String billing,
//			String billing_cycle_anchor, String coupon, String days_until_due, String default_source) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}
