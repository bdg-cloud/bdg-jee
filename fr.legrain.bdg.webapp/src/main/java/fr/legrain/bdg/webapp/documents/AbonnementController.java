package fr.legrain.bdg.webapp.documents;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.service.remote.ITaFrequenceServiceRemote;
import fr.legrain.abonnement.stripe.dao.IStripePlanDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
import fr.legrain.abonnement.stripe.service.MultitenantProxyTimerAbonnement;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.autorisations.ws.Remove;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCouponServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceItemServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeTSourceServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.abonnement.TaLAbonnementDTOJSF;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.SmsParam;
import fr.legrain.bdg.webapp.tiers.MoyenPaiementAbonnementDTOJSF;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.bdg.ws.rest.client.Config;
import fr.legrain.cron.model.TaCron;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class AbonnementController extends AbstractDocumentController<TaAbonnement,TaAbonnementDTO,TaLAbonnement,TaLAbonnementDTO,TaLAbonnementDTOJSF,TaInfosAbonnement> {  

	@Inject @Named(value="ouvertureTiersBean")
	private OuvertureTiersBean ouvertureTiersBean;

	@Inject @Named(value="ouvertureArticleBean")
	private OuvertureArticleBean ouvertureArticleBean;

	/****************************************/

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaStripeProductServiceRemote taStripeProductService;
	private @EJB ITaStripePlanServiceRemote taStripePlanService;
	private @EJB ITaStripeCouponServiceRemote taStripeCouponService;

	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaStripeChargeServiceRemote taStripeChargeService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeTSourceServiceRemote taStripeTSourceService;
	private @EJB ITaStripeInvoiceServiceRemote taStripeInvoiceService;
	private @EJB ITaStripeInvoiceItemServiceRemote taStripeInvoiceItemService;
	private @EJB ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;

	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;

	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;

	//	private @EJB TimerDeclenchePaiementPrevuService timerDeclenchePaiementPrevuService;
	//	private @EJB TimerCreerAvisEcheanceEtPaiementPrevuService timerCreerAvisEcheanceEtPaiementPrevuService;
	private @EJB ITaCronServiceRemote taCronService;
	private @EJB MultitenantProxyTimerAbonnement multitenantProxyTimerEvenement;

	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;
	
	private @EJB MultitenantProxyTimerAbonnement multitenantProxyTimerAbonnement;

	private TaStripeCustomer taStripeCustomer = new TaStripeCustomer();
	private TaStripeCustomerDTO newTaStripeCustomerDTO = new TaStripeCustomerDTO();
	private TaStripeCustomerDTO selectedTaStripeCustomerDTO = new TaStripeCustomerDTO();

	//private TaStripeSubscriptionDTO selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
	//private TaStripeSubscriptionDTO newTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
	//private List<TaStripeSubscriptionDTO> listeSubscription = new ArrayList<>();
	//private List<TaStripeSubscriptionDTO> listeSubscriptionNonStripe = new ArrayList<>();
	//private TaStripeSubscription taStripeSubscription = new TaStripeSubscription();

	private List<TaStripeSourceDTO> listeSource = new ArrayList<>();
	private TaStripeSourceDTO selectedTaStripeSourceDefautDTO = new TaStripeSourceDTO();
	private TaStripeSourceDTO selectedTaStripeSourceSubscriptionDTO = new TaStripeSourceDTO();

	private List<TaStripePaiementPrevuDTO> listePaiementPrevu = new ArrayList<>();
	private TaStripePaiementPrevuDTO selectedTaStripePaiementPrevuDTO = new TaStripePaiementPrevuDTO();
	private TaStripePaiementPrevu taStripePaiementPrevu = new TaStripePaiementPrevu();

	private TaStripeCouponDTO selectedTaStripeCouponDTO = new TaStripeCouponDTO();

	//private TaStripeSubscriptionItemDTO selectedTaStripeSubscriptionItemDTO = new TaStripeSubscriptionItemDTO();
	//private TaStripeSubscriptionItemDTO newTaStripeSubscriptionItemDTO = new TaStripeSubscriptionItemDTO();
	//private List<TaStripeSubscriptionItemDTO> listeSubscriptionItem = new ArrayList<>();
	//private TaStripeSubscriptionItem taStripeSubscriptionItem = new TaStripeSubscriptionItem();

	private TaStripeInvoiceDTO selectedTaStripeInvoiceDTO = new TaStripeInvoiceDTO();
	private List<TaStripeInvoiceDTO> listeInvoice = new ArrayList<>();

	private TaStripeInvoiceDTO selectedTaStripeInvoiceCustomerDTO = new TaStripeInvoiceDTO();
	private List<TaStripeInvoiceDTO> listeInvoiceCustomer = new ArrayList<>();

	private TaStripeChargeDTO selectedTaStripeChargeCustomerDTO = new TaStripeChargeDTO();
	private List<TaStripeChargeDTO> listeChargeCustomer = new ArrayList<>();

	private TaStripeInvoiceItemDTO selectedTaStripeInvoiceItemDTO = new TaStripeInvoiceItemDTO();
	private List<TaStripeInvoiceItemDTO> listeInvoiceItem = new ArrayList<>();

	private List<TaAvisEcheance> listeAvisEcheance = new ArrayList<>();
	private List<TaAvisEcheance> listeAvisEcheanceAbonnement = new ArrayList<>();
	private List<TaLEcheance> listeEcheanceNonEmiseAbonnement = new ArrayList<>();
	private List<TaLEcheance> listeLEcheance = new ArrayList<>();

	private List<TaStripePlanDTO> listePlan = new ArrayList<>();
	private TaStripePlanDTO selectedTaStripePlanDTO = new TaStripePlanDTO();
	private TaStripePlan taStripePlan = new TaStripePlan();

	private List<TaStripeProductDTO> listeProduct = new ArrayList<>();
	private TaStripeProductDTO selectedTaStripeProductDTO = new TaStripeProductDTO();
	private TaStripeProduct taStripeProduct = new TaStripeProduct();

	private LgrDozerMapper<TaStripePaiementPrevuDTO,TaStripePaiementPrevu> mapperUIToModelPaiementPrevu  = new LgrDozerMapper<>();

//	private LgrDozerMapper<TaStripeSubscriptionDTO,TaStripeSubscription> mapperUIToModelSubscription  = new LgrDozerMapper<>();
//	private LgrDozerMapper<TaStripeSubscription,TaStripeSubscriptionDTO> mapperModelToUISubscription  = new LgrDozerMapper<>();

	private LgrDozerMapper<TaStripeCustomerDTO,TaStripeCustomer> mapperUIToModelCustomer  = new LgrDozerMapper<TaStripeCustomerDTO,TaStripeCustomer>();
	private LgrDozerMapper<TaStripeCustomer,TaStripeCustomerDTO> mapperModelToUICustomer = new LgrDozerMapper<TaStripeCustomer,TaStripeCustomerDTO>();

	//	private TaTiers masterEntity;
	private TaCompteServiceWebExterne compte = null;

//	private TaTiersDTO taTiersDTO;
	private boolean modeDialogue;

	private MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF;
	/****************************************** */

	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	private @EJB ITaInfosAbonnementServiceRemote taInfosDocumentService;
	private @EJB ITaLAbonnementServiceRemote taLAbonnementService;
	private @EJB ITaFrequenceServiceRemote taFrequenceService;
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	
	private List<TaFrequenceDTO> listeFrequenceDTO = new ArrayList<TaFrequenceDTO>();
	private TaFrequenceDTO selectedTaFrequenceDTO = new TaFrequenceDTO();
	private Integer nbFrequence = 1;
	
	private List<String> listeTypeAbonnement = new ArrayList<String>();
	
	private String typeAbonnementAvecEngagement = TaAbonnement.TYPE_AVEC_ENGAGEMENT;
	private String typeAbonnementSansEngagement = TaAbonnement.TYPE_SANS_ENGAGEMENT;
	
	private Date dateDebutPeriode;
	private Date dateFinPeriode;
	
	//List<TaLAbonnement> nouvellesLignes = new ArrayList<TaLAbonnement>();
	
	private boolean disabledEnregistrement = false;
	
	private boolean CronValide = true;
	private String typeDoc = TaAbonnement.TYPE_DOC;
	
	@Inject private IStripePlanDAO daoPlan;
	
	private Integer nbMoisSuppressionAvis = 18;


	public AbonnementController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("devis");		
		listeTypeAbonnement.add(TaAbonnement.TYPE_AVEC_ENGAGEMENT);
		listeTypeAbonnement.add(TaAbonnement.TYPE_SANS_ENGAGEMENT);
		
		
		
		nomDocument="Abonnement";
		setTaDocumentService(taAbonnementService);
		setTaLDocumentService(taLAbonnementService);
		setTaInfosDocumentService(taInfosDocumentService);
		listeFrequenceDTO = taFrequenceService.findAllLight();
		stepSynthese = "idSyntheseAbonnement";
		stepEntete = "idEnteteAbonnement";
		stepLignes = "idLignesAbonnement";
		stepTotaux = "idTotauxFormAbonnement";
		stepValidation = "idValidationFormAbonnement";
		idMenuBouton = "form:tabView:idMenuBoutonAbonnement";
		wvEcran = LgrTab.WV_TAB_ABONNEMENT;
		classeCssDataTableLigne = "myclassAbonnement";
		idBtnWizardButtonPrecedent = "form:tabView:idBtnWizardButtonPrecedentAbonnement";
		idBtnWizardButtonSuivant = "form:tabView:idBtnWizardButtonSuivantAbonnement";

		JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard('.wizard-abonnement')";
		gestionBoutonWizardDynamique = true;

		refresh();

	}
	public void changeTypeAbonnement() {
		System.out.println("cliqué sur change Type Abo : ");
		System.out.println(selectedDocumentDTO.getTypeAbonnement());
		if(selectedDocumentDTO.getTypeAbonnement().equals(typeAbonnementAvecEngagement)) {
			selectedDocumentDTO.setNbMoisDureeEngagement(1);
			//calculeDateFinPeriodeActive();
//			verifieSiFacturationDivisible();
//			calculeDatesAbo();
			verifieSiFacturationDivisibleEtCalculDatesAbo();
		}else {
			selectedDocumentDTO.setNbMoisDureeEngagement(null);
			selectedDocumentDTO.setDateFin(null);
			selectedDocumentDTO.setDateFinEngagement(null);
		}
	}
	
	public void calculeDatesAbo() {
		calculeDatesAbo(null);

	}
	
	public void calculeDatesAbo(ActionEvent actionEvent) {	
		calculeDateFinEngagement();
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
			//on ne donne plus de dates de période active au un abonnement lors de sa création. Il faut regler une échéance pour activé les dates de péridoes actives.
			//selectedDocumentDTO.setDateDebutPeriodeActive(selectedDocumentDTO.getDateDebut());
			dateDebutPeriode = selectedDocumentDTO.getDateDebut();
		}
		calculeDateFinPeriodeActive();
		

	}
	
	public void calculeDatesAboLigne() {
		calculeDatesAboLigne(null);

	}
	
	public void calculeDatesAboLigne(ActionEvent actionEvent) {
		
	}
	
	public LocalDate dateToLocalDate(Date date) {
		if(date != null) {
			return LibDate.dateToLocalDate(date);
		}else {
			return null;
		}
		
	}

	
	public boolean editableLigneEnInsertionUniquement(TaLAbonnementDTO ligneAbo) {
		boolean disabled = false;
		if(ligneAbo.getIdLDocument() != null) {
			disabled = true;
		}
		return disabled;
	}
	
	
	public void verifieSiFacturationDivisibleEtCalculDatesAbo() {
		verifieSiFacturationDivisibleEtCalculDatesAbo(null);
	}
	public void verifieSiFacturationDivisibleEtCalculDatesAbo(ActionEvent actionEvent) {
		verifieSiFacturationDivisible();
		calculeDatesAbo();
	}
	
	
	public void calculeDateFinPeriodeActive() {
		calculeDateFinPeriodeActive(null);
	}
	/**
	 * Calcule la date de fin période active
	 * @param actionEvent
	 */
	public void calculeDateFinPeriodeActive(ActionEvent actionEvent) {
		
		if(dateDebutPeriode != null) {
			Date dateDebut = dateDebutPeriode;
			Date dateFin = null;
			LocalDate localdateFin = null;
			if(selectedDocumentDTO.getNbMoisFrequenceFacturation() != null) {				
				//On prend la date de début et on y ajoute le nombre de mois choisi pour la facturation moins 1 jour pour trouver la date de fin de la période active
				dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusMonths(selectedDocumentDTO.getNbMoisFrequenceFacturation()).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());									
			}			
			dateFinPeriode=dateFin;
		}
		
		if(selectedDocumentDTO.getDateDebutPeriodeActive() != null ) {
			Date dateDebut = selectedDocumentDTO.getDateDebutPeriodeActive();
			Date dateFin = null;
			LocalDate localdateFin = null;
			if(selectedDocumentDTO.getNbMoisFrequenceFacturation() != null) {				
				//On prend la date de début et on y ajoute le nombre de mois choisi pour la facturation moins 1 jour pour trouver la date de fin de la période active
				dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusMonths(selectedDocumentDTO.getNbMoisFrequenceFacturation()).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());									
			}			
			//selectedDocumentDTO.setDateFinPeriodeActive(dateFin);
			
			dateFinPeriode=dateFin;
		
		}
	}
	
	public Boolean IsDivisible(int x, int n)
	{
	   return (x % n) == 0;
	}
	

	
	
	
	public void vertifConcordancePlan() {
		vertifConcordancePlan(null);
	}
	/**
	 * Cette méthode vérifie si les plans choisis dans les lignes d'abo correspondent a la fréquence de facturation choisie
	 * @param actionEvent
	 */
	public void vertifConcordancePlan(ActionEvent actionEvent) {
		
		List<TaLAbonnementDTOJSF> lignes = valuesLigne;
		disabledEnregistrement = false;
		for (TaLAbonnementDTOJSF taLAbonnement : lignes) {
			if(taLAbonnement.getTaStripePlan().getNbMois() != selectedDocumentDTO.getNbMoisFrequenceFacturation()) {
				disabledEnregistrement = true;
			}
		}
		
		if (disabledEnregistrement) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", " Au moin un des plans choisis ne correspond pas à la fréquence de facturation choisie. Veuillez changer de plan ou changer de fréquence de facturation. L'enregistrement de cet abonnement est impossible jusqu'a résolution."));
		} 
	}
	
	
	public void verifieSiFacturationDivisible() {
		verifieSiFacturationDivisible(null);
	}
	public void verifieSiFacturationDivisible(ActionEvent actionEvent) {
		//si l'abonnement est de type avec engagement
		if(selectedDocumentDTO.getTypeAbonnement().equals(TaAbonnement.TYPE_AVEC_ENGAGEMENT)) {
			//on vérifie que la fréquence de facturatrion soit bien un multiple de la durée d'engagement
			if(selectedDocumentDTO.getNbMoisDureeEngagement() != null) {
				if(IsDivisible(selectedDocumentDTO.getNbMoisDureeEngagement(), selectedDocumentDTO.getNbMoisFrequenceFacturation())) {
					
				}else {
					selectedDocumentDTO.setNbMoisFrequenceFacturation(1);
					FacesContext context = FacesContext.getCurrentInstance();  
					//context.addMessage(null, new FacesMessage("Abonnement", "actEnregistrer")); 
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", " Veuillez choisir une fréquence de facturation qui soit un multiple entier de la durée d'engagement."));
				}
			}
			
		}
		verifieSiPaiementDivisible();
		
	}
	public void verifieSiPaiementDivisible() {
		verifieSiPaiementDivisible(null);
	}
	public void verifieSiPaiementDivisible(ActionEvent actionEvent) {
		if(IsDivisible(selectedDocumentDTO.getNbMoisFrequenceFacturation(), selectedDocumentDTO.getNbMoisFrequencePaiement())) {
			
		}else {
			selectedDocumentDTO.setNbMoisFrequencePaiement(1);
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Abonnement", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", " Veuillez choisir une fréquence de paiement qui soit un multiple entier de la fréquence de facturation."));
		}
	}
	public void calculeDateFinEngagement() {
		calculeDateFinEngagement(null);
	}
	/**
	 * Calcule la date de fin de l'engagement (cette date est optionnelle)
	 * @param actionEvent
	 */
	public void calculeDateFinEngagement(ActionEvent actionEvent) {
		if(selectedDocumentDTO.getTypeAbonnement().equals(TaAbonnement.TYPE_AVEC_ENGAGEMENT)) {
			if(selectedDocumentDTO.getDateDebut() != null ) {
				Date dateDebut = selectedDocumentDTO.getDateDebut();
				Date dateFin = null;
				LocalDate localdateFin = null;
				//On prend la date de début et on y ajoute X mois durée d'engagement choisi moins 1 jour pour trouver la date de fin de l'abo
				dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusMonths(selectedDocumentDTO.getNbMoisDureeEngagement()).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
				selectedDocumentDTO.setDateFin(dateFin);		
				selectedDocumentDTO.setDateFinEngagement(dateFin);	
				
			}
			verifieSiFacturationDivisible();
			//verifieSiPaiementDivisible();
		}
		

	}
	
	//old
//	public void calculeDateFinEngagement(ActionEvent actionEvent) {
//		if(selectedDocumentDTO.getDateDebut() != null && selectedTaFrequenceDTO != null) {
//			Date dateDebut = selectedDocumentDTO.getDateDebut();
//			Date dateFin = null;
//			LocalDate localdateFin = null;
//			if(nbFrequence > 0) {
//				switch (selectedTaFrequenceDTO.getCodeFrequence()) {
//				//On prend la date de début et on y ajoute X fois (nbFrequence) la fréquence choisi (année, mois, etc...) moins 1 jour pour trouver la date de fin de l'abo
//				case TaFrequence.CODE_FREQUENCE_JOUR:
//					dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusDays(nbFrequence).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
//					break;
//
//				case TaFrequence.CODE_FREQUENCE_AN:
//					dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusYears(nbFrequence).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
//					break;
//					
//				case TaFrequence.CODE_FREQUENCE_SEMAINE:
//					dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusWeeks(nbFrequence).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
//					break;
//					
//				case TaFrequence.CODE_FREQUENCE_MOIS:
//					dateFin = Date.from(LocalDateTime.from(dateDebut.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).plusMonths(nbFrequence).minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
//					break;
//				}
//			}
//			
//			selectedDocumentDTO.setDateFin(dateFin);						
//			
//		}
//	}

	public void refresh(){
		values = taAbonnementService.selectAllDTO();
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	/**
	 * Cette méthode vérifie si le CRON responsable de la génération des prochaines échéances, de la suspension et suppression des échéances,
	 *  et suspension ou annulation des lignes d'abo est bien passé il y a moins de 24 heures
	 */
	public void verifPassageCRON() {
		CronValide = taCronService.verifPassageCRONGenerationEcheance();
		//si il est plus grand que 24 heures, c'est que le dernier passage CRON ne s'est pas fait
		if(!CronValide) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", "Attention, il semble que le partie du programme BDG chargée de générer automatiquement les prochaines échéances d'abonnement,"
					+ " de suspendre ou supprimer les échéances d'abonnement, de suspendre ou d'annuler les lignes d'abonnement associées et enfin de créer les paiements prévus ne se soit pas déclenché comme prévue."
					+ " Nous vous conseillons fortement de lancer manuellement ces opérations via le bouton Lancer le cron dans l'écran Gestion des abonnements. Si l'opération se passse bien, ce message disparaitra quand vous rechargerai cet abonnement."));
		}
				
			
	}
	
	
	public List<TaLAbonnementDTOJSF> IHMmodel() {
		LinkedList<TaLAbonnement> ldao = new LinkedList<TaLAbonnement>();
		LinkedList<TaLAbonnementDTOJSF> lswt = new LinkedList<TaLAbonnementDTOJSF>();

		try {
			if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {
	
				ldao.addAll(masterEntity.getLignes());
	
				LgrDozerMapper<TaLAbonnement,TaLAbonnementDTO> mapper = new LgrDozerMapper<TaLAbonnement,TaLAbonnementDTO>();
				//LgrDozerMapper<TaStripeSubscriptionItem,TaStripeSubscriptionItemDTO> mapper2 = new LgrDozerMapper<TaStripeSubscriptionItem,TaStripeSubscriptionItemDTO>();
				TaLAbonnementDTO dto = null;
				//TaStripeSubscriptionItemDTO dto2 = null;
				for (TaLAbonnement o : ldao) {
					TaLAbonnementDTOJSF t = new TaLAbonnementDTOJSF();
					dto = (TaLAbonnementDTO) mapper.map(o, TaLAbonnementDTO.class);
					//dto2 = (TaStripeSubscriptionItemDTO) mapper2.map(o.getTaStripeSubscriptionItem(), TaStripeSubscriptionItemDTO.class);
					t.setDto(dto);
					//t.setDto2(dto2);
					t.setTaArticle(o.getTaArticle());
					if(o.getTaArticle()!=null){
						t.setTaRapport(o.getTaArticle().getTaRapportUnite());
					}
					if(o.getTaArticle()!=null){
						//t.setTaStripeProductDTO(taStripeProductService.findByIdDTO(taStripeProductService.rechercherProduct(o.getTaArticle()).getIdStripeProduct()));
					}
					//if(o.getTaStripeSubscriptionItem()!=null){
						//t.setTaStripeSubscriptionItem(o.getTaStripeSubscriptionItem());
						if(o.getTaPlan()!=null){
							t.setTaStripePlan(taStripePlanService.findByLigneAbo(o));
							//rajout check null yann
							if(t.getTaStripePlan()!=null) {
								t.setTaStripePlanDTO(taStripePlanService.findByIdDTO(t.getTaStripePlan().getIdStripePlan()));
							}
							
						}
					//}
					//				t.setTaEntrepot(o.getTaEntrepot()); //@@ champs à ajouter aux devis
					if(o.getU1LDocument()!=null) {
						t.setTaUnite1(new TaUnite());
						t.getTaUnite1().setCodeUnite(o.getU1LDocument());
					}
					if(o.getU2LDocument()!=null) {
						t.setTaUnite2(new TaUnite());
						t.getTaUnite2().setCodeUnite(o.getU2LDocument());
					}
					t.setTaREtatLigneDocument(o.getTaREtatLigneDocument());
					if(o.getTaREtatLigneDocument()!=null && o.getTaREtatLigneDocument().getTaEtat()!=null)
						t.setCodeEtat(o.getTaREtatLigneDocument().getTaEtat().getCodeEtat());
					t.updateDTO(false);
					
					List<TaLigneALigneEcheanceDTO>  le=rechercheSiLigneEcheanceDocLie(t);
					if(le!=null) {
						for (TaLigneALigneEcheanceDTO ligne : le) {
								if(t.getLigneAbonnement()==null)t.setLigneAbonnement(new LinkedList<>());
								t.getLigneAbonnement().add(ligne);
							}
					}
					lswt.add(t);
				}
	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lswt;
	}

	//	public String handleFlow(FlowEvent event) {
	//		try{
	//			currentStepId = event.getOldStep();
	//			String stepToGo = event.getNewStep();
	//
	//			if(modeEcranLigne.dataSetEnModif())
	//				actEnregistrerLigne(null);
	//
	//			if(currentStepId.equals(stepEntete) && stepToGo.equals(stepLignes)) {
	//				mapperUIToModel.map(selectedDocumentDTO, masterEntity);
	//			}
	//
	//
	//			currentStepId=event.getNewStep();
	//			RequestContext.getCurrentInstance().update("form:tabView:idMenuBoutonDevis");
	//			return currentStepId;
	//		} catch (Exception e) {
	//			FacesContext context = FacesContext.getCurrentInstance();  
	//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", e.getMessage()));
	//			return event.getOldStep();
	//		}
	//	}


	//	public void actAutoInsererLigne(ActionEvent actionEvent) /*throws Exception*/ {
	//		boolean existeDeja=false;
	//		if(insertAuto) {
	//			for (ILigneDocumentJSF ligne : valuesLigne) {
	//				if(ligne.ligneEstVide())existeDeja=true;
	//			}
	//			if(!existeDeja){	
	//			actInsererLigne(actionEvent);
	//			
	//			String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
	//			RequestContext.getCurrentInstance().execute(oncomplete);
	//			}
	//		} else {
	////			throw new Exception();
	//		}
	//	}



	public void actEnregistrerLigne(ActionEvent actionEvent) {

		try {
			selectedLigneJSF.updateDTO(false);


			LgrDozerMapper<TaLAbonnementDTO,TaLAbonnement> mapper = new LgrDozerMapper<TaLAbonnementDTO,TaLAbonnement>();
			mapper.map((TaLAbonnementDTO) selectedLigneJSF.getDto(),masterEntityLigne);



			if(masterEntityLigne.getCodeTvaLDocument()!=null && !masterEntityLigne.getCodeTvaLDocument().isEmpty()) {
				TaTva tva=taTvaService.findByCode(masterEntityLigne.getCodeTvaLDocument());
				if(tva!=null)
					masterEntityLigne.setLibTvaLDocument(tva.getLibelleTva());
			}
			
			
			if(selectedLigneJSF.getTaStripePlanDTO()!=null) {
				masterEntityLigne.setTaPlan(taStripePlanService.findById(selectedLigneJSF.getTaStripePlanDTO().getId()));
			}
			if(selectedLigneJSF.getDto()!=null && selectedLigneJSF.getDto().getQteLDocument()!= null) {
				//masterEntityLigne.setQuantity(selectedLigneJSF.getDto().getQteLDocument().intValue());
				masterEntityLigne.setQteLDocument(selectedLigneJSF.getDto().getQteLDocument());
			}
			if(selectedLigneJSF.getDto()!=null) {
				masterEntityLigne.setComplement1(selectedLigneJSF.getDto().getComplement1());
				masterEntityLigne.setComplement2(selectedLigneJSF.getDto().getComplement2());
				masterEntityLigne.setComplement3(selectedLigneJSF.getDto().getComplement3());
			}
			
			
			

			masterEntity.enregistreLigne(masterEntityLigne);
			if(!masterEntity.getLignes().contains(masterEntityLigne)) {
				masterEntity.addLigne(masterEntityLigne);
				
			}

			

			masterEntity.calculeTvaEtTotaux();
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);

			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			vertifConcordancePlan();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Abonnement", "actEnregisterLigne")); 
		}
	}


	/**
	 * Annule un abonnement et supprime toutes ses lignes d'échéances non liés à un avis d'échéance
	 * @author yann
	 * @param actionEvent
	 */
	public void actAnnulerAbonnement(ActionEvent actionEvent) {
		if(selectedDocumentDTO.getId() != null && selectedDocumentDTO.getId() != 0) {
			//if(masterEntity.getTaStripeSubscription().getDateAnnulation() == null) {
			if(masterEntity.getDateAnnulation() == null) {
				System.out.println("j'annule l'abonnement : ");
				System.out.println(" "+selectedDocumentDTO.getCodeDocument());
				System.out.println("avec le commentaire annulation suivant :");
				System.out.println(" "+selectedDocumentDTO.getCommentaireAnnulation());
				Date now = new Date();
//				masterEntity.getTaStripeSubscription().setDateAnnulation(now);
//				masterEntity.getTaStripeSubscription().setCommentaireAnnulation(selectedTaStripeSubscriptionDTO.getCommentaireAnnulation());
				masterEntity.setDateAnnulation(now);
				masterEntity.setCommentaireAnnulation(selectedDocumentDTO.getCommentaireAnnulation());
				masterEntity = taAbonnementService.merge(masterEntity);
				//actEnregistrer(null);
				//on cherche toutes les échéances à supprimer pour cet abonnement
				//List<TaLEcheance> listeEchASupprimer = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceSubscription(masterEntity.getTaStripeSubscription());
				List<TaLEcheance> listeEchASupprimer = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceAbonnement(masterEntity);
				
				for (TaLEcheance taLEcheance : listeEchASupprimer) {
					try {
						taLEcheanceService.remove(taLEcheance);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				updateTab();
				
				
			}
			
		}
	}
	
	/**
	 * Annule une ligne d'abonnement (etat doc_abandonne) et supprime toutes ses lignes d'échéances 
	 * @author yann
	 * @param TaLAbonnementDTOJSF ligne d'abonnement
	 */
	public void actAnnulerLigne(TaLAbonnementDTOJSF ligne) {
		if(ligne != null ) {
		
			TaLAbonnement ligneAbo;
			try {
				ligneAbo = taLAbonnementService.findById(ligne.getDto().getIdLDocument());
				taLAbonnementService.annuleLigne(ligneAbo);
					
				updateTab();
					
			} catch (FinderException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
		}
	}
	
	
	
	
	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taAbonnementService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}
				masterEntity=new TaAbonnement();
				selectedDocumentDTO=new TaAbonnementDTO();

				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);

				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taAbonnementService.findById(selectedDocumentDTO.getId());
					selectedDocumentDTO=taAbonnementService.findByIdDTO(selectedDocumentDTO.getId());
				}				
				break;

			default:
				break;
			}			

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Abonnement", "actAnnuler")); 
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void autoCompleteMapUIToDTO() {
		//		if(taTiers!=null) {
		//			validateUIField(Const.C_CODE_TIERS,taTiers);
		//			selectedDocumentDTO.setCodeTiers(taTiers.getCodeTiers());
		//		}
		if(taTiersDTO!=null) {
			validateUIField(Const.C_CODE_TIERS,taTiersDTO);
			selectedDocumentDTO.setCodeTiers(taTiersDTO.getCodeTiers());
		}
		if(taTPaiement!=null) {
			validateUIField(Const.C_CODE_T_PAIEMENT,taTPaiement.getCodeTPaiement());
			selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
			selectedDocumentDTO.setLiblTPaiement(taTPaiement.getLibTPaiement());
		}
		if(taCPaiementDoc!=null) {
			validateUIField(Const.C_CODE_C_PAIEMENT,taCPaiementDoc.getCodeCPaiement());
			selectedCPaiement.setCodeCPaiement(taCPaiementDoc.getCodeCPaiement());
		}

		if(taTTvaDoc!=null) {
			validateUIField(Const.C_CODE_T_TVA_DOC,taTTvaDoc.getCodeTTvaDoc());
			selectedDocumentDTO.setCodeTTvaDoc(taTTvaDoc.getCodeTTvaDoc());
		}
//		if(selectedEtatDocument!=null) {
//			validateUIField(Const.C_CODE_ETAT,selectedEtatDocument.getCodeEtat());
//			selectedDocumentDTO.setCodeEtat(selectedEtatDocument.getCodeEtat());
//			masterEntity.setTaEtat(selectedEtatDocument);
//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTiers = null;
			taTiersDTO = null;
			taTPaiement = null;
			taTTvaDoc = null;
			taCPaiementDoc = null;
			//			taTEntite = null;
			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
				taTiersDTO = taTiersService.findByCodeDTO(selectedDocumentDTO.getCodeTiers());
//				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
			}
			if(selectedDocumentDTO.getCodeTPaiement()!=null && !selectedDocumentDTO.getCodeTPaiement().equals("")) {
				taTPaiement = taTPaiementService.findByCode(selectedDocumentDTO.getCodeTPaiement());
				if(taTPaiement != null) {
					selectedDocumentDTO.setLiblTPaiement(taTPaiement.getLibTPaiement());
				}
			}
			if(selectedCPaiement.getCodeCPaiement()!=null && !selectedCPaiement.getCodeCPaiement().equals("")) {
				taCPaiementDoc = taCPaiementService.findByCode(selectedCPaiement.getCodeCPaiement());
			}			

			if(selectedDocumentDTO.getCodeTTvaDoc()!=null && !selectedDocumentDTO.getCodeTTvaDoc().equals("")) {
				taTTvaDoc = taTTvaDocService.findByCode(selectedDocumentDTO.getCodeTTvaDoc());
			}
			
//			if(masterEntity!=null && ((TaAbonnement)masterEntity).getTaStripeSubscription()!=null) {
//				if(((TaAbonnement)masterEntity).getTaStripeSubscription().getTaStripeSource()!=null) {
//					if(((TaAbonnement)masterEntity).getTaStripeSubscription().getTaStripeSource().getIdExterne()!=null) {
//						selectedTaStripeSourceSubscriptionDTO = taStripeSourceService.findByCodeDTO(((TaAbonnement)masterEntity).getTaStripeSubscription().getTaStripeSource().getIdExterne());
//					} else {
//						selectedTaStripeSourceSubscriptionDTO = taStripeSourceService.findByIdDTO(((TaAbonnement)masterEntity).getTaStripeSubscription().getTaStripeSource().getIdStripeSource());
//					}
//				}
//				
//			}
			
			if(masterEntity!=null ) {
				if(((TaAbonnement)masterEntity).getTaStripeSource()!=null) {
					if(((TaAbonnement)masterEntity).getTaStripeSource().getIdExterne()!=null) {
						selectedTaStripeSourceSubscriptionDTO = taStripeSourceService.findByCodeDTO(((TaAbonnement)masterEntity).getTaStripeSource().getIdExterne());
					} else {
						selectedTaStripeSourceSubscriptionDTO = taStripeSourceService.findByIdDTO(((TaAbonnement)masterEntity).getTaStripeSource().getIdStripeSource());
					}
				}
				
			}
			/////////////////////////////////////////////
			//selectedTaStripeSourceSubscriptionDTO.set
//			((TaAbonnementDTO)selectedDocumentDTO).setDateDebut(dateDebut);
//			((TaAbonnementDTO)selectedDocumentDTO).setDateDebut(dateDebut);
			////////////////////////////////////////////
			
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
//	public void genereProchainesEcheancesRegul(ActionEvent event) {
//		Integer nbEch = taAbonnementService.genereProchainesEcheancesRegul();
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbEch+" nouvelles échéances viennent d'être générées."));
//		
//	}
	
	//POUR EXISTANT//
	public void donneEtatEnCours(ActionEvent event) {
		taLEcheanceService.donneEtatEnCours();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " échéances en cours."));
	}
	public void annuleLigneAbo(ActionEvent event) {
		taAbonnementService.annuleLigneAbo();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " Annulation des lignes d'abonnement éffectués."));
	}
	
	public void suppressionEcheanceAnnule(ActionEvent event) {
		taLEcheanceService.suppressionEcheanceAnnule();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " Suppression des échéances lié a des lignes d'abo annulés éffectué."));
	}
	
	public void suppressionEcheanceDans60Jours(ActionEvent event){
		taLEcheanceService.suppressionEcheanceDans60Jours();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " échéances avec dates d'échéance dans 60 jours supprimées."));
	}
	public void supprimeEcheance(ActionEvent event) {
		 taLEcheanceService.supprimeEcheanceDelaiSurvieDepasse();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " échéances suspendues supprimées."));
		
	}
	
	public void supprimeEcheanceSansEtat(ActionEvent event) {
		Integer nbEch = taLEcheanceService.supprimeEcheanceSansEtat();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbEch+" échéances sans états viennent d'être supprimés."));
		
	}
	public void suspendEcheance(ActionEvent event) {
		 taLEcheanceService.suspendEcheances();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " échéances suspendues."));
		
	}
	
	
	public void supprimeAvisVieuxXMois(ActionEvent event) {
		Integer nbSuppr = taAvisEcheanceService.supprimeAvisVieuxXmois(nbMoisSuppressionAvis);
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbSuppr+" avis d'échéances supprimés (vieux de "+nbMoisSuppressionAvis+" mois)"));
		
	}
	
	
	public void transformeEcheance(ActionEvent event) {
		 taLEcheanceService.transformeEcheance();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " échéances transformées."));
		
	}
	public void insertionPeriode(ActionEvent event) {
		taAbonnementService.insertionPeriode();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " insertion des dates périodes."));
	}
	
	public void insertionPeriodeV2(ActionEvent event) {
		taAbonnementService.insertionPeriodeV2();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " insertion des dates périodes V2."));
	}
	
	
	
	public void insertionLigneALigneEcheanceAvis(ActionEvent event) {
		taAbonnementService.insertionLigneALigneEcheanceAvis();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " insertion des ligne a ligne Echeance -> Avis."));
	}
	
	public void insertionLigneALigneEcheanceFacture(ActionEvent event) {
		taAbonnementService.insertionLigneALigneEcheanceFacture();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " insertion des ligne a ligne Echeance -> Facture."));
	}
	
	//inutlisé pour l'instant
	public void insertionLigneALigneClassiqueAboAvis(ActionEvent event) {
		taAbonnementService.insertionLigneALigneClassiqueAboAvis();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " insertion des ligne a ligne Classique Abo -> Avis."));
	}
	
	public void addTaJourRelance(ActionEvent event) {
		taAbonnementService.addTaJourRelance();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " Ajout d'un relance mail à 20 jours avant date échéance."));
		
	}
	
	
	//FIN POUR EXISTANT//
	
	public void generePremiereEcheancesRegul(ActionEvent event) {
		Integer nbEch = taAbonnementService.generePremiereEcheancesRegul();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbEch+" premières échéances viennent d'être générées."));
		
	}
	
	public void supprimeEcheanceEnCours(ActionEvent event) {
		Integer nbEch = taAbonnementService.supprimeEcheanceEnCours();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbEch+" échéances en cours viennent d'être supprimés."));
		
	}
	

	
	
	
	
	public void supprimeToutAvisEcheance(ActionEvent event) {
		Integer nbAvis = taAbonnementService.supprimeTousAvisEcheance();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbAvis+" avis d'échéances viennent d'être supprimés."));
		
	}
	
	
	public void metADispositionTousAvisEcheance(ActionEvent event) {
		 taAbonnementService.metADispositionTousAvisEcheance();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " les avis viennet tous d'etre mis a dispo (mail) en date d'aujourdhui."));
		
	}
	
	public void supprimeTousRDocumentEvenementAvisEcheance(ActionEvent event) {
		 taAbonnementService.supprimeTousRDocumentEvenementAvisEcheance();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " les ta_r_document_evenement des avis viennent d'être supprimés."));
		
	}
	
	
//	public void supprimeAvisEcheanceFaux(ActionEvent event) {
//		Integer nbAvis = taAbonnementService.supprimeAvisEcheanceFaux();
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nbAvis+" avis d'échéances faux viennent d'être supprimés."));
//		
//	}
	
	public void genereAvisEcheance(ActionEvent event) {
		multitenantProxyTimerAbonnement.doWork(tenantInfo.getTenantId());
	}

	//	public void onRowReorder(ReorderEvent event) {
	//		int i=1;
	//		
	//		for (ILigneDocumentJSF l : valuesLigne) {
	//			l.getDto().setNumLigneLDocument(i);
	//			if(l.getDto().getIdLDocument()!=null &&  l.getDto().getIdLDocument()!=0) {
	//				masterEntityLigne=rechercheLigne(l.getDto().getIdLDocument());
	//			}else if(l.getDto().getNumLigneLDocument()!=null/* &&  selectedTaLBonReceptionDTOJSF.getDto().getNumLigneLDocument()!=0*/) {
	//				masterEntityLigne = rechercheLigneNumLigne(l.getDto().getNumLigneLDocument());
	//			}
	//			//change le num ligne et remplace l'odre des lignes dans la liste des lignes du document
	//			masterEntityLigne.setNumLigneLDocument(i);
	//			masterEntity.getLignes().remove(masterEntityLigne);
	//			masterEntity.getLignes().add(masterEntityLigne.getNumLigneLDocument()-1, masterEntityLigne);
	//			i++;
	//		}
	//		actModifier();
	//		if(valuesLigne!=null && !valuesLigne.isEmpty())
	//			selectionLigne(valuesLigne.get(0));
	//	}
	
	/*
	 * Cette méthode sert à re-générer une échéance suspendue pour chaque ligne de l'abonnement,
	 * et à changer l'état des lignes abo à suspendues
	 * @author yann
	 */
	public void genereEcheance(ActionEvent actionEvent) {
		Integer nbEch = 0;
		for (TaLAbonnementDTOJSF ligne : valuesLigne) {
			if(ligne.getDto() != null) {
				if(ligne.getDto().getIdLDocument() != null) {
					TaLAbonnement ligneAbo;
					ligneAbo = taLAbonnementService.findByIDFetch(ligne.getDto().getIdLDocument());
					if(ligneAbo != null) {
						if(ligneAbo.getTaREtatLigneDocument() != null) {
							if(!ligneAbo.getTaREtatLigneDocument().getTaEtat().getCodeEtat().equals("doc_encours")) {
								List<TaLEcheance> listeEchEnCours = taLEcheanceService.findAllEnCoursOuSuspenduByIdLAbonnement(ligneAbo.getIdLDocument());
								if(listeEchEnCours == null || listeEchEnCours.isEmpty()) {
									ligneAbo = taLAbonnementService.donneEtatSuspendu(ligneAbo);
									Date date = new Date();
									Date debutPerActive = null;
									Date finPerActive = null;
									TaStripePlan itemPlan = taStripePlanService.findByLigneAbo(ligneAbo);
									Date dateDebutPeriodeEch = null;
									Date dateFinPeriodeEch = null;
									Date dateEcheance= null;
									
									Boolean SansPeriodeActive = false;
									
									if(masterEntity.getDateDebutPeriodeActive() == null && masterEntity.getDateFinPeriodeActive() == null) {
										SansPeriodeActive = true;
									}
									if(SansPeriodeActive) {
										dateDebutPeriode = selectedDocumentDTO.getDateDebut();
										calculeDatesAbo();
										debutPerActive = dateDebutPeriode;
										finPerActive = dateFinPeriode;
										
										
									}else {
										debutPerActive = masterEntity.getDateDebutPeriodeActive();
										finPerActive = masterEntity.getDateFinPeriodeActive();
										
									}
									
									
					                
									
									//génération d'une échéance 
									LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
									LocalDateTime localDateTimeDebutPeriode = new Date(debutPerActive.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
									LocalDateTime localDateTimeFinPeriode = new Date(finPerActive.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

									
									if(!SansPeriodeActive) {
										localDateTimeDebutPeriode = localDateTimeFinPeriode.plusDays(1);
										localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(masterEntity.getNbMoisFrequenceFacturation());
									}
												

									
									dateFinPeriodeEch = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
									dateDebutPeriodeEch = Date.from(localDateTimeDebutPeriode.atZone(ZoneId.systemDefault()).toInstant());
									//à payer au plus tard la veille du début de la période suivante
									dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
											
									
									TaLEcheance ech = new TaLEcheance();

									ech.setTaAbonnement(masterEntity);
									ech.setTaLAbonnement(ligneAbo);
									ech.setTaArticle(itemPlan.getTaArticle());
									//ech.setQteLDocument(new BigDecimal(item.getQuantity()));
									ech.setQteLDocument(ligneAbo.getQteLDocument());
									ech.setPrixULDocument(itemPlan.getAmount());
									//ech.setRemTxLDocument(item.getTaLAbonnement().getRemTxLDocument());
									ech.setRemTxLDocument(ligneAbo.getRemTxLDocument());
									ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
									ech.setTauxTvaLDocument(ligneAbo.getTauxTvaLDocument());
									//ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
									ech.setDebAbonnement(ligneAbo.getDateDebutAbonnement());
									ech.setFinAbonnement(masterEntity.getDateFin());
									ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
									ech.setNbDecimalesPrix(2);
									ech.setNbDecimalesQte(2);
									ech.setDebutPeriode(dateDebutPeriodeEch); //le jour suivant la fin de période précédente
									ech.setFinPeriode(dateFinPeriodeEch);
									ech.setDateEcheance(dateEcheance); 
									ech.setCoefMultiplicateur(ligneAbo.getCoefMultiplicateur());
									ech.calculMontant();
									
									TaEtat etat;
									try {
										etat = taEtatService.findByCode("doc_suspendu");
										ech = taLEcheanceService.merge(ech);
										ech = taLEcheanceService.donneEtat(ech,etat);
										nbEch++;
										
									} catch (FinderException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								
								
								
							}
							
							
						}
						
					}
					
				}
			}
		}
		updateTab();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", " "+nbEch+" échéance(s) viennent d'être généré(s)."));
		
	}
	
	private TaAbonnementDTO testCreate() {
		TaAbonnementDTO dto = null;
		selectedDocumentDTO.getLignesDTO().clear();
		//Création et configuration du client
		String dossierTenant = "demo";
		String loginDeLaTableEspaceClient = "philippe.blanc@legrain.fr";
		String passwordDeLaTableEspaceClient = "aeB0maet";
		Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
		config.setVerificationSsl(false);
		config.setDevLegrain(true);
		BdgRestClient bdg = BdgRestClient.build(config);
		
		//Connexion du client pour une connexion par token JWT
		String accessToken = bdg.connexion();
		selectedDocumentDTO.setIdTiers( masterEntity.getTaTiers().getIdTiers());
	    LgrDozerMapper<TaLAbonnement,TaLAbonnementDTO> mapperLigneModelToUI  = new LgrDozerMapper<TaLAbonnement,TaLAbonnementDTO>();
	    
	    

	    //valuesLigne;
		for (TaLAbonnement ligne : masterEntity.getLignes()) {
			TaLAbonnementDTO ldto = new TaLAbonnementDTO();
			
			mapperLigneModelToUI.map(ligne, ldto);
			ldto.setIdStripePlan(ligne.getTaPlan().getIdStripePlan());
			ldto.setQteTitreTransport(ligne.getQteTitreTransport());
			selectedDocumentDTO.getLignesDTO().add(ldto);
		}
		mapperModelToUI.map(masterEntity, selectedDocumentDTO);
		
		selectedDocumentDTO.setIdTPaiement(masterEntity.getTaTPaiement().getIdTPaiement());
		try {
			 dto = bdg.abonnements().creerAbonnement(selectedDocumentDTO );
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_WARN, e.getMessage(),""));
			e.printStackTrace();
		}
		return dto;
		
	}
      
	public void actEnregistrer(ActionEvent actionEvent) {		
		try {
			masterEntity.calculeTvaEtTotaux();
			autoCompleteMapUIToDTO();
			validateDocumentAvantEnregistrement(selectedDocumentDTO);
			mapperUIToModel.map(selectedDocumentDTO, masterEntity);
			//rajout yann
			if(selectedDocumentDTO.getDateFin() != null) {
				//masterEntity.getTaStripeSubscription().setDateFin(selectedDocumentDTO.getDateFin());
				masterEntity.setDateFin(selectedDocumentDTO.getDateFin());
			}

			initInfosDocument();			
			mapperUIToModelDocumentVersInfosDoc.map(masterEntity, taInfosDocument);
			taInfosDocument.setNomTiers(selectedDocumentDTO.getNomTiers());
			taInfosDocument.setCodeTTvaDoc(selectedDocumentDTO.getCodeTTvaDoc());

			mapperUIToModelAdresseFactVersInfosDoc.map((AdresseInfosFacturationDTO) selectedAdresseFact, taInfosDocument);
			mapperUIToModelAdresseLivVersInfosDoc.map((AdresseInfosLivraisonDTO) selectedAdresseLiv, taInfosDocument);						
			mapperUIToModelCPaiementVersInfosDoc.map((TaCPaiementDTO) selectedCPaiement, taInfosDocument);		

			TaTLigne typeLigneCommentaire =  taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);
			masterEntity.setLegrain(false);
			List<TaLAbonnement> listeLigneVide = new ArrayList<TaLAbonnement>();
			//List<TaStripeSubscriptionItem> listeLigneVideItem = new ArrayList<TaStripeSubscriptionItem>();
			for (TaLAbonnement ligne : masterEntity.getLignes()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide() && ligne.getNumLigneLDocument().compareTo(masterEntity.getLignes().size())==0) {
					listeLigneVide.add(ligne);
//					listeLigneVideItem.add(ligne.getTaStripeSubscriptionItem());
				} else {
					if(ligne.getTaArticle()==null) {
						ligne.setTaTLigne(typeLigneCommentaire);
					}
				}
			}
			//on parcours toutes les lignes de l'abonnement et on vire les lignes vide (sans plan)
			//NEW
//			for (TaLAbonnement ligneAbo : masterEntity.getLignes()) {
//				if(ligneAbo.getTaStripeSubscriptionItem().ligneEstVide()) {
//					listeLigneVideItem.add(ligneAbo.getTaStripeSubscriptionItem()); 
//				}
//			}
			//OLD
//			for (TaStripeSubscriptionItem item : masterEntity.getTaStripeSubscription().getItems()) {
//				if(item.ligneEstVide()) {
//					listeLigneVideItem.add(item); 
//				}
//			}

			//supression des lignes vides
			for (TaLAbonnement ligne : listeLigneVide) {
				masterEntity.getLignes().remove(ligne);
			}
			//OLD
//			for (TaStripeSubscriptionItem it : listeLigneVideItem) {
//				masterEntity.getTaStripeSubscription().getItems().remove(it);
//			}
			
//			for (TaStripeSubscriptionItem it : listeLigneVideItem) {
//			masterEntity.getli.getItems().remove(it);
//			}

			//isa le 08/11/2016
			//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
			masterEntity.reinitialiseNumLignes(0, 1);
			
			
	//	*	
		masterEntity = majTaStripeSubscription(masterEntity);
			
//		if(selectedTaFrequenceDTO != null) {
//			TaFrequence taFrequence = taFrequenceService.findById(selectedTaFrequenceDTO.getId());
//			masterEntity.setTaFrequence(taFrequence);
//		}

		    //testCreate();
			masterEntity = taAbonnementService.mergeAndFindById(masterEntity,ITaAbonnementServiceRemote.validationContext);
			taAbonnementService.annuleCode(selectedDocumentDTO.getCodeDocument());
		  verifAbonnement(masterEntity);
			
	//	*	
		
		
		
		
		


			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			autoCompleteMapDTOtoUI();
			selectedDocumentDTOs=new TaAbonnementDTO[]{selectedDocumentDTO};


			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedDocumentDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			updateTab();
			
			//test 
			//refresh();
			
//			if(impressionDirect) {
//				//préparation des valeurs en session pour l'edition
//				actImprimer(null);
//				/*
//				 * l'ouverture de la fenêtre est déclenché par le oncomplete sur le bouton enregistrer,
//				 * la checkbox du dernier step (Totaux) est liée à la même valeur qu'une autre checkbox invisible dans le step 1
//				 * le oncomplete est déclenché après les update ajax, or l'action suivante est "wizardDocument.setStep(this.stepEntete);"
//				 * donc le oncomplete s'execute alors que nous somme déjà revenu sur le step 1
//				 * Il faut donc reprendre cette valeur si on souhaite y accéder à partir de PF('moncomposantcheckbox') dans le javascript du oncomplete
//				 */
//			}



			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);



		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Abonnement", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", e.getMessage()));
		}


		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Abonnement", "actEnregistrer")); 
		}
	}
	
	public TaAbonnement majTaStripeSubscription(TaAbonnement abo) {
		/*******************************************************************************************/
		/*******************************************************************************************/
		try {
//			abo.getTaStripeSubscription().setTaStripeCustomer(taStripeCustomerService.rechercherCustomer(abo.getTaTiers()));
//			abo.getTaStripeSubscription().setCodeDocument(abo.getCodeDocument());
			abo.setTaStripeCustomer(taStripeCustomerService.rechercherCustomer(abo.getTaTiers()));
			abo.setCodeDocument(abo.getCodeDocument());

			
			String sourceStripeID = selectedTaStripeSourceSubscriptionDTO.getIdExterne();
			Integer timpeStampBillingCycleAnchor = null;
			String billing = null;
			Integer daysUntilDue = null;
			TaTPaiement tpaiement = null;
			if(selectedTaStripeSourceSubscriptionDTO.getAutomatique() == null || !selectedTaStripeSourceSubscriptionDTO.getAutomatique()) {
				//if(sourceStripeID.equals("Chèque") || sourceStripeID.equals("Virement")) {
					//Mode manuel, pas de prélèvement automatique
					billing = "send_invoice";
					daysUntilDue = 30;
//					abo.getTaStripeSubscription().setBilling(billing);
//					abo.getTaStripeSubscription().setDaysUntilDue(daysUntilDue);
					abo.setBilling(billing);
					abo.setDaysUntilDue(daysUntilDue);
					sourceStripeID = null;
					
					//abo.getTaStripeSubscription().setTaStripeSource(taStripeSourceService.rechercherSourceManuelle(selectedTaStripeSourceSubscriptionDTO.getCodeStripeTSource()));
					abo.setTaStripeSource(taStripeSourceService.rechercherSourceManuelle(selectedTaStripeSourceSubscriptionDTO.getCodeStripeTSource()));
//						** Comment stocker le type de moyen de paiement "manuel" ? différent des moyen de paiement automatique type source stripe
//						** Créer des sources "virtuelles" ? ou créer une table avec des moyens de paiement "fixe" ? utiliser TPaiement 
//								** ? Ajouter des booleens dans les StripeSubscription ? ou un ID vers une de ces nouvelles tables ?
					//rajout yann
					if(selectedTaStripeSourceSubscriptionDTO.getIdExterne() != null) {
						if(selectedTaStripeSourceSubscriptionDTO.getIdExterne().equals("Virement")) {
							tpaiement = taTPaiementService.findByCode("VIR");
						}else if(selectedTaStripeSourceSubscriptionDTO.getIdExterne().equals("Chèque")) {
							tpaiement = taTPaiementService.findByCode("C");
						}else if(selectedTaStripeSourceSubscriptionDTO.getIdExterne().equals("CB")) {
							tpaiement = taTPaiementService.findByCode("CB");
						}
					}
					
					
				} else {
					//prélevement SEPA ou debit sur une carte de credit enregistrée
					TaStripeSource stripeSource = taStripeSourceService.rechercherSource(sourceStripeID);
					//abo.getTaStripeSubscription().setTaStripeSource(stripeSource);
					abo.setTaStripeSource(stripeSource);
					//rajout yann
					if(stripeSource!= null) {
						switch (stripeSource.getTaStripeTSource().getCodeStripeTSource()) {
						case "CB":
							tpaiement = taTPaiementService.findByCode("CB");
							break;

						case "PREL_SEPA":
							tpaiement = taTPaiementService.findByCode("PREL");
							break;
						}
						
					}
				}
			//rajout yann
			abo.setTaTPaiement(tpaiement);
			
			
	//		if(creerAbonnementSurStripe) {
	//			/*
	//			 * Création de l'abonnement chez Stripe avec l'api billing; dans ce cas:
	//			 * - Stripe génère directement des Invoices
	//			 * - peut envoyer des emails au customer suivant les paramètres du compte stripe (ou uniquement déclencher les webhooks)
	//			 * - déclencher des paiements automatiquement sur la source de paiement associé à l'abonnement/customer
	//			 * => BDG devra se synchroniser pour récupérer les invoices pour générer les avis de prélèvement
	//			 * => BDG devra se synchroniser pour récupérer les paiements pour générer les règlements correspondant et les factures
	//			 */
	//			String idSubscriptionStripe = paiementEnLigneDossierService.creerSubscriptionStripe(
	//					compte,
	//					taStripeCustomer.getIdExterne(),
	//					items, 
	//					sourceStripeID, 
	//					timpeStampBillingCycleAnchor, 
	//					billing,
	//					daysUntilDue);
	//			
	//			taStripeSubscription.setIdExterne(idSubscriptionStripe);
	//		} 
			/*
			 * ELSE
			 * Création de l'abonnement uniquement dans BDG (utilise les mêmes table que pour les abonnement Stripe)
			 * => pas de création d'abonnement dans Stripe, pas de génération d'invoice par Stripe, pas de déclenchement de paiement automatique par Stripe
			 * => BDG gère les échéances, génére les avis d'échéance, déclenche lui-même des paiements Stripe au bon momment
			 */
			
			return abo;
		} catch(Exception e) {
			e.printStackTrace();
		}
		/*******************************************************************************************/
		/*******************************************************************************************/
		return abo;
	}
	
	public void verifAbonnement(TaAbonnement abo) {
		//		
//		taStripeSubscription = taStripeSubscriptionService.merge(taStripeSubscription);
		
//		mapperModelToUISubscription.map(taStripeSubscription,newTaStripeSubscriptionDTO);
		
//		if(taStripeSubscription.getTimerHandle()!=null) {
//			//un timer existe deja pour cette notification, on l'annule et on le recréer
//			TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taStripeSubscription.getTimerHandle());
//			try {
//				Timer t = h.getTimer();
//				t.cancel();
//			} catch (NoSuchObjectLocalException ex) {
//				System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
//			}
//		}
//		taStripeSubscription.setTimerHandle(SerializationUtils.serialize((Serializable) timerCreerAvisEcheanceEtPaiementPrevuService.creerTimer(taStripeSubscription)));
		
//		taStripeSubscription = taStripeSubscriptionService.merge(taStripeSubscription); //pour avoir l'id
		
		if(modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION)) {
			//créer la/les premieres echeances dans le cas ou l'on ne laisse pas stripe gérer les invoices
			//List<TaLEcheance> listePremieresEcheances = taStripeSubscriptionService.generePremieresEcheances(abo.getTaStripeSubscription());
			
			TaEtat etat;
			try {
				etat = taEtatService.findByCode("doc_encours");
				List<TaLEcheance> listePremieresEcheances = taAbonnementService.generePremieresEcheances(abo);
				for (TaLEcheance taLEcheance : listePremieresEcheances) {
					taLEcheance = taLEcheanceService.merge(taLEcheance);
					taLEcheance = taLEcheanceService.donneEtat(taLEcheance, etat);
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//TODO création d'un avis d'échéance (avec échéance immédiate) pour le premier paiement 
			// et surtout déclenchement d'un "paiement prévu" immédiat (ou peut être une heure après comme stripe pour permetre d'annuler/modifier si besion)
			
			
		//rajout yann si on vient de modifier l'abonnement	
		}else if(modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION)) {
			//List<TaLAbonnement> nouvellesLignes = new ArrayList<TaLAbonnement>();
			
			//si on est en modification, on parcours toutes les lignes de l'abonnement
			for (TaLAbonnement ligne : abo.getLignes()) {
				
				//List<TaLEcheance> echeances = taLEcheanceService.rechercheEcheanceEnCoursOuSuspendusByIdLAbonnement( ligne.getIdLDocument());
				List<TaLEcheance> echeances = taLEcheanceService.rechercheEcheanceByIdLAbonnement(ligne.getIdLDocument());
				if(echeances == null || echeances.isEmpty() ) {
					taAbonnementService.generePremiereEcheance(ligne);
				}
				
				
			}
			
		}
		
//		if(creerAbonnementSurStripe) {
//			listeSubscription = taStripeSubscriptionService.rechercherSubscriptionCustomerDTO(taStripeCustomer.getIdExterne());
//		} else {
//			listeSubscriptionNonStripe = taStripeSubscriptionService.rechercherSubscriptionNonStripeCustomerDTO(taStripeCustomer.getIdExterne()); //recherche en local
//		}
	}
	
	public void donneEtatEnCoursLigneAboSansEtat(ActionEvent event) {
		Integer nb = 0;
		nb = taAbonnementService.donneEtatEnCoursLigneAboSansEtat();
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Abonnement", nb+" lignes d'abonnements sans états sont maintenant en cours "));

	}


	public void actInserer(ActionEvent actionEvent) {
		try {
			//rajout Yann
			taStripeCustomer = new TaStripeCustomer();
			selectedTaStripeSourceSubscriptionDTO = new TaStripeSourceDTO();
			selectedTaFrequenceDTO = listeFrequenceDTO.get(0);
			nbFrequence = 1;
			
			listePreferences= taPreferencesService.findByGroupe("devis");
			selectedDocumentDTO = new TaAbonnementDTO();
			selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
			//a l'insertion, on ne met pas de dates de périodes
			dateDebutPeriode = selectedDocumentDTO.getDateDebut();
			//selectedDocumentDTO.setDateDebutPeriodeActive(selectedDocumentDTO.getDateDebut());
			selectedDocumentDTO.setTypeAbonnement(TaAbonnement.TYPE_SANS_ENGAGEMENT);
			
			calculeDatesAbo();
			
			
			masterEntity = new TaAbonnement();
			masterEntity.setLegrain(true);
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			selectedEtatDocument=null;
			
			//selectedDocumentDTO.setTypeAbonnement(TaAbonnement.TYPE_AVEC_ENGAGEMENT);
			autoCompleteMapDTOtoUI();

			initInfosDocument();
			//initTaStripeSubscription();


			//			selectedDocumentDTO.setCodeDocument(taBonReceptionService.genereCode()); //ejb
			//			validateUIField(Const.C_CODE_DOCUMENT,selectedDocumentDTO.getCodeDocument());//permet de verrouiller le code genere

			//			selectedDocumentDTO.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<>();
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
			//			paramsCode.put(Const.C_NOMFOURNISSEUR, selectedDocumentDTO.getNomTiers());

			if(selectedDocumentDTO.getCodeDocument()!=null) {
				taAbonnementService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			//			String newCode = taDevisService.genereCode(paramsCode);
			//			if(newCode!=null && !newCode.equals("")){
			//				selectedDocumentDTO.setCodeDocument(newCode);
			//			}
			selectedDocumentDTO.setCodeDocument(taAbonnementService.genereCode(paramsCode));
			changementTiers(true);

			/*****************************************************************************/
			//selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//			taStripeSubscription = new TaStripeSubscription();

			//listeSubscriptionItem = new ArrayList<>();
			listeAvisEcheanceAbonnement = new ArrayList<>();
			listeEcheanceNonEmiseAbonnement = new ArrayList<>();

//			selectedTaStripeSubscriptionDTO.setDateDebut(new Date());
//			selectedTaStripeSubscriptionDTO.setDateFin(null);

			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			//		taStripeSubscription.setCodeDocument(taStripeSubscriptionService.genereCode(null));
			/*****************************************************************************/

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);


			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);


			scrollToTop();

			btnPrecedentVisible = false;
			btnSuivantVisible = true;
			initialisePositionBoutonWizard();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Abonnement", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			//		} catch (FinderException e) {
			//			e.printStackTrace();
		}
	}
	
//	public void initTaStripeSubscription() {
//		if(taStripeSubscription==null) {
//			taStripeSubscription = new TaStripeSubscription();
//		}
//		if(masterEntity!=null) {
//			//taStripeSubscription.setTaAbonnement(masterEntity);
//			masterEntity.setTaStripeSubscription(new TaStripeSubscription());
//			masterEntity.getTaStripeSubscription().setTaAbonnement(masterEntity);
//		}
//	}
	
//	public TaLAbonnement initTaStripeSubscriptionItem(TaLAbonnement taLAbonnement) {
////		if(taStripeSubscription==null) {
////			taStripeSubscription = new TaStripeSubscription();
////		}
//		TaStripeSubscriptionItem item = taLAbonnement.getTaStripeSubscriptionItem();
//		if(taLAbonnement!=null) {
//			if(item==null) {
//				item = new TaStripeSubscriptionItem();
//			}
//			item.setTaLAbonnement(taLAbonnement);
//			taLAbonnement.setTaStripeSubscriptionItem(item);
//		}
//		return taLAbonnement;
//	}
	public void actSuspendreAbonnement() {
		if(!selectedDocumentDTO.getSuspension()) {
			Date now = new Date();
			selectedDocumentDTO.setSuspension(true);
			selectedDocumentDTO.setDateSuspension(now);
			actEnregistrer(null);
		}

	}
	
	public void actActiverAbonnement() {
		if(selectedDocumentDTO.getSuspension()) {
			selectedDocumentDTO.setSuspension(false);
			actEnregistrer(null);
		}
		
	}
	public void actInsererLigne(ActionEvent actionEvent) {
		super.actInsererLigne(actionEvent);
		
		for (TaLAbonnementDTOJSF ligne : valuesLigne) {
			if(ligne.getDto().getDateDebutAbonnement() == null) {
				ligne.getDto().setDateDebutAbonnement(new Date());
			}
		}
		
		//valuesLigne.get(valuesLigne.size() -1).setDto2(new TaStripeSubscriptionItemDTO());
		//masterEntityLigne = initTaStripeSubscriptionItem(masterEntityLigne);
		masterEntityLigne.setPrixULDocument(null);
		
		

	}
	
	
	

	//	public void actModifier() {
	//		actModifier(null);
	//	}

	//	public void actModifier(ActionEvent actionEvent) {
	//		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION))
	//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	//		masterEntity.setLegrain(true);
	//		if(ConstWeb.DEBUG) {
	//			FacesContext context = FacesContext.getCurrentInstance();  
	//			context.addMessage(null, new FacesMessage("Abonnement", "actModifier")); 	 
	//		}
	//	}
	//
	//	public void actAide(ActionEvent actionEvent) {
	//
	//		//		RequestContext.getCurrentInstance().openDialog("aide");
	//
	//		Map<String,Object> options = new HashMap<String, Object>();
	//		options.put("modal", true);
	//		options.put("draggable", false);
	//		options.put("resizable", false);
	//		options.put("contentHeight", 320);
	//		Map<String,List<String>> params = null;
	//		RequestContext.getCurrentInstance().openDialog("aide", options, params);
	//
	//		if(ConstWeb.DEBUG) {
	//			FacesContext context = FacesContext.getCurrentInstance();  
	//			context.addMessage(null, new FacesMessage("Abonnement", "actAide")); 	
	//		}
	//	}

	//	public void actAideRetour(ActionEvent actionEvent) {
	//		
	//	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
		b.setTitre("Abonnement");
		b.setTemplate("abonnement/abonnement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ABONNEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ABONNEMENT);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Abonnement", 
					"Nouveau"
					)); 
		}
	} 

	//	public void supprimer(ActionEvent actionEvent) {
	//		actSupprimer(actionEvent);
	//	}
	//	
	//	public void detail() {
	//		detail(null);
	//	}
	//	
	//	public void detail(ActionEvent actionEvent) {
	//		onRowSelect(null);
	//	}
	//	
	//	public void actSupprimer() {
	//		actSupprimer(null);
	//	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaAbonnement obj = new TaAbonnement();
		//TaStripeSubscription sub = new TaStripeSubscription();
		List<TaLEcheance> listeLigneEch = new ArrayList<TaLEcheance>();
		try {
			if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
				obj = taAbonnementService.findById(selectedDocumentDTO.getId());
			}

			if(obj!=null) {
				//test
				//sub = taStripeSubscriptionService.findById(obj.getTaStripeSubscription().getIdStripeSubscription());
//				listeLigneEch = taLEcheanceService.rechercheEcheanceLieAAbonnement(obj.getTaStripeSubscription());
//				for (TaLEcheance ligneEch : listeLigneEch) {
////					ligneEch = taLEcheanceService.merge(ligneEch);
//					taLEcheanceService.remove(ligneEch);
//				}
//				
//				obj.getLignes().clear();
//				for (TaLEcheance taLEcheance : obj.getTaStripeSubscription().getItems()) {
//					
//				}
//				obj.getTaStripeSubscription().getItems().clear();
//				obj.getTaStripeSubscription().setTaAbonnement(null);
////				sub = taStripeSubscriptionService.merge(obj.getTaStripeSubscription());
//				obj.setTaStripeSubscription(null);
//				
//				
//				obj = taAbonnementService.merge(obj);
				//taStripeSubscriptionService.remove(sub);
				//taStripeSubscriptionService.remove(sub);
				
				//taStripeSubscriptionService.remove(obj.getTaStripeSubscription());
				//obj.setTaStripeSubscription(null);
				//fin test
				
				taAbonnementService.remove(obj);
				values.remove(selectedDocumentDTO);
			} else {
				if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>=1) {
					for (TaAbonnementDTO d : selectedDocumentDTOs) {
						taAbonnementService.removeDTO(d);
						values.remove(d);
					}
				}
			}

			if(!values.isEmpty()) {
				selectedDocumentDTO = values.get(0);
				selectedDocumentDTOs= new TaAbonnementDTO[]{selectedDocumentDTO};
				updateTab();
			}else{
				selectedDocumentDTO = new TaAbonnementDTO();
				selectedDocumentDTOs= new TaAbonnementDTO[]{};	
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Abonnement", "actSupprimer"));
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Abonnement", e.getMessage()));
		}	    
	}
	
	public void actSupprimerLigne(ActionEvent actionEvent) {
		//suppréssion de l' item correspondant a la ligne d'abo séléctionnée
//		if(masterEntity.getTaStripeSubscription() != null) {
//			masterEntity.getTaStripeSubscription().getItems().remove(selectedLigneJSF.getTaStripeSubscriptionItem());
//		}
		//suppréssion de la ligne d'abonnement séléctionnée
		super.actSupprimerLigne(actionEvent);
		
//		if(modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION)) {
//			if(nouvellesLignes.contains(masterEntityLigne)) {
//				nouvellesLignes.remove(masterEntityLigne);
//			}
//			
//		}
	}
	
//public void actDialogAnnulerAbonnement(ActionEvent actionEvent) {
//	int un = 1;
//	    if(un == 2) {
//	    	Map<String,Object> options = new HashMap<String, Object>();
//	        options.put("modal", true);
//	        options.put("draggable", false);
//	        options.put("closable", false);
//	        options.put("resizable", true);
//	        options.put("contentHeight", 300);
//	        options.put("contentWidth", "100%");
//	        //options.put("contentHeight", "100%");
//	        options.put("width", 720);
//	        
//	        Map<String,List<String>> params = new HashMap<String,List<String>>();
//	        List<String> list = new ArrayList<String>();
//	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//	        params.put("modeEcranDefaut", list);
//	        
//	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//			Map<String, Object> sessionMap = externalContext.getSessionMap();
//			
////			SmsParam email = new SmsParam();
////			email.setNumeroExpediteur(null);
////			email.setDestinataires(new String[]{taTiers.getTaTelephone().getNumeroTelephone()});
////			email.setTelephoneDestinataires(new TaTelephone[]{taTiers.getTaTelephone()});
//
//			
//			//sessionMap.put(SmsParam.NOM_OBJET_EN_SESSION, email);
//	        
//	        PrimeFaces.current().dialog().openDynamic("/dialog_annuler_abonnement", options, params);  
//	    }
//          
//	}
	
	public void handleReturnDialogAnnulerAbonnement(SelectEvent event) {
//		if(event!=null && event.getObject()!=null) {
////			TaMessageSMS j = (TaMessageSMS) event.getObject();
//			
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Abonnement", 
//					"Abonnement annulé "
//					)); 
//		}
	}
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	public void actFermer(ActionEvent actionEvent) {
		//fermeture de l'onglet en JavaScript

		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			actAnnuler(actionEvent);
			break;
		case C_MO_EDITION:
			actAnnuler(actionEvent);							
			break;

		default:
			break;
		}

		selectedDocumentDTO=new TaAbonnementDTO();
		selectedDocumentDTOs=new TaAbonnementDTO[]{selectedDocumentDTO};

		gestionBoutonWizardDynamique = false;
		updateTab();



		//		//gestion du filtre de la liste
		//        RequestContext requestContext = RequestContext.getCurrentInstance();
		//        requestContext.execute("PF('wvDataTableListeDevis').filter()");

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Abonnement", "actFermer")); 
		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Abonnement", "actImprimer")); 
		}
		try {

			//Fiche_devis.rptdesign&=7332&typeTraite=null&PageBreakTotaux=27&ape=&ParamChoix=choix 1
			// &PageBreakMaxi=36&ParamCorr=null&CoupureLigne=54
			//		&nomEntreprise=EARL DE GRINORD&capital=&rcs=&siret=&__document=doc1469798570882&__format=pdf

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaAbonnement doc =taAbonnementService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );
			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			//mapEdition.put("LibelleJournalTva", taTTvaDoc.getLibelleEdition());

			mapEdition.put("lignes", masterEntity.getLignes());


			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("devis");
			mapParametreEdition.put("preferences", liste);
			mapEdition.put("param", mapParametreEdition);

			sessionMap.put("edition", mapEdition);


			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("AbonnementController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    

	public void actImprimerListeDesAbonnements(ActionEvent actionEvent) {

		try {

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

			Map<String, Object> sessionMap = externalContext.getSessionMap();

			sessionMap.put("listeDesAbonnements", values);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}    

	//	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
	//		
	//		etiquetteCodeBarreBean.videListe();
	//		try {
	//			for (TaLAbonnement l : masterEntity.getLignes()) {
	//				etiquetteCodeBarreBean.getListeArticle().add(l.getTaArticle());
	//			}
	//			
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		
	//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	//		
	//		if(ConstWeb.DEBUG) {
	//		   	FacesContext context = FacesContext.getCurrentInstance();  
	//		    context.addMessage(null, new FacesMessage("Abonnement", "actImprimerEtiquetteCB")); 
	//		}
	//	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	//	public void onRowSimpleSelect(SelectEvent event) {
	//
	//		try {
	//			if(pasDejaOuvert()==false){
	//				onRowSelect(event);
	//	
	////				autoCompleteMapDTOtoUI();
	////				
	////				masterEntity = taDevisService.findById(selectedDocumentDTO.getId());
	////				masterEntity.calculeTvaEtTotaux();
	////				
	////				valuesLigne = IHMmodel();
	////				
	////				if(ConstWeb.DEBUG) {
	////					FacesContext context = FacesContext.getCurrentInstance();  
	////					context.addMessage(null, new FacesMessage("Abonnement", 
	////							"Chargement du Devis N°"+selectedDocumentDTO.getCodeTiers()
	////							)); 
	////				}
	//			} else {
	//				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_DEVIS);
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	} 

	//	public void updateTab(){
	//
	//		try {	
	//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);	
	//			if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0) {
	//				selectedDocumentDTO = selectedDocumentDTOs[0];
	//			}
	//			if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0) {
	//				masterEntity = taDevisService.findById(selectedDocumentDTO.getId());
	//			}
	//			valuesLigne = IHMmodel();
	//			
	//			masterEntity.calculeTvaEtTotaux();
	//			changementTiers(false);
	//			
	//			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
	//			
	//			autoCompleteMapDTOtoUI();
	//			
	//			if(ConstWeb.DEBUG) {
	//				FacesContext context = FacesContext.getCurrentInstance();  
	//				context.addMessage(null, new FacesMessage("Abonnement", 
	//						"Chargement du Devis N°"+selectedDocumentDTO.getCodeTiers()
	//						)); 
	//			}
	//		
	//		} catch (FinderException e) {
	//			e.printStackTrace();
	//		}
	//	}


	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
		b.setTitre("Abonnement");
		b.setTemplate("abonnement/abonnement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ABONNEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ABONNEMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		if(wizardDocument!=null) {
			wizardDocument.setStep(this.stepEntete);
		}		
		scrollToTop();
		updateTab();
		
		
	} 
	//rajout yann pour date fin 
	public void updateTab() {
		super.updateTab();
		
		if(selectedDocumentDTO != null) {
			
			verifPassageCRON();
			
			//selectedTaStripeSubscriptionDTO = taStripeSubscriptionService.findDTOByIdAbonnement( selectedDocumentDTO.getId());
//			if(selectedTaStripeSubscriptionDTO.getIdExterneSource() != null) {
//				//ici il faut remplir le selectedTaStripeSourceSubscriptionDTO en fonction du IdStripeSource du selectedTaStripeSubscriptionDTO, mais il faut le remplir avant
////				selectedTaStripeSourceSubscriptionDTO = 
//			}
			if(selectedDocumentDTO.getIdFrequence() == null) {
				if(masterEntity != null && masterEntity.getTaFrequence() != null ) {
					selectedDocumentDTO.setIdFrequence(masterEntity.getTaFrequence().getIdFrequence());
				}
			}
			

//			try {
//				//selectedTaFrequenceDTO = taFrequenceService.findByIdDTO(selectedDocumentDTO.getIdFrequence());
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		}
	}

	//	public boolean editable() {
	//		return !modeEcran.dataSetEnModif();
	//	}
	//
	//	public boolean editableEnInsertionUniquement() {
	//		return !modeEcran.dataSetEnInsertion();
	//	}

	//	public void actDialogTiers(ActionEvent actionEvent) {
	//
	//		//		RequestContext.getCurrentInstance().openDialog("aide");
	//
	//		Map<String,Object> options = new HashMap<String, Object>();
	//		options.put("modal", true);
	//		options.put("draggable", false);
	//		options.put("resizable", false);
	//		options.put("contentHeight", 640);
	//		options.put("modal", true);
	//
	//		//Map<String,List<String>> params = null;
	//		Map<String,List<String>> params = new HashMap<String,List<String>>();
	//		List<String> list = new ArrayList<String>();
	//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	//		params.put("modeEcranDefaut", list);
	//
	//		RequestContext.getCurrentInstance().openDialog("tiers/dialog_tiers", options, params);
	//
	//		//		FacesContext context = FacesContext.getCurrentInstance();  
	//		//		context.addMessage(null, new FacesMessage("Abonnement", "actAbout")); 	    
	//	}
	//
	//	public void handleReturnDialogTiers(SelectEvent event) {
	//		if(event!=null && event.getObject()!=null) {
	//			taTiers = (TaTiers) event.getObject();
	//		}
	//	}
	//
	//	public void actDialogTypeCivilite(ActionEvent actionEvent) {
	//
	//		//		RequestContext.getCurrentInstance().openDialog("aide");
	//
	//		Map<String,Object> options = new HashMap<String, Object>();
	//		options.put("modal", true);
	//		options.put("draggable", false);
	//		options.put("resizable", false);
	//		options.put("contentHeight", 640);
	//		options.put("modal", true);
	//
	//		//Map<String,List<String>> params = null;
	//		Map<String,List<String>> params = new HashMap<String,List<String>>();
	//		List<String> list = new ArrayList<String>();
	//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	//		params.put("modeEcranDefaut", list);
	//
	//		RequestContext.getCurrentInstance().openDialog("tiers/dialog_type_civilite", options, params);
	//
	//		//		FacesContext context = FacesContext.getCurrentInstance();  
	//		//		context.addMessage(null, new FacesMessage("Abonnement", "actAbout")); 	    
	//	}
	//
	//	public void handleReturnDialogTypeCivilite(SelectEvent event) {
	//		//		if(event!=null && event.getObject()!=null) {
	//		//			taTCivilite = (TaTCivilite) event.getObject();
	//		//		}
	//	}
	//
	//
	//
	//	public void actDialogTypeEntite(ActionEvent actionEvent) {
	//
	//		//		RequestContext.getCurrentInstance().openDialog("aide");
	//
	//		Map<String,Object> options = new HashMap<String, Object>();
	//		options.put("modal", true);
	//		options.put("draggable", false);
	//		options.put("resizable", false);
	//		options.put("contentHeight", 640);
	//		options.put("modal", true);
	//
	//		//Map<String,List<String>> params = null;
	//		Map<String,List<String>> params = new HashMap<String,List<String>>();
	//		List<String> list = new ArrayList<String>();
	//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	//		params.put("modeEcranDefaut", list);
	//
	//		RequestContext.getCurrentInstance().openDialog("tiers/dialog_type_entite", options, params);
	//
	//		//		FacesContext context = FacesContext.getCurrentInstance();  
	//		//		context.addMessage(null, new FacesMessage("Abonnement", "actAbout")); 	    
	//	}
	//
	//	public void handleReturnDialogTypeEntite(SelectEvent event) {
	//		//		if(event!=null && event.getObject()!=null) {
	//		//			taTEntite = (TaTEntite) event.getObject();
	//		//		}
	//	}
	//
	//	public boolean etatBouton(String bouton) {
	//		boolean retour = true;
	//		switch (modeEcran.getMode()) {
	//		case C_MO_INSERTION:
	//			switch (bouton) {
	//				case "annuler":
	//				case "enregistrer":
	//				case "fermer":
	//					retour = false;
	//					break;
	//				default:
	//					break;
	//			}
	//			break;
	//		case C_MO_EDITION:
	//			switch (bouton) {
	//			case "annuler":
	//			case "enregistrer":
	//			case "fermer":
	//				retour = false;
	//				break;
	//			default:
	//				break;
	//			}
	//			break;
	//		case C_MO_CONSULTATION:
	//			switch (bouton) {
	//			case "inserer":
	//			case "fermer":
	//				retour = false;
	//				break;
	//			case "supprimerListe":retour = false;break;	
	//			case "supprimer":
	//			case "modifier":
	//			case "imprimer":
	//				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null  && selectedDocumentDTO.getId()!=0 ) retour = false;
	//				break;				
	//			default:
	//				break;
	//		}
	//			break;
	//		default:
	//			break;
	//		}
	//		
	//		return retour;
	//
	//	}
	//
	//	public boolean etatBoutonLigne(String bouton) {
	//		boolean retour = true;
	//		if(modeEcran.dataSetEnModif()) {
	//			retour = false;
	//		}
	//		switch (modeEcranLigne.getMode()) {
	//		case C_MO_INSERTION:
	//			switch (bouton) {
	//			case "annuler":
	//			case "enregistrer":
	//			case "fermer":
	//				retour = false;
	//				break;
	//			case "rowEditor":
	//				retour = modeEcran.dataSetEnModif()?true:false;
	//				break;
	//			default:
	//				break;
	//			}
	//			break;
	//		case C_MO_EDITION:
	//			switch (bouton) {
	//			case "annuler":
	//			case "enregistrer":
	//			case "fermer":
	//				retour = false;
	//				break;
	//			case "rowEditor":
	//				retour = modeEcran.dataSetEnModif()?true:false;
	//				break;
	//			default:
	//				break;
	//			}
	//			break;
	//		case C_MO_CONSULTATION:
	//			switch (bouton) {
	//			case "supprimer":
	//			case "modifier":
	//			case "inserer":
	//			case "imprimer":
	//			case "fermer":
	//				retour = false;
	//				break;
	//			case "rowEditor":
	//				retour = modeEcran.dataSetEnModif()?true:false;
	//				break;
	//			default:
	//				break;
	//			}
	//			break;
	//		default:
	//			break;
	//		}
	//
	//		return retour;
	//
	//	}

	public void validateLignesDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String msg = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			validateUIField(nomChamp,value);
			TaLAbonnementDTO dtoTemp =new TaLAbonnementDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLAbonnementService.validateDTOProperty(dtoTemp, nomChamp, ITaLAbonnementServiceRemote.validationContext );

			//selectedDocumentDTO.setAdresse1Adresse("abcd");

			//		  if(selectedRadio.equals("aa")) {
			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//		  }

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		//		String msg = "";
		//
		//		try {
		//			//String selectedRadio = (String) value;
		//
		//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
		//
		//			//msg = "Mon message d'erreur pour : "+nomChamp;
		//
		//			validateUIField(nomChamp,value);
		//			TaAbonnementDTO dtoTemp = new TaAbonnementDTO();
		//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
		//			taBonReceptionService.validateDTOProperty(dtoTemp, nomChamp, ITaAbonnementServiceRemote.validationContext );
		//
		//			//selectedDocumentDTO.setAdresse1Adresse("abcd");
		//
		//			//		  if(selectedRadio.equals("aa")) {
		//			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		//			//		  }
		//
		//		} catch(Exception e) {
		//			msg += e.getMessage();
		//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		//		}

		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)||nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)){
				if(value==null || value.equals(""))value=BigDecimal.ZERO;
			}
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaAbonnementDTO>> violations = factory.getValidator().validateValue(TaAbonnementDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaAbonnementDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	public void validateDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			//taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaAbonnementServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	public void validateLigneDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			//			taLBonReceptionService.validateDTOProperty(selectedLigneJSF.getDto(),Const.C_CODE_ARTICLE,  ITaLAbonnementServiceRemote.validationContext );
			//			taLBonReceptionService.validateDTOProperty(selectedLigneJSF.getDto(),Const.C_NUM_LOT,  ITaLAbonnementServiceRemote.validationContext );
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	//	public void autcompleteSelection(SelectEvent event) {
	//		Object value = event.getObject();
	//		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
	//
	//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
	//		System.out.println("DevisController.autcompleteSelection() : "+nomChamp);
	//
	//		if(value!=null) {
	//		if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
	//		if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
	//		}
	//		validateUIField(nomChamp,value);
	//	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
						//						entity=(TaTiers) value;
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}

					changement=!entity.equalsCode(masterEntity.getTaTiers());


				}
				masterEntity.setTaTiers(entity);
				if(changement){
					String nomTiers=masterEntity.getTaTiers().getNomTiers();
					((TaAbonnementDTO)selectedDocumentDTO).setLibelleDocument("Abonnement N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);										
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaAbonnementDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
					}									
					changementTiers(true);
					taStripeCustomer = taStripeCustomerService.rechercherCustomer(entity);
				}
			}else if(nomChamp.equals(Const.C_NUM_LOT)) {

			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
				dateDansPeriode((Date)value,nomChamp);

			}else if(nomChamp.equals(Const.C_DATE_ECH_DOCUMENT)) {				
				dateDansPeriode((Date)value,nomChamp);
			}else if(nomChamp.equals("MOYEN_PAIEMENT")) {		
			//	selectedTaStripeSourceSubscriptionDTO
				
			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = null;
				TaPrix prix =null;
				boolean changementArticleLigne = false;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					} else if(value instanceof TaStripeProductDTO){
						entity = taArticleService.findByCode(((TaStripeProductDTO) value).getCodeArticle());
//						selectedLigneJSF.setTaStripeProductDTO((TaStripeProductDTO) value);
//						selectedLigneJSF.setTaStripeProduct(taStripeProductService.findByCode(((TaStripeProductDTO) value).getIdExterne()));
					}else{
						entity = taArticleService.findByCode((String)value);
					}
				}
				if(entity!=null) {
					prix = entity.chercheTarif(masterEntity.getTaTiers());
					if(prix==null)prix=new TaPrix(BigDecimal.ZERO,BigDecimal.ZERO);
				}
				if(selectedLigneJSF.getTaArticle()== null || selectedLigneJSF.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
					changementArticleLigne = true;
				}
				selectedLigneJSF.setTaArticle(entity);
				masterEntityLigne.setTaArticle(entity);
				if(entity!=null) {
					if(changementArticleLigne) {
						selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
						//					selectedLigneJSF.getDto().setDluo(LibDate.incrementDate(selectedLigneJSF.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0)+1  , 0, 0));
						//					selectedLigneJSF.getDto().setLibelleLot(entity.getLibellecArticle());
					}
				}
				
				selectedLigneJSF.setTaUnite1(null);
				selectedLigneJSF.setTaUnite2(null);
				selectedLigneJSF.setTaStripePlan(null);
				selectedLigneJSF.setTaStripePlanDTO(null);
				
				TaRapportUnite rapport= null;
				if(entity!=null) {
					rapport = entity.getTaRapportUnite();
				}
				TaCoefficientUnite coef = null;
				if(rapport!=null){
					if(rapport.getTaUnite2()!=null) {
						coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
						selectedLigneJSF.setTaCoefficientUnite(coef);
					}
				}
				if(coef!=null){
					selectedLigneJSF.setTaCoefficientUnite(coef);
					if(coef.getUniteA()!=null) {
						selectedLigneJSF.getDto().setU1LDocument(coef.getUniteA().getCodeUnite());
						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
					if(coef.getUniteB()!=null){
						selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}
				}else
					if(entity!=null){
						if(entity.getTaUnite1()!=null) {
							selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
							selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
						}
						for (TaRapportUnite obj : entity.getTaRapportUnites()) {
							if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
								if(obj!=null){
									if(obj.getTaUnite2()!=null) {										
										coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
									}
								}
								selectedLigneJSF.setTaCoefficientUnite(coef);
								if(coef!=null) {
									selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
									selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
								}
							}							
						}
					}
				if(prix!=null){
					selectedLigneJSF.getDto().setQteLDocument(new BigDecimal(1));
					if(masterEntity.getTtc()==1) {
						selectedLigneJSF.getDto().setPrixULDocument(prix.getPrixttcPrix());
					} else {
						selectedLigneJSF.getDto().setPrixULDocument(prix.getPrixPrix());
					}
					//modif yann
//					selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
//					selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				}
				if(entity != null) {
					if(entity.getTaTva()!=null && masterEntity.isGestionTVA()){
						selectedLigneJSF.getDto().setCodeTvaLDocument(entity.getTaTva().getCodeTva());
						selectedLigneJSF.getDto().setTauxTvaLDocument(entity.getTaTva().getTauxTva());
					}
				}
				
				masterEntityLigne.setCoefMultiplicateur(BigDecimal.ONE);
				selectedLigneJSF.getDto().setCoefMultiplicateur(BigDecimal.ONE);
				
				

			}else if(nomChamp.equals(Const.C_NICKNAME_PLAN)) {
				TaStripePlan entity = null;
				boolean changementPlanLigne = false;
				if(value!=null){
					if(value instanceof TaStripePlanDTO){
						entity = taStripePlanService.findById(((TaStripePlanDTO)value).getId());
					}
				}
				if(selectedLigneJSF.getTaStripePlan()== null || selectedLigneJSF.getTaStripePlan().getIdStripePlan()!=entity.getIdStripePlan()) {
					changementPlanLigne = true;
				}
				selectedLigneJSF.setTaStripePlan(entity);
				//masterEntityLigne.setTaStripePlan(entity);
				if(changementPlanLigne) {
					selectedLigneJSF.getDto().setLibLDocument(entity.getNickname());
					selectedLigneJSF.getDto().setPrixULDocument(entity.getAmount());
					selectedLigneJSF.getTaStripePlanDTO().setInterval(entity.getInterval());
				}
//				selectedLigneJSF.setTaUnite1(null);
//				selectedLigneJSF.setTaUnite2(null);
				if(selectedLigneJSF.getTaStripePlanDTO() != null) {
					selectedLigneJSF.getDto().setMtHtLDocument(selectedLigneJSF.getTaStripePlanDTO().getAmount().multiply(masterEntityLigne.getQteLDocument()));
					//selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
					masterEntityLigne.setPrixULDocument(selectedLigneJSF.getTaStripePlanDTO().getAmount());
					masterEntityLigne.calculMontant();
				}
				
				
			}else if(nomChamp.equals(Const.C_PRIX_U_L_DOCUMENT)) {
				BigDecimal prix=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						prix=(BigDecimal)value;
					}}
				masterEntityLigne.setPrixULDocument(prix);
				masterEntityLigne.calculMontant();
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());

			} else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
					if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
						if(selectedLigneJSF.getTaCoefficientUnite().getOperateurDivise()) 
							selectedLigneJSF.getDto().setQte2LDocument((qte).divide(selectedLigneJSF.getTaCoefficientUnite().getCoeff()
									,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
						else selectedLigneJSF.getDto().setQte2LDocument((qte).multiply(selectedLigneJSF.getTaCoefficientUnite().getCoeff()));
					}else  {
						selectedLigneJSF.getDto().setQte2LDocument(BigDecimal.ZERO);
						masterEntityLigne.setQte2LDocument(null);
					}
				} else {
					masterEntityLigne.setQte2LDocument(null);
				}
				masterEntityLigne.setQteLDocument(qte);
				//test rajout yann
				//old
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
//			selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				//new
				if(selectedLigneJSF.getTaStripePlanDTO() != null) {
					//selectedLigneJSF.getDto().setMtHtLDocument(selectedLigneJSF.getTaStripePlanDTO().getAmount().multiply(qte));
					//selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				}
				
			}else if(nomChamp.equals(Const.C_COEF_MULTIPLICATEUR)){	
				BigDecimal coef=BigDecimal.ONE;
				if(value!=null){
					if(!value.equals("")){
						coef=(BigDecimal)value;
					}

					
				} 
				
				masterEntityLigne.setCoefMultiplicateur(coef);
				masterEntityLigne.calculMontant();
				
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());

				
				
			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = taUniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					masterEntityLigne.setU1LDocument(entity.getCodeUnite());
				}else {
					selectedLigneJSF.getDto().setU1LDocument("");
					masterEntityLigne.setU1LDocument(null);
				}
				selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());		
			} else if(nomChamp.equals(Const.C_QTE2_L_DOCUMENT)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value==null) {
					masterEntityLigne.setQte2LDocument(null);
				}else if(!value.equals("")){
					qte=(BigDecimal)value;
				}
				selectedLigneJSF.getDto().setQte2LDocument(qte);
			}else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = taUniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					masterEntityLigne.setU2LDocument(entity.getCodeUnite());
					selectedLigneJSF.getDto().setU2LDocument(entity.getCodeUnite());
				}else {
					selectedLigneJSF.getDto().setU2LDocument("");
					masterEntityLigne.setU2LDocument(null);
					selectedLigneJSF.getDto().setQte2LDocument(null);	
				}
				selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());	
			} else if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
				TaTPaiement entity = null;
				if(value!=null){
					if(value instanceof TaTPaiement){
						entity = (TaTPaiement) value;
					}else{
						entity = taTPaiementService.findByCode((String)value);
					}
				}
				if(entity!=null && masterEntity.getTaTPaiement()!=null){
					if(entity.getCodeTPaiement()!=null) {
						changement=!entity.getCodeTPaiement().equals(masterEntity.getTaTPaiement().getCodeTPaiement());
					}
				}
				masterEntity.setTaTPaiement(entity);
				taTPaiement=entity;
				//			if(changement || masterEntity.getTaReglement()==null) {
				//				actInitReglement();
				//			}
			}else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {
				TaCPaiement entity = null;
				if(value!=null){
					entity = (TaCPaiement) value;
				}
				setTaCPaiementDoc(entity);
				if(entity!=null) {
					selectedCPaiement.setCodeCPaiement(entity.getCodeCPaiement());
					selectedCPaiement.setFinMoisCPaiement(entity.getFinMoisCPaiement());
					selectedCPaiement.setReportCPaiement(entity.getReportCPaiement());
					selectedCPaiement.setLibCPaiement(entity.getLibCPaiement());
				}			
			} else	if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)) {
				BigDecimal tx=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						tx=(BigDecimal)value;
					}
					masterEntity.setTxRemHtDocument(tx);
					mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				}
			} else	if(nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)) {
				BigDecimal tx=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						tx=(BigDecimal)value;
					}
					masterEntity.setTxRemTtcDocument(tx);
					mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				}
			} else	if(nomChamp.equals(Const.C_REM_TX_L_DOCUMENT)) {
				BigDecimal tx=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						tx=(BigDecimal)value;
					}
				}
				masterEntityLigne.setRemTxLDocument(tx);
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
			}else	if(nomChamp.equals(Const.C_EXPORT)) {
			} else if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {
				if(value!=null){
					if(value instanceof TaTTvaDoc){
						selectedDocumentDTO.setCodeTTvaDoc(((TaTTvaDoc) value).getCodeTTvaDoc());
					}else if(value instanceof String){
						selectedDocumentDTO.setCodeTTvaDoc((String) value);
					}
				}
				initLocalisationTVA();
				//si TTC est vrai et que codeTvaDoc différent de France alors on remets TTC à faux
				//car la saisie dans ce cas là doit se faire sur le HT
				if(selectedDocumentDTO.getTtc() && disableTtcSiDocSansTVA()) {
					selectedDocumentDTO.setTtc(false);
					validateUIField(Const.C_TTC, selectedDocumentDTO.getTtc());
				}
			} else if(nomChamp.equals(Const.C_TTC)) {
				masterEntity.setTtc(LibConversion.booleanToInt(selectedDocumentDTO.getTtc()));

			}			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	//	public List<TaTiers> tiersAutoComplete(String query) {
	//		List<TaTiers> allValues = taTiersService.selectAll();
	//		List<TaTiers> filteredValues = new ArrayList<TaTiers>();
	//
	//		for (int i = 0; i < allValues.size(); i++) {
	//			TaTiers civ = allValues.get(i);
	//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(civ);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//
	//	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
	//		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
	//		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
	//
	//		for (int i = 0; i < allValues.size(); i++) {
	//			TaTiersDTO civ = allValues.get(i);
	//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
	//					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(civ);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//	
	//	public List<TaArticle> articleAutoComplete(String query) {
	//		List<TaArticle> allValues = taArticleService.selectAll();
	//		List<TaArticle> filteredValues = new ArrayList<TaArticle>();
	//
	//		for (int i = 0; i < allValues.size(); i++) {
	//			TaArticle civ = allValues.get(i);
	//			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(civ);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
	//		List<TaArticleDTO> allValues = taArticleService.findByCodeLight("*");
	//		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
	//
	//		for (int i = 0; i < allValues.size(); i++) {
	//			TaArticleDTO civ = allValues.get(i);
	//			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(civ);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//	
	//	public List<TaTPaiement> typePaiementAutoComplete(String query) {
	//		List<TaTPaiement> allValues = taTPaiementService.selectAll();
	//		List<TaTPaiement> filteredValues = new ArrayList<TaTPaiement>();
	//		TaTPaiement cp = new TaTPaiement();
	//		cp.setCodeTPaiement(Const.C_AUCUN);
	//		filteredValues.add(cp);
	//		for (int i = 0; i < allValues.size(); i++) {
	//			cp = allValues.get(i);
	//			if(query.equals("*") || cp.getCodeTPaiement().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(cp);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//	
	//	public List<TaCPaiement> typeCPaiementAutoComplete(String query) {
	//		List<TaCPaiement> allValues = taCPaiementService.selectAll();
	//		List<TaCPaiement> filteredValues = new ArrayList<TaCPaiement>();
	//		TaCPaiement cp = new TaCPaiement();
	//		cp.setCodeCPaiement(Const.C_AUCUN);
	//		filteredValues.add(cp);
	//		for (int i = 0; i < allValues.size(); i++) {
	//			cp = allValues.get(i);
	//			if(query.equals("*") || cp.getCodeCPaiement().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(cp);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//	
	//	public List<TaTTvaDoc> typeTvaDocAutoComplete(String query) {
	//		List<TaTTvaDoc> allValues = taTTvaDocService.selectAll();
	//		List<TaTTvaDoc> filteredValues = new ArrayList<TaTTvaDoc>();
	//		TaTTvaDoc cp = new TaTTvaDoc();
	//		cp.setCodeTTvaDoc(Const.C_AUCUN);
	//		filteredValues.add(cp);
	//		for (int i = 0; i < allValues.size(); i++) {
	//			cp = allValues.get(i);
	//			if(query.equals("*") || cp.getCodeTTvaDoc().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(cp);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//	
	//	public List<TaUnite> uniteAutoComplete(String query) {
	//		List<TaUnite> allValues = taUniteService.selectAll();
	//		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
	//		TaUnite civ=new TaUnite();
	//		civ.setCodeUnite(Const.C_AUCUN);
	//		filteredValues.add(civ);
	//		for (int i = 0; i < allValues.size(); i++) {
	//			 civ = allValues.get(i);
	//			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(civ);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//
	//	public List<TaEntrepot> entrepotAutoComplete(String query) {
	//		List<TaEntrepot> allValues = taEntrepotService.selectAll();
	//		List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
	//
	//		for (int i = 0; i < allValues.size(); i++) {
	//			TaEntrepot civ = allValues.get(i);
	//			if(query.equals("*") || civ.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
	//				filteredValues.add(civ);
	//			}
	//		}
	//
	//		return filteredValues;
	//	}
	//
	//	public List<String> emplacementAutoComplete(String query) {
	//		List<String> filteredValues = new ArrayList<String>();
	//		if(selectedLigneJSF!=null && selectedLigneJSF.getTaEntrepot()!=null) {
	//			List<String> allValues = taEtatStockService.emplacementEntrepot(selectedLigneJSF.getTaEntrepot().getCodeEntrepot(),null);
	//			
	//			for (int i = 0; i < allValues.size(); i++) {
	//				String civ = allValues.get(i);
	//				if(civ !=null && (query.equals("*") || civ.toLowerCase().contains(query.toLowerCase()))) {
	//					filteredValues.add(civ);
	//				}
	//			}
	//		}
	//		return filteredValues;
	//	}
	//	
	//	public void actDialogControleLot(ActionEvent actionEvent) {
	//		
	////		RequestContext.getCurrentInstance().openDialog("aide");
	//    
	//        Map<String,Object> options = new HashMap<String, Object>();
	//        options.put("modal", true);
	//        options.put("draggable", false);
	//        options.put("resizable", false);
	//        options.put("contentHeight", 640);
	//        options.put("modal", true);
	//        
	//        //Map<String,List<String>> params = null;
	//        Map<String,List<String>> params = new HashMap<String,List<String>>();
	//        List<String> list = new ArrayList<String>();
	//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	//        params.put("modeEcranDefaut", list);
	//        
	//        RequestContext.getCurrentInstance().openDialog("solstyce/dialog_controle_article", options, params);
	//		
	////		FacesContext context = FacesContext.getCurrentInstance();  
	////		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	//	}
	//	
	//	public void handleReturnDialogControleLot(SelectEvent event) {
	//		if(event!=null && event.getObject()!=null) {
	//			//taTTiers = (TaTTiers) event.getObject();
	//		}
	//	}


	//	public void setInsertAuto(boolean insertAuto) {
	////		String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
	////		if(insertAuto) {
	////			rc.setOncomplete(oncomplete);
	////			System.out.println("Ajoutera une nouvelle ligne automatiquement");
	////		} else {
	////			rc.setOncomplete(null);
	////			System.out.println("N'ajoutera pas de nouvelle ligne automatiquement");
	////		}
	//		this.insertAuto = insertAuto;
	//	}

	public void initialisationDesInfosComplementaires(Boolean recharger,String typeARecharger){
		/*
		 * verifier que le type d'adresse existe
		 * verifier que le tiers à des adresses de ce tpe
		 * remplir les maps et changer les clauses where des DAO de modeles
		 * 
		 * remplir les modèles 
		 * avoir dans l'ordre :
		 * - adresse de l'infos facture si elle existe
		 * - adresse du type demandé s'il y en a
		 * - le reste des adresses du tiers
		 */

		try {
			if(((TaAbonnementDTO)selectedDocumentDTO).getCodeDocument()!=null) {
				taInfosDocument = taInfosDocumentService.findByCodeDevis(((TaAbonnementDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosAbonnement();
			}

			if(taInfosDocument!=null && taInfosDocument.getNomTiers()!=null)selectedDocumentDTO.setNomTiers(taInfosDocument.getNomTiers());
			if(taInfosDocument!=null && taInfosDocument.getCodeTTvaDoc()!=null)selectedDocumentDTO.setCodeTTvaDoc(taInfosDocument.getCodeTTvaDoc());
			if(recharger) {				
				if(masterEntity.getTaTiers()!=null){
					masterEntity.setTaTiers(taTiersService.findById(masterEntity.getTaTiers().getIdTiers()));
					selectedDocumentDTO.setCodeTiers(masterEntity.getTaTiers().getCodeTiers());
					selectedDocumentDTO.setNomTiers(masterEntity.getTaTiers().getNomTiers());
					selectedDocumentDTO.setPrenomTiers(masterEntity.getTaTiers().getPrenomTiers());
					selectedDocumentDTO.setNomEntreprise("");
					if(masterEntity.getTaTiers().getTaEntreprise()!=null)
						selectedDocumentDTO.setNomEntreprise(masterEntity.getTaTiers().getTaEntreprise().getNomEntreprise());

					if (masterEntity.getTaTiers().getTaTTvaDoc()!=null){
						selectedDocumentDTO.setCodeTTvaDoc(masterEntity.getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
					}
				}
			}

			if(typeARecharger==Const.RECHARGE_ADR_FACT||typeARecharger=="")
				initialisationDesAdrFact(recharger);
			if(typeARecharger==Const.RECHARGE_ADR_LIV||typeARecharger=="")
				initialisationDesAdrLiv(recharger);
			if(typeARecharger==Const.RECHARGE_C_PAIEMENT||typeARecharger=="")
				initialisationDesCPaiement(recharger);
			if(typeARecharger==Const.RECHARGE_INFOS_TIERS||typeARecharger=="")
				initialisationDesInfosTiers(recharger);
		}  catch (FinderException e) {
			e.printStackTrace();
		}
	}

	//	public void initialisationDesAdrFact(Boolean recharger){
	//		try {
	//
	//			LinkedList<AdresseInfosFacturationDTO> liste = new LinkedList<AdresseInfosFacturationDTO>();
	//	
	//			boolean leTiersADesAdresseFact = false;
	//			if(masterEntity.getTaTiers()!=null){
	//				if(typeAdresseFacturation!=null && taTAdrService.findByCode(typeAdresseFacturation)!=null) { //le type d'adresse existe bien
	//					leTiersADesAdresseFact = masterEntity.getTaTiers().aDesAdressesDuType(typeAdresseFacturation); //le tiers a des adresse de ce type
	//				}	
	//			
	//			if(leTiersADesAdresseFact) { 
	//				//ajout des adresse de facturation au modele
	//				for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
	//					if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseFacturation)){
	//						liste.add(mapperModelToUIAdresseInfosDocument.map(taAdresse, classModelAdresseFact));
	//					}
	//				}
	//			}else{
	//
	//			}
	//			
	//			//ajout des autres types d'adresse
	//			for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
	//				liste.add(mapperModelToUIAdresseInfosDocument.map(taAdresse, classModelAdresseFact));
	//			}
	//			if(liste.isEmpty()) 
	//				liste.add(mapperModelToUIAdresseInfosDocument.map(new TaAdresse(), classModelAdresseFact));			
	//			}
	//			//ajout de l'adresse de livraison inscrite dans l'infos facture
	//			if(taInfosDocument!=null) {
	//				if(recharger )
	//					liste.add(mapperModelToUIInfosDocVersAdresseFact.map(taInfosDocument, classModelAdresseFact));
	//				else
	//					liste.addFirst(mapperModelToUIInfosDocVersAdresseFact.map(taInfosDocument, classModelAdresseFact));
	//			}
	//			
	//			if (!liste.isEmpty())
	//				selectedAdresseFact.setIHMAdresse(liste.getFirst());
	//			else
	//				selectedAdresseFact.setIHMAdresse(new AdresseInfosFacturationDTO());
	//			
	//			System.out.println("DevisController.initialisationDesAdrFact() ** "+selectedAdresseFact.getAdresse1Adresse());
	//		
	//		}catch (FinderException e) {
	//			e.printStackTrace();
	//		}
	//				
	//	}
	//
	//
	//	public void initialisationDesAdrLiv(Boolean recharger){
	//		try {
	//		
	//			LinkedList<AdresseInfosLivraisonDTO> liste = new LinkedList<AdresseInfosLivraisonDTO>();
	//
	//		boolean leTiersADesAdresseLiv = false;
	//		if(masterEntity.getTaTiers()!=null){
	//			if(typeAdresseLivraison!=null && taTAdrService.findByCode(typeAdresseLivraison)!=null) { //le type d'adresse existe bien
	//				leTiersADesAdresseLiv = masterEntity.getTaTiers().aDesAdressesDuType(typeAdresseLivraison); //le tiers a des adresse de ce type
	//			}
	//		
	//		
	//		if(leTiersADesAdresseLiv) { 
	//			//ajout des adresse de livraison au modele
	//			for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
	//				if(taAdresse.getTaTAdr()!=null && taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
	//					liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
	//				}
	//			}
	//		}
	//		//ajout des autres types d'adresse
	//		for (TaAdresse taAdresse : masterEntity.getTaTiers().getTaAdresses()) {
	//			if(leTiersADesAdresseLiv && taAdresse.getTaTAdr()!=null && !taAdresse.getTaTAdr().getCodeTAdr().equals(typeAdresseLivraison)){
	//				liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
	//			} else {
	//				liste.add(mapperModelToUIAdresseLivInfosDocument.map(taAdresse, classModelAdresseLiv));
	//			}
	//		}
	//		}
	//		//ajout de l'adresse de livraison inscrite dans l'infos facture
	//		if(taInfosDocument!=null) {
	//			
	//			if(recharger )
	//				liste.add(mapperModelToUIInfosDocVersAdresseLiv.map(taInfosDocument, classModelAdresseLiv));
	//			else
	//				liste.addFirst(mapperModelToUIInfosDocVersAdresseLiv.map(taInfosDocument, classModelAdresseLiv));
	//		}		
	//		if (!liste.isEmpty())
	//			((AdresseInfosLivraisonDTO) selectedAdresseLiv).setIHMAdresse(liste.getFirst());
	//		else
	//			((AdresseInfosLivraisonDTO) selectedAdresseLiv).setIHMAdresse(new AdresseInfosLivraisonDTO());
	//		
	//		}catch (FinderException e) {
	//			e.printStackTrace();
	//		}
	//				
	//	}



	public void initialisationDesCPaiement(Boolean recharger) {
		LinkedList<TaCPaiementDTO> liste = new LinkedList<TaCPaiementDTO>();
		try {
			selectedCPaiement = new TaCPaiementDTO();
			TaInfosAbonnement taInfosDocument = null;
			if (masterEntity != null) {
				if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
					taInfosDocument = taInfosDocumentService
					.findByCodeDevis(masterEntity.getCodeDocument());
				else
					taInfosDocument = new TaInfosAbonnement();


				taCPaiementDoc = null;
				List<TaCPaiement> listeCPaiement=taCPaiementService.rechercheParType(TaAbonnement.TYPE_DOC);
				if(listeCPaiement!=null && !listeCPaiement.isEmpty())taCPaiementDoc=listeCPaiement.get(0);

				if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_DEVIS) != null
						&& taTCPaiementService.findByCode(
								TaTCPaiement.C_CODE_TYPE_DEVIS).getTaCPaiement() != null) {
					taCPaiementDoc = taTCPaiementService.findByCode(
							TaTCPaiement.C_CODE_TYPE_DEVIS).getTaCPaiement();
				}
				int report = 0;
				int finDeMois = 0;


				if (masterEntity.getTaTiers() != null) {
					if (masterEntity.getTaTiers().getTaTPaiement() != null) {
						if (masterEntity.getTaTiers().getTaTPaiement()
								.getReportTPaiement() != null)
							report = masterEntity.getTaTiers().getTaTPaiement()
							.getReportTPaiement();
						if (masterEntity.getTaTiers().getTaTPaiement()
								.getFinMoisTPaiement() != null)
							finDeMois = masterEntity.getTaTiers()
							.getTaTPaiement().getFinMoisTPaiement();
					}

					if (masterEntity.getTaTiers().getTaCPaiement() == null
							|| (report != 0 || finDeMois != 0)) {
						if (taCPaiementDoc == null
								|| (report != 0 || finDeMois != 0)) {// alors on
							// met
							// au
							// moins
							// une
							// condition
							// vide
							TaCPaiementDTO ihm = new TaCPaiementDTO();
							ihm.setReportCPaiement(report);
							ihm.setFinMoisCPaiement(finDeMois);
							liste.add(ihm);
						}
					} else
						liste.add(
								mapperModelToUICPaiementInfosDocument.map(
										masterEntity.getTaTiers()
										.getTaCPaiement(),
										classModelCPaiement));
				}


				if (taCPaiementDoc != null  )
					liste.add(
							mapperModelToUICPaiementInfosDocument.map(taCPaiementDoc,
									classModelCPaiement));
				masterEntity.setTaInfosDocument(taInfosDocument);
				// ajout de l'adresse de livraison inscrite dans l'infos facture
				if (taInfosDocument != null) {
					if (recharger)
						liste.add(
								mapperModelToUIInfosDocVersCPaiement.map(
										masterEntity.getTaInfosDocument(),
										classModelCPaiement));
					else
						liste.addFirst(
								mapperModelToUIInfosDocVersCPaiement.map(
										masterEntity.getTaInfosDocument(),
										classModelCPaiement));
				}
			}
			if (!liste.isEmpty()) {
				((TaCPaiementDTO) selectedCPaiement)
				.setSWTCPaiement(liste.getFirst());
			} else {
				((TaCPaiementDTO) selectedCPaiement)
				.setSWTCPaiement(new TaCPaiementDTO());
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void calculDateEcheance() {
		calculDateEcheance(false);
	}

	public void calculDateEcheance(Boolean appliquer) {
		if(selectedDocumentDTO!=null){
			if(((TaAbonnementDTO)selectedDocumentDTO).getId()==0|| appliquer) {

				Integer report = null;
				Integer finDeMois = null;
				if(((TaCPaiementDTO)selectedCPaiement)!=null /*&& ((TaCPaiementDTO)selectedCPaiement).getCodeCPaiement()!=null*/) { 
					if(((TaCPaiementDTO)selectedCPaiement).getReportCPaiement()!=null)
						report = ((TaCPaiementDTO)selectedCPaiement).getReportCPaiement();
					if(((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement()!=null)
						finDeMois = ((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement();
				}
				masterEntity.setDateDocument(selectedDocumentDTO.getDateDocument());
				((TaAbonnementDTO)selectedDocumentDTO).setDateEchDocument(taAbonnementService.calculDateEcheance(masterEntity,report, finDeMois));
			}
		}
	}


	//	public void initialisationDesInfosComplementaires(){
	//		initialisationDesInfosComplementaires(false,"");
	//	}
	//
	//	public void actReinitAdrFact() throws Exception {
	//		initialisationDesInfosComplementaires(true,Const.RECHARGE_ADR_FACT);
	//		actModifier();
	//	}
	//	
	//	public void actReinitAdrLiv() throws Exception {
	//		initialisationDesInfosComplementaires(true,Const.RECHARGE_ADR_LIV);
	//		actModifier();
	//	}
	//	
	//	public void actReinitCPaiement() throws Exception {
	//		initialisationDesInfosComplementaires(true,Const.RECHARGE_C_PAIEMENT);
	//		calculDateEcheance(true);
	//		actModifier();
	//	}
	//	
	//	public void actAppliquerCPaiement() throws Exception {
	//		calculDateEcheance(true);
	//		actModifier();
	//	}
	//	
	//	public void actReinitInfosTiers() throws Exception {
	//		initialisationDesInfosComplementaires(true,Const.RECHARGE_INFOS_TIERS);
	//		actModifier();
	//	}
	//	
	//	public void modifMode(EnumModeObjet mode){
	//		if (!VerrouInterface.isVerrouille() ){
	//			try {
	//				if(!modeEcran.dataSetEnModif()) {
	//					if(mode.equals(EnumModeObjet.C_MO_EDITION)) {
	//						actModifier();
	//					} 
	//					if(mode.equals(EnumModeObjet.C_MO_INSERTION)) {
	//						actInserer(null);
	//					} 					
	//				}
	//			} catch (Exception e1) {
	//				e1.printStackTrace();  
	//			}
	//		}
	//	}
	//	
	//	public boolean disableTtc(){
	//		if(masterEntity!=null && masterEntity.getLignes()!=null)
	//			return masterEntity.getLignes().size()>0;
	//		return false;
	//	}

	@Override
	public void calculTypePaiement(boolean recharger) {
		// TODO Auto-generated method stub

	}

	@Override
	public void IHMmodel(TaLAbonnementDTOJSF dtoJSF, TaLAbonnement ligne) {
		LgrDozerMapper<TaLAbonnement,TaLAbonnementDTO> mapper = new LgrDozerMapper<TaLAbonnement,TaLAbonnementDTO>();
		TaLAbonnementDTO dto = dtoJSF.getDto();

		dto = (TaLAbonnementDTO) mapper.map(ligne, TaLAbonnementDTO.class);
		dtoJSF.setDto(dto);
		//		dtoJSF.setTaLot(ligne.getTaLot());
		//		if(dtoJSF.getArticleLotEntrepotDTO()==null) {
		//			dtoJSF.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
		//		}
		//		if(ligne.getTaLot()!=null) {
		//			dtoJSF.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
		//		}
		dtoJSF.setTaArticle(ligne.getTaArticle());
		if(ligne.getTaArticle()!=null){
			dtoJSF.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
		}
		//		dtoJSF.setTaEntrepot(ligne.getTaEntrepot());
		if(ligne.getU1LDocument()!=null) {
			dtoJSF.setTaUnite1(new TaUnite());
			dtoJSF.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
		}
		if(ligne.getU2LDocument()!=null) {
			dtoJSF.setTaUnite2(new TaUnite());
			dtoJSF.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
		}
		dtoJSF.updateDTO(false);

	}

	@Override
	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
		actModifier();
	}

	public OuvertureTiersBean getOuvertureTiersBean() {
		return ouvertureTiersBean;
	}

	public void setOuvertureTiersBean(OuvertureTiersBean ouvertureTiersBean) {
		this.ouvertureTiersBean = ouvertureTiersBean;
	}

	public OuvertureArticleBean getOuvertureArticleBean() {
		return ouvertureArticleBean;
	}

	public void setOuvertureArticleBean(OuvertureArticleBean ouvertureArticleBean) {
		this.ouvertureArticleBean = ouvertureArticleBean;
	}


	/*******************/
	public List<TaStripeSource> taStripeSourceAutoComplete(String query) {
		List<TaStripeSource> allValues = taStripeSourceService.selectAll();
		List<TaStripeSource> filteredValues = new ArrayList<TaStripeSource>();

		for (int i = 0; i < allValues.size(); i++) {
			TaStripeSource civ = allValues.get(i);
			if(query.equals("*") || civ.getIdExterne().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		selectedTaStripeSourceSubscriptionDTO = null;
		
		return super.tiersAutoCompleteDTOLight(query);
		
		
	}
	
	public List<TaStripeSourceDTO> taStripeSourceAutoCompleteLight(String query) {
		List<TaStripeSourceDTO> filteredValues = new ArrayList<TaStripeSourceDTO>();
		List<TaStripeSourceDTO> allValues = null;

		if(taStripeCustomer!=null) {
			allValues = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
		} else {
			allValues = taStripeSourceService.rechercherSourceCustomerDTO(null);
		}

			if(allValues!=null) {
				for (int i = 0; i < allValues.size(); i++) {
					TaStripeSourceDTO civ = allValues.get(i);
					if(query.equals("*") 
							|| (civ.getIdExterne()!=null && civ.getIdExterne().toLowerCase().contains(query.toLowerCase()))
							|| (civ.getAutomatique()!=null && !civ.getAutomatique())
							) {
						filteredValues.add(civ);
					}
				}
			}
			
		if(allValues.isEmpty()) {
			selectedTaStripeSourceSubscriptionDTO = null;
		}
		
		TaStripeSourceDTO paiementCheque = new TaStripeSourceDTO();
		paiementCheque.setIdExterne("Chèque");
		paiementCheque.setLiblStripeTSource("Non automatique");
		filteredValues.add(paiementCheque);

		TaStripeSourceDTO paiementVirement = new TaStripeSourceDTO();
		paiementVirement.setIdExterne("Virement");
		paiementVirement.setLiblStripeTSource("Non automatique");
		filteredValues.add(paiementVirement);
		
		TaStripeSourceDTO paiementCb= new TaStripeSourceDTO();
		paiementCb.setIdExterne("CB");
		paiementCb.setLiblStripeTSource("Non automatique");
		filteredValues.add(paiementCb);

		return filteredValues;
	}
	/************************************/

//	public TaStripeSubscriptionDTO getSelectedTaStripeSubscriptionDTO() {
//		return selectedTaStripeSubscriptionDTO;
//	}
//
//	public void setSelectedTaStripeSubscriptionDTO(TaStripeSubscriptionDTO selectedTaStripeSubscriptionDTO) {
//		this.selectedTaStripeSubscriptionDTO = selectedTaStripeSubscriptionDTO;
//	}

	public TaStripeSourceDTO getSelectedTaStripeSourceSubscriptionDTO() {
		return selectedTaStripeSourceSubscriptionDTO;
	}

	public void setSelectedTaStripeSourceSubscriptionDTO(TaStripeSourceDTO selectedTaStripeSourceSubscriptionDTO) {
		this.selectedTaStripeSourceSubscriptionDTO = selectedTaStripeSourceSubscriptionDTO;
	}

//	public TaStripeSubscriptionItemDTO getSelectedTaStripeSubscriptionItemDTO() {
//		return selectedTaStripeSubscriptionItemDTO;
//	}
//
//	public void setSelectedTaStripeSubscriptionItemDTO(TaStripeSubscriptionItemDTO selectedTaStripeSubscriptionItemDTO) {
//		this.selectedTaStripeSubscriptionItemDTO = selectedTaStripeSubscriptionItemDTO;
//	}

	public TaStripePlanDTO getSelectedTaStripePlanDTO() {
		return selectedTaStripePlanDTO;
	}

	public void setSelectedTaStripePlanDTO(TaStripePlanDTO selectedTaStripePlanDTO) {
		this.selectedTaStripePlanDTO = selectedTaStripePlanDTO;
	}

	public TaStripeCustomer getTaStripeCustomer() {
		return taStripeCustomer;
	}

	public void setTaStripeCustomer(TaStripeCustomer taStripeCustomer) {
		this.taStripeCustomer = taStripeCustomer;
	}
	
	public List<TaStripeProductDTO> taStripeProductAutoCompleteLight(String query) {
		List<TaStripeProductDTO> allValues  = new ArrayList<TaStripeProductDTO>();
		 //allValues = taStripeProductService.selectAllDTOAvecPlan();  
		allValues = taStripeProductService.selectAllDTOAvecPlanByIdFrequence(selectedTaFrequenceDTO.getId()); 
        List<TaStripeProductDTO> filteredValues = new ArrayList<TaStripeProductDTO>();
         if(allValues != null) {
        	 for (int i = 0; i < allValues.size(); i++) {
             	TaStripeProductDTO civ = allValues.get(i);
             	if(query.equals("*") || civ.getName().toLowerCase().contains(query.toLowerCase())) {
                     filteredValues.add(civ);
                 }
             }
         }
        
         
        return filteredValues;
    }
	/**
	 * @author yann
	 * @param query
	 * @return
	 */
	public List<TaArticleDTO> taArticleAutoCompleteLight(String query) {
		List<TaArticleDTO> allValues  = new ArrayList<TaArticleDTO>();
		 //allValues = taStripeProductService.selectAllDTOAvecPlan();  
		//allValues = taStripeProductService.selectAllDTOAvecPlanByIdFrequence(selectedTaFrequenceDTO.getId()); 
		allValues = taArticleService.selectAllDTOAvecPlan(); 
        List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
         if(allValues != null) {
        	 for (int i = 0; i < allValues.size(); i++) {
        		 TaArticleDTO civ = allValues.get(i);
             	if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
                     filteredValues.add(civ);
                 }
             }
         }
        
         
        return filteredValues;
    }
	
	public List<TaStripePlan> taStripePlanAutoComplete(String query) {
        List<TaStripePlan> allValues = taStripePlanService.selectAll();
        List<TaStripePlan> filteredValues = new ArrayList<TaStripePlan>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaStripePlan civ = allValues.get(i);
            if(query.equals("*") || civ.getNickname().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	public List<TaStripePlanDTO> taStripePlanAutoCompleteLight(String query) {
        //List<TaStripePlanDTO> allValues = taStripePlanService.findByIdProductDTO(selectedLigneJSF.getTaStripeProductDTO().getId());
       // List<TaStripePlanDTO> allValues = taStripePlanService.findByIdProductDTOAndByIdFrequence(selectedLigneJSF.getTaStripeProductDTO().getId(), selectedTaFrequenceDTO.getId());
      //  List<TaStripePlanDTO> allValues = taStripePlanService.findByIdArticleDTOAndByIdFrequence(selectedLigneJSF.getTaArticle().getIdArticle(), selectedTaFrequenceDTO.getId());
		List<TaStripePlanDTO> allValues = taStripePlanService.findByIdArticleDTOAndByNbMois(selectedLigneJSF.getTaArticle().getIdArticle(), selectedDocumentDTO.getNbMoisFrequenceFacturation());
        List<TaStripePlanDTO> filteredValues = new ArrayList<TaStripePlanDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaStripePlanDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getNickname()==null || civ.getNickname().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }

	public TaStripeProductDTO getSelectedTaStripeProductDTO() {
		return selectedTaStripeProductDTO;
	}

	public void setSelectedTaStripeProductDTO(TaStripeProductDTO selectedTaStripeProductDTO) {
		this.selectedTaStripeProductDTO = selectedTaStripeProductDTO;
	}

	public List<TaStripePlanDTO> getListePlan() {
		return listePlan;
	}

	public void setListePlan(List<TaStripePlanDTO> listePlan) {
		this.listePlan = listePlan;
	}

	public List<TaFrequenceDTO> getListeFrequenceDTO() {
		return listeFrequenceDTO;
	}

	public void setListeFrequenceDTO(List<TaFrequenceDTO> listeFrequenceDTO) {
		this.listeFrequenceDTO = listeFrequenceDTO;
	}

	public TaFrequenceDTO getSelectedTaFrequenceDTO() {
		return selectedTaFrequenceDTO;
	}

	public void setSelectedTaFrequenceDTO(TaFrequenceDTO selectedTaFrequenceDTO) {
		this.selectedTaFrequenceDTO = selectedTaFrequenceDTO;
	}

	public Integer getNbFrequence() {
		return nbFrequence;
	}

	public void setNbFrequence(Integer nbFrequence) {
		this.nbFrequence = nbFrequence;
	}

	public List<String> getListeTypeAbonnement() {
		return listeTypeAbonnement;
	}

	public void setListeTypeAbonnement(List<String> listeTypeAbonnement) {
		this.listeTypeAbonnement = listeTypeAbonnement;
	}

	public String getTypeAbonnementAvecEngagement() {
		return typeAbonnementAvecEngagement;
	}

	public void setTypeAbonnementAvecEngagement(String typeAbonnementAvecEngagement) {
		this.typeAbonnementAvecEngagement = typeAbonnementAvecEngagement;
	}

	public String getTypeAbonnementSansEngagement() {
		return typeAbonnementSansEngagement;
	}

	public void setTypeAbonnementSansEngagement(String typeAbonnementSansEngagement) {
		this.typeAbonnementSansEngagement = typeAbonnementSansEngagement;
	}

	public boolean isDisabledEnregistrement() {
		return disabledEnregistrement;
	}

	public void setDisabledEnregistrement(boolean disabledEnregistrement) {
		this.disabledEnregistrement = disabledEnregistrement;
	}

	public Date getDateDebutPeriode() {
		return dateDebutPeriode;
	}

	public void setDateDebutPeriode(Date dateDebutPeriode) {
		this.dateDebutPeriode = dateDebutPeriode;
	}

	public Date getDateFinPeriode() {
		return dateFinPeriode;
	}

	public void setDateFinPeriode(Date dateFinPeriode) {
		this.dateFinPeriode = dateFinPeriode;
	}

	public boolean isCronValide() {
		return CronValide;
	}

	public void setCronValide(boolean cronValide) {
		CronValide = cronValide;
	}

	public String getTypeDoc() {
		return typeDoc;
	}

	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}

	public Integer getNbMoisSuppressionAvis() {
		return nbMoisSuppressionAvis;
	}

	public void setNbMoisSuppressionAvis(Integer nbMoisSuppressionAvis) {
		this.nbMoisSuppressionAvis = nbMoisSuppressionAvis;
	}

}
