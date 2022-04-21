package fr.legrain.bdg.webapp.paiement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeTSourceServiceRemote;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCarteBancaireServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class CreerSourceStripeController extends AbstractController implements Serializable {  

	private static final long serialVersionUID = -3512103552691966197L;
	
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;
	
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeTSourceServiceRemote taStripeTSourceService;
	private @EJB ITaCarteBancaireServiceRemote taCarteBancaireService;
	private @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	private @EJB ITaTiersServiceRemote taTiersService;
	
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	
	private TaStripeSource taStripeSource;
	private TaStripeCustomer taStripeCustomer;
	private TaCarteBancaire taCarteBancaire;
	private TaCompteBanque taCompteBanque;
	private TaTiers taTiers;

	private String libelle;
	
	private CreerSourceStripeParam param;
	
	private String clePubliqueStripe;
	private String secretClient;
	
	private TaCompteServiceWebExterne compte = null;
	
	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		param = (CreerSourceStripeParam) sessionMap.get(CreerSourceStripeParam.NOM_OBJET_EN_SESSION);
		sessionMap.remove(PaiementParam.NOM_OBJET_EN_SESSION);
		
		compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		UtilServiceWebExterne util = new UtilServiceWebExterne();
		compte = util.decrypter(compte);
		
		System.out.println("PaiementStripeDossierService clé 1 "+ compte.getApiKey1());	
		System.out.println("PaiementStripeDossierService clé 2 "+ compte.getApiKey2());
		System.out.println("PaiementStripeDossierService clé 3 "+ compte.getValeur1());
		System.out.println("PaiementStripeDossierService clé 4 "+ compte.getValeur2());
		
//		boolean modeTest = false;
//		modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();

		String API_KEY_DOSSIER_PK_LIVE = compte.getApiKey2();
		clePubliqueStripe = API_KEY_DOSSIER_PK_LIVE;
		
		if(param!=null) {
			taStripeSource = param.getTaStripeSource();
			taStripeCustomer = param.getTaStripeCustomer();
			taCarteBancaire = param.getTaCarteBancaire();
			taCompteBanque = param.getTaCompteBanque();
			taTiers = param.getTaTiers();
			libelle = param.getLibelle();
		}
		secretClient = paiementEnLigneDossierService.creerSetupIntent(compte);
	}
	
	
	
	public void actAnnuler(ActionEvent actionEvent) {
		System.out.println("CreerSourceStripeController.actAnnuler()");
		
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	static public void actDialogueCreerSourceStripe(CreerSourceStripeParam param) {
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
		
		sessionMap.put(CreerSourceStripeParam.NOM_OBJET_EN_SESSION, param);
        
        PrimeFaces.current().dialog().openDynamic("paiement/dialog_creer_source_stripe", options, params);
	}
	
	
	public void actCreerTokenStripeCB(ActionEvent e) {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String x = params.get("x");
		System.out.println("PaiementCbController.actCreerTokenStripeCB() ******* **-- -*-*-- "+x);
		
	}
	
	public void actCreerSourceStripeCB(ActionEvent evt) {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		String x = params.get("x");
		System.out.println("PaiementCbController.actCreerSourceStripeCB() ******* **-- -*-*-- "+x);
		
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
				TaStripeSourceDTO taStripeSourceDTO = paiementEnLigneDossierService.rechercherPaymentMethod(compte,x);
				s.setIdExterne(x);
				s.setTaCarteBancaire(taCarteBancaireService.findById(taCarteBancaire.getIdCarteBancaire()));
				s.setTaStripeTSource(taStripeTSourceService.findByCode(TaStripeTSource.TYPE_SOURCE_CB));
				
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
				paiementEnLigneDossierService.attacherPayementMethodStripe(compte, s.getIdExterne(), taStripeCustomer.getIdExterne());
				s.setTaStripeCustomer(taStripeCustomer);
				s = taStripeSourceService.merge(s);
				//si ce tiers n'a pas de source par defaut, affecter cette nouvelle source en tant que source par defaut
				taStripeCustomer.getSources().add(s);
				taStripeCustomer.setTaSourceDefault(s);
				taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
			} else {
				//créer un customer avec cette source par defaut
				String idCustomerStripe = paiementEnLigneDossierService.creerCustomerStripe(compte, taTiers.getTaEmail().getAdresseEmail(), s.getIdExterne(), taTiers.getNomTiers()+" "+taTiers.getCodeTiers());
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
			
			PrimeFaces.current().dialog().closeDynamic(null);
		} catch(Exception e) {
			e.printStackTrace();
		}
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



	public TaStripeSource getTaStripeSource() {
		return taStripeSource;
	}



	public void setTaStripeSource(TaStripeSource taStripeSource) {
		this.taStripeSource = taStripeSource;
	}



	public TaStripeCustomer getTaStripeCustomer() {
		return taStripeCustomer;
	}



	public void setTaStripeCustomer(TaStripeCustomer taStripeCustomer) {
		this.taStripeCustomer = taStripeCustomer;
	}



	public TaCarteBancaire getTaCarteBancaire() {
		return taCarteBancaire;
	}



	public void setTaCarteBancaire(TaCarteBancaire taCarteBancaire) {
		this.taCarteBancaire = taCarteBancaire;
	}



	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}



	public void setTaCompteBanque(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
	}



	public TaTiers getTaTiers() {
		return taTiers;
	}



	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}



	public String getLibelle() {
		return libelle;
	}



	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}



	public CreerSourceStripeParam getParam() {
		return param;
	}



	public void setParam(CreerSourceStripeParam param) {
		this.param = param;
	}



	public String getClePubliqueStripe() {
		return clePubliqueStripe;
	}



	public void setClePubliqueStripe(String clePubliqueStripe) {
		this.clePubliqueStripe = clePubliqueStripe;
	}



	public String getSecretClient() {
		return secretClient;
	}



	public void setSecretClient(String secretClient) {
		this.secretClient = secretClient;
	}
	
	
}  
