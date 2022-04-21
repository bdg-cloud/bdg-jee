package fr.legrain.bdg.rest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.compteclientfinal.service.IEtatPaiementCourant;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.model.ParamPaiement;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaPanier;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaAliasEspaceClientServiceRemote;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.service.LgrStripe;
import fr.legrain.paiement.service.PaiementGeneralDossierService;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("/paiement")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class StripePaiementRestService extends AbstractRestService {
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;

	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaPanierServiceRemote taPanierService;  
	@EJB private ITaStripeAccountServiceRemote taStripeAccountService;
	
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaPreferencesServiceRemote taPreferencesService;

	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementStripeDossierService;

	@EJB private PaiementGeneralDossierService paiementGeneralDossierService;
	//	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;

	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;

	@PostConstruct
	public void init() {
		try {
			bdgProperties = new BdgProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET()
	@Path("/etat-paiement-courant")
	@Operation(summary = "Etat d'un paiement en cours", tags = {MyApplication.TAG_STRIPE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	/**
	 		Pas de réponse 0
	  		IEtatPaiementCourant.PAIEMENT_OK 1
	  		IEtatPaiementCourant.PAIEMENT_ERREUR 2
			IEtatPaiementCourant.PAIEMENT_EN_COURS 3
	 * @param dossier
	 * @param idStripePaymentIntent
	 * @return
	 */
	public Response paiementTermine(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("idPaymentIntent") String idStripePaymentIntent) {

		try {
			//processing
			int etat = 0;

			TaStripePaymentIntent paymentIntent = null;

			TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
			IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);

			String connectedAccountId = null;
			TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
			if(taStripeAccount.getIdExterne()!=null && !taStripeAccount.getIdExterne().equals("")) {
				connectedAccountId = taStripeAccount.getIdExterne();
			}
			
			if(service!=null) {

				paymentIntent = service.updatePaymentIntentFromStripe(compte, idStripePaymentIntent,connectedAccountId); 

				if(paymentIntent!=null && paymentIntent.getStatus().equals("succeeded")) {
					etat = IEtatPaiementCourant.PAIEMENT_OK;
				} else if(paymentIntent!=null && paymentIntent.getStatus().equals("requires_capture") /*&& paymentIntent.getCaptureMethod().equals("manual")*/) {
					//			interfaceStripeElementResultatOk = true;
					etat = IEtatPaiementCourant.PAIEMENT_OK;
					//			FacesContext context = FacesContext.getCurrentInstance();  
					//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Paiement CB",
					//					"Le paiement a été validé. Le règlement correspondant doit être visible dans la liste des règlement"));
				} else if(paymentIntent!=null && paymentIntent.getStatus().equals("processing")) {
					//			interfaceStripeElementResultatNonTermine = true;
					etat = IEtatPaiementCourant.PAIEMENT_EN_COURS;
					//			FacesContext context = FacesContext.getCurrentInstance();  
					//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN , "Paiement CB", 
					//					"Le paiement est encore encore cours de validation. "
					//							+ "Quand il sera validé, le règlement correspondant apparetra dans la liste des règlements."
					//							+ "S'il n'apparait pas, veuillez verifier dans votre service de paiement (Stripe)"));
				} else  {
					//			interfaceStripeElementResultatErreur = true;
					etat = IEtatPaiementCourant.PAIEMENT_ERREUR;
					//			FacesContext context = FacesContext.getCurrentInstance();  
					//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", 
					//					"Une erreur est survenue pendant la validation du paiement. Il n'a pas été pris en compte"));
				}
			}

			//			return retour;

			return Response.status(200)
					.entity("{\"b\":\""+etat+"\"}")
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/paiment-cb-possible")
	@Operation(summary = "Vrai si un compte Stripe est paramétré sur le dossier.", tags = {MyApplication.TAG_STRIPE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response fournisseurPermetPaiementParCB() {
		try {
			boolean b = paiementGeneralDossierService.dossierPermetPaiementCB();
			return Response.status(200)
					.entity("{\"b\":"+b+"}")
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/cle-publique-stripe")
	@Operation(summary = "Clé publique Stripe du dossier si elle existe pour pouvoir l'utiliser dans du JavaScript par exemple", tags = {MyApplication.TAG_STRIPE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response clePubliqueStripe() {
		try {
			String b = null;

			try {
				TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT); 
				UtilServiceWebExterne utilServiceWebExterne = new UtilServiceWebExterne();

				boolean modeTest = false;
				boolean modeStripeConnect = false;
				modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
				TaStripeAccount taStripeAccount =  taStripeAccountService.findInstance(); 
				if(taStripeAccount!=null && taStripeAccount.getIdExterne()!=null) {
					modeStripeConnect = true;
				}
				if(modeStripeConnect) {
					//connect, donc utilisation des clés programmes
					taParametreService.initParamBdg();
					if(modeTest) {
						//b = LgrStripe.TEST_PUBLIC_API_KEY_PROGRAMME;
						b = paramBdg.getTaParametre().getStripeTestPublicApiKeyProgramme();
					} else {
						//b = LgrStripe.LIVE_PUBLIC_API_KEY_PROGRAMME;
						b = paramBdg.getTaParametre().getStripeLivePublicApiKeyProgramme();
					}
				} else {
					//utilisation des clé API du dossier s'il y en a, sinon erreur
					if(compte!=null && compte.getApiKey2()!=null) {
						String API_KEY_DOSSIER_PK_LIVE = utilServiceWebExterne.decrypter(compte).getApiKey2();
						b = API_KEY_DOSSIER_PK_LIVE;
					} else {
						//TODO exception, pas de compte connect et aucun cle dans le dossier
					}
				}
				//				if(compte!=null) {
				//					return utilServiceWebExterne.decrypter(compte).getApiKey2();
				//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response.status(200)
					.entity("{\"b\":\""+b+"\"}")
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/cle-connect-stripe")
	@Operation(summary = "Clé publique Stripe Connect du dossier si elle existe pour pouvoir l'utiliser dans du JavaScript par exemple", tags = {MyApplication.TAG_STRIPE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response cleConnectStripe() {
		String b = null;
		try {

			try {
				TaStripeAccount taStripeAccount =  taStripeAccountService.findInstance(); 
				//				UtilServiceWebExterne utilServiceWebExterne = new UtilServiceWebExterne();
				//				if(compte!=null) {
				//					return utilServiceWebExterne.decrypter(compte).getApiKey2();
				//				}
				if(taStripeAccount!=null && taStripeAccount.getIdExterne()!=null) {
					b = taStripeAccount.getIdExterne();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return Response.status(200)
					.entity("{\"b\":\""+b+"\"}")
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@POST()
	@Path("/paiement-document-cb")
	@Operation(summary = "Initialise le paiement d'une facture ou d'un autre document par CB et retourne l'identifiant du paiement pour poursuivre la procédure", tags = {MyApplication.TAG_STRIPE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response creerPaymentIntent(
			@Parameter(name = "p", required = true, description = "ParamPaiement.typeDocument (Facture,AvisEcheance,Panier) correspond à un TYPE_DOC") ParamPaiement p
			) throws EJBException{
		String paymentSecret = null;
		try {

			TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);

			TaFacture facture = null;
			TaAvisEcheance avisEcheance = null;
			TaPanier panier = null;
			try {
				if(p.getTypeDocument().equals(TaFacture.TYPE_DOC)) {//"Facture"

					facture = taFactureService.findByCode(p.getCodeDocument());

				} else if(p.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {//"AvisEcheance"
					avisEcheance = taAvisEcheanceService.findByCode(p.getCodeDocument());

				} else if(p.getTypeDocument().equals(TaPanier.TYPE_DOC)) {//"Panier"
					panier = taPanierService.findByCode(p.getCodeDocument());
				}
			} catch (FinderException e) {
				e.printStackTrace();
			}
			PaiementCarteBancaire cb  = new PaiementCarteBancaire();
			cb.setCompteClient(true);
			cb.setOriginePaiement("Espace client");
			cb.setMontantPaiement(p.getMontant());
			
			//Chercher si le document contient un ligne de prix variable
			if(p.getTypeDocument().equals(TaPanier.TYPE_DOC) ) {
				//pour l'instant seul les paniers peuvent gérer ce comportement
				TaArticle articleVar = null;
				TaPreferences prefCodeArtVar = taPreferencesService.findByCode("code_article_pour_ajustement_prix_variable");
				if(prefCodeArtVar!=null && prefCodeArtVar.getValeur() != null) {
					articleVar = taArticleService.findByCode(prefCodeArtVar.getValeur());
				}
				
				if(articleVar!=null) {//on a bien un paramétrage de prix variable sur le dossier
					TaLPanier lignePrixVariable = null; 
					//suppression de la ligne de prix variable
					for (TaLPanier ligne : panier.getLignes()) {
						if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(articleVar.getCodeArticle())) {
							lignePrixVariable = ligne;
							cb.setCapturablePlusTard(true);
							break;
						}
					}
				}
			}
			

			IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);

			//			if(compte!=null) {
			//
			//				String implementation = "";
			//
			//				switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
			//				case UtilServiceWebExterne.CODE_SWE_STRIPE:
			//					implementation = PaiementLgr.TYPE_STRIPE;
			//					break;
			//				case UtilServiceWebExterne.CODE_SWE_PAYPAL:
			//					implementation = PaiementLgr.TYPE_PAYPAL;
			//					break;
			//				default:
			//					break;
			//				}
			//				
			//				service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
			//			} else {
			//				//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
			//				service = paiementStripeDossierService;
			//			}	
			//				try {
			if(service!=null) {
				if(p.getTypeDocument().equals(TaFacture.TYPE_DOC)) {//"Facture"
					paymentSecret = service.creerPaymentIntent(compte, cb, facture, null);
				} else if(p.getTypeDocument().equals(TaAvisEcheance.TYPE_DOC)) {//"AvisEcheance"
					paymentSecret = service.creerPaymentIntent(compte, cb, avisEcheance, null); //TODO à modifier/améliorer quand on pourra relier un reglement à tout type de document y compris les "non payable"
				} else if(p.getTypeDocument().equals(TaPanier.TYPE_DOC)) {//"Panier"
					paymentSecret = service.creerPaymentIntent(compte, cb, panier, null); //TODO à modifier/améliorer quand on pourra relier un reglement à tout type de document y compris les "non payable"
				}
				//					return iServicePaiementEnLigneDossierInstance.creerPaymentIntent(compte, cb, facture, null);
				//				} catch(EJBException e) {
				//					throw new EJBException(e);
				//				}
			}
			return Response.status(200)
					.entity("{\"paymentSecret\":\""+paymentSecret+"\"}")
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (FinderException e) {
			throw new EJBException(e);
		}
	}
}
