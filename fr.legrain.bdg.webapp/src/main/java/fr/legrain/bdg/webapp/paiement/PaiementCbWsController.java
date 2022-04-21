package fr.legrain.bdg.webapp.paiement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.stripe.net.RequestOptions;
import com.stripe.net.RequestOptions.RequestOptionsBuilder;

import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.abonnement.stripe.service.TaStripePaymentIntentService;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaymentIntentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.rest.model.ParamPaiement;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.bdg.ws.rest.client.Config;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.general.model.TaParametre;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.paiement.service.LgrStripe;
import fr.legrain.paiement.service.PaiementGeneralDossierService;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.paiement.service.PaiementStripeDossierService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class PaiementCbWsController extends PaiementCbController implements Serializable {  

	private static final long serialVersionUID = -3391355420401619152L;
	
	protected PaiementParamWs param;
	protected SWTDocument swtDocumentPayableCB;
	@EJB private ITaParametreServiceRemote taParametreService;

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		param = (PaiementParamWs) sessionMap.get(PaiementParamWs.NOM_OBJET_EN_SESSION);
		sessionMap.remove(PaiementParamWs.NOM_OBJET_EN_SESSION);
		
		try {
			
			if(param!=null
					&& param.getDossierTenant()!=null
					&& param.getLoginDeLaTableEspaceClient()!=null
					&& param.getPasswordDeLaTableEspaceClient()!=null) {
		
				Config config = new Config(param.getDossierTenant(),param.getLoginDeLaTableEspaceClient(), param.getPasswordDeLaTableEspaceClient());
				config.setVerificationSsl(param.isVerificationSsl());
				config.setDevLegrain(param.isDevLegrain());
				BdgRestClient bdg = BdgRestClient.build(config);
				
				//Connexion du client pour une connexion par token JWT
				String codeTiers = bdg.connexionEspaceClient();
				
				paiementCbPossibleDansDossier = bdg.paiement().paimentCbPossible();
				
				clePubliqueStripe = bdg.paiement().clePubliqueStripe();
				compteStripeConnect = bdg.paiement().cleConnectStripe();
		
				interfaceStripeElementSaisie  = true;
				interfaceStripeElementEnCours = false;
		
				interfaceStripeElementResultat = false;
				interfaceStripeElementResultatOk = false;
				interfaceStripeElementResultatErreur = false;
				interfaceStripeElementResultatNonTermine = false;
				
				//TODO A TRANSFORMER POUR UNE UTILISATION AVEC DES WS SUR UN DOSSIER DIFFÉRENT
				//Pour cet écran travaillant sur un autre dossier, pour l'instant on peut seulement payer le montant total d'un document
				montantLibre = false; 

		
				totalTTC = param.getMontantPaiement();
				swtDocumentPayableCB = param.getSwtDocumentPayableCB();
				tiersPourReglementLibre = param.getTiersPourReglementLibre();
				montantLibre = param.isMontantLibre();
				libelle = param.getLibelle();
				if(montantLibre) {
					saisirMontant = true;
				} else { //paiement d'un document sans modification possible du montant, on connait le montant donc on peut creer l'intent directement
					PaiementCarteBancaire pcb = new PaiementCarteBancaire();
					pcb.setNumeroCarte(numCarte);
					pcb.setNomPorteurCarte(nomCarte);
					pcb.setMoisCarte(moisCarte);
					pcb.setAnneeCarte(anneeCarte);
					pcb.setCryptogrammeCarte(cryptoCarte);
					pcb.setMontantPaiement(totalTTC);
					pcb.setOriginePaiement(param.getOriginePaiement());
					pcb.setCompteClient(false);
//					secretClient = taTicketDeCaisseService.creerPaymentIntent(pcb, param.getDocumentPayableCB(),libelle);
					ParamPaiement p = new ParamPaiement();
					if(swtDocumentPayableCB!=null) {
						p.setCodeDocument(swtDocumentPayableCB.getCodeDocument());
						if(swtDocumentPayableCB instanceof TaFacture) {
							p.setTypeDocument(TaFacture.TYPE_DOC);
						} else if(swtDocumentPayableCB instanceof TaAvisEcheance) {
							p.setTypeDocument(TaAvisEcheance.TYPE_DOC);
						} else if(swtDocumentPayableCB instanceof TaPanier) {
							p.setTypeDocument(TaPanier.TYPE_DOC);
						}
					} 
					if(p.getTypeDocument()==null) {
						p.setTypeDocument(param.getTypeDocmument());
					}
					if(p.getCodeDocument()==null) {
						p.setCodeDocument(param.getCodeDocument());
					}
					
					
					p.setMontant(totalTTC);
					secretClient = bdg.paiement().paiementDocumentCb(p);
					idStripePaymentIntent = secretClient.split("_secret_")[0];
					
					paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
					initAdresseTicketRecu();
					
				}
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actPayer(ActionEvent actionEvent) {
//TODO A TRANSFORMER POUR UNE UTILISATION AVEC DES WS SUR UN DOSSIER DIFFÉRENT
		
//		try {
//			System.out.println("PaiementCbController.actPayer()");
//
//			RetourPaiementCarteBancaire rcb = null;
//			PaiementCarteBancaire pcb = new PaiementCarteBancaire();
//			pcb.setNumeroCarte(numCarte);
//			pcb.setNomPorteurCarte(nomCarte);
//			pcb.setMoisCarte(moisCarte);
//			pcb.setAnneeCarte(anneeCarte);
//			pcb.setCryptogrammeCarte(cryptoCarte);
//			pcb.setMontantPaiement(totalTTC);
//			pcb.setOriginePaiement(param.getOriginePaiement());
//			pcb.setCompteClient(false);
//
//			if(documentPayableCB!=null) {
//				rcb = taTicketDeCaisseService.payerTicketDeCaisseCB(pcb, documentPayableCB,libelle);
//			}else if(tiersPourReglementLibre!=null) {
//				rcb = taTicketDeCaisseService.payerCB(pcb, tiersPourReglementLibre,libelle);
//			}
//
//			if(rcb.getPaye()) {
//				PrimeFaces.current().dialog().closeDynamic(rcb);
//			}
//		} catch (Exception e) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", e.getMessage()/*+" "+ThrowableExceptionLgr.renvoieCauseRuntimeException(e)*/)); 
//		}
//		//PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void initAdresseTicketRecu() {
//TODO A TRANSFORMER POUR UNE UTILISATION AVEC DES WS SUR UN DOSSIER DIFFÉRENT
		
//		if(paymentIntent!=null && paymentIntent.getTaTiers()!=null 
//				&& paymentIntent.getTaTiers().getTaEmail()!=null
//				&& paymentIntent.getTaTiers().getTaEmail().getAdresseEmail()!=null) {
//			paymentIntent.setEmailTicket(paymentIntent.getTaTiers().getTaEmail().getAdresseEmail());
//			
//			emailTicketRecu = paymentIntent.getTaTiers().getTaEmail().getAdresseEmail();
//		} else {
//			emailTicketRecu = null;
//		}
	}
	
	public void actMajEnvoyerTicketRecu() {
//TODO A TRANSFORMER POUR UNE UTILISATION AVEC DES WS SUR UN DOSSIER DIFFÉRENT
		
//		try {
//			paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
//			if(envoyerTicketRecu) {
//				initAdresseTicketRecu();
//				if(emailTicketRecu!=null) {
//					paymentIntent.setEmailTicket(emailTicketRecu);
//					//mise à jour chez Stripe avant de déclencher un paiement
//					paiementStripeDossierService.ajouterAdresseEmailPourRecu(compte, idStripePaymentIntent, emailTicketRecu);
//				}
//			} else {
//				paiementStripeDossierService.ajouterAdresseEmailPourRecu(compte, idStripePaymentIntent, null);
//				paymentIntent.setEmailTicket(null);
//			}
//			//conservation de l'adresse email dans BDG
//			paymentIntent = taStripePaymentIntentService.merge(paymentIntent);
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
	
	public void actMajConserverCarte() {
//TODO A TRANSFORMER POUR UNE UTILISATION AVEC DES WS SUR UN DOSSIER DIFFÉRENT
		
//		try {
//			paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
//			paymentIntent.setConserverCarte(conserverCarte);
//			
//			/**
//			 * TODO mettre à jour le on-session off-session chez stripe via l'api stripe avant le paiement
//			 */
//			paymentIntent = taStripePaymentIntentService.merge(paymentIntent);
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }

	public void paiementEnCours() {
		System.out.println("PaiementCbController.paiementEnCours()");
		
		interfaceStripeElementSaisie  = false;
		interfaceStripeElementEnCours = true;
	}
	
	public void paiementTermine() {
		System.out.println("PaiementCbController.paiementTermine() ");
		
		
		Config config = new Config(param.getDossierTenant(),param.getLoginDeLaTableEspaceClient(), param.getPasswordDeLaTableEspaceClient());
		config.setVerificationSsl(param.isVerificationSsl());
		config.setDevLegrain(param.isDevLegrain());
		BdgRestClient bdg = BdgRestClient.build(config);
		
		Integer etat;
		
		//Connexion du client pour une connexion par token JWT
		String codeTiers = bdg.connexionEspaceClient();

        etat = bdg.paiement().etatPaiementCourant(idStripePaymentIntent);

		try {//processing	
			
			interfaceStripeElementEnCours = false;		
			interfaceStripeElementResultat = false;
			interfaceStripeElementResultatOk = false;
			interfaceStripeElementResultatErreur = false;
			interfaceStripeElementResultatNonTermine = false;

			Thread.sleep(4000);
			//Thread.sleep(60000);

			interfaceStripeElementResultat = true;
		
			if(etat!=null) {
            	if(etat == 0) {
            		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
        					FacesMessage.SEVERITY_ERROR, "Paiement Stripe ","Aucune réponse de la part de Stripe"));
            	}else if (etat == 1) {
            		interfaceStripeElementResultatOk = true;
    				FacesContext context = FacesContext.getCurrentInstance();  
    				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Paiement CB",
    						"Le paiement a été validé. Le règlement correspondant doit être visible dans la liste des règlement"));
            	}else if (etat == 2) {
            		interfaceStripeElementResultatErreur = true;
    				FacesContext context = FacesContext.getCurrentInstance();  
    				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", 
    						"Une erreur est survenue pendant la validation du paiement. Il n'a pas été pris en compte"));
            	}else if (etat == 3) {
            		interfaceStripeElementResultatNonTermine = true;
    				FacesContext context = FacesContext.getCurrentInstance();  
    				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN , "Paiement CB", 
    						"Le paiement est encore encore cours de validation. "
    								+ "Quand il sera validé, le règlement correspondant apparetra dans la liste des règlements."
    								+ "S'il n'apparait pas, veuillez verifier dans votre service de paiement (Stripe)"));
            	}
				
			} 
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//old
//	public void paiementTermine() {
//		System.out.println("PaiementCbController.paiementTermine() ");
//		
//		TaParametre param = taParametreService.findInstance();
//		String tenant = param.getDossierMaitre();
//
//		try {//processing	
//			
//			interfaceStripeElementEnCours = false;		
//			interfaceStripeElementResultat = false;
//			interfaceStripeElementResultatOk = false;
//			interfaceStripeElementResultatErreur = false;
//			interfaceStripeElementResultatNonTermine = false;
//
//			Thread.sleep(4000);
//			//Thread.sleep(60000);
//
//			interfaceStripeElementResultat = true;
//
//			paymentIntent = paiementStripeDossierService.updatePaymentIntentFromStripe(tenant, compte, idStripePaymentIntent, compteStripeConnect);
//			
////			if(paymentIntent.getEmailTicket()!=null) {
////				paiementStripeDossierService.ajouterAdresseEmailPourRecu(compte, paymentIntent.getIdExterne(), paymentIntent.getEmailTicket());
////			}
//		
//			if(paymentIntent!=null && paymentIntent.getStatus().equals("succeeded")) {
//				interfaceStripeElementResultatOk = true;
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Paiement CB",
//						"Le paiement a été validé. Le règlement correspondant doit être visible dans la liste des règlement"));
//			} else if(paymentIntent!=null && paymentIntent.getStatus().equals("processing")) {
//				interfaceStripeElementResultatNonTermine = true;
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN , "Paiement CB", 
//						"Le paiement est encore encore cours de validation. "
//								+ "Quand il sera validé, le règlement correspondant apparetra dans la liste des règlements."
//								+ "S'il n'apparait pas, veuillez verifier dans votre service de paiement (Stripe)"));
//			} else  {
//				interfaceStripeElementResultatErreur = true;
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", 
//						"Une erreur est survenue pendant la validation du paiement. Il n'a pas été pris en compte"));
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public void actValiderMontant(ActionEvent actionEvent) {
//TODO A TRANSFORMER POUR UNE UTILISATION AVEC DES WS SUR UN DOSSIER DIFFÉRENT
			
//		System.out.println("PaiementCbController.actValiderMontant() "+totalTTC);
//		try {
//			PaiementCarteBancaire pcb = new PaiementCarteBancaire();
//			pcb.setNumeroCarte(numCarte);
//			pcb.setNomPorteurCarte(nomCarte);
//			pcb.setMoisCarte(moisCarte);
//			pcb.setAnneeCarte(anneeCarte);
//			pcb.setCryptogrammeCarte(cryptoCarte);
//			pcb.setMontantPaiement(totalTTC);
//			pcb.setOriginePaiement(param.getOriginePaiement());
//			pcb.setCompteClient(true);
//			//pcb.setFraisPlateforme(new BigDecimal(2.50)); //test
//			
//			if(param.getDocumentPayableCB()!=null) {
//				//document payable pour lequel on a laissé la possibilité de modifier le montant du reglèment CB
//				secretClient = taTicketDeCaisseService.creerPaymentIntent(pcb, param.getDocumentPayableCB(),libelle);
//			} else {
//				//paiment d'un montant libre sur un tiers
//				secretClient = taTicketDeCaisseService.creerPaymentIntent(pcb, tiersPourReglementLibre,libelle);
//			}
//			idStripePaymentIntent = secretClient.split("_secret_")[0];
//			
//			paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
//			initAdresseTicketRecu();
//			
//			saisirMontant = false;
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	static public void actDialoguePaiementEcheanceParCarte(PaiementParam param) {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", false);
		options.put("draggable", true);
		options.put("resizable", true);
		//options.put("contentHeight", 400);
		options.put("width", "50%"); 
		options.put("height", "90%"); 
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");

		//Map<String,List<String>> params = null;
		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		//        params.put("modeEcranDefaut", list);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		sessionMap.put(PaiementParam.NOM_OBJET_EN_SESSION, param);

		PrimeFaces.current().dialog().openDynamic("paiement/dialog_paiement_cb_ws", options, params);
	}

	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		System.out.println("PaiementCbWsController.handleReturnDialoguePaiementEcheanceParCarte()");
		try {
			//			if(event!=null && event.getObject()!=null) {
			//				RetourPaiementCarteBancaire e = (RetourPaiementCarteBancaire) event.getObject();
			//				
			//				//taDossierRcd = taDossierRcdService.findById(taDossierRcd.getIdDossierRcd());
			//				
			//				for (TaEcheance taEcheance : taDossierRcd.getTaEcheances()) {
			//					if(taEcheance.getIdEcheance()==selectedTaEcheanceDTO.getId()) {
			//						taEcheance.setTaReglementAssurance(e.getTaReglementAssurance());
			//						taEcheance.getTaReglementAssurance().setTaEcheance(taEcheance);
			//					}
			//				}
			//				
			//				actEnregistrer(null);
			//				actActionApresPaiementAccepte();
			//				listeTaEcheanceDTO = taEcheanceService.findAllEcheanceRCDIdDTO(taDossierRcd.getIdDossierRcd());
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}  
