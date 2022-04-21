package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.service.MultitenantProxyTimerAbonnement;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCouponServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceItemServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.cron.model.TaCron;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class AbonnementTiersController extends AbstractController implements Serializable { 
	

//
//	@Inject @Named(value="tabListModelCenterBean")
//	private TabListModelBean tabsCenter;
//	
//	@Inject @Named(value="tabViewsBean")
//	private TabViewsBean tabViews;
//	
//	@Inject @Named(value="ouvertureDocumentBean")
//	private OuvertureDocumentBean ouvertureDocumentBean;
//	
//	private boolean creerAbonnementSurStripe = false;
//	
//	private String paramId;
//	
//	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach
//
//	private List<TaStripeCustomerDTO> values; 
//	private List<TaStripeCustomerDTO> valuesFiltered; 
//	
//	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranSubscription = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranPaiementPrevu = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranSubscriptionItem = new ModeObjetEcranServeur();
//	
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
//	
//	private @EJB ITaArticleServiceRemote taArticleService;
//	private @EJB ITaTLigneServiceRemote taTLigneService;
//	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
//	private @EJB ITaPrelevementServiceRemote taPrelevementService;
//	private @EJB ITaReglementServiceRemote taReglementService;
//	private @EJB ITaLotServiceRemote taLotService;
//
//	private @EJB ITaTiersServiceRemote taTiersService;
//	private @EJB ITaStripeProductServiceRemote taStripeProductService;
//	private @EJB ITaStripePlanServiceRemote taStripePlanService;
//	private @EJB ITaStripeCouponServiceRemote taStripeCouponService;
//	
//	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
//	private @EJB ITaStripeSubscriptionItemServiceRemote taStripeSubscriptionItemService;
//	private @EJB ITaStripeSubscriptionServiceRemote taStripeSubscriptionService;
//	private @EJB ITaStripeChargeServiceRemote taStripeChargeService;
//	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
//	private @EJB ITaStripeInvoiceServiceRemote taStripeInvoiceService;
//	private @EJB ITaStripeInvoiceItemServiceRemote taStripeInvoiceItemService;
//	private @EJB ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
//	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
//	
//	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
//	
//	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;
//	
////	private @EJB TimerDeclenchePaiementPrevuService timerDeclenchePaiementPrevuService;
////	private @EJB TimerCreerAvisEcheanceEtPaiementPrevuService timerCreerAvisEcheanceEtPaiementPrevuService;
//	private @EJB ITaCronServiceRemote taCronService;
//	private @EJB MultitenantProxyTimerAbonnement multitenantProxyTimerEvenement;
//	
//	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;
//
//	private TaTiers taFournisseur;
//	private TaTiersDTO taFournisseurDTO;
//	private TaStripeCustomerDTO[] selectedTaStripeCustomerDTOs; 
//	private TaStripeCustomer taStripeCustomer = new TaStripeCustomer();
//	private TaStripeCustomerDTO newTaStripeCustomerDTO = new TaStripeCustomerDTO();
//	private TaStripeCustomerDTO selectedTaStripeCustomerDTO = new TaStripeCustomerDTO();
////	private TaStripeCustomerDTO oldSelectedTaStripeCustomerDTO = new TaStripeCustomerDTO();
//	
//	private TaStripeSubscriptionDTO selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//	private TaStripeSubscriptionDTO newTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//	private List<TaStripeSubscriptionDTO> listeSubscription = new ArrayList<>();
//	private List<TaAbonnementFullDTO> listeAllAbonnement = new ArrayList<>();
//	private List<TaStripeSubscriptionDTO> listeSubscriptionNonStripe = new ArrayList<>();
//	private TaStripeSubscription taStripeSubscription = new TaStripeSubscription();
//	
//	private List<TaStripeSourceDTO> listeSource = new ArrayList<>();
//	private TaStripeSourceDTO selectedTaStripeSourceDefautDTO = new TaStripeSourceDTO();
//	private TaStripeSourceDTO selectedTaStripeSourceSubscriptionDTO = new TaStripeSourceDTO();
//	
//	private List<TaStripePaiementPrevuDTO> listePaiementPrevu = new ArrayList<>();
//	private TaStripePaiementPrevuDTO selectedTaStripePaiementPrevuDTO = new TaStripePaiementPrevuDTO();
//	private TaStripePaiementPrevu taStripePaiementPrevu = new TaStripePaiementPrevu();
//	
//	private TaStripeCouponDTO selectedTaStripeCouponDTO = new TaStripeCouponDTO();
//	
//	private TaStripeSubscriptionItemDTO selectedTaStripeSubscriptionItemDTO = new TaStripeSubscriptionItemDTO();
//	private TaStripeSubscriptionItemDTO newTaStripeSubscriptionItemDTO = new TaStripeSubscriptionItemDTO();
//	private List<TaStripeSubscriptionItemDTO> listeSubscriptionItem = new ArrayList<>();
//	private TaStripeSubscriptionItem taStripeSubscriptionItem = new TaStripeSubscriptionItem();
//	
//	private TaStripeInvoiceDTO selectedTaStripeInvoiceDTO = new TaStripeInvoiceDTO();
//	private List<TaStripeInvoiceDTO> listeInvoice = new ArrayList<>();
//	
//	private TaStripeInvoiceDTO selectedTaStripeInvoiceCustomerDTO = new TaStripeInvoiceDTO();
//	private List<TaStripeInvoiceDTO> listeInvoiceCustomer = new ArrayList<>();
//	
//	private TaStripeChargeDTO selectedTaStripeChargeCustomerDTO = new TaStripeChargeDTO();
//	private List<TaStripeChargeDTO> listeChargeCustomer = new ArrayList<>();
//	
//	private TaStripeInvoiceItemDTO selectedTaStripeInvoiceItemDTO = new TaStripeInvoiceItemDTO();
//	private List<TaStripeInvoiceItemDTO> listeInvoiceItem = new ArrayList<>();
//	
//	private List<TaAvisEcheance> listeAvisEcheance = new ArrayList<>();
//	private List<TaAvisEcheance> listeAvisEcheanceAbonnement = new ArrayList<>();
//	private List<TaLEcheance> listeEcheanceNonEmiseAbonnement = new ArrayList<>();
//	private List<TaLEcheance> listeLEcheance = new ArrayList<>();
//	
//	private List<TaStripePlanDTO> listePlan = new ArrayList<>();
//	private TaStripePlanDTO selectedTaStripePlanDTO = new TaStripePlanDTO();
//	private TaStripePlan taStripePlan = new TaStripePlan();
//	
//	private List<TaStripeProductDTO> listeProduct = new ArrayList<>();
//	private TaStripeProductDTO selectedTaStripeProductDTO = new TaStripeProductDTO();
//	private TaStripeProduct taStripeProduct = new TaStripeProduct();
//	
//	private LgrDozerMapper<TaStripePaiementPrevuDTO,TaStripePaiementPrevu> mapperUIToModelPaiementPrevu  = new LgrDozerMapper<>();
//	
//	private LgrDozerMapper<TaStripeSubscriptionDTO,TaStripeSubscription> mapperUIToModelSubscription  = new LgrDozerMapper<>();
//	private LgrDozerMapper<TaStripeSubscription,TaStripeSubscriptionDTO> mapperModelToUISubscription  = new LgrDozerMapper<>();
//	
//	private LgrDozerMapper<TaStripeCustomerDTO,TaStripeCustomer> mapperUIToModel  = new LgrDozerMapper<TaStripeCustomerDTO,TaStripeCustomer>();
//	private LgrDozerMapper<TaStripeCustomer,TaStripeCustomerDTO> mapperModelToUI  = new LgrDozerMapper<TaStripeCustomer,TaStripeCustomerDTO>();
//	
//	private TaTiers masterEntity;
//	private TaCompteServiceWebExterne compte = null;
//	
//	private TaTiersDTO taTiersDTO;
//	private boolean modeDialogue;
//	
//	private MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF;
//	
//	public AbonnementTiersController() {  
//		//values = getValues();
//	}  
//
//	@PostConstruct
//	public void postConstruct(){
//		refresh(null);
//		
//		modeEcranSubscription.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		
//		if(selectedTaStripeSubscriptionDTO==null)
//			selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO(); //TODO a supprimer peut être, pour eviter les NPE
//		if(selectedTaStripePaiementPrevuDTO==null)
//			selectedTaStripePaiementPrevuDTO = new TaStripePaiementPrevuDTO(); //TODO a supprimer peut être, pour eviter les NPE
//	}
//	public void refresh(){
//		refresh(null);
//		
//		if(selectedTaStripeSubscriptionDTO==null)
//			selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO(); //TODO a supprimer peut être, pour eviter les NPE
//		if(selectedTaStripePaiementPrevuDTO==null)
//			selectedTaStripePaiementPrevuDTO = new TaStripePaiementPrevuDTO(); //TODO a supprimer peut être, pour eviter les NPE
//	}
//
//	
//	public void refresh(ActionEvent ev){
//		selectedTaStripeCustomerDTO=null;
//		
//		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		if(params.get("modeEcranDefaut")!=null) {
//			modeDialogue = true;
//			String modeEcranDefaut = params.get("modeEcranDefaut");
//			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
//				actInserer(null);
//				
//				String codeFrs = params.get("codeFrs");
//				String codeBarre = params.get("codeBarre");
//				
////				selectedTaStripeCustomerDTO.setCodeBarreFournisseur(codeBarre);
////				taStripeCustomer.setCodeBarreFournisseur(codeBarre);
//				
////				selectedTaStripeCustomerDTO.setCodeFournisseur(codeFrs);
////				try {
////					taStripeCustomer.setTaFournisseur(taFournisseurService.findByCode(codeFrs));
////				} catch (FinderException e) {
////					e.printStackTrace();
////				}
//			}
//			
//			
//		} else {
//			modeDialogue = false;
//			compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
//			TaInfoEntreprise taInfoEntreprise = taInfoEntrepriseService.findInstance();
//			if( masterEntity!=null) {
//				listeSubscription = taStripeSubscriptionService.findAllDTOByIdTiers(masterEntity.getIdTiers());
//			}
//			
//			
//	   	    if(compte!=null && masterEntity!=null) {
//	   	    	try {
//					if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
//					
//					taStripeCustomer = taStripeCustomerService.rechercherCustomer(masterEntity);
//					if(taStripeCustomer!=null) {
//						selectedTaStripeCustomerDTO = taStripeCustomerService.findByIdDTO(taStripeCustomer.getIdStripeCustomer());
//						listeSource = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
//						
//						paiementEnLigneDossierService.rechercheSubscriptionStripeForCustomer(compte, taStripeCustomer.getIdExterne(), null, null);//mise à jour des données depuis stripe
//						paiementEnLigneDossierService.rechercheSubscriptionStripeForCustomer(compte, taStripeCustomer.getIdExterne(), null, "canceled");//mise à jour des données depuis stripe
////						listeSubscription = taStripeSubscriptionService.rechercherSubscriptionCustomerDTO(taStripeCustomer.getIdExterne()); //recherche en local
//						listeSubscription = taStripeSubscriptionService.findAllDTOByIdTiers(masterEntity.getIdTiers());
//						
//						Date dateDeb = taInfoEntreprise.getDatedebInfoEntreprise();
//						Date dateFin = taInfoEntreprise.getDatefinInfoEntreprise();
//						listeAvisEcheance = taAvisEcheanceService.rechercheDocument(dateDeb, dateFin, masterEntity.getCodeTiers());
//						
//						listeLEcheance = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceTiers(masterEntity.getCodeTiers());
////						
//						listeSubscriptionNonStripe = taStripeSubscriptionService.rechercherSubscriptionNonStripeCustomerDTO(taStripeCustomer.getIdExterne()); //recherche en local
//						
//						listeInvoiceCustomer = taStripeInvoiceService.rechercheInvoiceStripeForCustomerDTO(taStripeCustomer.getIdExterne(), null); //recherche dans la bdd locale
//						
//						paiementEnLigneDossierService.rechercheChargeStripeForCustomer(compte, taStripeCustomer.getIdExterne(),null,null);//mise à jour des données depuis stripe
//						listeChargeCustomer = taStripeChargeService.rechercherChargeCustomerDTO(taStripeCustomer.getIdExterne()); //recherche dans la bdd locale
//						
//						listePaiementPrevu = taStripePaiementPrevuService.rechercherPaiementPrevuCustomerDTO(taStripeCustomer.getIdExterne());
//						
//					} else {
//						//enregistrer au moins un des moyen de paiment dans stripe pour créer le customer
//						//OU
//						//Autoriser la création de customer sans source de paiement (a priori possible si envoi d'un "invoice" avec paiement par chèque
//					}
//					
//					
//		   	    	
//				} catch (FinderException e) {
//					e.printStackTrace();
//				}
//	   	    	
////				values = new ArrayList<>();
////				for (TaStripeCustomer refArticleFournisseur : masterEntity.getTaStripeCustomers()) {
////					TaStripeCustomerDTO dto=new TaStripeCustomerDTO();
////					mapperModelToUI.map(refArticleFournisseur, dto);
////					dto.setId(refArticleFournisseur.getIdRefArticleFournisseur());
////					dto.setIdArticle(masterEntity.getIdArticle());
////					dto.setCodeArticle(masterEntity.getCodeArticle());
////					if(refArticleFournisseur.getTaFournisseur()!=null) {
////						dto.setIdFournisseur(refArticleFournisseur.getTaFournisseur().getIdTiers());
////						dto.setCodeFournisseur(refArticleFournisseur.getTaFournisseur().getCodeTiers());
////					}
////					taFournisseur=refArticleFournisseur.getTaFournisseur();
////					values.add(dto);
////				}
////	
////				if(!values.isEmpty() && selectedTaStripeCustomerDTO==null)
////					selectedTaStripeCustomerDTO=values.get(0);
//			}
//			autoCompleteMapDTOtoUI();
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		}
//	}
//
//	public List<TaStripeCustomerDTO> getValues(){  
//		return values;
//	}
//	
//	public void actAnnulerPaiementPrevu(ActionEvent actionEvent) {
//		modeEcranPaiementPrevu.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	}
//	
//	public void actAnnulerAbonnement(ActionEvent actionEvent) {
//		modeEcranSubscription.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	}
//
//
//	public void actAnnuler(ActionEvent actionEvent) {
//		try {
//			switch (modeEcran.getMode()) {
//			case C_MO_INSERTION:
//				
//				refresh();
//				break;
//			case C_MO_EDITION:
//				if(selectedTaStripeCustomerDTO.getId()!=0){
//					taStripeCustomer=rechercheRefArticleFournisseurDansArticle(selectedTaStripeCustomerDTO);
//				}				
//				break;
//
//			default:
//				break;
//			}
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			mapperModelToUI.map(taStripeCustomer, selectedTaStripeCustomerDTO);
//
//			if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAnnuler"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//	}
//	
//	public void actPayerInvoiceManuellement(ActionEvent actionEvent) {
//		String idStripeInvoice = (String) actionEvent.getComponent().getAttributes().get("idStripeInvoice");
//		paiementEnLigneDossierService.payerInvoice(compte, idStripeInvoice,true);
//	}
//	
//	public void creerAvisEcheance(ActionEvent actionEvent) {
//		try {
//			String idStripeInvoice = (String) actionEvent.getComponent().getAttributes().get("idStripeInvoice");
//			multitenantProxyTimerEvenement.creerAvisEcheance(taStripeInvoiceService.findByCode(idStripeInvoice));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public TaPrelevement creerAvisPrelevement(TaStripeInvoice taStripeInvoice) {
//		return null;
//	}
//
//	public TaReglement creerReglement(TaStripeInvoice taStripeInvoice) {
//		return null;
//	}
//	
//	public void autoCompleteMapUIToDTO() {
//		if(selectedTaStripeSourceSubscriptionDTO!=null) {
////			try {
////				taFournisseur= taStripeSourceService.findByIdDTO(selectedTaStripeSourceSubscriptionDTO.getId());
////				
////			} catch (FinderException e) {
////				e.printStackTrace();
////			}
//		}
//	}
//	
//	public void autoCompleteMapDTOtoUI() {
//		try {
//			selectedTaStripeSourceSubscriptionDTO=null;
//			if(selectedTaStripeCustomerDTO!=null && selectedTaStripeCustomerDTO.getIdExterneSourceDefault()!=null && !selectedTaStripeCustomerDTO.getIdExterneSourceDefault().equals("")) {
//				selectedTaStripeSourceSubscriptionDTO = taStripeSourceService.findByCodeDTO(selectedTaStripeCustomerDTO.getIdExterneSourceDefault());
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public List<MoyenPaiementAbonnementDTOJSF> completeThemeContains(String query) {
//       // List<MoyenPaiementAbonnementDTOJSF> allThemes = service.getThemes();
//		List<MoyenPaiementAbonnementDTOJSF> allThemes = new ArrayList<>();
//		allThemes.add(new MoyenPaiementAbonnementDTOJSF("SEPA_BDG", "FRXXXXXXXXXXXX", "Compte XXXX", "src_46464654"));
//		allThemes.add(new MoyenPaiementAbonnementDTOJSF("SEPA_BDG", "FRXXXXXXXXXXXX", "Compte XXXX", "src_46464654"));
//		allThemes.add(new MoyenPaiementAbonnementDTOJSF("SEPA_STRIPE_SOURCE", "FRXXXXXXXXXXXX", "Compte XXXX", "src_46464654"));
//		allThemes.add(new MoyenPaiementAbonnementDTOJSF("CB", "FRXXXXXXXXXXXX", "Compte XXXX", "src_46464654"));
//		allThemes.add(new MoyenPaiementAbonnementDTOJSF("CB_STRIPE_SOURCE", "FRXXXXXXXXXXXX", "Compte XXXX", "src_46464654"));
//		
//        List<MoyenPaiementAbonnementDTOJSF> filteredThemes = new ArrayList<>();
//         
//        for (int i = 0; i < allThemes.size(); i++) {
//        	MoyenPaiementAbonnementDTOJSF skin = allThemes.get(i);
//            if(skin.getLibelle().toLowerCase().contains(query)) {
//                filteredThemes.add(skin);
//            }
//        }
//         
//        return filteredThemes;
//    }
//
//	public TaStripeCustomer rechercheRefArticleFournisseurDansArticle(TaStripeCustomerDTO dto){
////		for (TaStripeCustomer adr : masterEntity.getTaStripeCustomers()) {
////			if(adr.getIdRefArticleFournisseur()==dto.getId()) {
////				return adr;
////			}
////		}
//		return null;
//	}
//	
//
//	
//
//	public TaStripeCustomerDTO rechercheRefArticleFournisseurDansArticleDTO(TaStripeCustomerDTO dto){
////		for (TaStripeCustomerDTO ref : values) {
////			if(ref.getCodeFournisseur()!=null && ref.getCodeFournisseur().equals(dto.getCodeFournisseur()) &&
////					ref.getCodeArticleFournisseur()!=null && ref.getCodeArticleFournisseur().equals(dto.getCodeArticleFournisseur()) && 
////							ref.getCodeBarreFournisseur()!=null && ref.getCodeBarreFournisseur().equals(dto.getCodeBarreFournisseur())) {
////				return ref;
////			}
////		}
//		return null;
//	}
//	
//
//	public void actChangerSourceParDefautTiers(ActionEvent actionEvent) {
//		
//		try {
//			Integer idNouvelleSourceDefaut = (Integer) actionEvent.getComponent().getAttributes().get("idNouvelleSourceDefaut");
//			if(idNouvelleSourceDefaut!=null) {
//				taStripeCustomer.setTaSourceDefault(taStripeSourceService.findById(idNouvelleSourceDefaut));
//				taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
//				
//				//TODO mettre à jour les abonnements utilisant cette source ? voir comment fonctionne Stripe dans ce cas
//				
//				selectedTaStripeCustomerDTO = taStripeCustomerService.findByIdDTO(taStripeCustomer.getIdStripeCustomer());
//				listeSource = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
//				
//				//TODO changer la source par defaut dans Stripe aussi par API
//			}
//		
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void actDeclencherPaiementPrevuMaintenant(ActionEvent actionEvent) {
//		try {
//			Integer idPaiementPrevu = (Integer) actionEvent.getComponent().getAttributes().get("idPaiementPrevu");
//			if(idPaiementPrevu!=null) {
//				taStripePaiementPrevu = taStripePaiementPrevuService.findById(idPaiementPrevu);
//				if(taStripePaiementPrevu.getTaStripeCharge()==null) {
//					taStripePaiementPrevuService.declencherPaiementStripe(taStripePaiementPrevu);
//				} //else ce paiement a deja ete declenche
////				
////				//TODO mettre à jour les abonnements utilisant cette source ? voir comment fonctionne Stripe dans ce cas
////				
////				selectedTaStripeCustomerDTO = taStripeCustomerService.findByIdDTO(taStripeCustomer.getIdStripeCustomer());
////				listeSource = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
//				
//			}
//		
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	
//	public void actEnregistrer(ActionEvent actionEvent) {		
//		TaStripeCustomerDTO courant =selectedTaStripeCustomerDTO;
//		autoCompleteMapUIToDTO();
////		if(taFournisseurDTO!=null) {
////			taStripeCustomer.setTaFournisseur(taFournisseur);
////		}
//		
//		if(selectedTaStripeCustomerDTO!=null) {
//			mapperUIToModel.map(selectedTaStripeCustomerDTO, taStripeCustomer);
//		}
//		
//		try {
//			if(masterEntity!=null) {
////				masterEntity.addRefArticleFournisseur(taStripeCustomer);
//				taStripeCustomer.setTaTiers(masterEntity);
//				
////				masterEntity = taTiersService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
////				masterEntity = taTiersService.findById(masterEntity.getIdArticle());.
//				
//				String idCustomerStripe = null;
////				String idCustomerStripe = lgrStripe.creerCustomerStripe(
////						taStripeCustomer.getTaTiers().getTaEmail().getAdresseEmail(),
////						taStripeCustomer.getProductType(),
////						taStripeCustomer.getTaTiers().getNomTiers()
////						);
//				taStripeCustomer.setIdExterne(idCustomerStripe);
////				TaStripeCustomerDTO taStripeCustomerRech = lgrStripe.rechercherProduct(idProductStripe);
//				
//				
//				taStripeCustomerService.merge(taStripeCustomer);
//				mapperModelToUI.map(taStripeCustomer,selectedTaStripeCustomerDTO);
//				
//				if(modeDialogue) {
//					PrimeFaces.current().dialog().closeDynamic(taStripeCustomer);
//				} else {
//					refresh();
//					if(courant!=null) {
//						selectedTaStripeCustomerDTO=rechercheRefArticleFournisseurDansArticleDTO(courant);
//					}
//					
//					if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
//						values.add(selectedTaStripeCustomerDTO);
//					}
//					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//				}
//			}
//
//		} catch(Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référence article fournisseur", e.getMessage()));
//		}
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actEnregistrer"));
//		}
//	}
//	
//	public void actEnregistrerAbonnement(ActionEvent actionEvent) {
//		mapperUIToModelSubscription.map(selectedTaStripeSubscriptionDTO, taStripeSubscription);
//	
//		try {
//			taStripeSubscription.setTaStripeCustomer(taStripeCustomer);
////			taStripeSubscription.setStatus(null);
//			
//			Map<String,Integer> items = new HashMap<>();
//			
//			if(taStripeSubscription.getItems()==null) {
//				taStripeSubscription.setItems(new HashSet<>());
//			}
//			
//			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//			taStripeSubscription.setCodeDocument(taStripeSubscriptionService.genereCode(null));
//			
//			TaStripeSubscriptionItem taStripeSubscriptionItem = null;
//			for (TaStripeSubscriptionItemDTO taStripeSubscriptionItemDTO : listeSubscriptionItem) {
//				taStripeSubscriptionItem = new TaStripeSubscriptionItem();
//				taStripeSubscriptionItem.setTaStripeSubscription(taStripeSubscription);
//				taStripeSubscriptionItem.setTaPlan(taStripePlanService.findById(taStripeSubscriptionItemDTO.getIdStripePlan()));
//				taStripeSubscriptionItem.setQuantity(taStripeSubscriptionItemDTO.getQuantity());
////				taStripeSubscriptionItem.setQuantity(1);
//				taStripeSubscriptionItem.setComplement1(taStripeSubscriptionItemDTO.getComplement1());
//				taStripeSubscriptionItem.setComplement2(taStripeSubscriptionItemDTO.getComplement2());
//				taStripeSubscriptionItem.setComplement3(taStripeSubscriptionItemDTO.getComplement3());
//				
//				taStripeSubscription.getItems().add(taStripeSubscriptionItem);
//				
//				items.put(taStripeSubscriptionItem.getTaPlan().getIdExterne(), taStripeSubscriptionItem.getQuantity());
//			}
//			
//			String sourceStripeID = selectedTaStripeSourceSubscriptionDTO.getIdExterne();
////			String sourceStripeID = taStripeCustomer.getTaSourceDefault().getIdExterne();
//			Integer timpeStampBillingCycleAnchor = null;
//			String billing = null;
//			Integer daysUntilDue = null;
//			
//			if(sourceStripeID.equals("Chèque") || sourceStripeID.equals("Virement")) {
//				//Mode manuel, pas de prélèvement automatique
//				billing = "send_invoice";
//				daysUntilDue = 30;
//				taStripeSubscription.setBilling(billing);
//				taStripeSubscription.setDaysUntilDue(daysUntilDue);
//				sourceStripeID = null;
//			} else {
//				taStripeSubscription.setTaStripeSource(taStripeSourceService.rechercherSource(sourceStripeID));
//			}
//			
//			if(creerAbonnementSurStripe) {
//				/*
//				 * Création de l'abonnement chez Stripe avec l'api billing; dans ce cas:
//				 * - Stripe génère directement des Invoices
//				 * - peut envoyer des emails au customer suivant les paramètres du compte stripe (ou uniquement déclencher les webhooks)
//				 * - déclencher des paiements automatiquement sur la source de paiement associé à l'abonnement/customer
//				 * => BDG devra se synchroniser pour récupérer les invoices pour générer les avis de prélèvement
//				 * => BDG devra se synchroniser pour récupérer les paiements pour générer les règlements correspondant et les factures
//				 */
//				String idSubscriptionStripe = paiementEnLigneDossierService.creerSubscriptionStripe(
//						compte,
//						taStripeCustomer.getIdExterne(),
//						items, 
//						sourceStripeID, 
//						timpeStampBillingCycleAnchor, 
//						billing,
//						daysUntilDue);
//				
//				taStripeSubscription.setIdExterne(idSubscriptionStripe);
//			} 
//			/*
//			 * ELSE
//			 * Création de l'abonnement uniquement dans BDG (utilise les mêmes table que pour les abonnement Stripe)
//			 * => pas de création d'abonnement dans Stripe, pas de génération d'invoice par Stripe, pas de déclenchement de paiement automatique par Stripe
//			 * => BDG gère les échéances, génére les avis d'échéance, déclenche lui-même des paiements Stripe au bon momment
//			 */
//			
////			
//			taStripeSubscription = taStripeSubscriptionService.merge(taStripeSubscription);
////			mapperModelToUISubscription.map(taStripeSubscription,newTaStripeSubscriptionDTO);
//			
////			if(taStripeSubscription.getTimerHandle()!=null) {
////				//un timer existe deja pour cette notification, on l'annule et on le recréer
////				TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taStripeSubscription.getTimerHandle());
////				try {
////					Timer t = h.getTimer();
////					t.cancel();
////				} catch (NoSuchObjectLocalException ex) {
////					System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
////				}
////			}
////			taStripeSubscription.setTimerHandle(SerializationUtils.serialize((Serializable) timerCreerAvisEcheanceEtPaiementPrevuService.creerTimer(taStripeSubscription)));
//			
//			taStripeSubscription = taStripeSubscriptionService.merge(taStripeSubscription); //pour avoir l'id
//			
//			//créer la/les premieres echeances dans le cas ou l'on ne laisse pas stripe gérer les invoices
//			List<TaLEcheance> listePremieresEcheances = taStripeSubscriptionService.generePremieresEcheances(taStripeSubscription);
//			for (TaLEcheance taLEcheance : listePremieresEcheances) {
//				taLEcheance = taLEcheanceService.merge(taLEcheance);
//			}
//			//TODO création d'un avis d'échéance (avec échéance immédiate) pour le premier paiement 
//			// et surtout déclenchement d'un "paiement prévu" immédiat (ou peut être une heure après comme stripe pour permetre d'annuler/modifier si besion)
//			
//			
//			if(creerAbonnementSurStripe) {
//				listeSubscription = taStripeSubscriptionService.rechercherSubscriptionCustomerDTO(taStripeCustomer.getIdExterne());
//			} else {
//				listeSubscriptionNonStripe = taStripeSubscriptionService.rechercherSubscriptionNonStripeCustomerDTO(taStripeCustomer.getIdExterne()); //recherche en local
//			}
//			
//			modeEcranSubscription.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			modeEcranSubscriptionItem.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		} catch(Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subscription", e.getMessage()));
//		}
//	}
//	
//	public void actEnregistrerPaiementPrevu(ActionEvent actionEvent) {
//		mapperUIToModelPaiementPrevu.map(selectedTaStripePaiementPrevuDTO, taStripePaiementPrevu);
//		
//		try {
//			taStripePaiementPrevu.setTaStripeCustomer(taStripeCustomer);
////			taStripeSubscription.setStatus(null);
//		
//			String sourceStripeID = selectedTaStripeSourceSubscriptionDTO.getIdExterne();
////			String sourceStripeID = taStripeCustomer.getTaSourceDefault().getIdExterne();
//			Integer timpeStampBillingCycleAnchor = null;
//			String billing = null;
//			Integer daysUntilDue = null;
//			
//			if(sourceStripeID.equals("Chèque") || sourceStripeID.equals("Virement")) {
////				//Mode manuel, pas de prélèvement automatique
////				billing = "send_invoice";
////				daysUntilDue = 30;
////				taStripeSubscription.setBilling(billing);
////				taStripeSubscription.setDaysUntilDue(daysUntilDue);
//				sourceStripeID = null;
//			} else {
//				taStripePaiementPrevu.setTaStripeSource(taStripeSourceService.rechercherSource(sourceStripeID));
//			}
//			
////			//TODO mise en place ou mise à jour d'un timer ?
////			if(taStripePaiementPrevu.getTimerHandle()!=null) {
////				//un timer existe deja pour cette notification, on l'annule et on le recréer
////				TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taStripePaiementPrevu.getTimerHandle());
////				try {
////					Timer t = h.getTimer();
////					t.cancel();
////				} catch (NoSuchObjectLocalException ex) {
////					System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
////				}
////			}
////			taStripePaiementPrevu.setTimerHandle(SerializationUtils.serialize((Serializable) timerDeclenchePaiementPrevuService.creerTimer(taStripePaiementPrevu)));
//			taCronService.activerCronSysteme(TaCron.CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT, null);
////			
//			taStripePaiementPrevu= taStripePaiementPrevuService.merge(taStripePaiementPrevu);
////			mapperModelToUISubscription.map(taStripeSubscription,newTaStripeSubscriptionDTO);
//			
//			listePaiementPrevu = taStripePaiementPrevuService.rechercherPaiementPrevuCustomerDTO(taStripeCustomer.getIdExterne());
//			
//			modeEcranPaiementPrevu.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		} catch(Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Paiement prévu", e.getMessage()));
//		}
//	}
//	
//	public void actEnregistrerLigneAbonnement(ActionEvent actionEvent) {
////		mapperUIToModelSubscription.map(newTaStripeSubscriptionDTO, taStripeSubscription);
//	
//		try {
//			//selectedTaStripePlanDTO
////			TaStripeSubscriptionItemDTO taStripeSubscriptionItemDTO = new TaStripeSubscriptionItemDTO();
//			selectedTaStripeSubscriptionItemDTO.setId(0);
//			selectedTaStripeSubscriptionItemDTO.setIdStripePlan(selectedTaStripePlanDTO.getId());
//			selectedTaStripeSubscriptionItemDTO.setIdExternePlan(selectedTaStripePlanDTO.getIdExterne());
//			selectedTaStripeSubscriptionItemDTO.setNickname(selectedTaStripePlanDTO.getNickname());
//			selectedTaStripeSubscriptionItemDTO.setAmount(selectedTaStripePlanDTO.getAmount());
//			selectedTaStripeSubscriptionItemDTO.setInterval(selectedTaStripePlanDTO.getInterval());
//			selectedTaStripeSubscriptionItemDTO.setCodeArticle(selectedTaStripePlanDTO.getCodeArticle());
//			//selectedTaStripeSubscriptionItemDTO.setLibellecArticle(selectedTaStripePlanDTO.getCodeArticle());
////			selectedTaStripeSubscriptionItemDTO.setQuantity(1);
//			
//			TaStripeSubscriptionItem taStripeSubscriptionItem = new TaStripeSubscriptionItem();
//			taStripeSubscriptionItem.setTaStripeSubscription(taStripeSubscription);
//			taStripeSubscriptionItem.setTaPlan(taStripePlanService.findById(selectedTaStripePlanDTO.getId()));
////			taStripeSubscriptionItem.setQuantity(selectedTaStripePlanDTO.getq);
//			if(selectedTaStripeSubscriptionItemDTO.getQuantity()==null) {
//				selectedTaStripeSubscriptionItemDTO.setQuantity(1);
//			}
//			taStripeSubscriptionItem.setQuantity(selectedTaStripeSubscriptionItemDTO.getQuantity());
//			taStripeSubscriptionItem.setComplement1(selectedTaStripeSubscriptionItemDTO.getComplement1());
//			taStripeSubscriptionItem.setComplement2(selectedTaStripeSubscriptionItemDTO.getComplement2());
//			taStripeSubscriptionItem.setComplement3(selectedTaStripeSubscriptionItemDTO.getComplement3());
//			
//			if(listeSubscriptionItem==null) {
//				listeSubscriptionItem = new ArrayList<>();
//			}
//			listeSubscriptionItem.add(selectedTaStripeSubscriptionItemDTO);
//			
//			modeEcranSubscriptionItem.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subscription", e.getMessage()));
//		}
//	}
//	
//	public void actInsererAbonnement(ActionEvent actionEvent) {
//		selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//		taStripeSubscription = new TaStripeSubscription();
//		
//		listeSubscriptionItem = new ArrayList<>();
//		listeAvisEcheanceAbonnement = new ArrayList<>();
//		listeEcheanceNonEmiseAbonnement = new ArrayList<>();
//		
//		selectedTaStripeSubscriptionDTO.setDateDebut(new Date());
//		selectedTaStripeSubscriptionDTO.setDateFin(null);
//		
//		autoCompleteMapDTOtoUI();
//		
//		modeEcranSubscription.setMode(EnumModeObjet.C_MO_INSERTION);
//	}
//	
//	public void actInsererPaiementPrevu(ActionEvent actionEvent) {
//		selectedTaStripePaiementPrevuDTO = new TaStripePaiementPrevuDTO();
//		taStripePaiementPrevu = new TaStripePaiementPrevu();
//		
//		
////		autoCompleteMapDTOtoUI();
//		
//		modeEcranPaiementPrevu.setMode(EnumModeObjet.C_MO_INSERTION);
//	}
//	
//	public void actInsererLigneAbonnement(ActionEvent actionEvent) {
//		selectedTaStripeSubscriptionItemDTO = new TaStripeSubscriptionItemDTO();
////		taStripeSubscription = new TaStripeSubscription();
//		selectedTaStripeSubscriptionItemDTO.setQuantity(1);
//		
//		selectedTaStripeProductDTO = new TaStripeProductDTO();
//		selectedTaStripePlanDTO = new TaStripePlanDTO();
//		
//		modeEcranSubscriptionItem.setMode(EnumModeObjet.C_MO_INSERTION);
//	}
//	
//	public void actSupprimerLigneAbonnement(ActionEvent actionEvent) {
//		newTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
//		taStripeSubscription = new TaStripeSubscription();
//		
//		listeSubscriptionItem.remove(selectedTaStripeSubscriptionItemDTO);
//	}
//	
//	public void actSupprimerLigneAbonnement() {
//		actSupprimerLigneAbonnement(null);
//	}
//
//	public void actInserer(ActionEvent actionEvent) {
//		try {
//			selectedTaStripeCustomerDTO = new TaStripeCustomerDTO();
//			taStripeCustomer = new TaStripeCustomer();
//
//			mapperUIToModel.map(selectedTaStripeCustomerDTO, taStripeCustomer);
//			taStripeCustomer.setVersionObj(0);
//
//			autoCompleteMapDTOtoUI();
//			
//			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
//			scrollToTop();
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actInserer"));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void actModifier() {
//		actModifier(null);
//	}
//
//	public void actModifier(ActionEvent actionEvent) {
//		
//		try {
////			for (TaStripeCustomer adr : masterEntity.getTaStripeCustomers()) {
////				if(adr.getIdRefArticleFournisseur()==selectedTaStripeCustomerDTO.getId()) {
////					taStripeCustomer = adr;
////				}
////			}
//		
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//	
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actModifier"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void actModifierAbonnement(ActionEvent actionEvent) {
//		modeEcranSubscription.setMode(EnumModeObjet.C_MO_EDITION);
//	}
//	
//	public void actModifierPaiementPrevu(ActionEvent actionEvent) {
//		modeEcranPaiementPrevu.setMode(EnumModeObjet.C_MO_EDITION);
//	}
//	
//	public void actAide(ActionEvent actionEvent) {
//		
////		PrimeFaces.current().dialog().openDynamic("aide");
//    
//        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("draggable", false);
//        options.put("resizable", false);
//        options.put("contentHeight", 320);
//        Map<String,List<String>> params = null;
//        PrimeFaces.current().dialog().openDynamic("aide", options, params);
//		
//		if(ConstWeb.DEBUG) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAide"));
//		}
//	}
//	
////	public void actAideRetour(ActionEvent actionEvent) {
////		
////	}
//	   
//    public void nouveau(ActionEvent actionEvent) {  
//    	LgrTab b = new LgrTab();
//    	b.setTypeOnglet(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
//		b.setTitre("Tiers");
//		b.setStyle(LgrTab.CSS_CLASS_TAB_REF_ARTICLE_FOURNISSEUR);
//		b.setTemplate("article/ref_article_fournisseur_include.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_REF_ARTICLE_FOURNISSEUR);
//		b.setParamId("0");
//		
//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);
//		actInserer(actionEvent);
//		
//		if(ConstWeb.DEBUG) {
//    	FacesContext context = FacesContext.getCurrentInstance();  
//	    context.addMessage(null, new FacesMessage("Référence article fournisseur", 
//	    		"Nouveau"
//	    		)); }
//    } 
//    
//    public void supprimer(ActionEvent actionEvent) {
//    	actSupprimer(actionEvent);
//    }
//    
//    public void detail() {
//    	detail(null);
//    }
//    
//    public void detail(ActionEvent actionEvent) {
//    	onRowSelect(null);
//    }    
//	
//	public void actSupprimer() {
//		actSupprimer(null);
//	}
//	
//	public void actSupprimer(ActionEvent actionEvent) {
//		TaStripeCustomer taStripeCustomer = new TaStripeCustomer();
//		try {
//			if(selectedTaStripeCustomerDTO.getId()!=0){
//				taStripeCustomer = rechercheRefArticleFournisseurDansArticle(selectedTaStripeCustomerDTO);
//			}
//			
////			masterEntity.removeRefArticleFournisseur(taStripeCustomer);
//			masterEntity = taTiersService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
//			masterEntity = taTiersService.findById(masterEntity.getIdTiers());
//			selectedTaStripeCustomerDTO=null;
//			refresh();
//			
//
//			updateTab();
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actSupprimer"));
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référence article fournisseur", e.getCause().getCause().getCause().getCause().getMessage()));
//		} 	    
//	}
//	
//	public void actSupprimerAbonnement(ActionEvent actionEvent) {
//		paiementEnLigneDossierService.annulerSubscriptionStripe(compte, selectedTaStripeSubscriptionDTO.getIdExterne());
//		//TODO marquer comme supprimer dans base locale
//		paiementEnLigneDossierService.rechercheSubscriptionStripeForCustomer(compte, taStripeCustomer.getIdExterne(), null, "canceled");//mise à jour des données depuis stripe
//		
//		listeSubscription = taStripeSubscriptionService.rechercherSubscriptionCustomerDTO(taStripeCustomer.getIdExterne()); //recherche en local
//	}
//	
//	public void actPrepareSupprimerAbonnementNonStripe(ActionEvent actionEvent) {
//		Integer idSubscriptionNonStripe = (Integer) actionEvent.getComponent().getAttributes().get("idStripeSubscription");
//	}
//	
//	public void actSupprimerAbonnementNonStripe(ActionEvent actionEvent) {
//		String commentaireAnnulation = "";
//		boolean annuleDerniereEcheance = true;
//		taStripeSubscriptionService.annulerSubscriptionNonStripe(selectedTaStripeSubscriptionDTO.getId(), commentaireAnnulation, annuleDerniereEcheance);
//		listeSubscriptionNonStripe = taStripeSubscriptionService.rechercherSubscriptionNonStripeCustomerDTO(taStripeCustomer.getIdExterne()); //recherche en local
//	}
//	
//	public void actPrepareSupprimerPaiementPrevu(ActionEvent actionEvent) {
//		
//	}
//	
//	public void actSupprimerPaiementPrevu(ActionEvent actionEvent) {
//		try {
//			Integer idPaiementPrevu = (Integer) actionEvent.getComponent().getAttributes().get("idPaiementPrevu");
//			if(idPaiementPrevu!=null) {
//				taStripePaiementPrevu = taStripePaiementPrevuService.findById(idPaiementPrevu);
//				taStripePaiementPrevuService.remove(taStripePaiementPrevu);
//				
//				listePaiementPrevu = taStripePaiementPrevuService.rechercherPaiementPrevuCustomerDTO(taStripeCustomer.getIdExterne());
//				//TODO supprimer le timer associé ? 
//			}
//		
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void actFermer(ActionEvent actionEvent) {
//		
//		switch (modeEcran.getMode()) {
//		case C_MO_INSERTION:
//			actAnnuler(actionEvent);
//			break;
//		case C_MO_EDITION:
//			actAnnuler(actionEvent);							
//			break;
//
//		default:
//			break;
//		}
//		selectedTaStripeCustomerDTO=new TaStripeCustomerDTO();
////		selectedTaStripeCustomerDTOs=new TaStripeCustomerDTO[]{selectedTaStripeCustomerDTO};
//	
//	}
//
//	public void actImprimer(ActionEvent actionEvent) {
//		if(ConstWeb.DEBUG) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actImprimer"));
//		}
//		try {
//			
////		FacesContext facesContext = FacesContext.getCurrentInstance();
////		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
//		
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("tiers", taTiersService.findById(selectedTaStripeCustomerDTO.getId()));
//		
////			session.setAttribute("tiers", taTiersService.findById(selectedTaStripeCustomerDTO.getId()));
//			System.out.println("TiersController.actImprimer()");
//		} catch (FinderException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}    
//
//	public boolean pasDejaOuvert() {
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
//		return tabsCenter.ongletDejaOuvert(b)==null;
//	} 
//	
//	public void onRowSimpleSelect(SelectEvent event) {
//		
//		if(pasDejaOuvert()==false){
//			onRowSelect(event);
//			
//			autoCompleteMapDTOtoUI();
//
//		} else {
//			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
//		}
//	} 
//	public void updateTab(){
//		try {
//
//		autoCompleteMapDTOtoUI();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void onRowSelect(SelectEvent event) {  
//		
//		updateTab();
//		
//
//	} 
//	
//	public void onRowSelectAbonnement(SelectEvent event) { 
//		try {
//			if(selectedTaStripeSubscriptionDTO.getIdExterne()!=null) { //c'est un abonnement 'Stripe'
//				listeSubscriptionItem = taStripeSubscriptionItemService.rechercherSubscriptionItemDTO(selectedTaStripeSubscriptionDTO.getIdExterne());
//				System.out.println("AbonnementTiersController.onRowSelectAbonnement()");
//				
//				
//				//TODO remplir l'auto complete de la source
//				//TODO remplir l'auto complete du coupon
//				autoCompleteMapDTOtoUI();
//				
//				
//				//TODO remplir la taxe 
//				//TODO remplir le trial
//				//TODO remplir la liste d'invoice
//				paiementEnLigneDossierService.rechercheInvoiceStripeForSubscription(compte, selectedTaStripeSubscriptionDTO.getIdExterne(), null); //recherche et maj depuis stripe
//				listeInvoice = taStripeInvoiceService.rechercheInvoiceStripeForSubscriptionDTO(selectedTaStripeSubscriptionDTO.getIdExterne(), null); //recherche dans la bdd locale
//			
//			} else { //abonnement BDG
//				listeSubscriptionItem = taStripeSubscriptionItemService.rechercherSubscriptionItemDTO(selectedTaStripeSubscriptionDTO.getId());
//				//TODO remplir la liste d'avis d'échéance
//				listeAvisEcheanceAbonnement = taAvisEcheanceService.rechercheDocumentSubscription(selectedTaStripeSubscriptionDTO.getId());
//				
//				listeEcheanceNonEmiseAbonnement = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceSubscription(taStripeSubscriptionService.findById(selectedTaStripeSubscriptionDTO.getId()));
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void onRowSelectInvoiceAbonnement(SelectEvent event) { 
//		
//	}
//	
//	public void onRowSelectInvoiceCustomer(SelectEvent event) { 
//		
//	}
//	
//	public void onRowSelectPaiementCustomer(SelectEvent event) { 
//		
//	}
//	
//	public void onRowSelectPaiementPrevu(SelectEvent event) { 
//		
//	}
//	
//	public boolean editable() {
//		return !modeEcran.dataSetEnModif();
//	}
//	
//	public void actDialogTypeCompteBanque(ActionEvent actionEvent) {
//		
//  
//        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("draggable", false);
//        options.put("resizable", false);
//        options.put("contentHeight", 640);
//        options.put("modal", true);
//        
//
//        Map<String,List<String>> params = new HashMap<String,List<String>>();
//        List<String> list = new ArrayList<String>();
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//        params.put("modeEcranDefaut", list);
//        
//        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_banque", options, params);
// 	    
//	}
//	
//
//
//	
//	 public boolean etatBouton(String bouton) {
//		boolean retour = true;
//		switch (modeEcran.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//			case "enregistrer":
////				if(!selectedTaStripeCustomerDTO.estVide())
//					retour = true;
//				break;
//				case "annuler":
//				case "fermer":
//					retour = false;
//					break;
//				default:
//					break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "enregistrer":
////				if(!selectedTaStripeCustomerDTO.estVide())
//					retour = false;
//				break;
//			case "annuler":
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
//				if(selectedTaStripeCustomerDTO!=null && selectedTaStripeCustomerDTO.getId()!=0 ) retour = false;
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
//	public void validateCompteBanque(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//	
//		String messageComplet = "";
//		
//		try {
//		 
//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		  		  
////			//validation automatique via la JSR bean validation
//		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		  Set<ConstraintViolation<TaStripeCustomerDTO>> violations = factory.getValidator().validateValue(TaStripeCustomerDTO.class,nomChamp,value);
//			if (violations.size() > 0) {
//				messageComplet = "Erreur de validation : ";
//
//				for (ConstraintViolation<TaStripeCustomerDTO> cv : violations) {
//					messageComplet+=" "+cv.getMessage()+"\n";
//				}
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//
//			} else {
//				//aucune erreur de validation "automatique", on déclanche les validations du controller
//					 validateUIField(nomChamp,value);
//			}
//
//
//		} catch(Exception e) {
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//		}
//	}
//	
//
//	
//	public void autcompleteSelection(SelectEvent event) {
//		Object value = event.getObject();
//		
//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
//
//		if(value!=null) {
//			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//		}
//		
//		validateUIField(nomChamp,value);
//	}
//	
//	public boolean validateUIField(String nomChamp,Object value) {
//		
//		boolean changement=false;
//		
//		try {				
////			if(nomChamp.equals(Const.C_CODE_TIERS)) {
////				TaTiersDTO entity =null;
////				if(value!=null){
////					if(value instanceof TaTiersDTO){
////						selectedTaStripeCustomerDTO.setCodeFournisseur(((TaTiersDTO) value).getCodeTiers());
////					}
////				}else{
////					selectedTaStripeCustomerDTO.setCodeFournisseur(null);
////				}
////			}else if(nomChamp.equals(Const.C_CODE_BARRE)) {
////			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
////				if(modeDialogue) {
////					if(value!=null){
////						if(value instanceof TaTiersDTO){
////							masterEntity = taTiersService.findByCode(((TaTiersDTO) value).getCodeArticle());
////							selectedTaStripeCustomerDTO.setCodeArticle(((TaTiersDTO) value).getCodeArticle());
////							taStripeCustomer.setTaTiers(masterEntity);
////						}
////					}
////				}
////			}else if(nomChamp.equals(Const.C_Description)) {
////			}			
//
//			return false;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//
//	
//	public List<TaTiersDTO> fournisseurAutoCompleteLight(String query) {
////        List<TaTiersDTO> allValues = taFournisseurService.findAllLight();
//        List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
//         
////        for (int i = 0; i < allValues.size(); i++) {
////        	TaTiersDTO civ = allValues.get(i);
////            if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
////                filteredValues.add(civ);
////            }
////        }         
//        return filteredValues;
//    }
//	
//	public List<TaTiersDTO> articleAutoCompleteDTOLight(String query) {
//		List<TaTiersDTO> allValues = taTiersService.findByCodeLight(query.toUpperCase());
//		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaTiersDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeTiers().toUpperCase().contains(query.toUpperCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaStripeProduct> taStripeProductAutoComplete(String query) {
//        List<TaStripeProduct> allValues = taStripeProductService.selectAll();
//        List<TaStripeProduct> filteredValues = new ArrayList<TaStripeProduct>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripeProduct civ = allValues.get(i);
//            if(query.equals("*") || civ.getTaArticle().getLibellecArticle().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	public List<TaStripeProductDTO> taStripeProductAutoCompleteLight(String query) {
//        List<TaStripeProductDTO> allValues = taStripeProductService.selectAllDTO();
//        List<TaStripeProductDTO> filteredValues = new ArrayList<TaStripeProductDTO>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripeProductDTO civ = allValues.get(i);
//        	if(query.equals("*") || civ.getName().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	
//	public List<TaStripePlan> taStripePlanAutoComplete(String query) {
//        List<TaStripePlan> allValues = taStripePlanService.selectAll();
//        List<TaStripePlan> filteredValues = new ArrayList<TaStripePlan>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripePlan civ = allValues.get(i);
//            if(query.equals("*") || civ.getNickname().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	public List<TaStripePlanDTO> taStripePlanAutoCompleteLight(String query) {
//        List<TaStripePlanDTO> allValues = taStripePlanService.findByIdProductDTO(selectedTaStripeProductDTO.getId());
//        List<TaStripePlanDTO> filteredValues = new ArrayList<TaStripePlanDTO>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripePlanDTO civ = allValues.get(i);
//            if(query.equals("*") || civ.getNickname()==null || civ.getNickname().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	
//	public List<TaStripeCoupon> taStripeCouponAutoComplete(String query) {
//        List<TaStripeCoupon> allValues = taStripeCouponService.selectAll();
//        List<TaStripeCoupon> filteredValues = new ArrayList<TaStripeCoupon>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripeCoupon civ = allValues.get(i);
//            if(query.equals("*") || civ.getName().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	public List<TaStripeCouponDTO> taStripeCouponAutoCompleteLight(String query) {
//        List<TaStripeCouponDTO> allValues = taStripeCouponService.selectAllDTO();
//        List<TaStripeCouponDTO> filteredValues = new ArrayList<TaStripeCouponDTO>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripeCouponDTO civ = allValues.get(i);
//            if(query.equals("*") || civ.getName().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	
//	public List<TaStripeSource> taStripeSourceAutoComplete(String query) {
//        List<TaStripeSource> allValues = taStripeSourceService.selectAll();
//        List<TaStripeSource> filteredValues = new ArrayList<TaStripeSource>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripeSource civ = allValues.get(i);
//            if(query.equals("*") || civ.getIdExterne().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//         
//        return filteredValues;
//    }
//	public List<TaStripeSourceDTO> taStripeSourceAutoCompleteLight(String query) {
//        List<TaStripeSourceDTO> allValues = taStripeSourceService.selectAllDTO();
//        List<TaStripeSourceDTO> filteredValues = new ArrayList<TaStripeSourceDTO>();
//         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaStripeSourceDTO civ = allValues.get(i);
//            if(query.equals("*") || civ.getIdExterne().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }
//        TaStripeSourceDTO paiementCheque = new TaStripeSourceDTO();
//        paiementCheque.setIdExterne("Chèque");
//        filteredValues.add(paiementCheque);
//        
//        TaStripeSourceDTO paiementVirement = new TaStripeSourceDTO();
//        paiementVirement.setIdExterne("Virement");
//        filteredValues.add(paiementVirement);
//         
//        return filteredValues;
//    }
//	
//	
//	public TaStripeCustomerDTO[] getSelectedTaStripeCustomerDTOs() {
//		return selectedTaStripeCustomerDTOs;
//	}
//
//	public void setSelectedTaStripeCustomerDTOs(TaStripeCustomerDTO[] selectedTaStripeCustomerDTOs) {
//		this.selectedTaStripeCustomerDTOs = selectedTaStripeCustomerDTOs;
//	}
//
//	public TaStripeCustomerDTO getNewTaStripeCustomerDTO() {
//		return newTaStripeCustomerDTO;
//	}
//
//	public void setNewTaStripeCustomerDTO(TaStripeCustomerDTO newTaStripeCustomerDTO) {
//		this.newTaStripeCustomerDTO = newTaStripeCustomerDTO;
//	}
//
//	public TaStripeCustomerDTO getSelectedTaStripeCustomerDTO() {
//		return selectedTaStripeCustomerDTO;
//	}
//
//	public void setSelectedTaStripeCustomerDTO(TaStripeCustomerDTO selectedTaStripeCustomerDTO) {
//		this.selectedTaStripeCustomerDTO = selectedTaStripeCustomerDTO;
//	}
//
//	public TabListModelBean getTabsCenter() {
//		return tabsCenter;
//	}
//
//	public void setTabsCenter(TabListModelBean tabsCenter) {
//		this.tabsCenter = tabsCenter;
//	}
//
//	public String getParamId() {
//		return paramId;
//	}
//
//	public void setParamId(String paramId) {
//		this.paramId = paramId;
//	} 
//
//
//	public TabViewsBean getTabViews() {
//		return tabViews;
//	}
//
//	public void setTabViews(TabViewsBean tabViews) {
//		this.tabViews = tabViews;
//	}
//
//	public ModeObjetEcranServeur getModeEcran() {
//		return modeEcran;
//	}
//
//	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
//		this.modeEcran = modeEcran;
//	}
//	
//	public TaStripeCustomer getTaTiers() {
//		return taStripeCustomer;
//	}
//
//	public void setTaTiers(TaStripeCustomer taCompteBanque) {
//		this.taStripeCustomer = taCompteBanque;
//	}
//
//	public TabView getTabViewArticle() {
//		return tabViewArticle;
//	}
//
//	public void setTabViewArticle(TabView tabViewArticle) {
//		this.tabViewArticle = tabViewArticle;
//	}
//
//	public TaTiers getMasterEntity() {
//		return masterEntity;
//	}
//
//	public void setMasterEntity(TaTiers masterEntity) {
//		this.masterEntity = masterEntity;
//	}
//
//
//	public List<TaStripeCustomerDTO> getValuesFiltered() {
//		return valuesFiltered;
//	}
//
//	public void setValuesFiltered(List<TaStripeCustomerDTO> valuesFiltered) {
//		this.valuesFiltered = valuesFiltered;
//	}
//
//	public TaTiers getTaFournisseur() {
//		return taFournisseur;
//	}
//
//	public void setTaFournisseur(TaTiers taFournisseur) {
//		this.taFournisseur = taFournisseur;
//	}
//
//	public TaTiersDTO getTaFournisseurDTO() {
//		return taFournisseurDTO;
//	}
//
//	public void setTaFournisseurDTO(TaTiersDTO taFournisseurDTO) {
//		this.taFournisseurDTO = taFournisseurDTO;
//	}
//
//	public TaTiersDTO getTaTiersDTO() {
//		return taTiersDTO;
//	}
//
//	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
//		this.taTiersDTO = taTiersDTO;
//	}
//
//	public boolean isModeDialogue() {
//		return modeDialogue;
//	}
//
//	public void setModeDialogue(boolean modeDialogue) {
//		this.modeDialogue = modeDialogue;
//	}
//
//	public TaStripeSubscriptionDTO getSelectedTaStripeSubscriptionDTO() {
//		return selectedTaStripeSubscriptionDTO;
//	}
//
//	public void setSelectedTaStripeSubscriptionDTO(TaStripeSubscriptionDTO selectedTaStripeSubscriptionDTO) {
//		this.selectedTaStripeSubscriptionDTO = selectedTaStripeSubscriptionDTO;
//	}
//
//	public List<TaStripeSubscriptionDTO> getListeSubscription() {
//		return listeSubscription;
//	}
//
//	public void setListeSubscription(List<TaStripeSubscriptionDTO> listeSubscription) {
//		this.listeSubscription = listeSubscription;
//	}
//
//	public TaStripeSubscriptionDTO getNewTaStripeSubscriptionDTO() {
//		return newTaStripeSubscriptionDTO;
//	}
//
//	public void setNewTaStripeSubscriptionDTO(TaStripeSubscriptionDTO newTaStripeSubscriptionDTO) {
//		this.newTaStripeSubscriptionDTO = newTaStripeSubscriptionDTO;
//	}
//
//	public MoyenPaiementAbonnementDTOJSF getSelectedMoyenPaiementAbonnementDTOJSF() {
//		return selectedMoyenPaiementAbonnementDTOJSF;
//	}
//
//	public void setSelectedMoyenPaiementAbonnementDTOJSF(MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF) {
//		this.selectedMoyenPaiementAbonnementDTOJSF = selectedMoyenPaiementAbonnementDTOJSF;
//	}
//
//	public List<TaStripeSourceDTO> getListeSource() {
//		return listeSource;
//	}
//
//	public void setListeSource(List<TaStripeSourceDTO> listeSource) {
//		this.listeSource = listeSource;
//	}
//
//	public TaStripeCustomer getTaStripeCustomer() {
//		return taStripeCustomer;
//	}
//
//	public void setTaStripeCustomer(TaStripeCustomer taStripeCustomer) {
//		this.taStripeCustomer = taStripeCustomer;
//	}
//
//	public TaStripeSubscription getTaStripeSubscription() {
//		return taStripeSubscription;
//	}
//
//	public void setTaStripeSubscription(TaStripeSubscription taStripeSubscription) {
//		this.taStripeSubscription = taStripeSubscription;
//	}
//
//	public TaStripeSubscriptionItemDTO getSelectedTaStripeSubscriptionItemDTO() {
//		return selectedTaStripeSubscriptionItemDTO;
//	}
//
//	public void setSelectedTaStripeSubscriptionItemDTO(TaStripeSubscriptionItemDTO selectedTaStripeSubscriptionItemDTO) {
//		this.selectedTaStripeSubscriptionItemDTO = selectedTaStripeSubscriptionItemDTO;
//	}
//
//	public TaStripeSubscriptionItemDTO getNewTaStripeSubscriptionItemDTO() {
//		return newTaStripeSubscriptionItemDTO;
//	}
//
//	public void setNewTaStripeSubscriptionItemDTO(TaStripeSubscriptionItemDTO newTaStripeSubscriptionItemDTO) {
//		this.newTaStripeSubscriptionItemDTO = newTaStripeSubscriptionItemDTO;
//	}
//
//	public List<TaStripeSubscriptionItemDTO> getListeSubscriptionItem() {
//		return listeSubscriptionItem;
//	}
//
//	public void setListeSubscriptionItem(List<TaStripeSubscriptionItemDTO> listeSubscriptionItem) {
//		this.listeSubscriptionItem = listeSubscriptionItem;
//	}
//
//	public TaStripeSubscriptionItem getTaStripeSubscriptionItem() {
//		return taStripeSubscriptionItem;
//	}
//
//	public void setTaStripeSubscriptionItem(TaStripeSubscriptionItem taStripeSubscriptionItem) {
//		this.taStripeSubscriptionItem = taStripeSubscriptionItem;
//	}
//
//	public List<TaStripePlanDTO> getListePlan() {
//		return listePlan;
//	}
//
//	public void setListePlan(List<TaStripePlanDTO> listePlan) {
//		this.listePlan = listePlan;
//	}
//
//	public TaStripePlanDTO getSelectedTaStripePlanDTO() {
//		return selectedTaStripePlanDTO;
//	}
//
//	public void setSelectedTaStripePlanDTO(TaStripePlanDTO selectedTaStripePlanDTO) {
//		this.selectedTaStripePlanDTO = selectedTaStripePlanDTO;
//	}
//
//	public TaStripePlan getTaStripePlan() {
//		return taStripePlan;
//	}
//
//	public void setTaStripePlan(TaStripePlan taStripePlan) {
//		this.taStripePlan = taStripePlan;
//	}
//
//	public List<TaStripeProductDTO> getListeProduct() {
//		return listeProduct;
//	}
//
//	public void setListeProduct(List<TaStripeProductDTO> listeProduct) {
//		this.listeProduct = listeProduct;
//	}
//
//	public TaStripeProductDTO getSelectedTaStripeProductDTO() {
//		return selectedTaStripeProductDTO;
//	}
//
//	public void setSelectedTaStripeProductDTO(TaStripeProductDTO selectedTaStripeProductDTO) {
//		this.selectedTaStripeProductDTO = selectedTaStripeProductDTO;
//	}
//
//	public TaStripeProduct getTaStripeProduct() {
//		return taStripeProduct;
//	}
//
//	public void setTaStripeProduct(TaStripeProduct taStripeProduct) {
//		this.taStripeProduct = taStripeProduct;
//	}
//
//	public TaStripeSourceDTO getSelectedTaStripeSourceDefautDTO() {
//		return selectedTaStripeSourceDefautDTO;
//	}
//
//	public void setSelectedTaStripeSourceDefautDTO(TaStripeSourceDTO selectedTaStripeSourceDefautDTO) {
//		this.selectedTaStripeSourceDefautDTO = selectedTaStripeSourceDefautDTO;
//	}
//
//	public TaStripeSourceDTO getSelectedTaStripeSourceSubscriptionDTO() {
//		return selectedTaStripeSourceSubscriptionDTO;
//	}
//
//	public void setSelectedTaStripeSourceSubscriptionDTO(TaStripeSourceDTO selectedTaStripeSourceSubscriptionDTO) {
//		this.selectedTaStripeSourceSubscriptionDTO = selectedTaStripeSourceSubscriptionDTO;
//	}
//
//	public TaStripeCouponDTO getSelectedTaStripeCouponDTO() {
//		return selectedTaStripeCouponDTO;
//	}
//
//	public void setSelectedTaStripeCouponDTO(TaStripeCouponDTO selectedTaStripeCouponDTO) {
//		this.selectedTaStripeCouponDTO = selectedTaStripeCouponDTO;
//	}
//
//	public ModeObjetEcranServeur getModeEcranSubscription() {
//		return modeEcranSubscription;
//	}
//
//	public void setModeEcranSubscription(ModeObjetEcranServeur modeEcranSubscription) {
//		this.modeEcranSubscription = modeEcranSubscription;
//	}
//
//	public ModeObjetEcranServeur getModeEcranSubscriptionItem() {
//		return modeEcranSubscriptionItem;
//	}
//
//	public void setModeEcranSubscriptionItem(ModeObjetEcranServeur modeEcranSubscriptionItem) {
//		this.modeEcranSubscriptionItem = modeEcranSubscriptionItem;
//	}
//
//	public TaStripeInvoiceDTO getSelectedTaStripeInvoiceDTO() {
//		return selectedTaStripeInvoiceDTO;
//	}
//
//	public void setSelectedTaStripeInvoiceDTO(TaStripeInvoiceDTO selectedTaStripeInvoiceDTO) {
//		this.selectedTaStripeInvoiceDTO = selectedTaStripeInvoiceDTO;
//	}
//
//	public List<TaStripeInvoiceDTO> getListeInvoice() {
//		return listeInvoice;
//	}
//
//	public void setListeInvoice(List<TaStripeInvoiceDTO> listeInvoice) {
//		this.listeInvoice = listeInvoice;
//	}
//
//	public TaStripeInvoiceItemDTO getSelectedTaStripeInvoiceItemDTO() {
//		return selectedTaStripeInvoiceItemDTO;
//	}
//
//	public void setSelectedTaStripeInvoiceItemDTO(TaStripeInvoiceItemDTO selectedTaStripeInvoiceItemDTO) {
//		this.selectedTaStripeInvoiceItemDTO = selectedTaStripeInvoiceItemDTO;
//	}
//
//	public List<TaStripeInvoiceItemDTO> getListeInvoiceItem() {
//		return listeInvoiceItem;
//	}
//
//	public void setListeInvoiceItem(List<TaStripeInvoiceItemDTO> listeInvoiceItem) {
//		this.listeInvoiceItem = listeInvoiceItem;
//	}
//
//	public TaStripeInvoiceDTO getSelectedTaStripeInvoiceCustomerDTO() {
//		return selectedTaStripeInvoiceCustomerDTO;
//	}
//
//	public void setSelectedTaStripeInvoiceCustomerDTO(TaStripeInvoiceDTO selectedTaStripeInvoiceCustomerDTO) {
//		this.selectedTaStripeInvoiceCustomerDTO = selectedTaStripeInvoiceCustomerDTO;
//	}
//
//	public List<TaStripeInvoiceDTO> getListeInvoiceCustomer() {
//		return listeInvoiceCustomer;
//	}
//
//	public void setListeInvoiceCustomer(List<TaStripeInvoiceDTO> listeInvoiceCustomer) {
//		this.listeInvoiceCustomer = listeInvoiceCustomer;
//	}
//
//	public TaStripeChargeDTO getSelectedTaStripeChargeCustomerDTO() {
//		return selectedTaStripeChargeCustomerDTO;
//	}
//
//	public void setSelectedTaStripeChargeCustomerDTO(TaStripeChargeDTO selectedTaStripeChargeCustomerDTO) {
//		this.selectedTaStripeChargeCustomerDTO = selectedTaStripeChargeCustomerDTO;
//	}
//
//	public List<TaStripeChargeDTO> getListeChargeCustomer() {
//		return listeChargeCustomer;
//	}
//
//	public void setListeChargeCustomer(List<TaStripeChargeDTO> listeChargeCustomer) {
//		this.listeChargeCustomer = listeChargeCustomer;
//	}
//
//	public List<TaStripePaiementPrevuDTO> getListePaiementPrevu() {
//		return listePaiementPrevu;
//	}
//
//	public void setListePaiementPrevu(List<TaStripePaiementPrevuDTO> listePaiementPrevu) {
//		this.listePaiementPrevu = listePaiementPrevu;
//	}
//
//	public TaStripePaiementPrevuDTO getSelectedTaStripePaiementPrevuDTO() {
//		return selectedTaStripePaiementPrevuDTO;
//	}
//
//	public void setSelectedTaStripePaiementPrevuDTO(TaStripePaiementPrevuDTO selectedTaStripePaiementPrevuDTO) {
//		this.selectedTaStripePaiementPrevuDTO = selectedTaStripePaiementPrevuDTO;
//	}
//
//	public ModeObjetEcranServeur getModeEcranPaiementPrevu() {
//		return modeEcranPaiementPrevu;
//	}
//
//	public void setModeEcranPaiementPrevu(ModeObjetEcranServeur modeEcranPaiementPrevu) {
//		this.modeEcranPaiementPrevu = modeEcranPaiementPrevu;
//	}
//
//	public List<TaStripeSubscriptionDTO> getListeSubscriptionNonStripe() {
//		return listeSubscriptionNonStripe;
//	}
//
//	public void setListeSubscriptionNonStripe(List<TaStripeSubscriptionDTO> listeSubscriptionNonStripe) {
//		this.listeSubscriptionNonStripe = listeSubscriptionNonStripe;
//	}
//
//	public List<TaAvisEcheance> getListeAvisEcheance() {
//		return listeAvisEcheance;
//	}
//
//	public void setListeAvisEcheance(List<TaAvisEcheance> listeAvisEcheance) {
//		this.listeAvisEcheance = listeAvisEcheance;
//	}
//
//	public List<TaLEcheance> getListeLEcheance() {
//		return listeLEcheance;
//	}
//
//	public void setListeLEcheance(List<TaLEcheance> listeLEcheance) {
//		this.listeLEcheance = listeLEcheance;
//	}
//
//	public List<TaAvisEcheance> getListeAvisEcheanceAbonnement() {
//		return listeAvisEcheanceAbonnement;
//	}
//
//	public void setListeAvisEcheanceAbonnement(List<TaAvisEcheance> listeAvisEcheanceAbonnement) {
//		this.listeAvisEcheanceAbonnement = listeAvisEcheanceAbonnement;
//	}
//
//	public List<TaLEcheance> getListeEcheanceNonEmiseAbonnement() {
//		return listeEcheanceNonEmiseAbonnement;
//	}
//
//	public void setListeEcheanceNonEmiseAbonnement(List<TaLEcheance> listeEcheanceNonEmiseAbonnement) {
//		this.listeEcheanceNonEmiseAbonnement = listeEcheanceNonEmiseAbonnement;
//	}
//
//	public boolean isCreerAbonnementSurStripe() {
//		return creerAbonnementSurStripe;
//	}
//
//	public void setCreerAbonnementSurStripe(boolean creerAbonnementSurStripe) {
//		this.creerAbonnementSurStripe = creerAbonnementSurStripe;
//	}
//
//	public OuvertureDocumentBean getOuvertureDocumentBean() {
//		return ouvertureDocumentBean;
//	}
//
//	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
//		this.ouvertureDocumentBean = ouvertureDocumentBean;
//	}
//
//	public List<TaAbonnementFullDTO> getListeAllAbonnement() {
//		return listeAllAbonnement;
//	}
//
//	public void setListeAllAbonnement(List<TaAbonnementFullDTO> listeAllAbonnement) {
//		this.listeAllAbonnement = listeAllAbonnement;
//	}


}  
