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
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.document.dto.IDocumentPayableCB;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.droits.service.ParamBdg;
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
public class PaiementCbController extends AbstractController implements Serializable {  

	protected static final long serialVersionUID = -3512103552691966197L;

	protected @EJB ILgrStripe lgrStripe;
	protected @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	protected @EJB ITaStripePaymentIntentServiceRemote taStripePaymentIntentService;
	protected @EJB ITaStripeAccountServiceRemote taStripeAccountService;
	@EJB protected PaiementGeneralDossierService paiementGeneralDossierService;
	@EJB protected LgrEnvironnementServeur lgrEnvironnementServeur;
	protected @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementStripeDossierService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;

	protected IDocumentPayableCB documentPayableCB;
	protected TaTicketDeCaisse taTicketDeCaisseAPayer;
	protected TaFacture taFactureAPayer;
	protected TaAcompte taAcompteAPayer;
	protected TaReglement taReglementGenere;
	
	protected boolean paiementCbPossibleDansDossier = false;

	protected boolean interfaceStripeElement = true;
	protected boolean interfaceStripeElementNomComplet = true;
	protected boolean interfaceStripeElementEmail = true;
	protected boolean interfaceStripeElementTelephone = true;

	protected boolean interfaceStripeElementSaisie  = true;
	protected boolean interfaceStripeElementEnCours = false;
	protected boolean interfaceStripeElementResultat = false;
	protected boolean interfaceStripeElementResultatOk = false;
	protected boolean interfaceStripeElementResultatErreur = false;
	protected boolean interfaceStripeElementResultatNonTermine = false;

	protected TaTiers tiersPourReglementLibre;
	protected boolean montantLibre = false;
	protected String libelle;
	
	protected String emailTicketRecu = null;
	protected boolean envoyerTicketRecu = false;
	protected boolean conserverCarte = false;

	protected boolean saisirMontant = false;

	protected PaiementParam param;

	protected @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;

	protected int[] listeMoisCB = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
	protected int[] listeAnneeCB = new int[]{2019,2020,2021,2022,2023,2024,2025,2026,2027,2028,2029};

	protected BigDecimal totalHT;
	protected BigDecimal totalTTC;
	protected BigDecimal totalTVA;

	protected String nomCarte;
	protected int moisCarte;
	protected int anneeCarte;
	protected String cryptoCarte;
	protected String numCarte;
	protected String typeCarte;

	protected String clePubliqueStripe;
	protected String compteStripeConnect;
	protected String secretClient;
	protected String idStripePaymentIntent;

	protected TaCompteServiceWebExterne compte = null;
	protected TaStripePaymentIntent paymentIntent = null;

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		param = (PaiementParam) sessionMap.get(PaiementParam.NOM_OBJET_EN_SESSION);
		sessionMap.remove(PaiementParam.NOM_OBJET_EN_SESSION);
		
		paiementCbPossibleDansDossier = paiementGeneralDossierService.dossierPermetPaiementCB();

		compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		if(compte!=null) {
			UtilServiceWebExterne util = new UtilServiceWebExterne();
			compte = util.decrypter(compte);
	
			System.out.println("PaiementStripeDossierService clé 1 "+ compte.getApiKey1());	
			System.out.println("PaiementStripeDossierService clé 2 "+ compte.getApiKey2());
			System.out.println("PaiementStripeDossierService clé 3 "+ compte.getValeur1());
			System.out.println("PaiementStripeDossierService clé 4 "+ compte.getValeur2());
		}
		
		//		boolean modeTest = false;
		//		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();

//		String API_KEY_DOSSIER_PK_LIVE = compte.getApiKey2();
//		clePubliqueStripe = API_KEY_DOSSIER_PK_LIVE;
		
		/*************************************************MODIF CONNECT **/
		boolean modeTest = false;
		boolean modeStripeConnect = false;
		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
//
//		compteStripeConnect = null;
//		if(modeStripeConnect) {
//			
//			TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
//			compteStripeConnect = taStripeAccount.getIdExterne();
//		}
		
		TaStripeAccount taStripeAccount = taStripeAccountService.findInstance();
		if(taStripeAccount.getIdExterne()!=null && !taStripeAccount.getIdExterne().equals("")) {
			compteStripeConnect = taStripeAccount.getIdExterne();
			modeStripeConnect = true;
		}
		if(modeStripeConnect) {
			//connect, donc utilisation des clés programmes
			taParametreService.initParamBdg();
			if(modeTest) {
				//clePubliqueStripe = LgrStripe.TEST_PUBLIC_API_KEY_PROGRAMME;
				clePubliqueStripe = paramBdg.getTaParametre().getStripeTestPublicApiKeyProgramme();
			} else {
				//clePubliqueStripe = LgrStripe.LIVE_PUBLIC_API_KEY_PROGRAMME;
				clePubliqueStripe = paramBdg.getTaParametre().getStripeLivePublicApiKeyProgramme();
			}
		} else {
			//utilisation des clé API du dossier s'il y en a, sinon erreur
			if(compte!=null && compte.getApiKey2()!=null) {
				String API_KEY_DOSSIER_PK_LIVE = compte.getApiKey2();
				clePubliqueStripe = API_KEY_DOSSIER_PK_LIVE;
			} else {
				//TODO exception, pas de compte connect et aucun cle dans le dossier
			}
		}
		/*************************************************MODIF CONNECT **/

		interfaceStripeElementSaisie  = true;
		interfaceStripeElementEnCours = false;

		interfaceStripeElementResultat = false;
		interfaceStripeElementResultatOk = false;
		interfaceStripeElementResultatErreur = false;
		interfaceStripeElementResultatNonTermine = false;

		try {
			if(param!=null) {
				totalTTC = param.getMontantPaiement();
				documentPayableCB = param.getDocumentPayableCB();
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
					secretClient = taTicketDeCaisseService.creerPaymentIntent(pcb, param.getDocumentPayableCB(),libelle);
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
		try {
			System.out.println("PaiementCbController.actPayer()");

			RetourPaiementCarteBancaire rcb = null;
			PaiementCarteBancaire pcb = new PaiementCarteBancaire();
			pcb.setNumeroCarte(numCarte);
			pcb.setNomPorteurCarte(nomCarte);
			pcb.setMoisCarte(moisCarte);
			pcb.setAnneeCarte(anneeCarte);
			pcb.setCryptogrammeCarte(cryptoCarte);
			pcb.setMontantPaiement(totalTTC);
			pcb.setOriginePaiement(param.getOriginePaiement());
			pcb.setCompteClient(false);

			if(documentPayableCB!=null) {
				rcb = taTicketDeCaisseService.payerTicketDeCaisseCB(pcb, documentPayableCB,libelle);
			}else if(tiersPourReglementLibre!=null) {
				rcb = taTicketDeCaisseService.payerCB(pcb, tiersPourReglementLibre,libelle);
			}

			if(rcb.getPaye()) {
				PrimeFaces.current().dialog().closeDynamic(rcb);
			}
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", e.getMessage()/*+" "+ThrowableExceptionLgr.renvoieCauseRuntimeException(e)*/)); 
		}
		//PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void initAdresseTicketRecu() {
		if(paymentIntent!=null && paymentIntent.getTaTiers()!=null 
				&& paymentIntent.getTaTiers().getTaEmail()!=null
				&& paymentIntent.getTaTiers().getTaEmail().getAdresseEmail()!=null) {
			paymentIntent.setEmailTicket(paymentIntent.getTaTiers().getTaEmail().getAdresseEmail());
			
			emailTicketRecu = paymentIntent.getTaTiers().getTaEmail().getAdresseEmail();
		} else {
			emailTicketRecu = null;
		}
	}
	
	public void actMajEnvoyerTicketRecu() {
		try {
			paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
			if(envoyerTicketRecu) {
				initAdresseTicketRecu();
				if(emailTicketRecu!=null) {
					paymentIntent.setEmailTicket(emailTicketRecu);
					//mise à jour chez Stripe avant de déclencher un paiement
					paiementStripeDossierService.ajouterAdresseEmailPourRecu(compte, idStripePaymentIntent, emailTicketRecu);
				}
			} else {
				paiementStripeDossierService.ajouterAdresseEmailPourRecu(compte, idStripePaymentIntent, null);
				paymentIntent.setEmailTicket(null);
			}
			//conservation de l'adresse email dans BDG
			paymentIntent = taStripePaymentIntentService.merge(paymentIntent);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void actMajConserverCarte() {
		try {
			paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
			paymentIntent.setConserverCarte(conserverCarte);
			
			/**
			 * TODO mettre à jour le on-session off-session chez stripe via l'api stripe avant le paiement
			 */
			paymentIntent = taStripePaymentIntentService.merge(paymentIntent);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void paiementEnCours() {
		System.out.println("PaiementCbController.paiementEnCours()");
		
//		if(envoyerTicketRecu || conserverCarte) {
//			try {
//				paymentIntent = taStripePaymentIntentService.findByCode(secretClient.split("_secret_")[0]);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			paymentIntent.setConserverCarte(conserverCarte);
//			if(envoyerTicketRecu 
//					&& paymentIntent.getTaTiers()!=null 
//					&& paymentIntent.getTaTiers().getTaEmail()!=null
//					&& paymentIntent.getTaTiers().getTaEmail().getAdresseEmail()!=null) {
//				paymentIntent.setEmailTicket(paymentIntent.getTaTiers().getTaEmail().getAdresseEmail());
//			}
//			paymentIntent = taStripePaymentIntentService.merge(paymentIntent);
//		}
		
		
		interfaceStripeElementSaisie  = false;
		interfaceStripeElementEnCours = true;
	}

	public void paiementTermine() {
		System.out.println("PaiementCbController.paiementTermine() ");

		try {//processing
			interfaceStripeElementEnCours = false;
			
			interfaceStripeElementResultat = false;
			interfaceStripeElementResultatOk = false;
			interfaceStripeElementResultatErreur = false;
			interfaceStripeElementResultatNonTermine = false;

			Thread.sleep(4000);
			//Thread.sleep(60000);

			interfaceStripeElementResultat = true;

			paymentIntent = paiementStripeDossierService.updatePaymentIntentFromStripe(compte, idStripePaymentIntent, compteStripeConnect);
			
//			if(paymentIntent.getEmailTicket()!=null) {
//				paiementStripeDossierService.ajouterAdresseEmailPourRecu(compte, paymentIntent.getIdExterne(), paymentIntent.getEmailTicket());
//			}
		
			if(paymentIntent!=null && paymentIntent.getStatus().equals("succeeded")) {
				interfaceStripeElementResultatOk = true;
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Paiement CB",
						"Le paiement a été validé. Le règlement correspondant doit être visible dans la liste des règlement"));
			} else if(paymentIntent!=null && paymentIntent.getStatus().equals("processing")) {
				interfaceStripeElementResultatNonTermine = true;
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN , "Paiement CB", 
						"Le paiement est encore encore cours de validation. "
								+ "Quand il sera validé, le règlement correspondant apparetra dans la liste des règlements."
								+ "S'il n'apparait pas, veuillez verifier dans votre service de paiement (Stripe)"));
			} else  {
				interfaceStripeElementResultatErreur = true;
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", 
						"Une erreur est survenue pendant la validation du paiement. Il n'a pas été pris en compte"));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actAnnuler(ActionEvent actionEvent) {
		System.out.println("PaiementCbController.actAnnuler()");

		PrimeFaces.current().dialog().closeDynamic(idStripePaymentIntent);
	}

	public void actValiderMontant(ActionEvent actionEvent) {
		System.out.println("PaiementCbController.actValiderMontant() "+totalTTC);
		try {
			PaiementCarteBancaire pcb = new PaiementCarteBancaire();
			pcb.setNumeroCarte(numCarte);
			pcb.setNomPorteurCarte(nomCarte);
			pcb.setMoisCarte(moisCarte);
			pcb.setAnneeCarte(anneeCarte);
			pcb.setCryptogrammeCarte(cryptoCarte);
			pcb.setMontantPaiement(totalTTC);
			pcb.setOriginePaiement(param.getOriginePaiement());
			pcb.setCompteClient(true);
			//pcb.setFraisPlateforme(new BigDecimal(2.50)); //test
			
			if(param.getDocumentPayableCB()!=null) {
				//document payable pour lequel on a laissé la possibilité de modifier le montant du reglèment CB
				secretClient = taTicketDeCaisseService.creerPaymentIntent(pcb, param.getDocumentPayableCB(),libelle);
			} else {
				//paiment d'un montant libre sur un tiers
				secretClient = taTicketDeCaisseService.creerPaymentIntent(pcb, tiersPourReglementLibre,libelle);
			}
			idStripePaymentIntent = secretClient.split("_secret_")[0];
			
			paymentIntent = taStripePaymentIntentService.findByCode(idStripePaymentIntent);
			initAdresseTicketRecu();
			
			saisirMontant = false;
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		PrimeFaces.current().dialog().openDynamic("paiement/dialog_paiement_cb", options, params);
	}

	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		System.out.println("PaiementCbController.handleReturnDialoguePaiementEcheanceParCarte()");
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

	public int[] getListeMoisCB() {
		return listeMoisCB;
	}
	public void setListeMoisCB(int[] listeMoisCB) {
		this.listeMoisCB = listeMoisCB;
	}
	public int[] getListeAnneeCB() {
		return listeAnneeCB;
	}
	public void setListeAnneeCB(int[] listeAnneeCB) {
		this.listeAnneeCB = listeAnneeCB;
	}
	public BigDecimal getTotalHT() {
		return totalHT;
	}
	public void setTotalHT(BigDecimal totalHT) {
		this.totalHT = totalHT;
	}
	public BigDecimal getTotalTTC() {
		return totalTTC;
	}
	public void setTotalTTC(BigDecimal totalTTC) {
		this.totalTTC = totalTTC;
	}
	public BigDecimal getTotalTVA() {
		return totalTVA;
	}
	public void setTotalTVA(BigDecimal totalTVA) {
		this.totalTVA = totalTVA;
	}
	public String getNomCarte() {
		return nomCarte;
	}
	public void setNomCarte(String nomCarte) {
		this.nomCarte = nomCarte;
	}
	public int getMoisCarte() {
		return moisCarte;
	}
	public void setMoisCarte(int moisCarte) {
		this.moisCarte = moisCarte;
	}
	public int getAnneeCarte() {
		return anneeCarte;
	}
	public void setAnneeCarte(int anneeCarte) {
		this.anneeCarte = anneeCarte;
	}
	public String getCryptoCarte() {
		return cryptoCarte;
	}
	public void setCryptoCarte(String cryptoCarte) {
		this.cryptoCarte = cryptoCarte;
	}
	public String getNumCarte() {
		return numCarte;
	}
	public void setNumCarte(String numCarte) {
		this.numCarte = numCarte;
	}
	public String getTypeCarte() {
		return typeCarte;
	}
	public void setTypeCarte(String typeCarte) {
		this.typeCarte = typeCarte;
	}

	public TaTiers getTiersPourReglementLibre() {
		return tiersPourReglementLibre;
	}

	public void setTiersPourReglementLibre(TaTiers tiersPourReglementLibre) {
		this.tiersPourReglementLibre = tiersPourReglementLibre;
	}

	public boolean isMontantLibre() {
		return montantLibre;
	}

	public void setMontantLibre(boolean montantLibre) {
		this.montantLibre = montantLibre;
	}

	public boolean isSaisirMontant() {
		return saisirMontant;
	}

	public void setSaisirMontant(boolean saisirMontant) {
		this.saisirMontant = saisirMontant;
	}

	public boolean isInterfaceStripeElement() {
		return interfaceStripeElement;
	}

	public void setInterfaceStripeElement(boolean interfaceStripeElement) {
		this.interfaceStripeElement = interfaceStripeElement;
	}

	public boolean isInterfaceStripeElementNomComplet() {
		return interfaceStripeElementNomComplet;
	}

	public void setInterfaceStripeElementNomComplet(boolean interfaceStripeElementNomComplet) {
		this.interfaceStripeElementNomComplet = interfaceStripeElementNomComplet;
	}

	public boolean isInterfaceStripeElementEmail() {
		return interfaceStripeElementEmail;
	}

	public void setInterfaceStripeElementEmail(boolean interfaceStripeElementEmail) {
		this.interfaceStripeElementEmail = interfaceStripeElementEmail;
	}

	public boolean isInterfaceStripeElementTelephone() {
		return interfaceStripeElementTelephone;
	}

	public void setInterfaceStripeElementTelephone(boolean interfaceStripeElementTelephone) {
		this.interfaceStripeElementTelephone = interfaceStripeElementTelephone;
	}

	public String getClePubliqueStripe() {
		return clePubliqueStripe;
	}

	public String getSecretClient() {
		return secretClient;
	}

	public boolean isInterfaceStripeElementSaisie() {
		return interfaceStripeElementSaisie;
	}

	public void setInterfaceStripeElementSaisie(boolean interfaceStripeElementSaisie) {
		this.interfaceStripeElementSaisie = interfaceStripeElementSaisie;
	}

	public boolean isInterfaceStripeElementEnCours() {
		return interfaceStripeElementEnCours;
	}

	public void setInterfaceStripeElementEnCours(boolean interfaceStripeElementEnCours) {
		this.interfaceStripeElementEnCours = interfaceStripeElementEnCours;
	}

	public boolean isInterfaceStripeElementResultat() {
		return interfaceStripeElementResultat;
	}

	public void setInterfaceStripeElementResultat(boolean interfaceStripeElementResultat) {
		this.interfaceStripeElementResultat = interfaceStripeElementResultat;
	}

	public boolean isInterfaceStripeElementResultatOk() {
		return interfaceStripeElementResultatOk;
	}

	public void setInterfaceStripeElementResultatOk(boolean interfaceStripeElementResultatOk) {
		this.interfaceStripeElementResultatOk = interfaceStripeElementResultatOk;
	}

	public boolean isInterfaceStripeElementResultatErreur() {
		return interfaceStripeElementResultatErreur;
	}

	public void setInterfaceStripeElementResultatErreur(boolean interfaceStripeElementResultatErreur) {
		this.interfaceStripeElementResultatErreur = interfaceStripeElementResultatErreur;
	}

	public boolean isInterfaceStripeElementResultatNonTermine() {
		return interfaceStripeElementResultatNonTermine;
	}

	public void setInterfaceStripeElementResultatNonTermine(boolean interfaceStripeElementResultatNonTermine) {
		this.interfaceStripeElementResultatNonTermine = interfaceStripeElementResultatNonTermine;
	}

	public TaStripePaymentIntent getPaymentIntent() {
		return paymentIntent;
	}

	public void setPaymentIntent(TaStripePaymentIntent paymentIntent) {
		this.paymentIntent = paymentIntent;
	}

	public boolean isEnvoyerTicketRecu() {
		return envoyerTicketRecu;
	}

	public void setEnvoyerTicketRecu(boolean envoyerTicketRecu) {
		this.envoyerTicketRecu = envoyerTicketRecu;
	}

	public boolean isConserverCarte() {
		return conserverCarte;
	}

	public void setConserverCarte(boolean conserverCarte) {
		this.conserverCarte = conserverCarte;
	}

	public String getEmailTicketRecu() {
		return emailTicketRecu;
	}

	public void setEmailTicketRecu(String emailTicketRecu) {
		this.emailTicketRecu = emailTicketRecu;
	}

	public String getCompteStripeConnect() {
		return compteStripeConnect;
	}

	public void setCompteStripeConnect(String compteStripeConnect) {
		this.compteStripeConnect = compteStripeConnect;
	}

	public boolean isPaiementCbPossibleDansDossier() {
		return paiementCbPossibleDansDossier;
	}

	public void setPaiementCbPossibleDansDossier(boolean paiementCbPossibleDansDossier) {
		this.paiementCbPossibleDansDossier = paiementCbPossibleDansDossier;
	}
}  
