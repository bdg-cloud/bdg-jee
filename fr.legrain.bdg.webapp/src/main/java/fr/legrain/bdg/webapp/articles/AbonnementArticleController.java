package fr.legrain.bdg.webapp.articles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.apache.derby.impl.sql.catalog.SYSROUTINEPERMSRowFactory;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.abonnement.service.remote.ITaFrequenceServiceRemote;
import fr.legrain.abonnement.service.remote.ITaJourRelanceServiceRemote;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class AbonnementArticleController extends AbstractController implements Serializable { 
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 7097097336521299066L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	

	
	private String paramId;
	
	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaStripeProductDTO> values; 
	private List<TaStripeProductDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranPlan = new ModeObjetEcranServeur();

	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaStripeProductServiceRemote taStripeProductService;
	private @EJB ITaStripePlanServiceRemote taStripePlanService;
	private @EJB ITaFrequenceServiceRemote taFrequenceService;
	private @EJB ITaJourRelanceServiceRemote taJourRelanceService;
	
	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;
	
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	

	private TaTiers taFournisseur;
	private TaTiersDTO taFournisseurDTO;
	private TaStripeProductDTO[] selectedTaStripeProductDTOs; 
	private TaStripeProduct taStripeProduct = new TaStripeProduct();
	private TaStripeProductDTO newTaStripeProductDTO = new TaStripeProductDTO();
	private TaStripeProductDTO selectedTaStripeProductDTO = new TaStripeProductDTO();
//	private TaStripeProductDTO oldSelectedTaStripeProductDTO = new TaStripeProductDTO();
//	private TaStripeSubscriptionDTO selectedTaStripeSubscriptionDTO = new TaStripeSubscriptionDTO();
	
	private TaStripePlanDTO selectedTaStripePlanDTO = new TaStripePlanDTO();
	private TaStripePlanDTO newTaStripePlanDTO = new TaStripePlanDTO();
	private List<TaStripePlanDTO> listePlan = new ArrayList<>();
	private TaStripePlan taStripePlan = new TaStripePlan();
	
	private TaJourRelanceDTO selectedTaJourRelanceDTO = new TaJourRelanceDTO();
	private TaJourRelanceDTO newTaJourRelanceDTO = new TaJourRelanceDTO();
	private List<TaJourRelanceDTO> listeJourRelance = new ArrayList<>();
	private TaJourRelance taJourRelance = new TaJourRelance();
	
//	private List<TaStripeSubscriptionDTO> listeSubscription = new ArrayList<>();
	
	
	private LgrDozerMapper<TaStripePlanDTO,TaStripePlan> mapperUIToModelPlan  = new LgrDozerMapper<>();
	private LgrDozerMapper<TaStripePlan,TaStripePlanDTO> mapperModelToUIPlan  = new LgrDozerMapper<>();
	
	private LgrDozerMapper<TaJourRelanceDTO,TaJourRelance> mapperUIToModelJourRelance  = new LgrDozerMapper<>();
	private LgrDozerMapper<TaJourRelance,TaJourRelanceDTO> mapperModelToUIJourRelance  = new LgrDozerMapper<>();
	
	private LgrDozerMapper<TaStripeProductDTO,TaStripeProduct> mapperUIToModel  = new LgrDozerMapper<TaStripeProductDTO,TaStripeProduct>();
	private LgrDozerMapper<TaStripeProduct,TaStripeProductDTO> mapperModelToUI  = new LgrDozerMapper<TaStripeProduct,TaStripeProductDTO>();
	
	private TaArticle masterEntity;
	private TaCompteServiceWebExterne compte = null;
	
	private List<TaFrequenceDTO> listeFrequenceDTO = new ArrayList<TaFrequenceDTO>();
	private TaFrequenceDTO selectedTaFrequenceDTO = new TaFrequenceDTO();
	private TaArticleDTO taArticleDTO;
	private boolean modeDialogue;
	
	public AbonnementArticleController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		listeFrequenceDTO = taFrequenceService.findAllLight();
		refresh(null);

	}
	public void refresh(){
		refresh(null);
	}

	
	public void refresh(ActionEvent ev){
		selectedTaStripeProductDTO=null;
		//selectedTaFrequenceDTO = null;
		
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if(params.get("modeEcranDefaut")!=null) {
			modeDialogue = true;
			String modeEcranDefaut = params.get("modeEcranDefaut");
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
				actInserer(null);
				
				String codeFrs = params.get("codeFrs");
				String codeBarre = params.get("codeBarre");
				
//				selectedTaStripeProductDTO.setCodeBarreFournisseur(codeBarre);
//				taStripeProduct.setCodeBarreFournisseur(codeBarre);
				
//				selectedTaStripeProductDTO.setCodeFournisseur(codeFrs);
//				try {
//					taStripeProduct.setTaFournisseur(taFournisseurService.findByCode(codeFrs));
//				} catch (FinderException e) {
//					e.printStackTrace();
//				}
			}
			
			
		} else {
			modeDialogue = false;
			
			
			if(masterEntity!=null) {
				if(masterEntity.getIdArticle() != 0) {
					try {
						masterEntity = taArticleService.findById(masterEntity.getIdArticle());
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listePlan = taStripePlanService.findByIdArticleDTO(masterEntity.getIdArticle());
					listeJourRelance = taJourRelanceService.findByIdArticleDTO(masterEntity.getIdArticle());
				}
				
			}

			compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
	   	    if((compte!=null) && (masterEntity!=null)) {
	   	    	try {
//					if(masterEntity.getIdArticle()!=0) masterEntity = taArticleService.findById(masterEntity.getIdArticle());
					
					
					
					taStripeProduct = taStripeProductService.rechercherProduct(masterEntity);
					if(taStripeProduct!=null) {
						selectedTaStripeProductDTO = taStripeProductService.findByIdDTO(taStripeProduct.getIdStripeProduct());
						
						//paiementEnLigneDossierService.recherchePlanStripeForProduct(compte,taStripeProduct.getIdExterne(), null, null);
						
//						taStripePlanService.f
					} else {
						taStripeProduct = new TaStripeProduct();
						selectedTaStripeProductDTO = new TaStripeProductDTO();
						//listePlan = new ArrayList<>();
					}
		   	    	
				} catch (FinderException e) {
					e.printStackTrace();
				}
	   	    	
//				values = new ArrayList<>();
//				for (TaStripeProduct refArticleFournisseur : masterEntity.getTaStripeProducts()) {
//					TaStripeProductDTO dto=new TaStripeProductDTO();
//					mapperModelToUI.map(refArticleFournisseur, dto);
//					dto.setId(refArticleFournisseur.getIdRefArticleFournisseur());
//					dto.setIdArticle(masterEntity.getIdArticle());
//					dto.setCodeArticle(masterEntity.getCodeArticle());
//					if(refArticleFournisseur.getTaFournisseur()!=null) {
//						dto.setIdFournisseur(refArticleFournisseur.getTaFournisseur().getIdTiers());
//						dto.setCodeFournisseur(refArticleFournisseur.getTaFournisseur().getCodeTiers());
//					}
//					taFournisseur=refArticleFournisseur.getTaFournisseur();
//					values.add(dto);
//				}
//	
//				if(!values.isEmpty() && selectedTaStripeProductDTO==null)
//					selectedTaStripeProductDTO=values.get(0);
			}
			autoCompleteMapDTOtoUI();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
	}

	public List<TaStripeProductDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				refresh();
				break;
			case C_MO_EDITION:
				if(selectedTaStripeProductDTO.getId()!=0){
					taStripeProduct=rechercheRefArticleFournisseurDansArticle(selectedTaStripeProductDTO);
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taStripeProduct, selectedTaStripeProductDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAnnuler"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taFournisseurDTO!=null) {
//			try {
//				taFournisseur= taFournisseurService.findById(taFournisseurDTO.getId());
//				
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	public void autoCompleteMapDTOtoUI() {
		taFournisseurDTO=null;
//		if(selectedTaStripeProductDTO!=null && selectedTaStripeProductDTO.getCodeFournisseur()!=null && !selectedTaStripeProductDTO.getCodeFournisseur().equals("")) {
//			List<TaTiersDTO> liste=taFournisseurService.findByCodeLight(selectedTaStripeProductDTO.getCodeFournisseur());
//			if(liste!=null && !liste.isEmpty()) {
//				taFournisseurDTO=liste.get(0);
//			}
//		}
	}

	public TaStripeProduct rechercheRefArticleFournisseurDansArticle(TaStripeProductDTO dto){
//		for (TaStripeProduct adr : masterEntity.getTaStripeProducts()) {
//			if(adr.getIdRefArticleFournisseur()==dto.getId()) {
//				return adr;
//			}
//		}
		return null;
	}
	

	

	public TaStripeProductDTO rechercheRefArticleFournisseurDansArticleDTO(TaStripeProductDTO dto){
//		for (TaStripeProductDTO ref : values) {
//			if(ref.getCodeFournisseur()!=null && ref.getCodeFournisseur().equals(dto.getCodeFournisseur()) &&
//					ref.getCodeArticleFournisseur()!=null && ref.getCodeArticleFournisseur().equals(dto.getCodeArticleFournisseur()) && 
//							ref.getCodeBarreFournisseur()!=null && ref.getCodeBarreFournisseur().equals(dto.getCodeBarreFournisseur())) {
//				return ref;
//			}
//		}
		return null;
	}
	

	
	public void actEnregistrer(ActionEvent actionEvent) {
		actEnregistrer();
	}
	
	public  void actEnregistrer() {		
		TaStripeProductDTO courant =selectedTaStripeProductDTO;
		autoCompleteMapUIToDTO();
//		if(taFournisseurDTO!=null) {
//			taStripeProduct.setTaFournisseur(taFournisseur);
//		}
		
		if(selectedTaStripeProductDTO!=null) {
			mapperUIToModel.map(selectedTaStripeProductDTO, taStripeProduct);
		}
		
		try {
			if(masterEntity!=null) {
//				masterEntity.addRefArticleFournisseur(taStripeProduct);
				taStripeProduct.setTaArticle(masterEntity);
				
//				masterEntity = taArticleService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
//				masterEntity = taArticleService.findById(masterEntity.getIdArticle());.
				
				taStripeProduct.setProductType("service");
				
				//String idProductStripe = paiementEnLigneDossierService.creerProductStripe(compte,taStripeProduct.getTaArticle().getLibellecArticle(),taStripeProduct.getProductType());
				//taStripeProduct.setIdExterne(idProductStripe);
//				TaStripeProductDTO taStripeProductRech = lgrStripe.rechercherProduct(idProductStripe);
				
				
				taStripeProduct = taStripeProductService.merge(taStripeProduct);
//				mapperModelToUI.map(taStripeProduct,selectedTaStripeProductDTO);
				selectedTaStripeProductDTO = taStripeProductService.findByIdDTO(taStripeProduct.getIdStripeProduct());
				
				if(modeDialogue) {
					PrimeFaces.current().dialog().closeDynamic(taStripeProduct);
				} else {
					refresh();
//					if(courant!=null) {
//						selectedTaStripeProductDTO=rechercheRefArticleFournisseurDansArticleDTO(courant);
//					}
					
					if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
						values.add(selectedTaStripeProductDTO);
					}
					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référence article fournisseur", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actEnregistrer"));
		}
	}
	
	public void actModifieDelaiSurvie() {
		actModifieDelaiSurvie(null);
	}
	public void actModifieDelaiSurvie(ActionEvent actionEvent) {
		System.out.println(masterEntity.getDelaiSurvie());
		masterEntity = taArticleService.merge(masterEntity);
	}
	
	public void actModifieDelaiGrace() {
		actModifieDelaiGrace(null);
	}
	public void actModifieDelaiGrace(ActionEvent actionEvent) {
		System.out.println(masterEntity.getDelaiGrace());
		masterEntity = taArticleService.merge(masterEntity);
	}
	
	public void actModifieCoefMultiplicateur() {
		actModifieCoefMultiplicateur(null);
	}
	public void actModifieCoefMultiplicateur(ActionEvent actionEvent) {
		System.out.println(masterEntity.getCoefMultiplicateur());
		masterEntity = taArticleService.merge(masterEntity);
	}
	
	public void actActiverArticleAbonnement() {
		actActiverArticleAbonnement(null);
	}
	public void actActiverArticleAbonnement(ActionEvent actionEvent) {
		System.out.println(masterEntity.getAbonnement());
		masterEntity = taArticleService.merge(masterEntity);
	}
	
	public void actModifieLiblCoefMultiplicateur() {
		actModifieLiblCoefMultiplicateur(null);
	}
	public void actModifieLiblCoefMultiplicateur(ActionEvent actionEvent) {
		System.out.println(masterEntity.getLiblCoefMultiplicateur());
		masterEntity = taArticleService.merge(masterEntity);
	}
	
	public void actEnregistrerJourRelance(ActionEvent actionEvent) {
		
		actEnregistrer();
		
		
		
		mapperUIToModelJourRelance.map(selectedTaJourRelanceDTO, taJourRelance);
	
		try {
			taJourRelance.setTaArticle(masterEntity);
			
			
			taJourRelanceService.merge(taJourRelance);
			mapperModelToUIJourRelance.map(taJourRelance,selectedTaJourRelanceDTO);
			
			listeJourRelance = taJourRelanceService.findByIdArticleDTO(masterEntity.getIdArticle());
			
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Jours de relance", e.getMessage()));
		}
	}
	
	public void actEnregistrerPlan(ActionEvent actionEvent) {
		
		actEnregistrer();
		
		
		
		mapperUIToModelPlan.map(selectedTaStripePlanDTO, taStripePlan);
	
		try {
			taStripePlan.setTaArticle(masterEntity);
			//taStripePlan.setTaStripeProduct(taStripeProduct);
			taStripePlan.setCurrency("eur");
			
			if(selectedTaFrequenceDTO != null) {
				TaFrequence frequence = taFrequenceService.findById(selectedTaFrequenceDTO.getId());
				if(frequence != null) {
					taStripePlan.setTaFrequence(frequence);
					taStripePlan.setInterval(frequence.getLiblFrequence());
				}
			}
			
//			String idPlanStripe = paiementEnLigneDossierService.creerPlanStripe(
//					compte,
//					taStripePlan.getNickname(),
//					taStripePlan.getInterval(),taStripePlan.getCurrency(),
//					//taStripePlan.getTaArticle().getTaPrix().getPrixttcPrix(),
//					taStripePlan.getAmount(),
//					taStripePlan.getTaStripeProduct().getIdExterne());
			
//			taStripePlan.setIdExterne(idPlanStripe);
			
			taStripePlan.setActive(true);
			
			taStripePlanService.merge(taStripePlan);
			mapperModelToUIPlan.map(taStripePlan,selectedTaStripePlanDTO);
			
			//listePlan = taStripePlanService.findByIdProductDTO(taStripeProduct.getIdStripeProduct());
			listePlan = taStripePlanService.findByIdArticleDTO(masterEntity.getIdArticle());
			
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Plan", e.getMessage()));
		}
	}
	
	public void actInsererJourRelance(ActionEvent actionEvent) {
		selectedTaJourRelanceDTO = new TaJourRelanceDTO();
		taJourRelance = new TaJourRelance();
		
		
		taJourRelance.setTaArticle(masterEntity);
		
		
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}
	
	public void actInsererPlan(ActionEvent actionEvent) {
		selectedTaStripePlanDTO = new TaStripePlanDTO();
		taStripePlan = new TaStripePlan();
		selectedTaFrequenceDTO = listeFrequenceDTO.get(0);
		
		
		taStripePlan.setTaArticle(masterEntity);
		//taStripePlan.setTaStripeProduct(taStripeProduct);
		
		newTaStripePlanDTO.setInterval(selectedTaFrequenceDTO.getLiblFrequence());
		
		newTaStripePlanDTO.setAmount(taStripePlan.getTaArticle().getTaPrix().getPrixttcPrix());
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaStripeProductDTO = new TaStripeProductDTO();
			taStripeProduct = new TaStripeProduct();

			mapperUIToModel.map(selectedTaStripeProductDTO, taStripeProduct);
			taStripeProduct.setVersionObj(0);

			autoCompleteMapDTOtoUI();
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actInserer"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		try {
//			for (TaStripeProduct adr : masterEntity.getTaStripeProducts()) {
//				if(adr.getIdRefArticleFournisseur()==selectedTaStripeProductDTO.getId()) {
//					taStripeProduct = adr;
//				}
//			}
		
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actModifier"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actModifierPlan(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void actModifierJourRelance(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void actAide(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        Map<String,List<String>> params = null;
        PrimeFaces.current().dialog().openDynamic("aide", options, params);
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAide"));
		}
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	   
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
		b.setTitre("Tiers");
		b.setStyle(LgrTab.CSS_CLASS_TAB_REF_ARTICLE_FOURNISSEUR);
		b.setTemplate("article/ref_article_fournisseur_include.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REF_ARTICLE_FOURNISSEUR);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Référence article fournisseur", 
	    		"Nouveau"
	    		)); }
    } 
    
    public void supprimer(ActionEvent actionEvent) {
    	actSupprimer(actionEvent);
    }
    
    public void detail() {
    	detail(null);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }    
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaStripeProduct taStripeProduct = new TaStripeProduct();
		try {
			if(selectedTaStripeProductDTO.getId()!=0){
				taStripeProduct = rechercheRefArticleFournisseurDansArticle(selectedTaStripeProductDTO);
			}
			
//			masterEntity.removeRefArticleFournisseur(taStripeProduct);
			masterEntity = taArticleService.merge(masterEntity,ITaTiersServiceRemote.validationContext);
			masterEntity = taArticleService.findById(masterEntity.getIdArticle());
			selectedTaStripeProductDTO=null;
			refresh();
			

			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Référence article fournisseur", e.getCause().getCause().getCause().getCause().getMessage()));
		} 	    
	}
	
	public void actSupprimerPlan(ActionEvent actionEvent) {
		try {
			paiementEnLigneDossierService.supprimerPlan(compte,selectedTaStripePlanDTO.getIdExterne());
			taStripePlanService.remove(taStripePlanService.findById(selectedTaStripePlanDTO.getId()));
			listePlan = taStripePlanService.findByIdArticleDTO(masterEntity.getIdArticle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actSupprimerJourRelance(ActionEvent actionEvent) {
		try {
			taJourRelanceService.remove(taJourRelanceService.findById(selectedTaJourRelanceDTO.getIdJourRelance()));
			listeJourRelance = taJourRelanceService.findByIdArticleDTO(masterEntity.getIdArticle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actFermer(ActionEvent actionEvent) {
		
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
		selectedTaStripeProductDTO=new TaStripeProductDTO();
//		selectedTaStripeProductDTOs=new TaStripeProductDTO[]{selectedTaStripeProductDTO};
	
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taArticleService.findById(selectedTaStripeProductDTO.getId()));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaStripeProductDTO.getId()));
			System.out.println("TiersController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();

		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_REF_FOURNISSEUR_ARTICLE);
		}
	} 
	public void updateTab(){
		try {

		autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		
		updateTab();
		

	} 
	
	public void onRowSelectPlan(SelectEvent event) { 
		//listeSubscription = taStripeSubscriptionService.rechercherSubscriptionPlanDTO(selectedTaStripePlanDTO.getIdExterne()); //recherche en local
		//listeSubscription = taAbonnementService.rechercherSubscriptionPlanDTO(selectedTaStripePlanDTO.getIdExterne()); //recherche en local
		try {
			selectedTaFrequenceDTO = taFrequenceService.findByIdDTO(selectedTaStripePlanDTO.getIdFrequence());
		} catch (FinderException e) {
			e.printStackTrace();
		} 
	}
	
	public void onRowSelectJourRelance(SelectEvent event) { 
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogTypeCompteBanque(ActionEvent actionEvent) {
		
  
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        

        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_banque", options, params);
 	    
	}
	


	
	 public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "enregistrer":
//				if(!selectedTaStripeProductDTO.estVide())
					retour = true;
				break;
				case "annuler":
				case "fermer":
					retour = false;
					break;
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "enregistrer":
//				if(!selectedTaStripeProductDTO.estVide())
					retour = false;
				break;
			case "annuler":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "inserer":
			case "fermer":
				retour = false;
				break;
			case "supprimerListe":retour = false;break;	
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(selectedTaStripeProductDTO!=null && selectedTaStripeProductDTO.getId()!=0 ) retour = false;
				break;				
			default:
				break;
		}
			break;
		default:
			break;
		}
		
		return retour;

	}

	public void validateCompteBanque(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {
		 
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaStripeProductDTO>> violations = factory.getValidator().validateValue(TaStripeProductDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaStripeProductDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));

			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
					 validateUIField(nomChamp,value);
			}


		} catch(Exception e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	

	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
//			if(nomChamp.equals(Const.C_CODE_TIERS)) {
//				TaTiersDTO entity =null;
//				if(value!=null){
//					if(value instanceof TaTiersDTO){
//						selectedTaStripeProductDTO.setCodeFournisseur(((TaTiersDTO) value).getCodeTiers());
//					}
//				}else{
//					selectedTaStripeProductDTO.setCodeFournisseur(null);
//				}
//			}else if(nomChamp.equals(Const.C_CODE_BARRE)) {
//			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
//				if(modeDialogue) {
//					if(value!=null){
//						if(value instanceof TaArticleDTO){
//							masterEntity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
//							selectedTaStripeProductDTO.setCodeArticle(((TaArticleDTO) value).getCodeArticle());
//							taStripeProduct.setTaArticle(masterEntity);
//						}
//					}
//				}
//			}else if(nomChamp.equals(Const.C_Description)) {
//			}			

			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
	public List<TaTiersDTO> fournisseurAutoCompleteLight(String query) {
//        List<TaTiersDTO> allValues = taFournisseurService.findAllLight();
        List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
         
//        for (int i = 0; i < allValues.size(); i++) {
//        	TaTiersDTO civ = allValues.get(i);
//            if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
//                filteredValues.add(civ);
//            }
//        }         
        return filteredValues;
    }
	
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	
	public TaStripeProductDTO[] getSelectedTaStripeProductDTOs() {
		return selectedTaStripeProductDTOs;
	}

	public void setSelectedTaStripeProductDTOs(TaStripeProductDTO[] selectedTaStripeProductDTOs) {
		this.selectedTaStripeProductDTOs = selectedTaStripeProductDTOs;
	}

	public TaStripeProductDTO getNewTaStripeProductDTO() {
		return newTaStripeProductDTO;
	}

	public void setNewTaStripeProductDTO(TaStripeProductDTO newTaStripeProductDTO) {
		this.newTaStripeProductDTO = newTaStripeProductDTO;
	}

	public TaStripeProductDTO getSelectedTaStripeProductDTO() {
		return selectedTaStripeProductDTO;
	}

	public void setSelectedTaStripeProductDTO(TaStripeProductDTO selectedTaStripeProductDTO) {
		this.selectedTaStripeProductDTO = selectedTaStripeProductDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	} 


	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}
	
	public TaStripeProduct getTaTiers() {
		return taStripeProduct;
	}

	public void setTaTiers(TaStripeProduct taCompteBanque) {
		this.taStripeProduct = taCompteBanque;
	}

	public TabView getTabViewArticle() {
		return tabViewArticle;
	}

	public void setTabViewArticle(TabView tabViewArticle) {
		this.tabViewArticle = tabViewArticle;
	}

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}


	public List<TaStripeProductDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaStripeProductDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}

	public TaTiers getTaFournisseur() {
		return taFournisseur;
	}

	public void setTaFournisseur(TaTiers taFournisseur) {
		this.taFournisseur = taFournisseur;
	}

	public TaTiersDTO getTaFournisseurDTO() {
		return taFournisseurDTO;
	}

	public void setTaFournisseurDTO(TaTiersDTO taFournisseurDTO) {
		this.taFournisseurDTO = taFournisseurDTO;
	}

	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}

	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	public boolean isModeDialogue() {
		return modeDialogue;
	}

	public void setModeDialogue(boolean modeDialogue) {
		this.modeDialogue = modeDialogue;
	}

	public TaStripePlanDTO getSelectedTaStripePlanDTO() {
		return selectedTaStripePlanDTO;
	}

	public void setSelectedTaStripePlanDTO(TaStripePlanDTO selectedTaStripePlanDTO) {
		this.selectedTaStripePlanDTO = selectedTaStripePlanDTO;
	}

	public List<TaStripePlanDTO> getListePlan() {
		return listePlan;
	}

	public void setListePlan(List<TaStripePlanDTO> listePlan) {
		this.listePlan = listePlan;
	}

	public TaStripePlanDTO getNewTaStripePlanDTO() {
		return newTaStripePlanDTO;
	}

	public void setNewTaStripePlanDTO(TaStripePlanDTO newTaStripePlanDTO) {
		this.newTaStripePlanDTO = newTaStripePlanDTO;
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

	public TaJourRelanceDTO getSelectedTaJourRelanceDTO() {
		return selectedTaJourRelanceDTO;
	}

	public TaJourRelanceDTO getNewTaJourRelanceDTO() {
		return newTaJourRelanceDTO;
	}

	public List<TaJourRelanceDTO> getListeJourRelance() {
		return listeJourRelance;
	}

	public TaJourRelance getTaJourRelance() {
		return taJourRelance;
	}

	public void setSelectedTaJourRelanceDTO(TaJourRelanceDTO selectedTaJourRelanceDTO) {
		this.selectedTaJourRelanceDTO = selectedTaJourRelanceDTO;
	}

	public void setNewTaJourRelanceDTO(TaJourRelanceDTO newTaJourRelanceDTO) {
		this.newTaJourRelanceDTO = newTaJourRelanceDTO;
	}

	public void setListeJourRelance(List<TaJourRelanceDTO> listeJourRelance) {
		this.listeJourRelance = listeJourRelance;
	}

	public void setTaJourRelance(TaJourRelance taJourRelance) {
		this.taJourRelance = taJourRelance;
	}

	public LgrDozerMapper<TaJourRelanceDTO,TaJourRelance> getMapperUIToModelJourRelance() {
		return mapperUIToModelJourRelance;
	}

	public void setMapperUIToModelJourRelance(LgrDozerMapper<TaJourRelanceDTO,TaJourRelance> mapperUIToModelJourRelance) {
		this.mapperUIToModelJourRelance = mapperUIToModelJourRelance;
	}

	public LgrDozerMapper<TaJourRelance,TaJourRelanceDTO> getMapperModelToUIJourRelance() {
		return mapperModelToUIJourRelance;
	}

	public void setMapperModelToUIJourRelance(LgrDozerMapper<TaJourRelance,TaJourRelanceDTO> mapperModelToUIJourRelance) {
		this.mapperModelToUIJourRelance = mapperModelToUIJourRelance;
	}


}  
